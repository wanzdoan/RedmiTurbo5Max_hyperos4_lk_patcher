"""Bounded native H3 root-of-trust handoff; no phone access or code patches."""
from pathlib import Path
import hashlib
import itertools
import json
import struct
import sys
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine, cstr, assert_abi, B, DATA, STOP
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v2'))
from validate_deep_fakelock import handoff_case as v2_case

INPUT = HERE / 'input/dash-fenrir-versioned.bin'
INPUT_SHA = 'dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113'


def container_end(image):
    off = 0
    for i in range(15):
        assert image[off:off + 4] == bytes.fromhex('88168858')
        size = struct.unpack_from('<I', image, off + 4)[0]
        off = (off + 512 + size + 15) & ~15
    assert off <= len(image)
    return off


def native_case(image, fenrir, boot_state, backing_state, backing_status=0, key_error=0, smc_error=0):
    # Exclude the declared trailing file padding only when mapping the container.
    u, saved = machine(image[:container_end(image)])
    versions = (0x100001, 0x202611, 0x12345, 0x6789a)
    key = bytes(range(256))
    digests = (0x1020304050607080, 0x1112131415161718, 0xaabbccddeeff0123, 0x7766554433221100)
    u.mem_write(B + 0x20d8d0, struct.pack('<IIII', *versions))
    u.mem_write(B + 0x20d8e0, struct.pack('<QQQQ', *digests))
    u.mem_write(B + 0x121990, struct.pack('<I', 0xa5a5a5a5))
    u.reg_write(UC_ARM64_REG_W0, boot_state)
    packets, backing_calls, hash_calls, logs = [], [], [], []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0, value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, address, size, _):
        off = address - B
        if off == 0x7de54:
            logs.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret()
        elif off == 0x88654:
            assert uc.reg_read(UC_ARM64_REG_W1) == 256
            if not key_error:
                uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), key)
            ret(key_error)
        elif off == 0x1c55c:
            ret(0)  # Native software SHA256 selected.
        elif off == 0xa4550:
            hash_calls.append(off)  # Let the actual SHA256 implementation execute.
        elif off == 0x88d60:
            backing_calls.append(off)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', backing_state))
            ret(backing_status)
        elif off == 0x1ea6c:
            packets.append([uc.reg_read(r) for r in (UC_ARM64_REG_X0, UC_ARM64_REG_X1,
                UC_ARM64_REG_X2, UC_ARM64_REG_X3, UC_ARM64_REG_X4, UC_ARM64_REG_X5,
                UC_ARM64_REG_X6, UC_ARM64_REG_X7)])
            output = struct.unpack('<Q', uc.mem_read(uc.reg_read(UC_ARM64_REG_SP), 8))[0]
            uc.mem_write(output, struct.pack('<QQQQ', smc_error, 0, 0, 0))
            ret()
        elif off == 0x6895c:
            uc.emu_stop()
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    # Run the actual AVB state setter, then the actual state-copy/handoff calls.
    u.emu_start(B + 0x692f4, STOP, count=10000)
    assert_abi(u, saved)
    selected_state = 0 if fenrir else boot_state
    assert struct.unpack('<I', u.mem_read(B + 0x121990, 4))[0] == selected_state
    u.emu_start(B + 0x68950, STOP, count=200000)
    assert u.reg_read(UC_ARM64_REG_PC) == B + 0x6895c
    assert struct.unpack('<I', u.mem_read(B + 0x20d900, 4))[0] == selected_state
    locked = 1 if fenrir else int(backing_state != 3)
    real_hash = hashlib.sha256(key if not key_error else bytes(256)).digest()
    words = list(struct.unpack('<QQQQ', real_hash)) if locked else [0] * 4
    expected = [
        [0xc2000120, *words, 0, 0, 0],
        [0xc2000120, locked, selected_state, *versions[:2], 0, 0, 0],
        [0xc2000120, *digests, 0, 0, 0],
        [0xc2000120, *versions[2:], 0, 0, 0, 0, 0],
        [0xc2000120, 0, 0, 0, 0, 0, 0, 0],
    ]
    assert packets == expected, (fenrir, boot_state, backing_state, packets, expected)
    assert len(backing_calls) == int(not fenrir)
    assert hash_calls
    assert all(u.reg_read(reg) == val for reg, val in saved.items())
    return dict(fenrir=fenrir, input_verified_boot_state=boot_state,
                backing_lock_state_fixture=backing_state, backing_status_fixture=backing_status,
                key_extraction_error=key_error, secure_sink_status=smc_error,
                global_verified_boot_state=selected_state, handed_off_verified_boot_state=selected_state,
                lock_boolean=locked, backing_query_called=bool(backing_calls),
                native_software_sha256_executed=True, all_five_packets_match=True,
                key_hash_hex=struct.pack('<QQQQ', *packets[0][1:5]).hex(),
                key_extraction_error_logged=any('fail to get public key' in s for s in logs),
                secure_sink_is_mock=True, pass_case=True)


def main():
    fenrir = INPUT.read_bytes()
    assert hashlib.sha256(fenrir).hexdigest() == INPUT_SHA
    stock = (ROOT / 'lk_hyperos3_original/lk.img').read_bytes()
    cases = []
    for patched, image in ((False, stock), (True, fenrir)):
        for boot, state, error, keyerr, smcerr in itertools.product((0, 1, 2, 3, 0xffffffff),
                                                                  (3, 4), (0, 7), (0, 9), (0, 5)):
            cases.append(native_case(image, patched, boot, state, error, keyerr, smcerr))
    v2 = (ROOT / 'experiments/lk_h3_eng_fakelock_v2/lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_DEEP_HANDOFF_TEST.img').read_bytes()
    v2_cases = [v2_case(v2, True, state, key_error=keyerr, smc_error=err)
                for state, keyerr, err in itertools.product((0, 1, 2, 3, 0xffffffff), (0, 9), (0, 5))]
    lookup = {(row['real_boot_state'], row['key_extraction_error'], row['secure_sink_return']): row
              for row in v2_cases}
    compared = 0
    for row in cases:
        if not row['fenrir']:
            continue
        v = lookup[(row['input_verified_boot_state'], row['key_extraction_error'], row['secure_sink_status'])]
        assert row['key_hash_hex'] == v['packet1_key_hash_hex']
        assert row['lock_boolean'] == v['packet2_lock'] == 1
        assert row['handed_off_verified_boot_state'] == v['packet2_boot_state'] == 0
        compared += 1
    report = dict(status='PASS_BOUNDED_NATIVE_HANDOFF_NOT_TEE_OR_ATTESTATION', input_sha256=INPUT_SHA,
                  h3_stock_and_fenrir_cases=len(cases), existing_v2_cases=len(v2_cases),
                  cross_checked_fenrir_v2_key_lock_boot_state_packets=compared,
                  fenrir_handoff_function_unchanged_from_stock=True,
                  observations=['Fenrir common-wrapper state4 naturally selects locked OEM-key hash and lock boolean1',
                                'Fenrir patched AVB setter zeroes the global; native boot call sequence copies zero into handoff state',
                                'V2 emits matching lock1/state0/OEM-key hash but preserves its internal boot-state global',
                                'H3 native handoff in this build does not inspect the mocked secure-sink status; its code was not changed by Fenrir'],
                  cases=cases, v2_cases=v2_cases,
                  limits=['Boot verification input/state, backing RPMB and key extraction are fixtures',
                          'Native setter, caller sequence, wrappers, software SHA256 and packet assembly executed',
                          'Secure-monitor/TEE are mocked, not actual hardware attestation',
                          'No global boot success, Integrity, OTA, preloader or Android application compatibility established'])
    (HERE / 'root_handoff_validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(report['status'], len(cases), 'H3/Fenrir and', len(v2_cases), 'v2 cases')


if __name__ == '__main__':
    main()

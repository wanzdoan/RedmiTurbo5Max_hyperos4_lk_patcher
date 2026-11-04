"""Offline native MTE policy replay. Never invokes ADB or writes firmware images.

The optional live preloader input is an EXISTING private readback kept outside Git.
Only non-device-specific policy inputs/results and hashes are exported.
"""
from pathlib import Path
import hashlib
import itertools
import json
import os
import struct
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine, cstr, B, DATA, STACK, STOP

LK = ROOT / 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img'
SUPPLIED = Path(r'C:\Users\Administrator\Downloads\preloader_dash.bin')
LIVE = Path(os.environ['LOCALAPPDATA']) / 'Temp/dash-relock-readonly-20261104_123518/sda_boot_lun.bin'
LK_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
LIVE_SHA = 'e987fa22c5ee5d03cf992581b268466e0f557f9e47e6d9580b7283a5e4bdd800'
SUPPLIED_SHA = 'bcde516c37a8845287cc57441780391e89c16c51eabfd4c5e3c625ecbdf4ad8b'
sha = lambda d: hashlib.sha256(d).hexdigest()
MMIO = 0xffff00000c2cf5f4


def lk_case(image, bits, reserved, forced=False, image_forced_off=False):
    u, saved = machine(image)
    if image_forced_off:
        assert bytes(u.mem_read(B+0x34d20, 4)) == bytes.fromhex('11000014')
    u.mem_map(MMIO & ~0xfff, 0x1000)
    u.mem_write(MMIO, struct.pack('<I', 0x12345678))
    u.mem_write(DATA, struct.pack('<III', 0, 0, bits))
    u.reg_write(UC_ARM64_REG_X0, DATA)
    messages = []
    appended = []
    lookups = []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64)-1))
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, _):
        off = addr-B
        if off == 0x7ea84:
            messages.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0)))
            ret()
        elif off == 0x19d88:
            name = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)); lookups.append(name)
            assert name == 'security_booker_tag'
            ret(DATA+0x100 if reserved else 0)
        elif off == 0x18ab8:
            appended.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret()
        elif off == 0x7ffe0:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2))
            assert fmt == 'kasan.page_alloc.sample=%u'
            text = fmt % uc.reg_read(UC_ARM64_REG_W3)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode()+b'\0')
            ret(len(text))
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr+4)

    u.hook_add(UC_HOOK_CODE, hook)
    # Actual boottag hook copies the input word to the LK global.
    u.emu_start(B+0x34ca8, STOP, count=10000)
    got = struct.unpack('<I', u.mem_read(B+0x16da28, 4))[0]
    assert got == bits
    if forced:
        # DESIGN PROBE ONLY: change one instruction in emulator RAM, not an image.
        assert bytes(u.mem_read(B+0x34d20, 4)) == bytes.fromhex('20020054')
        u.mem_write(B+0x34d20, bytes.fromhex('11000014'))
    u.reg_write(UC_ARM64_REG_LR, STOP)
    u.emu_start(B+0x34cd4, STOP, count=10000)
    suppressed = forced or image_forced_off or not reserved or not bits & 6
    expected = ['arm64.nomte'] if suppressed else (
        ['kasan.stacktrace=on kasan.stack_ring_size=524288'] +
        ([f'kasan.page_alloc.sample={1 << ((bits >> 8) & 15)}'] if bits & 0xf00 else [])
        if bits & 4 else ['kasan=off'])
    assert appended == expected, (bits, reserved, forced, appended, expected)
    assert lookups == ['security_booker_tag']
    mmio = struct.unpack('<I', u.mem_read(MMIO, 4))[0]
    assert mmio == (0x12345670 | (14 if suppressed else 15))
    assert struct.unpack('<I', u.mem_read(B+0x16da28, 4))[0] == bits
    assert all(u.reg_read(r) == v for r, v in saved.items())
    return {'mte_info_fixture': hex(bits), 'reserved_memory_present': reserved,
            'force_native_suppress_branch_in_emulator_ram': forced,
            'input_image_already_has_suppression_patch': image_forced_off,
            'outgoing_kernel_arguments': appended, 'booker_low_nibble': hex(mmio & 15),
            'mte_info_global_preserved': True, 'pass': True}


def preloader_case(raw, initial=0, misc_mode=0, read_error=False,
                   valid=True, dconfig=None):
    base = 0x1ffef00
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(0x1ffe000, 0x600000)
    u.mem_write(base, raw)
    u.mem_map(0x3000000, 0x10000)
    u.mem_map(0x4000000, 0x1000)
    sp = 0x3008000
    u.reg_write(UC_ARM64_REG_SP, sp)
    u.reg_write(UC_ARM64_REG_LR, 0x4000000)
    u.mem_write(0x211c74c, struct.pack('<I', initial))
    message = bytearray(64)
    if valid:
        message[0] = 1
        struct.pack_into('<II', message, 1, 0x5afefe5a, misc_mode)
    accesses = []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64)-1))
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, _):
        if addr == 0x208d704: ret()  # diagnostic only
        elif addr == 0x207d4a0:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), bytes(message))
            accesses.append('misc-read-boundary-fixture'); ret(-1 if read_error else 0)
        elif addr == 0x207d540:
            accesses.append('misc-write-boundary-captured-not-written'); ret()
        elif addr == 0x2006fa8:
            ret(0 if dconfig is None else 0x3001000)
        elif addr == 0x2093c7c: ret(dconfig)
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr+4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(base+0x7e6a0, 0x4000000, count=10000)
    value = struct.unpack('<I', u.mem_read(0x211c74c, 4))[0]
    expected = initial | 7
    if not read_error:
        if valid:
            if misc_mode & 3: expected |= 3
            if misc_mode & 12: expected |= 0x405
            if misc_mode & 16: expected = 0
        if dconfig is not None: expected = dconfig
    assert value == expected, (initial, misc_mode, read_error, valid, dconfig, value, expected)
    assert u.reg_read(UC_ARM64_REG_SP) == sp
    return {'initial_fixture': hex(initial), 'misc_mode_fixture': hex(misc_mode),
            'misc_read_error': read_error, 'misc_header_valid': valid,
            'dconfig_fixture': dconfig, 'final_mte_info': hex(value),
            'storage_boundary_calls': accesses, 'pass': True}


def main():
    image = LK.read_bytes()
    assert sha(image) == LK_SHA
    # Explicit endpoint values plus all low-three-bit policy combinations.
    values = list(range(8)) + [0x405, 0x407, 0xf07, 0xffffffff]
    cases = [lk_case(image, bits, reserved, forced)
             for bits, reserved, forced in itertools.product(values, (False, True), (False, True))]
    preloader = []
    if LIVE.exists():
        raw = LIVE.read_bytes(); assert sha(raw) == LIVE_SHA
        for initial, mode, valid, error, dc in itertools.product(
                (0, 0x80), (0, 1, 2, 4, 8, 16, 31), (False, True), (False, True), (None, 0, 3)):
            preloader.append(preloader_case(raw, initial, mode, error, valid, dc))
    assert not SUPPLIED.exists() or sha(SUPPLIED.read_bytes()) == SUPPLIED_SHA
    # Export policy-code disassembly only; no device provisioning material.
    c = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    asm = []
    for label, data, base, lo, hi in [
        ('ENG LK main payload offsets', image[512:], B, 0x34ca8, 0x34e08),
        ('existing captured preloader raw offsets', LIVE.read_bytes() if LIVE.exists() else b'',
         0x1ffef00, 0x7e6a0, 0x7e860)]:
        asm.append(label)
        asm += [f'{i.address-base:08x}: {i.bytes.hex()} {i.mnemonic} {i.op_str}'
                for i in c.disasm(data[lo:hi], base+lo)]
    (HERE/'policy_disassembly.txt').write_text('\n'.join(asm)+'\n', encoding='utf-8')
    report = {'status': 'PASS_OFFLINE_POLICY_ONLY_NO_FIRMWARE_PATCH_OR_DEVICE_ACCESS',
              'inputs': {'eng_lk': LK_SHA, 'supplied_old_eng_preloader': SUPPLIED_SHA,
                         'existing_live_boot_lun': LIVE_SHA},
              'lk_native_cases': len(cases), 'preloader_native_cases': len(preloader),
              'lk_cases': cases, 'preloader_cases': preloader,
              'candidate_design': {'payload_offset': '0x34d20', 'whole_file_offset': '0x34f20',
                                   'old': '20020054', 'new': '11000014',
                                   'effect': 'Always take existing native MTE suppression branch at0x34d64',
                                   'applied': 'Unicorn RAM only; no firmware output written'},
              'mocks': ['PAC', 'LK reserved-memory lookup', 'LK MMIO register is plain emulator RAM',
                        'LK cmdline append/log/snprintf boundaries', 'Preloader misc read/write boundaries',
                        'Preloader dconfig lookup/integer conversion boundaries'],
              'limits': ['No physical boot or downstream kernel execution',
                        'Does not undo earlier preloader tag-memory reservations or secure-world state',
                        'Exact phone kernel interpretation and userspace HWCAP2_MTE unmeasured',
                        'No causal link to earlier screen crashes established',
                        'Only these bounded policy functions run; no full boot chain or ROM compatibility proof']}
    (HERE/'validation.json').write_text(json.dumps(report, indent=2)+'\n', encoding='utf-8')
    print(json.dumps({k: report[k] for k in ('status','lk_native_cases','preloader_native_cases','candidate_design')}, indent=2))


if __name__ == '__main__':
    main()

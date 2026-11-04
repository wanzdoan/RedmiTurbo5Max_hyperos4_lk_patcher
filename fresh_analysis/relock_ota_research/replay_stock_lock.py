"""Read-only host replay of original lock getters and sentinel setters.

Never invokes ADB, fastboot, a firmware builder, RPMB hardware or efuse hardware.
Native writes are redirected to a fixture journal inside Unicorn only.
"""
from pathlib import Path
import hashlib
import json
import random
import struct
import sys
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

ROOT = Path(__file__).resolve().parents[2]
OUT = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members

B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000
MAGIC = b'Jz8PNRUF'
SAVED = [UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21,
         UC_ARM64_REG_X22, UC_ARM64_REG_X23, UC_ARM64_REG_X24,
         UC_ARM64_REG_X25, UC_ARM64_REG_X26, UC_ARM64_REG_X27,
         UC_ARM64_REG_X28, UC_ARM64_REG_X29]
PROFILES = {
    'h3': dict(getter=0x8982c, setter=0x89b80, log=0x7de54, magic=0x89e08,
               storage=0x34fe4, size=0x9685c, region=0x96880, read=0x89ef8,
               write=0x8a118, verify=0x8960c, sbc=0x4a0ec, descriptor=0x88d4c),
    'h4': dict(getter=0x89aec, setter=0x89e40, log=0x7e11c, magic=0x8a0c8,
               storage=0x3527c, size=0x96b1c, region=0x96b40, read=0x8a1b8,
               write=0x8a3d8, verify=0x898cc, sbc=0x4a384, descriptor=0x8900c),
    'eng': dict(getter=0x896e4, setter=0x89a38, log=0x7ea84, magic=0x89cc0,
                storage=0x3585c, size=0x9679c, region=0x967c0, read=0x89db0,
                write=0x89fd0, verify=0x894c4, sbc=0x4b114, descriptor=0x88c04),
}
TARGETS = [
    ('h3_stock', 'h3', 'lk_hyperos3_original/lk.img', 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb', False),
    ('h3_token_key_clone', 'h3', 'experiments/lk_h3_token_key_clone_v1/lk_hyperos3_STOCK_TOKEN_KEY_MATCH_TEST.img', '69089ebb26e070c202213b19615cad5dcd9202a0ea35e521e4dce18a25599a90', False),
    ('h3_forced_unlock', 'h3', 'lk_hyperos3_patched_unlockBL/dash.img', 'e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18', True),
    ('h4_stock', 'h4', 'lk_hyperos4_original/lk.img', '82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b', False),
    ('h4_forced_unlock', 'h4', 'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img', 'f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171', True),
    ('eng_stock', 'eng', 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img', '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d', False),
    ('eng_forced_unlock', 'eng', 'experiments/lk_h3_eng_unlock_v1/lk_hyperos3_ENG_UNLOCKBL_TEST.img', '0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466', True),
]


def replay(image, profile, config, setter_state=None):
    p = PROFILES[profile]
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(B, 0x400000)
    u.mem_write(B, image[512:512 + members(image)[0]['size']])
    u.mem_map(STACK, 0x10000)
    u.mem_map(DATA, 0x10000)
    u.mem_map(STOP, 0x1000)
    u.mem_write(DATA, b'\xcd' * 256)
    output = DATA + 0x80
    u.mem_write(output, struct.pack('<I', 0xdeadbeef))
    u.mem_write(DATA + 0x200, struct.pack('<QQ', DATA + 0x300, 10))
    u.mem_write(DATA + 0x300, b'do nothing\0')
    regs = {r: random.Random(r).getrandbits(64) for r in SAVED}
    for r, v in regs.items():
        u.reg_write(r, v)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    u.reg_write(UC_ARM64_REG_X0, output if setter_state is None else setter_state)
    journal, reads, calls = [], [], []
    size = config.get('rpmb_size', 0x400000)
    base_block = 0x3fe0 if size == 0x400000 else 0xffe0

    def ret(value):
        u.reg_write(UC_ARM64_REG_X0, value & 0xffffffffffffffff)
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, address, ins_size, unused):
        off = address - B
        if off == p['log']:
            ret(0)
        elif off == p['magic']:
            calls.append('magic_read_boundary')
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), MAGIC if config.get('magic_matches', True) else b'badmagic')
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X1), struct.pack('<I', 8))
            ret(config.get('magic_status', 0))
        elif off == p['storage']:
            ret(config.get('storage', 2))
        elif off == p['size']:
            ret(size)
        elif off == p['region']:
            ret(0x1000000)
        elif off == p['sbc']:
            ret(config.get('sbc', 14))
        elif off == p['descriptor']:
            calls.append('token_descriptor_boundary')
            ret(DATA + 0x200)
        elif off == p['read']:
            block = uc.reg_read(UC_ARM64_REG_W0)
            dest = uc.reg_read(UC_ARM64_REG_X1)
            length = uc.reg_read(UC_ARM64_REG_W2)
            reads.append({'block': hex(block), 'length': length})
            if block == base_block + 1:
                uc.mem_write(dest, struct.pack('<I', config.get('siglen', 256)))
                ret(config.get('length_read_status', 0))
            elif block == base_block + 2:
                if length > 256:
                    raise AssertionError('Native code requested an oversized signature')
                fixture = config.get('signature_fixture', 'T' * 256).encode()
                uc.mem_write(dest, fixture[:length].ljust(length, b'\0'))
                ret(config.get('signature_read_status', 0))
            else:
                raise AssertionError(f'Unexpected virtual RPMB block {block:x}')
        elif off == p['write']:
            block = uc.reg_read(UC_ARM64_REG_W0)
            src = uc.reg_read(UC_ARM64_REG_X1)
            length = uc.reg_read(UC_ARM64_REG_W2)
            if length > 256:
                raise AssertionError('Oversized virtual write')
            value = bytes(uc.mem_read(src, length))
            journal.append({'block': hex(block), 'length': length, 'fixture_bytes_hex': value.hex()})
            ret(config.get('write_status', 0))
        elif off == p['verify']:
            calls.append('token_verification_status_boundary')
            ret(config.get('verify_status', 0))
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + (p['getter'] if setter_state is None else p['setter']), STOP, count=100000)
    if u.reg_read(UC_ARM64_REG_PC) != STOP or u.reg_read(UC_ARM64_REG_SP) != STACK + 0xf000:
        raise AssertionError('Replay did not return or stack unbalanced')
    if any(u.reg_read(r) != v for r, v in regs.items()):
        raise AssertionError('Callee-saved register clobber')
    if bytes(u.mem_read(DATA, 0x80)) != b'\xcd' * 0x80 or bytes(u.mem_read(output + 4, 0x7c)) != b'\xcd' * 0x7c:
        raise AssertionError('Output guard changed')
    return {'return': u.reg_read(UC_ARM64_REG_W0),
            'state': struct.unpack('<I', u.mem_read(output, 4))[0] if setter_state is None else None,
            'virtual_reads': reads, 'virtual_write_attempts': journal, 'boundaries': calls}


def main():
    fixtures = [
        ('valid_token_status', {}, 3, False),
        ('invalid_token_status', {'verify_status': -1}, 4, False),
        ('no_signature', {'siglen': 0}, 4, False),
        ('lock_sentinel', {'siglen': 10, 'signature_fixture': 'do nothing', 'verify_status': -1}, 4, False),
        ('oversized_signature', {'siglen': 257}, 4, False),
        ('length_read_failure', {'length_read_status': 4}, 4, False),
        ('signature_read_failure', {'signature_read_status': 4}, 4, False),
        ('unsupported_storage', {'storage': 1}, 4, False),
        ('unsupported_rpmb_size', {'rpmb_size': 0x800000}, 4, False),
        ('missing_magic', {'magic_matches': False}, 4, True),
        ('magic_helper_3', {'magic_status': 3}, 4, True),
        ('magic_helper_4', {'magic_status': 4}, 4, False),
        ('unprovisioned_secure', {'magic_status': 6, 'sbc': 14}, 4, False),
        ('unprovisioned_nonsecure', {'magic_status': 6, 'sbc': 0}, 3, False),
        ('other_magic_error_valid_token', {'magic_status': 1}, 3, False),
        ('magic_initialization_write_failure', {'magic_matches': False, 'write_status': 4}, 4, True),
        ('sixteen_mib_rpmb', {'rpmb_size': 0x1000000}, 3, False),
    ]
    getters, setters, sources = [], [], []
    for name, profile, path, expected_sha, forced in TARGETS:
        image = (ROOT / path).read_bytes()
        if hashlib.sha256(image).hexdigest() != expected_sha:
            raise ValueError('Research input identity changed: ' + name)
        sources.append({'target': name, 'path': path, 'sha256': expected_sha, 'profile': profile})
        for label, config, expected, writes in fixtures:
            r = replay(image, profile, config)
            if r['return'] != 0 or r['state'] != (3 if forced else expected):
                raise AssertionError((name, label, r))
            if forced:
                if r['virtual_reads'] or r['virtual_write_attempts'] or r['boundaries']:
                    raise AssertionError('Forced getter unexpectedly reached hardware boundary')
            elif bool(r['virtual_write_attempts']) != writes:
                raise AssertionError(('Unexpected magic-write behavior', name, label, r))
            if writes and not forced:
                if r['virtual_write_attempts'][0]['fixture_bytes_hex'] != MAGIC.hex():
                    raise AssertionError('Unexpected magic initialization bytes')
            getters.append(dict(target=name, fixture=label, config=config, **r))
        if name not in ('h3_stock', 'h4_stock', 'eng_stock'):
            continue
        for state in (0, 1, 2, 3, 4, 5, 0xffffffff):
            r = replay(image, profile, {}, setter_state=state)
            if state in (3, 4):
                expected = [{'block': '0x3fe1', 'length': 4, 'fixture_bytes_hex': '0a000000'},
                            {'block': '0x3fe2', 'length': 10, 'fixture_bytes_hex': b'do nothing'.hex()}]
                if r['return'] != 0 or r['virtual_write_attempts'] != expected:
                    raise AssertionError(('Wrong sentinel persistence', name, state, r))
            elif r['return'] != 4 or r['virtual_write_attempts']:
                raise AssertionError(('Invalid requested state accepted', name, state, r))
            setters.append(dict(target=name, fixture='sentinel_descriptor', requested_state=state, **r))
        for config, status, count in [({'rpmb_size': 0x1000000}, 0, 2),
                                     ({'write_status': 4}, 4, 1),
                                     ({'magic_status': 6}, 6, 0),
                                     ({'storage': 1}, 5, 0)]:
            r = replay(image, profile, config, setter_state=4)
            if r['return'] != status or len(r['virtual_write_attempts']) != count:
                raise AssertionError(('Setter failure propagation changed', name, config, r))
            setters.append(dict(target=name, fixture='setter_boundary_control', config=config, **r))
    report = {'status': 'PASS_HOST_REPLAY_ONLY_NO_DEVICE_IO', 'sources': sources,
              'getter_cases': len(getters), 'setter_cases': len(setters),
              'total_cpu_cases': len(getters) + len(setters),
              'real_code': ['stock/ENG getter and setter control flow', 'memcmp/memcpy/memset',
                            'length checks and fail-state selection', 'stack/ABI'],
              'mocks': ['magic helper', 'storage kind/size/region', 'RPMB block read/write wrappers',
                        'token verification RESULT (not a real device token)', 'descriptor source',
                        'SBC read and logging', 'PAC instructions'],
              'no_device_io': True, 'no_firmware_output_created': True,
              'limits': ['Fixture status is not verification of any actual token/RPMB record',
                         'Virtual write attempts are observed calls, not hardware writes or success evidence',
                         'No complete lock command, wipe, secure-ID, AVB, preloader or full boot execution',
                         'Does not determine the cause of the reported brick or establish a safe relock procedure'],
              'getter_results': getters, 'setter_results': setters}
    (OUT / 'stock_lock_replay.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'getter_cases', 'setter_cases', 'total_cpu_cases', 'no_device_io')}, indent=2))


if __name__ == '__main__':
    main()

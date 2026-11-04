"""ENG LK display-only fakelock. Internal unlock/AVB policy is preserved.

Fastboot variable storage is deliberately unchanged: some command paths read it.
Only getvar replies and the vendor lock-status reply are formatted as locked.
"""
from pathlib import Path
import hashlib
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix

INPUT = ROOT / 'experiments/lk_h3_eng_unlock_v1/lk_hyperos3_ENG_UNLOCKBL_TEST.img'
INPUT_SHA = '0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466'
NAME = 'lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_TEST.img'
HELPER = 0xc790
FLASH_ARG = 0xc7e0


def sha(data):
    return hashlib.sha256(data).hexdigest()


def word(value):
    return struct.pack('<I', value)


def branch(at, target, link=False):
    delta = target - at
    if delta % 4 or not -(1 << 27) <= delta < (1 << 27):
        raise ValueError('Branch out of range')
    return word((0x94000000 if link else 0x14000000) | ((delta // 4) & 0x3ffffff))


def cbnz(at, target, reg=0, nonzero=True):
    delta = target - at
    if delta % 4 or not -(1 << 20) <= delta < (1 << 20):
        raise ValueError('Conditional branch out of range')
    return word((0x35000000 if nonzero else 0x34000000) | (((delta // 4) & 0x7ffff) << 5) | reg)


def adr(at, target, reg):
    delta = target - at
    if not -(1 << 20) <= delta < (1 << 20):
        raise ValueError('ADR out of range')
    imm = delta & 0x1fffff
    return word(0x10000000 | ((imm & 3) << 29) | ((imm >> 2) << 5) | reg)


def new_oem_region():
    # Replace the original 140-byte public lock-status function, not another command.
    # Compact status reply occupies32 bytes; helper80 bytes; literal28 bytes.
    oem = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(0xc778, 0xe159e, 0)
           + branch(0xc77c, 0x9cfc, True) + adr(0xc780, 0xd788e, 0)
           + bytes.fromhex('fd7bc1a8bf2303d5') + branch(0xc78c, 0x9dec))
    # Internal ABI: x20 points to the current getvar node. Preserve x20/all callee-saved
    # registers; return the public value in x0 AND x4, and original name in x3.
    h = HELPER
    helper = (bytes.fromhex('3f2303d5fd7bbfa9800640f9')
              + adr(h + 0xc, 0xee940, 1) + branch(h + 0x10, 0x7f990, True)
              + cbnz(h + 0x14, h + 0x30, nonzero=False)
              + bytes.fromhex('800640f9') + adr(h + 0x1c, 0xca63f, 1)
              + branch(h + 0x20, 0x7f990, True)
              + cbnz(h + 0x24, h + 0x38, nonzero=False)
              + bytes.fromhex('800a40f9') + branch(h + 0x2c, h + 0x3c)
              + adr(h + 0x30, 0xe9853, 0) + branch(h + 0x34, h + 0x3c)
              + adr(h + 0x38, 0xd78b3, 0)
              + bytes.fromhex('e40300aa830640f9fd7bc1a8bf2303d5c0035fd6'))
    literal = b'androidboot.flash.locked=1\0'
    if len(oem) != 32 or len(helper) != 80 or len(literal) != 27:
        raise ValueError('Unexpected compact function layout')
    return oem + helper + literal + b'\0'


def new_android_report():
    start = 0x69da4
    code = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(start + 8, 0xc9ce4, 0)
            + branch(start + 0xc, 0x18ab8, True)
            + cbnz(start + 0x10, start + 0x1c)
            + adr(start + 0x14, FLASH_ARG, 0) + branch(start + 0x18, 0x18ab8, True)
            + bytes.fromhex('fd7bc1a8bf2303d5c0035fd6'))
    return code + bytes.fromhex('1f2003d5') * 3


def patches():
    return [(0xc770, new_oem_region(), 'Vendor public lock reply plus getvar-only formatting helper/literal'),
            (0xb014, branch(0xb014, HELPER, True), 'Single getvar reply formatting'),
            (0xb040, branch(0xb040, HELPER, True), 'getvar all reply formatting'),
            (0xbc79c, bytes.fromhex('e2030aaa'), 'AVB cmdline value locked; callback/internal state untouched'),
            (0x69da4, new_android_report(), 'Android green/flash.locked bootargs; internal boot-state variable untouched')]


def build(source):
    if sha(source) != INPUT_SHA:
        raise ValueError('Expected exact prior ENG unlock artifact')
    rows = members(source)
    main_size = rows[0]['size']
    main = source[512:512 + main_size]
    anchors = {0xb014: '800a40f9', 0xb040: '839240a9', 0xbc79c: '4201899a',
               0xc770: '3f2303d5', 0x69da4: '3f2303d5'}
    for off, expected in anchors.items():
        if main[off:off + 4].hex() != expected:
            raise ValueError(f'Unexpected instruction at {off:x}')
    if len(new_oem_region()) != 0x8c or len(new_android_report()) != 0x34:
        raise ValueError('Patched functions exceed original function boundaries')
    result = bytearray(source)
    changed = []
    allowed = set()
    for off, code, purpose in patches():
        whole = off + 512
        before = source[whole:whole + len(code)]
        result[whole:whole + len(code)] = code
        allowed.update(range(whole, whole + len(code)))
        changed.append({'payload_offset': hex(off), 'whole_file_offset': hex(whole),
                        'bytes': len(code), 'original': before.hex(), 'replacement': code.hex(), 'purpose': purpose})
    cert = rows[2]
    c2 = cert['offset'] + 512
    expected_prefix = prefix(source[:512], source[512:512 + main_size])
    if cert['size'] != 1162 or source[c2:c2 + 180] != expected_prefix:
        raise ValueError('Unexpected existing ENG hash prefix')
    result[c2:c2 + 180] = prefix(bytes(result[:512]), bytes(result[512:512 + main_size]))
    allowed.update(range(c2, c2 + 180))
    if any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, result))):
        raise ValueError('Change outside allowed display/prefix ranges')
    for i, r in enumerate(rows):
        if i not in (0, 2) and source[r['offset']:r['end']] != result[r['offset']:r['end']]:
            raise ValueError('Non-main ENG member changed')
    if result[c2 + 180:cert['end']] != source[c2 + 180:cert['end']]:
        raise ValueError('Signed DER changed')
    if result[0x898e4:0x898f4] != bytes.fromhex('68008052080000b900008052c0035fd6'):
        raise ValueError('Internal force-unlocked getter changed')
    # Internal fastboot variable storage/getter/setter and init/mutation code remain exact.
    for lo, hi in [(0x99dc, 0x9c20), (0xc208, 0xc3b4), (0xb6588, 0xb66a8), (0x68bfc, 0x68f40)]:
        if source[512 + lo:512 + hi] != result[512 + lo:512 + hi]:
            raise ValueError('Internal fastboot capability/state code changed')
    return bytes(result), changed


def main():
    output, changes = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(output)
    report = {'status': 'EXPERIMENTAL_NOT_HARDWARE_TESTED', 'artifact': NAME,
              'input_sha256': INPUT_SHA, 'output_sha256': sha(output), 'output_bytes': len(output),
              'public_android': {'androidboot.vbmeta.device_state': 'locked',
                                 'androidboot.verifiedbootstate': 'green', 'androidboot.flash.locked': '1'},
              'public_fastboot': {'getvar unlocked': 'no', 'getvar secure': 'yes', 'vendor lks': '1',
                                  'getvar all': 'same formatting overrides'},
              'internal_unlocked_state': 3, 'internal_fastboot_variable_store_unchanged': True,
              'internal_verified_boot_state_unchanged': True, 'hardware_attestation_forged': False,
              'all_other_eng_members_unchanged': True, 'signed_der_unchanged': True,
              'patches': changes, 'device_flashed': False,
              'limits': ['Display-only values do not relock or change actual verification/TEE/efuse state',
                         'No guarantee for hardware attestation, Play Integrity or app acceptance',
                         'Host flashing tools may independently refuse when told unlocked=no',
                         'Full flash/erase commands and Android property import need physical runtime verification',
                         'No new hardware boot validation; earlier-stage compatibility limitations still apply']}
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('artifact', 'output_sha256', 'output_bytes', 'status')}, indent=2))


if __name__ == '__main__':
    main()

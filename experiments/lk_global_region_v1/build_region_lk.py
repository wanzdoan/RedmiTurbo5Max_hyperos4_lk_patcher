"""Build a separate LK-only ROM-region experiment; never flashes or enables NR."""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

BASE_PATH = ROOT / 'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
BASE_SHA = 'f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
NAME = 'lk_h4_unlock_CN_GLOBAL_REGION_TEST.img'


def branch(at, target, link=False):
    delta = target - at
    if delta % 4 or not -(1 << 27) <= delta < (1 << 27):
        raise ValueError('Invalid ARM64 branch')
    return struct.pack('<I', (0x94000000 if link else 0x14000000) | ((delta // 4) & 0x3ffffff))


def adr(at, target, reg):
    delta = target - at
    if not -(1 << 20) <= delta < (1 << 20):
        raise ValueError('ADR out of range')
    imm = delta & 0x1fffff
    return struct.pack('<I', 0x10000000 | ((imm & 3) << 29) | ((imm >> 2) << 5) | reg)


def patches():
    # This block is entered only after the primary hardware-region key mismatches.
    # Keep certificate verification and key extraction, and compare the embedded GL key.
    fallback = (bytes.fromhex('e0030491') + adr(0x888d4, 0x1122d7, 1)
                + bytes.fromhex('02208052') + branch(0x888dc, 0x7eae8, True)
                + bytes.fromhex('1f000071f4039f5a') + branch(0x888e8, 0x88854))
    return [
        (0x6904c, bytes.fromhex('40110035'), branch(0x6904c, 0x6905c), 'CN software-SKU rejection'),
        (0x888d0, bytes.fromhex('e00200d0000c369111d6ff97a87340391f010071f4139f5adbffff17'),
         fallback, 'CN image key accepts the embedded GL key as an alternate'),
        (0xc5a50, bytes.fromhex('61020054'), branch(0xc5a50, 0xc5a9c),
         'AVB key trust is the union of existing CN and GL keys'),
    ]


def build(source):
    if sha(source) != BASE_SHA:
        raise ValueError('Input is not the exact known-working LK')
    ms = members(source)
    result = bytearray(source)
    metadata = []
    for offset, old, new, purpose in patches():
        absolute = offset + 512
        if len(old) != len(new) or source[absolute:absolute + len(old)] != old:
            raise ValueError(f'Unexpected instructions at {absolute:x}')
        result[absolute:absolute + len(new)] = new
        metadata.append({'payload_offset': hex(offset), 'whole_file_offset': hex(absolute),
                         'original': old.hex(), 'replacement': new.hex(), 'purpose': purpose})
    c2 = ms[2]['offset'] + 512
    if source[c2:c2 + 180] != prefix(source[:512], source[512:512 + ms[0]['size']]):
        raise ValueError('Unexpected existing certificate prefix')
    result[c2:c2 + 180] = prefix(bytes(result[:512]), bytes(result[512:512 + ms[0]['size']]))
    for i, member in enumerate(ms):
        if i not in (0, 2) and result[member['offset']:member['end']] != source[member['offset']:member['end']]:
            raise ValueError('Unexpected change outside main/prefix')
    if result[c2 + 180:ms[2]['end']] != source[c2 + 180:ms[2]['end']]:
        raise ValueError('Signed DER/padding changed')
    if result[0x89cec:0x89cfc] != bytes.fromhex('68008052080000b900008052c0035fd6'):
        raise ValueError('Unlock patch changed')
    return bytes(result), metadata


def main():
    source = BASE_PATH.read_bytes()
    image, changes = build(source)
    target = HERE / NAME
    target.write_bytes(image)
    report = {'status': 'EXPERIMENTAL_NOT_DEVICE_TESTED', 'artifact': NAME,
              'input_sha256': BASE_SHA, 'output_sha256': sha(image), 'size': len(image),
              'lk_only': True, 'is_5g_fix': False, 'nr_registration_verified': False,
              'previous_unlock_preserved': True, 'patches': changes,
              'main_cert_prefix_recomputed': True, 'retained_signed_der_unchanged': True,
              'all_other_members_byte_identical': True,
              'limits': ['No target Global ROM supplied; model/layout/firmware compatibility unknown',
                         'CN hardware region experiment; non-CN unknown image-key mismatch becomes stricter',
                         'No efuse writes added; original EP/production logic remains unchanged',
                         'No modem, radio property, Android or persistent data changes',
                         'No hardware boot or NR test; original H3 signed BL2 remains required']}
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report, indent=2))


if __name__ == '__main__':
    main()

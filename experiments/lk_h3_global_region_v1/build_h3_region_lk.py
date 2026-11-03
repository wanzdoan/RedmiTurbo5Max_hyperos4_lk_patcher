"""Add ROM-region patches to the exact supplied patched H3 LK. Never flashes."""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

BASE_PATH = ROOT / 'lk_hyperos3_patched_unlockBL/dash.img'
BASE_SHA = 'e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18'
NAME = 'lk_hyperos3_patched_unlockBL_CN_GLOBAL_REGION_TEST.img'


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
    fallback = (bytes.fromhex('e0030491') + adr(0x88614, 0x1120af, 1)
                + bytes.fromhex('02208052') + branch(0x8861c, 0x7e820, True)
                + bytes.fromhex('1f000071f4039f5a') + branch(0x88628, 0x88594))
    return [
        (0x68d84, bytes.fromhex('40110035'), branch(0x68d84, 0x68d94), 'CN software-SKU rejection'),
        (0x88610, bytes.fromhex('e00200d000a030910fd6ff97a87340391f010071f4139f5adbffff17'),
         fallback, 'CN image key accepts the embedded H3 GL key as an alternate'),
        (0xc53dc, bytes.fromhex('61020054'), branch(0xc53dc, 0xc5428),
         'AVB key trust is the union of existing H3 CN and GL keys'),
    ]


def build(source):
    if sha(source) != BASE_SHA:
        raise ValueError('Input is not the exact supplied patched H3 dash.img')
    ms = members(source)
    result = bytearray(source)
    metadata = []
    for offset, old, new, purpose in patches():
        absolute = offset + 512
        if len(old) != len(new) or source[absolute:absolute + len(old)] != old:
            raise ValueError(f'Unexpected H3 instructions at {absolute:x}')
        result[absolute:absolute + len(new)] = new
        metadata.append({'payload_offset': hex(offset), 'whole_file_offset': hex(absolute),
                         'original': old.hex(), 'replacement': new.hex(), 'purpose': purpose})
    c2 = ms[2]['offset'] + 512
    if source[c2:c2 + 180] != prefix(source[:512], source[512:512 + ms[0]['size']]):
        raise ValueError('Unexpected existing H3 certificate prefix')
    result[c2:c2 + 180] = prefix(bytes(result[:512]), bytes(result[512:512 + ms[0]['size']]))
    for i, member in enumerate(ms):
        if i not in (0, 2) and result[member['offset']:member['end']] != source[member['offset']:member['end']]:
            raise ValueError('Unexpected change outside H3 main/prefix')
    if result[c2 + 180:ms[2]['end']] != source[c2 + 180:ms[2]['end']]:
        raise ValueError('H3 signed DER/padding changed')
    if result[0x89a2c:0x89a3c] != bytes.fromhex('68008052080000b900008052c0035fd6'):
        raise ValueError('Existing H3 unlock patch changed')
    # Fail closed if any unlisted byte is changed, including earlier user patches.
    allowed = set(range(c2, c2 + 180))
    for offset, old, _, _ in patches():
        allowed.update(range(offset + 512, offset + 512 + len(old)))
    if any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, result))):
        raise ValueError('Changed bytes outside documented ranges')
    return bytes(result), metadata


def main():
    source = BASE_PATH.read_bytes()
    image, changes = build(source)
    (HERE / NAME).write_bytes(image)
    report = {'status': 'EXPERIMENTAL_NOT_DEVICE_TESTED', 'artifact': NAME,
              'input': 'lk_hyperos3_patched_unlockBL/dash.img', 'input_sha256': BASE_SHA,
              'output_sha256': sha(image), 'size': len(image), 'lk_only': True,
              'all_components_from_supplied_patched_h3': True, 'h4_components_transplanted': False,
              'is_5g_fix': False, 'previous_unlock_and_other_patches_preserved': True,
              'patches': changes, 'main_cert_prefix_recomputed': True,
              'all_retained_signed_der_unchanged': True, 'all_other_members_byte_identical': True,
              'limits': ['No device boot or target Global ROM test',
                         'CN dash experiment; non-CN image-key mismatch path becomes stricter',
                         'SKU region filter removed, including for empty/unrecognized SKU',
                         'Earlier-stage authentication, antirollback, model/layout compatibility remain separate',
                         'No efuse writes added; original EP/production logic unchanged',
                         'No modem, preloader, Android, radio-setting or device changes; not a proved 5G fix']}
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('artifact', 'input_sha256', 'output_sha256', 'size', 'status')}, indent=2))


if __name__ == '__main__':
    main()

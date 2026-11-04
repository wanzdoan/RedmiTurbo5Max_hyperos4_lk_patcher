"""Unlock getter patch for the exact supplied H3 ENG LK; no flashing."""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha, align

INPUT = HERE / 'input/lk_eng_original.img'
INPUT_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
NAME = 'lk_hyperos3_ENG_UNLOCKBL_TEST.img'
OFFSET = 0x898e4
OLD = bytes.fromhex('3f2303d5ff4305d1fd7b12a9fc5713a9')
PATCH = bytes.fromhex('68008052080000b900008052c0035fd6')


def build(source):
    if sha(source) != INPUT_SHA:
        raise ValueError('Refusing input other than the preserved H3 ENG LK')
    before = members(source)
    if source[OFFSET:OFFSET + 16] != OLD:
        raise ValueError('Unexpected ENG lock getter')
    main, cert = before[0], before[2]
    if main['name'] != 'lk' or cert['name'] != 'cert2' or cert['size'] != 982:
        raise ValueError('Unexpected ENG member layout')
    changed = bytearray(source)
    changed[OFFSET:OFFSET + 16] = PATCH
    pref = prefix(bytes(changed[:512]), bytes(changed[512:512 + main['size']]))
    header = bytearray(source[cert['offset']:cert['offset'] + 512])
    struct.pack_into('<I', header, 4, cert['size'] + len(pref))
    signed = source[cert['offset'] + 512:cert['offset'] + 512 + cert['size']]
    record = bytes(header) + pref + signed
    record += bytes(align(len(record)) - len(record))
    image = bytes(changed[:cert['offset']]) + record + source[cert['end']:]
    after = members(image)
    for index, (a, b) in enumerate(zip(before, after)):
        if index not in (0, 2) and source[a['offset']:a['end']] != image[b['offset']:b['end']]:
            raise ValueError(f'ENG member {index} changed')
    if image[512:OFFSET] != source[512:OFFSET] or image[OFFSET + 16:main['end']] != source[OFFSET + 16:main['end']]:
        raise ValueError('Unexpected additional main code change')
    if image[after[2]['offset'] + 692:after[2]['offset'] + 692 + len(signed)] != signed:
        raise ValueError('Signed certificate DER changed')
    return image, {'input_sha256': INPUT_SHA, 'output_sha256': sha(image),
                   'input_size': len(source), 'output_size': len(image),
                   'unlock_whole_file_offset': hex(OFFSET), 'unlock_payload_offset': hex(OFFSET - 512),
                   'original': OLD.hex(), 'replacement': PATCH.hex(),
                   'main_cert2_prefix_bytes': len(pref), 'all_other_eng_records_excluding_main_cert2_byte_identical': True,
                   'all_components_from_input_eng': True, 'other_build_components_transplanted': False,
                   'region_or_radio_patch_added': False, 'device_flashed': False,
                   'status': 'EXPERIMENTAL_NOT_HARDWARE_TESTED',
                   'limits': ['Forces runtime lock getter to state3/success0; does not write permanent RPMB unlock',
                              'Retains native ENG BL2; prefix parser behavior tested separately',
                              'Original DER signature retained, not re-signed with a private key',
                              'No full boot, preloader compatibility, rollback-fuse or real-device validation']}


def main():
    image, report = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(image)
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('input_sha256', 'output_sha256', 'output_size', 'status')}, indent=2))


if __name__ == '__main__':
    main()

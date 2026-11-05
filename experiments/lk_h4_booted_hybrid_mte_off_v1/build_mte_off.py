"""Only add MTE suppression to the exact user-confirmed H4 hybrid."""
from pathlib import Path
import json
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

INPUT = ROOT / 'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
INPUT_SHA = 'f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
NAME = 'lk_hyperos4_BL33_H3BL2_MTE_OFF_TEST.img'
OFFSET = 0x34740
OLD = bytes.fromhex('20020054')
NEW = bytes.fromhex('11000014')


def build(source):
    if sha(source) != INPUT_SHA: raise ValueError('Expected exact boot-confirmed f57 H4 hybrid')
    rows = members(source); at = 512 + OFFSET
    if source[at:at + 4] != OLD: raise ValueError('H4 MTE conditional branch differs')
    if source[512 + 0xdf9c3:512 + 0xdf9c3 + 12] != b'arm64.nomte\0':
        raise ValueError('H4 native MTE suppression literal differs')
    if source[0x89cec:0x89cfc] != bytes.fromhex('68008052080000b900008052c0035fd6'):
        raise ValueError('Existing H4 unlock patch differs')
    out = bytearray(source); out[at:at + 4] = NEW
    cert = rows[2]; c = cert['offset'] + 512; main_size = rows[0]['size']
    if cert['size'] != 1162 or source[c:c + 180] != prefix(source[:512], source[512:512 + main_size]):
        raise ValueError('Existing main hash prefix differs')
    out[c:c + 180] = prefix(bytes(out[:512]), bytes(out[512:512 + main_size]))
    allowed = set(range(at, at + 4)) | set(range(c, c + 180))
    if len(out) != len(source) or any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, out))):
        raise ValueError('Unexpected byte changed outside MTE branch/prefix')
    for i, row in enumerate(rows):
        if i not in (0, 2) and source[row['offset']:row['end']] != out[row['offset']:row['end']]:
            raise ValueError('Non-main member changed')
    if source[c + 180:cert['end']] != out[c + 180:cert['end']]: raise ValueError('Signed DER/padding changed')
    report = dict(status='EXPERIMENTAL_MTE_OFF_DERIVED_FROM_BOOT_CONFIRMED_H4_NOT_DEVICE_TESTED',
                  artifact=NAME, input_sha256=INPUT_SHA, output_sha256=sha(out), bytes=len(out),
                  source='Exact H4 main + H3 signed BL2 hybrid f57 reported booting by user',
                  patch=dict(payload_offset=hex(OFFSET), whole_file_offset=hex(at), original=OLD.hex(),
                             replacement=NEW.hex(), instruction='b.eq 0x34784 -> b 0x34784'),
                  main_certificate_prefix_bytes=180,
                  original_h4_unlock_patch_preserved=True, all_other_bytes_unchanged=True,
                  bl2_aee_dtb_certificates_and_der_unchanged=True,
                  no_extra_hwc_region_fakelock_or_boot_policy_patch=True,
                  mte_scope='Use native H4 suppression path to append arm64.nomte; does not undo preloader execution',
                  device_flashed=False, new_image_device_boot_tested=False,
                  source_runtime_report='../../fresh_analysis/output/runtime_confirmation.json')
    return bytes(out), report


if __name__ == '__main__':
    out, report = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(out); (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(NAME, report['output_sha256'], len(out))

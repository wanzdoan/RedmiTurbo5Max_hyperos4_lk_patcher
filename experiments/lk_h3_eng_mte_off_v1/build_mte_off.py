"""Separate kernel-MTE suppression variants; original preloader is not modified."""
from pathlib import Path
import json
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

TARGETS = {
    'unlocked_auto_hwc': (
        'experiments/lk_h3_eng_auto_hwc_v1/unlocked/lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_TEST.img',
        '64e91b35a058bfff943897157cfb993515ce278a1ff67d3c7fc0d1fb370764e8',
        'lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_MTE_OFF_TEST.img'),
    'fakelock_v2_auto_hwc': (
        'experiments/lk_h3_eng_auto_hwc_v1/fakelock_v2/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_TEST.img',
        '5345d07edbb65d6519b02d10117ac2e32405faf3dd13c6b54546eb66fa259032',
        'lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_MTE_OFF_TEST.img'),
    'stock_policy_auto_hwc': (
        'experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img',
        'fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0',
        'lk_hyperos3_ENG_STOCK_AUTO_HWC_MTE_OFF_TEST.img'),
}
OFFSET = 0x34d20
OLD = bytes.fromhex('20020054')
NEW = bytes.fromhex('11000014')


def build(kind, source):
    path, expected, name = TARGETS[kind]
    if sha(source) != expected:
        raise ValueError('Expected exact non-MTE-off base for ' + kind)
    at = 512 + OFFSET
    if source[at:at + 4] != OLD:
        raise ValueError('Expected conditional branch to native MTE-off path')
    rows = members(source)
    cert = rows[2]; c = cert['offset'] + 512
    if cert['size'] != 1162 or source[c:c + 180] != prefix(source[:512], source[512:512 + rows[0]['size']]):
        raise ValueError('Existing prefix differs')
    out = bytearray(source)
    out[at:at + 4] = NEW
    out[c:c + 180] = prefix(bytes(out[:512]), bytes(out[512:512 + rows[0]['size']]))
    allowed = set(range(at, at + 4)) | set(range(c, c + 180))
    if len(source) != len(out) or any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, out))):
        raise ValueError('Unexpected modification outside branch/prefix')
    for i, row in enumerate(rows):
        if i not in (0, 2) and source[row['offset']:row['end']] != out[row['offset']:row['end']]:
            raise ValueError('Other member changed')
    if source[c + 180:cert['end']] != out[c + 180:cert['end']]:
        raise ValueError('Signed DER/padding changed')
    report = dict(status='EXPERIMENTAL_KERNEL_MTE_OFF_NOT_DEVICE_BOOTED', artifact=name, variant=kind,
                  input=path, input_sha256=expected, output_sha256=sha(out), bytes=len(out),
                  patch=dict(payload_offset=hex(OFFSET), whole_file_offset=hex(at),
                             old=OLD.hex(), new=NEW.hex(),
                             instruction='b.eq 0x34d64 -> b 0x34d64'),
                  behavior='Always use existing kernel_mte_init suppression branch; append arm64.nomte',
                  original_mte_info_preserved=True, original_booker_off_path_preserved=True,
                  original_hwc_unlock_and_reporting_bytes_preserved=True,
                  all_other_members_and_original_der_unchanged=True,
                  no_preloader_or_device_writes=True, device_boot_tested=False,
                  limits=['Does not retroactively disable MTE in preloader or guarantee secure-world state',
                          'Exact running kernel consumption and downstream re-enablement not hardware verified',
                          'Disables a memory-safety feature for kernels honoring arm64.nomte',
                          'No boot, Android property readback, OTA or attestation success demonstrated'])
    return bytes(out), report


def main():
    for kind, (path, _, name) in TARGETS.items():
        out, report = build(kind, (ROOT / path).read_bytes())
        directory = HERE / kind; directory.mkdir(exist_ok=True)
        (directory / name).write_bytes(out)
        (directory / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
        print(kind, report['output_sha256'])


if __name__ == '__main__':
    main()

"""Exact Fenrir input + automatic HWC + native MTE-off branch, nothing else."""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import prefix, sha
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_v2_hybrid_v1/stock_hwc'))
from hwc_patch import patches as hwc_patches

INPUT = ROOT / 'fresh_analysis/fenrir_review/input/dash-fenrir-versioned.bin'
INPUT_SHA = 'dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113'
NAME = 'lk_FENRIR_ONLY_AUTO_HWC_MTE_OFF_TEST.img'


def members(data):
    rows = []; offset = 0
    for i in range(15):
        if data[offset:offset + 4] != bytes.fromhex('88168858'): raise ValueError('Bad member')
        size = struct.unpack_from('<I', data, offset + 4)[0]; end = (offset + 512 + size + 15) & ~15
        if end > len(data): raise ValueError('Member overrun')
        rows.append(dict(offset=offset, size=size, end=end, name=data[offset + 8:offset + 40].split(b'\0')[0].decode()))
        offset = end
    if data[offset:] != bytes(768): raise ValueError('Unexpected Fenrir trailer')
    return rows


def build(source):
    if sha(source) != INPUT_SHA: raise ValueError('Expected exact original Fenrir input')
    rows = members(source); out = bytearray(source); allowed = set(); changes = []
    operations = hwc_patches(source, 'stock') + [
        (0x344a8, bytes.fromhex('11000014'), 'Force existing kernel_mte_init suppression branch at0x344ec')]
    if source[512 + 0x344a8:512 + 0x344ac] != bytes.fromhex('20020054'):
        raise ValueError('Unexpected stock-H3 MTE branch')
    for off, code, purpose in operations:
        start = 512 + off
        if any(i in allowed for i in range(start, start + len(code))): raise ValueError('Patch overlap')
        changes.append(dict(payload_offset=hex(off), whole_file_offset=hex(start),
                            old=source[start:start + len(code)].hex(), new=code.hex(), bytes=len(code), purpose=purpose))
        out[start:start + len(code)] = code; allowed.update(range(start, start + len(code)))
    c = rows[2]['offset'] + 512; n = rows[0]['size']
    if rows[2]['size'] != 1072 or source[c:c + 90] != prefix(source[:512], source[512:512 + n])[:90]:
        raise ValueError('Unexpected original Fenrir main hash prefix')
    out[c:c + 90] = prefix(bytes(out[:512]), bytes(out[512:512 + n]))[:90]
    allowed.update(range(c, c + 90))
    if len(out) != len(source) or any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, out))):
        raise ValueError('Changed bytes outside HWC, MTE instruction and main hash prefix')
    for i, row in enumerate(rows):
        if i not in (0, 2) and source[row['offset']:row['end']] != out[row['offset']:row['end']]:
            raise ValueError('Non-main member changed')
    if source[c + 90:rows[2]['end']] != out[c + 90:rows[2]['end']]: raise ValueError('Signed DER/padding changed')
    # Explicitly retain Fenrir lock/verify/flash/report/version-maintenance bytes.
    protected = [(0xa0fc, 0xa148), (0xc334, 0xc3f8), (0xc868, 0xc994), (0xcd88, 0xd1e8),
                 (0x6858c, 0x693e0), (0x88750, 0x88790), (0x8982c, 0x8983c),
                 (0xc4670, 0xc487c)]
    for lo, hi in protected:
        if source[512 + lo:512 + hi] != out[512 + lo:512 + hi]:
            raise ValueError('Fenrir protected behavior changed at ' + hex(lo))
    report = dict(status='EXPERIMENTAL_FENRIR_ONLY_HWC_MTE_OFF_NOT_DEVICE_BOOTED', artifact=NAME,
                  input_sha256=INPUT_SHA, output_sha256=sha(out), bytes=len(out), patches=changes,
                  main_hash_prefix_bytes=90, all_other_bytes_unchanged=True,
                  no_v2_exports_added=True, no_maintenance_guard_added=True,
                  fenrir_lock_avb_root_flash_erase_reports_unchanged=True,
                  bl2_aee_dtb_certificates_and_zero_trailer_unchanged=True,
                  hwc_mapping={'cn_case_insensitive_exact': 'CN', 'nonempty_non_cn': 'GLOBAL',
                               'missing_empty_read_error': 'original hardware HWC'},
                  mte_off_scope='Native LK suppression path emits arm64.nomte to kernel; does not undo earlier preloader execution',
                  device_flashed=False, device_boot_tested=False, root_success_device_confirmed=False)
    return bytes(out), report


def main():
    out, report = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(out); (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(NAME, report['output_sha256'], len(out))


if __name__ == '__main__': main()

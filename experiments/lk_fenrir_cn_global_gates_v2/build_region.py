"""Add actual CN/Global region gates to the exact minimal Fenrir HWC/MTE file."""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_only_auto_hwc_mte_off_v1'))
from build_fenrir_only import members, prefix, sha

INPUT = ROOT / 'experiments/lk_fenrir_only_auto_hwc_mte_off_v1/lk_FENRIR_ONLY_AUTO_HWC_MTE_OFF_TEST.img'
INPUT_SHA = '8d616abb51cd48373d7a892013c080b902a811983133bfc42d1a0accd73e30e8'
NAME = 'lk_FENRIR_CN_GLOBAL_GATES_HWC_MTE_OFF_V2_TEST.img'


def branch(at, target, link=False):
    d = target - at
    if d % 4 or not -(1 << 27) <= d < (1 << 27): raise ValueError('Branch out of range')
    return struct.pack('<I', (0x94000000 if link else 0x14000000) | ((d // 4) & 0x3ffffff))


def adr(at, target, reg):
    d = target - at
    if not -(1 << 20) <= d < (1 << 20): raise ValueError('ADR out of range')
    return struct.pack('<I', 0x10000000 | ((d & 3) << 29) | (((d >> 2) & 0x7ffff) << 5) | reg)


def patches():
    fallback = (bytes.fromhex('e0030491') + adr(0x88614, 0x1120af, 1) + bytes.fromhex('02208052')
                + branch(0x8861c, 0x7e820, True) + bytes.fromhex('1f000071f4039f5a')
                + branch(0x88628, 0x88594))
    return [
        (0x68d84, bytes.fromhex('40110035'), branch(0x68d84, 0x68d94),
         'Remove CN/NATIVE-only init_boot SKU panic; keep preceding production/efuse checks'),
        (0x88610, bytes.fromhex('e00200d000fc30910fd6ff97a87340391f010071f4139f5adbffff17'), fallback,
         'After selected image-key mismatch, also accept the embedded Global256 key; retain chain/key extraction'),
        (0xc53dc, bytes.fromhex('61020054'), branch(0xc53dc, 0xc5428),
         'Use the existing CN/Global4096 AVB-key union regardless of hardware region'),
        (0x87098, bytes.fromhex('20010034'), branch(0x87098, 0x8709c),
         'Skip only CN-hardware/non-CN-software region-triggered recovery inside the Secure-ID path'),
    ]


def build(source):
    if sha(source) != INPUT_SHA: raise ValueError('Expected exact minimal Fenrir HWC/MTE input')
    rows = members(source); out = bytearray(source); allowed = set(); changes = []
    for off, old, new, purpose in patches():
        at = 512 + off
        if len(old) != len(new) or source[at:at + len(old)] != old:
            raise ValueError('Unexpected original gate at ' + hex(off))
        out[at:at + len(new)] = new; allowed.update(range(at, at + len(new)))
        changes.append(dict(payload_offset=hex(off), whole_file_offset=hex(at), original=old.hex(),
                            replacement=new.hex(), bytes=len(new), purpose=purpose))
    c = rows[2]['offset'] + 512; size = rows[0]['size']
    if source[c:c + 90] != prefix(source[:512], source[512:512 + size])[:90]: raise ValueError('Original prefix differs')
    out[c:c + 90] = prefix(bytes(out[:512]), bytes(out[512:512 + size]))[:90]
    allowed.update(range(c, c + 90))
    if len(out) != len(source) or any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, out))):
        raise ValueError('Undeclared change')
    for i, row in enumerate(rows):
        if i not in (0, 2) and source[row['offset']:row['end']] != out[row['offset']:row['end']]:
            raise ValueError('Non-main member changed')
    if source[c + 90:rows[2]['end']] != out[c + 90:rows[2]['end']]: raise ValueError('DER/padding changed')
    return bytes(out), dict(status='EXPERIMENTAL_CN_GLOBAL_GATES_NOT_POCO_DEVICE_TESTED', artifact=NAME,
                           input_sha256=INPUT_SHA, output_sha256=sha(out), bytes=len(out), patches=changes,
                           previous_hwc_and_mte_changes_preserved=True, fenrir_fake_lock_and_boot_policy_preserved=True,
                           all_other_members_and_signed_der_unchanged=True,
                           key_scope='Embedded CN/Global keys, not arbitrary/unknown keys or every POCO model',
                           poco_rom_build_and_images_not_available=True, actual_failure_stage_not_proven=True,
                           device_boot_tested=False, device_flashed=False)


if __name__ == '__main__':
    out, report = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(out); (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(NAME, report['output_sha256'], len(out))

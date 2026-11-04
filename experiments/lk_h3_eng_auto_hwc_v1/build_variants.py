"""Add automatic HWC to two exact ENG bases; preserve each base's policy."""
from pathlib import Path
import hashlib
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

TARGETS = {
    'unlocked': (
        'experiments/lk_h3_eng_unlock_v1/lk_hyperos3_ENG_UNLOCKBL_TEST.img',
        '0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466',
        'lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_TEST.img'),
    'fakelock_v2': (
        'experiments/lk_h3_eng_fakelock_v2/lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_DEEP_HANDOFF_TEST.img',
        '72e17062caf8893b0c6a7c82f6d3c25f0e3ed27544e01b5c47fe3a0234e5b075',
        'lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_TEST.img'),
}
CODE_SHA = '2d4fd1fd3c415a4771bfc8971a556768361d5d9ae9bad107e2811d0a4f2a67bb'


def branch(at, to):
    delta = to - at
    if delta % 4 or not -(1 << 27) <= delta < (1 << 27):
        raise ValueError('Branch out of range')
    return struct.pack('<I', 0x14000000 | ((delta // 4) & 0x3ffffff))


def adr(at, to, reg):
    delta = to - at
    if not -(1 << 20) <= delta < (1 << 20):
        raise ValueError('ADR out of range')
    return struct.pack('<I', 0x10000000 | ((delta & 3) << 29) | (((delta >> 2) & 0x7ffff) << 5) | reg)


def patches():
    code = (HERE / 'auto_hwc.bin').read_bytes()
    if len(code) != 128 or sha(code) != CODE_SHA or code[-8:] != b'GLOBAL\0\0':
        raise ValueError('Unexpected assembled HWC block/literal')
    return [
        (0x13364, code, 'Defer HWC emission; select CN/GLOBAL or original HWC after countrycode read'),
        (0x13960, bytes.fromhex('ff7f00a9') + adr(0x13964, 0xfaf36, 0) + branch(0x13968, 0x139b4),
         'Clear partial countrycode buffer on failed/short read; keep log and close'),
        (0x139c0, branch(0x139c0, 0x13368), 'Common countrycode continuation enters late HWC block'),
    ]


def build(kind, source):
    path, digest, name = TARGETS[kind]
    if sha(source) != digest:
        raise ValueError('Expected exact base image for ' + kind)
    for off, expected in [(0x139c0, bytes.fromhex('e0030691')),
                          (0x13728, bytes.fromhex('ff7f00a9')),
                          (0xf34d4, b'CN\0'),
                          (0xcf78f, b'mi_hwid: snprintf error.\n\0')]:
        if source[512 + off:512 + off + len(expected)] != expected:
            raise ValueError('Unexpected original code/literal at ' + hex(off))
    rows = members(source)
    out = bytearray(source)
    allowed = set()
    changes = []
    for off, code, purpose in patches():
        start = 512 + off
        changes.append(dict(payload_offset=hex(off), whole_file_offset=hex(start),
                            old=source[start:start + len(code)].hex(), new=code.hex(), purpose=purpose))
        out[start:start + len(code)] = code
        allowed.update(range(start, start + len(code)))
    cert = rows[2]
    start = cert['offset'] + 512
    if cert['size'] != 1162 or source[start:start + 180] != prefix(source[:512], source[512:512 + rows[0]['size']]):
        raise ValueError('Original hash prefix differs')
    out[start:start + 180] = prefix(bytes(out[:512]), bytes(out[512:512 + rows[0]['size']]))
    allowed.update(range(start, start + 180))
    if len(source) != len(out) or any(a != b and i not in allowed for i, (a, b) in enumerate(zip(source, out))):
        raise ValueError('Unexpected bytes changed')
    for i, row in enumerate(rows):
        if i not in (0, 2) and source[row['offset']:row['end']] != out[row['offset']:row['end']]:
            raise ValueError('Other container member changed')
    if source[start + 180:cert['end']] != out[start + 180:cert['end']]:
        raise ValueError('DER/padding changed')
    if source[512 + 0xc5888:512 + 0xc5960] != out[512 + 0xc5888:512 + 0xc5960]:
        raise ValueError('Original key callback modified')
    report = dict(status='EXPERIMENTAL_NOT_DEVICE_BOOTED', variant=kind, artifact=name,
                  input=path, input_sha256=digest, output_sha256=sha(out), bytes=len(out),
                  mapping={'exact_cn_case_insensitive': 'CN', 'nonempty_non_cn': 'GLOBAL',
                           'missing_empty_read_error': 'original hardware HWC'},
                  source='Existing active-slot countrycode partition read; no extra partition read',
                  base_unlock_and_reporting_policy_preserved=True, key_trust_callback_unchanged=True,
                  all_other_members_and_original_der_unchanged=True,
                  mte_changed=False, device_flashed=False, device_boot_tested=False,
                  formatter_failure='Skip HWC append, log existing no-format-argument suffix, restore caller pointers',
                  literal='GLOBAL is embedded at payload0x133dc inside the replaced block, never in AVB callback',
                  patches=changes,
                  limits=['HWC selection does not establish all-ROM compatibility or add signing keys',
                          'EEA/TW and other non-CN values map to GLOBAL; actual ROM boot not verified',
                          'Stale countrycode can select a stale region',
                          'Fakelock only changes reporting; does not prove true lock, OTA, Integrity or hardware attestation',
                          'No modem, Android, NVRAM, stored lock, preloader or MTE patch'])
    return bytes(out), report


def main():
    for kind, (path, _, name) in TARGETS.items():
        output, report = build(kind, (ROOT / path).read_bytes())
        directory = HERE / kind
        directory.mkdir(exist_ok=True)
        (directory / name).write_bytes(output)
        (directory / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
        print(kind, report['output_sha256'], len(output))


if __name__ == '__main__':
    main()

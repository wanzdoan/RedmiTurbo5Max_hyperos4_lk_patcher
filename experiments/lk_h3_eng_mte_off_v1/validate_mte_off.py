"""Validate final files, including native MTE code without runtime code patching."""
import itertools
import json
import random
import sys
from build_mte_off import *

sys.path.insert(0, str(ROOT / 'fresh_analysis/mte_research'))
from probe_mte import lk_case
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1'))
from validate_auto_hwc import flow_case
from validate_stock_flash_region import key_case
from build_stock_flash_region import STOCK_H3
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v2'))
from validate_deep_fakelock import handoff_case, seccfg_case, warning_case
from validate_fakelock import fastboot_case, android_state_case, avb_device_state_case
from validate_eng_unlock import main_case, bl2_run
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes


def check(kind):
    path, digest, name = TARGETS[kind]
    source = (ROOT / path).read_bytes(); out = (HERE / kind / name).read_bytes()
    assert build(kind, source)[0] == out == build(kind, source)[0]
    bad = bytearray(source); bad[12345] ^= 1
    try: build(kind, bytes(bad))
    except ValueError: pass
    else: raise AssertionError('Wrong input accepted')
    mte = []
    for image, off in ((source, False), (out, True)):
        for bits, reserved in itertools.product(tuple(range(8)) + (0x405, 0x407, 0xf07, 0xffffffff), (False, True)):
            mte.append(lk_case(image, bits, reserved, image_forced_off=off))
    region = []
    countries = ('cn', 'CN', 'Cn', 'cN', 'global', 'GLOBAL', 'eea', 'EEA', 'tw', 'TW',
                 'taiwan', 'eu', 'native', 'unknown', 'cn_extra', '', 'c', 'cnx', 'cna', ' cn', 'cn ')
    for country, hw, slot in itertools.product(countries, ('CN', 'GLOBAL'), ('_a', '_b')):
        region.append(flow_case(out, True, country, hw, suffix=slot))
    for country, level, version in itertools.product(('cn', 'global', 'eea', 'tw'), ('MP', 'EVT'), ('1.0', 'V2.3')):
        region.append(flow_case(out, True, country, level=level, version=version))
    for country, hw, slot, mode in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('_a', '_b'),
                                                   ('missing', 'empty', 'short', 'io_error')):
        region.append(flow_case(out, True, country, hw, suffix=slot, mode=mode))
    for country, hw, stage in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('name', 'country', 'hwc')):
        region.append(flow_case(out, True, country, hw, fmt_fail=stage))
    internal, public, deep, keys = [], [], [], []
    if kind != 'stock_policy_auto_hwc':
        rng = random.Random(9500)
        for initial in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]:
            for entry in (0x896e4, 0x88c18, 0x885fc):
                internal.append(main_case(out, entry, initial, 'getter'))
        for boot, state in itertools.product((0, 1, 2, 0xffffffff), (None, 0, 1, 2, 3, 4, 0xffffffff)):
            internal.append(main_case(out, 0x6979c, 0, 'avb', boot, state))
        for state in (None, 0, 1, 2, 3, 4, 0xffffffff):
            internal.append(main_case(out, 0xb6588, 0xffffffff, 'lock_boolean', mock_state=state))
    if kind == 'unlocked_auto_hwc':
        for state in (None, 0, 1, 2, 3, 4, 0xffffffff):
            internal.append(main_case(out, 0xc770, 0xffffffff, 'fastboot', mock_state=state))
    elif kind == 'fakelock_v2_auto_hwc':
        for boot, lock, status, keyerr in itertools.product((0, 1, 2, 3, 0xffffffff), (None, 3, 4), (0, 7), (0, 9)):
            deep.append(handoff_case(out, True, boot, lock, status, keyerr))
        for state, status in itertools.product((0, 1, 3, 4, 0xffffffff), (0, 7)):
            deep.append(seccfg_case(out, True, state, status))
        for state, mode in itertools.product((0, 1, 2, 3), (0, 1, 99)):
            deep.append(warning_case(out, True, state, mode))
        values = [('product', 'dash'), ('unlocked', 'yes'), ('secure', 'no'), ('current-slot', 'a'),
                  ('unlocked-critical', 'no'), ('slot-count', '2'), ('unlocked-extra', 'untouched'), ('secure-extra', 'untouched')]
        for entries in (values, list(reversed(values)), []):
            for query in ('all', 'unlocked', 'secure', 'product', 'current-slot', 'missing', 'unlocked-extra', 'secure-extra'):
                public.append(fastboot_case(out, query, entries, True))
        public.append(fastboot_case(out, '', values, True, oem=True))
        for state in (0, 1, 2, 3, 0xffffffff): public.append(android_state_case(out, state, patched=True))
        for unlocked, rc in itertools.product((0, 1, 2), (0, 1)):
            public.append(avb_device_state_case(out, unlocked, rc, True))
        for first, second in [(-8, 0), (0, -8)]: public.append(android_state_case(out, 2, first, second))
    else:
        for state in (1, 2, 3, 4):
            for entry, mode in ((0x6979c, 'avb'), (0xb6588, 'lock_boolean'), (0xc770, 'fastboot')):
                internal.append(main_case(out, entry, 0, mode, mock_state=state))
        stock = STOCK_H3.read_bytes()
        for label, bits, modulus in (
            ('ENG', 2048, out[512 + 0x130728:512 + 0x130828]),
            ('CN', 4096, stock[512 + 0x138638:512 + 0x138838]),
            ('GL', 4096, stock[512 + 0x138838:512 + 0x138a38]),
            ('unknown', 4096, bytes(512))):
            keys.append(dict(key=label, **key_case(out, bits, modulus, (0, 0) if label == 'unknown' else (0, 1))))
    rows = members(out); certs = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = out[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[180:] if i == 2 else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append(dict(member=i, retained_der_signature_valid=True))
    b, c = rows[3], rows[2]
    raw_bl2 = out[b['offset'] + 512:b['offset'] + 512 + b['size']]
    record = out[c['offset']:c['offset'] + 512 + c['size']]
    parsers = []
    for mask in (0x60, 0x1f4e2, 0x1f400):
        got = bl2_run(raw_bl2, record, mask)
        assert got['result'] == 0
        if mask != 0x1f400:
            assert got['payload_hash'] == sha(out[512:rows[0]['end']])
            assert got['header_hash'] == sha(out[:512])
        parsers.append(got)
    counts = dict(mte_cpu_cases=len(mte), hwc_cpu_cases=len(region), internal_policy_cpu_cases=len(internal),
                  deep_fakelock_cpu_cases=len(deep), public_fakelock_cpu_cases=len(public), key_callback_cpu_cases=len(keys))
    report = dict(status='PASS_BOUNDED_NATIVE_REPLAY_NOT_DEVICE_BOOT', variant=kind,
                  input_sha256=digest, output_sha256=sha(out), **counts, total_main_cpu_cases=sum(counts.values()),
                  final_image_code_not_modified_by_mte_emulator=True,
                  retained_der_signatures=certs, native_eng_bl2_parser_cases=parsers,
                  deterministic_build=True, wrong_input_rejected=True,
                  mte_cases=mte, region_cases=region, internal_cases=internal,
                  deep_cases=deep, public_cases=public, key_cases=keys,
                  limits=['MTE probe MMIO is emulator RAM; kernel and preloader are not executing with this image',
                          'MTE-off does not retroactively change preloader execution or secure-world state',
                          'HWC mapping and lock/report policy probes are bounded; not full device boot/OTA/attestation proof',
                          'Boundary mocks match the referenced HWC/fakelock/MTE probe documentation'])
    (HERE / kind / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(dict(variant=kind, output_sha256=sha(out), **counts, total_main_cpu_cases=sum(counts.values()))))


if __name__ == '__main__':
    for kind in TARGETS: check(kind)

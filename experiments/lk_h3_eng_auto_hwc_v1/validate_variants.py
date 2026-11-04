"""Replay final variant bytes; no device operations or old-report rewrites."""
import itertools
import json
import random
import sys
from build_variants import *
from assemble_auto import assemble

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1'))
from validate_auto_hwc import flow_case
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v2'))
from validate_deep_fakelock import handoff_case, seccfg_case, warning_case
from validate_fakelock import fastboot_case, android_state_case, avb_device_state_case
from validate_eng_unlock import main_case, bl2_run
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes


def check(kind):
    path, digest, name = TARGETS[kind]
    source = (ROOT / path).read_bytes()
    out = (HERE / kind / name).read_bytes()
    assert assemble() == (HERE / 'auto_hwc.bin').read_bytes()
    assert build(kind, source)[0] == out == build(kind, source)[0]
    bad = bytearray(source); bad[12345] ^= 1
    try:
        build(kind, bytes(bad))
    except ValueError:
        pass
    else:
        raise AssertionError('Wrong input accepted')
    countries = ('cn', 'CN', 'Cn', 'cN', 'global', 'GLOBAL', 'eea', 'EEA', 'tw', 'TW',
                 'taiwan', 'eu', 'native', 'unknown', 'cn_extra', '', 'c', 'cnx', 'cna', ' cn', 'cn ')
    region = []
    for country, hw, slot in itertools.product(countries, ('CN', 'GLOBAL'), ('_a', '_b')):
        region.append(flow_case(out, True, country, hw, suffix=slot))
    for country, level, version in itertools.product(('cn', 'global', 'eea', 'tw'), ('MP', 'EVT'), ('1.0', 'V2.3')):
        region.append(flow_case(out, True, country, level=level, version=version))
    for country, hw, slot, mode in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('_a', '_b'),
                                                   ('missing', 'empty', 'short', 'io_error')):
        region.append(flow_case(out, True, country, hw, suffix=slot, mode=mode))
    for country, hw, stage in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('name', 'country', 'hwc')):
        region.append(flow_case(out, True, country, hw, fmt_fail=stage))
    internal = []
    rng = random.Random(9500)
    for initial in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]:
        for entry in (0x896e4, 0x88c18, 0x885fc):
            internal.append(main_case(out, entry, initial, 'getter'))
    for boot, state in itertools.product((0, 1, 2, 0xffffffff), (None, 0, 1, 2, 3, 4, 0xffffffff)):
        internal.append(main_case(out, 0x6979c, 0, 'avb', boot, state))
    for state in (None, 0, 1, 2, 3, 4, 0xffffffff):
        internal.append(main_case(out, 0xb6588, 0xffffffff, 'lock_boolean', mock_state=state))
    deep, public = [], []
    if kind == 'unlocked':
        for state in (None, 0, 1, 2, 3, 4, 0xffffffff):
            internal.append(main_case(out, 0xc770, 0xffffffff, 'fastboot', mock_state=state))
    else:
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
        for state in (0, 1, 2, 3, 0xffffffff):
            public.append(android_state_case(out, state, patched=True))
        for unlocked, rc in itertools.product((0, 1, 2), (0, 1)):
            public.append(avb_device_state_case(out, unlocked, rc, True))
        for first, second in [(-8, 0), (0, -8)]:
            public.append(android_state_case(out, 2, first, second))
    rows = members(out)
    certs = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'):
            continue
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
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN); decoder.skipdata = True
    for ins in decoder.disasm(source[512 + 0x3000:512 + 0xc8000], 0x3000):
        if ins.mnemonic in ('b', 'bl', 'cbz', 'cbnz', 'tbz', 'tbnz') or ins.mnemonic.startswith('b.'):
            last = ins.op_str.rsplit(',', 1)[-1].strip()
            if not last.startswith('#0x'):
                continue
            target = int(last[1:], 16)
            for lo, hi in ((0x13364, 0x133e4), (0x13960, 0x1396c)):
                assert not (lo < target < hi and not lo <= ins.address < hi), (hex(ins.address), hex(target))
    report = dict(status='PASS_BOUNDED_NATIVE_REPLAY_NOT_DEVICE_BOOT', variant=kind,
                  input_sha256=digest, output_sha256=sha(out),
                  hwc_cpu_cases=len(region), internal_unlock_cpu_cases=len(internal),
                  deep_fakelock_cpu_cases=len(deep), public_fakelock_cpu_cases=len(public),
                  total_main_cpu_cases=len(region) + len(internal) + len(deep) + len(public),
                  retained_der_signatures=certs, native_eng_bl2_parser_cases=parsers,
                  deterministic_build_and_assembly=True, wrong_input_rejected=True,
                  no_external_direct_branch_into_repurposed_interior=True,
                  region_cases=region, internal_cases=internal, deep_cases=deep, public_cases=public,
                  mocks=['Partition lookup/read/close and slot suffix', 'snprintf, logging, bootarg append, PAC',
                         'Fakelock secure-monitor sink and OEM-key extraction fixtures; actual TEE not executed',
                         'Labelled lock-state controls; other getter probes run actual force-unlock code'],
                  limits=['Two bounded bootargs slices skip unrelated bootargs, not full CPU/device boot',
                          'Synthetic EEA/TW country fixtures; actual ROM boot unverified',
                          'Original DER signatures retained, signed main digest differs; parser replay is not full secure boot proof',
                          'No MTE changes, phone writes, OTA, real lock, Integrity or attestation proof'])
    (HERE / kind / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('variant', 'output_sha256', 'hwc_cpu_cases',
                     'internal_unlock_cpu_cases', 'deep_fakelock_cpu_cases', 'public_fakelock_cpu_cases', 'total_main_cpu_cases')}))


if __name__ == '__main__':
    for kind in TARGETS:
        check(kind)

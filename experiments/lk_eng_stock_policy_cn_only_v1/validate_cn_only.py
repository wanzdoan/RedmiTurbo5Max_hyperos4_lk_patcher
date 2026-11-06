"""Native CN-only callback, original region/HWC/lock policy and MTE tests."""
import itertools
import json
import random
import struct
import sys
from build_cn_only import *
from assemble_callback import assemble
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_locked_rom_policy_v1'))
from validate_stock_flash_region import key_case, region_case
from validate_eng_unlock import main_case, bl2_run
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_v2_hybrid_v1/stock_hwc'))
from validate_hwc import cases_for
sys.path.insert(0, str(ROOT / 'fresh_analysis/mte_research'))
from probe_mte import lk_case


def check(kind):
    source = INPUT.read_bytes(); image = (HERE / NAMES[kind]).read_bytes()
    assert assemble() == (HERE / 'trust_eng_cn.bin').read_bytes()
    assert build(source, kind)[0] == image == build(source, kind)[0]
    wrong = bytearray(source); wrong[12345] ^= 1
    try: build(bytes(wrong), kind)
    except ValueError: pass
    else: raise AssertionError('Wrong input accepted')
    stock = STOCK.read_bytes()
    keys = {'ENG': source[512 + 0x130728:512 + 0x130828],
            'CN': stock[512 + 0x138638:512 + 0x138838],
            'GL': stock[512 + 0x138838:512 + 0x138a38]}
    key_cases = []
    for name, key in keys.items():
        key_cases.append(dict(key=name, **key_case(image, 2048 if name == 'ENG' else 4096,
                                                   key, (0, int(name != 'GL')))))
        for offset in (0, 1, 15, 31, len(key) // 2, len(key) - 1):
            bad = bytearray(key); bad[offset] ^= 1
            key_cases.append(dict(key=name + '_mutated', **key_case(image, 2048 if name == 'ENG' else 4096,
                                                                   bytes(bad), (0, 0))))
    rng = random.Random(9500)
    for bits in (1024, 2048, 4096, 8192):
        key_cases.append(dict(key='unknown', **key_case(image, bits, rng.randbytes(bits // 8), (0, 0))))
    for bits, key in ((2048, keys['ENG']), (4096, keys['CN'])):
        for length in (0, 4, 7, 8, 263, 264, 519, 521, 1031, 1033):
            key_cases.append(dict(key='malformed_length', **key_case(image, bits, key, (2, 0), size=length)))
    for nk, no in ((True, False), (False, True), (True, True)):
        key_cases.append(dict(key='null', **key_case(image, 4096, keys['CN'], (2, 0xa5), null_key=nk, null_out=no)))
    key_cases.append(dict(key='hash_failure', **key_case(image, 4096, keys['CN'], (0, 0), hash_error=True)))
    digest_at = image.find(bytes.fromhex(CN_HASH), 512 + 0xc5888, 512 + 0xc5960)
    assert digest_at >= 0
    altered = bytearray(image); altered[digest_at] ^= 1
    key_cases.append(dict(key='corrupted_cn_digest_negative_control',
                          **key_case(bytes(altered), 4096, keys['CN'], (0, 0))))
    region = []
    for value, sb, hw, error in itertools.product(
            (' cn', ' CN', ' gl', ' GL', ' GLOBAL', ' ENG', ' unknown', '', 'factory', ' factory'),
            (0, 14), (0, 1), (0, 7)):
        region.append(region_case(image, False, value, sb=sb, hw=hw, fuse_rc=error))
    for value, rc, error, unlocked, config in itertools.product((' CN', ' GL'), (-1, -6), (0, 7), ('no', 'yes'), (False, True)):
        region.append(region_case(image, False, value, rpmb_rc=rc, rpmb_error=error, unlocked=unlocked, config=config))
    policy = []
    for state in (1, 2, 3, 4):
        for entry, mode in ((0x6979c, 'avb'), (0xb6588, 'lock_boolean'), (0xc770, 'fastboot')):
            policy.append(main_case(image, entry, 0, mode, mock_state=state))
    hwc = cases_for('debug', image, automatic=False)
    mte = [lk_case(image, bits, reserved, image_forced_off=(kind == 'mte_off'))
           for bits, reserved in itertools.product(tuple(range(8)) + (0x405, 0x407, 0xf07, 0xffffffff), (False, True))]
    rows = members(image); certs = []; parsers = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = image[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[180:] if i == 2 else raw; end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append(dict(member=i, original_der_signature_valid=True))
    b, c = rows[3], rows[2]; bl2 = image[b['offset'] + 512:b['offset'] + 512 + b['size']]
    cert_record = image[c['offset']:c['offset'] + 512 + c['size']]
    for mask in (0x60, 0x1f4e2, 0x1f400):
        got = bl2_run(bl2, cert_record, mask); assert got['result'] == 0
        if mask != 0x1f400:
            assert got['payload_hash'] == sha(image[512:rows[0]['end']]) and got['header_hash'] == sha(image[:512])
        parsers.append(got)
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN); decoder.skipdata = True
    for ins in decoder.disasm(source[512 + 0x3000:512 + 0xc8000], 0x3000):
        if ins.mnemonic in ('b', 'bl', 'cbz', 'cbnz', 'tbz', 'tbnz') or ins.mnemonic.startswith('b.'):
            last = ins.op_str.rsplit(',', 1)[-1].strip()
            if last.startswith('#0x'):
                target = int(last[1:], 16)
                assert not (0xc5888 < target < 0xc5960 and not 0xc5888 <= ins.address < 0xc5960)
    counts = dict(keys=len(key_cases), original_region=len(region), stock_policy=len(policy),
                  original_hwc=len(hwc), mte=len(mte), eng_bl2_parser=len(parsers))
    report = dict(status='PASS_NATIVE_CN_ONLY_STOCK_POLICY_NOT_DEVICE_BOOT', variant=kind,
                  input_sha256=INPUT_SHA, output_sha256=sha(image), deterministic_build_and_assembly=True,
                  wrong_input_rejected=True, counts=counts, total_native_cpu_cases=sum(counts.values()),
                  key_cases=key_cases, original_flash_region_cases=region, stock_lock_policy_cases=policy,
                  original_hwc_cases=hwc, mte_cases=mte, native_eng_bl2_parser_cases=parsers,
                  retained_der_signatures=certs, no_external_direct_branch_into_callback_interior=True,
                  limits=['No phone flash, boot, stored lock read/write, real lock, root, OTA or Integrity test',
                          'Lock getter only mocked in labelled state controls; hardware/efuse/config/query boundaries modeled',
                          'Native SHA256/memcmp, region string processing, original hardware getter and MTE branches run',
                          'HWC stays original hardware value; China hardware fixture stays CN for Global countrycode too',
                          'Original unlocked/factory/config policy bypass behavior is retained'])
    (HERE / (kind + '_validation.json')).write_text(json.dumps(report, indent=2) + '\n')
    print(kind, counts, report['total_native_cpu_cases'], report['output_sha256'], '10 DER PASS')


if __name__ == '__main__':
    for kind in NAMES: check(kind)

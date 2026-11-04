"""Offline family/container/certificate evidence, not hardware acceptance testing."""
from pathlib import Path
import hashlib
import json
import re
import struct
import sys
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, sha

PATHS = {
    'h3_original': 'lk_hyperos3_original/lk.img',
    'h3_patched_user': 'lk_hyperos3_patched_unlockBL/dash.img',
    'h4_original': 'lk_hyperos4_original/lk.img',
    'h4_hybrid_boot_reported': 'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img',
    'eng_original': 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img',
}


def custom_fields(raw):
    result = {}
    for match in re.finditer(bytes.fromhex('06076086769316'), raw[:721]):
        pos = match.start()
        oid = '2.16.886.2454.' + '.'.join(str(x) for x in raw[pos + 7:pos + 9])
        off = pos + 9
        tag = raw[off]; size = raw[off + 1]; header_size = 2
        if size & 128:
            count = size & 127
            size = int.from_bytes(raw[off + 2:off + 2 + count], 'big')
            header_size += count
        result[oid] = dict(tag=hex(tag), value=raw[off + header_size:off + header_size + size].hex(), size=size)
    return result


def certificate_check(raw):
    key = load_der_public_key(raw[181:475])
    end = 8 + int.from_bytes(raw[6:8], 'big')
    key.verify(raw[-256:], raw[4:end],
               padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
    return dict(sha256=sha(raw), embedded_spki_sha256=sha(raw[181:475]),
                embedded_key_signature_valid=True, custom_fields=custom_fields(raw))


def main():
    images = {label: (ROOT / path).read_bytes() for label, path in PATHS.items()}
    rows = {label: members(data) for label, data in images.items()}
    families = {}
    for label, data in images.items():
        family = dict(path=PATHS[label], sha256=sha(data), bytes=len(data), components=[])
        for index in (0, 3, 6, 9, 12):
            row = rows[label][index]; off = row['offset']
            family['components'].append(dict(index=index, name=row['name'], size=row['size'],
                payload_sha256=sha(data[off + 512:off + 512 + row['size']]),
                generic_header_bytes40_80=data[off + 40:off + 80].hex()))
        a, b, c = rows[label][3:6]
        cert1 = data[b['offset'] + 512:b['offset'] + 512 + b['size']]
        cert2 = data[c['offset'] + 512:c['offset'] + 512 + c['size']]
        one = certificate_check(cert1); two = certificate_check(cert2)
        assert cert1[496:790] == cert2[181:475]
        header_digest = sha(data[a['offset']:a['offset'] + 512])
        aligned_digest = sha(data[a['offset'] + 512:a['end']])
        assert two['custom_fields']['2.16.886.2454.2.1']['value'] == '00' + aligned_digest
        assert two['custom_fields']['2.16.886.2454.2.4']['value'] == '00' + header_digest
        family['bl2'] = dict(cert1=one, cert2=two, image_key_binding_valid=True,
            signed_header_hash_matches=True, signed_aligned_payload_hash_matches=True,
            triple_sha256=sha(data[a['offset']:c['end']]),
            startup_literal_pool={hex(i): hex(struct.unpack_from('<Q', data, a['offset'] + 512 + i)[0])
                                  for i in range(0x310, 0x340, 8)})
        families[label] = family
    assert families['h3_original']['bl2']['triple_sha256'] == families['h4_hybrid_boot_reported']['bl2']['triple_sha256']
    old_data = images['h3_patched_user']; hybrid_data = images['h4_hybrid_boot_reported']
    old_bl2, old_c1, old_c2 = rows['h3_patched_user'][3:6]
    hyb_bl2, hyb_c1, hyb_c2 = rows['h4_hybrid_boot_reported'][3:6]
    for a, b in ((old_bl2, hyb_bl2), (old_c1, hyb_c1)):
        assert old_data[a['offset']:a['end']] == hybrid_data[b['offset']:b['end']]
    old_cert = old_data[old_c2['offset'] + 512:old_c2['offset'] + 512 + old_c2['size']]
    hyb_cert = hybrid_data[hyb_c2['offset'] + 512:hyb_c2['offset'] + 512 + hyb_c2['size']]
    assert old_cert[:-256] == hyb_cert[:-256]
    assert old_cert[-256:] != hyb_cert[-256:]
    eng = families['eng_original']['bl2']
    for label in ('h3_original', 'h4_original'):
        stock = families[label]['bl2']
        assert eng['cert1']['sha256'] == stock['cert1']['sha256']
        assert eng['cert2']['embedded_spki_sha256'] == stock['cert2']['embedded_spki_sha256']
        for key, value in eng['cert2']['custom_fields'].items():
            if key not in ('2.16.886.2454.2.1', '2.16.886.2454.2.4'):
                assert value == stock['cert2']['custom_fields'][key]
    release = json.loads((ROOT / 'experiments/lk_h3_eng_mte_off_v1/release_manifest.json').read_text())
    variants = []
    eng_data = images['eng_original']; eng_rows = rows['eng_original']
    for item in release['files']:
        image = (ROOT / item['repository_path']).read_bytes(); actual = members(image)
        assert sha(image) == item['sha256']
        for index in range(3, 15):
            old = eng_rows[index]; new = actual[index]
            assert eng_data[old['offset']:old['end']] == image[new['offset']:new['end']]
        variants.append(dict(variant=item['variant'], mte=item['mte'], path=item['repository_path'],
            sha256=sha(image), all_non_main_triples_exact_original_eng=True,
            stock_h3_preloader_hardware_compatibility='NOT_DEVICE_TESTED_NOT_PROVEN',
            stock_h4_preloader_hardware_compatibility='NOT_DEVICE_TESTED_NOT_PROVEN'))
    report = dict(status='PASS_STATIC_BINDING_ONLY_NOT_PRELOADER_OR_DEVICE_ACCEPTANCE',
        families=families, variants=variants,
        h3_patched_vs_hybrid_bl2=dict(header_and_payload_record_identical=True, cert1_record_identical=True,
            cert2_tbs_and_all_non_signature_bytes_identical=True,
            cert2_signature_differs_but_both_rsa_pss_valid=True,
            cert2_signature_changed_bytes=sum(a != b for a, b in zip(old_cert[-256:], hyb_cert[-256:]))),
        conclusions=[
            'Old H3 patched and boot-reported H4 hybrid contain identical H3 BL2 code/header and cert1, same cert2 TBS but different valid PSS signatures',
            'H4 hybrid BL2 signed triple is exactly H3 original, not exactly H3 patched because its PSS signature differs',
            'All six ENG variants retain the same distinct original ENG BL2/AEE/DTB/DTBO triples',
            'ENG BL2 cert1 is byte-identical to release H3/H4; image-key SPKI is identical',
            'ENG BL2 signed custom fields differ from release only in expected header and payload hashes',
            'No separate ENG signing root or additional encoded BL2 version/SOCID flag identified in these fields',
            'H3 patched versus H4 hybrid changes main/AEE/DTB/DTBO, so success difference does not isolate BL2 or one ABI field'],
        limitations=[
            'Embedded-key PSS verification does not establish the current efuse/SoC root or hardware policy',
            'No actual preloader loader invocation, antirollback-counter read or handoff execution',
            'Identical generic MTK header fields do not establish full ABI/hardware initialization compatibility',
            'Missing failure-stage log for reported brick; cannot identify its root cause from artifact inventory',
            'MTE-off runs in main after preloader/BL2; cannot repair rejection before main executes'])
    (HERE / 'family_evidence.json').write_text(json.dumps(report, indent=2) + '\n')
    print('PASS: 10 BL2 signatures, 5 signed header/payload pairs, ENG key/field equality, 6 preserved ENG families')


if __name__ == '__main__':
    main()

"""Bounded offline identity/key/boot-chain audit; no firmware/device writes."""
from pathlib import Path
import hashlib
import json
import struct
import sys
import zipfile
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members


def sha(data):
    return hashlib.sha256(data).hexdigest()


def checked(path, expected):
    data = (ROOT / path).read_bytes()
    if sha(data) != expected:
        raise ValueError('Unexpected research input: ' + path)
    return data


def payload(data, index):
    m = members(data)[index]
    return data[m['offset'] + 512:m['offset'] + 512 + m['size']]


def bl2_tuple(data):
    ms = members(data)
    return data[ms[3]['offset']:ms[5]['end']]


def properties(path):
    data = path.read_bytes()
    if data[:4] != b'AVB0':
        raise ValueError('Expected root vbmeta')
    auth, aux = struct.unpack_from('>QQ', data, 12)
    off, size = struct.unpack_from('>QQ', data, 96)
    start, end = 256 + auth + off, 256 + auth + off + size
    if 256 + auth + aux > len(data) or end > 256 + auth + aux:
        raise ValueError('Descriptor block outside vbmeta')
    out, pos = [], start
    while pos < end:
        tag, size = struct.unpack_from('>QQ', data, pos)
        if size % 8 or pos + 16 + size > end:
            raise ValueError('Malformed descriptor')
        if tag == 0:
            kl, vl = struct.unpack_from('>QQ', data, pos + 16)
            first = pos + 32
            if 16 + kl + vl + 2 > size:
                raise ValueError('Property size out of range')
            key = data[first:first + kl].decode()
            value = data[first + kl + 1:first + kl + 1 + vl].decode()
            out.append({'key': key, 'value': value})
        pos += size + 16
    return {'sha256': sha(data), 'properties': out,
            'activation_property_in_this_root_vbmeta': any(x['key'] == 'com.android.build.vbmeta.miep.activated' for x in out),
            'limits': 'Only this root descriptor block, not all chained metadata or device efuses'}


def main():
    stock = checked('lk_hyperos3_original/lk.img', 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb')
    clone = checked('experiments/lk_h3_token_key_clone_v1/lk_hyperos3_STOCK_TOKEN_KEY_MATCH_TEST.img', '69089ebb26e070c202213b19615cad5dcd9202a0ea35e521e4dce18a25599a90')
    forced = checked('lk_hyperos3_patched_unlockBL/dash.img', 'e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18')
    key_changes = []
    for index, offset in [(0, 0x1121b2), (6, 0xd2464)]:
        a, b = payload(stock, index), payload(clone, index)
        diffs = [i for i, (x, y) in enumerate(zip(a, b)) if x != y]
        if len(a) != len(b) or any(not offset <= i < offset + 256 for i in diffs):
            raise ValueError('Token clone has unexpected executable changes')
        key_changes.append({'member': index, 'modulus_payload_offset': hex(offset),
                            'stock_modulus_sha256': sha(a[offset:offset + 256]),
                            'current_clone_modulus_sha256': sha(b[offset:offset + 256]),
                            'changed_bytes': len(diffs), 'all_other_payload_bytes_identical': True})
    if bl2_tuple(stock) != bl2_tuple(clone):
        raise ValueError('Unexpected clone BL2 change')
    bl2 = []
    for name, data in [('h3_stock', stock), ('h3_token_clone', clone), ('h3_forced', forced)]:
        m = members(data)
        certs = []
        for index in (4, 5):
            der = payload(data, index)
            end = 8 + int.from_bytes(der[6:8], 'big')
            load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
                padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
            certs.append({'member': index, 'embedded_der_rsa_pss_valid': True})
        bl2.append({'name': name, 'image_sha256': sha(data), 'bl2_tuple_sha256': sha(bl2_tuple(data)),
                    'bl2_payload_sha256': sha(payload(data, 3)), 'certificates': certs})
    if payload(stock, 3) != payload(forced, 3) or payload(stock, 4) != payload(forced, 4):
        raise ValueError('Prior forced H3 has unexpected before-main differences')
    a, b = payload(stock, 5), payload(forced, 5)
    differences = [i for i, (x, y) in enumerate(zip(a, b)) if x != y]
    if len(a) != len(b) or any(i < len(a) - 256 for i in differences):
        raise ValueError('Unexpected difference before BL2 cert2 signature')
    bootmeta = []
    for label, path in [
        ('supplied_H3_Global_root', ROOT / 'experiments/lk_h3_eng_locked_rom_policy_v1/recovery_region_v2/input/vbmeta.img'),
        ('supplied_H4_China_root', Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images\vbmeta.img')),
    ]:
        if path.exists():
            bootmeta.append(dict(label=label, **properties(path)))
    archive = Path.home() / 'Downloads/REDMI TURBO 5 MAX (UBL) BY FAHIM.zip'
    package = None
    if archive.exists():
        with zipfile.ZipFile(archive) as z:
            name = next(n for n in z.namelist() if n.endswith('/main/lk.dll'))
            raw = z.read(name)
            pos = 0
            for _ in range(15):
                if raw[pos:pos + 4] != bytes.fromhex('88168858'):
                    raise ValueError('Unknown package container')
                size = struct.unpack_from('<I', raw, pos + 4)[0]
                pos = (pos + 512 + size + 15) & ~15
            expected = (ROOT / 'experiments/lk_h3_eng_unlock_v1/lk_hyperos3_ENG_UNLOCKBL_TEST.img').read_bytes()
            batch = z.read(next(n for n in z.namelist() if n.endswith('/Unlocking Auto.bat'))).decode('utf-8', errors='replace')
            package = {'archive_sha256': sha(archive.read_bytes()), 'embedded_image_bytes': len(raw),
                       'embedded_image_sha256': sha(raw), 'container_bytes': pos,
                       'container_sha256': sha(raw[:pos]), 'container_matches_ENG_forced_unlock': raw[:pos] == expected,
                       'unused_partition_tail_is_all_zero': not any(raw[pos:]),
                       'batch_warns_upgrades_not_supported': 'does not support system upgrades' in batch,
                       'batch_targets_both_lk_slots': 'lk_a' in batch and 'lk_b' in batch,
                       'batch_contains_frp_userdata_metadata_erases': all('erase ' + p in batch for p in ('frp', 'userdata', 'metadata')),
                       'never_executed': True,
                       'limits': ['Not established that the friend used this package',
                                  'Batch text and image identity only; preload helper not executed or fully analyzed',
                                  'No unique tail contents persisted or interpreted as a cause']}
    report = {'status': 'PASS_BOUNDED_OFFLINE_AUDIT', 'token_key_changes': key_changes,
              'stock_and_token_clone_BL2_tuple_identical': True,
              'forced_H3_BL2_only_cert2_signature_differs': True,
              'forced_H3_cert2_signature_changed_bytes': len(differences), 'bl2_inputs': bl2,
              'available_root_vbmeta': bootmeta, 'optional_local_unlock_package': package,
              'no_device_io': True, 'OTA_package_audited': False,
              'limits': ['No exact failing friend ROM/LK/preloader or preserved boot log supplied',
                         'Current token/RPMB/Secure-ID contents have not been read or validated',
                         'Valid embedded DER signature and identical BL2 are not proof of full hardware chain acceptance',
                         'Public-key mismatch is not proof of physical damage or the hard-brick cause']}
    (HERE / 'input_audit.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'status': report['status'], 'stock_clone_BL2_same': True,
                      'token_key_regions_compared': len(key_changes), 'root_vbmeta_audited': len(bootmeta),
                      'optional_package_ENG_container_match': package and package['container_matches_ENG_forced_unlock']}, indent=2))


if __name__ == '__main__':
    main()

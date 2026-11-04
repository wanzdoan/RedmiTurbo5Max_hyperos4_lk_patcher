"""Inspect signed live AVB metadata and read active-slot hash-covered bytes only.

Uses existing su for ordinary block reads. No flashing, reboot, lock, RPMB or efuse.
Raw captures remain in the existing private directory outside Git.
"""
from pathlib import Path
import argparse
import hashlib
import json
import re
import shlex
import struct
import subprocess
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.primitives import hashes

HERE = Path(__file__).resolve().parent
CN_MODULUS_SHA = '8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e'


def parse(data):
    if data[:4] != b'AVB0':
        raise ValueError('Missing AVB metadata')
    auth, aux = struct.unpack_from('>QQ', data, 12)
    algo = struct.unpack_from('>I', data, 28)[0]
    ho, hl, so, sl, ko, kl = struct.unpack_from('>QQQQQQ', data, 32)
    if 256 + auth + aux > len(data) or algo not in range(1, 7):
        raise ValueError('Truncated or unsupported AVB image')
    key = data[256 + auth + ko:256 + auth + ko + kl]
    bits = struct.unpack_from('>I', key)[0]
    modulus = key[8:8 + bits // 8]
    alg = hashes.SHA256() if algo in (1, 2, 3) else hashes.SHA512()
    signed = data[:256] + data[256 + auth:256 + auth + aux]
    check = hashes.Hash(alg); check.update(signed)
    if check.finalize() != data[256 + ho:256 + ho + hl]:
        raise ValueError('Signed metadata hash mismatch')
    rsa.RSAPublicNumbers(65537, int.from_bytes(modulus, 'big')).public_key().verify(
        data[256 + so:256 + so + sl], signed, padding.PKCS1v15(), alg)
    descriptors = []
    off, size = struct.unpack_from('>QQ', data, 96)
    pos, end = 256 + auth + off, 256 + auth + off + size
    if end > 256 + auth + aux:
        raise ValueError('Descriptor range invalid')
    while pos < end:
        tag, n = struct.unpack_from('>QQ', data, pos)
        if pos + 16 + n > end:
            raise ValueError('Truncated descriptor')
        if tag == 2:
            length = struct.unpack_from('>Q', data, pos + 16)[0]
            algorithm = data[pos + 24:pos + 56].split(b'\0')[0].decode()
            nl, saltlen, digestlen = struct.unpack_from('>III', data, pos + 56)
            start = pos + 132
            partition = data[start:start + nl].decode(); start += nl
            salt = data[start:start + saltlen]; start += saltlen
            digest = data[start:start + digestlen]
            descriptors.append({'tag': tag, 'partition': partition, 'image_bytes': length,
                                'algorithm': algorithm, 'salt_hex': salt.hex(), 'expected_hash': digest.hex()})
        elif tag == 4:
            location, nl, kl = struct.unpack_from('>III', data, pos + 16)
            partition = data[pos + 92:pos + 92 + nl].decode()
            key = data[pos + 92 + nl:pos + 92 + nl + kl]
            bits2 = struct.unpack_from('>I', key)[0]
            descriptors.append({'tag': tag, 'partition': partition, 'rollback_location': location,
                                'expected_modulus_sha256': hashlib.sha256(key[8:8 + bits2 // 8]).hexdigest()})
        elif tag == 1:
            # Names only: validating full hashtrees would require additional large block scans.
            nl = struct.unpack_from('>I', data, pos + 104)[0]
            descriptors.append({'tag': tag, 'partition': data[pos + 180:pos + 180 + nl].decode(errors='replace'),
                                'hashtree_payload_scanned': False})
        pos += 16 + n
    return {'canonical_bytes': 256 + auth + aux, 'canonical_sha256': hashlib.sha256(data[:256 + auth + aux]).hexdigest(),
            'flags': struct.unpack_from('>I', data, 120)[0], 'rollback_index': struct.unpack_from('>Q', data, 112)[0],
            'public_key_bits': bits, 'modulus_sha256': hashlib.sha256(modulus).hexdigest(),
            'rsa_signature_valid': True, 'descriptors': descriptors}


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--offline', action='store_true', help='Recheck private captures without contacting a device')
    args = ap.parse_args()
    snapshot = json.loads((HERE / 'live_readonly_snapshot.json').read_text())
    private = Path(snapshot['private_raw_directory'])
    slot = snapshot['properties']['ro.boot.slot_suffix']
    if slot not in ('_a', '_b'):
        raise ValueError('Unknown active slot')
    serial = None
    if not args.offline:
        rows = [r.split() for r in subprocess.check_output(['adb', 'devices'], text=True).splitlines()[1:] if r.strip()]
        ids = [r[0] for r in rows if len(r) > 1 and r[1] == 'device']
        if len(ids) != 1:
            raise RuntimeError('Need exactly one authorized ADB target')
        serial = ids[0]

    def read_partition(name, length):
        if not re.fullmatch('[a-z0-9_]+', name) or not 0 < length <= 80 * 1024 * 1024:
            raise ValueError('Unexpected read target/size')
        target = private / (name + f'_prefix_{length}.bin')
        if args.offline:
            legacy = private / (name + '_avb_read.bin')
            source = target if target.exists() else legacy
            data = source.read_bytes()
            if len(data) < length:
                raise ValueError('Private capture is too short: ' + name)
            return data[:length]
        command = 'su -c ' + shlex.quote(f'head -c {length} /dev/block/by-name/{name}')
        result = subprocess.run(['adb', '-s', serial, 'exec-out', command], capture_output=True, timeout=90)
        if result.returncode or len(result.stdout) != length:
            raise RuntimeError('Expected block read failed: ' + name)
        target.write_bytes(result.stdout)
        return result.stdout

    root_name = 'vbmeta' + slot
    root = parse((private / (root_name + '.bin')).read_bytes())
    metadata = [dict(partition=root_name, **root)]
    checks = []
    queue = list(root['descriptors'])
    for item in list(queue):
        if item['tag'] != 4:
            continue
        name = item['partition']
        if name in ('vbmeta_system', 'vbmeta_vendor'):
            data = (private / (name + slot + '.bin')).read_bytes()
            parsed = parse(data)
        elif name == 'boot':
            if args.offline:
                footer = (private / ('boot' + slot + '_avb_footer.bin')).read_bytes()
                embedded = (private / ('boot' + slot + '_embedded_vbmeta.bin')).read_bytes()
            else:
                # Known dash boot partition size, checked read length. No partition modification.
                data = read_partition('boot' + slot, 67108864)
                footer = data[-64:]
            if footer[:4] != b'AVBf':
                raise ValueError('Boot AVB footer absent; cannot claim chain integrity')
            _, _, _, image_size, vbmeta_offset, vbmeta_size = struct.unpack('>4sIIQQQ28x', footer)
            if vbmeta_offset + vbmeta_size > 67108864:
                raise ValueError('Boot vbmeta outside partition')
            if not args.offline:
                embedded = data[vbmeta_offset:vbmeta_offset + vbmeta_size]
                (private / ('boot' + slot + '_avb_footer.bin')).write_bytes(footer)
                (private / ('boot' + slot + '_embedded_vbmeta.bin')).write_bytes(embedded)
            if len(embedded) != vbmeta_size:
                raise ValueError('Embedded vbmeta capture length mismatch')
            parsed = parse(embedded)
        else:
            raise ValueError('Unexamined chained partition: ' + name)
        parsed['chain_key_matches_parent'] = parsed['modulus_sha256'] == item['expected_modulus_sha256']
        metadata.append(dict(partition=name + slot, **parsed))
        queue.extend(parsed['descriptors'])
    for item in queue:
        if item['tag'] != 2:
            continue
        name, size = item['partition'] + slot, item['image_bytes']
        data = read_partition(name, size)
        actual = hashlib.new(item['algorithm'], bytes.fromhex(item['salt_hex']) + data).hexdigest()
        checks.append({'partition': name, 'bytes_hashed': size, 'algorithm': item['algorithm'],
                       'matches_signed_descriptor': actual == item['expected_hash'],
                       'expected_digest': item['expected_hash'], 'actual_digest': actual})
    report = {'evidence_type': 'LIVE_READ_ONLY_AVB_PARTIAL_AUDIT', 'active_slot': slot,
              'root_key_is_known_China': root['modulus_sha256'] == CN_MODULUS_SHA,
              'metadata': metadata, 'hash_descriptors': checks,
              'all_examined_hash_descriptors_match': all(c['matches_signed_descriptor'] for c in checks),
              'no_device_mutation': True,
              'limits': ['Hashtree data blocks, rollback values held in secure storage and earlier boot chain not fully verified',
                         'AVB reads do not replace actual stock locked boot or OTA testing',
                         'Root can affect software observations; no direct hardware attestation is claimed']}
    host = Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images\init_boot.img')
    if host.exists():
        desc = next(d for d in root['descriptors'] if d.get('partition') == 'init_boot')
        stock = host.read_bytes()
        digest = hashlib.new(desc['algorithm'], bytes.fromhex(desc['salt_hex']) + stock[:desc['image_bytes']]).hexdigest()
        report['host_stock_init_boot_reference'] = {'source_path': str(host),
            'file_sha256': hashlib.sha256(stock).hexdigest(), 'signed_bytes': desc['image_bytes'],
            'matches_live_signed_descriptor': digest == desc['expected_hash'], 'salted_digest': digest}
    (HERE / 'live_avb_audit.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'root_key_is_known_China': report['root_key_is_known_China'],
                      'metadata_signatures_checked': len(metadata), 'hash_descriptors': checks,
                      'all_examined_hash_descriptors_match': report['all_examined_hash_descriptors_match']}, indent=2))


if __name__ == '__main__':
    main()

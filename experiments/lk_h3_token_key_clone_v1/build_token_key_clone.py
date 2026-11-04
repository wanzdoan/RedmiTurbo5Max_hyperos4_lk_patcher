"""Patch stock H3 with the current-device token PUBLIC key; never writes RPMB.

No private key or token is created. Existing token verification remains active.
"""
from pathlib import Path
import hashlib
import json
import struct

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
STOCK = ROOT / 'lk_hyperos3_original/lk.img'
REFERENCE = HERE / 'input/reference_current_lk_container.img'
STOCK_SHA = 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb'
REFERENCE_SHA = '69089ebb26e070c202213b19615cad5dcd9202a0ea35e521e4dce18a25599a90'
KEY_SHA = 'a541cc0c42dc1fe116b4706d3d1a3ebdb90048bdd543755928fa81713cd81dca'
NAME = 'lk_hyperos3_STOCK_TOKEN_KEY_MATCH_TEST.img'
KEYS = {0: 0x1121b2, 6: 0xd2464}


def sha(data):
    return hashlib.sha256(data).hexdigest()


def align(n):
    return (n + 15) & ~15


def records(data):
    result, off = [], 0
    for i in range(15):
        if data[off:off + 4] != bytes.fromhex('88168858'):
            raise ValueError(f'Invalid MTK record at {off:x}')
        size = struct.unpack_from('<I', data, off + 4)[0]
        end = align(off + 512 + size)
        if end > len(data):
            raise ValueError('Truncated record')
        result.append({'index': i, 'offset': off, 'size': size, 'end': end,
                       'name': data[off + 8:off + 40].split(b'\0')[0].decode()})
        off = end
    if off != len(data):
        raise ValueError('Expected a trimmed 15-member container')
    return result


def payload(data, row):
    return data[row['offset'] + 512:row['offset'] + 512 + row['size']]


def hash_prefix(header, body):
    oid = bytes.fromhex('0607608676931602')
    result = (b'\xa0\x58' + oid + b'\x04\x03\x21\x00' + hashlib.sha256(header).digest()
              + oid + b'\x01\x03\x21\x00' + hashlib.sha256(body + bytes(align(len(body)) - len(body))).digest())
    if len(result) != 90:
        raise ValueError('Bad hash prefix')
    return result


def build(stock, reference):
    if sha(stock) != STOCK_SHA or sha(reference) != REFERENCE_SHA:
        raise ValueError('Input identity mismatch')
    original, ref = records(stock), records(reference)
    key = payload(reference, ref[0])[KEYS[0]:KEYS[0] + 256]
    if sha(key) != KEY_SHA or payload(reference, ref[6])[KEYS[6]:KEYS[6] + 256] != key:
        raise ValueError('Reference LK/AEE token public keys differ')
    parts = [stock[r['offset']:r['end']] for r in original]
    changes = []
    for index, off in KEYS.items():
        r = original[index]
        part = bytearray(parts[index])
        old = part[512 + off:512 + off + 256]
        part[512 + off:512 + off + 256] = key
        parts[index] = bytes(part)
        cert_index = index + 2
        cert = original[cert_index]
        signed_der = payload(stock, cert)
        if cert['name'] != 'cert2' or cert['size'] != 982 or not signed_der.startswith(bytes.fromhex('308203d2')):
            raise ValueError('Unexpected stock certificate layout')
        header = bytearray(stock[cert['offset']:cert['offset'] + 512])
        struct.pack_into('<I', header, 4, len(signed_der) + 90)
        prefix = hash_prefix(parts[index][:512], parts[index][512:512 + r['size']])
        new_cert = bytes(header) + prefix + signed_der
        new_cert += bytes(align(len(new_cert)) - len(new_cert))
        parts[cert_index] = new_cert
        changes.append({'component': r['name'], 'key_payload_offset': hex(off),
                        'key_whole_file_offset_in_stock': hex(r['offset'] + 512 + off),
                        'rsa_modulus_bytes': 256, 'old_key_sha256': sha(old), 'new_key_sha256': sha(key),
                        'certificate_prefix_bytes': 90, 'original_signed_der_preserved': True})
    output = b''.join(parts)
    after = records(output)
    if len(output) != len(reference):
        raise ValueError('Unexpected output container size')
    for i, r in enumerate(original):
        q = after[i]
        actual = output[q['offset']:q['end']]
        before = stock[r['offset']:r['end']]
        if i not in (0, 2, 6, 8) and actual != before:
            raise ValueError(f'Unexpected modification of member {i}')
        if i in KEYS:
            off = 512 + KEYS[i]
            if actual[:off] != before[:off] or actual[off + 256:] != before[off + 256:]:
                raise ValueError('Unexpected change outside token modulus')
    for i in (0, 3, 6, 9, 12):
        if payload(output, after[i]) != payload(reference, ref[i]):
            raise ValueError(f'Payload {i} differs from current-device reference')
    # Getter remains original, not the earlier unconditional state3 patch.
    if output[0x89a2c:0x89a3c] != stock[0x89a2c:0x89a3c]:
        raise ValueError('Stock lock getter was modified')
    return output, changes


def main():
    result, changes = build(STOCK.read_bytes(), REFERENCE.read_bytes())
    (HERE / NAME).write_bytes(result)
    report = {'status': 'EXPERIMENTAL_NOT_FLASHED', 'artifact': NAME,
              'stock_sha256': STOCK_SHA, 'reference_sha256': REFERENCE_SHA,
              'output_sha256': sha(result), 'output_bytes': len(result),
              'changes': changes, 'all_five_executable_or_dtb_payloads_identical_to_reference': True,
              'stock_code_unchanged': True, 'stock_signed_der_unchanged': True,
              'private_key_available': False, 'token_created_or_installed': False,
              'device_or_rpmb_written': False,
              'limits': ['Requires an existing compatible signed token; not unconditional unlock',
                         'Public key alone cannot generate a valid new token',
                         'Existing device token has not been read or validated in this task',
                         'No real-device flashing, boot or full-chain authentication test']}
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('artifact', 'output_sha256', 'output_bytes', 'status')}, indent=2))


if __name__ == '__main__':
    main()

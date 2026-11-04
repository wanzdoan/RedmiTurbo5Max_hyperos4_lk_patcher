"""Add stock H3 CN/GL boot-key trust to the exact original ENG LK.

Also remove the flash-check region rejection. Preserve the original lock logic
and signature verification; extend key trust, without accepting unknown keys.
"""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha, align

INPUT = ROOT / 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img'
INPUT_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
NAME = 'lk_hyperos3_ENG_STOCK_CN_GLOBAL_BOOT_FLASH_TEST.img'
OFFSET = 0x68e7c
OLD = bytes.fromhex('28020034')  # cbz w8, region-prefix check
PATCH = struct.pack('<I', 0x14000000 | ((0x68e80 - OFFSET) // 4))
STOCK_H3 = ROOT / 'lk_hyperos3_original/lk.img'
STOCK_H3_SHA = 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb'
CN_HASH = '8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e'
GL_HASH = '798f1f2bdeed4ab83d5df6a879170d32ef97cf332894d5f00c668634f38c49cb'
CALLBACK_SHA = '9c6375e3506416df3f7069e87cebe6a0a8a72d7e32d1a03ef977bdd8eadb6a28'


def patches():
    callback = (HERE / 'trust_cn_global.bin').read_bytes()
    if len(callback) != 216 or sha(callback) != CALLBACK_SHA:
        raise ValueError('Unexpected assembled callback')
    stock = STOCK_H3.read_bytes()
    if sha(stock) != STOCK_H3_SHA:
        raise ValueError('Unexpected stock H3 public-key reference')
    for offset, expected in [(0x138638, CN_HASH), (0x138838, GL_HASH)]:
        if sha(stock[512+offset:512+offset+512]) != expected:
            raise ValueError('Stock H3 CN/GL AVB modulus mismatch')
    return [(OFFSET, PATCH, 'CN flash-check region branch'),
            (0x68ec0, bytes.fromhex(CN_HASH + GL_HASH),
             'SHA256 fingerprints in the now-unreachable region-rejection block'),
            (0xc5888, callback, 'Original ENG AVB trust plus known stock CN/GL 4096-bit keys')]


def build(source):
    if sha(source) != INPUT_SHA:
        raise ValueError('Expected preserved original ENG image, not unlocked/fakelock')
    rows = members(source)
    if source[512 + OFFSET:516 + OFFSET] != OLD:
        raise ValueError('Unexpected stock ENG flash-check instruction')
    if len(rows) != 15 or rows[0]['name'] != 'lk' or rows[2]['size'] != 982:
        raise ValueError('Unexpected ENG container')
    main_end = 512 + rows[0]['size']
    changed = bytearray(source)
    changes = []
    allowed = set()
    for offset, code, purpose in patches():
        original = source[512+offset:512+offset+len(code)]
        changed[512+offset:512+offset+len(code)] = code
        allowed.update(range(offset, offset+len(code)))
        changes.append({'payload_offset': hex(offset), 'bytes': len(code),
                        'original': original.hex(), 'new': code.hex(), 'purpose': purpose})
    c = rows[2]
    header = bytearray(source[c['offset']:c['offset'] + 512])
    pref = prefix(bytes(changed[:512]), bytes(changed[512:main_end]))
    struct.pack_into('<I', header, 4, c['size'] + len(pref))
    der = source[c['offset'] + 512:c['offset'] + 512 + c['size']]
    record = bytes(header) + pref + der
    record += bytes(align(len(record)) - len(record))
    result = bytes(changed[:c['offset']]) + record + source[c['end']:]
    after = members(result)
    for i, (a, b) in enumerate(zip(rows, after)):
        if i not in (0, 2) and source[a['offset']:a['end']] != result[b['offset']:b['end']]:
            raise ValueError('Non-main/cert2 member changed')
    differences = [i for i in range(rows[0]['size']) if source[512+i] != result[512+i]]
    if any(i not in allowed for i in differences):
        raise ValueError('Unexpected main change')
    if result[after[2]['offset']+692:after[2]['offset']+692+len(der)] != der:
        raise ValueError('Signed DER changed')
    return result, {
        'status': 'EXPERIMENTAL_NOT_DEVICE_TESTED', 'artifact': NAME,
        'input_sha256': INPUT_SHA, 'output_sha256': sha(result),
        'input_bytes': len(source), 'output_bytes': len(result),
        'payload_patch_offset': hex(OFFSET), 'whole_file_patch_offset': hex(OFFSET+512),
        'old': OLD.hex(), 'new': PATCH.hex(),
        'instruction': 'cbz w8, 0x68ec0 -> b 0x68e80',
        'purpose': 'Retain original ENG lock mechanism; allow known H3 CN/GL boot keys and flash regions',
        'changed_main_bytes': len(differences), 'main_patch_span_bytes': sum(len(p[1]) for p in patches()),
        'patches': changes,
        'public_key_reference_sha256': STOCK_H3_SHA,
        'cn_4096_modulus_sha256': CN_HASH, 'global_4096_modulus_sha256': GL_HASH,
        'original_eng_2048_key_preserved': True, 'unknown_keys_trusted': False,
        'original_eng_lock_getter_and_setter_preserved': True,
        'unlock_or_fakelock_added': False, 'persistent_lock_changed': False,
        'boot_signature_verification_preserved': True, 'boot_key_trust_extended': True,
        'all_other_members_byte_identical': True, 'all_signed_der_unchanged': True,
        'main_cert2_hash_prefix_bytes': len(pref), 'device_flashed': False,
        'main_cert2_zero_padding_before': c['end']-(c['offset']+512+c['size']),
        'main_cert2_zero_padding_after': after[2]['end']-(after[2]['offset']+512+after[2]['size']),
        'limits': [
            'The flash-check region restriction is removed for all region strings, not only Global',
            'RPMB unprovisioned plus locked rejection remains active',
            'Original stored lock state is respected; this file neither unlocks nor relocks a device',
            'Signature verification, rollback, hardware model/layout and earlier boot stages remain unchanged',
            'Only the two known stock H3 CN/GL 4096-bit moduli are added, not all signed ROMs or all Global builds',
            'The new callback strictly checks serialized AVB key sizes; malformed inputs can be rejected earlier',
            'No target Global ROM signature/boot chain or physical flash/boot tested',
            'Passing flash_check and key-trust probes is not proof that a full ROM can be written or booted',
            'Main hash prefix uses the same native ENG BL2 parser behavior; not a new vendor signature'
        ]}


def main():
    output, report = build(INPUT.read_bytes())
    (HERE / NAME).write_bytes(output)
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('artifact', 'output_sha256', 'output_bytes', 'status')}, indent=2))


if __name__ == '__main__':
    main()

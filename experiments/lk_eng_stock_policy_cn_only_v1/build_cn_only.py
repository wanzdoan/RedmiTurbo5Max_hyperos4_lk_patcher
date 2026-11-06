"""Original ENG stock policy + China boot key, with optional native MTE-off."""
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
STOCK = ROOT / 'lk_hyperos3_original/lk.img'
STOCK_SHA = 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb'
CN_HASH = '8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e'
CALLBACK_SHA = '2fb1cb19ba624728772010615641e356be24c41539ff0f380ed0eeb00c5b787c'
NAMES = {'default': 'lk_ENG_STOCK_POLICY_CHINA_ONLY_TEST.img',
         'mte_off': 'lk_ENG_STOCK_POLICY_CHINA_ONLY_MTE_OFF_TEST.img'}


def build(source, kind):
    if kind not in NAMES: raise ValueError('Unknown variant')
    if sha(source) != INPUT_SHA: raise ValueError('Expected original ENG, not an unlocked/fakelock/global image')
    stock = STOCK.read_bytes()
    if sha(stock) != STOCK_SHA or sha(stock[512 + 0x138638:512 + 0x138838]) != CN_HASH:
        raise ValueError('China public-key reference differs')
    callback = (HERE / 'trust_eng_cn.bin').read_bytes()
    if len(callback) != 216 or sha(callback) != CALLBACK_SHA or bytes.fromhex(CN_HASH) not in callback:
        raise ValueError('Unexpected assembled China-only callback')
    rows = members(source); main = bytearray(source[512:512 + rows[0]['size']])
    operations = [(0xc5888, callback, 'Original ENG2048 or stock China4096 only; Global not added')]
    if kind == 'mte_off':
        if main[0x34d20:0x34d24] != bytes.fromhex('20020054'): raise ValueError('MTE branch differs')
        operations.append((0x34d20, bytes.fromhex('11000014'), 'Force existing native MTE suppression branch'))
    allowed = set(); changes = []
    for off, code, purpose in operations:
        changes.append(dict(payload_offset=hex(off), whole_file_offset=hex(off + 512), bytes=len(code),
                            old=bytes(main[off:off + len(code)]).hex(), new=code.hex(), purpose=purpose))
        main[off:off + len(code)] = code; allowed.update(range(off, off + len(code)))
    original_main = source[512:512 + rows[0]['size']]
    if any(a != b and i not in allowed for i, (a, b) in enumerate(zip(original_main, main))):
        raise ValueError('Undeclared main modification')
    for lo, hi in [(0x1320c, 0x13a00), (0x68bfc, 0x68f00), (0x8850c, 0x88c80),
                   (0x896e4, 0x89e00), (0xc208, 0xc3b4), (0xc4de0, 0xc5018), (0x69da4, 0x69e94)]:
        if main[lo:hi] != original_main[lo:hi]: raise ValueError('Original HWC/region/lock/boot/reporting changed')
    cert = rows[2]
    if cert['size'] != 982: raise ValueError('Unexpected original main cert2')
    c = cert['offset']; der = source[c + 512:c + 512 + cert['size']]
    header = bytearray(source[c:c + 512]); struct.pack_into('<I', header, 4, cert['size'] + 180)
    record = bytes(header) + prefix(source[:512], bytes(main)) + der
    record += bytes(align(len(record)) - len(record))
    first = source[:512] + bytes(main) + source[512 + rows[0]['size']:rows[0]['end']]
    output = first + source[rows[0]['end']:c] + record + source[cert['end']:]
    after = members(output)
    for i, (old, new) in enumerate(zip(rows, after)):
        if i not in (0, 2) and source[old['offset']:old['end']] != output[new['offset']:new['end']]:
            raise ValueError('Other component changed')
    new_c = after[2]['offset'] + 512
    if output[new_c + 180:new_c + 180 + len(der)] != der: raise ValueError('Original signed DER changed')
    report = dict(status='EXPERIMENTAL_STOCK_POLICY_CN_ONLY_NOT_DEVICE_BOOTED', variant=kind,
                  artifact=NAMES[kind], input_sha256=INPUT_SHA, output_sha256=sha(output), bytes=len(output),
                  patches=changes, china_modulus_sha256=CN_HASH, original_eng_key_preserved=True,
                  global_key_trust_added=False, hwc_code_unchanged=True, hwc_policy='Original hardware HWC; CN on China hardware',
                  china_flash_region_check_unchanged=True, original_lock_getter_setter_and_avb_policy_unchanged=True,
                  force_unlock_or_fakelock_added=False, non_main_members_and_original_der_unchanged=True,
                  mte_policy='Original' if kind == 'default' else 'Native LK suppression emits arm64.nomte',
                  device_flashed=False, device_boot_tested=False,
                  limits=['Stock policy follows stored state; no real lock/unlock operation performed',
                          'Original unlocked-mode allowances or factory/config bypasses are unchanged',
                          'Modified init_boot may be refused under locked verification; root/OTA/Integrity not proved',
                          'MTE-off is kernel-facing, not reversal of earlier preloader execution'])
    return output, report


if __name__ == '__main__':
    source = INPUT.read_bytes()
    for kind, name in NAMES.items():
        image, report = build(source, kind)
        (HERE / name).write_bytes(image)
        (HERE / (kind + '_manifest.json')).write_text(json.dumps(report, indent=2) + '\n')
        print(kind, report['output_sha256'], len(image))

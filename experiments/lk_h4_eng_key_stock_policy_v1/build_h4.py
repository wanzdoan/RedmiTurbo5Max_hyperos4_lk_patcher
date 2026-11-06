"""H4 main stock policy + ENG AVB public key + intact signed H3 ENG BL2.

Not a conversion into the manufacturer's full engineering build. Never flashes.
"""
from pathlib import Path
import json
import struct
import subprocess
import sys
import tempfile
from elftools.elf.elffile import ELFFile

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha, align, H4_SHA

H4 = ROOT / 'lk_hyperos4_original/lk.img'
ENG = ROOT / 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img'
ENG_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
CN_SHA = '8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e'
ENTRY, END = 0xc5978, 0xc5b18
NAME = 'lk_H4_ENG_KEY_STOCK_POLICY_H3ENG_BL2_TEST.img'


def assemble(eng):
    modulus = eng[512 + 0x130728:512 + 0x130828]
    with tempfile.TemporaryDirectory(prefix='lk-h4-eng-key-') as folder:
        tmp = Path(folder)
        (tmp / 'eng_modulus.bin').write_bytes(modulus)
        obj = tmp / 'callback.o'
        subprocess.run(['clang', '-target', 'aarch64-none-elf', '-c',
                        str(HERE / 'trust_eng_cn.S'), '-o', str(obj)], cwd=tmp, check=True)
        with obj.open('rb') as stream:
            elf = ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text', '.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved relocation')
            code = elf.get_section_by_name('.text').data()
    assert len(code) == END - ENTRY and code.count(modulus) == 1
    return code


def build(h4, eng):
    if sha(h4) != H4_SHA or sha(eng) != ENG_SHA:
        raise ValueError('Exact original H4 and H3 ENG required')
    old, donor = members(h4), members(eng)
    assert sha(h4[512 + 0x138658:512 + 0x138858]) == CN_SHA
    code = assemble(eng)
    main = bytearray(h4[512:512 + old[0]['size']])
    main[ENTRY:END] = code
    cert = old[2]
    der = h4[cert['offset'] + 512:cert['offset'] + 512 + cert['size']]
    assert cert['size'] == 982
    header = bytearray(h4[cert['offset']:cert['offset'] + 512])
    struct.pack_into('<I', header, 4, len(der) + 180)
    record = bytes(header) + prefix(h4[:512], bytes(main)) + der
    record += bytes(align(len(record)) - len(record))
    triple = eng[donor[3]['offset']:donor[5]['end']]
    image = (h4[:512] + bytes(main) + h4[512 + len(main):old[0]['end']]
             + h4[old[1]['offset']:old[1]['end']] + record
             + triple + h4[old[6]['offset']:])
    rows = members(image)
    result_main = image[512:512 + rows[0]['size']]
    original_main = h4[512:512 + old[0]['size']]
    assert result_main[:ENTRY] == original_main[:ENTRY]
    assert result_main[END:] == original_main[END:]
    for i in range(1, 15):
        if i == 2: continue
        source, row = (eng, donor[i]) if i in (3, 4, 5) else (h4, old[i])
        assert image[rows[i]['offset']:rows[i]['end']] == source[row['offset']:row['end']]
    report = dict(status='OFFLINE_EXPERIMENT_NOT_BOOT_TESTED', artifact=NAME,
        input_h4_sha256=sha(h4), input_h3_eng_sha256=sha(eng),
        output_sha256=sha(image), bytes=len(image), members=rows,
        main_callback=dict(payload_offset=hex(ENTRY), end=hex(END), bytes=len(code),
                           original_sha256=sha(original_main[ENTRY:END]), sha256=sha(code)),
        signed_eng_bl2_triple_sha256=sha(triple),
        component_origin={'main': 'H4; only AVB trust callback modified',
                          'BL2_and_certificates': 'Original H3 ENG signed triple',
                          'AEE_DTB_DTBO': 'Original H4'},
        force_lock=False, force_unlock=False, fake_green=False,
        mte='Original H4', hwc_and_flash_region='Original H4',
        stock_lock_getter_setter_avb_flags_root_handoff_unchanged=True,
        device_flash_or_lock_performed=False,
        limits=['Not full H4 engineering firmware: H4 release main retains release behavior',
                'Only CN4096 and original H3 ENG2048 accepted in top-level AVB key callback',
                'Recovery-specific and chained AVB keys retain original H4 behavior',
                'No bypass of modified boot hashes or signatures; locked rooted boot may fail',
                'No real relock, preloader compatibility, boot, OTA or Play Integrity proof',
                'Uses external main hash prefix; retained OEM DER is not re-signed'])
    return image, report


if __name__ == '__main__':
    image, report = build(H4.read_bytes(), ENG.read_bytes())
    (HERE / NAME).write_bytes(image)
    (HERE / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
    print(report['output_sha256'], len(image))

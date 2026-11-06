"""Offline H4 stock-policy/auto-HWC functional port; not full engineering source."""
from pathlib import Path
import hashlib
import json
import struct
import subprocess
import sys
import tempfile
from elftools.elf.elffile import ELFFile

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT/'fresh_analysis'))
from build_candidate import members, prefix, sha, align, H4_SHA
sys.path.insert(0, str(ROOT/'experiments/lk_global_region_v1'))
from build_region_lk import patches as h4_region_patches, branch, adr
H4 = ROOT/'lk_hyperos4_original/lk.img'
ENG = ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img'
ENG_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
REFERENCE_ZIP = ROOT/'experiments/lk_h3_eng_mte_off_v1/lk_eng_all_region_6_files.zip'
ENTRY, END, HWC_ENTRY = 0xc5978, 0xc5b18, 0xc5aa0
NAMES = {'default':'lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_TEST.img',
         'mte_off':'lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_MTE_OFF_TEST.img'}


def assemble(h4, eng, assembly='policy_and_hwc.S', expected_size=416):
    keys = [eng[512+0x130728:512+0x130828],
            h4[512+0x138658:512+0x138858], h4[512+0x138858:512+0x138a58]]
    assert sha(keys[1]) == '8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e'
    assert sha(keys[2]) == '798f1f2bdeed4ab83d5df6a879170d32ef97cf332894d5f00c668634f38c49cb'
    with tempfile.TemporaryDirectory(prefix='lk-h4-policy-') as folder:
        tmp = Path(folder); (tmp/'key_hashes.bin').write_bytes(b''.join(hashlib.sha256(k).digest() for k in keys))
        obj = tmp/'code.o'
        subprocess.run(['clang','-target','aarch64-none-elf','-c',str(HERE/assembly),'-o',str(obj)],
                       cwd=tmp, check=True)
        with obj.open('rb') as stream:
            elf = ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text','.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved relocation')
            code = elf.get_section_by_name('.text').data()
    assert len(code) == expected_size
    return code


def operations(h4, eng, kind):
    code = assemble(h4, eng)
    region = h4_region_patches()[:2]  # AVB callback separately replaced with three-key policy.
    off,before,after,purpose=region[1]
    region[1]=(off,before,branch(off,0x13b70)+bytes.fromhex('1f2003d5')*6,
               'Image-key mismatch tries both embedded CN/GL keys independent of hardware region')
    return region + [
        (0x87368, bytes.fromhex('40010034'), branch(0x87368,0x87390), 'Secure-ID region mismatch only; signature/bindings retained'),
        (0x68308, bytes.fromhex('28020034'), branch(0x68308,0x6830c), 'oem flash_check CN/non-CN gate; RPMB gate retained'),
        (ENTRY, h4[512+ENTRY:512+END], code, 'ENG/CN/GL key callback plus isolated late HWC code'),
        (0x13b68, h4[512+0x13b68:512+0x13b70], bytes.fromhex('f3030391')+branch(0x13b6c,0x13bc4),
         'Keep early hardware copy and version pointer; defer HWC emission until countrycode read'),
        (0x13b70, h4[512+0x13b70:512+0x13bc4], assemble(h4,eng,'image_key_fallback.S',84),
         'Reuse unreachable old HWC emission block for CN/GL image-key fallback'),
        (0x1410c, h4[512+0x1410c:512+0x14118], bytes.fromhex('ff7f00a9')+adr(0x14110,0x102002,0)+branch(0x14114,0x14160),
         'Clear partial countrycode on failed/short read'),
        (0x1416c, bytes.fromhex('e0430491'), branch(0x1416c,HWC_ENTRY), 'Emit one HWC then resume original H4 flow'),
        (0xa09c, bytes.fromhex('c8010034'), bytes.fromhex('1f2003d5'), 'Debug-like main command secure flag gate'),
        (0xa0b0, bytes.fromhex('21010054'), bytes.fromhex('1f2003d5'), 'Debug-like main command locked flag gate'),
        (0xce68, bytes.fromhex('21010054'), branch(0xce68,0xce8c), 'Allow flash entry while locked; query errors retained'),
        (0xd250, bytes.fromhex('81000054'), branch(0xd250,0xd260), 'Allow erase entry while locked; query errors retained'),
    ] + ([(0x34740,bytes.fromhex('20020054'),bytes.fromhex('11000014'),'Native H4 kernel-facing arm64.nomte suppression')]
         if kind == 'mte_off' else [])


def build(h4, eng, kind='default'):
    if sha(h4)!=H4_SHA or sha(eng)!=ENG_SHA or kind not in NAMES: raise ValueError('Exact source identity required')
    old, donor = members(h4), members(eng)
    main = bytearray(h4[512:512+old[0]['size']]); allowed=set(); changes=[]
    for off, before, after, purpose in operations(h4,eng,kind):
        assert len(before)==len(after) and main[off:off+len(before)]==before, hex(off)
        main[off:off+len(after)]=after; allowed.update(range(off,off+len(after)))
        changes.append(dict(payload_offset=hex(off), original=before.hex(), replacement=after.hex(),purpose=purpose))
    assert all(a==b or i in allowed for i,(a,b) in enumerate(zip(h4[512:512+len(main)],main)))
    cert=old[2]; der=h4[cert['offset']+512:cert['offset']+512+cert['size']]
    assert len(der)==982
    header=bytearray(h4[cert['offset']:cert['offset']+512]); struct.pack_into('<I',header,4,len(der)+180)
    record=bytes(header)+prefix(h4[:512],bytes(main))+der; record+=bytes(align(len(record))-len(record))
    triple=eng[donor[3]['offset']:donor[5]['end']]
    image=(h4[:512]+bytes(main)+h4[512+len(main):old[0]['end']]+h4[old[1]['offset']:old[1]['end']]
           +record+triple+h4[old[6]['offset']:])
    rows=members(image)
    for i in range(1,15):
        if i==2:continue
        source,row=(eng,donor[i]) if i in (3,4,5) else (h4,old[i])
        assert image[rows[i]['offset']:rows[i]['end']]==source[row['offset']:row['end']]
    return image,dict(status='EXPERIMENTAL_FUNCTIONAL_PORT_NOT_DEVICE_BOOTED',variant=kind,artifact=NAMES[kind],
        input_h4_sha256=sha(h4),input_eng_sha256=sha(eng),output_sha256=sha(image),bytes=len(image),patches=changes,
        origins=dict(main='H4 release with declared patches',BL2='Original signed H3 ENG triple',AEE_DTB_DTBO='H4 unchanged'),
        reference='ZIP stock_policy_auto_hwc; user reports working, exact live variant/hash not read back',
        reference_hashes=['fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0',
                          'b2535248b59fd65c8bb855d6bfa49b86ffbef31bcc7b647df0e8ca34a73df606'],
        stored_lock_getter_setter_avb_flags_root_handoff_unchanged=True,force_unlock_or_fakelock=False,
        hardware_cache_unchanged=True,mte='Original H4' if kind=='default' else 'Native H4 arm64.nomte',
        limits=['Functional patch port, not full H4 ENG firmware or original recovered source',
                'H4 main/AEE release paths still exist; only listed main fastboot gates opened, query errors retained',
                'Top-level trust only known ENG/CN/GL keys; unknown keys/model/layout/rollback can fail',
                'Modified init_boot can still fail locked verification; no fake green or root hash bypass',
                'No device flash/lock, stock preloader H4 boot, banking, OTA or Integrity verified',
                'HWC mapping changes outgoing bootarg only, not hardware binding or persistent region',
                'Signed BL2 triple retained but physical preloader acceptance not established'])


if __name__=='__main__':
    for kind,name in NAMES.items():
        image,report=build(H4.read_bytes(),ENG.read_bytes(),kind)
        (HERE/name).write_bytes(image); (HERE/(kind+'_manifest.json')).write_text(json.dumps(report,indent=2)+'\n')
        print(kind,report['output_sha256'],len(image))

"""Tie exact shipped LK hashes to H4 preloader native validation and controls."""
import itertools
import json
import sys
from native_cert import *
from native_locator import locate
from cryptography.exceptions import InvalidSignature
sys.path.insert(0,str(ROOT/'fresh_analysis/eng_family_compatibility_20261105'))
from compare_families import certificate_check
sys.path.insert(0,str(ROOT/'fresh_analysis/preloader_compatibility_20261105'))
from review_preloaders import policy_run
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run
sys.path.insert(0,str(ROOT/'fresh_analysis/preloader'))
from emulate_bl2_parser import run as release_parser

PATHS={
 'h4_unlocked_boot_reported':('fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img','f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'),
 'h3_eng_original':('experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img','6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'),
 'h3_eng_stock_auto':('experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img','fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0'),
 'h3_eng_stock_auto_off':('experiments/lk_h3_eng_mte_off_v1/stock_policy_auto_hwc/lk_hyperos3_ENG_STOCK_AUTO_HWC_MTE_OFF_TEST.img','b2535248b59fd65c8bb855d6bfa49b86ffbef31bcc7b647df0e8ca34a73df606'),
 'h4_new_stock_auto':('experiments/lk_h4_eng_policy_auto_hwc_v1/lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_TEST.img','da05dc16e6a2e1d48a7f824a9382cf9a4dc4ce3ad1fe58387ca34519a65cbf3c'),
 'h4_new_stock_auto_off':('experiments/lk_h4_eng_policy_auto_hwc_v1/lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_MTE_OFF_TEST.img','4206c420124ec5d2e95a7e6fe22fe81b05c405abd7940a74c7e6501d8b7dc893'),
 'h4_original_control':('lk_hyperos4_original/lk.img','82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b'),
}


def split(image):
    rows=members(image);a,b,c=rows[3:6]
    header=image[a['offset']:a['offset']+512]
    payload=image[a['offset']+512:a['offset']+512+a['size']]
    cert1=image[b['offset']+512:b['offset']+512+b['size']]
    record=image[c['offset']:c['offset']+512+c['size']]
    return rows,header,payload,cert1,record


def main():
    fw=FW.read_bytes();assert sha(fw)==FW_SHA
    images={label:(ROOT/path).read_bytes() for label,(path,_) in PATHS.items()}
    entries={};families={};root_reference=None
    for label,image in images.items():
        path,expected=PATHS[label];assert sha(image)==expected
        rows,header,payload,c1,record=split(image);c2=record[512:]
        a=certificate_check(c1);b=certificate_check(c2)
        assert c1[496:790]==c2[181:475]
        if root_reference is None:root_reference=c1[181:475]
        assert c1[181:475]==root_reference
        location=locate(image);assert location['status']=='0x0' and int(location['member_offset'],16)==rows[3]['offset']
        result=run(record,c1[496:790],component_header=header,component_payload=payload)
        assert result['return_status']==0 and all(item['match'] for item in result['content_checks'])
        assert result['native_entries']=={'0xa5c68':1,'0xa6760':1,'0xa6100':1,'0x9c700':1}
        main_cert=rows[2];main_record=image[main_cert['offset']:main_cert['offset']+512+main_cert['size']]
        triple=image[rows[3]['offset']:rows[5]['end']];triple_hash=sha(triple)
        families.setdefault(triple_hash,(label,header,payload,c1,record))
        parsers=[]
        for mask in (0x60,0x1f4e2,0x1f400):
            if label.startswith('h3_eng') or label.startswith('h4_new'):
                parser=bl2_run(payload,main_record,mask)
                assert parser['result']==0
            else:
                version=4 if label=='h4_original_control' else 3
                cached=(ROOT/f'fresh_analysis/container_chain/h{version}_original_03_bl2_ext.bin').read_bytes()
                assert cached==payload
                parser=release_parser(version,label,mask,certificate_record=main_record)
                assert parser['returned'] and parser['error'] is None and parser['status']=='0x0'
            if mask!=0x1f400:
                assert parser['header_hash']==sha(image[:512])
                assert parser['payload_hash']==sha(image[512:rows[0]['end']])
            parsers.append(parser)
        entries[label]=dict(path=path,sha256=sha(image),bytes=len(image),bl2_triple_sha256=triple_hash,
            cert1=a,cert2=b,cert1_to_cert2_image_key_binding=True,root_spki_same_as_boot_reported_reference=True,
            locator=location,preloader_bl2_certificate_and_content=result,bl2_main_hash_parser=parsers,
            actual_device_boot_reported=label=='h4_unlocked_boot_reported',
            scope='Bounded offline checks; no complete loader/rollback/hardware boot')
    controls=[]
    for _,(family,header,payload,c1,record) in families.items():
        for failure in ('signature','signed_payload_digest','trusted_key','magic','header','payload'):
            rec=bytearray(record);hh=bytearray(header);pp=bytearray(payload);spki=c1[496:790]
            if failure=='signature':rec[-1]^=1
            elif failure=='signed_payload_digest':
                off=rec.index(bytes.fromhex('060760867693160201'))+12;rec[off]^=1
            elif failure=='trusted_key':spki=c1[181:475] # OEM root is not the certified image key
            elif failure=='magic':rec[0]^=1
            elif failure=='header':hh[0x80]^=1
            elif failure=='payload':pp[0x100]^=1
            result=run(bytes(rec),spki,component_header=bytes(hh),component_payload=bytes(pp))
            if failure in ('header','payload'):
                assert result['return_status']==0
                assert [item['match'] for item in result['content_checks']]==([False,True] if failure=='header' else [True,False])
            else:assert result['return_status']!=0,(family,failure,result)
            controls.append(dict(family_reference=family,fault=failure,correctly_rejected_at_tested_boundary=True,result=result))
        bad=bytearray(c1);bad[-1]^=1
        try:certificate_check(bytes(bad))
        except InvalidSignature:controls.append(dict(family_reference=family,fault='cert1_signature',host_crypto_rejected=True))
        else:raise AssertionError('Bad cert1 accepted')
    bad=bytearray(images['h4_new_stock_auto']);bad[0]^=1
    negative_locator=locate(bytes(bad));assert negative_locator['status']!='0x0'
    policy=[]
    for sb,state,serr,derr in itertools.product((0,1),(0,1,2,3,4,0xffffffff),(0,7),(0,7)):
        value=policy_run(fw,sb,state,0x8cc18,sbc_error=serr,state_error=derr)
        assert value in (0,1)
        if not serr and not derr and state in (3,4):assert value==sb
        policy.append(dict(sbc_fixture=sb,state_fixture=state,sbc_error=serr,state_error=derr,auth_required=value))
    eng_triple=entries['h3_eng_original']['bl2_triple_sha256']
    for name in ('h3_eng_stock_auto','h3_eng_stock_auto_off','h4_new_stock_auto','h4_new_stock_auto_off'):
        assert entries[name]['bl2_triple_sha256']==eng_triple
    assert eng_triple!=entries['h4_unlocked_boot_reported']['bl2_triple_sha256']
    report=dict(status='PASS_OFFLINE_NATIVE_AUTH_COMPONENTS_NOT_DEVICE_ACCEPTANCE',
        preloader=dict(path=str(FW.relative_to(ROOT)),sha256=sha(fw),bytes=len(fw),linked_base=hex(BASE)),
        images=entries,negative_controls=controls,negative_locator=negative_locator,policy=policy,
        counts=dict(images=len(entries),native_cert2_positive=len(entries),native_hash_pairs=len(entries),
                    native_member_locators=len(entries)+1,bl2_main_parsers=3*len(entries),negative_controls=len(controls),
                    native_policy=len(policy)),
        conclusions=['Exact H4 preloader finds BL2 at correct member offsets in all seven containers',
            'All seven BL2 cert2 inputs pass native ASN.1/key compare/RSA-PSS under certified image-key fixtures',
            'Native aligned hash helper and byte comparator match both BL2 header and payload; corruptions fail',
            'H3 ENG stock-policy normal/off and H4 new normal/off have the exact same signed ENG BL2 triple',
            'No demonstrated certificate/hash/record-layout rejection of ENG BL2 by this H4 preloader in tested paths',
            'Cannot turn these results into unconditional acceptance or full boot on a physical device'],
        limits=['Actual efuse root, SBC/device state, rollback counters and primary provisioning not read',
                'Certified image key supplied from validated embedded cert1, not extracted live from device',
                'Cert1 signatures/binding checked by host cryptography; cert1 complete native loader path not replayed',
                'Cert2 ASN.1, key extraction/comparison, PSS/MGF, padded hash helper and comparator run native',
                'SHA256/RSA exponentiation use host math, heap/storage/readiness/mode/guard fixtures, PAC/logging bypassed',
                'No whole generic loader, rollback sequence, hardware handoff or full SoC boot',
                'BL2 main parser tests field selection only; not complete BL2 signature/load execution',
                'No phone flash/lock/reboot/erase, no firmware bytes changed'])
    (HERE/'evidence.json').write_text(json.dumps(report,indent=2)+'\n')
    print(report['status']);print(json.dumps(report['counts']));print('All corruption controls rejected; ENG BL2 triple identical across four patched variants')


if __name__=='__main__':main()

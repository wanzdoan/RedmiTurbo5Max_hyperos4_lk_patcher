"""Native all-region/unlock/MTE regression on final v2 images; no device IO."""
import gc
import itertools
import json
import struct
from build_all_region import *
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0,str(ROOT/'experiments/lk_h4_eng_policy_auto_hwc_v1'))
from hwc_cases import flow_case
from validate_port import execute,DATA
from validate_behavior import gate
sys.path.insert(0,str(ROOT/'experiments/lk_h4_booted_hybrid_mte_off_v1'))
from validate_mte_off import unlock_case,mte_case
sys.path.insert(0,str(ROOT/'experiments/lk_global_region_v1'))
from validate_region_lk import Replay as RegionReplay


def cryptographic_checks(image,kind):
    rows=members(image);certs=[]
    for i,row in enumerate(rows):
        if row['name'] not in ('cert1','cert2'):continue
        raw=image[row['offset']+512:row['offset']+512+row['size']];der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        if row['name']=='cert2':
            component=rows[i-2];start=component['offset']
            for oid,data in [('060760867693160204',image[start:start+512]),('060760867693160201',image[start+512:component['end']])]:
                at=der.index(bytes.fromhex(oid))+12
                assert (der[at:at+32].hex()==sha(data))==(i!=2 or oid.endswith('0204'))
        certs.append(dict(member=i,retained_der_signature_valid=True))
    c=rows[2];record=image[c['offset']:c['offset']+512+c['size']]
    assert record[512:692]==prefix(image[:512],image[512:rows[0]['end']])
    bl2=rows[3]
    assert image[bl2['offset']+512:bl2['offset']+512+bl2['size']]==(ROOT/'fresh_analysis/container_chain/h3_original_03_bl2_ext.bin').read_bytes()
    sys.path.insert(0,str(ROOT/'fresh_analysis/preloader'))
    from emulate_bl2_parser import run as parser
    parsers=[]
    for mask in (0x60,0x1f4e2,0x1f400):
        got=parser(3,'h4-all-region-v2-'+kind,mask,certificate_record=record)
        assert got['returned'] and got['error'] is None and got['status']=='0x0'
        if mask!=0x1f400:assert got['payload_hash']==sha(image[512:rows[0]['end']]) and got['header_hash']==sha(image[:512])
        parsers.append(got)
    sys.path.insert(0,str(ROOT/'fresh_analysis/h4_preloader_lk_acceptance_20261106'))
    from native_cert import run as preloader_cert
    from native_locator import locate
    a,b,c=rows[3:6];cert1=image[b['offset']+512:b['offset']+512+b['size']]
    result=preloader_cert(image[c['offset']:c['offset']+512+c['size']],cert1[496:790],component_header=image[a['offset']:a['offset']+512],component_payload=image[a['offset']+512:a['offset']+512+a['size']])
    assert result['return_status']==0 and all(row['match'] for row in result['content_checks'])
    locator=locate(image);assert locator['status']=='0x0' and int(locator['member_offset'],16)==a['offset']
    return dict(der=certs,release_bl2_parsers=parsers,stock_h4_preloader_boundary=result,stock_h4_preloader_locator=locator)


def region_cases(image):
    cases=[]
    for hw,sku in itertools.product((0,1),('CN','GLOBAL','EEA','TW','IN','NATIVE','')):
        replay=RegionReplay(image,hw=hw,sku=sku,block=True)
        assert replay.run(0x68f50,False)=='continued'
        cases.append(dict(gate='sku',hardware=hw,sku=sku,outcome='continued'))
    cn=image[512+0x1121d7:512+0x1122d7];gl=image[512+0x1122d7:512+0x1123d7]
    for hw,key_label,ce,ke in itertools.product((0,1),('CN','GL','unknown'),(0,0x1234),(0,0x321)):
        key={'CN':cn,'GL':gl,'unknown':bytes(256)}[key_label]
        replay=RegionReplay(image,hw=hw,key=key,chain_error=ce,key_error=ke)
        replay.u.reg_write(UC_ARM64_REG_X1,DATA+0x800);replay.u.mem_write(DATA+0x800,b'md1rom\0')
        result=replay.run(0x88734)
        assert result==(ce or ke or (0 if key_label in ('CN','GL') else 0xffffffff))
        assert replay.calls[0]=='certificate_chain_verify'
        cases.append(dict(gate='image_key',hardware=hw,key=key_label,chain_error=ce,key_error=ke,result=result))
    for hw,sku in itertools.product((0,1),('CN','GLOBAL','EEA','TW','IN','')):
        got=gate(image,'secure_region',hw,name=sku);assert got['allowed'];cases.append(got)
    return cases


def main():
    donor=UNLOCKED.read_bytes();images={};variants=[]
    stock=(ROOT/'lk_hyperos4_original/lk.img').read_bytes()
    eng=(ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img').read_bytes()
    for kind,cfg in VARIANTS.items():
        source=(POLICY_DIR/BASES[cfg['base']]['name']).read_bytes();image=(HERE/cfg['artifact']).read_bytes()
        assert image==build(source,donor,kind)[0]==build(source,donor,kind)[0]
        for bad_source in (True,False):
            wrong=bytearray(source if bad_source else donor);wrong[12345]^=1
            try:build(bytes(wrong) if bad_source else source,donor if bad_source else bytes(wrong),kind)
            except ValueError:pass
            else:raise AssertionError('Wrong input accepted')
        images[kind]=image
        hwc=[flow_case(image,True,country,hw,suffix=slot) for country,hw,slot in itertools.product(
            ('cn','CN','Cn','cN','global','GLOBAL','eea','EEA','tw','TW','in','id','unknown',''),('CN','GLOBAL'),('_a','_b'))]
        hwc += [flow_case(image,True,country,hw,mode=mode) for country,hw,mode in itertools.product(
            ('cn','global'),('CN','GLOBAL'),('missing','empty','short','io_error'))]
        keys=[]
        for label,key in [('CN',stock[512+0x138658:512+0x138858]),('GL',stock[512+0x138858:512+0x138a58]),('ENG',eng[512+0x130728:512+0x130828])]:
            for corrupted in (False,True):
                data=bytearray(key)
                if corrupted:data[len(data)//2]^=1
                blob=struct.pack('>II',len(key)*8,0)+data+bytes(len(key))
                result,memory,_=execute(image,0xc5978,{DATA+0x800:bytes(blob),UC_ARM64_REG_X1:DATA+0x800,
                    UC_ARM64_REG_X2:len(blob),UC_ARM64_REG_X5:DATA+0x700})
                assert result==0 and memory[0x700]==int(not corrupted)
                keys.append(dict(key=label,corrupted=corrupted,trusted=memory[0x700]))
        mte=[mte_case(image,bits,reserved,cfg['base']=='off') for bits,reserved in itertools.product(
            tuple(range(8))+(0x405,0x407,0xf07,0xffffffff),(False,True))]
        row=dict(variant=kind,sha256=sha(image),bytes=len(image),deterministic_rebuild=True,wrong_inputs_rejected=True,
                 hwc_cases=hwc,key_cases=keys,mte_cases=mte)
        counts=dict(hwc=len(hwc),keys=len(keys),mte=len(mte))
        if cfg['unlock']:
            internal=[unlock_case(image,entry,initial) for entry,initial in itertools.product(
                (0x89aec,0x89020,0x88a10),(0,1,2,3,4,0xffffffff))]
            internal += [unlock_case(image,0x68c94,0,boot,control) for boot,control in itertools.product(
                (0,1,2,0xffffffff),(None,0,1,2,3,4,0xffffffff))]
            regions=region_cases(image)
            gates=[]
            for cmd,state,error,name in itertools.product(('flash','erase'),(0,1),(0,7),('boot','lk')):
                got=gate(image,cmd,state,error=error,name=name);assert got['allowed']==(error==0);gates.append(got)
            row.update(unlock_cases=internal,region_cases=regions,flash_gate_cases=gates)
            counts.update(internal_unlock=len(internal),regions=len(regions),flash_gates=len(gates))
            row['cryptographic_checks']=cryptographic_checks(image,kind)
            sys.path.insert(0,str(ROOT/'fresh_analysis/h4_recovery_v3_20261106'))
            from slot_replay import Replay as SlotReplay
            replay=SlotReplay(image,'h4',state=3,flags=1,execute_force_getter=True)
            slot=replay.run();assert slot['returned'] and slot['error'] is None and slot['result']==0
            assert 'androidboot.vbmeta.device_state=unlocked' in slot['cmdline']
            loader=replay.load();assert loader['reached_copy_sink'] and loader['error'] is None
            packets=replay.handoff();assert packets['packets'][1][1]==0 and packets['packets'][0][1:5]==[0,0,0,0]
            row.update(native_slot=slot,native_loader=loader,native_root_handoff=packets)
            del replay;gc.collect()
        else:assert image==source
        row['counts']=counts;variants.append(row);print(kind,'PASS',sha(image),counts,flush=True)
    for state in ('locked','unlocked'):
        normal,off=images[state+'_default'],images[state+'_off'];rows=members(normal);at=rows[2]['offset']+512
        allowed=set(range(0x34940,0x34944))|set(range(at,at+180))
        assert len(normal)==len(off) and all(a==b or i in allowed for i,(a,b) in enumerate(zip(normal,off)))
    total=sum(sum(row['counts'].values()) for row in variants)
    report=dict(status='PASS_ALL_REGION_UNLOCKED_V2_OFFLINE_NOT_NEW_DEVICE_BOOT',variants=variants,total_main_cpu_cases=total,
        all_four_auto_hwc=True,each_mte_pair_only_branch_and_prefix_differ=True,
        limits=['Two locked outputs are exact retained v3; two unlocked outputs are new and not physically boot-tested',
                'Public AVB state and root packet remain genuinely unlocked in the new variants; no fake locked/green',
                'IO/heap/formatter/hardware/rollback/OEM key and secure-monitor boundaries use fixtures; no actual TEE or attestation',
                'Full native slot/loader uses supplied clean China H4 ROM; no actual Global/EEA/TW ROM boot validation',
                'Default MTE is original conditional policy; OFF only forces LK arm64.nomte, not preloader execution',
                'Preloader locator/certificate boundary passing is not full hardware acceptance',
                'AEE/DTBs/persistent setter/BL2/DER kept; not a full H4 ENG build or OTA/Integrity fix'])
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    print(report['status'],total,'main cases')


if __name__=='__main__':main()

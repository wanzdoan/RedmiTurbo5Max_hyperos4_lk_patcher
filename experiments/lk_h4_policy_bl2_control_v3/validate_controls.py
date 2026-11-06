"""Validate exact control differences, signed loaders and native AVB behavior."""
import json
from build_controls import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
sys.path.insert(0,str(ROOT/'fresh_analysis/h4_preloader_lk_acceptance_20261106'))
from native_cert import run as native_cert
from native_locator import locate
sys.path.insert(0,str(ROOT/'fresh_analysis/preloader'))
from emulate_bl2_parser import run as release_parser
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run
sys.path.insert(0,str(ROOT/'fresh_analysis/h4_recovery_v3_20261106'))
from slot_replay import Replay


def validate_reference():
    donor=DONOR.read_bytes();assert sha(donor)==DONOR_SHA and donor[0x89cec:0x89cfc]==PATCH
    replay=Replay(donor,'h4',state=3,flags=1,execute_force_getter=True)
    slot=replay.run();assert slot['returned'] and slot['error'] is None and slot['result']==0
    assert 'androidboot.vbmeta.device_state=unlocked' in slot['cmdline']
    loader=replay.load();assert loader['reached_copy_sink'] and loader['error'] is None
    result=dict(status='PASS_NATIVE_REFERENCE_NOT_NEW_DEVICE_TEST',sha256=sha(donor),native_slot=slot,native_loader=loader)
    (HERE/'booted_reference_validation.json').write_text(json.dumps(result,indent=2)+'\n')
    print('boot-confirmed f57 reference: native slot/load PASS')


def main():
    report=[];donor=DONOR.read_bytes()
    for kind,cfg in VARIANTS.items():
        source=(BASE/cfg['source']).read_bytes();out=(HERE/cfg['output']).read_bytes()
        assert build(source,donor,kind)[0]==out==build(source,donor,kind)[0]
        wrong=bytearray(source);wrong[12345]^=1
        try:build(bytes(wrong),donor,kind)
        except ValueError:pass
        else:raise AssertionError('Wrong source accepted')
        rows=members(out);certs=[]
        for i,row in enumerate(rows):
            if row['name'] not in ('cert1','cert2'):continue
            raw=out[row['offset']+512:row['offset']+512+row['size']];der=raw[180:] if i==2 else raw
            end=8+int.from_bytes(der[6:8],'big')
            load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
            if row['name']=='cert2':
                component=rows[i-2];off=component['offset']
                oid_h,oid_p=bytes.fromhex('060760867693160204'),bytes.fromhex('060760867693160201')
                assert der[der.index(oid_h)+12:der.index(oid_h)+44].hex()==sha(out[off:off+512])
                match=der[der.index(oid_p)+12:der.index(oid_p)+44].hex()==sha(out[off+512:component['end']])
                assert match==(i!=2)
                if i==2:assert raw[:180]==prefix(out[:512],out[512:512+rows[0]['size']])
            certs.append(dict(index=i,retained_der_signature_valid=True))
        a,b,c=rows[3:6];cert1=out[b['offset']+512:b['offset']+512+b['size']]
        result=native_cert(out[c['offset']:c['offset']+512+c['size']],cert1[496:790],
            component_header=out[a['offset']:a['offset']+512],component_payload=out[a['offset']+512:a['offset']+512+a['size']])
        assert result['return_status']==0 and all(x['match'] for x in result['content_checks'])
        locator=locate(out);assert locator['status']=='0x0' and int(locator['member_offset'],16)==a['offset']
        cert=rows[2];record=out[cert['offset']:cert['offset']+512+cert['size']]
        bl2=out[a['offset']+512:a['offset']+512+a['size']];parsers=[]
        for mask in (0x60,0x1f4e2,0x1f400):
            if cfg['release']:
                assert bl2==(ROOT/'fresh_analysis/container_chain/h3_original_03_bl2_ext.bin').read_bytes()
                got=release_parser(3,kind,mask,certificate_record=record)
                assert got['returned'] and got['status']=='0x0' and got['error'] is None
            else:
                got=bl2_run(bl2,record,mask);assert got['result']==0
            if mask!=0x1f400:
                assert got['header_hash']==sha(out[:512]) and got['payload_hash']==sha(out[512:rows[0]['end']])
            parsers.append(got)
        forced=not cfg['release'];state=3 if forced else 4
        replay=Replay(out,'h4',state=state,flags=1 if forced else 0,execute_force_getter=forced)
        slot=replay.run();assert slot['returned'] and slot['error'] is None and slot['result']==0,(kind,slot)
        assert ('androidboot.vbmeta.device_state='+('unlocked' if forced else 'locked')) in slot['cmdline']
        loader=replay.load();assert loader['reached_copy_sink'] and loader['error'] is None
        packets=replay.handoff()
        assert packets['packets'][1][1]==int(not forced)
        if forced:assert packets['packets'][0][1:5]==[0,0,0,0]
        report.append(dict(variant=kind,output_sha256=sha(out),bytes=len(out),deterministic_rebuild=True,
            wrong_input_rejected=True,main_exact_v2=cfg['release'],certificates=certs,native_preloader_cert=result,
            native_locator=locator,bl2_main_parsers=parsers,native_slot=slot,native_loader=loader,root_handoff=packets,
            status='PASS_OFFLINE_CONTROL_NOT_DEVICE_BOOT'))
        print(kind,'PASS',sha(out))
    summary=dict(status='PASS_OFFLINE_DIFFERENTIAL_CONTROLS_NOT_RUNTIME_FIX',variants=report,
        limits=['Main-only native replay does not execute the swapped BL2 or its hardware handoff',
                'Preloader certificate/native hash boundaries use explicit trusted-key/crypto/heap fixtures',
                'Original v2 main CPU evidence carries by exact byte identity for policy variants; not a new boot success',
                'Stored-state4/rollback0/CN/GUID/storage-tail/persistence fixtures do not represent measured device state',
                'Unlocked diagnostic runs actual force3 getter; does not write persistent unlock state',
                'Kernel, DTB/bootconfig, hardware initialization, Android/FBE not executed'])
    (HERE/'validation.json').write_text(json.dumps(summary,indent=2)+'\n')
    validate_reference()

if __name__=='__main__':main()

"""Fenrir component/certificate and bounded native AEE policy comparison."""
from pathlib import Path
import hashlib
import itertools
import json
import struct
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
sys.path.insert(0, str(ROOT/'fresh_analysis'))
from build_candidate import prefix
sys.path.insert(0, str(ROOT/'fresh_analysis/preloader'))
from emulate_bl2_parser import run as parser_run

sha = lambda b: hashlib.sha256(b).hexdigest()
INPUT = ROOT/'fresh_analysis/fenrir_review/input/dash-fenrir-versioned.bin'
EXPECTED = 'dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113'


def members(blob):
    out=[]; off=0
    for i in range(15):
        assert blob[off:off+4] == bytes.fromhex('88168858')
        size=struct.unpack_from('<I',blob,off+4)[0]; end=(off+512+size+15)&~15
        assert end <= len(blob)
        out.append(dict(offset=off,size=size,end=end,name=blob[off+8:off+40].split(b'\0')[0].decode(),
                        header=blob[off:off+512],payload=blob[off+512:off+512+size],
                        record=blob[off:end]))
        off=end
    return out, blob[off:]


def aee_case(aee, kind, patched, state, query=0, flag14=0, flag18=0, rc=0):
    base=0xffff0000be000000; data=0x3000000; stop=0x4000000
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    u.mem_map(base,0x200000);u.mem_write(base,aee)
    u.mem_map(data,0x20000);u.mem_map(stop,0x1000)
    sp=data+0x18000;x29=sp+0x50;desc=data+0x100;handler=stop+0x100
    u.reg_write(UC_ARM64_REG_SP,sp);u.reg_write(UC_ARM64_REG_X29,x29)
    u.reg_write(UC_ARM64_REG_X25,desc);u.reg_write(UC_ARM64_REG_X28,data+0x1000)
    u.reg_write(UC_ARM64_REG_X20,data+0x2000)
    u.reg_write(UC_ARM64_REG_X19,0)
    u.mem_write(x29-4,struct.pack('<I',query))
    u.mem_write(sp+8,struct.pack('<I',state))
    u.mem_write(desc+0x10,struct.pack('<III',3,flag14,flag18))
    u.mem_write(desc+0x20,struct.pack('<Q',handler))
    u.mem_write(data+0x2000+0x948,struct.pack('<I',0))
    # Loads through the original command descriptor/global pointers execute.
    u.mem_write(base+0x107968,struct.pack('<Q',data+0x5000))
    u.mem_write(base+0x1079a8,struct.pack('<I',1234))
    seen=[]
    def ret(v=0):
        u.reg_write(UC_ARM64_REG_W0,v);u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,addr,size,_):
        off=addr-base
        if addr==handler:seen.append('handler');ret()
        elif kind=='dispatcher' and off==0x13c20:u.emu_stop()
        elif kind=='flash_gate' and off==0x6791c:
            u.mem_write(u.reg_read(UC_ARM64_REG_X0),struct.pack('<I',state));ret(rc)
        elif kind=='flash_gate' and off in (0x164b0,0x167d8):
            seen.append('allowed_continuation' if off==0x164b0 else 'deny_log');u.emu_stop()
        elif int.from_bytes(u.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            u.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook)
    u.emu_start(base+(0x13bdc if kind=='dispatcher' else 0x16448),stop,count=10000)
    if kind=='dispatcher':
        wanted=patched or ((not query or flag14) and (not flag18 or state==3))
        assert seen==(['handler'] if wanted else []),(patched,state,query,flag14,flag18,seen)
    else:
        wanted=rc==0 and (patched or state!=1)
        assert seen==(['allowed_continuation'] if wanted else ['deny_log']),(patched,state,rc,seen)
    return dict(kind=kind,patched=patched,state_fixture=state,query_fixture=query,
                command_flag14_fixture=flag14,command_flag18_fixture=flag18,
                query_result_fixture=rc,observed=seen,pass_=True)


def main():
    image=INPUT.read_bytes();assert sha(image)==EXPECTED
    source=(ROOT/'lk_hyperos3_original/lk.img').read_bytes()
    fr,tail=members(image);sr,_=members(source)
    assert tail==bytes(768)
    comparisons=[];certs=[];parsers=[]
    for i,(f,s) in enumerate(zip(fr,sr)):
        comparisons.append(dict(member=i,name=f['name'],payload_sha256=sha(f['payload']),
            source_payload_sha256=sha(s['payload']),payload_identical=f['payload']==s['payload'],
            payload_size=f['size'],source_size=s['size']))
        if f['name'] not in ('cert1','cert2'):continue
        pref=90 if i in (2,8) else 0
        der=f['payload'][pref:];original=s['payload']
        assert der==original
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        row=dict(member=i,name=f['name'],prefix_bytes=pref,original_der_identical=True,
                 original_der_signature_valid=True)
        if f['name']=='cert2':
            comp=fr[i-2]
            oidp=bytes.fromhex('060760867693160201');oidh=bytes.fromhex('060760867693160204')
            ph=sha(comp['payload']+bytes((-comp['size'])%16));hh=sha(comp['header'])
            signedp=der[der.index(oidp)+12:der.index(oidp)+44].hex()
            signedh=der[der.index(oidh)+12:der.index(oidh)+44].hex()
            row.update(actual_aligned_payload_sha256=ph,signed_payload_sha256=signedp,
                       signed_payload_hash_matches=ph==signedp,signed_header_hash_matches=hh==signedh)
            assert (ph==signedp)==(pref==0)
            assert hh==signedh
            if pref:
                assert f['payload'][:90]==prefix(comp['header'],comp['payload'])[:90]
                row['prefix_exact_single_90_byte_hash_block']=True
                for version,mask in itertools.product((3,4),(0x60,0x1f4e2,0x1f400)):
                    result=parser_run(version,'fenrir_'+comp['name'],mask,certificate_record=f['record'])
                    assert result['returned'] and result['status']=='0x0' and result['error'] is None
                    if mask != 0x1f400:
                        assert result['payload_hash']==(ph if version==3 else signedp)
                        assert result['header_hash']==hh
                    parsers.append(result)
        certs.append(row)
    changed=[];decoder=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
    a,b=sr[6]['payload'],fr[6]['payload']
    for off in range(0,len(a),4):
        if a[off:off+4]==b[off:off+4]:continue
        old=list(decoder.disasm(a[off:off+4],off));new=list(decoder.disasm(b[off:off+4],off))
        assert len(old)==len(new)==1
        changed.append(dict(payload_offset=hex(off),whole_file_offset=hex(fr[6]['offset']+512+off),
                            old=a[off:off+4].hex(),new=b[off:off+4].hex(),
                            old_instruction=old[0].mnemonic+' '+old[0].op_str,
                            new_instruction=new[0].mnemonic+' '+new[0].op_str))
    assert len(changed)==4
    cases=[]
    for patched,blob in ((False,a),(True,b)):
        for state,query,flag14,flag18 in itertools.product((0,1,3,4),(0,1),(0,1),(0,1)):
            cases.append(aee_case(blob,'dispatcher',patched,state,query,flag14,flag18))
        for state,rc in itertools.product((0,1,3,4),(0,5)):
            cases.append(aee_case(blob,'flash_gate',patched,state,rc=rc))
    report=dict(status='PASS_OFFLINE_CONTAINER_SIGNATURES_BOUNDED_PARSER_AND_AEE',input_sha256=EXPECTED,
        input_bytes=len(image),trailing_zero_bytes=len(tail),member_comparisons=comparisons,
        retained_der_signatures=certs,native_bl2_parser_cases=parsers,aee_instruction_changes=changed,
        aee_cpu_cases=cases,aee_cpu_case_count=len(cases),
        facts=['BL2 payload/certificates and DTB/DTBO are exact H3 originals',
               'Main and AEE original signed DER retained; their actual signed payload digests differ',
               'One 90-byte prefix block per modified component, versus duplicated 180-byte block in our ENG v2 main',
               'AEE patches dispatch gates and locked-flashing denial, not AEE Root-of-Trust or lock getter'],
        mocks=['Parser PAC and logging only; no OID/ASN.1/hash-selection mocks',
               'AEE command handler and query-result fixtures; bounded slices, not complete command execution'],
        limits=['No TEE, kernel, device flash or hardware attestation execution',
                'Retained certificate signature valid under embedded key does not prove complete hardware trust acceptance',
                'AEE dispatch probes deliberately test the patched gate slices; later command handler/storage checks remain',
                'AEE flash gate keeps the original deny branch on query error; patch does not force every flash to succeed'])
    (HERE/'chain_evidence.json').write_text(json.dumps(report,indent=2)+'\n')
    print('PASS',len(certs),'DER signatures,',len(parsers),'native parsers,',len(cases),'bounded AEE cases')


if __name__=='__main__':main()

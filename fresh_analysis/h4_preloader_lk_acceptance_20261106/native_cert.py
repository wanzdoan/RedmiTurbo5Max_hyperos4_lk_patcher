"""H4 preloader certificate coordinator with native ASN.1/key compare/PSS.

SHA256 and RSA exponentiation use host arithmetic backends; no unconditional
crypto-success stubs. Trust key, mode, heap, readiness and guards are fixtures.
Never contacts a phone, queries efuses or writes any firmware.
"""
from pathlib import Path
import hashlib
import struct
import sys
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[1]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,sha
FW=ROOT/'preloader_hyperos4/preloader_dash.bin'
FW_SHA='104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77'
BASE=0x2000f00
INPUT,KEY,RESULT,HEAP,STACK,STOP=0x3000000,0x3010000,0x3020000,0x3030000,0x3100000,0x3200000


def run(record, trusted_spki, *, firmware=None, component_header=None, component_payload=None):
    fw=FW.read_bytes() if firmware is None else firmware
    assert sha(fw)==FW_SHA
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    for addr,size in ((0x2000000,0x180000),(INPUT,0x100000),(STACK,0x10000),(STOP,0x1000)):
        u.mem_map(addr,size)
    u.mem_write(BASE,fw);u.mem_write(INPUT,record)
    numbers=load_der_public_key(trusted_spki).public_numbers()
    assert numbers.n.bit_length()==2048 and numbers.e==65537
    key=numbers.n.to_bytes(256,'big')+bytes(256)+struct.pack('<I',256)
    key+=numbers.e.to_bytes(3,'big')+bytes(509)+struct.pack('<I',3)
    assert len(key)==0x408
    u.mem_write(KEY,key)
    # Actual linked addresses, not the historical carrier relocated -0x1000.
    def seed(slot,value,width=4):
        ptr=int.from_bytes(u.mem_read(slot,8),'little')
        assert 0x2000000<=ptr<0x2180000,(hex(slot),hex(ptr))
        u.mem_write(ptr,value.to_bytes(width,'little'))
    seed(0x2104280,0x12345678,8) # stack guard indirection
    seed(0x2104810,0)          # RSA2048 / SHA256 mode fixture
    seed(0x2104818,256)        # RSA signature length
    seed(0x2104838,256)        # RSA modulus length
    seed(0x2104828,32)         # hash length
    seed(0x2104840,0)          # repeat-verification flag
    events=[];heap=[HEAP];last=[];counts={};hash_contexts={}
    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,address,size,_):
        off=address-BASE;last.append(hex(off));last[:]=last[-12:]
        if off in (0xa5c68,0xa6760,0xa6100,0x9c700):counts[hex(off)]=counts.get(hex(off),0)+1
        if off in (0xa10ec,0x8aea0,0x8ad88):ret()
        elif off==0x9c7dc:ret(1) # accelerator/readiness fixture, not a signature verdict
        elif off==0xa7388:ret(0) # optional timing/verification mode guard
        elif off==0x9ad10:
            length=uc.reg_read(UC_ARM64_REG_X0);ptr=heap[0];heap[0]+=(length+15)&~15
            assert heap[0]<INPUT+0x100000
            uc.mem_write(ptr,bytes(length));ret(ptr)
        elif off==0x9ae5c:ret() # private emulator heap free
        elif off==0xa1ac0:
            ctx=uc.reg_read(UC_ARM64_REG_X0);hash_contexts[ctx]=bytearray();ret()
        elif off==0xa1adc:
            ctx,ptr,length=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2)]
            hash_contexts[ctx]+=bytes(uc.mem_read(ptr,length));ret()
        elif off==0xa1afc:
            ctx,out=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1)]
            message=hash_contexts.pop(ctx);digest=hashlib.sha256(message).digest()
            uc.mem_write(out,digest);events.append(dict(backend='SHA256_stream',length=len(message),digest=digest.hex()));ret()
        elif off==0x9c68c:
            ptr,length,out,mode=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3)]
            assert mode==0,mode
            digest=hashlib.sha256(bytes(uc.mem_read(ptr,length))).digest()
            uc.mem_write(out,digest);events.append(dict(backend='SHA256',length=length,digest=digest.hex()));ret()
        elif off==0x9c6c0:
            inp,mod,out,mode=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3)]
            assert mode==0,mode
            n=int.from_bytes(uc.mem_read(mod,256),'little')
            s=int.from_bytes(uc.mem_read(inp,256),'little')
            assert n==numbers.n
            uc.mem_write(out,pow(s,65537,n).to_bytes(256,'little'))
            events.append(dict(backend='RSA_public_exponentiation',modulus_sha256=sha(n.to_bytes(256,'big'))));ret()
        elif off==0x91190:raise AssertionError('Native stack guard failed')
        elif int.from_bytes(uc.mem_read(address,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,address+4)
    u.hook_add(UC_HOOK_CODE,hook)
    u.reg_write(UC_ARM64_REG_SP,STACK+0xf000);u.reg_write(UC_ARM64_REG_LR,STOP)
    for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3),
                       (INPUT,len(record),KEY,RESULT)):u.reg_write(reg,val)
    try:u.emu_start(BASE+0xa6508,STOP,count=2500000)
    except Exception as error:raise RuntimeError(f'{error}; PC={u.reg_read(UC_ARM64_REG_PC):x}; last={last}') from error
    assert u.reg_read(UC_ARM64_REG_PC)==STOP,('instruction bound',last)
    status=u.reg_read(UC_ARM64_REG_W0)
    result=bytes(u.mem_read(RESULT,0x154));content_checks=[]
    if status==0 and component_header is not None:
        assert len(component_header)==512 and component_payload is not None
        u.mem_map(0x3400000,0x200000);u.mem_map(0x3600000,0x10000)
        def call(entry,args):
            u.reg_write(UC_ARM64_REG_SP,STACK+0xf000);u.reg_write(UC_ARM64_REG_LR,STOP)
            for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4),args):
                u.reg_write(reg,val)
            u.emu_start(BASE+entry,STOP,count=100000)
            assert u.reg_read(UC_ARM64_REG_PC)==STOP
            return u.reg_read(UC_ARM64_REG_W0)
        for label,data,source,expected in [('header',component_header,0x3600000,RESULT+0x3c),
                                            ('payload',component_payload,0x3400000,RESULT+8)]:
            u.mem_write(source,data);pad=(-len(data))%16
            rc=call(0xa4d6c,[source,len(data),pad,0x3601000,32]);assert rc==0
            comparison=call(0xa0f58,[expected,0x3601000,32])
            content_checks.append(dict(component=label,hash_helper_return=rc,native_comparison_return=hex(comparison),
                match=comparison==0,sha256=bytes(u.mem_read(0x3601000,32)).hex()))
    return dict(return_status=status,native_entries=counts,backends=events,content_checks=content_checks,
        extracted_header_hash=result[0x3c:0x3c+int.from_bytes(result[0x6c:0x70],'little')].hex(),
        extracted_payload_hash=result[8:8+int.from_bytes(result[0x38:0x3c],'little')].hex(),
        linked_base=hex(BASE),crypto_success_unconditionally_mocked=False)


if __name__=='__main__':
    import json
    image=(ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img').read_bytes();rows=members(image)
    a,b=rows[4:6];cert1=image[a['offset']+512:a['offset']+512+a['size']]
    record=image[b['offset']:b['offset']+512+b['size']]
    print(json.dumps(run(record,cert1[496:790]),indent=2))

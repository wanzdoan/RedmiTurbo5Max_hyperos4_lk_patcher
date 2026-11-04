"""Bounded LK tests of outgoing HWC and preserved trust/lock behavior."""
import itertools
import json
import random
import struct
import sys
from capstone import Cs,CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_hwc_global import *

sys.path.insert(0,str(HERE.parent))
from validate_stock_flash_region import key_case
from build_stock_flash_region import STOCK_H3
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine,cstr,B,STACK,STOP
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import main_case,bl2_run


def hwc_case(image,patched,hwc,level,version,format_error,append_status):
    u,saved=machine(image)
    sp=u.reg_read(UC_ARM64_REG_SP);hw=sp+0x120
    record=bytearray(b'\xa5'*0x4c)
    for off,size,value in [(0,16,level),(16,20,version),(36,36,hwc)]:
        record[off:off+size]=value.encode().ljust(size,b'\0')
    record[0x48:0x4c]=struct.pack('<I',0x12345678)
    u.mem_write(hw-4,b'LEFT');u.mem_write(hw+len(record),b'RGHT')
    formatted=[];appended=[];outcomes=[];reads=[]

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value&0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        if off==0x3530:
            if uc.reg_read(UC_ARM64_REG_X0)!=hw:raise AssertionError('Unexpected hardware output pointer')
            reads.append('hardware fixture read');uc.mem_write(hw,bytes(record));ret()
        elif off==0x7ffe0:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2))
            values=[cstr(uc,uc.reg_read(r)) for r in (UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5)]
            if fmt!='androidboot.hwc=%s androidboot.hwlevel=%s androidboot.hwversion=%s':raise AssertionError('Format changed')
            if values!=['GLOBAL' if patched else hwc,level,version]:raise AssertionError(('Wrong argument routing',values))
            if uc.reg_read(UC_ARM64_REG_X4)!=hw or uc.reg_read(UC_ARM64_REG_X5)!=hw+16:
                raise AssertionError('HW level/version pointers changed')
            text=fmt%tuple(values);formatted.append(text)
            limit=uc.reg_read(UC_ARM64_REG_W1)
            if uc.reg_read(UC_ARM64_REG_X0)!=sp+0x180 or limit!=200:raise AssertionError('Output buffer/size changed')
            if not format_error:uc.mem_write(sp+0x180,text.encode()[:limit-1]+b'\0')
            ret(-1 if format_error else len(text))
        elif off==0x7ea84:ret()
        elif off==0x18ab8:
            appended.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(append_status)
        elif off==0x133e4:
            outcomes.append('original continuation');uc.emu_stop()
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    # Native memset runs; getter, formatter, logger and append are explicit boundaries.
    u.emu_start(B+0x13364,STOP,count=10000)
    expected=f'androidboot.hwc={"GLOBAL" if patched else hwc} androidboot.hwlevel={level} androidboot.hwversion={version}'
    if reads!=['hardware fixture read'] or formatted!=[expected] or outcomes!=['original continuation']:
        raise AssertionError('Incorrect bounded block execution')
    if appended!=([expected] if not format_error else []):raise AssertionError('Error/append path changed')
    if bytes(u.mem_read(hw,len(record)))!=bytes(record) or bytes(u.mem_read(hw-4,4))!=b'LEFT' or bytes(u.mem_read(hw+len(record),4))!=b'RGHT':
        raise AssertionError('Underlying hardware record or neighboring memory changed')
    if u.reg_read(UC_ARM64_REG_SP)!=sp or u.reg_read(UC_ARM64_REG_X19)!=hw+16:
        raise AssertionError('Caller frame/hardware-version cursor changed')
    for reg,value in saved.items():
        if reg not in (UC_ARM64_REG_X19,UC_ARM64_REG_X20) and u.reg_read(reg)!=value:
            raise AssertionError('Unrelated callee-saved register changed')
    return {'patched':patched,'hardware_hwc':hwc,'hardware_level':level,'hardware_version':version,
            'format_error':format_error,'append_return':append_status,'outgoing':appended,
            'hardware_record_and_neighbors_unchanged':True,'original_continuation_reached':True,'pass':True}


def main():
    source=INPUT.read_bytes();output=(HERE/NAME).read_bytes()
    if build(source)[0]!=output or build(source)[0]!=output:raise AssertionError('Non-deterministic build')
    wrong=bytearray(source);wrong[0x12345]^=1
    try:build(bytes(wrong))
    except ValueError:pass
    else:raise AssertionError('Wrong input accepted')
    hw=[]
    for patched,image in [(False,source),(True,output)]:
        for values in itertools.product(('CN','GLOBAL','IN','CN_TEST',''),('MP','EVT'),('1.0','V2.3'),(False,True),(0,-7)):
            hw.append(hwc_case(image,patched,*values))
    stock=STOCK_H3.read_bytes()
    keys={'ENG':source[512+0x130728:512+0x130828],
          'CN':stock[512+0x138638:512+0x138838],
          'GL':stock[512+0x138838:512+0x138a38]}
    cases=[];rng=random.Random(9500)
    for name,key in keys.items():
        bits=2048 if name=='ENG' else 4096
        cases.append(dict(kind=name,**key_case(output,bits,key,(0,1))))
        for i in (0,1,15,31,len(key)//2,len(key)-1):
            changed=bytearray(key);changed[i]^=1
            cases.append(dict(kind=name+'_changed_bit',**key_case(output,bits,bytes(changed),(0,0))))
    for bits in (1024,2048,4096,8192):
        cases.append(dict(kind='unknown',**key_case(output,bits,rng.randbytes(bits//8),(0,0))))
    for null_key,null_out in [(True,False),(False,True),(True,True)]:
        cases.append(dict(kind='null',**key_case(output,4096,keys['CN'],(2,0xa5),null_key=null_key,null_out=null_out)))
    for bits,key in [(2048,keys['ENG']),(4096,keys['CN'])]:
        for length in (0,4,7,8,263,264,519,521,1031,1033):
            cases.append(dict(kind='malformed_length',**key_case(output,bits,key,(2,0),size=length)))
    cases.append(dict(kind='hash_failure',**key_case(output,4096,keys['CN'],(0,0),hash_error=True)))
    v=(HERE.parent/'recovery_region_v2/input/vbmeta.img').read_bytes()
    if sha(v)!='4d6ad1a94bcbf4edc3cfc3ffb94978b61c8441c94a72c6466e3d4f976e263837':raise AssertionError('Global vbmeta identity')
    auth=struct.unpack_from('>Q',v,12)[0];ko,kl=struct.unpack_from('>QQ',v,64)
    key=v[256+auth+ko:256+auth+ko+kl];bits=struct.unpack_from('>I',key)[0]
    cases.append(dict(kind='actual_supplied_Global_vbmeta',**key_case(output,bits,key[8:8+bits//8],(0,1))))
    predicates=[]
    for label,image in [('v1',source),('HWC_GLOBAL',output)]:
        for state in (1,2,3,4):
            predicates.append(dict(image=label,**main_case(image,0x6979c,0,'avb',mock_state=state)))
            predicates.append(dict(image=label,**main_case(image,0xb6588,0,'lock_boolean',mock_state=state)))
            predicates.append(dict(image=label,**main_case(image,0xc770,0,'fastboot',mock_state=state)))
    rows=members(output);certs=[]
    for i,r in enumerate(rows):
        if r['name'] not in ('cert1','cert2'):continue
        raw=output[r['offset']+512:r['offset']+512+r['size']];der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        certs.append({'member':i,'original_der_signature_valid':True})
    r,c=rows[3],rows[2];bl2=output[r['offset']+512:r['offset']+512+r['size']]
    record=output[c['offset']:c['offset']+512+c['size']];parsers=[]
    for mask in (0x60,0x1f4e2,0x1f400):
        got=bl2_run(bl2,record,mask)
        if got['result'] or (mask!=0x1f400 and (got['payload_hash']!=sha(output[512:rows[0]['end']]) or got['header_hash']!=sha(output[:512]))):
            raise AssertionError('Native ENG BL2 did not choose new hashes')
        parsers.append(got)
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);decoder.skipdata=True
    for ins in decoder.disasm(source[512+0x3000:512+0xc8000],0x3000):
        if ins.mnemonic in ('b','bl','cbz','cbnz','tbz','tbnz') or ins.mnemonic.startswith('b.'):
            last=ins.op_str.rsplit(',',1)[-1].strip()
            if last.startswith('#0x') and LITERAL<=int(last[1:],16)<LITERAL+len(VALUE):
                raise AssertionError('Existing branch reaches literal slot')
    if struct.pack('<Q',0xffff000090700000+LITERAL) in source:
        raise AssertionError('Existing absolute pointer references literal slot')
    disassembly=[{'payload_offset':hex(i.address),'bytes':i.bytes.hex(),'instruction':i.mnemonic+' '+i.op_str}
                 for i in decoder.disasm(output[512+ARG:512+ARG+4],ARG)]
    report={'status':'PASS_BOUNDED_LK_TESTS_NOT_DEVICE_BOOT','input_sha256':INPUT_SHA,'artifact_sha256':sha(output),
            'hwc_cpu_cases':len(hw),'key_callback_cpu_cases':len(cases),'lock_control_cpu_cases':len(predicates),
            'total_main_cpu_cases':len(hw)+len(cases)+len(predicates),
            'retained_der_signatures':certs,'native_eng_bl2_parser_cases':parsers,
            'deterministic_build':True,'wrong_input_rejected':True,
            'literal_slot_no_existing_direct_branch_or_absolute_pointer':True,
            'hwc_cases':hw,'key_cases':cases,'lock_controls':predicates,'patch_disassembly':disassembly,
            'mocks':['Hardware record input fixture, snprintf/logger/cmdline append boundaries in HWC cases',
                     'PAC/logging, controlled hash failure, labelled lock-state controls'],
            'real_execution':['Patched/original HWC pointer setup and caller branches; native memset',
                              'Actual key callback, SHA256 and memcmp','Native ENG BL2 hash parser'],
            'limits':['No phone flash/boot, Android property readback or recovery error resolution demonstrated',
                      'No complete Global AVB boot or normal Android region consumer tested',
                      'Lock controls model getter state; no device RPMB state was read or written']}
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('status','artifact_sha256','hwc_cpu_cases','key_callback_cpu_cases','lock_control_cpu_cases','total_main_cpu_cases')},indent=2))


if __name__=='__main__':main()

"""Compare independent public seccfg and common-lock reports in Fenrir."""
import json
import struct
from probe_main import *


def seccfg_case(image, state, status, write_result=True):
    u, saved = machine(image); words=[]; responses=[]
    def hook(uc, addr, size, _):
        off=addr-B
        if off==0xb35b8:
            pointer=uc.reg_read(UC_ARM64_REG_X0); words.append(pointer)
            if write_result:uc.mem_write(pointer,struct.pack('<I',state))
            ret(uc,status)
        elif off==0x7f434:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2));arg=uc.reg_read(UC_ARM64_REG_W3)
            assert fmt in ('seccfg_state is %u','mtkrpmb_state is %u.')
            text=fmt%arg;uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),text.encode()+b'\0');ret(uc,len(text))
        elif off in (0x9c60,0x9d10,0x9d50):
            responses.append((off,cstr(uc,uc.reg_read(UC_ARM64_REG_X0))));ret(uc)
        else:hook_common(uc,off)
    u.hook_add(UC_HOOK_CODE,hook);run(u,0xc8f4,saved)
    expected_state=state if write_result else 4
    info=[s for off,s in responses if off==0x9c60]
    errors=[s for off,s in responses if off==0x9d10]
    assert info==[f'seccfg_state is {expected_state}','mtkrpmb_state is 4.']
    assert errors==(['Get seccfg_state failed.'] if status else [])
    assert len(words)==1 and int.from_bytes(u.mem_read(words[0],4),'little')==expected_state
    return dict(seccfg_state_fixture=state,getter_status_fixture=status,getter_writes_result=write_result,
                actual_result_word_preserved=True,info=info,errors=errors,pass_case=True)


def lks_case(image, seccfg_fixture):
    u,saved=machine(image);responses=[];read=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off==0xb35b8:read.append(True);raise AssertionError('LKS report unexpectedly read seccfg')
        elif off==0x7f434:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2));assert fmt=='lks = 1'
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),fmt.encode()+b'\0');ret(uc,len(fmt))
        elif off in (0x9c60,0x9d10,0x9d50):
            responses.append((off,cstr(uc,uc.reg_read(UC_ARM64_REG_X0))));ret(uc)
        else:hook_common(uc,off)
    u.hook_add(UC_HOOK_CODE,hook);run(u,0xc868,saved)
    info=[s for off,s in responses if off==0x9c60];assert info==['lks = 1'] and not read
    return dict(independent_seccfg_fixture=seccfg_fixture,seccfg_read=False,info=info,pass_case=True)


def main():
    image=INPUT.read_bytes();stock=STOCK.read_bytes();assert sha(image)==INPUT_SHA
    assert image[512+0xc8f4:512+0xc994]==stock[512+0xc8f4:512+0xc994]
    seccfg=[seccfg_case(image,state,status) for state in (0,1,3) for status in (0,7)]
    seccfg.append(seccfg_case(image,0,7,False))
    lks=[lks_case(image,state) for state in (0,1,3)]
    report=dict(status='PASS_BOUNDED_PUBLIC_QUERY_ONLY_NOT_DEVICE_STATE',input_sha256=INPUT_SHA,
                seccfg_function_original_bytes_unchanged=True,seccfg_cpu_cases=seccfg,lks_cpu_cases=lks,
                total_cpu_cases=len(seccfg)+len(lks),
                finding='Fenrir LKS locked1 does not imply its separate seccfg query is fake-locked1; seccfg prints actual getter word or initialized4 on no-write error',
                comparison='Our v2 explicitly changes this separate public seccfg formatting value to1 while preserving the getter word and error',
                limits=['seccfg getter is an explicit boundary fixture; no physical partition read','No actual device lock/flash/boot or stored state inferred'])
    (HERE/'seccfg_validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print('PASS: 7 seccfg and 3 independent LKS CPU cases')


if __name__=='__main__':main()

"""Native reproduction of the post-AVB recovery gate and final-image regression."""
import itertools
import json
import struct
from build_fix import *
from unicorn import Uc,UC_ARCH_ARM64,UC_MODE_ARM,UC_HOOK_CODE
from unicorn.arm64_const import *

B,DATA,STACK,STOP=0x90700000,0x3000000,0x2000000,0x4000000


def recovery_case(image,*,mode=0,state=4,sbc=14,query_error=0,primary=7,fallback=9,tag_error=1,direct=False,eng=False):
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    for addr,size in ((B,0x500000),(DATA,0x10000),(STACK,0x10000),(STOP,0x1000)):u.mem_map(addr,size)
    u.mem_write(B,image[512:512+members(image)[0]['size']])
    sp=STACK+0xf000;u.reg_write(UC_ARM64_REG_SP,sp);u.reg_write(UC_ARM64_REG_LR,STOP)
    calls=[];result=[]
    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,address,size,_):
        off=address-B
        if off==(0x14040 if eng else 0x147ec) and not direct:
            result.append('continue_boot');uc.emu_stop()
        elif off==0x87630:
            result.append('recovery_entry');uc.emu_stop() # Never execute misc write/reboot routine.
        elif off==0x86b8c:calls.append('native_Secure_ID_verifier')
        elif off in (0x7e11c,0x7ea84):ret()
        elif off==0x1617c and eng:ret(123)
        elif off==0x10bc0:ret(mode)
        elif off==0x88a10:
            calls.append('stored_lock_state_fixture')
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',state));ret(query_error)
        elif off==0x4a384:
            selector=uc.reg_read(UC_ARM64_REG_W0)
            calls.append('secure_boot_fixture' if selector==0x1b else 'hardware_id_fixture')
            ret(sbc if selector==0x1b else 0)
        elif off==0x86fe0:calls.append('primary_Secure_ID_read_fixture');ret(primary)
        elif off==0x87514:calls.append('fallback_Secure_ID_read_fixture');ret(fallback)
        elif off==0x87150:calls.append('binding_compare_fixture');ret(tag_error)
        elif off==0x9482c:calls.append('ufs_identity_fixture_error');ret(7)
        elif off in (0x8a3d8,0x95fa4,0x87854,0x745fd8,0x227e0):
            raise AssertionError('Unexpected storage write or reboot boundary')
        elif int.from_bytes(uc.mem_read(address,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,address+4)
    u.hook_add(UC_HOOK_CODE,hook)
    entry=0x86b8c if direct else (0x14028 if eng else 0x147a0)
    u.emu_start(B+entry,STOP,count=100000)
    assert u.reg_read(UC_ARM64_REG_SP)==sp,'Native Secure-ID stack unbalanced'
    if direct:
        assert u.reg_read(UC_ARM64_REG_PC)==STOP
        outcome='return_'+hex(u.reg_read(UC_ARM64_REG_W0))
    else:
        assert len(result)==1,(mode,state,calls,hex(u.reg_read(UC_ARM64_REG_PC)))
        outcome=result[0]
    return dict(boot_mode=mode,stored_state=state,sbc=sbc,query_error=query_error,primary_error=primary,
                fallback_error=fallback,binding_error=tag_error,direct_verifier=direct,outcome=outcome,calls=calls,
                no_device_or_persistent_writes=True)


def main():
    reports={}
    for kind,config in VARIANTS.items():
        source=(BASE/config['input']).read_bytes();image=(HERE/config['output']).read_bytes()
        assert build(source,kind)[0]==image==build(source,kind)[0]
        wrong=bytearray(source);wrong[12345]^=1
        try:build(bytes(wrong),kind)
        except ValueError:pass
        else:raise AssertionError('Wrong input accepted')
        cases=[]
        fixtures=[dict(primary=7,fallback=9,tag_error=1),dict(primary=0,fallback=0,tag_error=1),
                  dict(primary=7,fallback=0,tag_error=1)]
        for mode,state,sbc,error,fixture in itertools.product((0,1,2,255),(3,4),(0,14),(0,7),fixtures):
            before=recovery_case(source,mode=mode,state=state,sbc=sbc,query_error=error,**fixture)
            after=recovery_case(image,mode=mode,state=state,sbc=sbc,query_error=error,**fixture)
            expected=mode!=2 and (error!=0 or (state!=3 and sbc==14))
            assert (before['outcome']=='recovery_entry')==expected
            assert after['outcome']=='continue_boot' and not after['calls']
            cases.append(dict(original=before,fixed=after))
        direct=[]
        for state,sbc,error in itertools.product((3,4),(0,14),(0,7)):
            a=recovery_case(source,state=state,sbc=sbc,query_error=error,direct=True)
            b=recovery_case(image,state=state,sbc=sbc,query_error=error,direct=True)
            assert a==b;direct.append(b)
        c=members(image)[2]['offset']+512
        changed=[i for i,(a,b) in enumerate(zip(source,image)) if a!=b]
        assert all(512+OFFSET<=i<516+OFFSET or c<=i<c+180 for i in changed)
        reports[kind]=dict(status='PASS_NATIVE_CALLSITE_REPRODUCTION_NOT_DEVICE_FIX_CONFIRMATION',
            input_sha256=sha(source),output_sha256=sha(image),cases=cases,
            direct_verifier_unchanged_cases=direct,paired_case_count=len(cases),direct_case_count=len(direct),
            actual_changed_bytes=len(changed),
            limits=['Native Secure-ID caller/predicate/error handling; state/storage/binding/UFS results are fixtures',
                    'Recovery routine stopped at entry before any write/reboot',
                    'No actual failure log, Secure-ID record or current lock state read from phone',
                    'A reproduced possible path is not proof it caused this reported recovery'])
    eng=(ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img').read_bytes()
    h3=[]
    for mode,state in itertools.product((0,1,2,255),(3,4)):
        r=recovery_case(eng,mode=mode,state=state,eng=True)
        assert r['outcome']=='continue_boot' and not r['calls'];h3.append(r)
    report=dict(status='PASS',h4_variants=reports,h3_eng_native_control=h3,
                native_h3_eng_control_cases=len(h3),hardware_tested=False)
    (HERE/'recovery_gate_validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print('PASS:96 paired caller scenarios (192 runs) +8 paired unchanged-verifier controls per H4 image;8 ENG controls')


if __name__=='__main__':main()

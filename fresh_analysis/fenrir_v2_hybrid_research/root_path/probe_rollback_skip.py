"""Prove targeted maintenance-call removal, without executing OTP helpers."""
from pathlib import Path
import hashlib
import itertools
import json
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from probe_init_boot_hash import machine, ret, B, DATA, STACK, STOP, MAPS, ROOT, HERE

FLOW = {
    'stock_h3': dict(entry=0x68898, call=0x688b8, original='c6010094',
        maintenance=0x68fd0, stop=0x688c4, selector=0x8845c, logger=0x7de54, otp=0x889c8),
    'debug': dict(entry=0x69668, call=0x69688, original='05010094',
        maintenance=0x69a9c, stop=0x69694, selector=0x8850c, logger=0x7ea84, otp=0x88874),
}


def case(image,kind,skip,boot,flags,control_state=None):
    cfg=FLOW[kind];mapping=MAPS[kind]
    expected=bytes.fromhex('1f2003d5' if skip else cfg['original'])
    assert image[512+cfg['call']:516+cfg['call']]==expected
    u=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29,sp+0x100)
    u.mem_write(sp+0xf0,(DATA+0x1000).to_bytes(8,'little'))
    u.reg_write(UC_ARM64_REG_W0,0) # Earlier post-slot load succeeded.
    u.reg_write(UC_ARM64_REG_W19,boot);u.reg_write(UC_ARM64_REG_W22,flags)
    calls=[];otp=[];stop=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off==cfg['stop']:stop.append(off);uc.emu_stop()
        elif off==cfg['maintenance']:
            calls.append(off);ret(uc,0x12345678) # Do not run storage/counter code.
        elif off==cfg['otp']:
            otp.append(off);raise AssertionError('OTP helper reached')
        elif off==mapping['wrapper'] and control_state is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),control_state.to_bytes(4,'little'));ret(uc)
        elif off in (cfg['selector'],cfg['logger']):ret(uc)
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook);u.emu_start(B+cfg['entry'],STOP,count=10000)
    assert stop==[cfg['stop']] and not otp
    wanted=not skip and (4 if control_state is None else control_state)!=3
    assert len(calls)==int(wanted)
    assert u.reg_read(UC_ARM64_REG_W23)==0
    return dict(base=kind,boot_fixture=boot,flags_fixture=flags,
                lock_state_control=control_state,final_nop_present=skip,
                maintenance_entry_reached=bool(calls),otp_helper_executed=False,
                continuation_status_w23=0,code_ram_modified=False,pass_case=True)


def validate_rollback_skip(image,kind):
    cases=[case(image,kind,True,boot,flags) for boot,flags in itertools.product((0,1,99),(0,1))]
    return dict(status='PASS_FINAL_TARGETED_ROLLBACK_MAINTENANCE_SKIP',base=kind,
                image_sha256=hashlib.sha256(image).hexdigest(),native_cpu_cases=len(cases),cases=cases,
                only_targeted_caller_nop=True,
                limits=['Post-slot-load success is a fixture; predicate and callsite execute natively',
                        'No persistent counter was read or written; original maintenance is stopped at entry in controls',
                        'This does not disable every possible antirollback/OTP routine in the whole firmware'])


def main():
    rows=[]
    for kind,cfg in MAPS.items():
        image=(ROOT/cfg['path']).read_bytes()
        for state,boot,flags in itertools.product((3,4),(0,1,99),(0,1)):
            rows.append(case(image,kind,False,boot,flags,state))
    report=dict(status='PASS_NATIVE_ORIGINAL_CONTROL_FLOW_MAINTENANCE_GATING',
                native_cpu_cases=len(rows),cases=rows,mapping=FLOW,
                finding='Original state4 reaches conditional rollback-index maintenance; state3 skips it',
                maintenance_is_another_hash_check=False,
                limits=['Original maintenance body not executed; no OTP access',
                        'Flags and boot modes are fixtures; not a full boot test'])
    (HERE/'rollback_control_validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(report['status'],len(rows))


if __name__=='__main__':main()

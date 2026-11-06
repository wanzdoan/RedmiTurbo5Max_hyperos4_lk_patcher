"""Check unlocked verification-error routing without conflating it with relock.

One corrupted init_boot fixture exercises the native slot and loader. No phone
or on-disk ROM image is modified. This is not a physical root/Keystore test.
"""
import json
import struct
from validate_fakelock_h4 import machine, B, DATA, STACK, STOP, ret, pac
from build_fakelock_h4 import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM, CS_OP_IMM


def check_reused_function(source):
    main=source[512:512+members(source)[0]['size']]
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_ARM);decoder.detail=True;decoder.skipdata=True
    branches=[]
    for ins in decoder.disasm(main[:0xc7000],0):
        if ins.id and (ins.mnemonic in ('b','bl','cbz','cbnz','tbz','tbnz') or ins.mnemonic.startswith('b.')):
            targets=[op.imm for op in ins.operands if op.type==CS_OP_IMM]
            if targets and 0xc8e0<targets[-1]<0xc96c and not 0xc8e0<=ins.address<0xc96c:
                branches.append([hex(ins.address),hex(targets[-1])])
    pointers=[]
    for base in (0x90700000,0xffff000090700000):
        for offset in range(0,len(main)-7,8):
            target=int.from_bytes(main[offset:offset+8],'little')-base
            if 0xc8e0<target<0xc96c:pointers.append([hex(offset),hex(target)])
    assert not branches and not pointers
    return dict(source_sha256=sha(source),external_direct_interior_branches=branches,
                aligned_absolute_interior_pointers=pointers,
                scope='Bounded code scan through payload0xc7000 and aligned pointers at two known mapping bases; not proof about every computed branch')


def caller_case(image, flags, avb_result, loader_result, boot_type):
    u,_=machine(image);sp=STACK+0xf000
    u.reg_write(UC_ARM64_REG_X29,sp)
    u.reg_write(UC_ARM64_REG_X0,avb_result)
    u.reg_write(UC_ARM64_REG_W22,flags)
    u.reg_write(UC_ARM64_REG_W19,boot_type)
    u.mem_write(sp-16,struct.pack('<Q',DATA+0x100))
    seen=[];end=[]
    def hook(uc,address,size,_):
        off=address-B
        if off in (0x68b14,0x68b8c):end.append(off);uc.emu_stop()
        elif off==0x68d1c:seen.append('loader');ret(uc,loader_result)
        elif off==0x695bc:seen.append('set_green');ret(uc)
        elif off==0x69298:raise AssertionError('Force-unlocked caller attempted rollback maintenance')
        elif off in (0x7e11c,0x8871c):ret(uc)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook)
    u.emu_start(B+0x68b44,STOP,count=30000)
    accepted=bool(flags&1) or avb_result==0
    assert end==[0x68b14 if accepted else 0x68b8c]
    assert seen==(['loader']+(['set_green'] if not flags&1 and not loader_result else []) if accepted else [])
    return dict(allow_verification_error_flag=flags,avb_result_fixture=avb_result,
                loader_return_fixture=loader_result,boot_type=boot_type,
                loader_invoked='loader' in seen,events=seen,real_force3_getter_used=True,
                rollback_maintenance_invoked=False)


def main():
    image=(HERE/NAME).read_bytes();meta=json.loads((HERE/'manifest.json').read_text())
    assert sha(image)==meta['output_sha256']
    layout=check_reused_function(BASE.read_bytes())
    cases=[caller_case(image,flags,result,loader,boot) for flags in (0,1)
           for result in (0,3,4) for loader in (0,7) for boot in (0,1)]
    sys.path.insert(0,str(ROOT/'fresh_analysis/h4_recovery_v3_20261106'))
    from slot_replay import Replay
    replay=Replay(image,'h4',state=3,flags=1,execute_force_getter=True,fault='init_boot_hash')
    slot=replay.run()
    assert slot['returned'] and slot['error'] is None and slot['result']==3
    assert 'androidboot.vbmeta.device_state=locked' in slot['cmdline']
    loader=replay.load()
    assert loader['reached_copy_sink'] and loader['error'] is None
    report=dict(status='PASS_NATIVE_UNLOCKED_POLICY_NOT_PHYSICAL_ROOT_TEST',sha256=sha(image),
                caller_cases=cases,caller_count=len(cases),reused_function_layout_audit=layout,
                corrupted_init_boot_slot=slot,corrupted_init_boot_loader=loader,
                verification_error_retained=True,
                limits=['AVB still returns verification error3 for corruption; never described as clean verification',
                        'Native caller uses original unlocked allow-error bit and real force3 getter; rollback update path is skipped',
                        'Loader buffers are selected in emulator; kernel boot with rooted init_boot not observed',
                        'Storage/rollback/secure-world/key-cache/boot-state use explicit fixtures; no hardware attestation',
                        'No flash, reboot, root installation or device lock operation'])
    (HERE/'boot_policy_validation.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    print(report['status'],len(cases),'caller cases; corrupted init_boot error3 retained, native loader sink reached')


if __name__=='__main__':main()

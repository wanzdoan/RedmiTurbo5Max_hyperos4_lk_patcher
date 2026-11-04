"""Replay LK outgoing handoff, not a secure-world or Play Integrity emulator."""
import hashlib
import itertools
import json
import random
import struct
import sys
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_deep_fakelock import *

sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine, assert_abi, cstr, fastboot_case, android_state_case, avb_device_state_case, B, DATA, STACK, STOP
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import main_case, bl2_run


def run(u,saved,entry,count=200000):
    try:u.emu_start(B+entry,STOP,count=count)
    except Exception as error:
        raise RuntimeError(f'{error}; pc={u.reg_read(UC_ARM64_REG_PC):x}') from error
    assert_abi(u,saved)


def handoff_case(image,patched,boot_state,lock_control=None,smc_error=0,key_error=0):
    u,saved=machine(image)
    key=bytes(range(256))
    versions=[0x100001,0x202611,0x12345,0x6789a]
    u.mem_write(B+0x20d900,struct.pack('<I',boot_state))
    u.mem_write(B+0x119968,struct.pack('<I',boot_state))
    u.mem_write(B+0x20d8d0,struct.pack('<IIII',*versions))
    digest_words=[0x1020304050607080,0x1112131415161718,0xaabbccddeeff0123,0x7766554433221100]
    u.mem_write(B+0x20d8e0,struct.pack('<QQQQ',*digest_words))
    before=bytes(u.mem_read(B+0x20d8d0,0x34))
    packets,logs,locals_seen,hash_calls=[],[],[],[]

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value&0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        ins=int.from_bytes(uc.mem_read(addr,4),'little')
        if off==0x88578:
            if uc.reg_read(UC_ARM64_REG_W1)!=256:raise AssertionError('Key extraction size')
            if not key_error:uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),key)
            ret(key_error)
        elif off==0x1c0e0:
            ret(0) # Select the unchanged native software hash implementation.
        elif off==0xa4b20:
            hash_calls.append('real software SHA256')
        elif off==0x896e4 and lock_control is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',lock_control))
            ret()
        elif off==0x7ea84:
            logs.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)))
            ret()
        elif off==0x1e714:
            regs=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,
                                         UC_ARM64_REG_X4,UC_ARM64_REG_X5,UC_ARM64_REG_X6,UC_ARM64_REG_X7)]
            if regs[0]!=0xc2000120:raise AssertionError('Unexpected secure call')
            packets.append(regs)
            sp=uc.reg_read(UC_ARM64_REG_SP)
            locals_seen.append(struct.unpack('<I',uc.mem_read(sp+0x2c,4))[0])
            out=struct.unpack('<Q',uc.mem_read(sp,8))[0]
            uc.mem_write(out,struct.pack('<QQQQ',smc_error,0,0,0))
            ret()
        elif ins in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    run(u,saved,0xc4de0)
    real_locked=int((3 if lock_control is None else lock_control)!=3)
    actual_hash=hashlib.sha256(key if not key_error else bytes(256)).digest()
    key_words=list(struct.unpack('<QQQQ',actual_hash)) if patched or real_locked else [0]*4
    expected=[
        [0xc2000120,*key_words,0,0,0],
        [0xc2000120,1 if patched else real_locked,0 if patched else boot_state,*versions[:2],0,0,0],
        [0xc2000120,*digest_words,0,0,0],
        [0xc2000120,*versions[2:],0,0,0,0,0],
        [0xc2000120,0,0,0,0,0,0,0]]
    if packets!=expected or locals_seen!=[real_locked]*5:
        raise AssertionError(('handoff mismatch',patched,boot_state,lock_control,key_error,packets,expected,locals_seen))
    if before!=bytes(u.mem_read(B+0x20d8d0,0x34)) or struct.unpack('<I',u.mem_read(B+0x119968,4))[0]!=boot_state:
        raise AssertionError('Real boot state or versions/digest globals changed')
    if not hash_calls:raise AssertionError('Native hash did not execute')
    fail_send=any('fail to send root of trust info' in s for s in logs)
    warn_unlocked=any('root of trust smc call is not locked' in s for s in logs)
    if fail_send!=bool(smc_error) or warn_unlocked!=(not bool(smc_error)):
        raise AssertionError('Original secure-call error logging was suppressed')
    if bool(key_error)!=any('fail to get public key info' in s for s in logs):
        raise AssertionError('Key extraction failure hidden')
    return {'patched':patched,'real_boot_state':boot_state,'getter_control':lock_control,
            'actual_internal_getter_used':lock_control is None,'real_lock_boolean':real_locked,
            'key_extraction_error':key_error,'secure_sink_return':smc_error,
            'packet1_key_hash_hex':struct.pack('<QQQQ',*packets[0][1:5]).hex(),
            'packet2_lock':packets[1][1],'packet2_boot_state':packets[1][2],
            'all_five_packets_match':True,'real_local_and_global_state_unchanged':True,
            'original_error_logging_preserved':True,'secure_sink_is_mock':True,'pass':True}


def seccfg_case(image,patched,state,status):
    u,saved=machine(image)
    responses=[]
    real_word=[]

    def ret(v=0):
        u.reg_write(UC_ARM64_REG_W0,v&0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        if off==0xb3bec:
            out=uc.reg_read(UC_ARM64_REG_X0)
            uc.mem_write(out,struct.pack('<I',state));real_word.append(out);ret(status)
        elif off==0x7ffe0:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2))
            text=fmt.replace('%u',str(uc.reg_read(UC_ARM64_REG_W3)))
            limit=uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),text.encode()[:limit-1]+b'\0')
            ret(len(text))
        elif off in (0x9cfc,0x9dac,0x9dec):
            responses.append((off,cstr(uc,uc.reg_read(UC_ARM64_REG_X0))));ret()
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    run(u,saved,0xc7fc)
    info=[s for off,s in responses if off==0x9cfc]
    failed=[s for off,s in responses if off==0x9dac]
    if info!=[f'seccfg_state is {1 if patched else state}','mtkrpmb_state is 4.'] or len(failed)!=int(status!=0):
        raise AssertionError(('seccfg formatting',responses))
    if len(real_word)!=1 or struct.unpack('<I',u.mem_read(real_word[0],4))[0]!=state:
        raise AssertionError('Actual seccfg state was modified')
    return {'patched':patched,'real_seccfg':state,'getter_status':status,'public_info':info,
            'real_state_unchanged':True,'read_errors_preserved':True,'pass':True}


def warning_case(image,patched,state,boot_mode):
    u,saved=machine(image)
    u.mem_write(B+0x119968,struct.pack('<I',state))
    u.mem_write(DATA+0x100,b'boot\0')
    u.reg_write(UC_ARM64_REG_X0,DATA+0x100)
    ui,fatal=[],[]

    def ret(v=0):
        u.reg_write(UC_ARM64_REG_W0,v)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        if off==0x10708:ret(boot_mode)
        elif off==0x871ec:ui.append(uc.reg_read(UC_ARM64_REG_W0));ret()
        elif off in (0x14890,0x7d808,0x9e28):ret()
        elif off==0x69ea8:fatal.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret()
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    run(u,saved,0x69e00)
    expected=([2] if boot_mode!=1 else []) if state in (1,3) else ([1] if state==2 and boot_mode!=1 and not patched else [])
    if ui!=expected or fatal!=(['boot'] if state in (1,3) else []):
        raise AssertionError(('warning branch changed',patched,state,boot_mode,ui,fatal))
    if struct.unpack('<I',u.mem_read(B+0x119968,4))[0]!=state:raise AssertionError('Real state changed')
    return {'patched':patched,'real_state':state,'boot_mode':boot_mode,'ui_warning_ids':ui,
            'verification_error_handler_invoked':bool(fatal),'pass':True}


def main():
    source=INPUT.read_bytes();output=(HERE/NAME).read_bytes()
    if build(source)[0]!=output or build(source)[0]!=output:raise AssertionError('Non-reproducible build')
    bad=bytearray(source);bad[0x12345]^=1
    try:build(bytes(bad))
    except ValueError:pass
    else:raise AssertionError('Wrong input accepted')
    handoff=[];seccfg=[];warnings=[]
    for patched,image in [(False,source),(True,output)]:
        for boot,lock,status,keyerr in itertools.product((0,1,2,3,0xffffffff),(None,3,4),(0,7),(0,9)):
            handoff.append(handoff_case(image,patched,boot,lock,status,keyerr))
        for state,status in itertools.product((0,1,3,4,0xffffffff),(0,7)):
            seccfg.append(seccfg_case(image,patched,state,status))
        for state,mode in itertools.product((0,1,2,3),(0,1,99)):
            warnings.append(warning_case(image,patched,state,mode))
    # Retain the meaningful v1 public/internal probes without rerunning its main
    # function, which would rewrite the old experiment's report.
    public=[]
    values=[('product','dash'),('unlocked','yes'),('secure','no'),('current-slot','a'),
            ('unlocked-critical','no'),('slot-count','2'),('unlocked-extra','untouched'),('secure-extra','untouched')]
    for entries in (values,list(reversed(values)),[]):
        for query in ('all','unlocked','secure','product','current-slot','missing','unlocked-extra','secure-extra'):
            public.append(fastboot_case(output,query,entries,True))
    public.append(fastboot_case(output,'',values,True,oem=True))
    for state in (0,1,2,3,0xffffffff):public.append(android_state_case(output,state,patched=True))
    for unlocked,rc in itertools.product((0,1,2),(0,1)):
        public.append(avb_device_state_case(output,unlocked,rc,True))
    for first,second in [(-8,0),(0,-8)]:public.append(android_state_case(output,2,first,second))
    internal=[];rng=random.Random(9500)
    for initial in [0,1,2,3,4,0xffffffff]+[rng.getrandbits(32) for _ in range(94)]:
        for entry in (0x896e4,0x88c18,0x885fc):internal.append(main_case(output,entry,initial,'getter'))
    for boot,control in itertools.product((0,1,2,0xffffffff),(None,0,1,2,3,4,0xffffffff)):
        internal.append(main_case(output,0x6979c,0,'avb',boot,control))
    for control in (None,0,1,2,3,4,0xffffffff):
        internal.append(main_case(output,0xb6588,0xffffffff,'lock_boolean',mock_state=control))
    rows=members(output);certs=[]
    for i,r in enumerate(rows):
        if r['name'] not in ('cert1','cert2'):continue
        raw=output[r['offset']+512:r['offset']+512+r['size']];der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        certs.append({'member':i,'retained_der_signature_valid':True})
    b,c=rows[3],rows[2]
    bl2=output[b['offset']+512:b['offset']+512+b['size']]
    record=output[c['offset']:c['offset']+512+c['size']]
    parsers=[]
    for mask in (0x60,0x1f4e2,0x1f400):
        got=bl2_run(bl2,record,mask)
        if got['result'] or (mask!=0x1f400 and (got['payload_hash']!=sha(output[512:rows[0]['end']]) or got['header_hash']!=sha(output[:512]))):
            raise AssertionError('Native ENG parser selected wrong hash')
        parsers.append(got)
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
    disassembly=[]
    for off,old,new,_ in patches():
        disassembly.extend({'payload_offset':hex(i.address),'bytes':i.bytes.hex(),'instruction':i.mnemonic+' '+i.op_str}
                           for i in decoder.disasm(output[512+off:516+off],off))
    report={'status':'PASS_BOUNDED_LK_REPLAY_ONLY','artifact_sha256':sha(output),
            'handoff_cpu_cases':len(handoff),'seccfg_reply_cpu_cases':len(seccfg),
            'warning_cpu_cases':len(warnings),'retained_v1_public_cpu_cases':len(public),
            'retained_internal_unlock_cpu_cases':len(internal),
            'total_main_cpu_cases':len(handoff)+len(seccfg)+len(warnings)+len(public)+len(internal),
            'retained_der_signatures':certs,'native_eng_bl2_parser_cases':parsers,
            'input_sha256':INPUT_SHA,'deterministic_build':True,'wrong_input_rejected':True,
            'handoff_cases':handoff,'seccfg_cases':seccfg,'warning_cases':warnings,
            'v1_public_cases':public,'internal_unlock_cases':internal,'patch_disassembly':disassembly,
            'mocks':['Secure-monitor sink: captures all5 calls and simulates return structures; NOT the actual TEE',
                     'OEM key extraction: deterministic fixture/failure at the existing boundary',
                     'Crypto selector set to software; real native SHA256 then executes',
                     'Labelled lock controls only; unlabelled controls run actual force-unlocked getter',
                     'seccfg read boundary, USB/snprint/cmdline/UI/fatal-handler boundaries, PAC/logging'],
            'limits':['No phone flash/boot, actual secure-world processing or real attestation certificate measured',
                      'No Play Integrity or VNeID success demonstrated',
                      'Keystore/data-key compatibility on the phone remains unverified',
                      'Fake outgoing status does not establish authentic verified boot or repair extraction/hash/secure-call errors']}
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('status','artifact_sha256','handoff_cpu_cases','seccfg_reply_cpu_cases',
                                          'warning_cpu_cases','retained_v1_public_cpu_cases','retained_internal_unlock_cpu_cases','total_main_cpu_cases')},indent=2))


if __name__=='__main__':main()

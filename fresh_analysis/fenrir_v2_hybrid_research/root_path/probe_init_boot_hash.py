"""Actual libavb init_boot hash descriptor and eligibility filter, no device IO.

This tests the native partition-hash path and native slot-result filter as
bounded functions/slices. It is not a complete slot verification or device boot.
"""
from pathlib import Path
import hashlib
import itertools
import json
import struct
import sys
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
B, DATA, STACK, STOP, HEAP = 0x90700000, 0x3000000, 0x2000000, 0x4000000, 0x5000000
MAPS = {
    'stock_h3': dict(path='lk_hyperos3_original/lk.img', hash_entry=0xbfdb0,
        read=0xc068c, log=(0xc0b68, 0xc0874), strdup=0xc101c,
        filter_entry=0xbe270, accepted=0xbe298, rejected=(0xbe3f4,0xbe2f4),
        flag_entry=0x68640, flag_stop=0x6867c, flag_patch=0x68668,
        wrapper=0x88750, setter=0x692f4, setter_store=0x692fc, cache=0x121990,
        caller_after_verify=0x6887c, caller_load=0x68888, caller_reject=0x688c4,
        loader=0x68a54, boot_selector=0xfc14,
        sha_stream=(0xc6564,0xc659c,0xc65e0),software_sha=0xa4550),
    'debug': dict(path='experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img', hash_entry=0xc03e4,
        read=0xc0cc0, log=(0xc119c,0xc0ea8), strdup=0xc1650,
        filter_entry=0xbe8a4, accepted=0xbe8cc, rejected=(0xbea28,0xbe928),
        flag_entry=0x69410, flag_stop=0x6944c, flag_patch=0x69438,
        wrapper=0x88c18, thunk=0x885fc, setter=0x69dec, setter_store=0x69df4, cache=0x119968,
        caller_after_verify=0x6964c, caller_load=0x69658, caller_reject=0x69694,
        loader=0x69824, boot_selector=0xf894,
        sha_stream=(0xc6a38,0xc6a70,0xc6ab4),software_sha=0xa4b20),
}


def cstr(u, address):
    out = bytearray()
    for _ in range(4096):
        b = bytes(u.mem_read(address+len(out),1))
        if not b[0]: return out.decode()
        out += b
    raise ValueError('Unterminated string')


def machine(image):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    for address, size in ((B,0x500000),(DATA,0x10000),(STACK,0x10000),(STOP,0x1000),(HEAP,0x100000)):
        u.mem_map(address,size)
    size = int.from_bytes(image[4:8],'little')
    u.mem_write(B,image[512:512+size]);u.reg_write(UC_ARM64_REG_SP,STACK+0xf000)
    u.reg_write(UC_ARM64_REG_LR,STOP)
    return u


def ret(u, value=0):
    u.reg_write(UC_ARM64_REG_X0,value&((1<<64)-1))
    u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))


def fixture():
    # Android boot header v4-shaped fixture, original ramdisk64 bytes.
    image = bytearray(4096+64)
    image[:8] = b'ANDROID!'
    struct.pack_into('<I',image,12,64)
    struct.pack_into('<I',image,20,1584)
    struct.pack_into('<I',image,40,4)
    image[4096:] = bytes(range(64))
    return bytes(image)


def native_sha256(image, entry, data):
    u=machine(image);u.mem_write(DATA,data)
    for reg,val in ((UC_ARM64_REG_X0,DATA),(UC_ARM64_REG_X1,len(data)),(UC_ARM64_REG_X2,DATA+0x8000)):
        u.reg_write(reg,val)
    def hook(uc,addr,size,_):
        if int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook);u.emu_start(B+entry,STOP,count=3000000)
    assert u.reg_read(UC_ARM64_REG_PC)==STOP
    result=bytes(u.mem_read(DATA+0x8000,32));assert result==hashlib.sha256(data).digest()
    return result


def descriptor(original, algorithm='sha256', bad_length=False):
    name=b'init_boot';salt=b'root-path-fixture'
    digest=hashlib.sha256(salt+original).digest()
    body=(struct.pack('>Q',len(original))+algorithm.encode().ljust(32,b'\0')+
          struct.pack('>IIII',len(name),len(salt),len(digest),0)+bytes(60)+name+salt+digest)
    body += bytes((-len(body))%8)
    return struct.pack('>QQ',2,len(body) if not bad_length else 8)+body


def hash_case(kind, image, mode, flags):
    cfg=MAPS[kind];u=machine(image);original=fixture();candidate=bytearray(original)
    if mode=='mutated_ramdisk': candidate[4100]^=0x5a
    elif mode=='grown_ramdisk':
        candidate += b'ROOT-ADDED-RAMDISK'*8
        struct.pack_into('<I',candidate,12,len(candidate)-4096)
    algorithm='unsupported' if mode=='bad_algorithm' else 'sha256'
    desc=descriptor(original,algorithm,mode=='bad_descriptor')
    ops,req,suffix,desc_at,slot=DATA+0x1000,DATA+0x2000,DATA+0x2200,DATA+0x3000,DATA+0x4000
    u.mem_write(ops+0x58,struct.pack('<Q',STOP+0x100))
    u.mem_write(req,struct.pack('<QQ',DATA+0x2100,0));u.mem_write(DATA+0x2100,b'init_boot\0')
    u.mem_write(suffix,b'_a\0');u.mem_write(desc_at,desc)
    u.mem_write(slot+0x18,struct.pack('<Q',DATA+0x5000))
    for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,
                        UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5),
                       (ops,req,suffix,flags,desc_at,slot)):
        u.reg_write(reg,val)
    reads=[];logs=[];size_queries=[];heap=[HEAP+0x10000];hash_stream=[];native_hash_calls=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if addr==STOP+0x100:
            name=cstr(uc,uc.reg_read(UC_ARM64_REG_X1));size_queries.append(name)
            assert name=='init_boot_a'
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X2),struct.pack('<Q',len(candidate)));ret(uc)
        elif off==cfg['read']:
            regs=[uc.reg_read(r) for r in (UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4)]
            name=cstr(uc,regs[0]);assert name=='init_boot_a'
            reads.append(dict(partition=name,bytes=regs[1]))
            if mode=='io_error':ret(uc,2);return
            assert regs[1]<=len(candidate)
            uc.mem_write(HEAP,bytes(candidate[:regs[1]]))
            uc.mem_write(regs[2],struct.pack('<Q',HEAP));uc.mem_write(regs[3],b'\1');ret(uc)
        elif off in cfg['log']:
            for reg in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5):
                address=uc.reg_read(reg)
                if B<=address<B+0x500000:
                    try:logs.append(cstr(uc,address))
                    except (UnicodeDecodeError,ValueError):pass
            ret(uc)
        elif off==cfg['strdup']:
            text=cstr(uc,uc.reg_read(UC_ARM64_REG_X0)).encode()+b'\0'
            result=heap[0];heap[0]+=0x100;uc.mem_write(result,text);ret(uc,result)
        elif off==cfg['sha_stream'][0]:
            hash_stream.clear();ret(uc)
        elif off==cfg['sha_stream'][1]:
            hash_stream.append(bytes(uc.mem_read(uc.reg_read(UC_ARM64_REG_X1),uc.reg_read(UC_ARM64_REG_X2))));ret(uc)
        elif off==cfg['sha_stream'][2]:
            # Shipped stream wrappers call a secure hardware engine. Substitute
            # its boundary with this same image's unchanged software SHA256.
            hashed=b''.join(hash_stream);digest=native_sha256(image,cfg['software_sha'],hashed)
            native_hash_calls.append(dict(bytes=len(hashed),digest=digest.hex()))
            uc.mem_write(HEAP+0x20000,digest);ret(uc,HEAP+0x20000)
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook)
    try:u.emu_start(B+cfg['hash_entry'],STOP,count=3000000)
    except Exception as error:raise RuntimeError(f'{kind}/{mode}/{flags}: {error};pc={u.reg_read(UC_ARM64_REG_PC):x}') from error
    assert u.reg_read(UC_ARM64_REG_PC)==STOP
    result=u.reg_read(UC_ARM64_REG_W0)
    expected=0 if mode=='original' else 3 if mode in ('mutated_ramdisk','grown_ramdisk') else 2 if mode=='io_error' else 6
    assert result==expected,(kind,mode,flags,result,expected,logs)
    count=struct.unpack('<Q',u.mem_read(slot+0x20,8))[0]
    row={}
    if count:
        raw=bytes(u.mem_read(DATA+0x5000,32))
        name_ptr,data_ptr,data_size=struct.unpack_from('<QQQ',raw)
        row=dict(name=cstr(u,name_ptr),data_size=data_size,result=struct.unpack_from('<I',raw,28)[0])
        assert row['name']=='init_boot' and row['result']==result
    if mode in ('original','mutated_ramdisk','grown_ramdisk'):
        assert count==1 and reads and reads[0]['bytes']==(len(candidate) if flags&1 else len(original))
    if mode in ('mutated_ramdisk','grown_ramdisk'):
        assert any('Hash of data does not match' in text for text in logs)
    return dict(base=kind,mode=mode,flags=flags,return_code=result,read_requests=reads,
                partition_size_queries=size_queries,loaded_partition=row,
                hash_mismatch_logged=any('Hash of data does not match' in text for text in logs),
                native_software_sha256_calls=native_hash_calls,
                descriptor_and_digest_comparison_code_native=True,pass_case=True)


def eligibility_case(kind,image,result,flags):
    cfg=MAPS[kind];u=machine(image)
    u.reg_write(UC_ARM64_REG_W25,flags);u.reg_write(UC_ARM64_REG_W26,result)
    outcome=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off in (cfg['accepted'],*cfg['rejected']):outcome.append(off==cfg['accepted']);uc.emu_stop()
    u.hook_add(UC_HOOK_CODE,hook)
    u.emu_start(B+cfg['filter_entry'],STOP,count=100)
    wanted=result in (0,3,4,5) if flags&1 else result==0
    assert outcome==[wanted]
    return dict(base=kind,result=result,flags=flags,eligible=wanted,pass_case=True)


def flag_case(kind,image,expected_flag,control_state=None):
    cfg=MAPS[kind];u=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29,sp+0x100);stopped=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off==cfg['flag_stop']:stopped.append(off);uc.emu_stop()
        elif off==cfg['wrapper'] and control_state is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',control_state));ret(uc)
        elif off in (0x7de54,0x7ea84):ret(uc)
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook);u.emu_start(B+cfg['flag_entry'],STOP,count=10000)
    assert stopped==[cfg['flag_stop']]
    actual_flag=u.reg_read(UC_ARM64_REG_W22);assert actual_flag==expected_flag
    return dict(base=kind,control_state=control_state,allow_verification_error_flag=actual_flag,
                common_wrapper_native=control_state is None,
                stored_verified_boot_state=struct.unpack('<I',u.mem_read(B+cfg['cache'],4))[0],pass_case=True)


def caller_case(kind,image,result,flags):
    cfg=MAPS[kind];u=machine(image);u.reg_write(UC_ARM64_REG_W22,flags)
    u.reg_write(UC_ARM64_REG_W0,result);stop=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off in (cfg['caller_load'],cfg['caller_reject']):stop.append(off);uc.emu_stop()
    u.hook_add(UC_HOOK_CODE,hook)
    u.emu_start(B+cfg['caller_after_verify'],STOP,count=100)
    expected=cfg['caller_load'] if result==0 or flags&1 else cfg['caller_reject']
    assert stop==[expected]
    return dict(base=kind,result=result,flags=flags,reaches_post_slot_load=expected==cfg['caller_load'],pass_case=True)


def validate(image,kind):
    """Replay supplied final bytes without writing any code in emulator RAM."""
    assert kind in MAPS
    flag=flag_case(kind,image,1)
    hashes=[hash_case(kind,image,mode,f) for mode,f in itertools.product(
        ('original','mutated_ramdisk','grown_ramdisk','bad_algorithm','bad_descriptor','io_error'),(0,1))]
    filters=[eligibility_case(kind,image,result,f) for result,f in itertools.product(range(9),(0,1))]
    callers=[caller_case(kind,image,result,f) for result,f in itertools.product((0,3,6),(0,1))]
    for mode in ('mutated_ramdisk','grown_ramdisk'):
        h=next(row for row in hashes if row['mode']==mode and row['flags']==flag['allow_verification_error_flag'])
        assert h['return_code']==3
        assert next(row for row in filters if row['result']==h['return_code'] and row['flags']==1)['eligible']
        assert next(row for row in callers if row['result']==3 and row['flags']==1)['reaches_post_slot_load']
    return dict(status='PASS_FINAL_NATIVE_INIT_BOOT_HASH_PATH_NOT_BOOT_PROOF',base=kind,
                image_sha256=hashlib.sha256(image).hexdigest(),code_ram_modified=False,
                native_flag_case=flag,hash_cases=hashes,filter_cases=filters,caller_cases=callers,
                hash_cpu_cases=len(hashes),filter_cpu_cases=len(filters),caller_cpu_cases=len(callers),
                flag_cpu_cases=1,total_cpu_cases=1+len(hashes)+len(filters)+len(callers))


def main():
    hash_cases=[];filter_cases=[];flag_cases=[];caller_cases=[]
    for kind,cfg in MAPS.items():
        image=(ROOT/cfg['path']).read_bytes()
        for mode,flags in itertools.product(('original','mutated_ramdisk','grown_ramdisk','bad_algorithm','bad_descriptor','io_error'),(0,1)):
            hash_cases.append(hash_case(kind,image,mode,flags))
        for result,flags in itertools.product(range(9),(0,1)):
            filter_cases.append(eligibility_case(kind,image,result,flags))
        for state in (3,4):flag_cases.append(flag_case(kind,image,int(state==3),state))
        for result,flags in itertools.product((0,3,6),(0,1)):
            caller_cases.append(caller_case(kind,image,result,flags))
    report=dict(status='PASS_NATIVE_INIT_BOOT_HASH_AND_SLOT_FILTER_NOT_FULL_BOOT',
                hash_descriptor_cpu_cases=len(hash_cases),slot_result_filter_cpu_cases=len(filter_cases),
                flag_cpu_cases=len(flag_cases),caller_cpu_cases=len(caller_cases),
                total_cpu_cases=len(hash_cases)+len(filter_cases)+len(flag_cases)+len(caller_cases),
                maps=MAPS,hash_cases=hash_cases,filter_cases=filter_cases,
                flag_cases=flag_cases,caller_cases=caller_cases,
                mocks=['Partition size/read helper returns synthetic Android-v4-shaped init_boot bytes',
                       'Logging and strdup allocation; PAC skipped',
                       'Secure-engine streaming wrappers replaced at their boundary with same-image native software SHA256, independently checked against hashlib',
                       'Native descriptor parser, name/suffix handling, digest comparison and slot eligibility filter execute'],
                limits=['No full vbmeta signature chain, boot-image loading, kernel/root daemon or device execution',
                        'Two bounded stages establish hash mismatch3 and its eligible flag1 path, not whole-boot success',
                        'User recovery observation is consistent with hash failure under stock-policy flag0, not log-proven cause'])
    (HERE/'init_boot_hash_validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(report['status'],len(hash_cases),'hash and',len(filter_cases),'filter cases')


if __name__=='__main__':main()

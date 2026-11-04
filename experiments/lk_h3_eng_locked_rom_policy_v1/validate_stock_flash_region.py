"""Bounded real-code replay; never writes a phone or reuses old report outputs."""
import itertools
import json
import random
import struct
import sys
from pathlib import Path
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_stock_flash_region import *
from assemble_callback import assemble

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine, cstr, assert_abi, B, DATA, STACK, STOP
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run, main_case


def execute(u, entry, count=200000):
    try:
        u.emu_start(B + entry, STOP, count=count)
    except Exception as error:
        raise RuntimeError(f'{error}; pc={u.reg_read(UC_ARM64_REG_PC):x}') from error
    if u.reg_read(UC_ARM64_REG_PC) != STOP:
        raise AssertionError('Did not return within bounded replay')


def key_case(image, bits, modulus, expected, *, size=None, null_key=False,
             null_out=False, hash_error=False, entry=0xc5888):
    u, saved = machine(image)
    blob = struct.pack('>I', bits) + bytes(4) + modulus + bytes(len(modulus))
    u.mem_write(DATA + 0x800, blob)
    u.mem_write(DATA + 0x700, b'\xa5\x5a')
    u.reg_write(UC_ARM64_REG_X1, 0 if null_key else DATA+0x800)
    u.reg_write(UC_ARM64_REG_X2, len(blob) if size is None else size)
    u.reg_write(UC_ARM64_REG_X5, 0 if null_out else DATA+0x700)
    seen = []

    def hook(uc, addr, size_, _):
        off = addr-B
        ins = int.from_bytes(uc.mem_read(addr, 4), 'little')
        if off == 0xa4b20:
            seen.append('native_software_sha256')
            if hash_error:
                uc.reg_write(UC_ARM64_REG_W0, 7)
                uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif off == 0x7ea84:
            uc.reg_write(UC_ARM64_REG_W0, 0)
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif ins in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr+4)

    u.hook_add(UC_HOOK_CODE, hook)
    execute(u, entry)
    assert_abi(u, saved)
    result = (u.reg_read(UC_ARM64_REG_W0), u.mem_read(DATA+0x700, 1)[0])
    if result != expected or u.mem_read(DATA+0x701, 1)[0] != 0x5a:
        raise AssertionError(('key result', bits, size, null_key, null_out, hash_error, result, expected))
    return {'bits': bits, 'modulus_sha256': sha(modulus), 'size': len(blob) if size is None else size,
            'null_key': null_key, 'null_output': null_out, 'injected_hash_error': hash_error,
            'return': result[0], 'trusted': result[1], 'native_sha256_executed': bool(seen), 'pass': True}


def region_case(image, patched, region, *, sb=14, hw=0, fuse_rc=0,
                rpmb_rc=0, rpmb_error=0, unlocked='no', config=False):
    u, saved = machine(image)
    u.mem_write(DATA+0x100, region.encode()+b'\0')
    u.mem_write(DATA+0x400, unlocked.encode()+b'\0')
    u.mem_write(DATA+0x500, b'yes\0')
    u.reg_write(UC_ARM64_REG_X0, DATA+0x100)
    replies, calls = [], []

    def ret(v=0):
        u.reg_write(UC_ARM64_REG_X0, v & ((1 << 64)-1))
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, _):
        off=addr-B
        if off in (0x7ea84, 0x7d808):
            ret()
        elif off in (0x9cfc, 0x9dec, 0x9dac):
            replies.append(('INFO' if off==0x9cfc else 'OKAY' if off==0x9dec else 'FAIL', cstr(uc,uc.reg_read(UC_ARM64_REG_X0))))
            ret()
        elif off==0x21c80:
            ret(DATA+0x500 if config else 0)
        elif off==0x21db0:
            ret(int(config))
        elif off==0x3585c:
            ret(2)
        elif off==0x95cec:
            calls.append('rpmb_read')
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X4), struct.pack('<I',rpmb_error))
            ret(rpmb_rc)
        elif off==0x9bc8:
            if cstr(uc,uc.reg_read(UC_ARM64_REG_X0))!='unlocked':
                raise AssertionError('Unexpected cache lookup')
            ret(DATA+0x400)
        elif off==0x4b114:
            calls.append('secureboot_efuse_read')
            ret(sb)
        elif off==0x4b510:
            calls.append('region_efuse_read')
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X1),struct.pack('<I',hw))
            ret(fuse_rc)
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    execute(u,0x68bfc)
    assert_abi(u,saved)
    bypass=not region or region.startswith(' factory') or config
    denied_rpmb=not bypass and rpmb_rc!=0 and rpmb_error==7 and unlocked=='no'
    denied_region=not bypass and not denied_rpmb and (sb&14)==14 and not fuse_rc and hw==0 and not patched and not region.startswith((' cn',' CN'))
    failure=[s for typ,s in replies if typ=='FAIL']
    okay=[s for typ,s in replies if typ=='OKAY']
    wanted=('Not allow flashing when rpmb no provision and device locked.' if denied_rpmb
            else 'CN devices do not allow flashing non-CN ROMs.' if denied_region else None)
    if failure != ([wanted] if wanted else []) or len(okay)!=int(wanted is None):
        raise AssertionError(('region',patched,region,sb,hw,fuse_rc,rpmb_rc,rpmb_error,unlocked,config,replies,wanted))
    return {'patched':patched,'region_argument':region,'secureboot_bits':sb,'hardware_region':hw,
            'fuse_error':fuse_rc,'rpmb_return':rpmb_rc,'rpmb_error':rpmb_error,
            'unlocked_cache':unlocked,'signed_config_bypass':config,
            'outcome':wanted or 'allowed','read_calls':calls,'pass':True}


def main():
    source=INPUT.read_bytes()
    output=(HERE/NAME).read_bytes()
    if assemble()!=(HERE/'trust_cn_global.bin').read_bytes():
        raise AssertionError('Assembly not reproducible')
    if build(source)[0]!=output or build(source)[0]!=output:
        raise AssertionError('Build not reproducible')
    wrong=bytearray(source);wrong[0x12345]^=1
    try:build(bytes(wrong))
    except ValueError:pass
    else:raise AssertionError('Wrong input accepted')
    # Explicitly prove original lock/token/RPMB code, fastboot reports, boot args,
    # verified boot execution and root-of-trust handoff were not replaced.
    preserved=[(0x896e4,0x89cc0),(0x885fc,0x88670),(0x88c18,0x88c80),
               (0x99dc,0x9c20),(0xaf90,0xb070),(0xc208,0xc3b4),(0xc770,0xc92c),
               (0x6935c,0x69f10),(0xb6588,0xb66a8),(0xbc77c,0xbc7b0),
               (0xc4de0,0xc5004),(0x1306b0,0x130828)]
    for lo,hi in preserved:
        if source[512+lo:512+hi]!=output[512+lo:512+hi]:
            raise AssertionError(('Protected original ENG behavior changed',hex(lo)))
    stock=STOCK_H3.read_bytes()
    keys={'ENG':source[512+0x130728:512+0x130828],
          'CN':stock[512+0x138638:512+0x138838],
          'GL':stock[512+0x138838:512+0x138a38]}
    cases=[]
    rng=random.Random(9500)
    for patched, image in [(False,source),(True,output)]:
        for kind,key in keys.items():
            cases.append(dict(kind=kind,patched=patched,**key_case(image,2048 if kind=='ENG' else 4096,key,
                (0,int(kind=='ENG' or patched)))))
        for bits in (1024,2048,4096,8192):
            length=bits//8
            cases.append(dict(kind='unknown',patched=patched,**key_case(image,bits,rng.randbytes(length),(0,0))))
        for kind in ('CN','GL','ENG'):
            key=keys[kind]
            for i in (0,1,15,31,len(key)//2,len(key)-1):
                mutated=bytearray(key);mutated[i]^=1
                cases.append(dict(kind=kind+'_one_bit_changed',patched=patched,
                    **key_case(image,2048 if kind=='ENG' else 4096,bytes(mutated),(0,0))))
        for null_key,null_out in [(True,False),(False,True),(True,True)]:
            cases.append(dict(kind='null',patched=patched,**key_case(image,4096,keys['CN'],(2,0xa5),null_key=null_key,null_out=null_out)))
    for bits,key in [(2048,keys['ENG']),(4096,keys['CN'])]:
        for length in (0,4,7,8,263,264,519,521,1031,1033):
            cases.append(dict(kind='malformed_length',patched=True,
                **key_case(output,bits,key,(2,0),size=length)))
    cases.append(dict(kind='hash_failure',patched=True,**key_case(output,4096,keys['CN'],(0,0),hash_error=True)))
    # Prove the CPU's actual SHA256 output matches the Python fingerprint, not
    # merely a non-matching key accepted by accidental fallthrough.
    altered=bytearray(output);altered[512+0x68ec0]^=1
    cases.append(dict(kind='stored_CN_hash_corrupted',patched=True,
        **key_case(bytes(altered),4096,keys['CN'],(0,0))))
    regions=[]
    for patched,image in [(False,source),(True,output)]:
        for region,sb,hw,fuse_rc in itertools.product(
                [' cn',' CN',' gl',' GL',' GLOBAL',' ENG',' unknown','','factory',' factory'],[0,14],[0,1],[0,7]):
            regions.append(region_case(image,patched,region,sb=sb,hw=hw,fuse_rc=fuse_rc))
        for region,rc,err,unlocked,config in itertools.product(
                [' CN',' GL'],[-1,-6],[0,7],['no','yes'],[False,True]):
            regions.append(region_case(image,patched,region,rpmb_rc=rc,rpmb_error=err,unlocked=unlocked,config=config))
    predicates=[]
    for image,label in [(source,'source'),(output,'patched')]:
        for state in (1,2,3,4):
            predicates.append(dict(image=label,**main_case(image,0x6979c,0,'avb',mock_state=state)))
            predicates.append(dict(image=label,**main_case(image,0xb6588,0,'lock_boolean',mock_state=state)))
            predicates.append(dict(image=label,**main_case(image,0xc770,0,'fastboot',mock_state=state)))
    rows=members(output)
    original_rows=members(source)
    for image, row in [(source,original_rows[2]),(output,rows[2])]:
        pad=image[row['offset']+512+row['size']:row['end']]
        if any(pad):raise AssertionError('Unexpected certificate alignment padding')
    certs=[]
    for i,r in enumerate(rows):
        if r['name'] not in ('cert1','cert2'):continue
        raw=output[r['offset']+512:r['offset']+512+r['size']]
        der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        certs.append({'member':i,'retained_der_signature_valid':True})
    r,c=rows[3],rows[2]
    bl2=output[r['offset']+512:r['offset']+512+r['size']]
    record=output[c['offset']:c['offset']+512+c['size']]
    parsers=[]
    for mask in (0x60,0x1f4e2,0x1f400):
        got=bl2_run(bl2,record,mask)
        if got['result'] or (mask!=0x1f400 and (got['payload_hash']!=sha(output[512:rows[0]['end']]) or got['header_hash']!=sha(output[:512]))):
            raise AssertionError('Native ENG BL2 did not choose patched hash')
        parsers.append(got)
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);decoder.skipdata=True
    for ins in decoder.disasm(source[512+0x3000:512+0xc8000],0x3000):
        if ins.mnemonic in ('b','bl','cbz','cbnz','tbz','tbnz') or ins.mnemonic.startswith('b.'):
            last=ins.op_str.rsplit(',',1)[-1].strip()
            if not last.startswith('#0x'):continue
            target=int(last[1:],16)
            if 0x68ec0<=target<0x68f00 and not 0x68ec0<=ins.address<0x68f00 and ins.address!=OFFSET:
                raise AssertionError('External branch can enter repurposed region constants')
            if 0xc5888<target<0xc5960 and not 0xc5888<=ins.address<0xc5960:
                raise AssertionError('External branch enters rewritten callback interior')
    disassembly=[{'payload_offset':hex(ins.address),'bytes':ins.bytes.hex(),'instruction':ins.mnemonic+' '+ins.op_str}
        for ins in decoder.disasm(output[512+0xc5888:512+0xc5960],0xc5888)]
    report={'status':'PASS','input_sha256':INPUT_SHA,'artifact_sha256':sha(output),
            'key_trust_cpu_cases':len(cases),'flash_check_cpu_cases':len(regions),
            'original_lock_predicate_cpu_cases':len(predicates),
            'total_main_cpu_cases':len(cases)+len(regions)+len(predicates),
            'retained_der_signatures':certs,'native_eng_bl2_parser_cases':parsers,
            'original_lock_code_ranges_preserved':[[hex(a),hex(b)] for a,b in preserved],
            'deterministic_build_and_assembly':True,'wrong_input_rejected':True,
            'key_cases':cases,'region_cases':regions,'predicate_cases':predicates,'callback_disassembly':disassembly,
            'mocks':['PAC/logging/fastboot response transport','efuse/RPMB read/boot-device/dconfig/fastboot cache boundaries in labelled region cases',
                     'lock getter only in labelled state controls','SHA256 failure only in one labelled negative test'],
            'actual_code_executed':['SHA256 software implementation','memcmp/strcmp/strncmp','new and original key callbacks','original full flash_check function'],
            'limits':['No physical flash/boot or persisted lock-state write',
                      'No complete AVB slot verification with a target ROM signature',
                      'Known CN/GL modulus acceptance does not prove every ROM/model/rollback combination boots']}
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('status','artifact_sha256','key_trust_cpu_cases','flash_check_cpu_cases','original_lock_predicate_cpu_cases','total_main_cpu_cases')},indent=2))


if __name__=='__main__':main()

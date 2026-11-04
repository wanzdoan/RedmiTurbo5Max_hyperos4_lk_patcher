"""Real LK countrycode read flow + real hardware-cache copy; no phone writes."""
import itertools
import json
import struct
import sys
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from capstone import Cs,CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_auto_hwc import *
from assemble_auto import assemble

sys.path.insert(0,str(HERE.parent))
from validate_stock_flash_region import key_case
from build_stock_flash_region import STOCK_H3
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine,cstr,B,DATA,STACK,STOP
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run,main_case


def flow_case(image,automatic,country,hardware='CN',level='MP',version='1.0',suffix='_a',mode='ok',fmt_fail=None):
    u,saved=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29,sp+0x290)
    preserved={r:u.reg_read(r) for r in saved if r not in (UC_ARM64_REG_X19,UC_ARM64_REG_X20)}
    hardware_record=bytearray(b'\xa5'*0x54)
    for off,text in [(0,level),(16,version),(36,hardware)]:
        hardware_record[off:off+16]=text.encode().ljust(16,b'\0')
    u.mem_write(B+0x135018,bytes(hardware_record))
    u.mem_write(sp+0x11c,b'LEFT');u.mem_write(sp+0x174,b'RGHT')
    u.mem_write(DATA+0x100,suffix.encode()+b'\0')
    region=country.encode()[:16].ljust(16,b'\0')
    appended=[];get_names=[];reads=[];closes=[];hw_calls=[];errors=[];formats=[]
    phase=['early'];stop_at=[0x133e4];stopped=[]

    def ret(v=0):
        u.reg_write(UC_ARM64_REG_X0,v&((1<<64)-1))
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        if off==stop_at[0]:stopped.append(off);uc.emu_stop()
        elif off==0x3530:
            hw_calls.append(phase[0]) # Run the real native getter and its memcpy.
        elif off==0x1f2b0:ret(DATA+0x100)
        elif off==0x7ffe0:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2))
            if fmt=='countrycode%s':
                kind='name';text=fmt%cstr(uc,uc.reg_read(UC_ARM64_REG_X3))
            elif fmt=='androidboot.ptcountrycode=%s':
                kind='country';text=fmt%cstr(uc,uc.reg_read(UC_ARM64_REG_X3))
            elif fmt=='androidboot.hwc=%s androidboot.hwlevel=%s androidboot.hwversion=%s':
                kind='hwc';text=fmt%tuple(cstr(uc,uc.reg_read(r)) for r in (UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5))
                if uc.reg_read(UC_ARM64_REG_X4)!=sp+0x120 or uc.reg_read(UC_ARM64_REG_X5)!=sp+0x130:
                    raise AssertionError('HW level/version pointers changed')
            else:raise AssertionError(('Unexpected formatter',fmt))
            formats.append(kind)
            if kind==fmt_fail:ret(-1);return
            limit=uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),text.encode()[:limit-1]+b'\0');ret(len(text))
        elif off==0x7c990:
            name=cstr(uc,uc.reg_read(UC_ARM64_REG_X0));get_names.append(name)
            if name!='countrycode'+suffix:raise AssertionError('Slot suffix changed')
            ret(0 if mode=='missing' else DATA+0x1000)
        elif off==0x7ca24:
            values=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3)]
            if values!=[DATA+0x1000,sp,0,16]:raise AssertionError(('Read ABI changed',values))
            reads.append(values)
            content=bytes(16) if mode=='empty' else region
            uc.mem_write(sp,content)
            ret(7 if mode=='short' else -1 if mode=='io_error' else 16)
        elif off==0x7c914:closes.append(uc.reg_read(UC_ARM64_REG_X0));ret()
        elif off==0x18ab8:appended.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret()
        elif off==0x7ea84:errors.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret()
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    def run(entry,end):
        stop_at[0]=end;stopped.clear()
        try:u.emu_start(B+entry,STOP,count=50000)
        except Exception as error:raise RuntimeError(f'{error};pc={u.reg_read(UC_ARM64_REG_PC):x}') from error
        if stopped!=[end] or u.reg_read(UC_ARM64_REG_SP)!=sp:raise AssertionError('Caller continuation/frame changed')
    run(0x13364,0x133e4)
    early_count=len(appended)
    phase[0]='countrycode and late HWC'
    run(0x13728,0x139c4)
    # These are two bounded slices of one function. Unrelated intervening
    # bootargs are intentionally not replayed and are not described as a boot.
    valid_read=mode=='ok' and fmt_fail!='name' and bool(country)
    region_value=country[:15] if valid_read else ''
    selected=('CN' if region_value.lower()=='cn' else 'GLOBAL') if region_value else hardware
    expected_hwc=selected if automatic else 'GLOBAL'
    hw_output=[s for s in appended if s.startswith('androidboot.hwc=')]
    expected_line=f'androidboot.hwc={expected_hwc} androidboot.hwlevel={level} androidboot.hwversion={version}'
    if hw_output!=([expected_line] if fmt_fail!='hwc' else []):
        raise AssertionError(('HWC routing',automatic,country,hardware,mode,fmt_fail,hw_output,expected_line))
    if len(hw_calls)!=1 or hw_calls!=(['countrycode and late HWC'] if automatic else ['early']):
        raise AssertionError('Hardware getter duplicated or called in wrong phase')
    if automatic and early_count:raise AssertionError('Early HWC was not suppressed')
    expected_get=fmt_fail!='name'
    expected_read=expected_get and mode!='missing'
    if len(get_names)!=int(expected_get) or len(reads)!=int(expected_read) or len(closes)!=int(expected_read):
        raise AssertionError('Native partition access/close path changed')
    pt=[s for s in appended if s.startswith('androidboot.ptcountrycode=')]
    if pt!=([f'androidboot.ptcountrycode={country[:15]}'] if valid_read and fmt_fail!='country' else []):
        raise AssertionError('Original countrycode output changed')
    if automatic and mode in ('short','io_error') and fmt_fail!='name' and bytes(u.mem_read(sp,16))!=bytes(16):
        raise AssertionError('Partial I/O data was used as a region')
    if bytes(u.mem_read(B+0x135018,0x54))!=bytes(hardware_record):raise AssertionError('Hardware cache modified')
    if bytes(u.mem_read(sp+0x120,0x54))!=bytes(hardware_record):raise AssertionError('Native hardware copy changed')
    if bytes(u.mem_read(sp+0x11c,4))!=b'LEFT' or bytes(u.mem_read(sp+0x174,4))!=b'RGHT':
        raise AssertionError('Hardware record neighbors changed')
    if u.reg_read(UC_ARM64_REG_X19)!=sp+0x130:
        raise AssertionError('HW version pointer required by later caller code was not restored')
    if u.reg_read(UC_ARM64_REG_X0)!=sp+0x180:
        raise AssertionError('Original next-block output pointer was not restored')
    if any(u.reg_read(r)!=v for r,v in preserved.items()):raise AssertionError('Unrelated callee-saved register changed')
    if automatic and fmt_fail=='hwc' and not any('snprintf' in e for e in errors):
        raise AssertionError('Formatter failure diagnostic lost')
    return {'automatic':automatic,'countrycode_fixture':country,'hardware_hwc':hardware,
            'hwlevel':level,'hwversion':version,'slot_suffix':suffix,'read_mode':mode,'formatter_failure':fmt_fail,
            'outgoing_hwc':hw_output,'outgoing_countrycode':pt,'partition_reads':len(reads),
            'hardware_copy_calls':hw_calls,'later_hwversion_pointer_preserved':True,
            'hardware_cache_and_frame_unchanged':True,'pass':True}


def main():
    source=INPUT.read_bytes();out=(HERE/NAME).read_bytes()
    if assemble()!=(HERE/'auto_hwc.bin').read_bytes() or build(source)[0]!=out or build(source)[0]!=out:
        raise AssertionError('Assembly/build not reproducible')
    wrong=bytearray(source);wrong[12345]^=1
    try:build(bytes(wrong))
    except ValueError:pass
    else:raise AssertionError('Wrong input accepted')
    cases=[]
    countries=('cn','CN','Cn','cN','global','GLOBAL','eea','EEA','tw','TW','taiwan','eu','native','unknown','cn_extra','')
    for automatic,image in [(False,source),(True,out)]:
        for country,hardware,suffix in itertools.product(countries,('CN','GLOBAL'),('_a','_b')):
            cases.append(flow_case(image,automatic,country,hardware,suffix=suffix))
        for country,level,version in itertools.product(('cn','global','eea','tw'),('MP','EVT'),('1.0','V2.3')):
            cases.append(flow_case(image,automatic,country,level=level,version=version))
        for country,hardware,suffix,mode in itertools.product(('cn','global'),('CN','GLOBAL'),('_a','_b'),('missing','empty','short','io_error')):
            cases.append(flow_case(image,automatic,country,hardware,suffix=suffix,mode=mode))
        for country,hardware,stage in itertools.product(('cn','global'),('CN','GLOBAL'),('name','country','hwc')):
            cases.append(flow_case(image,automatic,country,hardware,fmt_fail=stage))
    # Verify key callback bytes/literal are unchanged; execute representative
    # real callbacks including the two allowed commercial keys and an unknown key.
    if source[512+0xc5888:512+0xc5960]!=out[512+0xc5888:512+0xc5960]:raise AssertionError('Key trust callback changed')
    stock=STOCK_H3.read_bytes();keys={'ENG':source[512+0x130728:512+0x130828],
        'CN':stock[512+0x138638:512+0x138838],'GL':stock[512+0x138838:512+0x138a38]}
    key_cases=[]
    for name,key in keys.items():key_cases.append(dict(kind=name,**key_case(out,2048 if name=='ENG' else 4096,key,(0,1))))
    key_cases.append(dict(kind='unknown4096',**key_case(out,4096,bytes(512),(0,0))))
    controls=[]
    for state in (1,2,3,4):
        controls.append(main_case(out,0x6979c,0,'avb',mock_state=state))
        controls.append(main_case(out,0xb6588,0,'lock_boolean',mock_state=state))
        controls.append(main_case(out,0xc770,0,'fastboot',mock_state=state))
    rows=members(out);certs=[]
    for i,r in enumerate(rows):
        if r['name'] not in ('cert1','cert2'):continue
        raw=out[r['offset']+512:r['offset']+512+r['size']];der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        certs.append({'member':i,'original_der_signature_valid':True})
    b,c=rows[3],rows[2];bl2=out[b['offset']+512:b['offset']+512+b['size']]
    rec=out[c['offset']:c['offset']+512+c['size']];parsers=[]
    for mask in (0x60,0x1f4e2,0x1f400):
        got=bl2_run(bl2,rec,mask)
        if got['result'] or (mask!=0x1f400 and (got['payload_hash']!=sha(out[512:rows[0]['end']]) or got['header_hash']!=sha(out[:512]))):
            raise AssertionError('Native BL2 hash parser selected wrong hashes')
        parsers.append(got)
    d=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);d.skipdata=True
    for i in d.disasm(source[512+0x3000:512+0xc8000],0x3000):
        if i.mnemonic in ('b','bl','cbz','cbnz','tbz','tbnz') or i.mnemonic.startswith('b.'):
            last=i.op_str.rsplit(',',1)[-1].strip()
            if last.startswith('#0x'):
                target=int(last[1:],16)
                if 0x13368<=target<0x133e4 and not 0x13364<=i.address<0x133e4:
                    raise AssertionError('External old branch enters repurposed HWC interior')
                if 0x13960<target<0x1396c and not 0x13960<=i.address<0x1396c:
                    raise AssertionError('External old branch enters rewritten read-error interior')
    assembly=[]
    for off,blob,_ in patches():
        assembly.extend({'payload_offset':hex(i.address),'bytes':i.bytes.hex(),'instruction':i.mnemonic+' '+i.op_str}
            for i in d.disasm(out[512+off:512+off+len(blob)],off))
    report={'status':'PASS_BOUNDED_LK_NOT_DEVICE_BOOT','input_sha256':INPUT_SHA,'artifact_sha256':sha(out),
        'countrycode_hwc_cpu_cases':len(cases),'key_callback_cpu_cases':len(key_cases),'lock_control_cpu_cases':len(controls),
        'total_main_cpu_cases':len(cases)+len(key_cases)+len(controls),
        'retained_der_signatures':certs,'native_eng_bl2_parser_cases':parsers,
        'deterministic_build_and_assembly':True,'wrong_input_rejected':True,
        'region_cases':cases,'key_cases':key_cases,'lock_controls':controls,'patch_disassembly':assembly,
        'native_code_executed':['Countrycode read control flow and slot-name setup','Hardware getter0x3530 and memcpy',
            'HWC selection, formatting argument routing, error/continuation branches','strlen/memset','Key callback/SHA256/memcmp','BL2 parser'],
        'mocks':['Partition lookup/read/close and slot-suffix source','snprintf/log/cmdline append boundaries','PAC','Labelled lock-state controls'],
        'limits':['Two bounded code slices skip unrelated intervening bootargs; not a complete function/device boot simulation',
            'EEA/Taiwan are synthetic countrycode fixtures until their actual images are provided',
            'No Android property readback or whole ROM compatibility/Integrity verification',
            'No physical flash, efuse or RPMB access']}
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('status','artifact_sha256','countrycode_hwc_cpu_cases','key_callback_cpu_cases','lock_control_cpu_cases','total_main_cpu_cases')},indent=2))


if __name__=='__main__':main()

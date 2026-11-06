"""Port of bounded HWC slices; hardware copy remains early on H4."""
import struct
import sys
from build_port import ROOT
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine,cstr,B,DATA,STACK,STOP

def flow_case(image,automatic,country,hardware='CN',level='MP',version='1.0',suffix='_a',mode='ok',fmt_fail=None):
    u,saved=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29,sp+0x220)
    preserved={r:u.reg_read(r) for r in saved if r not in (UC_ARM64_REG_X19,UC_ARM64_REG_X20)}
    hardware_record=bytearray(b'\xa5'*0x5c)
    for off,text in [(0,level),(16,version),(36,hardware)]:
        hardware_record[off:off+16]=text.encode().ljust(16,b'\0')
    u.mem_write(B+0x160558,bytes(hardware_record))
    u.mem_write(sp+0xac,b'LEFT');u.mem_write(sp+0x10c,b'RGHT')
    u.mem_write(DATA+0x100,suffix.encode()+b'\0')
    region=country.encode()[:16].ljust(16,b'\0')
    appended=[];get_names=[];reads=[];closes=[];hw_calls=[];errors=[];formats=[]
    phase=['early'];stop_at=[0x13bc4];stopped=[]

    def ret(v=0):
        u.reg_write(UC_ARM64_REG_X0,v&((1<<64)-1))
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))

    def hook(uc,addr,size,_):
        off=addr-B
        if off==stop_at[0]:stopped.append(off);uc.emu_stop()
        elif off==0x1c6a0:
            hw_calls.append(phase[0]) # Run the real native getter and its memcpy.
        elif off==0x1f790:ret(DATA+0x100)
        elif off==0x7f6fc:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2))
            if fmt=='countrycode%s':
                kind='name';text=fmt%cstr(uc,uc.reg_read(UC_ARM64_REG_X3))
            elif fmt=='androidboot.ptcountrycode=%s':
                kind='country';text=fmt%cstr(uc,uc.reg_read(UC_ARM64_REG_X3))
            elif fmt=='androidboot.hwc=%s androidboot.hwlevel=%s androidboot.hwversion=%s':
                kind='hwc';text=fmt%tuple(cstr(uc,uc.reg_read(r)) for r in (UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5))
                if uc.reg_read(UC_ARM64_REG_X4)!=sp+0xb0 or uc.reg_read(UC_ARM64_REG_X5)!=sp+0xc0:
                    raise AssertionError('HW level/version pointers changed')
            else:raise AssertionError(('Unexpected formatter',fmt))
            formats.append(kind)
            if kind==fmt_fail:ret(-1);return
            limit=uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),text.encode()[:limit-1]+b'\0');ret(len(text))
        elif off==0x7c028:
            name=cstr(uc,uc.reg_read(UC_ARM64_REG_X0));get_names.append(name)
            if name!='countrycode'+suffix:raise AssertionError('Slot suffix changed')
            ret(0 if mode=='missing' else DATA+0x1000)
        elif off==0x7c0bc:
            values=[uc.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3)]
            if values!=[DATA+0x1000,sp,0,16]:raise AssertionError(('Read ABI changed',values))
            reads.append(values)
            content=bytes(16) if mode=='empty' else region
            uc.mem_write(sp,content)
            ret(7 if mode=='short' else -1 if mode=='io_error' else 16)
        elif off==0x7bfac:closes.append(uc.reg_read(UC_ARM64_REG_X0));ret()
        elif off==0x19078:appended.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret()
        elif off==0x7e11c:errors.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret()
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)

    u.hook_add(UC_HOOK_CODE,hook)
    def run(entry,end):
        stop_at[0]=end;stopped.clear()
        try:u.emu_start(B+entry,STOP,count=50000)
        except Exception as error:raise RuntimeError(f'{error};pc={u.reg_read(UC_ARM64_REG_PC):x}') from error
        if stopped!=[end] or u.reg_read(UC_ARM64_REG_SP)!=sp:raise AssertionError('Caller continuation/frame changed')
    run(0x13b5c,0x13bc4)
    early_count=len(appended)
    phase[0]='countrycode and late HWC'
    run(0x13ed4,0x14170)
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
    if len(hw_calls)!=1 or hw_calls!=['early']:
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
    if bytes(u.mem_read(B+0x160558,0x5c))!=bytes(hardware_record):raise AssertionError('Hardware cache modified')
    if bytes(u.mem_read(sp+0xb0,0x5c))!=bytes(hardware_record):raise AssertionError('Native hardware copy changed')
    if bytes(u.mem_read(sp+0xac,4))!=b'LEFT' or bytes(u.mem_read(sp+0x10c,4))!=b'RGHT':
        raise AssertionError('Hardware record neighbors changed')
    if u.reg_read(UC_ARM64_REG_X19)!=sp+0xc0:
        raise AssertionError('HW version pointer required by later caller code was not restored')
    if u.reg_read(UC_ARM64_REG_X0)!=sp+0x110:
        raise AssertionError('Original next-block output pointer was not restored')
    if any(u.reg_read(r)!=v for r,v in preserved.items()):raise AssertionError('Unrelated callee-saved register changed')
    if automatic and fmt_fail=='hwc' and not any('snprintf' in e for e in errors):
        raise AssertionError('Formatter failure diagnostic lost')
    return {'automatic':automatic,'countrycode_fixture':country,'hardware_hwc':hardware,
            'hwlevel':level,'hwversion':version,'slot_suffix':suffix,'read_mode':mode,'formatter_failure':fmt_fail,
            'outgoing_hwc':hw_output,'outgoing_countrycode':pt,'partition_reads':len(reads),
            'hardware_copy_calls':hw_calls,'later_hwversion_pointer_preserved':True,
            'hardware_cache_and_frame_unchanged':True,'pass':True}


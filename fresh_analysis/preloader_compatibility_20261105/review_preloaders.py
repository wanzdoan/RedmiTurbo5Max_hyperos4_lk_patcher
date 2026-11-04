"""Offline stock preloader identity, bounded code/policy/DFD comparison.

No ADB, flashing, firmware edits, or invocation of hardware/security queries.
The existing private boot-LUN is used only for a byte-identity check.
"""
from pathlib import Path
from collections import Counter
import ast
import hashlib
import itertools
import json
import os
import re
import struct
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from capstone.arm64 import ARM64_OP_IMM, ARM64_OP_REG
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE, UC_HOOK_MEM_WRITE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
ARCHIVE = Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2')
STOCK_PATH = ARCHIVE/'dash_images_OS4.0.3.0.XPLCNXM_17.0/images/preloader_dash.bin'
ROOT_PATH = ARCHIVE/'preloader_dash.bin'
LIVE_PATH = Path(os.environ['LOCALAPPDATA'])/'Temp/dash-relock-readonly-20261104_123518/sda_boot_lun.bin'
DELTA = 0x2000f00
STACK, STOP, DATA = 0x3100000, 0x3200000, 0x3300000
sha = lambda data: hashlib.sha256(data).hexdigest()


def policy_run(b, sbc, state, entry, sbc_error=0, state_error=0):
    """Same unchanged accessor as earlier review; now true STOCK H3 inputs."""
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    u.mem_map(0x2000000,0x180000);u.mem_write(DELTA,b)
    u.mem_map(STACK,0x10000);u.mem_map(STOP,0x1000)
    def ret(v):
        u.reg_write(UC_ARM64_REG_W0,v)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,addr,size,_):
        off=addr-DELTA
        if off in (0xa4b04,0x8c9f4):
            value=sbc if off==0xa4b04 else state
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',value))
            ret(sbc_error if off==0xa4b04 else state_error)
        elif off==0x8aea0:ret(0)
        elif off==0x91190:raise AssertionError('stack guard failure')
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    u.hook_add(UC_HOOK_CODE,hook)
    u.reg_write(UC_ARM64_REG_SP,STACK+0xf000);u.reg_write(UC_ARM64_REG_LR,STOP)
    u.reg_write(UC_ARM64_REG_W0,2)  # real table row for LK partition
    u.emu_start(DELTA+entry,STOP,count=100000)
    assert u.reg_read(UC_ARM64_REG_PC)==STOP
    return u.reg_read(UC_ARM64_REG_W0)


def dfd_case(b, marker, read_error=False, partition_missing=False, dev_missing=False,
             debug_timeout_enabled=False, timeout_latched=False):
    """Run real expdb reader predicate, caller branch, wrappers and register RMW.

    Storage/hardware timing query boundaries are fixture inputs. MMIO is RAM.
    The first slice of the large caller is deliberately skipped; stop at its
    common continuation, before the rest of boot can run.
    """
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    u.mem_map(0x2000000,0x180000);u.mem_write(DELTA,b)
    for address,size in ((STACK,0x10000),(STOP,0x1000),(DATA,0x10000),
                         (0x1800e000,0x1000),(0x1c010000,0x1000)):
        u.mem_map(address,size)
    u.mem_write(DATA,struct.pack('<QQ',100,20))
    u.mem_write(0x1c01000c,struct.pack('<I',(1<<29) if timeout_latched else 0))
    u.reg_write(UC_ARM64_REG_X22,0x1c01000c)
    u.reg_write(UC_ARM64_REG_SP,STACK+0xf000)
    u.reg_write(UC_ARM64_REG_LR,STOP)
    writes=[];reads=[];exit_point=[]
    def ret(value=0):
        u.reg_write(UC_ARM64_REG_X0,value&((1<<64)-1))
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,addr,size,_):
        off=addr-DELTA
        if off==0x6aa50:exit_point.append('common boot continuation');uc.emu_stop()
        elif off in (0x6b4ac,0x6b4d8):
            exit_point.append('debug timeout fatal-handler branch');uc.emu_stop()
        elif off==0x7e480:ret(0 if partition_missing else DATA)
        elif off==0x37c8:ret(0 if dev_missing else DATA+0x100)
        elif off==0x91394:ret(0)
        elif off==0x3bc0:
            assert uc.reg_read(UC_ARM64_REG_W2)==0x34
            assert uc.reg_read(UC_ARM64_REG_X1)==((100+20)*4096-4096)
            buf=bytearray(0x34);struct.pack_into('<I',buf,0x24,marker)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X3),bytes(buf));reads.append('expdb final4KiB')
            ret(1 if read_error else 0)
        elif off in (0x8aea0,0x6b5c0,0x91d18):ret(0)
        elif off==0x9acd8:ret(int(debug_timeout_enabled))
        elif off==0x91190:raise AssertionError('stack guard failure')
        elif int.from_bytes(uc.mem_read(addr,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,addr+4)
    def memwrite(uc,access,addr,size,value,_):
        if 0x1800e000<=addr<0x1800f000:writes.append({'address':hex(addr),'value':hex(value)})
    u.hook_add(UC_HOOK_CODE,hook);u.hook_add(UC_HOOK_MEM_WRITE,memwrite)
    u.emu_start(DELTA+0x6a9d0,STOP,count=100000)
    assert len(exit_point)==1
    is_h4 = b[0x6a660:0x6a670] == bytes.fromhex('29688a522989a8721f01096be0079f1a')
    enabled = not (read_error or partition_missing or dev_missing) and (
        marker != 0x44495341 if is_h4 else marker == 0x454e454e)
    assert bool(writes)==enabled,(marker,is_h4,writes)
    fatal=enabled and debug_timeout_enabled and timeout_latched
    assert ('fatal' in exit_point[0])==fatal
    if enabled:
        assert writes[0]=={'address':'0x1800e0d8','value':'0x40000'}
        assert writes[1]=={'address':'0x1800e0f0','value':'0x1000000'}
    return {'marker_fixture':hex(marker),'expdb_read_error':read_error,
            'partition_missing':partition_missing,'storage_device_missing':dev_missing,
            'debug_timeout_enabled':debug_timeout_enabled,'timeout_latched':timeout_latched,
            'dfd_enabled':enabled,'storage_reads':reads,'register_writes_in_emulator_ram':writes,
            'continuation':exit_point[0],'pass':True}


def code_comparison(a,b):
    c=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);c.detail=True;c.skipdata=True
    ia={i.address-DELTA:i for i in c.disasm(a[0x100:0xb0000],DELTA+0x100)}
    ib={i.address-DELTA:i for i in c.disasm(b[0x100:0xb0000],DELTA+0x100)}
    nonaddress=[];addresses=[]
    def addr_value(table,off):
        i=table.get(off)
        if not i:return None
        if i.mnemonic=='adr':return i.operands[1].imm-DELTA
        if i.mnemonic=='adrp':
            reg=i.operands[0].reg
            for n in range(1,7):
                j=table.get(off+4*n)
                if j and j.mnemonic=='add' and len(j.operands)==3 and j.operands[1].type==ARM64_OP_REG and j.operands[1].reg==reg and j.operands[2].type==ARM64_OP_IMM:
                    return i.operands[1].imm+j.operands[2].imm-DELTA
        if i.mnemonic=='add' and len(i.operands)==3 and i.operands[2].type==ARM64_OP_IMM:
            for n in range(1,7):
                j=table.get(off-4*n)
                if j and j.mnemonic=='adrp' and j.operands[0].reg==i.operands[1].reg:
                    return j.operands[1].imm+i.operands[2].imm-DELTA
        return None
    def literal(raw,off):
        if off is None or not 0<=off<len(raw):return None
        v=raw[off:raw.find(b'\0',off)]
        return v.decode() if 0<len(v)<4096 and all(32<=x<127 or x in (10,13,9) for x in v) else None
    for off in range(0x100,0xb0000,4):
        if a[off:off+4]==b[off:off+4]:continue
        x,y=ia.get(off),ib.get(off)
        row={'file_offset':hex(off),'before':None if not x else x.mnemonic+' '+x.op_str,
             'after':None if not y else y.mnemonic+' '+y.op_str}
        if x and y and x.mnemonic in ('add','adr','adrp') and y.mnemonic in ('add','adr','adrp'):
            av,bv=addr_value(ia,off),addr_value(ib,off)
            s,t=literal(a,av),literal(b,bv)
            row.update(before_target=None if av is None else hex(av),after_target=None if bv is None else hex(bv),
                       same_resolved_string=s is not None and s==t)
            if s!=t:row.update(before_string=s,after_string=t)
            addresses.append(row)
        else:nonaddress.append(row)
    deltas=Counter()
    for off in range(0xd1800,0x103e00,4):
        x,y=struct.unpack_from('<I',a,off)[0],struct.unpack_from('<I',b,off)[0]
        if x!=y:deltas[hex((y-x)&0xffffffff)]+=1
    return {'byte_differences':sum(x!=y for x,y in zip(a,b)),
            'first_0x400_equal':a[:0x400]==b[:0x400],
            'bounded_instruction_range':['0x100','0xb0000 exclusive'],
            'non_address_instruction_changes':nonaddress,
            'address_instruction_changes':addresses,
            'address_changes_count':len(addresses),
            'resolved_identical_string_count':sum(r['same_resolved_string'] for r in addresses),
            'word_delta_counts_range_d1800_103e00':dict(deltas)}


def main():
    paths={'stock_H3_Global_3.304':ROOT/'Preloader/preloader_dash_3.304_global.bin',
           'stock_H3_China_3.306':ROOT/'Preloader/preloader_dash_3.306_china.bin',
           'stock_H4_China_4.403':ROOT/'Preloader/preloader_dash_4.403_china.bin',
           'supplied_h4_folder':ROOT/'preloader_hyperos4/preloader_dash.bin',
           'exact_H4_ROM_images':STOCK_PATH,
           'archive_root_current_ENG':ROOT_PATH}
    data={name:p.read_bytes() for name,p in paths.items()}
    stock=data['stock_H4_China_4.403']
    assert stock==data['supplied_h4_folder']==data['exact_H4_ROM_images']
    report={'status':'PASS_OFFLINE_BOUNDED_POLICY_AND_DFD_NOT_DEVICE_COMPATIBILITY',
            'inputs':{name:{'path':str(paths[name]),'bytes':len(raw),'sha256':sha(raw),
                            'GFH_load_address':hex(struct.unpack_from('<I',raw,0x1c)[0]),
                            'GFH_file_length':hex(struct.unpack_from('<I',raw,0x20)[0])}
                      for name,raw in data.items()},
            'exact_H4_ROM_matches_existing_H4_inputs':True}
    if LIVE_PATH.exists():
        raw=LIVE_PATH.read_bytes();eng=data['archive_root_current_ENG']
        assert raw[0x1000:0x1000+len(eng)]==eng
        report['existing_private_live_readback_identity']={
            'boot_lun_sha256':sha(raw),'GFH_start_offset':'0x1000',
            'archive_root_file_exactly_equals_live_window':True,
            'root_preloader_sha256':sha(eng),
            'note':'Only compares an existing readback; no device command or new collection'}
    report['comparisons_to_stock_H4']={}
    report['native_policy']={};report['native_dfd']={}
    for name in ('stock_H3_Global_3.304','stock_H3_China_3.306','stock_H4_China_4.403'):
        raw=data[name];assert struct.unpack_from('<I',raw,0x1c)[0]==DELTA
        if name!='stock_H4_China_4.403':report['comparisons_to_stock_H4'][name]=code_comparison(raw,stock)
        rows=[]
        for row in range(18):
            off=0xed110+row*56;ptrs=struct.unpack_from('<5Q',raw,off)
            names=[]
            for ptr in ptrs[1:]:
                if not ptr:names.append(None);continue
                o=ptr-DELTA;assert 0<=o<len(raw)
                names.append(raw[o:raw.find(b'\0',o)].decode())
            rows.append({'row':row,'names':names,'policy_bytes':list(raw[off+40:off+44])})
        cases=[]
        for sbc,state,se,de in itertools.product((0,1),(0,1,2,3,4,0xffffffff),(0,1),(0,1)):
            policy=policy_run(raw,sbc,state,0x8cad8,se,de)
            required=policy_run(raw,sbc,state,0x8cc18,se,de)
            assert required==(policy>>1)&1
            cases.append({'sbc_query_value':sbc,'device_state_query_value':state,
                          'sbc_query_error':se,'device_state_query_error':de,
                          'lk_policy':policy,'authentication_required':required})
        report['native_policy'][name]={'rows':rows,'fixture_cases':cases}
        dfd=[]
        for marker,debug,timeout in itertools.product((0,0x454e454e,0x44495341,0xffffffff),(False,True),(False,True)):
            dfd.append(dfd_case(raw,marker,debug_timeout_enabled=debug,timeout_latched=timeout))
        for fail in ('read_error','partition_missing','dev_missing'):
            for marker in (0,0x454e454e):dfd.append(dfd_case(raw,marker,**{fail:True}))
        report['native_dfd'][name]=dfd
    ns=list(report['native_policy'])
    for n in ns[1:]:
        assert report['native_policy'][n]==report['native_policy'][ns[0]]
    report['all_stock_policy_rows_and_cases_equal']=True
    report['native_policy_accessor_calls']=sum(2*len(v['fixture_cases']) for v in report['native_policy'].values())
    report['native_dfd_reader_and_caller_cases']=sum(map(len,report['native_dfd'].values()))
    report['limits']=['Bounded executable scan and resolved string-reference check, not full program equivalence',
        'SBC/device-state query values and errors are test fixtures, not measured current fuses/state',
        'DFD runs predicate/caller/wrappers/register RMW; storage/timing boundaries and MMIO modeled',
        'No cryptographic root-of-trust, rollback/efuse state or actual full container-loader execution',
        'No whole boot, ROM compatibility, or causal proof of the reported brick']
    (HERE/'evidence.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    c=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
    text=[]
    for lo,hi in ((0x6a578,0x6a678),(0x6a9d0,0x6aa54),(0x7f7c8,0x7f858),(0xa3f54,0xa4014)):
        text.append(f'Exact stock H4: raw{lo:x}..{hi:x}; linkedVA=raw+0x{DELTA:x}')
        text += [f'{i.address-DELTA:08x}: {i.bytes.hex()} {i.mnemonic} {i.op_str}'.rstrip()
                 for i in c.disasm(stock[lo:hi],DELTA+lo)]
    (HERE/'dfd_call_path.asm').write_text('\n'.join(text)+'\n',encoding='utf-8')
    print(json.dumps({k:report[k] for k in ('status','exact_H4_ROM_matches_existing_H4_inputs',
          'all_stock_policy_rows_and_cases_equal','native_policy_accessor_calls','native_dfd_reader_and_caller_cases')},indent=2))


if __name__=='__main__':main()

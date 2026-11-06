"""Native H4 regions, early fastboot gates, HWC, lock persistence, MTE."""
import itertools
import json
import struct
from build_port import *
from hwc_cases import flow_case, machine, cstr, B, DATA, STACK, STOP
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
sys.path.insert(0,str(ROOT/'experiments/lk_global_region_v1'))
from validate_region_lk import Replay
sys.path.insert(0,str(ROOT/'fresh_analysis/relock_ota_research'))
from replay_stock_lock import replay
sys.path.insert(0,str(ROOT/'experiments/lk_h4_booted_hybrid_mte_off_v1'))
from validate_mte_off import mte_case


def gate(image, kind, state, error=0, name='boot', secure=1, f14=0, f18=1):
    u,_=machine(image); sp=u.reg_read(UC_ARM64_REG_SP); result=[]
    u.reg_write(UC_ARM64_REG_X29,sp+0x50)
    u.mem_write(DATA+0x100,name.encode()+b'\0')
    u.reg_write(UC_ARM64_REG_X21,DATA+0x100)
    u.reg_write(UC_ARM64_REG_X19,DATA+0x100)
    if kind=='dispatch':
        entry=0xa090; yes=STOP+0x100; no=0xa0d4
        u.mem_write(sp+0x4c,struct.pack('<I',secure)); u.mem_write(sp+8,struct.pack('<I',state))
        u.mem_write(DATA+0x1010,struct.pack('<III',3,f14,f18))
        u.mem_write(DATA+0x1020,struct.pack('<Q',yes))
        u.reg_write(UC_ARM64_REG_X25,DATA+0x1000); u.reg_write(UC_ARM64_REG_X28,DATA+0x2000)
        u.reg_write(UC_ARM64_REG_X20,DATA+0x3000)
    elif kind=='flash':
        entry=0xce4c; yes=0xce8c; no=0xd1b0
        u.reg_write(UC_ARM64_REG_X0,sp+0x6c)
    elif kind=='erase':
        entry=0xd234; yes=0xd260; no=0xd588
        u.reg_write(UC_ARM64_REG_X0,sp+4)
    elif kind=='secure_region':
        entry=0x87318; yes=STOP; no=0x87630
        u.mem_write(B+0x1399f0,struct.pack('<Q',DATA+0x2000))
        u.mem_write(DATA+0x2000,struct.pack('<Q',DATA+0x2100))
        u.mem_write(DATA+0x2100,name.encode()+b'\0')
        u.mem_write(DATA+0x2200,('CN' if state==0 else 'GLOBAL').encode()+b'\0')
        u.reg_write(UC_ARM64_REG_X0,DATA+0x2200)
    elif kind=='flash_region':
        entry=0x682dc; yes=0x9cdc; no=0x9c9c
    else: raise ValueError(kind)
    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,address,size,_):
        off=address-B
        if address==yes or off==yes: result.append(True); uc.emu_stop()
        elif address==no or off==no: result.append(False); uc.emu_stop()
        elif off==0x8895c:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',state)); ret(error)
        elif off==0x4a384: ret(secure)
        elif off==0x4a780:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X1),struct.pack('<I',state)); ret(error)
        elif off in (0x7e11c,0x9bec):ret()
        elif int.from_bytes(uc.mem_read(address,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,address+4)
    u.hook_add(UC_HOOK_CODE,hook)
    u.emu_start(B+entry,STOP+0x800,count=10000)
    assert len(result)==1,(kind,result,hex(u.reg_read(UC_ARM64_REG_PC)))
    return dict(gate=kind,state_fixture=state,query_error=error,name=name,secure=secure,
                flag14=f14,flag18=f18,allowed=result[0])


def validate(kind):
    image=(HERE/NAMES[kind]).read_bytes(); h4=H4.read_bytes(); eng=ENG.read_bytes()
    hwc=[]
    for country,hw,suffix in itertools.product(('cn','CN','cN','Cn','global','GLOBAL','eea','tw','in','native','cn_extra',''),
                                               ('CN','GLOBAL'),('_a','_b')):
        hwc.append(flow_case(image,True,country,hardware=hw,suffix=suffix))
    for mode,hw in itertools.product(('missing','empty','short','io_error'),('CN','GLOBAL')):
        hwc.append(flow_case(image,True,'global',hardware=hw,mode=mode))
    for failure,country in itertools.product(('name','country','hwc'),('cn','global')):
        hwc.append(flow_case(image,True,country,fmt_fail=failure))
    region=[]
    for hw,sku in itertools.product((0,1),('CN','GLOBAL','EEA','TW','IN','NATIVE','')):
        r=Replay(image,hw=hw,sku=sku,block=True)
        assert r.run(0x68f50,False)=='continued'
        region.append(dict(gate='sku',hardware=hw,sku=sku,outcome='continued'))
    cn=image[512+0x1121d7:512+0x1122d7]; gl=image[512+0x1122d7:512+0x1123d7]
    for hw,key_label,key,ce,ke in itertools.product((0,1),('CN','GL','unknown'),(None,), (0,0x1234),(0,0x321)):
        key={'CN':cn,'GL':gl,'unknown':bytes(256)}[key_label]
        r=Replay(image,hw=hw,key=key,chain_error=ce,key_error=ke)
        r.u.reg_write(UC_ARM64_REG_X1,DATA+0x800); r.u.mem_write(DATA+0x800,b'md1rom\0')
        got=r.run(0x88734); expected=ce or ke or (0 if key_label in ('CN','GL') else 0xffffffff)
        assert got==expected,(hw,key_label,ce,ke,got,expected)
        assert r.calls[0]=='certificate_chain_verify'
        region.append(dict(gate='image_key',hardware=hw,key=key_label,chain_error=ce,key_error=ke,result=got))
    gates=[]
    for state,sec,f14,f18 in itertools.product((1,2,3,4),(0,1),(0,1),(0,1)):
        a=gate(h4,'dispatch',state,secure=sec,f14=f14,f18=f18)
        b=gate(image,'dispatch',state,secure=sec,f14=f14,f18=f18)
        assert a['allowed']==bool((not sec or f14) and (not f18 or state==3)) and b['allowed'],(a,b)
        gates.extend([dict(source='original',**a),dict(source='output',**b)])
    for cmd,state,err,name in itertools.product(('flash','erase'),(0,1),(0,7),('boot','init_boot','lk','boot_para')):
        result=gate(image,cmd,state,error=err,name=name)
        assert result['allowed']==(err==0)
        gates.append(result)
    for hw,sku in itertools.product((0,1),('CN','GLOBAL','EEA','TW','IN','')):
        a=gate(h4,'secure_region',hw,name=sku); b=gate(image,'secure_region',hw,name=sku)
        assert a['allowed']==(hw!=0 or sku.startswith('CN')) and b['allowed']
        gates.extend([dict(source='original',**a),dict(source='output',**b)])
    for hw,sb,err,region_name in itertools.product((0,1),(0,14),(0,7),(' cn',' CN',' gl',' GLOBAL',' unknown','')):
        result=gate(image,'flash_region',hw,error=err,name=region_name,secure=sb)
        assert result['allowed']; gates.append(result)
    lock=[]
    fixtures=[{}, {'verify_status':-1}, {'siglen':0}, {'siglen':10,'signature_fixture':'do nothing','verify_status':-1},
              {'siglen':257}, {'magic_matches':False}, {'magic_status':6,'sbc':14},
              {'magic_status':6,'sbc':0}, {'rpmb_size':0x1000000}, {'length_read_status':4}]
    for fixture in fixtures:
        a=replay(h4,'h4',fixture); b=replay(image,'h4',fixture); e=replay(eng,'eng',fixture)
        assert a==b
        assert (a['return'],a['state'])==(e['return'],e['state'])
        lock.append(dict(config=fixture,result=b,eng_state_agrees=True))
    for state in (0,1,2,3,4,5,0xffffffff):
        a=replay(h4,'h4',{},setter_state=state); b=replay(image,'h4',{},setter_state=state)
        assert a==b; lock.append(dict(requested_state=state,result=b))
    mte=[mte_case(image,bits,reserved,kind=='mte_off') for bits,reserved in
         itertools.product(tuple(range(8))+(0x405,0x407,0xf07,0xffffffff),(False,True))]
    report=dict(status='PASS_BOUNDED_CPU_NO_DEVICE_IO',variant=kind,sha256=sha(image),
        counts=dict(hwc=len(hwc),region=len(region),main_gates=len(gates),stock_lock=len(lock),mte=len(mte)),
        hwc=hwc,region=region,gates=gates,lock=lock,mte=mte,
        limits=['Two HWC slices, one early hardware copy; intervening unrelated bootargs not replayed',
                'Hardware/RPMB/token-verification results/formatter/storage and callbacks are fixtures',
                'Native writes journaled in emulator only; no real lock or flash',
                'Main gate tests stop before command execution; AEE release gates unchanged',
                'Native image-key chain verification mocked; original chain error propagation checked',
                'No stock H4 preloader, boot, banking, OTA or Integrity verification'])
    (HERE/(kind+'_behavior.json')).write_text(json.dumps(report,indent=2)+'\n')
    print(kind,report['status'],report['counts'])


if __name__=='__main__':
    for kind in NAMES:validate(kind)

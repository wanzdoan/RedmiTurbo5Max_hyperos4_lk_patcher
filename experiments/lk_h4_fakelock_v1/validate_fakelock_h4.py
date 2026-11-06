"""Bounded differential native H4 probes. No phone access or old report writes."""
import gc
import itertools
import json
import struct
from build_fakelock_h4 import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from validate_fakelock import machine, assert_abi, cstr, B, DATA, STACK, STOP
sys.path.insert(0, str(ROOT/'experiments/lk_h4_booted_hybrid_mte_off_v1'))
from validate_mte_off import unlock_case, mte_case


def ret(u, value=0):
    u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64)-1))
    u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))


def pac(u, address):
    if int.from_bytes(u.mem_read(address,4),'little') in (0xd503233f,0xd50323bf):
        u.reg_write(UC_ARM64_REG_PC,address+4)


def run(u, saved, entry, count=300000):
    try:u.emu_start(B+entry,STOP,count=count)
    except Exception as exc:raise RuntimeError(f'{exc}; PC={u.reg_read(UC_ARM64_REG_PC):x}') from exc
    assert_abi(u,saved)


def fastboot_case(image, query, values, fake, oem=False):
    u,saved=machine(image)
    nodes=[];text=DATA+0x1000
    for i,(name,value) in enumerate(values):
        node=DATA+0x100+i*32;np=text;u.mem_write(np,name.encode()+b'\0');text+=len(name)+1
        vp=text;u.mem_write(vp,value.encode()+b'\0');text+=len(value)+1
        nodes.append((node,np,vp))
    for i,(node,np,vp) in enumerate(nodes):
        u.mem_write(node,struct.pack('<QQQ',nodes[i+1][0] if i+1<len(nodes) else 0,np,vp))
    head=nodes[0][0] if nodes else 0
    u.mem_write(B+0x13d108,struct.pack('<Q',head))
    u.mem_write(DATA+0x8000,query.encode()+b'\0');u.reg_write(UC_ARM64_REG_X0,DATA+0x8000)
    before=bytes(u.mem_read(DATA,0x7000));info=[];okay=[];fail=[]
    def hook(uc,address,size,_):
        off=address-B
        if off==0x7f6fc:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2))
            if fmt=='\t%s: %s':value=fmt%(cstr(uc,uc.reg_read(UC_ARM64_REG_X3)),cstr(uc,uc.reg_read(UC_ARM64_REG_X4)))
            elif fmt in ('lks = 0','lks = 1'):value=fmt
            else:raise AssertionError(fmt)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),value.encode()[:uc.reg_read(UC_ARM64_REG_W1)-1]+b'\0');ret(uc,len(value))
        elif off in (0x9bec,0x9cdc,0x9c9c):
            (info if off==0x9bec else okay if off==0x9cdc else fail).append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(uc)
        elif off==0x7e11c:ret(uc)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook);run(u,saved,0xc8e0 if oem else 0xb068)
    assert before==bytes(u.mem_read(DATA,0x7000)) and int.from_bytes(u.mem_read(B+0x13d108,8),'little')==head
    expected=dict(values)
    if fake:
        for name,val in [('unlocked','no'),('secure','yes')]:
            if name in expected:expected[name]=val
    if oem:want_info=['lks = 1' if fake else 'lks = 0'];want_ok=['']
    elif query=='all':want_info=['\t%s: %s'%(name,expected[name]) for name,value in values];want_ok=['Done!!']
    else:want_info=[];want_ok=[expected.get(query,'')]
    assert (info,okay,fail)==(want_info,want_ok,[]),(query,info,okay,fail)
    internal={}
    for name in ('unlocked','secure','product'):
        u.mem_write(DATA+0x8000,name.encode()+b'\0');u.reg_write(UC_ARM64_REG_X0,DATA+0x8000)
        u.reg_write(UC_ARM64_REG_LR,STOP);run(u,saved,0x9ab8)
        internal[name]=cstr(u,u.reg_read(UC_ARM64_REG_X0))
        assert internal[name]==dict(values).get(name,'')
    return dict(query=query,oem=oem,fake=fake,info=info,okay=okay,internal_values=internal,cache_and_abi_preserved=True)


def android_case(image,state,first=0,second=0):
    u,saved=machine(image);u.mem_write(B+0x1219b0,struct.pack('<I',state));calls=[]
    def hook(uc,address,size,_):
        if address==B+0x19078:
            calls.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(uc,first if len(calls)==1 else second)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook);run(u,saved,0x69574)
    assert calls==['androidboot.verifiedbootstate=green']+(['androidboot.flash.locked=1'] if not first else [])
    assert u.reg_read(UC_ARM64_REG_W0)==((first or second)&0xffffffff)
    assert bytes(u.mem_read(B+0x1219b0,4))==struct.pack('<I',state)
    return dict(real_boot_state=state,append_statuses=[first,second],args=calls,real_state_and_errors_preserved=True)


def avb_report_case(image,unlocked,rc):
    u,_=machine(image);sp=STACK+0xf000;u.reg_write(UC_ARM64_REG_X29,sp);u.reg_write(UC_ARM64_REG_X19,DATA)
    u.mem_write(sp-0x44,bytes([unlocked]));calls=[];outcome=[]
    def hook(uc,address,size,_):
        off=address-B
        if off==0xbcb34:
            calls.append([cstr(uc,uc.reg_read(UC_ARM64_REG_X1)),cstr(uc,uc.reg_read(UC_ARM64_REG_X2))]);ret(uc,rc)
        elif off in (0xbc7ec,0xbcb08):outcome.append(off);uc.emu_stop()
    u.hook_add(UC_HOOK_CODE,hook);u.emu_start(B+0xbc7bc,STOP,count=100)
    assert calls==[['androidboot.vbmeta.device_state','locked']]
    assert outcome==[0xbc7ec if rc else 0xbcb08]
    assert bytes(u.mem_read(sp-0x44,1))==bytes([unlocked])
    return dict(internal_unlocked_flag=unlocked,append_return=rc,public=calls,error_branch_preserved=True)


def handoff_case(image,fake,state,control=None,key_error=0,smc_error=0):
    u,saved=machine(image);key=bytes(range(256));versions=[170000,202610,20261001,20261001];digest=bytes(range(32))
    u.mem_write(B+0x20d990,struct.pack('<IIII',*versions));u.mem_write(B+0x20d9a0,digest)
    u.mem_write(B+0x20d9c0,struct.pack('<I',state));before=bytes(u.mem_read(B+0x20d990,0x34))
    packets=[];locals_seen=[];logs=[]
    def hook(uc,address,size,_):
        off=address-B
        if off==0x88914:
            assert uc.reg_read(UC_ARM64_REG_W1)==256
            if not key_error:uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),key)
            ret(uc,key_error)
        elif off==0x1c6e8:ret(uc,0)  # Native software SHA, not hardware crypto.
        elif off==0x89aec and control is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),struct.pack('<I',control));ret(uc)
        elif off==0x7e11c:logs.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(uc)
        elif off==0x1ebf4:
            values=[uc.reg_read(reg) for reg in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5,UC_ARM64_REG_X6,UC_ARM64_REG_X7)]
            packets.append(values);sp=uc.reg_read(UC_ARM64_REG_SP);locals_seen.append(int.from_bytes(uc.mem_read(sp+0x2c,4),'little'))
            output=int.from_bytes(uc.mem_read(sp,8),'little');uc.mem_write(output,struct.pack('<QQQQ',smc_error,0,0,0));ret(uc)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook);run(u,saved,0xc4ce4)
    locked=int((3 if control is None else control)!=3)
    kh=list(struct.unpack('<QQQQ',bytes.fromhex(sha(key if not key_error else bytes(256)))) ) if fake or locked else [0]*4
    expected=[[0xc2000120,*kh,0,0,0],
              [0xc2000120,1 if fake else locked,0 if fake else state,*versions[:2],0,0,0],
              [0xc2000120,*struct.unpack('<QQQQ',digest),0,0,0],
              [0xc2000120,*versions[2:],0,0,0,0,0],
              [0xc2000120,0,0,0,0,0,0,0]]
    assert packets==expected and locals_seen==[locked]*5
    assert bytes(u.mem_read(B+0x20d990,0x34))==before
    assert bool(key_error)==any('fail to get public key info' in line for line in logs)
    return dict(fake=fake,real_boot_state=state,getter_control=control,actual_getter_used=control is None,
                key_error=key_error,smc_response_fixture=smc_error,packets=packets,local_and_global_preserved=True,
                note='H4 sender does not branch on secure-monitor return; unchanged behavior')


def seccfg_case(image,fake,state,status):
    u,saved=machine(image);responses=[];actual=[]
    def hook(uc,address,size,_):
        off=address-B
        if off==0xb3c28:
            ptr=uc.reg_read(UC_ARM64_REG_X0);actual.append(ptr);uc.mem_write(ptr,struct.pack('<I',state));ret(uc,status)
        elif off==0x7f6fc:
            fmt=cstr(uc,uc.reg_read(UC_ARM64_REG_X2));value=fmt.replace('%u',str(uc.reg_read(UC_ARM64_REG_W3)))
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0),value.encode()[:uc.reg_read(UC_ARM64_REG_W1)-1]+b'\0');ret(uc,len(value))
        elif off in (0x9bec,0x9c9c,0x9cdc):responses.append([off,cstr(uc,uc.reg_read(UC_ARM64_REG_X0))]);ret(uc)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook);run(u,saved,0xc96c)
    assert [s for off,s in responses if off==0x9bec]==[f'seccfg_state is {1 if fake else state}','mtkrpmb_state is 4.']
    assert len([s for off,s in responses if off==0x9c9c])==int(status!=0)
    assert len(actual)==1 and int.from_bytes(u.mem_read(actual[0],4),'little')==state
    return dict(fake=fake,real_state=state,getter_error=status,responses=responses,read_errors_and_actual_word_preserved=True)


def warning_case(image,fake,state,mode):
    u,saved=machine(image);u.mem_write(B+0x1219b0,struct.pack('<I',state));u.mem_write(DATA,b'boot\0');u.reg_write(UC_ARM64_REG_X0,DATA)
    ui=[];fatal=[]
    def hook(uc,address,size,_):
        off=address-B
        if off==0x10bc0:ret(uc,mode)
        elif off==0x869a0:ui.append(uc.reg_read(UC_ARM64_REG_W0));ret(uc)
        elif off in (0x1502c,0x7cea0,0x9d18):ret(uc)
        elif off==0x69678:fatal.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(uc)
        else:pac(uc,address)
    u.hook_add(UC_HOOK_CODE,hook);run(u,saved,0x695d0)
    expected=([2] if mode!=1 else []) if state in (1,3) else ([1] if state==2 and mode!=1 and not fake else [])
    assert ui==expected and fatal==(['boot'] if state in (1,3) else [])
    assert int.from_bytes(u.mem_read(B+0x1219b0,4),'little')==state
    return dict(fake=fake,state=state,boot_mode=mode,ui=ui,fatal_called=bool(fatal),real_state_unchanged=True)


def cryptographic_checks(image):
    rows=members(image);certs=[]
    for i,row in enumerate(rows):
        if row['name'] not in ('cert1','cert2'):continue
        raw=image[row['offset']+512:row['offset']+512+row['size']];der=raw[180:] if i==2 else raw
        end=8+int.from_bytes(der[6:8],'big')
        load_der_public_key(der[181:475]).verify(der[-256:],der[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
        if row['name']=='cert2':
            component=rows[i-2];start=component['offset']
            for oid,data in [('060760867693160204',image[start:start+512]),('060760867693160201',image[start+512:component['end']])]:
                at=der.index(bytes.fromhex(oid))+12;match=der[at:at+32].hex()==sha(data)
                assert match == (i!=2 or oid.endswith('0204'))
        certs.append(dict(member=i,retained_der_signature_valid=True))
    c=rows[2];record=image[c['offset']:c['offset']+512+c['size']]
    assert record[512:692]==prefix(image[:512],image[512:rows[0]['end']])
    sys.path.insert(0,str(ROOT/'fresh_analysis/preloader'))
    from emulate_bl2_parser import run as parser
    parsers=[]
    assert image[rows[3]['offset']+512:rows[3]['offset']+512+rows[3]['size']]==(ROOT/'fresh_analysis/container_chain/h3_original_03_bl2_ext.bin').read_bytes()
    for mask in (0x60,0x1f4e2,0x1f400):
        got=parser(3,'h4-fakelock-v1',mask,certificate_record=record)
        assert got['returned'] and got['error'] is None and got['status']=='0x0'
        if mask!=0x1f400:assert got['payload_hash']==sha(image[512:rows[0]['end']]) and got['header_hash']==sha(image[:512])
        parsers.append(got)
    sys.path.insert(0,str(ROOT/'fresh_analysis/h4_preloader_lk_acceptance_20261106'))
    from native_cert import run as preloader_cert
    from native_locator import locate
    a,b,c=rows[3:6];c1=image[b['offset']+512:b['offset']+512+b['size']]
    result=preloader_cert(image[c['offset']:c['offset']+512+c['size']],c1[496:790],component_header=image[a['offset']:a['offset']+512],component_payload=image[a['offset']+512:a['offset']+512+a['size']])
    assert result['return_status']==0 and all(row['match'] for row in result['content_checks'])
    locator=locate(image);assert locator['status']=='0x0' and int(locator['member_offset'],16)==a['offset']
    return dict(der=certs,release_bl2_parsers=parsers,stock_h4_preloader_cert_boundary=result,stock_h4_preloader_locator=locator)


def main():
    source,unlocked,h3=BASE.read_bytes(),UNLOCKED.read_bytes(),H3_REFERENCE.read_bytes();image=(HERE/NAME).read_bytes()
    assert image==build(source,unlocked,h3)[0]==build(source,unlocked,h3)[0]
    for arg in range(3):
        inputs=[source,unlocked,h3];bad=bytearray(inputs[arg]);bad[12345]^=1;inputs[arg]=bytes(bad)
        try:build(*inputs)
        except ValueError:pass
        else:raise AssertionError('Wrong input accepted')
    values=[('product','dash'),('unlocked','yes'),('secure','no'),('current-slot','a'),('unlocked-extra','keep'),('secure-extra','keep')]
    public=[]
    for fake,blob in [(False,unlocked),(True,image)]:
        for entries,query in itertools.product((values,list(reversed(values)),[]),('all','unlocked','secure','product','missing','unlocked-extra','secure-extra')):
            public.append(fastboot_case(blob,query,entries,fake))
        public.append(fastboot_case(blob,'',values,fake,oem=True))
    android=[android_case(image,state,a,b) for state,a,b in itertools.product((0,1,2,3,0xffffffff),(0,-8),(0,-9))]
    avb=[avb_report_case(image,unlocked_flag,rc) for unlocked_flag,rc in itertools.product((0,1,2),(0,1))]
    deep=[];seccfg=[];warnings=[]
    for fake,blob in [(False,unlocked),(True,image)]:
        for state,control,keyerr,smcerr in itertools.product((0,1,2,3,0xffffffff),(None,3,4),(0,9),(0,7)):
            deep.append(handoff_case(blob,fake,state,control,keyerr,smcerr))
        for state,rc in itertools.product((0,1,3,4,0xffffffff),(0,7)):seccfg.append(seccfg_case(blob,fake,state,rc))
        for state,mode in itertools.product((0,1,2,3),(0,1,99)):warnings.append(warning_case(blob,fake,state,mode))
    internal=[unlock_case(image,entry,initial) for entry,initial in itertools.product((0x89aec,0x89020,0x88a10),(0,1,2,3,4,0xffffffff))]
    internal += [unlock_case(image,0x68c94,0,boot,control) for boot,control in itertools.product((0,1,2,0xffffffff),(None,0,1,2,3,4,0xffffffff))]
    mte=[mte_case(image,bits,reserved,True) for bits,reserved in itertools.product(tuple(range(8))+(0x405,0x407,0xf07,0xffffffff),(False,True))]
    sys.path.insert(0,str(ROOT/'experiments/lk_h4_eng_policy_auto_hwc_v1'))
    from hwc_cases import flow_case
    from validate_behavior import gate
    from validate_port import execute
    hwc=[flow_case(image,True,country,hw,suffix=slot) for country,hw,slot in itertools.product(('cn','CN','global','eea','tw','','unknown'),('CN','GLOBAL'),('_a','_b'))]
    hwc += [flow_case(image,True,country,hw,mode=mode) for country,hw,mode in itertools.product(('cn','global'),('CN','GLOBAL'),('missing','empty','short','io_error'))]
    hwc += [flow_case(image,True,country,hw,fmt_fail=stage) for country,hw,stage in itertools.product(('cn','global'),('CN','GLOBAL'),('name','country','hwc'))]
    gates=[]
    for cmd,state,error,name in itertools.product(('flash','erase'),(0,1),(0,7),('boot','init_boot','lk','boot_para')):
        got=gate(image,cmd,state,error=error,name=name);assert got['allowed']==(error==0);gates.append(got)
    key_cases=[];stock=(ROOT/'lk_hyperos4_original/lk.img').read_bytes();eng=(ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img').read_bytes()
    for label,key in [('CN',stock[512+0x138658:512+0x138858]),('GL',stock[512+0x138858:512+0x138a58]),('ENG',eng[512+0x130728:512+0x130828])]:
        for altered in (False,True):
            data=bytearray(key)
            if altered:data[len(data)//2]^=1
            avbkey=struct.pack('>II',len(key)*8,0)+data+bytes(len(key))
            rc,memory,_=execute(image,0xc5978,{DATA+0x800:bytes(avbkey),UC_ARM64_REG_X1:DATA+0x800,UC_ARM64_REG_X2:len(avbkey),UC_ARM64_REG_X5:DATA+0x700})
            assert rc==0 and memory[0x700]==int(not altered)
            key_cases.append(dict(label=label,altered=altered,trusted=memory[0x700]))
    crypto=cryptographic_checks(image)
    # Full native slot and loader on user's clean China H4 ROM. Real force3
    # getter executes; storage/GUID/rollback/secure-world still use fixtures.
    sys.path.insert(0,str(ROOT/'fresh_analysis/h4_recovery_v3_20261106'))
    from slot_replay import Replay
    replay=Replay(image,'h4',state=3,flags=1,execute_force_getter=True);slot=replay.run()
    assert slot['returned'] and slot['error'] is None and slot['result']==0
    assert 'androidboot.vbmeta.device_state=locked' in slot['cmdline']
    loader=replay.load();assert loader['reached_copy_sink'] and loader['error'] is None
    handoff=replay.handoff();assert handoff['packets'][1][1:3]==[1,0] and any(handoff['packets'][0][1:5])
    del replay;gc.collect()
    # Compare representative semantics directly with latest H3 fakelock output.
    sys.path.insert(0,str(ROOT/'experiments/lk_h3_eng_fakelock_v2'))
    from validate_deep_fakelock import handoff_case as h3_handoff
    h3_semantics=[h3_handoff(h3,True,state) for state in (0,2,3,0xffffffff)]
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_ARM)
    assembly=[dict(offset=hex(ins.address),instruction=ins.mnemonic+' '+ins.op_str) for off,old,new,why in operations(source,unlocked)
              for ins in decoder.disasm(image[512+off:512+off+len(new)],off) if off!=0xc8e0 or ins.address<FLASH_ARG]
    groups=dict(public=public,android=android,avb_report=avb,handoff=deep,seccfg=seccfg,warnings=warnings,internal_unlock=internal,mte=mte,hwc=hwc,fastboot_gates=gates,keys=key_cases,h3_semantic_reference=h3_semantics)
    counts={name:len(cases) for name,cases in groups.items()}
    report=dict(status='PASS_OFFLINE_NATIVE_NOT_DEVICE_BOOT',artifact=NAME,output_sha256=sha(image),
                input_sha256=BASE_SHA,h3_reference_sha256=H3_SHA,deterministic_rebuild=True,all_three_wrong_inputs_rejected=True,
                main_cpu_counts=counts,total_main_cpu_cases=sum(counts.values()),cases=groups,cryptographic_checks=crypto,
                native_slot=slot,native_loader=loader,native_root_handoff=handoff,patch_disassembly=assembly,
                limits=['No device flash/boot/root/banking/Integrity/OTA/Keystore test',
                        'Stored-state controls, OEM-key extraction, hardware, formats/IO/heap, rollback and secure-monitor replies are fixtures',
                        'Actual force3 getter, cache lookup, packet SHA, AVB/RSA/hash comparison and mapped code execute natively',
                        'Native slot flags1 allow unlocked-mode verification errors; clean ROM returns0; no claim modified init_boot passes on hardware',
                        'H4 commercial AEE retained; not a full ENG port; command probes stop before actual flash/erase',
                        'Retained DER signatures authenticate original signed structures; patched main is authorized by existing parser prefix behavior, not newly signed'])
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    print(report['status'],sha(image),counts)


if __name__=='__main__':main()

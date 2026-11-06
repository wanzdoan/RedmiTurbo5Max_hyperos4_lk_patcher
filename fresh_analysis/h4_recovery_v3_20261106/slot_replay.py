"""Whole native avb_slot_verify on host ROM images, with explicit IO backends.

No phone I/O. Storage/rollback/lock/GUID/persistence are fixtures; SHA engine,
allocation and bulk copy use host backends. Firmware files are never modified.
"""
from pathlib import Path
import hashlib
import json
import struct
import re
import sys
import time
from unicorn import Uc,UC_ARCH_ARM64,UC_MODE_ARM,UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key

HERE=Path(__file__).resolve().parent;ROOT=HERE.parents[1]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,sha
ROM=Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images')
PART_SIZES={m[0]:int(m[1],16) for m in re.findall(r'partition_name:\s*(\S+).*?partition_size:\s*(0x[0-9a-fA-F]+)',
            (ROM/'MT6991_Android_scatter.txt').read_text(),re.S)}
B=0xffff000090700000;DATA=0x3000000;STACK=0x3100000;STOP=0x3200000;HEAP=0x4000000
PROFILES={
 'h4':dict(slot=0xbe740,ops=0x1385e0,read=0xc57f8,size=0xc5198,guid=0xc5c0c,
           rollback=0xc5b18,write_rollback=0xc5b98,persist_read=0xc51f0,persist_write=0xc54a4,
           getter=0x89aec,log=0x7e11c,liblog=0xc0ee8,alloc=0xc0f94,
           sha256=(0xc6bd8,0xc6c10,0xc6c54),memcpy=0x7eb20,memset=0x7ee50,hw=0x4a780,
           loader=0x68d1c,append=0x19078,suffix=0x1f790,malloc=0x7d514,config=0x21d30,config_ok=0x21e60,sbc=0x4a384,
           copy_sink=0x10a40,free=0x7d664,sender=0xc4ce4,key_extract=0x88914,crypto_mode=0x1c6e8,smc=0x1ebf4),
 'eng':dict(slot=0xbe700,ops=0x1306b0,read=0xc5708,size=0xc50a8,guid=0xc5a54,
           rollback=0xc5960,write_rollback=0xc59e0,persist_read=0xc5100,persist_write=0xc53b4,
           getter=0x896e4,log=0x7ea84,liblog=0xc0ea8,alloc=0xc0f54,
           sha256=(0xc6a38,0xc6a70,0xc6ab4),memcpy=0x7f488,memset=0x7f7b8,hw=0x4b510,
           loader=0x69824,append=0x18ab8,suffix=0x1f2b0,malloc=0x7de7c,config=0x21c80,config_ok=0x21db0,sbc=0x4b114,
           copy_sink=0x10588,free=0x7dfcc,sender=0xc4de0,key_extract=0x88578,crypto_mode=0x1c0e0,smc=0x1e714)}


class Replay:
    def __init__(self,image,kind,state=4,flags=0,mode=0,fault=None,rollback_value=0,execute_force_getter=False):
        self.image=image;self.cfg=c=PROFILES[kind];self.u=u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
        for addr,size in ((B,0x500000),(DATA,0x10000),(STACK,0x20000),(STOP,0x1000),(HEAP,0xa000000)):
            u.mem_map(addr,size)
        main=bytearray(image[512:512+members(image)[0]['size']])
        # PAC is modeled as no-op in emulator RAM only; bytes on disk stay intact.
        for off in range(0,len(main)-3,4):
            if int.from_bytes(main[off:off+4],'little') in (0xd503233f,0xd50323bf):main[off:off+4]=bytes.fromhex('1f2003d5')
        u.mem_write(B,bytes(main));self.heap=HEAP;self.allocations=[];self.events=[];self.logs=[];self.cache={};self.hashes={}
        self.state=state;self.flags=flags;self.mode=mode;self.cmdlines=[];self.copy_result=None;self.packets=[]
        self.fault=fault;self.rollback_value=rollback_value
        row=members(image)[2];raw=image[row['offset']+512:row['offset']+512+row['size']]
        der=raw[-982:];self.oem_modulus=load_der_public_key(der[181:475]).public_numbers().n.to_bytes(256,'big')
        for i,name in enumerate(('boot','vendor_boot','dtbo','init_boot')):
            ptr=DATA+0x100+i*0x40;u.mem_write(ptr,name.encode()+b'\0');u.mem_write(DATA+i*8,struct.pack('<Q',ptr))
        u.mem_write(DATA+0x300,b'_a\0')
        self.entries={}
        for key in ('read','size','guid','rollback','write_rollback','persist_read','persist_write','getter','log','liblog','alloc','memcpy','memset','hw'):
            if key=='getter' and execute_force_getter:continue
            self.entries.setdefault(c[key],[]).append(key)
        for i,off in enumerate(c['sha256']):self.entries.setdefault(off,[]).append('sha'+str(i))
        for key in ('append','suffix','malloc','config','config_ok','sbc','free','copy_sink','key_extract','crypto_mode','smc'):
            self.entries.setdefault(c[key],[]).append(key)
        for off in self.entries:u.hook_add(UC_HOOK_CODE,self.hook,begin=B+off,end=B+off)

    def cstr(self,p):
        out=bytearray()
        for i in range(2048):
            char=bytes(self.u.mem_read(p+i,1))
            if char==b'\0':return out.decode('utf-8',errors='replace')
            out+=char
        raise ValueError('unterminated string')

    def ret(self,value=0):
        self.u.reg_write(UC_ARM64_REG_X0,value&((1<<64)-1))
        self.u.reg_write(UC_ARM64_REG_PC,self.u.reg_read(UC_ARM64_REG_LR))

    def data(self,name):
        plain=name[:-2] if name.endswith(('_a','_b')) else name
        path=ROM/(plain+'.img')
        if not path.exists():return None
        if plain not in self.cache:
            raw=path.read_bytes();length=PART_SIZES.get(name,PART_SIZES.get(plain+'_a',len(raw)))
            assert length<=0x20000000,('unexpected large partition',name,length)
            self.cache[plain]=raw.ljust(length,b'\0') # Unwritten tail is an explicit zero-fill fixture.
            if self.fault=='init_boot_hash' and plain=='init_boot':
                changed=bytearray(self.cache[plain]);changed[0x2000]^=1;self.cache[plain]=bytes(changed)
            elif self.fault=='vbmeta_signature' and plain=='vbmeta':
                changed=bytearray(self.cache[plain]);sig=256+struct.unpack_from('>Q',changed,48)[0]
                changed[sig]^=1;self.cache[plain]=bytes(changed)
        return self.cache[plain]

    def hook(self,u,address,size,_):
        off=address-B;keys=self.entries[off]
        x=[u.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5)]
        key=keys[0]
        if key in ('alloc','malloc'):
            ptr=self.heap;self.heap+=(x[0]+63)&~63
            self.allocations.append(x[0]);assert self.heap<=HEAP+0x8c00000,('AVB heap exhausted',self.heap-HEAP,x[0])
            self.ret(ptr)
        elif key=='memcpy':
            if x[2]:u.mem_write(x[0],bytes(u.mem_read(x[1],x[2])))
            self.ret(x[0])
        elif key=='memset':
            if x[2]:u.mem_write(x[0],bytes([x[1]&255])*x[2])
            self.ret(x[0])
        elif key in ('log','liblog'):
            for ptr in x:
                if B<=ptr<B+len(self.image) or HEAP<=ptr<self.heap:
                    try:text=self.cstr(ptr)
                    except Exception:continue
                    if text and len(text)<1024:self.logs.append(text)
            self.ret()
        elif key=='sha0':self.hashes[x[0]]=hashlib.sha256();self.ret()
        elif key=='sha1':self.hashes[x[0]].update(bytes(u.mem_read(x[1],x[2])));self.ret()
        elif key=='sha2':
            digest=self.hashes.pop(x[0]).digest();u.mem_write(x[0],digest);self.ret(x[0])
        elif key=='getter':u.mem_write(x[0],struct.pack('<I',self.state));self.ret()
        elif key=='hw':u.mem_write(x[1],bytes(4));self.ret() # CN hardware region fixture
        elif key=='sbc':self.ret(14)
        elif key in ('config','config_ok','free'):self.ret()
        elif key=='suffix':self.ret(DATA+0x300)
        elif key=='append':self.cmdlines.append(self.cstr(x[0]));self.ret()
        elif key=='copy_sink':
            self.copy_result=dict(boot=x[0],vendor_boot=x[1],init_boot=x[2])
            u.emu_stop() # Stop before global image handoff; retain exact selected buffers.
        elif key=='key_extract':
            assert x[1]==256;u.mem_write(x[0],self.oem_modulus);self.ret()
        elif key=='crypto_mode':self.ret(0) # Native software SHA branch for root packet fixture.
        elif key=='smc':
            assert x[0]==0xc2000120
            self.packets.append([u.reg_read(r) for r in (UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,
                UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5,UC_ARM64_REG_X6,UC_ARM64_REG_X7)])
            out=int.from_bytes(u.mem_read(u.reg_read(UC_ARM64_REG_SP),8),'little')
            u.mem_write(out,bytes(32));self.ret() # Capture boundary only; no real secure-world execution.
        elif key=='rollback':
            self.events.append(dict(op=key,index=x[1],value=self.rollback_value));u.mem_write(x[2],struct.pack('<Q',self.rollback_value));self.ret()
        elif key=='write_rollback':raise AssertionError('slot verifier attempted rollback write')
        elif key=='persist_read':self.events.append(dict(op=key,name=self.cstr(x[1])));self.ret(5)
        elif key=='persist_write':
            self.events.append(dict(op=key,name=self.cstr(x[1]),bytes=x[2],virtual_only=True));self.ret()
        elif key=='guid':
            guid=b'00000000-0000-0000-0000-000000000001\0';assert x[3]>=len(guid)
            u.mem_write(x[2],guid);self.ret()
        elif key=='size':
            name=self.cstr(x[1]);blob=self.data(name);self.events.append(dict(op=key,name=name,size=len(blob) if blob else None))
            if self.fault=='init_boot_io' and name.startswith('init_boot'):self.ret(2)
            elif blob is None:self.ret(3)
            else:u.mem_write(x[2],struct.pack('<Q',len(blob)));self.ret()
        elif key=='read':
            name=self.cstr(x[1]);blob=self.data(name);position=x[2]-(1<<64) if x[2]>>63 else x[2]
            if blob is not None and position<0:position+=len(blob)
            self.events.append(dict(op=key,name=name,offset=position,bytes=x[3]))
            if self.fault=='init_boot_io' and name.startswith('init_boot'):self.ret(2)
            elif blob is None:self.ret(3)
            elif position<0 or position+x[3]>len(blob):self.ret(4)
            else:
                u.mem_write(x[4],blob[position:position+x[3]]);u.mem_write(x[5],struct.pack('<Q',x[3]));self.ret()
        else:raise ValueError(key)

    def run(self):
        u=self.u;u.reg_write(UC_ARM64_REG_SP,STACK+0x1f000);u.reg_write(UC_ARM64_REG_LR,STOP)
        for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4,UC_ARM64_REG_X5),
                           (B+self.cfg['ops'],DATA,DATA+0x300,self.flags,self.mode,DATA+0x400)):u.reg_write(reg,val)
        start=time.time();error=None
        try:u.emu_start(B+self.cfg['slot'],STOP,count=100000000)
        except Exception as exc:error=repr(exc)
        result=dict(returned=u.reg_read(UC_ARM64_REG_PC)==STOP,pc=hex(u.reg_read(UC_ARM64_REG_PC)),
                    result=u.reg_read(UC_ARM64_REG_W0),error=error,seconds=round(time.time()-start,3),
                    image_sha256=sha(self.image),state_fixture=self.state,flags=self.flags,error_mode=self.mode,
                    allocated_bytes=self.heap-HEAP,events=self.events,logs=self.logs[-100:])
        ptr=int.from_bytes(u.mem_read(DATA+0x400,8),'little')
        if result['returned'] and ptr:
            self.slotptr=ptr
            meta=bytes(u.mem_read(ptr,0x138));result['cmdline']=self.cstr(struct.unpack_from('<Q',meta,0x28)[0])
            result['loaded_partitions']=[]
            table,count=struct.unpack_from('<QQ',meta,0x18)
            for i in range(count):
                row=bytes(u.mem_read(table+i*32,32));name,data,length=struct.unpack_from('<QQQ',row)
                result['loaded_partitions'].append(dict(name=self.cstr(name),bytes=length))
        return result

    def load(self):
        u=self.u;u.reg_write(UC_ARM64_REG_SP,STACK+0x1f000);u.reg_write(UC_ARM64_REG_LR,STOP)
        for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2),
                           (B+self.cfg['ops'],0,self.slotptr)):u.reg_write(reg,val)
        error=None
        try:u.emu_start(B+self.cfg['loader'],STOP,count=10000000)
        except Exception as exc:error=repr(exc)
        result=dict(reached_copy_sink=self.copy_result is not None,returned=u.reg_read(UC_ARM64_REG_PC)==STOP,
                    pc=hex(u.reg_read(UC_ARM64_REG_PC)),lr=hex(u.reg_read(UC_ARM64_REG_LR)),result=u.reg_read(UC_ARM64_REG_W0),
                    error=error,cmdlines=self.cmdlines,logs=self.logs[-100:])
        if self.copy_result:
            result['selected_images']={name:dict(pointer=hex(ptr),magic=bytes(u.mem_read(ptr,8)).hex()) for name,ptr in self.copy_result.items()}
        return result

    def handoff(self):
        u=self.u;u.reg_write(UC_ARM64_REG_SP,STACK+0x1f000);u.reg_write(UC_ARM64_REG_LR,STOP)
        # Direct slot invocation bypasses the outer boot-state setter. Supply its
        # expected clean-slot value explicitly; this is not a measured boot state.
        state_offset=0x20d9c0 if self.cfg is PROFILES['h4'] else 0x20d900
        u.mem_write(B+state_offset,struct.pack('<I',2 if self.state==3 else 0))
        self.packets.clear()
        u.emu_start(B+self.cfg['sender'],STOP,count=1000000)
        assert u.reg_read(UC_ARM64_REG_PC)==STOP and len(self.packets)==5
        return dict(packets=self.packets,oem_modulus_sha256=sha(self.oem_modulus),
                    scope='Native packet assembly/software SHA; actual OEM key cache and SMC result are fixtures')


if __name__=='__main__':
    image=(ROOT/'experiments/lk_h4_eng_policy_recovery_fix_v2/lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img').read_bytes()
    replay=Replay(image,'h4');report=replay.run()
    if report['returned'] and report['result']==0:
        report['loader']=replay.load();report['root_handoff']=replay.handoff()
    (HERE/'h4_slot_probe.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps(report,indent=2))

"""Run H4 member locator with host-backed read-only partition data."""
import struct
from native_cert import *


def locate(image,name='bl2_ext'):
    fw=FW.read_bytes();assert sha(fw)==FW_SHA
    u=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    for addr,size in ((0x2000000,0x180000),(INPUT,0x10000),(STACK,0x10000),(STOP,0x1000)):
        u.mem_map(addr,size)
    u.mem_write(BASE,fw)
    ptr=int.from_bytes(u.mem_read(0x2104280,8),'little');u.mem_write(ptr,struct.pack('<Q',0x12345678))
    dev,part,offset,label=INPUT,INPUT+0x100,INPUT+0x200,INPUT+0x300
    u.mem_write(dev+4,struct.pack('<I',512))
    u.mem_write(part,struct.pack('<QQQQ',0,(len(image)+511)//512,0,0))
    u.mem_write(label,name.encode().ljust(32,b'\0'))
    reads=[]
    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0,value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_LR))
    def hook(uc,address,size,_):
        off=address-BASE
        if off==0x3bc0:
            start,length,destination=[uc.reg_read(r) for r in (UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3)]
            reads.append(dict(offset=hex(start),bytes=length))
            if start+length>len(image):ret(1)
            else:uc.mem_write(destination,image[start:start+length]);ret()
        elif off==0x8aea0:ret()
        elif off==0x91190:raise AssertionError('Stack guard failed')
        elif int.from_bytes(uc.mem_read(address,4),'little') in (0xd503233f,0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC,address+4)
    u.hook_add(UC_HOOK_CODE,hook)
    u.reg_write(UC_ARM64_REG_SP,STACK+0xf000);u.reg_write(UC_ARM64_REG_LR,STOP)
    for reg,value in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3),
                         (dev,part,offset,label)):u.reg_write(reg,value)
    u.emu_start(BASE+0x7f424,STOP,count=100000)
    assert u.reg_read(UC_ARM64_REG_PC)==STOP
    assert u.reg_read(UC_ARM64_REG_SP)==STACK+0xf000
    return dict(status=hex(u.reg_read(UC_ARM64_REG_W0)),member_offset=hex(int.from_bytes(u.mem_read(offset,4),'little')),
                header_reads=reads,storage='read-only host fixture; no phone access')

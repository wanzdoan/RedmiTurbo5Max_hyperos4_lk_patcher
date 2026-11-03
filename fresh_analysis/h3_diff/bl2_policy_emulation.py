from pathlib import Path
from unicorn import *
from unicorn.arm64_const import *
import json
p=Path('fresh_analysis/h3_diff');blob=Path('fresh_analysis/container_chain/h4_original_03_bl2_ext.bin').read_bytes();BASE=0xffff0000b8000000;STACK=0x10000000;STOP=0x20000000
results=[]
for sbc,rc,override in [(0,0,False),(1,0,False),(2,0,False),(0,5,False),(0,0,True),(1,0,True)]:
 u=Uc(UC_ARCH_ARM64,UC_MODE_ARM);u.mem_map(BASE,0x400000);u.mem_write(BASE,blob);u.mem_map(STACK,0x10000);u.mem_map(STOP,0x1000)
 u.reg_write(UC_ARM64_REG_SP,STACK+0xff00);u.reg_write(UC_ARM64_REG_X30,STOP);u.reg_write(UC_ARM64_REG_X0,BASE+0x9c16c);seen=[]
 def cstr(a):
  out=bytearray()
  for q in range(128):
   c=u.mem_read(a+q,1)
   if c==b'\0':return bytes(out)
   out.extend(c)
  return bytes(out)
 def ret(v):u.reg_write(UC_ARM64_REG_X0,v&0xffffffffffffffff);u.reg_write(UC_ARM64_REG_PC,u.reg_read(UC_ARM64_REG_X30))
 def hook(uc,a,n,user):
  o=a-BASE
  if o in [0x5b018,0x5b04c,0x5b0f8,0x52e70,0x52f04]:seen.append(hex(o))
  if o==0x6a34c:
   u.mem_write(u.reg_read(UC_ARM64_REG_X0),int(sbc).to_bytes(4,'little'));ret(rc)
  elif o==0x4c33c:ret(0)
  elif o==0x4d248:ret(0 if cstr(u.reg_read(UC_ARM64_REG_X0))==cstr(u.reg_read(UC_ARM64_REG_X1)) else 1)
  elif o==0x52f04 and override:
   u.mem_write(u.reg_read(UC_ARM64_REG_X0),(3).to_bytes(4,'little'));ret(0)
 u.hook_add(UC_HOOK_CODE,hook)
 try:
  u.emu_start(BASE+0x5b018,STOP,count=10000)
  results.append(dict(sboot_value=sbc,sboot_helper_status=rc,custom_getter_replaced_by_mock_success=override,auth_required=u.reg_read(UC_ARM64_REG_X0),visited=seen,pc=hex(u.reg_read(UC_ARM64_REG_PC))))
 except UcError as e:results.append(dict(error=str(e),pc=hex(u.reg_read(UC_ARM64_REG_PC))))
summary=dict(kind='isolated execution of supplied BL2 machine code; hardware/strcmp/logging helpers mocked',subject='h4_original_03_bl2_ext.bin',limits=['SBC value mocked; not device observation','No loader/certificate/decryption execution','Two override cases intentionally replace unsupported getter only in emulator hooks; no artifact modified'],cases=results)
(p/'bl2_policy_emulation.json').write_text(json.dumps(summary,indent=2));print(json.dumps(summary,indent=2))

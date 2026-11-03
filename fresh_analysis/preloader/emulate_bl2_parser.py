"""Actual BL2 ARM64 hash parser experiment; no crypto/auth-result mocks.
Only PAC instructions and logging are bypassed. No firmware patch/flash.
"""
from pathlib import Path
import struct,json
from unicorn import *
from unicorn.arm64_const import *
ROOT=Path(__file__).resolve().parents[2];OUT=Path(__file__).resolve().parent
inv=json.loads((ROOT/'fresh_analysis/container_chain/container_inventory.json').read_text())
BASE=0xffff0000b8000000;INPUT=0x3000000;STACK=0x3100000;STOP=0x3200000
def run(version,carrier,mask,certificate_record=None):
 code=(ROOT/'fresh_analysis/container_chain'/f'h{version}_original_03_bl2_ext.bin').read_bytes()
 if certificate_record is None:
  obj=inv[carrier];r=obj['records'][2];raw=(ROOT/obj['file']).read_bytes();buf=raw[r['header_offset']:r['end']]
 else:buf=certificate_record
 uc=Uc(UC_ARCH_ARM64,UC_MODE_ARM);uc.mem_map(BASE,0x300000);uc.mem_write(BASE,code)
 uc.mem_map(INPUT,0x30000);uc.mem_write(INPUT,buf);uc.mem_map(STACK,0x10000);uc.mem_map(STOP,0x1000)
 entry=0x6cf8c if version==3 else 0x6d6bc
 debug=0x4beb0 if version==3 else 0x4c33c
 payload=0x1b7ce8 if version==3 else 0x1b8d28
 header=0x1b7d1c if version==3 else 0x1b8d5c
 n=0;last=[]
 def hook(uc,addr,size,data):
  nonlocal n
  n+=1;off=addr-BASE;last.append(hex(off));last[:]=last[-12:]
  if off==debug:
   uc.reg_write(UC_ARM64_REG_X0,0);uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR));return
  word=int.from_bytes(uc.mem_read(addr,4),'little')
  if word in (0xd503233f,0xd50323bf):uc.reg_write(UC_ARM64_REG_PC,addr+4)
 uc.hook_add(UC_HOOK_CODE,hook)
 uc.reg_write(UC_ARM64_REG_SP,STACK+0xf000);uc.reg_write(UC_ARM64_REG_LR,STOP)
 for reg,val in zip((UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2),(INPUT,len(buf),mask)):uc.reg_write(reg,val)
 error=None
 try:uc.emu_start(BASE+entry,STOP,count=1500000)
 except Exception as ex:error=str(ex)
 return {'version':version,'carrier':carrier,'mask':hex(mask),'returned':uc.reg_read(UC_ARM64_REG_PC)==STOP,'error':error,'instructions':n,'last_offsets':last,'status':hex(uc.reg_read(UC_ARM64_REG_W0)),'header_hash':bytes(uc.mem_read(BASE+header,32)).hex(),'payload_hash':bytes(uc.mem_read(BASE+payload,32)).hex()}
if __name__=='__main__':
 result=[run(v,c,m) for v in (3,4) for c in ('h3_original','h3_patched','h4_original') for m in (0x60,0x1f4e2,0x1f400)]
 (OUT/'bl2_parser_emulation.json').write_text(json.dumps(result,indent=2));print(json.dumps(result,indent=2))


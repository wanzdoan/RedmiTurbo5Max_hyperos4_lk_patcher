from pathlib import Path
from capstone import *
from capstone.arm64 import *
import re,json
p=Path('fresh_analysis/container_chain');d=(p/'h4_original_03_bl2_ext.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.detail=True;cs.skipdata=True;ins=list(cs.disasm(d[:0x85000],0));wanted={m.start():m.group().decode() for m in re.finditer(rb'[ -~]{6,}',d) if any(x in m.group().lower() for x in [b'cert chain',b'cert parsing',b'image auth fail',b'lk verify',b'load bl33',b'end of cert'])};refs=[]
for i,x in enumerate(ins):
 if x.mnemonic not in ('adr','adrp') or len(x.operands)<2:continue
 reg=x.operands[0].reg;v=x.operands[1].imm
 if x.mnemonic=='adr':cand=[(v,x.address)]
 else:
  cand=[]
  for y in ins[i+1:i+9]:
   if y.mnemonic=='add' and len(y.operands)==3 and y.operands[1].type==ARM64_OP_REG and y.operands[1].reg==reg and y.operands[2].type==ARM64_OP_IMM:cand.append((v+y.operands[2].imm,y.address))
 for val,at in cand:
  for s,st in wanted.items():
   if s<=val<s+len(st):
    start=next((z.address for z in reversed(ins[max(0,i-400):i+1]) if z.mnemonic=='paciasp'),None);refs.append(dict(offset=at,func=start,string_offset=s,string=st))
(p/'bl2_auth_refs.json').write_text(json.dumps(refs,indent=2));print(json.dumps(refs,indent=2))


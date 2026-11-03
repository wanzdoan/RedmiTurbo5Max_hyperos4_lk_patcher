from pathlib import Path
from capstone import *
from capstone.arm64 import *
import json
p=Path('fresh_analysis/h3_diff/aee_analysis');d=(p/'aee_h4.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.detail=True;cs.skipdata=True
regs={}; hits=[]
for i in cs.disasm(d[:0xa3000],0):
 if i.mnemonic=='adrp':regs[i.operands[0].reg]=i.operands[1].imm
 elif i.mnemonic=='add' and len(i.operands)==3 and i.operands[2].type==ARM64_OP_IMM:
  r=i.operands[1].reg
  if r in regs:
   v=regs[r]+i.operands[2].imm;regs[i.operands[0].reg]=v
   if v in [0xb1562,0xc6e52,0xac62c]:hits.append((i.address,v))
 elif i.mnemonic=='adr':
  v=i.operands[1].imm
  if v in [0xb1562,0xc6e52,0xac62c]:hits.append((i.address,v))
print(hits)
for o,v in hits:
 print('REF',hex(v),'at',hex(o))
 for j in cs.disasm(d[o-40:o+100],o-40): print(hex(j.address),j.mnemonic,j.op_str)

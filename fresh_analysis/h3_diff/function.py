from pathlib import Path
from capstone import *
import struct,json,hashlib,re,difflib,collections
A=Path('lk_hyperos3_original/lk.img').read_bytes(); B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
out=[]
for name,d in [('original',A),('patched',B)]:
 out.append(name)
 for o in range(0x89a2c,0x89e00,4):
  for i in cs.disasm(d[o:o+4],o):out.append(f'{o:08x} {i.mnemonic} {i.op_str}')
 out.append('callers')
 for i in cs.disasm(d[0x200:0xcf000],0x200):
  if i.mnemonic in ('bl','b') and i.op_str=='#0x89a2c':
   out.append(f'CALL {i.address:x}')
   for x in cs.disasm(d[i.address-24:i.address+40],i.address-24):out.append(f'{x.address:08x} {x.mnemonic} {x.op_str}')
Path('fresh_analysis/h3_diff/function.txt').write_text('\n'.join(out)); print('\n'.join(out[:270]))

from pathlib import Path
from capstone import *
A=Path('lk_hyperos3_original/lk.img').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
out=[]
for i in cs.disasm(A[0x200:0xcf000],0x200):
 if i.mnemonic in ('bl','b') and i.op_str=='#0x88950':
  out.append(f'CALL {i.address:x}')
  for x in cs.disasm(A[i.address-48:i.address+100],i.address-48):out.append(f'{x.address:08x} {x.mnemonic} {x.op_str}')
Path('fresh_analysis/h3_diff/get_lock_callers2.txt').write_text('\n'.join(out));print('\n'.join(out))


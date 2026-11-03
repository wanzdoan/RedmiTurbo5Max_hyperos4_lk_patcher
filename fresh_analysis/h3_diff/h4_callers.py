from pathlib import Path
from capstone import *
A=Path('lk_hyperos4_original/lk.img').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
out=[]
for i in cs.disasm(A[0x200:0xcf000],0x200):
 if i.mnemonic in ('bl','b') and i.op_str=='#0x88c10':
  out.append(f'CALL {i.address:x}')
  for x in cs.disasm(A[i.address-48:i.address+100],i.address-48):out.append(f'{x.address:08x} {x.mnemonic} {x.op_str}')
Path('fresh_analysis/h3_diff/h4_lock_callers.txt').write_text('\n'.join(out));print('\n'.join(out))


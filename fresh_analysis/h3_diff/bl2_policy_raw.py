from pathlib import Path
from capstone import *
d=Path('fresh_analysis/container_chain/h4_original_03_bl2_ext.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
for s,e in [(0x5b018,0x5b1bc),(0x52e70,0x52f28)]:
 for i in cs.disasm(d[s:e],s):print(hex(i.address),i.mnemonic,i.op_str)

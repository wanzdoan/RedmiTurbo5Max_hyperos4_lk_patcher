from pathlib import Path
from capstone import *
d=Path('fresh_analysis/container_chain/h4_original_03_bl2_ext.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
for i in cs.disasm(d[0x9174:0x9480],0x9174):print(hex(i.address),i.mnemonic,i.op_str)

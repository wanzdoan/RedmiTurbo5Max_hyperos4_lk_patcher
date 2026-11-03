from pathlib import Path
import struct,re
from capstone import *
d=Path('lk_hyperos4_original/lk.img').read_bytes()
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
for x in cs.disasm(d[0x200:0x300],0x200):print(hex(x.address),x.mnemonic,x.op_str)
for m in re.finditer(bytes.fromhex('88168858'),d):
 o=m.start(); print(hex(o),d[o+8:o+40].split(b'\0')[0],hex(struct.unpack_from('<I',d,o+4)[0]),d[o+40:o+80].hex())

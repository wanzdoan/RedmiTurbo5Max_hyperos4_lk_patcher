from pathlib import Path
from capstone import *
import re
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
a=Path('lk_hyperos3_original/lk.img').read_bytes();b=Path('lk_hyperos4_original/lk.img').read_bytes()
pat=a[0x89a2c:0x89a2c+32]
print('h4 entry matches',[hex(m.start()) for m in re.finditer(re.escape(pat),b)])
for name,d in [('h3',a),('h4',b)]:
 print(name)
 for x in cs.disasm(d[0x899e0:0x89c00],0x899e0):print(hex(x.address),x.mnemonic,x.op_str)

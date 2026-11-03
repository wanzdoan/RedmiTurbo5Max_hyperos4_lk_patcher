from pathlib import Path
import re,difflib,json,struct
from capstone import *
A=Path('lk_hyperos3_original/lk.img').read_bytes();B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()
ss=lambda d:[(m.start(),m.group().decode()) for m in re.finditer(rb'[ -~]{5,}',d[:0x139d28])]
a=ss(A);b=ss(B); sm=difflib.SequenceMatcher(None,[s[1] for s in a],[s[1] for s in b],autojunk=False)
changes=[]
for op,i,j,k,l in sm.get_opcodes():
 if op!='equal':changes.append(dict(op=op,old=a[i:j],new=b[k:l]))
print(json.dumps(changes,indent=2));Path('fresh_analysis/h3_diff/string_changes.json').write_text(json.dumps(changes,indent=2))
for off in [0xfe191,0xe3ab7,0xdd338,0xd6d30,0xf36ff,0xe3ac9,0xeed81,0xfc4c9,0xfc500,0xf3e25,0x136890]: print(hex(off), A[off:off+140].split(b'\0')[0])
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
for start,end in [(0x88eec,0x88f94),(0x89d80,0x8a008)]:
 for i in cs.disasm(A[start:end],start):print(hex(i.address),i.mnemonic,i.op_str)

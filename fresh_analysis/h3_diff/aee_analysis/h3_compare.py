from pathlib import Path
from capstone import *
import json,collections,re
A=Path('lk_hyperos3_original/lk.img').read_bytes()[0x243670:0x243670+0xf5778];B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()[0x243720:0x243720+0xf5778]
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
counts=collections.Counter();changes=[]
for o in range(0,0xa3000,4):
 if A[o:o+4]==B[o:o+4]:continue
 a=next(cs.disasm(A[o:o+4],o));b=next(cs.disasm(B[o:o+4],o));counts[(a.mnemonic,b.mnemonic)]+=1
 if a.mnemonic==b.mnemonic and a.mnemonic in ['adr','adrp','add']:continue
 changes.append(dict(offset=hex(o),old=a.mnemonic+' '+a.op_str,new=b.mnemonic+' '+b.op_str))
print('h3 AEE changed mnemonics',counts,'nonaddr',len(changes));print(json.dumps(changes[:25],indent=2))
Path('fresh_analysis/h3_diff/aee_analysis/h3_aee_changes.json').write_text(json.dumps(dict(boundary='payload offsets0..0xa3000',mnemonics={str(k):v for k,v in counts.items()},non_address_changes=changes),indent=2))

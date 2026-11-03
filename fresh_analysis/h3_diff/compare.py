from pathlib import Path
from capstone import *
import json,collections
A=Path('lk_hyperos3_original/lk.img').read_bytes(); B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
changes=[];counts=collections.Counter()
for o in range(0x200,0xcf000,4):
 if A[o:o+4]==B[o:o+4]:continue
 a=list(cs.disasm(A[o:o+4],o));b=list(cs.disasm(B[o:o+4],o))
 if not a or not b:continue
 a,b=a[0],b[0]; counts[(a.mnemonic,b.mnemonic)]+=1
 if a.mnemonic==b.mnemonic and a.mnemonic in ['adrp','adr','add']:continue
 changes.append(dict(offset=hex(o),old=a.mnemonic+' '+a.op_str,new=b.mnemonic+' '+b.op_str,oldhex=A[o:o+4].hex(),newhex=B[o:o+4].hex()))
Path('fresh_analysis/h3_diff/word_changes.json').write_text(json.dumps(changes,indent=2));print('mnemonics',counts);print('non addr',len(changes));print(json.dumps(changes[:120],indent=2))

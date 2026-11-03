from pathlib import Path
from capstone import *
from capstone.arm64 import *
import collections,json,re,struct,hashlib
A=Path('lk_hyperos3_original/lk.img').read_bytes();B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.detail=True;cs.skipdata=True
C=collections.Counter(); examples=[]; bad=[]
for o in range(0x200,0xcf000,4):
 if A[o:o+4]==B[o:o+4]:continue
 a=next(cs.disasm(A[o:o+4],o-0x200));b=next(cs.disasm(B[o:o+4],o-0x200))
 if a.mnemonic=='add' and b.mnemonic=='add':
  C[(a.op_str.split(', #')[0], b.operands[-1].imm-a.operands[-1].imm)]+=1
  if a.op_str.split(', #')[0]!=b.op_str.split(', #')[0]:bad.append(hex(o))
  if len(examples)<20:examples.append((hex(o),a.op_str,b.op_str))
print('add changes',C);print('changed registers',bad); print('examples',examples)
# Header + body exact hashes for all 15 members
members=[]
for name,d in [('original',A),('patched',B)]:
 rows=[]
 for m in re.finditer(bytes.fromhex('88168858'),d):
  off=m.start();n=struct.unpack_from('<I',d,off+4)[0];label=d[off+8:off+40].split(b'\0')[0].decode()
  rows.append(dict(offset=hex(off),name=label,size=n,sha256=hashlib.sha256(d[off+512:off+512+n]).hexdigest(),header_sha256=hashlib.sha256(d[off:off+512]).hexdigest()))
 members.append((name,rows))
Path('fresh_analysis/h3_diff/members.json').write_text(json.dumps(members,indent=2));print(json.dumps(members,indent=2))

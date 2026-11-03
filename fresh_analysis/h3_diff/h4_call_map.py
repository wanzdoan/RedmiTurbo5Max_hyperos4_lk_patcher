from pathlib import Path
from capstone import *
import re,json
A=Path('lk_hyperos3_original/lk.img').read_bytes();H=Path('lk_hyperos4_original/lk.img').read_bytes()
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
inst=lambda d:list(cs.disasm(d[0x200:0xcf000],0x200))
a=inst(A);h=inst(H)
def canon(i):
 m=i.mnemonic;s=i.op_str
 if m in ['adr','adrp','bl','b'] or m.startswith('b.'):return m+(' '+s.split(',')[0] if m in ['adr','adrp'] else '')
 if m in ['add','ldr','str','ldrb','strb','ldp','stp','cbz','cbnz','tbz','tbnz']:
  s=re.sub(r'#[x0-9a-f]+','#IMM',s)
 return m+' '+s
at=[canon(i) for i in a];ht=[canon(i) for i in h]
text='\n'.join(ht); rows=[]
for idx,i in enumerate(a):
 if i.mnemonic in ('bl','b') and i.op_str in ['#0x88950','#0x88f60','#0x89a2c']:
  start=max(0,idx-12);pattern='\n'.join(at[start:idx+13]);matches=[m.start() for m in re.finditer(re.escape(pattern),text)]
  candidates=[]
  for pos in matches:
   q=text[:pos].count('\n');hi=q+idx-start
   candidates.append(dict(file_offset=hex(h[hi].address),target=h[hi].op_str,window=[f'{j.address:x}: {j.mnemonic} {j.op_str}' for j in h[q:q+25]]))
  rows.append(dict(h3_call=hex(i.address),h3_target=i.op_str,h4_candidates=candidates))
Path('fresh_analysis/h3_diff/h4_call_map.json').write_text(json.dumps(rows,indent=2));print(json.dumps([{**r,'h4_candidates':[{k:v for k,v in c.items() if k!='window'} for c in r['h4_candidates']]} for r in rows],indent=2))

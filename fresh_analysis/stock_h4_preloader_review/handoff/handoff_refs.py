from pathlib import Path
from capstone import *
import json,hashlib
root=Path.cwd();out=root/'fresh_analysis/stock_h4_preloader_review/handoff'; d=(root/'preloader_hyperos4/preloader_dash.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.skipdata=True
ins=list(cs.disasm(d[0x100:0xb0000],0x100));targets=[0x6a578,0x6a7a4];rows=[]
for i in ins:
 if i.mnemonic in ['bl','b'] and i.op_str in [f'#{hex(x)}' for x in targets]:
  lo=max(0,i.address-100);hi=i.address+160
  rows.append(dict(call=hex(i.address),target=i.op_str,context=[f'{j.address:08x}: {j.mnemonic} {j.op_str}' for j in cs.disasm(d[lo:hi],lo)]))
(out/'preloader_target_callers.json').write_text(json.dumps(rows,indent=2))
text=[]
for s,e in [(0x6a7a4,0x6a8d0),(0x6b000,0x6b360),(0x6a410,0x6a578),(0x6a680,0x6a7a4)]:
 text.append(f'RANGE{s:x}..{e:x}')
 for i in cs.disasm(d[s:e],s):text.append(f'{i.address:08x}: {i.mnemonic} {i.op_str}')
(out/'preloader_handoff_disassembly.txt').write_text('\n'.join(text))
print(json.dumps(rows,indent=2));print('\n'.join(text[:100]))

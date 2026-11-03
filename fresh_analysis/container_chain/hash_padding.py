from pathlib import Path
import json,hashlib
p=Path('fresh_analysis/container_chain');inv=json.loads((p/'container_inventory.json').read_text())
for k,o in inv.items():
 d=Path(o['file']).read_bytes()
 print(k)
 for i,r in enumerate(o['records']):
  if r['name']!='cert2':continue
  c=(p/f'{k}_{i:02d}_cert2.bin').read_bytes();a=c.find(bytes.fromhex('060760867693160201'));target=c[a+12:a+44];r=o['records'][i-2];start=r['header_offset']+512;end=r['end'];matches=[]
  for align in [4,8,16,32,64,512,4096]:
   alignend=(end+align-1)//align*align
   for label,b in [('file',d[start:alignend]),('zero',d[start:end]+bytes(alignend-end)),('ff',d[start:end]+b'\xff'*(alignend-end))]:
    if hashlib.sha256(b).digest()==target:matches.append((align,label,len(b)))
  print(r['name'],matches)

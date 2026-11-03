from pathlib import Path
import hashlib,struct,json
out=Path('fresh_analysis/container_chain');inv=json.loads((out/'container_inventory.json').read_text())
for k,obj in inv.items():
 for i,r in enumerate(obj['records']):
  if r['name']=='cert2':
   c=(out/f'{k}_{i:02d}_cert2.bin').read_bytes();po=obj['records'][i-2];p=(out/f'{k}_{i-2:02d}_{po["name"]}.bin').read_bytes();oid=bytes.fromhex('060760867693160201');at=c.find(oid);t=c[at+12:at+44];h=hashlib.sha256();match=[]
   for j,v in enumerate(p):
    h.update(bytes([v]))
    if h.digest()==t:match.append(j+1)
   print(k,po['name'],'prefixmatch',match,'target',t.hex(),'len',len(p))
   if po['name']=='lk':
    for off in [0x100,0x200,0x1000]:print('skip',hex(off),hashlib.sha256(p[off:]).hexdigest())

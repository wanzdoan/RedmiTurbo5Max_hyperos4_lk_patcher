from pathlib import Path
import json,hashlib,struct
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
out=Path('fresh_analysis/container_chain'); inv=json.loads((out/'container_inventory.json').read_text())
for k,obj in inv.items():
 d=Path(obj['file']).read_bytes();print('\n',k)
 for i,r in enumerate(obj['records']):
  if r['name']!='cert2':continue
  c=(out/f'{k}_{i:02d}_cert2.bin').read_bytes();po=obj['records'][i-2];payload=d[po['header_offset']+512:po['end']];header=d[po['header_offset']:po['header_offset']+512]
  targets={'payload':hashlib.sha256(payload).hexdigest(),'header':hashlib.sha256(header).hexdigest(),'headerpayload':hashlib.sha256(header+payload).hexdigest()}
  print('component',po['name'],'hashes',targets)
  for oid in [bytes.fromhex('060760867693160201'),bytes.fromhex('060760867693160204')]:
   pos=0
   while True:
    pos=c.find(oid,pos)
    if pos<0:break
    data=c[pos+11:pos+44] # expected bit str 03 21 00 bytes starts +12
    print('oid',oid.hex(),'at',hex(pos),'next',c[pos+9:pos+44].hex())
    pos+=1

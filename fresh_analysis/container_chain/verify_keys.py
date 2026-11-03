from pathlib import Path
from cryptography.hazmat.primitives.serialization import load_der_public_key,Encoding,PublicFormat
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
import hashlib,json
p=Path('fresh_analysis/container_chain');cs={};pubs={}
for k in ['h3_original','h3_patched','h4_original']:
 for ci in [1,2,4,5,7,8,10,11,13,14]:
  name='cert1' if ci%3==1 else 'cert2'; c=(p/f'{k}_{ci:02d}_{name}.bin').read_bytes();start=c.find(bytes.fromhex('308203d2')) if name=='cert2' else 0;c=c[start:];pub=load_der_public_key(c[181:475]);raw=pub.public_bytes(Encoding.DER,PublicFormat.SubjectPublicKeyInfo);digest=hashlib.sha256(raw).hexdigest();pubs[digest]=pub
  cs[f'{k}_{ci:02d}']={'c':c,'pub':digest,'start':start}
print('unique keys',len(pubs),list(pubs))
for n,o in cs.items():
 c=o['c'];end=8+int.from_bytes(c[6:8],'big'); verified=[]
 for h,pub in pubs.items():
  try:pub.verify(c[-256:],c[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256());verified.append(h)
  except:pass
 print(n,'own',o['pub'],'signedby',verified)

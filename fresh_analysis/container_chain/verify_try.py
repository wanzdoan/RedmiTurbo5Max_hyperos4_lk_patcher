from pathlib import Path
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
import json,struct
p=Path('fresh_analysis/container_chain'); data={}
for k in ['h3_original','h3_patched','h4_original']:
 print('\n',k)
 for ci in [1,2,4,5,7,8,10,11,13,14]:
  name='cert1' if ci%3==1 else 'cert2'; c=(p/f'{k}_{ci:02d}_{name}.bin').read_bytes();start=c.find(bytes.fromhex('308203d2')) if name=='cert2' else 0;c=c[start:]
  try:
   pub=load_der_public_key(c[181:475])
   tbslen=int.from_bytes(c[6:8],'big');end=8+tbslen
   sig=c[-256:]
   pub.verify(sig,c[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256());res='self verified'
  except Exception as e:res=str(type(e).__name__)+':'+str(e)
  print(ci,name,'certstart',start,'verify',res)

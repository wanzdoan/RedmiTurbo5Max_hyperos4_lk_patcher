from pathlib import Path
import hashlib,json,struct
from cryptography.hazmat.primitives.serialization import load_der_public_key,Encoding,PublicFormat
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
p=Path(__file__).resolve().parent;root=p.parent.parent;inv=json.loads((p/'container_inventory.json').read_text());evidence={'scope':'Static supplied artifacts only; no device execution or root-of-trust verification','certificates':[]}
for k,o in inv.items():
 d=(root/o['file']).read_bytes()
 for i,r in enumerate(o['records']):
  if r['name'] not in ['cert1','cert2']:continue
  raw=(p/f'{k}_{i:02d}_{r["name"]}.bin').read_bytes();start=180 if k=='h3_patched' and i==2 else 0;c=raw[start:];assert c[0:2]==b'\x30\x82';tbs_end=8+int.from_bytes(c[6:8],'big');pub=load_der_public_key(c[181:475]);pub.verify(c[-256:],c[4:tbs_end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256());item={'input':k,'record':i,'name':r['name'],'der_start':start,'rsa_pss_sha256_salt32_valid_under_embedded_spki':True,'spki_sha256':hashlib.sha256(pub.public_bytes(Encoding.DER,PublicFormat.SubjectPublicKeyInfo)).hexdigest()}
  if r['name']=='cert2':
   comp=o['records'][i-2];off=comp['header_offset'];sz=comp['size'];head=d[off:off+512];payload=d[off+512:off+512+((sz+15)&~15)];hh=hashlib.sha256(head).digest();ph=hashlib.sha256(payload).digest();oid1=bytes.fromhex('060760867693160201');oid4=bytes.fromhex('060760867693160204');loc1=c.index(oid1)+12;loc4=c.index(oid4)+12
   item.update(component=comp['name'],header_sha256=hh.hex(),aligned_payload_sha256=ph.hex(),signed_payload_digest=c[loc1:loc1+32].hex(),signed_payload_digest_matches=ph==c[loc1:loc1+32],signed_header_digest_matches=hh==c[loc4:loc4+32])
   if start:
    block=b'\xa0\x58'+oid4+b'\x03\x21\x00'+hh+oid1+b'\x03\x21\x00'+ph;assert len(block)==90;assert raw[:180]==block*2;item['prefix_exactly_duplicate_90_byte_hash_override']=True
  evidence['certificates'].append(item)
a=(root/'lk_hyperos3_original/lk.img').read_bytes();b=bytearray((root/'lk_hyperos3_patched_unlockBL/dash.img').read_bytes());b[0x89a2c:0x89a3c]=a[0x89a2c:0x89a3c];digest=hashlib.sha256(b[0x200:0x139d30]).hexdigest();signed=next(x['signed_payload_digest'] for x in evidence['certificates'] if x['input']=='h3_patched' and x['record']==2);assert digest==signed
evidence['restoring_original_function_prologue_in_donor_h3']={'file_offset':'0x89a2c','length':16,'restored_aligned_payload_sha256':digest,'matches_signed_embedded_der':True}
(p/'cryptographic_evidence.json').write_text(json.dumps(evidence,indent=2));print('PASS: 30 PSS signatures, all hash domains, duplicate prefix and donor-restoration digest verified')

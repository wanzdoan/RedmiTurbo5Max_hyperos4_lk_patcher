from pathlib import Path
import hashlib,json,struct
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
root=Path(__file__).resolve().parent.parent
src=(root/'lk_hyperos4_original/lk.img').read_bytes();out=(root/'fresh_analysis/rejected/lk_h4_main_only_REJECTED.img').read_bytes()
def parse(d):
 rows=[];off=0
 while off<len(d):
  assert d[off:off+4]==bytes.fromhex('88168858')
  n=int.from_bytes(d[off+4:off+8],'little');end=(off+512+n+15)&~15
  assert end<=len(d);rows.append((off,n,d[off+8:off+40].split(b'\0')[0].decode(),end));off=end
 assert off==len(d) and len(rows)==15
 return rows
old=parse(src);new=parse(out);e=[]
for i,(off,n,name,end) in enumerate(new):
 oo,on,_,oe=old[i]
 if i not in (0,2):assert out[off:end]==src[oo:oe]
 if name not in ('cert1','cert2'):continue
 raw=out[off+512:off+512+n];prefix=180 if i==2 else 0;c=raw[prefix:]
 assert c[0:2]==b'\x30\x82' and int.from_bytes(c[2:4],'big')+4==len(c)
 tbsend=8+int.from_bytes(c[6:8],'big');pub=load_der_public_key(c[181:475]);pub.verify(c[-256:],c[4:tbsend],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
 row=dict(member=i,name=name,retained_der_signature_valid=True)
 if name=='cert2':
  po,pn,component,pe=new[i-2];hdr=out[po:po+512];payload=out[po+512:pe];hh=hashlib.sha256(hdr).digest();ph=hashlib.sha256(payload).digest();oid1=bytes.fromhex('060760867693160201');oid4=bytes.fromhex('060760867693160204')
  signed_payload=c[c.index(oid1)+12:c.index(oid1)+44];signed_hdr=c[c.index(oid4)+12:c.index(oid4)+44]
  assert signed_hdr==hh;row.update(component=component,signed_header_matches=True,signed_payload_matches=signed_payload==ph)
  if prefix:
   block=b'\xa0\x58'+oid4+b'\x03\x21\x00'+hh+oid1+b'\x03\x21\x00'+ph
   assert raw[:180]==2*block and signed_payload!=ph
   assert c==src[oo+512:oo+512+on]
   row.update(unsigned_prefix_hashes_match=True,retained_signed_payload_mismatch_expected=True,requires_parser_or_auth_bypass=True)
  else:assert signed_payload==ph
 e.append(row)
report=dict(status='PASS',meaning='Structure and retained certificate math validated; overall image authentication NOT established',certificates=e,unmodified_records_byte_identical=13,modified_main_payload_bytes=sum(a!=b for a,b in zip(src[512:old[0][3]],out[512:new[0][3]])))
(root/'fresh_analysis/candidate_certificate_validation.json').write_text(json.dumps(report,indent=2));print(json.dumps(report,indent=2))

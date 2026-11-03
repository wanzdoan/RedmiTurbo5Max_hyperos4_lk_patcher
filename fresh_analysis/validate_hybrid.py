from build_candidate import ROOT,members,sha
from build_hybrid import build_hybrid,NAME
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
import hashlib,json
h3=(ROOT/'lk_hyperos3_original/lk.img').read_bytes();h4=(ROOT/'lk_hyperos4_original/lk.img').read_bytes();image=(ROOT/'fresh_analysis/output'/NAME).read_bytes()
assert build_hybrid(h4,h3)[0]==image
pure=(ROOT/'fresh_analysis/rejected/lk_h4_main_only_REJECTED.img').read_bytes();ms=members(image);ps=members(pure)
assert image[:ms[3]['offset']]==pure[:ps[3]['offset']]
# This equality ties already executed CPU code and candidate certificate snapshots to the final artifact.
snapshot=(ROOT/'fresh_analysis/preloader/h4_candidate_cert2_record_snapshot.bin').read_bytes();c2=ms[2]
assert image[c2['offset']:c2['offset']+512+c2['size']]==snapshot
rows=[]
for i,m in enumerate(ms):
 if m['name'] not in ('cert1','cert2'):continue
 p=image[m['offset']+512:m['offset']+512+m['size']];c=p[180:] if i==2 else p
 end=8+int.from_bytes(c[6:8],'big');load_der_public_key(c[181:475]).verify(c[-256:],c[4:end],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256())
 row=dict(member=i,name=m['name'],embedded_der_rsa_pss_valid=True)
 if m['name']=='cert2':
  comp=ms[i-2];o=comp['offset'];hh=hashlib.sha256(image[o:o+512]).digest();ph=hashlib.sha256(image[o+512:comp['end']]).digest()
  oid1=bytes.fromhex('060760867693160201');oid4=bytes.fromhex('060760867693160204');sh=c[c.index(oid4)+12:c.index(oid4)+44];sp=c[c.index(oid1)+12:c.index(oid1)+44]
  assert hh==sh
  if i==2:
   assert ph!=sp
   assert p[14:46]==hh and p[58:90]==ph
  else:assert ph==sp
  row.update(component=comp['name'],header_hash_matches=True,signed_payload_hash_matches=ph==sp,payload_sha256=ph.hex())
 rows.append(row)
report=dict(status='PASS',artifact=NAME,size=len(image),sha256=sha(image),deterministic_rebuild=True,final_main_and_cert_bytes_equal_cpu_tested_candidate=True,final_cert_record_equal_parser_tested_snapshot=True,all_10_retained_der_signatures_valid=True,all_non_main_signed_payloads_match=True,retained_bl2_is_exact_signed_H3=True,certificates=rows,limits=['Main signed digest differs by design: successful boot relies on actual H3 BL2 parser behavior, tested separately','No full SoC simulation, physical boot, antirollback/fuse or cross-version handoff validation'])
(ROOT/'fresh_analysis/hybrid_validation.json').write_text(json.dumps(report,indent=2));print(json.dumps({k:v for k,v in report.items() if k!='certificates'},indent=2))

from pathlib import Path
import hashlib,struct,json
from emulate_bl2_parser import run,ROOT,OUT

path=ROOT/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
image=path.read_bytes()
size=struct.unpack_from('<I',image,4)[0]
header_hash=hashlib.sha256(image[:512]).hexdigest()
payload_hash=hashlib.sha256(image[512:512+((size+15)&~15)]).hexdigest()
cert1=((512+size+15)//16)*16
cert1_size=struct.unpack_from('<I',image,cert1+4)[0]
cert2=((cert1+512+cert1_size+15)//16)*16
assert image[cert2+8:cert2+13]==b'cert2'
cert2_size=struct.unpack_from('<I',image,cert2+4)[0]
record=image[cert2:cert2+512+cert2_size]
(OUT/'h4_candidate_cert2_record_snapshot.bin').write_bytes(record)
stock=(ROOT/'fresh_analysis/container_chain/h4_original_02_cert2.bin').read_bytes()
oid=bytes.fromhex('060760867693160201')
signed_hash=stock[stock.index(oid)+12:stock.index(oid)+44].hex()
results=[run(v,'candidate_H4_BL33_H3_BL2',m,record) for v in (3,4) for m in (0x60,0x1f4e2,0x1f400)]
for result in results:
 assert result['returned'] and result['error'] is None and result['status']=='0x0'
 if result['mask']!='0x1f400':
  expected=payload_hash if result['version']==3 else signed_hash
  assert result['payload_hash']==expected,(result,expected)
  assert result['header_hash']==header_hash
evidence={'evidence_id':'BL2-CANDIDATE-PARSER-001','input_at_capture':str(path),'captured_image_size':len(image),'captured_image_sha256':hashlib.sha256(image).hexdigest(),
 'certificate_record_offset':hex(cert2),'certificate_record_sha256':hashlib.sha256(record).hexdigest(),
 'patched_payload_sha256':payload_hash,'original_H4_signed_payload_sha256':signed_hash,'main_header_sha256':header_hash,'results':results,
 'assertions':'All six parser calls return success. H3 parser extracts candidate patched digest; H4 parser extracts original signed H4 digest. Metadata-only mask requests no hashes.',
 'limitations':['Isolated ARM64 parser function execution; no hardware or boot-chain simulation.','PAC/logging bypasses only; no OID/authentication-result/crypto mocks.','This evidence validates hash selection only, not hybrid compatibility or real-device boot.','Captured candidate is the actual hybrid file; original signed H3 BL2 member identity is validated separately.']}
(OUT/'hybrid_candidate_parser_validation.json').write_text(json.dumps(evidence,indent=2))
print(json.dumps({k:evidence[k] for k in ('evidence_id','captured_image_sha256','patched_payload_sha256','original_H4_signed_payload_sha256','assertions')},indent=2))

from pathlib import Path
import struct,hashlib,json,re
from cryptography.hazmat.primitives.serialization import load_der_public_key,Encoding,PublicFormat
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
root=Path.cwd();out=root/'fresh_analysis/stock_h4_preloader_review/chain';out.mkdir(parents=True,exist_ok=True)
paths={'hybrid':'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img','h3':'lk_hyperos3_original/lk.img','h4':'lk_hyperos4_original/lk.img'}
def digest(d):return hashlib.sha256(d).hexdigest()
def parse(d):
 records=[];off=0
 while off<len(d):
  assert d[off:off+4]==bytes.fromhex('88168858');sz=struct.unpack_from('<I',d,off+4)[0];name=d[off+8:off+40].split(b'\0')[0].decode();end=(off+512+sz+15)&~15
  records.append(dict(name=name,off=off,size=sz,end=end,header=d[off:off+512],payload=d[off+512:off+512+sz],padded_payload=d[off+512:end]));off=end
 return records
def get_tlv(d,o):
 tag=d[o];n=d[o+1];hs=2
 if n&128:count=n&127;n=int.from_bytes(d[o+2:o+2+count],'big');hs+=count
 return tag,d[o+hs:o+hs+n],o+hs+n

def fields(c):
 r={}
 for m in re.finditer(bytes.fromhex('06076086769316'),c[:721]):
  o=m.start();oid='2.16.886.2454.'+'.'.join(str(x) for x in c[o+7:o+9]);tag,val,end=get_tlv(c,o+9);r[oid]={'tag':hex(tag),'value':val.hex(),'size':len(val)}
 return r
files={k:(root/p).read_bytes() for k,p in paths.items()};rs={k:parse(v) for k,v in files.items()};e={'evidence_id':'STOCKH4-CHAIN-001','inputs':{k:{'path':paths[k],'size':len(v),'sha256':digest(v)} for k,v in files.items()},'stage':{}}
for k,recs in rs.items():
 i=next(i for i,r in enumerate(recs) if r['name']=='bl2_ext');a,b,c=recs[i:i+3];cert1=b['payload'];cert2=c['payload'];key1=load_der_public_key(cert1[181:475]);key2=load_der_public_key(cert2[181:475]);ver=[]
 for raw,key in [(cert1,key1),(cert2,key2)]:
  tbsend=8+int.from_bytes(raw[6:8],'big');key.verify(raw[-256:],raw[4:tbsend],padding.PSS(mgf=padding.MGF1(hashes.SHA256()),salt_length=32),hashes.SHA256());ver.append(True)
 ff=fields(cert2);ph=digest(a['padded_payload']);hh=digest(a['header']);assert ff['2.16.886.2454.2.1']['value']==('00'+ph);assert ff['2.16.886.2454.2.4']['value']==('00'+hh);assert cert1[496:790]==cert2[181:475]
 tuplebytes=files[k][a['off']:c['end']]
 (out/f'{k}_bl2_tuple.bin').write_bytes(tuplebytes)
 e['stage'][k]={'tuple_offset':hex(a['off']),'tuple_size':len(tuplebytes),'tuple_sha256':digest(tuplebytes),'payload_size':a['size'],'aligned_payload_sha256':ph,'header_sha256':hh,'header_hex':a['header'][:128].hex(),'cert1_sha256':digest(cert1),'cert2_sha256':digest(cert2),'cert1_pss_sha256_valid_embedded_key':ver[0],'cert2_pss_sha256_valid_embedded_key':ver[1],'cert1_embedded_image_spki_equals_cert2_spki':True,'cert1_spki_sha256':digest(cert1[181:475]),'cert2_spki_sha256':digest(cert2[181:475]),'cert2_custom_fields':ff,'entry_link_literals':{hex(o):hex(struct.unpack_from('<Q',a['payload'],o)[0]) for o in range(0x310,0x340,8)}}
a=e['stage']['h3'];b=e['stage']['h4'];h=e['stage']['hybrid'];assert h['tuple_sha256']==a['tuple_sha256'];assert h['cert1_sha256']==b['cert1_sha256']
e['checks']={'hybrid_bl2_triple_exact_h3':True,'h3_h4_cert1_byte_identical':True,'h3_h4_cert2_same_spki':a['cert2_spki_sha256']==b['cert2_spki_sha256'],'h3_h4_cert2_custom_differences':{k:{'h3':v,'h4':b['cert2_custom_fields'].get(k)} for k,v in a['cert2_custom_fields'].items() if v!=b['cert2_custom_fields'].get(k)}}
e['limitations']=['Signatures checked against artifact embedded keys, not actual device efuse root','No preloader hardware-policy, rollback counter, rootkey/SOCID validation','No BROM acceptance test for H4 preloader or hardware boot test','Link-layout observations do not prove full ABI/handoff equivalence']
(out/'chain_evidence.json').write_text(json.dumps(e,indent=2));print(json.dumps(e['checks'],indent=2));print('PASS: 6 PSS signatures; all header/payload hashes; exact H3 donor triple and key bindings')

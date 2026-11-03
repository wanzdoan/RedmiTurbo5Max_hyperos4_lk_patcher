"""Reproducible experimental H4 port of observed H3 patch. Never flashes hardware."""
from pathlib import Path
import hashlib,json,struct,argparse
ROOT=Path(__file__).resolve().parent.parent
H4_SHA='82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b'
OLD=bytes.fromhex('3f2303d5ff4305d1fd7b12a9fc5713a9')
PATCH=bytes.fromhex('68008052080000b900008052c0035fd6')
MAGIC=bytes.fromhex('88168858')
def sha(d):return hashlib.sha256(d).hexdigest()
def align(n):return (n+15)&~15
def members(data):
 out=[];o=0
 while o<len(data):
  assert data[o:o+4]==MAGIC, f'bad member at {o:x}'
  n=struct.unpack_from('<I',data,o+4)[0];end=align(o+512+n)
  assert end<=len(data)
  out.append(dict(offset=o,size=n,name=data[o+8:o+40].split(b'\0')[0].decode(),end=end))
  o=end
 assert len(out)==15 and o==len(data)
 return out

def prefix(header,payload):
 hdrhash=hashlib.sha256(header).digest();imagehash=hashlib.sha256(payload+bytes(align(len(payload))-len(payload))).digest()
 oid=bytes.fromhex('0607608676931602')
 block=b'\xa0\x58'+oid+b'\x04\x03\x21\x00'+hdrhash+oid+b'\x01\x03\x21\x00'+imagehash
 assert len(block)==90
 return block*2

def build(src):
 assert sha(src)==H4_SHA,'Input identity mismatch; refusing patch'
 ms=members(src);assert [m['name'] for m in ms]==['lk','cert1','cert2','bl2_ext','cert1','cert2','aee','cert1','cert2','lk_main_dtb','cert1','cert2','lk_dtbo','cert1','cert2']
 assert src[0x89cec:0x89cfc]==OLD
 image=bytearray(src);image[0x89cec:0x89cfc]=PATCH
 main=ms[0];cert=ms[2];assert cert['size']==0x3d6
 pref=prefix(bytes(image[:512]),bytes(image[512:512+main['size']]))
 certheader=bytearray(src[cert['offset']:cert['offset']+512]);struct.pack_into('<I',certheader,4,cert['size']+len(pref))
 certpayload=src[cert['offset']+512:cert['offset']+512+cert['size']]
 rec=bytes(certheader)+pref+certpayload
 rec+=bytes(align(len(rec))-len(rec))
 result=bytes(image[:cert['offset']])+rec+src[cert['end']:]
 outms=members(result)
 # Every other member, including padding and certificates, is exactly preserved.
 for idx,(old,new) in enumerate(zip(ms,outms)):
  if idx not in (0,2):assert src[old['offset']:old['end']]==result[new['offset']:new['end']]
 assert result[0x89cec:0x89cfc]==PATCH
 assert result[512:0x89cec]==src[512:0x89cec] and result[0x89cfc:main['end']]==src[0x89cfc:main['end']]
 return result,dict(input_sha256=sha(src),output_sha256=sha(result),input_size=len(src),output_size=len(result),patch_offset='0x89cec',original=OLD.hex(),replacement=PATCH.hex(),cert2_prefix=pref.hex(),original_members=ms,output_members=outms,status='REJECTED_BY_H4_BL2_HASH_SELECTION')
if __name__=='__main__':
 ap=argparse.ArgumentParser();ap.add_argument('--input',type=Path,default=ROOT/'lk_hyperos4_original/lk.img');ap.add_argument('--output',type=Path,default=ROOT/'fresh_analysis/rejected/lk_h4_main_only_REJECTED.img');args=ap.parse_args()
 data,report=build(args.input.read_bytes());args.output.parent.mkdir(parents=True,exist_ok=True);args.output.write_bytes(data);args.output.with_suffix('.json').write_text(json.dumps(report,indent=2));print(json.dumps({k:v for k,v in report.items() if k not in ['original_members','output_members','cert2_prefix']},indent=2))

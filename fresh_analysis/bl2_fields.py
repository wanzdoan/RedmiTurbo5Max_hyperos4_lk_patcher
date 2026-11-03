from pathlib import Path
import re,json
out=[]
for v in (3,4):
 d=Path(f'fresh_analysis/container_chain/h{v}_original_05_cert2.bin').read_bytes();fields=[]
 for m in re.finditer(bytes.fromhex('06076086769316'),d):
  p=m.start();fields.append({'offset':hex(p),'oid_tail':d[p+7:p+9].hex(),'following_tlv':d[p+9:p+55].hex()})
 out.append({'version':v,'bl2_cert_fields':fields})
Path('fresh_analysis/bl2_signed_fields.json').write_text(json.dumps(out,indent=2));print(json.dumps(out,indent=2))

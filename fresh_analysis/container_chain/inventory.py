from pathlib import Path
import json,struct,hashlib,re
base=Path.cwd();out=base/'fresh_analysis/container_chain'; out.mkdir(exist_ok=True)
ps={'h3_original':'lk_hyperos3_original/lk.img','h3_patched':'lk_hyperos3_patched_unlockBL/dash.img','h4_original':'lk_hyperos4_original/lk.img'}
all={}
for key,p in ps.items():
 d=(base/p).read_bytes(); records=[]
 for m in re.finditer(bytes.fromhex('88168858'),d):
  off=m.start(); size=struct.unpack_from('<I',d,off+4)[0]; name=d[off+8:off+40].split(b'\0')[0].decode(); payload=d[off+0x200:off+0x200+size]
  record={'name':name,'header_offset':off,'size':size,'end':off+0x200+size,'sha256':hashlib.sha256(payload).hexdigest(),'head':payload[:96].hex(),'header':d[off:off+0x80].hex()}
  records.append(record)
  (out/f'{key}_{len(records)-1:02d}_{name}.bin').write_bytes(payload)
 all[key]={'file':p,'size':len(d),'sha256':hashlib.sha256(d).hexdigest(),'records':records}
 print(key)
 for r in records:print(r['name'],hex(r['header_offset']),hex(r['size']),r['sha256'],r['head'][:64])
(out/'container_inventory.json').write_text(json.dumps(all,indent=2))

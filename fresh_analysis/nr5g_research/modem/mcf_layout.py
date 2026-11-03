from ext4_readonly import *
fs=Ext4((BASE/'mcf_ota.img').read_bytes());rows=list(fs.walk());files={r['path']:r for r in rows if int(r['mode'],16)&0xf000==0x8000};ota=[];hits=[];fail=[]
for path,r in files.items():
 d=fs.read(r['inode'])
 if path.endswith(('.mcfopota','.mcfnwota','.mcfrawota')):
  off=0;tlvs=[]
  try:
   while off<len(d):
    tag,ln=struct.unpack_from('<II',d,off);assert ln>=8 and off+ln<=len(d);tlvs.append({'offset':off,'tag':hex(tag),'size':ln,'body_prefix_hex':d[off+8:off+min(ln,40)].hex()});off+=ln
   assert off==len(d)
  except Exception as e:fail.append({'path':path,'offset':off,'error':repr(e)})
  ota.append({'path':path,'size':len(d),'sha256':hashlib.sha256(d).hexdigest(),'sections':tlvs})
 if re.search(rb'(?<![0-9])4520[1245](?![0-9])',d):
  hits.append({'path':path,'matches':[{'offset':m.start(),'text':d[max(0,m.start()-30):m.end()+80].decode(errors='replace')} for m in re.finditer(rb'(?<![0-9])4520[1245](?![0-9])',d)]})
expected={}
for line in fs.read(files['/mcf_ota.md5sum']['inode']).decode().splitlines():
 h,n=line.split(None,1);expected[n.strip()]=h
matched=[];unmatched=[]
for n,h in expected.items():
 candidates=[(path,r) for path,r in files.items() if (path.lstrip('/')==n or path.rsplit('/',1)[-1]==n)]
 vals=[path for path,r in candidates if hashlib.md5(fs.read(r['inode'])).hexdigest()==h]
 (matched if vals else unmatched).append({'name':n,'expected':h,'paths':vals})
report={'evidence_id':'NR5G-MCF-READONLY-001','fs_metadata':fs.meta,'ota_files':len(ota),'top_level_section_parse_failures':fail,'section_tag_layouts':dict(collections.Counter(','.join(s['tag'] for s in r['sections']) for r in ota)),'plaintext_vietnam_plmn_file_matches':hits,'manifest_entries':len(expected),'manifest_hash_matched':len(matched),'manifest_hash_unmatched':unmatched,'limitations':['Top-level tag/length boundaries only; encrypted/security context is not decoded','No active SIM/network/operator profile read','PLMN naming-table existence does not prove NR band allow/disable setting']}
(OUT/'mcf_layout_summary.json').write_text(json.dumps(report,indent=2));(OUT/'mcf_ota_section_inventory.json').write_text(json.dumps(ota,indent=2));print(json.dumps(report,indent=2)[:6000])


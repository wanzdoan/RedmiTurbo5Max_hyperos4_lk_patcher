from pathlib import Path
import json,re,hashlib,struct
ROOT=Path.cwd();OUT=ROOT/'fresh_analysis/nr5g_research/lk'
items={'h4_main':'fresh_analysis/container_chain/h4_original_00_lk.bin','h3_bl2_donor':'fresh_analysis/container_chain/h3_original_03_bl2_ext.bin','h4_bl2_reference':'fresh_analysis/container_chain/h4_original_03_bl2_ext.bin','h4_aee':'fresh_analysis/container_chain/h4_original_06_aee.bin','h4_lk_dtb':'fresh_analysis/container_chain/h4_original_09_lk_main_dtb.bin','h4_lk_dtbo':'fresh_analysis/container_chain/h4_original_12_lk_dtbo.bin'}
pat=re.compile(r'(?i)(?:\b5g\b|\bnr5g\b|\bNR\b|NR[_-](?:band|support|cap|enable)|(?:band|enable|support)[_-]NR|\bmcc\b|\bmnc\b|\brat\b|rat[_-]config|world[_-]?phone|countrycode|md_capability|ccci|modem|lte|md1[_-](?:support|type)|drdi)')
rows=[]
for name,p in items.items():
 d=(ROOT/p).read_bytes();ss=[dict(offset=hex(m.start()),text=m.group().decode()) for m in re.finditer(rb'[ -~]{5,}',d) if pat.search(m.group().decode())]
 rows.append(dict(name=name,path=p,size=len(d),sha256=hashlib.sha256(d).hexdigest(),hits=ss))
(OUT/'binary_string_inventory.json').write_text(json.dumps(rows,indent=2));print('\n'.join(f'{r["name"]}: {len(r["hits"])} matches' for r in rows));print('\n'.join(f'{r["name"]} {x}' for r in rows for x in r['hits'] if not re.search('ccci|modem|drdi',x['text'],re.I)))
# identity of the boot-confirmed hybrid against source members
p=ROOT/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img';d=p.read_bytes();members=[]
for m in re.finditer(bytes.fromhex('88168858'),d):
 o=m.start();n=struct.unpack_from('<I',d,o+4)[0];name=d[o+8:o+40].split(b'\0')[0].decode();body=d[o+512:o+512+n];members.append(dict(offset=hex(o),name=name,size=n,sha256=hashlib.sha256(body).hexdigest()))
(OUT/'hybrid_members.json').write_text(json.dumps(dict(path=str(p.relative_to(ROOT)),sha256=hashlib.sha256(d).hexdigest(),members=members),indent=2))

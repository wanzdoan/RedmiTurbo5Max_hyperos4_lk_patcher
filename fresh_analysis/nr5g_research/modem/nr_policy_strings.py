from pathlib import Path
import json,re
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');d=(base/'modem.img').read_bytes();out=Path(__file__).resolve().parent
pat={
'policy_symbols':r'(?i)(SBP[_A-Z0-9]*(?:NR_|_NR|5G|ENDC|NSA|SA_)|(?:NR|5G|ENDC|NSA)[_A-Z0-9]*(?:DISABLE|RESTRICT|FORBID|BLACK|WHITE|ROAM))',
'nr_plmn_gate':r'(?i)(?:NR|5G|ENDC|NSA).{0,120}(?:MCC|MNC|PLMN|FOREIGN|OVERSEA|COUNTRY)|(?:MCC|MNC|PLMN|FOREIGN|OVERSEA|COUNTRY).{0,120}(?:NR|5G|ENDC|NSA)',
'roam_gate':r'(?i)(?:(?:nr|5g|endc|nsa|sa_only).{0,80}(?:roam|disable|restrict|forbid|foreign))|(?:(?:roam|disable|restrict|forbid|foreign).{0,80}(?:nr_|5g|endc|nsa))',
'operator_names':r'(?i)Vietnam|4520[1245]|Viettel|Vinaphone|Mobifone'
}
rs={k:[] for k in pat};counts={k:0 for k in pat}
for m in re.finditer(rb'[ -~]{6,}',d):
 s=m.group().decode()
 if len(s)>600:continue
 for k,p in pat.items():
  if re.search(p,s):
   counts[k]+=1
   if len(rs[k])<500:rs[k].append({'offset':hex(m.start()),'text':s})
(out/'nr_policy_strings.json').write_text(json.dumps(dict(counts=counts,groups=rs),indent=2));print(counts)
for k,v in rs.items():
 print('\nGROUP',k);print('\n'.join(x['offset']+' '+x['text'] for x in v[:70]))

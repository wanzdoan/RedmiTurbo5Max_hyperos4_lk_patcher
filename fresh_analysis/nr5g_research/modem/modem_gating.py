from pathlib import Path
import re,json,collections,struct
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');out=Path(__file__).resolve().parent;d=(base/'modem.img').read_bytes()
patterns={
 'foreign_region':r'(?i)(foreign|oversea|china|chinese|domestic|country.?code|460|452|cn_sim|sim_cn)',
 'nr_gate':r'(?i)(?:(?:nr|5g|endc|sa|nsa).{0,55}(?:disable|enable|blacklist|white.?list|restrict|allow|forbid|roam|sbp|mcc|mnc|plmn))|(?:(?:disable|enable|blacklist|white.?list|restrict|allow|forbid|roam|mcc|mnc|plmn).{0,55}(?:nr|5g|endc|nsa))',
 'profile_select':r'(?i)(sbp.*(?:select|switch|plmn|mcc|mnc)|(?:select|switch).*(?:sbp|mcf|opota|nwota)|(?:mcf|opota|nwota).*(?:sim|plmn|mcc|mnc|profile))',
 'config_format':r'(?i)(mcf.*(?:header|encrypt|decrypt|security|check)|header.*mcf)'
}
result={k:[] for k in patterns}
for m in re.finditer(rb'[ -~]{5,}',d):
 s=m.group().decode()
 for k,pat in patterns.items():
  if re.search(pat,s) and len(result[k])<300:result[k].append({'offset':hex(m.start()),'text':s[:800]})
for k,v in result.items():print(k,len(v));print('\n'.join(x['offset']+' '+x['text'] for x in v[:25]))
(out/'modem_gating_strings.json').write_text(json.dumps(result,indent=2))
records=[]
for m in re.finditer(bytes.fromhex('88168858'),d):
 o=m.start();name=d[o+8:o+40].split(b'\0')[0];sz=struct.unpack_from('<I',d,o+4)[0]
 if all(32<=c<127 for c in name) and name and o+512+sz<=len(d):records.append(dict(offset=hex(o),name=name.decode(),size=sz,header_hex=d[o:o+128].hex()))
print('headers',records);(out/'modem_mtk_records.json').write_text(json.dumps(records,indent=2))

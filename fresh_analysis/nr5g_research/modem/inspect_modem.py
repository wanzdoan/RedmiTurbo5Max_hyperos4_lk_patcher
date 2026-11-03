from pathlib import Path
import hashlib,struct,re,json,collections
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');out=Path('fresh_analysis/nr5g_research/modem'); inv={}
needles={'plmn_vietnam':rb'(?<![0-9])452[0-9]{2,3}(?![0-9])','country_vietnam':rb'Vietnam|VIETNAM|Viet Nam|Viettel|VINAPHONE|VinaPhone|MobiFone|MOBIFONE','nr_rat':rb'ENDC|EN-DC|NR5G|NR_SA|NR_NSA|SA_ONLY|NSA_ONLY|NR_BAND|NR BAND|nr_band|NR band|NR_DISABLE|nr_disable','config_names':rb'(?i)MCF|carrier_policy|operator_policy|rat_config|band_config|band_cap|mccmnc|countrycode'}
for name in ['modem.img','mcf_ota.img','countrycode.img']:
 d=(base/name).read_bytes();item={'path':str(base/name),'size':len(d),'sha256':hashlib.sha256(d).hexdigest(),'first256_hex':d[:256].hex(),'last128_hex':d[-128:].hex(),'ext4_magic_at1080':d[1080:1082].hex(),'sparse_magic':d[:4].hex()};hitgroups={}
 for key,pat in needles.items():
  hits=[];count=0
  for m in re.finditer(pat,d):
   count+=1
   if len(hits)<120:
    lo=max(0,m.start()-100);hi=min(len(d),m.end()+180);s=d[lo:hi];context=''.join(chr(c) if 32<=c<127 else '.' for c in s);hits.append({'offset':m.start(),'match':m.group().decode(errors='replace'),'context_ascii':context})
  hitgroups[key]={'count':count,'first_hits':hits}
 item['searches']=hitgroups;inv[name]=item
 print(name,item['size'],item['sha256'],d[:80],{k:x['count'] for k,x in hitgroups.items()})
(out/'inventory.json').write_text(json.dumps(inv,indent=2))

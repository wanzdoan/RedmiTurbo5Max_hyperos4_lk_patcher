from pathlib import Path
import json,re,hashlib
p=Path('fresh_analysis/nr5g_research/runtime_policy_logs.py');s=p.read_text(encoding='utf-8-sig');s=s.replace("if re.search(r'imsi|imei|iccid|msisdn|subscriberid|line1number',line,re.I):continue", "if re.search(r'imsi|imei|iccid|msisdn|subscriberid|line1number|mServiceState=|mNetworkRegistrationInfos=|onServiceStateChanged|updateCarrierDisplayName|ServiceState\\s*:',line,re.I):continue")
s=s.replace("return s\nresults={}","s=re.sub(r'(?i)(?:mTac|mCi|mNci|mPci|mEarfcn|mNrarfcn|mChannelNumber)\\s*[=:]\\s*[^ ,}\\]]+','[cell-field redacted]',s)\n return s\nresults={}")
p.write_text(s,encoding='utf-8')
p=Path('fresh_analysis/nr5g_research/runtime/radio_policy_redacted.json');d=json.loads(p.read_text())
for k in ('radio','main'):
 d[k]['matching_lines']=[s for s in d[k]['matching_lines'] if not re.search(r'mServiceState=|mNetworkRegistrationInfos=|onServiceStateChanged|updateCarrierDisplayName|ServiceState\s*:',s)]
 d[k]['retained_count']=len(d[k]['matching_lines'])
p.write_text(json.dumps(d,indent=2))
base=Path('fresh_analysis/nr5g_research/device_static');manifest=[]
for f in sorted(base.iterdir()):
 if f.is_file() and f.suffix in ('.apk','.jar','.so'):manifest.append({'file':f.name,'size':f.stat().st_size,'sha256':hashlib.sha256(f.read_bytes()).hexdigest(),'source':'Read-only ADB pull of shipped system/vendor code; no user-data files'})
(base/'complete_manifest.json').write_text(json.dumps(manifest,indent=2))
files=[f for f in Path('fresh_analysis/nr5g_research').rglob('*') if f.is_file() and '__pycache__' not in f.parts]
print('NR research',len(files),'files',sum(f.stat().st_size for f in files),'bytes');print('Largest',[(str(f),f.stat().st_size) for f in sorted(files,key=lambda x:x.stat().st_size,reverse=True)[:5]])

from pathlib import Path
import subprocess,re,json
OUT=Path('fresh_analysis/nr5g_research/runtime')
def read(cmd):return subprocess.run(['adb','shell',cmd],capture_output=True,timeout=45).stdout.decode(errors='replace')
def redact(s):
 s=re.sub(r'\b\d{9,}\b','[long-number redacted]',s)
 s=re.sub(r'(?i)(?:imsi|imei|iccid|msisdn|subscriberid|line1number)\s*[=:]\s*[^ ,}\]]+','[identifier redacted]',s)
 s=re.sub(r'\b(?:\d{1,3}\.){3}\d{1,3}\b','[IP redacted]',s)
 s=re.sub(r'(?i)(?:mTac|mCi|mNci|mPci|mEarfcn|mNrarfcn|mChannelNumber)\s*[=:]\s*[^ ,}\]]+','[cell-field redacted]',s)
 return s
results={}
for buffer in ('radio','main'):
 text=read('logcat -b '+buffer+' -d -t 6000');lines=[]
 for line in text.splitlines():
  if not re.search(r'E5GOPT|requestDisableNr|checkNrCapability|setAllowedNetwork|setPreferredNetwork|mIsCarrierNrSupported|NrSwitch|FiveGManager|FiveGCloudController|NR.*(?:disable|restrict|support)|(?:disable|restrict).*NR|mcf.*(?:select|profile)|SBP',line,re.I):continue
  if re.search(r'imsi|imei|iccid|msisdn|subscriberid|line1number|mServiceState=|mNetworkRegistrationInfos=|onServiceStateChanged|updateCarrierDisplayName|ServiceState\s*:',line,re.I):continue
  lines.append(redact(line))
 results[buffer]={'matching_lines':lines[-300:],'count':len(lines)}
# Query only non-personal columns; no full subscription database dump.
results['network_type_reasons']=redact(read('content query --uri content://telephony/siminfo --projection _id:allowed_network_types_for_reasons:carrier_id:sim_id'))
results['network_mode_settings']=redact(read('settings get global preferred_network_mode'))
text=read('pidof mtkfusionrild');pids=[p for p in text.split() if p.isdigit()]
libs=[]
for pid in pids:
 maps=read('cat /proc/'+pid+'/maps')
 for line in maps.splitlines():
  if '/' not in line:continue
  path=line.split()[-1]
  if re.search(r'ril|mcf|radio|modem',path,re.I) and path not in libs:libs.append(path)
results['rild_mapped_libraries']=libs
(OUT/'radio_policy_redacted.json').write_text(json.dumps(results,indent=2),encoding='utf-8')
print(json.dumps({'logs':{k:{'count':v['count'],'sample':v['matching_lines'][-12:]} for k,v in results.items() if isinstance(v,dict)},'network_type_reasons':results['network_type_reasons'],'network_mode_settings':results['network_mode_settings'],'rild_libraries':libs},indent=2))

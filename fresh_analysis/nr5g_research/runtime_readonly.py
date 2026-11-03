"""Read-only telephony snapshot. Selected/redacted values only; no device writes or raw log persistence."""
from pathlib import Path
import subprocess,json,re,datetime,hashlib
ROOT=Path(__file__).resolve().parents[2];OUT=ROOT/'fresh_analysis/nr5g_research/runtime'
rows=subprocess.check_output(['adb','devices'],text=True).splitlines()[1:];ids=[r.split()[0] for r in rows if '\tdevice' in r];assert len(ids)==1;serial=ids[0]
def shell(cmd):
 r=subprocess.run(['adb','-s',serial,'shell',cmd],capture_output=True,timeout=50)
 return r.returncode,r.stdout.decode('utf-8',errors='replace')
def scrub(s):
 s=re.sub(r'(?i)(?:imsi|imei|iccid|msisdn|subscriberid|line1number|serialnumber)\s*[=:]\s*[^ ,}\]]+', '[identifier redacted]',s)
 s=re.sub(r'\b\d{9,}\b','[long-number redacted]',s)
 s=re.sub(r'(?i)(?:mTac|mCi|mNci|mPci|mEarfcn|mNrarfcn|mChannelNumber|tac|cellId|nci|pci)\s*[=:]\s*[^ ,}\]]+','[cell-field redacted]',s)
 return s
rc,allprops=shell('getprop');props={}
for line in allprops.splitlines():
 m=re.match(r'\[([^]]+)\]: \[(.*)\]',line)
 if not m:continue
 k,v=m.groups()
 if re.search(r'imsi|imei|iccid|msisdn|serial|subscriber|meid|phone.?number',k,re.I):continue
 if k in ['ro.product.device','ro.build.version.incremental','ro.boot.ptcountrycode','ro.vendor.radio.build_region','ro.vendor.radio.build_profile','gsm.operator.numeric','gsm.sim.operator.numeric','gsm.network.type','gsm.version.baseband'] or re.search(r'(?:nr5g|nr_mode|nr_switch|nrscg|nsa|smart5g|rat_config|ratconfig|preferred_network|mtk_mcf|sbp|mcf|world_mode)',k,re.I):props[k]=scrub(v)
report={'evidence_id':'NR5G-RUNTIME-001','date_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'read_only':True,'device_serial_persisted':False,'properties':props,'dumps':{}}
patterns={
 'telephony.registry':r'mPhoneId|mDataNetworkType|mVoiceNetworkType|mDataConnectionState|mDisplayInfo|mAllowedNetwork|mNr|nrState|isNr|isDcNrRestricted|isEnDcAvailable|mPhysicalChannelConfigs',
 'phone':r'allowedNetworkTypes|AllowedNetworkTypes|mAllowedNetworkTypes|mPreferredNetworkType|mRadioAccessFamily|mRadioCapability|mNr|NrState|nrState|NR_STATE|NrMode|nrMode|mIsNr|5g|5G|ENDC|endc|NrAvailable|mNrSupported|nr.*restrict|restrict.*nr|mcf|MCF|sbp|SBP|mNetworkTypeBitmask',
 'carrier_config':r'carrier_nr_availabilities|nr_.*(bool|int|string)|5g|5G|nr.*(enable|disable|allow|restrict)|ENDC|endc|carrier_config_applied',
 'telephony.mtkregistry':r'NR|nr|NetworkType|ServiceState',
 'xm.radio.cdc':r'5[Gg]|\bNR\b|\bnr\b|country|region|452|460|rule|restrict|allow|deny'
}
for service,pat in patterns.items():
 try:
  rc,txt=shell('dumpsys '+service);selected=[]
  for line in txt.splitlines():
   if not re.search(pat,line):continue
   if re.search(r'imsi|imei|iccid|msisdn|subscriberId|line1Number',line,re.I):continue
   clean=scrub(line)
   if len(clean)>2200:clean=clean[:2200]+' [truncated]'
   selected.append(clean)
  report['dumps'][service]={'status':rc,'matched_lines':selected[:500],'total_matched':len(selected)}
 except Exception as ex:report['dumps'][service]={'error':str(ex)}
# No raw telephony dumps/logs are written.
(OUT/'snapshot_redacted.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps({'properties':props,'dumps':{k:{'status':v.get('status'),'count':v.get('total_matched'),'sample':v.get('matched_lines',[])[:12]} for k,v in report['dumps'].items()}},indent=2))

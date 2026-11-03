from pathlib import Path
import os,gzip,re,json,subprocess
raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103';out=Path('fresh_analysis/nr5g_research/crash_review');result={}
for name in ('boot_201512.gz','boot_221947.gz'):
 text=gzip.decompress((raw/name).read_bytes()).decode(errors='replace');rows=[]
 for i,line in enumerate(text.splitlines()):
  if 'com.mbmobile' not in line:continue
  if not re.search(r'Start proc|StartProcess|am_proc_start|ProcessRecord|died|crash|Force stopping|Fatal|tombstone|WIN DEATH',line,re.I):continue
  if re.search('imei|imsi|iccid|msisdn|subscriber|account|token|password',line,re.I):continue
  line=re.sub(r'\b(?:\d{1,3}\.){3}\d{1,3}\b','[IP redacted]',line)
  rows.append({'line':i+1,'text':line[:1800]})
 result[name]=rows
# Current process identity only, no account or application data.
text=subprocess.check_output(['adb','shell','ps','-A','-o','PID,TID,NAME']).decode(errors='replace');result['current_bank_process_rows']=[s for s in text.splitlines() if 'com.mbmobile' in s]
(out/'bank_process_timeline.json').write_text(json.dumps(result,indent=2))
print(json.dumps(result,indent=2))

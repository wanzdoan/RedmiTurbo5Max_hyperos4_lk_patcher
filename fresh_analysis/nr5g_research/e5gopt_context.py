from pathlib import Path
import subprocess,re,json
raw=subprocess.check_output(['adb','shell','logcat -b radio -d -t 12000']).decode(errors='replace').splitlines();out=[]
for i,s in enumerate(raw):
 if 'AT> AT+E5GOPT=' not in s:continue
 status=[]
 for x in raw[i+1:i+40]:
  if re.search(r'AT<\s+(?:OK|ERROR|\+CME ERROR|\+CMS ERROR)',x):status.append(re.sub(r'\b\d{9,}\b','[long-number redacted]',x))
 out.append({'command':re.sub(r'\b\d{9,}\b','[long-number redacted]',s),'nearby_status_not_transaction_proof':status[:5]})
Path('fresh_analysis/nr5g_research/runtime/e5gopt_response_context.json').write_text(json.dumps(out,indent=2));print(json.dumps(out[-8:],indent=2))

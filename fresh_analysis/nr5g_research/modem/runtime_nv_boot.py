import subprocess,re,json
from pathlib import Path
out=Path(__file__).resolve().parent
p='/mnt/vendor/nvdata/md/nv_boot_trace';d=subprocess.check_output(['adb','exec-out','su','-c','cat '+p]);hits=[]
for m in re.finditer(rb'[ -~]{6,}',d):
 s=m.group().decode()
 if re.search(r'(?i)(MCF|OPOTA|NWOTA|SBP|SP80|SP81|NR_BAND|NRRC)',s):
  # Keep only nonpersonal boot/config lines, reject subscriber/unique IDs.
  if re.search(r'(?i)(IMSI|IMEI|ICCID|MSISDN|serial|subscriber|phone.number)',s):continue
  s=re.sub(r'(?<![A-Za-z0-9])\d{10,}(?![A-Za-z0-9])','[long-number-redacted]',s)
  hits.append({'offset':hex(m.start()),'text':s[:900]})
report={'evidence_id':'NR5G-RUNTIME-NVBOOT-001','source':p,'size':len(d),'filtered_hits':hits[:180],'total_focused_hits':len(hits),'limitations':['Only printable substrings, not decoded binary event stream','No raw trace persisted; subscriber-sensitive lines omitted']}
(out/'runtime_nv_boot_config_trace.json').write_text(json.dumps(report,indent=2));print('size',len(d),'hits',len(hits));print('\n'.join(h['offset']+' '+h['text'] for h in hits[:50]))

from pathlib import Path
import os,re,gzip,json,hashlib
raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103';out=Path('fresh_analysis/nr5g_research/crash_review');report={}
pat=re.compile(r'(?i)panic|watchdog|kernel bug|fatal|assert|surfaceflinger|graphics.composer|modem.*exception|md1.*exception|ccci.*exception|gpu.*fault|iommu.*fault|SError|longkey|ESD.*fail|ESD.*error|DISP.*(?:hang|timeout|fail)|cmdq.*(?:timeout|error)')
for p in raw.iterdir():
 if not p.is_file():continue
 d=p.read_bytes();nz=next((i for i,b in enumerate(d) if b),None);info={'bytes':len(d),'sha256':hashlib.sha256(d).hexdigest(),'first_nonzero_offset':nz,'zero_fraction':round(d.count(0)/max(len(d),1),4)}
 if p.suffix=='.gz':
  try:d=gzip.decompress(d);info['gzip_decoded_bytes']=len(d)
  except Exception as ex:info['gzip_error']=str(ex)
 text=d.decode(errors='replace');hits=[]
 for i,s in enumerate(text.splitlines()):
  if not pat.search(s):continue
  if re.search(r'imsi|imei|iccid|msisdn|serialno|serialnumber|androidboot.serial',s,re.I):continue
  s=re.sub(r'\b\d{9,}\b','[long-number redacted]',s)
  hits.append({'line':i+1,'text':s[:1200]})
 info['selected_hits']=hits[:200];info['total_selected_hits']=len(hits)
 info['tombstone_markers']=[{'marker':x.decode(),'offset':d.find(x)} for x in (b'Build fingerprint',b'Cmdline:',b'Abort message',b'backtrace:',b'*** ***',b'surfaceflinger',b'graphics.composer',b'signal ')]
 report[p.name]=info
(out/'raw_log_inventory.json').write_text(json.dumps(report,indent=2))
for name,info in report.items():
 print(name, {k:v for k,v in info.items() if k not in ('selected_hits','tombstone_markers')})
 print('markers',info['tombstone_markers'])
 for h in info['selected_hits'][-18:]:print(h['line'],h['text'].encode('ascii','backslashreplace').decode())

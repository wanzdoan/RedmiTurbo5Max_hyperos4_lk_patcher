from pathlib import Path
import subprocess,json,re,os,datetime
raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103';out=Path('fresh_analysis/nr5g_research/crash_review')
for tag in ('SYSTEM_TOMBSTONE','SYSTEM_LAST_KMSG','data_app_native_crash'):
 r=subprocess.run(['adb','shell','dumpsys','dropbox','--print','2026-11-03',tag],capture_output=True,timeout=60);d=r.stdout;(raw/(tag+'.txt')).write_bytes(d)
 s=d.decode(errors='replace');lines=s.splitlines();hits=[]
 for i,line in enumerate(lines):
  if not re.search(r'(?i)^20\d\d-|Build fingerprint|Timestamp:|Process uptime|Cmdline:|pid:|signal \d|Abort message|backtrace:|^\s*#\d+|Kernel panic|Kernel BUG|watchdog.*(?:bite|expire|hang)|GPU.*(?:fault|hang)|(?:CMDQ|DISP|DRM).*(?:timeout|hang|esd.*fail)|CCCI.*(?:exception|assert)|modem.*(?:crash|exception)|^Process:|^Package:|^Subject:',line):continue
  if re.search(r'imsi|imei|iccid|msisdn|serialno|serialnumber|boot.serial',line,re.I):continue
  hits.append({'line':i+1,'text':line[:1600]})
 (out/(tag+'_selected.json')).write_text(json.dumps({'status':r.returncode,'raw_characters':len(s),'selected':hits[:260],'total_selected':len(hits)},indent=2))
 print(tag,'bytes',len(d),'hits',len(hits))
 for x in hits[-60:]:print(x['line'],x['text'].encode('ascii','backslashreplace').decode())

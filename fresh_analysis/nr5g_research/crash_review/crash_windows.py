from pathlib import Path
import os,re,json,subprocess,gzip
raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103';out=Path('fresh_analysis/nr5g_research/crash_review')
records={}
for name in ('SYSTEM_LAST_KMSG.txt','tombstone_31','data_app_native_crash.txt','SYSTEM_TOMBSTONE.txt'):
 d=(raw/name).read_bytes();s=d.decode(errors='replace');lines=s.splitlines();windows=[]
 needles=[i for i,x in enumerate(lines) if re.search(r'CPU:|Call trace|call trace|Backtrace|Tainted|BUG:|Kernel panic|watchdog|hung|segfault|synchronous|SERROR|SError|fatal signal',x,re.I)]
 for i in needles[-12:]:
  selection=[]
  for j in range(max(0,i-3),min(len(lines),i+22)):
   t=lines[j]
   if re.search('imei|imsi|iccid|msisdn|serialno|serialnumber',t,re.I):continue
   t=''.join(c if c in '\t' or ord(c)>=32 else '?' for c in t)
   selection.append({'line':j+1,'text':t[:800]})
  windows.append(selection)
 records[name]={'windows':windows,'prefix_after_header': [''.join(c if ord(c)>=32 else '?' for c in z)[:200] for z in lines[7:12]]}
rc=subprocess.run(['adb','shell','dmesg'],capture_output=True,timeout=30);text=rc.stdout.decode(errors='replace');(raw/'current_dmesg.txt').write_text(text,encoding='utf-8')
selected=[]
for i,s in enumerate(text.splitlines()):
 if not re.search(r'(?i)panic|BUG:|ESD.*(?:fail|error)|GPU.*(?:fault|hang)|IOMMU.*fault|SERROR|DISP.*(?:hang|timeout)|cmdq.*timeout|ccci.*(?:exception|assert)|modem.*(?:crash|exception)|ramoops.*(?:corrupt|ecc)|pstore.*(?:corrupt|ecc)',s):continue
 if re.search('imei|imsi|iccid|msisdn|serialno|serialnumber',s,re.I):continue
 selected.append({'line':i+1,'text':s[:1500]})
records['current_dmesg']={'status':rc.returncode,'selected':selected[:100],'total_selected':len(selected)}
(out/'crash_windows.json').write_text(json.dumps(records,ensure_ascii=False,indent=2),encoding='utf-8')
for name,row in records.items():
 print('\nFILE',name)
 for window in row.get('windows',[])[-3:]:
  for line in window:print(line['line'],line['text'].encode('ascii','backslashreplace').decode())
 for line in row.get('selected',[])[:12]:print(line['text'].encode('ascii','backslashreplace').decode())
 print('prefix',row.get('prefix_after_header'))

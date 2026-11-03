from pathlib import Path
import subprocess,re,json,os,gzip
out=Path('fresh_analysis/nr5g_research/crash_review');raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103'
def read(*args):return subprocess.run(['adb','shell',*args],capture_output=True,timeout=35).stdout.decode(errors='replace')
packages=read('pm','list','packages');bank=[s for s in packages.splitlines() if re.search(r'com\.mbmobile|mbbank|mb\.bank',s,re.I)]
modules=[]
paths=read('find','/data/adb/modules','-maxdepth','2','-name','module.prop').splitlines()
for path in paths:
 if not re.fullmatch(r'/data/adb/modules/[A-Za-z0-9_.-]+/module.prop',path):continue
 text=read('cat',path);fields={}
 for s in text.splitlines():
  if '=' in s:
   k,v=s.split('=',1)
   if k in ('id','name','version','author','description'):fields[k]=v
 fields['directory']=path.split('/')[-2];fields['disabled']=bool(read('ls',path.rsplit('/',1)[0]+'/disable').strip());modules.append(fields)
(out/'root_module_inventory.json').write_text(json.dumps(modules,ensure_ascii=False,indent=2),encoding='utf-8')
report={'installed_bank_packages':bank,'bank_version_fields':[],'artifact_app_marker_hits':{}}
for row in bank:
 pkg=row.split(':',1)[1];text=read('dumpsys','package',pkg)
 report['bank_version_fields'] += [s.strip() for s in text.splitlines() if re.search(r'versionName=|versionCode=|lastUpdateTime=|firstInstallTime=',s)]
for name in ('tombstone_31','data_app_native_crash.txt','boot_221947.gz','SYSTEM_LAST_KMSG.txt'):
 data=(raw/name).read_bytes()
 if name.endswith('.gz'):
  try:data=gzip.decompress(data)
  except Exception:pass
 hits=[]
 for token in (b'com.mbmobile',b'com.mbbank',b'libmbbank',b'libshield',b'libDexHelper',b'libjiagu',b'libtersafe',b'ovl_encode_real_fh',b'ovl_encode_fh',b'exportfs_encode_inode_fh',b'show_fdinfo',b'fsnotify'):
  positions=[m.start() for m in re.finditer(re.escape(token),data)]
  if positions:hits.append({'marker':token.decode(),'count':len(positions),'first_offsets':positions[:5]})
 report['artifact_app_marker_hits'][name]=hits
(out/'bank_and_stack_markers.json').write_text(json.dumps(report,indent=2))
print(json.dumps({'bank':report['installed_bank_packages'],'versions':report['bank_version_fields'],'markers':report['artifact_app_marker_hits'],'modules':[{'id':m.get('id'), 'name':m.get('name'),'disabled':m['disabled']} for m in modules]},ensure_ascii=True,indent=2))

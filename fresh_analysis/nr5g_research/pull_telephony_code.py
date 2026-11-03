"""Read-only ADB pull of shipped telephony code. Does not collect user data or write device settings."""
from pathlib import Path
import subprocess,json,hashlib
OUT=Path(__file__).resolve().parent/'device_static'
def adb(*args,timeout=60):return subprocess.run(['adb',*args],capture_output=True,timeout=timeout)
ids=[s.split()[0] for s in adb('devices').stdout.decode().splitlines()[1:] if '\tdevice' in s]
assert len(ids)==1,'Need exactly one authorized ADB device'
serial=ids[0]
def run(*args):return adb('-s',serial,*args)
assert run('shell','getprop','ro.product.device').stdout.strip()==b'dash'
files=['/vendor/lib64/libmtk-ril.so','/vendor/lib64/libmtkrilutils.so','/vendor/lib64/libmtkmipc-ril.so','/vendor/lib64/librilfusion.so','/system/framework/telephony-common.jar']
files += ['/system_ext/framework/'+x+'.jar' for x in ('mediatek-telephony-common','mediatek-telephony-base','xiaomi-telephony-common','miui-telephony-common','hyper-telephony-common','hyper-telephony-common-ext','xm-telephony-base','xm-telephony-common')]
manifest=[]
for path in files:
 dest=OUT/Path(path).name;r=run('pull',path,str(dest));row={'device_path':path,'status':r.returncode}
 if r.returncode==0:
  d=dest.read_bytes();row.update(local_file=dest.name,size=len(d),sha256=hashlib.sha256(d).hexdigest())
 else:row['error']=r.stderr.decode(errors='replace')[:500]
 manifest.append(row)
(OUT/'manifest.json').write_text(json.dumps(manifest,indent=2))
print(json.dumps([{'file':r['device_path'],'status':r['status'],'size':r.get('size')} for r in manifest],indent=2))

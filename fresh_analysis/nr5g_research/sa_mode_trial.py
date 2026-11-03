"""Optional reversible SA trial via the ROM's own secret-code receiver.
Default is inspect-only. --apply/--rollback modify a single Settings.System key
and ask the existing telephony receiver to refresh SA/NSA operation. Never flashes
firmware, writes NVRAM files or sends raw AT commands. Run only with user approval.
"""
from pathlib import Path
import subprocess,json,argparse,time,datetime
KEY='5g_sa_mode_disabled';ACTION='android.provider.Telephony.SECRET_CODE';CODE='7282583'
BASE=Path(__file__).resolve().parent/'runtime';BACKUP=BASE/'sa_trial_backup.json'
def adb(*args):
 r=subprocess.run(['adb',*args],capture_output=True,timeout=30)
 if r.returncode:raise RuntimeError(r.stderr.decode(errors='replace')[:400])
 return r.stdout.decode(errors='replace').strip()
def check_device():
 ids=[s.split()[0] for s in adb('devices').splitlines()[1:] if '\tdevice' in s]
 if len(ids)!=1:raise RuntimeError('Need one authorized device')
 if adb('shell','getprop','ro.product.device')!='dash':raise RuntimeError('Unexpected device')
 if adb('shell','getprop','ro.build.version.incremental')!='OS4.0.3.0.XPLCNXM':raise RuntimeError('Different ROM; review receiver code first')
 if not adb('shell','id').startswith('uid=0'):raise RuntimeError('Root ADB expected for this reviewed path')
def state():return adb('shell','settings','get','system',KEY)
def normalized(x):return '-1' if x=='null' else x
def cycle_to(target):
 cycle={'-1':'1','1':'0','0':'-1'}
 for _ in range(3):
  old=normalized(state())
  if old==target:return
  if old not in cycle:raise RuntimeError('Unexpected setting '+old)
  adb('shell','am','broadcast','--user','0','-a',ACTION,'-d','android_secret_code://'+CODE,'-p','com.android.phone')
  # Verify the receiver changed precisely the expected key before another event.
  expected=cycle[old]
  for poll in range(10):
   new=state()
   if new==expected:break
   time.sleep(0.2)
  else:raise RuntimeError('Receiver did not produce expected transition; stop and inspect')
 if normalized(state())!=target:raise RuntimeError('Could not reach requested setting')
def main():
 ap=argparse.ArgumentParser();op=ap.add_mutually_exclusive_group();op.add_argument('--apply',action='store_true');op.add_argument('--rollback',action='store_true');args=ap.parse_args();check_device();current=state()
 if not args.apply and not args.rollback:
  print(json.dumps({'mode':'INSPECT_ONLY','key':KEY,'current':current,'apply_target':'0 (override SA-disable=false)','receiver':ACTION,'secret_code':CODE,'rollback_backup':str(BACKUP),'no_settings_changed':True},indent=2));return
 BASE.mkdir(exist_ok=True)
 if args.apply:
  if BACKUP.exists():raise RuntimeError('Backup exists; inspect or rollback previous trial first')
  if normalized(current) not in ('-1','0','1'):raise RuntimeError('Unsupported existing value')
  backup={'recorded_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'rom':'OS4.0.3.0.XPLCNXM','device':'dash','key':KEY,'original_value':current,'original_absent':current=='null','action':'SA override through built-in receiver; Actual mode depends on ro.vendor.radio.5g: value2 selects SA-only commands; inspect logs.'}
  BACKUP.write_text(json.dumps(backup,indent=2));cycle_to('0');print('SA override now 0. This does not prove NR registration; collect logs/state next.')
 else:
  b=json.loads(BACKUP.read_text());assert b['key']==KEY and b['device']=='dash'
  cycle_to(normalized(b['original_value']))
  if b['original_absent']:adb('shell','settings','delete','system',KEY)
  final=state();assert final==b['original_value'],(final,b['original_value'])
  print('Original setting restored. Backup retained for audit.')
if __name__=='__main__':main()

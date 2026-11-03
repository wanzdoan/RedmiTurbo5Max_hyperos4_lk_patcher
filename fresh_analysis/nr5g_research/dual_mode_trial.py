"""Reversible user-approved radio mode test. Inspect-only unless --apply/--rollback.
Temporarily change the runtime property used by Phone's SA-only branch; no boot
image/module installation, persistent property storage, modem firmware/NVRAM writes
or raw AT commands. The ROM itself chooses radio commands after its built-in event.
"""
from pathlib import Path
import argparse,json,datetime
from sa_mode_trial import adb,check_device,state,normalized,cycle_to,KEY
OUT=Path(__file__).resolve().parent/'runtime';BACKUP=OUT/'dual_mode_trial_backup.json'
PROP='ro.vendor.radio.5g';TOOL='/data/adb/ksu/bin/resetprop'
def prop():return adb('shell','getprop',PROP)
def main():
 ap=argparse.ArgumentParser();g=ap.add_mutually_exclusive_group();g.add_argument('--apply',action='store_true');g.add_argument('--rollback',action='store_true');a=ap.parse_args();check_device()
 if not a.apply and not a.rollback:
  print(json.dumps({'mode':'INSPECT_ONLY','property':PROP,'current_property':prop(),'current_sa_setting':state(),'trial_property':'3','trial_sa_setting':'0','backup':str(BACKUP)},indent=2));return
 if a.apply:
  if BACKUP.exists():raise RuntimeError('Backup exists; review prior trial first')
  before={'recorded_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'property':PROP,'original_property':prop(),'setting':KEY,'original_setting':state(),'original_setting_absent':state()=='null','scope':'runtime memory property and built-in SA-setting override, user approved network trial'}
  if before['original_property']!='2':raise RuntimeError('Expected reviewed initial property2')
  BACKUP.write_text(json.dumps(before,indent=2))
  try:
   adb('shell',TOOL,'-n',PROP,'3');assert prop()=='3'
   cycle_to('0');print('Trial active: runtime property3, SA override0. Check modem status and registration; not a 5G success claim.')
  except Exception:
   adb('shell',TOOL,'-n',PROP,before['original_property']);raise
 else:
  b=json.loads(BACKUP.read_text());assert b['property']==PROP and b['setting']==KEY
  adb('shell',TOOL,'-n',PROP,b['original_property']);cycle_to(normalized(b['original_setting']))
  if b['original_setting_absent']:adb('shell','settings','delete','system',KEY)
  assert prop()==b['original_property'] and state()==b['original_setting']
  print('Both runtime property and original setting restored; backup retained.')
if __name__=='__main__':main()

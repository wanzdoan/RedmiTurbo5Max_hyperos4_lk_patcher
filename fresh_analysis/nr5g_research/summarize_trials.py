from pathlib import Path
import subprocess,re,json,datetime
base=Path('fresh_analysis/nr5g_research/runtime');raw=subprocess.check_output(['adb','shell','logcat -b radio -d -t 16000']).decode(errors='replace').splitlines();pending={};rows=[]
for line in raw:
 m=re.search(r'\[([01])\] AT([<>])\s+(.*?)\s+\((RIL_CMD[^ ,)]+)',line)
 if not m:continue
 slot,way,data,channel=m.groups();key=(slot,channel)
 if way=='>':
  pending[key]={'slot':int(slot),'channel':channel,'command':data,'time':line[:18]} if data.startswith('AT+E5GOPT') else None
 elif data in ('OK','ERROR') or data.startswith(('+CME ERROR','+CMS ERROR')):
  item=pending.get(key)
  if item:rows.append(dict(item,status=data));pending[key]=None
 elif pending.get(key) and data.startswith('+E5GOPT:'):pending[key]['response']=data
report={'method':'Track next AT terminal status on same reader channel and slot; an intervening command resets pending association','transactions':rows,'read_only_log_parse':True}
(base/'e5gopt_transactions.json').write_text(json.dumps(report,indent=2))
result={'trial':'temporary runtime SA+NSA branch test','user_approval':'Explicit approval for enabling/checking network mode, followed by continue instruction','initial':{'property':'2','sa_setting':'null'},'trial':{'property':'3','sa_setting':'0'},'observed_commands':{'AT+E5GOPT=5':'ERROR','AT+E5GOPT=7,2':'ERROR'},'observed_query_after_rejection':'+E5GOPT: 1 on both slots','result':'No NR registration; Android branch changed but radio/modem path rejected enabling NSA','rollback':{'property_restored':'2','sa_setting_restored':'null'},'scope':'No firmware flash, no manual NVRAM write, no persistent root module installed; requests sent by existing phone app after approved built-in event','transactions_reference':'e5gopt_transactions.json'}
(base/'dual_mode_trial_result.json').write_text(json.dumps(result,indent=2))
def get(k):return subprocess.check_output(['adb','shell','getprop',k]).decode().strip()
setting=subprocess.check_output(['adb','shell','settings','get','system','5g_sa_mode_disabled']).decode().strip()
current={'captured_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'ro.vendor.radio.5g':get('ro.vendor.radio.5g'),'5g_sa_mode_disabled':setting,'gsm.network.type':get('gsm.network.type'),'gsm.operator.numeric':get('gsm.operator.numeric')}
assert current['ro.vendor.radio.5g']=='2' and setting=='null'
(base/'restored_state.json').write_text(json.dumps(current,indent=2))
print(json.dumps({'current':current,'last_transactions':rows[-14:]},indent=2))

from pathlib import Path
import subprocess,json,re,datetime,hashlib
out=Path('fresh_analysis/nr5g_research/crash_review')
def read(*cmd):return subprocess.run(['adb','shell',*cmd],capture_output=True,timeout=40).stdout.decode(errors='replace').strip()
radio=read('logcat','-b','radio','-d','-t','7000');main=read('logcat','-b','main','-d','-t','4000')
selected=[]
for s in (radio+'\n'+main).splitlines():
 if not re.search(r'AT\+E5GOPT|mNsaStatus|mSaStatus|saModeSecretCodeDisabled|enabling SA|disabling NSA',s):continue
 if re.search('imei|imsi|iccid|msisdn|subscriber',s,re.I):continue
 selected.append(re.sub(r'\b\d{9,}\b','[long-number redacted]',s))
current={'recorded_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'boot_reason':read('getprop','ro.boot.bootreason'),'uptime':read('uptime'),'ro.vendor.radio.5g':read('getprop','ro.vendor.radio.5g'),'5g_sa_mode_disabled':read('settings','get','system','5g_sa_mode_disabled'),'preferred_network_mode':read('settings','get','global','preferred_network_mode'),'gsm.network.type':read('getprop','gsm.network.type'),'last_selected_radio_events':selected[-45:]}
assert current['ro.vendor.radio.5g']=='2' and current['5g_sa_mode_disabled']=='null'
(out/'rollback_and_current_state.json').write_text(json.dumps(current,indent=2))
image=Path('fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img');assert hashlib.sha256(image.read_bytes()).hexdigest()=='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
print(json.dumps({k:v for k,v in current.items() if k!='last_selected_radio_events'},indent=2));print('\n'.join(selected[-10:]))

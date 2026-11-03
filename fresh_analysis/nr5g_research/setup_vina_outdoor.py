from pathlib import Path
import sys,json,datetime,subprocess
root=Path('fresh_analysis/nr5g_research');sys.path.insert(0,str(root.resolve()));from sa_mode_trial import check_device,adb,state,cycle_to
check_device();prop=adb('shell','getprop','ro.vendor.radio.5g');assert prop=='2';assert state()=='null'
backup={'captured_utc':datetime.datetime.now(datetime.timezone.utc).isoformat(),'user_request':'Set VinaPhone to 5G so user can test outdoors','scope':'SA only using original device mode; NSA rejected in prior test','before':{'ro.vendor.radio.5g':prop,'5g_sa_mode_disabled':state(),'preferred_network_mode':adb('shell','settings','get','global','preferred_network_mode')},'rollback_command':'python fresh_analysis/nr5g_research/sa_mode_trial.py --rollback'}
(root/'runtime/vina_outdoor_setup.json').write_text(json.dumps(backup,indent=2));cycle_to('0')
backup['after']={'ro.vendor.radio.5g':adb('shell','getprop','ro.vendor.radio.5g'),'5g_sa_mode_disabled':state(),'preferred_network_mode':adb('shell','settings','get','global','preferred_network_mode'),'network_type_at_capture':adb('shell','getprop','gsm.network.type')};backup['outdoor_registration_result']='Pending user field test'
(root/'runtime/vina_outdoor_setup.json').write_text(json.dumps(backup,indent=2));print(json.dumps(backup,indent=2))

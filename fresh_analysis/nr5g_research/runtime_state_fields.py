from pathlib import Path
import subprocess,re,json
out=Path('fresh_analysis/nr5g_research/runtime');report={}
def read(cmd):return subprocess.check_output(['adb','shell',cmd],timeout=45).decode(errors='replace')
txt=read('dumpsys telephony.registry');blocks=re.split(r'(?=Phone Id=|mPhoneId=|Phone Id:)',txt)
sections=[]
for line in txt.splitlines():
 if 'mServiceState=' not in line:continue
 nets=[]
 for piece in line.split('NetworkRegistrationInfo{')[1:]:
  vals={}
  for key in ('domain','transportType','registrationState','networkRegistrationState','accessNetworkTechnology','nrState','rejectCause','isDcNrRestricted','isNrAvailable','isEnDcAvailable','mLteAttachResultType','mLteAttachExtraInfo'):
   m=re.search(r'\b'+key+r'\s*=\s*([^ ,}\]]+)',piece)
   if m:vals[key]=m.group(1)
  nets.append(vals)
 sections.append(nets)
report['registration_sections']=sections
report['allowed_network_values']=[]
for line in txt.splitlines():
 m=re.search(r'mAllowedNetworkTypeValue=(\d+)',line)
 if m:
  n=int(m.group(1));report['allowed_network_values'].append({'value':n,'hex':hex(n),'android_nr_bit_1_shift_19_present':bool(n&(1<<19))})
carrier=read('dumpsys carrier_config');context='';results=[]
for line in carrier.splitlines():
 if re.search(r'Phone Id|phoneId|subId|Subscription|Config for|Config from|CarrierConfigLoader|mConfigFrom',line):context=line.strip()
 if 'carrier_nr_availabilities_int_array' in line or 'carrier_config_applied_bool' in line:
  results.append({'context':context,'line':line.strip()})
report['carrier_nr_values']=results
for service in ('phone','telephony.mtkregistry','xm.radio.cdc'):
 text=read('dumpsys '+service)
 report[service+'_dump_shape']={'characters':len(text),'first_lines':text.splitlines()[:6]}
(out/'nr_state_structured.json').write_text(json.dumps(report,indent=2))
print(json.dumps(report,indent=2))

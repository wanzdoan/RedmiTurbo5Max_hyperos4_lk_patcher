from pathlib import Path
import xml.etree.ElementTree as ET,json,hashlib
O=Path('fresh_analysis/nr5g_research/android');R=O/'xiaomi_carrier_sources/resources'
ps=[R/'res/xml/vendor_miui.xml',R/'assets/default/default_v2.9.8.xml',R/'assets/device/dash/res/xml/vendor_device.xml'];keys=['carrier_sa_mode_available_bool','carrier_disable_sa_mode_bool','carrier_disable_vice_sa_mode_bool','carrier_5g_switch_available_bool','carrier_disable_5g_switch_bool','config_support_secret_code_for_sa_switch','config_support_secret_code_for_sa_value','config_is_show_vonr_switch','config_vonr_on_by_defalut_bool']
rows=[]
for p in ps:
 root=ET.parse(p).getroot();recs=[]
 for idx,e in enumerate(root.findall('carrier_config')):
  vals={n.attrib.get('name'):n.attrib.get('value') for n in e if n.tag=='boolean'}
  if not e.attrib or e.attrib.get('mcc')=='452':recs.append(dict(index=idx,filters=e.attrib,flags={k:v for k,v in vals.items() if k in keys},all_booleans=vals))
 rows.append(dict(path=str(p),sha256=hashlib.sha256(p.read_bytes()).hexdigest(),default_and_vietnam_records=recs))
result=dict(apk='fresh_analysis/nr5g_research/device_static/CarrierStudioCarrierConfig.apk',apk_sha256=hashlib.sha256(Path('fresh_analysis/nr5g_research/device_static/CarrierStudioCarrierConfig.apk').read_bytes()).hexdigest(),source_records=rows,derived_packaged_policy={'45204':{'carrier_sa_mode_available_bool':True,'carrier_disable_sa_mode_bool':False,'reason':'dash-specific452/04 override'},'45202':{'carrier_sa_mode_available_bool':False,'carrier_disable_sa_mode_bool':True,'reason':'commondefault; includeddefault+dash452/02 do not overrideSA'}},limits=['Not wholeAndroidCarrierConfig merging replay','opconfig/mi_ext/APEX/cloud may override; parent runtime reports corroborate these exact per-SIM values','No device writes or setting changes performed'])
(O/'xiaomi_carrier_sa_policy.json').write_text(json.dumps(result,indent=2));print(json.dumps(result['derived_packaged_policy'],indent=2))


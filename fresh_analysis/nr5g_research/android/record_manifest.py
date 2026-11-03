from pathlib import Path
import hashlib,json,re
O=Path('fresh_analysis/nr5g_research/android');I=Path('fresh_analysis/nr5g_research/device_static');paths=sorted(list(I.glob('*.jar'))+list(I.glob('*.apk')));rows=[dict(path=str(p),size=p.stat().st_size,sha256=hashlib.sha256(p.read_bytes()).hexdigest()) for p in paths];st=[]
for name in ['xiaomi','mtk','aosp','carrierconfig','teleservice','xiaomi_carrier']:
 p=O/(name+'_sources');log=O/('jadx_'+('carrier' if name=='carrierconfig' else name)+'.log'); txt=(log.read_text(encoding='utf-16') if log.read_bytes().startswith(bytes.fromhex('fffe')) else log.read_text(errors='replace')) if log.exists() else '';st.append(dict(group=name,java_files=len(list((p/'sources').rglob('*.java'))),log=str(log),error_counts=re.findall(r'finished with errors, count: (\d+)',txt)))
(O/'analysis_manifest.json').write_text(json.dumps(dict(inputs=rows,jadx='D:/Tools/jadx-1.5.6/bin/jadx.bat',exports=st,scope='offline shipped-code analysis; no agent-issued device commands or setting writes'),indent=2));print(json.dumps(st,indent=2))




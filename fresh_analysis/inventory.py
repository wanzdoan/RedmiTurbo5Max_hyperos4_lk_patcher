from pathlib import Path
import hashlib,json,re,struct
root=Path(__file__).resolve().parent.parent
paths=['lk_hyperos3_original/lk.img','lk_hyperos3_patched_unlockBL/dash.img','lk_hyperos4_original/lk.img']
a,b,c=[(root/p).read_bytes() for p in paths]
manifest=[]
for p,d in zip(paths,[a,b,c]):
 manifest.append(dict(path=p,size=len(d),sha256=hashlib.sha256(d).hexdigest(),head=d[:128].hex()))
 print(p,len(d),manifest[-1]['sha256'],d[:80].hex())
 strings=[dict(offset=m.start(),text=m.group().decode()) for m in re.finditer(rb'[ -~]{6,}',d)]
 (root/'fresh_analysis'/(Path(p).parent.name+'_strings.json')).write_text(json.dumps(strings,indent=2))
runs=[]
i=0
while i<min(len(a),len(b)):
 if a[i]==b[i]: i+=1; continue
 s=i
 while i<min(len(a),len(b)) and a[i]!=b[i]: i+=1
 runs.append(dict(offset=s,length=i-s,old=a[s:i].hex(),new=b[s:i].hex()))
print('diff runs',len(runs),'changed',sum(r['length'] for r in runs))
print(json.dumps(runs[:100],indent=2))
(root/'fresh_analysis/manifest.json').write_text(json.dumps(manifest,indent=2))
(root/'fresh_analysis/h3_diff.json').write_text(json.dumps(dict(runs=runs,old_tail=a[len(b):].hex(),new_tail=b[len(a):].hex()),indent=2))

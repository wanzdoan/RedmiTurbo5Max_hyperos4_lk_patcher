exec(open('fresh_analysis/container_chain/dtb_diff.py',encoding='utf-8-sig').read().split('for names,parser')[0])
def normalize(d):
 p=next(iter(overlays(d).values()));targets={}
 for k,v in p.items():
  if k.startswith('/__fixups__/'):
   for s in v.rstrip(b'\0').decode().split('\0'):
    path,prop,off=s.rsplit(':',2)
    if prop=='target':targets[path]=k.rsplit('/',1)[1]
 result={}
 for k,v in p.items():
  if k.startswith('/fragment@'):
   frag=k.split('/')[1];prefix='/'+frag;tag=targets.get(prefix,frag)
   rest=k[len(prefix):]
   if rest=='/target' or rest.endswith('/phandle'):continue
   result[tag+rest]=v
 return result,targets
p=Path('fresh_analysis/container_chain');a,ta=normalize((p/'h3_original_12_lk_dtbo.bin').read_bytes());b,tb=normalize((p/'h3_patched_12_lk_dtbo.bin').read_bytes());c=[]
for k in sorted(a.keys()|b.keys()):
 if a.get(k)!=b.get(k):c.append(dict(property=k,old=a.get(k,b'').hex(),new=b.get(k,b'').hex(),old_present=k in a,new_present=k in b))
(p/'lk_dtbo_h3_normalized_diff.json').write_text(json.dumps(c,indent=2))
print('targets old only',set(ta.values())-set(tb.values()),'new only',set(tb.values())-set(ta.values()))
print('normalized changes',len(c))
for x in c[:50]:print(x)

from pathlib import Path
import struct,json,hashlib
out=Path('fresh_analysis/container_chain')
def dtb(d):
 magic,total,so,st,rv,ver,last,cpu,szst,szs=struct.unpack_from('>10I',d)
 assert magic==0xd00dfeed
 strings=d[st:st+szst]; p=so; path=[]; props={}
 while p<so+szs:
  tok=struct.unpack_from('>I',d,p)[0];p+=4
  if tok==1:
   e=d.index(0,p);path.append(d[p:e].decode());p=(e+4)&~3
  elif tok==2:path.pop()
  elif tok==3:
   n,o=struct.unpack_from('>II',d,p);p+=8;name=strings[o:strings.index(0,o)].decode();v=d[p:p+n];p=(p+n+3)&~3;props['/'.join(path)+'/'+name]=v
  elif tok==4:pass
  elif tok==9:break
  else:raise ValueError((p,tok))
 return props
def overlays(d):
 h=struct.unpack_from('>8I',d);magic,total,hs,es,n,off,ps,ver=h; assert magic==0xd7b7ab1e
 result={}
 for i in range(n):
  e=struct.unpack_from('>8I',d,off+i*es);size,pos,id,rev,*custom=e
  result[(id,rev,tuple(custom))]=dtb(d[pos:pos+size])
 return result
for names,parser in [('lk_dtbo',overlays),('lk_main_dtb',lambda d:{'main':dtb(d)})]:
 a=parser((out/f'h3_original_{12 if names=="lk_dtbo" else 9:02d}_{names}.bin').read_bytes());b=parser((out/f'h3_patched_{12 if names=="lk_dtbo" else 9:02d}_{names}.bin').read_bytes());changes=[]
 for k in a.keys()|b.keys():
  aa=a.get(k,{});bb=b.get(k,{})
  for prop in aa.keys()|bb.keys():
   if aa.get(prop)!=bb.get(prop):changes.append({'entry':str(k),'property':prop,'old':aa.get(prop,b'').hex(),'new':bb.get(prop,b'').hex()})
 print(names,len(changes),changes)
 (out/f'{names}_h3_properties_diff.json').write_text(json.dumps(changes,indent=2))

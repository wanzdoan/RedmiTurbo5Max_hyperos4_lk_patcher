from pathlib import Path
import struct,json,re
OUT=Path('fresh_analysis/nr5g_research/lk')
def dtb(d):
 magic,total,so,st,rv,ver,last,cpu,szst,szs=struct.unpack_from('>10I',d);assert magic==0xd00dfeed
 strings=d[st:st+szst];p=so;path=[];props={}
 while p<so+szs:
  t=struct.unpack_from('>I',d,p)[0];p+=4
  if t==1:e=d.index(0,p);path.append(d[p:e].decode());p=(e+4)&~3
  elif t==2:path.pop()
  elif t==3:
   n,o=struct.unpack_from('>II',d,p);p+=8;name=strings[o:strings.index(0,o)].decode();props['/'.join(path)+'/'+name]=d[p:p+n];p=(p+n+3)&~3
  elif t==4:pass
  elif t==9:break
  else:raise ValueError(t)
 return props
def decode(v):
 if v and v.endswith(b'\0') and all(c==0 or 32<=c<127 for c in v):return {'strings':[s.decode() for s in v.rstrip(b'\0').split(b'\0')]}
 if len(v)%4==0 and len(v)<=128:return {'cells_be':[hex(x) for x in struct.unpack('>'+str(len(v)//4)+'I',v)]}
 return {'bytes':v.hex()[:256],'size':len(v)}
pat=re.compile(r'5g|nr5g|(?:^|[/,_-])(?:nr|rat|band|mcc|mnc)(?:$|[/,_-])|country|modem|mddriver|ccci|md1|drdi|world.?phone',re.I)
rows=[];counts={}
for name in ['h4_original_09_lk_main_dtb.bin','h4_original_12_lk_dtbo.bin']:
 d=Path('fresh_analysis/container_chain',name).read_bytes()
 if d[:4]==bytes.fromhex('d00dfeed'):entries=[('main',dtb(d))]
 else:
  magic,total,hs,es,n,off,ps,ver=struct.unpack_from('>8I',d);assert magic==0xd7b7ab1e;entries=[]
  for i in range(n):
   size,pos,id,rev,*custom=struct.unpack_from('>8I',d,off+i*es);entries.append((str((i,id,rev,custom)),dtb(d[pos:pos+size])))
 counts[name]=dict(entries=len(entries),properties=sum(len(p) for _,p in entries))
 for ent,props in entries:
  for p,v in props.items():
   if pat.search(p) or (v and v.endswith(b'\0') and pat.search(v.decode(errors='replace'))):rows.append(dict(artifact=name,entry=ent,path=p,hex=v.hex(),**decode(v)))
(OUT/'dtb_radio_properties.json').write_text(json.dumps(dict(counts=counts,matches=rows),indent=2));print(counts);print('\n'.join(str({k:v for k,v in x.items() if k!='hex'}) for x in rows))

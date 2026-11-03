"""Read Android v4 boot/vendor_boot headers and compressed newc ramdisks. No writes to inputs."""
from pathlib import Path
import struct,json,hashlib,gzip,re
import lz4.frame,lz4.block
ROOT=Path(__file__).resolve().parents[2]
OUT=ROOT/'fresh_analysis/nr5g_research/ramdisk'
ROM=Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images')
def u32(d,o):return struct.unpack_from('<I',d,o)[0]
def align(n,p):return (n+p-1)//p*p
def decompress(d):
 if d[:2]==b'\x1f\x8b':return gzip.decompress(d),'gzip'
 if d[:4]==bytes.fromhex('04224d18'):return lz4.frame.decompress(d),'lz4_frame'
 if d[:4]==bytes.fromhex('02214c18'):
  out=bytearray();o=4
  while o+4<=len(d):
   n=u32(d,o);o+=4
   if not n:break
   if o+n>len(d):raise ValueError('legacy LZ4 block out of range')
   out+=lz4.block.decompress(d[o:o+n],uncompressed_size=8*1024*1024);o+=n
  return bytes(out),'lz4_legacy'
 if d[:6] in (b'070701',b'070702'):return d,'raw_newc'
 raise ValueError('unknown compression '+d[:12].hex())
def cpio(d):
 o=0;rows=[];contents={}
 while o<len(d):
  while o<len(d) and d[o]==0:o+=1
  if o==len(d):break
  if d[o:o+6] not in (b'070701',b'070702'):raise ValueError('bad cpio magic at '+hex(o))
  h=[int(d[o+6+i*8:o+14+i*8],16) for i in range(13)];size=h[6];ns=h[11];name=d[o+110:o+110+ns-1].decode(errors='replace');start=align(o+110+ns,4);end=start+size
  assert end<=len(d)
  rows.append(dict(name=name,size=size,mode=oct(h[1]),header_offset=o,data_offset=start))
  if name=='TRAILER!!!':o=align(end,4);continue
  if size:contents[name]=d[start:end]
  o=align(end,4)
 return rows,contents
pat=re.compile(r'5g|nr5g|nr[_\.-]|[_\.-]nr\b|rat_config|ratconfig|radio|modem|mcf|carrier|countrycode|ptcountry|mcc|mnc|plmn|ccci|simlock|md1_support|md_capability',re.I)
report={'format_reference':'https://android.googlesource.com/platform/system/tools/mkbootimg/+/refs/heads/main/include/bootimg/bootimg.h','images':[],'ramdisks':[]}
for name in ('boot.img','init_boot.img','vendor_boot.img'):
 d=(ROM/name).read_bytes();row=dict(name=name,size=len(d),sha256=hashlib.sha256(d).hexdigest());report['images'].append(row);fragments=[]
 if d[:8]==b'ANDROID!':
  ks,rs,hs,v=u32(d,8),u32(d,12),u32(d,20),u32(d,40);assert v==4
  row.update(header_version=v,kernel_size=ks,ramdisk_size=rs,header_size=hs)
  if rs:fragments.append(('generic',4096+align(ks,4096),rs))
 else:
  assert d[:8]==b'VNDRBOOT';v,ps,rs=u32(d,8),u32(d,12),u32(d,24);assert v==4
  hs,ds,ts,tn,te,bs=u32(d,2096),u32(d,2100),u32(d,2112),u32(d,2116),u32(d,2120),u32(d,2124)
  row.update(header_version=v,page_size=ps,ramdisk_size=rs,header_size=hs,dtb_size=ds,table_size=ts,entries=tn,entry_size=te,bootconfig_size=bs,cmdline=d[28:2076].split(b'\0')[0].decode(errors='replace'))
  base=align(hs,ps);table=base+align(rs,ps)+align(ds,ps);bc=table+align(ts,ps)
  row['bootconfig']=d[bc:bc+bs].decode(errors='replace')
  for i in range(tn):
   p=table+i*te;sz,off,typ=u32(d,p),u32(d,p+4),u32(d,p+8);label=d[p+12:p+44].split(b'\0')[0].decode(errors='replace');assert off+sz<=rs
   fragments.append((label or 'fragment'+str(i),base+off,sz))
  if not fragments and rs:fragments.append(('vendor',base,rs))
 for label,off,size in fragments:
  rd=dict(image=name,label=label,file_offset=off,compressed_size=size);report['ramdisks'].append(rd)
  try:
   raw,codec=decompress(d[off:off+size]);entries,contents=cpio(raw);rd.update(codec=codec,uncompressed_size=len(raw),files=entries,text_matches=[])
   for path,data in contents.items():
    if len(data)>2*1024*1024 or b'\0' in data[:1024]:continue
    txt=data.decode('utf-8',errors='replace');matches=[dict(line=i+1,text=s) for i,s in enumerate(txt.splitlines()) if pat.search(s)]
    if matches:
     rd['text_matches'].append(dict(path=path,matches=matches))
     # Exact selected text only, avoiding module/binary bulk copies.
     safe=(name+'_'+label+'_'+path).replace('/','__').replace('\\','__').replace(':','_')
     (OUT/(safe+'.txt')).write_text(txt,encoding='utf-8')
  except Exception as ex:rd['error']=str(ex)
(OUT/'inventory.json').write_text(json.dumps(report,indent=2),encoding='utf-8')
print(json.dumps({'images':[(r['name'],r.get('header_version')) for r in report['images']],'ramdisks':[dict(image=r['image'],label=r['label'],codec=r.get('codec'),files=len(r.get('files',[])),matching_text_files=len(r.get('text_matches',[])),error=r.get('error')) for r in report['ramdisks']]},indent=2))

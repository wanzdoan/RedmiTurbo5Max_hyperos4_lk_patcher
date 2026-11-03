from pathlib import Path
import struct,json,hashlib,collections,re
BASE=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images')
OUT=Path(__file__).resolve().parent
class Ext4:
 def __init__(self,d):
  self.d=d;s=d[1024:2048];assert s[56:58]==b'\x53\xef';self.bs=1024<<struct.unpack_from('<I',s,24)[0];self.ipg=struct.unpack_from('<I',s,40)[0];self.isz=struct.unpack_from('<H',s,88)[0];self.ds=struct.unpack_from('<H',s,254)[0] or 32;self.gd=(2 if self.bs==1024 else 1)*self.bs;self.meta=dict(block_size=self.bs,inodes_per_group=self.ipg,inode_size=self.isz,descriptor_size=self.ds,feature_incompat=hex(struct.unpack_from('<I',s,96)[0]),uuid=s[104:120].hex(),volume=s[120:136].rstrip(b'\0').decode(errors='replace'))
 def inode(self,n):
  g,i=divmod(n-1,self.ipg);desc=self.d[self.gd+g*self.ds:self.gd+(g+1)*self.ds];table=struct.unpack_from('<I',desc,8)[0]
  if self.ds>=64:table|=struct.unpack_from('<I',desc,40)[0]<<32
  pos=table*self.bs+i*self.isz;ino=self.d[pos:pos+self.isz];assert len(ino)==self.isz
  size=struct.unpack_from('<I',ino,4)[0]|struct.unpack_from('<I',ino,108)[0]<<32;mode=struct.unpack_from('<H',ino,0)[0];flags=struct.unpack_from('<I',ino,32)[0];return ino,size,mode,flags
 def extents(self,b):
  magic,n,mx,depth,gen=struct.unpack_from('<HHHHI',b);assert magic==0xf30a
  for i in range(n):
   ent=b[12+i*12:24+i*12]
   if depth:
    lb,lo,hi,_=struct.unpack('<IIHH',ent);ptr=lo|(hi<<32);yield from self.extents(self.d[ptr*self.bs:(ptr+1)*self.bs])
   else:
    lb,ln,hi,lo=struct.unpack('<IHHI',ent);yield lb,ln&0x7fff,(hi<<32)|lo,ln>0x8000
 def read(self,n):
  ino,size,mode,flags=self.inode(n)
  if (mode&0xf000)==0xa000 and size<=60:return ino[40:40+size]
  assert flags&0x80000,('noextent',n,hex(flags),size)
  result=bytearray(size)
  for lb,ln,phys,uninit in self.extents(ino[40:100]):
   if uninit:continue
   start=lb*self.bs;length=min(ln*self.bs,max(0,size-start));result[start:start+length]=self.d[phys*self.bs:phys*self.bs+length]
  return bytes(result)
 def ls(self,n):
  data=self.read(n);off=0
  while off<len(data):
   inode,rl,nl,ft=struct.unpack_from('<IHBB',data,off)
   if rl<8:raise ValueError((n,off,rl))
   name=data[off+8:off+8+nl].decode(errors='replace')
   if inode and name not in ['.','..']:yield inode,name,ft
   off+=rl
 def walk(self,n=2,prefix=''):
  for inode,name,ft in self.ls(n):
   path=prefix+'/'+name;ino,sz,mode,flags=self.inode(inode);yield dict(path=path,inode=inode,size=sz,mode=hex(mode),flags=hex(flags))
   if mode&0xf000==0x4000:yield from self.walk(inode,path)
if __name__=='__main__':
 fs=Ext4((BASE/'mcf_ota.img').read_bytes());rows=list(fs.walk());print(fs.meta,'entries',len(rows));print('\n'.join(str(x) for x in rows[:30]));(OUT/'mcf_fs_inventory.json').write_text(json.dumps(dict(metadata=fs.meta,entries=rows),indent=2))
 for r in rows:
  if r['path'].endswith(('mcf_build_utc.txt','mcf_ota.md5sum')):
   d=fs.read(r['inode']);(OUT/(Path(r['path']).name+'.txt')).write_text(d.decode(errors='replace'));print(r['path'],d[:300])

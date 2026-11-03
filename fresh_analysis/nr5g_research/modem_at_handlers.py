from pathlib import Path
import json,lzma,re,struct
root=Path(__file__).resolve().parent;rom=Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images');d=(rom/'modem.img').read_bytes();rs=json.loads((root/'modem/modem_mtk_records.json').read_text());r=next(r for r in rs if r['name']=='md1_dbginfo');o=int(r['offset'],16)+512;x=lzma.decompress(d[o:o+r['size']],format=lzma.FORMAT_ALONE)
p=0x93;hits=[]
while p<len(x):
 e=x.find(b'\0',p)
 if e<0 or e==p or e-p>512 or not all(32<=b<127 for b in x[p:e]):break
 a,b=struct.unpack_from('<II',x,e+1);name=x[p:e].decode()
 if re.search(r'e5gopt|5gopt|nsa_support|support_nsa|sa_only|nsa_only|is_nr_supported|nr_mode.*(?:get|set)|(?:get|set).*nr_mode',name,re.I):hits.append({'name':name,'address':hex(a),'end':hex(b),'size':b-a})
 p=e+9
(root/'modem/at_nr_handler_symbols.json').write_text(json.dumps({'format':'CATICTNR decoded symbol names and declared address ranges only','table_end':hex(p),'symbols':hits,'limits':['No instruction decode or execution established','ELF/file mapping and processor mode not established for these ranges']},indent=2))
print(json.dumps(hits[:45],indent=2))

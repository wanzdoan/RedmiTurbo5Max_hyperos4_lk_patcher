from pathlib import Path
import json,lzma,gzip,re,hashlib,struct
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');out=Path(__file__).resolve().parent;d=(base/'modem.img').read_bytes();rs=json.loads((out/'modem_mtk_records.json').read_text());report={}
for name in ['md1_dbginfo','md1_mddbmetaodb','md1_mdmlayout']:
 r=next(r for r in rs if r['name']==name);o=int(r['offset'],16)+512;p=d[o:o+r['size']]
 if name=='md1_dbginfo':plain=lzma.decompress(p,format=lzma.FORMAT_ALONE);fmt='lzma-alone'
 elif name=='md1_mddbmetaodb':plain=gzip.decompress(p);fmt='gzip'
 else:plain=p;fmt='plaintext'
 info={'carrier_offset':hex(o),'compressed_size':len(p),'decoded_size':len(plain),'decoded_sha256':hashlib.sha256(plain).hexdigest(),'format':fmt,'first256_hex':plain[:256].hex()};hits=[]
 pat=re.compile(r'(?i)(nr.*(?:band.*plmn|disable.*mcc|band_support|mcc)|(?:disable.*nr|endc.*black|nr.*black)|mcf.*(?:auto_select|decrypt|security)|SBP_NR_CUSTOM_DISABLE_BAND_BY_PLMN|SBP_NAS_ROAMING_DISABLE_ENDC)')
 for m in re.finditer(rb'[ -~]{6,}',plain):
  s=m.group().decode()
  if pat.search(s) and len(hits)<150:hits.append({'offset':hex(m.start()),'text':s[:800]})
 info['focused_hits']=hits;report[name]=info;print(name,len(plain),plain[:150],len(hits));print('\n'.join(h['offset']+' '+h['text'] for h in hits[:30]))
(out/'modem_debug_metadata.json').write_text(json.dumps(report,indent=2))

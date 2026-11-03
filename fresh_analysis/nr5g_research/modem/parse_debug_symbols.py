from pathlib import Path
import json,lzma,re,struct,collections
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');out=Path(__file__).resolve().parent;d=(base/'modem.img').read_bytes();rs=json.loads((out/'modem_mtk_records.json').read_text());r=next(r for r in rs if r['name']=='md1_dbginfo');o=int(r['offset'],16)+512;x=lzma.decompress(d[o:o+r['size']],format=lzma.FORMAT_ALONE)
syms=[];p=0x93
while p<len(x):
 end=x.find(0,p)
 if end<0 or end-p>512 or end==p or not all(32<=c<127 for c in x[p:end]):break
 a,b=struct.unpack_from('<II',x,end+1);syms.append({'name':x[p:end].decode(),'address':a,'end':b,'size':b-a,'table_offset':p});p=end+9
print('symbolcount',len(syms),'stop',hex(p),'tail',x[p:p+64].hex());wanted=[s for s in syms if re.search(r'(?i)(band.*(?:disable|enable|plmn)|(?:nras|mrs).*(?:mcc|plmn)|sbp.*(?:default|feature|config)|nvram.*(?:default|custom).*|MCF.*(?:PLMN|MCC)|custom.*(?:endc|nr_band))',s['name'])]
(out/'modem_relevant_symbols.json').write_text(json.dumps({'parsed_symbols':len(syms),'table_stop':hex(p),'focused_symbols':wanted},indent=2));print('\n'.join(hex(s['address'])+' '+str(s['size'])+' '+s['name'] for s in wanted[:140]))

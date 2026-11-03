import subprocess,base64,struct,json
from pathlib import Path
out=Path(__file__).resolve().parent
cmd='for f in /mnt/vendor/nvdata/md/NVRAM/NVD_DATA/*; do printf "%s " "$f"; head -c 24 "$f" | base64; done'
raw=subprocess.check_output(['adb','exec-out','su','-c',cmd]).decode();want={960,961,2375,2392,2380,2390,2439,2437,2451,2433,2432};found=[];allheaders=[]
for line in raw.splitlines():
 path,b64=line.split(' ',1);h=base64.b64decode(b64)
 if h[:4]!=b'LID\0':continue
 lid,n,sz=struct.unpack_from('<III',h,8);hl=struct.unpack_from('<H',h,20)[0];row=dict(path=path,lid=lid,version=h[4:8].rstrip(b'\0').decode(),record_count=n,record_size=sz,header_length=hl)
 allheaders.append(row)
 if lid in want:found.append(row)
report={'evidence_id':'NR5G-RUNTIME-NVD-HEADERS-001','operation':'Read only first24bytes of regular NVD_DATA files; no payloads persisted','headers_count':len(allheaders),'target_lids':sorted(want),'found':found,'not_found_in_NVD_DATA':[x for x in sorted(want) if x not in {r['lid'] for r in found}],'limitations':['Not found means absent from this directory only; modem may use ROM defaults or another namespace','Header metadata does not reveal active encrypted recordvalues']}
(out/'runtime_nvram_headers.json').write_text(json.dumps(report,indent=2));print(json.dumps(report,indent=2))

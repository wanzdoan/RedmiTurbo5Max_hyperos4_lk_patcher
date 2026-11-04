"""Extract immutable recovery evidence from the supplied Global vendor_boot."""
from pathlib import Path
import argparse
import hashlib
import json
import re
import sys

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[2]
sys.path.insert(0,str(ROOT/'fresh_analysis/nr5g_research/lk_causal'))
from extract_handoff_inputs import decompress,cpio,u32,align

SOURCE_SHA='381a47c837e67fc46b9442f5a1c9ca0444c4b97f67c9f3b8fa82e7facf84a285'
RECOVERY_SHA='b4a558ce3801e1c84903ef2d1752e8a5d86fc1beecee1adbb8f64f379a3be483'
INIT_SHA='073ea3296c5465ef4b078c918e886a4250eddcddbc5d2bf862f6f086a2b581e9'


def sha(d):return hashlib.sha256(d).hexdigest()


def extract(source):
    p=source.read_bytes()
    if sha(p)!=SOURCE_SHA or p[:8]!=b'VNDRBOOT' or u32(p,8)!=4:
        raise ValueError('Not the user-supplied Global vendor_boot')
    page=u32(p,12);base=align(u32(p,2096),page)
    table=base+align(u32(p,24),page)+align(u32(p,2100),page)
    result=[]
    for i in range(u32(p,2116)):
        e=table+i*u32(p,2120);n=u32(p,e);off=u32(p,e+4)
        if off+n>u32(p,24):raise ValueError('Fragment outside vendor ramdisk')
        for name,data in cpio(decompress(p[base+off:base+off+n])):
            if name in ('system/bin/recovery','system/bin/init','system/lib64/librecovery_ui.so','system/lib64/librecovery_ui_ext.so'):
                target=HERE/'input'/Path(name).name
                if name=='system/bin/recovery' and sha(data)!=RECOVERY_SHA:
                    raise ValueError('Recovery identity mismatch')
                if name=='system/bin/init' and sha(data)!=INIT_SHA:
                    raise ValueError('Init identity mismatch')
                if target.exists() and target.read_bytes()!=data:
                    raise ValueError('Refusing to overwrite a different reference')
                target.parent.mkdir(parents=True,exist_ok=True)
                target.write_bytes(data)
                result.append({'member':name,'saved':str(target),'bytes':len(data),'sha256':sha(data)})
            elif name.endswith('prop.default'):
                result.append({'member':name,'sha256':sha(data),'build_properties':[
                    s for s in data.decode(errors='replace').splitlines()
                    if re.search(r'ro.product|fingerprint|build.version|security_patch|mod_device',s)]})
    return {'source_path':str(source),'source_sha256':sha(p),'bytes':len(p),
            'members':result,'device_read_or_written':False}


if __name__=='__main__':
    ap=argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--source',type=Path,default=Path(r'C:\Users\Administrator\Downloads\vendor_boot.img'))
    args=ap.parse_args();report=extract(args.source)
    (HERE/'target_inventory.json').write_text(json.dumps(report,indent=2)+'\n')
    print('Reference verified:',report['source_sha256'])

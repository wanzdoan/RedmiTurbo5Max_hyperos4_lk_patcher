"""Deterministic two-image evidence bundle; preserve reference ZIP bytes."""
import json
import zipfile
from build_port import *

def main():
    files=['README_VI.md','runtime_confirmation.json']
    manifest=[]
    for kind,name in NAMES.items():
        blob=(HERE/name).read_bytes(); report=json.loads((HERE/(kind+'_manifest.json')).read_text())
        tests=json.loads((HERE/(kind+'_validation.json')).read_text())
        behavior=json.loads((HERE/(kind+'_behavior.json')).read_text())
        assert sha(blob)==report['output_sha256']==tests['output_sha256']==behavior['sha256']
        assert tests['status'].startswith('PASS') and behavior['status'].startswith('PASS')
        files += [name,kind+'_manifest.json',kind+'_validation.json',kind+'_behavior.json']
        manifest.append(dict(variant=kind,artifact=name,bytes=len(blob),sha256=sha(blob)))
    release=dict(status='OFFLINE_VALIDATED_NOT_DEVICE_BOOTED',files=manifest,
                 new_images_device_flashed=False,stock_h4_preloader_compatibility_proven=False)
    (HERE/'release_manifest.json').write_text(json.dumps(release,indent=2)+'\n'); files.append('release_manifest.json')
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/n).read_bytes())+'  '+n+'\n' for n in files))
    files.append('SHA256SUMS.txt'); target=HERE/'lk_h4_stock_policy_auto_hwc_two_files.zip'
    with zipfile.ZipFile(target,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in files:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(target) as z:
        assert z.testzip() is None and z.namelist()==files
        for name in files: assert z.read(name)==(HERE/name).read_bytes()
    print(target.name,sha(target.read_bytes()),'CRC/hash PASS')

if __name__=='__main__':main()

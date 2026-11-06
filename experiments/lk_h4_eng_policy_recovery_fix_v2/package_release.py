"""Package immutable v2 images and offline evidence, never flashes."""
import json
import zipfile
from build_fix import *

def main():
    names=['README_VI.md','runtime_confirmation.json','recovery_gate_validation.json','preloader_validation.json']
    release=[]
    for kind,config in VARIANTS.items():
        name=config['output'];blob=(HERE/name).read_bytes()
        report=json.loads((HERE/(kind+'_manifest.json')).read_text())
        core=json.loads((HERE/(kind+'_validation.json')).read_text())
        behavior=json.loads((HERE/(kind+'_behavior.json')).read_text())
        recovery=json.loads((HERE/'recovery_gate_validation.json').read_text())['h4_variants'][kind]
        assert sha(blob)==report['output_sha256']==core['output_sha256']==behavior['sha256']==recovery['output_sha256']
        assert all(row['status'].startswith('PASS') for row in (core,behavior,recovery))
        names += [name,kind+'_manifest.json',kind+'_validation.json',kind+'_behavior.json']
        release.append(dict(variant=kind,artifact=name,sha256=sha(blob),bytes=len(blob)))
    (HERE/'release_manifest.json').write_text(json.dumps(dict(status='EXPERIMENTAL_NOT_DEVICE_BOOTED',files=release),indent=2)+'\n')
    names.append('release_manifest.json')
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/n).read_bytes())+'  '+n+'\n' for n in names))
    names.append('SHA256SUMS.txt');target=HERE/'lk_h4_eng_policy_recovery_fix_v2_two_files.zip'
    with zipfile.ZipFile(target,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in names:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(target) as z:
        assert z.testzip() is None and z.namelist()==names
        for name in names:assert z.read(name)==(HERE/name).read_bytes()
    print(target.name,sha(target.read_bytes()),'CRC/hash PASS')

if __name__=='__main__':main()

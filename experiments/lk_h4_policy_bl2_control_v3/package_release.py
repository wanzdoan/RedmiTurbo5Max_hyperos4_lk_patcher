"""Deterministic policy/control bundle, without altering previous releases."""
import json
import zipfile
from build_controls import *

def main():
    validation=json.loads((HERE/'validation.json').read_text());assert validation['status'].startswith('PASS')
    tests={row['variant']:row for row in validation['variants']}
    names=['README_VI.md','runtime_confirmation.json','validation.json','booted_reference_validation.json'];release=[]
    for kind,cfg in VARIANTS.items():
        blob=(HERE/cfg['output']).read_bytes();meta=json.loads((HERE/(kind+'_manifest.json')).read_text())
        assert sha(blob)==meta['output_sha256']==tests[kind]['output_sha256']
        names += [cfg['output'],kind+'_manifest.json']
        release.append(dict(variant=kind,artifact=cfg['output'],sha256=sha(blob),bytes=len(blob),
                            stored_lock_policy=cfg['release'],forced_unlocked_control=not cfg['release']))
    (HERE/'release_manifest.json').write_text(json.dumps(dict(status='OFFLINE_COMPARISON_NOT_RUNTIME_FIX',files=release),indent=2)+'\n')
    names.append('release_manifest.json')
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/n).read_bytes())+'  '+n+'\n' for n in names));names.append('SHA256SUMS.txt')
    path=HERE/'lk_h4_v3_bl2_comparison_bundle.zip'
    with zipfile.ZipFile(path,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in names:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(path) as z:
        assert z.testzip() is None and z.namelist()==names
        for name in names:assert z.read(name)==(HERE/name).read_bytes()
    print(path.name,sha(path.read_bytes()),'CRC/hash PASS')

if __name__=='__main__':main()

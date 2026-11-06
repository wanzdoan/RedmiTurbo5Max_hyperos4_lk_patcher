"""Deterministic four-image bundle; preserve the earlier three-image ZIP."""
import zipfile
from build_bundle import *


def main():
    validation=json.loads((HERE/'validation.json').read_text(encoding='utf-8'))
    assert validation['status'].startswith('PASS_')
    names=[]
    for kind,cfg in SOURCES.items():
        data=(HERE/cfg['artifact']).read_bytes()
        assert sha(data)==cfg['sha256'] and data==(ROOT/cfg['source']).read_bytes()
        names.append(cfg['artifact'])
    names += ['README_VI.md','release_manifest.json','runtime_confirmation.json','validation.json']
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/name).read_bytes())+'  '+name+'\n' for name in names),encoding='utf-8')
    names.append('SHA256SUMS.txt')
    target=HERE/'lk_h4_locked_unlocked_four_files.zip'
    with zipfile.ZipFile(target,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in names:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(target) as z:
        assert z.testzip() is None and z.namelist()==names
        for name in names:assert z.read(name)==(HERE/name).read_bytes()
    print(target.name,sha(target.read_bytes()),'CRC/hash PASS')


if __name__=='__main__':main()

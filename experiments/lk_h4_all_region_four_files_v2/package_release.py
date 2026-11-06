"""Bundle corrected all-region images with hash-bound offline evidence."""
import zipfile
from build_all_region import *


def main():
    manifest=json.loads((HERE/'release_manifest.json').read_text(encoding='utf-8'))
    validation=json.loads((HERE/'validation.json').read_text(encoding='utf-8'))
    runtime=json.loads((HERE/'runtime_confirmation.json').read_text(encoding='utf-8'))
    assert validation['status'].startswith('PASS_')
    tested={row['variant']:row for row in validation['variants']}
    names=[]
    for row in manifest['files']:
        image=(HERE/row['artifact']).read_bytes();kind=row['variant']
        assert sha(image)==row['sha256']==tested[kind]['sha256']==runtime['files'][kind]['sha256']
        assert row['auto_hwc'] and not row['fakelock']
        names.append(row['artifact'])
    assert len(names)==4
    names += ['README_VI.md','release_manifest.json','validation.json','runtime_confirmation.json']
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/name).read_bytes())+'  '+name+'\n' for name in names),encoding='utf-8')
    names.append('SHA256SUMS.txt')
    path=HERE/'lk_h4_all_region_four_files_v2.zip'
    with zipfile.ZipFile(path,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in names:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(path) as z:
        assert z.testzip() is None and z.namelist()==names
        for name in names:assert z.read(name)==(HERE/name).read_bytes()
    print(path.name,sha(path.read_bytes()),'CRC/hash PASS')


if __name__=='__main__':main()

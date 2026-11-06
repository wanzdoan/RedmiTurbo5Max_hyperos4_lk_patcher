"""Reproducible single-image delivery with hash-bound evidence."""
import json
import zipfile
from build_fakelock_h4 import *


def main():
    image=(HERE/NAME).read_bytes()
    reports=[json.loads((HERE/name).read_text(encoding='utf-8')) for name in
             ('manifest.json','validation.json','boot_policy_validation.json','runtime_confirmation.json')]
    assert sha(image)==reports[0]['output_sha256']==reports[1]['output_sha256']==reports[2]['sha256']==reports[3]['sha256']
    assert reports[1]['status'].startswith('PASS_') and reports[2]['status'].startswith('PASS_')
    manifest=dict(status='OFFLINE_VALIDATED_NOT_DEVICE_BOOTED',artifact=NAME,sha256=sha(image),bytes=len(image),
                  input_sha256=BASE_SHA,unlocked_reference_sha256=UNLOCKED_SHA,h3_reference_sha256=H3_SHA,
                  native_main_cpu_cases=reports[1]['total_main_cpu_cases']+reports[2]['caller_count'],
                  h3_reference_cases=reports[1]['main_cpu_counts']['h3_semantic_reference'],
                  mte_off=True,auto_hwc=True,internally_unlocked=True,exported_lock_is_synthetic=True,
                  boot_or_root_or_integrity_confirmed=False,runtime_report='runtime_confirmation.json')
    (HERE/'release_manifest.json').write_text(json.dumps(manifest,indent=2)+'\n',encoding='utf-8')
    names=[NAME,'README_VI.md','manifest.json','release_manifest.json','runtime_confirmation.json','validation.json','boot_policy_validation.json']
    (HERE/'SHA256SUMS.txt').write_text(''.join(sha((HERE/name).read_bytes())+'  '+name+'\n' for name in names),encoding='utf-8')
    names.append('SHA256SUMS.txt')
    target=HERE/'lk_h4_fakelock_v1_bundle.zip'
    with zipfile.ZipFile(target,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=9) as z:
        for name in names:
            info=zipfile.ZipInfo(name,(2026,11,6,0,0,0));info.compress_type=zipfile.ZIP_DEFLATED
            z.writestr(info,(HERE/name).read_bytes())
    with zipfile.ZipFile(target) as z:
        assert z.testzip() is None and z.namelist()==names
        for name in names:assert z.read(name)==(HERE/name).read_bytes()
    print(target.name,sha(target.read_bytes()),'CRC/hash PASS')


if __name__=='__main__':main()

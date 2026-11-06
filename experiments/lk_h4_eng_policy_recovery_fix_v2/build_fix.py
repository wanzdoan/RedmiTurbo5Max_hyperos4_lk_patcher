"""Match ENG's absent post-AVB Secure-ID call site; never flashes a phone."""
from pathlib import Path
import json
import sys
import struct

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[1]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,prefix,sha
BASE=ROOT/'experiments/lk_h4_eng_policy_auto_hwc_v1'
VARIANTS={
 'default':dict(input='lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_TEST.img',
     sha256='da05dc16e6a2e1d48a7f824a9382cf9a4dc4ce3ad1fe58387ca34519a65cbf3c',
     output='lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_TEST.img'),
 'mte_off':dict(input='lk_H4_STOCK_POLICY_AUTO_HWC_H3ENG_BL2_MTE_OFF_TEST.img',
     sha256='4206c420124ec5d2e95a7e6fe22fe81b05c405abd7940a74c7e6501d8b7dc893',
     output='lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img')}
OFFSET=0x147ac
OLD=bytes.fromhex('40010054') # b.eq0x147d4, originally only skips check in recovery mode
NEW=bytes.fromhex('0a000014') # b0x147d4, skip call-site block for all boot modes


def build(source,kind):
    if kind not in VARIANTS or sha(source)!=VARIANTS[kind]['sha256']:
        raise ValueError('Exact v1 input required')
    rows=members(source);at=OFFSET+512
    assert source[at:at+4]==OLD
    # Do not modify the persistent lock getter/setter or common Secure-ID verifier.
    out=bytearray(source);out[at:at+4]=NEW
    c=rows[2]['offset']+512;size=rows[0]['size']
    assert rows[2]['size']==1162
    assert source[c:c+180]==prefix(source[:512],source[512:512+size])
    out[c:c+180]=prefix(bytes(out[:512]),bytes(out[512:512+size]))
    allowed=set(range(at,at+4))|set(range(c,c+180))
    assert len(out)==len(source) and all(a==b or i in allowed for i,(a,b) in enumerate(zip(source,out)))
    for i,r in enumerate(rows):
        if i not in (0,2):assert source[r['offset']:r['end']]==out[r['offset']:r['end']]
    assert source[c+180:rows[2]['end']]==out[c+180:rows[2]['end']]
    return bytes(out),dict(status='EXPERIMENTAL_CALLSITE_FIX_NOT_DEVICE_BOOTED',variant=kind,
        input_path=str((BASE/VARIANTS[kind]['input']).relative_to(ROOT)),input_sha256=sha(source),
        artifact=VARIANTS[kind]['output'],output_sha256=sha(out),bytes=len(out),
        patch=dict(payload_offset=hex(OFFSET),file_offset=hex(at),old=OLD.hex(),new=NEW.hex(),
                   purpose='Skip only post-AVB Secure-ID gate absent in H3 ENG boot_linux_from_storage'),
        original_secinfo_verifier_getter_setter_avb_and_root_handoff_unchanged=True,
        hwc_keys_fastboot_mte_and_bl2_from_v1_unchanged=True,
        physical_device_flashed=False,physical_device_android_booted=False,
        limits=['Bypasses one vendor hardware/provisioning identity check at the boot call site, as ENG H3 does',
                'Does not repair Secure-ID provisioning or establish a safe real relock/OTA process',
                'Hypothesized recovery cause; no actual device failure log or LK readback yet',
                'AVB signature/hash/rollback checks unchanged; modified init_boot can still fail locked boot',
                'No forced locked/unlocked/green, no RPMB/efuse/partition write added',
                'No hardware preloader/boot/banking/Integrity proof'])


if __name__=='__main__':
    for kind,config in VARIANTS.items():
        image,report=build((BASE/config['input']).read_bytes(),kind)
        (HERE/config['output']).write_bytes(image)
        (HERE/(kind+'_manifest.json')).write_text(json.dumps(report,indent=2)+'\n')
        print(kind,report['output_sha256'],len(image))

"""One-variable BL2 controls; retain v2 main or explicitly labeled unlock control."""
from pathlib import Path
import json
import sys
HERE=Path(__file__).resolve().parent;ROOT=HERE.parents[1]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,prefix,sha,OLD,PATCH
BASE=ROOT/'experiments/lk_h4_eng_policy_recovery_fix_v2'
DONOR=ROOT/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
DONOR_SHA='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
VARIANTS={
 'policy_default':dict(source='lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_TEST.img',
    sha256='f93f513ef36accfd0056dd130d6bfc592ac1944820900785c059a46eed010884',
    output='lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_TEST.img',release=True),
 'policy_mte_off':dict(source='lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img',
    sha256='48912f04727fede359a77dbe57dcb636cbb70f52121f7052ab00af46fbc22c53',
    output='lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img',release=True),
 'unlocked_diagnostic_eng_mte_off':dict(source='lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img',
    sha256='48912f04727fede359a77dbe57dcb636cbb70f52121f7052ab00af46fbc22c53',
    output='diagnostic/lk_H4_V3_UNLOCKED_ENG_BL2_MTE_OFF_CONTROL.img',release=False)}


def build(source,donor,kind):
    if kind not in VARIANTS or sha(source)!=VARIANTS[kind]['sha256'] or sha(donor)!=DONOR_SHA:
        raise ValueError('Exact v2 image and boot-confirmed donor required')
    rows,dm=members(source),members(donor);c=rows[2]['offset']+512
    assert source[c:c+180]==prefix(source[:512],source[512:512+rows[0]['size']])
    if VARIANTS[kind]['release']:
        triple=donor[dm[3]['offset']:dm[5]['end']]
        out=source[:rows[3]['offset']]+triple+source[rows[5]['end']:]
        new=members(out)
        assert out[:new[3]['offset']]==source[:rows[3]['offset']]
        assert out[new[6]['offset']:]==source[rows[6]['offset']:]
        assert out[new[3]['offset']:new[5]['end']]==triple
        for i in range(6,15):
            assert out[new[i]['offset']:new[i]['end']]==donor[dm[i]['offset']:dm[i]['end']]
        main_unchanged=True;forced=False
    else:
        assert source[0x89cec:0x89cfc]==OLD
        out=bytearray(source);out[0x89cec:0x89cfc]=PATCH
        out[c:c+180]=prefix(bytes(out[:512]),bytes(out[512:512+rows[0]['size']]))
        allowed=set(range(0x89cec,0x89cfc))|set(range(c,c+180))
        assert all(a==b or i in allowed for i,(a,b) in enumerate(zip(source,out)))
        out=bytes(out);new=members(out);main_unchanged=False;forced=True
        for i in range(1,15):
            if i==2:continue
            assert out[new[i]['offset']:new[i]['end']]==source[rows[i]['offset']:rows[i]['end']]
    return out,dict(status='DIAGNOSTIC_CONTROL_NOT_RUNTIME_FIX_CONFIRMED',variant=kind,artifact=VARIANTS[kind]['output'],
        input_sha256=sha(source),donor_sha256=sha(donor),output_sha256=sha(out),bytes=len(out),
        change='Only signed BL2/cert1/cert2 triple and member offsets' if main_unchanged else 'Only force-unlock getter16 bytes and main hash prefix',
        main_and_main_certificate_records_exact_v2=main_unchanged,forced_unlock_control=forced,
        persistent_lock_write_performed=False,
        bl2_origin='Exact signed H3 release triple from boot-confirmed f57 image' if main_unchanged else 'Exact v2 signed H3 ENG triple',
        limits=['Comparison experiment; actual recovery cause unknown and no phone test',
                'Policy variants preserve v2 stored-state getter and all v2 main bytes including earlier Secure-ID call-site skip',
                'Unlocked diagnostic reports state3 at getter; never use it as locked/banking/Integrity evidence',
                'No extra signature/hash/rollback bypass added',
                'No preloader, RPMB, efuse, slot, partition or phone setting changed by builder'])


if __name__=='__main__':
    donor=DONOR.read_bytes()
    for kind,cfg in VARIANTS.items():
        blob,report=build((BASE/cfg['source']).read_bytes(),donor,kind)
        target=HERE/cfg['output'];target.parent.mkdir(parents=True,exist_ok=True);target.write_bytes(blob)
        (HERE/(kind+'_manifest.json')).write_text(json.dumps(report,indent=2)+'\n')
        print(kind,report['output_sha256'],len(blob))

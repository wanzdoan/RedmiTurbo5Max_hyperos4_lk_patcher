"""Four H4 auto-HWC images: original policy or actual forced unlock, never fake.

The two policy images remain exact retained v3 images. Each unlocked image
changes only the16-byte getter and existing main hash prefix of its matching
policy image, preserving the boot-confirmed release BL2 and all region code.
"""
from pathlib import Path
import json
import sys

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[1]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,prefix,sha,OLD,PATCH

POLICY_DIR=ROOT/'experiments/lk_h4_policy_bl2_control_v3'
UNLOCKED=ROOT/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
UNLOCKED_SHA='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
BASES={
    'default':dict(name='lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_TEST.img',sha256='8ac64a981d880b943992ba19dac927665e045e28d47b88c8f77bc664b4b2b664'),
    'off':dict(name='lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img',sha256='6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52'),
}
VARIANTS={
    'locked_default':dict(base='default',unlock=False,artifact='locked/lk_H4_LOCKED_AUTO_HWC_MTE_DEFAULT.img'),
    'locked_off':dict(base='off',unlock=False,artifact='locked/lk_H4_LOCKED_AUTO_HWC_MTE_OFF.img'),
    'unlocked_default':dict(base='default',unlock=True,artifact='unlocked/lk_H4_UNLOCKED_AUTO_HWC_MTE_DEFAULT.img'),
    'unlocked_off':dict(base='off',unlock=True,artifact='unlocked/lk_H4_UNLOCKED_AUTO_HWC_MTE_OFF.img'),
}


def build(source,donor,kind):
    if kind not in VARIANTS:raise ValueError('Unknown variant')
    cfg=VARIANTS[kind];expected=BASES[cfg['base']]
    if sha(source)!=expected['sha256'] or sha(donor)!=UNLOCKED_SHA:
        raise ValueError('Exact v3 policy source and boot-confirmed H4 unlock donor required')
    assert source[0x89cec:0x89cfc]==OLD and donor[0x89cec:0x89cfc]==PATCH
    rows,donor_rows=members(source),members(donor)
    assert source[rows[3]['offset']:rows[5]['end']]==donor[donor_rows[3]['offset']:donor_rows[5]['end']]
    c=rows[2]['offset']+512
    assert rows[2]['size']==1162 and source[c:c+180]==prefix(source[:512],source[512:rows[0]['end']])
    if cfg['unlock']:
        out=bytearray(source);out[0x89cec:0x89cfc]=donor[0x89cec:0x89cfc]
        out[c:c+180]=prefix(bytes(out[:512]),bytes(out[512:rows[0]['end']]))
        allowed=set(range(0x89cec,0x89cfc))|set(range(c,c+180))
        assert all(a==b or i in allowed for i,(a,b) in enumerate(zip(source,out)))
        assert len(out)==len(source)
        for i,row in enumerate(rows):
            if i not in (0,2):assert out[row['offset']:row['end']]==source[row['offset']:row['end']]
        assert out[c+180:rows[2]['end']]==source[c+180:rows[2]['end']]
        out=bytes(out)
    else:out=source
    return out,dict(variant=kind,artifact=cfg['artifact'],source=str((POLICY_DIR/expected['name']).relative_to(ROOT)),
        input_sha256=sha(source),unlocked_getter_donor_sha256=sha(donor),sha256=sha(out),bytes=len(out),
        change='Only force3 getter16 bytes plus existing main hash prefix180' if cfg['unlock'] else 'Exact retained v3 policy bytes',
        internally_force_unlocked=cfg['unlock'],fakelock=False,auto_hwc=True,known_cn_gl_eng_key_policy_preserved=True,
        mte='FORCED_LK_NOMTE' if cfg['base']=='off' else 'ORIGINAL_CONDITIONAL_POLICY_NOT_FORCE_ON',
        signed_bl2_triple_and_all_other_members_unchanged=True,persistent_lock_written=False,
        prior_user_android_boot_report=kind=='locked_off',new_device_test=False)


def main():
    donor=UNLOCKED.read_bytes();files=[]
    for kind,cfg in VARIANTS.items():
        source=(POLICY_DIR/BASES[cfg['base']]['name']).read_bytes()
        image,meta=build(source,donor,kind)
        target=HERE/cfg['artifact'];target.parent.mkdir(parents=True,exist_ok=True)
        if target.exists() and target.read_bytes()!=image:raise ValueError('Refusing to overwrite a differing existing image')
        target.write_bytes(image);files.append(meta)
        print(kind,sha(image))
    manifest=dict(status='ALL_FOUR_AUTO_HWC_UNLOCKED_VARIANTS_NEW_NOT_BOOTED',files=files,
        correction='Previous v1 bundle had auto HWC on locked only; preserve it as history and deliver corrected v2 separately',
        composition='H4 patched release main, exact signed H3 commercial BL2 triple, H4 AEE/DTBs; not pure H4 or full ENG',
        device_actions='none')
    (HERE/'release_manifest.json').write_text(json.dumps(manifest,indent=2)+'\n',encoding='utf-8')


if __name__=='__main__':main()

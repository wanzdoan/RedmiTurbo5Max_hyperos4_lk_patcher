"""Select four exact retained firmware images without repatching booted files."""
from pathlib import Path
import hashlib
import json

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
SOURCES = {
    'locked_auto_hwc_mte_default': dict(
        source='experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_TEST.img',
        artifact='locked/lk_H4_LOCKED_POLICY_AUTO_HWC_MTE_DEFAULT.img',
        sha256='8ac64a981d880b943992ba19dac927665e045e28d47b88c8f77bc664b4b2b664',
        lock_policy='Original stored-state getter; not forced locked',
        hwc='Auto CN for cn countrycode, GLOBAL for nonempty other code, hardware fallback on unavailable input',
        mte='Original conditional policy; may enable or suppress depending on runtime inputs, not force-ON',
        prior_user_boot_report=False,
        runtime_source='experiments/lk_h4_policy_bl2_control_v3/runtime_confirmation.json'),
    'locked_auto_hwc_mte_off': dict(
        source='experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img',
        artifact='locked/lk_H4_LOCKED_POLICY_AUTO_HWC_MTE_OFF.img',
        sha256='6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52',
        lock_policy='Original stored-state getter; not forced locked',
        hwc='Auto CN for cn countrycode, GLOBAL for nonempty other code, hardware fallback on unavailable input',
        mte='Existing LK arm64.nomte suppression path forced',
        prior_user_boot_report=True,
        runtime_source='experiments/lk_h4_policy_bl2_control_v3/runtime_confirmation.json'),
    'unlocked_mte_default': dict(
        source='fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img',
        artifact='unlocked/lk_H4_UNLOCKED_MTE_DEFAULT.img',
        sha256='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171',
        lock_policy='Force unlocked3, original public reporting',
        hwc='Original H4 behavior; no auto-region extension added to unlocked branch',
        mte='Original conditional policy; may enable or suppress depending on runtime inputs, not force-ON',
        prior_user_boot_report=True,
        runtime_source='fresh_analysis/output/runtime_confirmation.json'),
    'unlocked_mte_off': dict(
        source='experiments/lk_h4_booted_hybrid_mte_off_v1/lk_hyperos4_MTE_OFF_TEST.img',
        artifact='unlocked/lk_H4_UNLOCKED_MTE_OFF.img',
        sha256='7873361e63ade5e6840d985c23c44bbb01fe449e7f89fd3ebbdba0d3d244e2a4',
        lock_policy='Force unlocked3, original public reporting',
        hwc='Original H4 behavior; no auto-region extension added to unlocked branch',
        mte='Existing LK arm64.nomte suppression path forced',
        prior_user_boot_report=False,
        runtime_source='experiments/lk_h4_booted_hybrid_mte_off_v1/runtime_confirmation.json'),
}


def sha(data):
    return hashlib.sha256(data).hexdigest()


def select(kind, data):
    if kind not in SOURCES or sha(data) != SOURCES[kind]['sha256']:
        raise ValueError('Exact retained source required; no implicit firmware rebuild')
    return bytes(data)


def main():
    files=[]
    for kind,cfg in SOURCES.items():
        source=(ROOT/cfg['source']).read_bytes();out=select(kind,source)
        target=HERE/cfg['artifact'];target.parent.mkdir(parents=True,exist_ok=True)
        if target.exists() and target.read_bytes()!=out:
            raise ValueError('Refusing to replace a differing existing delivery image')
        target.write_bytes(out)
        files.append(dict(variant=kind,**cfg,bytes=len(out),identical_to_retained_source=True))
    report=dict(status='EXACT_RETAINED_IMAGES_NO_NEW_FIRMWARE_PATCH',files=files,
                fakelock_excluded=True,device_actions='none',
                source_members='All four retain H4 main plus signed H3 commercial BL2 and H4 AEE/DTBs; not pure H4 or full ENG',
                default_is_not_forced_mte_on=True)
    (HERE/'release_manifest.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    print('Selected',len(files),'exact retained images; old names and bytes preserved')


if __name__=='__main__':main()

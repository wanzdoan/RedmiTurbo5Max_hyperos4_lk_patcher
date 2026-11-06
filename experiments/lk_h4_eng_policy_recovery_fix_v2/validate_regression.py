"""Run existing behavioral probes against v2 final bytes into v2 reports only."""
import json
from build_fix import *
sys.path.insert(0,str(BASE))
import build_port as old_builder
import validate_port as core
import validate_behavior as behavior
sys.path.insert(0,str(ROOT/'fresh_analysis/h4_preloader_lk_acceptance_20261106'))
from native_cert import run as preloader_cert
from native_locator import locate


def composite(h4,eng,kind='default'):
    intermediate=old_builder.build(h4,eng,kind)[0]
    return build(intermediate,kind)


def main():
    names={kind:item['output'] for kind,item in VARIANTS.items()}
    core.HERE=HERE;core.NAMES=names;core.build=composite
    behavior.HERE=HERE;behavior.NAMES=names
    preloader=[]
    for kind,config in VARIANTS.items():
        core.validate_one(kind);behavior.validate(kind)
        original=(BASE/config['input']).read_bytes();out=(HERE/config['output']).read_bytes()
        rows=members(out);old=members(original)
        assert out[rows[3]['offset']:rows[5]['end']]==original[old[3]['offset']:old[5]['end']]
        a,b,c=rows[3:6];c1=out[b['offset']+512:b['offset']+512+b['size']]
        result=preloader_cert(out[c['offset']:c['offset']+512+c['size']],c1[496:790],
            component_header=out[a['offset']:a['offset']+512],
            component_payload=out[a['offset']+512:a['offset']+512+a['size']])
        assert result['return_status']==0 and all(row['match'] for row in result['content_checks'])
        loc=locate(out);assert loc['status']=='0x0' and int(loc['member_offset'],16)==a['offset']
        preloader.append(dict(variant=kind,output_sha256=sha(out),bl2_triple_unchanged=True,
                              locator=loc,certificate_and_hash=result))
    (HERE/'preloader_validation.json').write_text(json.dumps(dict(status='PASS_OFFLINE_NOT_DEVICE_BOOT',
        cases=preloader,limits=['Same H4 stock preloader as previous audit; live ENG75e939 fixture not emulated',
                              'Keys/mode/heap/crypto backends are explicit fixtures; no efuse/rollback/handoff test']),indent=2)+'\n')


if __name__=='__main__':main()

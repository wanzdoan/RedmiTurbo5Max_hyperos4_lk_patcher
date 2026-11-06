"""Validate selected bytes and targeted requested HWC/key/MTE behavior."""
import itertools
import json
import struct
import sys
from build_bundle import *
from unicorn.arm64_const import *

sys.path.insert(0,str(ROOT/'experiments/lk_h4_eng_policy_auto_hwc_v1'))
from hwc_cases import flow_case
from validate_port import execute,DATA
sys.path.insert(0,str(ROOT/'experiments/lk_h4_booted_hybrid_mte_off_v1'))
from validate_mte_off import mte_case
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members


def main():
    images={};identities=[]
    for kind,cfg in SOURCES.items():
        original=(ROOT/cfg['source']).read_bytes();image=(HERE/cfg['artifact']).read_bytes()
        assert select(kind,original)==image==original and sha(image)==cfg['sha256']
        wrong=bytearray(original);wrong[12345]^=1
        try:select(kind,bytes(wrong))
        except ValueError:pass
        else:raise AssertionError('Wrong input accepted')
        images[kind]=image
        identities.append(dict(variant=kind,sha256=sha(image),bytes=len(image),exact_source_identity=True,wrong_input_rejected=True))
    locked=images['locked_auto_hwc_mte_off']
    hwc=[flow_case(locked,True,country,hw,suffix=slot) for country,hw,slot in itertools.product(
        ('cn','CN','Cn','cN','global','GLOBAL','eea','EEA','tw','TW','in','id','unknown',''),('CN','GLOBAL'),('_a','_b'))]
    hwc += [flow_case(locked,True,country,hw,mode=mode) for country,hw,mode in itertools.product(
        ('cn','global'),('CN','GLOBAL'),('missing','empty','short','io_error'))]
    stock=(ROOT/'lk_hyperos4_original/lk.img').read_bytes()
    eng=(ROOT/'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img').read_bytes()
    keys=[]
    for label,key in [('CN',stock[512+0x138658:512+0x138858]),('GL',stock[512+0x138858:512+0x138a58]),('ENG',eng[512+0x130728:512+0x130828])]:
        for corrupted in (False,True):
            modulus=bytearray(key)
            if corrupted:modulus[len(modulus)//2]^=1
            blob=struct.pack('>II',len(key)*8,0)+modulus+bytes(len(key))
            result,memory,_=execute(locked,0xc5978,{DATA+0x800:bytes(blob),UC_ARM64_REG_X1:DATA+0x800,
                UC_ARM64_REG_X2:len(blob),UC_ARM64_REG_X5:DATA+0x700})
            assert result==0 and memory[0x700]==int(not corrupted)
            keys.append(dict(key=label,corrupted=corrupted,trusted=memory[0x700]))
    mte=[]
    for kind,image in images.items():
        off=kind.endswith('_off')
        for bits,reserved in itertools.product(tuple(range(8))+(0x405,0x407,0xf07,0xffffffff),(False,True)):
            mte.append(dict(variant=kind,**mte_case(image,bits,reserved,off)))
    default=images['unlocked_mte_default'];off=images['unlocked_mte_off']
    rows=members(default);prefix_at=rows[2]['offset']+512
    allowed=set(range(512+0x34740,512+0x34744))|set(range(prefix_at,prefix_at+180))
    assert len(default)==len(off) and all(a==b or i in allowed for i,(a,b) in enumerate(zip(default,off)))
    assert default[512+0x89aec:512+0x89afc]==off[512+0x89aec:512+0x89afc]==bytes.fromhex('68008052080000b900008052c0035fd6')
    assert locked[512+0x89aec:512+0x89afc]!=default[512+0x89aec:512+0x89afc]
    locked_default=images['locked_auto_hwc_mte_default']
    assert len(locked_default)==len(locked) and all(a==b or i in allowed for i,(a,b) in enumerate(zip(locked_default,locked)))
    assert locked_default[512+0x89aec:512+0x89afc]==locked[512+0x89aec:512+0x89afc]
    ref=members(locked)
    for image in images.values():
        row=members(image)
        assert image[row[3]['offset']:row[5]['end']]==locked[ref[3]['offset']:ref[5]['end']]
    report=dict(status='PASS_EXACT_SELECTION_AND_TARGETED_NATIVE_CHECKS_NOT_NEW_DEVICE_TEST',
        identities=identities,native_counts=dict(locked_hwc=len(hwc),known_and_corrupted_keys=len(keys),mte=len(mte)),
        locked_hwc_cases=hwc,locked_key_cases=keys,mte_cases=mte,
        unlocked_pair_only_mte_branch_and_prefix_differ=True,
        locked_pair_only_mte_branch_and_prefix_differ=True,all_signed_bl2_triples_identical=True,
        limits=['No new firmware bytes compared with retained sources; prior DER/parser/AVB evidence carries by exact identity',
                'Native targeted tests use hardware/storage/formatter and crypto mode fixtures; no physical flash/boot test performed',
                'CN/non-CN routing is HWC selection, not independent hardware SKU identity or proof every regional ROM boots',
                'Default MTE follows original runtime policy; tests show both enable/suppression paths, not force-ON',
                'Forced OFF is a kernel-facing arm64.nomte path, not a preloader modification',
                'Locked policy uses persistent state; this bundle does not itself lock or unlock a device'])
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
    print(report['status'],report['native_counts'])


if __name__=='__main__':main()

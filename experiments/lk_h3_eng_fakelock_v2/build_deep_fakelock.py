"""Experimental extension of v1 reporting, including the LK secure handoff.

Does not lock the device or make its software authentic. No device writes.
"""
from pathlib import Path
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members, prefix, sha

INPUT = ROOT / 'experiments/lk_h3_eng_fakelock_v1/lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_TEST.img'
INPUT_SHA = '28f7f7b918a5462685eddf5e4814df1c28302bc9450de4d983dea81da58371ec'
NAME = 'lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_DEEP_HANDOFF_TEST.img'


def patches():
    return [
        (0xc4eac, 'e82f40b9', '28008052',
         'Select the existing real OEM-key hash for secure-handoff packet1, instead of the unlocked zero hash'),
        (0xc4f0c, 'e12f80b9', '210080d2',
         'Packet2 exported lock flag x1=1; local stack word and actual getter stay unlocked'),
        (0xc4f14, '020149b9', '02008052',
         'Packet2 exported verified-boot state w2=0; actual state/global cache remain unchanged'),
        (0xc838, 'a3c35fb8', '23008052',
         'Public seccfg query formats locked state1, without modifying the actual seccfg getter or stored word'),
        (0x69e68, '61010054', struct.pack('<I',0x14000000+((0x69e94-0x69e68)//4)).hex(),
         'Skip only the orange/unlocked UI warning; retain yellow/red error-warning branches')
    ]


def build(source):
    if sha(source) != INPUT_SHA:
        raise ValueError('Expected the exact retained v1 ENG unlock+fakelock artifact')
    rows=members(source)
    changed=bytearray(source)
    metadata=[]
    allowed=set()
    for off,old,new,why in patches():
        old,new=bytes.fromhex(old),bytes.fromhex(new)
        if source[512+off:512+off+len(old)]!=old or len(old)!=len(new):
            raise ValueError(f'Unexpected original ENG instruction at {off:x}')
        changed[512+off:512+off+len(new)]=new
        allowed.update(range(512+off,512+off+len(new)))
        metadata.append({'payload_offset':hex(off),'whole_file_offset':hex(off+512),
                         'original':old.hex(),'new':new.hex(),'purpose':why})
    c=rows[2];c2=c['offset']+512
    if c['size']!=1162 or source[c2:c2+180]!=prefix(source[:512],source[512:512+rows[0]['size']]):
        raise ValueError('Unexpected existing v1 main-cert prefix')
    changed[c2:c2+180]=prefix(bytes(changed[:512]),bytes(changed[512:512+rows[0]['size']]))
    allowed.update(range(c2,c2+180))
    if any(a!=b and i not in allowed for i,(a,b) in enumerate(zip(source,changed))):
        raise ValueError('Changed bytes outside explicitly allowed instruction/prefix spans')
    for i,r in enumerate(rows):
        if i not in (0,2) and source[r['offset']:r['end']]!=changed[r['offset']:r['end']]:
            raise ValueError('Other ENG component changed')
    if source[c2+180:c['end']]!=changed[c2+180:c['end']]:
        raise ValueError('Signed DER or certificate padding changed')
    # Preserve the prior force-unlocked getter, old public-report patches and
    # actual fastboot capability/cache/lock mutation and verified-boot machinery.
    protected=[(0x896e4,0x896f4),(0x99dc,0x9c20),(0xc208,0xc3b4),
               (0xb6588,0xb66a8),(0x6935c,0x69d68),(0x69dd8,0x69e00),
               (0xb014,0xb018),(0xb040,0xb044),(0xc770,0xc7fc),
               (0xbc79c,0xbc7a0),(0x69da4,0x69dd8),(0xc5004,0xc5018),
               (0xc5888,0xc5960),(0x68bfc,0x68f00)]
    for lo,hi in protected:
        if source[512+lo:512+hi]!=changed[512+lo:512+hi]:
            raise ValueError(f'Protected behavior changed at {lo:x}')
    result=bytes(changed)
    return result,{
        'status':'EXPERIMENTAL_SECURE_HANDOFF_NOT_HARDWARE_TESTED','artifact':NAME,
        'input_sha256':INPUT_SHA,'output_sha256':sha(result),'bytes':len(result),
        'patches':metadata,'main_instruction_patch_span_bytes':20,
        'actual_internal_lock_state':3,'actual_verified_boot_state_unchanged':True,
        'fastboot_cache_and_permission_code_unchanged':True,
        'v1_reporting_features_preserved':True,
        'secure_handoff_packet1':'existing calculated OEM-key hash instead of unlocked zero hash',
        'secure_handoff_packet2':{'exported_lock_flag':1,'exported_boot_state':0},
        'all_other_eng_members_and_signed_der_unchanged':True,
        'vendor_attestation_private_keys_or_certificates_forged':False,
        'persistent_lock_or_efuse_written':False,'phone_flashed':False,
        'play_integrity_or_vneid_pass_confirmed':False,
        'limits':[
            'Outgoing LK values are fake and do not prove that verified boot actually succeeded',
            'Secure-monitor/TEE consumption and final hardware attestation have not been tested',
            'Changing secure-handoff root-of-trust data may affect existing hardware-bound keys; exact device behavior is unverified',
            'Original extraction/hash/read failures are still logged; no cryptographic failure is repaired by this patch',
            'No complete Integrity verdict report, device readback or physical flash/boot test is available',
            'This cannot create a trusted attestation certificate chain, device certification or genuine patch levels',
            'No CN/Global boot-key extension from the separate stock-ENG experiment is merged'
        ]}


def main():
    output,report=build(INPUT.read_bytes())
    (HERE/NAME).write_bytes(output)
    (HERE/'manifest.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('artifact','output_sha256','bytes','status')},indent=2))


if __name__=='__main__':main()

"""Hypothesis test: stock-policy ENG v1, outgoing androidboot.hwc=GLOBAL.

The original hardware record, lock logic and boot verification remain intact.
"""
from pathlib import Path
import json
import struct
import sys

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[2]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,prefix,sha

INPUT=HERE.parent/'lk_hyperos3_ENG_STOCK_CN_GLOBAL_BOOT_FLASH_TEST.img'
INPUT_SHA='ad3d66e3c374ff3a83fc73ba30880fe1d18ee049b3c2ec54ff6e97e45815760b'
NAME='lk_hyperos3_ENG_STOCK_CN_GLOBAL_HWC_GLOBAL_TEST.img'
ARG=0x13380
LITERAL=0xc5954
VALUE=b'GLOBAL\0\0'


def adr(at,target,reg):
    delta=target-at
    if not -(1<<20)<=delta<(1<<20):raise ValueError('ADR out of range')
    imm=delta&0x1fffff
    return struct.pack('<I',0x10000000|((imm&3)<<29)|((imm>>2)<<5)|reg)


def patches():
    return [(ARG,bytes.fromhex('63920091'),adr(ARG,LITERAL,3),
             'Only the HWC formatting argument x3 points to GLOBAL'),
            (LITERAL,bytes.fromhex('1f2003d51f2003d5'),VALUE,
             'GLOBAL string in two unreachable NOP slots after the existing v1 key callback')]


def build(source):
    if sha(source)!=INPUT_SHA:raise ValueError('Expected exact stock-policy v1, not unlock or fakelock')
    rows=members(source);result=bytearray(source);allowed=set();changes=[]
    # The slot is after a return and an unconditional branch to the epilogue.
    # No live callback instruction is relocated or changed.
    if source[512+0xc5948:512+0xc5954]!=bytes.fromhex('c0035fd640008052fbffff17'):
        raise ValueError('Unexpected unreachable slot predecessor')
    for offset,old,new,purpose in patches():
        if len(old)!=len(new) or source[512+offset:512+offset+len(old)]!=old:
            raise ValueError(f'Unexpected original bytes at {offset:x}')
        result[512+offset:512+offset+len(new)]=new
        allowed.update(range(512+offset,512+offset+len(new)))
        changes.append({'payload_offset':hex(offset),'whole_file_offset':hex(offset+512),
                        'old':old.hex(),'new':new.hex(),'purpose':purpose})
    c=rows[2];c2=c['offset']+512
    if c['size']!=1162 or source[c2:c2+180]!=prefix(source[:512],source[512:512+rows[0]['size']]):
        raise ValueError('Unexpected existing main-cert hash prefix')
    result[c2:c2+180]=prefix(bytes(result[:512]),bytes(result[512:512+rows[0]['size']]))
    allowed.update(range(c2,c2+180))
    if any(a!=b and i not in allowed for i,(a,b) in enumerate(zip(source,result))):
        raise ValueError('Modification outside HWC argument/literal and hash prefix')
    for i,r in enumerate(rows):
        if i not in (0,2) and source[r['offset']:r['end']]!=result[r['offset']:r['end']]:
            raise ValueError('Other ENG member modified')
    if source[c2+180:c['end']]!=result[c2+180:c['end']]:
        raise ValueError('Retained DER or padding changed')
    for lo,hi in [(0x896e4,0x89cc0),(0x3530,0x35d0),(0x885fc,0x88670),
                  (0xb6588,0xb66a8),(0x6935c,0x69f10),(0xc4de0,0xc5018),
                  (0xc5888,LITERAL),(0x68bfc,0x68f00),
                  (ARG+4,0x13404),(0x99dc,0x9c20),(0xc208,0xc3b4)]:
        if source[512+lo:512+hi]!=result[512+lo:512+hi]:
            raise ValueError(f'Protected original behavior changed at {lo:x}')
    output=bytes(result)
    return output,{
        'status':'HYPOTHESIS_TEST_NOT_DEVICE_BOOTED','artifact':NAME,
        'input_sha256':INPUT_SHA,'output_sha256':sha(output),'bytes':len(output),
        'patches':changes,'new_main_instruction_bytes':4,'literal_bytes':8,
        'outgoing_bootarg':'androidboot.hwc=GLOBAL',
        'underlying_hardware_record_changed':False,
        'hwlevel_hwversion_cpuid_and_ptcountrycode_code_changed':False,
        'original_eng_lock_getter_and_setter_preserved':True,
        'force_unlock_or_fakelock_added':False,
        'v1_CN_Global_ENG_boot_key_policy_preserved':True,
        'avb_verification_or_rollback_disabled':False,
        'all_other_members_and_signed_der_and_padding_unchanged':True,
        'main_cert2_prefix_recomputed':True,'device_flashed':False,
        'limits':[
            'User explicitly requested trying HWC despite the original mismatch trigger being unresolved',
            'No claim that changing HWC fixes the Global recovery error',
            'Android must actually import the outgoing bootarg; its final ro.boot.hwc was not read back',
            'The HWC override applies to normal boot and recovery, and does not detect ROM region dynamically',
            'Use as a separate Global-ROM experiment; China-ROM behavior after overriding HWC is untested',
            'Other Android consumers may change behavior in response to the HWC value',
            'No phone flash/boot, stored lock change, efuse/RPMB write or Android modification'
        ]}


def main():
    output,report=build(INPUT.read_bytes())
    (HERE/NAME).write_bytes(output)
    (HERE/'manifest.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('artifact','output_sha256','bytes','status')},indent=2))


if __name__=='__main__':main()

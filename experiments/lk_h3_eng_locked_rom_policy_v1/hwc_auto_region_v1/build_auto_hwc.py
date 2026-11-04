"""One LK: choose outgoing HWC using the existing active-slot countrycode read."""
from pathlib import Path
import json
import struct
import sys

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[2]
sys.path.insert(0,str(ROOT/'fresh_analysis'))
from build_candidate import members,prefix,sha
sys.path.insert(0,str(HERE.parent/'hwc_global_v1'))
from build_hwc_global import adr

INPUT=HERE.parent/'hwc_global_v1/lk_hyperos3_ENG_STOCK_CN_GLOBAL_HWC_GLOBAL_TEST.img'
INPUT_SHA='9eabe1a50088d875348270908b5303b89dba0eb49ac40d3c5b09750b9a9605cb'
NAME='lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img'
CODE_SHA='6a1244b7f83ed6113dc4332aed40ef5f0cfa6bbc923a8fe3341953a975f47290'


def branch(at,to):
    delta=to-at
    if delta%4 or not -(1<<27)<=delta<(1<<27):raise ValueError('Branch out of range')
    return struct.pack('<I',0x14000000|((delta//4)&0x3ffffff))


def patches():
    code=(HERE/'auto_hwc.bin').read_bytes()
    if len(code)!=128 or sha(code)!=CODE_SHA:raise ValueError('Unexpected assembled late HWC block')
    return [(0x13364,code,'Skip early HWC emission; late in-function block selects region and emits HWC/level/version'),
            (0x13960,bytes.fromhex('ff7f00a9')+adr(0x13964,0xfaf36,0)+branch(0x13968,0x139b4),
             'Clear partial countrycode data on read failure, retaining original diagnostic/close path'),
            (0x139c0,branch(0x139c0,0x13368),
             'After every countrycode result, run the late block and resume at0x139c4')]


def build(source):
    if sha(source)!=INPUT_SHA:raise ValueError('Expected exact fixed-GLOBAL image reported booting by user')
    if source[512+0xc5954:512+0xc595c]!=b'GLOBAL\0\0' or source[512+0xf34d4:512+0xf34d7]!=b'CN\0':
        raise ValueError('HWC literals differ')
    if source[512+0x139c0:512+0x139c4]!=bytes.fromhex('e0030691'):
        raise ValueError('Unexpected common countrycode continuation')
    if source[512+0x13728:512+0x1372c]!=bytes.fromhex('ff7f00a9'):
        raise ValueError('Unexpected original countrycode zeroing')
    rows=members(source);out=bytearray(source);allowed=set();changes=[]
    for off,code,purpose in patches():
        before=source[512+off:512+off+len(code)]
        out[512+off:512+off+len(code)]=code
        allowed.update(range(512+off,512+off+len(code)))
        changes.append({'payload_offset':hex(off),'whole_file_offset':hex(off+512),
                        'old':before.hex(),'new':code.hex(),'bytes':len(code),'purpose':purpose})
    c=rows[2];c2=c['offset']+512
    if c['size']!=1162 or source[c2:c2+180]!=prefix(source[:512],source[512:512+rows[0]['size']]):
        raise ValueError('Existing hash prefix differs')
    out[c2:c2+180]=prefix(bytes(out[:512]),bytes(out[512:512+rows[0]['size']]))
    allowed.update(range(c2,c2+180))
    if any(a!=b and i not in allowed for i,(a,b) in enumerate(zip(source,out))):
        raise ValueError('Changed bytes outside declared code/prefix spans')
    for i,r in enumerate(rows):
        if i not in (0,2) and source[r['offset']:r['end']]!=out[r['offset']:r['end']]:
            raise ValueError('Other member changed')
    if source[c2+180:c['end']]!=out[c2+180:c['end']]:raise ValueError('Original DER/padding changed')
    result=bytes(out)
    return result,{
        'status':'EXPERIMENTAL_AUTO_REGION_NOT_DEVICE_BOOTED','artifact':NAME,
        'input_sha256':INPUT_SHA,'output_sha256':sha(result),'bytes':len(result),
        'input_user_reported_boot':True,'input_device_readback_hash_verified':False,
        'region_source':'Existing native active-slot countrycode partition read in function0x1320c',
        'mapping':{'cn_case_insensitive_exact':'CN','nonempty_non_cn_including_global_eea_tw':'GLOBAL',
                   'missing_empty_or_read_error':'original hardware HWC'},
        'metadata_read_path_preserved':True,'extra_partition_read_added':False,
        'hwc_emission':'Deferred until after countrycode read; one HWC/level/version emission',
        'hardware_cache_unchanged':True,'lock_and_key_trust_and_avb_policy_unchanged':True,
        'force_unlock_or_fakelock_added':False,'stack_frame_layout_unchanged':True,
        'patches':changes,'code_span_bytes':144,
        'all_other_members_and_signed_der_unchanged':True,'device_flashed':False,
        'limits':['No boot test of this new automatic variant',
                  'China/Global have actual vbmeta evidence; EEA/Taiwan region inputs are test fixtures until their images are supplied',
                  'EEA/Taiwan map to the already-tested international HWC value GLOBAL, not invented EEA/TW HWC codes',
                  'All nonempty non-CN country strings route to GLOBAL; this is a HWC selection rule, not an expanded signing-key allowlist',
                  'Previously allowed ENG/CN/GL keys are unchanged; another ROM key, model/layout or rollback incompatibility may still fail',
                  'HWC moves later in bootarg construction; actual Android import and all consumers remain unverified',
                  'No efuse, stored lock, RPMB, modem, preloader, Android or countrycode partition writes']}


def main():
    out,report=build(INPUT.read_bytes())
    (HERE/NAME).write_bytes(out);(HERE/'manifest.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps({k:report[k] for k in ('artifact','output_sha256','bytes','status')},indent=2))


if __name__=='__main__':main()

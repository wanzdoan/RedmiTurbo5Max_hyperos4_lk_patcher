"""Port H3 deep fakelock semantics onto boot-confirmed H4 policy v3 MTE_OFF.

Host files only. The output is internally unlocked; exported locked/green
values are synthetic. No stored lock, attestation certificate or preloader edit.
"""
from pathlib import Path
import json
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT/'fresh_analysis'))
from build_candidate import members, prefix, sha
sys.path.insert(0, str(ROOT/'experiments/lk_h3_eng_fakelock_v1'))
from build_fakelock import branch, adr, cbnz

BASE = ROOT/'experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img'
BASE_SHA = '6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52'
UNLOCKED = ROOT/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
UNLOCKED_SHA = 'f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
H3_REFERENCE = ROOT/'experiments/lk_h3_eng_mte_off_v1/fakelock_v2_auto_hwc/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_MTE_OFF_TEST.img'
H3_SHA = '9af2950d2df4d5e80905a501c0d1a823f22a97060dd287827d9c3ff7e58103a3'
NAME = 'lk_H4_FAKELOCK_V1_AUTO_HWC_MTE_OFF_TEST.img'
HELPER, FLASH_ARG = 0xc900, 0xc950


def public_region():
    start = 0xc8e0
    oem = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(start+8, 0xe8d08, 0)
           + branch(start+12, 0x9bec, True) + adr(start+16, 0xdf338, 0)
           + bytes.fromhex('fd7bc1a8bf2303d5') + branch(start+28, 0x9cdc))
    h = HELPER
    helper = (bytes.fromhex('3f2303d5fd7bbfa9800640f9')
              + adr(h+12, 0xf5cb2, 1) + branch(h+16, 0x7f0ac, True)
              + cbnz(h+20, h+48, nonzero=False)
              + bytes.fromhex('800640f9') + adr(h+28, 0xd254d, 1)
              + branch(h+32, 0x7f0ac, True) + cbnz(h+36, h+56, nonzero=False)
              + bytes.fromhex('800a40f9') + branch(h+44, h+60)
              + adr(h+48, 0x10401c, 0) + branch(h+52, h+60)
              + adr(h+56, 0x104020, 0)
              + bytes.fromhex('e40300aa830640f9fd7bc1a8bf2303d5c0035fd6'))
    literal = b'androidboot.flash.locked=1\0\0'
    assert len(oem) == 32 and len(helper) == 80 and len(literal) == 28
    return oem + helper + literal


def android_report():
    start = 0x69574
    code = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(start+8, 0xd1cc4, 0)
            + branch(start+12, 0x19078, True) + cbnz(start+16, start+28)
            + adr(start+20, FLASH_ARG, 0) + branch(start+24, 0x19078, True)
            + bytes.fromhex('fd7bc1a8bf2303d5c0035fd6'))
    assert len(code) == 40
    return code + bytes.fromhex('1f2003d5')*3


def operations(source, unlocked):
    # Addresses are independently mapped in H4; no H3 offset or code transplant.
    return [
        (0x89aec, source[512+0x89aec:512+0x89afc], unlocked[512+0x89aec:512+0x89afc], 'Internal force-unlocked3 getter, exact16 bytes from boot-confirmed H4 unlocked'),
        (0xc8e0, source[512+0xc8e0:512+0xc96c], public_region(), 'Public OEM lks reply, getvar-only formatter and flash.locked literal within old lks function'),
        (0xb0ec, bytes.fromhex('800a40f9'), branch(0xb0ec, HELPER, True), 'Single public getvar formatting; variable cache remains real'),
        (0xb118, bytes.fromhex('839240a9'), branch(0xb118, HELPER, True), 'getvar all formatting; variable cache remains real'),
        (0xbc7dc, bytes.fromhex('4201899a'), bytes.fromhex('e2030aaa'), 'AVB exported cmdline locked; internal unlocked flag remains real'),
        (0x69574, source[512+0x69574:512+0x695a8], android_report(), 'Export green and flash.locked1; internal boot-state getter/setter unchanged'),
        (0xc4db0, bytes.fromhex('e82f40b9'), bytes.fromhex('28008052'), 'Packet1 selects existing OEM-key hash instead of unlocked zero hash'),
        (0xc4e10, bytes.fromhex('e12f80b9'), bytes.fromhex('210080d2'), 'Packet2 exported lock1; local lock word remains real'),
        (0xc4e18, bytes.fromhex('02c149b9'), bytes.fromhex('02008052'), 'Packet2 exported boot-state0; global boot state remains real'),
        (0xc9a8, bytes.fromhex('a3c35fb8'), bytes.fromhex('23008052'), 'Public seccfg1 formatting; original read/error path and stored word retained'),
        (0x69638, bytes.fromhex('61010054'), branch(0x69638, 0x69664), 'Skip only orange UI warning; yellow/red verification-error handlers retained'),
    ]


def build(source, unlocked, h3_reference):
    if (sha(source), sha(unlocked), sha(h3_reference)) != (BASE_SHA, UNLOCKED_SHA, H3_SHA):
        raise ValueError('Exact booted H4 base, unlocked donor and latest H3 fakelock reference required')
    assert unlocked[512+0x89aec:512+0x89afc] == bytes.fromhex('68008052080000b900008052c0035fd6')
    assert source[512+0x34740:512+0x34744] == bytes.fromhex('11000014')
    assert source[512+0x10401c:512+0x104024] == b'no\0\0yes\0'
    rows = members(source)
    donor = members(unlocked)
    assert source[rows[3]['offset']:rows[5]['end']] == unlocked[donor[3]['offset']:donor[5]['end']]
    out = bytearray(source)
    allowed = set()
    changes = []
    for off, old, new, purpose in operations(source, unlocked):
        assert len(old) == len(new) and source[512+off:512+off+len(old)] == old, hex(off)
        out[512+off:512+off+len(new)] = new
        allowed.update(range(512+off, 512+off+len(new)))
        changes.append(dict(payload_offset=hex(off), whole_file_offset=hex(off+512), original=old.hex(), replacement=new.hex(), purpose=purpose))
    cert = rows[2]
    c = cert['offset']+512
    assert cert['size'] == 1162 and source[c:c+180] == prefix(source[:512], source[512:rows[0]['end']])
    out[c:c+180] = prefix(bytes(out[:512]), bytes(out[512:rows[0]['end']]))
    allowed.update(range(c, c+180))
    assert len(out) == len(source)
    assert all(a == b or i in allowed for i, (a,b) in enumerate(zip(source,out)))
    assert out[c+180:cert['end']] == source[c+180:cert['end']]
    for i,row in enumerate(rows):
        if i not in (0,2):assert out[row['offset']:row['end']] == source[row['offset']:row['end']]
    # Key policy/HWC, MTE-off, persistent setter, native verification and main
    # flash gates remain byte-identical to the booted policy base.
    protected = [(0xc5978,0xc5b18),(0x13b68,0x13bc4),(0x1410c,0x14178),
                 (0x34740,0x34744),(0xbc7ec,0xbc82c),(0x695a8,0x695d0),
                 (0x89afc,0x89fc0),(0x68c94,0x68d1c),(0x9a58,0x9bec),
                 (0xa090,0xa0d4),(0xce4c,0xce90),(0xd234,0xd264),(0x147ac,0x147d4)]
    for lo,hi in protected:assert out[512+lo:512+hi] == source[512+lo:512+hi], hex(lo)
    blob = bytes(out)
    return blob, dict(status='EXPERIMENTAL_OFFLINE_ONLY_NOT_DEVICE_BOOTED', artifact=NAME,
        input_sha256=sha(source), unlocked_donor_sha256=sha(unlocked), h3_reference_sha256=sha(h3_reference),
        output_sha256=sha(blob), bytes=len(blob), patches=changes,
        internal_lock_state=3, public_fastboot=dict(unlocked='no',secure='yes',lks=1,seccfg=1),
        exported_boot_values=dict(device_state='locked',verifiedbootstate='green',flash_locked=1),
        public_reporting_only_cache_preserved=True, root_packet_export=dict(lock=1,boot_state=0,existing_oem_key_hash=True),
        internal_verified_boot_state_preserved=True, hwc_auto_and_known_cn_gl_eng_keys_preserved=True,
        mte_policy='Inherited existing arm64.nomte path from boot-confirmed H4 MTE_OFF; not a preloader edit',
        bl2_origin='Exact signed H3 commercial/release triple retained from boot-confirmed H4 v3/f57',
        all_other_members_and_signed_der_unchanged=True, persistent_lock_write=False, device_actions='none',
        limits=['H4 release main with patches, not a complete H4 ENG build',
                'Exported locked/green are synthetic and do not establish genuine relock or verified software',
                'No trusted attestation chain, patch level or certification is created',
                'No runtime boot/root/banking/Integrity/OTA/Keystore compatibility confirmation for this new image'])


if __name__ == '__main__':
    image, report = build(BASE.read_bytes(), UNLOCKED.read_bytes(), H3_REFERENCE.read_bytes())
    (HERE/NAME).write_bytes(image)
    (HERE/'manifest.json').write_text(json.dumps(report, indent=2)+'\n', encoding='utf-8')
    print(NAME, sha(image), len(image))

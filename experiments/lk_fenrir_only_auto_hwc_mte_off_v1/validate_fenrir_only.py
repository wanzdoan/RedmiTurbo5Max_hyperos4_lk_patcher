"""Validate minimal Fenrir output; no code RAM patch, device IO or flash."""
import itertools
import json
import struct
import sys
from build_fenrir_only import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/main_analysis'))
from probe_main import machine, run, ret, cstr, getter, fastboot_publish, avb_flags
from probe_main import B, DATA, STOP
sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review'))
from probe_root_handoff import native_case as handoff_case
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_v2_hybrid_v1/stock_hwc'))
from hwc_patch import check_external_branches
from validate_hwc import cases_for
sys.path.insert(0, str(ROOT / 'fresh_analysis/preloader'))
from emulate_bl2_parser import run as parser_run


def mte_case(image, flags, reserved, off):
    u, saved = machine(image)
    assert bytes(u.mem_read(B + 0x344a8, 4)) == bytes.fromhex('11000014' if off else '20020054')
    mmio = 0xffff00000c2cf5f4
    u.mem_map(mmio & ~0xfff, 0x1000); u.mem_write(mmio, struct.pack('<I', 0x12345678))
    u.mem_write(DATA, struct.pack('<III', 0, 0, flags)); u.reg_write(UC_ARM64_REG_X0, DATA)
    outputs = []; lookups = []
    def hook(uc, addr, size, _):
        pos = addr - B
        if pos == 0x7de54: ret(uc)
        elif pos == 0x1a204:
            name = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)); lookups.append(name)
            assert name == 'security_booker_tag'; ret(uc, DATA + 0x100 if reserved else 0)
        elif pos == 0x18f34:
            outputs.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret(uc)
        elif pos == 0x7f434:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2)); assert fmt == 'kasan.page_alloc.sample=%u'
            text = fmt % uc.reg_read(UC_ARM64_REG_W3)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode() + b'\0'); ret(uc, len(text))
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)
    u.hook_add(UC_HOOK_CODE, hook)
    run(u, 0x34430, saved)
    assert int.from_bytes(u.mem_read(B + 0x16da28, 4), 'little') == flags
    u.reg_write(UC_ARM64_REG_LR, STOP); run(u, 0x3445c, saved)
    suppressed = off or not reserved or not flags & 6
    expected = ['arm64.nomte'] if suppressed else (
        ['kasan.stacktrace=on kasan.stack_ring_size=524288'] +
        ([f'kasan.page_alloc.sample={1 << ((flags >> 8) & 15)}'] if flags & 0xf00 else [])
        if flags & 4 else ['kasan=off'])
    assert outputs == expected and lookups == ['security_booker_tag']
    assert int.from_bytes(u.mem_read(mmio, 4), 'little') == (0x12345670 | (14 if suppressed else 15))
    assert int.from_bytes(u.mem_read(B + 0x16da28, 4), 'little') == flags
    return dict(final_mte_off=off, flags_fixture=hex(flags), reserved_memory=reserved,
                outputs=outputs, original_mte_info_preserved=True, code_ram_modified=False,
                booker_low_nibble=14 if suppressed else 15)


def main():
    source = INPUT.read_bytes(); image = (HERE / NAME).read_bytes()
    assert build(source)[0] == image == build(source)[0]
    bad = bytearray(source); bad[12345] ^= 1
    try: build(bytes(bad))
    except ValueError: pass
    else: raise AssertionError('Wrong input accepted')
    hwc = cases_for('stock', image)
    assert check_external_branches(source, 'stock')
    mte = [mte_case(blob, flags, reserved, off)
           for blob, off in ((source, False), (image, True))
           for flags, reserved in itertools.product(tuple(range(8)) + (0x405, 0x407, 0xf07, 0xffffffff), (False, True))]
    core = [getter(image, state) for state in (0, 1, 2, 3, 4, 0xffffffff)]
    core += [fastboot_publish(image, existing) for existing in (False, True)]
    core.append(avb_flags(image))
    roots = [handoff_case(image, True, state, 3, key_error=keyerr, smc_error=err)
             for state, keyerr, err in itertools.product((0, 1, 2, 3, 0xffffffff), (0, 9), (0, 5))]
    rows = members(image); certs = []; parsers = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = image[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[90:] if i in (2, 8) else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append(dict(member=i, retained_original_der_signature_valid=True))
    for ci, component in ((2, 0), (8, 6)):
        r = rows[ci]; comp = rows[component]; record = image[r['offset']:r['offset'] + 512 + r['size']]
        for mask in (0x60, 0x1f4e2, 0x1f400):
            got = parser_run(3, 'fenrir-minimal-final', mask, record)
            assert got['returned'] and got['error'] is None and int(got['status'], 16) == 0
            if mask != 0x1f400:
                assert got['payload_hash'] == sha(image[comp['offset'] + 512:comp['end']])
                assert got['header_hash'] == sha(image[comp['offset']:comp['offset'] + 512])
            parsers.append(dict(component=component, **got))
    report = dict(status='PASS_FENRIR_ONLY_BOUNDED_NATIVE_NOT_DEVICE_BOOT',
                  input_sha256=INPUT_SHA, output_sha256=sha(image), deterministic_build=True,
                  wrong_input_rejected=True, hwc_cases=hwc, mte_cases=mte,
                  retained_fenrir_core_cases=core, retained_root_handoff_cases=roots,
                  native_bl2_parser_cases=parsers, retained_der_signatures=certs,
                  counts=dict(hwc=len(hwc), mte_baseline_and_off=len(mte), fenrir_core=len(core),
                              root_handoff=len(roots), bl2_parser=len(parsers)),
                  no_extra_fakelock_or_boot_policy_changes=True,
                  limits=['No phone flash/boot/root or actual kernel MTE/HWCAP observation',
                          'MTE MMIO is emulator RAM; storage/slot/formatter/log and secure-monitor boundaries modeled',
                          'MTE-off requests kernel suppression, not reversal of earlier preloader execution',
                          'All Fenrir lock/AVB/maintenance/report behavior retained, including its original tradeoffs'])
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(report['status'], report['output_sha256'], report['counts'], '10 DER PASS')


if __name__ == '__main__': main()

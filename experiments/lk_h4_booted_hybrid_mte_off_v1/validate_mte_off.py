"""Execute native H4 MTE and retained unlock code on final bytes; no device IO."""
import itertools
import json
import random
import struct
import sys
from build_mte_off import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/main_analysis'))
from probe_main import machine, run, ret, cstr, B, DATA, STOP
sys.path.insert(0, str(ROOT / 'fresh_analysis/preloader'))
from emulate_bl2_parser import run as parser_run


def mte_case(image, bits, reserved, off):
    u, saved = machine(image)
    assert bytes(u.mem_read(B + OFFSET, 4)) == (NEW if off else OLD)
    mmio = 0xffff00000c2cf5f4
    u.mem_map(mmio & ~0xfff, 0x1000); u.mem_write(mmio, struct.pack('<I', 0x12345678))
    u.mem_write(DATA, struct.pack('<III', 0, 0, bits)); u.reg_write(UC_ARM64_REG_X0, DATA)
    outputs = []; lookup = []
    def hook(uc, address, size, _):
        pos = address - B
        if pos == 0x7e11c: ret(uc)
        elif pos == 0x1a348:
            name = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)); lookup.append(name)
            assert name == 'security_booker_tag'; ret(uc, DATA + 0x100 if reserved else 0)
        elif pos == 0x19078:
            outputs.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret(uc)
        elif pos == 0x7f6fc:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2)); assert fmt == 'kasan.page_alloc.sample=%u'
            text = fmt % uc.reg_read(UC_ARM64_REG_W3)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode() + b'\0'); ret(uc, len(text))
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)
    u.hook_add(UC_HOOK_CODE, hook); run(u, 0x346c8, saved)
    assert int.from_bytes(u.mem_read(B + 0x16dac0, 4), 'little') == bits
    u.reg_write(UC_ARM64_REG_LR, STOP); run(u, 0x346f4, saved)
    suppressed = off or not reserved or not bits & 6
    expected = ['arm64.nomte'] if suppressed else (
        ['kasan.stacktrace=on kasan.stack_ring_size=524288'] +
        ([f'kasan.page_alloc.sample={1 << ((bits >> 8) & 15)}'] if bits & 0xf00 else [])
        if bits & 4 else ['kasan=off'])
    assert outputs == expected and lookup == ['security_booker_tag']
    assert int.from_bytes(u.mem_read(mmio, 4), 'little') == (0x12345670 | (14 if suppressed else 15))
    assert int.from_bytes(u.mem_read(B + 0x16dac0, 4), 'little') == bits
    return dict(mte_off=off, bits_fixture=hex(bits), reserved_memory_present=reserved,
                emitted_bootargs=outputs, booker_low_nibble=14 if suppressed else 15,
                original_mte_info_preserved=True, code_ram_modified=False)


def unlock_case(image, entry, initial, boot=0, control=None):
    u, saved = machine(image); u.mem_write(DATA, struct.pack('<I', initial))
    u.mem_write(DATA + 4, b'guard')
    u.reg_write(UC_ARM64_REG_X0, boot if entry == 0x68c94 else DATA)
    def hook(uc, address, size, _):
        off = address - B
        if off in (0x7e11c, 0x8871c): ret(uc)
        elif off == 0x89aec and control is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', control)); ret(uc)
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)
    u.hook_add(UC_HOOK_CODE, hook); run(u, entry, saved)
    if entry == 0x68c94:
        assert u.reg_read(UC_ARM64_REG_W0) == int(control not in (None, 3))
    else:
        assert u.reg_read(UC_ARM64_REG_W0) == 0 and int.from_bytes(u.mem_read(DATA, 4), 'little') == 3
        assert bytes(u.mem_read(DATA + 4, 5)) == b'guard'
    return dict(entry=hex(entry), initial=initial, boot_fixture=boot, mocked_state_control=control,
                result=u.reg_read(UC_ARM64_REG_W0), pass_case=True)


def main():
    source = INPUT.read_bytes(); out = (HERE / NAME).read_bytes()
    assert build(source)[0] == out == build(source)[0]
    wrong = bytearray(source); wrong[12345] ^= 1
    try: build(bytes(wrong))
    except ValueError: pass
    else: raise AssertionError('Wrong input accepted')
    mte = [mte_case(image, bits, reserved, off)
           for image, off in ((source, False), (out, True))
           for bits, reserved in itertools.product(tuple(range(8)) + (0x405, 0x407, 0xf07, 0xffffffff), (False, True))]
    rng = random.Random(9500)
    unlock = [unlock_case(out, entry, initial)
              for initial in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]
              for entry in (0x89aec, 0x89020, 0x88a10)]
    unlock += [unlock_case(out, 0x68c94, 0, boot, control)
               for boot, control in itertools.product((0, 1, 2, 0xffffffff), (None, 0, 1, 2, 3, 4, 0xffffffff))]
    rows = members(out); certs = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = out[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[180:] if i == 2 else raw; end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        if row['name'] == 'cert2':
            comp = rows[i - 2]; header = out[comp['offset']:comp['offset'] + 512]
            payload = out[comp['offset'] + 512:comp['end']]
            h = bytes.fromhex('060760867693160204'); p = bytes.fromhex('060760867693160201')
            assert der[der.index(h) + 12:der.index(h) + 44].hex() == sha(header)
            assert (der[der.index(p) + 12:der.index(p) + 44].hex() == sha(payload)) == (i != 2)
        certs.append(dict(member=i, original_der_signature_valid=True))
    c = rows[2]; record = out[c['offset']:c['offset'] + 512 + c['size']]
    parsers = []
    for mask in (0x60, 0x1f4e2, 0x1f400):
        got = parser_run(3, 'h4-booted-hybrid-mte-off', mask, record)
        assert got['returned'] and got['error'] is None and int(got['status'], 16) == 0
        if mask != 0x1f400:
            assert got['payload_hash'] == sha(out[512:rows[0]['end']]) and got['header_hash'] == sha(out[:512])
        parsers.append(got)
    assert out[rows[3]['offset'] + 512:rows[3]['offset'] + 512 + rows[3]['size']] == (ROOT / 'fresh_analysis/container_chain/h3_original_03_bl2_ext.bin').read_bytes()
    report = dict(status='PASS_BOUNDED_H4_MTE_AND_RETAINED_UNLOCK_NOT_DEVICE_BOOT',
                  input_sha256=INPUT_SHA, output_sha256=sha(out), deterministic_build=True, wrong_input_rejected=True,
                  native_mte_cases=mte, native_unlock_and_control_cases=unlock,
                  retained_der_signatures=certs, actual_h3_bl2_parser_cases=parsers,
                  counts=dict(mte_baseline_and_off=len(mte), unlock_and_controls=len(unlock), bl2_parser=len(parsers)),
                  original_boot_confirmed_input_unchanged=True,
                  limits=['Source runtime success is user reported; this modified file not booted on device',
                          'MTE MMIO, reserved lookup, bootarg/log/formatter, boot selector and PAC modeled',
                          'Getter mocked only in labelled negative AVB controls; actual unlock code runs otherwise',
                          'No kernel/HWCAP, preloader, secure-world, device flash or complete boot simulation',
                          'DER verification is against embedded keys; main signed digest differs by original hybrid design'])
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(report['status'], report['counts'], report['output_sha256'], '10 DER PASS')


if __name__ == '__main__': main()

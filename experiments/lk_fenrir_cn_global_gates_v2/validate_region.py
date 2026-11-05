"""Reproduce retained China restrictions and validate narrowly patched gates."""
import itertools
import json
import struct
import sys
from build_region import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_global_region_v1'))
from validate_h3_region_lk import Replay, B, DATA, STOP
sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/main_analysis'))
from probe_main import machine, ret, cstr, run
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_v2_hybrid_v1/stock_hwc'))
from validate_hwc import cases_for
sys.path.insert(0, str(ROOT / 'experiments/lk_fenrir_only_auto_hwc_mte_off_v1'))
from validate_fenrir_only import mte_case
sys.path.insert(0, str(ROOT / 'fresh_analysis/preloader'))
from emulate_bl2_parser import run as parser_run


def body(image): return image[:members(image)[-1]['end']]


def secure_id_region(image, sku, hwc, reboot_status):
    u, saved = machine(image); recovery = []
    # Native region helper compares the software-SKU pointer with the HW string.
    u.mem_write(B + 0x1399d0, struct.pack('<Q', DATA + 0x1000))
    u.mem_write(DATA + 0x1000, struct.pack('<Q', DATA + 0x1100))
    u.mem_write(DATA + 0x1100, sku.encode() + b'\0')
    u.mem_write(DATA + 0x1200, hwc.encode() + b'\0')
    u.reg_write(UC_ARM64_REG_X0, DATA + 0x1200)
    def hook(uc, addr, size, _):
        off = addr - B
        if off == 0x7de54: ret(uc)
        elif off == 0x87370: recovery.append(True); ret(uc, reboot_status)
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)
    u.hook_add(UC_HOOK_CODE, hook); run(u, 0x87050, saved)
    return bool(recovery), u.reg_read(UC_ARM64_REG_W0)


def main():
    original = INPUT.read_bytes(); image = (HERE / NAME).read_bytes()
    assert build(original)[0] == image == build(original)[0]
    wrong = bytearray(original); wrong[12345] ^= 1
    try: build(bytes(wrong))
    except ValueError: pass
    else: raise AssertionError('Wrong source accepted')
    cases = []
    configs = [(0, None), (0, 'yes'), (1, 'yes')]
    for patched, raw in ((False, original), (True, image)):
        code = body(raw)
        for sb, hw, sku, cfg, error in itertools.product((0, 14), (0, 1),
                ('CN', 'CN_TEST', 'NATIVE', 'GLOBAL', 'GL', 'POCO', 'EEA', 'TW', 'IN', ''), configs, (0, 7)):
            active = sb == 14 and cfg != (1, 'yes')
            expected = 'panic' if active and (error or (not patched and hw == 0 and not sku.startswith(('CN', 'NATIVE')))) else 'continued'
            r = Replay(code, sb=sb, hw=hw, sku=sku, dconfig=cfg[0], config=cfg[1], efuse_error=error, block=True)
            got = r.run(0x68c88, False)
            assert got == expected, ('sku', patched, sb, hw, sku, cfg, error, got, expected)
            cases.append(dict(gate='init_boot_sku', patched=patched, secure_boot_fixture=sb,
                              region_fixture=hw, sku_fixture=sku, dconfig_fixture=cfg,
                              efuse_error_fixture=error, outcome=got))
        cn = code[512 + 0x111faf:512 + 0x1120af]; gl = code[512 + 0x1120af:512 + 0x1121af]
        for sb, hw, keykind, chain, extract, fuse in itertools.product((0, 14), (0, 1),
                ('CN', 'GL', 'unknown'), (0, 0x1234), (0, 0x321), (0, 7)):
            key = {'CN': cn, 'GL': gl, 'unknown': bytes(256)}[keykind]
            expected = chain or (0 if sb != 14 else ('panic' if fuse else (extract or
                        (0 if keykind == ('CN' if hw == 0 else 'GL') or (patched and keykind == 'GL')
                         else (0xffffffff if patched or hw == 0 else 0)))))
            r = Replay(code, sb=sb, hw=hw, key=key, chain_error=chain, key_error=extract, efuse_error=fuse)
            r.u.reg_write(UC_ARM64_REG_X1, DATA + 0x800); r.u.mem_write(DATA + 0x800, b'md1rom\0')
            got = r.run(0x88474)
            assert got == expected, ('image-key', patched, sb, hw, keykind, chain, extract, fuse, got, expected)
            assert r.calls[0] == 'certificate_chain_verify'
            cases.append(dict(gate='image_key', patched=patched, secure_boot_fixture=sb, region_fixture=hw,
                              key_fixture=keykind, chain_error_fixture=chain, key_error_fixture=extract,
                              efuse_error_fixture=fuse, outcome=got))
        for sb, hw, keykind, bits in itertools.product((0, 14), (0, 1), ('CN', 'GL', 'unknown'), (2048, 4096)):
            key = code[512 + (0x138638 if keykind == 'CN' else 0x138838):512 + (0x138838 if keykind == 'CN' else 0x138a38)] if keykind != 'unknown' else bytes(512)
            r = Replay(code, sb=sb, hw=hw)
            r.u.mem_write(DATA + 0x800, struct.pack('>I', bits) + bytes(4) + key)
            r.u.mem_write(DATA + 0x700, b'\xa5'); r.u.reg_write(UC_ARM64_REG_X1, DATA + 0x800)
            r.u.reg_write(UC_ARM64_REG_X5, DATA + 0x700)
            status = r.run(0xc5304); trusted = r.u.mem_read(DATA + 0x700, 1)[0]
            expected = bits == 4096 and (keykind in ('CN', 'GL') if patched or sb != 14 else keykind == ('CN' if hw == 0 else 'GL'))
            assert status == 0 and trusted == int(expected)
            cases.append(dict(gate='avb_key', patched=patched, secure_boot_fixture=sb, region_fixture=hw,
                              key_fixture=keykind, bits=bits, trusted=trusted))
        for sku, hwc, reboot_status in itertools.product(('CN', 'GLOBAL', 'POCO', 'EEA', 'TW', ''), ('CN', 'GLOBAL'), (0, 7)):
            called, status = secure_id_region(raw, sku, hwc, reboot_status)
            expected = not patched and not sku.startswith('CN') and hwc.startswith('CN')
            assert called == expected and status == (reboot_status if expected else 0)
            cases.append(dict(gate='secure_id_region_recovery', patched=patched, sku_fixture=sku,
                              hwc_fixture=hwc, reboot_status_fixture=reboot_status,
                              region_recovery_called=called, outcome=status))
    hwc = cases_for('stock', image)
    mte = [mte_case(image, flags, reserved, True)
           for flags, reserved in itertools.product(tuple(range(8)) + (0x405, 0x407, 0xf07, 0xffffffff), (False, True))]
    certs = []; rows = members(image)
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = image[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[90:] if i in (2, 8) else raw; end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append(dict(member=i, retained_signature_valid=True))
    cert = rows[2]; record = image[cert['offset']:cert['offset'] + 512 + cert['size']]
    parsers = []
    for mask in (0x60, 0x1f4e2, 0x1f400):
        got = parser_run(3, 'fenrir-cn-global-final', mask, record)
        assert got['returned'] and got['error'] is None and int(got['status'], 16) == 0
        if mask != 0x1f400:
            assert got['payload_hash'] == sha(image[512:rows[0]['end']]) and got['header_hash'] == sha(image[:512])
        parsers.append(got)
    report = dict(status='PASS_BOUNDED_CN_GLOBAL_GATES_NOT_POCO_BOOT_PROOF', output_sha256=sha(image),
                  input_sha256=INPUT_SHA, deterministic_build=True, wrong_input_rejected=True,
                  gate_cases=cases, hwc_cases=hwc, mte_cases=mte, retained_der_signatures=certs,
                  native_bl2_parser_cases=parsers,
                  counts=dict(region_gate_cases=len(cases), hwc=len(hwc), mte=len(mte), bl2_parser=len(parsers)),
                  limits=['No actual POCO ROM package/public keys supplied; POCO SKU tests are fixtures',
                          'Secure boot/region efuse,dconfig,chain status/key extraction and recovery-call boundary mocked',
                          'Native memcmp/strcmp/memcpy, branches and epilogues execute; full Secure-ID crypto is not bypassed or emulated here',
                          'Preloader-only failure stage not proven; no physical flash/boot/root performed',
                          'Global image-key fallback changes unknown-key mismatch behavior on non-CN hardware to rejection'])
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(report['status'], report['counts'], report['output_sha256'])


if __name__ == '__main__': main()

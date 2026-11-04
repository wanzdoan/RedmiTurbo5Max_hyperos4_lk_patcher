"""Verify captured device-specific Secinfo privately; emit booleans/lengths only.

No ADB/device I/O. Actual stock TLV matcher code runs in host Unicorn.
"""
from pathlib import Path
import hashlib
import json
import struct
from cryptography.hazmat.primitives.asymmetric import ec, utils
from cryptography.hazmat.primitives import hashes
from cryptography.exceptions import InvalidSignature
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000


def native_match(image, entry, logger, record, expected, tag):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(B, 0x400000)
    main_size = struct.unpack_from('<I', image, 4)[0]
    u.mem_write(B, image[512:512 + main_size])
    u.mem_map(STACK, 0x10000)
    u.mem_map(DATA, 0x10000)
    u.mem_map(STOP, 0x1000)
    u.mem_write(DATA, record)
    u.mem_write(DATA + 0x2000, expected + b'\0')
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    u.reg_write(UC_ARM64_REG_X0, DATA + 0x2000)
    u.reg_write(UC_ARM64_REG_X1, DATA)
    u.reg_write(UC_ARM64_REG_W2, tag)

    def hook(uc, addr, length, user):
        if addr == B + logger:
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + entry, STOP, count=100000)
    if u.reg_read(UC_ARM64_REG_PC) != STOP or u.reg_read(UC_ARM64_REG_SP) != STACK + 0xf000:
        raise AssertionError('Stock TLV matcher did not return normally')
    return u.reg_read(UC_ARM64_REG_W0)


def main():
    snapshot = json.loads((HERE / 'live_readonly_snapshot.json').read_text())
    private = Path(snapshot['private_raw_directory'])
    raw = (private / 'secinfo.bin').read_bytes()
    stock3 = (ROOT / 'lk_hyperos3_original/lk.img').read_bytes()
    stock4 = (ROOT / 'lk_hyperos4_original/lk.img').read_bytes()
    actual_magic = stock3[512 + 0xffc83:512 + 0xffc83 + 13]
    if raw[:13] != actual_magic:
        raise ValueError('Captured partition magic does not match literal bytes of stock H3')
    record = raw[0x40f:0x40f + 2048]
    count = struct.unpack_from('<I', record, 10)[0]
    signed_length = count + 14
    if not 2 <= count or signed_length + 64 > len(record):
        raise ValueError('Captured signed record is out of bounds')
    signature = record[signed_length:signed_length + 64]
    signature_der = utils.encode_dss_signature(int.from_bytes(signature[:32], 'big'), int.from_bytes(signature[32:], 'big'))
    keys = []
    for label, image, offset in [('H3_stock', stock3, 0x138a5c), ('H4_stock', stock4, 0x138a7c)]:
        point = image[512 + offset:512 + offset + 64]
        pub = ec.EllipticCurvePublicNumbers(int.from_bytes(point[:32], 'big'), int.from_bytes(point[32:], 'big'), ec.SECP256R1()).public_key()
        pub.verify(signature_der, record[:signed_length], ec.ECDSA(hashes.SHA256()))
        for bad_data, bad_sig in [(bytes([record[0] ^ 1]) + record[1:signed_length], signature_der),
                                  (record[:signed_length], utils.encode_dss_signature(int.from_bytes(signature[:32], 'big') ^ 1, int.from_bytes(signature[32:], 'big')))]:
            try:
                pub.verify(bad_sig, bad_data, ec.ECDSA(hashes.SHA256()))
            except InvalidSignature:
                pass
            else:
                raise AssertionError('Tamper control unexpectedly verified')
        keys.append({'source': label, 'curve': 'P-256', 'public_xy_sha256': hashlib.sha256(point).hexdigest(),
                     'captured_signature_valid': True, 'two_tamper_controls_rejected': True})
    tags, layout, pos = {}, [], 14
    while pos < signed_length:
        if pos + 2 > signed_length:
            raise ValueError('Truncated TLV')
        tag, length = record[pos], record[pos + 1]
        value = record[pos + 2:pos + 2 + length]
        if not tag or pos + 2 + length > signed_length or tag in tags:
            raise ValueError('Invalid/duplicate TLV')
        tags[tag] = value
        layout.append({'tag': hex(tag), 'bytes': length})
        pos += 2 + length
    boot = {}
    for line in (private / 'bootconfig.txt').read_text(errors='replace').splitlines():
        key, sep, value = line.partition('=')
        if sep:
            boot[key.strip()] = value.strip().strip('"').encode()
    results, matches = [], []
    fields = [(0x41, 'androidboot.cpuid'), (0x43, 'androidboot.hwc'), (0x42, 'androidboot.hwlevel')]
    for tag, key in fields:
        expected = boot[key]
        if tag not in tags or expected != tags[tag]:
            raise ValueError('Device binding does not match bootconfig; raw values suppressed')
        matches.append({'tag': hex(tag), 'bootconfig_key': key, 'bytes': len(expected), 'exact_match': True})
        for label, code, entry, logger in [('H3_stock', stock3, 0x86e88, 0x7de54),
                                          ('H4_stock', stock4, 0x87150, 0x7e11c)]:
            for altered in (False, True):
                control = bytes([expected[0] ^ 1]) + expected[1:] if altered else expected
                result = native_match(code, entry, logger, record, control, tag)
                if (result == 0) == altered:
                    raise AssertionError('Native binding matcher disagrees')
                results.append({'source': label, 'tag': hex(tag), 'altered_expected_binding': altered, 'return': result})
    report = {'status': 'PASS_CAPTURED_PARTITION_FALLBACK_SIGNATURE_AND_BINDINGS',
              'device_specific_values_emitted': False, 'partition_sha256': hashlib.sha256(raw).hexdigest(),
              'record_offset': '0x40f', 'tlv_data_bytes': count, 'signed_bytes': signed_length,
              'signature_bytes': 64, 'stock_magic_matches': True,
              'signature_checks': keys, 'tlv_layout': layout, 'mandatory_bindings': matches,
              'native_matcher_cpu_cases': len(results), 'native_matcher_results': results,
              'full_stock_secure_id_gate_confirmed': False,
              'limits': ['Primary RPMB Secure ID not read; a valid but wrongly-bound primary record need not fall back',
                         'Bindings compared to current LK-generated kernel bootconfig, not independent fuse/attestation measurement',
                         'No direct RPMB/efuse call, live secure-world operation, full boot or lock command',
                         'Applies to the current phone only, not the friend device']}
    (HERE / 'live_secinfo_validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'stock_magic_matches', 'native_matcher_cpu_cases', 'mandatory_bindings')}, indent=2))


if __name__ == '__main__':
    main()

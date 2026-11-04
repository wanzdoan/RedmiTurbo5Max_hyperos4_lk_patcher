"""Validate the stock-derived token-key replica, without accessing the device."""
import json
import struct
from pathlib import Path
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_token_key_clone import HERE, STOCK, REFERENCE, NAME, build, records, payload, hash_prefix, sha


def parser_replay(code, cert_record, mask):
    base, data, stack, stop = 0xffff0000b8000000, 0x3000000, 0x3100000, 0x3200000
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(base, 0x300000)
    u.mem_write(base, code)
    u.mem_map(data, 0x30000)
    u.mem_write(data, cert_record)
    u.mem_map(stack, 0x10000)
    u.mem_map(stop, 0x1000)

    def hook(uc, addr, size, user):
        if addr == base + 0x4beb0:
            uc.reg_write(UC_ARM64_REG_W0, 0)
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    for reg, value in [(UC_ARM64_REG_SP, stack + 0xf000), (UC_ARM64_REG_LR, stop),
                       (UC_ARM64_REG_X0, data), (UC_ARM64_REG_X1, len(cert_record)),
                       (UC_ARM64_REG_X2, mask)]:
        u.reg_write(reg, value)
    u.emu_start(base + 0x6cf8c, stop, count=1500000)
    if u.reg_read(UC_ARM64_REG_PC) != stop:
        raise AssertionError('BL2 parser did not return')
    return {'mask': hex(mask), 'status': u.reg_read(UC_ARM64_REG_W0),
            'header_hash': bytes(u.mem_read(base + 0x1b7d1c, 32)).hex(),
            'payload_hash': bytes(u.mem_read(base + 0x1b7ce8, 32)).hex()}


def main():
    stock, reference = STOCK.read_bytes(), REFERENCE.read_bytes()
    output = (HERE / NAME).read_bytes()
    if build(stock, reference)[0] != output or output != reference:
        raise AssertionError('Output is not the exact current-device container')
    bad = bytearray(stock)
    bad[512] ^= 1
    try:
        build(bytes(bad), reference)
    except ValueError:
        pass
    else:
        raise AssertionError('Modified source accepted')
    rows, original_rows = records(output), records(stock)
    certificates = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'):
            continue
        raw = payload(output, row)
        prefix_bytes = 90 if i in (2, 8) else 0
        der = raw[prefix_bytes:]
        if der != payload(stock, original_rows[i]):
            raise AssertionError('Original signed DER changed')
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        if prefix_bytes:
            component = rows[i - 2]
            if raw[:90] != hash_prefix(output[component['offset']:component['offset'] + 512], payload(output, component)):
                raise AssertionError('Prefix hash mismatch')
        certificates.append({'member': i, 'prefix_bytes': prefix_bytes,
                             'stock_der_identical': True, 'retained_der_signature_valid': True})
    bl2 = payload(output, rows[3])
    if sha(bl2) != 'e9aca46a63376b22daf9150319e27c8fc2da1e176781af8bc1392da4f178e76b':
        raise AssertionError('Unexpected native BL2')
    parser_cases = []
    for index in (0, 6):
        comp, cert = rows[index], rows[index + 2]
        record = output[cert['offset']:cert['offset'] + 512 + cert['size']]
        hh = sha(output[comp['offset']:comp['offset'] + 512])
        ph = sha(output[comp['offset'] + 512:comp['end']])
        for mask in (0x60, 0x1f4e2, 0x1f400):
            result = parser_replay(bl2, record, mask)
            if result['status'] != 0 or (mask != 0x1f400 and (result['header_hash'] != hh or result['payload_hash'] != ph)):
                raise AssertionError('BL2 did not select the actual component hashes')
            result.update(component=comp['name'], control=False)
            parser_cases.append(result)
        altered = bytearray(record)
        altered[512 + 58] ^= 1
        wanted = bytes([bytes.fromhex(ph)[0] ^ 1]) + bytes.fromhex(ph)[1:]
        result = parser_replay(bl2, bytes(altered), 0x60)
        if result['status'] != 0 or result['payload_hash'] != wanted.hex():
            raise AssertionError('Prefix-selection control failed')
        result.update(component=comp['name'], control=True)
        parser_cases.append(result)
    report = {'status': 'PASS', 'output_sha256': sha(output), 'output_bytes': len(output),
              'reconstructed_from_stock': True, 'byte_identical_to_current_device_container': True,
              'stock_code_unchanged': True, 'wrong_input_rejected': True,
              'certificates': certificates, 'native_bl2_parser_cases': parser_cases,
              'limits': ['No token read, signed token creation, RPMB write or device flashing',
                         'Existing private key is unavailable; a new valid token cannot be issued',
                         'Same binary as readback is not a separate new hardware boot test',
                         'Parser emulation bypasses PAC/logging; it is not full boot-chain simulation']}
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'status': 'PASS', 'sha256': sha(output), 'bytes': len(output),
                      'exact_reference_match': True, 'valid_retained_der_signatures': len(certificates),
                      'native_bl2_parser_cases': len(parser_cases)}, indent=2))


if __name__ == '__main__':
    main()

"""Execute actual ENG getter/callers and its own unchanged BL2 parser/policy.

This is bounded ARM64 replay, not a full device boot or physical unlock test.
"""
import json
import random
import struct
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_eng_unlock import HERE, ROOT, INPUT, INPUT_SHA, NAME, OFFSET, PATCH, build, members, prefix, sha

B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000
BL2 = 0xffff0000b8000000
SAVED = [UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21,
         UC_ARM64_REG_X22, UC_ARM64_REG_X23, UC_ARM64_REG_X24,
         UC_ARM64_REG_X25, UC_ARM64_REG_X26, UC_ARM64_REG_X27,
         UC_ARM64_REG_X28, UC_ARM64_REG_X29]


def cstr(u, address):
    value = bytearray()
    for i in range(256):
        char = bytes(u.mem_read(address + i, 1))
        if char == b'\0':
            return bytes(value)
        value += char
    raise ValueError('Unterminated replay string')


def main_case(image, entry, initial, kind, boot=0, mock_state=None):
    rng = random.Random(9500 + initial)
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(B, 0x400000)
    u.mem_write(B, image[512:512 + members(image)[0]['size']])
    u.mem_map(STACK, 0x10000)
    u.mem_map(DATA, 0x1000)
    u.mem_map(STOP, 0x1000)
    pattern = rng.randbytes(0x1000)
    u.mem_write(DATA, pattern)
    ptr = DATA + 0x80
    u.mem_write(ptr, struct.pack('<I', initial))
    saved = {reg: rng.getrandbits(64) for reg in SAVED}
    for reg, value in saved.items():
        u.reg_write(reg, value)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    u.reg_write(UC_ARM64_REG_X0, boot if kind == 'avb' else ptr)
    info, formats, failures = [], [], []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0, value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, user):
        off = addr - B
        if off in (0x7ea84, 0x8850c):
            ret()
        elif off == 0x896e4 and mock_state is not None:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', mock_state))
            ret()
        elif off == 0x7ffe0:
            text = cstr(uc, uc.reg_read(UC_ARM64_REG_X2))
            if b'%' in text:
                raise ValueError('Unexpected format in focused fastboot probe')
            formats.append(text.decode())
            limit = uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text[:limit - 1] + b'\0')
            ret(len(text))
        elif off in (0x9cfc, 0x9dec, 0x9dac):
            text = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)).decode()
            if off == 0x9cfc:
                info.append(text)
            elif off == 0x9dac:
                failures.append(text)
            ret()
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + entry, STOP, count=20000)
    if u.reg_read(UC_ARM64_REG_PC) != STOP or u.reg_read(UC_ARM64_REG_SP) != STACK + 0xf000:
        raise AssertionError('Main code failed to return with balanced stack')
    if any(u.reg_read(reg) != value for reg, value in saved.items()):
        raise AssertionError('Main code clobbered a callee-saved register')
    result = u.reg_read(UC_ARM64_REG_W0)
    actual_state = struct.unpack('<I', u.mem_read(ptr, 4))[0]
    state = 3 if mock_state is None else mock_state
    if kind == 'getter':
        if result != 0 or actual_state != 3:
            raise AssertionError('Getter did not force success/state3')
        memory = bytes(u.mem_read(DATA, 0x1000))
        if memory[:0x80] != pattern[:0x80] or memory[0x84:] != pattern[0x84:]:
            raise AssertionError('Getter wrote beyond the output word')
    elif kind == 'avb':
        if result != int(state != 3):
            raise AssertionError('AVB predicate mismatch')
    elif kind == 'lock_boolean':
        if result != 0 or actual_state != int(state != 3):
            raise AssertionError('Lock boolean mismatch')
    elif kind == 'fastboot':
        expected = 'lks = 0' if state == 3 else 'lks = 1'
        if info != [expected] or formats != [expected] or failures:
            raise AssertionError(('Fastboot lock response mismatch', info, formats, failures))
    return {'entry_payload': hex(entry), 'kind': kind, 'initial': initial,
            'boot_type': boot, 'mock_state_control': mock_state, 'return': result,
            'output_word': actual_state, 'fastboot_info': info, 'pass': True}


def bl2_run(raw, record=None, mask=0x60, sb=0, status=0):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(BL2, 0x400000)
    u.mem_write(BL2, raw)
    u.mem_map(DATA, 0x30000)
    u.mem_map(STACK, 0x10000)
    u.mem_map(STOP, 0x1000)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    seen = []
    parser = record is not None
    if parser:
        u.mem_write(DATA, record)
        u.reg_write(UC_ARM64_REG_X0, DATA)
        u.reg_write(UC_ARM64_REG_X1, len(record))
        u.reg_write(UC_ARM64_REG_X2, mask)
    else:
        u.mem_write(DATA, b'lk\0')
        u.reg_write(UC_ARM64_REG_X0, DATA)

    def ret(value):
        u.reg_write(UC_ARM64_REG_W0, value)
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, user):
        off = addr - BL2
        if off in (0x5c660, 0x5c694, 0x5c740, 0x53f7c, 0x54010):
            seen.append(hex(off))
        if not parser and off == 0x6b9a4:
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', sb))
            ret(status)
        elif not parser and off == 0x4d640:
            ret(0)
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(BL2 + (0x6ea70 if parser else 0x5c660), STOP, count=1500000)
    if u.reg_read(UC_ARM64_REG_PC) != STOP:
        raise AssertionError('Native ENG BL2 did not return')
    return {'kind': 'parser' if parser else 'auth_policy', 'mask': hex(mask) if parser else None,
            'sboot_mock': sb if not parser else None, 'helper_status_mock': status if not parser else None,
            'result': u.reg_read(UC_ARM64_REG_W0), 'policy_path': seen,
            'header_hash': bytes(u.mem_read(BL2 + 0x1c3d1c, 32)).hex() if parser else None,
            'payload_hash': bytes(u.mem_read(BL2 + 0x1c3ce8, 32)).hex() if parser else None}


def main():
    source = INPUT.read_bytes()
    image = (HERE / NAME).read_bytes()
    if build(source)[0] != image or build(source)[0] != image:
        raise AssertionError('Non-deterministic artifact')
    bad = bytearray(source)
    bad[OFFSET] ^= 1
    try:
        build(bytes(bad))
    except ValueError:
        pass
    else:
        raise AssertionError('Changed input accepted')
    rng = random.Random(9500)
    results = []
    for value in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]:
        for entry in (0x896e4, 0x88c18, 0x885fc):
            results.append(main_case(image, entry, value, 'getter'))
    for boot in (0, 1, 2, 0xffffffff):
        for mock in (None, 0, 1, 2, 3, 4, 0xffffffff):
            results.append(main_case(image, 0x6979c, 0, 'avb', boot, mock))
    for kind, entry in [('lock_boolean', 0xb6588), ('fastboot', 0xc770)]:
        for mock in (None, 0, 1, 2, 3, 4, 0xffffffff):
            results.append(main_case(image, entry, 0xffffffff, kind, mock_state=mock))
    ms, original_ms = members(image), members(source)
    certs = []
    for i, m in enumerate(ms):
        if m['name'] not in ('cert1', 'cert2'):
            continue
        raw = image[m['offset'] + 512:m['offset'] + 512 + m['size']]
        der = raw[180:] if i == 2 else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        row = {'member': i, 'retained_der_signature_valid': True}
        if m['name'] == 'cert2':
            component = ms[i - 2]
            payload_digest = sha(image[component['offset'] + 512:component['end']])
            header_digest = sha(image[component['offset']:component['offset'] + 512])
            oid_h, oid_p = bytes.fromhex('060760867693160204'), bytes.fromhex('060760867693160201')
            signed_header = der[der.index(oid_h) + 12:der.index(oid_h) + 44].hex()
            signed_payload = der[der.index(oid_p) + 12:der.index(oid_p) + 44].hex()
            if signed_header != header_digest or ((i == 2) == (signed_payload == payload_digest)):
                raise AssertionError('Unexpected signed component hash relationship')
            row.update(header_hash_matches=True, payload_hash_matches=(i != 2))
        certs.append(row)
    bl2 = ms[3]
    raw_bl2 = image[bl2['offset'] + 512:bl2['offset'] + 512 + bl2['size']]
    if sha(raw_bl2) != 'ab5a5fbcc6c5260deed7b21d835732d530a6a847bae8b907b6af924f77030631':
        raise AssertionError('Wrong BL2 replay target')
    cert = ms[2]
    record = image[cert['offset']:cert['offset'] + 512 + cert['size']]
    old_cert = original_ms[2]
    old_record = source[old_cert['offset']:old_cert['offset'] + 512 + old_cert['size']]
    digest = sha(image[512:ms[0]['end']])
    old_digest = sha(source[512:original_ms[0]['end']])
    if digest == old_digest:
        raise AssertionError('Patched digest did not change')
    # A controlled bad prefix proves selection outside the retained signed DER.
    control = bytearray(record)
    for off in (512 + 58, 512 + 90 + 58):
        control[off] ^= 1
    altered = bytes([bytes.fromhex(digest)[0] ^ 1]) + bytes.fromhex(digest)[1:]
    parser_cases = []
    for label, cert_record, wanted in [('original_signed', old_record, old_digest),
                                      ('final_candidate', record, digest),
                                      ('prefix_selection_control', bytes(control), altered.hex())]:
        for mask in (0x60, 0x1f4e2, 0x1f400):
            result = bl2_run(raw_bl2, cert_record, mask)
            if result['result'] != 0:
                raise AssertionError('ENG parser failure')
            if mask != 0x1f400 and (result['payload_hash'] != wanted or result['header_hash'] != sha(image[:512])):
                raise AssertionError('ENG parser selected wrong hash')
            result['certificate'] = label
            parser_cases.append(result)
    policy_cases = [bl2_run(raw_bl2, sb=sb, status=status) for sb, status in [(0, 0), (1, 0), (2, 0), (0, 5)]]
    if any(r['result'] != 1 for r in policy_cases):
        raise AssertionError('Unexpected actual ENG BL2 authentication policy')
    report = {'status': 'PASS', 'artifact_sha256': sha(image), 'original_input_unchanged_sha256': sha(source),
              'deterministic_build': True, 'wrong_input_rejected': True,
              'main_cpu_cases': len(results), 'native_eng_bl2_parser_cases': len(parser_cases),
              'native_eng_bl2_policy_cases': len(policy_cases), 'retained_der_signatures': certs,
              'main_signed_digest_remains_original': True, 'new_main_digest': digest,
              'original_main_digest': old_digest,
              'mocks': ['main logging and boot partition selector', 'fastboot formatting/transmission',
                        'getter replacement only in explicitly labelled control cases',
                        'BL2 secure-state helper and logging only in policy cases', 'PAC instructions'],
              'parser_mocks': ['PAC instructions only; no ASN.1/OID/hash-selection/crypto/result mocks'],
              'limits': ['Actual ARM64 instructions with bounded boundary models, not a full boot/SoC test',
                         'Retained DER signatures validated offline; original main signed payload digest differs by design',
                         'No physical phone flash/boot, preloader compatibility, full loader or rollback-fuse validation',
                         'BL2 parser hash selection is not itself proof of the complete authentication chain'],
              'main_cases': results, 'parser_cases': parser_cases, 'policy_cases': policy_cases}
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'artifact_sha256', 'main_cpu_cases',
                     'native_eng_bl2_parser_cases', 'native_eng_bl2_policy_cases')}, indent=2))


if __name__ == '__main__':
    main()

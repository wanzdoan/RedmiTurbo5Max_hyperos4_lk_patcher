"""Execute actual LK ARM64 region code with hardware/crypto boundaries mocked.

Real byte comparison, string comparison, memcpy, control flow and epilogues run.
This is bounded CPU testing, not a whole-device or Global ROM boot test.
"""
import gc
import itertools
import json
from pathlib import Path
import struct
import sys

from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN

from build_region_lk import HERE, ROOT, BASE_PATH, NAME, build, patches, members, prefix, sha

B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000
SAVED = [UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21,
         UC_ARM64_REG_X22, UC_ARM64_REG_X23, UC_ARM64_REG_X24,
         UC_ARM64_REG_X25, UC_ARM64_REG_X26, UC_ARM64_REG_X27,
         UC_ARM64_REG_X28, UC_ARM64_REG_X29]


class Replay:
    def __init__(self, image, sb=14, hw=0, dconfig=0, config=None,
                 efuse_error=0, chain_error=0, key_error=0, key=None, sku='GLOBAL', block=False):
        self.u = u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
        u.mem_map(B, 0x500000)
        u.mem_write(B, image[512:512 + members(image)[0]['size']])
        u.mem_map(STACK, 0x10000)
        u.mem_map(DATA, 0x10000)
        u.mem_map(STOP, 0x1000)
        u.mem_write(DATA + 0x100, sku.encode() + b'\0')
        if config is not None:
            u.mem_write(DATA + 0x200, config.encode() + b'\0')
        u.mem_write(B + 0x20d9c4, bytes([hw]))
        self.initial = {reg: 0x100000 + i for i, reg in enumerate(SAVED)}
        for reg, value in self.initial.items():
            u.reg_write(reg, value)
        u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
        u.reg_write(UC_ARM64_REG_LR, STOP)
        if block:
            u.reg_write(UC_ARM64_REG_X29, STACK + 0xf000)
            u.reg_write(UC_ARM64_REG_X20, DATA + 0x1000)
        self.end = None
        self.calls = []

        def ret(value=0):
            u.reg_write(UC_ARM64_REG_W0, value & 0xffffffff)
            u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

        def hook(uc, address, size, user):
            offset = address - B
            if block and offset == 0x6905c:
                self.end = 'continued'
                uc.emu_stop()
            elif offset == 0x7cfcc:
                self.end = 'panic'
                uc.emu_stop()
            elif offset == 0x7e11c:
                ret()
            elif offset == 0x4a384:
                self.calls.append('secureboot_read')
                ret(sb)
            elif offset == 0x4a780:
                self.calls.append('region_efuse_read')
                if not efuse_error:
                    uc.mem_write(uc.reg_read(UC_ARM64_REG_X1), struct.pack('<I', hw))
                ret(efuse_error)
            elif offset == 0x21d30:
                uc.reg_write(UC_ARM64_REG_X0, DATA + 0x200 if config is not None else 0)
                uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
            elif offset == 0x21e60:
                ret(dconfig)
            elif offset == 0xc4f6c:
                uc.reg_write(UC_ARM64_REG_X0, DATA + 0x100)
                uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
            elif offset == 0xae288:
                self.calls.append('certificate_chain_verify')
                ret(chain_error)
            elif offset == 0xaf224:
                self.calls.append('extract_image_key')
                if not key_error:
                    uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), key or bytes(256))
                ret(key_error)
            elif offset in (0x7e31c, 0x8871c):
                ret()  # Existing lock wrapper logging / boot partition helper boundary.
            elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
                uc.reg_write(UC_ARM64_REG_PC, address + 4)  # PAC unsupported in this replay.

        u.hook_add(UC_HOOK_CODE, hook)

    def run(self, offset, function=True):
        try:
            self.u.emu_start(B + offset, STOP, count=50000)
        except Exception as error:
            raise RuntimeError(f'{error}; pc={self.u.reg_read(UC_ARM64_REG_PC):x}; x0={self.u.reg_read(UC_ARM64_REG_X0):x}; x1={self.u.reg_read(UC_ARM64_REG_X1):x}') from error
        if self.end == 'panic':
            return 'panic'
        if not function:
            if self.end != 'continued':
                raise AssertionError('Region block failed to reach continuation')
            return self.end
        if self.u.reg_read(UC_ARM64_REG_PC) != STOP:
            raise AssertionError('Function did not return')
        if self.u.reg_read(UC_ARM64_REG_SP) != STACK + 0xf000:
            raise AssertionError('Stack imbalance')
        if any(self.u.reg_read(reg) != value for reg, value in self.initial.items()):
            raise AssertionError('Callee-saved register clobbered')
        return self.u.reg_read(UC_ARM64_REG_W0)


def main():
    original = BASE_PATH.read_bytes()
    image = (HERE / NAME).read_bytes()
    if build(original)[0] != image or build(original)[0] != image:
        raise AssertionError('Artifact differs from deterministic build')
    bad = bytearray(original)
    bad[0x6924c] ^= 1
    try:
        build(bytes(bad))
    except ValueError:
        pass
    else:
        raise AssertionError('Wrong input accepted')
    cases = []
    configs = [(0, None), (0, 'yes'), (1, 'yes')]
    for patched, code in [(False, original), (True, image)]:
        for sb, hw, sku, cfg, error in itertools.product([0, 14], [0, 1],
                ['CN', 'CN_TEST', 'NATIVE', 'GLOBAL', 'GL', ''], configs, [0, 7]):
            active = sb == 14 and cfg != (1, 'yes')
            expected = 'panic' if active and (error or (not patched and hw == 0 and not sku.startswith(('CN', 'NATIVE')))) else 'continued'
            r = Replay(code, sb=sb, hw=hw, sku=sku, dconfig=cfg[0], config=cfg[1], efuse_error=error, block=True)
            got = r.run(0x68f50, False)
            if got != expected:
                raise AssertionError(('SKU', patched, sb, hw, sku, cfg, error, got, expected))
            cases.append({'gate': 'sku', 'patched': patched, 'sb': sb, 'hw': hw,
                          'sku': sku, 'dconfig': cfg, 'efuse_error': error, 'outcome': got})
        cn = code[512 + 0x1121d7:512 + 0x1122d7]
        gl = code[512 + 0x1122d7:512 + 0x1123d7]
        if len(cn) != 256 or len(gl) != 256 or cn == gl:
            raise AssertionError('Image key identity')
        for sb, hw, kind, chain_error, key_error, fuse_error in itertools.product(
                [0, 14], [0, 1], ['CN', 'GL', 'unknown'], [0, 0x1234], [0, 0x321], [0, 7]):
            key = {'CN': cn, 'GL': gl, 'unknown': bytes(256)}[kind]
            expected = chain_error or (0 if sb != 14 else
                       ('panic' if fuse_error else (key_error or
                        (0 if kind == ('CN' if hw == 0 else 'GL') or (patched and kind == 'GL')
                         else (0xffffffff if patched or hw == 0 else 0)))))
            r = Replay(code, sb=sb, hw=hw, key=key, chain_error=chain_error,
                       key_error=key_error, efuse_error=fuse_error)
            r.u.reg_write(UC_ARM64_REG_X1, DATA + 0x800)
            r.u.mem_write(DATA + 0x800, b'md1rom\0')
            got = r.run(0x88734)
            if got != expected:
                raise AssertionError(('image-key', patched, sb, hw, kind, chain_error, key_error, fuse_error, got, expected))
            if not r.calls or r.calls[0] != 'certificate_chain_verify':
                raise AssertionError('Certificate-chain verification was skipped')
            cases.append({'gate': 'image-key', 'patched': patched, 'sb': sb, 'hw': hw,
                          'key': kind, 'chain_error': chain_error, 'extract_error': key_error,
                          'efuse_error': fuse_error, 'outcome': got})
        for sb, hw, kind, bits in itertools.product([0, 14], [0, 1], ['CN', 'GL', 'unknown'], [2048, 4096]):
            key = (code[512 + 0x138658:512 + 0x138858] if kind == 'CN' else
                   code[512 + 0x138858:512 + 0x138a58] if kind == 'GL' else bytes(512))
            r = Replay(code, sb=sb, hw=hw)
            r.u.mem_write(DATA + 0x800, struct.pack('>I', bits) + bytes(4) + key)
            r.u.mem_write(DATA + 0x700, b'\xa5')
            r.u.reg_write(UC_ARM64_REG_X1, DATA + 0x800)
            r.u.reg_write(UC_ARM64_REG_X5, DATA + 0x700)
            got = r.run(0xc5978)
            trusted = r.u.mem_read(DATA + 0x700, 1)[0]
            expected = bits == 4096 and (kind in ('CN', 'GL') if patched or sb != 14 else kind == ('CN' if hw == 0 else 'GL'))
            if got != 0 or trusted != int(expected):
                raise AssertionError(('AVB-key', patched, sb, hw, kind, bits, got, trusted, expected))
            cases.append({'gate': 'avb-key', 'patched': patched, 'sb': sb, 'hw': hw,
                          'key': kind, 'bits': bits, 'trusted': trusted})
        gc.collect()
    # The same unlock getter/wrappers and Secure ID run on the final artifact.
    for entry in (0x89aec, 0x89020, 0x88a10):
        r = Replay(image)
        r.u.reg_write(UC_ARM64_REG_X0, DATA + 0x900)
        if r.run(entry) != 0 or r.u.mem_read(DATA + 0x900, 4) != struct.pack('<I', 3):
            raise AssertionError('Unlock chain failed')
    r = Replay(image)
    if r.run(0x86b8c) != 0 or 'region_efuse_read' in r.calls:
        raise AssertionError('Existing Secure ID unlock bypass failed')
    ms = members(image)
    certs = []
    for i, m in enumerate(ms):
        if m['name'] not in ('cert1', 'cert2'):
            continue
        raw = image[m['offset'] + 512:m['offset'] + 512 + m['size']]
        der = raw[180:] if i == 2 else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append({'member': i, 'retained_der_signature_valid': True})
    sys.path.insert(0, str(ROOT / 'fresh_analysis/preloader'))
    from emulate_bl2_parser import run as parser_run
    m = ms[2]
    record = image[m['offset']:m['offset'] + 512 + m['size']]
    digest = sha(image[512:512 + ((ms[0]['size'] + 15) & ~15)])
    der = record[512 + 180:]
    oid = bytes.fromhex('060760867693160201')
    signed_digest = der[der.index(oid) + 12:der.index(oid) + 44].hex()
    parser_cases = [parser_run(v, 'global_region_experiment', mask, record)
                    for v in (3, 4) for mask in (0x60, 0x1f4e2, 0x1f400)]
    for p in parser_cases:
        if not p['returned'] or p['error'] or p['status'] != '0x0':
            raise AssertionError('BL2 parser failure')
        if p['mask'] != '0x1f400':
            expected_digest = digest if p['version'] == 3 else signed_digest
            if p['payload_hash'] != expected_digest or p['header_hash'] != sha(image[:512]):
                raise AssertionError('Wrong parser hash selection')
    assembly = []
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    for off, old, new, purpose in patches():
        assembly += [{'payload_offset': hex(i.address), 'bytes': i.bytes.hex(),
                      'instruction': f'{i.mnemonic} {i.op_str}'} for i in decoder.disasm(new, off)]
    report = {'status': 'PASS', 'artifact_sha256': sha(image), 'gate_cpu_cases': len(cases),
              'unlock_secure_id_cpu_cases': 4, 'cpu_cases_total': len(cases) + 4,
              'der_signatures': certs, 'parser_cases': parser_cases, 'patch_disassembly': assembly,
              'wrong_input_rejected': True, 'deterministic_rebuild': True,
              'mocks': ['secure-boot/region efuse READS', 'dconfig/config getters', 'SKU extractor',
                        'certificate-chain status and image-key extraction', 'logging', 'PAC instructions'],
              'real_code': ['gate branches', 'memcmp/strcmp/memcpy', 'key length byte swap',
                            'returns/stack/callee-saved ABI', 'getter/wrappers/Secure ID', 'BL2 ASN.1 parser'],
              'limits': ['No hardware execution, full boot simulation or target Global ROM',
                         'Certificate failure propagation tested with supplied statuses; not real Global image signatures',
                         'No NR registration or 5G fix verified'], 'cases': cases}
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'artifact_sha256', 'cpu_cases_total', 'gate_cpu_cases', 'limits')}, indent=2))


if __name__ == '__main__':
    main()

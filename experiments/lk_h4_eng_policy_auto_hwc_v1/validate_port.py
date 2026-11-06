"""Bounded native AVB key/policy and ENG BL2 parser tests; no device I/O."""
import json
import random
import struct
from build_port import *
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run

B, DATA, STACK, STOP = 0x90700000, 0x3000000, 0x2000000, 0x4000000
SAVED = list(range(UC_ARM64_REG_X19, UC_ARM64_REG_X28 + 1)) + [UC_ARM64_REG_X29]


def execute(image, entry, arguments, state=None, hash_failure=False):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    for start, length in ((B, 0x500000), (DATA, 0x10000), (STACK, 0x10000), (STOP, 0x1000)):
        u.mem_map(start, length)
    u.mem_write(B, image[512:512 + members(image)[0]['size']])
    u.mem_write(DATA, bytes([0xa5]) * 0x10000)
    saved = {r: 0x1234000000 + r for r in SAVED}
    for reg, value in saved.items(): u.reg_write(reg, value)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    for reg, value in arguments.items():
        if isinstance(value, bytes): u.mem_write(reg, value)
        else: u.reg_write(reg, value)
    seen = []
    def hook(uc, address, size, _):
        off = address - B
        word = int.from_bytes(uc.mem_read(address, 4), 'little')
        if off == 0x7eae8: seen.append('native_memcmp')
        if off == 0xa4810 and hash_failure:
            uc.reg_write(UC_ARM64_REG_W0,7)
            uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR))
        elif off in (0x7e11c, 0x8871c) or (off == 0x89aec and state is not None):
            if off == 0x89aec:
                uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', state))
            uc.reg_write(UC_ARM64_REG_W0, 0)
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif word in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)
    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + entry, STOP, count=200000)
    assert u.reg_read(UC_ARM64_REG_PC) == STOP
    assert u.reg_read(UC_ARM64_REG_SP) == STACK + 0xf000
    assert all(u.reg_read(reg) == value for reg, value in saved.items())
    return u.reg_read(UC_ARM64_REG_W0), bytes(u.mem_read(DATA, 0x10000)), seen


def validate_one(kind):
    h4, eng = H4.read_bytes(), ENG.read_bytes()
    image = (HERE / NAMES[kind]).read_bytes()
    assert build(h4, eng, kind)[0] == image == build(h4, eng, kind)[0]
    for which in ('h4', 'eng'):
        bad = bytearray(h4 if which == 'h4' else eng); bad[10000] ^= 1
        try: build(bytes(bad) if which == 'h4' else h4, bytes(bad) if which == 'eng' else eng)
        except ValueError: pass
        else: raise AssertionError('Wrong input accepted')
    cases = []
    def key_case(label, bits, modulus, expected, size=None, null_key=False, null_out=False):
        blob = struct.pack('>II', bits, 0) + modulus + bytes(len(modulus))
        args = {DATA + 0x800: blob, UC_ARM64_REG_X1: 0 if null_key else DATA + 0x800,
                UC_ARM64_REG_X2: len(blob) if size is None else size,
                UC_ARM64_REG_X5: 0 if null_out else DATA + 0x700}
        rc, memory, seen = execute(image, ENTRY, args)
        trusted = memory[0x700]
        assert (rc, trusted) == expected, (label, size, rc, trusted, expected)
        assert memory[0x701] == 0xa5
        cases.append(dict(key=label, bits=bits, size=args[UC_ARM64_REG_X2], rc=rc,
                          trusted=trusted, native_memcmp=bool(seen), pass_result=True))
    keys = {'CN': h4[512+0x138658:512+0x138858],
            'GL': h4[512+0x138858:512+0x138a58],
            'ENG': eng[512+0x130728:512+0x130828]}
    for label, key in keys.items():
        bits = len(key) * 8
        key_case(label, bits, key, (0, 1))
        for off in (0, 1, 15, 31, len(key)//2, len(key)-1):
            altered = bytearray(key); altered[off] ^= 1
            key_case(label+'_mutated', bits, bytes(altered), (0, 0))
        for size in (0, 4, 7, 8, 263, 519, 521, 1031, 1033):
            key_case(label+'_bad_size', bits, key, (2, 0), size=size)
    rng = random.Random(9500)
    for bits in (1024, 2048, 4096, 8192):
        key_case('unknown', bits, rng.randbytes(bits//8), (0, 0))
    for nk, no in ((True, False), (False, True), (True, True)):
        key_case('null', 4096, keys['CN'], (2, 0xa5), null_key=nk, null_out=no)
    cn_blob=struct.pack('>II',4096,0)+keys['CN']+bytes(512)
    args={DATA+0x800:cn_blob,UC_ARM64_REG_X1:DATA+0x800,UC_ARM64_REG_X2:1032,UC_ARM64_REG_X5:DATA+0x700}
    corrupt=bytearray(image)
    at=corrupt.index(bytes.fromhex(sha(keys['CN'])),512+ENTRY,512+HWC_ENTRY)
    corrupt[at]^=1
    for label,raw,failure in [('digest_corruption',bytes(corrupt),False),('sha_failure',image,True)]:
        rc,memory,_=execute(raw,ENTRY,args,hash_failure=failure)
        assert rc==0 and memory[0x700]==0 and memory[0x701]==0xa5
        cases.append(dict(key=label,rc=rc,trusted=0,pass_result=True))
    policy = []
    for state in (0, 1, 2, 3, 4, 0xffffffff):
        for boot in (0, 1, 2, 0xffffffff):
            args = {UC_ARM64_REG_X0: boot}
            a = execute(h4, 0x68c94, args, state)[0]
            b = execute(image, 0x68c94, args, state)[0]
            assert a == b == int(state != 3)
            policy.append(dict(stored_state_fixture=state, boot_type=boot,
                               original_return=a, output_return=b, pass_result=True))
    rows = members(image); certs = []
    oid_h, oid_p = bytes.fromhex('060760867693160204'), bytes.fromhex('060760867693160201')
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = image[row['offset']+512:row['offset']+512+row['size']]
        der = raw[180:] if i == 2 else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        if row['name'] == 'cert2':
            component = rows[i-2]; offset = component['offset']
            hh = sha(image[offset:offset+512]); ph = sha(image[offset+512:component['end']])
            assert der[der.index(oid_h)+12:der.index(oid_h)+44].hex() == hh
            signed = der[der.index(oid_p)+12:der.index(oid_p)+44].hex()
            assert (signed == ph) == (i != 2)
            if i == 2:
                assert raw == prefix(image[:512], image[512:512+rows[0]['size']]) + der
        certs.append(dict(index=i, retained_der_signature_valid=True))
    bl2row, cert = rows[3], rows[2]
    bl2 = image[bl2row['offset']+512:bl2row['offset']+512+bl2row['size']]
    record = image[cert['offset']:cert['offset']+512+cert['size']]
    parsers = []
    for mask in (0x60, 0x1f4e2, 0x1f400):
        result = bl2_run(bl2, record, mask)
        assert result['result'] == 0
        if mask != 0x1f400:
            assert result['payload_hash'] == sha(image[512:rows[0]['end']])
            assert result['header_hash'] == sha(image[:512])
        parsers.append(result)
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN); decoder.skipdata = True
    for ins in decoder.disasm(h4[512+0x3000:512+0xc8000], 0x3000):
        if ins.mnemonic in ('b', 'bl', 'cbz', 'cbnz', 'tbz', 'tbnz') or ins.mnemonic.startswith('b.'):
            operand = ins.op_str.rsplit(',', 1)[-1].strip()
            if operand.startswith('#0x'):
                target = int(operand[1:], 16)
                assert not (ENTRY < target < END and target != HWC_ENTRY and not ENTRY <= ins.address < END)
                assert not (0x13b70 <= target < 0x13bc4 and not 0x13b70 <= ins.address < 0x13bc4)
    report = dict(status='PASS_OFFLINE_NATIVE_NOT_DEVICE_BOOT', output_sha256=sha(image),
        input_h4_sha256=sha(h4), input_h3_eng_sha256=sha(eng),
        deterministic_rebuild=True, wrong_inputs_rejected=True,
        counts=dict(key_cases=len(cases), policy_comparisons=len(policy), bl2_parsers=len(parsers), der=len(certs)),
        key_cases=cases, original_vs_output_policy=policy, eng_bl2_parsers=parsers, certificates=certs,
        no_external_direct_branches_into_callback_interior=True,
        limitations=['PAC and logging/boot-selection helpers modeled; getter mocked for policy fixtures',
                     'Actual memcmp, AVB predicate, BL2 parser code run; no full load/auth/SoC boot',
                     'No phone flash, lock, root, OTA or Play Integrity testing',
                     'Signature math verifies retained DER, not a new OEM signature for modified main'])
    (HERE/(kind+'_validation.json')).write_text(json.dumps(report, indent=2)+'\n')
    print(report['status'], report['counts'], report['output_sha256'])


if __name__ == '__main__':
    for kind in NAMES: validate_one(kind)

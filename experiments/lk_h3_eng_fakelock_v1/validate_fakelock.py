"""Native CPU probes for display-only fakelock; no ADB or device writes."""
import json
import random
import struct
import sys
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from build_fakelock import HERE, ROOT, INPUT, NAME, HELPER, FLASH_ARG, build, patches, members, sha

sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import main_case, bl2_run

B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000
SAVED = [UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21, UC_ARM64_REG_X22,
         UC_ARM64_REG_X23, UC_ARM64_REG_X24, UC_ARM64_REG_X25, UC_ARM64_REG_X26,
         UC_ARM64_REG_X27, UC_ARM64_REG_X28, UC_ARM64_REG_X29]


def cstr(u, ptr):
    out = bytearray()
    for i in range(1024):
        byte = bytes(u.mem_read(ptr + i, 1))
        if byte == b'\0':
            return out.decode()
        out += byte
    raise ValueError('Unterminated string')


def machine(image):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    u.mem_map(B, 0x500000)
    u.mem_write(B, image[512:512 + members(image)[0]['size']])
    u.mem_map(STACK, 0x10000)
    u.mem_map(DATA, 0x10000)
    u.mem_map(STOP, 0x1000)
    saved = {reg: 0x1234567800 + i for i, reg in enumerate(SAVED)}
    for reg, value in saved.items():
        u.reg_write(reg, value)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    return u, saved


def assert_abi(u, saved):
    if u.reg_read(UC_ARM64_REG_PC) != STOP or u.reg_read(UC_ARM64_REG_SP) != STACK + 0xf000:
        raise AssertionError('Unbalanced stack or failed return')
    if any(u.reg_read(reg) != value for reg, value in saved.items()):
        raise AssertionError('Callee-saved register changed')


def fastboot_case(image, query, values, patched, oem=False):
    u, saved = machine(image)
    ptr, strings, nodes = DATA + 0x100, DATA + 0x1000, []
    for name, value in values:
        node = ptr
        ptr += 0x20
        name_ptr = strings
        u.mem_write(strings, name.encode() + b'\0')
        strings += len(name) + 1
        value_ptr = strings
        u.mem_write(strings, value.encode() + b'\0')
        strings += len(value) + 1
        nodes.append((node, name_ptr, value_ptr))
    for i, (node, name_ptr, value_ptr) in enumerate(nodes):
        u.mem_write(node, struct.pack('<QQQ', nodes[i + 1][0] if i + 1 < len(nodes) else 0, name_ptr, value_ptr))
    head = nodes[0][0] if nodes else 0
    u.mem_write(B + 0x135168, struct.pack('<Q', head))
    u.mem_write(DATA + 0x8000, query.encode() + b'\0')
    u.reg_write(UC_ARM64_REG_X0, DATA + 0x8000)
    before = bytes(u.mem_read(DATA, 0x7000))
    info, okay, fail = [], [], []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_W0, value & 0xffffffff)
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, addr, size, user):
        off = addr - B
        if off == 0x7ffe0:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2))
            if fmt == '\t%s: %s':
                text = fmt % (cstr(uc, uc.reg_read(UC_ARM64_REG_X3)), cstr(uc, uc.reg_read(UC_ARM64_REG_X4)))
            elif fmt in ('lks = 0', 'lks = 1'):
                text = fmt
            else:
                raise ValueError(f'Unexpected format: {fmt!r}')
            cap = uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode()[:cap - 1] + b'\0')
            ret(len(text))
        elif off in (0x9cfc, 0x9dec, 0x9dac):
            text = cstr(uc, uc.reg_read(UC_ARM64_REG_X0))
            (info if off == 0x9cfc else okay if off == 0x9dec else fail).append(text)
            ret()
        elif off == 0x7ea84:
            ret()
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + (0xc770 if oem else 0xaf90), STOP, count=100000)
    assert_abi(u, saved)
    if before != bytes(u.mem_read(DATA, 0x7000)) or u.mem_read(B + 0x135168, 8) != struct.pack('<Q', head):
        raise AssertionError('Public reporting mutated internal variable storage')
    public = dict(values)
    if patched:
        if 'unlocked' in public:
            public['unlocked'] = 'no'
        if 'secure' in public:
            public['secure'] = 'yes'
    if oem:
        expected_info, expected_ok = ['lks = 1' if patched else 'lks = 0'], ['']
    elif query == 'all':
        expected_info, expected_ok = ['\t%s: %s' % (name, public[name]) for name, value in values], ['Done!!']
    else:
        expected_info, expected_ok = [], [public.get(query, '')]
    if info != expected_info or okay != expected_ok or fail:
        raise AssertionError(('Fastboot reporting mismatch', query, patched, info, okay, fail))
    # Execute the REAL internal getter after the reply on the same variable list.
    internal = {}
    for name in ('unlocked', 'secure', 'product'):
        u.mem_write(DATA + 0x8000, name.encode() + b'\0')
        u.reg_write(UC_ARM64_REG_X0, DATA + 0x8000)
        u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
        u.reg_write(UC_ARM64_REG_LR, STOP)
        u.emu_start(B + 0x9bc8, STOP, count=100000)
        internal[name] = cstr(u, u.reg_read(UC_ARM64_REG_X0))
        if internal[name] != dict(values).get(name, ''):
            raise AssertionError('Internal getvar now sees the faked public value')
    return {'kind': 'oem' if oem else 'getvar', 'query': query, 'patched': patched,
            'info': info, 'okay': okay, 'internal_values_after_reply': internal, 'abi_pass': True}


def android_state_case(image, state, first_status=0, second_status=0, patched=True):
    u, saved = machine(image)
    u.mem_write(B + 0x119968, struct.pack('<I', state))
    calls = []

    def hook(uc, addr, size, user):
        if addr == B + 0x18ab8:
            calls.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0)))
            rc = first_status if len(calls) == 1 else second_status
            uc.reg_write(UC_ARM64_REG_W0, rc & 0xffffffff)
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif int.from_bytes(uc.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, addr + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + 0x69da4, STOP, count=1000)
    assert_abi(u, saved)
    if u.mem_read(B + 0x119968, 4) != struct.pack('<I', state):
        raise AssertionError('Real verified-boot state was modified')
    if patched:
        expected = ['androidboot.verifiedbootstate=green']
        if first_status == 0:
            expected += ['androidboot.flash.locked=1']
    else:
        colors = ['green', 'yellow', 'orange', 'red']
        expected = ['androidboot.verifiedbootstate=' + colors[state]] if state < 4 else []
    if calls != expected:
        raise AssertionError(('Wrong Android args', state, calls, expected))
    if patched and u.reg_read(UC_ARM64_REG_W0) != ((first_status or second_status) & 0xffffffff):
        raise AssertionError('Append failure not propagated')
    return {'kind': 'android_bootargs', 'patched': patched, 'real_state': state,
            'first_append_status': first_status, 'second_append_status': second_status,
            'bootargs': calls, 'real_state_unchanged': True, 'abi_pass': True}


def avb_device_state_case(image, unlocked, rc, patched):
    u, saved = machine(image)
    u.reg_write(UC_ARM64_REG_X29, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_X19, DATA)
    u.mem_write(STACK + 0xf000 - 0x44, bytes([unlocked]))
    captured, outcome = [], []

    def hook(uc, addr, size, user):
        if addr == B + 0xbcaf4:
            captured.append((cstr(uc, uc.reg_read(UC_ARM64_REG_X1)), cstr(uc, uc.reg_read(UC_ARM64_REG_X2))))
            uc.reg_write(UC_ARM64_REG_W0, rc)
            uc.reg_write(UC_ARM64_REG_PC, uc.reg_read(UC_ARM64_REG_LR))
        elif addr in (B + 0xbc7ac, B + 0xbcac8):
            outcome.append('continue' if addr == B + 0xbc7ac else 'append_failure')
            uc.emu_stop()

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(B + 0xbc77c, STOP, count=100)
    wanted = 'locked' if patched or not unlocked else 'unlocked'
    if captured != [('androidboot.vbmeta.device_state', wanted)] or outcome != ['continue' if rc else 'append_failure']:
        raise AssertionError('AVB public-state selection or error branch changed incorrectly')
    if u.mem_read(STACK + 0xf000 - 0x44, 1)[0] != unlocked:
        raise AssertionError('AVB internal unlocked flag mutated')
    return {'kind': 'avb_cmdline', 'patched': patched, 'real_unlocked': unlocked,
            'append_return': rc, 'public': captured, 'outcome': outcome}


def main():
    source, output = INPUT.read_bytes(), (HERE / NAME).read_bytes()
    if build(source)[0] != output or build(source)[0] != output:
        raise AssertionError('Non-deterministic artifact')
    bad = bytearray(source)
    bad[0xb214] ^= 1
    try:
        build(bytes(bad))
    except ValueError:
        pass
    else:
        raise AssertionError('Wrong source accepted')
    cases = []
    values = [('product', 'dash'), ('unlocked', 'yes'), ('secure', 'no'),
              ('current-slot', 'a'), ('unlocked-critical', 'no'), ('slot-count', '2'),
              ('unlocked-extra', 'untouched'), ('secure-extra', 'untouched')]
    for patched, code in [(False, source), (True, output)]:
        for entries in (values, list(reversed(values)), []):
            for query in ['all', 'unlocked', 'secure', 'product', 'current-slot', 'missing', 'unlocked-extra', 'secure-extra']:
                cases.append(fastboot_case(code, query, entries, patched))
        cases.append(fastboot_case(code, '', values, patched, oem=True))
        for state in (0, 1, 2, 3, 0xffffffff):
            cases.append(android_state_case(code, state, patched=patched))
        for unlocked in (0, 1, 2):
            for rc in (0, 1):
                cases.append(avb_device_state_case(code, unlocked, rc, patched))
    for first, second in [(-8, 0), (0, -8)]:
        cases.append(android_state_case(output, 2, first, second))
    # Internal behavior remains force-unlocked even though reporting is locked.
    internal = []
    rng = random.Random(9500)
    for initial in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]:
        for entry in (0x896e4, 0x88c18, 0x885fc):
            internal.append(main_case(output, entry, initial, 'getter'))
    for boot in (0, 1, 2, 0xffffffff):
        for mock in (None, 0, 1, 2, 3, 4, 0xffffffff):
            internal.append(main_case(output, 0x6979c, 0, 'avb', boot, mock))
    for mock in (None, 0, 1, 2, 3, 4, 0xffffffff):
        internal.append(main_case(output, 0xb6588, 0xffffffff, 'lock_boolean', mock_state=mock))
    rows = members(output)
    certs = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'):
            continue
        raw = output[row['offset'] + 512:row['offset'] + 512 + row['size']]
        der = raw[180:] if i == 2 else raw
        end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append({'member': i, 'retained_der_signature_valid': True})
    r, c = rows[3], rows[2]
    bl2 = output[r['offset'] + 512:r['offset'] + 512 + r['size']]
    record = output[c['offset']:c['offset'] + 512 + c['size']]
    digest = sha(output[512:rows[0]['end']])
    parser = []
    for mask in (0x60, 0x1f4e2, 0x1f400):
        result = bl2_run(bl2, record, mask)
        if result['result'] != 0 or (mask != 0x1f400 and (result['payload_hash'] != digest or result['header_hash'] != sha(output[:512]))):
            raise AssertionError('Native ENG BL2 selected the wrong hashes')
        parser.append(result)
    # No external direct branch into repurposed interior bytes existed in the input.
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    decoder.skipdata = True
    main_blob = source[512:512 + rows[0]['size']]
    for ins in decoder.disasm(main_blob[0x3000:0xc8000], 0x3000):
        if (ins.mnemonic == 'b' or ins.mnemonic == 'bl' or ins.mnemonic.startswith('b.')
                or ins.mnemonic in ('cbz', 'cbnz', 'tbz', 'tbnz')):
            last = ins.op_str.rsplit(',', 1)[-1].strip()
            if last.startswith('#0x'):
                target = int(last[1:], 16)
                if 0xc770 < target < 0xc7fc and not 0xc770 <= ins.address < 0xc7fc:
                    raise AssertionError('External branch entered repurposed function interior')
    assembly = []
    for lo, hi in [(0xc770, 0xc790), (HELPER, FLASH_ARG), (0x69da4, 0x69dd8),
                   (0xb014, 0xb018), (0xb040, 0xb044), (0xbc79c, 0xbc7a0)]:
        assembly += [{'payload_offset': hex(i.address), 'bytes': i.bytes.hex(),
                      'instruction': i.mnemonic + ' ' + i.op_str}
                     for i in decoder.disasm(output[512 + lo:512 + hi], lo)]
    report = {'status': 'PASS', 'artifact_sha256': sha(output), 'public_reporting_cpu_cases': len(cases),
              'internal_unlock_cpu_cases': len(internal), 'total_main_cpu_cases': len(cases) + len(internal),
              'internal_variable_storage_unchanged_in_all_reporting_cases': True,
              'internal_verified_boot_state_unchanged': True, 'deterministic_build': True,
              'wrong_input_rejected': True, 'retained_der_signatures': certs,
              'native_eng_bl2_parser_cases': parser, 'patch_disassembly': assembly,
              'reporting_cases': cases, 'internal_cases': internal,
              'mocks': ['PAC/logging', 'fastboot transmission and snprintf', 'cmdline append boundary',
                        'boot-partition selector', 'lock getter only in labelled negative/control probes'],
              'limits': ['No device flashing/boot, Android property readback or flash/erase execution',
                         'Fastboot permission-related code/storage is unchanged, not whole-command hardware validation',
                         'TEE/hardware attestation remains real and is not forged']}
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'artifact_sha256', 'public_reporting_cpu_cases',
                     'internal_unlock_cpu_cases', 'total_main_cpu_cases')}, indent=2))


if __name__ == '__main__':
    main()

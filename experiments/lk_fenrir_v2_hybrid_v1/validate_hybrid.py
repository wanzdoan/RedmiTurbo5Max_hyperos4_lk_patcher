"""Final-byte native tests. No code-RAM modifications, ADB or flash operations."""
import itertools
import json
import random
import struct
import sys
from build_hybrid import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/main_analysis'))
import probe_main as stock_probes
from probe_main import machine, run, ret, cstr, B, DATA, STOP
sys.path.insert(0, str(HERE / 'stock_hwc'))
from hwc_patch import check_external_branches, assemble
from validate_hwc import cases_for
sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_v2_hybrid_research/debug_gates'))
from probe_debug_gates import validate as debug_gate_validate
sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_v2_hybrid_research/root_path'))
from probe_init_boot_hash import validate as root_validate
sys.path.insert(0, str(ROOT / 'fresh_analysis/preloader'))
from emulate_bl2_parser import run as release_bl2_parser
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_eng_unlock_v1'))
from validate_eng_unlock import bl2_run

MAP = {
    'stock_h3': dict(log=0x7de54, lockbool=0x8869c, auth=0x689cc, get_boot=0x10b48, selector=0x8845c,
                     pub=0xc334, pub_end=0xc3f8, cache_get=0x9b2c, cache_set=(0x9a38, 0x9acc),
                     strcmp=0x7ede4, cfg_query=0xb35b8, cfg_entry=0xc8f4, fmt=0x7f434, fail=0x9d10,
                     sequence=0x68950, sequence_end=0x6895c, key=0x88654, crypto=0x1c55c,
                     sha=0xa4550, smc=0x1ea6c, backing=(0x88d60, 0x8982c)),
    'debug_h3': dict(log=0x7ea84, lockbool=0x8856c, auth=0x6979c, get_boot=0x10708, selector=0x8850c,
                     pub=0xc208, pub_end=0xc2cc, cache_get=0x9bc8, cache_set=(0x9ab0, 0x9b68),
                     strcmp=0x7f990, cfg_query=0xb3bec, cfg_entry=0xc7fc, fmt=0x7ffe0, fail=0x9dac,
                     sequence=0x69720, sequence_end=0x6972c, key=0x88578, crypto=0x1c0e0,
                     sha=0xa4b20, smc=0x1e714, backing=(0x896e4,)),
}


def pac(u, addr):
    if int.from_bytes(u.mem_read(addr, 4), 'little') in (0xd503233f, 0xd50323bf):
        u.reg_write(UC_ARM64_REG_PC, addr + 4)


def state_case(kind, image, initial, mode='common', boot=0):
    cfg, m = BASES[kind], MAP[kind]
    u, saved = machine(image); u.mem_write(DATA, struct.pack('<I', initial))
    u.reg_write(UC_ARM64_REG_X0, boot if mode == 'auth' else DATA)
    def hook(uc, addr, size, _):
        off = addr - B
        if off in m['backing']: raise AssertionError('Backing persistent lock getter called')
        if off == m['log']: ret(uc)
        elif off == m['selector']: ret(uc, 0)
        elif off == m['get_boot']: ret(uc, boot)
        else: pac(uc, addr)
    u.hook_add(UC_HOOK_CODE, hook)
    entry = cfg['common'] if mode == 'common' else m['lockbool'] if mode == 'boolean' else m['auth']
    run(u, entry, saved)
    value = u.reg_read(UC_ARM64_REG_W0) if mode == 'auth' else int.from_bytes(u.mem_read(DATA, 4), 'little')
    assert value == (4 if mode == 'common' else 1)
    return dict(kind=mode, initial=initial, boot_fixture=boot, result=value, backing_getter_not_called=True)


def public_case(kind, image, existing):
    m = MAP[kind]; u, _ = machine(image); updates = []
    u.mem_write(DATA, b'\0'); u.mem_write(DATA + 0x100, b'stale\0')
    def hook(uc, addr, size, _):
        off = addr - B
        if off == m['pub_end']: uc.emu_stop()
        elif off == m['cache_get']: ret(uc, DATA + 0x100 if existing else DATA)
        elif off == m['strcmp']:
            a, b = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)), cstr(uc, uc.reg_read(UC_ARM64_REG_X1))
            ret(uc, (a > b) - (a < b))
        elif off in m['cache_set']:
            updates.append((cstr(uc, uc.reg_read(UC_ARM64_REG_X0)), cstr(uc, uc.reg_read(UC_ARM64_REG_X1))))
            ret(uc)
        elif off == m['log']: ret(uc)
        else: pac(uc, addr)
    u.hook_add(UC_HOOK_CODE, hook); run(u, m['pub'])
    assert updates == [('secure', 'yes'), ('unlocked', 'no')]
    return dict(existing_cache=existing, native_published_values=updates)


def export_case(kind, image, which, value=0, status=0):
    cfg, m = BASES[kind], MAP[kind]
    u, saved = machine(image); outputs = []; backing_words = []; append_count = 0
    u.mem_write(B + cfg['cache'], struct.pack('<I', value))
    def hook(uc, addr, size, _):
        nonlocal append_count
        off = addr - B
        if off == m['cfg_query']:
            p = uc.reg_read(UC_ARM64_REG_X0); backing_words.append(p)
            uc.mem_write(p, struct.pack('<I', value)); ret(uc, status)
        elif off == m['fmt']:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2)); assert fmt in ('seccfg_state is %u', 'mtkrpmb_state is %u.')
            text = fmt % uc.reg_read(UC_ARM64_REG_W3)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode() + b'\0'); ret(uc, len(text))
        elif off == cfg['append']:
            outputs.append(('append', cstr(uc, uc.reg_read(UC_ARM64_REG_X0))))
            append_count += 1; ret(uc, status if append_count == 1 else 0)
        elif off in (cfg['info'], cfg['okay'], m['fail']):
            outputs.append((off, cstr(uc, uc.reg_read(UC_ARM64_REG_X0)))); ret(uc)
        elif off == m['log']: ret(uc)
        else: pac(uc, addr)
    u.hook_add(UC_HOOK_CODE, hook)
    entry = cfg['oem'] if which == 'lks' else m['cfg_entry'] if which == 'seccfg' else cfg['report']
    run(u, entry, saved)
    if which == 'lks': assert outputs == [(cfg['info'], 'lks = 1'), (cfg['okay'], '')]
    elif which == 'seccfg':
        assert [text for channel, text in outputs if channel == cfg['info']] == ['seccfg_state is 1', 'mtkrpmb_state is 4.']
        assert len([x for channel, x in outputs if channel == m['fail']]) == int(status != 0)
        assert len(backing_words) == 1 and int.from_bytes(u.mem_read(backing_words[0], 4), 'little') == value
    else:
        assert outputs == [('append', 'androidboot.verifiedbootstate=green')] + ([] if status else [('append', 'androidboot.flash.locked=1')])
    assert int.from_bytes(u.mem_read(B + cfg['cache'], 4), 'little') == value
    return dict(channel=which, fixture=value, status_fixture=status, outputs=outputs,
                cache_unchanged_by_reporting=True, seccfg_stack_word_unchanged=(which == 'seccfg'))


def root_case(kind, image, state, key_error=0, sink_status=0):
    cfg, m = BASES[kind], MAP[kind]; u, saved = machine(image)
    versions = (0x100001, 0x202611, 0x12345, 0x6789a); key = bytes(range(256))
    digest_words = (0x1020304050607080, 0x1112131415161718, 0xaabbccddeeff0123, 0x7766554433221100)
    u.mem_write(B + 0x20d8d0, struct.pack('<IIII', *versions))
    u.mem_write(B + 0x20d8e0, struct.pack('<QQQQ', *digest_words))
    u.reg_write(UC_ARM64_REG_W0, state); packets = []; hash_calls = []
    def hook(uc, addr, size, _):
        off = addr - B
        if off in m['backing']: raise AssertionError('Backing lock getter called by handoff')
        if off == m['log']: ret(uc)
        elif off == m['key']:
            if not key_error: uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), key)
            ret(uc, key_error)
        elif off == m['crypto']: ret(uc, 0)
        elif off == m['sha']: hash_calls.append(off)
        elif off == m['smc']:
            packets.append([uc.reg_read(reg) for reg in (UC_ARM64_REG_X0, UC_ARM64_REG_X1,
                UC_ARM64_REG_X2, UC_ARM64_REG_X3, UC_ARM64_REG_X4, UC_ARM64_REG_X5, UC_ARM64_REG_X6, UC_ARM64_REG_X7)])
            pointer = struct.unpack('<Q', uc.mem_read(uc.reg_read(UC_ARM64_REG_SP), 8))[0]
            uc.mem_write(pointer, struct.pack('<QQQQ', sink_status, 0, 0, 0)); ret(uc)
        elif off == m['sequence_end']: uc.emu_stop()
        else: pac(uc, addr)
    u.hook_add(UC_HOOK_CODE, hook)
    run(u, cfg['setter'] - 8, saved)
    assert int.from_bytes(u.mem_read(B + cfg['cache'], 4), 'little') == 0
    u.emu_start(B + m['sequence'], STOP, count=200000)
    assert u.reg_read(UC_ARM64_REG_PC) == B + m['sequence_end']
    key_hash = __import__('hashlib').sha256(key if not key_error else bytes(256)).digest()
    expected = [[0xc2000120, *struct.unpack('<QQQQ', key_hash), 0, 0, 0],
                [0xc2000120, 1, 0, *versions[:2], 0, 0, 0],
                [0xc2000120, *digest_words, 0, 0, 0],
                [0xc2000120, *versions[2:], 0, 0, 0, 0, 0],
                [0xc2000120, 0, 0, 0, 0, 0, 0, 0]]
    assert packets == expected and hash_calls
    assert all(u.reg_read(reg) == val for reg, val in saved.items())
    assert int.from_bytes(u.mem_read(B + 0x20d900, 4), 'little') == 0
    return dict(input_boot_state=state, key_error_fixture=key_error, sink_status_fixture=sink_status,
                root_key_hash=key_hash.hex(), lock_boolean=1, boot_state=0, all_five_packets_match=True,
                actual_common_wrapper_used=True, secure_sink_is_mock=True)


def check(kind):
    cfg = BASES[kind]; source = (ROOT / cfg['path']).read_bytes(); out = (HERE / kind / cfg['name']).read_bytes()
    assert build(kind, source)[0] == out == build(kind, source)[0]
    wrong = bytearray(source); wrong[12345] ^= 1
    try: build(kind, bytes(wrong))
    except ValueError: pass
    else: raise AssertionError('Wrong input accepted')
    states = []; rng = random.Random(9500)
    for value in [0, 1, 2, 3, 4, 0xffffffff] + [rng.getrandbits(32) for _ in range(94)]:
        states.append(state_case(kind, out, value))
    states.append(state_case(kind, out, 0, 'boolean'))
    for boot in (0, 1, 2, 0xffffffff): states.append(state_case(kind, out, 0, 'auth', boot))
    public = [public_case(kind, out, existing) for existing in (False, True)]
    exports = [export_case(kind, out, 'lks')]
    for value, rc in itertools.product((0, 1, 3, 4, 0xffffffff), (0, 7)):
        exports.append(export_case(kind, out, 'seccfg', value, rc))
        exports.append(export_case(kind, out, 'android', value, rc))
    roots = [root_case(kind, out, state, ke, rc)
             for state, ke, rc in itertools.product((0, 1, 2, 3, 0xffffffff), (0, 9), (0, 5))]
    hwc_kind = 'stock' if kind == 'stock_h3' else 'debug'
    hwc = cases_for(hwc_kind, out)
    assert check_external_branches(source, hwc_kind)
    root_report = root_validate(out, kind='stock_h3' if kind == 'stock_h3' else 'debug')
    if kind == 'debug_h3':
        gates = debug_gate_validate(out)
    else:
        gate_cases = []
        for secure, permitted, needs in itertools.product((0, 1), repeat=3):
            gate_cases.append(stock_probes.dispatch(out, secure, permitted, needs))
        for locked, name in itertools.product((0, 1), ('boot_para', 'boot', 'init_boot', 'lk')):
            gate_cases.append(stock_probes.flash_gate(out, locked, name))
        for locked, scratch in itertools.product((0, 1), (0, 1, 4)):
            gate_cases.append(stock_probes.erase_gate(out, locked, scratch))
        # AEE remains byte-identical to the Fenrir component already probed.
        sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/chain_analysis'))
        from analyze_chain import aee_case
        rows, _ = members(out); a = rows[6]; aee = out[a['offset'] + 512:a['offset'] + 512 + a['size']]
        for state, secure, flag14, flag18 in itertools.product((0, 1, 3, 4), (0, 1), (0, 1), (0, 1)):
            gate_cases.append(aee_case(aee, 'dispatcher', True, state, secure, flag14, flag18))
        for state, rc in itertools.product((0, 1, 3, 4), (0, 5)):
            gate_cases.append(aee_case(aee, 'flash_gate', True, state, rc=rc))
        gates = dict(status='PASS_FENRIR_GATES_RETAINED', total_native_cases=len(gate_cases), cases=gate_cases)
    rows, tail = members(out); certs = []
    for i, row in enumerate(rows):
        if row['name'] not in ('cert1', 'cert2'): continue
        raw = out[row['offset'] + 512:row['offset'] + 512 + row['size']]
        skip = cfg['prefix_length'] if i == 2 else 90 if kind == 'stock_h3' and i == 8 else 0
        der = raw[skip:]; end = 8 + int.from_bytes(der[6:8], 'big')
        load_der_public_key(der[181:475]).verify(der[-256:], der[4:end],
            padding.PSS(mgf=padding.MGF1(hashes.SHA256()), salt_length=32), hashes.SHA256())
        certs.append(dict(member=i, retained_der_signature_valid=True))
    parsers = []
    bl = rows[3]; bl2 = out[bl['offset'] + 512:bl['offset'] + 512 + bl['size']]
    for ci, component in ((2, 0), (8, 6)):
        cert = rows[ci]; record = out[cert['offset']:cert['offset'] + 512 + cert['size']]
        comp = rows[component]
        for mask in (0x60, 0x1f4e2, 0x1f400):
            if kind == 'stock_h3':
                got = release_bl2_parser(3, 'hybrid-final', mask, record)
                assert got['returned'] and got['error'] is None and int(got['status'], 16) == 0
            else:
                got = bl2_run(bl2, record, mask); assert got['result'] == 0
            if mask != 0x1f400:
                assert got['payload_hash'] == sha(out[comp['offset'] + 512:comp['end']])
                assert got['header_hash'] == sha(out[comp['offset']:comp['offset'] + 512])
            parsers.append(dict(component=component, **got))
    counts = dict(common_state=len(states), publication=len(public), public_exports=len(exports),
                  root_handoff=len(roots), hwc=len(hwc), gates=gates['total_native_cases'],
                  init_boot_hash_path=root_report['total_cpu_cases'], bl2_parser=len(parsers))
    report = dict(status='PASS_BOUNDED_NATIVE_HYBRID_NOT_DEVICE_BOOT', variant=kind,
                  input_sha256=cfg['sha256'], output_sha256=sha(out), deterministic_build=True, wrong_input_rejected=True,
                  common_state_cases=states, native_publication_cases=public, public_export_cases=exports,
                  root_handoff_cases=roots, hwc_cases=hwc, root_modified_init_boot_probe=root_report,
                  command_gate_probe=gates, native_bl2_parser_cases=parsers, retained_der_signatures=certs,
                  cpu_counts=counts, total_native_cpu_cases=sum(counts.values()),
                  mocks=['PAC/logging and boot selector boundary', 'Public variable-cache lookup/update and formatter',
                         'Partition/slot/append/UI/callback boundaries and seccfg value fixtures',
                         'Secure-monitor sink and OEM-key extraction; native software SHA256 runs',
                         'See init_boot hash probe for synthetic-image and same-image crypto-engine boundary model'],
                  limits=['No actual phone boot/root, TEE, storage writes, OTA or attestation tested',
                          'Root descriptor test uses synthetic image/hash mutation, not actual root installation',
                          'AVB result filters and descriptor paths are bounded; not a full verifier/SoC simulation',
                          'Fenrir green override hides software warning states by design',
                          'HWC is countrycode mapping, not universal ROM/preloader compatibility'])
    (HERE / kind / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    print(kind, report['output_sha256'], report['cpu_counts'], 'root/init_boot and cert/parser PASS')


if __name__ == '__main__':
    for kind in BASES: check(kind)

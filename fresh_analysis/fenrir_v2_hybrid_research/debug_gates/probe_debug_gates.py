"""Native debug-build gate coverage, without firmware writes or phone access.

This proves the bounded paths actually present in the debug build. It does not
run partition writes, a full command handler, hardware query or storage device.
"""
from pathlib import Path
import hashlib
import itertools
import json
import struct
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
INPUT = ROOT / 'experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img'
EXPECTED = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'
DEBUG_GATE_PATCHES = []  # Equivalent early gates are absent in this build.
DATA, STACK, STOP = 0x3000000, 0x3100000, 0x4000000
REGS = (UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21,
        UC_ARM64_REG_X22, UC_ARM64_REG_X23, UC_ARM64_REG_X24,
        UC_ARM64_REG_X25, UC_ARM64_REG_X26, UC_ARM64_REG_X27,
        UC_ARM64_REG_X28, UC_ARM64_REG_X29)
SURFACES = {
    'main': dict(base=0x90700000, component=0, dispatcher=0xa184, dispatcher_end=0xa1a4,
                 dispatcher_scan=(0x9e6c, 0xa3f0),
                 global_context=0x135178, global_size=0x1351b8,
                 flash=0xcc90, erase=0xced0, partition_boundary=0xdcd0,
                 lock_helpers=(0x88c18, 0x885fc, 0x8856c, 0xb6588),
                 diagnostic_log=0x7ea84),
    'aee': dict(base=0xffff0000be000000, component=6, dispatcher=0x13d3c, dispatcher_end=0x13d5c,
                dispatcher_scan=(0x13a24, 0x13fb0),
                global_context=0x102968, global_size=0x1029a8,
                flash=0x15fd0, erase=0x16210, partition_boundary=0x17010,
                lock_helpers=(0x6791c, 0x67bfc, 0x906c4), diagnostic_log=0x60e0c),
}


def components(image):
    off = 0; rows = []
    for i in range(15):
        assert image[off:off + 4] == bytes.fromhex('88168858')
        size = struct.unpack_from('<I', image, off + 4)[0]
        end = (off + 512 + size + 15) & ~15
        assert end <= len(image)
        rows.append(image[off + 512:off + 512 + size]); off = end
    return rows


def machine(blob, base):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    for address, size in ((base, 0x500000), (DATA, 0x10000), (STACK, 0x10000), (STOP, 0x1000)):
        u.mem_map(address, size)
    u.mem_write(base, blob)
    for i, reg in enumerate(REGS): u.reg_write(reg, 0x12345000 + i)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    return u


def cstr(u, pointer):
    out = bytearray()
    for i in range(1024):
        b = bytes(u.mem_read(pointer + i, 1))
        if b == b'\0': return out.decode()
        out += b
    raise AssertionError('Unterminated native string')


def ret(u, value=0):
    u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64) - 1))
    u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))


def dispatcher_case(blob, surface, state, secure, flag14, flag18):
    config = SURFACES[surface]; base = config['base']
    u = machine(blob, base); node = DATA + 0x1000; args = DATA + 0x2000
    context = DATA + 0x3000; callback = STOP + 0x100
    u.reg_write(UC_ARM64_REG_X25, node); u.reg_write(UC_ARM64_REG_X28, args)
    u.reg_write(UC_ARM64_REG_X20, context)
    u.reg_write(UC_ARM64_REG_X29, STACK + 0xf100)
    u.mem_write(STACK + 0xf0fc, struct.pack('<I', secure))
    u.mem_write(node + 0x10, struct.pack('<III', 3, flag14, flag18))
    u.mem_write(node + 0x20, struct.pack('<Q', callback))
    u.mem_write(base + config['global_context'], struct.pack('<Q', DATA + 0x4000))
    u.mem_write(base + config['global_size'], struct.pack('<I', 1234))
    seen = []; queries = []

    def hook(uc, address, size, _):
        off = address - base
        if address == callback:
            seen.append([uc.reg_read(reg) for reg in (UC_ARM64_REG_X0, UC_ARM64_REG_X1, UC_ARM64_REG_X2)])
            ret(uc)
        elif off == config['dispatcher_end']:
            uc.emu_stop()
        elif off in config['lock_helpers']:
            queries.append(off)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', state)); ret(uc)
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(base + config['dispatcher'], STOP, count=10000)
    assert u.reg_read(UC_ARM64_REG_PC) == base + config['dispatcher_end']
    assert seen == [[args + 3, DATA + 0x4000, 1234]]
    assert not queries
    return dict(surface=surface, kind='dispatcher_callback_slice', common_lock_fixture=state,
                secure_fixture=secure, descriptor_flag14=flag14, descriptor_flag18=flag18,
                callback_arguments=seen[0], lock_query_calls=queries, pass_case=True)


def io_entry_case(blob, surface, command, state, name):
    config = SURFACES[surface]; base = config['base']
    u = machine(blob, base)
    u.mem_write(DATA, name.encode() + b'\0')
    u.reg_write(UC_ARM64_REG_X0, DATA)
    u.reg_write(UC_ARM64_REG_X1, DATA + 0x5000)
    u.reg_write(UC_ARM64_REG_W2, 4096)
    seen = []; queries = []

    def hook(uc, address, size, _):
        off = address - base
        if off == config['partition_boundary']:
            seen.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); uc.emu_stop()
        elif off in config['lock_helpers']:
            queries.append(off)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), struct.pack('<I', state)); ret(uc)
        elif off == config['diagnostic_log']:
            ret(uc)
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)

    u.hook_add(UC_HOOK_CODE, hook)
    u.emu_start(base + config[command], STOP, count=10000)
    assert u.reg_read(UC_ARM64_REG_PC) == base + config['partition_boundary']
    assert seen == [name], (surface, command, name, seen)
    assert not queries
    return dict(surface=surface, kind=command + '_entry_to_partition_query', common_lock_fixture=state,
                target=name, observed_partition_name=seen[0], lock_query_calls=queries,
                native_string_copy_search_length_executed=True, pass_case=True)


def validate(image):
    blobs = components(image); cases = []; static = []
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    for surface, config in SURFACES.items():
        blob = blobs[config['component']]
        assert b'Flashing is not allowed in Lock state' not in blob
        lo, hi = config['dispatcher_scan']
        descriptor = []; query_calls = []
        for instruction in decoder.disasm(blob[lo:hi], lo):
            if '[x25' in instruction.op_str:
                descriptor.append(dict(payload_offset=hex(instruction.address),
                                       instruction=instruction.mnemonic + ' ' + instruction.op_str))
                assert '#0x14]' not in instruction.op_str and '#0x18]' not in instruction.op_str
            if instruction.mnemonic == 'bl' and instruction.op_str.startswith('#0x'):
                if int(instruction.op_str[1:], 16) in config['lock_helpers']:
                    query_calls.append(hex(instruction.address))
        assert not query_calls
        static.append(dict(surface=surface, dispatcher_scan_span=[hex(lo), hex(hi)],
                           descriptor_x25_accesses=descriptor, direct_lock_query_calls=query_calls))
        for state, secure, flag14, flag18 in itertools.product((0, 1, 3, 4), (0, 1), (0, 1), (0, 1)):
            cases.append(dispatcher_case(blob, surface, state, secure, flag14, flag18))
        for command, state, name in itertools.product(('flash', 'erase'), (0, 1, 3, 4),
                                                     ('boot', 'init_boot', 'lk', 'boot_para')):
            cases.append(io_entry_case(blob, surface, command, state, name))
    return dict(status='PASS_DEBUG_GATE_SLICES_NOT_PARTITION_WRITE_OR_FULL_COMMAND',
                input_sha256=hashlib.sha256(image).hexdigest(), total_native_cases=len(cases),
                cases=cases, static_dispatcher_observations=static,
                recommended_gate_patches=DEBUG_GATE_PATCHES,
                facts=['Debug main and AEE already compile out the early locked-flash/erase gates patched by Fenrir on release H3',
                       'Debug command callback slice directly loads descriptor and calls callback without the release +0x14/+0x18 checks',
                       'All checked state fixtures reach the partition query with no common-lock query in these entry paths'],
                limits=['Dispatcher replay is a bounded callback slice, not the entire command parsing loop',
                        'Flash/erase replay stops before partition-specific eligibility/storage routines and performs no writes',
                        'No new gate patches needed here does not mean all later protections or query errors are absent',
                        'Common state is a labelled fixture that these particular paths never query; this is not a fake-lock getter test',
                        'No hardware, secure monitor, root/boot success or full flash/erase commands tested'])


def main():
    image = INPUT.read_bytes(); assert hashlib.sha256(image).hexdigest() == EXPECTED
    report = validate(image)
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')
    c = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    lines = []
    blobs = components(image)
    for surface, config in SURFACES.items():
        blob = blobs[config['component']]
        for label, lo, hi in [('dispatch', config['dispatcher'], config['dispatcher_end']),
                              ('flash prologue', config['flash'], config['flash'] + 0x50),
                              ('erase prologue', config['erase'], config['erase'] + 0x50)]:
            lines.append(surface + ': ' + label)
            lines += [f'{i.address:08x}: {i.bytes.hex()} {i.mnemonic} {i.op_str}'.rstrip()
                      for i in c.disasm(blob[lo:hi], lo)]
    (HERE / 'disassembly.txt').write_text('\n'.join(lines) + '\n')
    print(report['status'], report['total_native_cases'], 'native cases; no gate patches required')


if __name__ == '__main__':
    main()

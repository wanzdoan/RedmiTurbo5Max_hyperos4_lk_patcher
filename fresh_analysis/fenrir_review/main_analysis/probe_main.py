"""Bounded native comparison of Fenrir versus the exact H3 release base.

Never executes the file as a host program or accesses a phone. Hardware/state
boundaries for the stock control are explicitly labelled fixtures.
"""
from pathlib import Path
import hashlib
import itertools
import json
import random
import struct
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM, UC_HOOK_CODE
from unicorn.arm64_const import *

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
INPUT = HERE.parent / 'input/dash-fenrir-versioned.bin'
STOCK = ROOT / 'lk_hyperos3_original/lk.img'
INPUT_SHA = 'dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113'
B, STACK, DATA, STOP = 0x90700000, 0x2000000, 0x3000000, 0x4000000
SAVED = (UC_ARM64_REG_X19, UC_ARM64_REG_X20, UC_ARM64_REG_X21,
         UC_ARM64_REG_X22, UC_ARM64_REG_X23, UC_ARM64_REG_X24,
         UC_ARM64_REG_X25, UC_ARM64_REG_X26, UC_ARM64_REG_X27,
         UC_ARM64_REG_X28, UC_ARM64_REG_X29)
sha = lambda d: hashlib.sha256(d).hexdigest()


def machine(image):
    u = Uc(UC_ARCH_ARM64, UC_MODE_ARM)
    for start, length in ((B, 0x500000), (STACK, 0x10000), (DATA, 0x10000), (STOP, 0x1000)):
        u.mem_map(start, length)
    size = int.from_bytes(image[4:8], 'little')
    u.mem_write(B, image[512:512 + size])
    saved = {reg: 0x12345000 + n for n, reg in enumerate(SAVED)}
    for reg, value in saved.items(): u.reg_write(reg, value)
    u.reg_write(UC_ARM64_REG_SP, STACK + 0xf000)
    u.reg_write(UC_ARM64_REG_LR, STOP)
    return u, saved


def ret(u, value=0):
    u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64) - 1))
    u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))


def cstr(u, p):
    out = bytearray()
    for n in range(2048):
        b = bytes(u.mem_read(p + n, 1))
        if b == b'\0': return out.decode()
        out += b
    raise AssertionError('Unterminated string')


def hook_common(u, off, control_state=None):
    if off in (0x7de54, 0x8845c): ret(u); return True
    if off == 0x88750 and control_state is not None:
        u.mem_write(u.reg_read(UC_ARM64_REG_X0), struct.pack('<I', control_state)); ret(u); return True
    if int.from_bytes(u.mem_read(B + off, 4), 'little') in (0xd503233f, 0xd50323bf):
        u.reg_write(UC_ARM64_REG_PC, B + off + 4); return True
    return False


def run(u, entry, saved=None):
    u.emu_start(B + entry, STOP, count=50000)
    if saved is not None:
        assert u.reg_read(UC_ARM64_REG_PC) == STOP
        assert u.reg_read(UC_ARM64_REG_SP) == STACK + 0xf000
        assert all(u.reg_read(reg) == value for reg, value in saved.items())


def getter(image, initial):
    u, saved = machine(image)
    u.mem_write(DATA, struct.pack('<I', initial))
    u.reg_write(UC_ARM64_REG_X0, DATA)
    run(u, 0x88750, saved)
    assert u.reg_read(UC_ARM64_REG_W0) == 0
    assert bytes(u.mem_read(DATA, 4)) == struct.pack('<I', 4)
    return dict(initial=initial, returned_state=4, status=0, pass_case=True)


def lock_boolean(image, control_state=None, boot=0, auth=False):
    u, saved = machine(image)
    if auth: u.reg_write(UC_ARM64_REG_W0, boot)
    else: u.reg_write(UC_ARM64_REG_X0, DATA)
    u.hook_add(UC_HOOK_CODE, lambda uc, addr, size, _: hook_common(uc, addr - B, control_state))
    run(u, 0x689cc if auth else 0x8869c, saved)
    value = u.reg_read(UC_ARM64_REG_W0) if auth else int.from_bytes(u.mem_read(DATA, 4), 'little')
    expected = int((4 if control_state is None else control_state) != 3)
    assert value == expected
    return dict(stock_control_state=control_state, kind='image_auth_required' if auth else 'locked_boolean',
                boot_mode=boot, value=value, pass_case=True)


def fastboot_publish(image, existing, control_state=None):
    u, _ = machine(image); updates=[]
    empty = DATA + 0x1000
    stale = DATA + 0x1100
    u.mem_write(empty, b'\0'); u.mem_write(stale, b'stale\0')
    def hook(uc, addr, size, _):
        off = addr - B
        if off == 0xc3f8: uc.emu_stop()
        elif off == 0x9b2c: ret(uc, stale if existing else empty)
        elif off == 0x7ede4:
            a, b = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)), cstr(uc, uc.reg_read(UC_ARM64_REG_X1))
            ret(uc, (a > b) - (a < b))
        elif off in (0x9a38, 0x9acc):
            updates.append((cstr(uc, uc.reg_read(UC_ARM64_REG_X0)), cstr(uc, uc.reg_read(UC_ARM64_REG_X1))))
            ret(uc)
        else: hook_common(uc, off, control_state)
    u.hook_add(UC_HOOK_CODE, hook)
    run(u, 0xc334)
    locked = (4 if control_state is None else control_state) != 3
    assert updates == [('secure', 'yes' if locked else 'no'), ('unlocked', 'no' if locked else 'yes')]
    return dict(stock_control_state=control_state, existing_cache_entry=existing, updates=updates, pass_case=True)


def dispatch(image, secure, permitted_secure, needs_unlock, control_state=None):
    u, _ = machine(image); sp = u.reg_read(UC_ARM64_REG_SP); frame = sp + 0x100
    node, args, ctx = DATA + 0x1000, DATA + 0x2000, DATA + 0x3000
    u.reg_write(UC_ARM64_REG_X29, frame); u.reg_write(UC_ARM64_REG_X25, node)
    u.reg_write(UC_ARM64_REG_X28, args); u.reg_write(UC_ARM64_REG_X20, ctx)
    u.mem_write(frame - 4, struct.pack('<I', secure))
    u.mem_write(node + 0x10, struct.pack('<III', 0, permitted_secure, needs_unlock))
    u.mem_write(node + 0x20, struct.pack('<Q', STOP + 0x100))
    calls=[]
    def hook(uc, addr, size, _):
        off = addr - B
        if off == 0xa148: uc.emu_stop()
        elif addr == STOP + 0x100: calls.append(True); ret(uc)
        else: hook_common(uc, off, control_state)
    u.hook_add(UC_HOOK_CODE, hook)
    run(u, 0xa0fc)
    expected = True if control_state is None else (not secure or permitted_secure) and (not needs_unlock or control_state == 3)
    assert bool(calls) == bool(expected)
    return dict(stock_control_state=control_state, secure_fixture=secure,
                permitted_secure=permitted_secure, needs_unlock=needs_unlock,
                command_callback_invoked=bool(calls), pass_case=True)


def flash_gate(image, locked, name, control=False):
    u, _ = machine(image); sp=u.reg_read(UC_ARM64_REG_SP); frame=sp+0x100
    u.reg_write(UC_ARM64_REG_X29, frame); u.reg_write(UC_ARM64_REG_X21, DATA)
    u.mem_write(frame + 0x1c, struct.pack('<I', locked)); u.mem_write(DATA, name.encode()+b'\0')
    stops=[]
    def hook(uc, addr, size, _):
        off=addr-B
        if off in (0xce14, 0xd138): stops.append(off); uc.emu_stop()
        elif off == 0x7ede4:
            a,b=cstr(uc,uc.reg_read(UC_ARM64_REG_X0)),cstr(uc,uc.reg_read(UC_ARM64_REG_X1))
            ret(uc,(a>b)-(a<b))
    u.hook_add(UC_HOOK_CODE,hook); run(u,0xcde8)
    expected=0xce14 if not control or not locked or name=='boot_para' else 0xd138
    assert stops==[expected]
    return dict(stock_control=control, locked_fixture=locked, target=name, gate_passed=expected==0xce14, pass_case=True)


def erase_gate(image, locked, scratch, control=False):
    u,_=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.mem_write(sp+4,struct.pack('<I',locked));u.mem_write(sp+0xc,struct.pack('<I',scratch))
    stops=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off in (0xd1e8,0xd510):stops.append(off);uc.emu_stop()
    u.hook_add(UC_HOOK_CODE,hook);run(u,0xd1d0)
    expected=0xd510 if control and locked==1 else 0xd1e8
    assert stops==[expected]
    return dict(stock_control=control,locked_fixture=locked,scratch_word=scratch,gate_passed=expected==0xd1e8,pass_case=True)


def avb_flags(image, control_state=None):
    u,_=machine(image);sp=u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29,sp+0x100);stops=[]
    def hook(uc,addr,size,_):
        off=addr-B
        if off==0x6867c:stops.append(off);uc.emu_stop()
        else:hook_common(uc,off,control_state)
    u.hook_add(UC_HOOK_CODE,hook);run(u,0x68640)
    assert stops==[0x6867c]
    flag=u.reg_read(UC_ARM64_REG_W22);candidate=u.reg_read(UC_ARM64_REG_W21)
    state=int.from_bytes(u.mem_read(B+0x121990,4),'little')
    assert flag==(1 if control_state is None or control_state==3 else 0)
    assert candidate==(2 if control_state==3 else 3)
    assert state==(0 if control_state is None else candidate)
    return dict(stock_control_state=control_state,allow_verification_error_flag=flag,
                selected_initial_boot_state=candidate,stored_verified_boot_state=state,pass_case=True)


def warning(image, state, boot, control=False):
    u,saved=machine(image);ui=[];fatal=[]
    u.reg_write(UC_ARM64_REG_W0,state)
    u.hook_add(UC_HOOK_CODE,lambda uc,addr,size,_:hook_common(uc,addr-B))
    run(u,0x692f4,saved)
    stored=int.from_bytes(u.mem_read(B+0x121990,4),'little')
    assert stored==(state if control else 0)
    u.reg_write(UC_ARM64_REG_LR,STOP);u.reg_write(UC_ARM64_REG_X0,DATA);u.mem_write(DATA,b'boot\0')
    def hook(uc,addr,size,_):
        off=addr-B
        if off==0x10b48:ret(uc,boot)
        elif off==0x866d8:ui.append(uc.reg_read(UC_ARM64_REG_W0));ret(uc)
        elif off in (0x14ee8,0x7cbd8,0x9d8c):ret(uc)
        elif off==0x693b0:fatal.append(cstr(uc,uc.reg_read(UC_ARM64_REG_X0)));ret(uc)
    u.hook_add(UC_HOOK_CODE,hook);run(u,0x69308,saved)
    expected=([2] if boot!=1 else []) if control and state in (1,3) else ([1] if control and state==2 and boot!=1 else [])
    assert ui==expected
    assert fatal==(['boot'] if control and state in (1,3) else [])
    return dict(stock_control=control,setter_input=state,stored_state=stored,boot_mode=boot,
                warning_ids=ui,fatal_handler_calls=fatal,pass_case=True)


def main():
    image=INPUT.read_bytes();stock=STOCK.read_bytes();assert sha(image)==INPUT_SHA
    n=int.from_bytes(stock[4:8],'little');assert n==0x139b28==int.from_bytes(image[4:8],'little')
    changed=[i for i in range(n) if image[512+i]!=stock[512+i]];assert len(changed)==63
    decoder=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);words=[]
    for off in sorted({i&~3 for i in changed}):
        words.append(dict(payload_offset=hex(off),whole_file_offset=hex(off+512),
                          original=stock[512+off:516+off].hex(),fenrir=image[512+off:516+off].hex(),
                          original_disassembly=[i.mnemonic+' '+i.op_str for i in decoder.disasm(stock[512+off:516+off],off)],
                          fenrir_disassembly=[i.mnemonic+' '+i.op_str for i in decoder.disasm(image[512+off:516+off],off)]))
    rng=random.Random(9500)
    getters=[getter(image,x) for x in [0,1,2,3,4,0xffffffff]+[rng.getrandbits(32) for _ in range(94)]]
    booleans=[];publish=[];gates=[];flash=[];erase=[];flags=[];warnings=[]
    for src,control in [(image,None),(stock,3),(stock,4)]:
        booleans.append(lock_boolean(src,control))
        for boot in (0,1,99):booleans.append(lock_boolean(src,control,boot,True))
        for existing in (False,True):publish.append(fastboot_publish(src,existing,control))
        for a,b,c in itertools.product((0,1),repeat=3):gates.append(dispatch(src,a,b,c,control))
        flags.append(avb_flags(src,control))
    for src,control in [(image,False),(stock,True)]:
        for locked,name in itertools.product((0,1),('boot_para','boot','init_boot','lk')):
            flash.append(flash_gate(src,locked,name,control))
        for locked,scratch in itertools.product((0,1),(0,1,4)):
            erase.append(erase_gate(src,locked,scratch,control))
        for state,boot in itertools.product((0,1,2,3),(0,1)):
            warnings.append(warning(src,state,boot,control))
    groups={'getter':getters,'lock_and_auth':booleans,'fastboot_publication':publish,'dispatch_permissions':gates,
            'flash_gate':flash,'erase_gate':erase,'AVB_flag_selection':flags,'setter_and_warning':warnings}
    report=dict(status='PASS_BOUNDED_NATIVE_NOT_DEVICE_BOOT',input_sha256=INPUT_SHA,stock_sha256=sha(stock),
                main_payload_size=n,changed_main_bytes=len(changed),changed_words=words,
                cpu_case_counts={k:len(v) for k,v in groups.items()},total_cpu_cases=sum(map(len,groups.values())),
                cases=groups,mocks=['PAC/logging','Original common lock getter only in explicitly labelled stock controls',
                                  'Public-variable lookup/update and strcmp boundaries','Command callback sink',
                                  'Boot selector, UI/timing and fatal-handler sinks'],
                limits=['No full flash/erase or AVB slot verification execution','No phone/TEE/secure world or boot proof',
                        'Build identity bytes are data, despite incidental instruction disassembly'])
    (HERE/'validation.json').write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps(dict(status=report['status'],counts=report['cpu_case_counts'],total=report['total_cpu_cases']),indent=2))


if __name__=='__main__':main()

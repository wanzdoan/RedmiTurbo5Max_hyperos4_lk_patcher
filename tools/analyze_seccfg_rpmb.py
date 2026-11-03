import struct
import re

lk = open('lk_hyperos4_original/lk.img', 'rb').read()[0x200:0x200+1284936]

def find_str_and_refs(s):
    pos = lk.find(s)
    if pos < 0:
        print(f"String '{s.decode(errors='ignore')}' not found")
        return []
    va = 0xffff000090700000 + pos
    print(f"=== String: '{s.decode(errors='ignore')}' at offset 0x{pos:x} (VA 0x{va:x}) ===")
    refs = []
    for off in range(0, len(lk)-8, 4):
        insn = struct.unpack('<I', lk[off:off+4])[0]
        if (insn & 0x9f000000) == 0x90000000:
            immlo = (insn >> 29) & 0x3
            immhi = (insn >> 5) & 0x7ffff
            imm = (immhi << 2) | immlo
            if imm & 0x100000:
                imm -= 0x200000
            pc_page = (0xffff000090700000 + off) & ~0xfff
            target_page = pc_page + (imm << 12)
            if target_page == (va & ~0xfff):
                next_insn = struct.unpack('<I', lk[off+4:off+8])[0]
                if (next_insn & 0xffc00000) == 0x91000000:
                    add_imm = (next_insn >> 10) & 0xfff
                    if target_page + add_imm == va:
                        ref_va = 0xffff000090700000 + off
                        print(f"  Referenced at offset 0x{off:x} (VA 0x{ref_va:x})")
                        refs.append(off)
    return refs

targets = [
    b'Read TokenFrom Rpmb Failed!',
    b'Write TokenTo Rpmb Failed!',
    b'Write Token To Rpmb Failed!',
    b'seccfg_state is %u',
    b'Get seccfg_state failed.',
    b'Set seccfg_state failed.',
    b'rpmblock_support',
    b'Error : rpmb_init_misecids_base_offset fail',
    b'Error : rpmb_read fail',
    b'[%s] seccfg not found!',
    b'Token is empty, failed.',
    b'oem fbreason',
    b'unlocked',
    b'seccfg_ver: %x',
    b'[%s] Analysis major seccfg.',
    b'[%s] seccfg sej cipher'
]

for t in targets:
    find_str_and_refs(t)

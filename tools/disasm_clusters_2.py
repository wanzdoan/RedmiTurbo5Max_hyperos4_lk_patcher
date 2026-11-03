import capstone
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM

lk_data = open('lk_hyperos4_original/lk.img', 'rb').read()[0x200:0x200+1284936]
md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
md.detail = True

def disasm_range(start_off, count, title=""):
    print(f"\n{'='*20} {title} (Offset 0x{start_off:x}, VA 0x{0xffff000090700000+start_off:x}) {'='*20}")
    code = lk_data[start_off:start_off + count * 4]
    va = 0xffff000090700000 + start_off
    for insn in md.disasm(code, va):
        print(f"0x{insn.address:08x} (0x{insn.address - 0xffff000090700000:06x}): {insn.mnemonic:<8} {insn.op_str}")

# Disassemble 0xb3c28 (sec_get_seccfg / get_seccfg_state)
disasm_range(0xb3c28, 45, "sec_get_seccfg function (0xb3c28)")

# Disassemble 0xcd780 - 0xcd9f0 (rpmb_init_misecids & rpmb_read)
disasm_range(0xcd780, 50, "rpmb_init_misecids / read token")
disasm_range(0xcd980, 50, "rpmb_read misecids token")

# Disassemble 0x89aec (mi_get_lock_state original)
disasm_range(0x89aec, 50, "mi_get_lock_state original")

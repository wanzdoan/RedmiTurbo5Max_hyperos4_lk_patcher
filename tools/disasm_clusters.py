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

# Disassemble around 0xc400 - 0xcb00 (fastboot lock / seccfg state)
disasm_range(0xc400, 40, "Fastboot unlocked string handler")
disasm_range(0xc4ec, 30, "rpmblock_support check")
disasm_range(0xc980, 50, "Get/Set seccfg_state")

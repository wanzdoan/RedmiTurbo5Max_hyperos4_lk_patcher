import struct
import capstone
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM

lk_data = open('lk_hyperos4_original/lk.img', 'rb').read()[0x200:0x200+1284936]

def get_str(va):
    off = va - 0xffff000090700000
    if 0 <= off < len(lk_data):
        end = lk_data.find(b'\x00', off)
        if end != -1:
            return lk_data[off:end].decode(errors='replace')
    return "???"

strings_to_check = [
    0xffff0000907fe339,
    0xffff0000907e3b88,
    0xffff0000907dd40e,
    0xffff0000907d6dc6,
    0xffff0000907f388b,
]

for s in strings_to_check:
    print(f"VA 0x{s:x} -> '{get_str(s)}'")

md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
md.detail = True

def disasm_func(start_va, count):
    print(f"\nDisassembling VA 0x{start_va:x}:")
    off = start_va - 0xffff000090700000
    code = lk_data[off:off + count * 4]
    for insn in md.disasm(code, start_va):
        print(f"0x{insn.address:08x}: {insn.mnemonic:<8} {insn.op_str}")

disasm_func(0xffff00009078a0c8, 40)
disasm_func(0xffff000090789bac, 40)

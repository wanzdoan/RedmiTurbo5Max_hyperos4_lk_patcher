import capstone
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM

lk_data = open('lk_hyperos4_original/lk.img', 'rb').read()[0x200:0x200+1284936]

def get_str(va):
    off = va - 0xffff000090700000
    if 0 <= off < len(lk_data):
        end = lk_data.find(b'\x00', off)
        if end != -1:
            return lk_data[off:end].decode(errors='replace')
    return '???'

md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
md.detail = True

# Find function start before 0xc400
# Let's disassemble from 0xc300 to 0xc560
code = lk_data[0xc300:0xc560]
for insn in md.disasm(code, 0xffff00009070c300):
    comment = ""
    # Check if ADRP/ADD to find string
    print(f"0x{insn.address:08x}: {insn.mnemonic:<8} {insn.op_str}")

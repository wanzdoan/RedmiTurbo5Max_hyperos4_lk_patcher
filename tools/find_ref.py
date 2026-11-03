from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
import sys

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
md.detail = True

# Preloader load address: from GFH, load_addr = 0x02000f00, entry = 0x02001000
# String VA: 0x02000f00 + 0xd02ef = 0x020d11ef (or base 0x2000000 + 0xd02ef)
target_off = 0xd02ef

for base in [0x02000000, 0x02000f00, 0x02001000]:
    target_va = base + target_off
    target_page = target_va & ~0xfff
    target_add = target_va & 0xfff
    for off in range(0x100, 0xc0000, 4):
        # check adrp targeting target_page
        b = p3[off:off+4]
        for ins in md.disasm(b, base + off):
            if ins.mnemonic == 'adrp' and ins.operands[1].imm == target_page:
                # check next instruction
                b_next = p3[off+4:off+8]
                for ins2 in md.disasm(b_next, base + off + 4):
                    if ins2.mnemonic == 'add' and ins2.operands[2].imm == target_add:
                        print(f"Found reference at Base {hex(base)}: off 0x{off:x} (VA 0x{ins.address:x})")
                        # print surrounding instructions
                        for ins_ctx in md.disasm(p3[off-32:off+64], ins.address - 32):
                            print(f"  {ins_ctx.address:#x}: {ins_ctx.mnemonic} {ins_ctx.op_str}")
                        sys.exit(0)
print("No ADRP/ADD match found for standard bases.")

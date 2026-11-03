from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
import sys

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
md.detail = True

# Code starts at 0x100 and ends around 0xc0000
code_start = 0x100
code_end = 0xc0000

real_code_diffs = []
for off in range(code_start, code_end, 4):
    w3 = p3[off:off+4]
    w4 = p4[off:off+4]
    if w3 != w4:
        # disassemble
        i3 = list(md.disasm(w3, off))
        i4 = list(md.disasm(w4, off))
        if not i3 or not i4:
            real_code_diffs.append((off, w3.hex(), w4.hex(), "undecodable"))
            continue
        ins3 = i3[0]
        ins4 = i4[0]
        # check if mnemonic is same
        if ins3.mnemonic != ins4.mnemonic:
            real_code_diffs.append((off, f"{ins3.mnemonic} {ins3.op_str}", f"{ins4.mnemonic} {ins4.op_str}", "mnemonic"))
        else:
            # check if only operand immediate differs by ~0x40 (pointer shift)
            pass

print(f"Total instructions checked: {(code_end - code_start)//4}")
print(f"Mnemonic / Control Flow differences in code section: {len(real_code_diffs)}")
for off, s3, s4, reason in real_code_diffs[:30]:
    print(f"  0x{off:06x}: H3='{s3}' | H4='{s4}' ({reason})")

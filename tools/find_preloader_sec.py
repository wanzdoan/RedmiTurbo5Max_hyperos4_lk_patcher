import struct

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

s_pos3 = p3.find(b'[SEC_POLICY] lock_state = 0x%x')
s_pos4 = p4.find(b'[SEC_POLICY] lock_state = 0x%x')
print(f"H3 string pos: {hex(s_pos3)}, H4 string pos: {hex(s_pos4)}")

# Load address of preloader is usually 0x02000f00 or 0x02001000
# Let's search for references to s_pos3 in code
for p, s_pos, name in [(p3, s_pos3, 'H3'), (p4, s_pos4, 'H4')]:
    # search literal in ARM64: ADRP/ADD or literal address
    print(f"\n--- Checking {name} ---")
    va = 0x02001000 + s_pos # rough VA
    print(f"Possible VA: {hex(va)}")
    # search for 4-byte or 8-byte address in data/rodata
    for off in range(0, len(p) - 8, 4):
        val = struct.unpack('<I', p[off:off+4])[0]
        if val == va or val == (va & 0xffffffff):
            print(f"Pointer at {hex(off)} -> {hex(val)}")

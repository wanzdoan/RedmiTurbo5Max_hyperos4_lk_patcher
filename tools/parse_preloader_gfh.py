import struct

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

print("Header 0x00 - 0x100 comparison:")
diffs = [i for i in range(0x100) if p3[i] != p4[i]]
print(f"Header diff count (0x0 - 0x100): {len(diffs)}")
for d in diffs:
    print(f"  0x{d:02x}: H3={p3[d]:#04x} H4={p4[d]:#04x}")

if not diffs:
    print("GFH headers (0x00 - 0x100) are 100% IDENTICAL!")

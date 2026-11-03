import struct

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()

# Let's search for string 'Find socid in cert2' at 0xd02fd
# and find the code referencing it
pos = p3.find(b'Find socid in cert2')
print("String pos:", hex(pos))

# Search for ADRP / references to this string around pos
# Base address is 0x02001000 or similar
for off in range(0, len(p3) - 8, 4):
    w = struct.unpack('<I', p3[off:off+4])[0]
    # Check if any pointer points to pos
    if w == 0x02001000 + pos or w == 0x02000000 + pos:
        print(f"Direct pointer at 0x{off:x} -> 0x{w:x}")

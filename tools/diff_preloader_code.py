p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

# Let's find diff blocks that are NOT 1-2 byte pointer shifts
diff_indices = [i for i in range(len(p3)) if p3[i] != p4[i]]
ranges = []
start = prev = None
for idx in diff_indices:
    if start is None:
        start = prev = idx
    elif idx == prev + 1:
        prev = idx
    else:
        ranges.append((start, prev))
        start = prev = idx
if start is not None:
    ranges.append((start, prev))

print("Diff blocks with length > 4 bytes (excluding sig at end):")
for s, e in ranges:
    length = e - s + 1
    if length > 4 and s < 0x100000:
        sample3 = p3[s:s+min(32, length)]
        sample4 = p4[s:s+min(32, length)]
        print(f"  0x{s:06x} - 0x{e:06x} ({length:4} bytes):")
        print(f"    H3: {sample3.hex()}")
        print(f"    H4: {sample4.hex()}")

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

diff_indices = [i for i in range(len(p3)) if p3[i] != p4[i]]
print(f"Total differing bytes: {len(diff_indices)} / {len(p3)} ({len(diff_indices)/len(p3)*100:.2f}%)")

# Group into continuous ranges
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

print(f"Total diff blocks: {len(ranges)}")
print("\nFirst 30 diff blocks:")
for s, e in ranges[:30]:
    length = e - s + 1
    sample3 = p3[s:s+min(16, length)]
    sample4 = p4[s:s+min(16, length)]
    print(f"  0x{s:06x} - 0x{e:06x} ({length:6} bytes) | H3: {sample3.hex():<32} | H4: {sample4.hex():<32}")

if len(ranges) > 30:
    print(f"\nLast 10 diff blocks:")
    for s, e in ranges[-10:]:
        length = e - s + 1
        sample3 = p3[s:s+min(16, length)]
        sample4 = p4[s:s+min(16, length)]
        print(f"  0x{s:06x} - 0x{e:06x} ({length:6} bytes) | H3: {sample3.hex():<32} | H4: {sample4.hex():<32}")

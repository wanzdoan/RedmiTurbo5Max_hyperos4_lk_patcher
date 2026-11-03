p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

print("Strings around 0xc2409 in H3:")
start = max(0, 0xc2409 - 300)
end = min(len(p3), 0xc2409 + 300)
print(p3[start:end])

# Check if bytes around 0xc2409 differ between H3 and H4:
diffs = [i for i in range(start, end) if p3[i] != p4[i]]
print("Diffs around 0xc2409:", len(diffs))

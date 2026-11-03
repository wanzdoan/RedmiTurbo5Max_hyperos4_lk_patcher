import re

p3 = open('preloader_hyperos3/preloader_dash.bin', 'rb').read()
p4 = open('preloader_hyperos4/preloader_dash.bin', 'rb').read()

def extract_strings(b, min_len=6):
    res = {}
    for m in re.finditer(rb'[A-Za-z0-9_./@:\\ -]{%d,}' % min_len, b):
        res[m.start()] = m.group(0).decode('latin1', 'ignore')
    return res

s3 = extract_strings(p3)
s4 = extract_strings(p4)

set3 = set(s3.values())
set4 = set(s4.values())

print(f"Total strings H3: {len(set3)}, H4: {len(set4)}")
print(f"Strings only in H3: {len(set3 - set4)}")
print(f"Strings only in H4: {len(set4 - set3)}")

print("\n--- Strings only in H3 (sample) ---")
for s in sorted(list(set3 - set4))[:40]:
    print(" ", s)

print("\n--- Strings only in H4 (sample) ---")
for s in sorted(list(set4 - set3))[:40]:
    print(" ", s)

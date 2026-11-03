import re

c_code = open('ghidra_export/h4_orig/all_decompiled.c', 'r', encoding='utf-8').read()
words = ['destroy', 'corrupt', 'system', 'press', 'unlock', 'device', 'error', 'failed', 'redmi', 'dm_verity', 'halt']
for w in words:
    pattern = re.compile(rf'\bs_[a-zA-Z0-9_]*{w}[a-zA-Z0-9_]*\b', re.IGNORECASE)
    matches = set(pattern.findall(c_code))
    print(f'=== Word: {w} ({len(matches)} matches) ===')
    for m in sorted(matches)[:10]:
        print(' ', m)

from pathlib import Path
import struct,difflib,json
A=Path('lk_hyperos3_original/lk.img').read_bytes();B=Path('lk_hyperos3_patched_unlockBL/dash.img').read_bytes()
a=A[0x13a800:0x13abd6];b=B[0x13a800:0x13ac8a]
for name,d in [('cert2_original',a),('cert2_patched',b)]:
 Path('fresh_analysis/h3_diff/'+name+'.bin').write_bytes(d)
 print(name)
 for o in range(0,len(d),16):print(f'{o:04x} '+d[o:o+16].hex(' ')+' '+''.join(chr(x) if 32<=x<127 else '.' for x in d[o:o+16]))

from pathlib import Path
import struct,json,hashlib
from capstone import *
p=Path('lk_hyperos4_original/lk.img');raw=p.read_bytes();o=0x244550;n=struct.unpack_from('<I',raw,o+4)[0];d=raw[o+512:o+512+n]
Path('fresh_analysis/h3_diff/aee_analysis/aee_h4.bin').write_bytes(d)
print('size',hex(n),'head',d[:128].hex())
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
for i in cs.disasm(d[:0x300],0): print(hex(i.address),i.mnemonic,i.op_str)
for q in range(0x2d0,0x360,8):print('QWORD',hex(q),hex(struct.unpack_from('<Q',d,q)[0]))
Path('fresh_analysis/h3_diff/aee_analysis/manifest.json').write_text(json.dumps(dict(parent=str(p),parent_sha256=hashlib.sha256(raw).hexdigest(),member_header_offset=o,payload_offset=o+512,payload_size=n,sha256=hashlib.sha256(d).hexdigest()),indent=2))

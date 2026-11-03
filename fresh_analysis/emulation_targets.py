from pathlib import Path
from capstone import *
d=Path('lk_hyperos4_original/lk.img').read_bytes();c=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
for off,n in [(0x68e94,0xb0),(0x88c10,0x50),(0x89220,0x40)]:
 print('\nFUNCTION',hex(off))
 print('\n'.join(f'{x.address:x} {x.mnemonic} {x.op_str}' for x in c.disasm(d[off:off+n],off)))

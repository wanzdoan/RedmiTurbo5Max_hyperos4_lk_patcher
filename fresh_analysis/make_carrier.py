from pathlib import Path
import struct,json
root=Path(__file__).resolve().parent.parent
out=root/'fresh_analysis/derived';out.mkdir(exist_ok=True)
p=root/'lk_hyperos4_original/lk.img';d=p.read_bytes();size=struct.unpack_from('<I',d,4)[0];payload=d[0x200:0x200+size]
base=0xffff000090700000
# ELF64 little endian AArch64: a derived analysis carrier, not a flash image.
eh=struct.pack('<16sHHIQQQIHHHHHH',b'\x7fELF\x02\x01\x01'+bytes(9),2,183,1,base,64,0,0,64,56,1,0,0,0)
ph=struct.pack('<IIQQQQQQ',1,7,0x1000,base,base,len(payload),0x400000,0x1000)
(out/'h4_lk_analysis.elf').write_bytes(eh+ph+bytes(0x1000-len(eh)-len(ph))+payload)
(out/'h4_lk.raw').write_bytes(payload)
print(hex(base),hex(size))

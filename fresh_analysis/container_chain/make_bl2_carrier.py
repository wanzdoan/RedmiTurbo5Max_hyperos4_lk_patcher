from pathlib import Path
import struct
out=Path('fresh_analysis/container_chain');payload=(out/'h4_original_03_bl2_ext.bin').read_bytes();base=0xffff0000b8000000
eh=struct.pack('<16sHHIQQQIHHHHHH',b'\x7fELF\x02\x01\x01'+bytes(9),2,183,1,base,64,0,0,64,56,1,0,0,0);ph=struct.pack('<IIQQQQQQ',1,7,0x1000,base,base,len(payload),0x400000,0x1000)
(out/'h4_bl2_ext.elf').write_bytes(eh+ph+bytes(0x1000-len(eh)-len(ph))+payload)

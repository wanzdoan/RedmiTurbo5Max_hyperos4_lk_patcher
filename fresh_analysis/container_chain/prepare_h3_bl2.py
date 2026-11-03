from pathlib import Path
p=Path('fresh_analysis/container_chain');raw=(p/'h3_original_03_bl2_ext.bin').read_bytes();carrier=bytearray((p/'h4_bl2_ext.elf').read_bytes()[:4096]);import struct;struct.pack_into('<Q',carrier,64+32,len(raw));(p/'h3_bl2_ext.elf').write_bytes(carrier+raw)
s=Path('fresh_analysis/preloader/PreloaderFocus.java').read_text().replace('class PreloaderFocus','class Bl2Focus').replace('long delta=0x1ffff00L;','long delta=0xffff0000b8000000L;').replace('long[] seeds={0x7ec00,0x7ed00,0x7f300,0xa5200,0xa55d0,0x8c934,0x8cc18};','long[] seeds={0x6e584,0x6cf8c,0x6c4c4,0x9174,0x3dd94};')
s=s.replace('for(long off=0x7e400;off<0x7f800;off+=4)','for(long off=0;off<0;off+=4)').replace('for(long off=0xa4800;off<0xa5800;off+=4)','for(long off=0;off<0;off+=4)').replace('seen.size()<220','seen.size()<130').replace('delta+0x100 && target.getOffset()<delta+0xaf400','delta && target.getOffset()<delta+0x85000')
(p/'Bl2Focus.java').write_text(s)

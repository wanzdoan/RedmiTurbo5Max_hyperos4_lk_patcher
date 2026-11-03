from pathlib import Path
import struct,json,hashlib
out=Path(__file__).resolve().parent; d=(out/'aee_h4.bin').read_bytes();base=0xffff0000be000000
h=struct.pack('<16sHHIQQQIHHHHHH',b'\x7fELF\x02\x01\x01'+bytes(9),2,183,1,base,64,0,0,64,56,1,0,0,0)
p=struct.pack('<IIQQQQQQ',1,7,0x1000,base,base,len(d),0x400000,0x1000)
carrier=h+p+bytes(0x1000-len(h)-len(p))+d;(out/'aee_h4_analysis.elf').write_bytes(carrier)
m=json.loads((out/'manifest.json').read_text());m.update(architecture='AArch64 little-endian',inferred_link_base=hex(base),mapping_evidence={'literal_0x310':'0xffff0000be000000','literal_0x338':'0xffff0000be0001a8 referenced from adr x15 at0x1a8','literal_0x350':'0xffff0000be001000 used for vector base','literal_0x358':'0xffff0000be0f9010 used as stack pointer'},mapping_status='derived from entry literal pool; not observed at runtime',analysis_carrier_sha256=hashlib.sha256(carrier).hexdigest(),analysis_carrier_file_payload_offset=0x1000,analysis_carrier_memory_size=0x400000,memory_size_status='analyst-imposed to allow BSS; not recovered original segment extent');(out/'manifest.json').write_text(json.dumps(m,indent=2))

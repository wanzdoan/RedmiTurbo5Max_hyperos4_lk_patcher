from pathlib import Path
import struct,json
p=Path('fresh_analysis/container_chain');result={}
for ver,table in [('h4',0x106e38)]:
 d=(p/f'{ver}_original_03_bl2_ext.bin').read_bytes();rows=[]
 for i in range(23):
  row=d[table+i*48:table+(i+1)*48];vals=struct.unpack('<6Q',row);names=[]
  for x in vals[:4]:
   o=x-0xffff0000b8000000
   names.append(d[o:d.find(0,o)].decode(errors='replace') if 0<=o<len(d) else hex(x))
  rows.append({'index':i,'names':names,'raw':row.hex(),'policy_sbcoff':row[0x24],'policy_sbcon':row[0x28]})
 print(json.dumps(rows,indent=2));result[ver]=rows
(p/'bl2_policy.json').write_text(json.dumps(result,indent=2))

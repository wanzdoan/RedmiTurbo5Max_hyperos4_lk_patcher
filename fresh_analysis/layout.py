from pathlib import Path
import struct,re,json
for p in ['lk_hyperos3_original/lk.img','lk_hyperos3_patched_unlockBL/dash.img','lk_hyperos4_original/lk.img']:
 d=Path(p).read_bytes();print('\n',p)
 for sig in [bytes.fromhex('88168858'), b'\x7fELF',bytes.fromhex('04224d18')]:
  print('sig',sig.hex(),[hex(m.start()) for m in re.finditer(re.escape(sig),d)])
 for off in [0,0x200]:print(hex(off),d[off:off+128].hex())
 ss=json.loads(Path('fresh_analysis',Path(p).parent.name+'_strings.json').read_text())
 print('\n'.join(hex(s['offset'])+' '+s['text'] for s in ss if re.search('unlock|verified|rollback|build|Linux|clang|lk version|SBC|DAA|secure boot',s['text'],re.I)))

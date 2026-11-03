"""Execute actual ARM64 parser with explicit mocked public-key/crypto primitives.
This is a parser-selection experiment, NOT full secure-boot acceptance testing.
"""
from pathlib import Path
import struct,json,hashlib
from unicorn import *
from unicorn.arm64_const import *
from cryptography import x509

ROOT=Path(__file__).resolve().parents[2];OUT=Path(__file__).resolve().parent
inventory=json.loads((ROOT/'fresh_analysis/container_chain/container_inventory.json').read_text())
DELTA=0x1ffff00
INPUT=0x3000000;KEY=0x3010000;RESULT=0x3020000;STACK=0x3100000;STOP=0x3200000
XREGS=[UC_ARM64_REG_X0,UC_ARM64_REG_X1,UC_ARM64_REG_X2,UC_ARM64_REG_X3,UC_ARM64_REG_X4]
def run(version,carrier):
 fw=(ROOT/f'preloader_hyperos{version}/preloader_dash.bin').read_bytes()
 img=inventory[carrier];rec=img['records'][2];raw=(ROOT/img['file']).read_bytes()
 buf=raw[rec['header_offset']:rec['end']]
 der=(ROOT/'fresh_analysis/container_chain'/f'{carrier}_02_cert2.bin').read_bytes()
 pos=der.find(b'\x30\x82\x03\xd2')
 cert=der[pos:];signed=cert[4:8+int.from_bytes(cert[6:8],'big')]
 expected_hash=hashlib.sha256(signed).digest()
 uc=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
 uc.mem_map(0x1fff000,0x180000);uc.mem_write(DELTA,fw)
 uc.mem_map(INPUT,0x30000);uc.mem_write(INPUT,buf)
 uc.mem_map(STACK,0x10000);uc.mem_map(STOP,0x1000)
 # Data pointers imported from actual firmware point into initialized image/BSS.
 # Initialize guard through its real global indirection.
 guard_ptr=struct.unpack('<Q',uc.mem_read(0x2103280,8))[0]
 uc.mem_write(guard_ptr,struct.pack('<Q',0x12345678))
 size_ptr=struct.unpack('<Q',uc.mem_read(0x2103838,8))[0]
 uc.mem_write(size_ptr,struct.pack('<I',0x100))
 for table in (0x2103818,0x2103838):
  ptr=struct.unpack('<Q',uc.mem_read(table,8))[0];uc.mem_write(ptr,struct.pack('<I',0x100))
 # Key comparison is explicitly mocked; parser itself executes unchanged.
 trace=[];hashes=[];count=0;last=[]
 def ret(value=0):
  uc.reg_write(UC_ARM64_REG_X0,value);uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR))
 def hook(uc,address,size,data):
  nonlocal count
  count+=1
  last.append(hex(address-DELTA));last[:]=last[-20:]
  if address==STOP:uc.emu_stop();return
  off=address-DELTA
  word=int.from_bytes(uc.mem_read(address,4),'little')
  if word in (0xd503233f,0xd50323bf) and off not in (0xa10ec,0xa10ac,0x8aea0,0x8ad88,0xa5c68,0xa6760,0x9c68c,0x9c6c0,0x9c700,0xa7388,0x91190):
   uc.reg_write(UC_ARM64_REG_PC,address+4);return
  if off in (0xa10ec,0xa10ac,0x8aea0,0x8ad88):ret(0);return
  if off in (0xa5c68,0xa6760):
   trace.append({'mock':'key extraction/equality','offset':hex(off)});ret(0);return
  if off==0x9c68c:
   p,n,out,mode=[uc.reg_read(r) for r in XREGS[:4]]
   content=bytes(uc.mem_read(p,n));h=hashlib.sha256(content).digest()
   hashes.append({'input_offset':hex(p-INPUT),'length':n,'sha256':h.hex(),'equals_signed_tbs':h==expected_hash})
   uc.mem_write(out,h);ret(0);return
  if off==0x9c6c0:ret(0);return
  if off==0x9c700:
   p=uc.reg_read(UC_ARM64_REG_X2);valid=bool(hashes and hashes[-1]['equals_signed_tbs'])
   uc.mem_write(p,struct.pack('<I',int(valid)));trace.append({'mock':'RSA result gated on exact known signed TBS hash','valid':valid});ret(0);return
  if off==0xa7388:ret(0);return
  if off==0x91190:raise RuntimeError('stack protector failure')
 uc.hook_add(UC_HOOK_CODE,hook)
 uc.reg_write(UC_ARM64_REG_SP,STACK+0xf000);uc.reg_write(UC_ARM64_REG_LR,STOP)
 for reg,value in zip(XREGS,[INPUT,len(buf),KEY,RESULT,0]):uc.reg_write(reg,value)
 status='unknown';error=None
 try:
  uc.emu_start(DELTA+0xa6508,STOP,count=2000000)
  status='returned' if uc.reg_read(UC_ARM64_REG_PC)==STOP else 'instruction_limit'
 except Exception as ex:error=str(ex)
 result=bytes(uc.mem_read(RESULT,0x154))
 return {'firmware_version':version,'carrier':carrier,'status':status,'error':error,'last_offsets':last,
  'instructions':count,'return_status':hex(uc.reg_read(UC_ARM64_REG_W0)),
  'hash_primitive_calls':hashes,'mocks':trace,
  'extracted_header_hash':result[0x3c:0x3c+struct.unpack_from('<I',result,0x6c)[0]].hex(),
  'extracted_payload_hash':result[8:8+struct.unpack_from('<I',result,0x38)[0]].hex()}

if __name__=='__main__':
 results=[run(v,c) for v in (3,4) for c in ('h3_original','h3_patched','h4_original')]
 (OUT/'parser_emulation.json').write_text(json.dumps(results,indent=2))
 print(json.dumps(results,indent=2))






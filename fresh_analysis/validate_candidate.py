"""Execute actual AArch64 getter, wrappers and AVB predicate under Unicorn.
Logging and boot-partition selection are stubbed; this is NOT a full SoC boot test.
"""
from pathlib import Path
import struct,json,random
from unicorn import Uc,UC_ARCH_ARM64,UC_MODE_ARM,UC_HOOK_CODE
from unicorn.arm64_const import *
from build_candidate import build,prefix,members,ROOT,sha
original=(ROOT/'lk_hyperos4_original/lk.img').read_bytes();candidate,meta=build(original)
B=0x90700000;STACK=0x2000000;DATA=0x3000000;STOP=0x4000000
pc=lambda off:B+off-512
rng=random.Random(9500)
results=[]
def run(entry,initial,kind,boot_type=0,mock=None):
 u=Uc(UC_ARCH_ARM64,UC_MODE_ARM);u.mem_map(B,0x400000);u.mem_write(B,candidate[512:512+0x139b48]);u.mem_map(STACK,0x10000);u.mem_map(DATA,0x1000);u.mem_map(STOP,0x1000)
 pattern=rng.randbytes(0x1000);u.mem_write(DATA,pattern);ptr=DATA+0x80;u.mem_write(ptr,struct.pack('<I',initial))
 regs=[UC_ARM64_REG_X19,UC_ARM64_REG_X20,UC_ARM64_REG_X21,UC_ARM64_REG_X22,UC_ARM64_REG_X23,UC_ARM64_REG_X24,UC_ARM64_REG_X25,UC_ARM64_REG_X26,UC_ARM64_REG_X27,UC_ARM64_REG_X28,UC_ARM64_REG_X29]
 before={r:rng.getrandbits(64) for r in regs}
 for r,v in before.items():u.reg_write(r,v)
 u.reg_write(UC_ARM64_REG_SP,STACK+0xf000);u.reg_write(UC_ARM64_REG_LR,STOP);u.reg_write(UC_ARM64_REG_X0,boot_type if kind=='avb' else ptr)
 stubs=[]
 def hook(uc,address,size,unused):
  if address in (pc(0x7e31c),pc(0x8891c)):
   stubs.append(hex(address));uc.reg_write(UC_ARM64_REG_X0,0);uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR))
  elif address==pc(0x89cec) and mock is not None:
   dest=uc.reg_read(UC_ARM64_REG_X0);uc.mem_write(dest,struct.pack('<I',mock));uc.reg_write(UC_ARM64_REG_X0,0);uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR))
 u.hook_add(UC_HOOK_CODE,hook);u.emu_start(pc(entry),STOP,count=1000)
 assert u.reg_read(UC_ARM64_REG_PC)==STOP,'did not return'
 assert u.reg_read(UC_ARM64_REG_SP)==STACK+0xf000,'stack imbalance'
 assert all(u.reg_read(r)==v for r,v in before.items()),'callee-saved clobber'
 got=u.reg_read(UC_ARM64_REG_X0);state=struct.unpack('<I',u.mem_read(ptr,4))[0]
 if kind=='avb':assert got==(0 if mock in (None,3) else 1)
 else:
  assert got==0 and state==3
  memory=bytes(u.mem_read(DATA,0x1000));assert memory[:0x80]==pattern[:0x80] and memory[0x84:]==pattern[0x84:],'adjacent write'
 return dict(entry=hex(entry),kind=kind,initial=initial,boot_type=boot_type,mocked_state=mock,return_x0=got,pass_result=True)
for initial in [0,1,2,3,4,0xffffffff]+[rng.getrandbits(32) for _ in range(94)]:
 for entry in (0x89cec,0x89220,0x88c10):results.append(run(entry,initial,'getter_or_wrapper'))
for boot in (0,1,2,0xffffffff):
 for mock in (None,0,1,2,3,4,0xffffffff):results.append(run(0x68e94,0,'avb',boot,mock))
# The serialization recipe reproduces the observed H3 unsigned prefix byte for byte.
h3=(ROOT/'lk_hyperos3_patched_unlockBL/dash.img').read_bytes();ms=members(h3);cert=ms[2]
assert prefix(h3[:512],h3[512:512+ms[0]['size']])==h3[cert['offset']+512:cert['offset']+692]
# Reproducibility and expected-input identity failure.
assert build(original)[0]==candidate
bad=bytearray(original);bad[0x89cec]^=1
try:build(bytes(bad))
except AssertionError:pass
else:raise AssertionError('identity check failed open')
report=dict(status='PASS',cpu_cases=len(results),actual_code='H4 16-byte patch plus original custom/state wrappers and AVB predicate',stubs=['logging file0x7e31c','boot partition selection file0x8891c','getter only in explicit mocked-state controls'],prefix_matches_observed_h3=True,deterministic_build=True,wrong_input_rejected=True,limitations=['No BootROM, preloader whole boot, TEE, RPMB, UFS, rollback or physical phone execution','CPU mapping relocated to low address; actual instructions preserved; ABI subset and bounded paths only','Cryptographic signature acceptance of prefixed cert remains a separate parser investigation'],cases=results)
(ROOT/'fresh_analysis/validation.json').write_text(json.dumps(report,indent=2));print(json.dumps({k:v for k,v in report.items() if k!='cases'},indent=2))

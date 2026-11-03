"""Offline policy review. Hardware queries are explicit injected test inputs."""
from pathlib import Path
import json,hashlib,struct
from unicorn import *
from unicorn.arm64_const import *
ROOT=Path(__file__).resolve().parents[3];OUT=Path(__file__).resolve().parent
DELTA=0x2000f00;STACK=0x3100000;STOP=0x3200000
def cstr(b,p):
 o=p-DELTA
 return b[o:b.find(b'\0',o)].decode() if p else None
def policy_run(b,sbc,state,entry,sbc_error=0,state_error=0):
 uc=Uc(UC_ARCH_ARM64,UC_MODE_ARM);uc.mem_map(0x2000000,0x180000);uc.mem_write(DELTA,b)
 uc.mem_map(STACK,0x10000);uc.mem_map(STOP,0x1000)
 def finish(value):uc.reg_write(UC_ARM64_REG_W0,value);uc.reg_write(UC_ARM64_REG_PC,uc.reg_read(UC_ARM64_REG_LR))
 def hook(uc,addr,size,data):
  off=addr-DELTA
  if off in (0xa4b04,0x8c9f4):
   p=uc.reg_read(UC_ARM64_REG_X0);uc.mem_write(p,struct.pack('<I',sbc if off==0xa4b04 else state));finish(sbc_error if off==0xa4b04 else state_error);return
  if off==0x8aea0:finish(0);return
  if off==0x91190:raise RuntimeError('stack guard failure')
  w=int.from_bytes(uc.mem_read(addr,4),'little')
  if w in (0xd503233f,0xd50323bf):uc.reg_write(UC_ARM64_REG_PC,addr+4)
 uc.hook_add(UC_HOOK_CODE,hook);uc.reg_write(UC_ARM64_REG_SP,STACK+0xf000);uc.reg_write(UC_ARM64_REG_LR,STOP);uc.reg_write(UC_ARM64_REG_W0,2)
 uc.emu_start(DELTA+entry,STOP,count=100000)
 assert uc.reg_read(UC_ARM64_REG_PC)==STOP
 return uc.reg_read(UC_ARM64_REG_W0)
evidence={'evidence_id':'PL-REVIEW-POLICY-001','va_mapping':'linked VA = file offset + 0x02000f00; entry file0x100 ->VA0x02001000. Validated by all policy name pointers and stack-guard indirection.',
 'scope':'Read-only supplied files; actual ARM64 policy accessor with SBC and device-state queries injected; no device execution.'}
images=[]
for version in (3,4):
 p=ROOT/f'preloader_hyperos{version}/preloader_dash.bin';b=p.read_bytes();rows=[]
 for i in range(18):
  o=0xed110+i*56;ptrs=struct.unpack_from('<5Q',b,o)
  rows.append({'row':i,'file_offset':hex(o),'names':[cstr(b,p) for p in ptrs[1:]],'policy_bytes':list(b[o+0x28:o+0x2c])})
 cases=[]
 for sbc in (0,1):
  for state in (3,4):
   policy=policy_run(b,sbc,state,0x8cad8);required=policy_run(b,sbc,state,0x8cc18)
   assert required==(policy>>1)&1
   cases.append({'sbc_query_value':sbc,'device_state_query_value':state,'lk_partition_policy':policy,'authentication_required':required})
 cases.append({'sbc_query_error':1,'device_state_query_value':4,'authentication_required':policy_run(b,0,4,0x8cc18,sbc_error=1)})
 cases.append({'sbc_query_value':1,'device_state_query_error':1,'authentication_required':policy_run(b,1,0,0x8cc18,state_error=1)})
 images.append({'version':version,'path':str(p),'sha256':hashlib.sha256(b).hexdigest(),'rows':rows,'policy_cases':cases})
evidence['images']=images;evidence['lk_row_equal']=images[0]['rows'][2]==images[1]['rows'][2]
evidence['limitations']=['Device state values are reported as raw enum3/4; do not assume fastboot unlocked maps directly without device evidence.','SBC/device-state query results on user hardware are unknown.','This experiment mocks only hardware-state query outputs; it does not prove they have these values.']
(OUT/'policy_evidence.json').write_text(json.dumps(evidence,indent=2));print(json.dumps({'lk_rows':[x['rows'][2] for x in images],'cases':[x['policy_cases'] for x in images]},indent=2))

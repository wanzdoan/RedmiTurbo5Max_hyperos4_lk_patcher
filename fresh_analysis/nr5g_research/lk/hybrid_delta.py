from pathlib import Path
from capstone import *
import json,re,hashlib
O=Path('fresh_analysis/nr5g_research/lk');a=Path('fresh_analysis/container_chain/h3_original_03_bl2_ext.bin').read_bytes();b=Path('fresh_analysis/container_chain/h4_original_03_bl2_ext.bin').read_bytes();cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
rows=[]
for name,aa,bb,size in [('LTE_pubkey_hash_handoff',0x67634,0x67ac0,0x228),('maybe_MD1_pubkey_hash',0x6785c,0x67ce8,0x50)]:
 ia=list(cs.disasm(a[aa:aa+size],aa));ib=list(cs.disasm(b[bb:bb+size],bb));changes=[]
 def norm(i,start):
  s=i.op_str;m=i.mnemonic
  if m in ['adrp','adr']:s=s.split(',')[0]+', #ADDR'
  elif m=='bl':s='#CALL'
  elif m=='b' or m.startswith('b.') or m in ['cbz','cbnz','tbz','tbnz']:
   s=re.sub(r'#0x([0-9a-f]+)$',lambda z:'#'+hex(int(z[1],16)-start),s)
  elif m in ['add','ldr','str','ldrb','strb']:s=re.sub(r'#0x[0-9a-f]+','#IMM',s)
  return m+' '+s
 na=[norm(i,aa) for i in ia];nb=[norm(i,bb) for i in ib]
 for x,y in zip(ia,ib):
  if x.bytes!=y.bytes:changes.append(dict(h3=hex(x.address),h4=hex(y.address),old=x.mnemonic+' '+x.op_str,new=y.mnemonic+' '+y.op_str))
 rows.append(dict(function=name,h3_payload_offset=hex(aa),h4_payload_offset=hex(bb),size=size,normalized_streams_equal=na==nb,normalization='PCrelative/call addresses and ADD/load/store immediates masked; heuristic not complete equivalence proof',changed_instructions=changes))
# Hybrid exact member payload identity and main16bytes scope
hy=Path('fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img').read_bytes();mainorig=Path('fresh_analysis/container_chain/h4_original_00_lk.bin').read_bytes();mainhy=hy[512:512+len(mainorig)];diff=[i for i,(x,y) in enumerate(zip(mainorig,mainhy)) if x!=y];manifest=json.loads((O/'hybrid_members.json').read_text());comp={}
for member,src in [('bl2_ext','h3_original_03_bl2_ext.bin'),('aee','h4_original_06_aee.bin'),('lk_main_dtb','h4_original_09_lk_main_dtb.bin'),('lk_dtbo','h4_original_12_lk_dtbo.bin')]:
 m=next(m for m in manifest['members'] if m['name']==member);comp[member]=dict(source=src,sha256_matches=m['sha256']==hashlib.sha256(Path('fresh_analysis/container_chain',src).read_bytes()).hexdigest())
result=dict(lte_security_routines=rows,hybrid_identity=dict(artifact_sha256=manifest['sha256'],main_changed_bytes=len(diff),main_diff_first=hex(min(diff)),main_diff_last=hex(max(diff)),all_main_diffs_within_getter_16bytes=all(0x89aec<=x<0x89afc for x in diff),component_identity=comp));(O/'hybrid_radio_delta.json').write_text(json.dumps(result,indent=2));print(json.dumps({k:v for k,v in result.items() if k!='lte_security_routines'},indent=2));print([(x['function'],x['normalized_streams_equal'],len(x['changed_instructions'])) for x in rows])

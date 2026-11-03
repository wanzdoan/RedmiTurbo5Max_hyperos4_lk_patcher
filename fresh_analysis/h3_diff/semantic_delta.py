from pathlib import Path
from capstone import *
from capstone.arm64 import *
import re,json,hashlib,difflib,collections
cs=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN);cs.detail=True;cs.skipdata=True
sec=re.compile(r'lock|unlock|avb|rollback|seccfg|SBC|secure|security|SEC_|SOCID|socid|anti.?roll|efuse|rpm[bB]',re.I)
def analyze(path):
 d=Path(path).read_bytes()[512:];ins=list(cs.disasm(d[:0xcf000-512],0));starts=[i for i,x in enumerate(ins) if x.mnemonic=='paciasp'];func=[]
 for a,b in zip(starts,starts[1:]+[len(ins)]):
  code=ins[a:b];start=code[0].address;end=code[-1].address+4;reg={};tok=[];strings=[]
  for x in code:
   m=x.mnemonic;s=x.op_str;ops=x.operands if m!='.byte' else []
   if m in ('adr','adrp'):
    reg[ops[0].reg]=ops[1].imm
    v=ops[1].imm
    if m=='adr' and 0<=v<len(d):
     ss=d[v:v+240].split(b'\0')[0]
     if len(ss)>4 and all(9<=z<=126 for z in ss):strings.append(ss.decode(errors='replace'))
    s=x.reg_name(ops[0].reg)+', #ADDRESS'
   elif m=='add' and len(ops)==3 and ops[2].type==ARM64_OP_IMM and ops[1].reg in reg:
    v=reg[ops[1].reg]+ops[2].imm;reg[ops[0].reg]=v
    if 0<=v<len(d):
     ss=d[v:v+240].split(b'\0')[0]
     if len(ss)>4 and all(9<=z<=126 for z in ss):strings.append(ss.decode(errors='replace'))
    s=x.reg_name(ops[0].reg)+','+x.reg_name(ops[1].reg)+',#ADDRESS'
   elif m=='bl':s='#CALL'
   elif m=='b' or m.startswith('b.') or m in ['cbz','cbnz','tbz','tbnz']:
    if ops and ops[-1].type==ARM64_OP_IMM:
     t=ops[-1].imm;s=re.sub(r'#[^, ]+$','#'+(str(t-start) if start<=t<end else 'EXTERNAL'),s)
   tok.append(m+' '+s)
  func.append(dict(start=hex(start+512),size=end-start,tokens=tok,strings=list(dict.fromkeys(strings)),security=any(sec.search(s) for s in strings)))
 return func
A=analyze('lk_hyperos3_original/lk.img');B=analyze('lk_hyperos4_original/lk.img')
sig=lambda f:hashlib.sha256('\n'.join(f['tokens']).encode()).hexdigest()
asig=list(map(sig,A));bsig=list(map(sig,B));sm=difflib.SequenceMatcher(None,asig,bsig,autojunk=False);rows=[];count=collections.Counter()
for tag,i,j,k,l in sm.get_opcodes():
 count[tag]+=max(j-i,l-k)
 if tag=='equal':continue
 for ai in range(i,j):
  cand=[]
  for bi in range(k,l):
   if min(len(A[ai]['tokens']),len(B[bi]['tokens']))/max(len(A[ai]['tokens']),len(B[bi]['tokens']))<.4:continue
   ratio=difflib.SequenceMatcher(None,A[ai]['tokens'],B[bi]['tokens'],autojunk=False).ratio()
   cand.append((ratio,bi))
  if not cand:continue
  ratio,bi=max(cand)
  if not (A[ai]['security'] or B[bi]['security']):continue
  diff=list(difflib.unified_diff(A[ai]['tokens'],B[bi]['tokens'],n=3))
  rows.append(dict(h3=A[ai]['start'],h4_candidate=B[bi]['start'],ratio=ratio,h3_size=A[ai]['size'],h4_size=B[bi]['size'],h3_strings=A[ai]['strings'],h4_strings=B[bi]['strings'],normalized_diff=diff))
 for bi in range(k,l):
  if B[bi]['security'] and not any(r['h4_candidate']==B[bi]['start'] for r in rows):rows.append(dict(h4_candidate=B[bi]['start'],new_or_unmatched=True,h4_strings=B[bi]['strings'],h4_size=B[bi]['size']))
summary=dict(boundary='PACIASP-delimited chunks within whole-file0x200..0xcf000; heuristic not original function boundaries',h3_chunks=len(A),h4_chunks=len(B),alignment_counts=dict(count),security_changed_candidates=rows)
Path('fresh_analysis/h3_diff/h3_h4_semantic_delta.json').write_text(json.dumps(summary,indent=2));print('chunks',len(A),len(B),'counts',count,'securityrows',len(rows));print('\n'.join(str({k:v for k,v in r.items() if k not in ['normalized_diff','h3_strings','h4_strings']})+' '+str([s for s in r.get('h4_strings',[]) if sec.search(s)])[:250] for r in rows))

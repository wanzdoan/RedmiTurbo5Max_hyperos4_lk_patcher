"""Build experimental H4 BL33 with intact signed H3 BL2; never flashes a device."""
from pathlib import Path
import hashlib,json
from build_candidate import ROOT,build,members,sha
H3SHA='e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb'
NAME='lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
def build_hybrid(h4,h3):
 assert sha(h3)==H3SHA,'H3 donor identity mismatch'
 pure,meta=build(h4);hm=members(pure);dm=members(h3)
 assert [m['name'] for m in dm[3:6]]==['bl2_ext','cert1','cert2']
 triple=h3[dm[3]['offset']:dm[5]['end']]
 result=pure[:hm[3]['offset']]+triple+pure[hm[5]['end']:]
 rm=members(result);orig=members(h4)
 # Four changed-member indices: patched main, its prefixed cert, H3BL2payload and donor cert2.
 for i in (1,6,7,8,9,10,11,12,13,14):
  a,b=orig[i],rm[i];assert h4[a['offset']:a['end']]==result[b['offset']:b['end']]
 for i in (3,4,5):
  a,b=dm[i],rm[i];assert h3[a['offset']:a['end']]==result[b['offset']:b['end']]
 assert result[:rm[3]['offset']]==pure[:hm[3]['offset']]
 return result,dict(status='EXPERIMENTAL_HYBRID_NOT_HARDWARE_VERIFIED',description='H4 main LK force-unlock + observed H3 cert prefix; H3 intact signed BL2 triple; all H4 AEE/DTB/DTBO triples intact',input_h4_sha256=sha(h4),input_h3_sha256=sha(h3),output_sha256=sha(result),output_size=len(result),donor_triple_sha256=sha(triple),donor_triple_bytes=len(triple),main_patch_offset='0x89cec',members=rm,limitations=['Not a full-H4 boot chain: deliberately uses signed H3 BL2','Parser selection verified separately; full signature/load boot not emulated','No evidence yet for physical boot, stock H4 preloader handoff or rollback compatibility'])
if __name__=='__main__':
 h4=(ROOT/'lk_hyperos4_original/lk.img').read_bytes();h3=(ROOT/'lk_hyperos3_original/lk.img').read_bytes();result,report=build_hybrid(h4,h3)
 out=ROOT/'fresh_analysis/output'/NAME;out.parent.mkdir(parents=True,exist_ok=True);out.write_bytes(result);out.with_suffix('.json').write_text(json.dumps(report,indent=2));print(json.dumps({k:v for k,v in report.items() if k!='members'},indent=2))

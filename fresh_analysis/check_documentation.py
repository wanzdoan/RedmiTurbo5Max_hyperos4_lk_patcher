from pathlib import Path
import subprocess,re,json,hashlib
root=Path.cwd()
files=[root/'README.md',root/'AGENTS.md']+list((root/'docs').glob('*.md'));bad=[]
for p in files:
 s=p.read_text(encoding='utf-8')
 assert '\ufffd' not in s
 for target in re.findall(r'\[[^\]\n]*\]\(([^)]+)\)',s):
  if '://' in target or target.startswith('#'):continue
  path=target.split('#',1)[0]
  if path and not (p.parent/path).resolve().exists():bad.append([str(p.relative_to(root)),target])
assert not bad,bad
runtime=json.loads((root/'fresh_analysis/output/runtime_confirmation.json').read_text(encoding='utf-8'))
firmware=root/'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img';assert hashlib.sha256(firmware.read_bytes()).hexdigest()==runtime['artifact_sha256_on_host']
changed=subprocess.check_output(['git','diff','--name-only'],text=True).splitlines();assert not [p for p in changed if p.endswith(('.img','.bin'))]
print(json.dumps({'markdown_files_checked':len(files),'broken_local_links':bad,'firmware_unchanged':True,'runtime_reports':len(runtime['reports'])}))

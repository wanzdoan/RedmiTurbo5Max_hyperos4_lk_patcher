from pathlib import Path
import hashlib,json,zipfile
root=Path(__file__).resolve().parent.parent;fresh=root/'fresh_analysis'
image=fresh/'output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
expected='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
assert hashlib.sha256(image.read_bytes()).hexdigest()==expected
runtime=json.loads((fresh/'output/runtime_confirmation.json').read_text(encoding='utf-8'))
assert runtime['artifact_sha256_on_host']==expected
(fresh/'output/SHA256SUMS.txt').write_text(expected+'  '+image.name+'\n',encoding='utf-8')
paths={p for p in fresh.rglob('*') if p.is_file() and p.suffix not in ('.lock','.pyc') and '__pycache__' not in p.parts and not p.name.endswith('.lock~')}
paths.update(p for p in (root/'docs').rglob('*') if p.is_file())
paths.update((root/'README.md',root/'AGENTS.md'))
archive=root/'fresh_analysis_bundle.zip'
with zipfile.ZipFile(archive,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as z:
 for p in sorted(paths):z.write(p,p.relative_to(root).as_posix())
with zipfile.ZipFile(archive) as z:
 assert z.testzip() is None
 assert z.read(image.relative_to(root).as_posix())==image.read_bytes()
 for p in (root/'docs').rglob('*.md'):assert z.read(p.relative_to(root).as_posix())==p.read_bytes()
print(json.dumps(dict(archive=str(archive),bytes=archive.stat().st_size,files=len(paths),firmware_sha256=expected,crc_and_document_identity='PASS')))

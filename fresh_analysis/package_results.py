from pathlib import Path
import re,json,hashlib,zipfile
root=Path.cwd();fresh=root/'fresh_analysis';p=fresh/'README_VI.md';s=p.read_text(encoding='utf-8-sig')
for a,b in [('16byte','16 byte'),('180byte','180 byte'),('90byte','90 byte'),('512byte','512 byte'),('4.083.424 byte','4.083.424 byte'),('15record','15 record'),('10record','10 record'),('16callsite','16 callsite'),('34thay','34 thay'),('328ca','328 ca'),('18ca','18 ca'),('6ca','6 ca'),('30chữ','30 chữ'),('10DER','10 DER'),('3input','3 input'),('220hàm','220 hàm'),('2.328hàm','2.328 hàm'),('1.526hàm','1.526 hàm'),('1.827hàm','1.827 hàm'),('5.681file','5.681 file'),('Ghidra12.1.2','Ghidra 12.1.2'),('modulecapstone','module capstone'),('indexfunctions.tsv','index functions.tsv'),('input3phiên','input 3 phiên'),('DFD/expdb','DFD/expdb')]:s=s.replace(a,b)
s=s.replace('`h3_diff/BL2_POLICY_FINDINGS.md` nếu có','`h3_diff/BL2_LOADER.md`');p.write_text(s,encoding='utf-8')
img=fresh/'output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img';digest=hashlib.sha256(img.read_bytes()).hexdigest();assert digest=='f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'
(fresh/'output/SHA256SUMS.txt').write_text(digest+'  '+img.name+'\n')
(fresh/'output/READ_BEFORE_TEST.txt').write_text('EXPERIMENTAL HYBRID: H4 BL33/AEE/DTBs + intact signed H3 BL2.\nNot full H4. Not hardware boot verified. Stock H4 preloader compatibility unverified.\nSee ../README_VI.md for findings and tests.\nSHA256 '+digest+'\n')
counts={str(d.relative_to(fresh)):len(list(d.glob('*.c'))) for d in [fresh/'h4_pseudocode',fresh/'container_chain/bl2_decompile',fresh/'h3_diff/aee_analysis/pseudocode',fresh/'preloader/decompile_h4']}
(fresh/'export_index.json').write_text(json.dumps(dict(pseudocode_file_counts=counts,meaning='Tool-recognized and focused pseudocode, not original source or complete control-flow coverage'),indent=2))
archive=root/'fresh_analysis_bundle.zip'
files=[f for f in fresh.rglob('*') if f.is_file() and f.suffix not in ('.lock','.pyc') and '__pycache__' not in f.parts and not f.name.endswith('.lock~')]
with zipfile.ZipFile(archive,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6) as z:
 for f in files:z.write(f,f.relative_to(root))
print(json.dumps(dict(image_sha256=digest,pseudocode=counts,archive=str(archive),archive_bytes=archive.stat().st_size,files=len(files)),indent=2))

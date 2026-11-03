from pathlib import Path
import hashlib,json,importlib.metadata as md
rom=Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0')
out=Path('fresh_analysis/stock_h4_preloader_review');out.mkdir(exist_ok=True)
rows=[]
for p in rom.rglob('*'):
 if p.is_file() and ('preloader' in p.name.lower() or p.name in ('lk.img','anti_version.txt','misc.txt') or p.name.startswith('flash_all')):
  d=p.read_bytes();row=dict(path=str(p),size=len(d),sha256=hashlib.sha256(d).hexdigest());rows.append(row)
  if 'preloader' in p.name.lower() or p.name=='lk.img':print(json.dumps(row))
  if p.suffix in ('.bat','.sh') and p.name.startswith('flash_all'):
   lines=d.decode(errors='replace').splitlines();matches=[dict(line=i+1,text=s) for i,s in enumerate(lines) if any(k in s.lower() for k in ('preloader',' lk','anti','lock','slot'))];row['relevant_script_lines']=matches
(out/'rom_inventory.json').write_text(json.dumps(dict(rom=str(rom),files=rows),indent=2),encoding='utf-8')
print(json.dumps({'python_dependencies':{x:md.version(x) for x in ['capstone','unicorn','cryptography','pyelftools','lz4']}}))

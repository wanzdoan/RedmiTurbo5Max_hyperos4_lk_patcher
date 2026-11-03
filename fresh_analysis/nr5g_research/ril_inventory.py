from pathlib import Path
from elftools.elf.elffile import ELFFile
import re,json,hashlib
root=Path(__file__).resolve().parent;src=root/'device_static';out=root/'ril'
pat=re.compile(r'(?i)nr5g|nr[_\.-]|5g|nrmode|endc|nsa|(?:country|build_region|ptcountry|sbp|mcf|rat_config|ratconfig)|NR(?:Mode|Switch|Sa|Nsa|Band)|support.*nr|nr.*support')
result=[]
for name in ('libmtk-ril.so','libmtkrilutils.so','libmtkmipc-ril.so','librilfusion.so'):
 p=src/name;d=p.read_bytes();row={'file':name,'size':len(d),'sha256':hashlib.sha256(d).hexdigest(),'strings':[],'symbols':[]}
 with p.open('rb') as f:
  elf=ELFFile(f);row.update(architecture=elf.get_machine_arch(),elfclass=elf.elfclass)
  for sec in elf.iter_sections():
   if sec.header.sh_type in ('SHT_DYNSYM','SHT_SYMTAB'):
    for sym in sec.iter_symbols():
     if pat.search(sym.name):row['symbols'].append({'name':sym.name,'va':hex(sym.entry.st_value),'size':sym.entry.st_size,'type':sym.entry.st_info.type})
  loads=[s for s in elf.iter_segments() if s.header.p_type=='PT_LOAD']
  for m in re.finditer(rb'[ -~]{6,}',d):
   s=m.group().decode()
   if len(s)>600 or not pat.search(s):continue
   va=None
   for seg in loads:
    if seg.header.p_offset<=m.start()<seg.header.p_offset+seg.header.p_filesz:va=hex(seg.header.p_vaddr+m.start()-seg.header.p_offset);break
   row['strings'].append({'offset':hex(m.start()),'va':va,'text':s})
 result.append(row)
(out/'inventory.json').write_text(json.dumps(result,indent=2))
for row in result:
 print(row['file'],'strings',len(row['strings']),'symbols',len(row['symbols']))
 for s in row['strings']:
  if re.search(r'build_region|ptcountry|nr_mode|nr5g|NR_MODE|E5G|ENR|foreign|oversea|452|460|CN.*NR|NR.*CN|nr.*disable|disable.*nr',s['text'],re.I):print(s['va'],s['text'])

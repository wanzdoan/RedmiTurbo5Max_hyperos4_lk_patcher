exec(open('fresh_analysis/nr5g_research/modem/mddb_schema.py',encoding='utf-8-sig').read().split('terms=')[0])
need=[]
for e in tree.iter('NVRAM_LID'):
 if re.search(r'(?i)(SBP_MODEM|NR.*(?:BAND|CAP|MCC|PLMN|CUSTOM)|MRS.*(?:NR|BAND|MCC|PLMN)|MCF_FILE_SECURITY)',e.attrib['name']):
  typ=e.attrib.get('type_name');s=next((s for s in tree.iter('Struct') if s.attrib.get('name')==typ),None)
  rec={'lid':e.attrib,'struct':None if s is None else s.attrib,'fields':[]}
  if s is not None:
   for f in s.findall('Field'):
    ff=dict(f.attrib)
    if f.find('NVRAM_Bit_Data') is not None:
     ff['bits']=[x.attrib for x in f.findall('NVRAM_Bit_Data') if re.search(r'(?i)(NR_CUSTOM.*BAND|ENDC.*(?:COMB|WHITE|DISABLE)|SA.*MCC|NRRC_CAP_BC_WHITE|NR_PARTIAL)',x.attrib.get('name',''))]
    rec['fields'].append(ff)
  need.append(rec)
(out/'nvram_nr_lid_index.json').write_text(json.dumps(need,indent=2));print('\n'.join(x['lid']['id']+' '+x['lid']['name']+' '+x['lid'].get('type_name','')+' '+str(x['struct']) for x in need))


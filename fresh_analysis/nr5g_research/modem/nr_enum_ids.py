exec(open('fresh_analysis/nr5g_research/modem/mddb_schema.py',encoding='utf-8-sig').read().split('terms=')[0])
terms=['SBP_NR_CUSTOM_DISABLE_BAND_BY_PLMN','SBP_CONFIG_ENDC_COMB_BY_SBPID','SBP_NRRC_CAP_BC_WHITELIST_ENDC_NUM','SBP_NRRC_CAP_BC_WHITELIST_SA_NUM','SBP_NAS_ROAMING_DISABLE_ENDC','SBP_NR_CUSTOM_ENABLE_BAND_BY_PLMN','SBP_NWSEL_SKIP_NR_SEARCH_WHEN_SA_DISABLE_BY_MCC']
found=[]
for e in tree.iter('Enumerator'):
 if e.attrib.get('key') in terms:
  p=parents[e];row={'parent':p.attrib,'enum':e.attrib};found.append(row);print(row)
(out/'mddb_nr_enum_ids.json').write_text(json.dumps(found,indent=2))
print('bit count',len(found))
for e in tree.iter('Enum'):
 if any('GID' in e.attrib.get('name','') or 'SBP' in e.attrib.get('name','') for _ in [0]):print('ENUMNAME',e.attrib)

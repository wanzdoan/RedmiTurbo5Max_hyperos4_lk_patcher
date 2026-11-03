exec(open('fresh_analysis/nr5g_research/modem/mddb_schema.py',encoding='utf-8-sig').read().split('terms=')[0])
s=next(s for s in tree.iter('Struct') if s.attrib.get('name')=='nvram_ef_sbp_modem_config_struct');f=next(s for s in s.findall('Field') if s.attrib.get('name')=='modem_sbp_config');rows=[]
for bit in f.findall('NVRAM_Bit_Data'):
 a=bit.attrib;name=a['name'];o=d.find(name.encode()+b'\0',0x2210000,0x2240000)
 if o>=0:rows.append({'name':name,'schema_bit_index':int(a['byte_offset'])*8+int(a['bit_offset']),'string_offset':hex(o)})
rows.sort(key=lambda x:x['schema_bit_index']);bad=[]
for a,b in zip(rows,rows[1:]):
 if int(a['string_offset'],16)>=int(b['string_offset'],16):bad.append([a,b])
print('rows',len(rows),'outoforder',len(bad))
focus=[r for r in rows if any(x in r['name'] for x in ['NR_CUSTOM_DISABLE_BAND_BY_PLMN','NAS_ROAMING_DISABLE_ENDC','CONFIG_ENDC_COMB_BY_SBPID','NR_CUSTOM_ENABLE_BAND_BY_PLMN','SKIP_NR_SEARCH_WHEN_SA_DISABLE'])];print(focus)
(out/'sbp_schema_string_order.json').write_text(json.dumps(dict(total_matched=len(rows),out_of_order_pairs=bad,focus=focus,limits='schema_bit_index is not independently proven as public AT command featureID'),indent=2))

from pathlib import Path
import gzip,json,re,xml.etree.ElementTree as E,collections
base=Path(r'D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0/images');out=Path(__file__).resolve().parent;d=(base/'modem.img').read_bytes();rs=json.loads((out/'modem_mtk_records.json').read_text());r=next(r for r in rs if r['name']=='md1_mddbmetaodb');o=int(r['offset'],16)+512;xml=gzip.decompress(d[o:o+r['size']]);tree=E.fromstring(xml);parents={c:p for p in tree.iter() for c in p}; print(collections.Counter(e.tag for e in tree.iter()))
terms=['SBP_NR_CUSTOM_DISABLE_BAND_BY_PLMN','SBP_CONFIG_ENDC_COMB_BY_SBPID','SBP_NRRC_CAP_BC_WHITELIST_ENDC_NUM','SBP_NRRC_CAP_BC_WHITELIST_SA_NUM','SBP_NAS_ROAMING_DISABLE_ENDC','SBP_NR_CUSTOM_ENABLE_BAND_BY_PLMN','SBP_NWSEL_SKIP_NR_SEARCH_WHEN_SA_DISABLE_BY_MCC']
found=[]
for e in tree.iter():
 if any(term==e.attrib.get('name') for term in terms):
  p=parents.get(e);rec={'tag':e.tag,'attributes':e.attrib,'parent':p.attrib,'parent_tag':p.tag};found.append(rec);print(rec)
(out/'mddb_sbp_identifiers.json').write_text(json.dumps(found,indent=2))
# Preserve small schemas defining NR gates and MCF/SBP control records only.
related=[]
for e in tree.iter():
 name=e.attrib.get('name','')
 if e.tag in ['Struct','Enum','LogicalDataItem'] and re.search(r'(?i)(nras.*(?:band.*(?:enable|disable)|plmn)|nr.*band_support_plmn|mrs_nr_plmn|sbp.*(?:feature|config)|mcf.*(?:select|sbp))',name):
  text=E.tostring(e,encoding='unicode');related.append({'tag':e.tag,'name':name,'attributes':e.attrib,'xml':text[:80000]})
(out/'mddb_nr_schemas.json').write_text(json.dumps(related,indent=2));print('related',[(x['tag'],x['name']) for x in related][:80])
# Find references to the exact structure names in LID entries.
for e in tree.iter():
 if any('nras' in v.lower() or 'band_support_plmn' in v.lower() or 'sbp_modem_config' in v.lower() for v in e.attrib.values()) and e.tag not in ['Field','Struct']:
  print('REF',e.tag,e.attrib)

import json
from pathlib import Path
x=json.loads(Path('fresh_analysis/nr5g_research/modem/inventory.json').read_text())
for name,v in x.items():
 print('\nFILE',name)
 for k in ['plmn_vietnam','country_vietnam','nr_rat']:
  print(k)
  for h in v['searches'][k]['first_hits'][:120]:print(hex(h['offset']),h['context_ascii'])
 print('config first')
 for h in v['searches']['config_names']['first_hits'][:20]:print(hex(h['offset']),h['context_ascii'])

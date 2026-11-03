from pathlib import Path
import re,json
p=Path('fresh_analysis/h3_diff/aee_analysis');d=(p/'aee_h4.bin').read_bytes();ss=[dict(offset=hex(m.start()),va=hex(0xffff0000be000000+m.start()),text=m.group().decode()) for m in re.finditer(rb'[ -~]{6,}',d)];(p/'strings.json').write_text(json.dumps(ss,indent=2));print('\n'.join(str(x) for x in ss if re.search('unlock|lock.state|fastboot|boot.stage|SEC_UNLOCK|rollback|boot_mode',x['text'],re.I)))

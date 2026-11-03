from pathlib import Path
import os,re,json,hashlib
raw=Path(os.environ['LOCALAPPDATA'])/'Temp/redmi-dash-crash-20261103';out=Path('fresh_analysis/nr5g_research/crash_review');d=(raw/'tombstone_31').read_text(errors='replace');lines=d.splitlines();selected=[]
for s in lines[:200]:
 if re.search(r'Build fingerprint|Timestamp|Process uptime|Cmdline|pid:|signal |Abort message|backtrace:|^\s*#\d+',s):selected.append(s)
(out/'tombstone_header.json').write_text(json.dumps({'source':'/data/tombstones/tombstone_31','sha256':hashlib.sha256((raw/'tombstone_31').read_bytes()).hexdigest(),'selected_header_and_backtrace':selected},indent=2));print('\n'.join(selected))

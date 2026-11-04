"""Inventory immutable H3 ENG input and prepare byte-identical native carriers."""
from pathlib import Path
import argparse
import hashlib
import json
import re
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import members

INPUT_SHA = '6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d'


def inspect(data):
    if hashlib.sha256(data).hexdigest() != INPUT_SHA:
        raise ValueError('Unexpected ENG input')
    rows = []
    for m in members(data):
        rows.append(dict(m, header_sha256=hashlib.sha256(data[m['offset']:m['offset'] + 512]).hexdigest(),
                         payload_sha256=hashlib.sha256(data[m['offset'] + 512:m['offset'] + 512 + m['size']]).hexdigest()))
    return rows


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--cache', type=Path, required=True)
    args = ap.parse_args()
    data = (HERE / 'input/lk_eng_original.img').read_bytes()
    rows = inspect(data)
    args.cache.mkdir(parents=True, exist_ok=True)
    carriers = []
    for index, label, base in [(0, 'eng_main', 0xffff000090700000), (3, 'eng_bl2', 0xffff0000b8000000)]:
        m = rows[index]
        raw = data[m['offset'] + 512:m['offset'] + 512 + m['size']]
        eh = struct.pack('<16sHHIQQQIHHHHHH', b'\x7fELF\x02\x01\x01' + bytes(9),
                         2, 183, 1, base, 64, 0, 0, 64, 56, 1, 0, 0, 0)
        ph = struct.pack('<IIQQQQQQ', 1, 7, 0x1000, base, base, len(raw), 0x400000, 0x1000)
        carrier = eh + ph + bytes(0x1000 - len(eh) - len(ph)) + raw
        (args.cache / (label + '.bin')).write_bytes(raw)
        (args.cache / (label + '.elf')).write_bytes(carrier)
        carriers.append({'name': label, 'base': hex(base), 'raw_sha256': hashlib.sha256(raw).hexdigest(),
                         'elf_sha256': hashlib.sha256(carrier).hexdigest(), 'not_flashable': True})
    main = rows[0]
    strings = [{'payload_offset': hex(m.start()), 'text': m.group().decode()}
               for m in re.finditer(rb'[ -~]{6,}', data[512:512 + main['size']])
               if re.search(rb'mi_get_lock_state|Custom get lock|userdebug|eng\.|build.*version|b784', m.group(), re.I)]
    report = {'input_sha256': INPUT_SHA, 'size': len(data), 'members': rows,
              'selected_main_strings': strings, 'derived_analysis_carriers': carriers,
              'limits': ['ENG/HyperOS3 is user-supplied provenance; strings establish userdebug markers, not full ROM identity']}
    (HERE / 'input_inventory.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'input_sha256': INPUT_SHA, 'members': len(rows), 'cache': str(args.cache)}, indent=2))


if __name__ == '__main__':
    main()

"""Decode the shipped kernel's image-type map; never changes firmware/device."""
import argparse
import hashlib
import io
import json
from pathlib import Path
import struct
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from elftools.elf.elffile import ELFFile

HERE = Path(__file__).resolve().parent


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('module', type=Path)
    args = ap.parse_args()
    data = args.module.read_bytes()
    inputs = json.loads((HERE / 'handoff_inputs.json').read_text())
    expected = next(m['sha256'] for m in inputs['modules'] if m['source_member'].endswith('ccci_util_lib.ko'))
    if hashlib.sha256(data).hexdigest() != expected:
        raise ValueError('Unexpected CCCI module')
    elf = ELFFile(io.BytesIO(data))
    syms = {s.name: s for s in elf.get_section_by_name('.symtab').iter_symbols()}

    def body(name):
        sym = syms[name]
        sec = elf.get_section(sym.entry.st_shndx)
        off, size = sym.entry.st_value, sym.entry.st_size
        return sec.data()[off:off + size], off

    table, off = body('md_img_capability_map')
    values = list(struct.unpack('<' + 'I' * (len(table) // 4), table))
    code, code_offset = body('get_capability_bit')
    instructions = [{'offset_in_text': hex(i.address), 'bytes': i.bytes.hex(),
                     'instruction': f'{i.mnemonic} {i.op_str}'}
                    for i in Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN).disasm(code, code_offset)]
    if len(values) != 15 or values[13] != 0x7b or not values[13] & 0x40:
        raise ValueError('Unexpected modem image capability map')
    report = {'evidence_id': 'LK-RADIO-KERNEL-CAPABILITY-001',
              'module_sha256': expected, 'table_symbol': 'md_img_capability_map',
              'map': values, 'modem_image_type': inputs['modem']['image_type'],
              'type13_mask': hex(values[13]), 'n_bit': '0x40', 'type13_has_n': True,
              'interpretation_source': 'ccci_util/00108280.c: N/n -> 0x40; 001081f4.c: image type indexes map',
              'get_capability_bit_instructions': instructions,
              'limits': ['Static descriptor and kernel mapping; not proof of NR registration, active bands or operator permission']}
    (HERE / 'kernel_capability.json').write_text(json.dumps(report, indent=2) + '\n')
    print('PASS: modem image type13 -> kernel mask0x7b, including N/NR bit0x40.')


if __name__ == '__main__':
    main()

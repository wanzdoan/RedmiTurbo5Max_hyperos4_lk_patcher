"""Extract only CCCI kernel readers from the supplied H4 vendor boot ramdisk.

Inputs are immutable. No ADB or device mutation. Derived ELF files go to a
caller-selected cache, while hashes/symbols are retained as reproducible evidence.
"""
import argparse
import gzip
import hashlib
import io
import json
from pathlib import Path
import re
import struct

from elftools.elf.elffile import ELFFile
import lz4.block
import lz4.frame

HERE = Path(__file__).resolve().parent
DEFAULT_ROM = Path(r'D:\dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2\dash_images_OS4.0.3.0.XPLCNXM_17.0\images')


def u32(data, off):
    return struct.unpack_from('<I', data, off)[0]


def align(n, p):
    return (n + p - 1) // p * p


def decompress(data):
    if data.startswith(b'\x1f\x8b'):
        return gzip.decompress(data)
    if data.startswith(bytes.fromhex('04224d18')):
        return lz4.frame.decompress(data)
    if data.startswith(bytes.fromhex('02214c18')):
        out, off = bytearray(), 4
        while off + 4 <= len(data):
            size = u32(data, off)
            off += 4
            if not size:
                break
            if off + size > len(data):
                raise ValueError('LZ4 block outside fragment')
            out += lz4.block.decompress(data[off:off + size], uncompressed_size=8 * 1024 * 1024)
            off += size
        return bytes(out)
    if data.startswith(b'070701'):
        return data
    raise ValueError('Unknown ramdisk compression')


def cpio(data):
    off = 0
    while off < len(data):
        while off < len(data) and data[off] == 0:
            off += 1
        if off == len(data):
            break
        if data[off:off + 6] not in (b'070701', b'070702'):
            raise ValueError(f'Invalid cpio at {off:x}')
        fields = [int(data[off + 6 + i * 8:off + 14 + i * 8], 16) for i in range(13)]
        size, namesize = fields[6], fields[11]
        name = data[off + 110:off + 110 + namesize - 1].decode()
        start = align(off + 110 + namesize, 4)
        if start + size > len(data):
            raise ValueError('Truncated cpio member')
        yield name, data[start:start + size]
        off = align(start + size, 4)


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--rom', type=Path, default=DEFAULT_ROM)
    ap.add_argument('--cache', type=Path, required=True)
    args = ap.parse_args()
    args.cache.mkdir(parents=True, exist_ok=True)
    data = (args.rom / 'vendor_boot.img').read_bytes()
    # Identity is checked against the previous format-aware inventory.
    previous = json.loads((HERE.parent / 'ramdisk/inventory.json').read_text())
    expected = next(r['sha256'] for r in previous['images'] if r['name'] == 'vendor_boot.img')
    digest = hashlib.sha256(data).hexdigest()
    if digest != expected or data[:8] != b'VNDRBOOT' or u32(data, 8) != 4:
        raise ValueError('Unexpected vendor_boot identity or format')
    page, size, header = u32(data, 12), u32(data, 24), u32(data, 2096)
    dtb_size, table_size, count, entry_size = (u32(data, x) for x in (2100, 2112, 2116, 2120))
    base = align(header, page)
    table = base + align(size, page) + align(dtb_size, page)
    if count * entry_size > table_size:
        raise ValueError('Invalid fragment table')
    wanted = {'lib/modules/ccci_md_all.ko', 'lib/modules/ccci_util_lib.ko'}
    found = []
    for i in range(count):
        entry = table + i * entry_size
        length, offset = u32(data, entry), u32(data, entry + 4)
        if offset + length > size:
            raise ValueError('Fragment outside ramdisk')
        for name, content in cpio(decompress(data[base + offset:base + offset + length])):
            if name not in wanted:
                continue
            elf = ELFFile(io.BytesIO(content))
            symbols = []
            for section in elf.iter_sections():
                if section.header.sh_type not in ('SHT_SYMTAB', 'SHT_DYNSYM'):
                    continue
                for sym in section.iter_symbols():
                    if re.search(r'capabil|rat|support|md_type|md_img|md_info|ccci_tag|lk_info|boot_info|feature|world_mode', sym.name, re.I):
                        symbols.append({'name': sym.name, 'value': hex(sym.entry.st_value),
                                        'size': sym.entry.st_size, 'section': sym.entry.st_shndx})
            target = args.cache / Path(name).name
            target.write_bytes(content)
            found.append({'source_member': name, 'fragment_index': i,
                          'sha256': hashlib.sha256(content).hexdigest(), 'size': len(content),
                          'elf_type': elf.header.e_type, 'machine': elf.header.e_machine,
                          'symbols': symbols})
    if {r['source_member'] for r in found} != wanted or len(found) != 2:
        raise ValueError('CCCI module inventory differs')
    modem = (args.rom / 'modem.img').read_bytes()
    if modem[:4] != bytes.fromhex('88168858'):
        raise ValueError('Unexpected modem container')
    end = 512 + u32(modem, 4)
    header_size = u32(modem, end - 4)
    off = end - header_size
    if modem[off:off + 12] != b'CHECK_HEADER':
        raise ValueError('CHECK_HEADER missing')
    report = {'evidence_id': 'LK-RADIO-CAUSAL-INPUTS-001', 'read_only_device': True,
              'vendor_boot_sha256': digest, 'modules': found,
              'modem': {'sha256': hashlib.sha256(modem).hexdigest(),
                        'check_header_offset': hex(off), 'check_header_size': header_size,
                        'header_version': u32(modem, off + 12), 'image_type': u32(modem, off + 20),
                        'platform': modem[off + 24:off + 40].split(b'\0')[0].decode(),
                        'note': 'Image type is a descriptor. No NR-enable semantics inferred.'}}
    (HERE / 'handoff_inputs.json').write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8')
    print(json.dumps({'cache': str(args.cache), 'modules': [(r['source_member'], r['size']) for r in found],
                      'modem': report['modem']}, indent=2))


if __name__ == '__main__':
    main()

"""HWC port for exact stock-H3 and debug layouts, preserving early HW copy."""
from pathlib import Path
import hashlib
import struct
import subprocess
import tempfile
from elftools.elf.elffile import ELFFile
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
CONFIG = {
    'stock': dict(reference='lk_hyperos3_original/lk.img', base=0x13a78, span=136,
                  late=0x13a88, early_end=0x13b00, country=0x13e18, read_error=0x14050,
                  read_error_string=0x101e1c, error_log_close=0x140a4, after_country=0x140b0,
                  continuation=0x140b4, hw=0xb0, output=0x110, frame=0x220,
                  cache=0x13d018, getter=0x34f8, slot=0x1f608, snprintf=0x7f434,
                  lookup=0x7bd60, read=0x7bdf4, close=0x7bce4, append=0x18f34,
                  log=0x7de54, cn=0xd36b1, format=0xfcaaa, error_suffix=0xd74b7),
    'debug': dict(reference='experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img',
                  base=0x13364, span=136, late=0x13378, early_end=0x133ec,
                  country=0x13728, read_error=0x13960, read_error_string=0xfaf36,
                  error_log_close=0x139b4, after_country=0x139c0, continuation=0x139c4,
                  hw=0x120, output=0x180, frame=0x290, cache=0x135018,
                  getter=0x3530, slot=0x1f2b0, snprintf=0x7ffe0,
                  lookup=0x7c990, read=0x7ca24, close=0x7c914,
                  append=0x18ab8, log=0x7ea84, cn=0xf34d4, format=0xf5a3e,
                  error_suffix=0xcf78f),
}


def assemble(kind):
    with tempfile.TemporaryDirectory(prefix='hybrid-hwc-') as directory:
        obj = Path(directory) / 'hwc.o'
        subprocess.run(['clang', '-target', 'aarch64-none-elf', '-c',
                        str(HERE / (kind + '_auto_hwc.S')), '-o', str(obj)], check=True)
        with obj.open('rb') as stream:
            elf = ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text', '.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved assembly relocation')
            code = elf.get_section_by_name('.text').data()
    if len(code) != CONFIG[kind]['span'] or b'GLOBAL\0\0' not in code[-12:]:
        raise ValueError('Unexpected HWC code size/literal')
    return code


def branch(at, target):
    delta = target - at
    if delta % 4 or not -(1 << 27) <= delta < (1 << 27):
        raise ValueError('Branch out of range')
    return struct.pack('<I', 0x14000000 | ((delta // 4) & 0x3ffffff))


def adr(at, target, register):
    delta = target - at
    if not -(1 << 20) <= delta < (1 << 20):
        raise ValueError('ADR out of range')
    return struct.pack('<I', 0x10000000 | ((delta & 3) << 29) | (((delta >> 2) & 0x7ffff) << 5) | register)


def patches(image, kind):
    """Return (main-payload offset, bytes, purpose), without writing an image."""
    cfg = CONFIG[kind]
    original = (ROOT / cfg['reference']).read_bytes()
    code = assemble(kind)
    spans = [(cfg['base'], code,
              'Keep native early hardware copy; defer HWC emission and select countrycode region'),
             (cfg['read_error'], bytes.fromhex('ff7f00a9')
              + adr(cfg['read_error'] + 4, cfg['read_error_string'], 0)
              + branch(cfg['read_error'] + 8, cfg['error_log_close']),
              'Clear partial countrycode read before native error log and close'),
             (cfg['after_country'], branch(cfg['after_country'], cfg['late']),
              'Common countrycode result enters late HWC formatter')]
    for offset, blob, _ in spans:
        if image[512 + offset:512 + offset + len(blob)] != original[512 + offset:512 + offset + len(blob)]:
            raise ValueError('HWC original span differs at ' + hex(offset))
    for offset, expected in [(cfg['country'], bytes.fromhex('ff7f00a9')),
                             (cfg['cn'], b'CN\0'),
                             (cfg['format'], b'androidboot.hwc=%s androidboot.hwlevel=%s androidboot.hwversion=%s\0'),
                             (cfg['error_suffix'], b'mi_hwid: snprintf error.\n\0')]:
        if image[512 + offset:512 + offset + len(expected)] != expected:
            raise ValueError('Expected original HWC literal/control differs at ' + hex(offset))
    return spans


def check_external_branches(image, kind):
    cfg = CONFIG[kind]
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
    decoder.skipdata = True
    spans = ((cfg['base'], cfg['base'] + cfg['span']),
             (cfg['read_error'], cfg['read_error'] + 12))
    for ins in decoder.disasm(image[512 + 0x3000:512 + 0xcf000], 0x3000):
        if ins.mnemonic in ('b', 'bl', 'cbz', 'cbnz', 'tbz', 'tbnz') or ins.mnemonic.startswith('b.'):
            last = ins.op_str.rsplit(',', 1)[-1].strip()
            if last.startswith('#0x'):
                target = int(last[1:], 16)
                for lo, hi in spans:
                    if lo < target < hi and not lo <= ins.address < hi:
                        raise ValueError('External branch into repurposed interior: '
                                         + hex(ins.address) + ' -> ' + hex(target))
    return True


if __name__ == '__main__':
    for kind, cfg in CONFIG.items():
        code = assemble(kind)
        (HERE / (kind + '_auto_hwc.bin')).write_bytes(code)
        source = (ROOT / cfg['reference']).read_bytes()
        patches(source, kind)
        check_external_branches(source, kind)
        print(kind, len(code), hashlib.sha256(code).hexdigest())

"""Assemble a position-fixed callback without changing any old input/artifact."""
from pathlib import Path
import subprocess
import tempfile
from elftools.elf.elffile import ELFFile

HERE = Path(__file__).resolve().parent


def assemble():
    with tempfile.TemporaryDirectory(prefix='lk-eng-cn-gl-asm-') as directory:
        obj = Path(directory) / 'callback.o'
        subprocess.run(['clang', '-target', 'aarch64-none-elf', '-c',
                        str(HERE / 'trust_cn_global.S'), '-o', str(obj)], check=True)
        with obj.open('rb') as stream:
            elf = ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text', '.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved callback relocation')
            code = elf.get_section_by_name('.text').data()
    if len(code) != 216:
        raise ValueError('Callback does not fit its original function')
    return code


if __name__ == '__main__':
    data = assemble()
    (HERE / 'trust_cn_global.bin').write_bytes(data)
    import hashlib
    print(len(data), hashlib.sha256(data).hexdigest())

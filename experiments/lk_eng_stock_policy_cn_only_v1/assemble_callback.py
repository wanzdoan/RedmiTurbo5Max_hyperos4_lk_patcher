from pathlib import Path
import hashlib
import subprocess
import tempfile
from elftools.elf.elffile import ELFFile

HERE = Path(__file__).resolve().parent


def assemble():
    with tempfile.TemporaryDirectory(prefix='lk-eng-cn-only-') as folder:
        obj = Path(folder) / 'callback.o'
        subprocess.run(['clang', '-target', 'aarch64-none-elf', '-c', str(HERE / 'trust_eng_cn.S'), '-o', str(obj)], check=True)
        with obj.open('rb') as stream:
            elf = ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text', '.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved relocation')
            data = elf.get_section_by_name('.text').data()
    if len(data) != 216: raise ValueError('Callback must fit original216-byte function')
    return data


if __name__ == '__main__':
    data = assemble(); (HERE / 'trust_eng_cn.bin').write_bytes(data)
    print(len(data), hashlib.sha256(data).hexdigest())

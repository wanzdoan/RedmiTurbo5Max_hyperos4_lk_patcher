from pathlib import Path
import subprocess
import tempfile
import hashlib
from elftools.elf.elffile import ELFFile

HERE=Path(__file__).resolve().parent


def assemble():
    with tempfile.TemporaryDirectory(prefix='lk-auto-hwc-') as directory:
        obj=Path(directory)/'auto.o'
        subprocess.run(['clang','-target','aarch64-none-elf','-c',str(HERE/'auto_hwc.S'),'-o',str(obj)],check=True)
        with obj.open('rb') as stream:
            elf=ELFFile(stream)
            for section in elf.iter_sections():
                if section.name.startswith(('.rel.text','.rela.text')) and section.num_relocations():
                    raise ValueError('Unresolved code relocation')
            data=elf.get_section_by_name('.text').data()
    if len(data)!=128:raise ValueError('Late HWC block exceeds original128-byte span')
    return data


if __name__=='__main__':
    data=assemble();(HERE/'auto_hwc.bin').write_bytes(data)
    print(len(data),hashlib.sha256(data).hexdigest())

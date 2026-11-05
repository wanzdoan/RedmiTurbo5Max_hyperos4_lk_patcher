"""Check original direct branches against rewritten public/common blocks."""
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from build_hybrid import *


def main():
    decoder = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN); decoder.skipdata = True
    for kind, cfg in BASES.items():
        source = (ROOT / cfg['path']).read_bytes()
        spans = [(cfg['oem'], cfg['oem'] + 140), (cfg['report'], cfg['report'] + 52),
                 (cfg['common'], cfg['common'] + 24)]
        for ins in decoder.disasm(source[512 + 0x3000:512 + 0xc8000], 0x3000):
            if ins.mnemonic in ('b', 'bl', 'cbz', 'cbnz', 'tbz', 'tbnz') or ins.mnemonic.startswith('b.'):
                last = ins.op_str.rsplit(',', 1)[-1].strip()
                if not last.startswith('#0x'): continue
                target = int(last[1:], 16)
                for lo, hi in spans:
                    if lo < target < hi and not lo <= ins.address < hi:
                        raise AssertionError((kind, hex(ins.address), hex(target)))
        image = (HERE / kind / cfg['name']).read_bytes()
        report = dict(status='PASS_BOUNDED_DIRECT_BRANCH_SCAN', artifact_sha256=sha(image),
                      source_sha256=cfg['sha256'], scan='main payload0x3000..0xc8000 aligned Capstone disassembly',
                      rewritten_regions=[dict(start=hex(lo), end=hex(hi)) for lo, hi in spans],
                      no_external_direct_branch_into_rewritten_interior=True,
                      limits=['Computed indirect branches and whole program control flow are not proved'])
        (HERE / kind / 'layout_validation.json').write_text(json.dumps(report, indent=2) + '\n')
        print(kind, 'public/common block direct branch scan PASS')


if __name__ == '__main__': main()

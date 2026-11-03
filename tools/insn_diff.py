#!/usr/bin/env python3
"""Compare two AArch64 LK payloads at instruction level, normalising the
string-table shift caused by the relocated build-id string."""
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
from capstone.arm64 import ARM64_OP_IMM, ARM64_OP_MEM, ARM64_OP_REG

BASE = 0xFFFF000090700000
CODE_END = 0xCF000


def load(p):
    return open(p, "rb").read()


def insn_at(md, data, off):
    for i in md.disasm(data[off:off + 4], BASE + off):
        return i
    return None


def ref_addr(ins):
    """Return the absolute address referenced by adrp/add/ldr literal/branch."""
    if ins is None:
        return None
    if ins.mnemonic in ("adrp",):
        for op in ins.operands:
            if op.type == ARM64_OP_IMM:
                return op.imm
    if ins.mnemonic in ("add", "sub"):
        # adrp+add is normally two instructions; only the add imm matters
        for op in ins.operands:
            if op.type == ARM64_OP_IMM:
                return op.imm
    if ins.mnemonic in ("b", "bl", "cbz", "cbnz", "tbz", "tbnz"):
        for op in ins.operands:
            if op.type == ARM64_OP_IMM:
                return op.imm
    if ins.mnemonic == "ldr" and "pc" in ins.op_str:
        for op in ins.operands:
            if op.type == ARM64_OP_IMM:
                return op.imm
    return None


def main(a_path, b_path):
    a, b = load(a_path), load(b_path)
    md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
    md.detail = True
    n = min(len(a), len(b), CODE_END)
    real = []
    benign = 0
    for off in range(0, n, 4):
        if a[off:off + 4] == b[off:off + 4]:
            continue
        ia = insn_at(md, a, off)
        ib = insn_at(md, b, off)
        if ia is None or ib is None:
            real.append((off, a[off:off + 4].hex(), b[off:off + 4].hex(), "undecodable"))
            continue
        # identical text after replacing the immediate? compare mnemonic and
        # register/memory parts, ignoring immediate values.
        def shape(i):
            s = i.op_str
            # strip immediates
            import re
            return i.mnemonic + " " + re.sub(r"#?-?0x[0-9a-f]+|#?-?\d+", "#", s)
        ra, rb = ref_addr(ia), ref_addr(ib)
        if shape(ia) == shape(ib) and ra is not None and rb is not None and ra - rb == 0x17:
            benign += 1
            continue
        if shape(ia) == shape(ib) and ra is not None and rb is not None and abs(ra - rb) <= 0x40:
            benign += 1
            continue
        real.append((off, f"{ia.mnemonic} {ia.op_str}", f"{ib.mnemonic} {ib.op_str}", "diff"))
    print(f"benign string-offset changes: {benign}")
    print(f"candidate real changes: {len(real)}")
    for off, x, y, why in real:
        print(f"{off:#08x}  {x:<40} | {y:<40} {why}")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])

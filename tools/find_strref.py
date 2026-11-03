#!/usr/bin/env python3
"""Locate the function that references a given string in an AArch64 LK payload."""
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
from capstone.arm64 import ARM64_OP_IMM

BASE = 0xFFFF000090700000
STR_BASE = 0x90700000


def find_refs(data, target_off):
    """Find adrp/add (or adr) instruction pairs that materialise target_off."""
    md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
    md.detail = True
    n = len(data)
    hits = []
    adrp = {}
    for off in range(0, n - 4, 4):
        for i in md.disasm(data[off:off + 4], BASE + off):
            if i.mnemonic == "adrp":
                for op in i.operands:
                    if op.type == ARM64_OP_IMM:
                        adrp[off] = op.imm & 0xFFFFFFFF
            elif i.mnemonic in ("add", "ldr", "str") and (off - 4) in adrp:
                page = adrp[off - 4]
                if i.mnemonic == "add" and len(i.operands) == 3:
                    if i.operands[2].type == ARM64_OP_IMM:
                        addr = page + i.operands[2].imm
                        if addr == target_off:
                            hits.append((off - 4, off))
                elif i.mnemonic == "ldr" and len(i.operands) == 2:
                    for op in i.operands:
                        if op.type == 3 and op.mem.base and op.mem.disp:
                            pass
            elif i.mnemonic == "adr":
                for op in i.operands:
                    if op.type == ARM64_OP_IMM and (op.imm & 0xFFFFFFFF) == target_off:
                        hits.append((off, off))
    return hits


def function_start(data, off):
    """Walk back to the nearest paciasp / stp x29,x30 prologue."""
    while off > 0:
        w = data[off:off + 4]
        if w == b"\x3f\x23\x03\xd5" or w == b"\x7f\x23\x03\xd5":  # paciasp / pacibsp
            return off
        if off >= 4 and data[off - 4:off] in (b"\x5f\x24\x03\xd5", b"\x9f\x24\x03\xd5"):
            return off
        off -= 4
    return 0


if __name__ == "__main__":
    path = sys.argv[1]
    strva = int(sys.argv[2], 16)
    data = open(path, "rb").read()
    refs = find_refs(data, strva)
    print(f"refs to {strva:#x}: {len(refs)}")
    for a, b in refs:
        fs = function_start(data, a)
        print(f"  insn @ {a:#x}-{b:#x}  function @ payload {fs:#x}  VA {BASE + fs:#x}")

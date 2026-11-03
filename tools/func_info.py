#!/usr/bin/env python3
"""Dump a summary of an AArch64 LK function: referenced strings, call targets,
and large immediates. Works without Ghidra."""
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
from capstone.arm64 import ARM64_OP_IMM, ARM64_OP_REG

BASE = 0xFFFF000090700000
STR_BASE = 0x90700000


def cstr(data, off):
    if off < 0 or off >= len(data):
        return None
    end = data.find(b"\x00", off, min(off + 300, len(data)))
    if end < 0:
        return None
    raw = data[off:end]
    if len(raw) < 3:
        return None
    try:
        s = raw.decode("utf-8")
    except UnicodeDecodeError:
        return None
    if not all(32 <= ord(c) < 127 or c in "\n\r\t" for c in s):
        return None
    return s


def analyze(path, start, end, label=""):
    d = open(path, "rb").read()
    md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
    md.detail = True
    adrp = {}
    strings = []
    calls = []
    consts = []
    for ins in md.disasm(d[start:end], BASE + start):
        off = ins.address - BASE
        if ins.mnemonic == "adrp":
            for op in ins.operands:
                if op.type == ARM64_OP_IMM:
                    adrp[off] = op.imm & 0xFFFFFFFF
        elif ins.mnemonic == "add" and (off - 4) in adrp and len(ins.operands) == 3:
            if ins.operands[2].type == ARM64_OP_IMM:
                addr = adrp[off - 4] + ins.operands[2].imm
                s = cstr(d, (addr & 0xFFFFFFFF) - STR_BASE)
                if s:
                    strings.append((off, addr & 0xFFFFFFFF, s))
        elif ins.mnemonic == "adr":
            for op in ins.operands:
                if op.type == ARM64_OP_IMM:
                    s = cstr(d, (op.imm & 0xFFFFFFFF) - STR_BASE)
                    if s:
                        strings.append((off, op.imm & 0xFFFFFFFF, s))
        elif ins.mnemonic == "bl":
            for op in ins.operands:
                if op.type == ARM64_OP_IMM:
                    calls.append((off, (op.imm - BASE) & 0xFFFFFFFF))
        elif ins.mnemonic in ("mov", "movz", "movk") and ins.operands:
            if len(ins.operands) >= 2 and ins.operands[1].type == ARM64_OP_IMM:
                v = ins.operands[1].imm
                if abs(v) >= 0x1000:
                    consts.append((off, v))
    print(f"### {label} {path} [{start:#x}..{end:#x}]")
    print("-- strings --")
    seen = set()
    for off, addr, s in strings:
        if s in seen:
            continue
        seen.add(s)
        print(f"  {off:#08x} {addr:#010x}  {s!r}")
    print("-- calls --")
    print("  " + " ".join(f"{o:#x}->{t:#x}" for o, t in calls))
    print("-- big consts --")
    print("  " + " ".join(f"{o:#x}={v:#x}" for o, v in consts[:60]))
    print()


if __name__ == "__main__":
    path = sys.argv[1]
    start = int(sys.argv[2], 16)
    end = int(sys.argv[3], 16)
    label = sys.argv[4] if len(sys.argv) > 4 else ""
    analyze(path, start, end, label)

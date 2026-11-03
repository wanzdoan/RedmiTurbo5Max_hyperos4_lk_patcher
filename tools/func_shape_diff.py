#!/usr/bin/env python3
"""Function-level shape comparison between two AArch64 LK payloads.

Functions are anchored on PAC prologues (paciasp/pacibsp, optionally preceded
by bti). Each function is reduced to a sequence of (mnemonic, operand shape)
with immediates masked, then hashed. Branch targets are kept as
(target - function_start) so that layout shifts do not matter.
"""
import hashlib
import sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_ARM
from capstone.arm64 import ARM64_OP_IMM

BASE = 0xFFFF000090700000
CODE_END = 0xCF000
PAC = (b"\x3f\x23\x03\xd5", b"\x7f\x23\x03\xd5")
BTI = (b"\x5f\x24\x03\xd5", b"\x9f\x24\x03\xd5")


def functions(data):
    starts = []
    for off in range(0, min(len(data), CODE_END) - 4, 4):
        w = data[off:off + 4]
        if w in PAC:
            s = off
            if off >= 4 and data[off - 4:off] in BTI:
                s = off - 4
            starts.append(s)
    starts = sorted(set(starts))
    out = []
    for i, s in enumerate(starts):
        e = starts[i + 1] if i + 1 < len(starts) else min(len(data), CODE_END)
        out.append((s, e))
    return out


def shape(md, data, s, e):
    toks = []
    for ins in md.disasm(data[s:e], s):
        ops = []
        for op in ins.operands:
            if op.type == ARM64_OP_IMM:
                if ins.group(1) or ins.mnemonic.startswith("b") or ins.mnemonic in (
                        "cbz", "cbnz", "tbz", "tbnz"):
                    ops.append("T%+d" % (op.imm - s))
                else:
                    ops.append("I")
            else:
                ops.append(op.type)
        toks.append(ins.mnemonic + "/" + ",".join(str(x) for x in ops))
    return "\n".join(toks)


def main(pa, pb):
    da, db = open(pa, "rb").read(), open(pb, "rb").read()
    md = Cs(CS_ARCH_ARM64, CS_MODE_ARM)
    md.detail = True
    fa, fb = functions(da), functions(db)
    print(f"{pa}: {len(fa)} functions")
    print(f"{pb}: {len(fb)} functions")
    sa = {s: shape(md, da, s, e) for s, e in fa}
    sb = {s: shape(md, db, s, e) for s, e in fb}
    ha = {s: hashlib.sha1(v.encode()).hexdigest() for s, v in sa.items()}
    hb = {s: hashlib.sha1(v.encode()).hexdigest() for s, v in sb.items()}
    setb = {}
    for s, h in hb.items():
        setb.setdefault(h, []).append(s)
    same = [s for s, h in ha.items() if h in setb]
    diff = [s for s, h in ha.items() if h not in setb]
    onlyb = [s for s, h in hb.items() if h not in set(ha.values())]
    print(f"identical shape: {len(same)}   differing: {len(diff)}   only-in-B: {len(onlyb)}")
    print("--- functions whose shape differs (H3 addr -> size) ---")
    for s in diff:
        e = dict(fa)[s]
        print(f"{s:#08x} size={e - s:#06x}")
    print("--- functions only in B ---")
    for s in onlyb:
        e = dict(fb)[s]
        print(f"{s:#08x} size={e - s:#06x}")
    with open("extracted/shape_diff.txt", "w", encoding="utf-8") as f:
        for s in diff:
            e = dict(fa)[s]
            f.write(f"===== H3 {s:#x} size {e - s:#x}\n")
            f.write(sa[s] + "\n\n")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])

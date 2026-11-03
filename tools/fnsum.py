#!/usr/bin/env python3
"""Print one-line summaries (referenced strings / calls) for a list of functions.

Function boundaries are recovered from PAC prologues so a summary never bleeds
into the next function.
"""
import importlib.util
import io
import os
import sys
import contextlib

HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location("fi", os.path.join(HERE, "func_info.py"))
fi = importlib.util.module_from_spec(spec)
spec.loader.exec_module(fi)

PAC = (b"\x3f\x23\x03\xd5", b"\x7f\x23\x03\xd5")
BTI = (b"\x5f\x24\x03\xd5", b"\x9f\x24\x03\xd5")
CODE_END = 0xCF000


def starts(data):
    out = []
    for off in range(0, min(len(data), CODE_END) - 4, 4):
        if data[off:off + 4] in PAC:
            s = off
            if off >= 4 and data[off - 4:off] in BTI:
                s = off - 4
            out.append(s)
    return sorted(set(out))


def bounds(data):
    st = starts(data)
    return {s: (st[i + 1] if i + 1 < len(st) else CODE_END) for i, s in enumerate(st)}


def summarize(path, start, end=None):
    if end is None:
        end = bounds(open(path, "rb").read()).get(start, min(start + 0x2000, CODE_END))
    buf = io.StringIO()
    with contextlib.redirect_stdout(buf):
        fi.analyze(path, start, end, "")
    out = buf.getvalue()
    strings, calls = [], []
    mode = None
    for line in out.splitlines():
        if line.startswith("-- strings"):
            mode = "s"
            continue
        if line.startswith("-- calls"):
            mode = "c"
            continue
        if line.startswith("-- big"):
            mode = None
            continue
        if mode == "s" and line.strip():
            parts = line.strip().split(None, 2)
            if len(parts) == 3:
                strings.append(parts[2])
        if mode == "c" and line.strip():
            calls = line.strip().split()
    return strings, calls, end


if __name__ == "__main__":
    path = sys.argv[1]
    for a in sys.argv[2:]:
        s, c, e = summarize(path, int(a, 16))
        uniq = []
        for x in s:
            if x not in uniq:
                uniq.append(x)
        print(f"{int(a, 16):#08x}-{e:#08x}: {' | '.join(uniq[:8])[:240]}")

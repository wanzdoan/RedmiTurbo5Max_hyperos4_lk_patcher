#!/usr/bin/env python3
"""Semantic comparison of two Ghidra decompilation exports.

Decompiled C is normalised so that address-dependent artifacts (string symbol
names, data symbols, function names, numeric constants) do not count as logic
changes. Anything left is a real difference in the emitted code.
"""
import os
import re
import sys


def load(variant_dir):
    out = {}
    p = os.path.join(variant_dir, "functions")
    for fn in os.listdir(p):
        addr = fn.split("_")[0]
        with open(os.path.join(p, fn), encoding="utf-8", errors="replace") as f:
            out[addr] = f.read()
    return out


def norm(t):
    t = re.sub(r"/\*.*?\*/", "", t, flags=re.S)
    t = re.sub(r"//.*", "", t)
    t = re.sub(r"[0-9a-fA-F]{8,}", "ADDR", t)
    t = re.sub(r"\bs_[A-Za-z0-9_]+", "STR", t)
    t = re.sub(r"\b_?[A-Z]+_[0-9a-fA-F]{6,}", "SYM", t)
    t = re.sub(r"\b(?:FUN|LAB|SUB|thunk)_[0-9a-fA-F_]+", "SYM", t)
    t = re.sub(r"\b0x[0-9a-fA-F]+", "N", t)
    t = re.sub(r"\b\d+\b", "N", t)
    t = re.sub(r"\b(?:u|i|b|c|s|us|ui|ul|ull|byte|uint|int|char|short|long|"
               r"undefined|code|bool|float|double|size_t|ssize_t|"
               r"uint[0-9]+_t|int[0-9]+_t|undefined[0-9])\b", "T", t)
    t = re.sub(r"\bVar\d+\b", "V", t)
    t = re.sub(r"\bparam_\d+\b", "P", t)
    t = re.sub(r"\bin_\w+", "P", t)
    t = re.sub(r"\s+", " ", t)
    return t.strip()


def main(a_dir, b_dir, only_list=False):
    A = load(a_dir)
    B = load(b_dir)
    same = 0
    diff = []
    for k in sorted(A):
        if k in B and norm(A[k]) == norm(B[k]):
            same += 1
        else:
            diff.append(k)
    missing = [k for k in A if k not in B]
    extra = [k for k in B if k not in A]
    print(f"A={len(A)} B={len(B)} identical={same} different={len(diff)} "
          f"missing_in_B={len(missing)} extra_in_B={len(extra)}")
    if only_list:
        for k in diff:
            print(k)
        return
    print("--- different ---")
    for k in diff:
        print("  " + k)


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], len(sys.argv) > 3)

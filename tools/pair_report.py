#!/usr/bin/env python3
"""Pair unmatched functions between two Ghidra exports by best C similarity and
emit a markdown report with unified diffs."""
import difflib
import hashlib
import importlib.util
import os
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location("dd", os.path.join(HERE, "decomp_diff.py"))
dd = importlib.util.module_from_spec(spec)
spec.loader.exec_module(dd)

# Verified pairings (call-graph role + strings + decompiled similarity).
# H3 -> H4
MANUAL_PAIRS = [
    ("ffff0000907034f8", "ffff00009071c6a0"),  # mi_hwid_get (global struct -> contiguous struct)
    ("ffff000090706f84", "ffff000090706ee0"),  # mt6379_set_bat_data
    ("ffff00009070a494", "ffff00009070a420"),  # wait_for_enter_fastboot
    ("ffff000090710b5c", "ffff000090710bd4"),  # charger_get_poweroff_mode
    ("ffff000090713930", "ffff000090713a14"),  # androidboot_cmdline_builder
    ("ffff00009072203c", "ffff00009072227c"),  # boot_para_load_image
    ("ffff000090768270", "ffff000090768508"),  # get_dm_verity_status
    ("ffff000090768368", "ffff000090768638"),  # clear_dm_verity_status
    ("ffff000090787050", "ffff000090787318"),  # secinfo_verify_sku -> _gl_hardware
    ("ffff0000907ad910", "ffff0000907adbd0"),  # sec_func_init
    ("ffff0000907afe98", "ffff0000907b0160"),  # sec cert A
    ("ffff0000907b0960", "ffff0000907b0ec4"),  # sec cert B
    ("ffff0000907b1478", "ffff0000907b1be4"),  # sec socid A
    ("ffff0000907b1974", "ffff0000907b216c"),  # sec socid B
    ("ffff0000907b1f58", "ffff0000907b25b4"),  # sec socid C
    ("ffff0000907b5f98", "ffff0000907b6608"),  # print_lock_state
]

# H4 functions created by the SEC refactor (split out of the four H3 bodies).
SEC_HELPERS_H4 = {
    "ffff0000907b05cc", "ffff0000907b0d14", "ffff0000907b13bc",
    "ffff0000907b157c", "ffff0000907b171c", "ffff0000907b18dc",
    "ffff0000907b1a60",
}
NEW_IN_H4 = {
    "ffff0000907213d8": "bq28z610 fuel-gauge driver (new hardware)",
    "ffff00009072b0f8": "logstore/expdb uart_console flag getter (new)",
}


def sizes(export_dir):
    out = {}
    with open(os.path.join(export_dir, "functions.tsv"), encoding="utf-8") as f:
        for line in f:
            p = line.rstrip("\n").split("\t")
            if len(p) >= 3:
                out[p[0]] = (int(p[1]), p[2])
    return out


def main(a_dir, b_dir, out_md):
    A = dd.load(a_dir)
    B = dd.load(b_dir)
    ha = {k: hashlib.sha1(dd.norm(v).encode()).hexdigest() for k, v in A.items()}
    hb = {k: hashlib.sha1(dd.norm(v).encode()).hexdigest() for k, v in B.items()}
    setb = set(hb.values())
    seta = set(ha.values())
    onlya = sorted(k for k, v in ha.items() if v not in setb)
    onlyb = sorted(k for k, v in hb.items() if v not in seta)
    sa = sizes(a_dir)
    sb = sizes(b_dir)

    na = {k: dd.norm(A[k]) for k in onlya}
    nb = {k: dd.norm(B[k]) for k in onlyb}

    pairs = []
    used = set()
    for k, j in MANUAL_PAIRS:
        if k not in onlya or j not in onlyb:
            print(f"WARN: manual pair not in unmatched sets: {k} -> {j}")
            continue
        r = difflib.SequenceMatcher(None, na[k], nb[j], autojunk=False).ratio()
        used.add(j)
        pairs.append((k, j, r))

    with open(out_md, "w", encoding="utf-8") as f:
        f.write("# H3 vs H4 - changed functions\n\n")
        f.write(f"- H3 functions: {len(A)}, H4 functions: {len(B)}\n")
        f.write(f"- identical decompiled content: {len(A) - len(onlya)}\n")
        f.write(f"- H3-only: {len(onlya)}, H4-only: {len(onlyb)}\n\n")
        f.write("| H3 | H4 | similarity | H3 size | H4 size |\n|---|---|---|---|---|\n")
        for k, j, r in pairs:
            f.write(f"| {k} | {j} | {r:.2f} | {sa.get(k, ('?',))[0]} | {sb.get(j, ('?',))[0]} |\n")
        f.write("\n")
        for k, j, r in pairs:
            f.write(f"\n## {k} -> {j}  (similarity {r:.2f})\n\n")
            f.write("```diff\n")
            d = difflib.unified_diff(na[k].split(" "), nb[j].split(" "),
                                     fromfile="H3 " + k, tofile="H4 " + j,
                                     lineterm="", n=6)
            for line in d:
                f.write(line + "\n")
            f.write("```\n")
        left = [k for k in onlyb if k not in used]
        if left:
            f.write("\n## Functions only in H4 (no H3 counterpart)\n\n")
            for j in left:
                note = NEW_IN_H4.get(j)
                if note is None and j in SEC_HELPERS_H4:
                    note = "SEC refactor helper (split out of a larger H3 function)"
                f.write(f"- {j} size {sb.get(j, ('?',))[0]}"
                        + (f" - {note}" if note else "") + "\n")
        lefta = [k for k in onlya if k not in [p[0] for p in pairs]]
        if lefta:
            f.write("\n## Functions only in H3 (removed in H4)\n\n")
            for k in lefta:
                f.write(f"- {k} size {sa.get(k, ('?',))[0]}\n")
    print(f"pairs={len(pairs)} onlyA={len(onlya)} onlyB={len(onlyb)} -> {out_md}")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3])

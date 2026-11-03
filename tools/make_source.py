#!/usr/bin/env python3
"""Build a readable, name-annotated decompiled source tree from a Ghidra export.

Function names are derived from the strings each function references, because
LK is stripped: Ghidra only knows FUN_<addr>. A string that looks like a C
identifier (mi_get_lock_state, sec_func_init, ...) is almost always the name
the original developer used in a log/assert, so it is a good label.
"""
import os
import re
import sys

IDENT = re.compile(r"^[a-z_][a-z0-9_]{3,47}$")
BAD = re.compile(r"^[0-9a-f]{6,}$")


def parse_strings(path):
    """strings.tsv -> {func_addr: [strings]}"""
    per_func = {}
    with open(path, encoding="utf-8", errors="replace") as f:
        for line in f:
            parts = line.rstrip("\n").split("\t")
            if len(parts) < 3:
                continue
            s = parts[1]
            for m in re.finditer(r"([0-9a-f]{8,})\((FUN_[0-9a-f]+)\)", parts[2]):
                per_func.setdefault(m.group(2), []).append(s)
    return per_func


def clean(s):
    s = s.replace("\\n", " ").replace("\\t", " ").replace("\\r", " ")
    return s.strip()


STOP = {
    "takes", "failed", "product", "interface", "while", "allocate", "state",
    "error", "value", "boot", "info", "null", "size", "data", "mode", "name",
    "count", "index", "addr", "length", "buffer", "return", "start", "end",
    "open", "close", "read", "write", "init", "deinit", "invalid", "unsupport",
    "not", "the", "for", "and", "with", "from", "into", "this", "that",
}

# Hand-verified names for functions that matter to the bootloader-unlock story.
OVERRIDES = {
    "ffff000090703650": "lk_boot_init",
    "ffff0000907034f8": "get_poweroff_reason",
    "ffff000090706f84": "mt6379_set_bat_data",
    "ffff00009070a494": "fastboot_publish_attributes",
    "ffff000090709dd0": "fastboot_init",
    "ffff00009070b3dc": "oem_db_encrypt_cmd",
    "ffff00009070b470": "oem_selinux_cmd",
    "ffff00009070c4e0": "fastboot_oem_unlock",
    "ffff00009070c610": "fastboot_oem_lock",
    "ffff000090710b5c": "charger_get_poweroff_mode",
    "ffff000090713930": "androidboot_cmdline_builder",
    "ffff00009072203c": "boot_para_load_image",
    "ffff000090768270": "get_dm_verity_status",
    "ffff000090768368": "clear_dm_verity_status",
    "ffff000090787050": "secinfo_verify_sku",
    "ffff000090788750": "mi_get_lock_state_api",
    "ffff000090788d60": "mi_get_lock_state_wrapper",
    "ffff00009078982c": "mi_get_lock_state",
    "ffff0000907ad910": "sec_func_init",
    "ffff0000907afe98": "sec_verify_image_cert_a",
    "ffff0000907b0960": "sec_verify_image_cert_b",
    "ffff0000907b1478": "sec_check_socid_a",
    "ffff0000907b1974": "sec_check_socid_b",
    "ffff0000907b1f58": "sec_check_socid_c",
    "ffff0000907b5f98": "print_lock_state",
    # ---- HyperOS 4 ----
    "ffff000090706ee0": "mt6379_set_bat_data",
    "ffff00009070a420": "fastboot_publish_attributes",
    "ffff00009070c558": "fastboot_oem_unlock",
    "ffff00009070c688": "fastboot_oem_lock",
    "ffff000090710bd4": "charger_get_poweroff_mode",
    "ffff000090713a14": "androidboot_cmdline_builder",
    "ffff00009071c6a0": "hwid_info_check",
    "ffff0000907213d8": "bq28z610_get_battery_model_id",
    "ffff00009072227c": "boot_para_load_image",
    "ffff00009072b0f8": "logstore_lk_flush",
    "ffff000090768508": "get_dm_verity_status",
    "ffff000090768638": "clear_dm_verity_status",
    "ffff000090787318": "secinfo_verify_gl_hardware",
    "ffff000090788a10": "mi_get_lock_state_api",
    "ffff000090789020": "mi_get_lock_state_wrapper",
    "ffff000090789aec": "mi_get_lock_state",
    "ffff0000907adbd0": "sec_func_init",
    "ffff0000907b0160": "sec_verify_image_cert_a",
    "ffff0000907b05cc": "sec_verify_image_cert_b",
    "ffff0000907b0d14": "sec_check_socid_a",
    "ffff0000907b0ec4": "sec_check_socid_b",
    "ffff0000907b13bc": "sec_check_socid_c",
    "ffff0000907b157c": "sec_check_socid_d",
    "ffff0000907b171c": "sec_check_socid_e",
    "ffff0000907b18dc": "sec_check_socid_f",
    "ffff0000907b1a60": "sec_check_socid_g",
    "ffff0000907b1be4": "sec_check_socid_h",
    "ffff0000907b216c": "sec_check_socid_i",
    "ffff0000907b25b4": "sec_check_socid_j",
    "ffff0000907b6608": "print_lock_state",
}


def derive(func, strings):
    """Pick the most descriptive identifier-like string (frequency weighted)."""
    from collections import Counter
    cands = [clean(s) for s in strings]
    cnt = Counter(cands)

    def score(s):
        return (cnt[s], len(s))

    ident = [s for s in set(cands) if IDENT.match(s) and not BAD.match(s)
             and s not in STOP]
    # prefer identifiers containing an underscore (real function names)
    underscored = [s for s in ident if "_" in s]
    if underscored:
        return max(underscored, key=score)
    if ident:
        return max(ident, key=score)

    # "%s ... <ident>" / "[%s] <ident>" patterns
    pat = []
    for s in set(cands):
        m = re.search(r"[\]\s]([a-z][a-z0-9_]{4,40})\b", s)
        if m and m.group(1) not in STOP and not BAD.match(m.group(1)):
            pat.append((cnt[s], len(m.group(1)), m.group(1)))
    if pat:
        return max(pat)[2]

    for s in cands:
        if len(s) >= 4:
            t = re.sub(r"[^A-Za-z0-9_]+", "_", s).strip("_")
            if t:
                return "str_" + t[:40]
    return "sub"


def main(export_dir, out_dir, prefix=""):
    funcs_dir = os.path.join(export_dir, "functions")
    per_func = parse_strings(os.path.join(export_dir, "strings.tsv"))
    os.makedirs(out_dir, exist_ok=True)

    entries = []
    used = {}
    files = sorted(os.listdir(funcs_dir))
    for fn in files:
        addr = fn.split("_")[0]
        ghidra_name = fn.split("_", 1)[1][:-2]
        strings = per_func.get(ghidra_name, [])
        base = OVERRIDES.get(addr) or derive(ghidra_name, strings)
        name = base
        n = 2
        while name in used:
            name = f"{base}_{n}"
            n += 1
        used[name] = addr
        entries.append((addr, name, strings, fn))

    entries.sort()
    index = os.path.join(out_dir, "index.tsv")
    with open(index, "w", encoding="utf-8") as idx:
        idx.write("address\tname\tsize\tstrings\n")
        for addr, name, strings, fn in entries:
            with open(os.path.join(funcs_dir, fn), encoding="utf-8", errors="replace") as f:
                body = f.read()
            # size from the header comment
            m = re.search(r"size=(\d+)", body)
            size = m.group(1) if m else ""
            idx.write(f"{addr}\t{name}\t{size}\t{' | '.join(strings[:4])}\n")

    # combined file with renamed functions
    allc = os.path.join(out_dir, "all_named.c")
    with open(allc, "w", encoding="utf-8") as out:
        out.write(f"// decompiled LK source ({os.path.basename(export_dir)})\n")
        out.write("// names derived from log strings; addresses are virtual\n\n")
        for addr, name, strings, fn in entries:
            with open(os.path.join(funcs_dir, fn), encoding="utf-8", errors="replace") as f:
                body = f.read()
            body = re.sub(r"\b" + re.escape(ghidra_name_of(fn)) + r"\b", name, body)
            out.write(body)
            out.write("\n\n")
    print(f"{os.path.basename(export_dir)}: {len(entries)} functions -> {out_dir}")
    return entries


def ghidra_name_of(fn):
    # ffff00009078982c_FUN_ffff00009078982c.c -> FUN_ffff00009078982c
    return fn.split("_", 1)[1][:-2]


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])

#!/usr/bin/env python3
"""
Build pure unlocked Little Kernel (lk.img) for Xiaomi Redmi Turbo 5 Max (dash)
HyperOS 4 official firmware: OS4.0.3.0.XPLCNXM.
"""

import sys
from pathlib import Path
import hashlib
import zipfile
import capstone

CONTAINER_FILE = Path('lk_hyperos4_original/lk.img')
OUTPUT_DIR     = Path('lk_hyperos4_patched')
OUTPUT_FILE    = OUTPUT_DIR / 'lk.img'
ZIP_FILE       = Path('lk_hyperos4_patched.zip')

data = bytearray(CONTAINER_FILE.read_bytes())
assert len(data) == 4087536, f"Unexpected container length: {len(data)}"

PAYLOAD_BASE = 0x200
VA_BASE      = 0xffff000090700000

# 5 surgical patches
PATCHES = [
    (
        0x89aec,
        '3f2303d5ff4305d1fd7b12a9fc5713a9',
        '68008052080000b900008052c0035fd6',
        'unlock_bootloader',
        'Force mi_get_lock_state to return state = 3 (UNLOCKED) and ret 0'
    ),
    (
        0x695d0,
        '3f2303d5fd7bbea9',
        '00008052c0035fd6',
        'suppress_destroyed_dispatch',
        'Neutralize warning/destroyed dispatcher, completely preventing 5s shutdown and destroyed screen'
    ),
    (
        0x68420,
        '3f2303d5',
        'c0035fd6',
        'suppress_dmverity_corruption',
        'Suppress dm-verity corruption shutdown check'
    ),
    (
        0x695bc,
        '3f2303d5c805009000b109b9bf2303d5c0035fd6',
        '40008052c805009000b109b9c0035fd61f2003d5',
        'force_verified_boot_orange',
        'Force VerifiedBootState = 2 (ORANGE), passing androidboot.verifiedbootstate=orange to kernel'
    ),
    (
        0x87318,
        '3f2303d5fd7bbea9',
        '00008052c0035fd6',
        'bypass_cross_region',
        'Bypass secinfo_verify_gl_hardware cross-region GL/CN check'
    )
]

print("=== APPLYING SURGICAL UNLOCK PATCHES TO LK HYPEROS 4 ===")
md = capstone.Cs(capstone.CS_ARCH_ARM64, capstone.CS_MODE_ARM)

for payload_off, orig_hex, patch_hex, name, desc in PATCHES:
    cont_off = payload_off + PAYLOAD_BASE
    va       = VA_BASE + payload_off
    expected = bytes.fromhex(orig_hex)
    patch    = bytes.fromhex(patch_hex)
    assert len(expected) == len(patch)
    
    actual = data[cont_off : cont_off + len(expected)]
    if actual != expected:
        raise ValueError(
            f"Offset mismatch at {name} (0x{cont_off:x}): expected {orig_hex}, found {actual.hex()}"
        )
    
    data[cont_off : cont_off + len(patch)] = patch
    print(f"\n[+] Applied: {name}")
    print(f"    Container offset: 0x{cont_off:06x} | VA: 0x{va:08x} | Len: {len(patch)} bytes")
    print(f"    Description: {desc}")
    for i in md.disasm(patch, va):
        print(f"      0x{i.address:08x}: {i.mnemonic:<8} {i.op_str}")

OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
OUTPUT_FILE.write_bytes(data)

# Create ZIP
with zipfile.ZipFile(ZIP_FILE, 'w', zipfile.ZIP_DEFLATED) as z:
    z.write(OUTPUT_FILE, arcname='lk.img')

sha256 = hashlib.sha256(data).hexdigest()
print(f"\n[+] Successfully generated: {OUTPUT_FILE}")
print(f"    Size: {len(data):,} bytes (exact match)")
print(f"    SHA-256: {sha256}")
print(f"[+] Packaged into archive: {ZIP_FILE}")

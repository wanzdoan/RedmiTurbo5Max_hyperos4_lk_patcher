import sys
from pathlib import Path
import hashlib
import capstone

CONTAINER_FILE = Path('lk_hyperos4_original/lk.img')
OUTPUT_FILE    = Path('lk_hyperos4_patched/lk.img')
ZIP_FILE       = Path('lk_hyperos4_patched.zip')

data = bytearray(CONTAINER_FILE.read_bytes())
assert len(data) == 4087536, f"Unexpected container length: {len(data)}"

PAYLOAD_BASE = 0x200
VA_BASE      = 0xffff000090700000

# Definition of all patches
# (payload_offset, orig_hex, patch_hex, name, description)
PATCHES = [
    (
        0x89aec,
        '3f2303d5ff4305d1fd7b12a9fc5713a9',
        '68008052080000b900008052c0035fd6',
        'mi_get_lock_state',
        'Force bootloader lock state = 3 (UNLOCKED), return 0'
    ),
    (
        0x87318,
        '3f2303d5fd7bbea9',
        '00008052c0035fd6',
        'secinfo_verify_gl_hardware',
        'Bypass cross-region GL/CN check ("CN hardware not allow to flash GL Sw..")'
    ),
    (
        0x68420,
        '3f2303d5',
        'c0035fd6',
        'corruption',
        'Suppress dm-verity corruption shutdown & error screen ("has been destroyed")'
    ),
    (
        0x68f98,
        '21060054',
        '31000014',
        'avb_sku_check_skip',
        'Unconditional branch over AVB vbmeta SKU check to prevent 0.1s panic'
    ),
    (
        0x68edc,
        'e1020054',
        '17000014',
        'avb_ep_check_skip',
        'Unconditional branch over AVB vbmeta EP prototype check'
    ),
    (
        0x695c4,
        '00b109b9',
        '1fb109b9',
        'spoof_verified_boot_green',
        'Force Verified Boot State = 0 (GREEN), passing androidboot.verifiedbootstate=green'
    ),
    (
        0x695d0,
        '3f2303d5fd7bbea9',
        '00008052c0035fd6',
        'eliminate_warning_delay',
        'Eliminate 5-second countdown & warning splash screen in str_UNKNOWN'
    )
]

print("=== APPLYING ADVANCED FULL-COMBO PATCHES TO LK HYPEROS 4 ===")
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

OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
OUTPUT_FILE.write_bytes(data)

# Create ZIP
import zipfile
with zipfile.ZipFile(ZIP_FILE, 'w', zipfile.ZIP_DEFLATED) as z:
    z.write(OUTPUT_FILE, arcname='lk.img')

sha256 = hashlib.sha256(data).hexdigest()
print(f"\n[+] Successfully generated: {OUTPUT_FILE}")
print(f"    Size: {len(data):,} bytes")
print(f"    SHA-256: {sha256}")
print(f"[+] Packaged into archive: {ZIP_FILE}")

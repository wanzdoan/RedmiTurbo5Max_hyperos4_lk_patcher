"""Read selected Android metadata/partition bytes via authorized existing su.

No reboot/lock/flash, no block-device writes, no LK calls, no RPMB/efuse calls.
Raw device-specific evidence stays OUTSIDE this Git workspace.
"""
from pathlib import Path
import datetime
import hashlib
import json
import os
import shlex
import struct
import subprocess

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
PROPS = ['ro.product.device', 'ro.product.model', 'ro.build.version.incremental',
         'ro.build.version.release', 'ro.build.type', 'ro.boot.slot_suffix', 'ro.boot.hwc',
         'ro.boot.hwlevel', 'ro.boot.hwversion', 'ro.boot.ptcountrycode', 'ro.boot.verifiedbootstate',
         'ro.boot.vbmeta.device_state', 'ro.boot.flash.locked', 'ro.boot.veritymode',
         'ro.boot.bootreason', 'ro.boot.avb_version', 'ro.boot.vbmeta.digest',
         'ro.boot.vbmeta.hash_alg', 'ro.boot.vbmeta.size', 'ro.boot.secureboot']


def sha(data):
    return hashlib.sha256(data).hexdigest()


def main():
    rows = [r.split() for r in subprocess.check_output(['adb', 'devices'], text=True).splitlines()[1:] if r.strip()]
    ids = [r[0] for r in rows if len(r) > 1 and r[1] == 'device']
    if len(ids) != 1:
        raise RuntimeError('Need exactly one authorized ADB device')
    serial = ids[0]  # Never persisted.

    def shell(args):
        r = subprocess.run(['adb', '-s', serial, 'shell', *args], capture_output=True, timeout=30)
        if r.returncode:
            raise RuntimeError('Read-only shell query failed')
        return r.stdout.decode(errors='replace').strip()

    if shell(['getprop', 'ro.product.device']) != 'dash':
        raise RuntimeError('Unexpected target device')
    if shell(['su', '-c', 'id -u']) != '0':
        raise RuntimeError('Existing su does not provide root; nothing installed or changed')
    stamp = datetime.datetime.now(datetime.timezone.utc).strftime('%Y%m%d_%H%M%S')
    private = Path(os.environ['LOCALAPPDATA']) / 'Temp' / ('dash-relock-readonly-' + stamp)
    if private.resolve().is_relative_to(ROOT.resolve()):
        raise RuntimeError('Private evidence must be outside Git')
    private.mkdir(parents=True, exist_ok=False)
    props = {k: shell(['getprop', k]) for k in PROPS}
    # Fixed whitelist. head reads blocks as ordinary files; no writable ioctl is used.
    captures = [('lk_a', 16777216), ('lk_b', 16777216), ('secinfo', 131072),
                ('seccfg', 4096), ('vbmeta_a', 65536), ('vbmeta_b', 65536),
                ('vbmeta_system_a', 65536), ('vbmeta_vendor_a', 65536)]
    summaries = []
    known = {}
    for candidate in (ROOT / 'experiments').rglob('*.img'):
        if candidate.stat().st_size <= 20 * 1024 * 1024:
            known.setdefault(sha(candidate.read_bytes()), []).append(candidate.relative_to(ROOT).as_posix())
    for name, length in captures:
        command = 'su -c ' + shlex.quote(f'head -c {length} /dev/block/by-name/{name}')
        r = subprocess.run(['adb', '-s', serial, 'exec-out', command], capture_output=True, timeout=90)
        if r.returncode or len(r.stdout) != length:
            raise RuntimeError(f'Could not read expected bytes of {name}; no device changes made')
        data = r.stdout
        (private / (name + '.bin')).write_bytes(data)
        row = {'partition': name, 'captured_bytes': length, 'sha256': sha(data)}
        if name.startswith('lk_'):
            off = 0
            for _ in range(15):
                if data[off:off + 4] != bytes.fromhex('88168858'):
                    raise ValueError('Unexpected live LK container')
                size = struct.unpack_from('<I', data, off + 4)[0]
                off = (off + 512 + size + 15) & ~15
                if off > length:
                    raise ValueError('Live LK member outside capture')
            row.update(container_bytes=off, container_sha256=sha(data[:off]),
                       known_artifact_matches=known.get(sha(data[:off]), []),
                       tail_all_zero=not any(data[off:]))
        elif name == 'secinfo':
            stock = (ROOT / 'lk_hyperos3_original/lk.img').read_bytes()
            # Ghidra sanitizes '-'/'!' to '_' in symbol names; use original bytes.
            expected_magic = stock[512 + 0xffc83:512 + 0xffc83 + 13]
            row['stock_h3_partition_magic_matches'] = data[:13] == expected_magic
            row['has_nonzero_bytes'] = any(data)
        summaries.append(row)
    report = {'evidence_type': 'LIVE_DEVICE_READ_ONLY', 'recorded_utc': stamp,
              'serial_persisted': False, 'existing_su_used': True, 'properties': props,
              'captures': summaries, 'private_raw_directory': str(private),
              'device_mutations': [], 'limits': ['Properties may be spoofed by the active LK/root setup',
                                               'secinfo content not yet signature/binding validated',
                                               'No direct RPMB/efuse read, LK getter call or lock command',
                                               'Captures do not identify the friend device failure']}
    (HERE / 'live_readonly_snapshot.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report, indent=2))


if __name__ == '__main__':
    main()

"""Build the offline experiment. Never contacts or writes to an Android device."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import sys
import zipfile

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
LK_SHA = 'f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171'


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--module-builder', required=True, type=Path)
    args = parser.parse_args()
    source = ROOT / 'fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img'
    if sha(source) != LK_SHA:
        raise ValueError('Known-working LK hash mismatch')
    module = HERE / 'module'
    expected_prop = b'ro.vendor.radio.build_region=global\n'
    if (module / 'system.prop').read_bytes() != expected_prop:
        raise ValueError('Unexpected property override')
    for path in module.rglob('*'):
        if path.is_file() and b'\r' in path.read_bytes():
            raise ValueError(f'Non-LF file: {path}')
    for command in ('validate', 'pack'):
        argv = [sys.executable, str(args.module_builder), command, str(module)]
        if command == 'pack':
            argv += ['-o', str(HERE / 'dash_radio_region_global_test_v1.zip')]
        subprocess.run(argv, check=True)
    archive = HERE / 'dash_radio_region_global_test_v1.zip'
    with zipfile.ZipFile(archive) as z:
        if z.testzip() is not None:
            raise ValueError('ZIP CRC failed')
        expected = {p.relative_to(module).as_posix(): p.read_bytes()
                    for p in module.rglob('*') if p.is_file()}
        if set(z.namelist()) != set(expected):
            raise ValueError('ZIP has unexpected entries')
        for name, content in expected.items():
            if z.read(name) != content:
                raise ValueError(f'ZIP content mismatch: {name}')
    copied = HERE / 'lk_unlock_known_working.img'
    shutil.copyfile(source, copied)
    if sha(copied) != LK_SHA:
        raise ValueError('Copied LK differs')
    manifest = {
        'experiment': 'dash_radio_region_global_test_v1',
        'status': 'host-validated; not installed or tested for NR on device',
        'lk_changed': False,
        'lk_composition': 'H4 patched main LK + intact signed H3 BL2 + H4 AEE/DTBs',
        'property_override': {'ro.vendor.radio.build_region': {'from': 'cn', 'to': 'global'}},
        'target_value_proven_to_enable_nr': False,
        'sa_nsa_settings_changed': False,
        'persistent_properties_written': False,
        'device_flashed_or_module_installed': False,
        'checks': ['original and copy LK SHA256', 'module builder validation',
                   'LF source files', 'exact single property override',
                   'ZIP CRC and exact source contents'],
        'files': {p.name: {'size': p.stat().st_size, 'sha256': sha(p)}
                  for p in (copied, archive)},
    }
    (HERE / 'manifest.json').write_text(json.dumps(manifest, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(manifest, indent=2))


if __name__ == '__main__':
    main()

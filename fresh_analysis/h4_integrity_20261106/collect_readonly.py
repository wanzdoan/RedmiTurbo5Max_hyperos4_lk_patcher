"""Allowlisted ADB diagnostics. No su, installation, property write or reboot.

Writes a local report outside the repo. It does not request an Integrity token
or create attestation keys. Capture the checker result separately on screen.
"""
import argparse
import json
from pathlib import Path
import re
import subprocess

ROOT = Path(__file__).resolve().parents[2]
PROPS = (
    'ro.build.fingerprint', 'ro.vendor.build.fingerprint', 'ro.product.build.fingerprint',
    'ro.build.version.release', 'ro.build.version.sdk', 'ro.build.version.security_patch',
    'ro.vendor.build.security_patch', 'ro.product.first_api_level', 'ro.board.api_level',
    'ro.build.type', 'ro.build.tags', 'ro.boot.verifiedbootstate', 'ro.boot.vbmeta.device_state',
    'ro.boot.flash.locked', 'ro.boot.veritymode', 'ro.boot.vbmeta.digest',
    'ro.boot.vbmeta.hash_alg', 'ro.boot.vbmeta.size', 'ro.boot.hwc',
    'ro.product.mod_device', 'ro.miui.region', 'ro.crypto.state',
)


def adb(*args):
    result = subprocess.run(['adb', *args], capture_output=True, text=True,
                            encoding='utf-8', errors='replace', timeout=25)
    return result.returncode, result.stdout.strip(), result.stderr.strip()


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--output', type=Path, required=True)
    args = ap.parse_args()
    target = args.output.resolve()
    if target == ROOT or ROOT in target.parents:
        raise SystemExit('Keep device diagnostics outside the Git repository')
    if target.exists():
        raise SystemExit('Choose a new report path; existing reports are preserved')
    rc, state, _ = adb('get-state')
    if rc or state != 'device':
        raise SystemExit('Exactly one authorized ADB device is required; no report collected')
    props = {}
    for prop in PROPS:
        rc, value, _ = adb('shell', 'getprop', prop)
        props[prop] = dict(status='read' if rc == 0 else 'error', value=value if rc == 0 else None)
    packages = {}
    for name in ('com.android.vending', 'com.google.android.gms', 'com.google.android.gsf', 'com.android.rkpd'):
        rc, output, _ = adb('shell', 'dumpsys', 'package', name)
        packages[name] = dict(command_ok=rc == 0,
                              metadata=[line.strip() for line in output.splitlines()
                                        if re.match(r'\s*(versionCode=|versionName=|installerPackageName=)', line)])
    rc, output, _ = adb('shell', 'service', 'list')
    services = [line.strip() for line in output.splitlines()
                if re.search(r'keymint|keymaster|remotelyprovisioned|remoteprovision|keystore|rkpd', line, re.I)]
    report = dict(scope='Read-only ADB allowlist; Android props can differ from signed hardware attestation',
                  properties=props, package_metadata=packages, security_services=services,
                  services_command_ok=rc == 0,
                  missing=['Exact Play Integrity API Checker verdict/error', 'Real attestation chain and KeyMint/RKP error evidence'],
                  mutations_performed=False)
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(json.dumps(report, ensure_ascii=False, indent=2)+'\n', encoding='utf-8')
    print('Read-only report saved outside repo:', target)


if __name__ == '__main__':
    main()

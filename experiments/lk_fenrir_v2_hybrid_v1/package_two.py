"""Deterministic package of the two validated final hybrid images."""
import zipfile
from build_hybrid import *


def main():
    paths = []; entries = []
    for kind, cfg in BASES.items():
        folder = HERE / kind
        file = folder / cfg['name']; data = file.read_bytes()
        manifest = json.loads((folder / 'manifest.json').read_text())
        validation = json.loads((folder / 'validation.json').read_text())
        if sha(data) != manifest['output_sha256'] or sha(data) != validation['output_sha256']:
            raise ValueError('Final hash/report mismatch')
        if not validation['status'].startswith('PASS_'): raise ValueError('Validation not passing')
        entries.append(dict(variant=kind, artifact=cfg['name'], sha256=sha(data), bytes=len(data),
                            zip_path=kind + '/' + cfg['name'],
                            repository_path=file.relative_to(ROOT).as_posix(),
                            native_cpu_cases=validation['total_native_cpu_cases'],
                            device_boot_tested=False, root_success_hardware_confirmed=False))
        layout = json.loads((folder / 'layout_validation.json').read_text())
        if layout['artifact_sha256'] != sha(data): raise ValueError('Layout report hash mismatch')
        for p in (file, folder / 'manifest.json', folder / 'validation.json', folder / 'layout_validation.json'):
            paths.append((kind + '/' + p.name, p.read_bytes()))
    report = dict(status='TWO_EXPERIMENTAL_HYBRIDS_NOT_DEVICE_BOOTED', images=entries,
                  hwc_auto_region=True, mte_force_off_added=False,
                  common_locked_is_software_fake=True, post_load_maintenance_call_skipped=True,
                  no_agent_device_operations=True)
    release = json.dumps(report, indent=2) + '\n'
    (HERE / 'release_manifest.json').write_text(release)
    sums = ''.join(row['sha256'] + '  ' + row['zip_path'] + '\n' for row in entries)
    readme = ('Two separate hybrid LK images: H3 stock/Fenrir base and H3 debug base.\n'
              'Both: fake locked/green, AVB root-hash allowance, v2 public status, automatic HWC.\n'
              'MTE policy preserved. Only one conditional post-load version maintenance call skipped.\n'
              'No device flash, boot, root, OTA or Integrity confirmation for these new images.\n'
              'Root descriptor probes are synthetic; full phone boot remains unverified.\n'
              'Check SHA256SUMS.txt and per-image validation/manifest.\n'
              'Full docs: experiments/lk_fenrir_v2_hybrid_v1/README_VI.md in the private repository.\n')
    paths += [('release_manifest.json', release.encode()), ('SHA256SUMS.txt', sums.encode()),
              ('README.txt', readme.encode()), ('runtime_confirmation.json', (HERE / 'runtime_confirmation.json').read_bytes())]
    output = HERE / 'lk_fenrir_v2_hybrid_two_bases.zip'
    with zipfile.ZipFile(output, 'w', compression=zipfile.ZIP_DEFLATED) as z:
        for name, data in paths:
            info = zipfile.ZipInfo(name, date_time=(2026, 11, 5, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED; z.writestr(info, data)
    with zipfile.ZipFile(output) as z:
        if z.testzip() is not None: raise ValueError('Archive CRC failure')
        for row in entries:
            if sha(z.read(row['zip_path'])) != row['sha256']: raise ValueError('Archived image differs')
        if len([n for n in z.namelist() if n.endswith('.img')]) != 2: raise ValueError('Expected two images')
    print(output.name, sha(output.read_bytes()), output.stat().st_size, 'PASS')


if __name__ == '__main__': main()

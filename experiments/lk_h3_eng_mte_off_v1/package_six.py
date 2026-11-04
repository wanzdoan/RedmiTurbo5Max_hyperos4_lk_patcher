"""Bundle six exact files, without rebuilding or renaming previous artifacts."""
import zipfile
from build_mte_off import *


def main():
    release = []; paths = []
    for kind, (path, expected, off_name) in TARGETS.items():
        for state, file, digest in (
            ('DEFAULT_POLICY', ROOT / path, expected),
            ('MTE_OFF', HERE / kind / off_name,
             json.loads((HERE / kind / 'manifest.json').read_text())['output_sha256'])):
            data = file.read_bytes()
            if sha(data) != digest or len(data) != 4043792:
                raise ValueError('Hash/size mismatch: ' + str(file))
            zip_name = kind + '/' + file.name
            release.append(dict(variant=kind, mte=state, artifact=file.name,
                                repository_path=file.relative_to(ROOT).as_posix(),
                                zip_path=zip_name, sha256=digest, bytes=len(data),
                                all_region_hwc=True, device_boot_tested=False))
            paths.append((zip_name, data))
    manifest = dict(status='SIX_EXPERIMENTAL_FILES_NOT_DEVICE_BOOTED', files=release,
                    all_region_scope='countrycode cn -> CN, nonempty non-cn -> GLOBAL, unavailable -> hardware HWC',
                    mte_off_scope='Existing LK arm64.nomte suppression path; does not undo preloader execution',
                    device_flashed=False)
    manifest_text = json.dumps(manifest, indent=2) + '\n'
    (HERE / 'release_manifest.json').write_text(manifest_text)
    sums = ''.join(row['sha256'] + '  ' + row['zip_path'] + '\n' for row in release)
    readme = ('Six separate LK files: unlocked, fakelock v2, stock-policy; all use automatic HWC region.\n'
              'Each directory has DEFAULT_POLICY (original filename) and MTE_OFF (filename suffix).\n'
              'DEFAULT_POLICY does not force MTE on; it preserves the existing policy.\n'
              'MTE_OFF forces the existing LK suppression path emitting arm64.nomte to the kernel.\n'
              'It does not retroactively disable earlier preloader execution. No device boot test yet.\n'
              'HWC routes cn to CN, other nonempty countrycodes to GLOBAL, missing/error to hardware HWC.\n'
              'HWC mapping does not prove every ROM/signing key/rollback/model is compatible.\n'
              'All files: 4043792 bytes. Check SHA256SUMS.txt before comparing files.\n'
              'Full documentation: experiments/lk_h3_eng_mte_off_v1/README_VI.md in the private repository.\n')
    output = HERE / 'lk_eng_all_region_6_files.zip'
    # Fixed ZIP timestamps make the package deterministic.
    with zipfile.ZipFile(output, 'w', compression=zipfile.ZIP_DEFLATED) as archive:
        for name, data in paths + [('release_manifest.json', manifest_text.encode()),
                                   ('SHA256SUMS.txt', sums.encode()), ('README.txt', readme.encode())]:
            info = zipfile.ZipInfo(name, date_time=(2026, 11, 5, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            archive.writestr(info, data)
    with zipfile.ZipFile(output) as archive:
        if archive.testzip() is not None:
            raise ValueError('Bad archive CRC')
        for row in release:
            if sha(archive.read(row['zip_path'])) != row['sha256']:
                raise ValueError('Archived hash differs')
        if len([n for n in archive.namelist() if n.endswith('.img')]) != 6:
            raise ValueError('Expected six images')
    print(output.name, sha(output.read_bytes()), output.stat().st_size, 'six images PASS')


if __name__ == '__main__':
    main()

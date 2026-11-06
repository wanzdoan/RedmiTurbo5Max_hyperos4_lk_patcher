import zipfile
from build_cn_only import *


def main():
    entries = []; files = []
    for kind, name in NAMES.items():
        p = HERE / name; data = p.read_bytes()
        manifest = json.loads((HERE / (kind + '_manifest.json')).read_text())
        validation = json.loads((HERE / (kind + '_validation.json')).read_text())
        if sha(data) != manifest['output_sha256'] or sha(data) != validation['output_sha256']:
            raise ValueError('Image/report mismatch')
        entries.append(dict(variant=kind, artifact=name, sha256=sha(data), bytes=len(data),
                            native_cpu_cases=validation['total_native_cpu_cases'], device_boot_tested=False))
        files += [(name, data), (kind + '_manifest.json', (HERE / (kind + '_manifest.json')).read_bytes()),
                  (kind + '_validation.json', (HERE / (kind + '_validation.json')).read_bytes())]
    report = json.dumps(dict(status='TWO_CHINA_STOCK_POLICY_IMAGES_NOT_DEVICE_BOOTED', input_sha256=INPUT_SHA,
                             images=entries, hwc='Unchanged original hardware HWC', global_key_added=False), indent=2) + '\n'
    (HERE / 'release_manifest.json').write_text(report)
    sums = ''.join(row['sha256'] + '  ' + row['artifact'] + '\n' for row in entries)
    readme = ('Original ENG stock policy + known China boot key; original ENG key retained.\n'
              'No Global key added; no Global or automatic HWC patch.\n'
              'Original HWC is CN on China hardware. No force unlock or fakelock.\n'
              'DEFAULT preserves MTE policy; MTE_OFF requests kernel suppression via arm64.nomte.\n'
              'Not device boot tested; root/relock/OTA/Integrity not established.\n'
              'Full docs in experiments/lk_eng_stock_policy_cn_only_v1/README_VI.md.\n')
    files += [('release_manifest.json', report.encode()), ('SHA256SUMS.txt', sums.encode()), ('README.txt', readme.encode())]
    output = HERE / 'lk_eng_stock_policy_cn_only_two_files.zip'
    with zipfile.ZipFile(output, 'w', compression=zipfile.ZIP_DEFLATED) as z:
        for name, data in files:
            item = zipfile.ZipInfo(name, date_time=(2026, 11, 6, 0, 0, 0)); item.compress_type = zipfile.ZIP_DEFLATED
            z.writestr(item, data)
    with zipfile.ZipFile(output) as z:
        if z.testzip() is not None: raise ValueError('CRC failure')
        for row in entries:
            if sha(z.read(row['artifact'])) != row['sha256']: raise ValueError('ZIP image differs')
    print(output.name, sha(output.read_bytes()), output.stat().st_size, 'PASS')


if __name__ == '__main__': main()

"""Host-only syntax and install-guard checks using mocked Android commands."""
import os
from pathlib import Path
import subprocess
import sys
import tempfile

HERE = Path(__file__).resolve().parent
BASH = sys.argv[1] if len(sys.argv) > 1 else 'bash'


def main():
    scripts = [HERE / 'module/customize.sh',
               HERE / 'module/META-INF/com/google/android/update-binary']
    for path in scripts:
        subprocess.run([BASH, '-n', path.as_posix()], check=True)
    harness = '''
abort() { echo "abort: $*"; exit 42; }
getprop() {
  case "$1" in
    ro.product.device) echo "$TEST_DEVICE" ;;
    ro.build.version.incremental) echo "$TEST_ROM" ;;
    ro.vendor.radio.build_region) echo "$TEST_REGION" ;;
    *) exit 99 ;;
  esac
}
ui_print() { :; }
set_perm_recursive() { :; }
set_perm() { :; }
. "$TEST_CUSTOMIZE"
'''
    cases = [({}, 0), ({'BOOTMODE': 'false'}, 42),
             ({'TEST_DEVICE': 'other'}, 42),
             ({'TEST_ROM': 'other'}, 42),
             ({'TEST_REGION': 'global'}, 42)]
    with tempfile.TemporaryDirectory(prefix='dash-region-guards-') as temp:
        root = Path(temp)
        script = root / 'harness.sh'
        script.write_text(harness, encoding='utf-8', newline='\n')
        for i, (overrides, code) in enumerate(cases):
            modpath = root / f'case{i}'
            modpath.mkdir()
            env = dict(os.environ, BOOTMODE='true', TEST_DEVICE='dash',
                       TEST_ROM='OS4.0.3.0.XPLCNXM', TEST_REGION='cn',
                       TEST_CUSTOMIZE=scripts[0].as_posix(), MODPATH=modpath.as_posix())
            env.update(overrides)
            result = subprocess.run([BASH, script.as_posix()], env=env,
                                    capture_output=True, text=True)
            if result.returncode != code:
                raise RuntimeError(f'case {i}: {result.returncode}: {result.stderr}')
            backup = modpath / 'original_radio_region.txt'
            if code == 0:
                if backup.read_bytes() != b'cn\n':
                    raise ValueError('Incorrect backup')
            elif backup.exists():
                raise ValueError('Rejected case wrote a backup')
    print('PASS: 2 shell syntax checks; 5 mocked installation/guard cases; no ADB used.')


if __name__ == '__main__':
    main()

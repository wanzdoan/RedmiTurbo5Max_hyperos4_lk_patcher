"""Filter previously captured LK log. No device I/O or unique values emitted."""
from pathlib import Path
import hashlib
import json
import re

HERE = Path(__file__).resolve().parent


def main():
    snapshot = json.loads((HERE / 'live_readonly_snapshot.json').read_text())
    private = Path(snapshot['private_raw_directory'])
    data = (private / 'pl_lk.bin').read_bytes()
    text = data.decode('utf-8', errors='replace')
    wanted = re.compile(r'mi_get_lock_state|mi_check_magic|mi_get_sig|sig_len|lock_state|Is_Secure_ID|'
                        r'no need to verify secure id|sbc_status|\[AVB\].*(?:sbc_enabled|efuse sku|sku match|ep check)|'
                        r'RPMB.*(?:provision|fail)|mi_set_sig|token.*(?:mismatch|failed)', re.I)
    selected = {}
    for i, line in enumerate(text.splitlines(), 1):
        if not wanted.search(line) or any(ord(c) > 126 for c in line):
            continue
        if re.search(r'cpuid|ufsid|socid|imei|imsi|iccid|serial|nonce', line, re.I):
            continue
        line = re.sub(r'(?i)(?:0x)?[0-9a-f]{16,}', '[hex redacted]', line.strip())
        line = re.sub(r'\b\d{9,}\b', '[long number redacted]', line)
        if len(line) > 180:
            continue
        row = selected.setdefault(line, {'text': line, 'count': 0, 'first_line': i, 'last_line': i})
        row['count'] += 1; row['last_line'] = i
    report = {'source': '/proc/pl_lk', 'captured_bytes': len(data), 'raw_sha256': hashlib.sha256(data).hexdigest(),
              'selected_status_lines_deduplicated': list(selected.values()),
              'main_getter_state3_success_count': text.count('custom_get_lock_state return = 0, lock_state = 3'),
              'signature_length256_count': text.count('mi_get_lock_state sig_len = 256'),
              'secure_id_skip_observed': 'no need to verify secure id. countinue boot.' in text,
              'mi_set_sig_log_observed': 'mi_set_sig' in text,
              'raw_stored_outside_git': True,
              'limits': ['Current phone boot only; default state4/error4 lines before main are not its final lock state',
                         'No identified complete token signature or primary RPMB Secure ID exported',
                         'No hardware write or lock command was executed by this research']}
    (HERE / 'live_boot_log_selected.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('main_getter_state3_success_count', 'signature_length256_count',
                                           'secure_id_skip_observed', 'mi_set_sig_log_observed')}, indent=2))


if __name__ == '__main__':
    main()

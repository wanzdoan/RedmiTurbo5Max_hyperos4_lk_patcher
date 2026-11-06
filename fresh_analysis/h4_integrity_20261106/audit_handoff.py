"""Read-only native handoff audit; never a Google/TEE attestation test."""
from pathlib import Path
import gc
import hashlib
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis/h4_recovery_v3_20261106'))
from slot_replay import Replay, B, ROM

POLICY = ROOT / 'experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img'
STOCK = ROOT / 'lk_hyperos4_original/lk.img'
EXPECTED = '6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52'


def sha(data):
    return hashlib.sha256(data).hexdigest()


def main():
    policy, stock = POLICY.read_bytes(), STOCK.read_bytes()
    assert sha(policy) == EXPECTED
    # Complete sender, including all five secure-monitor packet calls.
    lo, hi = 0xc4ce4, 0xc4edc
    assert policy[512+lo:512+hi] == stock[512+lo:512+hi]
    cases = []
    # These are synthetic metadata fixtures, NOT a supplied H3 ROM comparison.
    for version in (160000, 170000):
        for lock in (3, 4):
            replay = Replay(policy, 'h4', state=lock)
            metadata = (version, 202610, 20261001, 20261001)
            digest = bytes(range(32))
            replay.u.mem_write(B+0x20d990, struct.pack('<IIII', *metadata))
            replay.u.mem_write(B+0x20d9a0, digest)
            result = replay.handoff()
            packets = result['packets']
            assert packets[1][1:5] == [int(lock != 3), 2 if lock == 3 else 0, version, 202610]
            assert packets[2][1:5] == list(struct.unpack('<QQQQ', digest))
            assert packets[3][1:3] == [20261001, 20261001]
            cases.append(dict(os_version_fixture=version, stored_state_fixture=lock,
                              native_handoff=result))
            del replay
            gc.collect()
    headers = []
    for name in ('boot.img', 'init_boot.img'):
        data = (ROM/name).read_bytes()
        assert data[:8] == b'ANDROID!'
        packed, header_version = struct.unpack_from('<I', data, 16)[0], struct.unpack_from('<I', data, 40)[0]
        os_bits = packed >> 11
        headers.append(dict(file=name, sha256=sha(data), header_version=header_version,
                            os_version_raw=packed,
                            os_version=[(os_bits >> 14) & 127, (os_bits >> 7) & 127, os_bits & 127],
                            patch_year=2000+((packed >> 4) & 127) if packed else None,
                            patch_month=packed & 15 if packed else None,
                            note='Zero version fields in modern headers are not an attestation failure diagnosis'))
    report = dict(status='PASS_BOUNDED_NATIVE_PACKET_AUDIT_NOT_INTEGRITY_TEST',
                  policy_sha256=sha(policy), stock_sha256=sha(stock),
                  sender_payload_range=[hex(lo), hex(hi)], sender_exact_stock=True,
                  cases=cases, supplied_h4_headers=headers,
                  observations=['Native sender forwards both synthetic OS versions without clamping or rejection',
                                'Stored-state fixture changes lock/boot-state packet fields as expected',
                                'This sender is unchanged from stock H4; no regression found in the tested packet assembly'],
                  limits=['Only packet assembly/software SHA runs natively; key extraction, stored-state getter, boot-state and secure-monitor response are fixtures',
                          'No actual certificate chain, KeyMint/RKP, Google verdict or phone readback',
                          'Android16/17 fixture cases do not execute real H3/H4 ROMs or their trusted worlds',
                          'Earlier slot replay also injects OEM key and boot state; it cannot establish authentic attestation',
                          'No firmware patch justified by these results alone'])
    (HERE/'handoff_audit.json').write_text(json.dumps(report, indent=2)+'\n', encoding='utf-8')
    print(report['status'], len(cases), 'cases; firmware unchanged')


if __name__ == '__main__':
    main()

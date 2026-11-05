"""Two independent Fenrir/v2 hybrids; never flashes or changes old artifacts."""
from pathlib import Path
import importlib.util
import json
import struct
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
sys.path.insert(0, str(ROOT / 'fresh_analysis'))
from build_candidate import prefix, sha

BASES = {
    'stock_h3': dict(path='fresh_analysis/fenrir_review/input/dash-fenrir-versioned.bin',
                     sha256='dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113',
                     name='lk_H3_STOCK_FENRIR_V2_HYBRID_AUTO_HWC_TEST.img',
                     oem=0xc868, lks=0xe8bc4, empty=0xdf295, info=0x9c60, okay=0x9d50,
                     report=0x692ac, green=0xd1c87, append=0x18f34, seccfg=0xc930,
                     common=0x88750, avb_flag=0x68668, setter=0x692fc, cache=0x121990,
                     maintenance_call=0x688b8, maintenance_original='c6010094',
                     prefix_length=90),
    'debug_h3': dict(path='experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img',
                     sha256='6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d',
                     name='lk_H3_DEBUG_FENRIR_V2_HYBRID_AUTO_HWC_TEST.img',
                     oem=0xc770, lks=0xe159e, empty=0xd788e, info=0x9cfc, okay=0x9dec,
                     report=0x69da4, green=0xc9ce4, append=0x18ab8, seccfg=0xc838,
                     common=0x88c18, avb_flag=0x69438, setter=0x69df4, cache=0x119968,
                     maintenance_call=0x69688, maintenance_original='05010094',
                     prefix_length=180),
}


def members(data):
    rows = []; offset = 0
    for i in range(15):
        if data[offset:offset + 4] != bytes.fromhex('88168858'):
            raise ValueError('Invalid member at ' + hex(offset))
        size = struct.unpack_from('<I', data, offset + 4)[0]
        end = (offset + 512 + size + 15) & ~15
        if end > len(data): raise ValueError('Member overrun')
        rows.append(dict(offset=offset, size=size, end=end,
                         name=data[offset + 8:offset + 40].split(b'\0')[0].decode()))
        offset = end
    tail = data[offset:]
    if any(tail): raise ValueError('Nonzero undeclared trailer')
    return rows, tail


def word(x): return struct.pack('<I', x)


def branch(at, to, link=False):
    d = to - at
    if d % 4 or not -(1 << 27) <= d < (1 << 27): raise ValueError('Branch out of range')
    return word((0x94000000 if link else 0x14000000) | ((d // 4) & 0x3ffffff))


def adr(at, to, reg=0):
    d = to - at
    if not -(1 << 20) <= d < (1 << 20): raise ValueError('ADR out of range')
    return word(0x10000000 | ((d & 3) << 29) | (((d >> 2) & 0x7ffff) << 5) | reg)


def cbnz(at, to, reg=0):
    d = to - at
    if d % 4 or not -(1 << 20) <= d < (1 << 20): raise ValueError('CBNZ out of range')
    return word(0x35000000 | (((d // 4) & 0x7ffff) << 5) | reg)


def public_blocks(config):
    o = config['oem']; literal_at = o + 32
    code = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(o + 8, config['lks'])
            + branch(o + 12, config['info'], True) + adr(o + 16, config['empty'])
            + bytes.fromhex('fd7bc1a8bf2303d5') + branch(o + 28, config['okay']))
    literal = b'androidboot.flash.locked=1\0'
    oem = code + literal + b'\0' + bytes.fromhex('1f2003d5') * 20
    if len(oem) != 140: raise ValueError('OEM replacement size')
    r = config['report']
    report = (bytes.fromhex('3f2303d5fd7bbfa9') + adr(r + 8, config['green'])
              + branch(r + 12, config['append'], True) + cbnz(r + 16, r + 28)
              + adr(r + 20, literal_at) + branch(r + 24, config['append'], True)
              + bytes.fromhex('fd7bc1a8bf2303d5c0035fd6') + bytes.fromhex('1f2003d5') * 3)
    if len(report) != 52: raise ValueError('Android report replacement size')
    return [(o, oem, 'Compact public LKS1 reply and explicit flash.locked=1 literal'),
            (r, report, 'V2 green/flash.locked bootargs using original append ABI'),
            (config['seccfg'], bytes.fromhex('23008052'), 'V2 public seccfg reply1; no stored-state write')]


def hwc_module():
    path = HERE / 'stock_hwc' / 'hwc_patch.py'
    spec = importlib.util.spec_from_file_location('hybrid_hwc', path)
    module = importlib.util.module_from_spec(spec); spec.loader.exec_module(module)
    return module


def build(kind, source):
    cfg = BASES[kind]
    if sha(source) != cfg['sha256']: raise ValueError('Wrong exact base for ' + kind)
    rows, tail = members(source)
    expected = ['lk', 'cert1', 'cert2', 'bl2_ext', 'cert1', 'cert2', 'aee', 'cert1', 'cert2',
                'lk_main_dtb', 'cert1', 'cert2', 'lk_dtbo', 'cert1', 'cert2']
    if [r['name'] for r in rows] != expected: raise ValueError('Unexpected component layout')
    if len(tail) != (768 if kind == 'stock_h3' else 0): raise ValueError('Unexpected source trailer')
    main = bytearray(source[512:512 + rows[0]['size']]); allowed = set(); changes = []

    def patch(off, code, reason, old=None):
        if old is not None and main[off:off + len(old)] != old: raise ValueError('Original instruction differs at ' + hex(off))
        if off + len(code) > len(main) or any(i in allowed for i in range(off, off + len(code))):
            raise ValueError('Overlapping or out-of-range main patch')
        changes.append(dict(payload_offset=hex(off), old=bytes(main[off:off + len(code)]).hex(),
                            new=code.hex(), bytes=len(code), purpose=reason))
        main[off:off + len(code)] = code; allowed.update(range(off, off + len(code)))

    if kind == 'debug_h3':
        patch(cfg['common'], bytes.fromhex('88008052080000b900008052c0035fd61f2003d51f2003d5'),
              'Fenrir-style common lock wrapper returns locked4/success0, backing getter unchanged')
        patch(cfg['avb_flag'], bytes.fromhex('36008052'), 'Allow verification-error flag for modified init_boot path', bytes.fromhex('f6179f1a'))
        patch(cfg['setter'], bytes.fromhex('1f6909b9'), 'Fenrir-style RAM boot-state setter0', bytes.fromhex('006909b9'))
    else:
        if main[cfg['common']:cfg['common'] + 16] != bytes.fromhex('88008052080000b900008052c0035fd6'):
            raise ValueError('Fenrir base common lock patch missing')
        if main[cfg['avb_flag']:cfg['avb_flag'] + 4] != bytes.fromhex('36008052'):
            raise ValueError('Fenrir base AVB allow-error flag missing')
        if main[cfg['setter']:cfg['setter'] + 4] != bytes.fromhex('1f9109b9'):
            raise ValueError('Fenrir base zero state setter missing')
    patch(cfg['maintenance_call'], bytes.fromhex('1f2003d5'),
          'Preserve v2 unlocked-style skip of this post-load rollback/OTP maintenance call despite fake locked4',
          bytes.fromhex(cfg['maintenance_original']))
    for off, code, purpose in public_blocks(cfg): patch(off, code, purpose)
    hwc = hwc_module()
    hwc_kind = 'stock' if kind == 'stock_h3' else 'debug'
    for off, code, purpose in hwc.patches(source, hwc_kind): patch(off, code, purpose)
    original_main = source[512:512 + rows[0]['size']]
    if any(a != b and i not in allowed for i, (a, b) in enumerate(zip(original_main, main))):
        raise ValueError('Undeclared main modification')
    # All binary families keep their own BL2/AEE/DTB components. Fenrir source
    # already contains its AEE gates; debug builds omit the equivalent gates.
    blocks = [source[r['offset']:r['end']] for r in rows]
    blocks[0] = source[:512] + bytes(main) + source[512 + rows[0]['size']:rows[0]['end']]
    cert = rows[2]; c = cert['offset'] + 512
    old_prefix = 90 if kind == 'stock_h3' else 0
    cert_payload = source[c:c + cert['size']]
    der = cert_payload[old_prefix:]
    if not der.startswith(bytes.fromhex('3082')): raise ValueError('Original DER not found')
    if old_prefix and cert_payload[:old_prefix] != prefix(source[:512], original_main)[:old_prefix]:
        raise ValueError('Existing Fenrir prefix mismatch')
    new_prefix = prefix(source[:512], bytes(main))[:cfg['prefix_length']]
    new_payload = new_prefix + der
    header = bytearray(source[cert['offset']:cert['offset'] + 512]); struct.pack_into('<I', header, 4, len(new_payload))
    record = bytes(header) + new_payload; record += bytes((-len(record)) % 16)
    blocks[2] = record
    output = b''.join(blocks) + tail
    new_rows, new_tail = members(output)
    for i in range(15):
        if i not in (0, 2) and blocks[i] != source[rows[i]['offset']:rows[i]['end']]:
            raise ValueError('Other component changed')
    if new_tail != tail: raise ValueError('Trailer changed')
    manifest = dict(status='EXPERIMENTAL_HYBRID_NOT_DEVICE_BOOTED', variant=kind, artifact=cfg['name'],
                    input=cfg['path'], input_sha256=cfg['sha256'], output_sha256=sha(output), bytes=len(output),
                    features=['Fenrir common locked4 and RAM boot-state0', 'AVB allow-error flag for root-modified init_boot path',
                              'Public LKS1, explicit flash.locked1 and seccfg reply1 from v2', 'Automatic countrycode HWC'],
                    inherited_fenrir_main_and_aee_gates=(kind == 'stock_h3'),
                    debug_equivalent_gates_already_omitted=(kind == 'debug_h3'),
                    original_backing_lock_getter_unchanged=True, no_persistent_lock_write=True,
                    post_load_rollback_maintenance_call_skipped=True,
                    global_otp_and_version_helpers_unchanged=True,
                    own_bl2_aee_dtb_certificates_unchanged=True, mte_force_off_added=False,
                    main_certificate_prefix_bytes=cfg['prefix_length'], patches=changes,
                    root_boot_hardware_verified=False, device_flashed=False,
                    limits=['Native AVB probes cover hash mismatch allowance, not actual Magisk/KernelSU/device boot',
                            'Forced green is software fakelock and can hide warning states; not actual verified boot or lock',
                            'No claim OTA, attestation, bank apps or stock H4 preloader compatibility',
                            'Non-CN countrycodes map to GLOBAL; HWC mapping does not prove every ROM compatible'])
    return output, manifest


def main():
    for kind, cfg in BASES.items():
        out, report = build(kind, (ROOT / cfg['path']).read_bytes())
        directory = HERE / kind; directory.mkdir(exist_ok=True)
        (directory / cfg['name']).write_bytes(out)
        (directory / 'manifest.json').write_text(json.dumps(report, indent=2) + '\n')
        print(kind, report['output_sha256'], len(out))


if __name__ == '__main__': main()

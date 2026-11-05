"""Native HWC/countrycode flow for both layouts and arbitrary final images."""
import itertools
import json
import struct
import sys
from hwc_patch import *
from unicorn import UC_HOOK_CODE
from unicorn.arm64_const import *

sys.path.insert(0, str(ROOT / 'fresh_analysis/fenrir_review/main_analysis'))
from probe_main import machine, cstr, B, DATA, STOP


def flow_case(kind, image, country, hardware='CN', level='MP', version='1.0', suffix='_a',
              mode='ok', fmt_fail=None, automatic=True, change_cache_after_early=False):
    cfg = CONFIG[kind]
    u, saved = machine(image)
    sp = u.reg_read(UC_ARM64_REG_SP)
    u.reg_write(UC_ARM64_REG_X29, sp + cfg['frame'])
    preserved = {r: u.reg_read(r) for r in saved if r not in (UC_ARM64_REG_X19, UC_ARM64_REG_X20)}
    hw = cfg['hw']; output = cfg['output']
    record = bytearray(b'\xa5' * 0x54)
    for offset, value in ((0, level), (16, version), (36, hardware)):
        record[offset:offset + 16] = value.encode().ljust(16, b'\0')
    u.mem_write(B + cfg['cache'], bytes(record))
    u.mem_write(sp + hw - 4, b'LEFT'); u.mem_write(sp + hw + 0x54, b'RGHT')
    u.mem_write(DATA + 0x100, suffix.encode() + b'\0')
    region = country.encode()[:16].ljust(16, b'\0')
    appended, names, reads, closes, hw_calls, errors = [], [], [], [], [], []
    phase = ['early']; end = [cfg['early_end']]; stopped = []

    def ret(value=0):
        u.reg_write(UC_ARM64_REG_X0, value & ((1 << 64) - 1))
        u.reg_write(UC_ARM64_REG_PC, u.reg_read(UC_ARM64_REG_LR))

    def hook(uc, address, size, _):
        off = address - B
        if off == end[0]:
            stopped.append(off); uc.emu_stop()
        elif off == cfg['getter']:
            hw_calls.append(phase[0])  # Real native cache getter and memcpy execute.
        elif off == cfg['slot']:
            ret(DATA + 0x100)
        elif off == cfg['snprintf']:
            fmt = cstr(uc, uc.reg_read(UC_ARM64_REG_X2))
            if fmt == 'countrycode%s':
                stage = 'name'; text = fmt % cstr(uc, uc.reg_read(UC_ARM64_REG_X3))
            elif fmt == 'androidboot.ptcountrycode=%s':
                stage = 'country'; text = fmt % cstr(uc, uc.reg_read(UC_ARM64_REG_X3))
            elif fmt == 'androidboot.hwc=%s androidboot.hwlevel=%s androidboot.hwversion=%s':
                stage = 'hwc'
                text = fmt % tuple(cstr(uc, uc.reg_read(r)) for r in
                                   (UC_ARM64_REG_X3, UC_ARM64_REG_X4, UC_ARM64_REG_X5))
                assert uc.reg_read(UC_ARM64_REG_X4) == sp + hw
                assert uc.reg_read(UC_ARM64_REG_X5) == sp + hw + 16
            else:
                raise AssertionError(('Unexpected formatter', fmt))
            if stage == fmt_fail:
                ret(-1); return
            limit = uc.reg_read(UC_ARM64_REG_W1)
            uc.mem_write(uc.reg_read(UC_ARM64_REG_X0), text.encode()[:limit - 1] + b'\0')
            ret(len(text))
        elif off == cfg['lookup']:
            name = cstr(uc, uc.reg_read(UC_ARM64_REG_X0)); names.append(name)
            assert name == 'countrycode' + suffix
            ret(0 if mode == 'missing' else DATA + 0x1000)
        elif off == cfg['read']:
            args = [uc.reg_read(r) for r in
                    (UC_ARM64_REG_X0, UC_ARM64_REG_X1, UC_ARM64_REG_X2, UC_ARM64_REG_X3)]
            assert args == [DATA + 0x1000, sp, 0, 16], args
            reads.append(args)
            uc.mem_write(sp, bytes(16) if mode == 'empty' else region)
            ret(7 if mode == 'short' else -1 if mode == 'io_error' else 16)
        elif off == cfg['close']:
            closes.append(uc.reg_read(UC_ARM64_REG_X0)); ret()
        elif off == cfg['append']:
            appended.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret()
        elif off == cfg['log']:
            errors.append(cstr(uc, uc.reg_read(UC_ARM64_REG_X0))); ret()
        elif int.from_bytes(uc.mem_read(address, 4), 'little') in (0xd503233f, 0xd50323bf):
            uc.reg_write(UC_ARM64_REG_PC, address + 4)

    u.hook_add(UC_HOOK_CODE, hook)

    def run(entry, stop):
        end[0] = stop; stopped.clear()
        try:
            u.emu_start(B + entry, STOP, count=50000)
        except Exception as error:
            raise RuntimeError(f'{error};PC={u.reg_read(UC_ARM64_REG_PC):x}') from error
        assert stopped == [stop] and u.reg_read(UC_ARM64_REG_SP) == sp

    run(cfg['base'], cfg['early_end'])
    early_count = len(appended)
    # Native stock code has intervening level comparisons before countrycode;
    # prove that the original copy already exists at that point, not only late.
    assert hw_calls == ['early']
    assert bytes(u.mem_read(sp + hw, 0x54)) == bytes(record)
    assert cstr(u, sp + hw) == level and cstr(u, sp + hw + 16) == version
    if kind == 'stock':
        assert bytes(u.mem_read(sp + cfg['frame'] + 0x1c, 4)) == bytes(4)
    # This negative/control fixture distinguishes early snapshot preservation
    # from a late re-read; the fixture changes cache in emulator RAM only.
    expected_cache = bytes(record)
    if change_cache_after_early:
        expected_cache = bytes([0x5a]) * 0x54
        u.mem_write(B + cfg['cache'], expected_cache)
    phase[0] = 'countrycode and late HWC'
    run(cfg['country'], cfg['continuation'])
    valid = mode == 'ok' and fmt_fail != 'name' and bool(country)
    value = country[:15] if valid else ''
    chosen = ('CN' if value.lower() == 'cn' else 'GLOBAL') if value else hardware
    expected_hwc = chosen if automatic else hardware
    outputs = [s for s in appended if s.startswith('androidboot.hwc=')]
    expected = f'androidboot.hwc={expected_hwc} androidboot.hwlevel={level} androidboot.hwversion={version}'
    assert outputs == ([expected] if fmt_fail != 'hwc' else []), (outputs, expected)
    assert hw_calls == ['early'], hw_calls
    if automatic:
        assert early_count == 0
    expected_lookup = fmt_fail != 'name'
    expected_read = expected_lookup and mode != 'missing'
    assert len(names) == int(expected_lookup)
    assert len(reads) == len(closes) == int(expected_read)
    country_out = [s for s in appended if s.startswith('androidboot.ptcountrycode=')]
    assert country_out == ([f'androidboot.ptcountrycode={country[:15]}']
                           if valid and fmt_fail != 'country' else [])
    if automatic and mode in ('short', 'io_error') and fmt_fail != 'name':
        assert bytes(u.mem_read(sp, 16)) == bytes(16)
    assert bytes(u.mem_read(B + cfg['cache'], 0x54)) == expected_cache
    assert bytes(u.mem_read(sp + hw, 0x54)) == bytes(record)
    assert bytes(u.mem_read(sp + hw - 4, 4)) == b'LEFT'
    assert bytes(u.mem_read(sp + hw + 0x54, 4)) == b'RGHT'
    assert u.reg_read(UC_ARM64_REG_X19) == sp + hw + 16
    assert u.reg_read(UC_ARM64_REG_X0) == sp + output
    assert all(u.reg_read(r) == value for r, value in preserved.items())
    if fmt_fail == 'hwc':
        assert any('snprintf' in text for text in errors)
    return dict(kind=kind, automatic=automatic, country=country, hardware=hardware,
                level=level, version=version, slot=suffix, read_mode=mode,
                formatter_failure=fmt_fail, early_hardware_snapshot_preserved=True,
                cache_changed_fixture=change_cache_after_early, outgoing_hwc=outputs,
                outgoing_countrycode=country_out, native_getter_calls=hw_calls, pass_case=True)


def cases_for(kind, image, automatic=True):
    rows = []
    countries = ('cn', 'CN', 'Cn', 'cN', 'global', 'GLOBAL', 'eea', 'EEA', 'tw', 'TW',
                 'taiwan', 'eu', 'native', 'unknown', 'cn_extra', '', 'c', 'cnx', 'cna', ' cn', 'cn ')
    for country, hw, slot in itertools.product(countries, ('CN', 'GLOBAL'), ('_a', '_b')):
        rows.append(flow_case(kind, image, country, hw, suffix=slot, automatic=automatic))
    for country, level, version in itertools.product(('cn', 'global', 'eea', 'tw'), ('MP', 'EVT'), ('1.0', 'V2.3')):
        rows.append(flow_case(kind, image, country, level=level, version=version, automatic=automatic))
    for country, hw, slot, mode in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('_a', '_b'),
                                                   ('missing', 'empty', 'short', 'io_error')):
        rows.append(flow_case(kind, image, country, hw, suffix=slot, mode=mode, automatic=automatic))
    for country, hw, stage in itertools.product(('cn', 'global'), ('CN', 'GLOBAL'), ('name', 'country', 'hwc')):
        rows.append(flow_case(kind, image, country, hw, fmt_fail=stage, automatic=automatic))
    for country in ('cn', 'global', ''):
        rows.append(flow_case(kind, image, country, automatic=automatic, change_cache_after_early=True))
    return rows


def main():
    report = dict(status='PASS_BOUNDED_HWC_NOT_DEVICE_BOOT', variants={})
    for kind, cfg in CONFIG.items():
        source = (ROOT / cfg['reference']).read_bytes()
        candidate = bytearray(source)
        for offset, code, _ in patches(source, kind):
            candidate[512 + offset:512 + offset + len(code)] = code
        original = cases_for(kind, source, False)
        patched = cases_for(kind, bytes(candidate), True)
        report['variants'][kind] = dict(original_cases=len(original), patched_cases=len(patched),
            assembly_sha256=hashlib.sha256(assemble(kind)).hexdigest(),
            external_direct_branch_check=check_external_branches(source, kind),
            cases=original + patched)
        print(kind, len(original), 'original and', len(patched), 'patched native cases')
    report['limits'] = ['Unrelated middle bootargs skipped; actual early snapshot exists before their reads',
                        'Partition/slot/formatter/log/append boundaries and PAC modeled',
                        'Native hardware getter/memcpy, strlen/memset and branch code execute',
                        'Candidate is emulator-only code view; certificate prefix rebuilding belongs to parent builder',
                        'No flash, phone, Android property or full-ROM compatibility proof']
    (HERE / 'validation.json').write_text(json.dumps(report, indent=2) + '\n')


if __name__ == '__main__':
    main()

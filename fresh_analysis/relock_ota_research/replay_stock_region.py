"""Replay stock H3 region gates using the previously verified H3 boundary model.

Input is immutable stock H3, not the region-patched experiment. No device I/O.
"""
from pathlib import Path
import hashlib
import json
import struct
import sys

ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT / 'experiments/lk_h3_global_region_v1'))
from validate_h3_region_lk import Replay, DATA, UC_ARM64_REG_X1, UC_ARM64_REG_X5


def main():
    stock = (ROOT / 'lk_hyperos3_original/lk.img').read_bytes()
    expected = 'e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb'
    if hashlib.sha256(stock).hexdigest() != expected:
        raise ValueError('Stock H3 input identity changed')
    sku_cases, key_cases = [], []
    for sb in (0, 14):
        for hw in (0, 1):
            for sku in ('CN', 'GL', 'NATIVE'):
                r = Replay(stock, sb=sb, hw=hw, sku=sku, block=True)
                got = r.run(0x68c88, False)
                wanted = 'panic' if sb == 14 and hw == 0 and sku == 'GL' else 'continued'
                if got != wanted:
                    raise AssertionError(('Unexpected stock SKU behavior', sb, hw, sku, got))
                sku_cases.append({'secure_boot_bits_mock': sb, 'hardware_region_byte_mock': hw,
                                  'software_sku_mock': sku, 'outcome': got})
            for kind, off in [('CN', 0x138638), ('GL', 0x138838)]:
                modulus = stock[512 + off:512 + off + 512]
                r = Replay(stock, sb=sb, hw=hw)
                r.u.mem_write(DATA + 0x800, struct.pack('>I', 4096) + bytes(4) + modulus)
                r.u.mem_write(DATA + 0x700, b'\xa5')
                r.u.reg_write(UC_ARM64_REG_X1, DATA + 0x800)
                r.u.reg_write(UC_ARM64_REG_X5, DATA + 0x700)
                rc = r.run(0xc5304)
                trust = r.u.mem_read(DATA + 0x700, 1)[0]
                wanted = sb == 0 or kind == ('CN' if hw == 0 else 'GL')
                if rc != 0 or trust != int(wanted):
                    raise AssertionError(('Unexpected stock key trust', sb, hw, kind, rc, trust))
                key_cases.append({'secure_boot_bits_mock': sb, 'hardware_region_byte_mock': hw,
                                  'key': kind, 'callback_return': rc, 'trusted_out': trust})
    report = {'status': 'PASS_BOUNDED_STOCK_H3_REGION_REPLAY', 'stock_input_sha256': expected,
              'cpu_cases': len(sku_cases) + len(key_cases), 'sku_cases': sku_cases, 'key_cases': key_cases,
              'conclusions': ['In the modeled CN+secure-boot case, stock H3 rejects GL software SKU',
                              'Its root-key callback does not trust the GL key under the modeled CN hardware flag',
                              'These gates are independent of successfully storing a lock request'],
              'mocks': ['SBC/region reads', 'dconfig/config getters', 'SKU extraction', 'logging/PAC'],
              'real_code': ['stock H3 SKU branches', 'stock AVB key callback', 'memcmp/strcmp and ABI'],
              'limits': ['No actual device region/efuse/token state was measured',
                         'The failed friend ROM build is not known to equal this reference',
                         'No complete AVB chain, OTA, recovery or hardware execution'],
              'no_device_io': True, 'no_firmware_created': True}
    (HERE / 'stock_region_replay.json').write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({k: report[k] for k in ('status', 'cpu_cases', 'no_device_io')}, indent=2))


if __name__ == '__main__':
    main()

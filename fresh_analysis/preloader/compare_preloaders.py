"""Fresh, read-only comparison of the two operator-supplied preloaders.

Evidence is local and reproducible; this does not execute a preloader or flash.
Capstone offsets below are FILE offsets, not asserted device addresses.
"""
from pathlib import Path
import hashlib, json, re, struct, random
from collections import Counter
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from unicorn import Uc, UC_ARCH_ARM64, UC_MODE_ARM
from unicorn.arm64_const import UC_ARM64_REG_W0, UC_ARM64_REG_W8

ROOT = Path(__file__).resolve().parents[2]
OUT = Path(__file__).resolve().parent
paths = [ROOT / f'preloader_hyperos{v}/preloader_dash.bin' for v in (3,4)]
images = [p.read_bytes() for p in paths]
a,b = images
assert len(a) == len(b)
md = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
diff = [i for i in range(len(a)) if a[i] != b[i]]
runs = []
for o in diff:
    if runs and o == runs[-1][1]: runs[-1][1] += 1
    else: runs.append([o,o+1])
changes=[]
for o in range(0x100,0xb0000,4):
    if a[o:o+4] == b[o:o+4]: continue
    ins = [next(md.disasm(v[o:o+4],o),None) for v in images]
    changes.append({'offset':hex(o), 'before_bytes':a[o:o+4].hex(), 'after_bytes':b[o:o+4].hex(),
        'before':ins[0].mnemonic+' '+ins[0].op_str if ins[0] else None,
        'after':ins[1].mnemonic+' '+ins[1].op_str if ins[1] else None,
        'address_instruction_candidate':all(i and i.mnemonic in ('add','adr','adrp') for i in ins)})
strings = [[{'offset':hex(m.start()),'value':m.group().decode()} for m in re.finditer(rb'[ -~]{5,}',v)] for v in images]
sets = [{s['value'] for s in ss} for ss in strings]
string_differences = [[s for s in ss if s['value'] not in sets[1-i]] for i,ss in enumerate(strings)]
disassembly=[]
for v,bb in zip((3,4),images):
    disassembly.append(f'HyperOS {v}; file offsets, not runtime addresses')
    for i in md.disasm(bb[0x6a578:0x6a678],0x6a578):
        disassembly.append(f'{i.address:08x}: {i.bytes.hex():8s} {i.mnemonic} {i.op_str}')
(OUT/'dfd_function_disassembly.txt').write_text('\n'.join(disassembly),encoding='utf-8')

# Execute ONLY four ordinary register instructions (MOV/MOVK/CMP/CSET),
# separately from the firmware, to verify the recovered predicate.
rng=random.Random(9500)
values=[0,0xffffffff,0x454e454e,0x44495341]+[rng.getrandbits(32) for _ in range(1000)]
for v,bb in zip((3,4),images):
    uc=Uc(UC_ARCH_ARM64,UC_MODE_ARM)
    uc.mem_map(0x1000,0x1000)
    uc.mem_write(0x1000,bb[0x6a660:0x6a670])
    for value in values:
        uc.reg_write(UC_ARM64_REG_W8,value)
        uc.emu_start(0x1000,0x1010,count=4)
        actual=uc.reg_read(UC_ARM64_REG_W0)
        expected=int(value==0x454e454e) if v==3 else int(value!=0x44495341)
        assert actual==expected,(v,value,actual,expected)

evidence={
 'evidence_id':'PRELOADER-LOCAL-001',
 'inputs':[{'path':str(p),'size':len(v),'sha256':hashlib.sha256(v).hexdigest()} for p,v in zip(paths,images)],
 'byte_diff_count':len(diff),'diff_run_count':len(runs),
 'diff_runs':[{'start':hex(s),'end_exclusive':hex(e),'before':a[s:e].hex(),'after':b[s:e].hex()} for s,e in runs],
 'header_first_0x100_equal':a[:0x100]==b[:0x100],
 'identity_security_metadata_0x270_0x400_equal':a[0x270:0x400]==b[0x270:0x400],
 'instruction_scan_boundary':['0x100','0xb0000 exclusive'],
 'changed_instruction_count':len(changes),
 'instruction_changes':changes,
 'string_set_differences':string_differences,
 'isolated_predicate_emulation':{'engine':'Unicorn ARM64','instructions_per_case':4,'cases_per_version':len(values),'result':'pass',
 'hyperos3':'w0 = (w8 == 0x454e454e)','hyperos4':'w0 = (w8 != 0x44495341)',
 'limitation':'Not whole-preloader execution; no hardware, efuse, trustzone, storage or authentication was emulated.'},
 'limitations':['Raw instruction boundary scan is bounded, not complete function recovery.',
 'Address instruction candidates are not all individually proven equivalent.',
 'No signature validation or device execution performed.',
 'No claim of LK compatibility or secure boot bypass follows from string inventory.']}
(OUT/'comparison.json').write_text(json.dumps(evidence,indent=2),encoding='utf-8')
print(json.dumps({k:evidence[k] for k in ('evidence_id','inputs','byte_diff_count','diff_run_count','changed_instruction_count','isolated_predicate_emulation')},indent=2))

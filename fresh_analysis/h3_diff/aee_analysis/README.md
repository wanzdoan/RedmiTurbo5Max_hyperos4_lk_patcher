> Cập nhật 2026-11-03: người dùng đã báo hybrid boot thành công, fastboot unlocked yes với ENG H3; sau đó báo test thay stock H4 được và yêu cầu dừng kiểm tra. Phần bên dưới giữ phạm vi/lịch sử của bước phân tích gốc. Tài liệu hiện tại: [docs](../../../docs/README.md).

﻿# HyperOS 4 AEE payload: fresh native analysis

The input is the `aee` member of supplied `lk_hyperos4_original/lk.img`, extracted from whole-file offset `0x244750` for `0xf57b0` bytes. `manifest.json` records exact parent/member hashes and mapping evidence. Inputs and previous work were not changed.

The startup code is AArch64 little endian. The linked base is inferred as `0xffff0000be000000`: the startup literal pool at payload `0x310` contains this address; literal `0x338` equals base+`0x1a8` and is used alongside an ADR of payload location `0x1a8`; literals at `0x350` and `0x358` provide the vector address base+`0x1000` and stack address base+`0xf9010`. These are static observations and derived mapping, not a runtime trace.

A derived ELF carrier was created for Ghidra with entry at that base and a deliberately imposed 4 MiB memory region so global/BSS references can be represented. The carrier is an analysis file, not a flash image. Its imposed segment boundaries are not recovered original linker sections.

Ghidra 12.1.2 auto-analysis completed, then exported all recognized functions through the fresh `ExportFresh.java` script: **1827 recognized functions, 1827 successful decompilations**. See `pseudocode/functions.tsv`, individual `.c` files, `console.log`, `ghidra.log`, and saved private project `ghidra_db/AeeFresh`. A successful decompilation does not certify correctness of inferred types, boundaries, indirect call targets, data structures or hardware behavior. This is the comprehensive feasible recognized-function export, not exact original source recovery.

## Unlock/security observations

- VA `ffff0000be067df8`, payload offset `0x67df8`, logs `Custom get lock state...` then returns error value4 without updating an output pointer. Raw instructions and the decompiler agree. This is an unsupported/error getter stub, not the main LK BL33 RPMB signature getter.
- Wrapper VA `ffff0000be0679e4` calls that stub and logs the caller output. VA `ffff0000be0906c4` initializes local state0, invokes that wrapper and computes `locked = (state != 3)`, yielding locked under this stub.
- The token routine at VA `ffff0000be09add0` logs `boot stage is not lk_bl33, can not get unlock token` and returns -1. It contains ID formatting work but does not produce a successful unlock token at this stage.
- Fastboot flashing restrictions and separate security policy functions remain present. This analysis does not establish that normal/main boot traverses the AEE fastboot paths; their presence must not be confused with an observed boot failure.

A separate same-offset scan of H3 original versus supplied patched AEE, payload range `[0,0xa3000)`, found only3429 `ADD`,12 `ADRP`,11 `ADR` word differences and no non-address instruction changes. Full data/relocation equivalence was not proved, but no main-getter-style force-state3 modification was observed in that code boundary. See `h3_aee_changes.json` and reproducible `h3_compare.py`.

No AEE patch was made. The evidence does not justify applying the main LK unlock stub to AEE's intentionally unsupported getter. No device execution or flashing was performed.

The logs include decompiler callback warnings about improperly aligned targets `ffff0000be0b6085` and `ffff0000be0c0846`. Those lie in the string/data area, so inferred indirect targets and auto-analysis must be treated cautiously. Export success is a tool completion count, not proof that every recognized function or inferred edge represents executable code.

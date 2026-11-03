> Cập nhật 2026-11-03: người dùng đã báo hybrid boot thành công, fastboot unlocked yes với ENG H3; sau đó báo test thay stock H4 được và yêu cầu dừng kiểm tra. Phần bên dưới giữ phạm vi/lịch sử của bước phân tích gốc. Tài liệu hiện tại: [docs](../../docs/README.md).

﻿# Independent H4 BL2 -> BL33 load/auth trace

Inputs: read-only fresh extraction `fresh_analysis/container_chain/h4_original_03_bl2_ext.bin` and its Ghidra export. No BL2 or main image was patched by this investigation. All short offsets below are BL2-payload offsets with inferred linked VA `0xffff0000b8000000 + offset`.

## Actual main-LK load path

Raw code at `0x9174` was not auto-recognized as a function by the broad Ghidra export; `bl2_loader_trace.py` disassembles it from original bytes. It obtains `BL33_reserved`, maps that memory, then at `0x9238` calls generic loader `0x50eb8` with partition/name `lk` (stack literal0x6b6c and string `lk` at0x9c16c). At0x92ac it logs `load bl33` only after successful load. A negative loader return leads to cleanup and assertion at0x9290.

The generic loader0x50eb8 finds the mkimg member named `lk` and sets payload size from header. It then calls image-auth-required0x5b018 with the partition name.

## Auth policy is fail-closed in shipped BL2

```text
0x5b018 image_auth_required(partition)
  ->0x5b04c match partition policy index (lk row2)
  ->0x5b0f8 policy(index)
      ->0x6a34c read sboot state
      ->0x52e70 custom lock wrapper
          ->0x52f04 custom lock getter: log; return4; no output write
      ->getter error: log fail; return1
  ->return policy&1
```

Thus with these unmodified instructions the lock getter forces the conservative `auth_required=1` path. Merely supplying SBC0 from an engineering preloader does not reach the optional-auth policy table. Dynamic modifications by an earlier stage could alter this, but no such runtime modification is established here.

`bl2_policy_emulation.py` executes the actual extracted policy/getter machine code in Unicorn. Only the sboot hardware helper, logging and strcmp are mocked. SBC values0,1,2 all return auth_required1; simulated sboot-helper error5 also returns1. Counterfactual cases replace the custom getter with a success/state3 mock: then SBC0 yieldsauth0 and SBC1 yieldsauth1, consistent with the raw policy table. No firmware bytes are modified by these probes. This is isolated instruction execution, not phone execution or whole-boot emulation.

## Required-auth branch

```text
0x50eb8
  ->0x52c3c certificate/region verification wrapper
      ->0x6aa80 certificate chain verification
          ->0x6e3dc cert1 verification
          ->0x6edac cert2 verification
      ->optional CN/GL key/region checks
  ->0x52bc4 anti-rollback check
  ->0x6b29c header hash authentication
  ->read main LK payload
  ->0x6b460 image authentication
```

Nonzero chain, rollback, header or image-auth results lead to assertions in this loader. A valid return from the preloader's acceptance of BL2 or LK metadata alone does not establish acceptance by this later chain.

## Optional-auth branch (conditional, currently not reached)

If an earlier change or runtime environment actually makes0x5b018 return0:

```text
0x50eb8
  ->0x6af68 parse certificate metadata (failure only logged)
      ->0x6d6bc parse cert2 fields, no chain signature verification here
  ->read main LK payload
  ->0x6a460 image_restore
      ->0x6a490
```

The parse initializer clears decode-type globals `DAT...1b8810` and `DAT...1b8814`. Restore0x6a490 returns success immediately when `DAT...1b8810 == 0`; its later decrypt/decode/hash checks are skipped in that case. Therefore an unencrypted main-LK instruction modification can pass this hypothetical optional-auth route without a duplicate prefix, provided certificate parsing leaves valid unencrypted/default metadata and all loading requirements succeed. It is incorrect to state that arbitrary prefixes always pass: metadata may select decoding or cause other invalid state. It is also incorrect to assume this route is active in the supplied BL2, whose policy probe returns1.

## What remains unknown

- Actual device sboot/fuse/boot-tag inputs and any previous-stage runtime memory edits.
- Whether supplied engineering preloader makes further code modifications; its acceptance bypass is a separate stage.
- Whether a particular prefix defeats the actual H4 BL2 required-auth chain. The container investigator is examining its changed parser context; this report does not substitute a signature proof or full-machine-code parser probe.
- Hardware operation, successful boot and every optional load path.

An additional BL2 acceptance mechanism must be accounted for before claiming an ENGPL + patched-main-H4 combination is bootable. This trace alone does not prescribe a specific patch and does not justify disabling authentication globally.

Additional static reachability evidence: BL2 app descriptor at payload0x108630 contains name pointer to `bl2_ext_boot`; its init pointer is base+0x9168 and entry pointer at0x108640 is base+0x9174. Thus the load-BL33 routine is an explicitly registered application entry despite being absent from Ghidra's auto-recognized function export. Raw loader branch0x50fe0 calls thunk0x52c24 ->auth-required0x5b018; required-auth call is at0x51018, optional parse call at0x51110. This illustrates why an all-recognized-functions export is not a complete coverage guarantee.

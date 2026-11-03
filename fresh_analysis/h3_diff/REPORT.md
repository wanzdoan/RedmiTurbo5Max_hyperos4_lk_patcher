# Fresh HyperOS 3 patch comparison

This report uses only the supplied original and patched H3 files and the supplied H4 original. It does not use earlier project reports or patched H4 outputs. All offsets below are whole-file offsets, unless explicitly labelled otherwise.

## Observed executable patch

In the same-offset aligned 4-byte scan over H3 main payload file range `[0x200, 0xcf000)`, 5,935 instruction words differ:

- 5,891 `ADD` changes: registers unchanged; immediate decreases by 23, modulo 4096.
- 23 `ADRP` changes and 17 `ADR` changes.
- Exactly four other changed instruction words, contiguous at `0x89a2c..0x89a3b`.

The four-word patch replaces the prologue of a function whose own log string is `mi_get_lock_state`:

```c
// behavioral equivalent of observed patched four instructions
int mi_get_lock_state(uint32_t *out_state) {
    *out_state = 3;
    return 0;
}
```

Original bytes: `3f2303d5ff4305d1fd7b12a9fc5713a9`.
Patched bytes: `68008052080000b900008052c0035fd6`.

The custom wrapper at file `0x88f60` logs `Custom get lock state...` and tail-calls the target at `0x88f90`. The next wrapper at `0x88950` calls it at `0x88964` and returns its status. `function.txt`, `get_lock_callers.txt`, and `get_lock_callers2.txt` preserve disassembly.

`3` means unlocked for the observed callers: the fastboot query selects `lks = 0` when state equals 3; the AVB image-auth predicate computes `state != 3` and logs `[AVB] img_auth_required = %d`.

## H4 correspondence

H4 contains a unique exact first-32-byte match of the original getter at file `0x89cec`; its own log points to `mi_get_lock_state`. Main wrapper H3 `0x88950` corresponds to H4 `0x88c10`, custom wrapper H3 `0x88f60` to H4 `0x89220`.

A bounded direct-call scan found 16 callsites of the main wrapper in each supplied main LK executable scan. No new direct call to that wrapper was found in this boundary. See `h4_call_map.json` and `h4_lock_callers.txt` for the complete mapping, retained windows, and correspondence candidates. The match algorithm normalizes PC-relative call/branch operands and some immediate addresses; it is a correspondence aid, not an equivalence proof.

Examples:

| Meaning | H3 call | H4 call | Observed state handling |
|---|---:|---:|---|
| Fastboot lock query | 0xca8c | 0xcb04 | state 3 selects `lks = 0` |
| AVB setup | 0x6884c | 0x68b14 | state 3 sets permissive authentication-related flags |
| AVB auth predicate | 0x68c20 | 0x68ee8 | return `state != 3` |
| Lock boolean wrapper | 0xb6170 | 0xb67e0 | output `state != 3` |
| State mutation wrapper | 0xb61b8 | 0xb6828 | getter-error behavior changed, see below |

One genuine caller difference was found: H3 function entry `0xb6198` returned immediately if getting lock state failed; corresponding H4 function `0xb6808` writes local state 4 and continues. The observed forced getter returns success 0, so that changed error fallback is inactive under the four-instruction patch. This observation does not establish safety of all write/relock operations.

## Build and container differences

The H3 files are not simply the same shipped image with one function overwritten. Printable strings show build identifier `b784be3ce_202607021449` in original versus `b784be3ce_202602281424` in patched. Main strings after deletion move by 23 bytes, explaining most address changes. The matched 4-word patch is therefore a semantic patch candidate, not a full byte-diff transplant.

`members.json` records offsets, declared lengths, payload hashes, and header hashes for the 15 container members. Main LK payload has the same size and same header in both. `bl2_ext` and `lk_main_dtb` payloads are identical; `aee` and `lk_dtbo` differ. Every cert1 payload is identical. Every cert2 payload differs. The first cert2 grows from 982 to 1162 bytes, so later members move +0xb0 until the smaller dtbo changes the final displacement. The first cert2 includes a 180-byte prefix before the ASN.1 sequence seen in original. Certificate verification is a separate investigation; these observations do not establish valid signatures.

## Limits

This is static analysis only. No firmware was executed, no phone was connected or flashed, and no hardware boot was verified. The aligned executable scan does not prove all data or indirect control-flow equivalence, nor does it cover BootROM, TEE, fuses, RPMB, or other partitions. Original source code cannot be reconstructed exactly from this stripped binary. Any decompiler output is recovered pseudocode.

## Independent review of H4 Ghidra pseudocode

Reviewed the new H4 exports produced by the root investigation, checked against earlier raw instruction evidence. VA/file mapping: `VA = 0xffff000090700000 + file_offset - 0x200` for the main payload.

The getter at VA `ffff000090789aec` / file `0x89cec` reads RPMB magic, signature length and stored signature. Its successful signature-verification path writes state3; failure/default paths write state4. A hardware security fallback also exists when the magic-check helper returns6. The four-instruction replacement returns state3 without calling these helpers and without writing persistent unlock state.

The AVB operation table at VA `ffff0000908385e0`, file `0x1387e0`, contains callback VA `ffff0000907c5ba8` at offset `+0x48`. That callback calls VA `ffff0000907b65c4`, whose output is `(state != 3)`, and emits `is_unlocked = !locked`. The command-line builder at VA `ffff0000907bc5d4` uses that operation to choose `androidboot.vbmeta.device_state=unlocked`. Therefore the observed getter patch affects actual AVB device-state input, not just the fastboot text.

At VA `ffff000090768854` / file `0x68a54`, state3 sets bit0 of flags passed to `ffff0000907be740` (the packaged slot verifier). Both the outer and inner logic permit continuation when that bit is set despite selected verification failures. The inner filter only retains results `{0,3,4,5}`; other numeric results remain rejected. This is not an unrestricted load-everything bypass. File IO, malformed-image handling, partition availability, parser assumptions and other boot stages remain relevant.

At VA `ffff000090768c94` / file `0x68e94`, image authentication required is exactly `(state != 3)`. At VA `ffff00009070c3ac`, getvar publication uses the locked wrapper to select `secure=no` and `unlocked=yes` under state3. Another variable in this function is named `fuse` and reflects hardware-function bits; it is not a critical unlock flag.

A bounded search of all exported main LK pseudocode and printable main-LK strings found no separately named `unlock_critical`, critical-unlock query, or `flashing unlock` handler. This absence does not prove that no indirect/unnamed mechanism exists; separate critical unlock support is unresolved and must not be claimed as implemented.

`h4_recovered_semantics.c` provides focused high-level equivalents for these routines. It is an analyst model, not original source and not a linkable replacement firmware. The H4 wrapper getter-error fallback difference recorded above remains the identified changed lock-state caller; the forced getter-success path does not encounter it. No additional change was established as necessary in these reviewed AVB/getvar routines, but this review does not establish full boot-chain compatibility.

## Broader H3/H4 main-LK delta survey

`semantic_delta.py` creates `h3_h4_semantic_delta.json` directly from original binaries. It splits code at PACIASP instructions in whole-file range `[0x200,0xcf000)` and normalizes PC-relative addresses, call operands and tracked address additions. It found2538 H3 chunks and2547 H4 chunks;1772 chunks align with identical normalized streams. Remaining774 changed candidates include relocated globals, debug line numbers, altered function boundaries and actual semantic changes. This is a triage mechanism, not a proof of equivalence: normalizing call operands can hide changed callees, PACIASP is not a complete function-boundary oracle, register tracking is approximate and indirect execution is not covered.

Reviewed actual changes relevant to boot/security:

| H3 whole-file | H4 whole-file | Observed H4 change | Implication/boundary |
|---|---|---|---|
| 0xb2158 | 0xb27b4 | Resets certificate/input cursor and remaining length a second time before parser call | Main LK certificate validation changed; container/preloader investigation must determine stage relevance. Getter patch does not alter this code. |
| 0x68568 | 0x68838 | clear_dm_verity_status writes zero to MMIO0xffff00001800d088, previously cleared bit0 only | Existing maintenance path changed; hardware register meaning not established here. |
| absent/unmatched | 0x885ac | New weak sec_free_fw_protect_share_mem stub logs and returns0 | No-op hook, not an established unlock blocker. |
| 0x2223c | 0x2247c | Dconfig SOCID-mismatch-ignore branch signals an event before its existing log | No evidence of changed final auth policy from that added call alone. |
| 0xadb10 | 0xaddd0 | sec_func_init includes version202520730 in log | Version-observability change; no new check established in this function. |
| 0xb6198 | 0xb6808 | Getter error fallback writes local state4 and continues instead of immediate return | Forced getter success avoids this fallback; existing relock/write operations still need care. |
| 0xa694 | 0xa620 | Fastboot exports hwlevel,hwversion,project_adc,build_adc | Hardware reporting additions; no identified unlock gate. |
| 0x13b30 | 0x13c14 | Cmdline function removes permissive-SELinux branch and adds uart_console reporting | State3 does not restore removed SELinux-permissive behavior; independent of AVB unlock state. |

For the certificate change specifically, H4 Ghidra VAffff0000907b25b4 first initializes globalsDAT...90cc68/90cc78 to `input+*(u32*)(input+0x34)` and `length-offset`, scans/compares the certificate public key and converts the modulus, then repeats those initializations before calling VAffff0000907b216c. H3 lacks this second reset. The caller/parser relationship was sent to the independent container investigator. It must not be equated automatically with preloader self-validation of the patched LK.

This survey adds evidence of real H4 changes but cannot support a claim that every H4 mechanism has been exhaustively proved compatible or fixed.

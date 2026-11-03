# BL2_EXT verification path: HyperOS 4 hardening

## Direct code observations

The H4 `bl2_ext` payload uses link base `0xffff0000b8000000` (literal at payload+0x310). Decompilation created a separate fresh project `bl2_h4.gpr`; `bl2_decompile/functions.tsv` indexes 1526 recovered functions, including some analysis artifacts. This is pseudocode, not original source, and not all code was recognized automatically. Focus exports explicitly recovered the BL33 loader entry.

`bl2_h4_focus/00009174.c`: BL2's load-BL33 routine constructs partition name `lk` and calls H4 payload function `0x50eb8` to load member `lk` to reserved BL33 memory. Therefore the main LK is consumed by BL2_EXT, independently of preloader verification of BL2_EXT.

`bl2_decompile/ffff0000b8050eb8.c`: the generic loader asks `0x5b018` whether authentication is required. If required, it calls cert-chain verification wrapper `0x52c3c`, antirollback check `0x52bc4`, header auth `0x6b29c`, reads payload, then image authentication `0x6b460`. Any nonzero verifier status in this path reaches a fatal assertion.

`0x5b018 -> 0x5b0f8` queries SBC and lock state. The H4 `custom_get_lock_state` function `0x52f04` logs and returns status 4. The caller handles any nonzero query status by returning auth-required 1. Thus this recovered static path fails closed; an unlocked main-LK getter does not itself disable this earlier BL2 authentication.

## Decisive H3/H4 parser difference

H3 `bl2_h3_focus/0006cf8c.c` initializes ASN.1 base/length to the raw cert2 payload and searches hash OIDs there directly.

H4 `bl2_decompile/ffff0000b806d6bc.c` initializes to the raw cert2 payload, calls helper `0x6cdc4` to locate the signed TBS sequence, then replaces ASN.1 base/length with that sequence before searching hash OIDs. Added payload instructions include:

- 0x6d700..0x6d70c: prepare output pointers and call TBS locator 0x6cdc4.
- 0x6d710: return on locator error.
- 0x6d714..0x6d720: load located TBS pointer/length and store them as the search scope.

This excludes the outside duplicate ASN.1 prefix hashes present in the H3 patched image. H4's TBS locator parses constructed tags and seeks the certificate SEQUENCE and its inner TBS SEQUENCE; it does not treat an outside A0 prefix as signed TBS.

The full signature routine also changes: H3 payload entry 0x6e584, H4 entry 0x6edac. H4 reinitializes raw input cursor/length before signature verification, whereas H3 leaves the earlier parser cursor. H4 then calls `0x6d6bc` for field parsing with mask 0x1f4e2. The noauth path uses mask 0x1f400 and does not request the header/payload digest fields (bits 5 and 6); it is a different path, and should not be confused with a successful enforcing verification.

## Implication and unresolved boundary

A main-LK-only 16-byte patch with external replacement hash prefixes cannot be treated as accepted by H4 BL2. The retained signed H4 cert2 hashes the original main payload; patched bytes change that hash. H4 BL2's enforcing parser now uses the signed hash, so its digest check is expected to reject the candidate. A real ARM64 parser replay is being performed by the parallel preloader investigator to corroborate this inference.

Patching H4 BL2's parser itself also changes a payload authenticated by the earlier preloader. The supplied H3/H4 preloader parser was separately replayed and scopes hashes to signed TBS, so simply adding another external prefix to a modified BL2 does not solve that boundary. Reusing the unmodified signed H3 BL2 with a patched H4 main is an experimental hybrid option; hardware initialization, antirollback and cross-version handoff compatibility would still require validation. No such hybrid was built in this task.

## ARM64 replay corroboration

The parallel investigator completed 18 BL2 parser cases using the actual extracted ARM64 instructions: `fresh_analysis/preloader/emulate_bl2_parser.py` and `bl2_parser_emulation.json`. No signature or authentication result was mocked in these parser-only calls; logging/PAC and initial zero BSS were modeled. With masks 0x60 and 0x1f4e2, H3 0x6cf8c selects patched prefix digest `dc606f8d...`; H4 0x6d6bc selects signed donor digest `77af9712...`. Both return zero parser status, which means successful field extraction, not successful payload authentication. Mask 0x1f400 extracts neither hash. This confirms the parser hardening described above at the actual consuming BL2 layer.

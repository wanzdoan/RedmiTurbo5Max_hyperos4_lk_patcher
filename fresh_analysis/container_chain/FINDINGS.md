> Cập nhật 2026-11-03: người dùng đã báo hybrid boot thành công, fastboot unlocked yes với ENG H3; sau đó báo test thay stock H4 được và yêu cầu dừng kiểm tra. Phần bên dưới giữ phạm vi/lịch sử của bước phân tích gốc. Tài liệu hiện tại: [docs](../../docs/README.md).

﻿# Container and certificate findings

All analysis here reads only the three user-designated LK inputs. It does not use historical project outputs or execute firmware.

## Observed structure

Each file contains 15 MediaTek records, in five component/cert1/cert2 triples: `lk`, `bl2_ext`, `aee`, `lk_main_dtb`, `lk_dtbo`. Each record has a 512-byte header; payloads and record starts align to 16 bytes. Header word +4 is unaligned payload size. `container_inventory.json` records exact locations, sizes and SHA256 values; the extracted `.bin` files are exact declared payload bytes.

The HyperOS 3 patched file is based on a differently built/signed donor. Its `bl2_ext` and `lk_main_dtb` payloads exactly match the H3 original. Its `aee` and `lk_dtbo` differ. Normalizing DTBO fragment paths using the `__fixups__` target labels yields 34 property changes, chiefly charging configuration (for example 17000 -> 17600). These changes do not establish any unlock function and should not be ported to H4.

## Reproduced hash and signature observations

Run `python fresh_analysis/container_chain/verify_container.py` after `inventory.py`. The verifier confirms all 30 cert1/cert2 RSA-PSS SHA256 signatures under their embedded keys with MGF1 SHA256 and salt length 32. This validates mathematical signatures, not the device root of trust. Cert1's additional SPKI at DER +496..790 equals cert2's SPKI at +181..475. All cert1 keys across versions are identical; all cert2 keys across versions are identical.

MediaTek OID `2.16.886.2454.2.4` is the SHA256 of the preceding component's 512-byte header. OID `.2.1` is SHA256 of the component payload padded with zeros to a multiple of 16 bytes, excluding its 512-byte header. These domains match all 15 components when using the first occurrence of the OID in the respective cert2 record.

## Exact H3 modified certificate construction

Only main LK cert2 has a special prefix. Original declared size is 0x3d6; patched size is 0x48a. The patched payload is 180 prefix bytes followed by a normal 0x3d6-byte signed DER certificate. The prefix is two identical 90-byte ASN.1 blocks:

`A0 58 | 06 07 60 86 76 93 16 02 04 | 03 21 00 | SHA256(header512) | 06 07 60 86 76 93 16 02 01 | 03 21 00 | SHA256(payload_zero_padded_16)`

The hashes in both prefix blocks match the patched component. The normal DER's signed payload digest is instead `77af9712f9533da9e85c629bbe5a63a7a40da08526c579b853b1256ba3f9a841`. Restoring just the original 16-byte function prologue at H3 container file offset 0x89a2c in the patched donor reproduces this digest exactly. The patched aligned payload digest is `dc606f8db302a5895482b4f46799d34591a7bb00a777151101ac83812d862d78`.

Thus no private signing key is implied: the artifact retains a valid donor signed certificate and prepends replacement hashes for the changed image. The two prefix hash fields are outside the retained DER's signature coverage. This is direct artifact evidence for certificate parser ambiguity as the intended authentication bypass, rather than normal re-signing.

The main cert2 512-byte header differs only in its +4 size word (0x3d6 -> 0x48a). Aligning after the extra 180 bytes causes the next component to shift by 176 bytes because padding shrinks by four bytes.

## Reproduction recipe and limits

To reproduce the observed construction for a differently patched H4 main payload, retain the original H4 signed DER intact, compute the two hashes with the domains above, prepend two 90-byte blocks, update only the cert2 header payload size, and align the new member to 16 bytes before the unchanged remaining H4 records. This is a candidate format recipe, not evidence that H4 accepts the construction. Successful boot still depends on the consuming preloader/parser and runtime state; the chain must be traced or emulated to establish which hash occurrence it selects. This task did not create or flash a patched image.

`cryptographic_evidence.json` is the machine-readable finding ledger. `lk_dtbo_h3_normalized_diff.json` records normalized unrelated DTBO changes. No assertion of complete original source recovery is made.

## Follow-up: consuming preloader contradicts simple prefix acceptance

The parallel preloader investigation recovered H4 function at file offset 0xa6508: after successful signature and key checks, it replaces the ASN.1 search base/length with the verified TBS range before extracting header/payload digest OIDs. This would exclude the 180 outside prefix bytes on the successful verification path. That evidence narrows the result above: prefix format reproduction alone is insufficient to claim compatibility. The actual path that accepted the user's H3 patched artifact remains unresolved (potentially a different verification/control path or device-resident preloader state). Consult `fresh_analysis/preloader/decompile_h4/000a6508.c`, `000a5950.c` and `000a6100.c`, with raw instruction evidence from the preloader investigation. The prefix mechanism must not be reported as proven effective in supplied H4 preloader.

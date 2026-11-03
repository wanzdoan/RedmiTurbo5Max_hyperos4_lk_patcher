# Offline LK / 5G-NR review

Evidence boundary: supplied H4 main LK, H3 signed BL2 donor, H4 BL2 reference, H4 AEE, H4 LK DTB/DTBO, and the user-confirmed hybrid artifact. No phone attachment, flashing, modem command, NVRAM access, or firmware edits. All files created by this task are under this directory. Root established checkpoint `before-5g-research-20261103` before this read-only investigation.

## Findings

**No usable LK switch to enable cellular 5G/NR in Vietnam was established.** The inspected LK contains modem loading and boot-information handoff, but the observed knobs/strings below are not a proved NR regional enable flag. This is a bounded negative, not a proof that every opaque firmware table or downstream Android/modem component is irrelevant.

### Actual modem capability flow

H4 main VA `ffff00009072eb14` (`get_check_header`, original whole-file0x2ed14) reads the final32-bit size from a supplied modem image, validates CHECK_HEADER and accepted sizes0xbc/0x11c/0x200 with versions1/3/6. It returns a pointer into that modem image's header. Its input is image data, not a hard-coded NR-enable setting in LK.

VA `ffff00009072e484` (original whole-file0x2e684) reads32-bit header+0x14 and copies it through `ffff000090732394` into the `md_capability` CCCI tag. The same field is logged as `image_type` by `ffff00009072ee30`. It also forwards modem platform, build time, build version, DRDI version, memory sizes and check-header data. `ffff00009072f664` caches the low byte and sizes. This establishes data forwarding; the numeric enum's precise meaning and actual image field value must be established from the modem artifact/downstream consumer before proposing any change.

VA `ffff00009072df14` reads DT `/mddriver` or `/soc/mddriver` properties whose names start with `ref_args`. It recognizes `md_disable` and sets an entire-modem-disable variable. This is not an observed separate 5G-only gate. The parsed H4 LK DTB has `/soc/mddriver@d180000` hardware/transport/power properties; no NR-enable, RAT config, cellular band mask, MCC/MNC or Vietnam table was found in the parsed matching properties.

### Country/region handoff exists, direct NR control not demonstrated

H4 main VA `ffff000090713a14` (original whole-file0x13c14), around its countrycode block, constructs the slot-specific `countrycode%s` partition name, reads16bytes at partition offset0, and appends `androidboot.ptcountrycode=%s` to the kernel command line through `ffff000090719078`. There is no branch in this block that selects NR, a cellular band, MCC/MNC or radio access technology. A downstream Android/vendor/modem consumer could interpret regional information; this review does not trace that consumer or establish that changing countrycode enables 5G. The main getter patch leaves this block byte-for-byte unchanged.

### Misleading matches were resolved

- `nr-max-core`, `nr-big-core`, `nr-rs-entry-*`, `nr-header-row` belong to `/soc/dfd-mcu`; VA `ffff00009073b3d8` retrieves this DFD CPU crash/debug configuration. DT values include max cores8, big cores4, entries16/32 and header rows4. Here `nr` is count-related, not a cellular NR setting.
- `nr-port` occurs in charging/USB-PD nodes; `bat,nr` is in PD data.
- `band-path-wifi` and `band-path-bt` belong to connectivity FEM layouts. They are explicitly Wi-Fi/Bluetooth paths, not cellular NR band masks.
- `Check lte cert pubk_hash`, `set LTE pubk hash done` refer to certificate public-key-hash handoff. H4 BL2 VA `ffff0000b8067ac0` prepares hash words and uses SMC0xc2000102, subindices0..3; `ffff0000b8067ce8` guards that path by hardware bit and image type. The corresponding H3 donor routines at payload0x67634/0x6785c have identical normalized instruction/register streams to H4 routines0x67ac0/0x67ce8. This is a security handoff, not an observed LTE-only-mode selection. The normalization masks address/call and ADD/load/store immediates, so it is corroborative structural evidence rather than complete equivalence proof.

## Does the hybrid introduce a radio configuration delta?

`hybrid_radio_delta.json` checks the actual artifact SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`:

- Main LK differs from original H4 in exactly16bytes at payload0x89aec..0x89afb (whole-file0x89cec..0x89cfb), the established unlock getter replacement. No modem-loading, capability-forwarding or countrycode block was changed.
- AEE, LK main DTB and LK DTBO payload hashes exactly match original H4.
- BL2 payload hash exactly matches the signed H3 donor. A full boot-stage replacement can have indirect effects; the absence of a named NR setting and matching reviewed LTE-key routines does not prove all behavior equivalent. No specific radio-limiting delta was established here.

Therefore current evidence does **not** support attributing 4G+ to an LK NR-disable patch, or creating a speculative LK “enable5G” patch. The actual ROM modem configuration, Android telephony/vendor properties, operator/SIM provisioning and live radio diagnostics are separate evidence boundaries.

## Reproducible evidence

- `inventory_radio.py` / `binary_string_inventory.json`: input hashes, sizes and offset-anchored printable-ASCII matches(minimum5characters). The H3/H4 BL2 matching radio-related string sets are identical; 5G/NR-enable/MCC/MNC/RAT/band-mask names were not established. Generic substring matches include unrelated words and are explicitly resolved above.
- `parse_dt_radio.py` / `dtb_radio_properties.json`: format-aware parsing of13078 LK DTB properties and2425 properties in the single DTBO entry, retaining raw hex and decoded candidates. Decode heuristic may represent all-zero cells as empty strings; raw hex is authoritative.
- `hybrid_delta.py` / `hybrid_radio_delta.json`: actual hybrid component identities, main16byte diff and reviewed H3/H4 LTE certificate-handoff function correspondence.
- `hybrid_members.json`: actual output member offsets/sizes/hashes.

Code references above use existing fresh Ghidra pseudocode, corroborated with artifact identity. Decompiled names/types are analyst recovery, not original source. No hardware execution or live Android/RIL observations were made. No claim that a device can receive a specific Vietnamese NR band follows from this review.

# LK causal trace — 2026-11-03

Device remains untouched. Only LK output is authorized; no region module installation, radio setting changes or flashing. The root-manager region module from the prior step is not a validated NR fix and is outside this LK-only work.

## Modem handoff: NR already present in the descriptor

The actual H4 modem CHECK_HEADER at file0x2950000 declares image type13. LK `ffff00009072e484` forwards that value into `md_capability`. The shipped kernel module `ccci_util_lib.ko`, extracted with identity checks from this ROM's vendor_boot, maps type13 to mask0x7b. Its `get_capability_bit` defines `N`/`n` as0x40. The mask already includes that bit; changing the descriptor to14 would add C, not enable missing NR.

See [input identity](handoff_inputs.json), [decoded mapping](kernel_capability.json), [get_md_bin_capability](ccci_util/001081f4.c), [get_capability_bit](ccci_util/00108280.c), and [LK argument collector](ccci_util/0010bb40.c). Decompiler outputs have external-call and switch-recovery limitations; original module symbols, table bytes and ARM64 instructions corroborate the map. This does not show active band/operator policy or registration.

`md1_phy_cap_gear` controls capture shared-memory allocation, not NR. `md_disable`, modem power/start and hardware-remap calls control the whole modem. No demonstrated NR enable patch in these paths.

## Country propagation: a real consumer, incomplete normal-boot chain

LK `ffff000090713a14` reads countrycode and emits `androidboot.ptcountrycode`. The recovery ramdisk `prop.default:1300` imports `/odm/etc/dash_${ro.boot.ptcountrycode}_build.prop`. This is a real consumer, but the normal-boot property loader and ODM region files are unavailable in the current offline evidence. No claim that the same import affects normal Android boot or changes radio policy is justified.

CarrierStudio uses `ro.miui.build.region` to select region-gated XML; inspected Vietnam SA entries have no build_region filter. Native radio exports also include region helpers, but no direct caller linking them to an NR decision was established. `ModemServiceMoniter.notifyBuildRegion` has region-copy semantics but no Java callsite was found in the extracted trees. Static function existence is not execution evidence.

## Actual CN-to-Global ROM gates (separate from NR)

1. `ffff000090768d1c` obtains `com.android.build.init_boot.sku` from AVB metadata. Secure-boot CN hardware accepts CN/NATIVE prefixes, otherwise panics. Unlock does not bypass it. Final reject branch: whole-file0x6924c, `40110035`, `cbnz w0,ffff000090769274`.
2. `ffff000090788734` verifies a certificate chain, retrieves the image public key and enforces the hardware-region key. CN mismatch returns-1. A proper change should retain signature-chain and key-extraction errors while allowing the embedded GL key as an alternate, rather than accepting arbitrary keys.
3. `ffff0000907c5978` checks AVB key identity against embedded CN/GL keys; secure boot chooses one by region. A narrow union of the two existing keys can preserve rejection of unknown keys.
4. `ffff000090787318` would send CN hardware/non-CN software to recovery, but its caller `Is_Secure_ID` already returns success with the existing patched getter state3. Extra patch is redundant.

These are candidates for an independently named ROM-region experiment, not proof of a 5G fix or of compatibility with any unprovided Global ROM. Antirollback, image authentication, partition/model/layout checks and earlier boot-stage authentication are separate mechanisms.

## Reproduce

Run `extract_handoff_inputs.py --cache <temporary-directory>` against the same ROM, then `kernel_capability_evidence.py <cache>/ccci_util_lib.ko`. Use local Ghidra12.1.2 headless with `TraceRadioNative.java`; derived projects belong outside Git. Focus outputs here are static evidence, not recovered original source or a whole-device test.

# Modem/MCF NR investigation

Scope: original OS4.0.3.0.XPLCNXM modem/MCF/countrycode images; selective read-only queries against the connected device. No AT settings, NV writes, firmware modifications or flashing were performed.

## What is established

- `modem.img`: 121970384 bytes, SHA256 `93df50a5c641d547c68a28ec34b7cc22e6f3cd3b0c2bf27d4eb1b360ef38d876`. Format-aware MTK member inventory identifies `md1rom`, `md1drdi`, `md1dsp`, certificates, diagnostic metadata and debug/schema databases.
- `mcf_ota.img`: 33538048 bytes, SHA256 `cd348ee328bb3383c9cbe483ee2592529451980e415c59493b246627549dc079`. It is raw ext4, 4096-byte blocks, volume label `/mnt/vendor/mdot`. A read-only extent parser enumerated 1547 entries and 1225 binary OTA configuration files. All 1225 obey the observed top-level tag/length boundaries (`0x10004`, followed by one or more `0x20004` sections). Their encrypted/security context was not decoded.
- The ext4 parser's extracted content matches 862 of 864 entries in the shipped MD5 manifest; the two unmatched entries concern CA certificate resources, not modem OTA profiles. This is corroboration of the parser, not full filesystem integrity validation.
- `countrycode.img`: 1048576 bytes, SHA256 `065d16b492631a65cd2e90dfcc6608edb168d8177b3eef1161b35142bf26ece6`. Starts with `cn`, and contains `countrycodeCn`. These bytes alone do not establish a NR switch or a safe edit.

The only complete plaintext 45201/45202/45204/45205 PLMN matches inside the MCF filesystem occur in `/misc_data/plmn/TS25/4/52.txt`, a network-name CSV. Its existence does not prove any NR band/profile permission for Vietnam.

## Recovered real modem schema and symbols

`md1_mddbmetaodb` is a GZIP member which decodes in memory to 6720096 bytes of XML. `md1_dbginfo` is LZMA-alone and decodes to 5791613 bytes of `CATICTNR` symbol metadata. The symbol parser reads 138558 name/start/end entries. These decompressed databases were not copied wholesale into the repository; scripts regenerate bounded indexes.

The XML directly defines actual gates and configuration layouts:

| LID | Meaning | Structure bytes |
|---|---|---:|
| 960 | SBP modem feature configuration | 332, 2 records |
| 961 | SBP modem data configuration | 424, 2 records |
| 2375 | NR band disable lists | 11948 |
| 2392 | NR band enable lists | 5526 |
| 2380 | NR operator/PLMN band table | 8004 |
| 2390 | NR partial-band table | 9604 |
| 2439 | NRRC band support by PLMN | 52404 |
| 2437 | Custom ENDC combination list | 23520 |

LID2375's own schema describes both positive and inverted lists: a positive match can disable specified bands; the inverted list disables specified bands when the PLMN does not match any entry in the permitted PLMN groups. There are separate national/international roaming variants. This establishes a real potential mechanism for region/foreign-PLMN filtering, but the active list contents have not been read/decrypted.

Exact LID960 feature-array locations (array starts at structure offset4):

| Feature | Array byte/bit | Schema bit index |
|---|---|---:|
| NAS_ROAMING_DISABLE_ENDC | 91 / 0 | 728 |
| CONFIG_ENDC_COMB_BY_SBPID | 106 / 0 | 848 |
| NR_CUSTOM_DISABLE_BAND_BY_PLMN | 111 / 4 | 892 |
| NR_CUSTOM_DISABLE_BAND_BY_PLMN_WITH_SIM | 130 / 7 | 1047 |
| NR_CUSTOM_DISABLE_BAND_BY_PLMN_FOR_ROAMING | 160 / 0 | 1280 |
| NR_CUSTOM_ENABLE_BAND_BY_PLMN | 217 / 3 | 1739 |
| NWSEL_SKIP_NR_SEARCH_WHEN_SA_DISABLE_BY_MCC | 218 / 2 | 1746 |

LID961's data array starts at offset4; `NRRC_CAP_BC_WHITELIST_ENDC_NUM` and `...SA_NUM` are array bytes313 and314. Schema indices are not independently established as public AT-command IDs.

Relevant shipped function symbols include `mrs_nras_sa_nr_band_bitmask_get_by_plmn` at0x915d200e, `mrs_nras_endc_nr_band_bitmask_get_by_plmn` at0x915d2154, `mrs_nras_band_setting_update_by_plmn` at0x915d39d4, and `nrrc_nr_band_support_by_plmn_enabled` at0x91a8e5e8. Symbol presence supplies concrete tracing targets; it does not prove a branch is currently active.

## What was observed on the connected device

Reading only the first24 bytes of235 files under `NVD_DATA` identified:

- `SP80_636`: LID960, version636, two332-byte logical records.
- `SP81_133`: LID961, version133, two424-byte logical records.
- `NR01_038`: LID2432, two88-byte UE NR capability records.
- `NR0H_004`: LID2451, one3442-byte enhanced carrier-info record.

The seven other target NR table LIDs were not found in this directory's headers. This is a bounded absence observation; ROM defaults or another namespace may supply them.

A filtered read of `nv_boot_trace` shows the modem actively reading and writing SP80/SP81: SP80 record I/O begins at file192 with stride384; SP81 begins at192 with stride464. Their logical-record sizes therefore cannot be used as direct raw-file offsets. The on-disk bodies are not decoded, and no active feature bit values were claimed. Only sanitized config trace lines and header metadata were persisted; raw NV files and the raw boot trace were not saved.

## Remaining question

The evidence locates actual modem-level NR/SIM/PLMN policy mechanisms and their data sources. It does **not** yet identify which active value disables NR for the user's 45204/45202 SIMs, whether it is a modem SBP/list restriction or an Android-side command, or a proven LK change that enables NR. Reading decoded LID960/961 and the relevant NR lists through a verified read-only modem interface, or tracing the actual modem code/config selection, is the next evidence needed. Blindly changing countrycode, ciphertext NV offsets or similarly named SBP identifiers would not be a validated fix.

Machine-readable evidence: `inventory.json`, `mcf_layout_summary.json`, `nvram_nr_lid_index.json`, `mddb_sbp_identifiers.json`, `sbp_schema_string_order.json`, `modem_relevant_symbols.json`, `runtime_nvram_headers.json`, and `runtime_nv_boot_config_trace.json`.

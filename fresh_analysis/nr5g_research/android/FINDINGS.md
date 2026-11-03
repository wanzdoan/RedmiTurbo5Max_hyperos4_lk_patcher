# Android telephony / SA policy findings

This is offline analysis of the APKs/JARs pulled by the parent investigation. No device commands, settings changes, broadcasts, flashing or NV writes were performed by this task. Input digests and JADX completion/error counts are in `analysis_manifest.json`. Decompiled text is recovered code; selected routines are simple/readable, but the exports have2MTK,4framework and12TeleService JADX errors, including a complex FiveGManagerBase initialization method. These limits do not invalidate the directly readable policy methods and exact XML below, but prohibit claiming complete source recovery.

## Decisive per-SIM policy

**The shipped dash carrier configuration treats the two Vietnamese SIMs differently.** It does not implement a blanket “CN phone disables all foreign-SIM5G” rule in this observed path.

| SIM PLMN | SAavailable | disableSA | Source |
|---|---:|---:|---|
| Viettel45204 | true | false | `xiaomi_carrier_sources/resources/assets/device/dash/res/xml/vendor_device.xml`195..200 explicitly overrides both keys |
| VinaPhone45202 | false | true | Common default in `resources/res/xml/vendor_miui.xml`32..36; dash230..232 only enables WFC, leaving SA defaults |

The dash file includes `assets/default/default_v2.9.8.xml`; its Vietnam blocks1598..1613 provide IMS settings but no SAoverride. The common vendor_miui Vietnam blocks10500/10863 similarly do not change SA. `xiaomi_carrier_sa_policy.json` retains exact default/Vietnam entries and XML/APK digests, independently reproducible by `extract_carrier_sa.py`.

`com.xiaomi.carrierconfig.DefaultCarrierConfigService.onLoadConfig()`236 calls `loadMiuiCarrierConfig()`157. In the ordinary packaged path it loads common vendor_miui, then device `ro.product.device` asset(default/include values then device overrides). Single MCC/MNC attributes are numerically normalized, so04and4 both match. COTA/opconfig, mi_ext and carrier APEX paths can override packaged data; parent runtime evidence independently reported exactly the table above for the live two SIMs. The effective provider package is `com.xiaomi.carrierconfig`, not merely generic AOSP CarrierConfig.apk.

## Exact downstream consequence

`FiveGManagerBase.fetchSaModeCarrierStatus()`899..944 reads:

- `carrier_sa_mode_available_bool` (fallbackfalse).
- `carrier_disable_sa_mode_bool` (fallback`Build.IS_INTERNATIONAL_BUILD`).
- System`5g_sa_mode_disabled` (default-1), whose explicit0/1 overrides the disable boolean.

The parent observed Systemoverride absent, Global`fiveg_network_mode=-1`, current data slot1(Vina), cloud enablefalse/disablefalse, carrieravailablefalse/carrierdisabledtrue. Substituting those values into `FiveGManagerBase.getFiveGNetworkMode()`1080..1090 deterministically returns0:

```text
if origMode!=-1 and(secretCodeSupported or carrierAvailable): returnorigMode
if cloudEnabled: return2
return(cloudDisabled or carrierDisabled)?0:2
```

`isUserFiveGSaEnabled()`1053 is exactly`mode==2`. `updateFiveGSaStatus()`979 applies mode to the default data slot; secondary slot uses dual-SA settings. `FiveGManager.execSetUserFiveGNetworkMode()`391 treats2asSAon. On MTK it invokes438onSet5GSaAtCommand:

- Non-SA-only device, enabled ->473`enableNsaAndEnableSA`: `AT+E5GOPT=7` or`7,2` if existing SAoption3 feature is set.
- Non-SA-only device, disabled ->487`enableNsaAndDisableSA`: `AT+E5GOPT=5`.
- SA-only device disabled uses`AT+E5GOPT=1`.

Thus the observed Vina policy disables **SA** while leaving **NSA enabled in this command path**. It is incorrect to call it proof that all5G/NRis disabled. It can prevent SAregistration, but does not by itself explain why the current LTE cell reports no EN-DC/NRavailability, nor prove that changing it produces NRregistration.

Viettel45204is explicitly allowedSA; its lack of NRcannot be attributed to this Vina-specific fallback. Actual default data slot, dual-SA policy, coverage, modem bands and network/SIM provisioning remain separate conditions.

## General NR mask gate already distinguished

Framework `aosp_sources/sources/com/android/internal/telephony/Phone.java`1774 intersects all allowed-network reason masks; if`mIsCarrierNrSupported`isfalse it clears NRbit524288, then1973intersects with modemRAFbefore sending the bitmap. `GsmCdmaPhone.java`3336 sets that support bit from nonempty`carrier_nr_availabilities_int_array`. Parent runtime found both USERmask916479(0xdfbff)already includes NR and default array[1,2]; a default array alone is not proof of every applied carrier overlay. The XiaomiSAcommand path above is separate from this broad NRmask.

## Region/cloud paths and bounded negative

XiaomiJAR `ModemServiceMoniter.notifyBuildRegion()`621..651 copies `ro.miui.build.region`to`persist.vendor.radio.build_region`through its hook then notifies common message113. That method contains no NRcountry blacklist. `ModemUtils.isChinaBuild()`3940/3950compares MIUI/vendor radio build region tocn; identifying a property consumer does not establish an NRrestriction.

`FiveGCloudController`can match model, version type, customizedregion and SIM operator/location. `addFiveGSaCloudStatus()`2471 populates SAstatus by operator; `getFiveGSaCloudStatus()`1438 gives `All`precedence, then exactSIMPLMN, else-1. Status0isdisabled,1enabled. Parent observed neither cloud enable nor disable for current data SIM, so this observed block comes from carrier policy rather than a matched cloudSAblock. No hard-coded45202/45204or blanketforeignSIMNRblock was found in the reviewed FiveGManager/FiveGManagerBase/FiveGCloudController methods.

The generic AOSP CarrierConfig.apk did not expose the decisive XiaomiSAoverride; its initial lack of Vietnam-specific assets must not be interpreted as a lack of effective per-SIM configuration.

## Builtin selector/secret-code guard is a separate restriction

The packaged common default vendor_miui.xml459..464 sets`config_support_secret_code_for_sa_switch=false`and`config_support_secret_code_for_sa_value=false`. No dashVietnam override was found. Stable-build helpers use these effective carrier keys despite their Java fallbacktrue; nonstable builds permit the helpers. This means the builtin secret code may be unavailable even when its handler exists. Read-only runtime guard values are required before proposing the corresponding test.

`FiveGUtils.shouldShow5GSaSwitch()`74 shows the selector if SAsupportedandcarrierSAavailable, otherwise requires secret-code visibility and support. Current Vina unavailable policy can hide the selector, while Viettel has an explicit availability override.

`MiuiLog.java`272..291 has the real7282583handler. It cycles Systemkey`5g_sa_mode_disabled`-1->1->0->-1, then explicitly refreshes carrier status and updates all slots. `MiuiLog.register()`628..630 registers action`android.provider.Telephony.SECRET_CODE`, URI scheme`android_secret_code`, exportedflag2. No direct ContentObserver for this key was established in FiveGManagerBase. Full conditional backup/test/rollback planning is in `SA_TEST_PLAN_NOT_EXECUTED.md`; it was not executed and requires consent.

## Read-only diagnostic route

The shipped manifest exposes `com.android.phone.TelephonyDebugService`with permission`android.permission.DUMP`. Itsdump delegates to framework DebugService and MiPhoneApp/TelephonyDelegate; `TelephonyDelegateImpl.java`322calls FiveGManagerBase.dump, which prints its local policy logs. A dump can corroborate recalculation without changing settings; sending the secret-code broadcast is state-changing and is not a read-only diagnostic.

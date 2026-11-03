# SA test plan — NOT EXECUTED

This document is a reviewable future test plan. No command below has been executed by this investigation. Root's current authorization remains read-only; consent is required before changing settings or sending the state-changing secret-code broadcast.

## What is established

Packaged Xiaomi carrier policy and parent runtime agree: dash/Viettel45204 allows SA, while current data SIM VinaPhone45202 inherits SA disabled. Mainline allowed-network masks already include NR. The proposed test only changes Xiaomi's SA policy outcome; it does not establish network coverage, accepted NR registration, RF-band support or SIM provisioning.

`MiuiLog.java`272..291 handles action `android.provider.Telephony.SECRET_CODE` with URI host7282583. `MiuiLog.register()`628..630 registers a dynamic exported receiver for scheme`android_secret_code`. Its guards are `MiuiPhoneUtils.is5gSaSupported()` (`ro.vendor.radio.5g >=2`) and `isSupportSecretCodeForSAValue()`.

The latter loops all slots and succeeds if any slot has ICC records loaded and permits `config_support_secret_code_for_sa_value`. Stable builds read that carrier boolean with fallbacktrue; nonstable builds returntrue. **The packaged common carrier default explicitly sets this keyfalse**, and no dash45202/45204 override was found. Therefore a stable device may reject this secret code; effective runtime guard must be checked before choosing that path. A fallbacktrue in Java is not proof of availability. `config_support_secret_code_for_sa_switch` is also packagedfalse.

The supported dialer equivalent is `*#*#7282583#*#*`, subject to exactly the same receiver guards. It is not a separate bypass.

## Preparation / backup (read-only)

Record the exact active Android user, current System key presence/value, and active data slot. Root reports System`5g_sa_mode_disabled`currently absent (`settings get` prints`null`). Also retain Global`fiveg_network_mode`, Global`dual_sa_enabled`, both per-SIM carrier SA booleans, both secret-code guard booleans, `ro.vendor.radio.5g`, and current radio registration. Avoid subscriber identifiers in the retained report.

Do not change data SIM, preferred-network masks, build-region properties, modem/NVRAM contents, firmware or LK during this test. An existing registered service dump and selected logs are sufficient to check whether Xiaomi's policy was recalculated.

## Path A — builtin receiver, ONLY if runtime guard permits it

Receiver transitions System`5g_sa_mode_disabled` as follows:

| Before (`getInt` default=-1) | After | Meaning |
|---|---:|---|
| absent or -1 | 1 | force SA disabled |
| 1 | 0 | force SA enabled |
| 0 | -1 | clear forced policy, return to carrier defaults |

Every accepted invocation explicitly runs `fetchSaModeCarrierStatus()` and `updateFiveGSaStatus()` for **all** slots. No direct ContentObserver for this System key was found in FiveGManagerBase; setting the value alone need not immediately apply it.

If starting from the backed-up absent key, two invocations are needed to reach0. The first temporarily forces SAoff for both slots; this brief interruption, including the already-enabled Viettel policy, must be part of the user's approval. Send one invocation, verify resulting key1, then send the second and verify0. Stop if either guard blocks it or observed state differs.

The candidate command for each invocation (future approval only) is:

```powershell
adb shell su -c 'am broadcast -a android.provider.Telephony.SECRET_CODE -d android_secret_code://7282583 -p com.android.phone'
```

This reaches the package's dynamic receiver, rather than an invented manifest component. Protected-broadcast delivery and actual receiver registration still must be verified from result/logs; a successful `am` command alone does not prove the handler ran.

After0, the expected data-slot decision is mode2: current original mode-1, cloud enable/disablefalse, and forced carrier-SA-disabledfalse. MTK `FiveGManager.execSetUserFiveGNetworkMode` maps mode2 to `AT+E5GOPT=7` or`7,2` depending on the existing SAoption3 feature. This enables SAandNSA in that code path. It is an expected command-path result, not a promise of NR attachment. Secondary-slot SA still obeys dual-SA and slot rules.

Rollback for original absent key: one further accepted invocation changes0->-1 and refreshes both slots to carrier policy; then delete the System key to restore actual absence (not just equivalent-1). For example, after approval and verifying the transition:

```powershell
adb shell su -c 'settings delete system 5g_sa_mode_disabled'
```

If the original key was present, restore its exact backed-up value instead. Do not apply the absent-key rollback recipe blindly to another initial state.

## Path B — if the builtin guard is false

Do not claim PathA works, do not change its carrier guard, and do not send additional unrelated secret codes. The official SA selector also depends on support/carrier availability or permitted secret-code visibility (`FiveGUtils.shouldShow5GSaSwitch()`); Vina's carrier-SA-availablefalse and secret-code guardsfalse can hide it.

A separate, explicitly approved fallback could write only System`5g_sa_mode_disabled=0` and apply it through a normal user reboot. Startup/carrier refresh reads that override independently of the secret-code guard. This avoids the temporary1 transition but adds a reboot and bypasses the UI's availability guard. Root must obtain consent specifically for this alternative; it is not authorized by this document.

The future setting change is only:

```powershell
adb shell su -c 'settings put system 5g_sa_mode_disabled 0'
```

Then the user performs a normal reboot, followed by read-only checks. Rollback for an originally absent key is `settings delete system 5g_sa_mode_disabled`, then normal reboot to recalculate carrier defaults. No service kill, raw AT injection, firmware flash or NV-partition write is included.

## Limits and checks

The app's own receiver/settings path updates modem runtime through its existing AT interface; underlying modem-side persistence is not established from this Java review. This plan contains no direct NV writes, but should not be described as proving all modem internal storage untouched.

Success criteria separate policy and reception: key0 and mode2/SAenabled command confirm policy application; NR-SA registration is a separate outcome. Unchanged LTE does not disprove policy application. Rollback should recover absent key and prior per-SIM policy values, not merely an icon. No change is necessary to already-allowed Viettel SA for this investigation.

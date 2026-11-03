package com.android.phone.utils;

import android.os.Bundle;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.IUtilityModule;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.util.EnumSet;
import java.util.Set;
import miui.os.Build;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes.dex */
public abstract class FiveGUtils {
    private static String LOG_TAG = "FiveGUtils";
    private static final Set SA_SCENES = EnumSet.of(NrSceneConstants$Scene.INIT, NrSceneConstants$Scene.SSR, NrSceneConstants$Scene.SPEED_TEST, NrSceneConstants$Scene.CALL, NrSceneConstants$Scene.SA_CDRX, NrSceneConstants$Scene.SA_WECHAT_VOIP, NrSceneConstants$Scene.SA_NIGHT_MODE, NrSceneConstants$Scene.SA_THERMAL_MODE, NrSceneConstants$Scene.SMART_NETWORK, NrSceneConstants$Scene.NETWORK_RECOVERY_MODE, NrSceneConstants$Scene.SMART_TIERED_POWER_SAVING, NrSceneConstants$Scene.IRAT_PINGPONG_MODE, NrSceneConstants$Scene.WECHAT_VIDEO_MTK, NrSceneConstants$Scene.SPECIAL_SIM, NrSceneConstants$Scene.SA_SMS, NrSceneConstants$Scene.WIFI_CALL, NrSceneConstants$Scene.NETWORK_RECOVERY_STRATEGY, NrSceneConstants$Scene.RECOVERY_NR, NrSceneConstants$Scene.DEPRI_SA_TEST, NrSceneConstants$Scene.STREAM_MEDIA_MTK, NrSceneConstants$Scene.GAME_RESIDENT_CELL, NrSceneConstants$Scene.CALL_FAILED);
    private static final Set NSA_SCENES = EnumSet.of(NrSceneConstants$Scene.SCREEN, NrSceneConstants$Scene.WWAN, NrSceneConstants$Scene.WLAN, NrSceneConstants$Scene.UPDATE, NrSceneConstants$Scene.GAME, NrSceneConstants$Scene.FIVEG_HOSTED_SPEED_MODE, NrSceneConstants$Scene.SLM_WLAN, NrSceneConstants$Scene.WHITE_LIST, NrSceneConstants$Scene.NSA_THERMAL_MODE, NrSceneConstants$Scene.NSA_SCG_POOR_MODE, NrSceneConstants$Scene.NSA_NETWORK_RECOVERY_STRATEGY, NrSceneConstants$Scene.ENDC_TEST);

    public static boolean isSupportSingleFiveG() {
        return false;
    }

    public static int getOtherSlotId(int i) {
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (i2 != i) {
                return i2;
            }
        }
        return -1;
    }

    public static int getViceSlotId() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != DefaultSimManager.getDefaultDataSlotId()) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isViceSlotActivated() {
        SubscriptionInfo subscriptionInfoForSlot;
        int viceSlotId = getViceSlotId();
        return (viceSlotId == -1 || (subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(viceSlotId)) == null || !subscriptionInfoForSlot.isActivated()) ? false : true;
    }

    public static boolean isSimDeactivated(int i) {
        SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
        return subscriptionInfoForSlot == null || !subscriptionInfoForSlot.isActivated();
    }

    public static boolean isAllSimActivated() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
            if (subscriptionInfoForSlot == null || !subscriptionInfoForSlot.isActivated()) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShow5GSaSwitch() {
        boolean zIs5gSaSupported = MiuiPhoneUtils.is5gSaSupported();
        FiveGManagerBase.getInstance();
        boolean zIs5GSaModeAvailableBySecretCode = FiveGManagerBase.is5GSaModeAvailableBySecretCode();
        FiveGManagerBase.getInstance();
        boolean zIs5GSaModeCarrierAvailable = FiveGManagerBase.is5GSaModeCarrierAvailable();
        boolean zShouldShow5GNetworkMode = shouldShow5GNetworkMode();
        boolean zIsSupportSecretCodeForSA = MiuiPhoneUtils.isSupportSecretCodeForSA(DefaultSimManager.getDefaultDataSlotId());
        log("shouldShow5GSaSwitch, is5gSaSupported: " + zIs5gSaSupported + ", is5GSaModeCarrierAvailable: " + zIs5GSaModeCarrierAvailable + ", is5GSaModeAvailableBySecretCode: " + zIs5GSaModeAvailableBySecretCode + ", shouldShow5GNetworkMode: " + zShouldShow5GNetworkMode + ", isSupportSecretCodeForSA: " + zIsSupportSecretCodeForSA);
        if (!zIs5gSaSupported) {
            return false;
        }
        if (zIs5GSaModeCarrierAvailable) {
            return true;
        }
        return zIs5GSaModeAvailableBySecretCode && zIsSupportSecretCodeForSA;
    }

    public static boolean shouldShow5GNetworkMode() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        boolean zIs5gSaSupported = MiuiPhoneUtils.is5gSaSupported();
        FiveGManagerBase.getInstance();
        boolean zIs5GSaModeCarrierAvailable = FiveGManagerBase.is5GSaModeCarrierAvailable();
        FiveGManagerBase.getInstance();
        boolean zIs5GSaModeAvailableBySecretCode = FiveGManagerBase.is5GSaModeAvailableBySecretCode();
        log("shouldShow5GNetworkMode, is5gSaSupported: " + zIs5gSaSupported + ", is5GSaModeCarrierAvailable: " + zIs5GSaModeCarrierAvailable + ", is5GSaModeAvailableBySecretCode: " + zIs5GSaModeAvailableBySecretCode);
        return zIs5gSaSupported && (zIs5GSaModeCarrierAvailable || zIs5GSaModeAvailableBySecretCode);
    }

    public static boolean isDualNrSupported() {
        return TelephonyManager.getDefault().isDualNrSupported();
    }

    public static boolean isDualSaSupported() {
        return TelephonyManager.getDefault().isDualSaSupported();
    }

    public static boolean isVoNRSupported() {
        return TelephonyManager.getDefault().isVoNRSupported();
    }

    public static boolean isVonrSwitchVisiable(int i) {
        boolean z = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_vonr_switch_visiable", 0) == 1;
        boolean z2 = MiuiPhoneUtils.is5gSaSupported() && (DefaultSimManager.getDefaultDataSlotId() == i || isDualSaSupported());
        Phone phone = MiuiPhoneUtils.getPhone(i);
        boolean z3 = phone != null && CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneGlobals.getInstance(), phone.getSubId(), "config_is_show_vonr_switch", false);
        StringBuilder sb = new StringBuilder("isVonrSwitchVisiable, slotId: ");
        sb.append(i);
        sb.append(", subId: ");
        sb.append(phone != null ? Integer.valueOf(phone.getSubId()) : "N/A");
        sb.append(", isVoNRSupported: ");
        sb.append(isVoNRSupported());
        sb.append(", isSaSupportSlot: ");
        sb.append(z2);
        sb.append(", isVonrVisibleBySecretCode: ");
        sb.append(z);
        sb.append(", isVonrVisibleByCarrier: ");
        sb.append(z3);
        log(sb.toString());
        return z2 && (z || (isVoNRSupported() && z3));
    }

    public static boolean shouldShowViceSaSwitch(int i) {
        return DefaultSimManager.getDefaultDataSlotId() != i && FiveGManagerBase.getInstance().isUserFiveGEnabled(i) && (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "vice_5g_sa_switch_visiable", 0) == 1) && MiuiPhoneUtils.isSupportSecretCodeForSA(i);
    }

    public static boolean isXmRadioSupported() {
        int halVersion = XmTelephonyManagerInner.getDefault().getHalVersion(DefaultSimManager.getDefaultDataSlotId());
        return (ModemProvider.IS_QCOM && halVersion >= 1) || (ModemProvider.IS_MTK && halVersion > 1);
    }

    public static int getTypeByScene(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return isSAScene(nrSceneConstants$Scene) ? 1 : 2;
    }

    private static boolean isSAScene(NrSceneConstants$Scene nrSceneConstants$Scene) {
        return SA_SCENES.contains(nrSceneConstants$Scene);
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    public static boolean isVonrSettingSupportedForMtk() {
        return isVoNRSupported() && SystemProperties.getInt("persist.vendor.vonr_setting_support", 0) == 1;
    }

    public static boolean isLastForcedRecoveryNr() {
        return SystemProperties.getBoolean("persist.radio.force.recovery.nr", false);
    }

    public static boolean isForcedRecoveryNr() {
        return isLastForcedRecoveryNr() && !isFiveGNetworkForceVisable();
    }

    public static boolean isFiveGNetWorkInVisible() {
        if (isFiveGNetworkForceVisable()) {
            return false;
        }
        boolean zIsFiveGInvisableByDefault = isFiveGInvisableByDefault();
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        boolean z = cloudData != null ? cloudData.getBoolean("5g_switch_invisible") : false;
        boolean z2 = cloudData != null ? cloudData.getBoolean("5g_switch_visible") : false;
        boolean zIsLastForcedRecoveryNr = isLastForcedRecoveryNr();
        log("isFiveGNetWorkInVisible, isInVisibleByCloud: " + z + ", isVisibleByCloud = " + z2 + ", isInvisbleByDevice = " + zIsFiveGInvisableByDefault + ", isLastForcedRecoveryNr = " + zIsLastForcedRecoveryNr);
        if (z) {
            return true;
        }
        if (z2) {
            return false;
        }
        return zIsLastForcedRecoveryNr || zIsFiveGInvisableByDefault;
    }

    public static boolean isFiveGInvisableByDefault() {
        return TelephonyManagerEx.getDefault().isHideFiveGAndNetwork();
    }

    public static boolean isNetworkModeInVisableByDevelopOption() {
        return !MiuiSettings.System.getBoolean(PhoneGlobals.getInstance().getContentResolver(), "enable_pref_network_type", false);
    }

    public static boolean isFiveGNetworkForceVisable() {
        return (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_network__visible", -1) == 1) || (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_cloud", -1) == 1) || (isFiveGInvisableByDefault() && Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_visible", -1) == 1);
    }
}

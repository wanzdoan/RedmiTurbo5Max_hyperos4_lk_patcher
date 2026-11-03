package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.phone.module.ITelephonyModule;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiImsPhoneUtils {
    public static boolean isCustForEsVodafone() {
        return TelephonyManager.isCustForEsVodafone();
    }

    public static boolean isCustForVodafoneDE(Phone phone) {
        if (!isCustForEsVodafone()) {
            return false;
        }
        String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(phone);
        boolean z = "26202".equals(operatorNumeric) || "26209".equals(operatorNumeric);
        Rlog.i("MiuiImsPhoneUtils", "isCustForVodafoneDE " + z + " numeric:" + operatorNumeric);
        return z;
    }

    public static boolean isCustForVodafoneUK(Phone phone) {
        if (isCustForEsVodafone()) {
            return isVodafoneUKSim(phone);
        }
        return false;
    }

    public static boolean isVodafoneUKSim(Phone phone) {
        String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(phone);
        boolean zEquals = "23415".equals(operatorNumeric);
        Rlog.i("MiuiImsPhoneUtils", "isCustForVodafoneUK " + zEquals + " numeric:" + operatorNumeric);
        return zEquals;
    }

    public static boolean isCbNoNeedPassword(Phone phone) {
        if (phone == null) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "is_call_barring_no_need_password", false);
    }

    public static boolean isCbOverImsNoNeedPassword(Phone phone, String str) {
        if (phone == null) {
            return false;
        }
        if (phone.isUtEnabled() && isCbNoNeedPassword(phone)) {
            return true;
        }
        if (phone.isImsRegistered()) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_callbarring_over_ims_no_need_password", false);
        }
        return false;
    }

    public static boolean isCustForOperatorVoWifiFeature(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_customize_for_operator_vowifi_feature", false);
    }

    public static boolean shouldShowWifiCallingDialog(Phone phone) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "show_wifi_calling_dialog", false);
        }
        return isCustForOperatorVoWifiFeature(phone) && MiuiPhoneUtils.isOrangePolandSim(phone);
    }

    public static boolean shouldShowWifiCallingNotification(Phone phone) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "show_wifi_calling_notification", false);
        }
        return (!isCustForOperatorVoWifiFeature(phone) || MiuiPhoneUtils.isOrangeFranceSim(phone) || MiuiPhoneUtils.isOrangePolandSim(phone)) ? false : true;
    }

    public static boolean shouldShowWifiCallingDialogOrNotification(Phone phone) {
        return shouldShowWifiCallingDialog(phone) || shouldShowWifiCallingNotification(phone);
    }

    public static boolean hideVolteOptionForOperatorCustimized(int i) {
        return ImsAdapterBase.getInstance().hideEnhanded4GLte(i);
    }

    public static void saveOmacpWifiState(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("omacp_vowifi_enabled", z);
        editorEdit.putBoolean("omacp_vowifi_enabled" + i, z);
        editorEdit.apply();
    }

    public static boolean showWifiCallSwitchView(Phone phone) {
        boolean z = false;
        if (phone == null) {
            return false;
        }
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        boolean zIsWfcEnabledByPlatform = ImsAdapterBase.getInstance().isWfcEnabledByPlatform(phoneGlobals, phone.getPhoneId());
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            if (zIsWfcEnabledByPlatform && !TelephonyCapabilities.slotNonsupportWifiCall(phone.getPhoneId())) {
                z = true;
            }
            zIsWfcEnabledByPlatform = z;
        }
        return (!ImsAdapterBase.getInstance().hideWfc(phoneGlobals, phone.getSubId())) & zIsWfcEnabledByPlatform;
    }

    public static void sendWifiEmergencyCallNotification(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || !SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
            return;
        }
        if (!supportEccVowifiNotification(phone)) {
            Log.i("MiuiImsPhoneUtils", "sendWifiEmergencyCallNotification not supported!!");
            return;
        }
        boolean z = phone.isImsRegistered() && phone.isWifiCallingEnabled();
        Log.i("MiuiImsPhoneUtils", "sendWifiEmergencyCallNotification shouldShow:" + z + " phoneId:" + i + " subId:" + phone.getSubId() + " imsRegistered:" + phone.isImsRegistered() + " wfcEnabled:" + phone.isWifiCallingEnabled());
        NotificationManager notificationManager = (NotificationManager) PhoneGlobals.getInstance().getSystemService("notification");
        if (z) {
            Notification.Builder builder = new Notification.Builder(PhoneGlobals.getInstance());
            builder.setContentTitle(getTextForId(phone, R.string.vowifi_emergency_notification_title)).setSmallIcon(R.drawable.stat_sys_vowifi_call).setAutoCancel(false).setOngoing(true);
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "show_vowifi_ecc_warning_dialog", true)) {
                builder.setStyle(new Notification.BigTextStyle(builder).bigText(getTextForId(phone, R.string.vowifi_emergency_notification_content))).setContentText(getTextForId(phone, R.string.vowifi_emergency_notification_content));
                if (!isWfcEccWarnDialogChecked(phone.getSubId())) {
                    builder.setContentIntent(createWifiEccWarningPendingIntent(i));
                }
            }
            MiuiNotificationMgrUtils.setChannelId(builder, "wfc");
            notificationManager.notify("wifi_call_ecc_notification", i, builder.build());
            return;
        }
        notificationManager.cancel("wifi_call_ecc_notification", i);
    }

    public static void forceDismissWifiEmergencyCallNotification(int i) {
        ((NotificationManager) PhoneGlobals.getInstance().getSystemService("notification")).cancel("wifi_call_ecc_notification", i);
    }

    private static CharSequence getTextForId(Phone phone, int i) {
        Resources resourcesForSubId;
        if (phone != null && phone.getContext() != null && SubscriptionManager.isValidSubscriptionId(phone.getSubId()) && (resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(phone.getContext(), phone.getSubId())) != null) {
            return resourcesForSubId.getText(i);
        }
        return PhoneGlobals.getInstance().getResources().getText(i);
    }

    private static PendingIntent createWifiEccWarningPendingIntent(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent errorDialogActivityIntent = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
        errorDialogActivityIntent.putExtra("dialog_type", 7);
        errorDialogActivityIntent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        errorDialogActivityIntent.setFlags(268435456);
        return PendingIntent.getActivity(phoneGlobals, 0, errorDialogActivityIntent, 201326592);
    }

    public static void updateEmergencyWarningDialogAndCheckState(Phone phone, boolean z) {
        if (phone != null && supportEccVowifiNotification(phone) && HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "show_vowifi_ecc_warning_dialog", true) && !isWfcEccWarnDialogChecked(phone.getSubId()) && z) {
            showEmergencyWarningDialog(phone.getPhoneId());
        }
    }

    public static void showEmergencyWarningDialog(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent errorDialogActivityIntent = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
        errorDialogActivityIntent.setFlags(268435456);
        errorDialogActivityIntent.putExtra("dialog_type", 7);
        errorDialogActivityIntent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(errorDialogActivityIntent);
        Rlog.i("MiuiImsPhoneUtils", "showEmergencyWarningDialog");
    }

    public static void saveWfcEccWarnDialogCheckedState(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("is_wifi_call_ecc_warning_checked" + i, z);
        editorEdit.apply();
    }

    public static boolean isWfcEccWarnDialogChecked(int i) {
        boolean z = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean("is_wifi_call_ecc_warning_checked" + i, false);
        Rlog.i("MiuiImsPhoneUtils", "isWfcEccWarnDialogChecked subId:" + i + " isChecked:" + z);
        return z;
    }

    public static boolean hasSimSupportWfc() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && ImsAdapterBase.getInstance().isWfcEnabledByPlatform(phoneGlobals, phone.getPhoneId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWifiCallingEnabled() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.isWifiCallingEnabled()) {
                return true;
            }
        }
        return false;
    }

    public static boolean supportSecretCodeForVolteAndWfc() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (MiuiPhoneUtils.getIccRecordsLoaded(i) && !supportSecretCodeForVolteAndWfc(i)) {
                Log.i("MiuiImsPhoneUtils", "supportSecretCodeForVolteAndWfc: SecretCode is not supported!");
                return false;
            }
        }
        return true;
    }

    private static boolean supportSecretCodeForVolteAndWfc(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && Build.IS_STABLE_VERSION) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_secret_code_for_volte_and_wfc", true);
        }
        return true;
    }

    public static boolean supportSecretCodeForVolteAndWfc(PersistableBundle persistableBundle) {
        if (persistableBundle == null || !Build.IS_STABLE_VERSION) {
            return true;
        }
        return persistableBundle.getBoolean("config_support_secret_code_for_volte_and_wfc");
    }

    public static boolean shouldShowWfcOptionWhenAirplaneOn(Phone phone) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (phone == null || MiuiPhoneUtils.isAirtelSim(MiuiPhoneUtils.getOperatorNumeric(phone))) {
            return false;
        }
        return ImsAdapterBase.getInstance().isWfcEnabledByPlatform(phoneGlobals, phone.getPhoneId());
    }

    public static boolean supportEccVowifiNotification(Phone phone) {
        return phone != null && CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_ecc_notification_for_wifi_calling", false);
    }

    public static void setPrefBoolean(String str, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean(str, z);
        editorEdit.apply();
    }

    public static boolean getPrefBoolean(String str, boolean z) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean(str, z);
    }

    public static boolean shouldShowSubDisplaynameWhenWifiCall(Phone phone) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_show_wifi_call_subscription_displayname", true);
    }

    public static boolean disableWfcWhenEnableWifiAp(int i) {
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneGlobals.getInstance(), i, "config_disable_wfc_when_enable_wifi_ap", false);
        }
        return false;
    }

    public static boolean shouldRestoreParticipantsPresentation(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_restore_participants_presentation", false);
    }

    public static boolean dismissCountryISOAfterConference(Phone phone) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "config_dismiss_countryiso_after_conference", false);
        }
        if (TelephonyManagerEx.getDefault().isRegionForTaiWan()) {
            return true;
        }
        return phone != null && CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_dismiss_countryiso_after_conference", false);
    }

    public static boolean isRatLTE(Phone phone) {
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        if (serviceState == null) {
            return false;
        }
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        int rilVoiceRadioTechnology = serviceState.getRilVoiceRadioTechnology();
        return rilDataRadioTechnology == 14 || rilDataRadioTechnology == 19 || rilVoiceRadioTechnology == 14 || rilVoiceRadioTechnology == 19;
    }

    public static boolean isRatNr(Phone phone) {
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        if (serviceState == null) {
            return false;
        }
        return serviceState.getRilDataRadioTechnology() == 20 || serviceState.getRilVoiceRadioTechnology() == 20;
    }

    public static boolean isClaroWifiCallDialogChecked(int i) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean("claro_wifi_call_dialog_checked_" + i, false);
    }

    public static void saveClaroWifiCallDialogCheckedState(int i, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit().putBoolean("claro_wifi_call_dialog_checked_" + i, z).apply();
    }
}

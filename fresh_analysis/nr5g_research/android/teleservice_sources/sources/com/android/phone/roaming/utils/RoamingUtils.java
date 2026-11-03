package com.android.phone.roaming.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.ims.ImsException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Iterator;
import miui.telephony.FeatureConfiguration;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class RoamingUtils {
    private static String TAG = "RoamingUtils";

    public static boolean isRoamingOptimizationSupported(Context context) {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_ROAMING_OPTIMIZATION);
    }

    public static Boolean getRoamingVtDisabledState(int i) {
        boolean z = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean("roaming_vt_ims_disabled" + i, false);
        Rlog.d(TAG, "getRoamingVtEnabledState subId=" + i + ", result=" + z);
        return Boolean.valueOf(z);
    }

    public static void saveRoamingVtDisabledState(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("roaming_vt_ims_disabled" + i, z);
        editorEdit.apply();
    }

    public static String getNetworkOperatorNumeric(ServiceState serviceState) {
        Rlog.d(TAG, "getNetworkOperatorNumeric ss:" + serviceState);
        Iterator<NetworkRegistrationInfo> it = serviceState.getNetworkRegistrationInfoList().iterator();
        while (it.hasNext()) {
            String registeredPlmn = it.next().getRegisteredPlmn();
            if (!TextUtils.isEmpty(registeredPlmn)) {
                Rlog.i(TAG, "getNetworkOperatorNumeric rPlmn:" + registeredPlmn);
                return registeredPlmn;
            }
        }
        return null;
    }

    public static int getSubId(int i) {
        return SubscriptionManager.getSubscriptionId(i);
    }

    public static String getPhoneNumber(int i) {
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot == null) {
            return null;
        }
        return availableSubscriptionInfoBySlot.getDisplayNumber();
    }

    public static boolean isSameNumber(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        return TextUtils.equals(PhoneNumberUtils.stripSeparatorsAndCountryCode(str), PhoneNumberUtils.stripSeparatorsAndCountryCode(str2));
    }

    public static boolean getVoiceRoaming(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || phone.getServiceState() == null || phone.getServiceState().getState() != 0) {
            return false;
        }
        return phone.getServiceState().getVoiceRoaming();
    }

    public static boolean isVoiceInService(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return (phone == null || phone.getServiceState() == null || phone.getServiceState().getVoiceRegState() != 0) ? false : true;
    }

    public static void saveRoamingCFUStatus(int i, boolean z, String str) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("cfu_status_key" + i, z);
        editorEdit.putString("cfu_number_key" + i, str);
        editorEdit.apply();
    }

    public static boolean getRoamingCFUStatusBySlot(int i) {
        return getRoamingCFUStatusBySub(getSubId(i));
    }

    public static boolean getRoamingCFUStatusBySub(int i) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean("cfu_status_key" + i, false);
    }

    public static String getRoamingCFUNumber(int i) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getString("cfu_number_key" + i, "");
    }

    public static boolean getCFUIndicator(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return false;
        }
        return phone.getCallForwardingIndicator();
    }

    public static boolean isRoamingCFUCall(int i, String str) {
        if (TextUtils.isEmpty(str) || !getRoamingCFUStatusBySub(i) || !isSameNumber(getRoamingCFUNumber(i), str)) {
            return false;
        }
        Rlog.d(TAG, "Unconditionally forward calls while roaming!");
        return true;
    }

    public static void trackEvent(int i, String str, int i2) {
        String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
        HashMap map = new HashMap();
        map.put("NetworkPlmn", networkNumeric);
        map.put("SimOperator", simOperatorNumeric);
        if (i2 != -1) {
            map.put("exception_code", Integer.valueOf(i2));
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, map);
    }

    public static int getExceptionCode(Throwable th) {
        if (th == null) {
            return -1;
        }
        if (th instanceof CommandException) {
            return ((CommandException) th).getCommandError().ordinal();
        }
        if (th instanceof ImsException) {
            return ((ImsException) th).getCode();
        }
        return -1;
    }

    public static String getOperatorMcc(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 3) {
            return "";
        }
        return str.substring(0, 3);
    }

    public static void updateRoamingCFNotifyRecord(Context context, String str) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean(str, true);
        editorEdit.apply();
    }

    public static boolean hasRoamingCFNotifyRecord(Context context, String str) {
        if (context == null) {
            return false;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(str, false);
    }
}

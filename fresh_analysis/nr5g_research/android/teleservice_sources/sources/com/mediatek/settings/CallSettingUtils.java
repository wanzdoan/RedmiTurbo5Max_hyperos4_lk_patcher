package com.mediatek.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.Preference;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;
import com.android.phone.CdmaCallForwardOptions;
import com.android.phone.CdmaCallOptions;
import com.android.phone.GsmUmtsAdditionalCallOptions;
import com.android.phone.GsmUmtsCallBarringOptions;
import com.android.phone.GsmUmtsCallForwardOptions;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallSettingUtils {
    private static final Map mOPMap = new HashMap() { // from class: com.mediatek.settings.CallSettingUtils.1
        {
            put(OPID.OP01, Arrays.asList("46000", "46002", "46004", "46007", "46008"));
            put(OPID.OP02, Arrays.asList("46001", "46006", "46009", "45407"));
            put(OPID.OP09, Arrays.asList("45502", "46003", "46011", "46012", "45507"));
        }
    };
    private static final String[] PROPERTY_SIM_PIN2_RETRY = {"vendor.gsm.sim.retry.pin2", "vendor.gsm.sim.retry.pin2.2", "vendor.gsm.sim.retry.pin2.3", "vendor.gsm.sim.retry.pin2.4"};
    private static final String[] PROPERTY_SIM_PUK2_RETRY = {"vendor.gsm.sim.retry.puk2", "vendor.gsm.sim.retry.puk2.2", "vendor.gsm.sim.retry.puk2.3", "vendor.gsm.sim.retry.puk2.4"};

    public enum DialogType {
        NONE,
        DATA_OPEN,
        DATA_TRAFFIC,
        DATA_ROAMING,
        CDMA_DATA_TRAFFIC
    }

    public enum OPID {
        OP01,
        OP02,
        OP09
    }

    public static boolean isMtkDualMicSupport() {
        AudioManager audioManager = (AudioManager) PhoneGlobals.getInstance().getSystemService("audio");
        if (audioManager == null) {
            return false;
        }
        String parameters = audioManager.getParameters("MTK_DUAL_MIC_SUPPORT");
        log("isMtkDualMicSupport: " + parameters);
        return parameters.equalsIgnoreCase("MTK_DUAL_MIC_SUPPORT=true");
    }

    public static void setDualMicMode(String str) {
        Context applicationContext = PhoneGlobals.getInstance().getApplicationContext();
        if (applicationContext == null) {
            return;
        }
        ((AudioManager) applicationContext.getSystemService("audio")).setParameters("Enable_Dual_Mic_Setting=" + str);
        log("setDualMicMode finish: " + str);
    }

    public static boolean isDualMicModeEnabled() {
        AudioManager audioManager;
        Context applicationContext = PhoneGlobals.getInstance().getApplicationContext();
        if (applicationContext != null && (audioManager = (AudioManager) applicationContext.getSystemService("audio")) != null) {
            String parameters = audioManager.getParameters("Get_Dual_Mic_Setting");
            log("getDualMicMode(): state: " + parameters);
            if (parameters.equalsIgnoreCase("Get_Dual_Mic_Setting=1")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHacSupport() {
        AudioManager audioManager;
        Context applicationContext = PhoneGlobals.getInstance().getApplicationContext();
        if (applicationContext == null || (audioManager = (AudioManager) applicationContext.getSystemService("audio")) == null) {
            return false;
        }
        String parameters = audioManager.getParameters("GET_HAC_SUPPORT");
        log("hac support: " + parameters);
        return "GET_HAC_SUPPORT=1".equals(parameters);
    }

    private static void log(String str) {
        Log.d("CallSettingUtils", str);
    }

    public static boolean isPhoneBookReady(Context context, int i) {
        boolean zIsPhbReady;
        IMtkTelephonyEx iMtkTelephonyExAsInterface = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (iMtkTelephonyExAsInterface != null) {
            try {
                zIsPhbReady = iMtkTelephonyExAsInterface.isPhbReady(i);
                try {
                    Log.d("CallSettingUtils", "isPhoneBookReady:" + zIsPhbReady + ", subId:" + i);
                } catch (RemoteException e) {
                    e = e;
                    Log.e("CallSettingUtils", "isPhoneBookReady catch exception:");
                    e.printStackTrace();
                }
            } catch (RemoteException e2) {
                e = e2;
                zIsPhbReady = false;
            }
        } else {
            zIsPhbReady = false;
        }
        if (!zIsPhbReady) {
            Toast.makeText(context, context.getString(R.string.fdn_phone_book_busy), 0).show();
        }
        return zIsPhbReady;
    }

    /* JADX WARN: Code duplicated, block: B:29:0x00b8  */
    public static void showDialogTips(Context context, int i, DialogType dialogType, Preference preference) {
        Intent intent;
        if (dialogType == DialogType.DATA_TRAFFIC) {
            log("showDialogTips preference: " + preference);
            if (preference != null) {
                if (preference.getKey().equals("call_forwarding_key")) {
                    intent = new Intent(context, (Class<?>) GsmUmtsCallForwardOptions.class);
                } else if (preference.getKey().equals("call_barring_key")) {
                    intent = new Intent(context, (Class<?>) GsmUmtsCallBarringOptions.class);
                } else if (preference.getKey().equals("additional_gsm_call_settings_key")) {
                    intent = new Intent(context, (Class<?>) GsmUmtsAdditionalCallOptions.class);
                } else {
                    intent = new Intent(context, (Class<?>) GsmUmtsAdditionalCallOptions.class);
                }
                SubscriptionInfoHelper.addExtrasToIntent(intent, SubscriptionManager.from(context).getActiveSubscriptionInfo(i));
            } else {
                intent = null;
            }
        } else if (dialogType == DialogType.CDMA_DATA_TRAFFIC) {
            log("showDialogTips cdma preference: " + preference);
            if (preference != null) {
                if (preference.getKey().equals("call_forwarding_key")) {
                    intent = new Intent(context, (Class<?>) CdmaCallForwardOptions.class);
                } else if (preference.getKey().equals("button_cdma_more_expand_key")) {
                    intent = new Intent(context, (Class<?>) CdmaCallOptions.class);
                } else {
                    intent = new Intent(context, (Class<?>) CdmaCallOptions.class);
                }
                SubscriptionInfoHelper.addExtrasToIntent(intent, SubscriptionManager.from(context).getActiveSubscriptionInfo(i));
            } else {
                intent = null;
            }
        } else {
            intent = null;
        }
        MobileDataDialogFragment.show(intent, dialogType, i, ((Activity) context).getFragmentManager());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v9 */
    public static int getPin2RetryNumber(int i) {
        if (!PhoneUtils.isValidSubId(i)) {
            log("getPin2RetryNumber invalid subId: " + i);
            return -1;
        }
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        log("getPin2RetryNumber subId: " + i + ",Slot: " + slotIndex);
        ?? r4 = 0;
        try {
            if (TelephonyUtils.isGeminiProject()) {
                String[] strArr = PROPERTY_SIM_PIN2_RETRY;
                if (slotIndex < strArr.length) {
                    r4 = strArr[slotIndex];
                } else {
                    Log.w("CallSettingUtils", "getPin2RetryNumber: Error happened!");
                    r4 = strArr[0];
                }
            } else {
                r4 = PROPERTY_SIM_PIN2_RETRY[0];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log("getPin2RetryNumber exception: " + e.getMessage());
            r4 = PROPERTY_SIM_PIN2_RETRY[r4];
        }
        return SystemProperties.getInt((String) r4, -1);
    }

    public static String getPinPuk2RetryLeftNumTips(Context context, int i, boolean z) {
        int puk2RetryNumber;
        if (!PhoneUtils.isValidSubId(i)) {
            log("getPinPuk2RetryLeftNumTips inValid SubId: " + i);
            return " ";
        }
        if (z) {
            puk2RetryNumber = getPin2RetryNumber(i);
        } else {
            puk2RetryNumber = getPuk2RetryNumber(i);
        }
        log("getPinPuk2RetryLeftNumTips retry count: " + puk2RetryNumber + ",isPin: " + z);
        return puk2RetryNumber != -1 ? context.getString(R.string.retries_left, Integer.valueOf(puk2RetryNumber)) : " ";
    }

    public static int getPuk2RetryNumber(int i) {
        String str;
        if (!PhoneUtils.isValidSubId(i)) {
            log("getPuk2RetryNumber inValid SubId: " + i);
            return -1;
        }
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        log("getPuk2RetryNumber subId: " + i + ",Slot: " + slotIndex);
        if (TelephonyUtils.isGeminiProject()) {
            if (slotIndex < PROPERTY_SIM_PIN2_RETRY.length) {
                str = PROPERTY_SIM_PUK2_RETRY[slotIndex];
            } else {
                Log.w("CallSettingUtils", "getPuk2RetryNumber: Error happened!");
                str = PROPERTY_SIM_PUK2_RETRY[0];
            }
        } else {
            str = PROPERTY_SIM_PUK2_RETRY[0];
        }
        return SystemProperties.getInt(str, -1);
    }

    public static void sensitiveLog(String str, String str2, String str3) {
        Log.d(str, str2 + toSafeString(str3));
    }

    public static String toSafeString(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            sb.append('x');
        }
        return sb.toString();
    }
}

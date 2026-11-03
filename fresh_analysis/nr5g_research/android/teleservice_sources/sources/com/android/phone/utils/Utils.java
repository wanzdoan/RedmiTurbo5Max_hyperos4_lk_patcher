package com.android.phone.utils;

import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Locale;
import miui.os.Build;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.ServiceProviderUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils {
    public static final boolean IS_MIUI_LITE_DEVICE = isMiuiLiteDevice();

    public interface AiCallback {
        void onComplete(boolean z);
    }

    public interface Dual4GCallBack {
        void onSetDual4GResult(boolean z, int i);
    }

    private static boolean isCfEnable(int i) {
        return i == 1 || i == 3;
    }

    public static boolean isSupportMiMobile(Context context) {
        return false;
    }

    public class Cfu {
        public final Message mOnComplete;
        public final int mServiceClass;
        public final String mSetCfNumber;

        public Cfu(String str, Message message, int i) {
            this.mSetCfNumber = str;
            this.mOnComplete = message;
            this.mServiceClass = i;
        }
    }

    public static Message handleGetCFMessage(int i, Message message) {
        return i == 0 ? message.getTarget().obtainMessage(13, message) : message;
    }

    public static Message handleSetCFMessage(int i, int i2, String str, int i3, Message message) {
        if (i2 != 0) {
            return message;
        }
        return message.getTarget().obtainMessage(12, isCfEnable(i) ? 1 : 0, 0, new Cfu(str, message, i3));
    }

    public static boolean isLanguageOfRTL() {
        String displayName = Locale.getDefault().getDisplayName();
        if (TextUtils.isEmpty(displayName)) {
            return false;
        }
        byte directionality = Character.getDirectionality(displayName.charAt(0));
        return directionality == 1 || directionality == 2;
    }

    public static String localizeNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isDigit(charArray[i])) {
                sb.append(String.format("%d", Integer.valueOf(Character.getNumericValue(charArray[i]))));
            } else {
                sb.append(charArray[i]);
            }
        }
        return BidiFormatter.getInstance().unicodeWrap(sb.toString(), TextDirectionHeuristics.LTR);
    }

    public static boolean isInstalled(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationInfo(str, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getAiVersionCode(Context context) {
        Bundle bundle;
        int i = 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.xiaomi.aiasst.service", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                i = bundle.getInt("support_incallui_place", 0);
            }
            Log.i("AiAssistant", "getAiVersionCode=" + i);
            return i;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("AiAssistant", "Failed to get ai version code.");
            return i;
        }
    }

    public static boolean isAiAvailable(Context context) throws Throwable {
        boolean zIsInstalled = isInstalled(context, "com.xiaomi.aiasst.service");
        Log.i("AiAssistant", "isAiInstalled=" + zIsInstalled);
        boolean z = false;
        if (!zIsInstalled) {
            return false;
        }
        boolean z2 = getAiVersionCode(context) >= 1;
        Log.i("AiAssistant", "supportAi=" + z2);
        if (!z2) {
            return false;
        }
        ContentProviderClient contentProviderClient = null;
        try {
            try {
                ContentProviderClient contentProviderClientAcquireUnstableContentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient(Uri.parse("content://com.xiaomi.aiasst.service.aicall.provider"));
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    try {
                        Bundle bundleCall = contentProviderClientAcquireUnstableContentProviderClient.call("GET_AICALL_AVAILABLE", null, null);
                        if (bundleCall == null) {
                            Log.i("AiAssistant", "Bundle is null.");
                        } else {
                            int i = bundleCall.getInt("KEY_STATUS_CODE");
                            Log.i("AiAssistant", "code=" + i);
                            if (i != 2) {
                                z = true;
                            }
                        }
                    } catch (Exception e) {
                        e = e;
                        contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                        Log.e("AiAssistant", "Get ai status failed.", e);
                        if (contentProviderClient != null) {
                            contentProviderClient.close();
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                        if (contentProviderClient != null) {
                            contentProviderClient.close();
                        }
                        throw th;
                    }
                }
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    contentProviderClientAcquireUnstableContentProviderClient.close();
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static boolean supportXiaoAi(Context context) {
        int aiVersionCode = getAiVersionCode(context);
        return aiVersionCode >= 6 && aiVersionCode < 8;
    }

    public static boolean isAiAssistAnswerSupported(Context context) {
        Bundle bundle;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo("com.android.incallui", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo("com.android.contacts", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            Bundle bundle2 = applicationInfo.metaData;
            return bundle2 != null && bundle2.getInt("incall_ai_assist_answer", 0) >= 1 && (bundle = applicationInfo2.metaData) != null && bundle.getInt("support_ai_call", 0) >= 1;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("AiAssistant", "Failed to get AI assist answer support status.");
            return false;
        }
    }

    public static void isAiAvailableAsync(final Context context, final AiCallback aiCallback) {
        new AsyncTask() { // from class: com.android.phone.utils.Utils.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... voidArr) {
                return Boolean.valueOf(Utils.isAiAvailable(context));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                AiCallback aiCallback2 = aiCallback;
                if (aiCallback2 != null) {
                    aiCallback2.onComplete(bool.booleanValue());
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static boolean isNightMode(Context context) {
        Configuration configuration;
        return (context == null || (configuration = context.getResources().getConfiguration()) == null || (configuration.uiMode & 48) != 32) ? false : true;
    }

    public static boolean isTalkBackEnabled(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        return accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
    }

    public static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    public static boolean isSupportSensorAnswer(Context context) {
        return ((SensorManager) context.getSystemService("sensor")).getDefaultSensor(33171063) != null;
    }

    public static Intent getIntentForVirtualSim(String str) {
        Intent intent = new Intent();
        str.hashCode();
        switch (str) {
            case "pref_sim_roaming_key":
                intent.setComponent(new ComponentName("com.miui.virtualsim", "com.miui.virtualsim.ui.MainActivity"));
                break;
            case "pref_mi_mobile_key":
                intent.setPackage("com.miui.virtualsim");
                intent.setData(Uri.parse("com.miui.virtualsim://main?url=virtualsim://eventWebView?activityUrl=https://partner.10046.mi.com/miMobileOrder/card_sell?channel=b20210406"));
                break;
            case "pref_mi_sim_key":
                intent.setComponent(new ComponentName("com.miui.virtualsim", "com.miui.mimobile.ui.MmRouterActivity"));
                break;
        }
        if (!Build.IS_TABLET) {
            intent.addFlags(268435456);
        }
        return intent;
    }

    public static boolean isBCLStrategyEnable() {
        return SystemProperties.getBoolean("persist.vendor.low.cutoff", false);
    }

    private static boolean isMiuiLiteDevice() {
        Boolean boolValueOf = Boolean.FALSE;
        try {
            boolValueOf = Boolean.valueOf(PhoneGlobals.getInstance().getResources().getBoolean(R.bool.is_support_lite));
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return boolValueOf.booleanValue();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
        }
        return bArr;
    }

    public static String bytesToHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(cArr[(b & 240) >>> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    public static String intToHexString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(i).toUpperCase(Locale.ENGLISH));
        if (sb.length() % 2 != 0) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    public static boolean isGameMode(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "gb_handsfree", 0) == 1;
    }

    public static boolean isSupportFlashlight(Context context) {
        return context != null && context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public static String getContentDescriptionForNumber(String str) {
        if (TextUtils.isEmpty(str) || (!PhoneNumberUtils.isGlobalPhoneNumber(str) && !PhoneNumberUtils.isChinaMobileNumber(str))) {
            return str;
        }
        CharSequence charSequenceCreateTtsSpannable = PhoneNumberUtils.createTtsSpannable(str);
        return charSequenceCreateTtsSpannable != null ? charSequenceCreateTtsSpannable.toString() : "";
    }

    public class UPLMNInfoWithEf {
        private int mNetworkMode;
        private String mOperatorAlphaName;
        private String mOperatorNumeric;
        private int mPriority;

        public String getOperatorNumeric() {
            return this.mOperatorNumeric;
        }

        public String getOperatorAlphaNumeric() {
            return TextUtils.isEmpty(this.mOperatorAlphaName) ? this.mOperatorNumeric : this.mOperatorAlphaName;
        }

        public int getNetworkMode() {
            return this.mNetworkMode;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public void setOperatorNumeric(String str) {
            this.mOperatorNumeric = str;
        }

        public void setPriority(int i) {
            this.mPriority = i;
        }

        public UPLMNInfoWithEf(int i, String str, int i2, int i3) {
            this.mOperatorNumeric = str;
            this.mNetworkMode = i2;
            this.mPriority = i3;
            this.mOperatorAlphaName = ServiceProviderUtils.get(str, i);
        }

        public String toString() {
            return "UPLMNInfoWithEf: " + this.mOperatorNumeric + "/" + this.mNetworkMode + "/" + this.mPriority;
        }
    }
}

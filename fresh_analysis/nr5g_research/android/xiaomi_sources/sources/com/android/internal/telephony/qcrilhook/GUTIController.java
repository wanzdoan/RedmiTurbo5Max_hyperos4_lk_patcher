package com.android.internal.telephony.qcrilhook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;

/* JADX INFO: loaded from: classes.dex */
public class GUTIController {
    public static final boolean IS_PROJECT_SUPPORT_GUTI;
    private static final String PREFERENCE_KEY_VERSION = "version";
    private static final String PREFERENCE_REMOVE_GUTI = "remove_guti";
    private static final String TAG = GUTIController.class.getSimpleName();
    private static final String THREAD_NAME_QC_GUTI = "THREAD_QC_GUTI";
    private static volatile boolean[] sPurgeGutiRevoking;

    static {
        IS_PROJECT_SUPPORT_GUTI = "jp_sb".equals(SystemProperties.get("ro.miui.customized.region", "")) && (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING);
        sPurgeGutiRevoking = new boolean[]{false, false};
    }

    public static GUTIController getInstance(Context context) {
        return null;
    }

    private GUTIController(Context context) {
    }

    public void initQcGUTIHandler(int phoneId, CommandsInterface ci) {
    }

    public void start(int phoneId) {
    }

    private static void saveVersion(Context context, int id) {
        String currentVersion = Build.VERSION.INCREMENTAL;
        if (TextUtils.isEmpty(currentVersion)) {
            loge("saveVersion Error: currentVersion is Empty");
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_REMOVE_GUTI, 0);
        if (sp == null) {
            loge("saveVersion Error: sp is Empty");
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("version" + id, currentVersion);
        editor.commit();
        logd("save version" + id + ": " + currentVersion);
    }

    public void removeQcGUTIHandler(int phoneId) {
    }

    private void dispose() {
    }

    public static boolean needWork(Context context, int phoneId) {
        return false;
    }

    private static boolean isOTAUpdate(Context context, int id) {
        String currentVersion = Build.VERSION.INCREMENTAL;
        if (TextUtils.isEmpty(currentVersion)) {
            loge("currentVersion is Empty");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_REMOVE_GUTI, 0);
        if (sp == null) {
            loge("sp is Empty");
            return false;
        }
        String version = sp.getString("version" + id, "");
        if (version != null && !currentVersion.equals(version)) {
            logd("version" + id + " update from " + version + " to " + currentVersion);
            return true;
        }
        logd("version" + id + " not change");
        return false;
    }

    public void setRadioPowerOn(int phoneId) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void purgeGuti(final Context context, String str, final int i) {
        if (HyperOSCustFeatureResolve.getBoolean("config_device_purge_guti", false) && IS_PROJECT_SUPPORT_GUTI) {
            if ((i != 0 && i != 1) || context == null || TextUtils.isEmpty(str) || sPurgeGutiRevoking[i] || PhoneFactory.getPhone(i) == null) {
                return;
            }
            final int subscriptionId = SubscriptionManager.getSubscriptionId(i);
            boolean zIsActiveSubId = SubscriptionManager.from(context).isActiveSubId(subscriptionId);
            Object[] objArr = TelephonyManager.getSimStateForSlotIndex(i) == 2;
            Object[] objArr2 = TelephonyManager.getSimStateForSlotIndex(i) == 3;
            logd("phoneId:" + i + ",subId:" + subscriptionId + ",active:" + (zIsActiveSubId ? 1 : 0) + ",pin:" + (objArr != false ? 1 : 0) + ",puk:" + (objArr2 == true ? 1 : 0) + ",reason:" + str);
            if (zIsActiveSubId && objArr == false && objArr2 == false && isOTAUpdate(context, subscriptionId) && "addSubInfo".equals(str)) {
                sPurgeGutiRevoking[i] = true;
                new Thread(new Runnable() { // from class: com.android.internal.telephony.qcrilhook.GUTIController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        GUTIController.lambda$purgeGuti$0(i, context, subscriptionId);
                    }
                }).start();
            }
        }
    }

    static /* synthetic */ void lambda$purgeGuti$0(int phoneId, Context context, int subId) {
        try {
            PhoneFactory.getPhone(phoneId).setRadioPower(false);
            Thread.sleep(500L);
            onHookUimPurgeGutiReq(context, phoneId, subId);
            Thread.sleep(500L);
            PhoneFactory.getPhone(phoneId).setRadioPower(true);
        } catch (Exception e2) {
            Thread.currentThread().interrupt();
            logd("runtimeException e2," + e2);
        }
    }

    private static boolean onHookUimPurgeGutiReq(Context context, int phoneId, int subId) {
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone != null) {
            phone.invokeOemRilRequestStrings(new String[]{"AT+ECMSID=7", "+ECMSID"}, (Message) null);
            saveVersion(context, subId);
            return true;
        }
        return true;
    }

    private static void logd(String msg) {
        Rlog.d(TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(TAG, msg);
    }
}

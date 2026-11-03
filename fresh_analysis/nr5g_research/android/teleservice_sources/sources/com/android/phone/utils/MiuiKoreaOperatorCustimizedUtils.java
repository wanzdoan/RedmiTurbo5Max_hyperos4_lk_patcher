package com.android.phone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiKoreaOperatorCustimizedUtils {
    private static final boolean IS_GLOBAL_BUILD = TextUtils.equals("global", SystemProperties.get("ro.miui.build.region", ""));
    private static final boolean IS_KR_BUILD = TextUtils.equals("kr", TelephonyManager.REGION);

    public static boolean isCustForKrOps() {
        return isCustForKrSkt() || isCustForKrKt() || isCustForKrLgu();
    }

    public static boolean isCustForKrSkt() {
        return TelephonyManager.isCustForKrSkt();
    }

    public static boolean isCustForKrKt() {
        return TelephonyManager.isCustForKrKt();
    }

    public static boolean isCustForKrLgu() {
        return TelephonyManager.isCustForKrLgu();
    }

    public static boolean isKrSim(Phone phone) {
        return isKrKtSim(phone) || isKrLguSim(phone) || isKrSktSim(phone);
    }

    public static boolean isKrKtSim(Phone phone) {
        return "45008".equals(MiuiPhoneUtils.getOperatorNumeric(phone));
    }

    public static boolean isKrSktSim(Phone phone) {
        return "45005".equals(MiuiPhoneUtils.getOperatorNumeric(phone));
    }

    public static boolean isKrLguSim(Phone phone) {
        return "45006".equals(MiuiPhoneUtils.getOperatorNumeric(phone));
    }

    public static String getCustomerServiceNumber() {
        if (isCustForKrKt()) {
            return "tel:+82221900901";
        }
        if (isCustForKrSkt()) {
            return "tel:+82263439000";
        }
        if (isCustForKrLgu()) {
            return "tel:+82234167010";
        }
        return "";
    }

    public static String getRoamingPackageLink() {
        if (isCustForKrKt()) {
            return "http://m.ktroaming.kt.com";
        }
        if (isCustForKrSkt()) {
            return "http://m.troaming.co.kr";
        }
        if (isCustForKrLgu()) {
            return "http://m.roaming.uplus.co.kr";
        }
        return "";
    }

    public static void showRoamingPackage(Context context) {
        if (context == null) {
            Log.w("MiuiKoreaOperatorCustimizedUtils", "showRoamingPackage: context is null");
            return;
        }
        String roamingPackageLink = getRoamingPackageLink();
        if (roamingPackageLink == null || roamingPackageLink.isEmpty()) {
            Log.w("MiuiKoreaOperatorCustimizedUtils", "showRoamingPackage: link is empty");
            return;
        }
        try {
            Uri uri = Uri.parse(roamingPackageLink);
            Log.i("MiuiKoreaOperatorCustimizedUtils", "uri: " + uri.toString());
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("MiuiKoreaOperatorCustimizedUtils", "Failed to start roaming package activity", e);
        }
    }

    public static void dialCustomerService(Context context) {
        if (context == null) {
            Log.w("MiuiKoreaOperatorCustimizedUtils", "dialCustomerService: context is null");
            return;
        }
        String customerServiceNumber = getCustomerServiceNumber();
        if (customerServiceNumber == null || customerServiceNumber.isEmpty()) {
            Log.w("MiuiKoreaOperatorCustimizedUtils", "dialCustomerService: number is empty");
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.CALL");
            intent.setData(Uri.parse(customerServiceNumber));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("MiuiKoreaOperatorCustimizedUtils", "Failed to start call activity", e);
        }
    }
}

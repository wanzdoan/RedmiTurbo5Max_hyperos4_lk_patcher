package com.mediatek.settings.cdma;

import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyUtilsEx {
    private static final boolean DBG;

    static {
        String str = Build.TYPE;
        DBG = "eng".equals(str) || TextUtils.equals(str, "userdebug");
    }

    public static int getMainPhoneId() {
        String str = SystemProperties.get("persist.vendor.radio.simswitch", "1");
        log("current 3G Sim = " + str);
        int i = !TextUtils.isEmpty(str) ? Integer.parseInt(str) - 1 : -1;
        log("getMainPhoneId: " + i);
        return i;
    }

    public static boolean isCapabilityPhone(Phone phone) {
        boolean z = getMainPhoneId() == phone.getPhoneId();
        log("isCapabilityPhone result = " + z + " phoneId = " + phone.getPhoneId());
        return z;
    }

    private static void log(String str) {
        if (DBG) {
            Log.d("TelephonyUtilsEx", str);
        }
    }
}

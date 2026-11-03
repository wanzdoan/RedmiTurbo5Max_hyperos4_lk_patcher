package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class MELog {
    private static final String PROPERTY_MODEM_ENHANCE_LOG = "persist.radio.me.modem_enhance_log";
    private static int level = 0;
    private static boolean mTestFlag = true;
    private boolean errorLogState = false;

    private MELog() {
    }

    public static void init() {
        int i = SystemProperties.getInt(PROPERTY_MODEM_ENHANCE_LOG, 1);
        level = i;
        mTestFlag = i >= 2;
    }

    public static void d(String str, String str2) {
        if (level >= 1) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (level >= 2) {
            Log.i(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (level >= 0) {
            Log.e(str, str2);
        }
    }

    public static void setTestFlag(int i) {
        try {
            SystemProperties.set(PROPERTY_MODEM_ENHANCE_LOG, String.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTestFlag = i >= 2;
    }

    public static boolean getTestFlag() {
        return mTestFlag;
    }
}

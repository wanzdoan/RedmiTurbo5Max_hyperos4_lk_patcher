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
        level = SystemProperties.getInt(PROPERTY_MODEM_ENHANCE_LOG, 1);
        mTestFlag = level >= 2;
    }

    public static void d(String tag, String str) {
        if (level >= 1) {
            Log.d(tag, str);
        }
    }

    public static void i(String tag, String str) {
        if (level >= 2) {
            Log.i(tag, str);
        }
    }

    public static void e(String tag, String str) {
        if (level >= 0) {
            Log.e(tag, str);
        }
    }

    public static void setTestFlag(int flag) {
        try {
            SystemProperties.set(PROPERTY_MODEM_ENHANCE_LOG, String.valueOf(flag));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTestFlag = flag >= 2;
    }

    public static boolean getTestFlag() {
        return mTestFlag;
    }
}

package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.xiaomi.modem.ModemUtils;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarLog {
    private static final String PROPERTY_DYNAMIC_SAR_LOG = "persist.vendor.radio.dynamic_sar_log";
    private static Context mContext = null;
    private static int mLogLevel = 0;

    private ModemSarLog() {
    }

    public static void init(Context context) {
        mContext = context;
        mLogLevel = SystemProperties.getInt("persist.vendor.radio.dynamic_sar_log", 1);
    }

    public static void d(String logTag, String msg) {
        if (mLogLevel >= 1) {
            Rlog.d(logTag + "(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
        }
    }

    public static void i(String logTag, String msg) {
        if (mLogLevel >= 2) {
            Rlog.i(logTag + "(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
        }
    }

    public static void e(String logTag, String msg) {
        if (mLogLevel >= 0) {
            Rlog.e(logTag + "(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
        }
    }
}

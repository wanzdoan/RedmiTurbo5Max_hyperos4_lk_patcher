package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.xiaomi.modem.ModemUtils;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarLog {
    private static final String PROPERTY_DYNAMIC_SAR_LOG = "persist.vendor.radio.dynamic_sar_log";
    private static Context mContext;
    private static int mLogLevel;

    private ModemSarLog() {
    }

    public static void init(Context context) {
        mContext = context;
        mLogLevel = SystemProperties.getInt(PROPERTY_DYNAMIC_SAR_LOG, 1);
    }

    public static void d(String str, String str2) {
        if (mLogLevel >= 1) {
            Rlog.d(str + "(" + ModemUtils.getCurProcessName(mContext) + ")", str2);
        }
    }

    public static void i(String str, String str2) {
        if (mLogLevel >= 2) {
            Rlog.i(str + "(" + ModemUtils.getCurProcessName(mContext) + ")", str2);
        }
    }

    public static void e(String str, String str2) {
        if (mLogLevel >= 0) {
            Rlog.e(str + "(" + ModemUtils.getCurProcessName(mContext) + ")", str2);
        }
    }
}

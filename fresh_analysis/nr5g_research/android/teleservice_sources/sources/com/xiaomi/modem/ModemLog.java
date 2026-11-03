package com.xiaomi.modem;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ModemLog {
    private static final String LOG_TAG = "ModemLog";
    public static final String PROP_PRINT_IDLE = "0";
    public static final String PROP_PRINT_OFF = "2";
    public static final String PROP_PRINT_ON = "1";
    private static Context mContext;
    private String mPrintFlag;

    public ModemLog(Context context, String str, String str2) {
        this.mPrintFlag = "0";
        mContext = context;
        this.mPrintFlag = SystemProperties.get(str, str2);
        Log.d("ModemLog(" + ModemUtils.getCurProcessName(mContext) + ")", "mContext = " + mContext + ", mPrintFlag = " + this.mPrintFlag + ", strProp = " + str + ", strPropDefVal = " + str2);
    }

    public void log(String str, String str2) {
        if ("1".equals(this.mPrintFlag)) {
            Log.d(str + "(" + ModemUtils.getCurProcessName(mContext) + ")", str2);
        }
    }
}

package com.xiaomi.modem;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class ModemLog {
    private static final String LOG_TAG = "ModemLog";
    public static final String PROP_PRINT_IDLE = "0";
    public static final String PROP_PRINT_OFF = "2";
    public static final String PROP_PRINT_ON = "1";
    private static Context mContext = null;
    private String mPrintFlag;

    public ModemLog(Context context, String strProp) {
        this(context, strProp, "1");
    }

    public ModemLog(Context context, String strProp, String strPropDefVal) {
        this.mPrintFlag = "0";
        mContext = context;
        this.mPrintFlag = SystemProperties.get(strProp, strPropDefVal);
        Log.d("ModemLog(" + ModemUtils.getCurProcessName(mContext) + ")", "mContext = " + mContext + ", mPrintFlag = " + this.mPrintFlag + ", strProp = " + strProp + ", strPropDefVal = " + strPropDefVal);
    }

    public void log(String logTag, String msg) {
        if ("1".equals(this.mPrintFlag)) {
            Log.d(logTag + "(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
        }
    }
}

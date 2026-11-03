package com.android.phone;

import android.content.Context;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiDeviceEuiccBindingMgrBase {
    private static Context mContext;
    public static volatile MiuiDeviceEuiccBindingMgrBase mInstance;
    public static TelephonyManager mTeleMgr;

    public abstract String processBindingData(List list);

    public static MiuiDeviceEuiccBindingMgrBase getInstance() {
        return mInstance;
    }

    protected MiuiDeviceEuiccBindingMgrBase() {
        logi("MiuiDeviceSimBindingManagerBase init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        mTeleMgr = (TelephonyManager) phoneGlobals.getSystemService("phone");
    }

    protected static void logi(String str) {
        Rlog.i("DEBMgr", str);
    }

    protected static void logd(String str) {
        Rlog.d("DEBMgr", str);
    }
}

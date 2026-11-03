package com.xiaomi.internal.telephony;

import android.content.Context;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class MESimAbnormalMonitorHelper {
    private static final String LOG_TAG = "MESimAbnormalMonitorHelper";

    public static void init(Context context) {
        if (HyperOSCustFeatureResolve.getBoolean("dfx_sim_abnormal_monitor_support", false)) {
            MEModuleSimAbnormalMonitor.getInstance(context);
            MELog.d(LOG_TAG, "MEModuleSimAbnormalMonitor init.");
        }
    }
}

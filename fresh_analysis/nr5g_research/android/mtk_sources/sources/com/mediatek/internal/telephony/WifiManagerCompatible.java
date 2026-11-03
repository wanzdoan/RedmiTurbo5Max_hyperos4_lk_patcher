package com.mediatek.internal.telephony;

import android.content.Context;
import android.net.wifi.AmlMiuiWifiManager;
import android.net.wifi.MiuiWifiManager;
import java.util.List;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class WifiManagerCompatible {
    public static void setSARLimit(Context context, int set) {
        MiuiWifiManager mws = (MiuiWifiManager) context.getSystemService("MiuiWifiService");
        AmlMiuiWifiManager mAmlws = (AmlMiuiWifiManager) context.getSystemService("AmlMiuiWifiService");
        if (mws != null) {
            mws.setSARLimit(set);
        } else if (mAmlws != null) {
            mAmlws.setSARLimit(set);
        }
    }

    public static void setTaSARLimit(Context context, List<String> taCmdList) {
        MiuiWifiManager mws = (MiuiWifiManager) context.getSystemService("MiuiWifiService");
        AmlMiuiWifiManager mAmlws = (AmlMiuiWifiManager) context.getSystemService("AmlMiuiWifiService");
        if (mws != null && taCmdList != null) {
            mws.setTaSARLimit(taCmdList);
        } else if (mAmlws != null && taCmdList != null) {
            mAmlws.setTaSARLimit(taCmdList);
        }
    }

    public static void enableTaSar(Context context, boolean enable) {
        MiuiWifiManager mws = (MiuiWifiManager) context.getSystemService("MiuiWifiService");
        AmlMiuiWifiManager mAmlws = (AmlMiuiWifiManager) context.getSystemService("AmlMiuiWifiService");
        if (mws != null) {
            mws.enableTaSar(enable);
        } else if (mAmlws != null) {
            mAmlws.enableTaSar(enable);
        }
    }

    public static boolean isNeeded() {
        boolean isSupportCnSar = HyperOSCustFeatureResolve.getBoolean("config_support_cn_sar", false);
        return Build.IS_GLOBAL_BUILD || isSupportCnSar;
    }
}

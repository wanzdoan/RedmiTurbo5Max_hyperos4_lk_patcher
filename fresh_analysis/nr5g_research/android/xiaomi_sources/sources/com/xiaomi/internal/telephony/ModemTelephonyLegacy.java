package com.xiaomi.internal.telephony;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class ModemTelephonyLegacy {
    private static String LOG_TAG = "ModemTelephonyLegacy";
    public static final int SAR_TYPE_L12A = 1;
    public static final int SAR_TYPE_M12 = 5;
    public static final int SAR_TYPE_M12A = 3;
    public static final int SAR_TYPE_M12_CN = 4;
    public static final int SAR_TYPE_M9_CN = 2;
    public static final int SAR_TYPE_N11A = 6;
    public static final int SAR_TYPE_SAR_V8550 = 7;
    public static final int SAR_TYPE_TA = 0;

    public static void make(Context context, CommandsInterface[] sCommandsInterfaces) {
        if (context == null) {
            Rlog.w(LOG_TAG, "Context is null, abort.");
            return;
        }
        if (sCommandsInterfaces == null || sCommandsInterfaces.length == 0) {
            Rlog.w(LOG_TAG, "sCommandsInterfaces is empty or null, skip modem enhancement");
            return;
        }
        Rlog.d(LOG_TAG, "MTK ModemTelephonyLegacy make start for miui");
        Integer sarType = Integer.valueOf(HyperOSCustFeatureResolve.getInt("mtk_legacy_sar_type", -1));
        Boolean t1_support = Boolean.valueOf(HyperOSCustFeatureResolve.getBoolean("mtk_me_t1_optimize_support", false));
        Boolean uac_support = Boolean.valueOf(HyperOSCustFeatureResolve.getBoolean("mtk_me_uac_support", false));
        switch (sarType.intValue()) {
            case 0:
                ModemSarControllerTA.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerTA make.");
                break;
            case 1:
                ModemSarControllerL12A.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerL12A make.");
                break;
            case 2:
                ModemSarControllerM9CN.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerM9CN make.");
                break;
            case 3:
                ModemSarControllerM12A.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerM12A make.");
                break;
            case 4:
                ModemSarControllerM12CN.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerM12CN make.");
                break;
            case 5:
                ModemSarControllerM12.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerM12 make.");
                break;
            case 6:
                ModemSarControllerN11A.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarControllerN11A make.");
                break;
            case 7:
                ModemSarControllerSarV8550.make(context, sCommandsInterfaces);
                Rlog.d(LOG_TAG, "ModemSarController SarV8550 make.");
                break;
            default:
                Rlog.w(LOG_TAG, "Unknown SAR type: " + sarType);
                break;
        }
        if (t1_support.booleanValue()) {
            MEModuleSignalT1.getInstance(context);
            Rlog.d(LOG_TAG, "MEModuleSignalT1 init.");
        }
        if (uac_support.booleanValue()) {
            XiaoMiUACDetectService.getInstance(context, sCommandsInterfaces);
            Rlog.d(LOG_TAG, "XiaoMiUACDetectService init.");
        }
        Rlog.d(LOG_TAG, "MTK ModemTelephonyLegacy make end");
    }
}

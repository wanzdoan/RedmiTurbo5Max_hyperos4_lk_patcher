package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class ModemEnhanceMain extends Thread {
    private static final String LOG_TAG = "Modem_ModemEnhanceMain";

    public static void MEStart(Context context) {
        Log.d(LOG_TAG, "MEStart, context = " + context);
        make(context);
    }

    public static void make(Context context) {
        String factorybuild = SystemProperties.get("ro.boot.factorybuild", "");
        if ("1".equals(factorybuild)) {
            Log.d(LOG_TAG, "factorybuild do nothing, context = " + context);
            return;
        }
        MELog.init();
        MEFeatureConfig.init(context);
        start(context);
        startDefaultModules(context);
    }

    public static void start(Context context) {
        if (!MEFeatureConfig.isAnyFeatureSupported()) {
            Log.e(LOG_TAG, "No Modem enhance feature supported!");
            return;
        }
        if (context == null) {
            Log.d(LOG_TAG, "context is null, will do nothing!");
            return;
        }
        for (int id = 0; id < 6; id++) {
            switch (id) {
                case 1:
                    if (MEFeatureConfig.isFeatureSupported(id)) {
                        MEModuleSignalUAC.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                    }
                    break;
                case 2:
                    if (MEFeatureConfig.isFeatureSupported(id)) {
                        MEModuleSignalUAC.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                    }
                    break;
                case 3:
                    if (MEFeatureConfig.isFeatureSupported(id)) {
                        MEModuleSignalT1s.getInstance(context);
                    }
                    break;
                case 4:
                    if (MEFeatureConfig.isFeatureSupported(id)) {
                        MEModuleSmartTx.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                    }
                    break;
                case 5:
                    if (MEFeatureConfig.isFeatureSupported(id)) {
                        LockGSMAntService.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                    }
                    break;
            }
        }
    }

    public static void startDefaultModules(Context context) {
        if (context == null) {
            Log.d(LOG_TAG, "startDefaultModules context is null, will do nothing!");
        } else {
            MESimAbnormalMonitorHelper.init(context);
        }
    }

    public static void onHookDisconnected() {
        Log.d(LOG_TAG, "onHookDisconnected do nothing");
    }
}

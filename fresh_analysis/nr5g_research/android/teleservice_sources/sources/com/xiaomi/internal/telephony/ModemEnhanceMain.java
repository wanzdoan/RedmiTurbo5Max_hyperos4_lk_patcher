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
        if ("1".equals(SystemProperties.get(ModemSarUtils.PROPERTY_FACTORY_BUILD, ""))) {
            Log.d(LOG_TAG, "factorybuild do nothing, context = " + context);
        } else {
            MELog.init();
            MEFeatureConfig.init(context);
            start(context);
            startDefaultModules(context);
        }
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
        for (int i = 0; i < 6; i++) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            if (MEFeatureConfig.isFeatureSupported(i)) {
                                MEModuleSmartTx.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                            }
                        } else if (i == 5 && MEFeatureConfig.isFeatureSupported(i)) {
                            LockGSMAntService.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                        }
                    } else if (MEFeatureConfig.isFeatureSupported(i)) {
                        MEModuleSignalT1s.getInstance(context);
                    }
                } else if (MEFeatureConfig.isFeatureSupported(i)) {
                    MEModuleSignalUAC.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
                }
            } else if (MEFeatureConfig.isFeatureSupported(i)) {
                MEModuleSignalUAC.getInstance(context, XiaomiTelephonyComponentFactory.getHookAgent());
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

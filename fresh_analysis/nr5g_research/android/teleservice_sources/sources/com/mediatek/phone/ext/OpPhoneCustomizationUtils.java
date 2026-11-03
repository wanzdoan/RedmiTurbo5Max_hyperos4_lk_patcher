package com.mediatek.phone.ext;

import android.content.Context;
import android.util.Log;
import com.mediatek.common.util.OperatorCustomizationFactoryLoader;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OpPhoneCustomizationUtils {
    private static final String LOG_TAG = "OpPhoneCustomizationUtils";
    static OpPhoneCustomizationFactoryBase sFactory;
    private static final List sOperatorFactoryInfoList;

    static {
        ArrayList arrayList = new ArrayList();
        sOperatorFactoryInfoList = arrayList;
        sFactory = null;
        arrayList.add(new OperatorCustomizationFactoryLoader.OperatorFactoryInfo("OP01TeleService.apk", "com.mediatek.op01.phone.plugin.Op01PhoneCustomizationFactory", "com.mediatek.op01.phone.plugin", "OP01"));
        arrayList.add(new OperatorCustomizationFactoryLoader.OperatorFactoryInfo("OP08TeleService.apk", "com.mediatek.op08.phone.Op08PhoneCustomizationFactory", "com.mediatek.op08.phone", "OP08"));
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    public static synchronized OpPhoneCustomizationFactoryBase getOpFactory(Context context) {
        try {
            if (sFactory == null) {
                OpPhoneCustomizationFactoryBase opPhoneCustomizationFactoryBase = (OpPhoneCustomizationFactoryBase) OperatorCustomizationFactoryLoader.loadFactory(context, sOperatorFactoryInfoList);
                sFactory = opPhoneCustomizationFactoryBase;
                if (opPhoneCustomizationFactoryBase == null) {
                    sFactory = new OpPhoneCustomizationFactoryBase();
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return sFactory;
    }

    public static synchronized void resetOpFactory(Context context) {
        log("resetOpFactory");
        sFactory = null;
    }
}

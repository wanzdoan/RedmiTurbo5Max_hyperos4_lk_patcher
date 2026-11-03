package com.xiaomi.internal.telephony;

import android.content.Context;
import com.xiaomi.modem.ModemUtils;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class XmPhoneServiceFactory {
    private static final String LIB_PATH = "/system_ext/framework/xm-telephony-common.jar";
    private static final String LOG_TAG = "XmPhoneServiceFactory";

    public static void init(Context context) {
        try {
            PathClassLoader pathClassLoader = new PathClassLoader(LIB_PATH, XmPhoneServiceFactory.class.getClassLoader());
            logd("classLoader = " + pathClassLoader.toString());
            Class<?> cls = Class.forName("com.android.internal.telephony.XmPhoneFactory", true, pathClassLoader);
            logd("cls = " + cls);
            Method declaredMethod = cls.getDeclaredMethod("init", Context.class);
            logd("initMethod = " + declaredMethod);
            declaredMethod.invoke(null, context);
            logd("XmPhoneFactory init method invoked successfully.");
        } catch (Exception e) {
            loge("Reflect exception getInstance: " + e.toString());
        }
    }

    private static void logd(String str) {
        ModemUtils.logd(LOG_TAG, str);
    }

    private static void loge(String str) {
        ModemUtils.loge(LOG_TAG, str);
    }
}

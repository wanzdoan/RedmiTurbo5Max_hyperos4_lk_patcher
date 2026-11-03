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
            PathClassLoader classLoader = new PathClassLoader(LIB_PATH, XmPhoneServiceFactory.class.getClassLoader());
            logd("classLoader = " + classLoader.toString());
            Class<?> cls = Class.forName("com.android.internal.telephony.XmPhoneFactory", true, classLoader);
            logd("cls = " + cls);
            Method initMethod = cls.getDeclaredMethod("init", Context.class);
            logd("initMethod = " + initMethod);
            initMethod.invoke(null, context);
            logd("XmPhoneFactory init method invoked successfully.");
        } catch (Exception e) {
            loge("Reflect exception getInstance: " + e.toString());
        }
    }

    private static void logd(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }

    private static void loge(String msg) {
        ModemUtils.loge(LOG_TAG, msg);
    }
}

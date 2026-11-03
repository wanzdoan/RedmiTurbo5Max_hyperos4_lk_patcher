package com.xiaomi.onetrack.util;

import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public abstract class l {
    private static Method a;
    private static Method b;
    private static Method c;

    static {
        try {
            Class<?> cls = Class.forName("miui.util.FeatureParser");
            a = cls.getMethod("getString", String.class);
            b = cls.getMethod("getBoolean", String.class);
            c = cls.getMethod("getInteger", String.class);
        } catch (Throwable unused) {
        }
    }

    public static boolean a(String str, boolean z) {
        Method method = b;
        if (method != null) {
            try {
                return ((Boolean) method.invoke(null, str, Boolean.valueOf(z))).booleanValue();
            } catch (Throwable unused) {
            }
        }
        return z;
    }
}

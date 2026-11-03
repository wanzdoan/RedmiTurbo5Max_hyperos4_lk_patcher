package com.xiaomi.onetrack.util;

import android.content.Context;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public abstract class p {
    private static Object b;
    private static Class c;
    private static Method d;

    static {
        try {
            Class<?> cls = Class.forName("com.android.id.impl.IdProviderImpl");
            c = cls;
            b = cls.newInstance();
            d = c.getMethod("getOAID", Context.class);
        } catch (Exception e) {
            r.a("IdentifierManager", "reflect exception!", e);
        }
    }

    public static String a(Context context) {
        return a(context, d);
    }

    private static String a(Context context, Method method) {
        Object obj;
        if (context != null && (obj = b) != null && method != null) {
            try {
                Object objInvoke = method.invoke(obj, context);
                if (objInvoke != null) {
                    return (String) objInvoke;
                }
                return "";
            } catch (Throwable th) {
                r.a("IdentifierManager", "oaid invoke exception!", th);
                return "";
            }
        }
        return "";
    }
}

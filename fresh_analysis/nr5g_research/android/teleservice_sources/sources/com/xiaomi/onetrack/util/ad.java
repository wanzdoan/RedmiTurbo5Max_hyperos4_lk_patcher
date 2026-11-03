package com.xiaomi.onetrack.util;

import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ad {
    public static String a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, str, str2);
        } catch (Throwable th) {
            Log.e(r.a("SystemProperties"), "get e" + th.getMessage());
            return str2;
        }
    }

    public static String a(String str) {
        return a(str, "");
    }
}

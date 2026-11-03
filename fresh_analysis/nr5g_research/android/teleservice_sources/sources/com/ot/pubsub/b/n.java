package com.ot.pubsub.b;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class n {
    private static String a = "ConfigProvider";
    private static volatile boolean b = false;
    private static volatile boolean c = true;

    public static boolean a() {
        try {
            String[] strArrB = com.ot.pubsub.c.f.a().b();
            return (!TextUtils.isEmpty(strArrB[0]) && !TextUtils.isEmpty(strArrB[1])) && !com.ot.pubsub.util.l.c(a);
        } catch (Exception e) {
            com.ot.pubsub.util.j.a(a, "ConfigProvider.available", e);
            return false;
        }
    }

    public static int a(int i) {
        int iIntValue;
        if (com.ot.pubsub.util.j.b) {
            com.ot.pubsub.util.j.a(a, "debug upload mode, send events immediately");
            return 0;
        }
        try {
            iIntValue = ((Integer) e.c().get(Integer.valueOf(i + 1))).intValue();
        } catch (Exception unused) {
            iIntValue = 60000;
        }
        com.ot.pubsub.util.j.a(a, "getUploadInterval " + iIntValue);
        return iIntValue;
    }

    public static synchronized void a(boolean z) {
        b = z;
    }

    public static synchronized boolean b() {
        return b;
    }

    public static boolean c() {
        return com.ot.pubsub.util.k.a();
    }
}

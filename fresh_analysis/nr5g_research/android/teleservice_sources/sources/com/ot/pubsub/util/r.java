package com.ot.pubsub.util;

import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public class r {
    private static final String a = "ProcessUtil";

    public static boolean a() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}

package com.xiaomi.onetrack.util;

import android.os.Looper;

/* JADX INFO: loaded from: classes2.dex */
public abstract class y {
    public static boolean a() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}

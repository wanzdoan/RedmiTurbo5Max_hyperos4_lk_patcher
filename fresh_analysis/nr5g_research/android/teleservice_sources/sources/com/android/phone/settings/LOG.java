package com.android.phone.settings;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public abstract class LOG {
    public static void d(String str) {
        Log.d("fakecell_settings", String.format("< %s > %s", Thread.currentThread().getName(), str));
    }

    public static void e(String str) {
        Log.e("fakecell_settings", String.format("< %s > %s", Thread.currentThread().getName(), str));
    }
}

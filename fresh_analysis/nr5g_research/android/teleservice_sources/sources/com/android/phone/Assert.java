package com.android.phone;

import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public abstract class Assert {
    private static Boolean sIsMainThreadForTest;

    public static void isTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static void isMainThread() {
        Boolean bool = sIsMainThreadForTest;
        if (bool != null) {
            isTrue(bool.booleanValue());
        } else {
            isTrue(Looper.getMainLooper().equals(Looper.myLooper()));
        }
    }

    public static void fail(String str) {
        throw new AssertionError(str);
    }
}

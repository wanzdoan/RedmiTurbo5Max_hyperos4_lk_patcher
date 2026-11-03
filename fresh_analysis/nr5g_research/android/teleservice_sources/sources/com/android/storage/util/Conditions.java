package com.android.storage.util;

/* JADX INFO: loaded from: classes.dex */
public abstract class Conditions {
    public static void checkArgInRange(String str, int i, int i2, int i3) {
        if (i2 > i || i > i3) {
            throw new IllegalArgumentException(str + "=" + i + " must be between " + i2 + " and " + i3);
        }
    }

    public static void checkArgInRange(String str, int i, String str2, int i2, String str3, int i3) {
        if (i2 > i || i > i3) {
            throw new IllegalArgumentException(str + "=" + i + " must be between " + str2 + "=" + i2 + " and " + str3 + "=" + i3);
        }
    }

    public static void checkStateInRange(String str, int i, String str2, int i2, String str3, int i3) {
        if (i2 > i || i > i3) {
            throw new IllegalStateException(str + "=" + i + " must be between " + str2 + "=" + i2 + " and " + str3 + "=" + i3);
        }
    }
}

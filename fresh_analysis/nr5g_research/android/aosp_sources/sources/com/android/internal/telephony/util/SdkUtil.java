package com.android.internal.telephony.util;

import android.os.Build;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class SdkUtil {

    public static class LateSdk<T> {
        public final T value;

        public LateSdk(T t) {
            this.value = t;
        }
    }

    public static boolean isAtLeast25Q4() {
        return Build.VERSION.SDK_INT_FULL >= 3600001;
    }

    public static boolean isFullSdkVersionAtMost(int i) {
        int i2 = Build.VERSION.SDK_INT_FULL;
        return (i != i2 || Objects.equals(Build.VERSION.CODENAME, "REL")) && i2 <= i;
    }

    public static boolean isAtLeast26Q2() {
        int i = Build.VERSION.SDK_INT_FULL;
        return (i == 3600001 && !Objects.equals(Build.VERSION.CODENAME, "REL")) || i >= 3700000;
    }
}

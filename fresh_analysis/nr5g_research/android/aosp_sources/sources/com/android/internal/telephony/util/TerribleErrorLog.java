package com.android.internal.telephony.util;

import android.util.Log;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class TerribleErrorLog {
    private static final String TAG = "TerribleErrorLog";

    public static void logTerribleError(BiConsumer<Integer, Integer> biConsumer, String str, int i, int i2) {
        biConsumer.accept(Integer.valueOf(i), Integer.valueOf(i2));
        Log.wtf(TAG, str);
    }
}

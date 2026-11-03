package com.xiaomi.sensor.aidl;

import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Calibrate {
    public static int nativeSetConfig(int i, float[] fArr) {
        Log.e("xiaomiSensorCalibrate", "log in nativeSetConfig and the value of data:" + fArr[0]);
        return CitSensorCheckManager.getInstance().setConfig(i, fArr);
    }
}

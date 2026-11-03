package com.android.phone.settings;

/* JADX INFO: loaded from: classes.dex */
public class VendorUtilsBase {
    protected static volatile VendorUtilsBase mInstance;

    public boolean setLteBandPref(int[] iArr, int i) {
        return false;
    }

    public boolean setXiaomiBandMode(long[] jArr, int i) {
        return false;
    }

    public static VendorUtilsBase getInstance() {
        if (mInstance == null) {
            synchronized (VendorUtilsBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new VendorUtilsBase();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }
}

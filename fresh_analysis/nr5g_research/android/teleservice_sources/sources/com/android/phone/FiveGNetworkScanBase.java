package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public class FiveGNetworkScanBase {
    protected static volatile FiveGNetworkScanBase mInstance;

    public boolean startNetworkScan(int i) {
        return false;
    }

    public boolean stopNetworkScan(int i) {
        return false;
    }

    public static FiveGNetworkScanBase getInstance() {
        if (mInstance == null) {
            synchronized (FiveGNetworkScanBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new FiveGNetworkScanBase();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }
}

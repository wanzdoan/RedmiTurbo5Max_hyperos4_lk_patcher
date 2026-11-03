package com.android.phone.signalMap;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapSnsControllerBase {
    public static boolean DEBUG = false;
    protected static volatile SignalMapSnsControllerBase mInstance;

    public void setLteNrDubiousCellIfNeed(List list, String str) {
    }

    public void updateSignalMapCellCloud() {
    }

    public static SignalMapSnsControllerBase getInstance() {
        if (mInstance == null) {
            synchronized (SignalMapSnsControllerBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new SignalMapSnsControllerBase();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }
}

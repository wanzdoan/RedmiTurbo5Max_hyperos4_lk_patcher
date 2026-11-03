package com.android.phone.ntn;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public class NtnOptimizeControllerBase extends Handler {
    protected static volatile NtnOptimizeControllerBase mInstance;

    public boolean isSatelliteInThremalState() {
        return false;
    }

    public boolean isSatellitePaMegathermal() {
        return false;
    }

    public static NtnOptimizeControllerBase getInstance() {
        if (mInstance == null) {
            synchronized (NtnOptimizeControllerBase.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new NtnOptimizeControllerBase(Looper.getMainLooper());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    protected NtnOptimizeControllerBase(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
    }
}

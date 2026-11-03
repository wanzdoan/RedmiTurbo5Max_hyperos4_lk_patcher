package com.android.services.telephony.relay.phonecall;

import android.os.Handler;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class RelayHandlerExecutor implements Executor {
    private static final String TAG = "RelayHandlerExecutor";
    private final Handler mHandler;

    public RelayHandlerExecutor(Handler handler) {
        this.mHandler = handler;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (this.mHandler.post(runnable)) {
            return;
        }
        RelayLog.loge(TAG, this.mHandler + " is shutting down");
    }
}

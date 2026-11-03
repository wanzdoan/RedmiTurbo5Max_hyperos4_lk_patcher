package com.xiaomi.internal.telephony;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.xiaomi.modem.ModemUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class ModemDumpCollectService extends Service {
    private static final String LOG_TAG = "Mi_ModemDumpCollect_Service";

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        log("onCreate");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }
}

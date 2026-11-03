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
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        log("onCreate");
    }

    private static void log(String str) {
        ModemUtils.logd(LOG_TAG, str);
    }
}

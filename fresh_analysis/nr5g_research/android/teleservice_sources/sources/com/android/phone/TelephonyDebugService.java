package com.android.phone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.android.internal.telephony.DebugService;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyDebugService extends Service {
    private static String TAG = "TelephonyDebugService";
    private DebugService mDebugService;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public TelephonyDebugService() {
        Log.d(TAG, "TelephonyDebugService()");
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mDebugService = new DebugService(this);
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mDebugService.dump(fileDescriptor, printWriter, strArr);
        if (strArr == null || strArr.length != 0) {
            return;
        }
        MiPhoneAppStub.get().dumpDebugService(fileDescriptor, printWriter, strArr);
    }
}

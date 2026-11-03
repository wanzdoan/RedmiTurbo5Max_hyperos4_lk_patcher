package com.android.phone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class ModemSmartEngineService extends Service {
    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d("Modem_MSEService", "onBind Successfully...");
        return null;
    }
}

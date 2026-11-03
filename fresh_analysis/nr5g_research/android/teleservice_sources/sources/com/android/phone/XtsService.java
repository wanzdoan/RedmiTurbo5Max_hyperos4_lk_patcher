package com.android.phone;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class XtsService extends Service {
    private static OemHookAgent mHookAgent;
    private Object mLock = new Object();
    protected Handler mHandler = new Handler() { // from class: com.android.phone.XtsService.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            XtsService.log("handleMessage msg id: " + message.what);
            int i = message.what;
            if (i == 100) {
                XtsService.log("EVENT_OEM_HOOK_READY");
                XtsService.this.onHookDone();
            } else {
                if (i == 101) {
                    XtsService.log("EVENT_OEM_HOOK_DISCONNECTED");
                    return;
                }
                XtsService.log("invalid msg id: " + message.what);
            }
        }
    };
    private ServiceConnection mMbnServiceConnection = new ServiceConnection() { // from class: com.android.phone.XtsService.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (XtsService.this.mLock) {
                XtsService.log("onServiceConnected");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            XtsService.log("onServiceDisconnected");
        }
    };

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        log("dump");
        int hookType = OemHookAgent.getHookType();
        log("dump, Hook Type = " + hookType);
        if (hookType == 2) {
            log("Qcom XTS will do MODEM_DUMP");
            ModemUtils.dumpLteBandpref(mHookAgent, printWriter);
            ModemUtils.dumpRffeScanResult(mHookAgent, printWriter);
            ModemUtils.dumpRfdeviceReportResult(mHookAgent, printWriter);
            return;
        }
        if (hookType == 1) {
            log("MTK XTS will do MODEM_DUMP");
        } else {
            log("UNKNOWN XTS will do nothing for MODEM_DUMP");
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        log("onBind, intent = " + intent);
        return null;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        log("onUnbind, intent = " + intent);
        return super.onUnbind(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHookDone() {
        log("onHookDone");
        OemHookAgent.unregisterHookReadyEvent(this.mHandler);
        mHookAgent = OemHookAgent.getHook();
        log("mHookAgent: " + mHookAgent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        log("onCreate");
        OemHookAgent.registerHookDisconnectedEvent(this, this.mHandler, 101, null);
        OemHookAgent.registerHookReadyEvent(this, this.mHandler, 100, null);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        log("onStartCommand, flags = " + i + ", startId = " + i2 + ", intent = " + intent);
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        ModemUtils.logd("XtsService", str);
    }
}

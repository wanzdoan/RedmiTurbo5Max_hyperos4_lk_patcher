package com.android.phone.dcservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiNewCallUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class MiDcService extends Service {
    private MiDcServiceController mDcServiceController;
    private boolean mBroadcastReceiverRegistered = false;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.dcservice.MiDcService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.telecom.intent.action.DATA_CHANNEL_INFO".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("telecomCallId");
                int intExtra = intent.getIntExtra("modemCallId", -1);
                int intExtra2 = intent.getIntExtra("phoneId", -1);
                Log.i("MiDcService", "onReceive data channel info , telecomCallId: " + stringExtra + ", modemCallId: " + intExtra + ", phoneId: " + intExtra2);
                if (stringExtra == null || intExtra <= 0 || MiDcService.this.mDcServiceController == null) {
                    return;
                }
                MiDcService.this.mDcServiceController.initDataChannel(intExtra2, stringExtra, Integer.toString(intExtra));
            }
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (this.mDcServiceController == null || !MiuiNewCallUtils.isNewCallFeatureEnabled()) {
            return null;
        }
        Log.i("MiDcService", "Service onBind");
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "bind_miui_dc_service");
        return this.mDcServiceController.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        Log.i("MiDcService", "Service onCreate...");
        if (MiuiNewCallUtils.isNewCallFeatureEnabled()) {
            if (this.mDcServiceController == null) {
                this.mDcServiceController = MiDcServiceController.getInstance();
            }
            registerCallIdBroadcastReceiver();
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.i("MiDcService", "Service onDestroy...");
        MiDcServiceController miDcServiceController = this.mDcServiceController;
        if (miDcServiceController != null) {
            miDcServiceController.dispose();
            this.mDcServiceController = null;
        }
        unregisterCallIdBroadcastReceiver();
    }

    public void registerCallIdBroadcastReceiver() {
        if (getBaseContext() != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.telecom.intent.action.DATA_CHANNEL_INFO");
            getBaseContext().registerReceiver(this.mBroadcastReceiver, intentFilter);
            this.mBroadcastReceiverRegistered = true;
        }
    }

    public void unregisterCallIdBroadcastReceiver() {
        if (getBaseContext() == null || !this.mBroadcastReceiverRegistered) {
            return;
        }
        getBaseContext().unregisterReceiver(this.mBroadcastReceiver);
        this.mBroadcastReceiverRegistered = false;
    }
}

package com.android.services.telephony.relay.offlinecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class OfflineManager {
    private static final String ACTION_OFFLINE_CALL = "miui.intent.action.OFFLINE_CALL";
    private static final String EXTRA_BLE_ADDRESS = "extra_ble_addr";
    private static final String EXTRA_LOCAL_NUMBER = "extra_local_number";
    private static final String EXTRA_NUMBER = "extra_number";
    private static final String EXTRA_PLACE_OFFLINE_CALL = "extra_place_offline_call";
    private static final String PREFIX_DEMO_DEVICE = "Demo";
    private static final String TAG = "OfflineManager";
    private String mLocalNumber = "0000";
    private final OfflinePhoneAccountRegistry mOfflinePhoneAccountRegistry = new OfflinePhoneAccountRegistry();
    private final OfflineConnectionsManager mOfflineConnectionsManager = new OfflineConnectionsManager();

    public OfflineManager() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_OFFLINE_CALL);
        OfflineUtils.System.getContext().registerReceiver(new BroadcastReceiver() { // from class: com.android.services.telephony.relay.offlinecall.OfflineManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                OfflineCallLog.logi(OfflineManager.TAG, "onReceive action:" + action);
                if (OfflineManager.ACTION_OFFLINE_CALL.equals(action)) {
                    OfflineManager.this.handleOfflineCallUpdateAction(intent);
                }
            }
        }, intentFilter, "com.android.phone.permission.ACCESS_OFFLINE_SERVICE", null, 2);
    }

    public OfflineConnectionsManager getOfflineConnectionsManager() {
        return this.mOfflineConnectionsManager;
    }

    public void enableOfflinePhoneAccount(boolean z) {
        if (!z) {
            this.mOfflineConnectionsManager.tearDown();
        }
        this.mOfflinePhoneAccountRegistry.enablePhoneAccount(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOfflineCallUpdateAction(Intent intent) {
        String stringExtra = intent.getStringExtra(EXTRA_NUMBER);
        if (!isDemoDevice() && stringExtra != null && !stringExtra.startsWith(PREFIX_DEMO_DEVICE)) {
            stringExtra = stringExtra.replaceAll(" ", "");
        }
        String stringExtra2 = intent.getStringExtra(EXTRA_BLE_ADDRESS);
        boolean booleanExtra = intent.getBooleanExtra(EXTRA_PLACE_OFFLINE_CALL, false);
        if (TextUtils.isEmpty(stringExtra)) {
            OfflineCallLog.logi(TAG, "handleOfflineCallUpdateAction number is empty!");
            return;
        }
        if (isDemoDevice()) {
            this.mLocalNumber = intent.getStringExtra(EXTRA_LOCAL_NUMBER);
        }
        if (booleanExtra) {
            placeCall(stringExtra, stringExtra2);
        }
    }

    public void placeCall(String str, String str2) {
        OfflineCallsManager.getInstance().dialOfflineCall(str, "", str2);
        OfflineCallLog.logi(TAG, "placeCall");
    }

    public void answerCall(OfflineConnection offlineConnection) {
        OfflineCallsManager.getInstance().answerOfflineCall();
        if (offlineConnection != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("telecomm.EXTRA_OFFLINE_ANSWERED", true);
            offlineConnection.putExtras(bundle);
            offlineConnection.updateState(4);
        }
        OfflineCallLog.logi(TAG, "answerCall");
    }

    public void endCall(OfflineConnection offlineConnection) {
        OfflineCallsManager.getInstance().hangupOfflineCall();
        OfflineCallLog.logi(TAG, "endCall");
    }

    public void rejectCall(OfflineConnection offlineConnection) {
        OfflineCallsManager.getInstance().rejectOfflineCall(0);
        OfflineCallLog.logi(TAG, "rejectCall");
    }

    public String getDemoDeviceLocalnumber() {
        return this.mLocalNumber;
    }

    public static boolean isDemoDevice() {
        PackageInfo packageInfo;
        try {
            packageInfo = OfflineUtils.System.getContext().getPackageManager().getPackageInfo("com.xiaomi.mihomemanager", 0);
        } catch (Exception unused) {
            packageInfo = null;
        }
        return packageInfo != null && TextUtils.equals(packageInfo.sharedUserId, "android.uid.system");
    }
}

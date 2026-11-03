package com.android.phone;

import android.app.Activity;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSimLockMtnActivity extends Activity {
    private Button mEmergencyButton;
    private StatusBarManager mStatusBarManager;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiSimLockMtnActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Log.d("MiuiSimLockMtnActivity", "receive action : ".concat(action));
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action) && "ABSENT".equals(intent.getStringExtra("ss")) && MiuiSimLockMtnActivity.this.isAllSimAbsent()) {
                MiuiSimLockMtnActivity.this.finish();
            }
        }
    };
    View.OnClickListener mEmergencyListener = new View.OnClickListener() { // from class: com.android.phone.MiuiSimLockMtnActivity.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Log.d("MiuiSimLockMtnActivity", "mEmergencyListener,start emergency call");
            MiuiSimLockMtnActivity.this.launchEmergencyDialer();
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("MiuiSimLockMtnActivity", "onCreate");
        setContentView(R.layout.miui_simlock_mtn);
        Button button = (Button) findViewById(R.id.mtn_emergency_button);
        this.mEmergencyButton = button;
        button.setOnClickListener(this.mEmergencyListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        registerReceiver(this.mReceiver, intentFilter, 2);
        enableStatusBar(false);
        StatusBarManager statusBarManager = (StatusBarManager) getSystemService("statusbar");
        if (statusBarManager != null) {
            statusBarManager.disable(23134208);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.d("MiuiSimLockMtnActivity", "onDestroy");
        enableStatusBar(true);
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void enableStatusBar(boolean z) {
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) getSystemService("statusbar");
        }
        this.mStatusBarManager.disable(z ? 0 : 65536);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllSimAbsent() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (TelephonyManager.getDefault().getSimStateForSlot(i) != 1) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchEmergencyDialer() {
        Intent intent = new Intent("com.android.phone.EmergencyDialer.DIAL");
        intent.setFlags(268435456);
        startActivity(intent);
    }
}

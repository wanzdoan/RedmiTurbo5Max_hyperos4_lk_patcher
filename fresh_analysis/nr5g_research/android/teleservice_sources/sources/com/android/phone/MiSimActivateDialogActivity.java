package com.android.phone;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MiSimActivateDialogActivity extends AppCompatActivity {
    private static String LOG_TAG = "MiSimActivateDialogActivity";
    private AlertDialog mDialog = null;
    private int mPhoneId = 0;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiSimActivateDialogActivity.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("phone", -1);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) && intExtra == MiSimActivateDialogActivity.this.mPhoneId && "ABSENT".equals(intent.getStringExtra("ss"))) {
                MiSimActivateDialogActivity.this.log("sim absent, exit");
                MiSimActivateDialogActivity.this.finish();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            return;
        }
        this.mPhoneId = getIntent().getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, 0);
        showDialog(1);
        registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        final String iccId = MiuiPhoneUtils.getIccId(this.mPhoneId);
        MiuiPhoneUtils.getImsi(this.mPhoneId);
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.mi_sim_activate_title, (ViewGroup) null);
        MiStatInterfaceUtil.recordCountEvent("misim_activate", "prompt_count");
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setCustomTitle(viewInflate).setMessage(getString(R.string.mi_mobile_sim_activate_message, new Object[]{Integer.valueOf(this.mPhoneId + 1), 3, 5})).setIcon(R.drawable.ic_mi_sim_activate).setPositiveButton(R.string.mi_mobile_sim_activate_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiSimActivateDialogActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                Intent intent = new Intent("com.miui.virtualsim.action.CARD_ACTIVATE");
                intent.setFlags(268435456);
                ComponentName componentName = new ComponentName("com.miui.virtualsim", "com.miui.mimobile.ui.entry.RouterActivity");
                intent.putExtra("iccid", iccId);
                intent.setComponent(componentName);
                MiSimActivateDialogActivity.this.startActivity(intent);
                MiSimActivateDialogActivity.this.finish();
                MiStatInterfaceUtil.recordCountEvent("misim_activate", "accept_count");
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiSimActivateDialogActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                MiSimActivateDialogActivity.this.finish();
                MiStatInterfaceUtil.recordCountEvent("misim_activate", "decline_count");
            }
        }).setCancelable(false).create();
        this.mDialog = alertDialogCreate;
        return alertDialogCreate;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        unregisterReceiver(this.mBroadcastReceiver);
    }

    protected void log(String str) {
        Log.d(LOG_TAG, str);
    }
}

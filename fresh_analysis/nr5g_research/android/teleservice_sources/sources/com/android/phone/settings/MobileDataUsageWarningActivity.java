package com.android.phone.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import com.android.phone.R;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MobileDataUsageWarningActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showGenericErrorDialog(getString(R.string.data_enable_warning));
    }

    private void showGenericErrorDialog(CharSequence charSequence) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileDataUsageWarningActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TelephonyManager.from(MobileDataUsageWarningActivity.this).enableDataConnectivity();
                MobileDataUsageWarningActivity.this.finish();
            }
        };
        DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MobileDataUsageWarningActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MobileDataUsageWarningActivity.this.finish();
            }
        };
        new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setMessage(charSequence).setPositiveButton(android.R.string.ok, onClickListener).setNegativeButton(android.R.string.cancel, onClickListener2).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.settings.MobileDataUsageWarningActivity.3
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                MobileDataUsageWarningActivity.this.finish();
            }
        }).create().show();
    }
}

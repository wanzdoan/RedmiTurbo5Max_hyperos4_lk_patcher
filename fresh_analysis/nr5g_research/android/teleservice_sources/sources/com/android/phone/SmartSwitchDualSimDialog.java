package com.android.phone;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SmartSwitchDualSimDialog extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showDialog(R.string.smart_switch_dual_sim_diag_title);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.smart_switch_dual_sim_diag_title).setMessage(R.string.smart_switch_dual_sim_diag_message).setPositiveButton(R.string.smart_switch_dual_sim_diag_ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.SmartSwitchDualSimDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    Intent intent = new Intent();
                    intent.addFlags(268435456);
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MiuiConfigureMobileSettings");
                    intent.putExtra("extra_from", RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
                    SmartSwitchDualSimDialog.this.startActivity(intent);
                    NetworkSpeedTracker.getInstance().updateSmartSwithDualSimDialogCount(true);
                    SmartSwitchDualSimDialog.this.finish();
                } catch (ActivityNotFoundException unused) {
                    Log.d("SmartSwitchDualSimDialog", "transferMiuiConfigureMobileSettings Exception");
                }
            }
        }).setNegativeButton(R.string.smart_switch_dual_sim_diag_cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.SmartSwitchDualSimDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                NetworkSpeedTracker.getInstance().updateSmartSwithDualSimDialogCount(false);
                SmartSwitchDualSimDialog.this.finish();
            }
        }).setCancelable(false);
        return builder.create();
    }
}

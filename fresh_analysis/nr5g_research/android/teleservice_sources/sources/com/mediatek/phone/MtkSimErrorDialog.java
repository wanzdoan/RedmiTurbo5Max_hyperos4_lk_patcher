package com.mediatek.phone;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import com.android.services.telephony.Log;
import com.mediatek.services.telephony.MtkTelephonyConnectionServiceUtil;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkSimErrorDialog extends AlertDialog implements DialogInterface.OnClickListener {
    public IntentFilter mIntentFilter;
    public BroadcastReceiver mReceiver;

    public MtkSimErrorDialog(Context context, ArrayList arrayList) {
        super(new ContextThemeWrapper(context, (Resources.Theme) null));
        this.mIntentFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        this.mReceiver = new BroadcastReceiver() { // from class: com.mediatek.phone.MtkSimErrorDialog.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Log.d(this, "onReceive: cancel the request dialog. action = " + action, new Object[0]);
                if (action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                    MtkSimErrorDialog.this.dismiss();
                }
            }
        };
        getWindow().setType(2002);
        if (arrayList == null || arrayList.size() != 4) {
            Log.d(this, "Finish this with illegle dialog information : ", arrayList);
            return;
        }
        setTitle((CharSequence) arrayList.get(0));
        setMessage((CharSequence) arrayList.get(1));
        setButton(-1, (CharSequence) arrayList.get(2), this);
        setButton(-2, (CharSequence) arrayList.get(3), this);
        setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.mediatek.phone.MtkSimErrorDialog.1
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrShowAlertingFinalize();
                MtkSimErrorDialog.this.setMtkSimErrorDialog(null);
                Log.d(MtkSimErrorDialog.this, "SimErrorDialog dismissed: " + MtkSimErrorDialog.this, new Object[0]);
            }
        });
        setMtkSimErrorDialog(this);
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrShowAlertingFinalize();
            Log.d(this, "SimErrorDialog onClick cancel.", new Object[0]);
        } else {
            if (i == -1) {
                if (MtkTelephonyConnectionServiceUtil.getInstance().isCellConnMgrAlive()) {
                    MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrHandleEvent();
                }
                Log.d(this, "SimErrorDialog onClick ok.", new Object[0]);
                return;
            }
            Log.d(this, "SimErrorDialog onClick.", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMtkSimErrorDialog(MtkSimErrorDialog mtkSimErrorDialog) {
        MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrSetSimErrorDialogActivity(mtkSimErrorDialog);
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        getContext().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        getContext().unregisterReceiver(this.mReceiver);
    }
}

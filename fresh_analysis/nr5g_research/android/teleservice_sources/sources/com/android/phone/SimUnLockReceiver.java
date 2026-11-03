package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;

/* JADX INFO: loaded from: classes.dex */
public class SimUnLockReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.d("SimUnLockReceiver", "SIMUNLOCKRECEIVER RECEIVED!");
        IccNetworkDepersonalizationPanel.showDialog(null, IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK.ordinal());
    }
}

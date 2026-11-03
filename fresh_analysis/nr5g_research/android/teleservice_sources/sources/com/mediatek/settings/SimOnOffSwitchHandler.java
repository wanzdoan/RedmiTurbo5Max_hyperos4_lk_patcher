package com.mediatek.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.mediatek.telephony.MtkTelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SimOnOffSwitchHandler {
    private Context mContext;
    private OnSimOnOffSwitchListener mListener;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.mediatek.settings.SimOnOffSwitchHandler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SimOnOffSwitchHandler.this.handleSimApplicationStateChanged(intent);
        }
    };
    private boolean mSimOnOffEnabled = MtkTelephonyManagerEx.getDefault().isSimOnOffEnabled();
    private int mSimState;
    private int mSlotId;

    public interface OnSimOnOffSwitchListener {
        void onSimOnOffStateChanged();
    }

    public SimOnOffSwitchHandler(Context context, int i) {
        this.mContext = context;
        this.mSlotId = i;
        this.mSimState = MtkTelephonyManagerEx.getDefault().getSimOnOffState(i);
        Log.d("SimOnOffSwitchHandler", "handler=" + this + ", simOnOffEnabled=" + this.mSimOnOffEnabled + ", slotId=" + this.mSlotId + ", state=" + this.mSimState);
    }

    public void registerOnSimOnOffSwitch(OnSimOnOffSwitchListener onSimOnOffSwitchListener) {
        Context context = this.mContext;
        if (context == null || !this.mSimOnOffEnabled) {
            return;
        }
        context.registerReceiver(this.mReceiver, new IntentFilter("android.telephony.action.SIM_APPLICATION_STATE_CHANGED"));
        this.mListener = onSimOnOffSwitchListener;
        Log.d("SimOnOffSwitchHandler", "registerOnSimOnOffSwitch, handler=" + this + ", listener=" + onSimOnOffSwitchListener);
    }

    public void unregisterOnSimOnOffSwitch() {
        Context context = this.mContext;
        if (context != null && this.mSimOnOffEnabled) {
            context.unregisterReceiver(this.mReceiver);
            Log.d("SimOnOffSwitchHandler", "unregisterOnSimOnOffSwitch, handler=" + this);
        }
        this.mListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimApplicationStateChanged(Intent intent) {
        OnSimOnOffSwitchListener onSimOnOffSwitchListener;
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d("SimOnOffSwitchHandler", "handleSimApplicationStateChange, extra=null");
            return;
        }
        int i = extras.getInt("slot", -1);
        if (SubscriptionManager.isValidSlotIndex(i)) {
            int simOnOffState = MtkTelephonyManagerEx.getDefault().getSimOnOffState(this.mSlotId);
            Log.d("SimOnOffSwitchHandler", "handleSimApplicationStateChange, slotId=" + i + ", prevState=" + this.mSimState + ", currState=" + simOnOffState);
            if (this.mSimState != simOnOffState && (onSimOnOffSwitchListener = this.mListener) != null) {
                onSimOnOffSwitchListener.onSimOnOffStateChanged();
            }
            this.mSimState = simOnOffState;
        }
    }
}

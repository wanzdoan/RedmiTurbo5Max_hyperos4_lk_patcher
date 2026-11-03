package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.telephony.Rlog;
import com.ot.pubsub.b.a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BatteryStateManager {
    private static BatteryStateManager INSTANCE = new BatteryStateManager();
    private BroadcastReceiver mBatteryReceiver;
    public int mChargeState;
    private Context mContext;
    public int mBatteryLevel = -1;
    private List mListeners = new ArrayList();

    public interface BatteryStateListener {
        void notifyBatteryStateChanged(int i);
    }

    private BatteryStateManager() {
    }

    public static BatteryStateManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.BatteryStateManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Rlog.i("BatteryStateManager", "BatteryStateManager onReceive action " + intent.getAction());
                if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra(a.d, 0);
                    int intExtra2 = intent.getIntExtra("scale", 100);
                    BatteryStateManager.this.mChargeState = intent.getIntExtra("plugged", 0);
                    BatteryStateManager batteryStateManager = BatteryStateManager.this;
                    batteryStateManager.mBatteryLevel = (intExtra * 100) / intExtra2;
                    batteryStateManager.notifyStateChanged();
                }
            }
        };
        this.mBatteryReceiver = broadcastReceiver;
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
    }

    public int getmBatteryLevel() {
        return this.mBatteryLevel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStateChanged() {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((BatteryStateListener) it.next()).notifyBatteryStateChanged(this.mBatteryLevel);
        }
    }

    public void registerBatteryStateListener(BatteryStateListener batteryStateListener) {
        if (this.mListeners.contains(batteryStateListener)) {
            return;
        }
        batteryStateListener.notifyBatteryStateChanged(this.mBatteryLevel);
        this.mListeners.add(batteryStateListener);
    }

    public void unregisterBatteryStateListener(BatteryStateListener batteryStateListener) {
        this.mListeners.remove(batteryStateListener);
    }

    public boolean isBatteryCharging() {
        return this.mChargeState > 0;
    }
}

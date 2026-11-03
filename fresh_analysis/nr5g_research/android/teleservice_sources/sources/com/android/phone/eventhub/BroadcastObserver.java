package com.android.phone.eventhub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.phone.eventhub.events.BroadcastEvent;
import com.android.phone.eventhub.events.EventDispatcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastObserver extends EventObserver {
    private final Context mContext;
    private List mIntentFilters;
    private BroadcastReceiver mReceiver;

    public BroadcastObserver(Context context, EventDispatcher eventDispatcher) {
        super(context, eventDispatcher);
        this.mContext = context;
    }

    @Override // com.android.phone.eventhub.EventObserver
    public void startTracking() {
        if (this.mReceiver == null) {
            initialize();
        }
        try {
            Iterator it = this.mIntentFilters.iterator();
            while (it.hasNext()) {
                this.mContext.registerReceiver(this.mReceiver, (IntentFilter) it.next(), null, null, 2);
            }
        } catch (Exception e) {
            loge("Failed to start broadcast tracking: " + e);
        }
    }

    private void initialize() {
        ArrayList arrayList = new ArrayList();
        this.mIntentFilters = arrayList;
        arrayList.add(new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.mIntentFilters.add(new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        this.mIntentFilters.add(new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
        this.mIntentFilters.add(new IntentFilter("android.intent.action.ACTION_IMS_REGISTED"));
        this.mIntentFilters.add(new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        this.mIntentFilters.add(new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
        this.mIntentFilters.add(new IntentFilter("android.telephony.action.SIM_CARD_STATE_CHANGED"));
        this.mIntentFilters.add(new IntentFilter("com.android.phone.intent.action.WECHAT_VOIP_SCENE"));
        this.mIntentFilters.add(new IntentFilter("com.miui.powerkeeper_sleep_changed"));
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.eventhub.BroadcastObserver.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                BroadcastEvent broadcastEvent = new BroadcastEvent(intent.getAction(), intent.getExtras());
                BroadcastObserver.this.notifyEvent(broadcastEvent);
                BroadcastObserver.this.logd(broadcastEvent.toString());
            }
        };
    }
}

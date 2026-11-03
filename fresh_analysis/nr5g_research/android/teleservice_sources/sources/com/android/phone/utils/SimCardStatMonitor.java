package com.android.phone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.android.phone.PhoneGlobals;
import java.util.HashMap;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class SimCardStatMonitor extends Handler implements SubscriptionManager.OnSubscriptionsChangedListener {
    private boolean isNewCardInserted = true;
    private int mSubSize;

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            recordDeaultVoiceEvent(message.arg1);
        } else {
            int size = SubscriptionManager.getDefault().getSubscriptionInfoList().size();
            this.mSubSize = size;
            if (size == 2 || size == 1) {
                recordCountEvent(size);
            }
        }
    }

    class TelephonyBroadcastReceiver extends BroadcastReceiver {
        private TelephonyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                SimCardStatMonitor.this.removeMessages(2);
                Message messageObtainMessage = SimCardStatMonitor.this.obtainMessage(2);
                messageObtainMessage.arg1 = intent.getIntExtra("subscription", SubscriptionManager.DEFAULT_SUBSCRIPTION_ID);
                SimCardStatMonitor.this.sendMessageDelayed(messageObtainMessage, 4000L);
            }
        }
    }

    public SimCardStatMonitor() {
        SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
        PhoneGlobals.getInstance().registerReceiver(new TelephonyBroadcastReceiver(), new IntentFilter("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED"), 2);
    }

    public void onSubscriptionsChanged() {
        int size = SubscriptionManager.getDefault().getSubscriptionInfoList().size();
        this.mSubSize = size;
        if (size == 2) {
            if (this.isNewCardInserted) {
                this.isNewCardInserted = false;
                recordCountEvent(size);
                return;
            }
            return;
        }
        if (size == 1) {
            if (this.isNewCardInserted) {
                this.isNewCardInserted = false;
                sendEmptyMessageDelayed(1, 4000L);
                return;
            }
            return;
        }
        this.isNewCardInserted = true;
    }

    private void recordCountEvent(int i) {
        HashMap map = new HashMap();
        map.put("sim_count", String.valueOf(i));
        MiStatInterfaceUtil.recordCountEvent("event_sim_count", "sim_count", map);
    }

    private void recordDeaultVoiceEvent(int i) {
        String str;
        int slotIdForSubscription = SubscriptionManager.getDefault().getSlotIdForSubscription(i);
        if ((slotIdForSubscription == SubscriptionManager.INVALID_SLOT_ID || slotIdForSubscription == 0 || slotIdForSubscription == 1) && this.mSubSize == 2) {
            if (slotIdForSubscription == 0) {
                str = "sim_1";
            } else if (slotIdForSubscription != 1) {
                str = "sim_not_set";
            } else {
                str = "sim_2";
            }
            HashMap map = new HashMap();
            map.put("key_default_voice_slot", str);
            MiStatInterfaceUtil.recordCountEvent("event_default_voice_slot", "default_voice_slot", map);
        }
    }
}

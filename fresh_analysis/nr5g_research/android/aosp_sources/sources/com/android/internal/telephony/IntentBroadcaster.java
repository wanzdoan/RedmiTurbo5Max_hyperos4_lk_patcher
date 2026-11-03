package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class IntentBroadcaster {
    private static IntentBroadcaster sIntentBroadcaster;
    private Map mRebroadcastIntents = new HashMap();
    private final BroadcastReceiver mReceiver;

    private IntentBroadcaster(Context context) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.IntentBroadcaster.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.intent.action.USER_UNLOCKED")) {
                    synchronized (IntentBroadcaster.this.mRebroadcastIntents) {
                        try {
                            Iterator it = IntentBroadcaster.this.mRebroadcastIntents.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry entry = (Map.Entry) it.next();
                                Intent intent2 = (Intent) entry.getValue();
                                intent2.putExtra("rebroadcastOnUnlock", true);
                                it.remove();
                                IntentBroadcaster.this.logd("Rebroadcasting intent " + intent2.getAction() + " " + intent2.getStringExtra("ss") + " for slotId " + entry.getKey());
                                context2.sendStickyBroadcastAsUser(intent2, UserHandle.ALL);
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        context.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
    }

    public static IntentBroadcaster getInstance(Context context) {
        if (sIntentBroadcaster == null) {
            sIntentBroadcaster = new IntentBroadcaster(context);
        }
        return sIntentBroadcaster;
    }

    public static IntentBroadcaster getInstance() {
        return sIntentBroadcaster;
    }

    public void broadcastStickyIntent(Context context, Intent intent, int i) {
        logd("Broadcasting and adding intent for rebroadcast: " + intent.getAction() + " " + intent.getStringExtra("ss") + " for phoneId " + i);
        synchronized (this.mRebroadcastIntents) {
            context.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            this.mRebroadcastIntents.put(Integer.valueOf(i), intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("IntentBroadcaster", str);
    }
}

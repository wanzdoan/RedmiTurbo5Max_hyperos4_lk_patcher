package com.android.phone.eventhub;

import android.content.Context;
import android.util.Log;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.EventDispatcher;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public abstract class EventObserver {
    protected Context mContext;
    protected EventDispatcher mDispatcher;

    public abstract void startTracking();

    public EventObserver(Context context, EventDispatcher eventDispatcher) {
        this.mContext = context.getApplicationContext();
        this.mDispatcher = eventDispatcher;
    }

    protected void notifyEvent(BaseEvent baseEvent) {
        EventDispatcher eventDispatcher = this.mDispatcher;
        if (eventDispatcher != null) {
            eventDispatcher.dispatchEvent(baseEvent);
        }
    }

    protected void logd(String str) {
        if (PhoneDebug.VDBG) {
            Log.d("event_observer", str);
        }
    }

    protected void loge(String str) {
        Log.e("event_observer", str);
    }
}

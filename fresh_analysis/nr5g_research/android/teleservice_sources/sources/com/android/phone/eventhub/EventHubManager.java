package com.android.phone.eventhub;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.EventDispatcher;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class EventHubManager {
    private static final String TAG = "EventHubManager";
    private static volatile EventHubManager sInstance;
    private EventDispatcher mDispatcher;
    private volatile boolean mInitialized = false;
    private final Map mObservers = new ConcurrentHashMap();

    public static EventHubManager getInstance() {
        if (sInstance == null) {
            synchronized (EventHubManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new EventHubManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        if (this.mInitialized) {
            Log.w(TAG, "EventHubManager already initialized");
            return;
        }
        this.mDispatcher = new EventDispatcher();
        initializeObservers(context);
        this.mInitialized = true;
    }

    private void initializeObservers(Context context) {
        Log.d(TAG, "initializeObservers");
        this.mObservers.put("broadcast", new BroadcastObserver(context, this.mDispatcher));
        this.mObservers.put(DeviceStateKt.DEVICE_STATE_CATEGORY, new DeviceStateObserver(context, this.mDispatcher));
        this.mObservers.put("telephony", new TelephonyObserver(context, this.mDispatcher));
        Iterator it = this.mObservers.values().iterator();
        while (it.hasNext()) {
            ((EventObserver) it.next()).startTracking();
        }
    }

    public void registerListener(String str, BaseEvent.IEventListener iEventListener) {
        this.mDispatcher.registerListener(str, iEventListener, null);
    }

    public void registerListener(String str, String str2, BaseEvent.IEventListener iEventListener) {
        this.mDispatcher.registerListener(str, str2, iEventListener, (Handler) null);
    }

    public void registerListener(String str, String str2, BaseEvent.IEventListener iEventListener, Handler handler) {
        this.mDispatcher.registerListener(str, str2, iEventListener, handler);
    }

    public void registerListener(String str, String[] strArr, BaseEvent.IEventListener iEventListener) {
        this.mDispatcher.registerListener(str, strArr, iEventListener, (Handler) null);
    }

    public void registerListener(String str, String[] strArr, BaseEvent.IEventListener iEventListener, Handler handler) {
        this.mDispatcher.registerListener(str, strArr, iEventListener, handler);
    }

    public void unregisterListener(BaseEvent.IEventListener iEventListener) {
        this.mDispatcher.unregisterListener(iEventListener);
    }

    public void postEvent(BaseEvent baseEvent) {
        EventDispatcher eventDispatcher = this.mDispatcher;
        if (eventDispatcher == null) {
            Log.w(TAG, "Not initialized, dropping event");
        } else {
            eventDispatcher.dispatchEvent(baseEvent);
        }
    }
}

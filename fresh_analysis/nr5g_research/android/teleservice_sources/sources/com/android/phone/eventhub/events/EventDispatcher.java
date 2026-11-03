package com.android.phone.eventhub.events;

import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Predicate;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class EventDispatcher {
    private static final String TAG = "EventDispatcher";
    private final Object mLock = new Object();
    private final Map mListenerMap = new HashMap();
    private final Map mStickyEvents = new HashMap();
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor(new ThreadFactory(this) { // from class: com.android.phone.eventhub.events.EventDispatcher.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "EventDispatcher");
        }
    });

    class ListenerEntry {
        final Handler handler;
        final BaseEvent.IEventListener listener;

        ListenerEntry(BaseEvent.IEventListener iEventListener, Handler handler) {
            this.listener = iEventListener;
            this.handler = handler;
        }
    }

    public void registerListener(String str, BaseEvent.IEventListener iEventListener, Handler handler) {
        registerListener(str, (String[]) null, iEventListener, handler);
    }

    public void registerListener(String str, String str2, BaseEvent.IEventListener iEventListener, Handler handler) {
        registerListener(str, new String[]{str2}, iEventListener, handler);
    }

    public void registerListener(String str, String[] strArr, BaseEvent.IEventListener iEventListener, Handler handler) {
        if (PhoneDebug.VDBG) {
            Log.d(TAG, "registerListener: subTypes: " + Arrays.toString(strArr) + " ,listener: " + iEventListener);
        }
        if (str == null || iEventListener == null) {
            return;
        }
        synchronized (this.mLock) {
            try {
                Map map = (Map) this.mListenerMap.get(str);
                if (map == null) {
                    map = new HashMap();
                    this.mListenerMap.put(str, map);
                }
                if (strArr == null || strArr.length == 0) {
                    strArr = new String[]{"*"};
                }
                ListenerEntry listenerEntry = new ListenerEntry(iEventListener, handler);
                for (String str2 : strArr) {
                    List arrayList = (List) map.get(str2);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        map.put(str2, arrayList);
                    }
                    Iterator it = arrayList.iterator();
                    do {
                        if (!it.hasNext()) {
                            arrayList.add(listenerEntry);
                            break;
                        }
                    } while (((ListenerEntry) it.next()).listener != iEventListener);
                    BaseEvent baseEvent = (BaseEvent) this.mStickyEvents.get(stickyKey(str, str2));
                    if (baseEvent != null) {
                        notifyListener(listenerEntry, baseEvent);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void unregisterListener(final BaseEvent.IEventListener iEventListener) {
        if (iEventListener == null) {
            return;
        }
        synchronized (this.mLock) {
            try {
                for (Map map : this.mListenerMap.values()) {
                    Iterator it = map.values().iterator();
                    while (it.hasNext()) {
                        ((List) it.next()).removeIf(new Predicate() { // from class: com.android.phone.eventhub.events.EventDispatcher$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return EventDispatcher.m2507$r8$lambda$e4oJxHHxxFCZW4Jf3MnDJN30uE(iEventListener, (EventDispatcher.ListenerEntry) obj);
                            }
                        });
                    }
                    map.entrySet().removeIf(new Predicate() { // from class: com.android.phone.eventhub.events.EventDispatcher$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return ((List) ((Map.Entry) obj).getValue()).isEmpty();
                        }
                    });
                }
                this.mListenerMap.entrySet().removeIf(new Predicate() { // from class: com.android.phone.eventhub.events.EventDispatcher$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ((Map) ((Map.Entry) obj).getValue()).isEmpty();
                    }
                });
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$e4oJxHHxxFCZW4-Jf3MnDJN30uE, reason: not valid java name */
    public static /* synthetic */ boolean m2507$r8$lambda$e4oJxHHxxFCZW4Jf3MnDJN30uE(BaseEvent.IEventListener iEventListener, ListenerEntry listenerEntry) {
        return listenerEntry.listener == iEventListener;
    }

    public void dispatchEvent(BaseEvent baseEvent) {
        List list;
        if (baseEvent == null) {
            return;
        }
        synchronized (this.mLock) {
            try {
                String type = baseEvent.getType();
                String subType = baseEvent.getSubType();
                if (baseEvent.isSticky()) {
                    this.mStickyEvents.put(stickyKey(type, subType), baseEvent);
                }
                Map map = (Map) this.mListenerMap.get(type);
                if (map != null) {
                    List list2 = (List) map.get("*");
                    if (list2 != null && !list2.isEmpty()) {
                        Iterator it = list2.iterator();
                        while (it.hasNext()) {
                            notifyListener((ListenerEntry) it.next(), baseEvent);
                        }
                    }
                    if (subType != null && (list = (List) map.get(subType)) != null && !list.isEmpty()) {
                        Iterator it2 = list.iterator();
                        while (it2.hasNext()) {
                            notifyListener((ListenerEntry) it2.next(), baseEvent);
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void notifyListener(final ListenerEntry listenerEntry, final BaseEvent baseEvent) {
        Runnable runnable = new Runnable() { // from class: com.android.phone.eventhub.events.EventDispatcher$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                EventDispatcher.$r8$lambda$1X76AAlZVoJoxtRnUvt5VBhjJfs(listenerEntry, baseEvent);
            }
        };
        Handler handler = listenerEntry.handler;
        if (handler != null) {
            handler.post(runnable);
        } else {
            this.mExecutorService.execute(runnable);
        }
    }

    public static /* synthetic */ void $r8$lambda$1X76AAlZVoJoxtRnUvt5VBhjJfs(ListenerEntry listenerEntry, BaseEvent baseEvent) {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            listenerEntry.listener.onEvent(baseEvent);
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            if (PhoneDebug.VDBG) {
                Log.d(TAG, "notifyListener, listener: " + listenerEntry.listener + " ,event: " + baseEvent + " ,duration: " + jCurrentTimeMillis2);
            }
            if (jCurrentTimeMillis2 > 100) {
                Log.w(TAG, "Listener " + listenerEntry.listener.getClass().getSimpleName() + " took " + jCurrentTimeMillis2 + "ms to process event: " + baseEvent.getType() + "/" + baseEvent.getSubType());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error dispatching event to listener: ".concat(listenerEntry.listener.getClass().getSimpleName()), e);
        }
    }

    private static String stickyKey(String str, String str2) {
        return (str + ":" + str2).intern();
    }
}

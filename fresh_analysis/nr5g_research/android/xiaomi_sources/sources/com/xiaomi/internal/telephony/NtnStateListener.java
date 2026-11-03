package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.RegistrantList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NtnStateListener {
    private static final String LOG_TAG = "NtnStateListener";
    private static final int STATE_LISTENER_STATE_IDLE = 0;
    private static final int STATE_LISTENER_STATE_RUNING = 1;
    public static final int TYPE_NTN_RADIO_POWER_STATE = 1;
    private static Map<Integer, Integer> mListenerStateMap = new HashMap();
    private static Map<Integer, RegistrantList> mRegRequestInfoMap = new HashMap();
    private static Context mContext = null;
    private static NtnStateListener mInstance = null;

    public static void registerNtnStateChangeEvent(Context context, int eventType, Handler handler, int what, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerNtnStateChangeEvent, context = " + context + ", eventType = " + eventType + ", handler = " + handler + ", what = " + what + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                RegistrantList registrants = new RegistrantList();
                registrants.addUnique(handler, what, obj);
                mRegRequestInfoMap.put(Integer.valueOf(eventType), registrants);
            } else {
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).addUnique(handler, what, obj);
            }
        }
    }

    public static void unregisterNtnStateChangeEvent(int eventType, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterNtnStateChangeEvent, eventType = " + eventType + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                return;
            }
            mRegRequestInfoMap.get(Integer.valueOf(eventType)).remove(handler);
        }
    }

    private static synchronized void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NtnStateListener(context);
            ModemSarLog.d(LOG_TAG, "initInstance done");
        }
    }

    private NtnStateListener(Context context) {
        mContext = context;
    }
}

package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import com.android.internal.telephony.RegistrantList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NtnStateListener {
    private static final String LOG_TAG = "NtnStateListener";
    private static final int STATE_LISTENER_STATE_IDLE = 0;
    private static final int STATE_LISTENER_STATE_RUNING = 1;
    public static final int TYPE_NTN_RADIO_POWER_STATE = 1;
    private static Map mListenerStateMap = new HashMap();
    private static Map mRegRequestInfoMap = new HashMap();
    private static Context mContext = null;
    private static NtnStateListener mInstance = null;

    public static void registerNtnStateChangeEvent(Context context, int i, Handler handler, int i2, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerNtnStateChangeEvent, context = " + context + ", eventType = " + i + ", handler = " + handler + ", what = " + i2 + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    RegistrantList registrantList = new RegistrantList();
                    registrantList.addUnique(handler, i2, obj);
                    mRegRequestInfoMap.put(Integer.valueOf(i), registrantList);
                } else {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).addUnique(handler, i2, obj);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void unregisterNtnStateChangeEvent(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterNtnStateChangeEvent, eventType = " + i + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    return;
                }
                ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).remove(handler);
            } catch (Throwable th) {
                throw th;
            }
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

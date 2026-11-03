package com.xiaomi.modem;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class OemHookAgent extends MiRilHook {
    private static Context mContext = null;
    private static OemHookAgent mHookAgent = null;
    private static int mMiRilHookMakeRetryCount = 0;
    private static volatile boolean mMiRilHookReady = false;
    private static RegistrantList mHookReadyRegistrants = new RegistrantList();
    private static RegistrantList mHookDisconnectedRegistrants = new RegistrantList();
    private static List mHookReadyCallbacks = new ArrayList();
    private static List mHookDisconnectedCallbacks = new ArrayList();
    public static Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.modem.OemHookAgent.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            OemHookAgent.log("handleMessage msg id: " + message.what);
            int i = message.what;
            if (i == -3) {
                OemHookAgent.log("EVENT_MIRIL_HOOK_MAKE_RETRY");
                OemHookAgent.makeHookRetry();
                return;
            }
            if (i == -2) {
                OemHookAgent.log("EVENT_MIRIL_HOOK_DISCONNECTED");
                OemHookAgent.notifyHookDisconnected();
            } else if (i == -1) {
                OemHookAgent.log("EVENT_MIRIL_HOOK_READY");
                OemHookAgent.notifyHookReady();
            } else {
                OemHookAgent.log("invalid msg id " + message.what);
            }
        }
    };
    private static MiRilHookCallback mQcRilHookCb = new MiRilHookCallback() { // from class: com.xiaomi.modem.OemHookAgent.2
    };

    public interface IHookReadyCallBack {
        void onHookReady(OemHookAgent oemHookAgent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void makeHookRetry() {
        log("makeHookRetry, mMiRilHookReady=" + mMiRilHookReady + ", mHookDisconnectedRegistrants=" + mHookDisconnectedRegistrants + ", mHookReadyCallbacks=" + mHookReadyCallbacks + ", mHookAgent =" + getHook() + ", mContext =" + mContext + ", old mMiRilHookMakeRetryCount =" + mMiRilHookMakeRetryCount);
        if (mMiRilHookMakeRetryCount > 5) {
            log("makeHookRetry, the retry count has been reached the max value, will do nothing.");
        } else {
            if (mMiRilHookReady) {
                log("makeHookRetry, the hook agent has been ready, will do nothing.");
                return;
            }
            mHookAgent = null;
            makeHook(mContext);
            mMiRilHookMakeRetryCount++;
        }
    }

    public static void registerHookReadyCallback(IHookReadyCallBack iHookReadyCallBack, Context context) {
        log("registerHookReadyCallback, context=" + context + ", mMiRilHookReady=" + mMiRilHookReady);
        synchronized (mHookReadyCallbacks) {
            try {
                makeHook(context);
                if (iHookReadyCallBack != null && !mHookReadyCallbacks.contains(iHookReadyCallBack)) {
                    mHookReadyCallbacks.add(iHookReadyCallBack);
                    if (mMiRilHookReady) {
                        iHookReadyCallBack.onHookReady(getHook());
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookReady() {
        log("notifyHookReady, mMiRilHookReady=" + mMiRilHookReady + ", mHookDisconnectedRegistrants=" + mHookDisconnectedRegistrants + ", mHookReadyCallbacks=" + mHookReadyCallbacks + ", mHookAgent =" + getHook());
        mMiRilHookReady = true;
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.notifyRegistrants();
        }
        synchronized (mHookReadyCallbacks) {
            try {
                Iterator it = mHookReadyCallbacks.iterator();
                while (it.hasNext()) {
                    ((IHookReadyCallBack) it.next()).onHookReady(getHook());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookDisconnected() {
        log("notifyHookDisconnected, mMiRilHookReady=" + mMiRilHookReady + ", mHookDisconnectedRegistrants=" + mHookDisconnectedRegistrants + ", mHookDisconnectedCallbacks=" + mHookDisconnectedCallbacks);
        mMiRilHookReady = false;
        synchronized (mHookDisconnectedRegistrants) {
            mHookDisconnectedRegistrants.notifyRegistrants();
        }
        synchronized (mHookDisconnectedCallbacks) {
            try {
                Iterator it = mHookDisconnectedCallbacks.iterator();
                if (it.hasNext()) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                    throw null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void registerHookReadyEvent(Context context, Handler handler, int i, Object obj) {
        log("registerHookReadyEvent, context = " + context + ", handler = " + handler + ", what = " + i + ", obj = " + obj + ", mMiRilHookReady = " + mMiRilHookReady);
        makeHook(context);
        Registrant registrant = new Registrant(handler, i, obj);
        synchronized (mHookReadyRegistrants) {
            try {
                mHookReadyRegistrants.add(registrant);
                if (mMiRilHookReady) {
                    registrant.notifyRegistrant();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void unregisterHookReadyEvent(Handler handler) {
        log("unregisterHookReadyEvent, handler = " + handler + ", mMiRilHookReady = " + mMiRilHookReady);
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.remove(handler);
        }
    }

    public static void registerHookDisconnectedEvent(Context context, Handler handler, int i, Object obj) {
        log("registerHookDisconnectedEvent, context = " + context + ", handler = " + handler + ", what = " + i + ", obj = " + obj + ", mMiRilHookReady = " + mMiRilHookReady);
        Registrant registrant = new Registrant(handler, i, obj);
        synchronized (mHookDisconnectedRegistrants) {
            mHookDisconnectedRegistrants.add(registrant);
        }
    }

    public static void unregisterHookDisconnectedEvent(Handler handler) {
        log("unregisterHookDisconnectedEvent, handler = " + handler + ", mMiRilHookReady = " + mMiRilHookReady);
        synchronized (mHookDisconnectedRegistrants) {
            mHookDisconnectedRegistrants.remove(handler);
        }
    }

    private static void makeHook(Context context) {
        log("makeHook, context = " + context + ", mHookType = " + getHookType() + ", mHookAgent = " + mHookAgent);
        if (mHookAgent != null) {
            log("makeHook, done, do nothing");
            return;
        }
        mContext = context;
        if (2 == getHookType()) {
            mHookAgent = new OemHookAgent(mContext, mQcRilHookCb, ActivityThread.currentPackageName());
        } else if (1 == getHookType() || getHookType() == 0 || 3 == getHookType() || 4 == getHookType()) {
            mHookAgent = new OemHookAgent(mContext, mHandler);
        } else {
            log("makeHook, mHookType is invalid, do nothing");
            return;
        }
        log("makeHook, end, mHookAgent = " + mHookAgent);
    }

    public static OemHookAgent getHook() {
        log("getHook, mMiRilHookReady = " + mMiRilHookReady + ", mHookAgent = " + mHookAgent);
        if (mMiRilHookReady) {
            return mHookAgent;
        }
        return null;
    }

    public static int getHookType() {
        return MiRilHook.getHookType();
    }

    public OemHookAgent(Context context, MiRilHookCallback miRilHookCallback, String str) {
        super(context, miRilHookCallback, str);
        log("OemHookAgent, mContext = " + mContext + ", cb = " + miRilHookCallback + ", packageName = " + str);
    }

    public OemHookAgent(Context context, Handler handler) {
        super(context, handler);
        log("OemHookAgent, mContext = " + mContext + ", handler = " + handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("OemHookAgent(" + ModemUtils.getCurProcessName(mContext) + ")", str);
    }
}

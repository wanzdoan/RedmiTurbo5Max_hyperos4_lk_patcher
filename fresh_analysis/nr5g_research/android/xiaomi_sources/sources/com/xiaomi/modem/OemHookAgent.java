package com.xiaomi.modem;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookCallback;
import java.util.ArrayList;
import java.util.List;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class OemHookAgent extends MiRilHook {
    private static final int EVENT_MIRIL_HOOK_DISCONNECTED = -2;
    private static final int EVENT_MIRIL_HOOK_MAKE_RETRY = -3;
    private static final int EVENT_MIRIL_HOOK_READY = -1;
    private static final String LOG_TAG = "OemHookAgent";
    private static final int MIRIL_HOOK_DISCONNECTED_NOTIFY_DELAY_MS = 0;
    private static final int MIRIL_HOOK_MAKE_RETRY_COUNT_MAX = 5;
    private static final int MIRIL_HOOK_MAKE_RETRY_DELAY_S = 20;
    private static final int MIRIL_HOOK_READY_NOTIFY_DELAY_MS = 0;
    private static volatile boolean mMiRilHookReady = false;
    private static int mMiRilHookMakeRetryCount = 0;
    private static Context mContext = null;
    private static OemHookAgent mHookAgent = null;
    private static RegistrantList mHookReadyRegistrants = new RegistrantList();
    private static RegistrantList mHookDisconnectedRegistrants = new RegistrantList();
    private static List<IHookReadyCallBack> mHookReadyCallbacks = new ArrayList();
    private static List<IHookDisconnectedCallBack> mHookDisconnectedCallbacks = new ArrayList();
    public static Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.modem.OemHookAgent.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            OemHookAgent.log("handleMessage msg id: " + msg.what);
            switch (msg.what) {
                case OemHookAgent.EVENT_MIRIL_HOOK_MAKE_RETRY /* -3 */:
                    OemHookAgent.log("EVENT_MIRIL_HOOK_MAKE_RETRY");
                    OemHookAgent.makeHookRetry();
                    break;
                case -2:
                    OemHookAgent.log("EVENT_MIRIL_HOOK_DISCONNECTED");
                    OemHookAgent.notifyHookDisconnected();
                    break;
                case -1:
                    OemHookAgent.log("EVENT_MIRIL_HOOK_READY");
                    OemHookAgent.notifyHookReady();
                    break;
                default:
                    OemHookAgent.log("invalid msg id " + msg.what);
                    break;
            }
        }
    };
    private static MiRilHookCallback mQcRilHookCb = new MiRilHookCallback() { // from class: com.xiaomi.modem.OemHookAgent.2
        public void onQcRilHookReady() {
            OemHookAgent.log("MiRilHookCallback onQcRilHookReady");
            OemHookAgent.notifyHookReadyQueue();
        }

        public void onQcRilHookDisconnected() {
            OemHookAgent.log("MiRilHookCallback onQcRilHookDisconnected");
            OemHookAgent.notifyHookDisconnectedQueue();
        }
    };

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    public interface IHookDisconnectedCallBack {
        void onHookDisconnected();
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
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

    public void onHookAgentResponse(int msgWhat, byte[] response, int phoneId) {
        log("onHookAgentResponse, phoneId: " + phoneId + ", msgWhat: " + msgWhat);
    }

    public static void registerHookReadyCallback(IHookReadyCallBack callback, Context context) {
        log("registerHookReadyCallback, context=" + context + ", mMiRilHookReady=" + mMiRilHookReady);
        synchronized (mHookReadyCallbacks) {
            makeHook(context);
            if (callback != null && !mHookReadyCallbacks.contains(callback)) {
                mHookReadyCallbacks.add(callback);
                if (mMiRilHookReady) {
                    callback.onHookReady(getHook());
                }
            }
        }
    }

    public static void unregisterHookReadyCallback(IHookReadyCallBack callback) {
        log("unregisterHookReadyCallback, callback=" + callback + ", mMiRilHookReady=" + mMiRilHookReady);
        synchronized (mHookReadyCallbacks) {
            mHookReadyCallbacks.remove(callback);
        }
    }

    public static void registerHookDisconnectedCallback(IHookDisconnectedCallBack callback, Context context) {
        log("registerHookDisconnectedCallback, context=" + context + ", mMiRilHookReady=" + mMiRilHookReady);
        synchronized (mHookDisconnectedCallbacks) {
            if (callback != null) {
                if (!mHookDisconnectedCallbacks.contains(callback)) {
                    mHookDisconnectedCallbacks.add(callback);
                }
            }
        }
    }

    public static void unregisterHookDisconnectedCallback(IHookDisconnectedCallBack callback) {
        log("unregisterHookDisconnectedCallback, callback=" + callback + ", mMiRilHookReady=" + mMiRilHookReady);
        synchronized (mHookDisconnectedCallbacks) {
            mHookDisconnectedCallbacks.remove(callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookReadyQueue() {
        log("notifyHookReadyQueue, mMiRilHookReady=" + mMiRilHookReady + ", mHandler=" + mHandler + ", mHookReadyCallbacks=" + mHookReadyCallbacks + ", mHookAgent =" + getHook());
        Message msg = Message.obtain();
        msg.what = -1;
        mHandler.removeMessages(-1);
        mHandler.removeMessages(-2);
        mHandler.sendMessageDelayed(msg, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookReady() {
        log("notifyHookReady, mMiRilHookReady=" + mMiRilHookReady + ", mHookDisconnectedRegistrants=" + mHookDisconnectedRegistrants + ", mHookReadyCallbacks=" + mHookReadyCallbacks + ", mHookAgent =" + getHook());
        mMiRilHookReady = true;
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.notifyRegistrants();
        }
        synchronized (mHookReadyCallbacks) {
            for (IHookReadyCallBack callback : mHookReadyCallbacks) {
                callback.onHookReady(getHook());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookDisconnectedQueue() {
        log("notifyHookDisconnectedQueue, mMiRilHookReady=" + mMiRilHookReady + ", mHandler=" + mHandler + ", mHookDisconnectedCallbacks=" + mHookDisconnectedCallbacks + ", mHookAgent =" + getHook());
        Message msg = Message.obtain();
        msg.what = -2;
        mHandler.removeMessages(-1);
        mHandler.removeMessages(-2);
        mHandler.sendMessageDelayed(msg, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyHookDisconnected() {
        log("notifyHookDisconnected, mMiRilHookReady=" + mMiRilHookReady + ", mHookDisconnectedRegistrants=" + mHookDisconnectedRegistrants + ", mHookDisconnectedCallbacks=" + mHookDisconnectedCallbacks);
        mMiRilHookReady = false;
        synchronized (mHookDisconnectedRegistrants) {
            mHookDisconnectedRegistrants.notifyRegistrants();
        }
        synchronized (mHookDisconnectedCallbacks) {
            for (IHookDisconnectedCallBack callback : mHookDisconnectedCallbacks) {
                callback.onHookDisconnected();
            }
        }
    }

    public static void registerHookReadyEvent(Context context, Handler handler, int what, Object obj) {
        log("registerHookReadyEvent, context = " + context + ", handler = " + handler + ", what = " + what + ", obj = " + obj + ", mMiRilHookReady = " + mMiRilHookReady);
        makeHook(context);
        Registrant r = new Registrant(handler, what, obj);
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.add(r);
            if (mMiRilHookReady) {
                r.notifyRegistrant();
            }
        }
    }

    public static void unregisterHookReadyEvent(Handler handler) {
        log("unregisterHookReadyEvent, handler = " + handler + ", mMiRilHookReady = " + mMiRilHookReady);
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.remove(handler);
        }
    }

    public static void registerHookDisconnectedEvent(Context context, Handler handler, int what, Object obj) {
        log("registerHookDisconnectedEvent, context = " + context + ", handler = " + handler + ", what = " + what + ", obj = " + obj + ", mMiRilHookReady = " + mMiRilHookReady);
        Registrant r = new Registrant(handler, what, obj);
        synchronized (mHookDisconnectedRegistrants) {
            mHookDisconnectedRegistrants.add(r);
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

    public OemHookAgent(Context context, MiRilHookCallback cb) {
        super(context, cb);
        log("OemHookAgent, mContext = " + mContext + ", cb = " + cb);
    }

    public OemHookAgent(Context context, MiRilHookCallback cb, String packageName) {
        super(context, cb, packageName);
        log("OemHookAgent, mContext = " + mContext + ", cb = " + cb + ", packageName = " + packageName);
    }

    public OemHookAgent(Context context, Handler handler) {
        super(context, handler);
        log("OemHookAgent, mContext = " + mContext + ", handler = " + handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        Log.d("OemHookAgent(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
    }
}

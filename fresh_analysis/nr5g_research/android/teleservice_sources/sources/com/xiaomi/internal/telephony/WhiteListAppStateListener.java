package com.xiaomi.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.util.ArrayUtils;
import com.xiaomi.modem.ModemUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class WhiteListAppStateListener {
    private static final long APP_WHITELIST_COUNT_TIMEOUT = 60000;
    public static final int APP_WHITELIST_FIRE_MODE = 1;
    public static final int APP_WHITELIST_NORMAL_MODE = 0;
    private static final long APP_WHITELIST_SAFETY_TIMEOUT = 180000;
    private static final int DELAY_FORG_TIME_MILLISECOND = 1000;
    private static final int DISABLE_APP_WHITELIST_FIRE_MODE_TIMEOUT = 1000;
    private static final String LOG_TAG = "WhiteListAppStateListener";
    private static final int STATE_LISTENER_STATE_IDLE = 0;
    private static final int STATE_LISTENER_STATE_RUNING = 1;
    public static final int TYPE_SCAN_CODE_STATE = 1;
    private static Context mContext;
    private static AppWhitelistHandler mFgHandler;
    private static HandlerThread mFgWorkThread;
    private static WhiteListAppStateListener mInstance;
    private static Map mListenerStateMap = new HashMap();
    private static Map mRegRequestInfoMap = new HashMap();
    private static ModemSarSynchronizedTimer mCountTimer = null;
    private static Runnable mCountTimerRunnable = null;
    private static ModemSarSynchronizedTimer mSafetyTimer = null;
    private static Runnable mSafetyTimerRunnable = null;
    private static int mLastMode = 0;
    private static long mStartTime = 0;
    private static String mCurrentApp = null;
    private static int mCurrentScanCodeState = 0;
    private static IActivityChangeListener.Stub mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.3
        public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged pre: " + componentName + ", cur: " + componentName2);
            if (componentName == null || componentName2 == null) {
                return;
            }
            String className = componentName2.getClassName();
            String className2 = componentName.getClassName();
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged preClassName: " + className2 + ", curClassName: " + className);
            if (!TextUtils.isEmpty(className) && ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity"), className)) {
                ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged enter ScanQR");
                WhiteListAppStateListener.mFgHandler.sendMessageDelayed(WhiteListAppStateListener.mFgHandler.obtainMessage(1, 1), 1000L);
                WhiteListAppStateListener.mCurrentApp = className;
            } else {
                if (TextUtils.isEmpty(className2) || !ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity"), className2)) {
                    return;
                }
                ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged exit ScanQR");
                WhiteListAppStateListener.mFgHandler.sendMessageDelayed(WhiteListAppStateListener.mFgHandler.obtainMessage(1, 0), 1000L);
            }
        }
    };

    public static void registerWhiteListAppStateListener(Context context, int i, Handler handler, int i2, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerWhiteListAppStateListener, context = " + context + ", eventType = " + i + ", handler = " + handler + ", what = " + i2 + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    RegistrantList registrantList = new RegistrantList();
                    registrantList.addUnique(handler, i2, obj);
                    mRegRequestInfoMap.put(Integer.valueOf(i), registrantList);
                } else {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).addUnique(handler, i2, obj);
                }
                startDeviceStateChangeListener(i, new Registrant(handler, i2, obj));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void unregisterWhiteListAppStateChangeEvent(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterWhiteListAppStateChangeEvent, eventType = " + i + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) == null) {
                    return;
                }
                ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).remove(handler);
                if (((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).size() == 0) {
                    stopDeviceStateChangeListener(i, handler);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static synchronized void initInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WhiteListAppStateListener(context);
            ModemSarLog.d(LOG_TAG, "initInstance done");
        }
    }

    private WhiteListAppStateListener(Context context) {
        mContext = context;
        if (context == null) {
            ModemSarLog.e(LOG_TAG, "Constructor, mContext is null");
            return;
        }
        HandlerThread handlerThread = new HandlerThread(LOG_TAG, 10);
        mFgWorkThread = handlerThread;
        handlerThread.start();
        mFgHandler = new AppWhitelistHandler(mFgWorkThread.getLooper());
        initTimers();
    }

    private static void startDeviceStateChangeListener(int i, Registrant registrant) {
        ModemSarLog.d(LOG_TAG, "startDeviceStateChangeListener, eventType = " + i + ", r = " + registrant);
        synchronized (mListenerStateMap) {
            if (i == 1) {
                try {
                    if (mListenerStateMap.get(Integer.valueOf(i)) == null || ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 0) {
                        startScanCodeListener();
                    } else {
                        notifyDeviceStateChangeEvent(i, registrant, mCurrentScanCodeState);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private static void stopDeviceStateChangeListener(int i, Handler handler) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + i + ", h = " + handler);
        synchronized (mListenerStateMap) {
            if (i == 1) {
                try {
                    if (mListenerStateMap.get(Integer.valueOf(i)) != null && ((Integer) mListenerStateMap.get(Integer.valueOf(i))).intValue() == 1) {
                        stopScanCodeListener();
                        mCurrentScanCodeState = 0;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private static void notifyDeviceStateChangeEvent(int i, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + i + ", intValue = " + i2);
        synchronized (mRegRequestInfoMap) {
            try {
                if (mRegRequestInfoMap.get(Integer.valueOf(i)) != null) {
                    ((RegistrantList) mRegRequestInfoMap.get(Integer.valueOf(i))).notifyRegistrants(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void notifyDeviceStateChangeEvent(int i, Registrant registrant, int i2) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + i + ", r = " + registrant + ", intValue = " + i2);
        if (registrant == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ModemUtils.getBytes(i2), (Throwable) null));
        }
    }

    private static void startScanCodeListener() {
        ModemSarLog.d(LOG_TAG, "startScanCodeListener");
        resetTimer();
        if (startAppMonitorJob()) {
            mListenerStateMap.put(1, 1);
        }
        showSupportedApps();
    }

    private static void stopScanCodeListener() {
        ModemSarLog.d(LOG_TAG, "stopScanCodeListener");
        stopTimer();
        if (stopAppMonitorJob()) {
            mListenerStateMap.put(1, 0);
        }
    }

    public static void initTimers() {
        Runnable runnable = new Runnable() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.1
            @Override // java.lang.Runnable
            public void run() {
                if (WhiteListAppStateListener.mSafetyTimer != null) {
                    WhiteListAppStateListener.mCurrentScanCodeState = 0;
                    WhiteListAppStateListener.updateScanCodeMode(WhiteListAppStateListener.mCurrentScanCodeState);
                    WhiteListAppStateListener.mSafetyTimer.start();
                }
            }
        };
        mCountTimerRunnable = runnable;
        mCountTimer = new ModemSarSynchronizedTimer("AppMonitorCountTimer", 60000L, runnable);
        Runnable runnable2 = new Runnable() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.2
            @Override // java.lang.Runnable
            public void run() {
                if (WhiteListAppStateListener.mCountTimer != null) {
                    WhiteListAppStateListener.mCountTimer.resetTimer();
                }
            }
        };
        mSafetyTimerRunnable = runnable2;
        mSafetyTimer = new ModemSarSynchronizedTimer("AppMonitorSafetyTimer", APP_WHITELIST_SAFETY_TIMEOUT, runnable2);
    }

    public static boolean startAppMonitorJob() {
        try {
            registerActivityChangeListener();
            return true;
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "startAppMonitorJob exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean stopAppMonitorJob() {
        try {
            unregisterActivityChangeListener();
            mFgHandler.removeCallbacksAndMessages(null);
            return true;
        } catch (Exception e) {
            ModemSarLog.e(LOG_TAG, "stopAppMonitorJob exception:" + e.getMessage());
            return false;
        }
    }

    private static void showSupportedApps() {
        if (HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list") == null) {
            ModemSarLog.e(LOG_TAG, "No supported app");
            return;
        }
        Iterator it = HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list").iterator();
        while (it.hasNext()) {
            ModemSarLog.i(LOG_TAG, "Supported App:" + ((String) it.next()));
        }
    }

    private static void stopTimer() {
        ModemSarSynchronizedTimer modemSarSynchronizedTimer = mSafetyTimer;
        if (modemSarSynchronizedTimer != null) {
            modemSarSynchronizedTimer.stop();
        }
        ModemSarSynchronizedTimer modemSarSynchronizedTimer2 = mCountTimer;
        if (modemSarSynchronizedTimer2 != null) {
            modemSarSynchronizedTimer2.stop();
        }
    }

    private static void resetTimer() {
        ModemSarSynchronizedTimer modemSarSynchronizedTimer = mSafetyTimer;
        if (modemSarSynchronizedTimer != null) {
            modemSarSynchronizedTimer.resetTimer();
        }
        ModemSarSynchronizedTimer modemSarSynchronizedTimer2 = mCountTimer;
        if (modemSarSynchronizedTimer2 != null) {
            modemSarSynchronizedTimer2.resetTimer();
        }
    }

    class AppWhitelistHandler extends Handler {
        AppWhitelistHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "handleMessage=" + message.what);
            if (message.what != 1) {
                return;
            }
            WhiteListAppStateListener.processForegAndScanCodeEvent(((Integer) message.obj).intValue());
        }
    }

    private static void registerActivityChangeListener() {
        ModemSarLog.d(LOG_TAG, "registerActivityChangeListener");
        if (mActivityChangeListener != null) {
            List stringArray = HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity");
            List stringArray2 = HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list");
            ProcessManager.unregisterActivityChanageListener(mActivityChangeListener);
            ProcessManager.registerActivityChangeListener(stringArray2, stringArray, mActivityChangeListener);
        }
    }

    private static void unregisterActivityChangeListener() {
        ModemSarLog.d(LOG_TAG, "unregisterActivityChangeListener");
        IActivityChangeListener.Stub stub = mActivityChangeListener;
        if (stub != null) {
            ProcessManager.unregisterActivityChanageListener(stub);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processForegAndScanCodeEvent(int i) {
        ModemSarLog.d(LOG_TAG, "processForegroundInfoEvent mode = " + i);
        if (mSafetyTimer.isTimerRunning()) {
            ModemSarLog.d(LOG_TAG, "SafetyTimer is running");
            mCurrentScanCodeState = 0;
            return;
        }
        if (i == mCurrentScanCodeState) {
            ModemSarLog.d(LOG_TAG, "Already in mode:" + i);
            return;
        }
        mCurrentScanCodeState = i;
        if (i == 0) {
            mCountTimer.stop();
        } else if (i == 1) {
            mCountTimer.start();
        } else {
            ModemSarLog.e(LOG_TAG, "Illegal Mode!");
            return;
        }
        updateScanCodeMode(mCurrentScanCodeState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateScanCodeMode(int i) {
        ModemSarLog.d(LOG_TAG, "updateScanCodeMode:" + i);
        notifyDeviceStateChangeEvent(1, i);
        if (i == 0) {
            if (mLastMode == 1) {
                long jCurrentTimeMillis = (System.currentTimeMillis() - mStartTime) / 1000;
                HashMap map = new HashMap();
                map.put("time", String.valueOf(jCurrentTimeMillis));
                map.put("app", mCurrentApp);
                ModemSarStatistics.reportTxWhitelistScancodeData(map);
                ModemSarLog.i(LOG_TAG, "whitelistHashMap: " + map);
                mLastMode = 0;
                mStartTime = 0L;
                mCurrentApp = null;
                return;
            }
            return;
        }
        if (i == 1 && mLastMode == 0) {
            mStartTime = System.currentTimeMillis();
            mLastMode = 1;
        }
    }
}

package com.xiaomi.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import com.xiaomi.modem.ModemUtils;
import java.util.HashMap;
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
    private static AppWhitelistHandler mFgHandler;
    private static HandlerThread mFgWorkThread;
    private static Context mContext = null;
    private static WhiteListAppStateListener mInstance = null;
    private static Map<Integer, Integer> mListenerStateMap = new HashMap();
    private static Map<Integer, RegistrantList> mRegRequestInfoMap = new HashMap();
    private static ModemSarSynchronizedTimer mCountTimer = null;
    private static Runnable mCountTimerRunnable = null;
    private static ModemSarSynchronizedTimer mSafetyTimer = null;
    private static Runnable mSafetyTimerRunnable = null;
    private static int mLastMode = 0;
    private static long mStartTime = 0;
    private static String mCurrentApp = null;
    private static int mCurrentScanCodeState = 0;
    private static IActivityChangeListener.Stub mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.3
        public void onActivityChanged(ComponentName preName, ComponentName curName) {
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged pre: " + preName + ", cur: " + curName);
            if (preName == null || curName == null) {
                return;
            }
            String curClassName = curName.getClassName();
            String preClassName = preName.getClassName();
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged preClassName: " + preClassName + ", curClassName: " + curClassName);
            if (!TextUtils.isEmpty(curClassName) && ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity"), curClassName)) {
                ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged enter ScanQR");
                WhiteListAppStateListener.mFgHandler.sendMessageDelayed(WhiteListAppStateListener.mFgHandler.obtainMessage(1, 1), 1000L);
                WhiteListAppStateListener.mCurrentApp = curClassName;
            } else if (!TextUtils.isEmpty(preClassName) && ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity"), preClassName)) {
                ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "onActivityChanged exit ScanQR");
                WhiteListAppStateListener.mFgHandler.sendMessageDelayed(WhiteListAppStateListener.mFgHandler.obtainMessage(1, 0), 1000L);
            }
        }
    };

    public static void registerWhiteListAppStateListener(Context context, int eventType, Handler handler, int what, Object obj) {
        initInstance(context);
        ModemSarLog.d(LOG_TAG, "registerWhiteListAppStateListener, context = " + context + ", eventType = " + eventType + ", handler = " + handler + ", what = " + what + ", obj = " + obj);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                RegistrantList registrants = new RegistrantList();
                registrants.addUnique(handler, what, obj);
                mRegRequestInfoMap.put(Integer.valueOf(eventType), registrants);
            } else {
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).addUnique(handler, what, obj);
            }
            startDeviceStateChangeListener(eventType, new Registrant(handler, what, obj));
        }
    }

    public static void unregisterWhiteListAppStateChangeEvent(int eventType, Handler handler) {
        ModemSarLog.d(LOG_TAG, "unregisterWhiteListAppStateChangeEvent, eventType = " + eventType + ", handler = " + handler);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) == null) {
                return;
            }
            mRegRequestInfoMap.get(Integer.valueOf(eventType)).remove(handler);
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)).size() == 0) {
                stopDeviceStateChangeListener(eventType, handler);
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
        if (mContext == null) {
            ModemSarLog.e(LOG_TAG, "Constructor, mContext is null");
            return;
        }
        mFgWorkThread = new HandlerThread(LOG_TAG, 10);
        mFgWorkThread.start();
        mFgHandler = new AppWhitelistHandler(mFgWorkThread.getLooper());
        initTimers();
    }

    private static void startDeviceStateChangeListener(int eventType, Registrant r) {
        ModemSarLog.d(LOG_TAG, "startDeviceStateChangeListener, eventType = " + eventType + ", r = " + r);
        synchronized (mListenerStateMap) {
            switch (eventType) {
                case 1:
                    if (mListenerStateMap.get(Integer.valueOf(eventType)) == null || mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 0) {
                        startScanCodeListener();
                        break;
                    } else {
                        notifyDeviceStateChangeEvent(eventType, r, mCurrentScanCodeState);
                        break;
                    }
                default:
                    break;
            }
            throw th;
        }
    }

    private static void stopDeviceStateChangeListener(int eventType, Handler h) {
        ModemSarLog.d(LOG_TAG, "stopDeviceStateChangeListener, eventType = " + eventType + ", h = " + h);
        synchronized (mListenerStateMap) {
            switch (eventType) {
                case 1:
                    if (mListenerStateMap.get(Integer.valueOf(eventType)) != null && mListenerStateMap.get(Integer.valueOf(eventType)).intValue() == 1) {
                        stopScanCodeListener();
                        mCurrentScanCodeState = 0;
                        break;
                    }
                default:
                    break;
            }
            throw th;
        }
    }

    private static void notifyDeviceStateChangeEvent(int eventType, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + eventType + ", intValue = " + intValue);
        synchronized (mRegRequestInfoMap) {
            if (mRegRequestInfoMap.get(Integer.valueOf(eventType)) != null) {
                byte[] bytes = ModemUtils.getBytes(intValue);
                mRegRequestInfoMap.get(Integer.valueOf(eventType)).notifyRegistrants(new AsyncResult((Object) null, bytes, (Throwable) null));
            }
        }
    }

    private static void notifyDeviceStateChangeEvent(int eventType, Registrant r, int intValue) {
        ModemSarLog.d(LOG_TAG, "notifyDeviceStateChangeEvent, eventType = " + eventType + ", r = " + r + ", intValue = " + intValue);
        if (r == null) {
            ModemSarLog.e(LOG_TAG, "r is null, notifyDeviceStateChangeEvent do nothing");
        } else {
            byte[] bytes = ModemUtils.getBytes(intValue);
            r.notifyRegistrant(new AsyncResult((Object) null, bytes, (Throwable) null));
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
        mCountTimerRunnable = new Runnable() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.1
            @Override // java.lang.Runnable
            public void run() {
                if (WhiteListAppStateListener.mSafetyTimer != null) {
                    WhiteListAppStateListener.mCurrentScanCodeState = 0;
                    WhiteListAppStateListener.updateScanCodeMode(WhiteListAppStateListener.mCurrentScanCodeState);
                    WhiteListAppStateListener.mSafetyTimer.start();
                }
            }
        };
        mCountTimer = new ModemSarSynchronizedTimer("AppMonitorCountTimer", APP_WHITELIST_COUNT_TIMEOUT, mCountTimerRunnable);
        mSafetyTimerRunnable = new Runnable() { // from class: com.xiaomi.internal.telephony.WhiteListAppStateListener.2
            @Override // java.lang.Runnable
            public void run() {
                if (WhiteListAppStateListener.mCountTimer != null) {
                    WhiteListAppStateListener.mCountTimer.resetTimer();
                }
            }
        };
        mSafetyTimer = new ModemSarSynchronizedTimer("AppMonitorSafetyTimer", APP_WHITELIST_SAFETY_TIMEOUT, mSafetyTimerRunnable);
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
        for (String appInList : HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list")) {
            ModemSarLog.i(LOG_TAG, "Supported App:" + appInList);
        }
    }

    private static void stopTimer() {
        if (mSafetyTimer != null) {
            mSafetyTimer.stop();
        }
        if (mCountTimer != null) {
            mCountTimer.stop();
        }
    }

    private static void resetTimer() {
        if (mSafetyTimer != null) {
            mSafetyTimer.resetTimer();
        }
        if (mCountTimer != null) {
            mCountTimer.resetTimer();
        }
    }

    private static class AppWhitelistHandler extends Handler {
        AppWhitelistHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ModemSarLog.d(WhiteListAppStateListener.LOG_TAG, "handleMessage=" + msg.what);
            switch (msg.what) {
                case 1:
                    WhiteListAppStateListener.processForegAndScanCodeEvent(((Integer) msg.obj).intValue());
                    break;
            }
        }
    }

    private static void registerActivityChangeListener() {
        ModemSarLog.d(LOG_TAG, "registerActivityChangeListener");
        if (mActivityChangeListener != null) {
            List<String> targetActivities = HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list_activity");
            List<String> targetPackages = HyperOSCustFeatureResolve.getStringArray("sar_scan_code_white_list");
            ProcessManager.unregisterActivityChanageListener(mActivityChangeListener);
            ProcessManager.registerActivityChangeListener(targetPackages, targetActivities, mActivityChangeListener);
        }
    }

    private static void unregisterActivityChangeListener() {
        ModemSarLog.d(LOG_TAG, "unregisterActivityChangeListener");
        if (mActivityChangeListener != null) {
            ProcessManager.unregisterActivityChanageListener(mActivityChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processForegAndScanCodeEvent(int mode) {
        ModemSarLog.d(LOG_TAG, "processForegroundInfoEvent mode = " + mode);
        if (mSafetyTimer.isTimerRunning()) {
            ModemSarLog.d(LOG_TAG, "SafetyTimer is running");
            mCurrentScanCodeState = 0;
            return;
        }
        if (mode == mCurrentScanCodeState) {
            ModemSarLog.d(LOG_TAG, "Already in mode:" + mode);
            return;
        }
        mCurrentScanCodeState = mode;
        if (mCurrentScanCodeState == 0) {
            mCountTimer.stop();
        } else if (mCurrentScanCodeState == 1) {
            mCountTimer.start();
        } else {
            ModemSarLog.e(LOG_TAG, "Illegal Mode!");
            return;
        }
        updateScanCodeMode(mCurrentScanCodeState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateScanCodeMode(int mode) {
        ModemSarLog.d(LOG_TAG, "updateScanCodeMode:" + mode);
        notifyDeviceStateChangeEvent(1, mode);
        if (mode == 0) {
            if (mLastMode == 1) {
                long endTime = System.currentTimeMillis();
                long timeGap = (endTime - mStartTime) / 1000;
                HashMap<String, String> whitelistHashMap = new HashMap<>();
                whitelistHashMap.put("time", String.valueOf(timeGap));
                whitelistHashMap.put("app", mCurrentApp);
                ModemSarStatistics.reportTxWhitelistScancodeData(whitelistHashMap);
                ModemSarLog.i(LOG_TAG, "whitelistHashMap: " + whitelistHashMap);
                mLastMode = 0;
                mStartTime = 0L;
                mCurrentApp = null;
                return;
            }
            return;
        }
        if (mode == 1 && mLastMode == 0) {
            mStartTime = System.currentTimeMillis();
            mLastMode = 1;
        }
    }
}

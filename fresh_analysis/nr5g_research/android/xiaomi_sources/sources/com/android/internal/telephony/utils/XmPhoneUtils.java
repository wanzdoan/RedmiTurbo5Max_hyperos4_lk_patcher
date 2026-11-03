package com.android.internal.telephony.utils;

import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class XmPhoneUtils {
    private static final String TAG = "XmPhoneUtils";
    private final String THREAD_NAME = "XmPhoneServiceWorkThread";
    private HandlerThread mHandlerThread;
    public static final int PHONE_COUNT = TelephonyManager.getDefault().getActiveModemCount();
    public static final boolean IS_DATA_CAPABLE = TelephonyManager.getDefault().isDataCapable();
    private static XmPhoneUtils sInstance = null;

    private XmPhoneUtils() {
        this.mHandlerThread = null;
        this.mHandlerThread = new HandlerThread("XmPhoneServiceWorkThread");
        this.mHandlerThread.start();
        Log.d(TAG, "XmPhoneServiceWorkThread start");
    }

    public static XmPhoneUtils getInstance() {
        synchronized (XmPhoneUtils.class) {
            if (sInstance == null) {
                sInstance = new XmPhoneUtils();
            }
        }
        return sInstance;
    }

    public Looper getLooper() {
        return this.mHandlerThread.getLooper();
    }

    public static <T extends IInterface> boolean addCallbackLocked(List<T> cbList, T cb) {
        if (cb == null || cbList == null) {
            return false;
        }
        IBinder binder = cb.asBinder();
        for (T item : cbList) {
            if (item.asBinder() == binder) {
                return false;
            }
        }
        return cbList.add(cb);
    }

    public static <T extends IInterface> boolean removeCallbackLocked(List<T> cbList, T cb) {
        if (cb == null || cbList == null) {
            return false;
        }
        IBinder binder = cb.asBinder();
        Iterator<T> it = cbList.iterator();
        while (it.hasNext()) {
            T item = it.next();
            if (item.asBinder() == binder) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < PHONE_COUNT;
    }
}

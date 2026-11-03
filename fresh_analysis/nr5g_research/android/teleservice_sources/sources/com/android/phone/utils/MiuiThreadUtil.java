package com.android.phone.utils;

import android.os.HandlerThread;
import android.os.Looper;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiThreadUtil {

    class WorkThreadHolder extends HandlerThread {
        public static WorkThreadHolder sInstance = new WorkThreadHolder();

        private WorkThreadHolder() {
            super("PhoneWorkThread", 10);
            start();
        }
    }

    public static Looper getWorkThreadLooper() {
        return WorkThreadHolder.sInstance.getLooper();
    }

    class SmartNetworkWorkThread extends HandlerThread {
        public static SmartNetworkWorkThread sInstance = new SmartNetworkWorkThread();

        public SmartNetworkWorkThread() {
            super("SmartNetworkWorkThread", 10);
            start();
        }
    }

    public static Looper getSmartNetworkWorkThread() {
        return SmartNetworkWorkThread.sInstance.getLooper();
    }
}

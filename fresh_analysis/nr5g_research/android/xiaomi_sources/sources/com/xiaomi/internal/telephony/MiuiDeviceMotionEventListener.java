package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDeviceMotionEventListener {
    private static final String LOG_TAG = "MiuiDeviceMotionEventListener";
    private static Context mContext;
    private static Handler mHandler;
    private static HandlerThread mHandlerThread;
    private static Object mMiuiInputManager;
    private static Class mMiuiMotionEventListenerClass;
    private static final List<MiuiMotionEventListener> mMiuiMotionEventListenerList = new CopyOnWriteArrayList();
    private static MotionEvent mMotionEvent;
    private static Object mProxyMiuiMotionEventListener;
    private static Method mRegisterMiuiMotionEventListenerMethod;
    private static Method mUnregisterMiuiMotionEventListenerMethod;
    private static volatile MiuiDeviceMotionEventListener sInstance;

    public interface MiuiMotionEventListener {
        void onMotionEvent(MotionEvent motionEvent);
    }

    public static void RegisterMiuiMotionEventListener(Context context, MiuiMotionEventListener miuiMotionEventListener) {
        if (miuiMotionEventListener == null) {
            ModemSarLog.e(LOG_TAG, "register MiuiMotionEventListener has param error");
            return;
        }
        initInstance(context);
        if (mMiuiMotionEventListenerList.isEmpty()) {
            initAndRegisterMiuiMotionEventListener();
        }
        if (!mMiuiMotionEventListenerList.contains(miuiMotionEventListener)) {
            mMiuiMotionEventListenerList.add(miuiMotionEventListener);
        }
        if (mMotionEvent != null) {
            notifyMiuiMotionEventListener(mMotionEvent, miuiMotionEventListener);
        }
    }

    public static void UnregisterMiuiMotionEventListener(MiuiMotionEventListener miuiMotionEventListener) {
        if (miuiMotionEventListener == null) {
            ModemSarLog.e(LOG_TAG, "unregister MiuiMotionEventListener has param error");
            return;
        }
        mMiuiMotionEventListenerList.remove(miuiMotionEventListener);
        if (mMiuiMotionEventListenerList.isEmpty()) {
            unregisterMiuiMotionEventListener();
        }
    }

    private static void initAndRegisterMiuiMotionEventListener() {
        if (mContext == null || mHandler == null) {
            ModemSarLog.e(LOG_TAG, "create MiuiMotionEventListener has param error");
            return;
        }
        try {
            mMiuiInputManager = mContext.getSystemService("MiuiInputManager");
            Class<?> cls = Class.forName("miui.hardware.input.MiuiInputManager");
            for (Class<?> cls2 : cls.getDeclaredClasses()) {
                if ("miui.hardware.input.MiuiInputManager$MiuiMotionEventListener".equals(cls2.getName())) {
                    mMiuiMotionEventListenerClass = cls2;
                    break;
                }
            }
            if (mMiuiMotionEventListenerClass == null) {
                throw new Exception("miui input manager can't find listener class");
            }
            ProxyMiuiMotionEventListener proxyMiuiMotionEventListener = new ProxyMiuiMotionEventListener();
            mProxyMiuiMotionEventListener = Proxy.newProxyInstance(mMiuiMotionEventListenerClass.getClassLoader(), new Class[]{mMiuiMotionEventListenerClass}, proxyMiuiMotionEventListener);
            mRegisterMiuiMotionEventListenerMethod = cls.getDeclaredMethod("registerMiuiMotionEventListener", mMiuiMotionEventListenerClass, Handler.class);
            mUnregisterMiuiMotionEventListenerMethod = cls.getDeclaredMethod("unregisterMiuiMotionEventListener", mMiuiMotionEventListenerClass);
            mRegisterMiuiMotionEventListenerMethod.invoke(mMiuiInputManager, mProxyMiuiMotionEventListener, mHandler);
            ModemSarLog.d(LOG_TAG, "register MiuiMotionEventListener success");
        } catch (Exception e) {
            e.printStackTrace();
            ModemSarLog.e(LOG_TAG, "create MiuiMotionEventListener has reflect error");
        }
    }

    private static void unregisterMiuiMotionEventListener() {
        try {
            try {
                if (mUnregisterMiuiMotionEventListenerMethod != null && mProxyMiuiMotionEventListener != null) {
                    mUnregisterMiuiMotionEventListenerMethod.invoke(mMiuiInputManager, mProxyMiuiMotionEventListener);
                    ModemSarLog.d(LOG_TAG, "unregister MiuiMotionEventListener success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "unregister MiuiMotionEventListener has reflect error");
            }
        } finally {
            destroyMiuiMotionEventListener();
        }
    }

    private static void destroyMiuiMotionEventListener() {
        mMiuiInputManager = null;
        mMiuiMotionEventListenerClass = null;
        mRegisterMiuiMotionEventListenerMethod = null;
        mUnregisterMiuiMotionEventListenerMethod = null;
        mProxyMiuiMotionEventListener = null;
        mMotionEvent = null;
    }

    private static void initInstance(Context context) {
        if (sInstance == null) {
            synchronized (MiuiDeviceMotionEventListener.class) {
                if (sInstance == null) {
                    sInstance = new MiuiDeviceMotionEventListener(context);
                }
            }
        }
    }

    private MiuiDeviceMotionEventListener(Context context) {
        mContext = context;
        mHandlerThread = new HandlerThread("MiuiDeviceMotionEventListenerBgThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    private static class ProxyMiuiMotionEventListener implements InvocationHandler {
        private ProxyMiuiMotionEventListener() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("onMotionEvent".equals(method.getName())) {
                MiuiDeviceMotionEventListener.mMotionEvent = (MotionEvent) args[0];
                MiuiDeviceMotionEventListener.notifyMiuiMotionEventListener(MiuiDeviceMotionEventListener.mMotionEvent);
                return null;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyMiuiMotionEventListener(MotionEvent event) {
        if (event == null || mMiuiMotionEventListenerList.isEmpty()) {
            ModemSarLog.e(LOG_TAG, "notifyMiuiMotionEventListener has param error");
            return;
        }
        for (MiuiMotionEventListener miuiMotionEventListener : mMiuiMotionEventListenerList) {
            miuiMotionEventListener.onMotionEvent(event);
        }
    }

    private static void notifyMiuiMotionEventListener(MotionEvent event, MiuiMotionEventListener miuiMotionEventListener) {
        if (event == null || miuiMotionEventListener == null) {
            ModemSarLog.e(LOG_TAG, "notifyMiuiMotionEventListener has param error");
        } else {
            miuiMotionEventListener.onMotionEvent(event);
        }
    }
}

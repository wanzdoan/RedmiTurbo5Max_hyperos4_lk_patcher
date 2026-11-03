package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
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
    private static final List mMiuiMotionEventListenerList = new CopyOnWriteArrayList();
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
        List list = mMiuiMotionEventListenerList;
        if (list.isEmpty()) {
            initAndRegisterMiuiMotionEventListener();
        }
        if (!list.contains(miuiMotionEventListener)) {
            list.add(miuiMotionEventListener);
        }
        MotionEvent motionEvent = mMotionEvent;
        if (motionEvent != null) {
            notifyMiuiMotionEventListener(motionEvent, miuiMotionEventListener);
        }
    }

    public static void UnregisterMiuiMotionEventListener(MiuiMotionEventListener miuiMotionEventListener) {
        if (miuiMotionEventListener == null) {
            ModemSarLog.e(LOG_TAG, "unregister MiuiMotionEventListener has param error");
            return;
        }
        List list = mMiuiMotionEventListenerList;
        list.remove(miuiMotionEventListener);
        if (list.isEmpty()) {
            unregisterMiuiMotionEventListener();
        }
    }

    private static void initAndRegisterMiuiMotionEventListener() {
        Context context = mContext;
        if (context == null || mHandler == null) {
            ModemSarLog.e(LOG_TAG, "create MiuiMotionEventListener has param error");
            return;
        }
        try {
            mMiuiInputManager = context.getSystemService("MiuiInputManager");
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
            mProxyMiuiMotionEventListener = Proxy.newProxyInstance(mMiuiMotionEventListenerClass.getClassLoader(), new Class[]{mMiuiMotionEventListenerClass}, new ProxyMiuiMotionEventListener());
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
        Object obj;
        try {
            Method method = mUnregisterMiuiMotionEventListenerMethod;
            if (method != null && (obj = mProxyMiuiMotionEventListener) != null) {
                method.invoke(mMiuiInputManager, obj);
                ModemSarLog.d(LOG_TAG, "unregister MiuiMotionEventListener success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ModemSarLog.e(LOG_TAG, "unregister MiuiMotionEventListener has reflect error");
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
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiDeviceMotionEventListener(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private MiuiDeviceMotionEventListener(Context context) {
        mContext = context;
        HandlerThread handlerThread = new HandlerThread("MiuiDeviceMotionEventListenerBgThread");
        mHandlerThread = handlerThread;
        handlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    class ProxyMiuiMotionEventListener implements InvocationHandler {
        private ProxyMiuiMotionEventListener() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            if (!"onMotionEvent".equals(method.getName())) {
                return null;
            }
            MiuiDeviceMotionEventListener.mMotionEvent = (MotionEvent) objArr[0];
            MiuiDeviceMotionEventListener.notifyMiuiMotionEventListener(MiuiDeviceMotionEventListener.mMotionEvent);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyMiuiMotionEventListener(MotionEvent motionEvent) {
        if (motionEvent != null) {
            List list = mMiuiMotionEventListenerList;
            if (!list.isEmpty()) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ((MiuiMotionEventListener) it.next()).onMotionEvent(motionEvent);
                }
                return;
            }
        }
        ModemSarLog.e(LOG_TAG, "notifyMiuiMotionEventListener has param error");
    }

    private static void notifyMiuiMotionEventListener(MotionEvent motionEvent, MiuiMotionEventListener miuiMotionEventListener) {
        if (motionEvent == null || miuiMotionEventListener == null) {
            ModemSarLog.e(LOG_TAG, "notifyMiuiMotionEventListener has param error");
        } else {
            miuiMotionEventListener.onMotionEvent(motionEvent);
        }
    }
}

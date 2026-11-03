package com.hyper.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.common.HyperModuleList;
import com.android.internal.telephony.common.IHyperCommonModule;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class HyperModuleManager {
    private static final String TAG = "HyperModuleManager";
    private static HyperModuleManager sInstance = null;
    private static final HashMap<String, Boolean> sModuleSwitchMap = new HashMap<>();
    private static final HashMap<String, Boolean> sModuleTraceMap = new HashMap<>();
    private static final boolean[] mModuleDisable = new boolean[HyperModuleList.HyperIndex.End.ordinal()];
    private static final boolean[] mModuleTraing = new boolean[HyperModuleList.HyperIndex.End.ordinal()];

    public HyperModuleManager getInstance() {
        HyperModuleManager hyperModuleManager;
        synchronized (HyperModuleManager.class) {
            if (sInstance == null) {
                sInstance = new HyperModuleManager();
            }
            hyperModuleManager = sInstance;
        }
        return hyperModuleManager;
    }

    public static void init(Context context) {
    }

    public static <T extends IHyperCommonModule> boolean isSupport(T def) {
        boolean disable = false;
        try {
            int index = getIndex(def);
            boolean[] zArr = mModuleDisable;
            disable = zArr[index];
            if (disable) {
                synchronized (def.getDefault()) {
                    try {
                        disable = zArr[index];
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        } catch (Exception e) {
        }
        return !disable;
    }

    public static <T extends IHyperCommonModule> boolean isTracing(T def) {
        boolean enable = false;
        try {
            int index = getIndex(def);
            boolean[] zArr = mModuleTraing;
            enable = zArr[index];
            if (enable) {
                synchronized (def.getDefault()) {
                    enable = zArr[index];
                }
            }
        } catch (Exception e) {
        }
        return enable;
    }

    public static boolean isSupport(String name) {
        synchronized (sModuleSwitchMap) {
        }
        return true;
    }

    public static boolean isTracing(String name) {
        synchronized (sModuleTraceMap) {
        }
        return false;
    }

    public static <T extends IHyperCommonModule> T getTraceMonitor(T real) throws IllegalArgumentException {
        if (real == null) {
            throw new IllegalArgumentException("params must not be null");
        }
        HyperModuleList.HyperIndex index = real.index();
        if (isTracing(real)) {
            DynamicProxy handler = new DynamicProxy(real, index.name());
            Class<T> cls = (Class) real.getDefault().getClass().getGenericInterfaces()[0];
            return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{cls}, handler);
        }
        return real;
    }

    private static class DynamicProxy implements InvocationHandler {
        private String feature;
        private Object realObj;

        public DynamicProxy(Object object, String f) {
            this.realObj = object;
            this.feature = f;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            Object result = method.invoke(this.realObj, objects);
            return result;
        }
    }

    private static <T extends IHyperCommonModule> int getIndex(T service) throws IllegalArgumentException {
        if (service == null) {
            throw new IllegalArgumentException("dummy must not be null");
        }
        int index = service.index().ordinal();
        boolean[] zArr = mModuleDisable;
        if (index >= zArr.length) {
            throw new IllegalArgumentException("index = " + index + " size = " + zArr.length);
        }
        return index;
    }
}

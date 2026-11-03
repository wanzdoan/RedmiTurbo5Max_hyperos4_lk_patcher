package com.android.internal.telephony;

import android.util.Log;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonFactory;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.telephony.Rlog;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes.dex */
public class HyperTelephonyFactory implements IHyperCommonFactory {
    protected static final String TAG = "HyperTelephonyFactory";
    private static final Object mLock = new Object();
    private static HyperTelephonyFactory sInstance = null;
    private static boolean sIsValidModule = false;

    public static HyperTelephonyFactory getInstance() {
        HyperTelephonyFactory hyperTelephonyFactory;
        synchronized (mLock) {
            if (sInstance == null) {
                try {
                    sInstance = (HyperTelephonyFactory) newInstance("/system_ext/framework/hyper-telephony-common.jar", "com.hyper.internal.telephony.HyperTelephonyFactoryImpl");
                    sIsValidModule = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Rlog.e(TAG, "Reflect exception getInstance: " + e.toString());
                    sInstance = new HyperTelephonyFactory();
                    sIsValidModule = false;
                }
                hyperTelephonyFactory = sInstance;
            } else {
                hyperTelephonyFactory = sInstance;
            }
            throw th;
        }
        return hyperTelephonyFactory;
    }

    public static Object newInstance(String str, String str2) throws Exception {
        PathClassLoader pathClassLoader = new PathClassLoader(str, HyperTelephonyFactory.class.getClassLoader());
        Rlog.d(TAG, "classLoader = " + pathClassLoader);
        Class<?> cls = Class.forName(str2, true, pathClassLoader);
        Rlog.d(TAG, "clazz = " + cls);
        Class[] clsArr = new Class[0];
        Constructor<?> constructor = cls.getConstructor(null);
        Rlog.d(TAG, "constructor = " + constructor);
        return constructor.newInstance(null);
    }

    public static <T extends IHyperCommonModule> T getModuleFromCache(int i, T t) {
        T t2;
        synchronized (mLock) {
            try {
                if (sInstance == null) {
                    getInstance().getModuleInCache(i, t);
                }
                t2 = (T) sInstance.getModuleInCache(i, t);
            } catch (Throwable th) {
                throw th;
            }
        }
        return t2;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonFactory
    public boolean isValid(int i) {
        boolean z = i < HyperModuleList$HyperIndex.End.ordinal() && i > HyperModuleList$HyperIndex.Start.ordinal();
        Log.i(TAG, "isValid = " + z + "index = " + i);
        return z;
    }
}

package com.qti.dpmapi;

import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.xiaomi.internal.telephony.DynamicSarService;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class DpmApi {
    private static String TAG = "DpmApi";
    private Context mContext;
    private Class mDpmApiClass = null;
    private Class dpmStubClass = null;
    private Object mDpmService = null;
    private Method mUpdateFdParams = null;
    private Method mUpdateFdParamsXSlot = null;
    private Method mGetTCMFeatureEnabledMethod = null;
    private Method mSetTCMFeatureMethod = null;

    enum FeatureType {
        TCM,
        FDMGR
    }

    public DpmApi(Context context) throws Exception {
        this.mContext = null;
        if (context != null) {
            this.mContext = context;
            loadDpmService();
            loadMethods();
            return;
        }
        throw new DpmApiInvalidArgumentException("Context cannot be null");
    }

    private void loadMethods() throws Exception {
        try {
            if (this.mDpmApiClass != null) {
                Log.e(TAG, "loadMethods " + this.mDpmApiClass);
                Class cls = this.mDpmApiClass;
                Class<?> cls2 = Integer.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("setTCMFeature", cls2);
                this.mSetTCMFeatureMethod = declaredMethod;
                if (declaredMethod == null) {
                    Log.e(TAG, "DPM preference interface is not accessible.");
                    return;
                }
                Class[] clsArr = new Class[0];
                Method declaredMethod2 = this.mDpmApiClass.getDeclaredMethod("getTCMFeatureEnabled", null);
                this.mGetTCMFeatureEnabledMethod = declaredMethod2;
                if (declaredMethod2 == null) {
                    Log.e(TAG, "DPM preference interface is not accessible.");
                    return;
                }
                Log.e(TAG, "DPM UpdateFdParams Entering");
                this.mUpdateFdParams = this.mDpmApiClass.getDeclaredMethod("updateFdConfigParams", cls2, cls2, cls2, cls2);
                Log.e(TAG, "DPM UpdateFdParams Ent");
                if (this.mUpdateFdParams == null) {
                    Log.e(TAG, "DPM UpdateFdParams interface is not accessible.");
                    return;
                }
                Log.e(TAG, "DPM UpdateFdParamsXSlot Entering");
                this.mUpdateFdParamsXSlot = this.mDpmApiClass.getDeclaredMethod("updateFdConfigParamsXSlot", cls2, cls2, cls2, cls2, cls2);
                Log.e(TAG, "DPM UpdateFdParamsXSlot Ent");
                if (this.mUpdateFdParamsXSlot == null) {
                    Log.e(TAG, "DPM UpdateFdParamsXSlot interface is not accessible.");
                    return;
                }
                return;
            }
            Log.e(TAG, "DPM preference interface is not accessible.");
        } catch (Exception e) {
            Log.e(TAG, "Exception happened: " + e.toString());
            throw e;
        }
    }

    private void loadDpmService() throws Exception {
        IBinder service = ServiceManager.getService("dpmservice");
        int i = SystemProperties.getInt("persist.vendor.dpm.feature", 0);
        int i2 = SystemProperties.getInt("persist.vendor.dpm.vndr.feature", 0);
        if (service != null) {
            try {
                if (i2 != 0) {
                    PathClassLoader pathClassLoader = new PathClassLoader("/system/system_ext/framework/com.qti.dpmframework.jar", ClassLoader.getSystemClassLoader());
                    this.mDpmApiClass = pathClassLoader.loadClass("com.qti.dpm.IDpmServiceApi");
                    Log.e(TAG, "Load class is successful " + this.mDpmApiClass);
                    if (this.mDpmApiClass == null) {
                        Log.e(TAG, "com.qti.dpm.IDpmServiceApi class is not found in dpm library.");
                        return;
                    }
                    this.dpmStubClass = pathClassLoader.loadClass("com.qti.dpm.IDpmServiceApi$Stub");
                    Log.e(TAG, "Load stub class is successful");
                    if (this.dpmStubClass == null) {
                        Log.e(TAG, "IDpmServiceApi$Stub class is not found in dpm library.");
                        return;
                    }
                } else if (i != 0) {
                    PathClassLoader pathClassLoader2 = new PathClassLoader("/system/system_ext/framework/com.qti.dpmframework.jar", ClassLoader.getSystemClassLoader());
                    this.mDpmApiClass = pathClassLoader2.loadClass("com.qti.dpm.IDpmService");
                    Log.e(TAG, "Load legacy class is successful " + this.mDpmApiClass);
                    if (this.mDpmApiClass == null) {
                        Log.e(TAG, "com.qti.dpm.IDpmService / IDpmServiceApi class is not found in dpm library.");
                        return;
                    }
                    Class clsLoadClass = pathClassLoader2.loadClass("com.qti.dpm.IDpmService$Stub");
                    this.dpmStubClass = clsLoadClass;
                    if (clsLoadClass == null) {
                        Log.e(TAG, "IDpmService$Stub / IDpmServiceApi$Stub class is not found in dpm library.");
                        return;
                    }
                } else {
                    Log.e(TAG, "dpmd is not enabled , check persist properties");
                    return;
                }
                Method declaredMethod = this.dpmStubClass.getDeclaredMethod("asInterface", IBinder.class);
                if (declaredMethod == null) {
                    Log.e(TAG, "Method asInterface is not fount in IDpmApi$Stub class.");
                    return;
                }
                Object objInvoke = declaredMethod.invoke(null, service);
                this.mDpmService = objInvoke;
                if (objInvoke == null) {
                    Log.e(TAG, "Failed to get DpmService as interface.");
                    return;
                }
                return;
            } catch (Exception e) {
                Log.e(TAG, "Exception happened: " + e.toString());
                throw e;
            }
        }
        Log.d(TAG, "dpm service is not found.");
    }

    private void checkPermissions() throws DpmApiSecurityException {
        int i = this.mContext.getApplicationInfo().flags;
        if ((i & 1) == 0 && (i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 0) {
            throw new DpmApiSecurityException("Not a system app!!");
        }
    }

    private void checkFeatureEnabled(FeatureType featureType) throws DpmApiFeatureUnsupportedException {
        int i = SystemProperties.getInt("persist.vendor.dpm.vndr.feature", 0);
        Log.d(TAG, "checkFeatureEnabled " + featureType + "value dpmvndr property " + i);
        if (i == 0) {
            i = SystemProperties.getInt("persist.vendor.dpm.feature", 0);
            Log.d(TAG, "checkFeatureEnabled legacy" + featureType + "value dpm property " + i);
        }
        int i2 = AnonymousClass1.$SwitchMap$com$qti$dpmapi$DpmApi$FeatureType[featureType.ordinal()];
        if (i2 != 1) {
            if (i2 == 2 && (i & 1) == 1) {
                return;
            }
        } else if ((i & 8) == 8) {
            return;
        }
        throw new DpmApiFeatureUnsupportedException();
    }

    /* JADX INFO: renamed from: com.qti.dpmapi.DpmApi$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$qti$dpmapi$DpmApi$FeatureType;

        static {
            int[] iArr = new int[FeatureType.values().length];
            $SwitchMap$com$qti$dpmapi$DpmApi$FeatureType = iArr;
            try {
                iArr[FeatureType.TCM.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$qti$dpmapi$DpmApi$FeatureType[FeatureType.FDMGR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public void updateFdConfigParams(int i, int i2, int i3, int i4) throws DpmApiException {
        Method method;
        checkPermissions();
        checkFeatureEnabled(FeatureType.FDMGR);
        try {
            Object obj = this.mDpmService;
            if (obj != null && (method = this.mUpdateFdParams) != null) {
                method.invoke(obj, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
            } else {
                Log.d(TAG, "dpm updateFdParams interface is not available.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception happened: " + e.toString());
            throw new DpmApiException();
        }
    }
}

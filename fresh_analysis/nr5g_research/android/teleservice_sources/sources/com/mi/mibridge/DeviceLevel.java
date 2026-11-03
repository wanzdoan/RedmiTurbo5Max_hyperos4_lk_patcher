package com.mi.mibridge;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeviceLevel {
    public static int CPU;
    public static int GPU;
    public static int HIGH;
    public static boolean IS_MIUI_GO_VERSION;
    public static boolean IS_MIUI_LITE_VERSION;
    public static boolean IS_MIUI_MIDDLE_VERSION;
    public static int LOW;
    public static int MIDDLE;
    public static int RAM;
    public static int TOTAL_RAM;
    public static int UNKNOWN;
    private static Application application;
    private static Context applicationContext;
    private static Constructor mConstructor;
    private static Method mGetDeviceLevel;
    private static Method mGetDeviceLevelForWhole;
    private static Method mGetMiuiLiteVersion;
    private static Method mGetMiuiMiddleVersion;
    private static Method mInitDeviceLevel;
    private static Method mIsSupportPrune;
    private static Object mPerf;
    private static Class perfClass;
    private static PathClassLoader perfClassLoader;

    static {
        try {
            PathClassLoader pathClassLoader = new PathClassLoader("/system/framework/MiuiBooster.jar", ClassLoader.getSystemClassLoader());
            perfClassLoader = pathClassLoader;
            Class clsLoadClass = pathClassLoader.loadClass("com.miui.performance.DeviceLevelUtils");
            perfClass = clsLoadClass;
            mConstructor = clsLoadClass.getConstructor(Context.class);
            Class[] clsArr = new Class[0];
            mInitDeviceLevel = perfClass.getDeclaredMethod("initDeviceLevel", null);
            Class<?> cls = Integer.TYPE;
            mGetDeviceLevel = perfClass.getDeclaredMethod("getDeviceLevel", cls, cls);
            mGetDeviceLevelForWhole = perfClass.getDeclaredMethod("getDeviceLevel", cls);
            Class[] clsArr2 = new Class[0];
            mIsSupportPrune = perfClass.getDeclaredMethod("isSupportPrune", null);
            RAM = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_RAM")).intValue();
            CPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_CPU")).intValue();
            GPU = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_GPU")).intValue();
            LOW = ((Integer) getStaticObjectField(perfClass, "LOW_DEVICE")).intValue();
            MIDDLE = ((Integer) getStaticObjectField(perfClass, "MIDDLE_DEVICE")).intValue();
            HIGH = ((Integer) getStaticObjectField(perfClass, "HIGH_DEVICE")).intValue();
            UNKNOWN = ((Integer) getStaticObjectField(perfClass, "DEVICE_LEVEL_UNKNOWN")).intValue();
            IS_MIUI_LITE_VERSION = ((Boolean) getStaticObjectField(perfClass, "IS_MIUI_LITE_VERSION")).booleanValue();
            IS_MIUI_GO_VERSION = ((Boolean) getStaticObjectField(perfClass, "IS_MIUI_GO_VERSION")).booleanValue();
            TOTAL_RAM = ((Integer) getStaticObjectField(perfClass, "TOTAL_RAM")).intValue();
            Class[] clsArr3 = new Class[0];
            mGetMiuiLiteVersion = perfClass.getDeclaredMethod("getMiuiLiteVersion", null);
        } catch (Exception e) {
            Log.e("DeviceLevel", "MiDeviceLevelBridge(): Load Class Exception:" + e);
        }
        try {
            IS_MIUI_MIDDLE_VERSION = ((Boolean) getStaticObjectField(perfClass, "IS_MIUI_MIDDLE_VERSION")).booleanValue();
            Class[] clsArr4 = new Class[0];
            mGetMiuiMiddleVersion = perfClass.getDeclaredMethod("getMiuiMiddleVersion", null);
        } catch (Exception e2) {
            Log.e("DeviceLevel", "DeviceLevelUtils(): newInstance Exception:" + e2);
            e2.printStackTrace();
        }
        if (applicationContext == null) {
            try {
                Class[] clsArr5 = new Class[0];
                Application application2 = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", null).invoke(null, null);
                application = application2;
                if (application2 != null) {
                    applicationContext = application2.getApplicationContext();
                }
            } catch (Exception e3) {
                Log.e("DeviceLevel", "android.app.ActivityThread Exception:" + e3);
            }
        }
        if (applicationContext == null) {
            try {
                Class[] clsArr6 = new Class[0];
                Application application3 = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", null).invoke(null, null);
                application = application3;
                if (application3 != null) {
                    applicationContext = application3.getApplicationContext();
                }
            } catch (Exception e4) {
                Log.e("DeviceLevel", "android.app.AppGlobals Exception:" + e4);
            }
        }
        try {
            Constructor constructor = mConstructor;
            if (constructor != null) {
                mPerf = constructor.newInstance(applicationContext);
            }
        } catch (Exception e5) {
            Log.e("DeviceLevel", "DeviceLevelUtils(): newInstance Exception:" + e5);
            e5.printStackTrace();
        }
    }

    private static Object getStaticObjectField(Class cls, String str) throws NoSuchFieldException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(null);
    }
}

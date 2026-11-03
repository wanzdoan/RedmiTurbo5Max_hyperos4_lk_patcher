package com.mediatek.internal.telephony.devreg;

import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemProperties;
import android.telephony.TelephonyBaseUtilsStub;
import android.text.TextUtils;
import android.util.Log;
import com.mediatek.internal.telephony.MtkUiccSmsController;
import com.mediatek.internal.telephony.OpTelephonyCustomizationFactoryBase;
import com.mediatek.internal.telephony.OpTelephonyCustomizationUtils;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class DeviceRegisterController {
    private static final String TAG = "DeviceRegisterController";
    private static IDeviceRegisterExt sDeviceRegisterExt = null;
    private MtkUiccSmsController mSmsController;

    public DeviceRegisterController(Context context, MtkUiccSmsController controller) {
        OpTelephonyCustomizationFactoryBase factoryBase;
        this.mSmsController = null;
        this.mSmsController = controller;
        try {
            if (TelephonyBaseUtilsStub.isMiuiRom() && "cn".equals(SystemProperties.get("ro.miui.cust_variant", ""))) {
                factoryBase = loadFactory(context);
            } else {
                factoryBase = OpTelephonyCustomizationUtils.getOpFactory(context);
            }
            sDeviceRegisterExt = factoryBase.makeDeviceRegisterExt(context, this);
        } catch (Exception e) {
            e.printStackTrace();
            sDeviceRegisterExt = new DefaultDeviceRegisterExt(context, this);
        }
    }

    public void sendDataSms(int subId, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        this.mSmsController.sendData(subId, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
    }

    private static IDeviceRegisterExt getDeviceRegisterExt() {
        return sDeviceRegisterExt;
    }

    public void handleAutoRegMessage(int subId, String format, byte[] pdu) {
        if (sDeviceRegisterExt != null) {
            sDeviceRegisterExt.handleAutoRegMessage(subId, format, pdu);
        }
    }

    private OpTelephonyCustomizationFactoryBase loadFactory(Context context) throws ClassNotFoundException {
        String path = searchTargetPath("OP09CTelephony.jar");
        if (TextUtils.isEmpty(path)) {
            throw new ClassNotFoundException("The path for OP09CTelephony.jar is empty");
        }
        OpTelephonyCustomizationFactoryBase factory = (OpTelephonyCustomizationFactoryBase) loadFactoryInternal(context, path, "com.mediatek.op09c.telephony.Op09CTelephonyCustomizationFactory");
        return factory;
    }

    private Object loadFactoryInternal(Context context, String target, String factoryClassName) {
        Log.d(TAG, "load factory " + factoryClassName + " from " + target);
        try {
            ClassLoader classLoader = new PathClassLoader(target, context.getClassLoader());
            Class<?> clazz = classLoader.loadClass(factoryClassName);
            Log.d(TAG, "Load successfully with classLoader:" + classLoader);
            return clazz.newInstance();
        } catch (Exception ex) {
            Log.e(TAG, "Exception when initial instance", ex);
            return null;
        }
    }

    private static String searchTargetPath(String target) {
        String[] searchFolders = {"/system/operator/libs/", "/custom/operator/libs/"};
        for (String folder : searchFolders) {
            File file = new File(folder + target);
            if (file.exists()) {
                return folder + target;
            }
        }
        Log.i(TAG, "Can not find target " + target + " in " + Arrays.toString(searchFolders));
        return null;
    }
}

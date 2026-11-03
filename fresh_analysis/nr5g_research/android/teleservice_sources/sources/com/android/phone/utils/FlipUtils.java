package com.android.phone.utils;

import android.content.res.Resources;
import android.hardware.devicestate.DeviceStateManagerGlobal;
import android.util.Log;
import miui.util.MiuiMultiDisplayTypeInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class FlipUtils {
    public static boolean isFlip() {
        return MiuiMultiDisplayTypeInfo.isFlipDevice();
    }

    public static boolean isFold() {
        return MiuiMultiDisplayTypeInfo.isFoldDevice() && !isFlip();
    }

    public static boolean isSecondaryScreen() {
        if (isFlip()) {
            return isFoldedState(DeviceStateManagerGlobal.getInstance().getCurrentState());
        }
        return false;
    }

    public static boolean isFoldedState(int i) {
        Resources system = Resources.getSystem();
        int[] intArray = system.getIntArray(system.getIdentifier("config_foldedDeviceStates", "array", "android"));
        Log.i("FlipUtils", "sFoldedDeviceStates = " + intArray);
        if (intArray == null) {
            return false;
        }
        for (int i2 : intArray) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }
}

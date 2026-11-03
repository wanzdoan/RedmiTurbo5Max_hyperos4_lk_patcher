package com.miui.support.drawable;

import miuix.device.DeviceUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class DrawableUtils {
    private static Boolean mIsCommonLiteStrategy;

    public static boolean isCommonLiteStrategy() {
        if (mIsCommonLiteStrategy == null) {
            mIsCommonLiteStrategy = Boolean.valueOf(DeviceUtils.isMiuiLiteV2() || DeviceUtils.isLiteV1StockPlus() || DeviceUtils.isMiuiMiddle());
        }
        return mIsCommonLiteStrategy.booleanValue();
    }
}

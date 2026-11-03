package com.android.phone;

import com.android.phone.network.ModemFiveGAScheduleInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemDataCollectorBase {
    protected static volatile ModemDataCollectorBase mInstance;
    public boolean mIsCollecting = false;

    public abstract ModemFiveGAScheduleInfo[] get5GAScheduleInfo();

    public abstract boolean getCallEndInfo();

    public abstract boolean isInErrorState();

    public abstract boolean isSupportWechatUlEnhance();

    public abstract void startModemCallback();

    public abstract void startTest();

    public abstract void startWechatUlEnhanceCallback();

    public abstract void stopModemCallback();

    public abstract void stopTest();

    public abstract void stopWechatUlEnhanceCallback();

    public abstract boolean updateModemAiData();

    public static ModemDataCollectorBase getInstance() {
        return mInstance;
    }

    protected ModemDataCollectorBase() {
    }
}

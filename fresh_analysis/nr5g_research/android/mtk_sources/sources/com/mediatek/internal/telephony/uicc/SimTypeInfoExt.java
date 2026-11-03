package com.mediatek.internal.telephony.uicc;

import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public class SimTypeInfoExt {
    private int mSupport = -1;
    private int mCurrentType = -1;
    private int mSource = -1;

    public static SimTypeInfoExt createFromParcel(Parcel source) {
        SimTypeInfoExt instance = new SimTypeInfoExt();
        instance.mSupport = source.readInt();
        instance.mCurrentType = source.readInt();
        instance.mSource = source.readInt();
        return instance;
    }

    public String toString() {
        return super.toString() + ";support: " + this.mSupport + ", currentType: " + this.mCurrentType + ", source:" + this.mSource;
    }

    public void setSupport(int support) {
        this.mSupport = support;
    }

    public void setCurrentType(int currentType) {
        this.mCurrentType = currentType;
    }

    public void setSource(int source) {
        this.mSource = source;
    }

    public int getSupport() {
        return this.mSupport;
    }

    public int getCurrentType() {
        return this.mCurrentType;
    }

    public int getSource() {
        return this.mSource;
    }
}

package com.android.phone.settings.sim.bean;

/* JADX INFO: loaded from: classes.dex */
public class PackageBean {
    public String mBillName;
    public String mBillUnit;
    public String mBillValue;
    public String mClickAction;
    public int mPackageType;
    public int mSlotId;
    public String mTrafficName;
    public String mTrafficUnit;
    public String mTrafficValue;

    public boolean notSetPackage() {
        return this.mPackageType == -1;
    }

    public boolean noSim() {
        return this.mPackageType == -2;
    }
}

package com.android.phone.settings;

import com.android.phone.PhoneGlobals;
import com.android.phone.RilHook;

/* JADX INFO: loaded from: classes.dex */
public class VendorUtils extends VendorUtilsBase {
    private RilHook mRilHook = RilHook.init(PhoneGlobals.getInstance());

    public static VendorUtilsBase make() {
        if (!(VendorUtilsBase.mInstance instanceof VendorUtils)) {
            VendorUtilsBase.mInstance = new VendorUtils();
        }
        return VendorUtilsBase.mInstance;
    }

    private VendorUtils() {
    }

    @Override // com.android.phone.settings.VendorUtilsBase
    public boolean setXiaomiBandMode(long[] jArr, int i) {
        return this.mRilHook.qcRilSetXiaomiBandMode(jArr, i);
    }

    @Override // com.android.phone.settings.VendorUtilsBase
    public boolean setLteBandPref(int[] iArr, int i) {
        return this.mRilHook.qcRilSetLteBandPref(iArr, i);
    }
}

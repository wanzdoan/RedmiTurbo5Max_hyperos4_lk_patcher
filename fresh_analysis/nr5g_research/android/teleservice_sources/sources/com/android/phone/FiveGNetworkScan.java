package com.android.phone;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class FiveGNetworkScan extends FiveGNetworkScanBase {
    private RilHook mRilHook = RilHook.init(PhoneGlobals.getInstance());

    public static FiveGNetworkScanBase init() {
        if (!(FiveGNetworkScanBase.mInstance instanceof FiveGNetworkScan)) {
            FiveGNetworkScanBase.mInstance = new FiveGNetworkScan();
        }
        return FiveGNetworkScanBase.mInstance;
    }

    private FiveGNetworkScan() {
    }

    @Override // com.android.phone.FiveGNetworkScanBase
    public boolean startNetworkScan(int i) {
        if (!MiuiPhoneUtils.isQcSaNetwork()) {
            return false;
        }
        Log.d("QcFiveGNetworkScan", "startNetworkScan");
        RilHook rilHook = this.mRilHook;
        if (rilHook == null) {
            return false;
        }
        return rilHook.qcRilPerformIncrManualScan(i);
    }

    @Override // com.android.phone.FiveGNetworkScanBase
    public boolean stopNetworkScan(int i) {
        if (!MiuiPhoneUtils.isQcSaNetwork()) {
            return false;
        }
        Log.d("QcFiveGNetworkScan", "stopNetworkScan");
        RilHook rilHook = this.mRilHook;
        if (rilHook == null) {
            return false;
        }
        return rilHook.qcRilAbortNetworkScan(i);
    }
}

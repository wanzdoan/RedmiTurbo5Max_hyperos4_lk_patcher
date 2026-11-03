package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public class CloudPowerSaveData {
    private boolean mIsBalanceModeSupport;
    private String mOptStatus;

    public CloudPowerSaveData(FiveGCloudController.FeatureCloudInfo featureCloudInfo) {
        this.mOptStatus = featureCloudInfo.getParams2();
        this.mIsBalanceModeSupport = featureCloudInfo.getStatus() == 1;
    }

    public boolean isBalanceModeSupport() {
        return this.mIsBalanceModeSupport;
    }

    public String getOptStatus() {
        return this.mOptStatus;
    }
}

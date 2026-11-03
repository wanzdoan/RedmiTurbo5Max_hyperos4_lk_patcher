package com.android.phone;

import android.provider.MiuiSettings;

/* JADX INFO: loaded from: classes.dex */
public class CloudSpecialNetworkData {
    private String mCustomizedRegion;
    private int mElevatorStatus;
    private int mFeatureStatus;
    private int mGeographyFenceStatus;
    private String mModel;
    private String mSimOperator;
    private String mVersionType;

    public CloudSpecialNetworkData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        this.mFeatureStatus = -1;
        this.mElevatorStatus = -1;
        this.mGeographyFenceStatus = -1;
        this.mModel = cloudData.getString("Model", "NA");
        this.mVersionType = cloudData.getString("DeviceVersionType", "NA");
        this.mCustomizedRegion = cloudData.getString("CustomizedRegion", "NA");
        this.mSimOperator = cloudData.getString("SimOperator", "NA");
        if (isBasicInfoMatched()) {
            this.mFeatureStatus = cloudData.getInt("FeatureStatus", -1);
            this.mElevatorStatus = cloudData.getInt("ElevatorStatus", -1);
            this.mGeographyFenceStatus = cloudData.getInt("GeographyFenceStatus", -1);
        }
    }

    private boolean isBasicInfoMatched() {
        return FiveGCloudUtils.modelMatched(this.mModel) && FiveGCloudUtils.versionTypeMatched(this.mVersionType) && FiveGCloudUtils.customizedRegionMatched(this.mCustomizedRegion) && FiveGCloudUtils.simOperatorMatched(this.mSimOperator);
    }

    public int getSpecialNetworkStatus() {
        return this.mFeatureStatus;
    }

    public int getElevatorStatus() {
        return this.mElevatorStatus;
    }

    public int getGeographyFenceStatus() {
        return this.mGeographyFenceStatus;
    }
}

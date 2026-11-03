package com.android.phone;

import android.provider.MiuiSettings;

/* JADX INFO: loaded from: classes.dex */
public class CloudModemComponentData {
    private String mCustomizedRegion;
    private String mModel;
    private int mQet7100DayCount;
    private int mQet7100Status;
    private int mQet7100TotalCount;
    private String mSimOperator;
    private String mVersionType;

    public CloudModemComponentData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        this.mQet7100Status = -1;
        this.mQet7100DayCount = -1;
        this.mQet7100TotalCount = -1;
        this.mModel = cloudData.getString("Model", "NA");
        this.mVersionType = cloudData.getString("DeviceVersionType", "NA");
        this.mCustomizedRegion = cloudData.getString("CustomizedRegion", "NA");
        this.mSimOperator = cloudData.getString("SimOperator", "NA");
        if (isBasicInfoMatched()) {
            this.mQet7100Status = cloudData.getInt("Status1", -1);
            this.mQet7100DayCount = cloudData.getInt("DayCount1", -1);
            this.mQet7100TotalCount = cloudData.getInt("TotalCount1", -1);
        }
    }

    private boolean isBasicInfoMatched() {
        return FiveGCloudUtils.modelMatched(this.mModel) && FiveGCloudUtils.versionTypeMatched(this.mVersionType) && FiveGCloudUtils.customizedRegionMatched(this.mCustomizedRegion) && FiveGCloudUtils.simOperatorMatched(this.mSimOperator);
    }

    public int getQet7100Status() {
        return this.mQet7100Status;
    }

    public int getQet7100DayCount() {
        return this.mQet7100DayCount;
    }

    public int getQet7100TotalCount() {
        return this.mQet7100TotalCount;
    }
}

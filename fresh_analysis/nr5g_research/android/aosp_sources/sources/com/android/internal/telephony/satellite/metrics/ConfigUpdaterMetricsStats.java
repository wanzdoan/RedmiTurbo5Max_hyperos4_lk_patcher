package com.android.internal.telephony.satellite.metrics;

import android.util.Log;
import com.android.internal.telephony.metrics.SatelliteStats;

/* JADX INFO: loaded from: classes.dex */
public class ConfigUpdaterMetricsStats {
    private static final String TAG = "ConfigUpdaterMetricsStats";
    private static ConfigUpdaterMetricsStats sInstance;
    private int mCarrierConfigResult;
    private int mConfigVersion;
    private int mOemConfigResult;

    private ConfigUpdaterMetricsStats() {
        initializeConfigUpdaterParams();
    }

    public static ConfigUpdaterMetricsStats getOrCreateInstance() {
        if (sInstance == null) {
            logd("Create new ConfigUpdaterMetricsStats.");
            sInstance = new ConfigUpdaterMetricsStats();
        }
        return sInstance;
    }

    public ConfigUpdaterMetricsStats setConfigVersion(int i) {
        this.mConfigVersion = i;
        return this;
    }

    public ConfigUpdaterMetricsStats setOemConfigResult(int i) {
        this.mOemConfigResult = i;
        return this;
    }

    public ConfigUpdaterMetricsStats setCarrierConfigResult(int i) {
        this.mCarrierConfigResult = i;
        return this;
    }

    public void reportOemConfigError(int i) {
        this.mOemConfigResult = i;
        reportConfigUpdaterMetrics();
    }

    public void reportCarrierConfigError(int i) {
        this.mCarrierConfigResult = i;
        reportConfigUpdaterMetrics();
    }

    public void reportOemAndCarrierConfigError(int i) {
        this.mOemConfigResult = i;
        this.mCarrierConfigResult = i;
        reportConfigUpdaterMetrics();
    }

    public void reportConfigUpdateSuccess() {
        this.mOemConfigResult = 1;
        this.mCarrierConfigResult = 1;
        reportConfigUpdaterMetrics();
    }

    private void reportConfigUpdaterMetrics() {
        SatelliteStats.SatelliteConfigUpdaterParams satelliteConfigUpdaterParamsBuild = new SatelliteStats.SatelliteConfigUpdaterParams.Builder().setConfigVersion(this.mConfigVersion).setOemConfigResult(this.mOemConfigResult).setCarrierConfigResult(this.mCarrierConfigResult).setCount(1).build();
        SatelliteStats.getInstance().onSatelliteConfigUpdaterMetrics(satelliteConfigUpdaterParamsBuild);
        logd("reportConfigUpdaterMetrics: " + satelliteConfigUpdaterParamsBuild);
        CarrierRoamingSatelliteControllerStats.getOrCreateInstance().reportCountOfSatelliteConfigUpdateRequest();
        initializeConfigUpdaterParams();
    }

    private void initializeConfigUpdaterParams() {
        this.mConfigVersion = -1;
        this.mOemConfigResult = 0;
        this.mCarrierConfigResult = 0;
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }
}

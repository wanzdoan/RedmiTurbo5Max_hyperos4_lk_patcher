package com.android.internal.telephony.satellite.metrics;

import android.util.Log;
import com.android.internal.telephony.metrics.SatelliteStats;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionMetricsStats {
    public static final int INVALID_TIME = -1;
    private static final String TAG = "ProvisionMetricsStats";
    private static ProvisionMetricsStats sInstance;
    private int mCarrierId;
    private boolean mIsCanceled;
    private boolean mIsNtnOnlyCarrier;
    private boolean mIsProvisionRequest;
    private int mProvisioningStartTimeSec;
    private int mResultCode;
    private int mSupportedConnectionMode;

    private ProvisionMetricsStats() {
        initializeProvisionParams();
    }

    public static ProvisionMetricsStats getOrCreateInstance() {
        if (sInstance == null) {
            logd("Create new ProvisionMetricsStats.");
            sInstance = new ProvisionMetricsStats();
        }
        return sInstance;
    }

    public ProvisionMetricsStats setResultCode(int i) {
        this.mResultCode = i;
        return this;
    }

    public void setProvisioningStartTime() {
        this.mProvisioningStartTimeSec = (int) (System.currentTimeMillis() / 1000);
    }

    public ProvisionMetricsStats setIsProvisionRequest(boolean z) {
        this.mIsProvisionRequest = z;
        return this;
    }

    public ProvisionMetricsStats setIsCanceled(boolean z) {
        this.mIsCanceled = z;
        return this;
    }

    public ProvisionMetricsStats setCarrierId(int i) {
        this.mCarrierId = i;
        return this;
    }

    public ProvisionMetricsStats setIsNtnOnlyCarrier(boolean z) {
        this.mIsNtnOnlyCarrier = z;
        return this;
    }

    public ProvisionMetricsStats setSupportedConnectionMode(int i) {
        this.mSupportedConnectionMode = i;
        return this;
    }

    public void reportProvisionMetrics() {
        SatelliteStats.SatelliteProvisionParams satelliteProvisionParamsBuild = new SatelliteStats.SatelliteProvisionParams.Builder().setResultCode(this.mResultCode).setProvisioningTimeSec(((int) (System.currentTimeMillis() / 1000)) - this.mProvisioningStartTimeSec).setIsProvisionRequest(this.mIsProvisionRequest).setIsCanceled(this.mIsCanceled).setCarrierId(this.mCarrierId).setIsNtnOnlyCarrier(this.mIsNtnOnlyCarrier).setSupportedConnectionMode(this.mSupportedConnectionMode).build();
        SatelliteStats.getInstance().onSatelliteProvisionMetrics(satelliteProvisionParamsBuild);
        logd("reportProvisionMetrics: " + satelliteProvisionParamsBuild);
        initializeProvisionParams();
    }

    private void initializeProvisionParams() {
        this.mResultCode = -1;
        this.mProvisioningStartTimeSec = -1;
        this.mIsProvisionRequest = false;
        this.mIsCanceled = false;
        this.mCarrierId = -1;
        this.mIsNtnOnlyCarrier = false;
        this.mSupportedConnectionMode = 0;
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }
}

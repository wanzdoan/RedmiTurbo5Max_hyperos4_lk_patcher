package com.android.internal.telephony.satellite.metrics;

import android.util.Log;
import com.android.internal.telephony.metrics.SatelliteStats;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public class AccessControllerMetricsStats {
    private static final String TAG = "AccessControllerMetricsStats";
    private static AccessControllerMetricsStats sInstance;
    private int mAccessControlType;
    private int mCarrierId;
    private int mConfigDataSource;
    private String[] mCountryCodes;
    private Boolean mIsAllowed;
    private Boolean mIsEmergency;
    private boolean mIsNtnOnlyCarrier;
    private long mLocationQueryTimeMillis;
    private long mOnDeviceLookupTimeMillis;
    private int mResultCode;
    private int mSessionConnectionMode;
    private int mSupportedConnectionMode;
    private long mTotalCheckingTimeMillis;
    private int mTriggeringEvent;

    private AccessControllerMetricsStats() {
        initializeAccessControllerMetricsParam();
    }

    public static AccessControllerMetricsStats getInstance() {
        if (sInstance == null) {
            loge("create new AccessControllerMetricsStats.");
            sInstance = new AccessControllerMetricsStats();
        }
        return sInstance;
    }

    private void initializeAccessControllerMetricsParam() {
        this.mAccessControlType = 0;
        this.mLocationQueryTimeMillis = 0L;
        this.mOnDeviceLookupTimeMillis = 0L;
        this.mTotalCheckingTimeMillis = 0L;
        this.mIsAllowed = null;
        this.mIsEmergency = null;
        this.mResultCode = 0;
        this.mCountryCodes = new String[0];
        this.mConfigDataSource = 0;
        this.mCarrierId = -1;
        this.mTriggeringEvent = 0;
        this.mIsNtnOnlyCarrier = false;
        this.mSupportedConnectionMode = 0;
        this.mSessionConnectionMode = 0;
    }

    public AccessControllerMetricsStats setAccessControlType(int i) {
        this.mAccessControlType = i;
        logd("setAccessControlType: access control type = " + this.mAccessControlType);
        return this;
    }

    public AccessControllerMetricsStats setLocationQueryTime(long j) {
        this.mLocationQueryTimeMillis = j > 0 ? getCurrentTime() - j : 0L;
        logd("setLocationQueryTimeMillis: location query time = " + this.mLocationQueryTimeMillis);
        return this;
    }

    public AccessControllerMetricsStats setOnDeviceLookupTime(long j) {
        this.mOnDeviceLookupTimeMillis = j > 0 ? getCurrentTime() - j : 0L;
        logd("setLocationQueryTime: on device lookup time = " + this.mOnDeviceLookupTimeMillis);
        return this;
    }

    public AccessControllerMetricsStats setTotalCheckingTime(long j) {
        this.mTotalCheckingTimeMillis = j > 0 ? getCurrentTime() - j : 0L;
        logd("setTotalCheckingTime: location query time = " + this.mTotalCheckingTimeMillis);
        return this;
    }

    public AccessControllerMetricsStats setIsAllowed(boolean z) {
        this.mIsAllowed = Boolean.valueOf(z);
        logd("setIsAllowed: allowed=" + this.mIsAllowed);
        return this;
    }

    public AccessControllerMetricsStats setIsEmergency(boolean z) {
        this.mIsEmergency = Boolean.valueOf(z);
        logd("setIsEmergency: emergency =" + this.mIsEmergency);
        return this;
    }

    public AccessControllerMetricsStats setResult(int i) {
        this.mResultCode = i;
        logd("setResult: result = " + this.mResultCode);
        return this;
    }

    public static /* synthetic */ String[] $r8$lambda$ZOSMDr9wdK0qNsR5jwvgIkakRAo(int i) {
        return new String[i];
    }

    public AccessControllerMetricsStats setCountryCodes(List<String> list) {
        this.mCountryCodes = (String[]) list.stream().toArray(new IntFunction() { // from class: com.android.internal.telephony.satellite.metrics.AccessControllerMetricsStats$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return AccessControllerMetricsStats.$r8$lambda$ZOSMDr9wdK0qNsR5jwvgIkakRAo(i);
            }
        });
        logd("setCountryCodes: country code is " + Arrays.toString(this.mCountryCodes));
        return this;
    }

    public AccessControllerMetricsStats setConfigDataSource(int i) {
        this.mConfigDataSource = i;
        logd("setConfigDataSource: config data source = " + this.mConfigDataSource);
        return this;
    }

    public AccessControllerMetricsStats setCarrierId(int i) {
        this.mCarrierId = i;
        logd("setCarrierId: Carrier ID = " + this.mCarrierId);
        return this;
    }

    public AccessControllerMetricsStats setTriggeringEvent(int i) {
        this.mTriggeringEvent = i;
        logd("setTriggeringEvent: triggering event = " + this.mTriggeringEvent);
        return this;
    }

    public AccessControllerMetricsStats setIsNtnOnlyCarrier(boolean z) {
        this.mIsNtnOnlyCarrier = z;
        logd("setIsNtnOnlyCarrier: isNtnOnlyCarrier = " + this.mIsNtnOnlyCarrier);
        return this;
    }

    public AccessControllerMetricsStats setSupportedConnectionMode(int i) {
        this.mSupportedConnectionMode = i;
        logd("setSupportedConnectionMode: supportedConnectionMode = " + this.mSupportedConnectionMode);
        return this;
    }

    public AccessControllerMetricsStats setSessionConnectionMode(int i) {
        this.mSessionConnectionMode = i;
        logd("setSessionConnectionMode: sessionConnectionMode = " + this.mSessionConnectionMode);
        return this;
    }

    public void reportAccessControllerMetrics() {
        SatelliteStats.SatelliteAccessControllerParams satelliteAccessControllerParamsBuild = new SatelliteStats.SatelliteAccessControllerParams.Builder().setAccessControlType(this.mAccessControlType).setLocationQueryTime(this.mLocationQueryTimeMillis).setOnDeviceLookupTime(this.mOnDeviceLookupTimeMillis).setTotalCheckingTime(this.mTotalCheckingTimeMillis).setIsAllowed(this.mIsAllowed.booleanValue()).setIsEmergency(this.mIsEmergency.booleanValue()).setResult(this.mResultCode).setCountryCodes(this.mCountryCodes).setConfigDatasource(this.mConfigDataSource).setCarrierId(this.mCarrierId).setTriggeringEvent(this.mTriggeringEvent).setIsNtnOnlyCarrier(this.mIsNtnOnlyCarrier).setSupportedConnectionMode(this.mSupportedConnectionMode).setSessionConnectionMode(this.mSessionConnectionMode).build();
        logd("reportAccessControllerMetrics: " + satelliteAccessControllerParamsBuild.toString());
        SatelliteStats.getInstance().onSatelliteAccessControllerMetrics(satelliteAccessControllerParamsBuild);
        initializeAccessControllerMetricsParam();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }

    private static void loge(String str) {
        Log.e(TAG, str);
    }
}

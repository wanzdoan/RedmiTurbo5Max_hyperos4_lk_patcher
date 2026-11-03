package com.android.internal.telephony.satellite.metrics;

import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.SatelliteController;

/* JADX INFO: loaded from: classes.dex */
public class EntitlementMetricsStats {
    private static final String TAG = "EntitlementMetricsStats";
    private static EntitlementMetricsStats sInstance;
    private int mEntitlementDataPolicy;
    private int[] mEntitlementServiceType;
    private int mEntitlementStatus;
    private int mHttpStatusCode;
    private boolean mIsAllowedServiceEntitlement;
    private boolean mIsRetry;
    private int mResult;
    private int mSubId;
    private int mTriggerEvent;

    private EntitlementMetricsStats() {
    }

    public static EntitlementMetricsStats getOrCreateInstance() {
        if (sInstance == null) {
            logd("Create new EntitlementMetricsStats.");
            sInstance = new EntitlementMetricsStats();
        }
        return sInstance;
    }

    public void reportSuccess(int i, int i2, boolean z, boolean z2, int[] iArr, int i3, int i4) {
        this.mSubId = i;
        this.mResult = 200;
        this.mEntitlementStatus = i2;
        this.mIsRetry = z;
        this.mIsAllowedServiceEntitlement = z2;
        this.mEntitlementServiceType = iArr;
        this.mEntitlementDataPolicy = i3;
        this.mHttpStatusCode = 0;
        this.mTriggerEvent = i4;
        reportEntitlementMetrics();
    }

    public void reportError(int i, int i2, boolean z, int i3, int i4) {
        this.mSubId = i;
        this.mResult = i2;
        this.mIsRetry = z;
        this.mEntitlementStatus = 0;
        this.mIsAllowedServiceEntitlement = false;
        this.mEntitlementServiceType = new int[0];
        this.mEntitlementDataPolicy = 0;
        this.mHttpStatusCode = i3;
        this.mTriggerEvent = i4;
        reportEntitlementMetrics();
    }

    private void reportEntitlementMetrics() {
        SatelliteStats.SatelliteEntitlementParams satelliteEntitlementParamsBuild = new SatelliteStats.SatelliteEntitlementParams.Builder().setCarrierId(getCarrierId(this.mSubId)).setResult(this.mResult).setEntitlementStatus(this.mEntitlementStatus).setIsRetry(this.mIsRetry).setCount(1).setIsAllowedServiceEntitlement(this.mIsAllowedServiceEntitlement).setEntitlementServiceType(this.mEntitlementServiceType).setEntitlementDataPolicy(this.mEntitlementDataPolicy).setSupportedConnectionMode(SatelliteController.getInstance().getSupportedConnectTypeMetrics(this.mSubId)).setHttpStatusCode(this.mHttpStatusCode).setTriggerEvent(this.mTriggerEvent).build();
        SatelliteStats.getInstance().onSatelliteEntitlementMetrics(satelliteEntitlementParamsBuild);
        logd("reportEntitlementMetrics: " + satelliteEntitlementParamsBuild);
        CarrierRoamingSatelliteControllerStats.getOrCreateInstance().reportCountOfEntitlementStatusQueryRequest(this.mSubId);
    }

    private int getCarrierId(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null) {
            return phone.getCarrierId();
        }
        return -1;
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }
}

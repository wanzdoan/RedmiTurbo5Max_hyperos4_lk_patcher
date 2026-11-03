package com.android.internal.telephony.satellite.metrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.SatelliteServiceUtils;

/* JADX INFO: loaded from: classes.dex */
public class ControllerMetricsStats {
    private static final String TAG = "ControllerMetricsStats";
    private static ControllerMetricsStats sInstance;
    private long mBatteryChargedStartTime;
    private int mBatteryLevelWhenServiceOn;
    private final BroadcastReceiver mBatteryStatusReceiver;
    private final Context mContext;
    private Boolean mIsBatteryCharged;
    private boolean mIsSatelliteModemOn;
    private long mSatelliteOnTimeMillis;
    private SatelliteStats mSatelliteStats;
    private int mTotalBatteryChargeTimeSec;

    public static ControllerMetricsStats getInstance() {
        if (sInstance == null) {
            loge("ControllerMetricsStats was not yet initialized.");
        }
        return sInstance;
    }

    public static ControllerMetricsStats make(Context context) {
        if (sInstance == null) {
            sInstance = new ControllerMetricsStats(context);
        }
        return sInstance;
    }

    public static ControllerMetricsStats make(Context context, SatelliteStats satelliteStats) {
        if (sInstance == null) {
            sInstance = new ControllerMetricsStats(context, satelliteStats);
        }
        return sInstance;
    }

    ControllerMetricsStats(Context context) {
        this.mIsBatteryCharged = null;
        this.mBatteryStatusReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.metrics.ControllerMetricsStats.1
            private long mLastUpdatedTime = 0;

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                long elapsedRealtime = ControllerMetricsStats.this.getElapsedRealtime();
                if (elapsedRealtime - this.mLastUpdatedTime > 60000) {
                    this.mLastUpdatedTime = elapsedRealtime;
                    boolean z = intent.getIntExtra("status", -1) == 2;
                    ControllerMetricsStats.logd("Battery is charged(" + z + ")");
                    ControllerMetricsStats.this.updateSatelliteBatteryChargeTime(z);
                }
            }
        };
        this.mContext = context;
        this.mSatelliteStats = SatelliteStats.getInstance();
    }

    protected ControllerMetricsStats(Context context, SatelliteStats satelliteStats) {
        this.mIsBatteryCharged = null;
        this.mBatteryStatusReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.metrics.ControllerMetricsStats.1
            private long mLastUpdatedTime = 0;

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                long elapsedRealtime = ControllerMetricsStats.this.getElapsedRealtime();
                if (elapsedRealtime - this.mLastUpdatedTime > 60000) {
                    this.mLastUpdatedTime = elapsedRealtime;
                    boolean z = intent.getIntExtra("status", -1) == 2;
                    ControllerMetricsStats.logd("Battery is charged(" + z + ")");
                    ControllerMetricsStats.this.updateSatelliteBatteryChargeTime(z);
                }
            }
        };
        this.mContext = context;
        this.mSatelliteStats = satelliteStats;
    }

    public void reportServiceEnablementSuccessCount(boolean z) {
        logd("reportServiceEnablementSuccessCount: isDemoMode=" + z);
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (z) {
            builder.setCountOfDemoModeSatelliteServiceEnablementsSuccess(1);
        } else {
            builder.setCountOfSatelliteServiceEnablementsSuccess(1);
        }
        this.mSatelliteStats.onSatelliteControllerMetrics(builder.build());
    }

    public void reportServiceEnablementFailCount(boolean z) {
        logd("reportServiceEnablementFailCount: isDemoMode=" + z);
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (z) {
            builder.setCountOfDemoModeSatelliteServiceEnablementsFail(1);
        } else {
            builder.setCountOfSatelliteServiceEnablementsFail(1);
        }
        this.mSatelliteStats.onSatelliteControllerMetrics(builder.build());
    }

    public void reportOutgoingDatagramSuccessCount(int i, boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (z) {
            builder.setCountOfDemoModeOutgoingDatagramSuccess(1);
        } else {
            builder.setCountOfOutgoingDatagramSuccess(1);
            if (SatelliteServiceUtils.isSosMessage(i)) {
                builder.setCountOfDatagramTypeSosSmsSuccess(1);
            } else if (i == 2) {
                builder.setCountOfDatagramTypeLocationSharingSuccess(1);
            } else if (i == 3) {
                builder.setCountOfDatagramTypeKeepAliveSuccess(1).build();
            } else if (i == 6) {
                builder.setCountOfOutgoingDatagramTypeSmsSuccess(1);
            }
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = builder.build();
        logd("reportOutgoingDatagramSuccessCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportOutgoingDatagramFailCount(int i, boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (z) {
            builder.setCountOfDemoModeOutgoingDatagramFail(1);
        } else {
            builder.setCountOfOutgoingDatagramFail(1);
            if (SatelliteServiceUtils.isSosMessage(i)) {
                builder.setCountOfDatagramTypeSosSmsFail(1);
            } else if (i == 2) {
                builder.setCountOfDatagramTypeLocationSharingFail(1);
            } else if (i == 3) {
                builder.setCountOfDatagramTypeKeepAliveFail(1);
            } else if (i == 6) {
                builder.setCountOfOutgoingDatagramTypeSmsFail(1);
            }
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = builder.build();
        logd("reportOutgoingDatagramFailCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportIncomingDatagramCount(int i, boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (z) {
            if (i == 0) {
                builder.setCountOfDemoModeIncomingDatagramSuccess(1);
            } else {
                builder.setCountOfDemoModeIncomingDatagramFail(1);
            }
        } else if (i == 0) {
            builder.setCountOfIncomingDatagramSuccess(1).setCountOfIncomingDatagramTypeSosSmsSuccess(1);
        } else {
            builder.setCountOfIncomingDatagramFail(1).setCountOfIncomingDatagramTypeSosSmsFail(1);
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = builder.build();
        logd("reportIncomingDatagramCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportIncomingNtnSmsCount(int i) {
        SatelliteStats.SatelliteControllerParams.Builder builder = new SatelliteStats.SatelliteControllerParams.Builder();
        if (i == 0) {
            builder.setCountOfIncomingDatagramTypeSmsSuccess(1);
        } else {
            builder.setCountOfIncomingDatagramTypeSmsFail(1);
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = builder.build();
        logd("reportIncomingNtnSmsCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportProvisionCount(int i) {
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild;
        if (i == 0) {
            satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfProvisionSuccess(1).build();
        } else {
            satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfProvisionFail(1).build();
        }
        logd("reportProvisionCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportDeprovisionCount(int i) {
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild;
        if (i == 0) {
            satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfDeprovisionSuccess(1).build();
        } else {
            satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfDeprovisionFail(1).build();
        }
        logd("reportDeprovisionCount(): " + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportAllowedSatelliteAccessCount(boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder countOfDisallowedSatelliteAccess;
        if (z) {
            countOfDisallowedSatelliteAccess = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfAllowedSatelliteAccess(1);
        } else {
            countOfDisallowedSatelliteAccess = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfDisallowedSatelliteAccess(1);
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = countOfDisallowedSatelliteAccess.build();
        logd("reportAllowedSatelliteAccessCount:" + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportFailedSatelliteAccessCheckCount() {
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfSatelliteAccessCheckFail(1).build();
        logd("reportFailedSatelliteAccessCheckCount:" + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public int captureTotalServiceUpTimeSec() {
        long elapsedRealtime = getElapsedRealtime() - this.mSatelliteOnTimeMillis;
        this.mSatelliteOnTimeMillis = 0L;
        return (int) (elapsedRealtime / 1000);
    }

    public int captureTotalBatteryChargeTimeSec() {
        int i = this.mTotalBatteryChargeTimeSec;
        this.mTotalBatteryChargeTimeSec = 0;
        return i;
    }

    public void onSatelliteEnabled() {
        if (isSatelliteModemOn()) {
            return;
        }
        this.mIsSatelliteModemOn = true;
        startCaptureBatteryLevel();
        this.mSatelliteOnTimeMillis = getElapsedRealtime();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        logd("register BatteryStatusReceiver");
        this.mContext.registerReceiver(this.mBatteryStatusReceiver, intentFilter);
    }

    public void onSatelliteDisabled() {
        if (isSatelliteModemOn()) {
            this.mIsSatelliteModemOn = false;
            logd("unregister BatteryStatusReceiver");
            this.mContext.unregisterReceiver(this.mBatteryStatusReceiver);
            int iCaptureTotalServiceUpTimeSec = captureTotalServiceUpTimeSec();
            SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = new SatelliteStats.SatelliteControllerParams.Builder().setTotalServiceUptimeSec(iCaptureTotalServiceUpTimeSec).setTotalBatteryConsumptionPercent(captureTotalBatteryConsumptionPercent(this.mContext)).setTotalBatteryChargedTimeSec(captureTotalBatteryChargeTimeSec()).build();
            logd("onSatelliteDisabled(): " + satelliteControllerParamsBuild);
            this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSatelliteBatteryChargeTime(boolean z) {
        logd("updateSatelliteBatteryChargeTime(" + z + ")");
        Boolean bool = this.mIsBatteryCharged;
        if (bool == null || z != bool.booleanValue()) {
            this.mIsBatteryCharged = Boolean.valueOf(z);
            if (z) {
                this.mBatteryChargedStartTime = getElapsedRealtime();
            } else {
                this.mTotalBatteryChargeTimeSec += (int) ((getElapsedRealtime() - this.mBatteryChargedStartTime) / 1000);
                this.mBatteryChargedStartTime = 0L;
            }
        }
    }

    public void startCaptureBatteryLevel() {
        try {
            this.mBatteryLevelWhenServiceOn = ((BatteryManager) this.mContext.getSystemService(BatteryManager.class)).getIntProperty(4);
            logd("sBatteryLevelWhenServiceOn = " + this.mBatteryLevelWhenServiceOn);
        } catch (NullPointerException unused) {
            loge("BatteryManager is null");
        }
    }

    public int captureTotalBatteryConsumptionPercent(Context context) {
        try {
            return Math.max(this.mBatteryLevelWhenServiceOn - ((BatteryManager) context.getSystemService(BatteryManager.class)).getIntProperty(4), 0);
        } catch (NullPointerException unused) {
            loge("BatteryManager is null");
            return 0;
        }
    }

    public void setIsProvisioned(int i, boolean z, boolean z2, int i2) {
        logd("setIsProvisioned: carrierId=" + i + ", isProvisioned=" + z);
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setCarrierId(i).setSupportedConnectionMode(i2).setIsProvisioned(z).setIsNtnOnlyCarrier(z2).build());
    }

    public void setCarrierIdInfo(int i, boolean z, int i2) {
        logd("setCarrierId:" + i + ", isNtnOnlyCarrier:" + z);
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setCarrierId(i).setSupportedConnectionMode(i2).setIsNtnOnlyCarrier(z).build());
    }

    public void reportAllowedStateChanged() {
        logd("reportAllowedStateChanged:");
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setCountOfSatelliteAllowedStateChangedEvents(1).build());
    }

    public void reportLocationQuerySuccessful(boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder countOfFailedLocationQueries;
        if (z) {
            countOfFailedLocationQueries = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfSuccessfulLocationQueries(1);
        } else {
            countOfFailedLocationQueries = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfFailedLocationQueries(1);
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = countOfFailedLocationQueries.build();
        logd("reportLocationQuerySuccessful:" + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public void reportCurrentVersionOfSatelliteAccessConfig(int i) {
        logd("reportCurrentVersionOfSatelliteAccessConfig:" + i);
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setVersionOfSatelliteAccessControl(i).build());
    }

    public void reportCurrentVersionOfCarrierRoamingSatelliteConfig(int i, int i2, int i3) {
        logd("reportCurrentVersionOfCarrierRoamingSatelliteConfig: cid=" + i + " version=" + i2);
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setCarrierId(i).setSupportedConnectionMode(i3).setCarrierRoamingSatelliteConfigVersion(i2).build());
    }

    public void reportCurrentMaxAllowedDataMode(int i, int i2, int i3) {
        logd("reportCurrentMaxAllowedDataMode: cid=" + i + " maxAllowedDataMode=" + i2);
        this.mSatelliteStats.onSatelliteControllerMetrics(new SatelliteStats.SatelliteControllerParams.Builder().setCarrierId(i).setSupportedConnectionMode(i3).setMaxAllowedDataMode(i2).build());
    }

    public void reportP2PSmsEligibilityNotificationsCount(boolean z) {
        SatelliteStats.SatelliteControllerParams.Builder countOfP2PSmsAvailableNotificationRemoved;
        if (z) {
            countOfP2PSmsAvailableNotificationRemoved = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfP2PSmsAvailableNotificationShown(1);
        } else {
            countOfP2PSmsAvailableNotificationRemoved = new SatelliteStats.SatelliteControllerParams.Builder().setCountOfP2PSmsAvailableNotificationRemoved(1);
        }
        SatelliteStats.SatelliteControllerParams satelliteControllerParamsBuild = countOfP2PSmsAvailableNotificationRemoved.build();
        logd("reportP2PSmsEligibilityNotificationsCount:" + satelliteControllerParamsBuild);
        this.mSatelliteStats.onSatelliteControllerMetrics(satelliteControllerParamsBuild);
    }

    public boolean isSatelliteModemOn() {
        return this.mIsSatelliteModemOn;
    }

    public long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d(TAG, str);
    }

    private static void loge(String str) {
        Log.e(TAG, str);
    }
}

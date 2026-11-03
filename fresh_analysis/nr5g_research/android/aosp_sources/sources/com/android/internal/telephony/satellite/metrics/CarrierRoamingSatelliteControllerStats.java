package com.android.internal.telephony.satellite.metrics;

import android.os.SystemClock;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteServiceUtils;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CarrierRoamingSatelliteControllerStats {
    private static final String TAG = "CarrierRoamingSatelliteControllerStats";
    private static CarrierRoamingSatelliteControllerStats sInstance;
    private Map mSessionStartTimeMap = new HashMap();
    private Map mSessionEndTimeMap = new HashMap();
    private final SatelliteStats mSatelliteStats = SatelliteStats.getInstance();
    private final SubscriptionManagerService mSubscriptionManagerService = SubscriptionManagerService.getInstance();

    public CarrierRoamingSatelliteControllerStats() {
        resetSessionGapLists();
    }

    public static CarrierRoamingSatelliteControllerStats getOrCreateInstance() {
        if (sInstance == null) {
            logd("Create new CarrierRoamingSatelliteControllerStats.");
            sInstance = new CarrierRoamingSatelliteControllerStats();
        }
        return sInstance;
    }

    public void reportConfigDataSource(int i, int i2) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).setConfigDataSource(i2).build());
    }

    public void reportCountOfEntitlementStatusQueryRequest(int i) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setCountOfEntitlementStatusQueryRequest(1).build());
    }

    public void reportCountOfSatelliteConfigUpdateRequest() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList == null || activeSubIdList.length == 0) {
            logd("reportCountOfSatelliteConfigUpdateRequest: No active subIds to report for.");
            return;
        }
        logd("reportCountOfSatelliteConfigUpdateRequest: Processing " + activeSubIdList.length + " active subIds.");
        SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder countOfSatelliteConfigUpdateRequest = new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCountOfSatelliteConfigUpdateRequest(1);
        for (int i : activeSubIdList) {
            int carrierIdFromSubscription = getCarrierIdFromSubscription(i);
            if (carrierIdFromSubscription != -1) {
                logd("reportCountOfSatelliteConfigUpdateRequest: Reporting for subId=" + i + ", carrierId=" + carrierIdFromSubscription);
                this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(countOfSatelliteConfigUpdateRequest.setCarrierId(carrierIdFromSubscription).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).build());
            } else {
                logd("reportCountOfSatelliteConfigUpdateRequest: Skipping subId=" + i + " due to UNKNOWN_CARRIER_ID");
            }
        }
    }

    public void reportCountOfSatelliteNotificationDisplayed(int i) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).setCountOfSatelliteNotificationDisplayed(1).build());
    }

    public void reportCarrierId(int i) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).build());
    }

    public void reportIsDeviceEntitled(int i, boolean z) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(z).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).build());
    }

    public void reportServiceDataPolicy(int i, int i2) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).setServiceDataPolicy(i2).build());
    }

    public void reportDeviceEligibilitySource(int i, boolean z, int i2) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(z).setEligibilitySource(i2).build());
    }

    public void onSessionStart(int i) {
        List list = (List) this.mSessionStartTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList());
        list.add(Long.valueOf(getElapsedRealtime()));
        this.mSessionStartTimeMap.put(Integer.valueOf(i), list);
        int sessionConnectTypeMetrics = SatelliteController.getInstance().getSessionConnectTypeMetrics(i);
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).increaseCountOfSatelliteSessions().increaseCountOfSessionConnectionModeAutomatic(sessionConnectTypeMetrics == 1).increaseCountOfSessionConnectionModeManual(sessionConnectTypeMetrics == 2).build());
    }

    public void onSessionEnd(int i) {
        int iIntValue;
        int iIntValue2;
        List list = (List) this.mSessionEndTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList());
        list.add(Long.valueOf(getElapsedRealtime()));
        this.mSessionEndTimeMap.put(Integer.valueOf(i), list);
        List satelliteSessionGapList = getSatelliteSessionGapList(i, getNumberOfSatelliteSessions(i));
        if (satelliteSessionGapList.isEmpty()) {
            iIntValue = 0;
            iIntValue2 = 0;
        } else {
            iIntValue = ((Integer) Collections.min(satelliteSessionGapList)).intValue();
            iIntValue2 = ((Integer) Collections.max(satelliteSessionGapList)).intValue();
        }
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSatelliteSessionGapMinSec(iIntValue).setSatelliteSessionGapAvgSec(getAvg(satelliteSessionGapList)).setSatelliteSessionGapMaxSec(iIntValue2).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).build());
    }

    public void addSessionDurationSec(int i, int i2) {
        this.mSatelliteStats.onCarrierRoamingSatelliteControllerStatsMetrics(new SatelliteStats.CarrierRoamingSatelliteControllerStatsParams.Builder().setCarrierId(getCarrierIdFromSubscription(i)).setIsDeviceEntitled(isDeviceEntitled(i)).setIsMultiSim(isMultiSim()).setIsNbIotNtn(isNbIotNtn(i)).setSupportedConnectionMode(getSupportedConnectType(i)).setSessionDurationSec(i2).setSatelliteAttachSupported(isSatelliteAttachSupported(i)).setEligibilitySource(getSatelliteEligibilitySource(i)).build());
    }

    public void resetSessionGapLists() {
        this.mSessionStartTimeMap = new HashMap();
        this.mSessionEndTimeMap = new HashMap();
    }

    private int getNumberOfSatelliteSessions(int i) {
        return Math.min(((List) this.mSessionStartTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList())).size(), ((List) this.mSessionEndTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList())).size());
    }

    private List getSatelliteSessionGapList(int i, int i2) {
        if (i2 == 0) {
            return new ArrayList();
        }
        List list = (List) this.mSessionStartTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList());
        List list2 = (List) this.mSessionEndTimeMap.getOrDefault(Integer.valueOf(i), new ArrayList());
        ArrayList arrayList = new ArrayList();
        for (int i3 = 1; i3 < i2; i3++) {
            long jLongValue = ((Long) list2.get(i3 - 1)).longValue();
            long jLongValue2 = ((Long) list.get(i3)).longValue();
            if (jLongValue2 > jLongValue && jLongValue > 0) {
                arrayList.add(Integer.valueOf((int) ((jLongValue2 - jLongValue) / 1000)));
            }
        }
        return arrayList;
    }

    private int getAvg(List list) {
        int iIntValue = 0;
        if (list.isEmpty()) {
            return 0;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            iIntValue += ((Integer) it.next()).intValue();
        }
        return iIntValue / list.size();
    }

    protected long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public boolean isMultiSim() {
        return SubscriptionManagerService.getInstance().getActiveSubIdList(true).length > 1;
    }

    protected int getCarrierIdFromSubscription(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null) {
            return phone.getCarrierId();
        }
        return -1;
    }

    protected boolean isDeviceEntitled(int i) {
        return SatelliteServiceUtils.isDeviceEntitledForSubscription(i);
    }

    protected boolean isNbIotNtn(int i) {
        return SatelliteServiceUtils.isNbIotNtn(i);
    }

    protected int getSupportedConnectType(int i) {
        return SatelliteServiceUtils.getSupportedConnectTypeMetrics(i);
    }

    private boolean isSatelliteAttachSupported(int i) {
        return SatelliteServiceUtils.isSatelliteAttachSupported(i);
    }

    private int getSatelliteEligibilitySource(int i) {
        return SatelliteServiceUtils.getSatelliteEligibilitySource(i);
    }

    private static void logd(String str) {
        Log.d(TAG, str);
    }
}

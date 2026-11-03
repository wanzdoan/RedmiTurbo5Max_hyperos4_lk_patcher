package com.android.internal.telephony.satellite.metrics;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkTemplate;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.metrics.ModemPowerMetrics$$ExternalSyntheticLambda0;
import com.android.internal.telephony.metrics.SatelliteStats;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.satellite.SatelliteServiceUtils;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/* JADX INFO: loaded from: classes.dex */
public class CarrierRoamingSatelliteSessionStats {
    private static final String TAG = "CarrierRoamingSatelliteSessionStats";
    private static final SparseArray sCarrierRoamingSatelliteSessionStats = new SparseArray();
    private int mAccumulatedScreenOnTimeSec;
    private BatteryManager mBatteryManager;
    private int mCarrierId;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private int mCountOfEmergencyDialerButtonDisplayed;
    private int mCountOfIncomingMms;
    private int mCountOfIncomingSms;
    private int mCountOfNonEmergencyDialerDialogDisplayed;
    private int mCountOfOutgoingMms;
    private int mCountOfOutgoingSms;
    private int mCountOfSatelliteNotificationDisplayed;
    private DataStallIntentReceiver mDataStallIntentReceiver;
    private FeatureFlags mFeatureFlags;
    private long mIncomingMessageId;
    private boolean mIsNtnRoamingInHomeCountry;
    private boolean mIsWfcEnabled;
    private boolean mIsWfcRegistered;
    private boolean mIsWifiConnected;
    private boolean mIsWifiEnabled;
    private NetworkCapabilities mNetworkcapabilities;
    private Phone mPhone;
    private String mPlmn;
    private List mRsrpList;
    private List mRssnrList;
    private SatelliteConnectionTimes mSatelliteConnectionTimes;
    private List mSatelliteConnectionTimesList;
    private long mScreenOnStartTimeMillis;
    private int mServiceDataPolicy;
    private int mSessionConnectionMode;
    private int mSessionStartTimeSec;
    private int mStartBatteryPropertyCapacity;
    private long mStartBatteryPropertyEnergyCounter;
    private final SubscriptionManagerService mSubscriptionManagerService;
    private int mSupportedConnectionMode;
    private int[] mSupportedSatelliteServices;
    private TelephonyListenerImpl mTelephonyListener;
    private boolean mWasChargingDuringSession;
    private long mSatelliteDataConsumedBytes = 0;
    private long mTotalRxDataConsumedBytes = 0;
    private long mTotalTxDataConsumedBytes = 0;
    private long mDataUsageOnSessionStartBytes = 0;
    private long mTotalRxDataOnSessionStartBytes = 0;
    private long mTotalTxDataOnSessionStartBytes = 0;
    private Map mPerAppDataUsageOnSessionStartMap = new HashMap();
    private Map mPerAppDataUsageOnSessionStartMapLegacy = new HashMap();
    private Map mSatelliteAppUidMap = new HashMap();
    private int[] mLastFailCauses = new int[5];
    private int mCountOfDataConnections = 0;
    private int mCountOfDataDisconnections = 0;
    private int mCurrentState = 0;
    private int mFailCauseIndex = 0;
    private int mCountOfDataStalls = 0;
    private int mSumOfDownlinkBandwidthKbps = 0;
    private int mSumOfUplinkBandwidthKbps = 0;
    private int mAverageUplinkBandwidthKbps = 0;
    private int mAverageDownlinkBandwidthKbps = 0;
    private int mMinUplinkBandwidthKbps = Integer.MAX_VALUE;
    private int mMaxUplinkBandwidthKbps = 0;
    private int mMaxDownlinkBandwidthKbps = 0;
    private int mMinDownlinkBandwidthKbps = Integer.MAX_VALUE;
    private final Executor mExecutor = new CarrierPrivilegesTracker$$ExternalSyntheticLambda0();
    String[] mSatelliteAppsPackageNameArray = null;
    private long[] mPerAppSatelliteDataConsumedBytesArray = {0};
    private long[] mPerAppRxDataConsumedBytesArray = {0};
    private long[] mPerAppTxDataConsumedBytesArray = {0};
    private int[] mSatelliteAppsUidArray = new int[5];
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats.1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            CarrierRoamingSatelliteSessionStats.this.logd("On Available: " + network);
            if (network == null || CarrierRoamingSatelliteSessionStats.this.mConnectivityManager == null) {
                return;
            }
            CarrierRoamingSatelliteSessionStats carrierRoamingSatelliteSessionStats = CarrierRoamingSatelliteSessionStats.this;
            carrierRoamingSatelliteSessionStats.mNetworkcapabilities = carrierRoamingSatelliteSessionStats.mConnectivityManager.getNetworkCapabilities(network);
            if (CarrierRoamingSatelliteSessionStats.this.mNetworkcapabilities != null && CarrierRoamingSatelliteSessionStats.this.mNetworkcapabilities.hasTransport(10) && CarrierRoamingSatelliteSessionStats.this.mNetworkcapabilities.hasCapability(12)) {
                CarrierRoamingSatelliteSessionStats.this.logd("found satellite data connection");
                CarrierRoamingSatelliteSessionStats.this.startDataConnectionTracker();
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            CarrierRoamingSatelliteSessionStats.this.logd("onCapabilitiesChanged: " + network);
            CarrierRoamingSatelliteSessionStats.this.mNetworkcapabilities = networkCapabilities;
        }
    };

    public static class DataUsage {
        private final long mRxBytes;
        private final long mTxBytes;

        public DataUsage(long j, long j2) {
            this.mRxBytes = j;
            this.mTxBytes = j2;
        }

        public long getRxBytes() {
            return this.mRxBytes;
        }

        public long getTxBytes() {
            return this.mTxBytes;
        }

        public long getTotalBytes() {
            return this.mRxBytes + this.mTxBytes;
        }

        public String toString() {
            return "DataUsage(" + this.mRxBytes + " rx, " + this.mTxBytes + " tx)";
        }
    }

    public class DataStallIntentReceiver extends BroadcastReceiver {
        public DataStallIntentReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle bundleExtra;
            if (intent.getAction().equals("android.intent.action.DATA_STALL_DETECTED") && (bundleExtra = intent.getBundleExtra("EXTRA_DSRS_STATS_BUNDLE")) != null && bundleExtra.containsKey("IsRecovered")) {
                boolean z = false;
                if (bundleExtra.get("IsRecovered") instanceof Integer) {
                    if (bundleExtra.getInt("IsRecovered") == 1) {
                        z = true;
                    }
                } else if (bundleExtra.get("IsRecovered") instanceof Boolean) {
                    z = bundleExtra.getBoolean("IsRecovered");
                }
                if (z || CarrierRoamingSatelliteSessionStats.this.mCurrentState != 2) {
                    return;
                }
                CarrierRoamingSatelliteSessionStats.this.mCountOfDataStalls++;
                CarrierRoamingSatelliteSessionStats.this.logd("data stall count: " + CarrierRoamingSatelliteSessionStats.this.mCountOfDataStalls);
            }
        }
    }

    private class TelephonyListenerImpl extends TelephonyCallback implements TelephonyCallback.PreciseDataConnectionStateListener {
        private final Executor mExecutor;
        private TelephonyManager mTelephonyManager = null;

        TelephonyListenerImpl(Executor executor) {
            this.mExecutor = executor;
        }

        public void register(TelephonyManager telephonyManager) {
            if (telephonyManager == null) {
                return;
            }
            this.mTelephonyManager = telephonyManager;
            telephonyManager.registerTelephonyCallback(this.mExecutor, this);
        }

        public void unregister() {
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.unregisterTelephonyCallback(this);
                this.mTelephonyManager = null;
            }
        }

        @Override // android.telephony.TelephonyCallback.PreciseDataConnectionStateListener
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            CarrierRoamingSatelliteSessionStats.this.logd("Received PrecisionDataStateChange:" + preciseDataConnectionState);
            if (preciseDataConnectionState == null || (preciseDataConnectionState.getApnSetting().getApnTypeBitmask() & 17) <= 0) {
                return;
            }
            int state = preciseDataConnectionState.getState();
            CarrierRoamingSatelliteSessionStats.this.logd("Internet Connection status: " + state);
            if (CarrierRoamingSatelliteSessionStats.this.mCurrentState != state) {
                if (state == 2) {
                    CarrierRoamingSatelliteSessionStats.this.handleConnection();
                    CarrierRoamingSatelliteSessionStats.this.updateLinkBandwidthForConnection();
                } else if (state == 0) {
                    CarrierRoamingSatelliteSessionStats.this.handleDisconnection();
                    CarrierRoamingSatelliteSessionStats.this.storeFailCause(preciseDataConnectionState.getLastCauseCode());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLinkBandwidthForConnection() {
        NetworkCapabilities networkCapabilities = this.mNetworkcapabilities;
        if (networkCapabilities != null) {
            int linkUpstreamBandwidthKbps = networkCapabilities.getLinkUpstreamBandwidthKbps();
            int linkDownstreamBandwidthKbps = this.mNetworkcapabilities.getLinkDownstreamBandwidthKbps();
            this.mSumOfUplinkBandwidthKbps += linkUpstreamBandwidthKbps;
            this.mSumOfDownlinkBandwidthKbps += linkDownstreamBandwidthKbps;
            if (linkUpstreamBandwidthKbps > 0) {
                this.mMaxUplinkBandwidthKbps = Math.max(this.mMaxUplinkBandwidthKbps, linkUpstreamBandwidthKbps);
                this.mMinUplinkBandwidthKbps = Math.min(this.mMinUplinkBandwidthKbps, linkUpstreamBandwidthKbps);
            }
            if (linkDownstreamBandwidthKbps > 0) {
                this.mMaxDownlinkBandwidthKbps = Math.max(this.mMaxDownlinkBandwidthKbps, linkDownstreamBandwidthKbps);
                this.mMinDownlinkBandwidthKbps = Math.min(this.mMinDownlinkBandwidthKbps, linkDownstreamBandwidthKbps);
                return;
            }
            return;
        }
        loge("networkcapabilities found null");
    }

    private boolean registerTelephonyListener() {
        int subId = this.mPhone.getSubId();
        if (subId == -1) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        if (telephonyManager == null) {
            return true;
        }
        TelephonyListenerImpl telephonyListenerImpl = new TelephonyListenerImpl(this.mExecutor);
        this.mTelephonyListener = telephonyListenerImpl;
        telephonyListenerImpl.register(telephonyManager.createForSubscriptionId(subId));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnection() {
        this.mCountOfDataConnections++;
        logd("Connection established. Total connections: " + this.mCountOfDataConnections);
        this.mCurrentState = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisconnection() {
        this.mCountOfDataDisconnections++;
        logd("Connection lost. Total disconnections: " + this.mCountOfDataDisconnections);
        this.mCurrentState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeFailCause(int i) {
        int[] iArr = this.mLastFailCauses;
        int i2 = this.mFailCauseIndex;
        iArr[i2] = i;
        this.mFailCauseIndex = (i2 + 1) % 5;
        logd("current fail causes: " + Arrays.toString(this.mLastFailCauses));
    }

    public CarrierRoamingSatelliteSessionStats(int i) {
        logd("Create new CarrierRoamingSatelliteSessionStats. subId=" + i);
        initializeParams();
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
    }

    public static CarrierRoamingSatelliteSessionStats getInstance(int i) {
        CarrierRoamingSatelliteSessionStats carrierRoamingSatelliteSessionStats;
        SparseArray sparseArray = sCarrierRoamingSatelliteSessionStats;
        synchronized (sparseArray) {
            try {
                if (sparseArray.get(i) == null) {
                    sparseArray.put(i, new CarrierRoamingSatelliteSessionStats(i));
                }
                carrierRoamingSatelliteSessionStats = (CarrierRoamingSatelliteSessionStats) sparseArray.get(i);
            } catch (Throwable th) {
                throw th;
            }
        }
        return carrierRoamingSatelliteSessionStats;
    }

    public static void clearInstancesForTest() {
        sCarrierRoamingSatelliteSessionStats.clear();
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0080  */
    public void onSessionStart(int i, Phone phone, int[] iArr, int i2, List<String> list, int i3, int i4, String str, FeatureFlags featureFlags, boolean z, boolean z2) {
        boolean z3;
        logd("onSessionStart:");
        this.mPhone = phone;
        this.mContext = phone.getContext();
        this.mCarrierId = i;
        this.mSupportedSatelliteServices = iArr;
        this.mServiceDataPolicy = i2;
        this.mSessionStartTimeSec = getElapsedRealtimeInSec();
        this.mIsNtnRoamingInHomeCountry = false;
        onConnectionStart(this.mPhone);
        this.mTotalRxDataConsumedBytes = 0L;
        this.mTotalTxDataConsumedBytes = 0L;
        this.mFeatureFlags = featureFlags;
        this.mDataUsageOnSessionStartBytes = getDataUsage();
        this.mTotalRxDataOnSessionStartBytes = 0L;
        this.mTotalTxDataOnSessionStartBytes = 0L;
        logd("current data consumed: " + this.mDataUsageOnSessionStartBytes);
        this.mPerAppDataUsageOnSessionStartMapLegacy = getPerAppSatelliteDataUsage(list);
        this.mSupportedConnectionMode = i3;
        this.mPlmn = str;
        this.mSessionConnectionMode = i4;
        WifiManager wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        if (wifiManager != null) {
            this.mIsWifiEnabled = wifiManager.isWifiEnabled();
        }
        boolean zIsWifiCallingEnabled = this.mPhone.isWifiCallingEnabled();
        this.mIsWfcEnabled = zIsWifiCallingEnabled;
        if (zIsWifiCallingEnabled && this.mPhone.isImsRegistered()) {
            z3 = this.mPhone.getImsRegistrationTech() == 1;
        }
        this.mIsWfcRegistered = z3;
        logd("mIsWifiEnabled: " + this.mIsWifiEnabled + ", mIsWfcEnabled: " + this.mIsWfcEnabled + ", mIsWfcRegistered: " + this.mIsWfcRegistered);
        registerForSatelliteDataNetworkCallback();
        this.mAccumulatedScreenOnTimeSec = 0;
        if (z) {
            this.mScreenOnStartTimeMillis = getElapsedRealtime();
        } else {
            this.mScreenOnStartTimeMillis = 0L;
        }
        BatteryManager batteryManager = (BatteryManager) this.mContext.getSystemService(BatteryManager.class);
        this.mBatteryManager = batteryManager;
        if (batteryManager != null) {
            this.mWasChargingDuringSession = batteryManager.isCharging();
            this.mStartBatteryPropertyCapacity = getBatteryPropertyCapacity();
            this.mStartBatteryPropertyEnergyCounter = getBatteryPropertyEnergyCounter();
        }
        this.mIsWifiConnected = z2;
    }

    private int getBatteryPropertyCapacity() {
        BatteryManager batteryManager = this.mBatteryManager;
        if (batteryManager == null) {
            return -1;
        }
        int intProperty = batteryManager.getIntProperty(4);
        logd("getBatteryPropertyCapacity: batteryPercentage=" + intProperty);
        return intProperty;
    }

    private long getBatteryPropertyEnergyCounter() {
        BatteryManager batteryManager = this.mBatteryManager;
        if (batteryManager == null) {
            return -1L;
        }
        long longProperty = batteryManager.getLongProperty(5);
        logd("getBatteryPropertyEnergyCouter: energyNwh=" + longProperty);
        return longProperty;
    }

    public void onConnectionStart(Phone phone) {
        this.mSatelliteConnectionTimes = new SatelliteConnectionTimes(getElapsedRealtime());
        updateNtnRoamingInHomeCountry(phone);
    }

    private void registerDataStallIntentReceiver() {
        if (this.mDataStallIntentReceiver == null) {
            logd("Track data stall status");
            this.mDataStallIntentReceiver = new DataStallIntentReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.DATA_STALL_DETECTED");
            this.mContext.registerReceiver(this.mDataStallIntentReceiver, intentFilter, 2);
        }
    }

    private void registerForSatelliteDataNetworkCallback() {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(10);
        builder.removeCapability(37);
        builder.removeCapability(13);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService("connectivity");
        this.mConnectivityManager = connectivityManager;
        if (connectivityManager != null) {
            logd("register for best matching");
            HandlerThread handlerThread = new HandlerThread("SatelliteNetworkCallbackThread");
            handlerThread.start();
            this.mConnectivityManager.registerBestMatchingNetworkCallback(builder.build(), this.mNetworkCallback, new Handler(handlerThread.getLooper()));
            return;
        }
        loge("network callback not registered");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDataConnectionTracker() {
        logd("Start tracking data disconnection for the cause");
        if (this.mTelephonyListener == null) {
            if (registerTelephonyListener()) {
                registerDataStallIntentReceiver();
                return;
            } else {
                loge("fails tor register for precision data connection state change");
                return;
            }
        }
        loge("telephony listener is registered");
    }

    protected DataUsage getDetailedDataUsage() {
        Context context = this.mContext;
        if (context == null) {
            return new DataUsage(0L, 0L);
        }
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NetworkStatsManager.class);
        if (networkStatsManager != null) {
            NetworkTemplate.Builder builder = new NetworkTemplate.Builder(1);
            String subscriberId = this.mPhone.getSubscriberId();
            logd("subscriber id for data consumed:" + subscriberId);
            if (!TextUtils.isEmpty(subscriberId)) {
                builder.setSubscriberIds(Set.of(subscriberId));
                builder.setMeteredness(1);
                NetworkStats.Bucket bucketQuerySummaryForDevice = networkStatsManager.querySummaryForDevice(builder.build(), 0L, System.currentTimeMillis());
                return new DataUsage(bucketQuerySummaryForDevice.getRxBytes(), bucketQuerySummaryForDevice.getTxBytes());
            }
        }
        return new DataUsage(0L, 0L);
    }

    protected long getDataUsage() {
        return getDetailedDataUsage().getTotalBytes();
    }

    private void stopDataConnectionTracker() {
        logd("deregister tracking data disconnection and cause");
        DataStallIntentReceiver dataStallIntentReceiver = this.mDataStallIntentReceiver;
        if (dataStallIntentReceiver != null) {
            this.mContext.unregisterReceiver(dataStallIntentReceiver);
            this.mDataStallIntentReceiver = null;
        }
        TelephonyListenerImpl telephonyListenerImpl = this.mTelephonyListener;
        if (telephonyListenerImpl != null) {
            telephonyListenerImpl.unregister();
            this.mTelephonyListener = null;
        }
    }

    private int updateAvgBandwidthForSession(int i, int i2) {
        return (int) Math.round(((double) i) / ((double) i2));
    }

    private void deregisterSatelliteDataNetworkCallback() {
        logd("unregister callbacks");
        stopDataConnectionTracker();
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mConnectivityManager = null;
        }
    }

    private void logSatelliteAppData() {
        String[] strArr = this.mSatelliteAppsPackageNameArray;
        if (strArr == null || strArr.length == 0) {
            Log.d(TAG, "No satellite app data to log.");
            return;
        }
        for (int i = 0; i < this.mSatelliteAppsPackageNameArray.length; i++) {
            if (i < this.mSatelliteAppsUidArray.length && i < this.mPerAppSatelliteDataConsumedBytesArray.length) {
                Log.d(TAG, "Package: " + this.mSatelliteAppsPackageNameArray[i] + ", UID: " + this.mSatelliteAppsUidArray[i] + ", Bytes: " + this.mPerAppSatelliteDataConsumedBytesArray[i]);
            } else {
                Log.w(TAG, "Mismatched array lengths at index: " + i + ". Skipping logging for this entry.");
                return;
            }
        }
    }

    private void updatePerAppDataConsumedMaptoArray(Map map) {
        if (map == null || map.isEmpty()) {
            Log.w(TAG, "No satellite data usage found. The app data usage map is null or empty.");
            return;
        }
        String[] strArr = (String[]) map.keySet().toArray(new String[0]);
        this.mSatelliteAppsPackageNameArray = strArr;
        this.mSatelliteAppsUidArray = Arrays.stream(strArr).mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats$$ExternalSyntheticLambda2
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return this.f$0.lambda$updatePerAppDataConsumedMaptoArray$1((String) obj);
            }
        }).toArray();
        this.mPerAppSatelliteDataConsumedBytesArray = map.values().stream().mapToLong(new ModemPowerMetrics$$ExternalSyntheticLambda0()).toArray();
        logSatelliteAppData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$updatePerAppDataConsumedMaptoArray$1(String str) {
        return ((Integer) this.mSatelliteAppUidMap.getOrDefault(str, -1)).intValue();
    }

    private Map computePerAppSatelliteDataUsageWithSessionLegacy(Map map) {
        long jLongValue;
        HashMap map2 = new HashMap();
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            Long l = (Long) entry.getValue();
            if (this.mPerAppDataUsageOnSessionStartMapLegacy.containsKey(str)) {
                jLongValue = l.longValue() - ((Long) this.mPerAppDataUsageOnSessionStartMapLegacy.get(str)).longValue();
            } else {
                jLongValue = l.longValue();
            }
            if (jLongValue > 0) {
                map2.put(str, Long.valueOf(jLongValue));
            }
        }
        return map2;
    }

    private Map findTopNPackagesWithMaxData(Map map) {
        if (map == null || map.isEmpty()) {
            return new HashMap();
        }
        LinkedList<Map.Entry> linkedList = new LinkedList(map.entrySet());
        Collections.sort(linkedList, new Comparator<Map.Entry<String, Long>>(this) { // from class: com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats.2
            @Override // java.util.Comparator
            public int compare(Map.Entry<String, Long> entry, Map.Entry<String, Long> entry2) {
                return entry2.getValue().compareTo(entry.getValue());
            }
        });
        HashMap map2 = new HashMap();
        int i = 0;
        for (Map.Entry entry : linkedList) {
            if (i >= 5) {
                break;
            }
            map2.put((String) entry.getKey(), (Long) entry.getValue());
            i++;
        }
        return map2;
    }

    public void onSessionEnd(int i, List<String> list) {
        int i2;
        int i3;
        logd("onSessionEnd:");
        onConnectionEnd();
        long dataUsage = getDataUsage();
        logd("update data consumed: " + dataUsage);
        if (dataUsage > 0) {
            long j = this.mDataUsageOnSessionStartBytes;
            if (dataUsage > j) {
                this.mSatelliteDataConsumedBytes = dataUsage - j;
            }
        }
        logd("satellite data consumed at session: " + this.mSatelliteDataConsumedBytes);
        Map<String, Long> perAppSatelliteDataUsage = getPerAppSatelliteDataUsage(list);
        if (!perAppSatelliteDataUsage.isEmpty()) {
            Map mapFindTopNPackagesWithMaxData = findTopNPackagesWithMaxData(computePerAppSatelliteDataUsageWithSessionLegacy(perAppSatelliteDataUsage));
            logd("top 5 satellite data usage apps:" + mapFindTopNPackagesWithMaxData);
            updatePerAppDataConsumedMaptoArray(mapFindTopNPackagesWithMaxData);
        } else {
            loge("per app satellite consumed array is empty");
        }
        int i4 = this.mSumOfDownlinkBandwidthKbps;
        if (i4 > 0 && (i3 = this.mCountOfDataConnections) > 0) {
            this.mAverageDownlinkBandwidthKbps = updateAvgBandwidthForSession(i4, i3);
        }
        int i5 = this.mSumOfUplinkBandwidthKbps;
        if (i5 > 0 && (i2 = this.mCountOfDataConnections) > 0) {
            this.mAverageUplinkBandwidthKbps = updateAvgBandwidthForSession(i5, i2);
        }
        if (this.mCurrentState == 2) {
            handleDisconnection();
            storeFailCause(65535);
        }
        reportMetrics(i);
        this.mIsNtnRoamingInHomeCountry = false;
        this.mSupportedSatelliteServices = new int[0];
        this.mServiceDataPolicy = 0;
        this.mSatelliteDataConsumedBytes = 0L;
        this.mTotalRxDataConsumedBytes = 0L;
        this.mTotalTxDataConsumedBytes = 0L;
        this.mSatelliteAppsPackageNameArray = null;
        Arrays.fill(this.mSatelliteAppsUidArray, 0);
        this.mPerAppSatelliteDataConsumedBytesArray = new long[]{0};
        this.mPerAppRxDataConsumedBytesArray = new long[]{0};
        this.mPerAppTxDataConsumedBytesArray = new long[]{0};
        this.mDataUsageOnSessionStartBytes = 0L;
        this.mTotalRxDataOnSessionStartBytes = 0L;
        this.mTotalTxDataOnSessionStartBytes = 0L;
        this.mPerAppDataUsageOnSessionStartMap.clear();
        this.mPerAppDataUsageOnSessionStartMapLegacy.clear();
        resetSatelliteDataState();
    }

    private Map updatePackageNameWithUids(List list) {
        HashMap map = new HashMap();
        try {
            if (this.mContext != null && list != null && !list.isEmpty()) {
                PackageManager packageManager = this.mContext.getPackageManager();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
                    try {
                        map.put(str, Integer.valueOf(packageManager.getApplicationInfo(str, 0).uid));
                    } catch (PackageManager.NameNotFoundException unused) {
                        loge("Package Manager found exception");
                    }
                }
            }
        } catch (Exception e) {
            loge("found exception on finding uids:" + e);
        }
        return map;
    }

    private String findKeysByValue(Map map, Integer num) {
        String str = null;
        if (map == null || map.isEmpty()) {
            logd("Found Packagename: null");
            return null;
        }
        for (Map.Entry entry : map.entrySet()) {
            if (((Integer) entry.getValue()).equals(num)) {
                str = (String) entry.getKey();
                logd("Found Packagename: " + str);
            }
        }
        return str;
    }

    private void updateDetailedDataUsageMap(NetworkStats networkStats, Map map) {
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            if (bucket.getUid() != -1 && this.mSatelliteAppUidMap.containsValue(Integer.valueOf(bucket.getUid()))) {
                String strFindKeysByValue = findKeysByValue(this.mSatelliteAppUidMap, Integer.valueOf(bucket.getUid()));
                long rxBytes = bucket.getRxBytes();
                long txBytes = bucket.getTxBytes();
                if (map.containsKey(strFindKeysByValue)) {
                    DataUsage dataUsage = (DataUsage) map.get(strFindKeysByValue);
                    rxBytes += dataUsage.getRxBytes();
                    txBytes += dataUsage.getTxBytes();
                }
                if (rxBytes + txBytes > 0) {
                    map.put(strFindKeysByValue, new DataUsage(rxBytes, txBytes));
                }
            }
        }
    }

    protected Map<String, DataUsage> getPerAppDetailedDataUsage(List<String> list) {
        HashMap map = new HashMap();
        logd("satellite app List: " + list);
        if (!list.isEmpty()) {
            this.mSatelliteAppUidMap = updatePackageNameWithUids(list);
            logd("satellite App Map: " + this.mSatelliteAppUidMap);
            if (!this.mSatelliteAppUidMap.isEmpty()) {
                NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.mContext.getSystemService(NetworkStatsManager.class);
                String subscriberId = this.mPhone.getSubscriberId();
                if (networkStatsManager != null && !TextUtils.isEmpty(subscriberId)) {
                    NetworkTemplate.Builder builder = new NetworkTemplate.Builder(1);
                    logd("subscriber id for data consumed: " + subscriberId);
                    try {
                        builder.setSubscriberIds(Set.of(subscriberId));
                        builder.setMeteredness(1);
                        updateDetailedDataUsageMap(networkStatsManager.querySummary(builder.build(), 0L, System.currentTimeMillis()), map);
                    } catch (SecurityException e) {
                        loge("querying networkstats met with approach:" + e);
                    }
                }
            }
            logd("NetworkStats per apps: " + map);
            return map;
        }
        loge("Satellite apps list is empty");
        return map;
    }

    protected Map<String, Long> getPerAppSatelliteDataUsage(List<String> list) {
        HashMap map = new HashMap();
        for (Map.Entry<String, DataUsage> entry : getPerAppDetailedDataUsage(list).entrySet()) {
            map.put(entry.getKey(), Long.valueOf(entry.getValue().getTotalBytes()));
        }
        return map;
    }

    private void resetSatelliteDataState() {
        deregisterSatelliteDataNetworkCallback();
        Arrays.fill(this.mLastFailCauses, 0);
        this.mFailCauseIndex = 0;
        this.mCurrentState = 0;
        this.mCountOfDataConnections = 0;
        this.mCountOfDataDisconnections = 0;
        this.mCountOfDataStalls = 0;
        this.mAverageUplinkBandwidthKbps = 0;
        this.mAverageDownlinkBandwidthKbps = 0;
        this.mSumOfDownlinkBandwidthKbps = 0;
        this.mSumOfUplinkBandwidthKbps = 0;
        this.mMinUplinkBandwidthKbps = Integer.MAX_VALUE;
        this.mMaxUplinkBandwidthKbps = 0;
        this.mMinDownlinkBandwidthKbps = Integer.MAX_VALUE;
        this.mMaxDownlinkBandwidthKbps = 0;
    }

    public void onConnectionEnd() {
        SatelliteConnectionTimes satelliteConnectionTimes = this.mSatelliteConnectionTimes;
        if (satelliteConnectionTimes != null) {
            satelliteConnectionTimes.setEndTime(getElapsedRealtime());
            this.mSatelliteConnectionTimesList.add(this.mSatelliteConnectionTimes);
            this.mSatelliteConnectionTimes = null;
            return;
        }
        loge("onConnectionEnd: mSatelliteConnectionTimes is null");
    }

    public void onSignalStrength(Phone phone) {
        CellSignalStrengthLte cellSignalStrengthLte = getCellSignalStrengthLte(phone);
        int rsrp = cellSignalStrengthLte.getRsrp();
        int rssnr = cellSignalStrengthLte.getRssnr();
        if (rsrp == Integer.MAX_VALUE) {
            logd("onSignalStrength: rsrp unavailable");
            return;
        }
        if (rssnr == Integer.MAX_VALUE) {
            logd("onSignalStrength: rssnr unavailable");
            return;
        }
        this.mRsrpList.add(Integer.valueOf(rsrp));
        this.mRssnrList.add(Integer.valueOf(rssnr));
        logd("onSignalStrength : rsrp=" + rsrp + ", rssnr=" + rssnr);
    }

    public void onIncomingSms(int i) {
        if (isNtnConnected()) {
            this.mCountOfIncomingSms++;
            logd("onIncomingSms: subId=" + i + ", count=" + this.mCountOfIncomingSms);
        }
    }

    public void onOutgoingSms(int i) {
        if (isNtnConnected()) {
            this.mCountOfOutgoingSms++;
            logd("onOutgoingSms: subId=" + i + ", count=" + this.mCountOfOutgoingSms);
        }
    }

    public void onMms(boolean z, long j) {
        if (isNtnConnected()) {
            if (z) {
                this.mIncomingMessageId = j;
                this.mCountOfIncomingMms++;
                logd("onMms: messageId=" + j + ", countOfIncomingMms=" + this.mCountOfIncomingMms);
                return;
            }
            if (this.mIncomingMessageId == j) {
                logd("onMms: NotifyResponse ignore it.");
                this.mIncomingMessageId = 0L;
            } else {
                this.mCountOfOutgoingMms++;
                logd("onMms: countOfOutgoingMms=" + this.mCountOfOutgoingMms);
            }
        }
    }

    private void reportMetrics(int i) {
        int iIntValue;
        int iIntValue2;
        int elapsedRealtimeInSec = this.mSessionStartTimeSec > 0 ? getElapsedRealtimeInSec() - this.mSessionStartTimeSec : 0;
        int numberOfSatelliteConnections = getNumberOfSatelliteConnections();
        List satelliteConnectionGapList = getSatelliteConnectionGapList(numberOfSatelliteConnections);
        if (satelliteConnectionGapList.isEmpty()) {
            iIntValue = 0;
            iIntValue2 = 0;
        } else {
            iIntValue = ((Integer) Collections.min(satelliteConnectionGapList)).intValue();
            iIntValue2 = ((Integer) Collections.max(satelliteConnectionGapList)).intValue();
        }
        boolean z = this.mSubscriptionManagerService.getActiveSubIdList(true).length > 1;
        updateFinalScreenOnTime();
        SatelliteStats.CarrierRoamingSatelliteSessionParams carrierRoamingSatelliteSessionParamsBuild = new SatelliteStats.CarrierRoamingSatelliteSessionParams.Builder().setCarrierId(this.mCarrierId).setSupportedConnectionMode(this.mSupportedConnectionMode).setPlmn(this.mPlmn).setSessionConnectionMode(this.mSessionConnectionMode).setIsNtnRoamingInHomeCountry(this.mIsNtnRoamingInHomeCountry).setTotalSatelliteModeTimeSec(elapsedRealtimeInSec).setNumberOfSatelliteConnections(numberOfSatelliteConnections).setAvgDurationOfSatelliteConnectionSec(getAvgDurationOfSatelliteConnection()).setSatelliteConnectionGapMinSec(iIntValue).setSatelliteConnectionGapAvgSec(getAvg(satelliteConnectionGapList)).setSatelliteConnectionGapMaxSec(iIntValue2).setRsrpAvg(getAvg(this.mRsrpList)).setRsrpMedian(getMedian(this.mRsrpList)).setRssnrAvg(getAvg(this.mRssnrList)).setRssnrMedian(getMedian(this.mRssnrList)).setCountOfIncomingSms(this.mCountOfIncomingSms).setCountOfOutgoingSms(this.mCountOfOutgoingSms).setCountOfIncomingMms(this.mCountOfIncomingMms).setCountOfOutgoingMms(this.mCountOfOutgoingMms).setSupportedSatelliteServices(this.mSupportedSatelliteServices).setServiceDataPolicy(this.mServiceDataPolicy).setSatelliteDataConsumedBytes(this.mSatelliteDataConsumedBytes).setTotalRxDataBytes(this.mTotalRxDataConsumedBytes).setTotalTxDataBytes(this.mTotalTxDataConsumedBytes).setIsMultiSim(z).setIsNbIotNtn(SatelliteServiceUtils.isNbIotNtn(i)).setCountOfDataConnections(this.mCountOfDataConnections).setLastFailCauses(this.mLastFailCauses).setCountOfDataDisconnections(this.mCountOfDataDisconnections).setCountOfDataStalls(this.mCountOfDataStalls).setAverageUplinkBandwidthKbps(this.mAverageUplinkBandwidthKbps).setAverageDownlinkBandwidthKbps(this.mAverageDownlinkBandwidthKbps).setMinimumUplinkBandwidthKbps(this.mMinUplinkBandwidthKbps).setMaximumUplinkBandwidthKbps(this.mMaxUplinkBandwidthKbps).setMinimumDownlinkBandwidthKbps(this.mMinDownlinkBandwidthKbps).setMaximumDownlinkBandwidthKbps(this.mMaxDownlinkBandwidthKbps).setSatelliteSupportedApps(this.mSatelliteAppsPackageNameArray).setSatelliteSupportedUids(this.mSatelliteAppsUidArray).setPerAppSatelliteDataConsumedBytes(this.mPerAppSatelliteDataConsumedBytesArray).setPerAppRxDataBytes(this.mPerAppRxDataConsumedBytesArray).setPerAppTxDataBytes(this.mPerAppTxDataConsumedBytesArray).setIsWifiEnabled(this.mIsWifiEnabled).setIsWifiConnected(this.mIsWifiConnected).setIsWfcEnabled(this.mIsWfcEnabled).setIsWfcRegistered(this.mIsWfcRegistered).setEligibilitySource(SatelliteServiceUtils.getSatelliteEligibilitySource(i)).setScreenOnTimeSec(this.mAccumulatedScreenOnTimeSec).setBatteryLevelDropPercent(Math.max(0, this.mStartBatteryPropertyCapacity - getBatteryPropertyCapacity())).setWasChargingDuringSession(this.mWasChargingDuringSession).setEnergyConsumedNwh(Math.max(0L, this.mStartBatteryPropertyEnergyCounter - getBatteryPropertyEnergyCounter())).setCountOfNonEmergencyDialerDialogDisplayed(this.mCountOfNonEmergencyDialerDialogDisplayed).setCountOfEmergencyDialerButtonDisplayed(this.mCountOfEmergencyDialerButtonDisplayed).setCountOfSatelliteNotificationDisplayed(this.mCountOfSatelliteNotificationDisplayed).build();
        SatelliteStats.getInstance().onCarrierRoamingSatelliteSessionMetrics(carrierRoamingSatelliteSessionParamsBuild);
        CarrierRoamingSatelliteControllerStats.getOrCreateInstance().addSessionDurationSec(i, elapsedRealtimeInSec);
        logd("Supported satellite services: " + Arrays.toString(this.mSupportedSatelliteServices));
        logd("last fail causes: " + Arrays.toString(this.mLastFailCauses));
        logd("reportMetrics: " + carrierRoamingSatelliteSessionParamsBuild);
        initializeParams();
    }

    private void initializeParams() {
        this.mCarrierId = -1;
        this.mSupportedConnectionMode = 0;
        this.mSessionConnectionMode = 0;
        this.mPlmn = SatelliteConstants.DEFAULT_PLMN;
        this.mIsNtnRoamingInHomeCountry = false;
        this.mCountOfIncomingSms = 0;
        this.mCountOfOutgoingSms = 0;
        this.mCountOfIncomingMms = 0;
        this.mCountOfOutgoingMms = 0;
        this.mIncomingMessageId = 0L;
        this.mSessionStartTimeSec = 0;
        this.mSatelliteConnectionTimes = null;
        this.mSatelliteConnectionTimesList = new ArrayList();
        this.mRsrpList = new ArrayList();
        this.mRssnrList = new ArrayList();
        this.mIsWifiEnabled = false;
        this.mIsWifiConnected = false;
        this.mIsWfcEnabled = false;
        this.mIsWfcRegistered = false;
        this.mAccumulatedScreenOnTimeSec = 0;
        this.mScreenOnStartTimeMillis = 0L;
        this.mWasChargingDuringSession = false;
        this.mStartBatteryPropertyCapacity = 0;
        this.mStartBatteryPropertyEnergyCounter = 0L;
        this.mCountOfNonEmergencyDialerDialogDisplayed = 0;
        this.mCountOfEmergencyDialerButtonDisplayed = 0;
        this.mCountOfSatelliteNotificationDisplayed = 0;
        this.mTotalRxDataConsumedBytes = 0L;
        this.mTotalTxDataConsumedBytes = 0L;
        this.mTotalRxDataOnSessionStartBytes = 0L;
        this.mTotalTxDataOnSessionStartBytes = 0L;
        this.mPerAppDataUsageOnSessionStartMap = new HashMap();
        this.mPerAppDataUsageOnSessionStartMapLegacy = new HashMap();
        this.mPerAppRxDataConsumedBytesArray = new long[]{0};
        this.mPerAppTxDataConsumedBytesArray = new long[]{0};
        logd("initializeParams");
    }

    private CellSignalStrengthLte getCellSignalStrengthLte(Phone phone) {
        for (CellSignalStrength cellSignalStrength : phone.getSignalStrength().getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                return (CellSignalStrengthLte) cellSignalStrength;
            }
        }
        return new CellSignalStrengthLte();
    }

    private int getNumberOfSatelliteConnections() {
        return this.mSatelliteConnectionTimesList.size();
    }

    private int getAvgDurationOfSatelliteConnection() {
        if (this.mSatelliteConnectionTimesList.isEmpty()) {
            return 0;
        }
        OptionalDouble optionalDoubleAverage = this.mSatelliteConnectionTimesList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((CarrierRoamingSatelliteSessionStats.SatelliteConnectionTimes) obj).isValid();
            }
        }).mapToLong(new ToLongFunction() { // from class: com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats$$ExternalSyntheticLambda1
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return ((CarrierRoamingSatelliteSessionStats.SatelliteConnectionTimes) obj).getDuration();
            }
        }).average();
        return (int) (optionalDoubleAverage.isPresent() ? optionalDoubleAverage.getAsDouble() / 1000.0d : 0.0d);
    }

    private List getSatelliteConnectionGapList(int i) {
        if (this.mSatelliteConnectionTimesList.size() < 2) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 1; i2 < this.mSatelliteConnectionTimesList.size(); i2++) {
            SatelliteConnectionTimes satelliteConnectionTimes = (SatelliteConnectionTimes) this.mSatelliteConnectionTimesList.get(i2 - 1);
            SatelliteConnectionTimes satelliteConnectionTimes2 = (SatelliteConnectionTimes) this.mSatelliteConnectionTimesList.get(i2);
            if (satelliteConnectionTimes.getEndTime() > 0 && satelliteConnectionTimes2.getStartTime() > satelliteConnectionTimes.getEndTime()) {
                arrayList.add(Integer.valueOf((int) ((satelliteConnectionTimes2.getStartTime() - satelliteConnectionTimes.getEndTime()) / 1000)));
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

    private int getMedian(List list) {
        if (list.isEmpty()) {
            return 0;
        }
        int size = list.size();
        if (size == 1) {
            return ((Integer) list.get(0)).intValue();
        }
        Collections.sort(list);
        if (size % 2 == 0) {
            int i = size / 2;
            return (((Integer) list.get(i - 1)).intValue() + ((Integer) list.get(i)).intValue()) / 2;
        }
        return ((Integer) list.get(size / 2)).intValue();
    }

    private int getElapsedRealtimeInSec() {
        return (int) (getElapsedRealtime() / 1000);
    }

    protected long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    private boolean isNtnConnected() {
        return this.mSessionStartTimeSec != 0;
    }

    private void updateNtnRoamingInHomeCountry(Phone phone) {
        int subId = phone.getSubId();
        ServiceState serviceState = phone.getServiceState();
        if (serviceState == null) {
            logd("ServiceState is null");
            return;
        }
        String registeredPlmn = "";
        for (NetworkRegistrationInfo networkRegistrationInfo : serviceState.getNetworkRegistrationInfoList()) {
            if (networkRegistrationInfo.isNonTerrestrialNetwork()) {
                registeredPlmn = networkRegistrationInfo.getRegisteredPlmn();
            }
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(subId);
        if (subscriptionInfoInternal == null) {
            logd("SubscriptionInfoInternal is null");
            return;
        }
        String strCountryCodeForMcc = MccTable.countryCodeForMcc(subscriptionInfoInternal.getMcc());
        this.mIsNtnRoamingInHomeCountry = true;
        if (registeredPlmn != null && registeredPlmn.length() >= 3) {
            if (strCountryCodeForMcc.equalsIgnoreCase(MccTable.countryCodeForMcc(registeredPlmn.substring(0, 3)))) {
                this.mIsNtnRoamingInHomeCountry = true;
            } else {
                this.mIsNtnRoamingInHomeCountry = false;
            }
        }
        logd("updateNtnRoamingInHomeCountry: mIsNtnRoamingInHomeCountry=" + this.mIsNtnRoamingInHomeCountry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class SatelliteConnectionTimes {
        private long mEndTime = 0;
        private final long mStartTime;

        SatelliteConnectionTimes(long j) {
            this.mStartTime = j;
        }

        public void setEndTime(long j) {
            this.mEndTime = j;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public long getEndTime() {
            return this.mEndTime;
        }

        public long getDuration() {
            if (isValid()) {
                return this.mEndTime - this.mStartTime;
            }
            return 0L;
        }

        public boolean isValid() {
            long j = this.mEndTime;
            long j2 = this.mStartTime;
            return j > j2 && j2 > 0;
        }
    }

    public void onWifiConnectivityStateChanged(boolean z) {
        if (z) {
            this.mIsWifiConnected = true;
        }
        logd("onWifiConnectivityStateChanged: isWifiConnected=" + z + ", mIsWifiConnected=" + this.mIsWifiConnected);
    }

    public void setWasChargingDuringSession() {
        this.mWasChargingDuringSession = true;
    }

    public void onScreenStateChanged(boolean z) {
        if (z && this.mScreenOnStartTimeMillis == 0) {
            this.mScreenOnStartTimeMillis = getElapsedRealtime();
        } else {
            if (z || this.mScreenOnStartTimeMillis <= 0) {
                return;
            }
            this.mAccumulatedScreenOnTimeSec += (int) ((getElapsedRealtime() - this.mScreenOnStartTimeMillis) / 1000);
            this.mScreenOnStartTimeMillis = 0L;
        }
    }

    private void updateFinalScreenOnTime() {
        if (this.mScreenOnStartTimeMillis > 0) {
            this.mAccumulatedScreenOnTimeSec += (int) ((getElapsedRealtime() - this.mScreenOnStartTimeMillis) / 1000);
            this.mScreenOnStartTimeMillis = 0L;
        }
    }

    public void onNonEmergencyDialerDialogDisplayed() {
        this.mCountOfNonEmergencyDialerDialogDisplayed++;
        logd("onNonEmergencyDialerDialogDisplayed: count=" + this.mCountOfNonEmergencyDialerDialogDisplayed);
    }

    public void onEmergencyDialerButtonDisplayed() {
        this.mCountOfEmergencyDialerButtonDisplayed++;
        logd("onEmergencyDialerButtonDisplayed: count=" + this.mCountOfEmergencyDialerButtonDisplayed);
    }

    public void onSatelliteNotificationDisplayed() {
        this.mCountOfSatelliteNotificationDisplayed++;
        logd("onSatelliteNotificationDisplayed: count=" + this.mCountOfSatelliteNotificationDisplayed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d(TAG, str);
    }

    private void loge(String str) {
        Log.e(TAG, str);
    }
}

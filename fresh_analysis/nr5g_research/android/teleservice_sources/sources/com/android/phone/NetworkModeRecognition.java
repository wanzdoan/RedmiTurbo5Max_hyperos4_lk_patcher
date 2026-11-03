package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeRecognition {
    private static NetworkModeRecognition sInstance;
    private Context mContext;
    ContentObserver mDataEnabledObserver;
    private NetworkModeHandler mHandler;
    public int mHikingModeCityIndex;
    private boolean mIsWifiConnected;
    private ContentResolver mResolver;
    private SettingsObserver mSettingsObserver;
    private HandlerThread mWorkThread;
    private static List mHikingModeCityWhiteList = new ArrayList();
    private static LinkedList cellQueue = new LinkedList();
    private boolean VDBG = false;
    private long mSubwayInTime = 0;
    private long mHikingInTime = -1;
    private long mLastHikingHeightChgTime = -1;
    private boolean mIsHighSpeedTrain = false;
    private boolean mIsAirPlaneMode = false;
    private boolean mIsMobileConnected = false;
    private boolean mUserDataEnabled = false;
    private int mDdsPhoneId = -1;
    private Set mMovelikeCidSet = new HashSet();
    private String mCellQueuePlmn = "";
    private String mLastCid = "NOT_LTE_NR";
    private long mLastEnterTime = -1;
    private boolean mIsMovelikeState = false;
    private int mMovelikeLifeCycle = 0;
    private TelephonyCallBack mTelephonyCallBack = null;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.NetworkModeRecognition.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            NetworkModeRecognition.this.log("receive action".concat(action));
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                NetworkModeRecognition.this.mHandler.obtainMessage(4, intent).sendToTarget();
                return;
            }
            if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
                NetworkModeRecognition.this.mHandler.obtainMessage(5).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                NetworkModeRecognition.this.mHandler.obtainMessage(6).sendToTarget();
                return;
            }
            if ("com.android.phone.intent.action.CLOUD_HIKING_MODE_CITY_LIST_DONE".equals(action)) {
                NetworkModeRecognition.this.log("onReceive ACTION_CLOUD_HIKING_MODE_CITY_LIST_DONE");
                NetworkModeRecognition.this.initHikingModeCityWhiteList();
            } else if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                NetworkModeRecognition.this.mHandler.obtainMessage(9, intent).sendToTarget();
            } else if ("NetworkModeRecognition.intent.debug".equals(action)) {
                NetworkModeRecognition.this.mHandler.sendEmptyMessage(100);
            }
        }
    };
    private LocationListener mLocationListener = new LocationListener() { // from class: com.android.phone.NetworkModeRecognition.4
        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location != null) {
                NetworkModeRecognition.this.log("onLocationChanged:, provider=" + location.getProvider() + ", accuracy=" + location.getAccuracy() + ", time=" + location.getTime());
                NetworkModeRecognition.this.getAddressFromLocation(location);
            }
        }
    };

    public boolean isAirplaneInFlight(int i) {
        return (i & 48) == 16;
    }

    public boolean isAirplaneInLand(int i) {
        return (i & 48) == 32;
    }

    public boolean isHikingIn(int i) {
        return (i & 12) == 4;
    }

    public boolean isHikingOut(int i) {
        return (i & 12) == 8;
    }

    public boolean isHikingStateUpdate(int i) {
        return (i & 12) != 0;
    }

    public boolean isSubwayStart(int i) {
        return (i & 3) == 1;
    }

    public boolean isSubwayStartStopStateUpdate(int i) {
        return (i & 3) != 0;
    }

    public boolean isSubwayStop(int i) {
        return (i & 3) == 2;
    }

    public static synchronized NetworkModeRecognition getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NetworkModeRecognition();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NetworkModeRecognition() {
        this.mDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.NetworkModeRecognition.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsMobileDataSettingOn = NetworkModeRecognition.this.isMobileDataSettingOn();
                NetworkModeRecognition.this.log("data state:" + zIsMobileDataSettingOn + ", mUserDataEnabled=" + NetworkModeRecognition.this.mUserDataEnabled);
                if (NetworkModeRecognition.this.mUserDataEnabled != zIsMobileDataSettingOn) {
                    NetworkModeRecognition.this.mUserDataEnabled = zIsMobileDataSettingOn;
                }
            }
        };
        log("NetworkModeRecognition init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mResolver = phoneGlobals.getContentResolver();
        HandlerThread handlerThread = new HandlerThread("NetworkModeRecognition", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        NetworkModeHandler networkModeHandler = new NetworkModeHandler(this.mWorkThread.getLooper());
        this.mHandler = networkModeHandler;
        networkModeHandler.sendEmptyMessage(1);
    }

    class NetworkModeHandler extends Handler {
        NetworkModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NetworkModeRecognition.this.logv("handleMessage changed=" + message.what);
            int i = message.what;
            if (i == 21) {
                NetworkModeRecognition networkModeRecognition = NetworkModeRecognition.this;
                networkModeRecognition.processAirplaneStateChange(networkModeRecognition.getAirplaneStatus());
                return;
            }
            if (i != 100) {
                switch (i) {
                    case 1:
                        NetworkModeRecognition.this.init();
                        break;
                    case 2:
                        NetworkModeRecognition.this.processHikingStateChanged(NetworkModeRecognition.this.getSensorHikingState());
                        break;
                    case 3:
                        NetworkModeRecognition.this.processSubwayStartStopStateChanged(NetworkModeRecognition.this.getSensorSubwayStartStopState());
                        break;
                    case 4:
                        NetworkModeRecognition.this.processNetworkStateChanged((Intent) message.obj);
                        break;
                    case 5:
                        NetworkModeRecognition networkModeRecognition2 = NetworkModeRecognition.this;
                        networkModeRecognition2.mDdsPhoneId = networkModeRecognition2.getDefaultDataSlotId();
                        break;
                    case 6:
                        NetworkModeRecognition networkModeRecognition3 = NetworkModeRecognition.this;
                        networkModeRecognition3.mIsAirPlaneMode = networkModeRecognition3.isAirPlaneModeOn();
                        break;
                    case 7:
                        NetworkModeRecognition.this.processSubwayStateChanged(NetworkModeRecognition.this.getSensorSubwayState());
                        break;
                    case 8:
                        NetworkModeRecognition.this.processHikingHeightChanged();
                        break;
                    case 9:
                        NetworkModeRecognition.this.mIsHighSpeedTrain = ((Intent) message.obj).getBooleanExtra("mIsHstMode", false);
                        break;
                    case 10:
                        NetworkModeRecognition.this.processCellLocationChanged(message.arg1);
                        break;
                    case 11:
                        NetworkModeRecognition.this.resetMovelikeState();
                        break;
                }
                return;
            }
            NetworkModeRecognition.this.VDBG = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mUserDataEnabled = isMobileDataSettingOn();
        this.mDdsPhoneId = getDefaultDataSlotId();
        this.mIsAirPlaneMode = isAirPlaneModeOn();
        registerSettingsCallbacks();
        initHikingModeCityWhiteList();
        registerTelephonyCallBack();
        registerBroadcastReceiver();
        regMobileDataObserve();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initHikingModeCityWhiteList() {
        if (mHikingModeCityWhiteList.size() > 0) {
            mHikingModeCityWhiteList.clear();
        }
        List hikingModeCityWhiteListByCloud = FiveGManagerBase.getHikingModeCityWhiteListByCloud(this.mContext);
        mHikingModeCityWhiteList = hikingModeCityWhiteListByCloud;
        if (hikingModeCityWhiteListByCloud.size() != 0) {
            log("mHikingModeCityWhiteList=" + mHikingModeCityWhiteList);
        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_HIKING_MODE_CITY_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("NetworkModeRecognition.intent.debug");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void registerSettingsCallbacks() {
        SettingsObserver settingsObserver = new SettingsObserver(this.mContext, this.mHandler);
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe(Settings.Global.getUriFor("sensor_state_subway"), 7);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("sensor_state_subway_start_stop"), 3);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("sensor_state_hiking"), 2);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("sensor_state_hiking_height"), 8);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("sensor_airplane_state"), 21);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
        if (state == state2 || state == NetworkInfo.State.DISCONNECTED) {
            int type = networkInfo.getType();
            boolean z = state == state2;
            log("networkType=" + type + ",connected=" + z);
            if (type == 0) {
                if (this.mIsMobileConnected == z) {
                    return;
                }
                this.mIsMobileConnected = z;
                log("mobile=" + this.mIsMobileConnected);
                return;
            }
            if (type != 1 || this.mIsWifiConnected == z) {
                return;
            }
            this.mIsWifiConnected = z;
            log("wifi connected=" + this.mIsWifiConnected);
            if (this.mIsWifiConnected) {
                int networkModeState = getNetworkModeState();
                if (isHikingIn(networkModeState)) {
                    log("exit hiking mode");
                    putSettingValueForNetworkMode(-13, 8);
                }
                this.mSubwayInTime = -1L;
                if (isSubwayStartStopStateUpdate(networkModeState)) {
                    putSettingValueForNetworkMode(-4, 0);
                }
            }
        }
    }

    private void regMobileDataObserve() {
        this.mResolver.registerContentObserver(Settings.Global.getUriFor(getMobileDataSettingName()), false, this.mDataEnabledObserver);
    }

    private String getMobileDataSettingName() {
        return TelephonyManager.getDefault().getPhoneCount() > 1 ? "mobile_data0" : "mobile_data";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataSettingOn() {
        return Settings.Global.getInt(this.mResolver, getMobileDataSettingName(), 0) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mResolver, "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStateChanged(int i) {
        log("processSubwayStateChanged, subwayState=" + i);
        if (i == 1) {
            this.mSubwayInTime = SystemClock.elapsedRealtime();
            return;
        }
        this.mSubwayInTime = -1L;
        if (isSubwayStartStopStateUpdate(getNetworkModeState())) {
            putSettingValueForNetworkMode(-4, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStartStopStateChanged(int i) {
        log("processSubwayStartStopStateChanged, subwayStartStopState=" + i);
        if (!isNormalCaseForSubwayStartStop()) {
            putSettingValueForNetworkMode(-4, 0);
            return;
        }
        if (i == 0) {
            putSettingValueForNetworkMode(-4, 0);
        } else if (i == 1) {
            putSettingValueForNetworkMode(-4, 1);
        } else {
            if (i != 2) {
                return;
            }
            putSettingValueForNetworkMode(-4, 2);
        }
    }

    private boolean isNormalCaseForSubwayStartStop() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mSubwayInTime;
        boolean z = j != -1 && jElapsedRealtime > 0 && jElapsedRealtime - j <= 7200000;
        boolean z2 = z && !this.mIsWifiConnected && this.mUserDataEnabled && this.mIsMobileConnected;
        if (!z2) {
            log("isTimeSpan=" + z + ", mIsWifiConnected=" + this.mIsWifiConnected + ", mUserDataEnabled=" + this.mUserDataEnabled + ", mIsMobileConnected=" + this.mIsMobileConnected);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:14:0x0035  */
    public void processHikingHeightChanged() {
        if (!isHikingIn(getNetworkModeState())) {
            log("processHikingHeightChanged: not hiking mode");
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastHikingHeightChgTime;
        if (j == -1 || jElapsedRealtime - j <= 3600000) {
            long j2 = this.mHikingInTime;
            if (j2 != -1 && jElapsedRealtime - j2 > 21600000) {
                log("exit hiking mode");
                putSettingValueForNetworkMode(-13, 8);
            }
        } else {
            log("exit hiking mode");
            putSettingValueForNetworkMode(-13, 8);
        }
        this.mLastHikingHeightChgTime = jElapsedRealtime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHikingStateChanged(int i) {
        int networkModeState = getNetworkModeState();
        log("processHikingStateChanged, hikingState=" + i + ", networkModeState=" + networkModeState);
        if (i != 0) {
            if (i == 1) {
                if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && isHikingNetworkCharacter() && !isHikingIn(networkModeState)) {
                    if (this.VDBG) {
                        log("ignore location while debug is true, enter hiking mode");
                        putSettingValueForNetworkMode(-13, 4);
                        return;
                    } else {
                        startGetCurrentLocation();
                        return;
                    }
                }
                return;
            }
            if (i != 2) {
                return;
            }
        }
        if (isHikingIn(networkModeState)) {
            log("exit hiking mode");
            putSettingValueForNetworkMode(-13, 8);
        }
    }

    private boolean isHikingNetworkCharacter() {
        return !this.mIsWifiConnected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSensorSubwayState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_subway", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSensorSubwayStartStopState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_subway_start_stop", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSensorHikingState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_hiking", 0);
    }

    private int getNetworkModeState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAirplaneStatus() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_airplane_state", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putSettingValueForNetworkMode(int i, int i2) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "network_mode_state", (i & getNetworkModeState()) | i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCityWhiteList(String str) {
        if (str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < mHikingModeCityWhiteList.size(); i++) {
            if (((String) mHikingModeCityWhiteList.get(i)).equals(str)) {
                this.mHikingModeCityIndex = i;
                return true;
            }
        }
        return false;
    }

    private void startGetCurrentLocation() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService("location");
        if (locationManager != null) {
            String str = "network";
            if (!locationManager.isProviderEnabled("network")) {
                str = "passive";
                if (!locationManager.isProviderEnabled("passive")) {
                    str = "gps";
                }
            }
            log("provider : ".concat(str));
            Executor mainExecutor = this.mContext.getMainExecutor();
            LocationListener locationListener = this.mLocationListener;
            Objects.requireNonNull(locationListener);
            locationManager.getCurrentLocation(str, null, mainExecutor, new NetworkModeRecognition$$ExternalSyntheticLambda0(locationListener));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAddressFromLocation(Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        logv("latitude=" + latitude + ", longitude=" + longitude);
        new Thread() { // from class: com.android.phone.NetworkModeRecognition.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                List<Address> fromLocation = null;
                try {
                    fromLocation = new Geocoder(NetworkModeRecognition.this.mContext, Locale.ENGLISH).getFromLocation(latitude, longitude, 1);
                    NetworkModeRecognition.this.log("listSize=" + fromLocation.size());
                } catch (Exception e) {
                    NetworkModeRecognition.this.log("Get Location Error");
                    e.printStackTrace();
                }
                if (fromLocation == null || fromLocation.isEmpty()) {
                    return;
                }
                synchronized (fromLocation) {
                    try {
                        for (Address address : fromLocation) {
                            if (address != null) {
                                String address2 = NetworkModeRecognition.this.formatAddress(address.getLocality());
                                NetworkModeRecognition.this.logv("getAddressFromLocation city=" + address2);
                                if (NetworkModeRecognition.this.isCityWhiteList(address2)) {
                                    NetworkModeRecognition.this.log("enter hiking mode");
                                    NetworkModeRecognition.this.mHikingInTime = SystemClock.elapsedRealtime();
                                    NetworkModeRecognition.this.mLastHikingHeightChgTime = -1L;
                                    NetworkModeRecognition.this.putSettingValueForNetworkMode(-13, 4);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatAddress(String str) {
        if (str != null) {
            return str.contains(" ") ? str.substring(0, str.indexOf(" ")) : str;
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i) {
        log("processCellLocationChanged, phoneId=" + i);
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("invalid phone info");
            return;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return;
        }
        if (cellQueuePlmnChanged()) {
            log("plmn changed, clear queue");
            clearCellQueue();
            resetMovelikeState();
        }
        String lteNrCid = getLteNrCid(currentCellIdentity);
        if (TextUtils.isEmpty(lteNrCid)) {
            return;
        }
        if ("NOT_LTE_NR".equals(lteNrCid) && !"NOT_LTE_NR".equals(this.mLastCid)) {
            startTimerForNoLteNr();
        } else if (!"NOT_LTE_NR".equals(lteNrCid)) {
            stopTimerForNoLteNr();
        }
        updateCellQueue(lteNrCid, SystemClock.elapsedRealtime() / 1000);
    }

    private void startTimerForNoLteNr() {
        stopTimerForNoLteNr();
        log("startTimerForNoLteNr");
        this.mHandler.sendEmptyMessageDelayed(11, 300000L);
    }

    private void stopTimerForNoLteNr() {
        if (this.mHandler.hasMessages(11)) {
            log("stopTimerForNoLteNr");
            this.mHandler.removeMessages(11);
        }
    }

    private boolean cellQueuePlmnChanged() {
        String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(this.mDdsPhoneId));
        if (TextUtils.equals(operatorNumeric, this.mCellQueuePlmn)) {
            return false;
        }
        this.mCellQueuePlmn = operatorNumeric;
        return true;
    }

    private synchronized void clearCellQueue() {
        cellQueue.clear();
        this.mMovelikeCidSet.clear();
    }

    private void updateCellQueue(String str, long j) {
        logv("cid=" + str + ", mLastCid=" + this.mLastCid);
        if (this.mLastEnterTime != -1 && !"NOT_LTE_NR".equals(this.mLastCid)) {
            if (!this.mMovelikeCidSet.contains(this.mLastCid)) {
                offerCellQueue(new CellDurationInfo(this, this.mLastCid, j - this.mLastEnterTime));
                updateMovelikeState(getMovelikeLifeCycle());
            } else {
                updateMovelikeState(-10);
            }
        }
        this.mLastEnterTime = j;
        this.mLastCid = str;
    }

    private synchronized void offerCellQueue(CellDurationInfo cellDurationInfo) {
        if (cellDurationInfo == null) {
            return;
        }
        try {
            if (getCellQueueSize() >= 10) {
                this.mMovelikeCidSet.remove(((CellDurationInfo) cellQueue.poll()).cid);
            }
            cellQueue.offer(cellDurationInfo);
            this.mMovelikeCidSet.add(cellDurationInfo.cid);
        } catch (Throwable th) {
            throw th;
        }
    }

    private int getCellQueueSize() {
        return cellQueue.size();
    }

    private int getMovelikeLifeCycle() {
        int cellQueueSize = getCellQueueSize();
        logv("size=" + cellQueueSize);
        if (cellQueueSize >= 5) {
            long j = 0;
            long jMax = 0;
            for (int i = cellQueueSize - 1; i >= 0; i--) {
                long duration = ((CellDurationInfo) cellQueue.get(i)).getDuration();
                jMax = Math.max(duration, jMax);
                j += duration;
            }
            long j2 = j / ((long) cellQueueSize);
            logv("AverDura=" + j2 + ", maxDura=" + jMax);
            if (jMax >= 600000) {
                return -60;
            }
            if (j2 <= 75 && jMax <= 240) {
                return 10;
            }
        }
        return -5;
    }

    private void updateMovelikeState(int i) {
        logv("lifeCycle=" + i + ", OldLifeCycle=" + this.mMovelikeLifeCycle);
        int i2 = this.mMovelikeLifeCycle + i;
        this.mMovelikeLifeCycle = i2;
        if (i2 > 60) {
            this.mMovelikeLifeCycle = 60;
        } else if (i2 < 0) {
            this.mMovelikeLifeCycle = 0;
        }
        int i3 = this.mMovelikeLifeCycle;
        boolean z = this.mIsMovelikeState;
        if (i3 > 0) {
            if (z) {
                return;
            }
            this.mIsMovelikeState = true;
            sendMovelikeState(true);
            return;
        }
        if (z) {
            this.mIsMovelikeState = false;
            sendMovelikeState(false);
        }
    }

    private void sendMovelikeState(boolean z) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.MOVELIKE_STATE_CHANGED");
        intent.putExtra("isMovelikeState", z);
        this.mContext.sendBroadcast(intent);
        localLog("movelikeState", "state=" + z);
    }

    private String getLteNrCid(CellIdentity cellIdentity) {
        long nci;
        if (cellIdentity instanceof CellIdentityLte) {
            nci = ((CellIdentityLte) cellIdentity).getCi();
        } else {
            if (cellIdentity instanceof CellIdentityNr) {
                nci = ((CellIdentityNr) cellIdentity).getNci();
            }
            return "NOT_LTE_NR";
        }
        if (nci != Long.MAX_VALUE && nci != 2147483647L) {
            return String.valueOf(nci);
        }
        return "NOT_LTE_NR";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetMovelikeState() {
        if (this.mIsMovelikeState) {
            this.mIsMovelikeState = false;
            sendMovelikeState(false);
        }
        this.mMovelikeLifeCycle = 0;
        this.mLastEnterTime = -1L;
        this.mLastCid = "NOT_LTE_NR";
        stopTimerForNoLteNr();
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("registerTelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (i != NetworkModeRecognition.this.mDdsPhoneId || cellLocation == null || cellLocation.isEmpty()) {
                return;
            }
            NetworkModeRecognition.this.mHandler.obtainMessage(10, i, 0).sendToTarget();
        }
    }

    public class CellDurationInfo {
        private String cid;
        private long duration;

        public CellDurationInfo(NetworkModeRecognition networkModeRecognition, String str, long j) {
            this.cid = str;
            this.duration = j;
        }

        public long getDuration() {
            return this.duration;
        }

        public String toString() {
            return "CellDurationInfo{cid=" + this.cid + ", duration=" + this.duration + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirplaneStateChange(int i) {
        if (i == 1) {
            airplaneTakeOff();
        } else if (i == 2) {
            airplaneLanding();
        }
    }

    private void airplaneTakeOff() {
        if (isAirplaneInFlight(getNetworkModeState())) {
            return;
        }
        putSettingValueForNetworkMode(-49, 16);
    }

    private void airplaneLanding() {
        if (isAirplaneInLand(getNetworkModeState())) {
            return;
        }
        putSettingValueForNetworkMode(-49, 32);
    }

    public boolean isInSubwayState() {
        return getSensorSubwayState() == 1;
    }

    public boolean isHighSpeedTrain() {
        return this.mIsHighSpeedTrain;
    }

    public boolean isMovelikeState() {
        return this.mIsMovelikeState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logv(String str) {
        if (this.VDBG) {
            Log.d("NetworkModeRecognition", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("NetworkModeRecognition", str);
    }

    private void localLog(String str, String str2) {
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            NetworkMetricsController.localLog("NetworkModeRecognition", str, str2);
        }
    }
}

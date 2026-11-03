package com.android.services.telephony.relay.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.MiuiSignalStrengthStub;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.GsonUtil;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayServerController {
    private static final String EASY_TETHER_AUTO_CONNECT_SETTING = "pref_key_easy_tether_auto_connect_state";
    private static final int EVENT_P2P_RSSI_DELAY_TIME = 5000;
    private static final int EVENT_P2P_RSSI_RETRY = 0;
    private static final int EVENT_WIFI_SCANNING_DELAY_TIME = 60000;
    private static final int EVENT_WIFI_SCANNING_RETRY = 1;
    private static final String KEY_P2P_RSSI = "currentRssiDbm";
    public static final int NETWORK_TYPE_CDMA = 1;
    public static final int NETWORK_TYPE_GSM = 2;
    public static final int NETWORK_TYPE_LTE = 5;
    public static final int NETWORK_TYPE_NR = 6;
    public static final int NETWORK_TYPE_TDSCDMA = 4;
    public static final int NETWORK_TYPE_WCDMA = 3;
    private static final int RSSI_INVALID = -1;
    private static final int SETTINGS_STATE_DISABLE = 0;
    private static final int SETTINGS_STATE_ENABLE = 1;
    private static final int SIGNAL_STRENGTH_FIVE = 5;
    public static final String SIM_SLOT = "sim_slot";
    private static final String TAG = "NetworkRelayServerController";
    public static final String TRAFFIC_MONTH_USED = "month_used";
    public static final String TRAFFIC_TOTAL = "total_limit";
    public static final String TRAFFIC_TYPE = "traffic_type";
    public static final String TRAFFIC_UNIT = "traffic_unit";
    public static final String TRAFFIC_VALUE = "traffic_value";
    private static final int VERSION_2 = 2;
    private static WifiP2pManager.Channel mChannel = null;
    private static NetworkRelayInfo mNetworkRelayInfo = null;
    private static boolean mWifiScanning = false;
    private static NetworkRelayServerController sNetworkRelayServerController;
    private Context mContext;
    private int mCurrentListenSubId = -1;
    private BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.network.NetworkRelayServerController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                Rlog.d(NetworkRelayServerController.TAG, "default data sub has been changed..");
                int subId = MiuiPhoneUtils.getDataPhone().getSubId();
                if (!SubscriptionManager.isValidSubscriptionId(NetworkRelayServerController.this.mCurrentListenSubId) || NetworkRelayServerController.this.mCurrentListenSubId == subId) {
                    return;
                }
                NetworkRelayServerController networkRelayServerController = NetworkRelayServerController.this;
                networkRelayServerController.enableNetworkListener(false, networkRelayServerController.mCurrentListenSubId);
                NetworkRelayServerController.this.enableNetworkListener(true);
                return;
            }
            if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra("resultsUpdated", false);
                Rlog.d(NetworkRelayServerController.TAG, "WiFi scan result = " + booleanExtra);
                if (booleanExtra) {
                    try {
                        if (NetworkRelayServerController.this.mWifiManager != null) {
                            for (ScanResult scanResult : NetworkRelayServerController.this.mWifiManager.getScanResults()) {
                                for (Map.Entry entry : NetworkRelayServerController.deviceBssidMapObject.entrySet()) {
                                    String str = (String) entry.getKey();
                                    String str2 = (String) entry.getValue();
                                    if (!TextUtils.isEmpty(str2) && str2.equals(scanResult.BSSID)) {
                                        int wiFiLevel = SmartNetworkRelayScore.getWiFiLevel(scanResult.level);
                                        if (wiFiLevel == NetworkRelayServerController.mNetworkRelayInfo.mWiFiSignalLevel) {
                                            return;
                                        }
                                        NetworkRelayServerController.mNetworkRelayInfo.mWiFiSignalLevel = wiFiLevel;
                                        NetworkRelayServerController.mNetworkRelayInfo.mGoBssid = scanResult.BSSID;
                                        NetworkRelayServerController.mNetworkRelayInfo.mGoDeviceId = str;
                                        Rlog.d(NetworkRelayServerController.TAG, "Not p2p device rssi = " + NetworkRelayServerController.mNetworkRelayInfo.mWiFiSignalLevel + ", mGoBssid = ***, mGoDeviceId = " + NetworkRelayServerController.mNetworkRelayInfo.mGoDeviceId);
                                        NetworkRelayServerController.this.notifyNetworkInfo(2);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Rlog.i(NetworkRelayServerController.TAG, "SCAN_RESULTS_AVAILABLE_ACTION error = " + e);
                    }
                }
            }
        }
    };
    private final MobileTelephonyCallback mTelephonyCallback;
    private TelephonyManager mTelephonyMgr;
    private NetworkRelayServerHandler mThreadHandler;
    private WifiManager mWifiManager;
    private WifiP2pManager mWifiP2pManager;
    public static HashMap deviceBssidMapObject = new HashMap();
    private static final Uri TRAFFIC_TOTAL_URI = Uri.parse("content://com.miui.networkassistant.provider/datausage_status_detailed");
    private static final Uri TRAFFIC_URI = Uri.parse("content://com.miui.networkassistant.provider/bill_traffic_inquire");

    public static int getSignalType(int i) {
        if (i == 20 || i == 30) {
            return 6;
        }
        switch (i) {
            case 1:
            case 2:
                return 2;
            case 3:
            case 8:
            case 9:
            case 10:
            case 11:
                return 3;
            case 4:
            case 5:
            case 6:
            case 7:
            case 12:
                return 1;
            default:
                switch (i) {
                    case 14:
                        return 1;
                    case 15:
                        return 3;
                    case 16:
                        return 2;
                    case 17:
                        return 4;
                    default:
                        return 5;
                }
        }
    }

    public static synchronized NetworkRelayServerController getInstance(Context context) {
        try {
            if (sNetworkRelayServerController == null) {
                sNetworkRelayServerController = new NetworkRelayServerController(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sNetworkRelayServerController;
    }

    private NetworkRelayServerController(Context context) {
        Rlog.d(TAG, "new NetworkRelayServerController");
        this.mContext = context;
        this.mTelephonyCallback = new MobileTelephonyCallback();
        mNetworkRelayInfo = new NetworkRelayInfo();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            intentFilter.addAction("android.telephony.smartNetwork.test");
        }
        context.registerReceiver(this.mDefaultDataChangedReceiver, intentFilter, 2);
        initNetworkRelayServerHandler();
    }

    private void initNetworkRelayServerHandler() {
        if (this.mThreadHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, 10);
            handlerThread.start();
            this.mThreadHandler = new NetworkRelayServerHandler(handlerThread.getLooper());
        }
    }

    class NetworkRelayServerHandler extends Handler {
        public NetworkRelayServerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i != 1) {
                    return;
                }
                NetworkRelayServerController.mWifiScanning = false;
            } else if (NetworkRelayManager.hasCreateServer) {
                NetworkRelayServerController.this.registerP2pWifiRssi();
            }
        }
    }

    public void registerP2pWifiRssi() {
        WifiP2pManager.Channel channel;
        NetworkRelayServerHandler networkRelayServerHandler;
        Context context = this.mContext;
        if (context != null && this.mWifiP2pManager == null) {
            this.mWifiP2pManager = (WifiP2pManager) context.getSystemService("wifip2p");
        }
        Context context2 = this.mContext;
        if (context2 != null && (networkRelayServerHandler = this.mThreadHandler) != null) {
            initP2pChannel(context2, networkRelayServerHandler.getLooper());
        }
        WifiP2pManager wifiP2pManager = this.mWifiP2pManager;
        if (wifiP2pManager == null || (channel = mChannel) == null) {
            return;
        }
        try {
            wifiP2pManager.requestLinkInfo(channel, new WifiP2pManager.LinkInfoListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayServerController.1
                public void onLinkInfoAvailable(Bundle bundle) {
                    int iIntValue = bundle != null ? ((Integer) bundle.get(NetworkRelayServerController.KEY_P2P_RSSI)).intValue() : -1;
                    if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && NetworkRelayServerController.this.mContext != null) {
                    }
                    Rlog.d(NetworkRelayServerController.TAG, "registerP2pWifiRssi p2pRssi = " + iIntValue);
                    NetworkRelayServerController.this.notifyP2pGcDeviceRssiOrDataUsage(iIntValue);
                    if (NetworkRelayServerController.this.mThreadHandler != null) {
                        NetworkRelayServerController.this.mThreadHandler.sendMessageDelayed(NetworkRelayServerController.this.mThreadHandler.obtainMessage(0), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    }
                }
            });
        } catch (Exception e) {
            Rlog.i(TAG, "registerP2pWifiRssi error = " + e);
        }
    }

    public void unRegisterP2pWifiRssi() {
        this.mWifiP2pManager = null;
        mChannel = null;
        NetworkRelayServerHandler networkRelayServerHandler = this.mThreadHandler;
        if (networkRelayServerHandler != null) {
            networkRelayServerHandler.removeMessages(0);
        }
    }

    private void initP2pChannel(Context context, Looper looper) {
        if (mChannel != null) {
            return;
        }
        WifiP2pManager wifiP2pManager = this.mWifiP2pManager;
        if (wifiP2pManager != null) {
            mChannel = wifiP2pManager.initialize(context, looper, null);
        }
        if (mChannel == null) {
            Rlog.e(TAG, "Failed to set up connection with wifi p2p service");
        }
    }

    public void enableWiFiRssi() {
        Context context = this.mContext;
        if (context != null && this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        }
        if (this.mWifiManager != null && !mWifiScanning) {
            Rlog.d(TAG, "enableWiFiRssi start WiFi scan");
            this.mWifiManager.startScan();
            mWifiScanning = true;
            NetworkRelayServerHandler networkRelayServerHandler = this.mThreadHandler;
            if (networkRelayServerHandler != null) {
                networkRelayServerHandler.sendMessageDelayed(networkRelayServerHandler.obtainMessage(1), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                return;
            }
            return;
        }
        Rlog.d(TAG, "enableWiFiRssi scanning ignore!");
    }

    public void enableNetworkListener(boolean z) {
        enableNetworkListener(z, -1);
    }

    public void enableNetworkListener(boolean z, int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            i = MiuiPhoneUtils.getDataPhone().getSubId();
        }
        Rlog.d(TAG, "enable:" + z + " subId=" + i);
        if (z && this.mCurrentListenSubId == i) {
            Rlog.d(TAG, "already in liesten, return..");
            notifyNetworkInfo(2);
            return;
        }
        TelephonyManager telephonyManager = new TelephonyManager(this.mContext, i);
        this.mTelephonyMgr = telephonyManager;
        if (z) {
            telephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallback);
            this.mCurrentListenSubId = i;
        } else {
            telephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mCurrentListenSubId = -1;
        }
    }

    class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.DisplayInfoListener, TelephonyCallback.PreciseDataConnectionStateListener {
        private MobileTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.SignalStrengthsListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            boolean z = NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel != signalStrength.getMiuiLevel();
            Rlog.d(NetworkRelayServerController.TAG, "old Signallevel = " + NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel + ", new Signallevel =" + signalStrength.getMiuiLevel());
            NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel = signalStrength.getMiuiLevel();
            if (NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel == MiuiSignalStrengthStub.MIUI_NUM_SIGNAL_STRENGTH_BARS) {
                NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel = 5;
            }
            NetworkRelayServerController.mNetworkRelayInfo.mSignalType = NetworkRelayServerController.getSignalType(NetworkRelayServerController.mNetworkRelayInfo.mNetworkType);
            Rlog.d(NetworkRelayServerController.TAG, "onSignalStrengthsChanged: level:" + NetworkRelayServerController.mNetworkRelayInfo.mSignalLevel + "SignalType=" + NetworkRelayServerController.mNetworkRelayInfo.mSignalType);
            if (z) {
                Rlog.d(NetworkRelayServerController.TAG, "isValidChanged = " + z);
                NetworkRelayServerController.this.notifyNetworkInfo(2);
            }
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            NetworkRelayServerController.mNetworkRelayInfo.mVoiceRegState = serviceState.getVoiceRegState();
            NetworkRelayServerController.mNetworkRelayInfo.mDataRegState = serviceState.getDataRegState();
            NetworkRelayServerController.mNetworkRelayInfo.mNetworkType = serviceState.getDataNetworkType();
            if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                NetworkRelayServerController.mNetworkRelayInfo.mIsDataRoaming = serviceState.getDataRoaming();
                NetworkRelayServerController.mNetworkRelayInfo.mSignalType = NetworkRelayServerController.getSignalType(NetworkRelayServerController.mNetworkRelayInfo.mNetworkType);
                Rlog.d(NetworkRelayServerController.TAG, "onServiceStateChanged:VoiceRegState = " + NetworkRelayServerController.mNetworkRelayInfo.mVoiceRegState + "; DataRegState = " + NetworkRelayServerController.mNetworkRelayInfo.mDataRegState + "; NetworkType = " + NetworkRelayServerController.mNetworkRelayInfo.mNetworkType + "; DataRoaming = " + NetworkRelayServerController.mNetworkRelayInfo.mIsDataRoaming);
            } else {
                Rlog.d(NetworkRelayServerController.TAG, "onServiceStateChanged: VoiceRegState" + NetworkRelayServerController.mNetworkRelayInfo.mVoiceRegState + " DataRegState=" + NetworkRelayServerController.mNetworkRelayInfo.mDataRegState + " NetworkType=" + NetworkRelayServerController.mNetworkRelayInfo.mNetworkType);
            }
            NetworkRelayServerController.this.notifyNetworkInfo(2);
        }

        @Override // android.telephony.TelephonyCallback.PreciseDataConnectionStateListener
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            int state = preciseDataConnectionState.getState();
            int networkType = preciseDataConnectionState.getNetworkType();
            if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                Rlog.d(NetworkRelayServerController.TAG, "onPreciseDataConnectionStateChanged:ConnectState = " + state + "; NetworkType  = " + networkType);
                if (state == 2) {
                    NetworkRelayServerController.mNetworkRelayInfo.mIsValid = true;
                } else {
                    NetworkRelayServerController.mNetworkRelayInfo.mIsValid = false;
                }
                NetworkRelayServerController.mNetworkRelayInfo.mSignalType = NetworkRelayServerController.getSignalType(networkType);
            } else {
                Rlog.d(NetworkRelayServerController.TAG, "onPreciseDataConnectionStateChanged: " + state);
            }
            NetworkRelayServerController.mNetworkRelayInfo.mDataConectionState = state;
            NetworkRelayServerController.mNetworkRelayInfo.mDataConectionNetworkType = networkType;
            NetworkRelayServerController.this.notifyNetworkInfo(2);
        }

        @Override // android.telephony.TelephonyCallback.DisplayInfoListener
        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            Rlog.d(NetworkRelayServerController.TAG, "onDisplayInfoChanged:" + telephonyDisplayInfo);
            NetworkRelayServerController.mNetworkRelayInfo.mNetwork = telephonyDisplayInfo.getNetworkType();
            NetworkRelayServerController.mNetworkRelayInfo.mOverrideNetwork = telephonyDisplayInfo.getOverrideNetworkType();
            NetworkRelayServerController.this.notifyNetworkInfo(2);
        }
    }

    public void notifyNetworkInfo(int i) {
        NetworkRelayInfo networkRelayInfo = mNetworkRelayInfo;
        networkRelayInfo.mMethod = i;
        networkRelayInfo.mSimSate = 1;
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            NetworkRelayInfo networkRelayInfo2 = mNetworkRelayInfo;
            networkRelayInfo2.mVersion = 2;
            networkRelayInfo2.mAutoShareNetworkEnabled = getAutoShareNetworkEnabled();
            int phoneId = MiuiPhoneUtils.getDataPhone().getPhoneId();
            Cursor cursorQuery = null;
            try {
                try {
                    cursorQuery = this.mContext.getContentResolver().query(TRAFFIC_TOTAL_URI, null, null, null, null);
                    if (cursorQuery != null && cursorQuery.moveToFirst()) {
                        do {
                            if (phoneId == cursorQuery.getInt(cursorQuery.getColumnIndex(SIM_SLOT))) {
                                mNetworkRelayInfo.mTotalTrafficLimit = cursorQuery.getLong(cursorQuery.getColumnIndex(TRAFFIC_TOTAL));
                                mNetworkRelayInfo.mUsedMonthTraffic = cursorQuery.getLong(cursorQuery.getColumnIndex(TRAFFIC_MONTH_USED));
                            }
                        } while (cursorQuery.moveToNext());
                    } else {
                        Rlog.i(TAG, "queryDataUsageInfo failed, total cursor is empty.");
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                } catch (Exception e) {
                    Rlog.e(TAG, "Failed to notifyNetworkInfo.", e);
                    if (0 != 0) {
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    cursorQuery.close();
                    throw th;
                }
                throw th;
            }
        }
        String json = GsonUtil.toJson(mNetworkRelayInfo);
        Rlog.d(TAG, "notifyNetworkInfo info:" + json);
        NetworkRelayChannelServer.getInstance(this.mContext).sendNetworkRequest(Packet.fromBytes(json.getBytes(StandardCharsets.UTF_8)));
    }

    public void queryDataUsageInfo(Context context) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int phoneId = MiuiPhoneUtils.getDataPhone().getPhoneId();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mContext.getContentResolver().query(TRAFFIC_URI, null, null, null, null);
                if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                    Rlog.i(TAG, "queryDataUsageInfo failed, cursor is empty.");
                } else {
                    do {
                        if (phoneId == cursorQuery.getInt(cursorQuery.getColumnIndex(SIM_SLOT))) {
                            mNetworkRelayInfo.mTrafficName = cursorQuery.getInt(cursorQuery.getColumnIndex(TRAFFIC_TYPE)) + "";
                            mNetworkRelayInfo.mTrafficValue = cursorQuery.getString(cursorQuery.getColumnIndex(TRAFFIC_VALUE));
                            mNetworkRelayInfo.mTrafficUnit = cursorQuery.getInt(cursorQuery.getColumnIndex(TRAFFIC_UNIT)) + "";
                        }
                    } while (cursorQuery.moveToNext());
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Exception e) {
                Rlog.e(TAG, "Failed to queryDataUsageInfo.", e);
                if (0 != 0) {
                }
            }
            mNetworkRelayInfo.mDurationTime = SystemClock.elapsedRealtime() - jElapsedRealtime;
            notifyNetworkInfo(1);
        } catch (Throwable th) {
            if (0 != 0) {
                cursorQuery.close();
                throw th;
            }
            throw th;
        }
    }

    public void receiveRelayMessage(byte[] bArr) {
        receiveRelayMessage(bArr, "");
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0067  */
    /* JADX WARN: Code duplicated, block: B:23:0x0070  */
    /* JADX WARN: Code duplicated, block: B:26:0x007c  */
    /* JADX WARN: Code duplicated, block: B:41:? A[RETURN, SYNTHETIC] */
    public void notifyP2pGcDeviceRssiOrDataUsage(int i) {
        long j;
        long j2;
        NetworkRelayInfo networkRelayInfo;
        NetworkRelayServerController networkRelayServerController;
        int wiFiLevel = SmartNetworkRelayScore.getWiFiLevel(i);
        int phoneId = MiuiPhoneUtils.getDataPhone().getPhoneId();
        Cursor cursor = null;
        try {
            try {
                Cursor cursorQuery = this.mContext.getContentResolver().query(TRAFFIC_TOTAL_URI, null, null, null, null);
                if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                    Rlog.i(TAG, "notifyP2pGcDeviceRssiOrDataUsage failed, total cursor is empty.");
                } else {
                    while (true) {
                        if (phoneId == cursorQuery.getInt(cursorQuery.getColumnIndex(SIM_SLOT))) {
                            j2 = cursorQuery.getLong(cursorQuery.getColumnIndex(TRAFFIC_TOTAL));
                            j = cursorQuery.getLong(cursorQuery.getColumnIndex(TRAFFIC_MONTH_USED));
                            break;
                        }
                        if (!cursorQuery.moveToNext()) {
                        }
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    networkRelayInfo = mNetworkRelayInfo;
                    if (wiFiLevel == networkRelayInfo.mWiFiSignalLevel) {
                        networkRelayServerController = this;
                        if (!networkRelayServerController.isChangedDateUsage(networkRelayInfo.mTotalTrafficLimit, networkRelayInfo.mUsedMonthTraffic, j2, j)) {
                            return;
                        }
                    } else {
                        networkRelayServerController = this;
                    }
                    NetworkRelayInfo networkRelayInfo2 = mNetworkRelayInfo;
                    networkRelayInfo2.mTotalTrafficLimit = j2;
                    networkRelayInfo2.mUsedMonthTraffic = j;
                    networkRelayInfo2.mWiFiSignalLevel = wiFiLevel;
                    Rlog.d(TAG, "P2p device rssi = " + mNetworkRelayInfo.mWiFiSignalLevel + "; totalTrafficLimit = " + mNetworkRelayInfo.mTotalTrafficLimit + "; usedMonthTraffic = " + mNetworkRelayInfo.mUsedMonthTraffic);
                    networkRelayServerController.notifyNetworkInfo(2);
                }
                j2 = 0;
                j = 0;
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                networkRelayInfo = mNetworkRelayInfo;
                if (wiFiLevel == networkRelayInfo.mWiFiSignalLevel) {
                    networkRelayServerController = this;
                    if (!networkRelayServerController.isChangedDateUsage(networkRelayInfo.mTotalTrafficLimit, networkRelayInfo.mUsedMonthTraffic, j2, j)) {
                        return;
                    }
                } else {
                    networkRelayServerController = this;
                }
                NetworkRelayInfo networkRelayInfo3 = mNetworkRelayInfo;
                networkRelayInfo3.mTotalTrafficLimit = j2;
                networkRelayInfo3.mUsedMonthTraffic = j;
                networkRelayInfo3.mWiFiSignalLevel = wiFiLevel;
                Rlog.d(TAG, "P2p device rssi = " + mNetworkRelayInfo.mWiFiSignalLevel + "; totalTrafficLimit = " + mNetworkRelayInfo.mTotalTrafficLimit + "; usedMonthTraffic = " + mNetworkRelayInfo.mUsedMonthTraffic);
                networkRelayServerController.notifyNetworkInfo(2);
            } catch (Exception e) {
                Rlog.e(TAG, "Failed to notifyP2pGcDeviceRssiOrDataUsage.", e);
                if (0 != 0) {
                    cursor.close();
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    private boolean isChangedDateUsage(long j, long j2, long j3, long j4) {
        if (SmartNetworkRelayScore.transferTrafficGb(j, j2) <= 1.0d || SmartNetworkRelayScore.transferTrafficGb(j3, j4) <= 1.0d) {
            return SmartNetworkRelayScore.transferTrafficGb(j, j2) >= 1.0d || SmartNetworkRelayScore.transferTrafficGb(j3, j4) >= 1.0d;
        }
        return false;
    }

    public void receiveRelayMessage(byte[] bArr, String str) {
        if (bArr == null) {
            Rlog.d(TAG, "received data is null from client...");
            return;
        }
        NetworkRelayInfo networkRelayInfo = (NetworkRelayInfo) GsonUtil.fromJson(new String(bArr), NetworkRelayInfo.class);
        Rlog.d(TAG, "receiveRelayMessage:" + networkRelayInfo);
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport() && !TextUtils.isEmpty(str)) {
            if (!deviceBssidMapObject.containsKey(str)) {
                deviceBssidMapObject.put(str, networkRelayInfo.mGoBssid);
            } else {
                deviceBssidMapObject.remove(str);
                deviceBssidMapObject.put(str, networkRelayInfo.mGoBssid);
            }
        }
        if (networkRelayInfo == null) {
            Rlog.d(TAG, "network info is null from client...");
            return;
        }
        int i = networkRelayInfo.mMethod;
        if (i == 1) {
            queryDataUsageInfo(this.mContext);
        } else {
            if (i == 2) {
                notifyNetworkInfo(2);
                return;
            }
            Rlog.d(TAG, "invalid method:" + networkRelayInfo.mMethod);
        }
    }

    private boolean getAutoShareNetworkEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), EASY_TETHER_AUTO_CONNECT_SETTING, 0) == 1;
    }
}

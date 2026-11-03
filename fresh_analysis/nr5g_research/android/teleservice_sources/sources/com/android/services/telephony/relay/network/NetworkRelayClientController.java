package com.android.services.telephony.relay.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.wifi.MiuiWifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.data.ApnSetting;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.services.telephony.relay.phonecall.message.GsonUtil;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayClientController {
    private static final String ACTION_REQUEST_DATA_USAGE_INFO = "com.android.systemui.fsgesture";
    private static final int EVENT_SHOW_DEFAULT_NETWORK_INFO = 1;
    private static final int EVENT_WIFI_STATE_CHANGED = 2;
    private static final String FAKE_SIM_CARRIER_NAME = "fake sim";
    private static final String FAKE_SIM_ICCID = "123456789";
    private static final int FAKE_SIM_SLOTID = 0;
    private static final String GO_BSSID = "miui_p2p_group_mac_addr";
    private static final String NETWORK_RELAY_DATA_USAGE_INFO = "network_relay_data_usage_info";
    public static final int P2P_STATE_FOR_TETHERED_CELLUAR = 3;
    private static final int SIGNAL_LEVEL_FIVE = 5;
    private static final String TAG = "NetworkRelayClientController";
    private static final int TIME_DELAYED_SHOW_DEFAULT_NETWORK_INFO = 300;
    public static int mDataState;
    private static NetworkRelayClientController sNetworkRelayClientController;
    private Context mContext;
    private boolean mIsWifiConnected;
    private NetworkRelayTrafficStats mNetworkRelayTrafficStats;
    private SubscriptionManager mSubscriptionManager;
    private SubscriptionManagerService mSubscriptionManagerService;
    private TelephonyManager mTelephonyMgr;
    private TelephonyRegistryManager mTelephonyRegistryMgr;
    private NetworkRelayInfo mNetworkRelayInfo = null;
    private NetworkRelayInfo mCurrentNetworkRelayInfo = null;
    private CellSignalStrengthCdma mCdma = new CellSignalStrengthCdma(-93, -132, -89, -125, 5);
    private CellSignalStrengthGsm mGsm = new CellSignalStrengthGsm(-79, 2, 5);
    private CellSignalStrengthWcdma mWcdma = new CellSignalStrengthWcdma(-94, 4, MiuiSynergySdk.SEND_APP_FAIL_REMOTE_BUSY_MIRROR, -5);
    private CellSignalStrengthTdscdma mTdscdma = new CellSignalStrengthTdscdma(-95, 2, MiuiSynergySdk.SEND_APP_FAIL_REMOTE_BUSY_MIPLAY);
    private CellSignalStrengthLte mLte = new CellSignalStrengthLte(-85, -91, -6, -10, 1, 12, 1);
    private CellSignalStrengthNr mNr = new CellSignalStrengthNr(-139, -20, -23, -44, -20, -23);
    private int mCurrentFakeSimSubId = -1;
    private long mRequestTime = 0;
    private boolean mHasRelaySimCard = false;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.services.telephony.relay.network.NetworkRelayClientController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                NetworkRelayClientController.this.displayRelayNetworkInfo();
                return;
            }
            if (i == 2) {
                Intent intent = (Intent) message.obj;
                NetworkInfo networkInfo = intent != null ? (NetworkInfo) intent.getParcelableExtra("networkInfo") : null;
                NetworkRelayClientController.this.mIsWifiConnected = networkInfo != null && networkInfo.isConnected();
                Rlog.d(NetworkRelayClientController.TAG, "wifi state=" + NetworkRelayClientController.this.mIsWifiConnected);
                NetworkRelayClientController.this.notifyDataConnectionIfNeed(true);
                return;
            }
            Rlog.d(NetworkRelayClientController.TAG, "invalid msg:" + message.what);
        }
    };
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.network.NetworkRelayClientController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent != null ? intent.getAction() : "";
            boolean booleanExtra = intent != null ? intent.getBooleanExtra("isEnter", false) : false;
            Rlog.d(NetworkRelayClientController.TAG, "action:" + action + ", isEnter=" + booleanExtra + ", p2pConnected=" + NetworkRelayManager.p2pConnected);
            if (NetworkRelayManager.p2pConnected && booleanExtra && action.equals(NetworkRelayClientController.ACTION_REQUEST_DATA_USAGE_INFO)) {
                NetworkRelayClientController.this.requestDataUsage();
            } else if (action.equals("android.net.wifi.STATE_CHANGE")) {
                NetworkRelayClientController.this.mHandler.obtainMessage(2, intent).sendToTarget();
            }
        }
    };

    public static synchronized NetworkRelayClientController getInstance(Context context) {
        try {
            if (sNetworkRelayClientController == null) {
                sNetworkRelayClientController = new NetworkRelayClientController(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sNetworkRelayClientController;
    }

    private NetworkRelayClientController(Context context) {
        this.mIsWifiConnected = false;
        Rlog.d(TAG, "new NetworkRelayClientController");
        this.mContext = context;
        this.mSubscriptionManager = SubscriptionManager.from(context);
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        this.mNetworkRelayTrafficStats = new NetworkRelayTrafficStats(this.mContext);
        this.mTelephonyRegistryMgr = (TelephonyRegistryManager) this.mContext.getSystemService("telephony_registry");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REQUEST_DATA_USAGE_INFO);
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        this.mIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
    }

    private boolean isP2PStateWithCelluar() {
        Rlog.d(TAG, "NetworkRelayManager.p2pConnected=" + NetworkRelayManager.p2pConnected);
        HashMap map = NetworkRelayManager.p2pConnectedObject;
        if (map == null) {
            Rlog.d(TAG, "NetworkRelayManager.p2pConnectedObject is null..");
            return false;
        }
        Iterator it = map.entrySet().iterator();
        if (it.hasNext()) {
            Integer num = (Integer) ((Map.Entry) it.next()).getValue();
            Rlog.d(TAG, "p2pState:" + num);
            if (num.intValue() == 3) {
                return true;
            }
        }
        return false;
    }

    public void updateUIDisplay() {
        notifyServiceStateIfNeed(true);
        notifyDisplayInfoIfNeed(true);
        notifyDataConnectionIfNeed(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDataConnectionIfNeed(boolean z) {
        if (this.mNetworkRelayInfo == null) {
            return;
        }
        if (isDataConnectionChanged() || z) {
            Rlog.d(TAG, "data state has been changed... mIsWifiConnected=" + this.mIsWifiConnected);
            if (!this.mIsWifiConnected && isP2PStateWithCelluar()) {
                TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryMgr;
                int i = this.mCurrentFakeSimSubId;
                NetworkRelayInfo networkRelayInfo = this.mNetworkRelayInfo;
                telephonyRegistryManager.notifyDataConnectionForSubscriber(0, i, createPreciseDataConnectionState(networkRelayInfo.mDataConectionState, networkRelayInfo.mDataConectionNetworkType));
                mDataState = this.mNetworkRelayInfo.mDataConectionState;
            } else {
                this.mTelephonyRegistryMgr.notifyDataConnectionForSubscriber(0, this.mCurrentFakeSimSubId, createPreciseDataConnectionState(0, this.mNetworkRelayInfo.mDataConectionNetworkType));
                mDataState = 0;
            }
            this.mNetworkRelayTrafficStats.handleTrafficStatsPollConditionChanged();
        }
    }

    private void notifyServiceStateIfNeed(boolean z) {
        if (this.mNetworkRelayInfo == null) {
            return;
        }
        Rlog.d(TAG, "isForceNotify = " + z);
        if (isServiceChanged() || z) {
            Rlog.d(TAG, "service has been changed..");
            boolean zIsP2PStateWithCelluar = isP2PStateWithCelluar();
            TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryMgr;
            if (zIsP2PStateWithCelluar) {
                int i = this.mCurrentFakeSimSubId;
                NetworkRelayInfo networkRelayInfo = this.mNetworkRelayInfo;
                telephonyRegistryManager.notifyServiceStateChanged(0, i, createService(networkRelayInfo.mVoiceRegState, networkRelayInfo.mDataRegState, networkRelayInfo.mNetworkType));
            } else {
                int i2 = this.mCurrentFakeSimSubId;
                NetworkRelayInfo networkRelayInfo2 = this.mNetworkRelayInfo;
                telephonyRegistryManager.notifyServiceStateChanged(0, i2, createService(networkRelayInfo2.mVoiceRegState, networkRelayInfo2.mDataRegState, 0));
            }
        }
    }

    private void notifyDisplayInfoIfNeed(boolean z) {
        if (this.mNetworkRelayInfo == null) {
            return;
        }
        Rlog.d(TAG, "isForceNotify = " + z);
        if (isDisplayInfoChanged() || z) {
            Rlog.d(TAG, "displayInfo has been changed...");
            boolean zIsP2PStateWithCelluar = isP2PStateWithCelluar();
            TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryMgr;
            if (zIsP2PStateWithCelluar) {
                int i = this.mCurrentFakeSimSubId;
                NetworkRelayInfo networkRelayInfo = this.mNetworkRelayInfo;
                telephonyRegistryManager.notifyDisplayInfoChanged(0, i, createTelephonyDisplayInfo(networkRelayInfo.mNetwork, networkRelayInfo.mOverrideNetwork));
                return;
            }
            telephonyRegistryManager.notifyDisplayInfoChanged(0, this.mCurrentFakeSimSubId, createTelephonyDisplayInfo(0, 0));
        }
    }

    public NetworkRelayInfo getNetworkRelayInfo(byte[] bArr) {
        if (bArr == null) {
            Rlog.d(TAG, "received data is null");
            return null;
        }
        this.mNetworkRelayInfo = (NetworkRelayInfo) GsonUtil.fromJson(new String(bArr), NetworkRelayInfo.class);
        Rlog.d(TAG, "receiveRelayMessage:" + this.mNetworkRelayInfo);
        return this.mNetworkRelayInfo;
    }

    public void receiveRelayMessage(byte[] bArr) {
        if (bArr == null || !this.mHasRelaySimCard) {
            StringBuilder sb = new StringBuilder("received data is null or no relay sim:");
            sb.append(!this.mHasRelaySimCard);
            Rlog.d(TAG, sb.toString());
            return;
        }
        this.mNetworkRelayInfo = (NetworkRelayInfo) GsonUtil.fromJson(new String(bArr), NetworkRelayInfo.class);
        Rlog.d(TAG, "receiveRelayMessage:" + this.mNetworkRelayInfo);
        NetworkRelayInfo networkRelayInfo = this.mNetworkRelayInfo;
        if (networkRelayInfo == null) {
            Rlog.d(TAG, "network info is null from server...");
            return;
        }
        int i = networkRelayInfo.mMethod;
        if (i != 1) {
            if (i == 2) {
                displayRelayNetworkInfo();
                this.mCurrentNetworkRelayInfo = this.mNetworkRelayInfo;
                return;
            } else {
                Rlog.d(TAG, "invalid method:" + this.mNetworkRelayInfo.mMethod);
                return;
            }
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(NetworkRelayServerController.TRAFFIC_TYPE, this.mNetworkRelayInfo.mTrafficName);
            jSONObject.put(NetworkRelayServerController.TRAFFIC_VALUE, this.mNetworkRelayInfo.mTrafficValue);
            jSONObject.put(NetworkRelayServerController.TRAFFIC_UNIT, this.mNetworkRelayInfo.mTrafficUnit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Rlog.d(TAG, "data usage is received:" + jSONObject);
        Settings.System.putString(this.mContext.getContentResolver(), NETWORK_RELAY_DATA_USAGE_INFO, jSONObject.toString());
        long jElapsedRealtime = ((SystemClock.elapsedRealtime() - this.mRequestTime) - this.mNetworkRelayInfo.mDurationTime) / 2;
        Rlog.d(TAG, "request delay time:" + jElapsedRealtime);
        NetworkRelayStats.getInstance().recordRelayDelay(jElapsedRealtime);
        this.mNetworkRelayInfo.mDurationTime = 0L;
        this.mRequestTime = 0L;
    }

    public void displayRelayNetworkInfo() {
        if (this.mNetworkRelayInfo == null) {
            Rlog.d(TAG, "nothing for displaying");
            return;
        }
        Rlog.d(TAG, "displayRelayNetworkInfo:" + this.mNetworkRelayInfo);
        notifyServiceStateIfNeed(false);
        if (isSignalChanged()) {
            Rlog.d(TAG, "signal has been changed..");
            TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryMgr;
            int i = this.mCurrentFakeSimSubId;
            NetworkRelayInfo networkRelayInfo = this.mNetworkRelayInfo;
            telephonyRegistryManager.notifySignalStrengthChanged(0, i, createSignalStrength(networkRelayInfo.mSignalLevel, networkRelayInfo.mSignalType));
        }
        notifyDataConnectionIfNeed(false);
        notifyDisplayInfoIfNeed(false);
    }

    private boolean isServiceChanged() {
        NetworkRelayInfo networkRelayInfo = this.mCurrentNetworkRelayInfo;
        if (networkRelayInfo == null) {
            return true;
        }
        NetworkRelayInfo networkRelayInfo2 = this.mNetworkRelayInfo;
        return (networkRelayInfo2.mDataRegState == networkRelayInfo.mDataRegState && networkRelayInfo2.mVoiceRegState == networkRelayInfo.mVoiceRegState && networkRelayInfo2.mNetworkType == networkRelayInfo.mNetworkType) ? false : true;
    }

    private boolean isDataConnectionChanged() {
        NetworkRelayInfo networkRelayInfo = this.mCurrentNetworkRelayInfo;
        if (networkRelayInfo == null) {
            return true;
        }
        NetworkRelayInfo networkRelayInfo2 = this.mNetworkRelayInfo;
        return (networkRelayInfo2.mDataConectionState == networkRelayInfo.mDataConectionState && networkRelayInfo2.mDataConectionNetworkType == networkRelayInfo.mDataConectionNetworkType) ? false : true;
    }

    private boolean isSignalChanged() {
        NetworkRelayInfo networkRelayInfo = this.mCurrentNetworkRelayInfo;
        if (networkRelayInfo == null) {
            return true;
        }
        NetworkRelayInfo networkRelayInfo2 = this.mNetworkRelayInfo;
        return (networkRelayInfo2.mSignalLevel == networkRelayInfo.mSignalLevel && networkRelayInfo2.mSignalType == networkRelayInfo.mSignalType) ? false : true;
    }

    private boolean isDisplayInfoChanged() {
        NetworkRelayInfo networkRelayInfo = this.mCurrentNetworkRelayInfo;
        if (networkRelayInfo == null) {
            return true;
        }
        NetworkRelayInfo networkRelayInfo2 = this.mNetworkRelayInfo;
        return (networkRelayInfo2.mNetwork == networkRelayInfo.mNetwork && networkRelayInfo2.mOverrideNetwork == networkRelayInfo.mOverrideNetwork) ? false : true;
    }

    public void activeRelaySim(boolean z, boolean z2) {
        Rlog.d(TAG, "activeRelaySim active:" + z);
        this.mHasRelaySimCard = z;
        SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
        if (z) {
            subscriptionManagerService.removeSubInfo(FAKE_SIM_ICCID, 0);
            this.mSubscriptionManagerService.addSubInfo(FAKE_SIM_ICCID, FAKE_SIM_CARRIER_NAME, 0, 0);
            SubscriptionInfo activeSubscriptionInfoForIcc = this.mSubscriptionManager.getActiveSubscriptionInfoForIcc(FAKE_SIM_ICCID);
            Rlog.d(TAG, "activeRelaySim subinfo:" + activeSubscriptionInfoForIcc);
            if (activeSubscriptionInfoForIcc != null) {
                int subscriptionId = activeSubscriptionInfoForIcc.getSubscriptionId();
                this.mCurrentFakeSimSubId = subscriptionId;
                this.mSubscriptionManager.setUiccApplicationsEnabled(subscriptionId, true);
                this.mSubscriptionManager.setDefaultDataSubId(this.mCurrentFakeSimSubId);
                Rlog.d(TAG, "active relay sim successfully...");
                requestDataUsage();
            } else {
                Rlog.e(TAG, "active relay sim failed...");
                this.mHasRelaySimCard = false;
            }
        } else {
            subscriptionManagerService.removeSubInfo(FAKE_SIM_ICCID, 0);
            this.mCurrentFakeSimSubId = -1;
            this.mNetworkRelayInfo = null;
            this.mCurrentNetworkRelayInfo = null;
            mDataState = 0;
            this.mSubscriptionManager.setDefaultDataSubId(-1);
            Rlog.d(TAG, "inactive relay sim successfully...");
        }
        if (z && z2) {
            showDefaultNetworkInfo();
        }
        Rlog.d(TAG, "default data sub id:" + SubscriptionManager.getDefaultSubscriptionId());
        this.mTelephonyRegistryMgr.notifyActiveDataSubIdChanged(this.mCurrentFakeSimSubId);
    }

    private TelephonyDisplayInfo createTelephonyDisplayInfo(int i, int i2) {
        return new TelephonyDisplayInfo(i, i2);
    }

    private PreciseDataConnectionState createPreciseDataConnectionState(int i, int i2) {
        return new PreciseDataConnectionState.Builder().setTransportType(1).setId(1).setState(i).setNetworkType(i2).setApnSetting(new ApnSetting.Builder().setApnTypeBitmask(17).setApnName("default").setEntryName("default").build()).setLinkProperties(new LinkProperties()).setFailCause(0).build();
    }

    private ServiceState createService(int i, int i2, int i3) {
        Rlog.d(TAG, "createService dataRegSate=" + i + " voiceRegState=" + i2 + " tech=" + i3);
        ServiceState serviceState = new ServiceState();
        serviceState.setDataRegState(i);
        serviceState.setVoiceRegState(i2);
        serviceState.addNetworkRegistrationInfo(new NetworkRegistrationInfo.Builder().setTransportType(1).setAccessNetworkTechnology(i3).setDomain(2).build());
        return serviceState;
    }

    public void requestDataUsage() {
        this.mRequestTime = SystemClock.elapsedRealtime();
        NetworkRelayInfo networkRelayInfo = new NetworkRelayInfo();
        networkRelayInfo.mMethod = 1;
        String json = GsonUtil.toJson(networkRelayInfo);
        Rlog.d(TAG, "queryDataUsage:" + json);
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            networkRelayInfo.mGoBssid = getGoBssid();
            json = GsonUtil.toJson(networkRelayInfo);
        }
        NetworkRelayChannelClient.getInstance(this.mContext).sendNetworkRequest(Packet.fromBytes(json.getBytes(StandardCharsets.UTF_8)));
    }

    private SignalStrength createSignalStrength(int i, int i2) {
        SignalStrength signalStrength;
        switch (i2) {
            case 1:
                signalStrength = new SignalStrength(this.mCdma, new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
                break;
            case 2:
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), this.mGsm, new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
                break;
            case 3:
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), this.mWcdma, new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
                break;
            case 4:
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), this.mTdscdma, new CellSignalStrengthLte(), new CellSignalStrengthNr());
                break;
            case 5:
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), this.mLte, new CellSignalStrengthNr());
                break;
            case 6:
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), this.mNr);
                break;
            default:
                Rlog.d(TAG, "invalid signalType:" + i2);
                signalStrength = new SignalStrength(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), this.mLte, new CellSignalStrengthNr());
                break;
        }
        signalStrength.setMiuiLevel(i);
        return signalStrength;
    }

    private void showDefaultNetworkInfo() {
        if (this.mNetworkRelayInfo != null) {
            Rlog.d(TAG, "show the real network info instead of default");
            this.mHandler.sendEmptyMessageDelayed(1, 300L);
            return;
        }
        NetworkRelayInfo networkRelayInfo = new NetworkRelayInfo();
        this.mNetworkRelayInfo = networkRelayInfo;
        networkRelayInfo.mSimSate = 5;
        networkRelayInfo.mMethod = 2;
        networkRelayInfo.mDataRegState = 0;
        networkRelayInfo.mVoiceRegState = 0;
        networkRelayInfo.mNetworkType = 0;
        networkRelayInfo.mSignalLevel = 5;
        networkRelayInfo.mSignalType = 6;
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        this.mHandler.sendEmptyMessageDelayed(1, 300L);
    }

    private String getGoBssid() {
        Context context = this.mContext;
        if (context != null && ((MiuiWifiManager) context.getSystemService("MiuiWifiService")) != null) {
            return Settings.Global.getString(PhoneGlobals.getInstance().getContentResolver(), GO_BSSID);
        }
        return "";
    }
}

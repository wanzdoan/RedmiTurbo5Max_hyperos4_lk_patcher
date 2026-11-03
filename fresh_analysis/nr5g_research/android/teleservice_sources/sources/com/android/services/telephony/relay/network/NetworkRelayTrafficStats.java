package com.android.services.telephony.relay.network;

import android.content.Context;
import android.net.IMiuiNetworkManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyRegistryManager;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayTrafficStats {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "NetworkRelayTrafficStats";
    private static final int MSG_INIT_DATA = 1;
    private static final int MSG_SCREEN_STATE_CHANGED = 2;
    private static final int MSG_TRAFFIC_STATS_POLL = 3;
    private static final String NETWORK_RELAY_INTERFACE = "p2p0";
    private static final int TRAFFIC_STATS_POLL_INTERVAL_MS = 1000;
    private static final int TYPE_RX_BYTES = 0;
    private static final int TYPE_TX_BYTES = 2;
    private Context mContext;
    protected NetworkRelayTrafficStatsHandler mHandler;
    private long mLastMobileRxBytes;
    private long mLastMobileTxBytes;
    private IMiuiNetworkManager mMiuiNetworkManager;
    private TelephonyRegistryManager mTelephonyRegistryMgr;
    private HandlerThread mWorkThread;
    private boolean mIsScreenOn = false;
    private boolean mLastTrafficValid = true;
    private int mLastDataActivity = 0;
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayTrafficStats.1
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                NetworkRelayTrafficStats.this.mHandler.obtainMessage(2).sendToTarget();
            }
        }
    };

    public NetworkRelayTrafficStats(Context context) {
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(LOG_TAG);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        NetworkRelayTrafficStatsHandler networkRelayTrafficStatsHandler = new NetworkRelayTrafficStatsHandler(this.mWorkThread.getLooper());
        this.mHandler = networkRelayTrafficStatsHandler;
        networkRelayTrafficStatsHandler.sendEmptyMessage(1);
    }

    private IMiuiNetworkManager getNetworkManager() {
        try {
            IMiuiNetworkManager iMiuiNetworkManagerAsInterface = IMiuiNetworkManager.Stub.asInterface((IBinder) ReflectUtil.callObjectMethod(ReflectUtil.callStaticObjectMethod(Class.forName("android.os.INetworkManagementService$Stub"), "asInterface", new Class[]{IBinder.class}, (IBinder) ReflectUtil.callStaticObjectMethod(Class.forName("android.os.ServiceManager"), IBinder.class, "getService", new Class[]{String.class}, (String) ReflectUtil.getStaticObjectField(Class.forName("android.content.Context"), "NETWORKMANAGEMENT_SERVICE", String.class))), "getMiuiNetworkManager", null, new Object[0]));
            log("getNetworkManager:" + iMiuiNetworkManagerAsInterface);
            return iMiuiNetworkManagerAsInterface;
        } catch (Exception e) {
            Log.e(LOG_TAG, "get NetworkManager failed " + e);
            return null;
        }
    }

    public class NetworkRelayTrafficStatsHandler extends Handler {
        public NetworkRelayTrafficStatsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                NetworkRelayTrafficStats.this.initAction();
                return;
            }
            if (i == 2) {
                NetworkRelayTrafficStats.this.handleScreenStateChanged();
            } else {
                if (i == 3) {
                    NetworkRelayTrafficStats.this.startNetStatPoll();
                    return;
                }
                Log.e(NetworkRelayTrafficStats.LOG_TAG, "invalid message " + message.what);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        if (this.mTelephonyRegistryMgr == null) {
            this.mTelephonyRegistryMgr = (TelephonyRegistryManager) this.mContext.getSystemService("telephony_registry");
        }
        this.mMiuiNetworkManager = getNetworkManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenStateChanged() {
        boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        if (this.mIsScreenOn == zIsScreenOn) {
            return;
        }
        this.mIsScreenOn = zIsScreenOn;
        handleTrafficStatsPollConditionChanged();
    }

    public void handleTrafficStatsPollConditionChanged() {
        log("mIsScreenOn: " + this.mIsScreenOn + ", mDataState: " + NetworkRelayClientController.mDataState);
        this.mHandler.removeMessages(3);
        if (this.mIsScreenOn && NetworkRelayClientController.mDataState == 2) {
            startNetStatPoll();
        }
    }

    protected void startNetStatPoll() {
        IMiuiNetworkManager iMiuiNetworkManager = this.mMiuiNetworkManager;
        if (iMiuiNetworkManager == null) {
            log("mMiuiNetworkManager is null");
            return;
        }
        int i = 2;
        try {
            long shareStats = iMiuiNetworkManager.getShareStats(2);
            long shareStats2 = this.mMiuiNetworkManager.getShareStats(0);
            long j = shareStats - this.mLastMobileTxBytes;
            long j2 = shareStats2 - this.mLastMobileRxBytes;
            this.mHandler.sendEmptyMessageDelayed(3, 1000L);
            this.mLastMobileTxBytes = shareStats;
            this.mLastMobileRxBytes = shareStats2;
            boolean z = j >= 0 && j2 >= 0;
            if (!this.mLastTrafficValid || !z) {
                this.mLastTrafficValid = z;
                log(" run into invalid traffic count");
                return;
            }
            if (j > 0 && j2 > 0) {
                i = 3;
            } else if (j2 > 0) {
                i = 1;
            } else if (j <= 0) {
                i = 0;
            }
            if (this.mLastDataActivity != i) {
                this.mLastDataActivity = i;
                this.mTelephonyRegistryMgr.notifyDataActivityChanged(SubscriptionManager.getDefaultSubscriptionId(), i);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "get ShareStats failed " + e);
        }
    }

    private void log(String str) {
        Log.d("NetworkRelayTrafficStats[" + SubscriptionManager.getDefaultSubscriptionId() + "] ", str);
    }
}

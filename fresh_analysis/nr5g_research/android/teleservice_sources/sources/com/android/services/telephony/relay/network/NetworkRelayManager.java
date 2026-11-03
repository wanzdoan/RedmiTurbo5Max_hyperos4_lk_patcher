package com.android.services.telephony.relay.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayManager {
    private static final String ACTION_CELLUAR_TETHER_STATE_CHANGED = "android.intent.action.CELLUAR_TETHER_STATE_CHANGED";
    private static final String ACTION_SIM_CARD_STATE_CHANGED = "android.telephony.action.SIM_CARD_STATE_CHANGED";
    private static final int CELLUAR_TETHER_STATE_CONNECTED = 3;
    private static final int CELLUAR_TETHER_STATE_UNAVAILABLE = 0;
    private static final int EVENT_DELAY_TIME = 60000;
    private static final int EVENT_RELAY_DEATH = 1;
    private static final int EVENT_SCREEN_OFF_DELAY = 0;
    private static final int EVENT_SCREEN_STATE_CHANGED = 2;
    private static final String TAG = "NetworkRelayManager";
    private static final int WIFI_TETHER_STATE_CONNECTED = 4;
    private static boolean dataEnabled = false;
    private static boolean displayOffDelay = false;
    private static boolean displayRegistered = false;
    private static boolean hasCreateClient = false;
    public static boolean hasCreateServer = false;
    private static boolean hasSimCard = false;
    private static NetworkRelayManager mInstance = null;
    public static boolean p2pConnected = false;
    public static String p2pConnectedDeviceId = "";
    public static HashMap p2pConnectedObject = new HashMap();
    private static boolean screenOffLong = false;
    private static boolean userPresent = false;
    private Context mContext;
    private NetworkingManager mNetworkingManager;
    private TelephonyManager mSubTelephonyMgr;
    private MobileTelephonyCallback mTelephonyCallback;
    private TelephonyManager mTelephonyManager;
    private NetworkRelayManagerHandler mThreadHandler;
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayManager.2
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (!displayEvent.isDefaultDisplay() || NetworkRelayManager.this.mThreadHandler == null) {
                    return;
                }
                NetworkRelayManager.this.mThreadHandler.sendMessage(NetworkRelayManager.this.mThreadHandler.obtainMessage(2, displayEvent));
            }
        }
    };
    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.network.NetworkRelayManager.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(NetworkRelayManager.TAG, "action = " + action);
            if (NetworkRelayManager.ACTION_SIM_CARD_STATE_CHANGED.equals(action)) {
                NetworkRelayManager.this.handleSimStateChange();
                return;
            }
            if ("android.intent.action.USER_PRESENT".equals(action)) {
                NetworkRelayManager.userPresent = true;
                NetworkRelayManager.this.initService();
            } else if (NetworkRelayManager.ACTION_CELLUAR_TETHER_STATE_CHANGED.equals(action)) {
                String stringExtra = intent.getStringExtra(CallMethod.ARG_DEVICE_ID);
                int intExtra = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 0);
                Rlog.d(NetworkRelayManager.TAG, "deviceId = " + stringExtra + "; state = " + intExtra);
                NetworkRelayManager.this.handleCelluarStateChange(stringExtra, intExtra);
            }
        }
    };

    public static synchronized NetworkRelayManager getInstance(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new NetworkRelayManager(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private NetworkRelayManager(Context context) {
        this.mContext = context;
    }

    public void init() {
        initNetworkRelayManagerHandler();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SIM_CARD_STATE_CHANGED);
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction(ACTION_CELLUAR_TETHER_STATE_CHANGED);
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            this.mTelephonyCallback = new MobileTelephonyCallback();
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallback);
            }
        }
        this.mContext.registerReceiver(this.mStateReceiver, intentFilter, 2);
        initService();
        if (this.mNetworkingManager == null) {
            NetworkingManager networkingManager = NetworkingManager.getInstance(this.mContext);
            this.mNetworkingManager = networkingManager;
            networkingManager.registerDeathCallback(new Runnable() { // from class: com.android.services.telephony.relay.network.NetworkRelayManager.1
                @Override // java.lang.Runnable
                public void run() {
                    if (NetworkRelayManager.this.mThreadHandler != null) {
                        NetworkRelayManager.this.mThreadHandler.sendMessageDelayed(NetworkRelayManager.this.mThreadHandler.obtainMessage(1), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initService() {
        Rlog.d(TAG, "userPresent = " + userPresent + "; hasSimCard = " + hasSimCard + "; hasCreateServer= " + hasCreateServer + "; hasCreateClient = " + hasCreateClient + "; p2pConnected = " + p2pConnected + "; p2pConnectedDeviceId = " + p2pConnectedDeviceId + "; displayRegistered = " + displayRegistered + "; screenOffLong = " + screenOffLong);
        if (!userPresent) {
            Rlog.i(TAG, "userPresent not ready!!");
            return;
        }
        try {
            if (isServerReady()) {
                if (hasCreateClient) {
                    NetworkRelayChannelClient.getInstance(this.mContext).removeSubscribeService();
                    NetworkRelayClientController.getInstance(this.mContext).activeRelaySim(false, false);
                    hasCreateClient = false;
                    p2pConnected = false;
                    p2pConnectedDeviceId = "";
                    p2pConnectedObject.clear();
                    if (displayRegistered) {
                        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
                        displayRegistered = false;
                        screenOffLong = false;
                    }
                    Rlog.d(TAG, "client close success");
                }
                if (!hasCreateServer) {
                    NetworkRelayChannelServer.getInstance(this.mContext).addNetworkRelayService();
                    hasCreateServer = true;
                    Rlog.d(TAG, "server create success");
                }
            } else {
                if (hasCreateServer) {
                    NetworkRelayChannelServer.getInstance(this.mContext).removeNetworkRelayService();
                    hasCreateServer = false;
                    p2pConnected = false;
                    p2pConnectedDeviceId = "";
                    p2pConnectedObject.clear();
                    Rlog.d(TAG, "server close success");
                }
                if (!Build.IS_TABLET) {
                    Rlog.i(TAG, "Not support, return");
                    return;
                }
                if (!p2pConnected) {
                    NetworkRelayChannelClient.getInstance(this.mContext).removeSubscribeService();
                    NetworkRelayClientController.getInstance(this.mContext).activeRelaySim(false, false);
                    hasCreateClient = false;
                    if (displayRegistered) {
                        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
                        displayRegistered = false;
                        screenOffLong = false;
                    }
                    Rlog.d(TAG, "client close success");
                } else {
                    if (!displayRegistered) {
                        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
                        displayRegistered = true;
                        screenOffLong = false;
                    }
                    boolean z = screenOffLong;
                    if (z && hasCreateClient) {
                        NetworkRelayChannelClient.getInstance(this.mContext).removeSubscribeService();
                        hasCreateClient = false;
                        Rlog.d(TAG, "client close success, but network dispaly");
                    } else if (!z && !hasCreateClient) {
                        NetworkRelayChannelClient.getInstance(this.mContext).addSubscribeService();
                        NetworkRelayClientController.getInstance(this.mContext).activeRelaySim(true, true);
                        hasCreateClient = true;
                        Rlog.d(TAG, "client create success");
                    }
                }
            }
        } catch (Exception e) {
            Rlog.i(TAG, "initService error = " + e);
        }
    }

    private boolean isServerReady() {
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            return (hasSimCard || isSimCardPresent()) && dataEnabled;
        }
        return hasSimCard || isSimCardPresent();
    }

    class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.UserMobileDataStateListener {
        private MobileTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.UserMobileDataStateListener
        public void onUserMobileDataStateChanged(boolean z) {
            Rlog.d(NetworkRelayManager.TAG, "onUserMobileDataStateChanged:enabled = " + z);
            NetworkRelayManager.dataEnabled = z;
            NetworkRelayManager.this.initService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCelluarStateChange(String str, int i) {
        if (p2pConnectedObject.containsKey(str)) {
            int iIntValue = ((Integer) p2pConnectedObject.get(str)).intValue();
            Rlog.d(TAG, "device = " + str + "; old p2pState = " + iIntValue + "; new state = " + i);
            if (3 != i && 4 != i) {
                p2pConnectedObject.remove(str);
                Rlog.d(TAG, "remove device = " + str);
            } else if (iIntValue != i) {
                p2pConnectedObject.replace(str, Integer.valueOf(i));
                Rlog.d(TAG, "device = " + str + "; replace new state = " + i);
                if (hasCreateClient) {
                    NetworkRelayClientController.getInstance(this.mContext).updateUIDisplay();
                }
            }
        } else {
            Rlog.d(TAG, "device = " + str + "; new state = " + i);
            if (3 == i || 4 == i) {
                p2pConnectedObject.put(str, Integer.valueOf(i));
            }
        }
        boolean z = !p2pConnectedObject.isEmpty();
        if (z != p2pConnected) {
            p2pConnected = z;
            if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                if (p2pConnected) {
                    p2pConnectedDeviceId = str;
                    if (hasCreateServer) {
                        NetworkRelayServerController.getInstance(this.mContext).registerP2pWifiRssi();
                    }
                } else {
                    p2pConnectedDeviceId = "";
                    NetworkRelayServerController.getInstance(this.mContext).unRegisterP2pWifiRssi();
                    NetworkRelayChannelClient.getInstance(this.mContext).transferNewDevice();
                }
                Rlog.d(TAG, "p2pConnected = " + p2pConnected + "; p2pConnectedDeviceId = " + p2pConnectedDeviceId);
            } else {
                Rlog.d(TAG, "p2pConnected = " + p2pConnected);
            }
            initService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimStateChange() {
        boolean zIsSimCardPresent = isSimCardPresent();
        if (hasSimCard != zIsSimCardPresent) {
            hasSimCard = zIsSimCardPresent;
            Rlog.d(TAG, "hasSimCard = " + hasSimCard);
            initService();
        }
    }

    private boolean isSimCardPresent() {
        if (this.mTelephonyManager != null) {
            Rlog.d(TAG, "isSimCardPresent getPhoneType = " + this.mTelephonyManager.getPhoneType());
            Rlog.d(TAG, "isSimCardPresent getSimState = " + this.mTelephonyManager.getSimState());
            if (this.mTelephonyManager.getPhoneType() != 0 && this.mTelephonyManager.getSimState() != 0 && this.mTelephonyManager.getSimState() != 1) {
                return true;
            }
        }
        return false;
    }

    private void initNetworkRelayManagerHandler() {
        if (this.mThreadHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, 10);
            handlerThread.start();
            this.mThreadHandler = new NetworkRelayManagerHandler(handlerThread.getLooper());
        }
    }

    class NetworkRelayManagerHandler extends Handler {
        public NetworkRelayManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                NetworkRelayManager.displayOffDelay = false;
                if (NetworkRelayManager.screenOffLong || !MiuiPhoneUtils.isScreenOff(NetworkRelayManager.this.mContext)) {
                    return;
                }
                Rlog.d(NetworkRelayManager.TAG, "screen close >60s, close network channel");
                NetworkRelayManager.screenOffLong = true;
                NetworkRelayManager.this.initService();
                return;
            }
            if (i == 1) {
                Rlog.d(NetworkRelayManager.TAG, "relay death, reset");
                NetworkRelayManager.hasCreateServer = false;
                NetworkRelayManager.hasCreateClient = false;
                NetworkRelayManager.screenOffLong = false;
                NetworkRelayManager.this.initService();
                return;
            }
            if (i != 2) {
                return;
            }
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) message.obj;
            if (displayEvent.isScreenOn()) {
                Rlog.d(NetworkRelayManager.TAG, "onDisplayChanged to STATE ON");
                NetworkRelayManager.screenOffLong = false;
                NetworkRelayManager.this.initService();
            } else if (displayEvent.isScreenOff()) {
                Rlog.d(NetworkRelayManager.TAG, "onDisplayChanged to STATE OFF");
                if (NetworkRelayManager.this.mThreadHandler == null || NetworkRelayManager.displayOffDelay) {
                    return;
                }
                NetworkRelayManager.displayOffDelay = true;
                NetworkRelayManager.this.mThreadHandler.sendMessageDelayed(NetworkRelayManager.this.mThreadHandler.obtainMessage(0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
        }
    }
}

package com.android.services.telephony.relay.network;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.bluetooth.ble.app.IMiuiNearbyApiService;
import com.android.telephony.Rlog;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ClientChannelOptions;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.channel.PacketTransferProgressCallback;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.continuity.networking.ServiceFilter;
import com.xiaomi.continuity.networking.ServiceListener;
import com.xiaomi.continuity.networking.TrustedDeviceInfo;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayChannelClient {
    private static final int AP_CONNECT_CELLUAR = 6;
    private static final String BLE_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_GATT_ENABLE";
    private static final String BLE_TETHER_SERVICE_NAME = "miui.bluetooth.mible.EasyTetherCoreService";
    private static final String BT_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_RFCOMM_CLASS_TYPE";
    private static final int BT_HALF_OPEN_WHITH_LIST_CODE = 1111;
    private static final int CREATE_RELAY_CHANNEL_TIMEOUT_IN_MILLIS = 10000;
    private static final int CREATE_SUCCESS = 0;
    private static final int ERROR_15036 = 15036;
    private static final int EVENT_CREATE_CHANNEL_RETRY = 1;
    private static final int EVENT_MONITORING_NETWORK_RELAY_DEVICE = 2;
    private static final int EVENT_SCANNING_NETWORK_RELAY_DEVICE = 3;
    private static final int EVENT_SUBSCRIBE_SERVICE_RETRY = 0;
    private static final int MONITORING_NETWORK_RELAY_DEVICE_TIME = 5000;
    private static final String NETWORK_RELAY_PACKAGE_NAME = "com.android.phone";
    private static final String NETWORK_RELAY_SERVICE_NAME = "networkRelay";
    private static final String PACKAGE_BLUETOOTH = "com.xiaomi.bluetooth";
    private static final int RETRY_CHANNEL_ERROR_DELAY_TIME = 6000;
    private static final int RETRY_DELAY_TIME = 60000;
    private static final int RETRY_MAX = 10;
    private static final int SCANNING_NETWORK_RELAY_DEVICE_TIME = 3000;
    private static final int SMART_STATE_ACTIVE = 1;
    public static final int SMART_STATE_SCANNING = 2;
    private static final String TAG = "NetworkRelayChannelClient";
    private static final int VERSION_1 = 1;
    public static HashMap channelMapObject = new HashMap();
    private static NetworkRelayChannelClient mInstance = null;
    public static int mSmartNetworkRelayState = 1;
    private ChannelListener mChannelListener;
    private ContinuityChannelManager mChannelManager;
    private NetworkRelayClientHandler mClientHandler;
    private Context mContext;
    private NetworkingManager mNetworkingManager;
    private ServiceListener mServiceListener;
    private int mSubscribeServiceRetryCount = 0;
    private int mCreateChannelRetryCount = 0;
    private boolean mIsNetworkScanningWaiting = false;
    private boolean mIsNetworkMonitoringWaiting = false;
    private HashMap deviceScoreMapObject = new HashMap();
    private ServiceConnection mShareServiceConnection = null;
    private IMiuiNearbyApiService mNearbyApiService = null;
    private boolean mWaitingTransferNewDevice = false;
    private String mGreatDeviceId = "";

    public static synchronized NetworkRelayChannelClient getInstance(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new NetworkRelayChannelClient(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private NetworkRelayChannelClient(Context context) {
        this.mContext = context;
        initNetworkRelayClientHandler();
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            bindEasyTetherService();
        }
    }

    private void bindEasyTetherService() {
        if (this.mShareServiceConnection == null) {
            try {
                this.mShareServiceConnection = new ServiceConnection() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelClient.1
                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        NetworkRelayChannelClient.this.mNearbyApiService = IMiuiNearbyApiService.Stub.asInterface(iBinder);
                    }
                };
                Intent intent = new Intent(BLE_TETHER_SERVICE_NAME);
                intent.setPackage(PACKAGE_BLUETOOTH);
                this.mContext.bindService(intent, this.mShareServiceConnection, 1);
            } catch (Exception e) {
                Rlog.i(TAG, "bindEasyTetherService error = " + e);
            }
        }
    }

    public void sendNetworkRequest(Packet packet) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(BT_HALF_OPEN_WHITH_LIST, BT_HALF_OPEN_WHITH_LIST_CODE);
            bundle.putBoolean(BLE_HALF_OPEN_WHITH_LIST, true);
            packet.putExtras(bundle);
            for (Map.Entry entry : channelMapObject.entrySet()) {
                Rlog.d(TAG, "try to sendNetworkRequest channelId =" + ((Integer) entry.getKey()).intValue());
                Channel channel = (Channel) entry.getValue();
                if (channel != null) {
                    channel.send(packet, new PacketTransferProgressCallback() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelClient.2
                        public void onPacketTransferProgressUpdate(Packet packet2, PacketTransferProgress packetTransferProgress) {
                            int transferState = packetTransferProgress.getTransferState();
                            Rlog.d(NetworkRelayChannelClient.TAG, "onPacketTransferProgressUpdate =" + transferState);
                            if (transferState == 3) {
                                NetworkRelayChannelClient.this.changeChannelTypeRetry();
                            }
                        }
                    }, new HandlerExecutor(this.mClientHandler));
                }
            }
        } catch (Exception e) {
            Rlog.i(TAG, "sendNetworkRequest error = " + e);
        }
    }

    public synchronized void addSubscribeService() {
        NetworkRelayClientHandler networkRelayClientHandler;
        int i;
        try {
            try {
                if (this.mNetworkingManager == null) {
                    this.mNetworkingManager = NetworkingManager.getInstance(this.mContext);
                }
                ServiceFilter serviceFilter = new ServiceFilter();
                ArrayList arrayList = new ArrayList();
                arrayList.add(1);
                serviceFilter.setTrustedTypeFilter(arrayList);
                serviceFilter.setServiceFilter(new ServiceName("com.android.phone", NETWORK_RELAY_SERVICE_NAME));
                ServiceListener serviceListener = new ServiceListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelClient.3
                    public void onServiceOnline(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
                        String deviceId = trustedDeviceInfo.getDeviceId();
                        Rlog.d(NetworkRelayChannelClient.TAG, "onServiceOnline mServiceName = " + businessServiceInfo.getServiceName() + ";\n mPackageName = " + businessServiceInfo.getPackageName() + ";\n mDeviceId = " + deviceId + ";\n mDeviceType = " + trustedDeviceInfo.getDeviceType() + ";\n mDeviceName = " + trustedDeviceInfo.getDeviceName());
                        if (!SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                            if (!NetworkRelayManager.p2pConnectedObject.containsKey(deviceId)) {
                                Rlog.i(NetworkRelayChannelClient.TAG, "onServiceOnline but not p2p connected device, don't create channel");
                                return;
                            } else {
                                NetworkRelayChannelClient.this.createChannel(deviceId, true);
                                return;
                            }
                        }
                        NetworkRelayChannelClient.this.createChannel(deviceId, true);
                    }

                    public void onServiceOffline(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo, int i2) {
                        Rlog.d(NetworkRelayChannelClient.TAG, "onServiceOffline mServiceName = " + businessServiceInfo.getServiceName() + ";\n mPackageName = " + businessServiceInfo.getPackageName() + ";\n mDeviceId = " + trustedDeviceInfo.getDeviceId() + ";\n mDeviceType = " + trustedDeviceInfo.getDeviceType() + ";\n mDeviceName = " + trustedDeviceInfo.getDeviceName() + ";\n reason = " + i2);
                    }

                    public void onDeviceChanged(TrustedDeviceInfo trustedDeviceInfo) {
                        Rlog.d(NetworkRelayChannelClient.TAG, "onDeviceChanged mDeviceId = " + trustedDeviceInfo.getDeviceId() + ";\n mDeviceTyp = e" + trustedDeviceInfo.getDeviceType() + ";\n mDeviceName = " + trustedDeviceInfo.getDeviceName());
                    }

                    public void onServiceChanged(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
                        Rlog.d(NetworkRelayChannelClient.TAG, "onServiceChanged mServiceName = " + businessServiceInfo.getServiceName() + ";\n mPackageName = " + businessServiceInfo.getPackageName() + ";\n mDeviceId = " + trustedDeviceInfo.getDeviceId() + ";\n mDeviceType = " + trustedDeviceInfo.getDeviceType() + ";\n mDeviceName = " + trustedDeviceInfo.getDeviceName());
                    }
                };
                this.mServiceListener = serviceListener;
                int iAddServiceListener = this.mNetworkingManager.addServiceListener(serviceFilter, serviceListener);
                Rlog.d(TAG, "addSubscribeService code = " + iAddServiceListener);
                if (iAddServiceListener != 0 && (networkRelayClientHandler = this.mClientHandler) != null && (i = this.mSubscribeServiceRetryCount) < 10) {
                    this.mSubscribeServiceRetryCount = i + 1;
                    networkRelayClientHandler.sendMessageDelayed(networkRelayClientHandler.obtainMessage(0), this.mSubscribeServiceRetryCount * 60000);
                }
            } catch (Exception e) {
                Rlog.i(TAG, "addSubscribeService error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void createChannel(final String str, boolean z) {
        NetworkRelayClientHandler networkRelayClientHandler;
        int i;
        try {
            if (this.mChannelManager == null) {
                this.mChannelManager = ContinuityChannelManager.getInstance(this.mContext);
            }
            ServiceName serviceNameBuild = new ServiceName.Builder().setName(NETWORK_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build();
            ClientChannelOptions clientChannelOptions = new ClientChannelOptions();
            if (z) {
                clientChannelOptions.setConnectMediumType(0);
            } else {
                clientChannelOptions.setConnectMediumType(1);
            }
            clientChannelOptions.setTrustLevel(16);
            clientChannelOptions.setTimeout(10000);
            ChannelListener channelListener = new ChannelListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelClient.4
                public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
                }

                public void onChannelConfirm(String str2, ServiceName serviceName, int i2, ConfirmInfo confirmInfo) {
                    Rlog.d(NetworkRelayChannelClient.TAG, "onChannelConfirm channelId = " + i2);
                    if (NetworkRelayManager.p2pConnected) {
                        ContinuityChannelManager.getInstance(NetworkRelayChannelClient.this.mContext).confirmChannel(i2, 0);
                    } else {
                        Rlog.i(NetworkRelayChannelClient.TAG, "onChannelConfirm device p2p disconnected");
                    }
                }

                public void onChannelCreateSuccess(Channel channel) {
                    Rlog.d(NetworkRelayChannelClient.TAG, "onChannelCreateSuccess channelId = " + channel.getChannelId());
                    if (!NetworkRelayManager.p2pConnected && NetworkRelayChannelClient.this.mChannelManager != null) {
                        Rlog.i(NetworkRelayChannelClient.TAG, "onChannelCreateSuccess device p2p disconnected");
                        NetworkRelayChannelClient.this.mChannelManager.destroyChannel(channel.getChannelId());
                    } else {
                        if (!NetworkRelayChannelClient.channelMapObject.containsKey(Integer.valueOf(channel.getChannelId()))) {
                            NetworkRelayChannelClient.channelMapObject.put(Integer.valueOf(channel.getChannelId()), channel);
                        }
                        NetworkRelayClientController.getInstance(NetworkRelayChannelClient.this.mContext).requestDataUsage();
                    }
                }

                public void onChannelCreateFailed(String str2, ServiceName serviceName, int i2, int i3) {
                    Rlog.d(NetworkRelayChannelClient.TAG, "onChannelCreateFailed deviceId = " + str2 + ";\n serviceName = " + serviceName + ";\n channelId = " + i2 + ";\n code = " + i3);
                    NetworkRelayStats.getInstance().recordErrorCode(false, i3);
                    if (i3 != NetworkRelayChannelClient.ERROR_15036 || NetworkRelayChannelClient.this.mClientHandler == null) {
                        return;
                    }
                    NetworkRelayChannelClient.this.mClientHandler.sendMessageDelayed(NetworkRelayChannelClient.this.mClientHandler.obtainMessage(1, str), 6000L);
                }

                public void onChannelReceive(Channel channel, Packet packet) {
                    Rlog.d(NetworkRelayChannelClient.TAG, "onChannelReceive channelId = " + channel.getChannelId());
                    if (!NetworkRelayManager.p2pConnected && NetworkRelayChannelClient.this.mChannelManager != null) {
                        Rlog.i(NetworkRelayChannelClient.TAG, "onChannelReceive device p2p disconnected");
                        NetworkRelayChannelClient.this.mChannelManager.destroyChannel(channel.getChannelId());
                        return;
                    }
                    try {
                        byte[] bArrAsBytes = packet.asBytes();
                        if (!SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                            NetworkRelayClientController.getInstance(NetworkRelayChannelClient.this.mContext).receiveRelayMessage(bArrAsBytes);
                            return;
                        }
                        boolean zContainsKey = NetworkRelayManager.p2pConnectedObject.containsKey(channel.getDeviceId());
                        NetworkRelayChannelClient networkRelayChannelClient = NetworkRelayChannelClient.this;
                        if (zContainsKey) {
                            NetworkRelayClientController.getInstance(networkRelayChannelClient.mContext).receiveRelayMessage(bArrAsBytes);
                            NetworkRelayChannelClient.this.handleScore(bArrAsBytes, channel, true);
                        } else {
                            networkRelayChannelClient.handleScore(bArrAsBytes, channel, false);
                        }
                    } catch (Exception e) {
                        Rlog.i(NetworkRelayChannelClient.TAG, "onChannelReceive error = " + e);
                    }
                }

                public void onChannelRelease(Channel channel, int i2) {
                    Rlog.d(NetworkRelayChannelClient.TAG, "onChannelRelease deviceId = " + channel.getDeviceId() + ";\n role = " + channel.getChannelRole() + ";\n code = " + i2);
                    if (NetworkRelayChannelClient.channelMapObject.containsKey(Integer.valueOf(channel.getChannelId()))) {
                        NetworkRelayChannelClient.channelMapObject.remove(Integer.valueOf(channel.getChannelId()));
                    }
                    if (SmartNetworkRelayScore.isSmartNetworkRelaySupport() && NetworkRelayChannelClient.this.deviceScoreMapObject.containsKey(channel.getDeviceId())) {
                        NetworkRelayChannelClient.this.deviceScoreMapObject.remove(channel.getDeviceId());
                    }
                }
            };
            this.mChannelListener = channelListener;
            int iCreateChannel = this.mChannelManager.createChannel(str, serviceNameBuild, clientChannelOptions, channelListener, new HandlerExecutor(this.mClientHandler));
            Rlog.d(TAG, "createChannel code = " + iCreateChannel);
            if (iCreateChannel < 0 && (networkRelayClientHandler = this.mClientHandler) != null && (i = this.mCreateChannelRetryCount) < 10) {
                this.mCreateChannelRetryCount = i + 1;
                networkRelayClientHandler.sendMessageDelayed(networkRelayClientHandler.obtainMessage(1, str), this.mCreateChannelRetryCount * 60000);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleScore(byte[] bArr, Channel channel, boolean z) {
        try {
            NetworkRelayInfo networkRelayInfo = NetworkRelayClientController.getInstance(this.mContext).getNetworkRelayInfo(bArr);
            int score = SmartNetworkRelayScore.getScore(networkRelayInfo);
            StringBuilder sb = new StringBuilder("handleScore isP2pConnectedDevice = ");
            sb.append(z);
            sb.append("; score = ");
            sb.append(score);
            sb.append("; mSmartNetworkRelayState >>>> ");
            sb.append(mSmartNetworkRelayState == 1 ? "ACTIVE" : "SCANNING");
            Rlog.d(TAG, sb.toString());
            if (mSmartNetworkRelayState == 1) {
                HashMap map = channelMapObject;
                if (map != null && map.size() < 2) {
                    Rlog.d(TAG, "handleScore connect device size < 2, ignore");
                    return;
                }
                if (!z) {
                    Rlog.d(TAG, "handleScore active state, not p2p device, ignore");
                    return;
                }
                if (score > 84) {
                    SmartNetworkRelayScore.stopMonitoring(this.mContext);
                    stopNetworkMonitoring();
                    Rlog.d(TAG, "handleScore p2p device network great, ignore");
                    return;
                } else {
                    if (networkRelayInfo.mVersion == 1) {
                        SmartNetworkRelayScore.stopMonitoring(this.mContext);
                        stopNetworkMonitoring();
                        Rlog.d(TAG, "handleScore old device not support, ignore");
                        return;
                    }
                    boolean z2 = networkRelayInfo.mAutoShareNetworkEnabled;
                    Context context = this.mContext;
                    if (z2) {
                        SmartNetworkRelayScore.stopMonitoring(context);
                        stopNetworkMonitoring();
                        Rlog.d(TAG, "handleScore auto share network enabled, ignore");
                        return;
                    }
                    SmartNetworkRelayScore.handleMonitoring(context, networkRelayInfo);
                }
            }
            if (mSmartNetworkRelayState == 2) {
                if (z) {
                    if (score > 84) {
                        mSmartNetworkRelayState = 1;
                        NetworkRelayClientHandler networkRelayClientHandler = this.mClientHandler;
                        if (networkRelayClientHandler != null) {
                            networkRelayClientHandler.removeCallbacksAndMessages(3);
                            this.mClientHandler.removeCallbacksAndMessages(2);
                        }
                        this.deviceScoreMapObject.clear();
                        SmartNetworkRelayScore.stopMonitoring(this.mContext);
                        stopNetworkMonitoring();
                    }
                } else if (score > 84) {
                    NetworkRelayClientHandler networkRelayClientHandler2 = this.mClientHandler;
                    if (networkRelayClientHandler2 != null && !this.mIsNetworkScanningWaiting) {
                        this.mIsNetworkScanningWaiting = true;
                        networkRelayClientHandler2.sendMessageDelayed(networkRelayClientHandler2.obtainMessage(3), 3000L);
                    }
                    if (!this.deviceScoreMapObject.containsKey(channel.getDeviceId())) {
                        this.deviceScoreMapObject.put(channel.getDeviceId(), Integer.valueOf(score));
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void startNetworkMonitoring() {
        if (this.mClientHandler == null || this.mIsNetworkMonitoringWaiting) {
            return;
        }
        Rlog.d(TAG, "startNetworkMonitoring");
        this.mIsNetworkMonitoringWaiting = true;
        NetworkRelayClientHandler networkRelayClientHandler = this.mClientHandler;
        networkRelayClientHandler.sendMessageDelayed(networkRelayClientHandler.obtainMessage(2), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    public void stopNetworkMonitoring() {
        if (this.mClientHandler == null || !this.mIsNetworkMonitoringWaiting) {
            return;
        }
        Rlog.d(TAG, "stopNetworkMonitoring");
        this.mIsNetworkMonitoringWaiting = false;
        this.mClientHandler.removeCallbacksAndMessages(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScanning() {
        try {
            String str = "";
            int i = 84;
            for (Map.Entry entry : this.deviceScoreMapObject.entrySet()) {
                String str2 = (String) entry.getKey();
                int iIntValue = ((Integer) entry.getValue()).intValue();
                Rlog.d(TAG, "updateGreatDeviceId deviceId = " + str2 + "; score = " + iIntValue);
                if (iIntValue > i) {
                    i = iIntValue;
                    str = str2;
                }
            }
            if (i > 84 && !TextUtils.isEmpty(str)) {
                if (!str.equals(this.mGreatDeviceId)) {
                    this.mGreatDeviceId = str;
                    disconnectTransferDevice();
                }
                this.deviceScoreMapObject.clear();
                mSmartNetworkRelayState = 1;
            }
            this.mIsNetworkScanningWaiting = false;
        } catch (Exception e) {
            Rlog.i(TAG, "handleScanning error = " + e);
        }
    }

    private void disconnectTransferDevice() {
        Rlog.d(TAG, "disconnectTransferDevice old device = " + NetworkRelayManager.p2pConnectedDeviceId + " >>>>> new device = " + this.mGreatDeviceId);
        if (this.mGreatDeviceId.equals(NetworkRelayManager.p2pConnectedDeviceId)) {
            return;
        }
        try {
            IMiuiNearbyApiService iMiuiNearbyApiService = this.mNearbyApiService;
            if (iMiuiNearbyApiService != null) {
                iMiuiNearbyApiService.disconnectCelluarTether(NetworkRelayManager.p2pConnectedDeviceId);
                this.mWaitingTransferNewDevice = true;
            }
        } catch (Exception e) {
            Rlog.i(TAG, "disconnectTransferDevice error = " + e);
        }
    }

    public void transferNewDevice() {
        Rlog.d(TAG, "transferNewDevice >>>>> new device = " + this.mGreatDeviceId + "; mWaitingTransferNewDevice = " + this.mWaitingTransferNewDevice);
        if (!this.mWaitingTransferNewDevice || TextUtils.isEmpty(this.mGreatDeviceId)) {
            return;
        }
        try {
            IMiuiNearbyApiService iMiuiNearbyApiService = this.mNearbyApiService;
            if (iMiuiNearbyApiService != null) {
                iMiuiNearbyApiService.connectHotspotForEasyTether(this.mGreatDeviceId, 6);
                this.mWaitingTransferNewDevice = false;
                this.mGreatDeviceId = "";
            }
        } catch (Exception e) {
            Rlog.i(TAG, "transferNewDevice error = " + e);
        }
    }

    public void removeSubscribeService() {
        try {
            if (this.mNetworkingManager == null) {
                this.mNetworkingManager = NetworkingManager.getInstance(this.mContext);
            }
            ServiceListener serviceListener = this.mServiceListener;
            if (serviceListener != null) {
                this.mNetworkingManager.removeServiceListener(serviceListener);
                Rlog.d(TAG, "removeServiceListener");
            }
            destroyChannelAndRetry();
        } catch (Exception e) {
            Rlog.i(TAG, "removeSubscribeService error = " + e);
        }
    }

    private void destroyChannelAndRetry() {
        if (this.mChannelListener != null) {
            destroyAllChannel();
        }
        NetworkRelayClientHandler networkRelayClientHandler = this.mClientHandler;
        if (networkRelayClientHandler != null) {
            networkRelayClientHandler.removeCallbacksAndMessages(null);
        }
        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
            this.deviceScoreMapObject.clear();
            this.mIsNetworkScanningWaiting = false;
        }
        channelMapObject.clear();
        this.mSubscribeServiceRetryCount = 0;
        this.mCreateChannelRetryCount = 0;
    }

    private void destroyAllChannel() {
        if (this.mChannelManager != null) {
            Iterator it = channelMapObject.entrySet().iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) ((Map.Entry) it.next()).getKey()).intValue();
                this.mChannelManager.destroyChannel(iIntValue);
                Rlog.d(TAG, "try to destroyAllChannel = " + iIntValue);
            }
        }
    }

    private void initNetworkRelayClientHandler() {
        if (this.mClientHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, 10);
            handlerThread.start();
            this.mClientHandler = new NetworkRelayClientHandler(handlerThread.getLooper());
        }
    }

    class NetworkRelayClientHandler extends Handler {
        public NetworkRelayClientHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Rlog.d(NetworkRelayChannelClient.TAG, "subscribe service retry");
                NetworkRelayChannelClient.this.addSubscribeService();
                return;
            }
            if (i == 1) {
                Rlog.d(NetworkRelayChannelClient.TAG, "create channel retry");
                NetworkRelayChannelClient.this.createChannel((String) message.obj, true);
            } else if (i == 2) {
                Rlog.d(NetworkRelayChannelClient.TAG, "create network monitoring");
                NetworkRelayClientController.getInstance(NetworkRelayChannelClient.this.mContext).requestDataUsage();
            } else {
                if (i != 3) {
                    return;
                }
                Rlog.d(NetworkRelayChannelClient.TAG, "create network scanning");
                NetworkRelayChannelClient.this.handleScanning();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeChannelTypeRetry() {
        try {
            destroyChannelAndRetry();
            Iterator it = NetworkRelayManager.p2pConnectedObject.entrySet().iterator();
            while (it.hasNext()) {
                String str = (String) ((Map.Entry) it.next()).getKey();
                Rlog.d(TAG, "try to changeChannelTypeRetry deviceId =" + str);
                createChannel(str, false);
            }
        } catch (Exception e) {
            Rlog.i(TAG, "changeChannelTypeRetry error = " + e);
        }
    }
}

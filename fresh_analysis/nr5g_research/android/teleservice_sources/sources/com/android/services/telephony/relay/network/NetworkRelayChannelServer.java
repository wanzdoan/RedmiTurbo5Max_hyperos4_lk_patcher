package com.android.services.telephony.relay.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.telephony.Rlog;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.channel.PacketTransferProgressCallback;
import com.xiaomi.continuity.channel.ServerChannelOptions;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayChannelServer {
    private static final String BLE_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_GATT_ENABLE";
    private static final String BT_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_RFCOMM_CLASS_TYPE";
    private static final int BT_HALF_OPEN_WHITH_LIST_CODE = 1111;
    private static final int CREATE_SUCCESS = 0;
    private static final int EVENT_ADD_SERVICE_RETRY = 0;
    private static final int EVENT_REGISTER_CHANNEL_RETRY = 2;
    private static final int EVENT_REMOVE_SERVICE_RETRY = 1;
    private static final int EVENT_UNREGISTER_CHANNEL_RETRY = 3;
    private static final String NETWORK_RELAY_PACKAGE_NAME = "com.android.phone";
    private static final String NETWORK_RELAY_SERVICE_NAME = "networkRelay";
    private static final int RETRY_DELAY_TIME = 60000;
    private static final int RETRY_MAX = 10;
    private static final String TAG = "NetworkRelayChannelServer";
    private static NetworkRelayChannelServer mInstance;
    private ContinuityChannelManager mChannelManager;
    private Context mContext;
    private NetworkingManager mNetworkingManager;
    private NetworkRelayServerHandler mServerHandler;
    private int mAddServiceRetryCount = 0;
    private int mRemoveServiceRetryCount = 0;
    private int mRegisterChannelCount = 0;
    private int mUnregisterChannelCount = 0;
    private HashMap channelMapObject = new HashMap();

    public static synchronized NetworkRelayChannelServer getInstance(Context context) {
        try {
            if (mInstance == null) {
                mInstance = new NetworkRelayChannelServer(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private NetworkRelayChannelServer(Context context) {
        this.mContext = context;
        initNetworkRelayServerHandler();
    }

    public void sendNetworkRequest(Packet packet) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(BT_HALF_OPEN_WHITH_LIST, BT_HALF_OPEN_WHITH_LIST_CODE);
            bundle.putBoolean(BLE_HALF_OPEN_WHITH_LIST, true);
            packet.putExtras(bundle);
            for (Map.Entry entry : this.channelMapObject.entrySet()) {
                int iIntValue = ((Integer) entry.getKey()).intValue();
                Channel channel = (Channel) entry.getValue();
                if (!SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                    Rlog.d(TAG, "try to sendNetworkRequest channelId =" + iIntValue);
                } else if (!NetworkRelayManager.p2pConnected || channel.getDeviceId().equals(NetworkRelayManager.p2pConnectedDeviceId)) {
                    Rlog.d(TAG, "try to sendNetworkRequest channelId =" + iIntValue + "; p2pConnectedDeviceId = " + NetworkRelayManager.p2pConnectedDeviceId);
                }
                if (channel != null) {
                    channel.send(packet, new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelServer.1
                        public void onPacketTransferProgressUpdate(Packet packet2, PacketTransferProgress packetTransferProgress) {
                            Rlog.d(NetworkRelayChannelServer.TAG, "onPacketTransferProgressUpdate =" + packetTransferProgress.getTransferState());
                        }
                    }, new HandlerExecutor(this.mServerHandler));
                }
            }
        } catch (Exception e) {
            Rlog.i(TAG, "sendNetworkRequest error = " + e);
        }
    }

    public synchronized void addNetworkRelayService() {
        new Thread(new Runnable() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelServer.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (NetworkRelayChannelServer.this.mNetworkingManager == null) {
                        NetworkRelayChannelServer networkRelayChannelServer = NetworkRelayChannelServer.this;
                        networkRelayChannelServer.mNetworkingManager = NetworkingManager.getInstance(networkRelayChannelServer.mContext);
                    }
                    BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
                    businessServiceInfo.setServiceName(NetworkRelayChannelServer.NETWORK_RELAY_SERVICE_NAME);
                    int iAddServiceInfo = NetworkRelayChannelServer.this.mNetworkingManager.addServiceInfo(businessServiceInfo);
                    Rlog.d(NetworkRelayChannelServer.TAG, "addNetworkRelayService code = " + iAddServiceInfo);
                    if (iAddServiceInfo == 0) {
                        NetworkRelayChannelServer.this.registerNetworkChannelListener();
                        return;
                    }
                    NetworkRelayStats.getInstance().recordErrorCode(true, iAddServiceInfo);
                    if (NetworkRelayChannelServer.this.mServerHandler == null || NetworkRelayChannelServer.this.mAddServiceRetryCount >= 10) {
                        return;
                    }
                    NetworkRelayChannelServer.this.mAddServiceRetryCount++;
                    NetworkRelayChannelServer.this.mServerHandler.sendMessageDelayed(NetworkRelayChannelServer.this.mServerHandler.obtainMessage(0), NetworkRelayChannelServer.this.mAddServiceRetryCount * 60000);
                } catch (Exception e) {
                    Rlog.i(NetworkRelayChannelServer.TAG, "addNetworkRelayService error = " + e);
                }
            }
        }).start();
    }

    public synchronized void removeNetworkRelayService() {
        int i;
        try {
            try {
                if (this.mNetworkingManager == null) {
                    this.mNetworkingManager = NetworkingManager.getInstance(this.mContext);
                }
                BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
                businessServiceInfo.setServiceName(NETWORK_RELAY_SERVICE_NAME);
                int iRemoveServiceInfo = this.mNetworkingManager.removeServiceInfo(businessServiceInfo);
                Rlog.d(TAG, "removeNetworkRelayService code = " + iRemoveServiceInfo);
                NetworkRelayServerHandler networkRelayServerHandler = this.mServerHandler;
                if (iRemoveServiceInfo == 0) {
                    if (networkRelayServerHandler != null) {
                        networkRelayServerHandler.removeCallbacksAndMessages(null);
                    }
                    this.mAddServiceRetryCount = 0;
                    this.mRemoveServiceRetryCount = 0;
                    this.mRegisterChannelCount = 0;
                    this.mUnregisterChannelCount = 0;
                } else if (networkRelayServerHandler != null && (i = this.mRemoveServiceRetryCount) < 10) {
                    this.mRemoveServiceRetryCount = i + 1;
                    networkRelayServerHandler.sendMessageDelayed(networkRelayServerHandler.obtainMessage(1), this.mRemoveServiceRetryCount * 60000);
                }
                unRegisterNetworkChannelListener();
                destroyAllChannel();
                this.channelMapObject.clear();
                NetworkRelayServerController.getInstance(this.mContext).enableNetworkListener(false);
            } catch (Exception e) {
                Rlog.i(TAG, "removeNetworkRelayService error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void registerNetworkChannelListener() {
        NetworkRelayServerHandler networkRelayServerHandler;
        int i;
        try {
            try {
                if (this.mChannelManager == null) {
                    this.mChannelManager = ContinuityChannelManager.getInstance(this.mContext);
                }
                if (this.mChannelManager.registerChannelListener(new ServiceName.Builder().setName(NETWORK_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build(), new ServerChannelOptions(16), new ChannelListener() { // from class: com.android.services.telephony.relay.network.NetworkRelayChannelServer.3
                    public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
                    }

                    public void onChannelConfirm(String str, ServiceName serviceName, int i2, ConfirmInfo confirmInfo) {
                        Rlog.d(NetworkRelayChannelServer.TAG, "onChannelConfirm channelId = " + i2);
                        NetworkRelayChannelServer.this.mChannelManager.confirmChannel(i2, 0);
                    }

                    public void onChannelCreateSuccess(Channel channel) {
                        Rlog.d(NetworkRelayChannelServer.TAG, "onChannelCreateSuccess channelId = " + channel.getChannelId());
                        if (!NetworkRelayChannelServer.this.channelMapObject.containsKey(Integer.valueOf(channel.getChannelId()))) {
                            NetworkRelayChannelServer.this.channelMapObject.put(Integer.valueOf(channel.getChannelId()), channel);
                        }
                        NetworkRelayServerController.getInstance(NetworkRelayChannelServer.this.mContext).enableNetworkListener(true);
                    }

                    public void onChannelCreateFailed(String str, ServiceName serviceName, int i2, int i3) {
                        Rlog.d(NetworkRelayChannelServer.TAG, "onChannelCreateFailed deviceId = " + str + ";\n serviceName = " + serviceName + ";\n channelId = " + i2 + ";\n code = " + i3);
                    }

                    public void onChannelReceive(Channel channel, Packet packet) {
                        Rlog.d(NetworkRelayChannelServer.TAG, "onChannelReceive channelId = " + channel.getChannelId() + ";\n p2pConnected = " + NetworkRelayManager.p2pConnected);
                        if (!SmartNetworkRelayScore.isSmartNetworkRelaySupport()) {
                            if (NetworkRelayManager.p2pConnected) {
                                NetworkRelayServerController.getInstance(NetworkRelayChannelServer.this.mContext).receiveRelayMessage(packet.asBytes(), "");
                            }
                        } else {
                            if (!NetworkRelayManager.p2pConnected) {
                                NetworkRelayServerController.getInstance(NetworkRelayChannelServer.this.mContext).enableWiFiRssi();
                            }
                            NetworkRelayServerController.getInstance(NetworkRelayChannelServer.this.mContext).receiveRelayMessage(packet.asBytes(), channel.getDeviceId());
                        }
                    }

                    public void onChannelRelease(Channel channel, int i2) {
                        Rlog.d(NetworkRelayChannelServer.TAG, "onChannelRelease deviceId = " + channel.getDeviceId() + ";\n channelId = " + channel.getChannelId() + ";\n role = " + channel.getChannelRole() + ";\n code = " + i2);
                        if (NetworkRelayChannelServer.this.channelMapObject.containsKey(Integer.valueOf(channel.getChannelId()))) {
                            NetworkRelayChannelServer.this.channelMapObject.remove(Integer.valueOf(channel.getChannelId()));
                        }
                        if (NetworkRelayChannelServer.this.channelMapObject.isEmpty()) {
                            NetworkRelayServerController.getInstance(NetworkRelayChannelServer.this.mContext).enableNetworkListener(false);
                        }
                        if (SmartNetworkRelayScore.isSmartNetworkRelaySupport() && NetworkRelayServerController.deviceBssidMapObject.containsKey(channel.getDeviceId())) {
                            NetworkRelayServerController.deviceBssidMapObject.remove(channel.getDeviceId());
                        }
                    }
                }, new HandlerExecutor(this.mServerHandler)) != 0 && (networkRelayServerHandler = this.mServerHandler) != null && (i = this.mRegisterChannelCount) < 10) {
                    this.mRegisterChannelCount = i + 1;
                    networkRelayServerHandler.sendMessageDelayed(networkRelayServerHandler.obtainMessage(2), this.mRegisterChannelCount * 60000);
                }
            } catch (Exception e) {
                Rlog.i(TAG, "registerNetworkChannelListener error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void unRegisterNetworkChannelListener() {
        NetworkRelayServerHandler networkRelayServerHandler;
        int i;
        try {
            try {
                if (this.mChannelManager == null) {
                    this.mChannelManager = ContinuityChannelManager.getInstance(this.mContext);
                }
                int iUnregisterChannelListener = this.mChannelManager.unregisterChannelListener(new ServiceName.Builder().setName(NETWORK_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build());
                Rlog.d(TAG, "unRegisterNetworkChannelListener code = " + iUnregisterChannelListener);
                if (iUnregisterChannelListener != 0 && (networkRelayServerHandler = this.mServerHandler) != null && (i = this.mUnregisterChannelCount) < 10) {
                    this.mUnregisterChannelCount = i + 1;
                    networkRelayServerHandler.sendMessageDelayed(networkRelayServerHandler.obtainMessage(3), this.mUnregisterChannelCount * 60000);
                }
            } catch (Exception e) {
                Rlog.i(TAG, "unRegisterNetworkChannelListener error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void destroyAllChannel() {
        if (this.mChannelManager != null) {
            Iterator it = this.channelMapObject.entrySet().iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) ((Map.Entry) it.next()).getKey()).intValue();
                this.mChannelManager.destroyChannel(iIntValue);
                Rlog.d(TAG, "try to destroyAllChannel =" + iIntValue);
            }
        }
    }

    private void initNetworkRelayServerHandler() {
        if (this.mServerHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, 10);
            handlerThread.start();
            this.mServerHandler = new NetworkRelayServerHandler(handlerThread.getLooper());
        }
    }

    class NetworkRelayServerHandler extends Handler {
        public NetworkRelayServerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Rlog.d(NetworkRelayChannelServer.TAG, "add service retry");
                NetworkRelayChannelServer.this.addNetworkRelayService();
                return;
            }
            if (i == 1) {
                Rlog.d(NetworkRelayChannelServer.TAG, "remove service retry");
                NetworkRelayChannelServer.this.removeNetworkRelayService();
            } else if (i == 2) {
                Rlog.d(NetworkRelayChannelServer.TAG, "register channel retry");
                NetworkRelayChannelServer.this.registerNetworkChannelListener();
            } else {
                if (i != 3) {
                    return;
                }
                Rlog.d(NetworkRelayChannelServer.TAG, "unRegister channel retry");
                NetworkRelayChannelServer.this.unRegisterNetworkChannelListener();
            }
        }
    }
}

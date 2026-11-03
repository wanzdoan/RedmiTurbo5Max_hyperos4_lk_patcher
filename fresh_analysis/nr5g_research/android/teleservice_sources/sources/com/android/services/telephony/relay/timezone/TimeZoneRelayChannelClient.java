package com.android.services.telephony.relay.timezone;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.telephony.Rlog;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ClientChannelOptions;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.continuity.networking.ServiceFilter;
import com.xiaomi.continuity.networking.ServiceListener;
import com.xiaomi.continuity.networking.TrustedDeviceInfo;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class TimeZoneRelayChannelClient {
    private static final int CREATE_RELAY_CHANNEL_TIMEOUT_IN_MILLIS = 10000;
    private static final int CREATE_SUCCESS = 0;
    private static final int EVENT_CREATE_CHANNEL_RETRY = 1;
    private static final int EVENT_SUBSCRIBE_SERVICE_RETRY = 0;
    private static final String PERSIST_SYS_TIMEZONE = "persist.sys.timezone";
    private static final String RELAY_TIME_ZONE_ID = "ralay_id";
    private static final String RELAY_TIME_ZONE_STATE = "ralay_state";
    private static final String RELAY_TIME_ZONE_STATE_CHANGED_ACTION = "com.android.phone.relay.timezone.STATE_CHANGE";
    private static final int RELAY_TIME_ZONE_STATE_NOT_READY = 0;
    private static final int RELAY_TIME_ZONE_STATE_READY = 1;
    private static final int RETRY_MAX = 5;
    private static final String TAG_CLIENT = "[MiuiLTZP] [RelayClient]";
    private static final String TIMEZONE_RELAY_PACKAGE_NAME = "com.android.phone";
    private static final String TIMEZONE_RELAY_SERVICE_NAME = "timezoneRelay";
    private static TimeZoneRelayChannelClient sInstance;
    private ChannelListener mChannelListener;
    private ContinuityChannelManager mChannelManager;
    private TimeZoneRelayClientHandler mClientHandler;
    private Context mContext;
    private String mDeviceId;
    private NetworkingManager mNetworkingManager;
    private ServiceListener mServiceListener;
    private int mSubscribeServiceRetryCount = 0;
    private int mCreateChannelRetryCount = 0;
    private int mChannelId = 0;

    public static synchronized TimeZoneRelayChannelClient getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new TimeZoneRelayChannelClient(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private TimeZoneRelayChannelClient(Context context) {
        this.mContext = context;
        initTimeZoneRelayClientHandler();
    }

    private void initTimeZoneRelayClientHandler() {
        if (this.mClientHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG_CLIENT, 10);
            handlerThread.start();
            this.mClientHandler = new TimeZoneRelayClientHandler(handlerThread.getLooper());
        }
    }

    public synchronized void addSubscribeService() {
        TimeZoneRelayClientHandler timeZoneRelayClientHandler;
        int i;
        Context context;
        try {
            try {
                if (this.mNetworkingManager == null && (context = this.mContext) != null) {
                    this.mNetworkingManager = NetworkingManager.getInstance(context);
                }
                ServiceFilter serviceFilter = new ServiceFilter();
                ArrayList arrayList = new ArrayList();
                arrayList.add(1);
                serviceFilter.setTrustedTypeFilter(arrayList);
                serviceFilter.setServiceFilter(new ServiceName("com.android.phone", TIMEZONE_RELAY_SERVICE_NAME));
                ServiceListener serviceListener = new ServiceListener() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayChannelClient.1
                    public void onServiceOnline(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
                        String deviceId = trustedDeviceInfo.getDeviceId();
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onServiceOnline mServiceName = " + businessServiceInfo.getServiceName() + ";\n packageName = " + businessServiceInfo.getPackageName() + ";\n deviceId = " + deviceId + ";\n deviceType = " + trustedDeviceInfo.getDeviceType() + ";\n deviceName = " + trustedDeviceInfo.getDeviceName() + ";\n mDeviceId = " + TimeZoneRelayChannelClient.this.mDeviceId + ";\n mChannelId = " + TimeZoneRelayChannelClient.this.mChannelId);
                        if (TextUtils.isEmpty(deviceId)) {
                            return;
                        }
                        if (TextUtils.isEmpty(TimeZoneRelayChannelClient.this.mDeviceId) || (deviceId.equals(TimeZoneRelayChannelClient.this.mDeviceId) && TimeZoneRelayChannelClient.this.mChannelId == 0)) {
                            TimeZoneRelayChannelClient.this.createChannel(deviceId, true);
                            TimeZoneRelayChannelClient.this.mDeviceId = deviceId;
                        }
                    }

                    public void onServiceOffline(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo, int i2) {
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onServiceOffline mServiceName = " + businessServiceInfo.getServiceName() + ";\n packageName = " + businessServiceInfo.getPackageName() + ";\n deviceId = " + trustedDeviceInfo.getDeviceId() + ";\n deviceType = " + trustedDeviceInfo.getDeviceType() + ";\n deviceName = " + trustedDeviceInfo.getDeviceName() + ";\n reason = " + i2);
                        if (trustedDeviceInfo.getDeviceId().equals(TimeZoneRelayChannelClient.this.mDeviceId)) {
                            TimeZoneRelayChannelClient.this.mDeviceId = null;
                            TimeZoneRelayChannelClient.this.sendMessage(0, "");
                            if (TimeZoneRelayChannelClient.this.mChannelId != 0) {
                                TimeZoneRelayChannelClient.this.destroyChannel();
                            }
                        }
                    }

                    public void onDeviceChanged(TrustedDeviceInfo trustedDeviceInfo) {
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onDeviceChanged mDeviceId = " + trustedDeviceInfo.getDeviceId() + ";\n deviceTyp = e" + trustedDeviceInfo.getDeviceType() + ";\n deviceName = " + trustedDeviceInfo.getDeviceName());
                    }

                    public void onServiceChanged(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onServiceChanged mServiceName = " + businessServiceInfo.getServiceName() + ";\n packageName = " + businessServiceInfo.getPackageName() + ";\n deviceId = " + trustedDeviceInfo.getDeviceId() + ";\n deviceType = " + trustedDeviceInfo.getDeviceType() + ";\n deviceName = " + trustedDeviceInfo.getDeviceName());
                        String deviceId = trustedDeviceInfo.getDeviceId();
                        if (TextUtils.isEmpty(deviceId)) {
                            return;
                        }
                        if (TextUtils.isEmpty(TimeZoneRelayChannelClient.this.mDeviceId) || (deviceId.equals(TimeZoneRelayChannelClient.this.mDeviceId) && TimeZoneRelayChannelClient.this.mChannelId == 0)) {
                            TimeZoneRelayChannelClient.this.createChannel(deviceId, true);
                            TimeZoneRelayChannelClient.this.mDeviceId = deviceId;
                        }
                    }
                };
                this.mServiceListener = serviceListener;
                NetworkingManager networkingManager = this.mNetworkingManager;
                if (networkingManager != null) {
                    int iAddServiceListener = networkingManager.addServiceListener(serviceFilter, serviceListener);
                    Rlog.d(TAG_CLIENT, "addSubscribeService code = " + iAddServiceListener);
                    if (iAddServiceListener != 0 && (timeZoneRelayClientHandler = this.mClientHandler) != null && (i = this.mSubscribeServiceRetryCount) < 5) {
                        this.mSubscribeServiceRetryCount = i + 1;
                        timeZoneRelayClientHandler.sendMessageDelayed(timeZoneRelayClientHandler.obtainMessage(0), this.mSubscribeServiceRetryCount * 30000);
                    }
                }
            } catch (Exception e) {
                Rlog.i(TAG_CLIENT, "addSubscribeService error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    class TimeZoneRelayClientHandler extends Handler {
        public TimeZoneRelayClientHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "subscribe service retry");
                TimeZoneRelayChannelClient.this.addSubscribeService();
            } else {
                if (i != 1) {
                    return;
                }
                Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "create channel retry");
                TimeZoneRelayChannelClient.this.createChannel((String) message.obj, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void createChannel(String str, boolean z) {
        int i;
        Context context;
        try {
            if (this.mChannelManager == null && (context = this.mContext) != null) {
                this.mChannelManager = ContinuityChannelManager.getInstance(context);
            }
            ServiceName serviceNameBuild = new ServiceName.Builder().setName(TIMEZONE_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build();
            ClientChannelOptions clientChannelOptions = new ClientChannelOptions();
            if (z) {
                clientChannelOptions.setConnectMediumType(0);
            } else {
                clientChannelOptions.setConnectMediumType(1);
            }
            clientChannelOptions.setTrustLevel(16);
            clientChannelOptions.setTimeout(10000);
            ChannelListener channelListener = new ChannelListener() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayChannelClient.2
                public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
                }

                public void onChannelConfirm(String str2, ServiceName serviceName, int i2, ConfirmInfo confirmInfo) {
                    if (TimeZoneRelayChannelClient.this.mContext != null) {
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelConfirm channelId = " + i2);
                        ContinuityChannelManager.getInstance(TimeZoneRelayChannelClient.this.mContext).confirmChannel(i2, 0);
                    }
                }

                public void onChannelCreateSuccess(Channel channel) {
                    if (channel != null) {
                        TimeZoneRelayChannelClient.this.mChannelId = channel.getChannelId();
                        Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelCreateSuccess channelId = " + TimeZoneRelayChannelClient.this.mChannelId);
                    }
                }

                public void onChannelCreateFailed(String str2, ServiceName serviceName, int i2, int i3) {
                    Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelCreateFailed deviceId = " + str2 + ";\n serviceName = " + serviceName + ";\n channelId = " + i2 + ";\n code = " + i3);
                    if (TimeZoneRelayChannelClient.this.mCreateChannelRetryCount < 5) {
                        TimeZoneRelayChannelClient.this.mCreateChannelRetryCount++;
                        TimeZoneRelayChannelClient.this.mClientHandler.sendMessageDelayed(TimeZoneRelayChannelClient.this.mClientHandler.obtainMessage(1, str2), TimeZoneRelayChannelClient.this.mCreateChannelRetryCount * 30000);
                    }
                }

                public void onChannelReceive(Channel channel, Packet packet) {
                    Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelReceive channelId = " + channel.getChannelId());
                    try {
                        byte[] bArrAsBytes = packet.asBytes();
                        if (bArrAsBytes == null || bArrAsBytes.length == 0) {
                            return;
                        }
                        TimeZoneRelayChannelClient.this.sendMessage(1, new String(bArrAsBytes, StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        Rlog.i(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelReceive error = " + e);
                    }
                }

                public void onChannelRelease(Channel channel, int i2) {
                    Rlog.d(TimeZoneRelayChannelClient.TAG_CLIENT, "onChannelRelease deviceId = " + channel.getDeviceId() + ";\n role = " + channel.getChannelRole() + ";\n code = " + i2);
                    TimeZoneRelayChannelClient.this.mChannelId = 0;
                }
            };
            this.mChannelListener = channelListener;
            ContinuityChannelManager continuityChannelManager = this.mChannelManager;
            if (continuityChannelManager != null && this.mClientHandler != null) {
                int iCreateChannel = continuityChannelManager.createChannel(str, serviceNameBuild, clientChannelOptions, channelListener, new HandlerExecutor(this.mClientHandler));
                Rlog.d(TAG_CLIENT, "createChannel code = " + iCreateChannel);
                if (iCreateChannel < 0 && (i = this.mCreateChannelRetryCount) < 5) {
                    this.mCreateChannelRetryCount = i + 1;
                    TimeZoneRelayClientHandler timeZoneRelayClientHandler = this.mClientHandler;
                    timeZoneRelayClientHandler.sendMessageDelayed(timeZoneRelayClientHandler.obtainMessage(1, str), this.mCreateChannelRetryCount * 30000);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int i, String str) {
        Rlog.d(TAG_CLIENT, "sendMessage state= " + i + "; timeZone = " + str);
        if (this.mContext != null) {
            Intent intent = new Intent(RELAY_TIME_ZONE_STATE_CHANGED_ACTION);
            intent.putExtra(RELAY_TIME_ZONE_ID, str);
            intent.putExtra(RELAY_TIME_ZONE_STATE, i);
            this.mContext.sendBroadcast(intent);
        }
        destroyChannel();
    }

    public void removeSubscribeService() {
        NetworkingManager networkingManager;
        Context context;
        try {
            if (this.mNetworkingManager == null && (context = this.mContext) != null) {
                this.mNetworkingManager = NetworkingManager.getInstance(context);
            }
            ServiceListener serviceListener = this.mServiceListener;
            if (serviceListener != null && (networkingManager = this.mNetworkingManager) != null) {
                networkingManager.removeServiceListener(serviceListener);
                this.mServiceListener = null;
                this.mDeviceId = null;
                sendMessage(0, "");
                Rlog.d(TAG_CLIENT, "removeServiceListener");
            }
            destroyChannelAndRetry();
        } catch (Exception e) {
            Rlog.i(TAG_CLIENT, "removeSubscribeService error = " + e);
        }
    }

    private void destroyChannelAndRetry() {
        if (this.mChannelListener != null) {
            destroyChannel();
        }
        TimeZoneRelayClientHandler timeZoneRelayClientHandler = this.mClientHandler;
        if (timeZoneRelayClientHandler != null) {
            timeZoneRelayClientHandler.removeCallbacksAndMessages(null);
        }
        this.mSubscribeServiceRetryCount = 0;
        this.mCreateChannelRetryCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyChannel() {
        int i;
        ContinuityChannelManager continuityChannelManager = this.mChannelManager;
        if (continuityChannelManager == null || (i = this.mChannelId) == 0) {
            return;
        }
        continuityChannelManager.destroyChannel(i);
        Rlog.d(TAG_CLIENT, "try to destroyChannel = " + this.mChannelId);
        this.mChannelId = 0;
    }
}

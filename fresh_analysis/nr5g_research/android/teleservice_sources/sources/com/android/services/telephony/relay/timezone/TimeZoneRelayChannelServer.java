package com.android.services.telephony.relay.timezone;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TimeZoneRelayChannelServer {
    private static final String BLE_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_GATT_ENABLE";
    private static final String BT_HALF_OPEN_WHITH_LIST = "com.xiaomi.continuity.bluetooth.EXTRA_RFCOMM_CLASS_TYPE";
    private static final int BT_HALF_OPEN_WHITH_LIST_CODE = 1111;
    private static final int CREATE_SUCCESS = 0;
    private static final int EVENT_ADD_SERVICE_RETRY = 0;
    private static final int EVENT_REGISTER_CHANNEL_RETRY = 2;
    private static final int EVENT_REMOVE_SERVICE_RETRY = 1;
    private static final int EVENT_UNREGISTER_CHANNEL_RETRY = 3;
    private static final String PERSIST_SYS_TIMEZONE = "persist.sys.timezone";
    private static final int RETRY_MAX = 5;
    private static final String TAG_SERVER = "[MiuiLTZP] [RelayServer]";
    private static final String TIMEZONE_RELAY_PACKAGE_NAME = "com.android.phone";
    private static final String TIMEZONE_RELAY_SERVICE_NAME = "timezoneRelay";
    private static final int TRANSFER_SUCCESS = 0;
    private static int mServiceData = 1;
    private static TimeZoneRelayChannelServer sInstance;
    private ContinuityChannelManager mChannelManager;
    private Context mContext;
    private NetworkingManager mNetworkingManager;
    private TimeZoneRelayServerHandler mServerHandler;
    private int mAddServiceRetryCount = 0;
    private int mRegisterChannelCount = 0;
    private int mRemoveServiceRetryCount = 0;
    private int mUnregisterChannelCount = 0;
    private boolean mDestoryProcessing = false;
    private HashMap channelMapObject = new HashMap();

    public static synchronized TimeZoneRelayChannelServer getInstance(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new TimeZoneRelayChannelServer(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private TimeZoneRelayChannelServer(Context context) {
        this.mContext = context;
        initTimeZoneRelayServerHandler();
    }

    public synchronized void addTimeZoneRelayService() {
        int i;
        Context context;
        try {
            try {
                if (this.mNetworkingManager == null && (context = this.mContext) != null) {
                    this.mNetworkingManager = NetworkingManager.getInstance(context);
                }
                BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
                businessServiceInfo.setServiceName(TIMEZONE_RELAY_SERVICE_NAME);
                mServiceData = (mServiceData % 100) + 1;
                Rlog.d(TAG_SERVER, "addTimeZoneRelayService mServiceData = " + mServiceData);
                businessServiceInfo.setServiceData(getRelayServiceData(mServiceData));
                NetworkingManager networkingManager = this.mNetworkingManager;
                if (networkingManager != null) {
                    int iAddServiceInfo = networkingManager.addServiceInfo(businessServiceInfo);
                    Rlog.d(TAG_SERVER, "addTimeZoneRelayService code = " + iAddServiceInfo);
                    if (iAddServiceInfo == 0) {
                        registerTimeZoneChannelListener();
                    } else {
                        TimeZoneRelayServerHandler timeZoneRelayServerHandler = this.mServerHandler;
                        if (timeZoneRelayServerHandler != null && (i = this.mAddServiceRetryCount) < 5) {
                            this.mAddServiceRetryCount = i + 1;
                            timeZoneRelayServerHandler.sendMessageDelayed(timeZoneRelayServerHandler.obtainMessage(0), this.mAddServiceRetryCount * 30000);
                        }
                    }
                }
            } catch (Exception e) {
                Rlog.i(TAG_SERVER, "addTimeZoneRelayService error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private byte[] getRelayServiceData(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

    public synchronized void removeTimeZoneRelayService() {
        int i;
        Context context;
        try {
            try {
                if (this.mNetworkingManager == null && (context = this.mContext) != null) {
                    this.mNetworkingManager = NetworkingManager.getInstance(context);
                }
                BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
                businessServiceInfo.setServiceName(TIMEZONE_RELAY_SERVICE_NAME);
                NetworkingManager networkingManager = this.mNetworkingManager;
                if (networkingManager != null) {
                    int iRemoveServiceInfo = networkingManager.removeServiceInfo(businessServiceInfo);
                    Rlog.d(TAG_SERVER, "removeTimeZoneRelayService code = " + iRemoveServiceInfo);
                    TimeZoneRelayServerHandler timeZoneRelayServerHandler = this.mServerHandler;
                    if (iRemoveServiceInfo == 0) {
                        if (timeZoneRelayServerHandler != null) {
                            timeZoneRelayServerHandler.removeCallbacksAndMessages(null);
                        }
                        this.mAddServiceRetryCount = 0;
                        this.mRemoveServiceRetryCount = 0;
                        this.mRegisterChannelCount = 0;
                        this.mUnregisterChannelCount = 0;
                    } else if (timeZoneRelayServerHandler != null && (i = this.mRemoveServiceRetryCount) < 5) {
                        this.mRemoveServiceRetryCount = i + 1;
                        timeZoneRelayServerHandler.sendMessageDelayed(timeZoneRelayServerHandler.obtainMessage(1), this.mRemoveServiceRetryCount * 30000);
                    }
                }
                unregisterTimeZoneChannelListener();
                destroyAllChannel();
                this.channelMapObject.clear();
            } catch (Exception e) {
                Rlog.i(TAG_SERVER, "removeTimeZoneRelayService error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTimeZoneChannelListener() {
        TimeZoneRelayServerHandler timeZoneRelayServerHandler;
        int i;
        Context context;
        try {
            if (this.mChannelManager == null && (context = this.mContext) != null) {
                this.mChannelManager = ContinuityChannelManager.getInstance(context);
            }
            ServiceName serviceNameBuild = new ServiceName.Builder().setName(TIMEZONE_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build();
            ServerChannelOptions serverChannelOptions = new ServerChannelOptions(16);
            ContinuityChannelManager continuityChannelManager = this.mChannelManager;
            if (continuityChannelManager == null || this.mServerHandler == null || continuityChannelManager.registerChannelListener(serviceNameBuild, serverChannelOptions, new ChannelListener() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayChannelServer.1
                public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
                }

                public void onChannelConfirm(String str, ServiceName serviceName, int i2, ConfirmInfo confirmInfo) {
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelConfirm channelId = " + i2);
                    TimeZoneRelayChannelServer.this.mChannelManager.confirmChannel(i2, 0);
                }

                public void onChannelCreateSuccess(Channel channel) {
                    int channelId = channel.getChannelId();
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelCreateSuccess channelId = " + channelId);
                    if (!TimeZoneRelayChannelServer.this.channelMapObject.containsKey(Integer.valueOf(channelId))) {
                        TimeZoneRelayChannelServer.this.channelMapObject.put(Integer.valueOf(channelId), channel);
                    }
                    String str = SystemProperties.get(TimeZoneRelayChannelServer.PERSIST_SYS_TIMEZONE);
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelCreateSuccess phoneTimeZone = " + str);
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    TimeZoneRelayChannelServer.this.sendMessage(channelId, str);
                }

                public void onChannelCreateFailed(String str, ServiceName serviceName, int i2, int i3) {
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelCreateFailed deviceId = " + str + ";\n serviceName = " + serviceName + ";\n channelId = " + i2 + ";\n code = " + i3);
                }

                public void onChannelReceive(Channel channel, Packet packet) {
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelReceive channelId = " + channel.getChannelId());
                }

                public void onChannelRelease(Channel channel, int i2) {
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onChannelRelease deviceId = " + channel.getDeviceId() + ";\n channelId = " + channel.getChannelId() + ";\n role = " + channel.getChannelRole() + ";\n code = " + i2);
                    if (TimeZoneRelayChannelServer.this.mDestoryProcessing || !TimeZoneRelayChannelServer.this.channelMapObject.containsKey(Integer.valueOf(channel.getChannelId()))) {
                        return;
                    }
                    TimeZoneRelayChannelServer.this.channelMapObject.remove(Integer.valueOf(channel.getChannelId()));
                }
            }, new HandlerExecutor(this.mServerHandler)) == 0 || (timeZoneRelayServerHandler = this.mServerHandler) == null || (i = this.mRegisterChannelCount) >= 5) {
                return;
            }
            this.mRegisterChannelCount = i + 1;
            timeZoneRelayServerHandler.sendMessageDelayed(timeZoneRelayServerHandler.obtainMessage(2), this.mRegisterChannelCount * 30000);
        } catch (Exception e) {
            Rlog.i(TAG_SERVER, "registerTimeZoneChannelListener error = " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void unregisterTimeZoneChannelListener() {
        TimeZoneRelayServerHandler timeZoneRelayServerHandler;
        int i;
        Context context;
        try {
            try {
                if (this.mChannelManager == null && (context = this.mContext) != null) {
                    this.mChannelManager = ContinuityChannelManager.getInstance(context);
                }
                ServiceName serviceNameBuild = new ServiceName.Builder().setName(TIMEZONE_RELAY_SERVICE_NAME).setPackageName("com.android.phone").build();
                ContinuityChannelManager continuityChannelManager = this.mChannelManager;
                if (continuityChannelManager != null) {
                    int iUnregisterChannelListener = continuityChannelManager.unregisterChannelListener(serviceNameBuild);
                    Rlog.d(TAG_SERVER, "unregisterTimeZoneChannelListener code = " + iUnregisterChannelListener);
                    if (iUnregisterChannelListener != 0 && (timeZoneRelayServerHandler = this.mServerHandler) != null && (i = this.mUnregisterChannelCount) < 5) {
                        this.mUnregisterChannelCount = i + 1;
                        timeZoneRelayServerHandler.sendMessageDelayed(timeZoneRelayServerHandler.obtainMessage(3), this.mUnregisterChannelCount * 30000);
                    }
                }
            } catch (Exception e) {
                Rlog.i(TAG_SERVER, "unregisterTimeZoneChannelListener error = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void initTimeZoneRelayServerHandler() {
        if (this.mServerHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG_SERVER, 10);
            handlerThread.start();
            this.mServerHandler = new TimeZoneRelayServerHandler(handlerThread.getLooper());
        }
    }

    class TimeZoneRelayServerHandler extends Handler {
        public TimeZoneRelayServerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "add service retry");
                TimeZoneRelayChannelServer.this.addTimeZoneRelayService();
                return;
            }
            if (i == 1) {
                Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "remove service retry");
                TimeZoneRelayChannelServer.this.removeTimeZoneRelayService();
            } else if (i == 2) {
                Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "register channel retry");
                TimeZoneRelayChannelServer.this.registerTimeZoneChannelListener();
            } else {
                if (i != 3) {
                    return;
                }
                Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "unregister channel retry");
                TimeZoneRelayChannelServer.this.unregisterTimeZoneChannelListener();
            }
        }
    }

    private void destroyAllChannel() {
        if (this.mChannelManager != null) {
            synchronized (this) {
                try {
                    Iterator it = this.channelMapObject.entrySet().iterator();
                    while (it.hasNext()) {
                        int iIntValue = ((Integer) ((Map.Entry) it.next()).getKey()).intValue();
                        it.remove();
                        this.mDestoryProcessing = true;
                        this.mChannelManager.destroyChannel(iIntValue);
                        Rlog.d(TAG_SERVER, "try to destroyAllChannel = " + iIntValue);
                    }
                    this.mDestoryProcessing = false;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void sendMessageAllChannel() {
        String str = SystemProperties.get(PERSIST_SYS_TIMEZONE);
        Rlog.d(TAG_SERVER, "sendMessageAllChannel phoneTimeZone = " + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Iterator it = this.channelMapObject.entrySet().iterator();
        while (it.hasNext()) {
            sendMessage(((Integer) ((Map.Entry) it.next()).getKey()).intValue(), str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(final int i, String str) {
        Channel channel;
        Packet packetFromBytes = Packet.fromBytes(str.getBytes(StandardCharsets.UTF_8));
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(BT_HALF_OPEN_WHITH_LIST, BT_HALF_OPEN_WHITH_LIST_CODE);
            bundle.putBoolean(BLE_HALF_OPEN_WHITH_LIST, true);
            packetFromBytes.putExtras(bundle);
            Rlog.d(TAG_SERVER, "sendMessage channelId = " + i);
            if (!this.channelMapObject.containsKey(Integer.valueOf(i)) || (channel = (Channel) this.channelMapObject.get(Integer.valueOf(i))) == null || this.mServerHandler == null || this.mChannelManager == null) {
                return;
            }
            channel.send(packetFromBytes, new PacketTransferProgressCallback() { // from class: com.android.services.telephony.relay.timezone.TimeZoneRelayChannelServer.2
                public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
                    int transferState = packetTransferProgress.getTransferState();
                    Rlog.d(TimeZoneRelayChannelServer.TAG_SERVER, "onPacketTransferProgressUpdate =" + transferState);
                    if (transferState != 1) {
                        TimeZoneRelayChannelServer.this.mChannelManager.destroyChannel(i);
                    }
                }
            }, new HandlerExecutor(this.mServerHandler));
        } catch (Exception e) {
            Rlog.i(TAG_SERVER, "sendMessage error = " + e);
        }
    }
}

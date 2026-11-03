package com.android.services.telephony.relay.phonecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.services.telephony.relay.DeviceInRelayChangeCallback;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ClientChannelOptions;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.channel.PacketTransferProgressCallback;
import com.xiaomi.continuity.channel.ServerChannelOptions;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.continuity.networking.ServiceFilter;
import com.xiaomi.continuity.networking.ServiceListener;
import com.xiaomi.continuity.networking.TrustedDeviceInfo;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class PadContinuityController {
    private static final Context CONTEXT = RelayUtils.System.getContext();
    private static final int CREATE_RELAY_CHANNEL_TIMEOUT_IN_MILLIS = 10000;
    private static final int MSG_STOP_PAD_DIST_AUDIO_SERVICE = 2;
    private static final String TAG = "PadContinuityController";
    private static PadContinuityController sInstance;
    private ContinuityChannelManager mChannelManager;
    private BroadcastReceiver mConnectivityReceiver;
    private String mDeviceInRelay;
    private DeviceInRelayChangeCallback mDeviceInRelayChangeCallback;
    private boolean mIsAddRelayPhoneServiceListener;
    private boolean mIsAddRelayService;
    private boolean mIsRegisterRelayChannelListener;
    private NetworkingManager mNetworkingManager;
    private RelayChannelInfo mRelayChannelInfo;
    private RelayManager mRelayManager;
    private RelayServiceInfo mRelayPhoneServiceInfo;
    private RelayServiceInfo mRelayPhoneWlanServiceInfo;
    private final MyHandler mHandler = new MyHandler(this, Looper.getMainLooper());
    private int mRelayPhoneVersion = -1;
    private final ServiceName mRelayServiceName = new ServiceName.Builder().setName(RelayUtils.Relay.RELAY_SERVICE_NAME).setPackageName(CONTEXT.getPackageName()).build();

    public static synchronized PadContinuityController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new PadContinuityController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private PadContinuityController() {
    }

    public void registerConnectivityReceiver() {
        if (this.mConnectivityReceiver == null) {
            this.mConnectivityReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    int intExtra = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1);
                    boolean zIsLyraSupportRelay = RelayUtils.Relay.isLyraSupportRelay();
                    RelayLog.logi(PadContinuityController.TAG, "onReceive action:" + action + ", state:" + intExtra + ", isRelayEnable:" + zIsLyraSupportRelay);
                    if (zIsLyraSupportRelay) {
                        RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    PadContinuityController.this.init();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_ON");
            intentFilter.addAction("com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_OFF");
            CONTEXT.registerReceiver(this.mConnectivityReceiver, intentFilter, 2);
        }
    }

    public void init() {
        if (!RelayUtils.Relay.isLyraSupportRelay()) {
            RelayLog.logi(TAG, "Lyra not support.");
            return;
        }
        if (this.mNetworkingManager == null) {
            try {
                this.mNetworkingManager = NetworkingManager.getInstance(CONTEXT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder("Create network manager, success=");
            sb.append(this.mNetworkingManager != null);
            RelayLog.logi(TAG, sb.toString());
            if (this.mNetworkingManager != null) {
                addRelayService(false);
                addRelayPhoneServiceListener(false);
                this.mNetworkingManager.registerDeathCallback(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.2
                    @Override // java.lang.Runnable
                    public void run() {
                        RelayLog.logi(PadContinuityController.TAG, "registerDeathCallback");
                        RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                PadContinuityController.this.addRelayService(true);
                                PadContinuityController.this.addRelayPhoneServiceListener(true);
                                PadContinuityController.this.registerRelayChannelListener(true);
                            }
                        });
                    }
                });
            }
        } else {
            if (!this.mIsAddRelayService) {
                RelayLog.logi(TAG, "addRelayService, retry!");
                addRelayService(false);
            }
            if (!this.mIsAddRelayPhoneServiceListener) {
                RelayLog.logi(TAG, "addRelayPhoneServiceListener, retry!");
                addRelayPhoneServiceListener(false);
            }
            RelayLog.logi(TAG, "Network manager already created.");
        }
        if (this.mChannelManager == null) {
            try {
                this.mChannelManager = ContinuityChannelManager.getInstance(CONTEXT);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            StringBuilder sb2 = new StringBuilder("Create channel manager, success=");
            sb2.append(this.mChannelManager != null);
            RelayLog.logi(TAG, sb2.toString());
        } else {
            RelayLog.logi(TAG, "Channel manager already created.");
        }
        if (this.mChannelManager != null && !this.mIsRegisterRelayChannelListener) {
            registerRelayChannelListener(false);
        }
        if (this.mRelayManager == null) {
            RelayManager relayManager = new RelayManager();
            this.mRelayManager = relayManager;
            relayManager.createRelayPhoneAccount();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRelayService(boolean z) {
        try {
            BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
            businessServiceInfo.setServiceName(RelayUtils.Relay.RELAY_SERVICE_NAME);
            boolean z2 = true;
            businessServiceInfo.setServiceData(getRelayServiceVersion(1));
            int iAddServiceInfo = this.mNetworkingManager.addServiceInfo(businessServiceInfo);
            RelayLog.logi(TAG, "addRelayService, code:" + iAddServiceInfo);
            if (iAddServiceInfo != 0) {
                z2 = false;
            }
            this.mIsAddRelayService = z2;
            if (iAddServiceInfo == 0 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.3
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(PadContinuityController.TAG, "addRelayService, retry!");
                    PadContinuityController.this.addRelayService(true);
                }
            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getRelayServiceVersion(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRelayPhoneServiceListener(boolean z) {
        try {
            ServiceFilter serviceFilter = new ServiceFilter();
            ArrayList arrayList = new ArrayList();
            boolean z2 = true;
            arrayList.add(1);
            serviceFilter.setTrustedTypeFilter(arrayList);
            serviceFilter.setServiceFilter(new ServiceName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, RelayUtils.Relay.RELAY_PHONE_SERVICE_NAME));
            int iAddServiceListener = this.mNetworkingManager.addServiceListener(serviceFilter, new AnonymousClass4());
            RelayLog.logd(TAG, "addRelayPhoneServiceListener, code:" + iAddServiceListener);
            if (iAddServiceListener != 0 && iAddServiceListener != 1301000) {
                z2 = false;
            }
            this.mIsAddRelayPhoneServiceListener = z2;
            if (iAddServiceListener == 0 || iAddServiceListener == 1301000 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.5
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(PadContinuityController.TAG, "addRelayPhoneServiceListener, retry!");
                    PadContinuityController.this.addRelayPhoneServiceListener(false);
                }
            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PadContinuityController$4, reason: invalid class name */
    class AnonymousClass4 implements ServiceListener {
        AnonymousClass4() {
        }

        public void onServiceOnline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$4$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceOnline$0(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceOnline$0(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayLog.logd(PadContinuityController.TAG, "onServiceOnline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
                byte[] serviceData = businessServiceInfo.getServiceData();
                if (serviceData != null && serviceData.length == 4) {
                    PadContinuityController padContinuityController = PadContinuityController.this;
                    padContinuityController.mRelayPhoneVersion = padContinuityController.getRelayServiceVersionInt(serviceData);
                    RelayLog.logd(PadContinuityController.TAG, "onServiceOnline:version:" + PadContinuityController.this.mRelayPhoneVersion);
                }
                PadContinuityController.this.mRelayPhoneServiceInfo = new RelayServiceInfo(businessServiceInfo.getPackageName(), businessServiceInfo.getServiceName(), businessServiceInfo.getServiceData(), trustedDeviceInfo.getDeviceId(), trustedDeviceInfo.getDeviceName(), trustedDeviceInfo.getDeviceType(), PadContinuityController.this.mRelayPhoneVersion);
            }
        }

        public void onServiceOffline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo, int i) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$4$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PadContinuityController.AnonymousClass4.$r8$lambda$IopS1OULfaeGxHe4I0WNSRIr3kU(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$IopS1OULfaeGxHe4I0WNSRIr3kU(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayLog.logd(PadContinuityController.TAG, "onServiceOffline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
            }
        }

        public void onDeviceChanged(final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDeviceChanged$2(trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeviceChanged$2(TrustedDeviceInfo trustedDeviceInfo) {
            RelayLog.logd(PadContinuityController.TAG, "onDeviceChanged:deviceInfo:" + trustedDeviceInfo + ", hasWlan:" + trustedDeviceInfo.hasWlan());
            if (!trustedDeviceInfo.hasWlan() || TextUtils.isEmpty(trustedDeviceInfo.getDeviceId()) || PadContinuityController.this.mRelayPhoneServiceInfo == null || !trustedDeviceInfo.getDeviceId().equals(PadContinuityController.this.mRelayPhoneServiceInfo.mDeviceId)) {
                return;
            }
            PadContinuityController padContinuityController = PadContinuityController.this;
            padContinuityController.mRelayPhoneWlanServiceInfo = padContinuityController.mRelayPhoneServiceInfo;
        }

        public void onServiceChanged(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        RelayLog.logd(PadContinuityController.TAG, "onServiceChanged:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
                    }
                });
            }
        }
    }

    public int getRelayServiceVersionInt(byte[] bArr) {
        return (bArr[0] & TransitionInfo.INIT) | ((bArr[1] & TransitionInfo.INIT) << 8) | ((bArr[2] & TransitionInfo.INIT) << 16) | ((bArr[3] & TransitionInfo.INIT) << 24);
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PadContinuityController$6, reason: invalid class name */
    class AnonymousClass6 implements ChannelListener {
        AnonymousClass6() {
        }

        public void onChannelConfirm(String str, final ServiceName serviceName, final int i, ConfirmInfo confirmInfo) {
            RelayLog.logd(PadContinuityController.TAG, "onChannelConfirm:\n------------------------------------\ndeviceId: " + str + "\nserviceName: " + serviceName.toString() + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChannelConfirm$0(serviceName, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChannelConfirm$0(ServiceName serviceName, int i) {
            if (RelayUtils.Relay.isRelayService(serviceName.getName())) {
                PadContinuityController.this.confirmRelayChannel(i, !PadContinuityController.this.isCreateChannel() ? 1 : 0);
            }
        }

        public void onChannelCreateSuccess(Channel channel) {
            if (channel == null) {
                RelayLog.logd(PadContinuityController.TAG, "onChannelCreateSuccess");
                return;
            }
            RelayLog.logd(PadContinuityController.TAG, "onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
            boolean zIsCreateChannel = PadContinuityController.this.isCreateChannel();
            boolean z = zIsCreateChannel && PadContinuityController.this.mRelayChannelInfo != null;
            RelayLog.logd(PadContinuityController.TAG, "onChannelCreateSuccess: isCreateChannel:" + zIsCreateChannel + ", isDestroyChannel:" + z);
            if (z) {
                PadContinuityController padContinuityController = PadContinuityController.this;
                padContinuityController.destroyRelayChannel(padContinuityController.mRelayChannelInfo.mChannelId);
            }
            if (zIsCreateChannel) {
                PadContinuityController.this.mRelayChannelInfo = new RelayChannelInfo(channel.getDeviceId(), channel.getChannelId(), channel);
                PadContinuityController.this.mRelayManager.enableRelayPhoneAccount(true);
            }
        }

        public void onChannelCreateFailed(String str, ServiceName serviceName, int i, int i2) {
            RelayLog.logd(PadContinuityController.TAG, "onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\ncode: " + i2 + "\n------------------------------------");
        }

        public void onChannelReceive(Channel channel, Packet packet) {
            if (packet != null) {
                byte[] bArrAsBytes = packet.asBytes();
                PadContinuityController.this.onReceiveRelayMessage(new String(bArrAsBytes));
                if (channel == null) {
                    RelayLog.logd(PadContinuityController.TAG, "onChannelReceive");
                    return;
                }
                RelayLog.logd(PadContinuityController.TAG, "onChannelReceive:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\nmessage: " + RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, StandardCharsets.UTF_8)) + "\n------------------------------------");
            }
        }

        public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
            RelayLog.logd(PadContinuityController.TAG, "onChannelTransferProgressUpdate");
        }

        public void onChannelRelease(Channel channel, int i) {
            if (channel == null) {
                RelayLog.logd(PadContinuityController.TAG, "onChannelRelease");
            } else {
                RelayLog.logd(PadContinuityController.TAG, "onChannelRelease:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
            }
            if (channel == null || PadContinuityController.this.mRelayChannelInfo == null || channel.getChannelId() != PadContinuityController.this.mRelayChannelInfo.mChannelId) {
                return;
            }
            PadContinuityController.this.setDeviceInRelay(null);
            PadContinuityController.this.mRelayChannelInfo = null;
            for (RelayConnection relayConnection : PadContinuityController.this.mRelayManager.getRelayConnectionsManager().getRelayConnections()) {
                relayConnection.forceDisconnected(relayConnection.getDisconnectCause("Channel release"));
            }
            PadContinuityController.this.mRelayManager.enableRelayPhoneAccount(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRelayChannelListener(boolean z) {
        try {
            int iRegisterChannelListener = this.mChannelManager.registerChannelListener(this.mRelayServiceName, new ServerChannelOptions(16), new AnonymousClass6(), RelayUtils.MainThread.getMainExecutor());
            this.mIsRegisterRelayChannelListener = iRegisterChannelListener == 0;
            if (iRegisterChannelListener != 0 && z) {
                RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.7
                    @Override // java.lang.Runnable
                    public void run() {
                        RelayLog.logi(PadContinuityController.TAG, "registerRelayChannelListener, retry!");
                        PadContinuityController.this.registerRelayChannelListener(false);
                    }
                }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
            RelayLog.logi(TAG, "registerRelayChannelListener, code:" + iRegisterChannelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyRelayChannel(int i) {
        ContinuityChannelManager continuityChannelManager = this.mChannelManager;
        if (continuityChannelManager == null) {
            RelayLog.loge(TAG, "destroyRelayChannel...No channel manager created.");
            return;
        }
        continuityChannelManager.destroyChannel(i);
        RelayLog.logi(TAG, "destroyRelayChannel...channelId:" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCreateChannel() {
        return this.mRelayChannelInfo == null || TextUtils.isEmpty(getDeviceInRelay());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmRelayChannel(int i, int i2) {
        ContinuityChannelManager continuityChannelManager = this.mChannelManager;
        if (continuityChannelManager != null) {
            continuityChannelManager.confirmChannel(i, i2);
            RelayLog.logi(TAG, "confirmRelayChannel..." + i2);
        }
    }

    public void sendRelayMessage(String str) {
        Channel channel;
        if (this.mRelayChannelInfo == null) {
            RelayLog.logd(TAG, "The channel was not created successfully!");
            if (str.startsWith("relay://" + RelayApi.DIAL.getMethodName())) {
                sendCreateChannelRequest(str);
                return;
            }
            return;
        }
        Packet packetFromBytes = Packet.fromBytes(str.getBytes(StandardCharsets.UTF_8));
        RelayLog.logd(TAG, "send message deviceId:" + this.mRelayChannelInfo.mDeviceId);
        RelayChannelInfo relayChannelInfo = this.mRelayChannelInfo;
        if (relayChannelInfo == null || (channel = relayChannelInfo.mChannel) == null) {
            return;
        }
        channel.send(packetFromBytes, new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.8
            public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
            }
        }, RelayUtils.MainThread.getMainExecutor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceiveRelayMessage(String str) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.onRelayMessageReceived(str);
        }
    }

    public RelayChannelInfo getChannelInRelay() {
        return this.mRelayChannelInfo;
    }

    public String getDeviceInRelay() {
        RelayLog.logi(TAG, "getDeviceInRelay, " + this.mDeviceInRelay);
        return this.mDeviceInRelay;
    }

    public void setDeviceInRelay(String str) {
        boolean zEquals;
        boolean zIsEmpty = TextUtils.isEmpty(str);
        String str2 = this.mDeviceInRelay;
        if (zIsEmpty) {
            zEquals = TextUtils.isEmpty(str2);
        } else {
            zEquals = str.equals(str2);
        }
        if (!zEquals) {
            RelayLog.logi(TAG, "setDeviceInRelay, old:" + this.mDeviceInRelay + ", new:" + str);
            RelayChannelInfo relayChannelInfo = this.mRelayChannelInfo;
            if (relayChannelInfo != null) {
                RelayUtils.Relay.setIconVisibility(CONTEXT, relayChannelInfo.mDeviceId, "", 1, !TextUtils.isEmpty(str));
            }
            this.mDeviceInRelay = str;
            DeviceInRelayChangeCallback deviceInRelayChangeCallback = this.mDeviceInRelayChangeCallback;
            if (deviceInRelayChangeCallback != null) {
                deviceInRelayChangeCallback.onDeviceChange();
            }
        }
    }

    public RelayManager getRelayManager() {
        return this.mRelayManager;
    }

    public void setDeviceInRelayChangeCallback(DeviceInRelayChangeCallback deviceInRelayChangeCallback) {
        this.mDeviceInRelayChangeCallback = deviceInRelayChangeCallback;
    }

    public void sendCreateChannelRequest(String str) {
        if (this.mChannelManager == null) {
            RelayLog.loge(TAG, "createRelayPhoneChannel...No channel manager created.");
            return;
        }
        if (this.mRelayPhoneWlanServiceInfo == null) {
            RelayLog.loge(TAG, "createRelayPhoneChannel...No wlan relay phone service.");
            return;
        }
        ServiceName serviceNameBuild = new ServiceName.Builder().setName(this.mRelayPhoneWlanServiceInfo.mServiceName).setPackageName(this.mRelayPhoneWlanServiceInfo.mPackageName).build();
        ClientChannelOptions clientChannelOptions = new ClientChannelOptions();
        clientChannelOptions.setConnectMediumType(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        clientChannelOptions.setTrustLevel(16);
        clientChannelOptions.setTimeout(10000);
        RelayLog.logd(TAG, "createRelayPhoneChannel, code:" + this.mChannelManager.createChannel(this.mRelayPhoneWlanServiceInfo.mDeviceId, serviceNameBuild, clientChannelOptions, new AnonymousClass9(str), RelayUtils.MainThread.getMainExecutor()));
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PadContinuityController$9, reason: invalid class name */
    class AnonymousClass9 implements ChannelListener {
        final /* synthetic */ String val$messageString;

        public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
        }

        AnonymousClass9(String str) {
            this.val$messageString = str;
        }

        public void onChannelConfirm(String str, final ServiceName serviceName, final int i, ConfirmInfo confirmInfo) {
            RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelConfirm:\n------------------------------------\ndeviceId: " + str + "\nserviceName: " + serviceName + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChannelConfirm$0(serviceName, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChannelConfirm$0(ServiceName serviceName, int i) {
            if (RelayUtils.Relay.isRelayPhoneService(serviceName.getName())) {
                PadContinuityController.this.confirmRelayChannel(i, 0);
            }
        }

        public void onChannelCreateSuccess(Channel channel) {
            if (channel == null) {
                RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelCreateSuccess");
                return;
            }
            RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
            channel.send(Packet.fromBytes(this.val$messageString.getBytes(StandardCharsets.UTF_8)), new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.PadContinuityController.9.1
                public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
                }
            }, RelayUtils.MainThread.getMainExecutor());
        }

        public void onChannelCreateFailed(String str, ServiceName serviceName, int i, int i2) {
            RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\nerrorCode: " + i2 + "\n------------------------------------");
        }

        public void onChannelReceive(Channel channel, Packet packet) {
            if (packet != null) {
                byte[] bArrAsBytes = packet.asBytes();
                if (channel == null) {
                    RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelReceive");
                    return;
                }
                PadContinuityController.this.mChannelManager.destroyChannel(channel.getChannelId());
                RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelReceive:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\nmessage: " + RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, StandardCharsets.UTF_8)) + "\n------------------------------------");
            }
        }

        public void onChannelRelease(Channel channel, int i) {
            if (channel == null) {
                RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelRelease");
                return;
            }
            RelayLog.logd(PadContinuityController.TAG, "createRelayPhoneChannel, onChannelRelease:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
        }
    }

    public void onRelayStart() {
        this.mHandler.removeMessages(2);
        RelayUtils.Relay.startRelayAudioService(RelayUtils.System.getContext(), 1);
    }

    public void onRelayEnd() {
        this.mHandler.sendEmptyMessageDelayed(2, 1000L);
    }

    class MyHandler extends Handler {
        public MyHandler(PadContinuityController padContinuityController, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 2) {
                RelayUtils.Relay.stopRelayAudioService(RelayUtils.System.getContext());
            }
        }
    }
}

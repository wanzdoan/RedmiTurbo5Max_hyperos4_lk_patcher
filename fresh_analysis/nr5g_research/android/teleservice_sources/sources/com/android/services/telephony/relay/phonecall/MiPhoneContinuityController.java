package com.android.services.telephony.relay.phonecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.services.telephony.relay.DeviceInRelayChangeCallback;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;
import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;
import com.miui.audiomonitor.distaudio.utils.ResultCode;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ClientChannelOptionsV2;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.channel.PacketTransferProgressCallback;
import com.xiaomi.continuity.channel.ServerChannelOptionsV2;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.continuity.networking.ServiceFilter;
import com.xiaomi.continuity.networking.ServiceListener;
import com.xiaomi.continuity.networking.TrustedDeviceInfo;
import com.xiaomi.distaudiokit.DistAudioCallback;
import com.xiaomi.distaudiokit.MiAudioManager;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MiPhoneContinuityController {
    private static final String ACTION_MILINK_NOTIFICATION_CALL = "com.milink.service.voip.ACTION_VOIP_CALL";
    private static final String CONNECTION_SERVICE_CHANGED_ON = "com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_ON";
    private static final int CREATE_RELAY_CHANNEL_TIMEOUT_IN_MILLIS = 10000;
    private static final int DISCONNECT_FROM_SERVER = -5;
    private static final String EXTRA_VOIP_CALL_DEVICE_ID = "extra_sender_device_id";
    private static final String EXTRA_VOIP_CALL_DEVICE_NAME = "extra_sender_device_name";
    private static final String EXTRA_VOIP_CALL_NAME = "extra_title";
    private static final String EXTRA_VOIP_CALL_NUMBER_LOCAL = "extra_message";
    private static final int MSG_STOP_MIPHONE_DIST_AUDIO_SERVICE = 2;
    private static final String TAG = "MiPhoneContinuityController";
    private static final int USE_REMOTE_ENABLED = 1;
    private static MiPhoneContinuityController sInstance;
    private BroadcastReceiver mBroadcastReceiver;
    private ChannelListener mChannelListener;
    private ContinuityChannelManager mChannelManager;
    private String mDeviceInRelay;
    private DeviceInRelayChangeCallback mDeviceInRelayChangeCallback;
    private boolean mIsAddRelayPhoneServiceListener;
    private boolean mIsAddRelayService;
    private boolean mIsRegisterRelayChannelListener;
    private volatile boolean mIsRemoteType;
    private MiAudioManager mMiAudioManager;
    private NetworkingManager mNetworkingManager;
    private RelayChannelInfo mRelayChannelInfo;
    private RelayManager mRelayManager;
    private static final Context CONTEXT = RelayUtils.System.getContext();
    public static HashMap mChannelInfoMap = new HashMap();
    private final MyHandler mHandler = new MyHandler(Looper.getMainLooper());
    private final DistAudioCallback mDistAudioCallback = new DistAudioCallback() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.1
        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onDistAudioDeviceAdded(DistAudioDeviceInfo distAudioDeviceInfo) {
            RelayLog.logi(MiPhoneContinuityController.TAG, "onDistAudioDeviceAdded, " + distAudioDeviceInfo);
        }

        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onDistAudioDeviceRemoved(DistAudioDeviceInfo distAudioDeviceInfo) {
            RelayLog.logi(MiPhoneContinuityController.TAG, "onDistAudioDeviceRemoved, " + distAudioDeviceInfo);
        }

        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onConnectionStatus(DistAudioDeviceInfo distAudioDeviceInfo, int i, String str) {
            RelayLog.logi(MiPhoneContinuityController.TAG, "onConnectionStatus " + distAudioDeviceInfo + ", " + i + ", " + str);
            if (i == ResultCode.RESULT_OK.getCode() || i == -5) {
                return;
            }
            MiPhoneContinuityController.this.setDeviceInRelay(null);
            Iterator it = MiPhoneContinuityController.this.mRelayManager.getRelayConnectionsManager().getRelayConnections().iterator();
            while (it.hasNext()) {
                MiPhoneContinuityController.this.mRelayManager.relayRelease(((RelayConnection) it.next()).getAddress().getSchemeSpecificPart(), 7);
            }
            MiPhoneContinuityController.this.onRelayEnd();
        }
    };
    private final ServiceName mRelayServiceName = new ServiceName.Builder().setName(RelayUtils.Relay.RELAY_SERVICE_NAME).setPackageName(CONTEXT.getPackageName()).build();

    public static synchronized MiPhoneContinuityController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiPhoneContinuityController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private MiPhoneContinuityController() {
    }

    public void registerReceiver() {
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    boolean zIsRelayEnable = RelayUtils.Relay.isRelayEnable(context);
                    RelayLog.logi(MiPhoneContinuityController.TAG, "onReceive action is " + action + " isRelayCallEnable is " + zIsRelayEnable);
                    if (zIsRelayEnable) {
                        if (action.equals(MiPhoneContinuityController.ACTION_MILINK_NOTIFICATION_CALL)) {
                            if (RelayUtils.Relay.isInCall(context)) {
                                RelayLog.logi(MiPhoneContinuityController.TAG, "device is in call, not support relay call");
                                return;
                            }
                            if (MiPhoneContinuityController.this.mRelayManager != null) {
                                String stringExtra = intent.getStringExtra(MiPhoneContinuityController.EXTRA_VOIP_CALL_NAME);
                                String stringExtra2 = intent.getStringExtra(MiPhoneContinuityController.EXTRA_VOIP_CALL_NUMBER_LOCAL);
                                String stringExtra3 = intent.getStringExtra(MiPhoneContinuityController.EXTRA_VOIP_CALL_DEVICE_ID);
                                intent.getStringExtra(MiPhoneContinuityController.EXTRA_VOIP_CALL_DEVICE_NAME);
                                String str = "";
                                String strReplaceAll = stringExtra2.replaceAll("[^0-9+]", "");
                                if (!strReplaceAll.isEmpty()) {
                                    str = stringExtra;
                                    stringExtra = strReplaceAll;
                                }
                                MiPhoneContinuityController.this.mRelayManager.sendWantRelayRequest(stringExtra, stringExtra3);
                                MiPhoneContinuityController.this.mRelayManager.recordIncomingInfo(stringExtra, str);
                                return;
                            }
                            return;
                        }
                        if (action.equals(MiPhoneContinuityController.CONNECTION_SERVICE_CHANGED_ON)) {
                            RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    try {
                                        MiPhoneContinuityController.this.init();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CONNECTION_SERVICE_CHANGED_ON);
            intentFilter.addAction(ACTION_MILINK_NOTIFICATION_CALL);
            CONTEXT.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }
    }

    public void init() {
        if (!RelayUtils.Relay.isLyraSupportRelay() || !RelayUtils.Relay.supportDualPhoneRelayCallFeature(CONTEXT)) {
            RelayLog.logi(TAG, "Lyra not support or dual phone relay call not support.");
            return;
        }
        initNetworkingManager();
        initChannelManager();
        initRelayManager();
    }

    private void initNetworkingManager() {
        if (this.mNetworkingManager == null) {
            createNetworkingManager();
            setupNetworkingServices();
        } else {
            retrySetupNetworkingServicesIfNeeded();
        }
    }

    private void createNetworkingManager() {
        try {
            this.mNetworkingManager = NetworkingManager.getInstance(CONTEXT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder("Create network manager, success=");
        sb.append(this.mNetworkingManager != null);
        RelayLog.logi(TAG, sb.toString());
    }

    private void setupNetworkingServices() {
        if (this.mNetworkingManager != null) {
            addRelayService(false);
            addRelayPhoneServiceListener(false);
            this.mNetworkingManager.registerDeathCallback(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.3
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(MiPhoneContinuityController.TAG, "registerDeathCallback");
                    RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MiPhoneContinuityController.this.addRelayService(true);
                            MiPhoneContinuityController.this.addRelayPhoneServiceListener(true);
                            MiPhoneContinuityController.this.registerRelayChannelListener(true);
                        }
                    });
                }
            });
        }
    }

    private void retrySetupNetworkingServicesIfNeeded() {
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

    private void initChannelManager() {
        if (this.mChannelManager == null) {
            try {
                this.mChannelManager = ContinuityChannelManager.getInstance(CONTEXT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder("Create channel manager, success=");
            sb.append(this.mChannelManager != null);
            RelayLog.logi(TAG, sb.toString());
        } else {
            RelayLog.logi(TAG, "Channel manager already created.");
        }
        registerChannelListenerIfNeeded();
    }

    private void registerChannelListenerIfNeeded() {
        if (this.mChannelManager == null || this.mIsRegisterRelayChannelListener) {
            return;
        }
        registerRelayChannelListener(false);
    }

    private void initRelayManager() {
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
            businessServiceInfo.setServiceData(getRelayServiceVersion(7));
            int iAddServiceInfo = this.mNetworkingManager.addServiceInfo(businessServiceInfo);
            RelayLog.logi(TAG, "addRelayService, code:" + iAddServiceInfo);
            this.mNetworkingManager.updateNetworkingPolicy(4, RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
            this.mIsAddRelayService = iAddServiceInfo == 0;
            if (iAddServiceInfo == 0 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.4
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(MiPhoneContinuityController.TAG, "addRelayService, retry!");
                    MiPhoneContinuityController.this.addRelayService(true);
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
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(2);
            arrayList2.add(32);
            arrayList2.add(Integer.valueOf(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY));
            arrayList2.add(65536);
            arrayList2.add(262144);
            serviceFilter.setMediumTypeFilter(arrayList2);
            serviceFilter.setServiceFilter(new ServiceName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, RelayUtils.Relay.RELAY_PHONE_SERVICE_NAME));
            int iAddServiceListener = this.mNetworkingManager.addServiceListener(serviceFilter, new AnonymousClass5());
            RelayLog.logd(TAG, "addRelayPhoneServiceListener, code:" + iAddServiceListener);
            if (iAddServiceListener != 0 && iAddServiceListener != 1301000) {
                z2 = false;
            }
            this.mIsAddRelayPhoneServiceListener = z2;
            if (iAddServiceListener == 0 || iAddServiceListener == 1301000 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.6
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(MiPhoneContinuityController.TAG, "addRelayPhoneServiceListener, retry!");
                    MiPhoneContinuityController.this.addRelayPhoneServiceListener(false);
                }
            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$5, reason: invalid class name */
    class AnonymousClass5 implements ServiceListener {
        AnonymousClass5() {
        }

        public void onServiceOnline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    MiPhoneContinuityController.AnonymousClass5.$r8$lambda$NjKvTOGS8MwCMRJ1P4TC1uyStMw(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$NjKvTOGS8MwCMRJ1P4TC1uyStMw(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "onServiceOnline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
                RelayLog.logd(MiPhoneContinuityController.TAG, "onServiceOnline:deviceInfo hasWlan: " + trustedDeviceInfo.hasWlan() + " hasP2p: " + trustedDeviceInfo.hasP2p() + " hasRemote: " + trustedDeviceInfo.hasRemote() + " hasRestrictedWlan: " + trustedDeviceInfo.hasRestrictedWlan());
            }
        }

        public void onServiceOffline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo, int i) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceOffline$1(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceOffline$1(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "onServiceOffline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
                if (MiPhoneContinuityController.this.mChannelManager == null || !MiPhoneContinuityController.mChannelInfoMap.containsKey(trustedDeviceInfo.getDeviceId())) {
                    return;
                }
                MiPhoneContinuityController.this.mChannelManager.destroyChannel(((Channel) MiPhoneContinuityController.mChannelInfoMap.get(trustedDeviceInfo.getDeviceId())).getChannelId());
            }
        }

        public void onDeviceChanged(final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MiPhoneContinuityController.AnonymousClass5.$r8$lambda$BhucRF8wtoqIXbqEWZnoAFICKWg(trustedDeviceInfo);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$BhucRF8wtoqIXbqEWZnoAFICKWg(TrustedDeviceInfo trustedDeviceInfo) {
            RelayLog.logd(MiPhoneContinuityController.TAG, "onDeviceChanged:deviceInfo:" + trustedDeviceInfo);
            RelayLog.logd(MiPhoneContinuityController.TAG, "onDeviceChanged:deviceInfo hasWlan: " + trustedDeviceInfo.hasWlan() + " hasP2p: " + trustedDeviceInfo.hasP2p() + " hasRemote: " + trustedDeviceInfo.hasRemote() + " hasRestrictedWlan: " + trustedDeviceInfo.hasRestrictedWlan());
        }

        public void onServiceChanged(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            if (RelayUtils.Relay.isRelayPhoneService(businessServiceInfo.getServiceName())) {
                RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$5$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        RelayLog.logd(MiPhoneContinuityController.TAG, "onServiceChanged:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRelayChannelListener(boolean z) {
        try {
            ServerChannelOptionsV2 serverChannelOptionsV2 = new ServerChannelOptionsV2(16);
            Bundle bundle = new Bundle();
            boolean z2 = true;
            bundle.putInt("CHANNEL_OPTIONAL_USE_CELLULAR", 1);
            bundle.putInt("CHANNEL_OPTIONAL_USE_REMOTE", 1);
            serverChannelOptionsV2.setOptionalValues(bundle);
            int iRegisterChannelListener = this.mChannelManager.registerChannelListener(this.mRelayServiceName, serverChannelOptionsV2, new AnonymousClass7(), RelayUtils.MainThread.getMainExecutor());
            if (iRegisterChannelListener != 0) {
                z2 = false;
            }
            this.mIsRegisterRelayChannelListener = z2;
            if (iRegisterChannelListener != 0 && z) {
                RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.8
                    @Override // java.lang.Runnable
                    public void run() {
                        RelayLog.logi(MiPhoneContinuityController.TAG, "registerRelayChannelListener, retry!");
                        MiPhoneContinuityController.this.registerRelayChannelListener(false);
                    }
                }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
            RelayLog.logi(TAG, "registerRelayChannelListener, code:" + iRegisterChannelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$7, reason: invalid class name */
    class AnonymousClass7 implements ChannelListener {
        AnonymousClass7() {
        }

        public void onChannelConfirm(String str, final ServiceName serviceName, final int i, final ConfirmInfo confirmInfo) {
            RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelConfirm:\n------------------------------------\ndeviceId: " + str + "\nserviceName: " + serviceName.toString() + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChannelConfirm$0(serviceName, i, confirmInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChannelConfirm$0(ServiceName serviceName, int i, ConfirmInfo confirmInfo) {
            if (RelayUtils.Relay.isRelayService(serviceName.getName())) {
                MiPhoneContinuityController.this.confirmRelayChannel(i, !MiPhoneContinuityController.this.isCreateChannel() ? 1 : 0);
                if (confirmInfo != null) {
                    MiPhoneContinuityController.this.mIsRemoteType = confirmInfo.getMediumType() == 262144;
                }
            }
        }

        public void onChannelCreateSuccess(Channel channel) {
            if (channel == null) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelCreateSuccess channel is null");
                return;
            }
            RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
            boolean zIsCreateChannel = MiPhoneContinuityController.this.isCreateChannel();
            boolean z = zIsCreateChannel && MiPhoneContinuityController.this.mRelayChannelInfo != null;
            RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelCreateSuccess: isCreateChannel:" + zIsCreateChannel + ", isDestroyChannel:" + z);
            if (z) {
                MiPhoneContinuityController miPhoneContinuityController = MiPhoneContinuityController.this;
                miPhoneContinuityController.destroyRelayChannel(miPhoneContinuityController.mRelayChannelInfo.mChannelId);
            }
            if (zIsCreateChannel) {
                MiPhoneContinuityController.this.mRelayChannelInfo = new RelayChannelInfo(channel.getDeviceId(), channel.getChannelId(), channel);
            }
        }

        public void onChannelCreateFailed(String str, ServiceName serviceName, int i, int i2) {
            RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\ncode: " + i2 + "\n------------------------------------");
        }

        public void onChannelReceive(Channel channel, Packet packet) {
            if (packet != null) {
                byte[] bArrAsBytes = packet.asBytes();
                MiPhoneContinuityController.this.onReceiveRelayMessage(new String(bArrAsBytes));
                if (channel == null) {
                    RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelReceive");
                    return;
                }
                RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelReceive:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\nmessage: " + RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, StandardCharsets.UTF_8)) + "\n------------------------------------");
            }
        }

        public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
            RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelTransferProgressUpdate");
        }

        public void onChannelRelease(Channel channel, int i) {
            if (channel == null) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelRelease");
            } else {
                RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelRelease:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
            }
            if (channel == null || MiPhoneContinuityController.this.mRelayChannelInfo == null || channel.getChannelId() != MiPhoneContinuityController.this.mRelayChannelInfo.mChannelId) {
                return;
            }
            MiPhoneContinuityController.this.setDeviceInRelay(null);
            MiPhoneContinuityController.this.mRelayChannelInfo = null;
            for (RelayConnection relayConnection : MiPhoneContinuityController.this.mRelayManager.getRelayConnectionsManager().getRelayConnections()) {
                relayConnection.forceDisconnected(relayConnection.getDisconnectCause("CHANNEL_RELEASE"));
            }
            MiPhoneContinuityController.this.onRelayEnd();
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

    public void destroyWantRelayChannel(String str) {
        Channel channel = (Channel) mChannelInfoMap.remove(str);
        if (channel == null) {
            RelayLog.logi(TAG, "destroyWantRelayChannel, no channel for deviceId:" + str);
            return;
        }
        destroyRelayChannel(channel.getChannelId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCreateChannel() {
        return (this.mRelayChannelInfo == null || TextUtils.isEmpty(getDeviceInRelay())) && !RelayUtils.Relay.isInCall(CONTEXT);
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
        Packet packetFromBytes = Packet.fromBytes(str.getBytes(StandardCharsets.UTF_8));
        if (this.mRelayChannelInfo != null) {
            RelayLog.logd(TAG, "send message deviceId:" + this.mRelayChannelInfo.mDeviceId);
            Channel channel = this.mRelayChannelInfo.mChannel;
            if (channel != null) {
                channel.send(packetFromBytes, new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.9
                    public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
                    }
                }, RelayUtils.MainThread.getMainExecutor());
            }
        }
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

    public void onRelayStart() {
        this.mHandler.removeMessages(2);
        RelayUtils.Relay.startRelayAudioService(RelayUtils.System.getContext(), 1);
        try {
            MiAudioManager miAudioManager = this.mMiAudioManager;
            if (miAudioManager != null) {
                miAudioManager.release();
                this.mMiAudioManager = null;
            }
            Context context = CONTEXT;
            MiAudioManager miAudioManager2 = new MiAudioManager(context);
            this.mMiAudioManager = miAudioManager2;
            miAudioManager2.registerDistAudioCallback(context.getPackageName(), this.mDistAudioCallback);
        } catch (Exception e) {
            RelayLog.logi(TAG, "MiAudioManager create/register exception" + e);
        }
    }

    public void onRelayEnd() {
        this.mHandler.sendEmptyMessageDelayed(2, 1000L);
    }

    public void sendWantRelayRequest(final String str, final String str2) {
        createChannel(str2, new ChannelCreateCallback(this) { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.10
            @Override // com.android.services.telephony.relay.phonecall.ChannelCreateCallback
            public void onChannelCreateSuccess() {
                Packet packetFromBytes = Packet.fromBytes(str.getBytes(StandardCharsets.UTF_8));
                if (MiPhoneContinuityController.mChannelInfoMap.containsKey(str2)) {
                    Channel channel = (Channel) MiPhoneContinuityController.mChannelInfoMap.get(str2);
                    RelayLog.logd(MiPhoneContinuityController.TAG, "send wantRelay message to deviceId: " + channel.getDeviceId());
                    channel.send(packetFromBytes, new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.10.1
                        public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
                        }
                    }, RelayUtils.MainThread.getMainExecutor());
                }
            }
        });
    }

    private void createChannel(String str, final ChannelCreateCallback channelCreateCallback) {
        if (mChannelInfoMap.containsKey(str)) {
            if (channelCreateCallback != null) {
                channelCreateCallback.onChannelCreateSuccess();
                return;
            }
            return;
        }
        if (this.mChannelManager == null) {
            this.mChannelManager = ContinuityChannelManager.getInstance(CONTEXT);
        }
        ServiceName serviceNameBuild = new ServiceName.Builder().setName(RelayUtils.Relay.RELAY_PHONE_SERVICE_NAME).setPackageName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME).build();
        this.mChannelListener = new ChannelListener() { // from class: com.android.services.telephony.relay.phonecall.MiPhoneContinuityController.11
            public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
            }

            public void onChannelConfirm(String str2, ServiceName serviceName, int i, ConfirmInfo confirmInfo) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "createChannel onChannelConfirm:\n------------------------------------\ndeviceId: " + str2 + "\nserviceName: " + serviceName + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
                if (MiPhoneContinuityController.this.mChannelManager != null) {
                    MiPhoneContinuityController.this.mChannelManager.confirmChannel(i, 0);
                    RelayLog.logd(MiPhoneContinuityController.TAG, "confirmChannel...");
                }
            }

            public void onChannelCreateSuccess(Channel channel) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "createChannel onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
                if (!MiPhoneContinuityController.mChannelInfoMap.containsKey(channel.getDeviceId())) {
                    MiPhoneContinuityController.mChannelInfoMap.put(channel.getDeviceId(), channel);
                }
                ChannelCreateCallback channelCreateCallback2 = channelCreateCallback;
                if (channelCreateCallback2 != null) {
                    channelCreateCallback2.onChannelCreateSuccess();
                }
            }

            public void onChannelCreateFailed(String str2, ServiceName serviceName, int i, int i2) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "createChannel onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str2 + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\nerrorCode: " + i2 + "\n------------------------------------");
            }

            public void onChannelReceive(Channel channel, Packet packet) {
                if (packet != null) {
                    byte[] bArrAsBytes = packet.asBytes();
                    String str2 = new String(bArrAsBytes);
                    MiPhoneContinuityController.this.onReceiveRelayMessage(str2);
                    if (channel == null) {
                        RelayLog.logd(MiPhoneContinuityController.TAG, "onChannelReceive");
                        return;
                    }
                    RelayLog.logd(MiPhoneContinuityController.TAG, "createChannel onChannelReceive:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\nmessage: " + RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, StandardCharsets.UTF_8)) + "\n------------------------------------");
                    if (str2.contains(RelayApi.WANT_RELAY.getMethodName()) && str2.contains("/response?")) {
                        MiPhoneContinuityController.this.destroyWantRelayChannel(channel.getDeviceId());
                    }
                }
            }

            public void onChannelRelease(Channel channel, int i) {
                RelayLog.logd(MiPhoneContinuityController.TAG, "createChannel onChannelRelease:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
                if (MiPhoneContinuityController.mChannelInfoMap.containsKey(channel.getDeviceId())) {
                    MiPhoneContinuityController.mChannelInfoMap.remove(channel.getDeviceId());
                }
            }
        };
        ClientChannelOptionsV2 clientChannelOptionsV2 = new ClientChannelOptionsV2();
        Bundle bundle = new Bundle();
        bundle.putInt("CHANNEL_OPTIONAL_USE_CELLULAR", 1);
        bundle.putInt("CHANNEL_OPTIONAL_USE_REMOTE", 1);
        bundle.putInt("CHANNEL_OPTIONAL_BIZ_PROF_BANDWIDTH_LEVEL", 3);
        clientChannelOptionsV2.setOptionalValues(bundle);
        clientChannelOptionsV2.setTrustLevel(16);
        clientChannelOptionsV2.setTimeout(10000);
        RelayLog.logd(TAG, "createChannel, code:" + this.mChannelManager.createChannelV2(str, serviceNameBuild, clientChannelOptionsV2, this.mChannelListener, RelayUtils.MainThread.getMainExecutor()));
    }

    public boolean getRemoteType() {
        return this.mIsRemoteType;
    }

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 2) {
                RelayUtils.Relay.stopRelayAudioService(RelayUtils.System.getContext());
                try {
                    if (MiPhoneContinuityController.this.mMiAudioManager != null) {
                        MiPhoneContinuityController.this.mMiAudioManager.unregisterDistAudioCallback(MiPhoneContinuityController.CONTEXT.getPackageName());
                        MiPhoneContinuityController.this.mMiAudioManager.release();
                        MiPhoneContinuityController.this.mMiAudioManager = null;
                    }
                } catch (Exception e) {
                    RelayLog.logi(MiPhoneContinuityController.TAG, "MiAudioManager release/unregister exception" + e);
                }
            }
        }
    }
}

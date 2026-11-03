package com.android.services.telephony.relay.phonecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.Connection;
import android.text.TextUtils;
import com.android.services.telephony.relay.DeviceInRelayChangeCallback;
import com.android.services.telephony.relay.RelayDeviceChangeCallback;
import com.android.services.telephony.relay.phonecall.message.MessageCodecUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.android.services.telephony.relay.phonecall.relayaudio.DistAudioDeviceProvider;
import com.xiaomi.continuity.ServiceName;
import com.xiaomi.continuity.channel.Channel;
import com.xiaomi.continuity.channel.ChannelListener;
import com.xiaomi.continuity.channel.ClientChannelOptions;
import com.xiaomi.continuity.channel.ClientChannelOptionsV2;
import com.xiaomi.continuity.channel.ConfirmInfo;
import com.xiaomi.continuity.channel.ContinuityChannelManager;
import com.xiaomi.continuity.channel.Packet;
import com.xiaomi.continuity.channel.PacketTransferProgress;
import com.xiaomi.continuity.channel.PacketTransferProgressCallback;
import com.xiaomi.continuity.channel.ServerChannelOptionsV2;
import com.xiaomi.continuity.networking.BusinessServiceInfo;
import com.xiaomi.continuity.networking.NetworkingManager;
import com.xiaomi.continuity.networking.PropertyType;
import com.xiaomi.continuity.networking.ServiceFilter;
import com.xiaomi.continuity.networking.ServiceListener;
import com.xiaomi.continuity.networking.TrustedDeviceInfo;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class PhoneContinuityController {
    private static final Context CONTEXT = RelayUtils.System.getContext();
    private static final int CREATE_RELAY_CHANNEL_TIMEOUT_IN_MILLIS = 10000;
    private static final long DESTROY_RELAY_CHANNEL_DELAY_MILLIS = 3000;
    private static final String KEY_MIRROR_CALL_RELAY_ACTIVE = "mirror_call_relay_active";
    private static final int LOCAL_CHANNEL_ID = 0;
    private static final int MSG_STOP_PHONE_DIST_AUDIO_SERVICE = 1;
    private static final int SUPPORT_APPLE_VERSION = 2;
    private static final String TAG = "PhoneContinuityController";
    private static final int USE_REMOTE_ENABLED = 1;
    private static PhoneContinuityController sInstance;
    private ContinuityChannelManager mChannelManager;
    private BroadcastReceiver mConnectivityReceiver;
    private String mDeviceInRelay;
    private DeviceInRelayChangeCallback mDeviceInRelayChangeCallback;
    private boolean mIsAddRelayPhoneService;
    private boolean mIsAddRelayServiceListener;
    private boolean mIsRegisterRelayPhoneChannelListener;
    private ContentObserver mMirrorContentObserver;
    private NetworkingManager mNetworkingManager;
    private boolean mRelayAudioServiceStarted;
    private RelayDeviceChangeCallback mRelayDeviceChangeCallback;
    private RelayManager mRelayManager;
    private final Map mRelayServiceInfoMap = new LinkedHashMap() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.1
        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayServiceInfo put(String str, RelayServiceInfo relayServiceInfo) {
            if (relayServiceInfo != null) {
                RelayLog.logi("RelayService, add relay service, serviceName:" + relayServiceInfo.mServiceName + ", deviceId:" + relayServiceInfo.mDeviceId);
            }
            return (RelayServiceInfo) super.put(str, relayServiceInfo);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayServiceInfo remove(Object obj) {
            if (obj != null) {
                RelayLog.logi("RelayService, remove relay service, deviceId:" + obj.toString());
            }
            return (RelayServiceInfo) super.remove(obj);
        }
    };
    private final Map mRelayFilteredServiceInfoMap = new HashMap();
    private final Map mAllServiceInfoMap = new HashMap() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.2
        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayServiceInfo put(String str, RelayServiceInfo relayServiceInfo) {
            if (relayServiceInfo != null) {
                RelayLog.logi("AllService, add service, serviceName:" + relayServiceInfo.mServiceName + ", deviceId:" + relayServiceInfo.mDeviceId);
            }
            return (RelayServiceInfo) super.put(str, relayServiceInfo);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayServiceInfo remove(Object obj) {
            if (obj != null) {
                RelayLog.logi("AllService, remove service, deviceId:" + obj.toString());
            }
            return (RelayServiceInfo) super.remove(obj);
        }
    };
    private final Map mRelayChannelInfoMap = new HashMap() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.3
        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayChannelInfo put(String str, RelayChannelInfo relayChannelInfo) {
            if (relayChannelInfo != null) {
                RelayLog.logi("Add relay channel, deviceId:" + relayChannelInfo.mDeviceId + ", channelId:" + relayChannelInfo.mChannelId);
            }
            return (RelayChannelInfo) super.put(str, relayChannelInfo);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RelayChannelInfo remove(Object obj) {
            if (obj != null) {
                RelayLog.logi("Remove relay channel, deviceId:" + obj.toString());
            }
            return (RelayChannelInfo) super.remove(obj);
        }
    };
    private final MyHandler mHandler = new MyHandler(Looper.getMainLooper());
    private final Uri mMirrorUri = MiuiSynergySdk.getUriFor(KEY_MIRROR_CALL_RELAY_ACTIVE);
    private int mMirrorState = -1;
    private boolean disableRelayInMirror = false;
    private final LinkedList mChannelQueue = new LinkedList();
    private boolean mConnectRelayAudio = false;
    private final ServiceName mRelayPhoneServiceName = new ServiceName.Builder().setName(RelayUtils.Relay.RELAY_PHONE_SERVICE_NAME).setPackageName(CONTEXT.getPackageName()).build();

    public static synchronized PhoneContinuityController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new PhoneContinuityController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private PhoneContinuityController() {
    }

    public void registerConnectivityReceiver() {
        if (this.mConnectivityReceiver == null) {
            this.mConnectivityReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.4
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    int intExtra = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1);
                    boolean zIsRelayEnable = RelayUtils.Relay.isRelayEnable(context);
                    RelayLog.logi(PhoneContinuityController.TAG, "onReceive action:" + action + ", state:" + intExtra + ", isRelayCallEnable:" + zIsRelayEnable);
                    if (zIsRelayEnable) {
                        RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    PhoneContinuityController.this.init();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.milink.service.connectivity.RELAY_CALL_STATE_CHANGED_ON");
            intentFilter.addAction("com.milink.service.connectivity.RELAY_CALL_STATE_CHANGED_OFF");
            intentFilter.addAction("com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_ON");
            intentFilter.addAction("com.milink.service.connectivity.CONNECTION_SERVICE_CHANGED_OFF");
            CONTEXT.registerReceiver(this.mConnectivityReceiver, intentFilter, 2);
        }
    }

    public void init() {
        Context context = CONTEXT;
        if (!RelayUtils.Relay.isRelayEnable(context) || !RelayUtils.Relay.isLyraSupportRelay()) {
            RelayLog.logi(TAG, "Relay call disabled or lyra not support.");
            return;
        }
        if (this.mNetworkingManager == null) {
            try {
                this.mNetworkingManager = NetworkingManager.getInstance(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder("Create network manager, success=");
            sb.append(this.mNetworkingManager != null);
            RelayLog.logi(TAG, sb.toString());
            if (this.mNetworkingManager != null) {
                addRelayPhoneService(false);
                addAllRelayServiceListener(false);
                this.mNetworkingManager.registerDeathCallback(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.5
                    @Override // java.lang.Runnable
                    public void run() {
                        RelayLog.logi(PhoneContinuityController.TAG, "registerDeathCallback");
                        RelayUtils.WorkThread.getWorkHandler().post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.5.1
                            @Override // java.lang.Runnable
                            public void run() {
                                PhoneContinuityController.this.addRelayPhoneService(true);
                                PhoneContinuityController.this.addAllRelayServiceListener(true);
                                PhoneContinuityController.this.registerRelayPhoneChannelListener(true);
                            }
                        });
                    }
                });
            }
        } else {
            if (!this.mIsAddRelayPhoneService) {
                RelayLog.logi(TAG, "addRelayPhoneService, retry!");
                addRelayPhoneService(false);
            }
            if (!this.mIsAddRelayServiceListener) {
                RelayLog.logi(TAG, "addRelayServiceListener, retry!");
                addAllRelayServiceListener(false);
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
        if (this.mChannelManager != null && !this.mIsRegisterRelayPhoneChannelListener) {
            registerRelayPhoneChannelListener(false);
        }
        if (this.mRelayManager == null) {
            this.mRelayManager = new RelayManager();
        }
        updateMirrorState();
        registerMirrorObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRelayPhoneService(boolean z) {
        try {
            BusinessServiceInfo businessServiceInfo = new BusinessServiceInfo();
            businessServiceInfo.setServiceName(RelayUtils.Relay.RELAY_PHONE_SERVICE_NAME);
            businessServiceInfo.setServiceData(getRelayServiceVersion(2));
            int iAddServiceInfo = this.mNetworkingManager.addServiceInfo(businessServiceInfo);
            RelayLog.logi(TAG, "addRelayPhoneService, code:" + iAddServiceInfo);
            this.mIsAddRelayPhoneService = iAddServiceInfo == 0;
            if (iAddServiceInfo == 0 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.6
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(PhoneContinuityController.TAG, "registerDeathCallback, retry!");
                    PhoneContinuityController.this.addRelayPhoneService(false);
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
    public void addAllRelayServiceListener(boolean z) {
        addRelayPadServiceListener(z);
        addRelayPcServiceListener(z);
        addRelayIphoneServiceListener(z);
    }

    private void addRelayPadServiceListener(boolean z) {
        addRelayServiceListener(z, RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
    }

    private void addRelayIphoneServiceListener(boolean z) {
        addRelayServiceListener(z, RelayUtils.Relay.RELAY_IOS_SERVICE_PACKAGE_NAME);
    }

    private void addRelayPcServiceListener(boolean z) {
        addRelayServiceListener(z, RelayUtils.Relay.RELAY_PC_SERVICE_PACKAGE_NAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRelayServiceListener(boolean z, final String str) {
        try {
            ServiceFilter serviceFilter = new ServiceFilter();
            ArrayList arrayList = new ArrayList();
            boolean z2 = true;
            arrayList.add(1);
            serviceFilter.setTrustedTypeFilter(arrayList);
            if (str.equals(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME) && !RelayUtils.Relay.supportDualPhoneRelayCallFeature(CONTEXT)) {
                RelayLog.logd(TAG, "only register listener for pad if not support dual phone");
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(2);
                serviceFilter.setDeviceTypeFilter(arrayList2);
            }
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(2);
            arrayList3.add(32);
            arrayList3.add(Integer.valueOf(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY));
            arrayList3.add(65536);
            arrayList3.add(262144);
            serviceFilter.setMediumTypeFilter(arrayList3);
            serviceFilter.setServiceFilter(new ServiceName(str, RelayUtils.Relay.RELAY_SERVICE_NAME));
            int iAddServiceListener = this.mNetworkingManager.addServiceListener(serviceFilter, new AnonymousClass7());
            RelayLog.logd(TAG, "addRelayServiceListener, code:" + iAddServiceListener);
            if (iAddServiceListener != 0 && iAddServiceListener != 1301000) {
                z2 = false;
            }
            this.mIsAddRelayServiceListener = z2;
            if (iAddServiceListener == 0 || iAddServiceListener == 1301000 || !z) {
                return;
            }
            RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.8
                @Override // java.lang.Runnable
                public void run() {
                    RelayLog.logi(PhoneContinuityController.TAG, "addRelayServiceListener, retry!");
                    PhoneContinuityController.this.addRelayServiceListener(false, str);
                }
            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PhoneContinuityController$7, reason: invalid class name */
    class AnonymousClass7 implements ServiceListener {
        AnonymousClass7() {
        }

        public void onServiceOnline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$7$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceOnline$0(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceOnline$0(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            if (businessServiceInfo == null || trustedDeviceInfo == null || !RelayUtils.Relay.isRelayService(businessServiceInfo.getServiceName())) {
                return;
            }
            RelayLog.logd(PhoneContinuityController.TAG, "onServiceOnline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
            PhoneContinuityController.this.updateRelayService(businessServiceInfo, trustedDeviceInfo);
        }

        public void onServiceOffline(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo, final int i) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceOffline$1(businessServiceInfo, trustedDeviceInfo, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceOffline$1(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo, int i) {
            RelayLog.logd(PhoneContinuityController.TAG, "onServiceOffline:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo + ",reason:" + i);
            if (businessServiceInfo == null || trustedDeviceInfo == null || !RelayUtils.Relay.isRelayService(businessServiceInfo.getServiceName())) {
                return;
            }
            String deviceId = trustedDeviceInfo.getDeviceId();
            PhoneContinuityController.this.mAllServiceInfoMap.remove(deviceId);
            if (PhoneContinuityController.this.mRelayServiceInfoMap.containsKey(deviceId)) {
                if (PhoneContinuityController.this.mRelayManager != null && deviceId.equals(PhoneContinuityController.this.getDeviceInRelay())) {
                    PhoneContinuityController.this.mRelayManager.releaseRelayExtra(PhoneContinuityController.this.mRelayManager.getRelayConnectionsManager().getLocalConnectionCallRelayAnswered());
                    PhoneContinuityController.this.setDeviceInRelay(null);
                }
                PhoneContinuityController.this.mRelayServiceInfoMap.remove(deviceId);
                PhoneContinuityController.this.updateFilteredService();
                if (!PhoneContinuityController.this.mRelayChannelInfoMap.containsKey(deviceId) || PhoneContinuityController.this.mChannelManager == null) {
                    return;
                }
                PhoneContinuityController.this.mChannelManager.destroyChannel(((RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(deviceId)).mChannelId);
                PhoneContinuityController.this.mRelayChannelInfoMap.remove(deviceId);
            }
        }

        public void onDeviceChanged(final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDeviceChanged$2(trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeviceChanged$2(TrustedDeviceInfo trustedDeviceInfo) {
            RelayLog.logd(PhoneContinuityController.TAG, "onDeviceChanged:deviceInfo:" + trustedDeviceInfo);
            if (trustedDeviceInfo == null) {
                return;
            }
            RelayLog.logd(PhoneContinuityController.TAG, "updateRelayService:mediumType:" + trustedDeviceInfo.getMediumTypes() + ", hasWlan:" + trustedDeviceInfo.hasWlan() + ", hasP2p:" + trustedDeviceInfo.hasP2p() + ", hasBle:" + trustedDeviceInfo.hasBle());
            StringBuilder sb = new StringBuilder("onDeviceChanged, hasRemote:");
            sb.append(trustedDeviceInfo.hasRemote());
            sb.append(", hasRestrictedWlan:");
            sb.append(trustedDeviceInfo.hasRestrictedWlan());
            RelayLog.logd(PhoneContinuityController.TAG, sb.toString());
            String deviceId = trustedDeviceInfo.getDeviceId();
            boolean zIsRelayDeviceService = PhoneContinuityController.this.isRelayDeviceService(trustedDeviceInfo);
            PhoneContinuityController phoneContinuityController = PhoneContinuityController.this;
            if (zIsRelayDeviceService) {
                if (phoneContinuityController.mAllServiceInfoMap.containsKey(deviceId)) {
                    PhoneContinuityController phoneContinuityController2 = PhoneContinuityController.this;
                    phoneContinuityController2.updateRelayWlanService((RelayServiceInfo) phoneContinuityController2.mAllServiceInfoMap.get(deviceId));
                    return;
                }
                return;
            }
            if (phoneContinuityController.mRelayServiceInfoMap.containsKey(deviceId)) {
                PhoneContinuityController.this.mRelayServiceInfoMap.remove(deviceId);
                PhoneContinuityController.this.updateFilteredService();
                if (!PhoneContinuityController.this.mRelayChannelInfoMap.containsKey(deviceId) || PhoneContinuityController.this.mChannelManager == null) {
                    return;
                }
                PhoneContinuityController.this.mChannelManager.destroyChannel(((RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(deviceId)).mChannelId);
                PhoneContinuityController.this.mRelayChannelInfoMap.remove(deviceId);
            }
        }

        public void onServiceChanged(final BusinessServiceInfo businessServiceInfo, final TrustedDeviceInfo trustedDeviceInfo) {
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$7$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceChanged$3(businessServiceInfo, trustedDeviceInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceChanged$3(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
            RelayLog.logd(PhoneContinuityController.TAG, "onServiceChanged:service:" + businessServiceInfo + ",deviceInfo:" + trustedDeviceInfo);
            String deviceId = trustedDeviceInfo.getDeviceId();
            boolean zIsRelayDeviceService = PhoneContinuityController.this.isRelayDeviceService(trustedDeviceInfo);
            PhoneContinuityController phoneContinuityController = PhoneContinuityController.this;
            if (zIsRelayDeviceService) {
                if (phoneContinuityController.mAllServiceInfoMap.containsKey(deviceId)) {
                    PhoneContinuityController phoneContinuityController2 = PhoneContinuityController.this;
                    phoneContinuityController2.updateRelayWlanService((RelayServiceInfo) phoneContinuityController2.mAllServiceInfoMap.get(deviceId));
                    return;
                }
                return;
            }
            if (phoneContinuityController.mRelayServiceInfoMap.containsKey(deviceId)) {
                PhoneContinuityController.this.mRelayServiceInfoMap.remove(deviceId);
                PhoneContinuityController.this.updateFilteredService();
                if (!PhoneContinuityController.this.mRelayChannelInfoMap.containsKey(deviceId) || PhoneContinuityController.this.mChannelManager == null) {
                    return;
                }
                PhoneContinuityController.this.mChannelManager.destroyChannel(((RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(deviceId)).mChannelId);
                PhoneContinuityController.this.mRelayChannelInfoMap.remove(deviceId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRelayDeviceService(TrustedDeviceInfo trustedDeviceInfo) {
        if (trustedDeviceInfo == null || !(trustedDeviceInfo.getDeviceType() == 11 || trustedDeviceInfo.getDeviceType() == 16777280)) {
            return isRelayMediumType(trustedDeviceInfo) && isRelayRegion(trustedDeviceInfo);
        }
        return isRelayMediumType(trustedDeviceInfo);
    }

    private boolean isRelayMediumType(TrustedDeviceInfo trustedDeviceInfo) {
        if (trustedDeviceInfo == null) {
            RelayLog.logi(TAG, "deviceInfo is null in isRelayMediumType");
            return false;
        }
        int deviceType = trustedDeviceInfo.getDeviceType();
        RelayLog.logi(TAG, "deviceInfo.hasRemote = " + trustedDeviceInfo.hasRemote() + ", deviceInfo.hasRestrictedWlan = " + trustedDeviceInfo.hasRestrictedWlan() + ", deviceInfo.hasBle = " + trustedDeviceInfo.hasBle());
        boolean z = trustedDeviceInfo.hasWlan() || trustedDeviceInfo.hasP2p() || trustedDeviceInfo.hasRestrictedWlan();
        if (deviceType == 1 || deviceType == 11) {
            return z || trustedDeviceInfo.hasRemote();
        }
        return deviceType == 16777280 ? trustedDeviceInfo.hasBle() : z;
    }

    private boolean isRelayRegion(TrustedDeviceInfo trustedDeviceInfo) {
        NetworkingManager networkingManager;
        TrustedDeviceInfo localDeviceInfo;
        if (trustedDeviceInfo == null || (networkingManager = this.mNetworkingManager) == null || (localDeviceInfo = networkingManager.getLocalDeviceInfo()) == null) {
            return false;
        }
        NetworkingManager networkingManager2 = this.mNetworkingManager;
        String deviceId = trustedDeviceInfo.getDeviceId();
        PropertyType propertyType = PropertyType.PropBuildRegion;
        String stringProperty = networkingManager2.getStringProperty(deviceId, propertyType.toInteger());
        String stringProperty2 = this.mNetworkingManager.getStringProperty(localDeviceInfo.getDeviceId(), propertyType.toInteger());
        boolean z = !TextUtils.isEmpty(stringProperty) && stringProperty.contains("cn");
        boolean z2 = !TextUtils.isEmpty(stringProperty2) && stringProperty2.contains("cn");
        RelayLog.logi(TAG, "isRelayRegion...deviceRegion:" + stringProperty + ", localDeviceRegion:" + stringProperty2);
        return z == z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRelayService(BusinessServiceInfo businessServiceInfo, TrustedDeviceInfo trustedDeviceInfo) {
        byte[] serviceData = businessServiceInfo.getServiceData();
        int relayServiceVersionInt = (serviceData == null || serviceData.length != 4) ? -1 : getRelayServiceVersionInt(serviceData);
        RelayLog.logd(TAG, "updateRelayService:deviceId:" + trustedDeviceInfo.getDeviceId() + ", relayVersion:" + relayServiceVersionInt);
        RelayServiceInfo relayServiceInfo = new RelayServiceInfo(businessServiceInfo.getPackageName(), businessServiceInfo.getServiceName(), businessServiceInfo.getServiceData(), trustedDeviceInfo.getDeviceId(), trustedDeviceInfo.getDeviceName(), trustedDeviceInfo.getDeviceType(), relayServiceVersionInt);
        this.mAllServiceInfoMap.put(trustedDeviceInfo.getDeviceId(), relayServiceInfo);
        RelayLog.logd(TAG, "updateRelayService:mediumType:" + trustedDeviceInfo.getMediumTypes() + ", hasWlan:" + trustedDeviceInfo.hasWlan() + ", hasP2p:" + trustedDeviceInfo.hasP2p() + ", hasBle:" + trustedDeviceInfo.hasBle());
        StringBuilder sb = new StringBuilder("updateRelayService, hasRemote:");
        sb.append(trustedDeviceInfo.hasRemote());
        sb.append(", hasRestrictedWlan:");
        sb.append(trustedDeviceInfo.hasRestrictedWlan());
        RelayLog.logd(TAG, sb.toString());
        if (isRelayDeviceService(trustedDeviceInfo)) {
            updateRelayWlanService(relayServiceInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRelayWlanService(RelayServiceInfo relayServiceInfo) {
        if (relayServiceInfo == null) {
            RelayLog.logd(TAG, "updateRelayWlanService, service is null");
            return;
        }
        this.mRelayServiceInfoMap.put(relayServiceInfo.mDeviceId, relayServiceInfo);
        updateFilteredService();
        RelayLog.logd(TAG, "updateRelayWlanService");
    }

    public int getRelayServiceVersionInt(byte[] bArr) {
        return (bArr[0] & TransitionInfo.INIT) | ((bArr[1] & TransitionInfo.INIT) << 8) | ((bArr[2] & TransitionInfo.INIT) << 16) | ((bArr[3] & TransitionInfo.INIT) << 24);
    }

    public void createRelayChannel(String str, String str2, ChannelCreateCallback channelCreateCallback) {
        int iCreateChannelV2;
        if (isChannelConnected(str2)) {
            if (channelCreateCallback != null) {
                channelCreateCallback.onChannelCreateSuccess();
                return;
            }
            return;
        }
        if (isChannelConnecting(str2)) {
            this.mChannelQueue.add(channelCreateCallback);
            RelayLog.loge(TAG, "createRelayChannel....callback enqueue.");
            return;
        }
        if (this.mChannelManager == null) {
            RelayLog.loge(TAG, "createRelayChannel...No channel manager created.");
            return;
        }
        if (!this.mRelayFilteredServiceInfoMap.containsKey(str2)) {
            RelayLog.loge(TAG, "createRelayChannel...No relay service created.");
            return;
        }
        ServiceName serviceNameBuild = new ServiceName.Builder().setName(((RelayServiceInfo) this.mRelayFilteredServiceInfoMap.get(str2)).mServiceName).setPackageName(((RelayServiceInfo) this.mRelayFilteredServiceInfoMap.get(str2)).mPackageName).build();
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(channelCreateCallback);
        if (getRelayServiceInfo(str2).mDeviceType == 11 || getRelayServiceInfo(str2).mDeviceType == 1) {
            RelayLog.logi(TAG, "createRelayChannel support IOS remote + WLAN");
            ClientChannelOptionsV2 clientChannelOptionsV2 = new ClientChannelOptionsV2();
            Bundle bundle = new Bundle();
            bundle.putInt("CHANNEL_OPTIONAL_USE_CELLULAR", 1);
            bundle.putInt("CHANNEL_OPTIONAL_USE_REMOTE", 1);
            bundle.putInt("CHANNEL_OPTIONAL_BIZ_PROF_BANDWIDTH_LEVEL", 3);
            clientChannelOptionsV2.setOptionalValues(bundle);
            clientChannelOptionsV2.setTrustLevel(16);
            clientChannelOptionsV2.setTimeout(10000);
            this.mRelayChannelInfoMap.put(str2, new RelayChannelInfo(str2, 0, null));
            iCreateChannelV2 = this.mChannelManager.createChannelV2(str2, serviceNameBuild, clientChannelOptionsV2, anonymousClass9, RelayUtils.MainThread.getMainExecutor());
        } else {
            RelayLog.logi(TAG, "createRelayChannel support only WLAN");
            ClientChannelOptions clientChannelOptions = new ClientChannelOptions();
            if (getRelayServiceInfo(str2).mDeviceType == 16777280) {
                clientChannelOptions.setConnectMediumType(2);
            } else {
                clientChannelOptions.setConnectMediumType(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            }
            clientChannelOptions.setTrustLevel(16);
            clientChannelOptions.setTimeout(10000);
            this.mRelayChannelInfoMap.put(str2, new RelayChannelInfo(str2, 0, null));
            iCreateChannelV2 = this.mChannelManager.createChannel(str2, serviceNameBuild, clientChannelOptions, anonymousClass9, RelayUtils.MainThread.getMainExecutor());
        }
        RelayLog.logd(TAG, "createRelayChannel, code:" + iCreateChannelV2);
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PhoneContinuityController$9, reason: invalid class name */
    class AnonymousClass9 implements ChannelListener {
        final /* synthetic */ ChannelCreateCallback val$channelCreateCallback;

        public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
        }

        AnonymousClass9(ChannelCreateCallback channelCreateCallback) {
            this.val$channelCreateCallback = channelCreateCallback;
        }

        public void onChannelConfirm(String str, final ServiceName serviceName, final int i, ConfirmInfo confirmInfo) {
            RelayLog.logd(PhoneContinuityController.TAG, "onChannelConfirm:\n------------------------------------\ndeviceId: " + str + "\nserviceName: " + serviceName + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChannelConfirm$0(serviceName, i);
                }
            });
            PhoneContinuityController.this.mRelayChannelInfoMap.put(str, new RelayChannelInfo(str, i, null));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChannelConfirm$0(ServiceName serviceName, int i) {
            if (RelayUtils.Relay.isRelayService(serviceName.getName())) {
                PhoneContinuityController.this.confirmRelayChannel(i);
            }
        }

        public void onChannelCreateSuccess(Channel channel) {
            if (channel == null) {
                RelayLog.logd(PhoneContinuityController.TAG, "onChannelCreateSuccess");
                return;
            }
            RelayLog.logd(PhoneContinuityController.TAG, "onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
            PhoneContinuityController.this.mRelayChannelInfoMap.put(channel.getDeviceId(), new RelayChannelInfo(channel.getDeviceId(), channel.getChannelId(), channel));
            PhoneContinuityController.this.notifyChannelCallback(this.val$channelCreateCallback);
            Iterator it = PhoneContinuityController.this.mChannelQueue.iterator();
            while (it.hasNext()) {
                PhoneContinuityController.this.notifyChannelCallback((ChannelCreateCallback) it.next());
            }
            PhoneContinuityController.this.mChannelQueue.clear();
        }

        public void onChannelCreateFailed(String str, ServiceName serviceName, int i, int i2) {
            int i3;
            RelayLog.logd(PhoneContinuityController.TAG, "onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\nerrorCode: " + i2 + "\n------------------------------------");
            RelayChannelInfo relayChannelInfo = (RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(str);
            if (relayChannelInfo != null && ((i3 = relayChannelInfo.mChannelId) == i || i3 == 0)) {
                PhoneContinuityController.this.mRelayChannelInfoMap.remove(str);
            }
            PhoneContinuityController.this.mChannelQueue.clear();
        }

        public void onChannelReceive(Channel channel, Packet packet) {
            if (packet != null) {
                byte[] bArrAsBytes = packet.asBytes();
                PhoneContinuityController.this.onReceiveRelayMessage(new String(bArrAsBytes));
                if (channel == null) {
                    RelayLog.logd(PhoneContinuityController.TAG, "onChannelReceive");
                    return;
                }
                RelayLog.logd(PhoneContinuityController.TAG, "onChannelReceive:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\nmessage: " + RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, StandardCharsets.UTF_8)) + "\n------------------------------------");
            }
        }

        public void onChannelRelease(Channel channel, int i) {
            if (channel == null) {
                RelayLog.logd(PhoneContinuityController.TAG, "onChannelRelease");
                return;
            }
            String deviceId = channel.getDeviceId();
            RelayLog.logd(PhoneContinuityController.TAG, "onChannelRelease:\n------------------------------------\ndeviceId: " + deviceId + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
            RelayChannelInfo relayChannelInfo = (RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(deviceId);
            if (relayChannelInfo == null || relayChannelInfo.mChannel == null || relayChannelInfo.mChannelId != channel.getChannelId()) {
                return;
            }
            PhoneContinuityController.this.mRelayChannelInfoMap.remove(deviceId);
            if (PhoneContinuityController.this.mRelayManager == null || !deviceId.equals(PhoneContinuityController.this.getDeviceInRelay())) {
                return;
            }
            PhoneContinuityController.this.mRelayManager.releaseRelayExtra(PhoneContinuityController.this.mRelayManager.getRelayConnectionsManager().getLocalConnectionCallRelayAnswered());
            PhoneContinuityController.this.setDeviceInRelay(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyChannelCallback(ChannelCreateCallback channelCreateCallback) {
        if (channelCreateCallback != null) {
            channelCreateCallback.onChannelCreateSuccess();
        }
    }

    public void destroyRelayChannel() {
        if (this.mChannelManager == null) {
            RelayLog.loge(TAG, "destroyRelayChannel...No channel manager created.");
            return;
        }
        Iterator it = this.mRelayChannelInfoMap.entrySet().iterator();
        while (it.hasNext()) {
            int i = ((RelayChannelInfo) ((Map.Entry) it.next()).getValue()).mChannelId;
            this.mChannelManager.destroyChannel(i);
            RelayLog.logi(TAG, "destroyRelayChannel...channelId:" + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: destroyRelayChannel, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleDestroyRelayChannel$0(String str) {
        if (this.mChannelManager == null || TextUtils.isEmpty(str)) {
            return;
        }
        RelayServiceInfo relayServiceInfo = (RelayServiceInfo) this.mRelayFilteredServiceInfoMap.get(str);
        if (relayServiceInfo == null) {
            RelayLog.logi(TAG, "destroyRelayChannel, no service for deviceId:" + str);
            return;
        }
        if (relayServiceInfo.mDeviceType != 1) {
            RelayLog.logi(TAG, "destroyRelayChannel, skip device type:" + relayServiceInfo.mDeviceType + ", deviceId:" + str);
            return;
        }
        RelayChannelInfo relayChannelInfo = (RelayChannelInfo) this.mRelayChannelInfoMap.get(str);
        if (relayChannelInfo == null) {
            RelayLog.logi(TAG, "destroyRelayChannel, no channel for deviceId:" + str);
            return;
        }
        this.mChannelManager.destroyChannel(relayChannelInfo.mChannelId);
        this.mRelayChannelInfoMap.remove(str);
        RelayLog.logi(TAG, "destroyRelayChannel...deviceId:" + str + ", channelId:" + relayChannelInfo.mChannelId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmRelayChannel(int i) {
        ContinuityChannelManager continuityChannelManager = this.mChannelManager;
        if (continuityChannelManager != null) {
            continuityChannelManager.confirmChannel(i, 0);
            RelayLog.logi(TAG, "confirmRelayChannel...");
        }
    }

    public void sendRelayMessage(final String str, final String str2) {
        createRelayChannel(str, str2, new ChannelCreateCallback() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.10
            @Override // com.android.services.telephony.relay.phonecall.ChannelCreateCallback
            public void onChannelCreateSuccess() {
                Packet packetFromBytes = Packet.fromBytes(str.getBytes(StandardCharsets.UTF_8));
                RelayServiceInfo relayServiceInfo = (RelayServiceInfo) PhoneContinuityController.this.mRelayFilteredServiceInfoMap.get(str2);
                RelayChannelInfo relayChannelInfo = (RelayChannelInfo) PhoneContinuityController.this.mRelayChannelInfoMap.get(str2);
                Uri uri = Uri.parse(str);
                if (uri == null) {
                    return;
                }
                String strValueOf = String.valueOf(uri.getPort());
                if (relayServiceInfo == null) {
                    PhoneContinuityController.this.mRelayManager.getMessagesManager().removeRequestMessage(strValueOf);
                    RelayLog.logd(PhoneContinuityController.TAG, "Service offline!");
                    return;
                }
                if (relayChannelInfo == null) {
                    PhoneContinuityController.this.mRelayManager.getMessagesManager().removeRequestMessage(strValueOf);
                    RelayLog.logd(PhoneContinuityController.TAG, "The channel was not created successfully!");
                    return;
                }
                Channel channel = relayChannelInfo.mChannel;
                if (channel != null) {
                    channel.send(packetFromBytes, new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.10.1
                        public void onPacketTransferProgressUpdate(Packet packet, PacketTransferProgress packetTransferProgress) {
                        }
                    }, RelayUtils.MainThread.getMainExecutor());
                    RelayLog.logd(PhoneContinuityController.TAG, "sendRelayMessage, deviceId:" + str2);
                }
            }
        });
    }

    public List getConnectedServiceDevices() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mRelayFilteredServiceInfoMap.entrySet().iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Map.Entry) it.next()).getKey());
        }
        return arrayList;
    }

    public RelayServiceInfo getRelayServiceInfo(String str) {
        for (Map.Entry entry : this.mRelayFilteredServiceInfoMap.entrySet()) {
            if (((String) entry.getKey()).equals(str)) {
                return (RelayServiceInfo) entry.getValue();
            }
        }
        return null;
    }

    public boolean isContinuityServiceConnected() {
        int size = this.mRelayFilteredServiceInfoMap.size();
        RelayLog.logi(TAG, "isContinuityServiceConnected, " + size);
        return size > 0;
    }

    private boolean isChannelConnected(String str) {
        RelayChannelInfo relayChannelInfo = (RelayChannelInfo) this.mRelayChannelInfoMap.get(str);
        return (relayChannelInfo == null || relayChannelInfo.mChannel == null) ? false : true;
    }

    private boolean isChannelConnecting(String str) {
        RelayChannelInfo relayChannelInfo = (RelayChannelInfo) this.mRelayChannelInfoMap.get(str);
        return relayChannelInfo != null && relayChannelInfo.mChannel == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceiveRelayMessage(String str) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.onRelayMessageReceived(str);
        }
    }

    public void forceReleaseRelayCall(Connection connection, int i) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.forceReleaseRelayCall(connection, i);
        }
    }

    public void onRelayCallStateChanged(Connection connection) {
        NetworkingManager networkingManager;
        if (RelayUtils.Relay.supportDualPhoneRelayCallFeature(CONTEXT) && connection.getState() == 2 && (networkingManager = this.mNetworkingManager) != null) {
            networkingManager.updateNetworkingPolicy(4, RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        }
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.onRelayCallStateChanged(connection);
            if (connection != null && RelayUtils.Relay.isCallRelayed(connection) && connection.getState() == 4) {
                RelayLog.logi(TAG, "onRelayCallStateChanged, connect relay audio, connectRelayAudio:" + this.mConnectRelayAudio);
                if (this.mConnectRelayAudio) {
                    RelayUtils.MainThread.runOnUiThreadDelay(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.11
                        @Override // java.lang.Runnable
                        public void run() {
                            DistAudioDeviceProvider.getInstance().connectDistAudioDevice(PhoneContinuityController.this.getDeviceInRelay());
                        }
                    }, 1000L);
                }
                this.mConnectRelayAudio = false;
            }
        }
    }

    public void addLocalConnection(Connection connection) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.addLocalConnection(connection);
        }
    }

    public void removeLocalConnection(Connection connection) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.removeLocalConnection(connection);
        }
    }

    public void sendRelayRingCallRequest(Connection connection) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.sendRelayRingCallRequest(connection);
        }
    }

    public void sendRelayRingCallRequestToDeviceId(Connection connection, String str) {
        RelayManager relayManager = this.mRelayManager;
        if (relayManager != null) {
            relayManager.sendRelayRingCallRequestToDeviceId(connection, str);
        }
    }

    public RelayManager getRelayManager() {
        return this.mRelayManager;
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
            boolean zIsEmpty2 = TextUtils.isEmpty(str);
            boolean z = zIsEmpty2 ^ true;
            RelayServiceInfo relayServiceInfo = getRelayServiceInfo(TextUtils.isEmpty(str) ? this.mDeviceInRelay : str);
            if (relayServiceInfo != null) {
                RelayUtils.Relay.setIconVisibility(CONTEXT, relayServiceInfo.mDeviceId, relayServiceInfo.mDeviceName, relayServiceInfo.mDeviceType, z);
            }
            if (!zIsEmpty2) {
                Connection localConnectionCallRelayed = getRelayManager().getRelayConnectionsManager().getLocalConnectionCallRelayed();
                if (localConnectionCallRelayed != null) {
                    int state = localConnectionCallRelayed.getState();
                    RelayLog.logi(TAG, "setDeviceInRelay, connect relay audio, callState:" + state);
                    if (state == 4) {
                        DistAudioDeviceProvider.getInstance().connectDistAudioDevice(str);
                    } else {
                        this.mConnectRelayAudio = true;
                    }
                }
            } else {
                DistAudioDeviceProvider.getInstance().disconnectDistAudioDevice(this.mDeviceInRelay);
                this.mConnectRelayAudio = false;
            }
            this.mDeviceInRelay = str;
            DeviceInRelayChangeCallback deviceInRelayChangeCallback = this.mDeviceInRelayChangeCallback;
            if (deviceInRelayChangeCallback != null) {
                deviceInRelayChangeCallback.onDeviceChange();
            }
        }
    }

    public String getDeviceInRelay() {
        RelayLog.logi(TAG, "getDeviceInRelay, " + this.mDeviceInRelay);
        return this.mDeviceInRelay;
    }

    public boolean isDeviceInRelay() {
        return !TextUtils.isEmpty(this.mDeviceInRelay);
    }

    public void setDeviceInRelayChangeCallback(DeviceInRelayChangeCallback deviceInRelayChangeCallback) {
        this.mDeviceInRelayChangeCallback = deviceInRelayChangeCallback;
    }

    public void setRelayDeviceChangeCallback(RelayDeviceChangeCallback relayDeviceChangeCallback) {
        this.mRelayDeviceChangeCallback = relayDeviceChangeCallback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRelayPhoneChannelListener(boolean z) {
        try {
            ServerChannelOptionsV2 serverChannelOptionsV2 = new ServerChannelOptionsV2(16);
            Bundle bundle = new Bundle();
            boolean z2 = true;
            bundle.putInt("CHANNEL_OPTIONAL_USE_CELLULAR", 1);
            bundle.putInt("CHANNEL_OPTIONAL_USE_REMOTE", 1);
            serverChannelOptionsV2.setOptionalValues(bundle);
            int iRegisterChannelListener = this.mChannelManager.registerChannelListener(this.mRelayPhoneServiceName, serverChannelOptionsV2, new AnonymousClass12(), RelayUtils.MainThread.getMainExecutor());
            if (iRegisterChannelListener != 0) {
                z2 = false;
            }
            this.mIsRegisterRelayPhoneChannelListener = z2;
            if (iRegisterChannelListener != 0 && z) {
                RelayUtils.WorkThread.getWorkHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.13
                    @Override // java.lang.Runnable
                    public void run() {
                        RelayLog.logi(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, retry!");
                        PhoneContinuityController.this.registerRelayPhoneChannelListener(false);
                    }
                }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
            RelayLog.logi(TAG, "registerRelayPhoneChannelListener, code:" + iRegisterChannelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.relay.phonecall.PhoneContinuityController$12, reason: invalid class name */
    class AnonymousClass12 implements ChannelListener {
        public void onChannelTransferProgressUpdate(Channel channel, Packet packet, PacketTransferProgress packetTransferProgress) {
        }

        AnonymousClass12() {
        }

        public void onChannelConfirm(String str, final ServiceName serviceName, final int i, ConfirmInfo confirmInfo) {
            RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelConfirm:\n------------------------------------\ndeviceId: " + str + "\nserviceName: " + serviceName.toString() + "\nchannelId: " + i + "\nappInfo: " + confirmInfo.toString() + "\n------------------------------------");
            RelayUtils.MainThread.runOnUiThread(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$12$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChannelConfirm$0(serviceName, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChannelConfirm$0(ServiceName serviceName, int i) {
            if (RelayUtils.Relay.isRelayPhoneService(serviceName.getName())) {
                PhoneContinuityController.this.confirmRelayChannel(i);
            }
        }

        public void onChannelCreateSuccess(Channel channel) {
            if (channel == null) {
                RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelCreateSuccess");
                return;
            }
            RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelCreateSuccess:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\n------------------------------------");
        }

        public void onChannelCreateFailed(String str, ServiceName serviceName, int i, int i2) {
            RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelCreateFailed:\n------------------------------------\ndeviceId: " + str + "\nserviceId: " + serviceName + "\nchannelId: " + i + "\ncode: " + i2 + "\n------------------------------------");
        }

        public void onChannelReceive(Channel channel, Packet packet) {
            boolean z;
            if (packet != null) {
                byte[] bArrAsBytes = packet.asBytes();
                String str = new String(bArrAsBytes);
                if (str.contains("wantRelay")) {
                    channel.send(Packet.fromBytes(PhoneContinuityController.this.handleWantRelayResponse(str).getBytes(StandardCharsets.UTF_8)), new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.12.1
                        public void onPacketTransferProgressUpdate(Packet packet2, PacketTransferProgress packetTransferProgress) {
                        }
                    }, RelayUtils.MainThread.getMainExecutor());
                    z = true;
                } else {
                    z = false;
                }
                PhoneContinuityController.this.onReceiveRelayMessage(new String(bArrAsBytes));
                if (channel == null) {
                    RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelReceive");
                    return;
                }
                StringBuilder sb = new StringBuilder("registerRelayPhoneChannelListener, onChannelReceive:\n------------------------------------\ndeviceId: ");
                sb.append(channel.getDeviceId());
                sb.append("\nchannelId: ");
                sb.append(channel.getChannelId());
                sb.append("\nrole: ");
                sb.append(channel.getChannelRole());
                sb.append("\nmessage: ");
                Charset charset = StandardCharsets.UTF_8;
                sb.append(RelayUtils.Relay.hideSensitiveInfo(new String(bArrAsBytes, charset)));
                sb.append("\n------------------------------------");
                RelayLog.logd(PhoneContinuityController.TAG, sb.toString());
                if (z) {
                    return;
                }
                channel.send(Packet.fromBytes(ResponseObject.DEFAULT_MESSAGE_SUCCEED.getBytes(charset)), new PacketTransferProgressCallback(this) { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.12.2
                    public void onPacketTransferProgressUpdate(Packet packet2, PacketTransferProgress packetTransferProgress) {
                    }
                }, RelayUtils.MainThread.getMainExecutor());
            }
        }

        public void onChannelRelease(Channel channel, int i) {
            if (channel == null) {
                RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelRelease");
                return;
            }
            RelayLog.logd(PhoneContinuityController.TAG, "registerRelayPhoneChannelListener, onChannelRelease:\n------------------------------------\ndeviceId: " + channel.getDeviceId() + "\nchannelId: " + channel.getChannelId() + "\nrole: " + channel.getChannelRole() + "\ncode: " + i + "\n------------------------------------");
        }
    }

    private void registerMirrorObserver() {
        if (this.mMirrorContentObserver == null) {
            this.mMirrorContentObserver = new ContentObserver(null) { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController.14
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    PhoneContinuityController.this.updateMirrorState();
                }
            };
            try {
                CONTEXT.getContentResolver().registerContentObserver(this.mMirrorUri, false, this.mMirrorContentObserver);
                RelayLog.logi(TAG, "registerMirrorObserver");
            } catch (Exception e) {
                RelayLog.loge(TAG, "registerMirrorObserver " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMirrorState() {
        Connection connection;
        try {
            int i = MiuiSynergySdk.getInstance().getInt(CONTEXT, this.mMirrorUri, -1);
            int i2 = this.mMirrorState;
            if (i2 != i) {
                boolean z = true;
                if (i2 != 1 || i != 0) {
                    z = false;
                }
                this.disableRelayInMirror = z;
                this.mMirrorState = i;
                if (!z && (connection = RelayUtils.Relay.currentConnection) != null && connection.getState() == 2) {
                    RelayLog.logi(TAG, "send ring request in mirror state");
                    RelayUtils.Relay.maybeRelayCall(RelayUtils.System.getContext(), RelayUtils.Relay.currentallConnections, RelayUtils.Relay.currentConnection, null);
                }
            }
            RelayLog.logi(TAG, "updateMirrorState disableRelayInMirror:" + this.disableRelayInMirror + ", state:" + i);
        } catch (Exception e) {
            RelayLog.loge(TAG, "registerMirrorObserver " + e);
        }
    }

    public boolean disableRelayInMirror() {
        return this.disableRelayInMirror;
    }

    public void updateFilteredService() {
        RelayDeviceChangeCallback relayDeviceChangeCallback;
        List connectedServiceDevices = getConnectedServiceDevices();
        RelayServiceFilterUtils.updateFilteredServiceInfo(this.mRelayFilteredServiceInfoMap, this.mRelayServiceInfoMap);
        if (new HashSet(connectedServiceDevices).equals(new HashSet(getConnectedServiceDevices())) || (relayDeviceChangeCallback = this.mRelayDeviceChangeCallback) == null) {
            return;
        }
        relayDeviceChangeCallback.onDeviceChange();
        RelayLog.logi(TAG, "updateFilteredService onDeviceChange");
    }

    public int getRelayVersion(String str) {
        RelayServiceInfo relayServiceInfo = (RelayServiceInfo) this.mRelayServiceInfoMap.get(str);
        if (relayServiceInfo != null) {
            return relayServiceInfo.mVersion;
        }
        return -1;
    }

    public void onRelayStart(Context context, Connection connection) {
        this.mHandler.removeMessages(1);
        RelayUtils.Relay.startRelayAudioService(RelayUtils.System.getContext(), 0);
        this.mRelayAudioServiceStarted = true;
        if (RelayUtils.Relay.isConnectDiscAudio()) {
            DistAudioDeviceProvider.getInstance().registerMiAudioManager();
            RelayManager relayManager = getInstance().getRelayManager();
            if (relayManager != null) {
                String number = RelayUtils.Telephony.getNumber(connection);
                String strQueryContactName = RelayUtils.System.queryContactName(context, number);
                if (!TextUtils.isEmpty(strQueryContactName)) {
                    relayManager.updateContact(number, strQueryContactName);
                } else {
                    RelayUtils.System.queryYellowPage(context, number);
                }
            }
        }
    }

    public void onRelayEnd(Context context) {
        MyHandler myHandler = this.mHandler;
        myHandler.sendMessageDelayed(myHandler.obtainMessage(1, context), 1000L);
    }

    public void scheduleDestroyRelayChannel(final String str) {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.PhoneContinuityController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleDestroyRelayChannel$0(str);
            }
        }, DESTROY_RELAY_CHANNEL_DELAY_MILLIS);
    }

    public String handleWantRelayResponse(String str) {
        int state;
        RelayLog.logd(TAG, "handleWantRelayResponse.");
        Uri uri = Uri.parse(str);
        String number = RelayUtils.Telephony.getNumber(RelayUtils.Relay.currentConnection);
        String strValueOf = String.valueOf(uri.getPort());
        Connection connection = RelayUtils.Relay.currentConnection;
        if (connection != null && connection.getState() == 2) {
            return MessageCodecUtils.encodeResponseMessage("wantRelay", strValueOf, ResponseObject.getDefaultSuccessResponseWithCallState(number, RelayUtils.Relay.currentConnection.getState()));
        }
        if (RelayUtils.Relay.currentConnection != null) {
            RelayLog.logd(TAG, "handleWantRelayResponse, not ringing state");
            state = RelayUtils.Relay.currentConnection.getState();
        } else {
            state = 6;
        }
        return MessageCodecUtils.encodeResponseMessage("wantRelay", strValueOf, ResponseObject.getDefaultFailedResponseWithCallState(number, state));
    }

    public void removeDevice(String str) {
        ContinuityChannelManager continuityChannelManager;
        this.mRelayFilteredServiceInfoMap.remove(str);
        RelayDeviceChangeCallback relayDeviceChangeCallback = this.mRelayDeviceChangeCallback;
        if (relayDeviceChangeCallback != null) {
            relayDeviceChangeCallback.onDeviceChange();
            RelayLog.logi(TAG, "onDeviceChange for remove device");
        }
        if (!this.mRelayChannelInfoMap.containsKey(str) || (continuityChannelManager = this.mChannelManager) == null) {
            return;
        }
        continuityChannelManager.destroyChannel(((RelayChannelInfo) this.mRelayChannelInfoMap.get(str)).mChannelId);
        this.mRelayChannelInfoMap.remove(str);
    }

    public boolean getRelayAudioServiceState() {
        return this.mRelayAudioServiceStarted;
    }

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                RelayUtils.Relay.stopRelayAudioService((Context) message.obj);
                DistAudioDeviceProvider.getInstance().releaseMiAudioManager();
                PhoneContinuityController.this.mRelayAudioServiceStarted = false;
            }
        }
    }
}

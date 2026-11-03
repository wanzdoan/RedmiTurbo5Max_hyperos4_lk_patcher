package com.android.phone;

import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.ims.RcsConfig;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IRcsConfigCallback;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.SparseArray;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.ims.FeatureConnector;
import com.android.ims.RcsFeatureManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.internal.telephony.util.HandlerExecutor;
import com.android.internal.util.CollectionUtils;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class RcsProvisioningMonitor {
    private static final boolean DBG = Build.IS_ENG;
    private static RcsProvisioningMonitor sInstance;
    private final CarrierConfigManager mCarrierConfigManager;
    private Boolean mDeviceSingleRegistrationEnabledOverride;
    private String mDmaPackageName;
    private FeatureConnectorFactory mFeatureFactory;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private final PhoneGlobals mPhone;
    private RcsStats mRcsStats;
    private final RoleManagerAdapter mRoleManager;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyRegistryManager mTelephonyRegistryManager;
    private volatile boolean mTestModeEnabled;
    private final ConcurrentHashMap mRcsProvisioningInfos = new ConcurrentHashMap();
    private final HashMap mCarrierSingleRegistrationEnabledOverride = new HashMap();
    private final ConcurrentHashMap mImsFeatureValidationOverride = new ConcurrentHashMap();
    private final SparseArray mRcsFeatureListeners = new SparseArray();
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.RcsProvisioningMonitor.1
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            if (RcsProvisioningMonitor.this.mHandler.hasMessages(1)) {
                return;
            }
            RcsProvisioningMonitor.this.mHandler.sendEmptyMessage(1);
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.RcsProvisioningMonitor.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                RcsProvisioningMonitor.logv("Carrier-config changed for sub : " + intExtra);
                if (!SubscriptionManager.isValidSubscriptionId(intExtra) || RcsProvisioningMonitor.this.mHandler.hasMessages(3)) {
                    return;
                }
                RcsProvisioningMonitor.this.mHandler.sendEmptyMessage(3);
            }
        }
    };
    private final DmaChangedListener mDmaChangedListener = new DmaChangedListener();

    public interface FeatureConnectorFactory {
        FeatureConnector create(Context context, int i, FeatureConnector.Listener listener, Executor executor, String str);
    }

    public interface RoleManagerAdapter {
        void addOnRoleHoldersChangedListenerAsUser(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle);

        List getRoleHolders(String str);

        void removeOnRoleHoldersChangedListenerAsUser(OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle);
    }

    final class DmaChangedListener implements OnRoleHoldersChangedListener {
        private DmaChangedListener() {
        }

        public void onRoleHoldersChanged(String str, UserHandle userHandle) {
            if ("android.app.role.SMS".equals(str)) {
                RcsProvisioningMonitor.logv("default messaging application changed.");
                RcsProvisioningMonitor.this.mHandler.sendEmptyMessage(2);
            }
        }

        public void register() {
            try {
                RcsProvisioningMonitor.this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(RcsProvisioningMonitor.this.mPhone.getMainExecutor(), this, UserHandle.SYSTEM);
            } catch (RuntimeException e) {
                RcsProvisioningMonitor.loge("Could not register dma change listener due to " + e);
            }
        }

        public void unregister() {
            try {
                RcsProvisioningMonitor.this.mRoleManager.removeOnRoleHoldersChangedListenerAsUser(this, UserHandle.SYSTEM);
            } catch (RuntimeException e) {
                RcsProvisioningMonitor.loge("Could not unregister dma change listener due to " + e);
            }
        }
    }

    final class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            RcsProvisioningMonitor.logv("handleMessage: " + message);
            switch (message.what) {
                case 1:
                    RcsProvisioningMonitor.this.onSubChanged();
                    break;
                case 2:
                    RcsProvisioningMonitor.this.onDefaultMessagingApplicationChanged();
                    break;
                case 3:
                    RcsProvisioningMonitor.this.onCarrierConfigChange();
                    break;
                case 4:
                    RcsProvisioningMonitor.this.onConfigReceived(message.arg1, (byte[]) message.obj, message.arg2 == 1);
                    break;
                case 5:
                    RcsProvisioningMonitor.this.onReconfigRequest(message.arg1);
                    break;
                case 6:
                    Boolean bool = (Boolean) message.obj;
                    if (!RcsProvisioningMonitor.booleanEquals(bool, RcsProvisioningMonitor.this.mDeviceSingleRegistrationEnabledOverride)) {
                        RcsProvisioningMonitor.this.mDeviceSingleRegistrationEnabledOverride = bool;
                        RcsProvisioningMonitor.this.onCarrierConfigChange();
                    }
                    break;
                case 7:
                    Boolean bool2 = (Boolean) message.obj;
                    if (!RcsProvisioningMonitor.booleanEquals(bool2, (Boolean) RcsProvisioningMonitor.this.mCarrierSingleRegistrationEnabledOverride.put(Integer.valueOf(message.arg1), bool2))) {
                        RcsProvisioningMonitor.this.onCarrierConfigChange();
                    }
                    break;
                case 8:
                    RcsProvisioningMonitor.this.reset();
                    break;
                case 9:
                default:
                    RcsProvisioningMonitor.loge("Unhandled event " + message.what);
                    break;
                case 10:
                    RcsProvisioningMonitor.this.onInitialize();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitialize() {
        this.mDmaPackageName = getDmaPackageName();
        logv("DMA is " + this.mDmaPackageName);
        init();
    }

    final class RcsProvisioningInfo {
        private volatile byte[] mConfig;
        private boolean mHasReconfigRequest;
        private IImsConfig mIImsConfig;
        private ArraySet mRcsConfigCallbacks = new ArraySet();
        private volatile int mSingleRegistrationCapability;
        private int mSubId;

        RcsProvisioningInfo(int i, int i2, byte[] bArr) {
            this.mSubId = i;
            this.mSingleRegistrationCapability = i2;
            this.mConfig = bArr;
            RcsProvisioningMonitor.this.registerRcsFeatureListener(this);
        }

        int getSubId() {
            return this.mSubId;
        }

        void setSingleRegistrationCapability(int i) {
            if (this.mSingleRegistrationCapability != i) {
                this.mSingleRegistrationCapability = i;
                notifyDma();
                RcsProvisioningMonitor.this.mRcsStats.setEnableSingleRegistration(this.mSubId, this.mSingleRegistrationCapability == 0);
            }
        }

        void notifyDma() {
            if ((this.mSingleRegistrationCapability & 61440) != 61440) {
                RcsProvisioningMonitor.logi("notify default messaging app for sub:" + this.mSubId + " with capability:" + this.mSingleRegistrationCapability);
                RcsProvisioningMonitor.this.notifyDmaForSub(this.mSubId, this.mSingleRegistrationCapability);
            }
        }

        int getSingleRegistrationCapability() {
            return this.mSingleRegistrationCapability;
        }

        void setConfig(byte[] bArr) {
            if (Arrays.equals(this.mConfig, bArr)) {
                return;
            }
            this.mConfig = bArr;
            if (this.mConfig != null) {
                notifyRcsAutoConfigurationReceived();
            } else {
                notifyRcsAutoConfigurationRemoved();
            }
        }

        byte[] getConfig() {
            return this.mConfig;
        }

        boolean addRcsConfigCallback(IRcsConfigCallback iRcsConfigCallback) {
            if (this.mIImsConfig == null) {
                RcsProvisioningMonitor.logd("fail to addRcsConfigCallback as imsConfig is null");
                return false;
            }
            synchronized (this.mRcsConfigCallbacks) {
                try {
                    try {
                        this.mIImsConfig.addRcsConfigCallback(iRcsConfigCallback);
                        this.mRcsConfigCallbacks.add(iRcsConfigCallback);
                    } catch (RemoteException e) {
                        RcsProvisioningMonitor.loge("fail to addRcsConfigCallback due to " + e);
                        return false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return true;
        }

        boolean removeRcsConfigCallback(IRcsConfigCallback iRcsConfigCallback) {
            boolean z;
            synchronized (this.mRcsConfigCallbacks) {
                IImsConfig iImsConfig = this.mIImsConfig;
                if (iImsConfig != null) {
                    try {
                        iImsConfig.removeRcsConfigCallback(iRcsConfigCallback);
                    } catch (RemoteException e) {
                        RcsProvisioningMonitor.loge("fail to removeRcsConfigCallback due to " + e);
                    }
                    z = true;
                } else {
                    z = false;
                }
                try {
                    iRcsConfigCallback.onRemoved();
                } catch (RemoteException unused) {
                    RcsProvisioningMonitor.logd("Failed to notify onRemoved due to dead binder of " + iRcsConfigCallback);
                }
                this.mRcsConfigCallbacks.remove(iRcsConfigCallback);
            }
            return z;
        }

        void triggerRcsReconfiguration() {
            if (this.mIImsConfig != null) {
                try {
                    RcsProvisioningMonitor.logv("triggerRcsReconfiguration for sub:" + this.mSubId);
                    this.mIImsConfig.triggerRcsReconfiguration();
                    this.mHasReconfigRequest = false;
                    return;
                } catch (RemoteException e) {
                    RcsProvisioningMonitor.loge("triggerRcsReconfiguration failed due to " + e);
                    return;
                }
            }
            RcsProvisioningMonitor.logd("triggerRcsReconfiguration failed due to IImsConfig null.");
            this.mHasReconfigRequest = true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void destroy() {
            RcsProvisioningMonitor.this.unregisterRcsFeatureListener(this);
            clear();
            this.mIImsConfig = null;
            this.mRcsConfigCallbacks = null;
        }

        void clear() {
            setConfig(null);
            clearCallbacks();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onRcsStatusChanged(IImsConfig iImsConfig) {
            RcsProvisioningMonitor.logv("onRcsStatusChanged for sub:" + this.mSubId + ", IImsConfig?" + iImsConfig);
            if (this.mIImsConfig != iImsConfig) {
                this.mIImsConfig = iImsConfig;
                if (iImsConfig != null) {
                    if (this.mHasReconfigRequest) {
                        triggerRcsReconfiguration();
                    } else {
                        notifyRcsAutoConfigurationReceived();
                    }
                    registerMetricsCallback();
                    return;
                }
                clearCallbacks();
            }
        }

        private void notifyRcsAutoConfigurationReceived() {
            if (this.mConfig == null) {
                RcsProvisioningMonitor.logd("Rcs config is null for sub : " + this.mSubId);
            } else {
                if (this.mIImsConfig != null) {
                    try {
                        RcsProvisioningMonitor.logv("notifyRcsAutoConfigurationReceived for sub:" + this.mSubId);
                        this.mIImsConfig.notifyRcsAutoConfigurationReceived(this.mConfig, false);
                        return;
                    } catch (RemoteException e) {
                        RcsProvisioningMonitor.loge("notifyRcsAutoConfigurationReceived failed due to " + e);
                        return;
                    }
                }
                RcsProvisioningMonitor.logd("notifyRcsAutoConfigurationReceived failed due to IImsConfig null.");
            }
        }

        private void notifyRcsAutoConfigurationRemoved() {
            if (this.mIImsConfig != null) {
                try {
                    RcsProvisioningMonitor.logv("notifyRcsAutoConfigurationRemoved for sub:" + this.mSubId);
                    this.mIImsConfig.notifyRcsAutoConfigurationRemoved();
                    return;
                } catch (RemoteException e) {
                    RcsProvisioningMonitor.loge("notifyRcsAutoConfigurationRemoved failed due to " + e);
                    return;
                }
            }
            RcsProvisioningMonitor.logd("notifyRcsAutoConfigurationRemoved failed due to IImsConfig null.");
        }

        private void clearCallbacks() {
            ArraySet arraySet = this.mRcsConfigCallbacks;
            if (arraySet == null) {
                return;
            }
            synchronized (arraySet) {
                Iterator it = this.mRcsConfigCallbacks.iterator();
                while (it.hasNext()) {
                    IRcsConfigCallback iRcsConfigCallback = (IRcsConfigCallback) it.next();
                    IImsConfig iImsConfig = this.mIImsConfig;
                    if (iImsConfig != null) {
                        try {
                            iImsConfig.removeRcsConfigCallback(iRcsConfigCallback);
                        } catch (RemoteException e) {
                            RcsProvisioningMonitor.loge("fail to removeRcsConfigCallback due to " + e);
                        }
                        try {
                            iRcsConfigCallback.onRemoved();
                        } catch (RemoteException unused) {
                            RcsProvisioningMonitor.logd("Failed to notify onRemoved due to dead binder of " + iRcsConfigCallback);
                        }
                        it.remove();
                    } else {
                        iRcsConfigCallback.onRemoved();
                        it.remove();
                    }
                    throw th;
                }
            }
        }

        private void registerMetricsCallback() {
            RcsStats.RcsProvisioningCallback rcsProvisioningCallback = RcsProvisioningMonitor.this.mRcsStats.getRcsProvisioningCallback(this.mSubId, this.mSingleRegistrationCapability == 0);
            if (rcsProvisioningCallback == null || rcsProvisioningCallback.getRegistered() || !addRcsConfigCallback(rcsProvisioningCallback)) {
                return;
            }
            rcsProvisioningCallback.setRegistered(true);
        }
    }

    final class RcsFeatureListener implements FeatureConnector.Listener {
        private FeatureConnector mConnector;
        private RcsFeatureManager mRcsFeatureManager;
        private final ArraySet mRcsProvisioningInfos = new ArraySet();

        RcsFeatureListener(RcsProvisioningMonitor rcsProvisioningMonitor, int i) {
            FeatureConnector featureConnectorCreate = rcsProvisioningMonitor.mFeatureFactory.create(rcsProvisioningMonitor.mPhone, i, this, new HandlerExecutor(rcsProvisioningMonitor.mHandler), "RcsProvisioningMonitor");
            this.mConnector = featureConnectorCreate;
            featureConnectorCreate.connect();
        }

        void destroy() {
            this.mConnector.disconnect();
            this.mConnector = null;
            this.mRcsFeatureManager = null;
            this.mRcsProvisioningInfos.clear();
        }

        void addRcsProvisioningInfo(RcsProvisioningInfo rcsProvisioningInfo) {
            if (this.mRcsProvisioningInfos.contains(rcsProvisioningInfo)) {
                return;
            }
            this.mRcsProvisioningInfos.add(rcsProvisioningInfo);
            RcsFeatureManager rcsFeatureManager = this.mRcsFeatureManager;
            rcsProvisioningInfo.onRcsStatusChanged(rcsFeatureManager == null ? null : rcsFeatureManager.getConfig());
        }

        void removeRcsProvisioningInfo(RcsProvisioningInfo rcsProvisioningInfo) {
            this.mRcsProvisioningInfos.remove(rcsProvisioningInfo);
        }

        public void connectionReady(final RcsFeatureManager rcsFeatureManager, int i) {
            this.mRcsFeatureManager = rcsFeatureManager;
            this.mRcsProvisioningInfos.forEach(new Consumer() { // from class: com.android.phone.RcsProvisioningMonitor$RcsFeatureListener$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((RcsProvisioningMonitor.RcsProvisioningInfo) obj).onRcsStatusChanged(rcsFeatureManager.getConfig());
                }
            });
        }

        public void connectionUnavailable(int i) {
            this.mRcsFeatureManager = null;
            this.mRcsProvisioningInfos.forEach(new Consumer() { // from class: com.android.phone.RcsProvisioningMonitor$RcsFeatureListener$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((RcsProvisioningMonitor.RcsProvisioningInfo) obj).onRcsStatusChanged(null);
                }
            });
        }
    }

    public RcsProvisioningMonitor(PhoneGlobals phoneGlobals, Looper looper, RoleManagerAdapter roleManagerAdapter, FeatureConnectorFactory featureConnectorFactory, RcsStats rcsStats, FeatureFlags featureFlags) {
        this.mPhone = phoneGlobals;
        this.mFeatureFlags = featureFlags;
        this.mHandler = new MyHandler(looper);
        this.mCarrierConfigManager = (CarrierConfigManager) phoneGlobals.getSystemService(CarrierConfigManager.class);
        this.mSubscriptionManager = (SubscriptionManager) phoneGlobals.getSystemService(SubscriptionManager.class);
        this.mTelephonyRegistryManager = (TelephonyRegistryManager) phoneGlobals.getSystemService(TelephonyRegistryManager.class);
        this.mRoleManager = roleManagerAdapter;
        this.mFeatureFactory = featureConnectorFactory;
        this.mRcsStats = rcsStats;
        onInitialize();
    }

    public static RcsProvisioningMonitor make(PhoneGlobals phoneGlobals, FeatureFlags featureFlags) {
        if (sInstance == null) {
            logd("RcsProvisioningMonitor created.");
            HandlerThread handlerThread = new HandlerThread("RcsProvisioningMonitor");
            handlerThread.start();
            sInstance = new RcsProvisioningMonitor(phoneGlobals, handlerThread.getLooper(), new RoleManagerAdapterImpl(phoneGlobals), new FeatureConnectorFactory() { // from class: com.android.phone.RcsProvisioningMonitor$$ExternalSyntheticLambda0
                @Override // com.android.phone.RcsProvisioningMonitor.FeatureConnectorFactory
                public final FeatureConnector create(Context context, int i, FeatureConnector.Listener listener, Executor executor, String str) {
                    return RcsFeatureManager.getConnector(context, i, listener, executor, str);
                }
            }, RcsStats.getInstance(), featureFlags);
        }
        return sInstance;
    }

    public static RcsProvisioningMonitor getInstance() {
        return sInstance;
    }

    private void init() {
        logd("init.");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mPhone.registerReceiver(this.mReceiver, intentFilter);
        TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryManager;
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = this.mSubChangedListener;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        telephonyRegistryManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler));
        this.mDmaChangedListener.register();
        onSubChanged();
    }

    private void release() {
        logd("release.");
        this.mDmaChangedListener.unregister();
        this.mTelephonyRegistryManager.removeOnSubscriptionsChangedListener(this.mSubChangedListener);
        this.mPhone.unregisterReceiver(this.mReceiver);
        int i = 0;
        while (true) {
            int size = this.mRcsFeatureListeners.size();
            SparseArray sparseArray = this.mRcsFeatureListeners;
            if (i < size) {
                ((RcsFeatureListener) sparseArray.valueAt(i)).destroy();
                i++;
            } else {
                sparseArray.clear();
                this.mRcsProvisioningInfos.forEach(new BiConsumer() { // from class: com.android.phone.RcsProvisioningMonitor$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        ((RcsProvisioningMonitor.RcsProvisioningInfo) obj2).destroy();
                    }
                });
                this.mRcsProvisioningInfos.clear();
                this.mCarrierSingleRegistrationEnabledOverride.clear();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        release();
        init();
    }

    public void destroy() {
        logd("destroy it.");
        release();
        this.mHandler.getLooper().quit();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public byte[] getConfig(int i) {
        if (this.mRcsProvisioningInfos.containsKey(Integer.valueOf(i))) {
            return ((RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i))).getConfig();
        }
        return null;
    }

    public Boolean isRcsVolteSingleRegistrationEnabled(int i) {
        if (this.mRcsProvisioningInfos.containsKey(Integer.valueOf(i))) {
            return Boolean.valueOf(((RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i))).getSingleRegistrationCapability() == 0);
        }
        return null;
    }

    public void updateConfig(int i, byte[] bArr, boolean z) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(4, i, z ? 1 : 0, bArr));
    }

    public void requestReconfig(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(5, i, 0));
    }

    public boolean registerRcsProvisioningCallback(int i, IRcsConfigCallback iRcsConfigCallback) {
        RcsProvisioningInfo rcsProvisioningInfo = (RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i));
        if (rcsProvisioningInfo == null) {
            logd("fail to register rcs provisioning callback due to subscription unavailable");
            return false;
        }
        return rcsProvisioningInfo.addRcsConfigCallback(iRcsConfigCallback);
    }

    public boolean unregisterRcsProvisioningCallback(int i, IRcsConfigCallback iRcsConfigCallback) {
        RcsProvisioningInfo rcsProvisioningInfo = (RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i));
        if (rcsProvisioningInfo == null) {
            logd("fail to unregister rcs provisioning changed due to subscription unavailable");
            return false;
        }
        return rcsProvisioningInfo.removeRcsConfigCallback(iRcsConfigCallback);
    }

    public void setTestModeEnabled(boolean z) {
        logv("setTestModeEnabled as " + z);
        if (this.mTestModeEnabled != z) {
            this.mTestModeEnabled = z;
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(8));
        }
    }

    public boolean getTestModeEnabled() {
        return this.mTestModeEnabled;
    }

    public void overrideDeviceSingleRegistrationEnabled(Boolean bool) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(6, bool));
    }

    public boolean overrideCarrierSingleRegistrationEnabled(int i, Boolean bool) {
        if (!this.mRcsProvisioningInfos.containsKey(Integer.valueOf(i))) {
            return false;
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(7, i, 0, bool));
        return true;
    }

    public boolean overrideImsFeatureValidation(int i, Boolean bool) {
        ConcurrentHashMap concurrentHashMap = this.mImsFeatureValidationOverride;
        if (bool == null) {
            concurrentHashMap.remove(Integer.valueOf(i));
            return true;
        }
        concurrentHashMap.put(Integer.valueOf(i), bool);
        return true;
    }

    public boolean getDeviceSingleRegistrationEnabled() {
        Iterator it = this.mRcsProvisioningInfos.values().iterator();
        return it.hasNext() && (((RcsProvisioningInfo) it.next()).getSingleRegistrationCapability() & 1) == 0;
    }

    public boolean getCarrierSingleRegistrationEnabled(int i) {
        return this.mRcsProvisioningInfos.containsKey(Integer.valueOf(i)) && (((RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i))).getSingleRegistrationCapability() & 2) == 0;
    }

    public Boolean getImsFeatureValidationOverride(int i) {
        return (Boolean) this.mImsFeatureValidationOverride.get(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDefaultMessagingApplicationChanged() {
        String dmaPackageName = getDmaPackageName();
        if (TextUtils.equals(this.mDmaPackageName, dmaPackageName) || dmaPackageName == null) {
            return;
        }
        this.mDmaPackageName = dmaPackageName;
        logv("new default messaging application " + this.mDmaPackageName);
        this.mRcsProvisioningInfos.forEach(new BiConsumer() { // from class: com.android.phone.RcsProvisioningMonitor$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                this.f$0.lambda$onDefaultMessagingApplicationChanged$1((Integer) obj, (RcsProvisioningMonitor.RcsProvisioningInfo) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDefaultMessagingApplicationChanged$1(Integer num, RcsProvisioningInfo rcsProvisioningInfo) {
        rcsProvisioningInfo.notifyDma();
        byte[] config = rcsProvisioningInfo.getConfig();
        rcsProvisioningInfo.clear();
        if (isAcsUsed(num.intValue())) {
            logv("acs used, trigger to re-configure.");
            updateConfigForSub(num.intValue(), null, true);
            rcsProvisioningInfo.triggerRcsReconfiguration();
        } else {
            logv("acs not used, set cached config and notify.");
            rcsProvisioningInfo.setConfig(config);
        }
        this.mRcsStats.onRcsClientProvisioningStats(num.intValue(), 3);
    }

    private void updateConfigForSub(int i, byte[] bArr, boolean z) {
        logv("updateConfigForSub, subId:" + i + ", mTestModeEnabled:" + this.mTestModeEnabled);
        if (this.mTestModeEnabled) {
            return;
        }
        RcsConfig.updateConfigForSub(this.mPhone, i, bArr, z);
    }

    private byte[] loadConfigForSub(int i) {
        logv("loadConfigForSub, subId:" + i + ", mTestModeEnabled:" + this.mTestModeEnabled);
        if (this.mTestModeEnabled) {
            return null;
        }
        return RcsConfig.loadRcsConfigForSub(this.mPhone, i, false);
    }

    private boolean isAcsUsed(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        if (configForSubId == null) {
            return false;
        }
        return configForSubId.getBoolean("use_acs_for_rcs_bool");
    }

    private int getSingleRegistrationRequiredByCarrier(int i) {
        Boolean bool = (Boolean) this.mCarrierSingleRegistrationEnabledOverride.get(Integer.valueOf(i));
        if (bool != null) {
            return bool.booleanValue() ? 0 : 2;
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        if (CarrierConfigManager.isConfigForIdentifiedCarrier(configForSubId)) {
            return configForSubId.getBoolean("ims.ims_single_registration_required_bool") ? 0 : 2;
        }
        return 61440;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getSingleRegistrationCapableValue(int i) {
        boolean zHasSystemFeature;
        Boolean bool = this.mDeviceSingleRegistrationEnabledOverride;
        if (bool != null) {
            zHasSystemFeature = bool.booleanValue();
        } else {
            zHasSystemFeature = this.mPhone.getPackageManager().hasSystemFeature("android.hardware.telephony.ims.singlereg");
        }
        int singleRegistrationRequiredByCarrier = getSingleRegistrationRequiredByCarrier(i) | (!zHasSystemFeature);
        logv("SingleRegistrationCapableValue : " + singleRegistrationRequiredByCarrier);
        return singleRegistrationRequiredByCarrier;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigChange() {
        logv("onCarrierConfigChange");
        this.mRcsProvisioningInfos.forEach(new BiConsumer() { // from class: com.android.phone.RcsProvisioningMonitor$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                this.f$0.lambda$onCarrierConfigChange$2((Integer) obj, (RcsProvisioningMonitor.RcsProvisioningInfo) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCarrierConfigChange$2(Integer num, RcsProvisioningInfo rcsProvisioningInfo) {
        rcsProvisioningInfo.setSingleRegistrationCapability(getSingleRegistrationCapableValue(num.intValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubChanged() {
        int[] activeSubscriptionIdList = this.mSubscriptionManager.getActiveSubscriptionIdList();
        ArraySet arraySet = new ArraySet(this.mRcsProvisioningInfos.keySet());
        for (int i : activeSubscriptionIdList) {
            arraySet.remove(Integer.valueOf(i));
            if (!this.mRcsProvisioningInfos.containsKey(Integer.valueOf(i))) {
                byte[] bArrLoadConfigForSub = loadConfigForSub(i);
                int singleRegistrationCapableValue = getSingleRegistrationCapableValue(i);
                logv("new info is created for sub : " + i + ", single registration capability :" + singleRegistrationCapableValue + ", rcs config : " + Arrays.toString(bArrLoadConfigForSub));
                this.mRcsProvisioningInfos.put(Integer.valueOf(i), new RcsProvisioningInfo(i, singleRegistrationCapableValue, bArrLoadConfigForSub));
            }
        }
        arraySet.forEach(new Consumer() { // from class: com.android.phone.RcsProvisioningMonitor$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onSubChanged$3((Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSubChanged$3(Integer num) {
        RcsProvisioningInfo rcsProvisioningInfo = (RcsProvisioningInfo) this.mRcsProvisioningInfos.remove(num);
        if (rcsProvisioningInfo != null) {
            rcsProvisioningInfo.destroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigReceived(int i, byte[] bArr, boolean z) {
        logv("onConfigReceived, subId:" + i + ", config:" + Arrays.toString(bArr) + ", isCompressed:" + z);
        RcsProvisioningInfo rcsProvisioningInfo = (RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i));
        if (rcsProvisioningInfo == null) {
            logd("sub[" + i + "] has been removed");
            return;
        }
        rcsProvisioningInfo.setConfig(z ? RcsConfig.decompressGzip(bArr) : bArr);
        updateConfigForSub(i, bArr, z);
        if (isAcsUsed(i)) {
            this.mRcsStats.onRcsAcsProvisioningStats(i, 200, 2, isRcsVolteSingleRegistrationEnabled(i).booleanValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReconfigRequest(int i) {
        logv("onReconfigRequest, subId:" + i);
        RcsProvisioningInfo rcsProvisioningInfo = (RcsProvisioningInfo) this.mRcsProvisioningInfos.get(Integer.valueOf(i));
        if (rcsProvisioningInfo != null) {
            rcsProvisioningInfo.setConfig(null);
            updateConfigForSub(i, null, true);
            rcsProvisioningInfo.triggerRcsReconfiguration();
        }
        this.mRcsStats.onRcsClientProvisioningStats(i, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDmaForSub(int i, int i2) {
        Intent intent = new Intent("android.telephony.ims.action.RCS_SINGLE_REGISTRATION_CAPABILITY_UPDATE");
        intent.setPackage(this.mDmaPackageName);
        intent.putExtra("android.telephony.ims.extra.SUBSCRIPTION_ID", i);
        intent.putExtra("android.telephony.ims.extra.STATUS", i2);
        logv("notify " + intent + ", sub:" + i + ", capability:" + i2);
        boolean z = this.mTestModeEnabled;
        PhoneGlobals phoneGlobals = this.mPhone;
        if (!z) {
            phoneGlobals.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.PERFORM_IMS_SINGLE_REGISTRATION");
        } else {
            phoneGlobals.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private String getDmaPackageName() {
        try {
            return (String) CollectionUtils.firstOrNull(this.mRoleManager.getRoleHolders("android.app.role.SMS"));
        } catch (RuntimeException e) {
            loge("Could not get dma name due to " + e);
            return null;
        }
    }

    void registerRcsFeatureListener(RcsProvisioningInfo rcsProvisioningInfo) {
        int slotIndex = SubscriptionManager.getSlotIndex(rcsProvisioningInfo.getSubId());
        RcsFeatureListener rcsFeatureListener = (RcsFeatureListener) this.mRcsFeatureListeners.get(slotIndex);
        if (rcsFeatureListener == null) {
            rcsFeatureListener = new RcsFeatureListener(this, slotIndex);
            this.mRcsFeatureListeners.put(slotIndex, rcsFeatureListener);
        }
        rcsFeatureListener.addRcsProvisioningInfo(rcsProvisioningInfo);
    }

    void unregisterRcsFeatureListener(RcsProvisioningInfo rcsProvisioningInfo) {
        for (int i = 0; i < this.mRcsFeatureListeners.size(); i++) {
            ((RcsFeatureListener) this.mRcsFeatureListeners.valueAt(i)).removeRcsProvisioningInfo(rcsProvisioningInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean booleanEquals(Boolean bool, Boolean bool2) {
        if (bool == null && bool2 == null) {
            return true;
        }
        Boolean bool3 = Boolean.TRUE;
        if (bool3.equals(bool) && bool3.equals(bool2)) {
            return true;
        }
        Boolean bool4 = Boolean.FALSE;
        return bool4.equals(bool) && bool4.equals(bool2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logv(String str) {
        if (DBG) {
            Rlog.d("RcsProvisioningMonitor", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logi(String str) {
        Rlog.i("RcsProvisioningMonitor", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Rlog.d("RcsProvisioningMonitor", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Rlog.e("RcsProvisioningMonitor", str);
    }

    class RoleManagerAdapterImpl implements RoleManagerAdapter {
        private final RoleManager mRoleManager;

        private RoleManagerAdapterImpl(Context context) {
            this.mRoleManager = (RoleManager) context.getSystemService(RoleManager.class);
        }

        @Override // com.android.phone.RcsProvisioningMonitor.RoleManagerAdapter
        public List getRoleHolders(String str) {
            return this.mRoleManager.getRoleHolders(str);
        }

        @Override // com.android.phone.RcsProvisioningMonitor.RoleManagerAdapter
        public void addOnRoleHoldersChangedListenerAsUser(Executor executor, OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle) {
            this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(executor, onRoleHoldersChangedListener, userHandle);
        }

        @Override // com.android.phone.RcsProvisioningMonitor.RoleManagerAdapter
        public void removeOnRoleHoldersChangedListenerAsUser(OnRoleHoldersChangedListener onRoleHoldersChangedListener, UserHandle userHandle) {
            this.mRoleManager.removeOnRoleHoldersChangedListenerAsUser(onRoleHoldersChangedListener, userHandle);
        }
    }
}

package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.ims.aidl.IFeatureProvisioningCallback;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.util.SparseArray;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsConfig;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.RcsFeatureManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.util.HandlerExecutor;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.IntPredicate;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class ImsProvisioningController {
    protected static final int EVENT_MULTI_SIM_CONFIGURATION_CHANGE = 3;
    private static ImsProvisioningController sInstance;
    private final PhoneGlobals mApp;
    private final CarrierConfigManager mCarrierConfigManager;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private final ImsProvisioningLoader mImsProvisioningLoader;
    private final MmTelFeatureConnector mMmTelFeatureConnector;
    private int mNumSlot;
    private final RcsFeatureConnector mRcsFeatureConnector;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyRegistryManager mTelephonyRegistryManager;
    private static final int[] LOCAL_IMS_CONFIG_KEYS = {10, 11, 28, 25};
    private static final int[] LOCAL_RADIO_TECHS = {0, 1, 2, 3};
    private static final int[] LOCAL_MMTEL_CAPABILITY = {1, 2, 4, 8, 16};
    private static final int[] LOCAL_RCS_CAPABILITY = {1, 2};
    private static final Map KEYS_MMTEL_CAPABILITY = Map.of(1, "ims.capability_type_voice_int_array", 2, "ims.capability_type_video_int_array", 4, "ims.capability_type_ut_int_array", 8, "ims.capability_type_sms_int_array", 16, "ims.capability_type_call_composer_int_array");
    private static final Map KEYS_RCS_CAPABILITY = Map.of(1, "ims.capability_type_options_uce_int_array", 2, "ims.capability_type_presence_uce_int_array");
    private final SparseArray mMmTelFeatureListenersSlotMap = new SparseArray();
    private final SparseArray mRcsFeatureListenersSlotMap = new SparseArray();
    private final SparseArray mProvisioningCallbackManagersSlotMap = new SparseArray();

    public interface MmTelFeatureConnector {
        FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor);
    }

    public interface RcsFeatureConnector {
        FeatureConnector create(Context context, int i, FeatureConnector.Listener listener, Executor executor, String str);
    }

    private boolean getBoolValue(int i) {
        return i == 1;
    }

    private int getCapabilityFromKey(int i) {
        if (i == 10) {
            return 1;
        }
        if (i == 11 || i == 25) {
            return 2;
        }
        return i != 28 ? -1 : 1;
    }

    private int getIntValue(boolean z) {
        return z ? 1 : 0;
    }

    private int getKeyFromCapability(int i, int i2) {
        if (i == 1 && i2 == 1) {
            return 28;
        }
        if (i == 1 && i2 == 0) {
            return 10;
        }
        return (i == 2 && i2 == 0) ? 11 : -1;
    }

    private int getTechFromKey(int i) {
        if (i == 10 || i == 11 || i == 25) {
            return 0;
        }
        return i != 28 ? -1 : 1;
    }

    final class FeatureProvisioningData {
        public final int mCapability;
        public final boolean mIsMmTel;
        public final boolean mProvisioned;
        public final int mTech;

        FeatureProvisioningData(int i, int i2, boolean z, boolean z2) {
            this.mCapability = i;
            this.mTech = i2;
            this.mProvisioned = z;
            this.mIsMmTel = z2;
        }
    }

    final class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                ImsProvisioningController.this.onSubscriptionsChanged();
                return;
            }
            if (i == 2) {
                try {
                    ((ProvisioningCallbackManager) ImsProvisioningController.this.mProvisioningCallbackManagersSlotMap.get(message.arg1)).notifyProvisioningCapabilityChanged((FeatureProvisioningData) message.obj);
                    return;
                } catch (NullPointerException unused) {
                    ImsProvisioningController.this.logw("Handler", message.arg1, "can not find callback manager, message" + message.what);
                    return;
                }
            }
            if (i == 3) {
                ImsProvisioningController.this.onMultiSimConfigChanged(((Integer) ((AsyncResult) message.obj).result).intValue());
                return;
            }
            if (i == 4) {
                ImsProvisioningController.this.logAttr("ImsConfig", "EVENT_PROVISIONING_VALUE_CHANGED", message.arg1, "changed provisioning value, item : " + message.arg2 + " value : " + ((Integer) message.obj).intValue());
                ImsProvisioningController.this.updateCapabilityTechFromKey("ImsConfig[" + message.arg1 + "]", message.arg1, message.arg2, ((Integer) message.obj).intValue());
                return;
            }
            if (i == 5) {
                int i2 = message.arg1;
                int i3 = message.arg2;
                IFeatureProvisioningCallback iFeatureProvisioningCallback = (IFeatureProvisioningCallback) message.obj;
                ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
                StringBuilder sb = new StringBuilder("slotId ");
                sb.append(i2);
                sb.append(" subId ");
                sb.append(i3);
                sb.append(" callback ");
                sb.append(iFeatureProvisioningCallback != null);
                imsProvisioningController.log(sb.toString());
                ImsProvisioningController.this.notifyMmTelProvisioningStatus(i2, i3, iFeatureProvisioningCallback);
                ImsProvisioningController.this.notifyRcsProvisioningStatus(i2, i3, iFeatureProvisioningCallback);
                return;
            }
            ImsProvisioningController.this.log("unknown message " + message);
        }
    }

    final class ProvisioningCallbackManager {
        private RemoteCallbackList mIFeatureProvisioningCallbackList = new RemoteCallbackList();
        private int mSlotId;
        private int mSubId;

        ProvisioningCallbackManager(int i) {
            this.mSlotId = i;
            this.mSubId = ImsProvisioningController.this.getSubId(i);
            ImsProvisioningController.this.log("ProvisioningCallbackManager", this.mSlotId, "ProvisioningCallbackManager create");
        }

        public void clear() {
            ImsProvisioningController.this.log("ProvisioningCallbackManager", this.mSlotId, "ProvisioningCallbackManager clear ");
            this.mIFeatureProvisioningCallbackList.kill();
            this.mIFeatureProvisioningCallbackList = new RemoteCallbackList();
        }

        public void registerCallback(IFeatureProvisioningCallback iFeatureProvisioningCallback) {
            if (this.mIFeatureProvisioningCallbackList.register(iFeatureProvisioningCallback, Integer.valueOf(this.mSubId))) {
                return;
            }
            ImsProvisioningController.this.log("ProvisioningCallbackManager", this.mSlotId, "registration callback fail");
        }

        public void unregisterCallback(IFeatureProvisioningCallback iFeatureProvisioningCallback) {
            this.mIFeatureProvisioningCallbackList.unregister(iFeatureProvisioningCallback);
        }

        public void setSubId(int i) {
            if (this.mSubId == i) {
                ImsProvisioningController.this.log("ProvisioningCallbackManager", this.mSlotId, "subId is not changed ");
            } else {
                this.mSubId = i;
                clear();
            }
        }

        public boolean hasCallblacks() {
            int iBeginBroadcast = this.mIFeatureProvisioningCallbackList.beginBroadcast();
            this.mIFeatureProvisioningCallbackList.finishBroadcast();
            return iBeginBroadcast > 0;
        }

        public void notifyProvisioningCapabilityChanged(final FeatureProvisioningData featureProvisioningData) {
            int iBeginBroadcast = this.mIFeatureProvisioningCallbackList.beginBroadcast();
            int i = 0;
            while (true) {
                RemoteCallbackList remoteCallbackList = this.mIFeatureProvisioningCallbackList;
                if (i < iBeginBroadcast) {
                    try {
                        IFeatureProvisioningCallback broadcastItem = remoteCallbackList.getBroadcastItem(i);
                        if (featureProvisioningData.mIsMmTel && Arrays.stream(ImsProvisioningController.LOCAL_MMTEL_CAPABILITY).anyMatch(new IntPredicate() { // from class: com.android.phone.ImsProvisioningController$ProvisioningCallbackManager$$ExternalSyntheticLambda0
                            @Override // java.util.function.IntPredicate
                            public final boolean test(int i2) {
                                return ImsProvisioningController.ProvisioningCallbackManager.$r8$lambda$2mBysql2uZQUpyylG6ZPYdm7Oxc(featureProvisioningData, i2);
                            }
                        })) {
                            broadcastItem.onFeatureProvisioningChanged(featureProvisioningData.mCapability, featureProvisioningData.mTech, featureProvisioningData.mProvisioned);
                            ImsProvisioningController.this.logi("ProvisioningCallbackManager", this.mSlotId, "notifyProvisioningCapabilityChanged : onFeatureProvisioningChanged capability " + featureProvisioningData.mCapability + " tech " + featureProvisioningData.mTech + " isProvisioned " + featureProvisioningData.mProvisioned);
                        } else {
                            int i2 = featureProvisioningData.mCapability;
                            if (i2 == 2) {
                                broadcastItem.onRcsFeatureProvisioningChanged(i2, featureProvisioningData.mTech, featureProvisioningData.mProvisioned);
                                ImsProvisioningController.this.logi("ProvisioningCallbackManager", this.mSlotId, "notifyProvisioningCapabilityChanged : onRcsFeatureProvisioningChanged capability " + featureProvisioningData.mCapability + " tech " + featureProvisioningData.mTech + " isProvisioned " + featureProvisioningData.mProvisioned);
                            } else {
                                ImsProvisioningController.this.loge("ProvisioningCallbackManager", this.mSlotId, "notifyProvisioningCapabilityChanged : unknown capability " + featureProvisioningData.mCapability);
                            }
                        }
                    } catch (RemoteException unused) {
                        ImsProvisioningController.this.loge("ProvisioningCallbackManager", this.mSlotId, "notifyProvisioningChanged: callback #" + i + " failed");
                    }
                    i++;
                } else {
                    remoteCallbackList.finishBroadcast();
                    return;
                }
            }
        }

        public static /* synthetic */ boolean $r8$lambda$2mBysql2uZQUpyylG6ZPYdm7Oxc(FeatureProvisioningData featureProvisioningData, int i) {
            return i == featureProvisioningData.mCapability;
        }
    }

    final class MmTelFeatureListener implements FeatureConnector.Listener {
        private ConfigCallback mConfigCallback;
        private FeatureConnector mConnector;
        private ImsManager mImsManager;
        private boolean mReady = false;
        private boolean mRequiredNotify = false;
        private int mSlotId;
        private int mSubId;

        MmTelFeatureListener(int i) {
            ImsProvisioningController.this.log("MmTelFeatureListener", i, "created");
            this.mSlotId = i;
            this.mSubId = ImsProvisioningController.this.getSubId(i);
            this.mConfigCallback = ImsProvisioningController.this.new ConfigCallback(this.mSubId);
            FeatureConnector featureConnectorCreate = ImsProvisioningController.this.mMmTelFeatureConnector.create(ImsProvisioningController.this.mApp, i, "ImsProvisioningController", this, new HandlerExecutor(ImsProvisioningController.this.mHandler));
            this.mConnector = featureConnectorCreate;
            featureConnectorCreate.connect();
        }

        public void setSubId(int i) {
            if (this.mRequiredNotify && this.mReady) {
                this.mRequiredNotify = false;
                setInitialProvisioningKeys(i);
            }
            if (this.mSubId == i) {
                ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "subId is not changed");
            } else {
                this.mSubId = i;
                this.mConfigCallback.setSubId(i);
            }
        }

        public void destroy() {
            ImsProvisioningController.this.log("destroy");
            ImsManager imsManager = this.mImsManager;
            if (imsManager != null) {
                try {
                    ImsConfig imsConfig = ImsProvisioningController.this.getImsConfig(imsManager);
                    if (imsConfig != null) {
                        imsConfig.removeConfigCallback(this.mConfigCallback);
                    }
                } catch (ImsException e) {
                    ImsProvisioningController.this.logw("MmTelFeatureListener", this.mSlotId, "destroy : " + e.getMessage());
                }
            }
            this.mConfigCallback = null;
            this.mConnector.disconnect();
            this.mConnector = null;
            this.mReady = false;
            this.mImsManager = null;
        }

        public void connectionReady(ImsManager imsManager, int i) {
            ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "connection ready");
            this.mReady = true;
            this.mImsManager = imsManager;
            if (imsManager != null) {
                try {
                    ImsConfig imsConfig = ImsProvisioningController.this.getImsConfig(imsManager);
                    if (imsConfig != null) {
                        imsConfig.addConfigCallback(this.mConfigCallback);
                    }
                } catch (ImsException e) {
                    ImsProvisioningController.this.logw("MmTelFeatureListener", this.mSlotId, "addConfigCallback : " + e.getMessage());
                }
            }
            onMmTelAvailable();
        }

        public void connectionUnavailable(int i) {
            ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "connection unavailable " + i);
            this.mReady = false;
            this.mImsManager = null;
            if (i == 2) {
                onMmTelUnavailable();
            }
        }

        public int setProvisioningValue(int i, int i2) {
            boolean z = this.mReady;
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
            int config = 1;
            if (!z) {
                imsProvisioningController.loge("MmTelFeatureListener", this.mSlotId, "service is Unavailable");
                return 1;
            }
            try {
                config = imsProvisioningController.getImsConfig(this.mImsManager).setConfig(i, i2);
                ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "setConfig called with key " + i + " value " + i2);
                return config;
            } catch (ImsException e) {
                ImsProvisioningController.this.logw("MmTelFeatureListener", this.mSlotId, "setConfig operation failed for key =" + i + ", value =" + i2 + ". Exception:" + e.getMessage());
                return config;
            }
        }

        public int getProvisioningValue(int i) {
            boolean z = this.mReady;
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
            if (!z) {
                imsProvisioningController.loge("MmTelFeatureListener", this.mSlotId, "service is Unavailable");
                return -1;
            }
            try {
                return imsProvisioningController.getImsConfig(this.mImsManager).getConfigInt(i);
            } catch (ImsException e) {
                ImsProvisioningController.this.logw("MmTelFeatureListener", this.mSlotId, "getConfig operation failed for key =" + i + ", value =-1. Exception:" + e.getMessage());
                return -1;
            }
        }

        public void onMmTelAvailable() {
            ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "onMmTelAvailable");
            if (ImsProvisioningController.this.isValidSubId(this.mSubId)) {
                this.mRequiredNotify = false;
                setInitialProvisioningKeys(this.mSubId);
                ProvisioningCallbackManager provisioningCallbackManager = (ProvisioningCallbackManager) ImsProvisioningController.this.mProvisioningCallbackManagersSlotMap.get(this.mSlotId);
                if (provisioningCallbackManager == null || !provisioningCallbackManager.hasCallblacks()) {
                    return;
                }
                ImsProvisioningController.this.notifyMmTelProvisioningStatus(this.mSlotId, this.mSubId, null);
                return;
            }
            this.mRequiredNotify = true;
        }

        public void onMmTelUnavailable() {
            ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "onMmTelUnavailable");
            try {
                ((ProvisioningCallbackManager) ImsProvisioningController.this.mProvisioningCallbackManagersSlotMap.get(ImsProvisioningController.this.getSlotId(this.mSubId))).clear();
            } catch (NullPointerException unused) {
                ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
                imsProvisioningController.logw("MmTelFeatureListener", imsProvisioningController.getSlotId(this.mSubId), "can not find callback manager to clear");
            }
        }

        private void setInitialProvisioningKeys(int i) {
            int provisioningStatus;
            int provisioningStatus2;
            int provisioningStatus3;
            try {
                boolean zIsImsProvisioningRequiredForCapability = ImsProvisioningController.this.isImsProvisioningRequiredForCapability(i, 1, 0);
                ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "setInitialProvisioningKeys provisioning required(voice, lte) " + zIsImsProvisioningRequiredForCapability);
                if (zIsImsProvisioningRequiredForCapability && (provisioningStatus3 = ImsProvisioningController.this.mImsProvisioningLoader.getProvisioningStatus(i, 1, 1, 0)) != -1) {
                    setProvisioningValue(10, provisioningStatus3 == 1 ? 1 : 0);
                }
                try {
                    boolean zIsImsProvisioningRequiredForCapability2 = ImsProvisioningController.this.isImsProvisioningRequiredForCapability(i, 2, 0);
                    ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "setInitialProvisioningKeys provisioning required(video, lte) " + zIsImsProvisioningRequiredForCapability2);
                    if (zIsImsProvisioningRequiredForCapability2 && (provisioningStatus2 = ImsProvisioningController.this.mImsProvisioningLoader.getProvisioningStatus(i, 1, 2, 0)) != -1) {
                        setProvisioningValue(11, provisioningStatus2 == 1 ? 1 : 0);
                    }
                    try {
                        boolean zIsImsProvisioningRequiredForCapability3 = ImsProvisioningController.this.isImsProvisioningRequiredForCapability(i, 1, 1);
                        ImsProvisioningController.this.log("MmTelFeatureListener", this.mSlotId, "setInitialProvisioningKeys provisioning required(voice, iwlan) " + zIsImsProvisioningRequiredForCapability3);
                        if (!zIsImsProvisioningRequiredForCapability3 || (provisioningStatus = ImsProvisioningController.this.mImsProvisioningLoader.getProvisioningStatus(i, 1, 1, 1)) == -1) {
                            return;
                        }
                        setProvisioningValue(28, provisioningStatus == 1 ? 1 : 0);
                    } catch (IllegalArgumentException e) {
                        ImsProvisioningController.this.logw("setInitialProvisioningKeys: KEY_VOICE_OVER_WIFI_ENABLED_OVERRIDE failed for subId=" + i + ", exception: " + e.getMessage());
                    }
                } catch (IllegalArgumentException e2) {
                    ImsProvisioningController.this.logw("setInitialProvisioningKeys: KEY_VT_PROVISIONING_STATUS failed for subId=" + i + ", exception: " + e2.getMessage());
                }
            } catch (IllegalArgumentException e3) {
                ImsProvisioningController.this.logw("setInitialProvisioningKeys: KEY_VOLTE_PROVISIONING_STATUS failed for subId=" + i + ", exception: " + e3.getMessage());
            }
        }
    }

    final class RcsFeatureListener implements FeatureConnector.Listener {
        private ConfigCallback mConfigCallback;
        private FeatureConnector mConnector;
        private RcsFeatureManager mRcsFeatureManager;
        private boolean mReady = false;
        private boolean mRequiredNotify = false;
        private int mSlotId;
        private int mSubId;

        RcsFeatureListener(int i) {
            ImsProvisioningController.this.log("RcsFeatureListener", i, "created");
            this.mSlotId = i;
            this.mSubId = ImsProvisioningController.this.getSubId(i);
            this.mConfigCallback = ImsProvisioningController.this.new ConfigCallback(this.mSubId);
            FeatureConnector featureConnectorCreate = ImsProvisioningController.this.mRcsFeatureConnector.create(ImsProvisioningController.this.mApp, i, this, new HandlerExecutor(ImsProvisioningController.this.mHandler), "ImsProvisioningController");
            this.mConnector = featureConnectorCreate;
            featureConnectorCreate.connect();
        }

        public void setSubId(int i) {
            if (this.mRequiredNotify && this.mReady) {
                this.mRequiredNotify = false;
                setInitialProvisioningKeys(i);
            }
            if (this.mSubId == i) {
                ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "subId is not changed");
            } else {
                this.mSubId = i;
                this.mConfigCallback.setSubId(i);
            }
        }

        public void destroy() {
            ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "destroy");
            RcsFeatureManager rcsFeatureManager = this.mRcsFeatureManager;
            if (rcsFeatureManager != null) {
                try {
                    ImsConfig imsConfig = ImsProvisioningController.this.getImsConfig(rcsFeatureManager.getConfig());
                    if (imsConfig != null) {
                        imsConfig.removeConfigCallback(this.mConfigCallback);
                    }
                } catch (ImsException e) {
                    ImsProvisioningController.this.logw("RcsFeatureListener", this.mSlotId, "destroy :" + e.getMessage());
                }
            }
            this.mConfigCallback = null;
            this.mConnector.disconnect();
            this.mConnector = null;
            this.mReady = false;
            this.mRcsFeatureManager = null;
        }

        public void connectionReady(RcsFeatureManager rcsFeatureManager, int i) {
            ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "connection ready");
            this.mReady = true;
            this.mRcsFeatureManager = rcsFeatureManager;
            if (rcsFeatureManager != null) {
                try {
                    ImsConfig imsConfig = ImsProvisioningController.this.getImsConfig(rcsFeatureManager.getConfig());
                    if (imsConfig != null) {
                        imsConfig.addConfigCallback(this.mConfigCallback);
                    }
                } catch (ImsException e) {
                    ImsProvisioningController.this.logw("RcsFeatureListener", this.mSlotId, "addConfigCallback :" + e.getMessage());
                }
            }
            onRcsAvailable();
        }

        public void connectionUnavailable(int i) {
            ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "connection unavailable");
            this.mReady = false;
            this.mRcsFeatureManager = null;
            if (i == 2) {
                onRcsUnavailable();
            }
        }

        public int setProvisioningValue(int i, int i2) {
            boolean z = this.mReady;
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
            int config = 1;
            if (!z) {
                imsProvisioningController.loge("RcsFeatureListener", this.mSlotId, "service is Unavailable");
                return 1;
            }
            try {
                config = imsProvisioningController.getImsConfig(this.mRcsFeatureManager.getConfig()).setConfig(i, i2);
                ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "setConfig called with key " + i + " value " + i2);
                return config;
            } catch (ImsException e) {
                ImsProvisioningController.this.logw("RcsFeatureListener", this.mSlotId, "setConfig operation failed for key =" + i + ", value =" + i2 + ". Exception:" + e.getMessage());
                return config;
            }
        }

        public int getProvisioningValue(int i) {
            boolean z = this.mReady;
            ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
            if (!z) {
                imsProvisioningController.loge("RcsFeatureListener", this.mSlotId, "service is Unavailable");
                return -1;
            }
            try {
                return imsProvisioningController.getImsConfig(this.mRcsFeatureManager.getConfig()).getConfigInt(i);
            } catch (ImsException e) {
                ImsProvisioningController.this.logw("RcsFeatureListener", this.mSlotId, "getConfig operation failed for key =" + i + ", value =-1. Exception:" + e.getMessage());
                return -1;
            }
        }

        public boolean isConnectionReady() {
            return this.mReady;
        }

        public void onRcsAvailable() {
            ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "onRcsAvailable");
            if (ImsProvisioningController.this.isValidSubId(this.mSubId)) {
                this.mRequiredNotify = false;
                setInitialProvisioningKeys(this.mSubId);
                ProvisioningCallbackManager provisioningCallbackManager = (ProvisioningCallbackManager) ImsProvisioningController.this.mProvisioningCallbackManagersSlotMap.get(this.mSlotId);
                if (provisioningCallbackManager == null || !provisioningCallbackManager.hasCallblacks()) {
                    return;
                }
                ImsProvisioningController.this.notifyRcsProvisioningStatus(this.mSlotId, this.mSubId, null);
                return;
            }
            this.mRequiredNotify = true;
        }

        public void onRcsUnavailable() {
            ImsProvisioningController.this.log("RcsFeatureListener", this.mSlotId, "onRcsUnavailable");
            try {
                ((ProvisioningCallbackManager) ImsProvisioningController.this.mProvisioningCallbackManagersSlotMap.get(ImsProvisioningController.this.getSlotId(this.mSubId))).clear();
            } catch (NullPointerException unused) {
                ImsProvisioningController imsProvisioningController = ImsProvisioningController.this;
                imsProvisioningController.logw("RcsFeatureListener", imsProvisioningController.getSlotId(this.mSubId), "can not find callback manager to clear");
            }
        }

        private void setInitialProvisioningKeys(int i) {
            int provisioningStatus;
            try {
                if (!ImsProvisioningController.this.isRcsProvisioningRequiredForCapability(i, 2, 0) || (provisioningStatus = ImsProvisioningController.this.mImsProvisioningLoader.getProvisioningStatus(i, 2, 2, 0)) == -1) {
                    return;
                }
                setProvisioningValue(25, provisioningStatus == 1 ? 1 : 0);
            } catch (IllegalArgumentException e) {
                ImsProvisioningController.this.logw("setInitialProvisioningKeys: KEY_EAB_PROVISIONING_STATUS failed for subId=" + i + ", exception: " + e.getMessage());
            }
        }
    }

    final class ConfigCallback extends IImsConfigCallback.Stub {
        private int mSubId;

        public void onStringConfigChanged(int i, String str) {
        }

        ConfigCallback(int i) {
            this.mSubId = i;
        }

        public void setSubId(int i) {
            this.mSubId = i;
        }

        /* JADX INFO: renamed from: $r8$lambda$yZ-P3JDRIPLDrlDpaArypD7kMIo, reason: not valid java name */
        public static /* synthetic */ boolean m979$r8$lambda$yZP3JDRIPLDrlDpaArypD7kMIo(int i, int i2) {
            return i2 == i;
        }

        public void onIntConfigChanged(final int i, int i2) {
            if (Arrays.stream(ImsProvisioningController.LOCAL_IMS_CONFIG_KEYS).anyMatch(new IntPredicate() { // from class: com.android.phone.ImsProvisioningController$ConfigCallback$$ExternalSyntheticLambda0
                @Override // java.util.function.IntPredicate
                public final boolean test(int i3) {
                    return ImsProvisioningController.ConfigCallback.m979$r8$lambda$yZP3JDRIPLDrlDpaArypD7kMIo(i, i3);
                }
            })) {
                long jClearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (ImsProvisioningController.this.mHandler != null) {
                        ImsProvisioningController.this.mHandler.sendMessage(ImsProvisioningController.this.mHandler.obtainMessage(4, this.mSubId, i, Integer.valueOf(i2)));
                    }
                } finally {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                }
            }
        }
    }

    public ImsProvisioningController(PhoneGlobals phoneGlobals, int i, Looper looper, MmTelFeatureConnector mmTelFeatureConnector, RcsFeatureConnector rcsFeatureConnector, ImsProvisioningLoader imsProvisioningLoader, FeatureFlags featureFlags) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.ImsProvisioningController.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                if (ImsProvisioningController.this.mHandler.hasMessages(1)) {
                    return;
                }
                ImsProvisioningController.this.mHandler.sendEmptyMessage(1);
            }
        };
        this.mSubChangedListener = onSubscriptionsChangedListener;
        log("ImsProvisioningController");
        this.mApp = phoneGlobals;
        this.mNumSlot = i;
        MessageHandler messageHandler = new MessageHandler(looper);
        this.mHandler = messageHandler;
        this.mMmTelFeatureConnector = mmTelFeatureConnector;
        this.mRcsFeatureConnector = rcsFeatureConnector;
        this.mCarrierConfigManager = (CarrierConfigManager) phoneGlobals.getSystemService(CarrierConfigManager.class);
        this.mSubscriptionManager = (SubscriptionManager) phoneGlobals.getSystemService(SubscriptionManager.class);
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) phoneGlobals.getSystemService(TelephonyRegistryManager.class);
        this.mTelephonyRegistryManager = telephonyRegistryManager;
        Objects.requireNonNull(messageHandler);
        telephonyRegistryManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener, new ConcurrencyHelpers$$ExternalSyntheticLambda0(messageHandler));
        this.mImsProvisioningLoader = imsProvisioningLoader;
        this.mFeatureFlags = featureFlags;
        PhoneConfigurationManager.registerForMultiSimConfigChange(messageHandler, 3, (Object) null);
        initialize(i);
    }

    private void initialize(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            this.mMmTelFeatureListenersSlotMap.put(i2, new MmTelFeatureListener(i2));
            this.mRcsFeatureListenersSlotMap.put(i2, new RcsFeatureListener(i2));
            this.mProvisioningCallbackManagersSlotMap.put(i2, new ProvisioningCallbackManager(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged(int i) {
        log("onMultiSimConfigChanged: NumSlot " + this.mNumSlot + " newNumSlot " + i);
        int i2 = this.mNumSlot;
        if (i2 >= i) {
            if (i2 > i) {
                while (true) {
                    i2--;
                    if (i2 <= i - 1) {
                        break;
                    }
                    MmTelFeatureListener mmTelFeatureListener = (MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(i2);
                    this.mMmTelFeatureListenersSlotMap.remove(i2);
                    mmTelFeatureListener.destroy();
                    RcsFeatureListener rcsFeatureListener = (RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(i2);
                    this.mRcsFeatureListenersSlotMap.remove(i2);
                    rcsFeatureListener.destroy();
                    ProvisioningCallbackManager provisioningCallbackManager = (ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(i2);
                    this.mProvisioningCallbackManagersSlotMap.remove(i2);
                    provisioningCallbackManager.clear();
                }
            }
        } else {
            while (i2 < i) {
                this.mMmTelFeatureListenersSlotMap.put(i2, new MmTelFeatureListener(i2));
                this.mRcsFeatureListenersSlotMap.put(i2, new RcsFeatureListener(i2));
                this.mProvisioningCallbackManagersSlotMap.put(i2, new ProvisioningCallbackManager(i2));
                i2++;
            }
        }
        this.mNumSlot = i;
    }

    public void destroy() {
        SparseArray sparseArray;
        SparseArray sparseArray2;
        log("destroy");
        this.mHandler.getLooper().quit();
        this.mTelephonyRegistryManager.removeOnSubscriptionsChangedListener(this.mSubChangedListener);
        int i = 0;
        while (true) {
            int size = this.mMmTelFeatureListenersSlotMap.size();
            sparseArray = this.mMmTelFeatureListenersSlotMap;
            if (i >= size) {
                break;
            }
            ((MmTelFeatureListener) sparseArray.get(i)).destroy();
            i++;
        }
        sparseArray.clear();
        int i2 = 0;
        while (true) {
            int size2 = this.mRcsFeatureListenersSlotMap.size();
            sparseArray2 = this.mRcsFeatureListenersSlotMap;
            if (i2 >= size2) {
                break;
            }
            ((RcsFeatureListener) sparseArray2.get(i2)).destroy();
            i2++;
        }
        sparseArray2.clear();
        for (int i3 = 0; i3 < this.mProvisioningCallbackManagersSlotMap.size(); i3++) {
            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(i3)).clear();
        }
    }

    public static ImsProvisioningController make(PhoneGlobals phoneGlobals, int i, FeatureFlags featureFlags) {
        synchronized (ImsProvisioningController.class) {
            try {
                if (sInstance == null) {
                    Rlog.i("ImsProvisioningController", "ImsProvisioningController created");
                    HandlerThread handlerThread = new HandlerThread("ImsProvisioningController");
                    handlerThread.start();
                    sInstance = new ImsProvisioningController(phoneGlobals, i, handlerThread.getLooper(), new MmTelFeatureConnector() { // from class: com.android.phone.ImsProvisioningController$$ExternalSyntheticLambda3
                        @Override // com.android.phone.ImsProvisioningController.MmTelFeatureConnector
                        public final FeatureConnector create(Context context, int i2, String str, FeatureConnector.Listener listener, Executor executor) {
                            return ImsManager.getConnector(context, i2, str, listener, executor);
                        }
                    }, new RcsFeatureConnector() { // from class: com.android.phone.ImsProvisioningController$$ExternalSyntheticLambda4
                        @Override // com.android.phone.ImsProvisioningController.RcsFeatureConnector
                        public final FeatureConnector create(Context context, int i2, FeatureConnector.Listener listener, Executor executor, String str) {
                            return RcsFeatureManager.getConnector(context, i2, listener, executor, str);
                        }
                    }, new ImsProvisioningLoader(phoneGlobals), featureFlags);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public static ImsProvisioningController getInstance() {
        ImsProvisioningController imsProvisioningController;
        synchronized (ImsProvisioningController.class) {
            imsProvisioningController = sInstance;
        }
        return imsProvisioningController;
    }

    public void addFeatureProvisioningChangedCallback(int i, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        if (iFeatureProvisioningCallback == null) {
            Folme$$ExternalSyntheticBUOutline0.m("provisioning callback can't be null");
            return;
        }
        int slotId = getSlotId(i);
        if (slotId < 0 || slotId >= this.mNumSlot) {
            Folme$$ExternalSyntheticBUOutline0.m("subscription id is not available");
            return;
        }
        try {
            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(slotId)).registerCallback(iFeatureProvisioningCallback);
            log("Feature Provisioning Callback registered.");
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(5, getSlotId(i), i, iFeatureProvisioningCallback));
        } catch (NullPointerException unused) {
            logw("can not access callback manager to add callback");
        }
    }

    public void removeFeatureProvisioningChangedCallback(int i, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        if (iFeatureProvisioningCallback == null) {
            Folme$$ExternalSyntheticBUOutline0.m("provisioning callback can't be null");
            return;
        }
        int slotId = getSlotId(i);
        if (slotId < 0 || slotId >= this.mNumSlot) {
            Folme$$ExternalSyntheticBUOutline0.m("subscription id is not available");
            return;
        }
        try {
            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(slotId)).unregisterCallback(iFeatureProvisioningCallback);
            log("Feature Provisioning Callback removed.");
        } catch (NullPointerException unused) {
            logw("can not access callback manager to remove callback");
        }
    }

    public boolean isImsProvisioningRequiredForCapability(int i, int i2, int i3) {
        String str;
        int slotId = getSlotId(i);
        if (slotId <= -1 || slotId >= this.mNumSlot) {
            loge("Fail to retrieve slotId from subId");
            Folme$$ExternalSyntheticBUOutline0.m("subscribe id is invalid");
            return false;
        }
        if (i2 <= 0 || i2 >= 33) {
            S2Support$$ExternalSyntheticBUOutline0.m("MmTel capability '", i2, "' is invalid");
            return false;
        }
        if (-1 >= i3 || i3 >= 5) {
            log("Ims not matched radio tech " + i3);
            S2Support$$ExternalSyntheticBUOutline0.m("Registration technology '", i3, "' is invalid");
            return false;
        }
        boolean zIsProvisioningRequired = isProvisioningRequired(i, i2, i3, true);
        if (!zIsProvisioningRequired && (i2 == 1 || i2 == 2 || i2 == 4)) {
            if (i2 != 4) {
                str = "carrier_volte_provisioning_required_bool";
            } else {
                str = "carrier_ut_provisioning_required_bool";
            }
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
            zIsProvisioningRequired = configForSubId != null ? configForSubId.getBoolean(str) : CarrierConfigManager.getDefaultConfig().getBoolean(str);
        }
        log("isImsProvisioningRequiredForCapability capability " + i2 + " tech " + i3 + " return value " + zIsProvisioningRequired);
        return zIsProvisioningRequired;
    }

    public boolean isRcsProvisioningRequiredForCapability(int i, int i2, int i3) {
        int slotId = getSlotId(i);
        if (slotId <= -1 || slotId >= this.mNumSlot) {
            loge("Fail to retrieve slotId from subId");
            Folme$$ExternalSyntheticBUOutline0.m("subscribe id is invalid");
            return false;
        }
        if (i2 <= 0 || i2 >= 3) {
            S2Support$$ExternalSyntheticBUOutline0.m("Rcs capability '", i2, "' is invalid");
            return false;
        }
        if (-1 >= i3 || i3 >= 5) {
            log("Rcs not matched radio tech " + i3);
            S2Support$$ExternalSyntheticBUOutline0.m("Registration technology '", i3, "' is invalid");
            return false;
        }
        boolean zIsProvisioningRequired = isProvisioningRequired(i, i2, i3, false);
        if (!zIsProvisioningRequired) {
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
            if (configForSubId == null) {
                zIsProvisioningRequired = CarrierConfigManager.getDefaultConfig().getBoolean("carrier_rcs_provisioning_required_bool");
            } else {
                zIsProvisioningRequired = configForSubId.getBoolean("carrier_rcs_provisioning_required_bool");
            }
        }
        log("isRcsProvisioningRequiredForCapability capability " + i2 + " tech " + i3 + " return value " + zIsProvisioningRequired);
        return zIsProvisioningRequired;
    }

    public boolean getImsProvisioningStatusForCapability(String str, int i, int i2, int i3) {
        ImsProvisioningController imsProvisioningController;
        String str2;
        int i4;
        int i5;
        int i6;
        boolean boolValue;
        if (!isImsProvisioningRequiredForCapability(i, i2, i3)) {
            logAttr(str, "getImsProvisioningStatusForCapability", i, " not required, capability " + i2 + " tech " + i3);
            return true;
        }
        int provisioningStatus = this.mImsProvisioningLoader.getProvisioningStatus(i, 1, i2, i3);
        if (provisioningStatus == -1) {
            int valueFromImsService = getValueFromImsService(i, i2, i3);
            boolValue = getBoolValue(valueFromImsService);
            imsProvisioningController = this;
            str2 = str;
            i4 = i;
            i5 = i2;
            i6 = i3;
            if (valueFromImsService != -1) {
                imsProvisioningController.setAndNotifyMmTelProvisioningValue(str2, i4, i5, i6, boolValue);
            }
        } else {
            imsProvisioningController = this;
            str2 = str;
            i4 = i;
            i5 = i2;
            i6 = i3;
            boolValue = imsProvisioningController.getBoolValue(provisioningStatus);
        }
        imsProvisioningController.logAttr(str2, "getImsProvisioningStatusForCapability", i4, " capability " + i5 + " tech " + i6 + " result " + boolValue);
        return boolValue;
    }

    public void setImsProvisioningStatusForCapability(String str, int i, int i2, int i3, boolean z) {
        if (!isImsProvisioningRequiredForCapability(i, i2, i3)) {
            logAttr(str, "setImsProvisioningStatusForCapability", i, "not required, capability " + i2 + " tech " + i3);
            return;
        }
        if (!setAndNotifyMmTelProvisioningValue(str, i, i2, i3, z)) {
            logAttr(str, "setImsProvisioningStatusForCapability", i, "status not changed, capability " + i2 + " tech " + i3);
            return;
        }
        int slotId = getSlotId(i);
        int intValue = getIntValue(z);
        int keyFromCapability = getKeyFromCapability(i2, i3);
        if (keyFromCapability != -1) {
            logAttr(str, "setImsProvisioningStatusForCapability", i, "matched key " + keyFromCapability);
            try {
                ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(slotId)).setProvisioningValue(keyFromCapability, intValue);
            } catch (NullPointerException unused) {
                loge("can not access MmTelFeatureListener with capability " + i2);
            }
        }
    }

    public boolean getRcsProvisioningStatusForCapability(int i, int i2, int i3) {
        boolean boolValue;
        if (!isRcsProvisioningRequiredForCapability(i, i2, i3)) {
            log("getRcsProvisioningStatusForCapability : not required capability " + i2 + " tech " + i3);
            return true;
        }
        int provisioningStatus = this.mImsProvisioningLoader.getProvisioningStatus(i, 2, i2, i3);
        if (provisioningStatus == -1) {
            int rcsValueFromImsService = getRcsValueFromImsService(i, i2);
            boolValue = getBoolValue(rcsValueFromImsService);
            if (rcsValueFromImsService != -1) {
                setAndNotifyRcsProvisioningValueForAllTech(i, i2, boolValue);
            }
        } else {
            boolValue = getBoolValue(provisioningStatus);
        }
        log("getRcsProvisioningStatusForCapability :  capability " + i2 + " tech " + i3 + " result " + boolValue);
        return boolValue;
    }

    public void setRcsProvisioningStatusForCapability(int i, int i2, int i3, boolean z) {
        if (!isRcsProvisioningRequiredForCapability(i, i2, i3)) {
            log("set rcs provisioning status but not required");
            return;
        }
        if (!setAndNotifyRcsProvisioningValue(i, i2, i3, z)) {
            log("status not changed rcs capability " + i2 + " tech " + i3);
            return;
        }
        int slotId = getSlotId(i);
        int intValue = getIntValue(z);
        try {
            if (((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).isConnectionReady()) {
                ((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).setProvisioningValue(25, intValue);
            }
            ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(slotId)).setProvisioningValue(25, intValue);
        } catch (NullPointerException unused) {
            loge("can not access RcsFeatureListener with capability " + i2);
        }
    }

    public int setProvisioningValue(String str, int i, final int i2, int i3) {
        logAttr(str, "setProvisioningValue", i, i2 + ": " + i3);
        if (!Arrays.stream(LOCAL_IMS_CONFIG_KEYS).anyMatch(new IntPredicate() { // from class: com.android.phone.ImsProvisioningController$$ExternalSyntheticLambda1
            @Override // java.util.function.IntPredicate
            public final boolean test(int i4) {
                return ImsProvisioningController.$r8$lambda$dJDLGcth2yx5qONnZ2vD8joT8C4(i2, i4);
            }
        })) {
            logAttr(str, "setProvisioningValue", i, "not matched key " + i2);
            return -1;
        }
        int slotId = getSlotId(i);
        if (slotId <= -1 || slotId >= this.mNumSlot) {
            logAttrE(str, "setProvisioningValue", i, "Fail to retrieve slotId from subId");
            return 1;
        }
        try {
            int provisioningValue = ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(slotId)).setProvisioningValue(i2, i3);
            if (i2 == 25 && ((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).isConnectionReady()) {
                provisioningValue = ((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).setProvisioningValue(i2, i3);
            }
            updateCapabilityTechFromKey(str, i, i2, i3);
            return provisioningValue;
        } catch (NullPointerException unused) {
            logAttrE(str, "setProvisioningValue", i, "can not access FeatureListener to set provisioning value");
            return 1;
        }
    }

    public static /* synthetic */ boolean $r8$lambda$dJDLGcth2yx5qONnZ2vD8joT8C4(int i, int i2) {
        return i2 == i;
    }

    public static /* synthetic */ boolean $r8$lambda$sf923WRDhuV5GjdbVIisoys8hig(int i, int i2) {
        return i2 == i;
    }

    public int getProvisioningValue(String str, int i, final int i2) {
        int provisioningStatus;
        if (!Arrays.stream(LOCAL_IMS_CONFIG_KEYS).anyMatch(new IntPredicate() { // from class: com.android.phone.ImsProvisioningController$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i3) {
                return ImsProvisioningController.$r8$lambda$sf923WRDhuV5GjdbVIisoys8hig(i2, i3);
            }
        })) {
            logAttr(str, "getProvisioningValue", i, "not matched key " + i2);
            return -1;
        }
        int slotId = getSlotId(i);
        if (slotId <= -1 || slotId >= this.mNumSlot) {
            logAttrE(str, "getProvisioningValue", i, "Fail to retrieve slotId from subId");
            return -1;
        }
        int capabilityFromKey = getCapabilityFromKey(i2);
        int techFromKey = getTechFromKey(i2);
        if (capabilityFromKey != -1 && techFromKey != -1) {
            ImsProvisioningLoader imsProvisioningLoader = this.mImsProvisioningLoader;
            if (i2 == 25) {
                provisioningStatus = imsProvisioningLoader.getProvisioningStatus(i, 2, capabilityFromKey, techFromKey);
            } else {
                provisioningStatus = imsProvisioningLoader.getProvisioningStatus(i, 1, capabilityFromKey, techFromKey);
            }
            if (provisioningStatus != -1) {
                logAttr(str, "getProvisioningValue", i, "cache hit : key=" + i2 + ": value=" + provisioningStatus);
                return provisioningStatus;
            }
        }
        if (i2 == 25) {
            int rcsValueFromImsService = getRcsValueFromImsService(i, capabilityFromKey);
            if (rcsValueFromImsService == -1) {
                logAttrW(str, "getProvisioningValue", i, "fail to get data from ImsService, capability=" + capabilityFromKey);
                return rcsValueFromImsService;
            }
            logAttr(str, "getProvisioningValue", i, "cache miss, get from RCS - key=" + i2 + ": value=" + rcsValueFromImsService);
            setAndNotifyRcsProvisioningValueForAllTech(i, capabilityFromKey, getBoolValue(rcsValueFromImsService));
            return rcsValueFromImsService;
        }
        int valueFromImsService = getValueFromImsService(i, capabilityFromKey, techFromKey);
        if (valueFromImsService == -1) {
            logAttrW(str, "getProvisioningValue", i, "fail to get data from ImsService, capability=" + capabilityFromKey);
            return valueFromImsService;
        }
        logAttr(str, "getProvisioningValue", i, "cache miss, get from MMTEL - key=" + i2 + ": value=" + valueFromImsService);
        setAndNotifyMmTelProvisioningValue(str, i, capabilityFromKey, techFromKey, getBoolValue(valueFromImsService));
        return valueFromImsService;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    private boolean isProvisioningRequired(int i, int i2, final int i3, boolean z) {
        int[] techsFromCarrierConfig = getTechsFromCarrierConfig(i, i2, z);
        if (techsFromCarrierConfig == null) {
            logw("isProvisioningRequired : getTechsFromCarrierConfig failed");
            return false;
        }
        if (Arrays.stream(techsFromCarrierConfig).anyMatch(new IntPredicate() { // from class: com.android.phone.ImsProvisioningController$$ExternalSyntheticLambda2
            @Override // java.util.function.IntPredicate
            public final boolean test(int i4) {
                return ImsProvisioningController.m958$r8$lambda$Qy8AfUGQpyFd3fe4fRfy3WIEKM(i3, i4);
            }
        })) {
            return true;
        }
        log("isProvisioningRequired : not matched capability " + i2 + " tech " + i3);
        return false;
    }

    /* JADX INFO: renamed from: $r8$lambda$Qy8AfUGQpyFd3fe4f-Rfy3WIEKM, reason: not valid java name */
    public static /* synthetic */ boolean m958$r8$lambda$Qy8AfUGQpyFd3fe4fRfy3WIEKM(int i, int i2) {
        return i2 == i;
    }

    private int[] getTechsFromCarrierConfig(int i, int i2, boolean z) {
        String str;
        String str2;
        if (z) {
            str = (String) KEYS_MMTEL_CAPABILITY.get(Integer.valueOf(i2));
            str2 = "ims.mmtel_requires_provisioning_bundle";
        } else {
            str = (String) KEYS_RCS_CAPABILITY.get(Integer.valueOf(i2));
            str2 = "ims.rcs_requires_provisioning_bundle";
        }
        if (str == null) {
            return null;
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        if (configForSubId == null) {
            log("getTechsFromCarrierConfig : imsCarrierConfigs null");
            return null;
        }
        PersistableBundle persistableBundle = configForSubId.getPersistableBundle(str2);
        if (persistableBundle == null) {
            log("getTechsFromCarrierConfig : provisioningBundle null");
            return null;
        }
        return persistableBundle.getIntArray(str);
    }

    private int getValueFromImsService(int i, int i2, int i3) {
        if (i2 == 1) {
            return ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(getSlotId(i))).getProvisioningValue(i3 == 1 ? 28 : 10);
        }
        if (i2 == 2) {
            return ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(getSlotId(i))).getProvisioningValue(11);
        }
        log("Capability " + i2 + " has been provisioning");
        return -1;
    }

    private int getRcsValueFromImsService(int i, int i2) {
        int slotId = getSlotId(i);
        if (i2 != 2) {
            log("Capability " + i2 + " has been provisioning");
            return -1;
        }
        try {
            if (((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).isConnectionReady()) {
                return ((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(slotId)).getProvisioningValue(25);
            }
            log("Rcs ImsService is not available, EAB provisioning status should be read from MmTel ImsService");
            return ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(slotId)).getProvisioningValue(25);
        } catch (NullPointerException e) {
            logw("can not access FeatureListener : " + e.getMessage());
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubscriptionsChanged() {
        for (int i = 0; i < this.mMmTelFeatureListenersSlotMap.size(); i++) {
            ((MmTelFeatureListener) this.mMmTelFeatureListenersSlotMap.get(i)).setSubId(getSubId(i));
        }
        for (int i2 = 0; i2 < this.mRcsFeatureListenersSlotMap.size(); i2++) {
            ((RcsFeatureListener) this.mRcsFeatureListenersSlotMap.get(i2)).setSubId(getSubId(i2));
        }
        for (int i3 = 0; i3 < this.mProvisioningCallbackManagersSlotMap.size(); i3++) {
            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(i3)).setSubId(getSubId(i3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCapabilityTechFromKey(String str, int i, int i2, int i3) {
        ImsProvisioningController imsProvisioningController;
        int i4;
        boolean boolValue = getBoolValue(i3);
        int capabilityFromKey = getCapabilityFromKey(i2);
        int techFromKey = getTechFromKey(i2);
        if (capabilityFromKey == -1 || techFromKey == -1) {
            logAttrW(str, "updateCapabilityTechFromKey", i, "unknown key " + i2);
            return;
        }
        if (i2 == 10 || i2 == 11 || i2 == 28) {
            imsProvisioningController = this;
            i4 = i;
            imsProvisioningController.setAndNotifyMmTelProvisioningValue(str, i4, capabilityFromKey, techFromKey, boolValue);
        } else {
            imsProvisioningController = this;
            i4 = i;
        }
        if (i2 == 25) {
            imsProvisioningController.setAndNotifyRcsProvisioningValueForAllTech(i4, capabilityFromKey, boolValue);
        }
    }

    protected int getSubId(int i) {
        return SubscriptionManager.getSubscriptionId(i);
    }

    protected int getSlotId(int i) {
        return SubscriptionManager.getPhoneId(i);
    }

    protected ImsConfig getImsConfig(ImsManager imsManager) {
        return imsManager.getConfigInterface();
    }

    protected ImsConfig getImsConfig(IImsConfig iImsConfig) {
        return new ImsConfig(iImsConfig);
    }

    private void checkUncommonProvisioningValues(String str, int i, int i2, int i3, boolean z) {
        if (!z && i2 == 1 && i3 == 0) {
            logAttrW(str, "checkUncommonProvisioningValues", i, "VoLTE provisioning disabled");
        }
    }

    private boolean setAndNotifyMmTelProvisioningValue(String str, int i, int i2, int i3, boolean z) {
        boolean provisioningStatus = this.mImsProvisioningLoader.setProvisioningStatus(i, 1, i2, i3, z);
        if (provisioningStatus) {
            checkUncommonProvisioningValues(str, i, i2, i3, z);
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(2, getSlotId(i), 0, new FeatureProvisioningData(i2, i3, z, true)));
        }
        return provisioningStatus;
    }

    private boolean setAndNotifyRcsProvisioningValue(int i, int i2, int i3, boolean z) {
        boolean provisioningStatus = this.mImsProvisioningLoader.setProvisioningStatus(i, 2, i2, i3, z);
        if (provisioningStatus) {
            int slotId = getSlotId(i);
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(2, slotId, 0, new FeatureProvisioningData(i2, i3, z, false)));
        }
        return provisioningStatus;
    }

    private boolean setAndNotifyRcsProvisioningValueForAllTech(int i, int i2, boolean z) {
        boolean andNotifyRcsProvisioningValue = false;
        for (int i3 : LOCAL_RADIO_TECHS) {
            andNotifyRcsProvisioningValue |= setAndNotifyRcsProvisioningValue(i, i2, i3, z);
        }
        return andNotifyRcsProvisioningValue;
    }

    protected boolean isValidSubId(int i) {
        int slotId = getSlotId(i);
        return slotId > -1 && slotId < this.mNumSlot;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMmTelProvisioningStatus(int i, int i2, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        for (int i3 : LOCAL_MMTEL_CAPABILITY) {
            int[] techsFromCarrierConfig = getTechsFromCarrierConfig(i2, i3, true);
            if (techsFromCarrierConfig != null) {
                for (int i4 : techsFromCarrierConfig) {
                    int provisioningStatus = this.mImsProvisioningLoader.getProvisioningStatus(i2, 1, i3, i4);
                    if (provisioningStatus != -1) {
                        int i5 = provisioningStatus == 1 ? 1 : 0;
                        if (iFeatureProvisioningCallback == null) {
                            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(i)).notifyProvisioningCapabilityChanged(new FeatureProvisioningData(i3, i4, getBoolValue(i5), true));
                        } else {
                            try {
                                iFeatureProvisioningCallback.onFeatureProvisioningChanged(i3, i4, getBoolValue(i5));
                            } catch (RemoteException unused) {
                                logw("notifyMmTelProvisioningStatus callback is not available");
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyRcsProvisioningStatus(int i, int i2, IFeatureProvisioningCallback iFeatureProvisioningCallback) {
        for (int i3 : LOCAL_RCS_CAPABILITY) {
            int[] techsFromCarrierConfig = getTechsFromCarrierConfig(i2, i3, false);
            if (techsFromCarrierConfig != null) {
                for (int i4 : techsFromCarrierConfig) {
                    int provisioningStatus = this.mImsProvisioningLoader.getProvisioningStatus(i2, 2, i3, i4);
                    if (provisioningStatus != -1) {
                        int i5 = provisioningStatus != 1 ? 0 : 1;
                        if (iFeatureProvisioningCallback == null) {
                            ((ProvisioningCallbackManager) this.mProvisioningCallbackManagersSlotMap.get(i)).notifyProvisioningCapabilityChanged(new FeatureProvisioningData(i3, i4, getBoolValue(i5), false));
                        } else {
                            try {
                                iFeatureProvisioningCallback.onRcsFeatureProvisioningChanged(i3, i4, getBoolValue(i5));
                            } catch (RemoteException unused) {
                                logw("notifyRcsProvisioningStatus callback is not available");
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAttr(String str, String str2, int i, String str3) {
        Rlog.d("ImsProvisioningController", str2 + "[" + i + "]: " + str3 + ", attr = [" + str + "]");
    }

    private void logAttrW(String str, String str2, int i, String str3) {
        Rlog.w("ImsProvisioningController", str2 + "[" + i + "]: " + str3 + ", attr = [" + str + "]");
    }

    private void logAttrE(String str, String str2, int i, String str3) {
        Rlog.e("ImsProvisioningController", str2 + "[" + i + "]: " + str3 + ", attr = [" + str + "]");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("ImsProvisioningController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str, int i, String str2) {
        Rlog.d("ImsProvisioningController", str + "[" + i + "] " + str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String str, int i, String str2) {
        Rlog.i("ImsProvisioningController", str + "[" + i + "] " + str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        Rlog.w("ImsProvisioningController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str, int i, String str2) {
        Rlog.w("ImsProvisioningController", str + "[" + i + "] " + str2);
    }

    private void loge(String str) {
        Rlog.e("ImsProvisioningController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str, int i, String str2) {
        Rlog.e("ImsProvisioningController", str + "[" + i + "] " + str2);
    }
}

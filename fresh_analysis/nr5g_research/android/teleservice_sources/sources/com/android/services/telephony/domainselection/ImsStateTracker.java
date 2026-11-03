package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.BarringInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsStateCallback;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.feature.MmTelFeature;
import android.util.ArraySet;
import android.util.Log;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ImsStateTracker {
    protected static final long MMTEL_FEATURE_AVAILABLE_WAIT_TIME_MILLIS = 1000;
    private static final String TAG = "ImsStateTracker";
    private BarringInfo mBarringInfo;
    private final Context mContext;
    private final Handler mHandler;
    private Boolean mImsRegistered;
    private Boolean mImsRegisteredOverCrossSim;
    private RegistrationManager.RegistrationCallback mImsRegistrationCallback;
    private ImsStateCallback mImsStateCallback;
    private MmTelFeature.MmTelCapabilities mMmTelCapabilities;
    private ImsMmTelManager.CapabilityCallback mMmTelCapabilityCallback;
    private Boolean mMmTelFeatureAvailable;
    private ImsMmTelManager mMmTelManager;
    private ServiceState mServiceState;
    private final int mSlotId;
    private final LocalLog mEventLog = new LocalLog(30);
    private int mSubId = -1;
    private final Set mServiceStateListeners = new ArraySet(2);
    private final Set mBarringInfoListeners = new ArraySet(2);
    private final Set mImsStateListeners = new ArraySet(5);
    private int mImsAccessNetworkType = 0;
    private final Runnable mMmTelFeatureUnavailableRunnable = new Runnable() { // from class: com.android.services.telephony.domainselection.ImsStateTracker.1
        @Override // java.lang.Runnable
        public void run() {
            ImsStateTracker.this.setImsStateAsUnavailable();
            ImsStateTracker.this.notifyImsMmTelFeatureAvailableChanged();
        }
    };

    public interface BarringInfoListener {
        void onBarringInfoUpdated(BarringInfo barringInfo);
    }

    public interface ImsStateListener {
        void onImsMmTelCapabilitiesChanged();

        void onImsMmTelFeatureAvailableChanged();

        void onImsRegistrationStateChanged();
    }

    public interface ServiceStateListener {
        void onServiceStateUpdated(ServiceState serviceState);
    }

    private static int imsRegTechToAccessNetworkType(int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1 || i == 2) {
            return 5;
        }
        return i != 3 ? 0 : 6;
    }

    public ImsStateTracker(Context context, int i, Looper looper) {
        this.mContext = context;
        this.mSlotId = i;
        this.mHandler = new Handler(looper);
    }

    public void destroy() {
        stopListeningForImsState();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public int getSlotId() {
        return this.mSlotId;
    }

    public int getSubId() {
        return this.mSubId;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void start(int i) {
        int i2 = this.mSubId;
        if (i2 == i) {
            if (!SubscriptionManager.isValidSubscriptionId(i2)) {
                setImsStateAsUnavailable();
                return;
            } else if (this.mImsStateCallback != null) {
                logd("start: ignored for same subscription(" + this.mSubId + ")");
                return;
            }
        } else {
            logi("start: subscription changed from " + this.mSubId + " to " + i);
            this.mSubId = i;
        }
        stopListeningForImsState();
        startListeningForImsState();
    }

    public void updateServiceState(ServiceState serviceState) {
        this.mServiceState = serviceState;
        Iterator it = this.mServiceStateListeners.iterator();
        while (it.hasNext()) {
            ((ServiceStateListener) it.next()).onServiceStateUpdated(serviceState);
        }
    }

    public void addServiceStateListener(final ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.add(serviceStateListener);
        final ServiceState serviceState = this.mServiceState;
        if (serviceState != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.domainselection.ImsStateTracker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$addServiceStateListener$0(serviceStateListener, serviceState);
                }
            });
        }
    }

    public void removeServiceStateListener(ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.remove(serviceStateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyServiceStateUpdated, reason: merged with bridge method [inline-methods] */
    public void lambda$addServiceStateListener$0(ServiceStateListener serviceStateListener, ServiceState serviceState) {
        if (this.mServiceStateListeners.contains(serviceStateListener)) {
            serviceStateListener.onServiceStateUpdated(serviceState);
        }
    }

    public void updateBarringInfo(BarringInfo barringInfo) {
        this.mBarringInfo = barringInfo;
        Iterator it = this.mBarringInfoListeners.iterator();
        while (it.hasNext()) {
            ((BarringInfoListener) it.next()).onBarringInfoUpdated(barringInfo);
        }
    }

    public void addBarringInfoListener(final BarringInfoListener barringInfoListener) {
        this.mBarringInfoListeners.add(barringInfoListener);
        final BarringInfo barringInfo = this.mBarringInfo;
        if (barringInfo != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.domainselection.ImsStateTracker$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$addBarringInfoListener$1(barringInfoListener, barringInfo);
                }
            });
        }
    }

    public void removeBarringInfoListener(BarringInfoListener barringInfoListener) {
        this.mBarringInfoListeners.remove(barringInfoListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyBarringInfoUpdated, reason: merged with bridge method [inline-methods] */
    public void lambda$addBarringInfoListener$1(BarringInfoListener barringInfoListener, BarringInfo barringInfo) {
        if (this.mBarringInfoListeners.contains(barringInfoListener)) {
            barringInfoListener.onBarringInfoUpdated(barringInfo);
        }
    }

    public void addImsStateListener(final ImsStateListener imsStateListener) {
        this.mImsStateListeners.add(imsStateListener);
        this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.domainselection.ImsStateTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addImsStateListener$2(imsStateListener);
            }
        });
    }

    public void removeImsStateListener(ImsStateListener imsStateListener) {
        this.mImsStateListeners.remove(imsStateListener);
    }

    public boolean isImsStateReady() {
        return (this.mMmTelFeatureAvailable == null || this.mImsRegistered == null || this.mMmTelCapabilities == null) ? false : true;
    }

    public boolean isMmTelFeatureAvailable() {
        Boolean bool = this.mMmTelFeatureAvailable;
        return bool != null && bool.booleanValue();
    }

    public boolean isImsRegistered() {
        Boolean bool = this.mImsRegistered;
        return bool != null && bool.booleanValue();
    }

    public boolean isImsRegisteredOverWlan() {
        return this.mImsAccessNetworkType == 5;
    }

    public boolean isImsRegisteredOverCrossSim() {
        Boolean bool = this.mImsRegisteredOverCrossSim;
        return bool != null && bool.booleanValue();
    }

    public boolean isImsVoiceCapable() {
        MmTelFeature.MmTelCapabilities mmTelCapabilities = this.mMmTelCapabilities;
        return mmTelCapabilities != null && mmTelCapabilities.isCapable(1);
    }

    public boolean isImsVideoCapable() {
        MmTelFeature.MmTelCapabilities mmTelCapabilities = this.mMmTelCapabilities;
        return mmTelCapabilities != null && mmTelCapabilities.isCapable(2);
    }

    public boolean isImsSmsCapable() {
        MmTelFeature.MmTelCapabilities mmTelCapabilities = this.mMmTelCapabilities;
        return mmTelCapabilities != null && mmTelCapabilities.isCapable(8);
    }

    public boolean isImsUtCapable() {
        MmTelFeature.MmTelCapabilities mmTelCapabilities = this.mMmTelCapabilities;
        return mmTelCapabilities != null && mmTelCapabilities.isCapable(4);
    }

    public int getImsAccessNetworkType() {
        return this.mImsAccessNetworkType;
    }

    private void initImsState() {
        this.mMmTelFeatureAvailable = null;
        this.mImsRegistered = null;
        this.mImsAccessNetworkType = 0;
        this.mImsRegisteredOverCrossSim = null;
        this.mMmTelCapabilities = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImsStateAsUnavailable() {
        logd("setImsStateAsUnavailable");
        setMmTelFeatureAvailable(false);
        setImsRegistered(false);
        setImsAccessNetworkType(0);
        setImsRegisteredOverCrossSim(false);
        setMmTelCapabilities(new MmTelFeature.MmTelCapabilities());
    }

    private void setMmTelFeatureAvailable(boolean z) {
        if (Objects.equals(this.mMmTelFeatureAvailable, Boolean.valueOf(z))) {
            return;
        }
        logi("setMmTelFeatureAvailable: " + this.mMmTelFeatureAvailable + " >> " + z);
        this.mMmTelFeatureAvailable = Boolean.valueOf(z);
    }

    private void setImsRegistered(boolean z) {
        if (Objects.equals(this.mImsRegistered, Boolean.valueOf(z))) {
            return;
        }
        logi("setImsRegistered: " + this.mImsRegistered + " >> " + z);
        this.mImsRegistered = Boolean.valueOf(z);
    }

    private void setImsAccessNetworkType(int i) {
        if (this.mImsAccessNetworkType != i) {
            logi("setImsAccessNetworkType: " + accessNetworkTypeToString(this.mImsAccessNetworkType) + " >> " + accessNetworkTypeToString(i));
            this.mImsAccessNetworkType = i;
        }
    }

    private void setMmTelCapabilities(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        if (Objects.equals(this.mMmTelCapabilities, mmTelCapabilities)) {
            return;
        }
        logi("MMTEL capabilities: " + this.mMmTelCapabilities + " >> " + mmTelCapabilities);
        this.mMmTelCapabilities = mmTelCapabilities;
    }

    private void setImsRegisteredOverCrossSim(boolean z) {
        if (Objects.equals(this.mImsRegisteredOverCrossSim, Boolean.valueOf(z))) {
            return;
        }
        logi("setImsRegisteredOverCrossSim: " + this.mImsRegisteredOverCrossSim + " >> " + z);
        this.mImsRegisteredOverCrossSim = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyImsStateChangeIfValid, reason: merged with bridge method [inline-methods] */
    public void lambda$addImsStateListener$2(ImsStateListener imsStateListener) {
        if (this.mImsStateListeners.contains(imsStateListener)) {
            if (this.mMmTelFeatureAvailable != null) {
                imsStateListener.onImsMmTelFeatureAvailableChanged();
            }
            if (this.mImsRegistered != null) {
                imsStateListener.onImsRegistrationStateChanged();
            }
            if (this.mMmTelCapabilities != null) {
                imsStateListener.onImsMmTelCapabilitiesChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyImsMmTelFeatureAvailableChanged() {
        Iterator it = this.mImsStateListeners.iterator();
        while (it.hasNext()) {
            ((ImsStateListener) it.next()).onImsMmTelFeatureAvailableChanged();
        }
    }

    private void notifyImsRegistrationStateChanged() {
        logi("ImsState: " + imsStateToString());
        Iterator it = this.mImsStateListeners.iterator();
        while (it.hasNext()) {
            ((ImsStateListener) it.next()).onImsRegistrationStateChanged();
        }
    }

    private void notifyImsMmTelCapabilitiesChanged() {
        logi("ImsState: " + imsStateToString());
        Iterator it = this.mImsStateListeners.iterator();
        while (it.hasNext()) {
            ((ImsStateListener) it.next()).onImsMmTelCapabilitiesChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMmTelFeatureAvailable() {
        logd("onMmTelFeatureAvailable");
        this.mHandler.removeCallbacks(this.mMmTelFeatureUnavailableRunnable);
        setMmTelFeatureAvailable(true);
        registerImsRegistrationCallback();
        registerMmTelCapabilityCallback();
        notifyImsMmTelFeatureAvailableChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMmTelFeatureUnavailable(int i) {
        logd("onMmTelFeatureUnavailable: reason=" + disconnectedCauseToString(i));
        if (i == 1 || i == 6) {
            initImsState();
            setMmTelFeatureAvailable(false);
            this.mHandler.postDelayed(this.mMmTelFeatureUnavailableRunnable, 1000L);
            return;
        }
        if (i == 2 || i == 4) {
            setImsStateAsUnavailable();
            notifyImsMmTelFeatureAvailableChanged();
            return;
        }
        if (i == 3) {
            initImsState();
            setMmTelFeatureAvailable(false);
            unregisterImsRegistrationCallback();
            unregisterMmTelCapabilityCallback();
            this.mHandler.postDelayed(this.mMmTelFeatureUnavailableRunnable, 1000L);
            return;
        }
        if (i == 5) {
            setImsStateAsUnavailable();
            unregisterImsRegistrationCallback();
            unregisterMmTelCapabilityCallback();
            this.mImsStateCallback = null;
            notifyImsMmTelFeatureAvailableChanged();
            return;
        }
        logw("onMmTelFeatureUnavailable: unexpected reason=" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
        logd("onImsRegistered: " + imsRegistrationAttributes);
        setImsRegistered(true);
        setImsAccessNetworkType(imsRegTechToAccessNetworkType(imsRegistrationAttributes.getRegistrationTechnology()));
        setImsRegisteredOverCrossSim(imsRegistrationAttributes.getRegistrationTechnology() == 2);
        notifyImsRegistrationStateChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onImsUnregistered(ImsReasonInfo imsReasonInfo) {
        logd("onImsUnregistered: " + imsReasonInfo);
        setImsRegistered(false);
        setImsAccessNetworkType(0);
        setImsRegisteredOverCrossSim(false);
        setMmTelCapabilities(new MmTelFeature.MmTelCapabilities());
        notifyImsRegistrationStateChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMmTelCapabilitiesChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        logd("onMmTelCapabilitiesChanged: " + mmTelCapabilities);
        setMmTelCapabilities(mmTelCapabilities);
        notifyImsMmTelCapabilitiesChanged();
    }

    private void startListeningForImsState() {
        if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
            setImsStateAsUnavailable();
            return;
        }
        this.mMmTelManager = ((ImsManager) this.mContext.getSystemService(ImsManager.class)).getImsMmTelManager(getSubId());
        initImsState();
        registerImsStateCallback();
    }

    private void stopListeningForImsState() {
        this.mHandler.removeCallbacks(this.mMmTelFeatureUnavailableRunnable);
        if (this.mMmTelManager != null) {
            unregisterMmTelCapabilityCallback();
            unregisterImsRegistrationCallback();
            unregisterImsStateCallback();
            this.mMmTelManager = null;
        }
    }

    private void registerImsStateCallback() {
        if (this.mImsStateCallback != null) {
            loge("ImsStateCallback is already registered for sub-" + getSubId());
            return;
        }
        this.mImsStateCallback = new ImsStateCallback() { // from class: com.android.services.telephony.domainselection.ImsStateTracker.2
            @Override // android.telephony.ims.ImsStateCallback
            public void onError() {
            }

            @Override // android.telephony.ims.ImsStateCallback
            public void onUnavailable(int i) {
                ImsStateTracker.this.onMmTelFeatureUnavailable(i);
            }

            @Override // android.telephony.ims.ImsStateCallback
            public void onAvailable() {
                ImsStateTracker.this.onMmTelFeatureAvailable();
            }
        };
        try {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            imsMmTelManager.registerImsStateCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this.mImsStateCallback);
        } catch (ImsException e) {
            loge("Exception when registering ImsStateCallback: " + e);
            this.mImsStateCallback = null;
        }
    }

    private void unregisterImsStateCallback() {
        ImsStateCallback imsStateCallback = this.mImsStateCallback;
        if (imsStateCallback != null) {
            try {
                this.mMmTelManager.unregisterImsStateCallback(imsStateCallback);
            } catch (Exception e) {
                logd("Exception when unregistering ImsStateCallback: " + e);
            }
            this.mImsStateCallback = null;
        }
    }

    private void registerImsRegistrationCallback() {
        if (this.mImsRegistrationCallback != null) {
            logd("RegistrationCallback is already registered for sub-" + getSubId());
            return;
        }
        this.mImsRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.services.telephony.domainselection.ImsStateTracker.3
            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                ImsStateTracker.this.onImsRegistered(imsRegistrationAttributes);
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                ImsStateTracker.this.onImsUnregistered(imsReasonInfo);
            }
        };
        try {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            imsMmTelManager.registerImsRegistrationCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this.mImsRegistrationCallback);
        } catch (ImsException e) {
            loge("Exception when registering RegistrationCallback: " + e);
            this.mImsRegistrationCallback = null;
        }
    }

    private void unregisterImsRegistrationCallback() {
        RegistrationManager.RegistrationCallback registrationCallback = this.mImsRegistrationCallback;
        if (registrationCallback != null) {
            try {
                this.mMmTelManager.unregisterImsRegistrationCallback(registrationCallback);
            } catch (Exception e) {
                logd("Exception when unregistering RegistrationCallback: " + e);
            }
            this.mImsRegistrationCallback = null;
        }
    }

    private void registerMmTelCapabilityCallback() {
        if (this.mMmTelCapabilityCallback != null) {
            logd("CapabilityCallback is already registered for sub-" + getSubId());
            return;
        }
        this.mMmTelCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.android.services.telephony.domainselection.ImsStateTracker.4
            @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
            public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                ImsStateTracker.this.onMmTelCapabilitiesChanged(mmTelCapabilities);
            }
        };
        try {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            imsMmTelManager.registerMmTelCapabilityCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this.mMmTelCapabilityCallback);
        } catch (ImsException e) {
            loge("Exception when registering CapabilityCallback: " + e);
            this.mMmTelCapabilityCallback = null;
        }
    }

    private void unregisterMmTelCapabilityCallback() {
        ImsMmTelManager.CapabilityCallback capabilityCallback = this.mMmTelCapabilityCallback;
        if (capabilityCallback != null) {
            try {
                this.mMmTelManager.unregisterMmTelCapabilityCallback(capabilityCallback);
            } catch (Exception e) {
                logd("Exception when unregistering CapabilityCallback: " + e);
            }
            this.mMmTelCapabilityCallback = null;
        }
    }

    public String imsStateToString() {
        return "{ MMTEL: featureAvailable=" + booleanToString(this.mMmTelFeatureAvailable) + ", registered=" + booleanToString(this.mImsRegistered) + ", accessNetworkType=" + accessNetworkTypeToString(this.mImsAccessNetworkType) + ", capabilities=" + mmTelCapabilitiesToString(this.mMmTelCapabilities) + " }";
    }

    protected static String accessNetworkTypeToString(int i) {
        switch (i) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "GERAN";
            case 2:
                return "UTRAN";
            case 3:
                return "EUTRAN";
            case 4:
                return "CDMA2000";
            case 5:
                return "IWLAN";
            case 6:
                return "NGRAN";
            default:
                return Integer.toString(i);
        }
    }

    private static String booleanToString(Boolean bool) {
        return bool == null ? "null" : bool.toString();
    }

    private static String mmTelCapabilitiesToString(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        if (mmTelCapabilities == null) {
            return "null";
        }
        return "[voice=" + mmTelCapabilities.isCapable(1) + ", video=" + mmTelCapabilities.isCapable(2) + ", ut=" + mmTelCapabilities.isCapable(4) + ", sms=" + mmTelCapabilities.isCapable(8) + "]";
    }

    private static String disconnectedCauseToString(int i) {
        switch (i) {
            case 1:
                return "UNKNOWN_TEMPORARY_ERROR";
            case 2:
                return "UNKNOWN_PERMANENT_ERROR";
            case 3:
                return "IMS_SERVICE_DISCONNECTED";
            case 4:
                return "NO_IMS_SERVICE_CONFIGURED";
            case 5:
                return "SUBSCRIPTION_INACTIVE";
            case 6:
                return "IMS_SERVICE_NOT_READY";
            default:
                return Integer.toString(i);
        }
    }

    public void dump(PrintWriter printWriter) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("ImsStateTracker:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("SlotId: " + getSlotId());
        androidUtilIndentingPrintWriter.println("SubId: " + getSubId());
        androidUtilIndentingPrintWriter.println("ServiceState: " + this.mServiceState);
        androidUtilIndentingPrintWriter.println("BarringInfo: " + this.mBarringInfo);
        androidUtilIndentingPrintWriter.println("ImsState: " + imsStateToString());
        androidUtilIndentingPrintWriter.println("Event Log:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mEventLog.dump(androidUtilIndentingPrintWriter);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    private void logd(String str) {
        Log.d(TAG, "[" + getSlotId() + "|" + getSubId() + "] " + str);
    }

    private void logi(String str) {
        Log.i(TAG, "[" + getSlotId() + "|" + getSubId() + "] " + str);
        this.mEventLog.log("[" + getSlotId() + "|" + getSubId() + "] " + str);
    }

    private void loge(String str) {
        Log.e(TAG, "[" + getSlotId() + "|" + getSubId() + "] " + str);
        this.mEventLog.log("[" + getSlotId() + "|" + getSubId() + "] " + str);
    }

    private void logw(String str) {
        Log.w(TAG, "[" + getSlotId() + "|" + getSubId() + "] " + str);
        this.mEventLog.log("[" + getSlotId() + "|" + getSubId() + "] " + str);
    }
}

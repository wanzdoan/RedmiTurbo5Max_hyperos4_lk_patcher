package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.DomainSelectionService;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TransportSelectorCallback;
import android.telephony.WwanSelectorCallback;
import android.telephony.ims.ImsReasonInfo;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class NormalCallDomainSelector extends DomainSelectorBase implements ImsStateTracker.ImsStateListener, ImsStateTracker.ServiceStateListener {
    protected static final int MSG_WAIT_FOR_IMS_STATE_TIMEOUT = 11;
    protected static final int WAIT_FOR_IMS_STATE_TIMEOUT_MS = 3000;
    private boolean mImsRegStateReceived;
    private boolean mMmTelCapabilitiesReceived;
    private boolean mReselectDomain;
    protected SelectorState mSelectorState;
    protected ServiceState mServiceState;

    public enum SelectorState {
        ACTIVE,
        INACTIVE,
        DESTROYED
    }

    public NormalCallDomainSelector(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener) {
        super(context, i, i2, looper, imsStateTracker, destroyListener, "NCDS");
        this.mSelectorState = SelectorState.INACTIVE;
        if (SubscriptionManager.isValidSubscriptionId(i2)) {
            logd("Subscribing to state callbacks. Subid:" + i2);
            this.mImsStateTracker.addServiceStateListener(this);
            this.mImsStateTracker.addImsStateListener(this);
            return;
        }
        loge("Invalid Subscription. Subid:" + i2);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 11) {
            loge("ImsStateTimeout. ImsState callback not received");
            if (this.mSelectorState != SelectorState.ACTIVE) {
                return;
            }
            if (!this.mImsRegStateReceived) {
                onImsRegistrationStateChanged();
            }
            if (this.mMmTelCapabilitiesReceived) {
                return;
            }
            onImsMmTelCapabilitiesChanged();
            return;
        }
        super.handleMessage(message);
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes, TransportSelectorCallback transportSelectorCallback) {
        this.mSelectionAttributes = selectionAttributes;
        this.mTransportSelectorCallback = transportSelectorCallback;
        this.mSelectorState = SelectorState.ACTIVE;
        if (transportSelectorCallback == null) {
            this.mSelectorState = SelectorState.INACTIVE;
            loge("Invalid params: TransportSelectorCallback is null");
            return;
        }
        if (selectionAttributes == null) {
            loge("Invalid params: SelectionAttributes are null");
            notifySelectionTerminated(43);
            return;
        }
        int subscriptionId = selectionAttributes.getSubscriptionId();
        boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(subscriptionId);
        if (selectionAttributes.getSelectorType() != 1 || selectionAttributes.isEmergency() || !zIsValidSubscriptionId) {
            loge("Domain Selection stopped. SelectorType:" + selectionAttributes.getSelectorType() + ", isEmergency:" + selectionAttributes.isEmergency() + ", ValidSubscriptionId:" + zIsValidSubscriptionId);
            notifySelectionTerminated(43);
            return;
        }
        if (subscriptionId == getSubId()) {
            logd("NormalCallDomainSelection triggered. Sub-id:" + subscriptionId);
            if (!this.mReselectDomain) {
                sendEmptyMessageDelayed(11, 3000L);
            }
            post(new Runnable() { // from class: com.android.services.telephony.domainselection.NormalCallDomainSelector$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$selectDomain$0();
                }
            });
            return;
        }
        this.mSelectorState = SelectorState.INACTIVE;
        loge("Subscription-ids doesn't match. This instance is associated with sub-id:" + getSubId() + ", requested sub-id:" + subscriptionId);
    }

    public void reselectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        logd("reselectDomain called");
        this.mReselectDomain = true;
        selectDomain(selectionAttributes, this.mTransportSelectorCallback);
    }

    public synchronized void finishSelection() {
        logd("finishSelection");
        SelectorState selectorState = this.mSelectorState;
        if (selectorState == SelectorState.ACTIVE) {
            cancelSelection();
            return;
        }
        if (selectorState != SelectorState.DESTROYED) {
            this.mImsStateTracker.removeServiceStateListener(this);
            this.mImsStateTracker.removeImsStateListener(this);
            this.mSelectionAttributes = null;
            this.mTransportSelectorCallback = null;
            destroy();
        }
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void destroy() {
        logd("destroy");
        int iOrdinal = this.mSelectorState.ordinal();
        if (iOrdinal == 0) {
            loge("destroy is called when selector state is in ACTIVE state");
            cancelSelection();
        } else if (iOrdinal == 1) {
            this.mSelectorState = SelectorState.DESTROYED;
            super.destroy();
        } else {
            if (iOrdinal != 2) {
                return;
            }
            super.destroy();
        }
    }

    public void cancelSelection() {
        logd("cancelSelection");
        this.mSelectorState = SelectorState.INACTIVE;
        this.mReselectDomain = false;
        TransportSelectorCallback transportSelectorCallback = this.mTransportSelectorCallback;
        if (transportSelectorCallback != null) {
            transportSelectorCallback.onSelectionTerminated(44);
        }
        finishSelection();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsRegistrationStateChanged() {
        logd("onImsRegistrationStateChanged. IsImsRegistered: " + this.mImsStateTracker.isImsRegistered());
        this.mImsRegStateReceived = true;
        lambda$selectDomain$0();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelCapabilitiesChanged() {
        logd("onImsMmTelCapabilitiesChanged. ImsVoiceCap: " + this.mImsStateTracker.isImsVoiceCapable() + " ImsVideoCap: " + this.mImsStateTracker.isImsVideoCapable());
        this.mMmTelCapabilitiesReceived = true;
        lambda$selectDomain$0();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelFeatureAvailableChanged() {
        logd("onImsMmTelFeatureAvailableChanged");
        lambda$selectDomain$0();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ServiceStateListener
    public void onServiceStateUpdated(ServiceState serviceState) {
        logd("onServiceStateUpdated:" + serviceState.getState());
        this.mServiceState = serviceState;
        lambda$selectDomain$0();
    }

    private void notifyPsSelected() {
        logd("notifyPsSelected");
        this.mSelectorState = SelectorState.INACTIVE;
        if (this.mImsStateTracker.isImsRegisteredOverWlan()) {
            logd("WLAN selected");
            this.mTransportSelectorCallback.onWlanSelected(false);
        } else if (this.mWwanSelectorCallback == null) {
            this.mTransportSelectorCallback.onWwanSelected(new Consumer() { // from class: com.android.services.telephony.domainselection.NormalCallDomainSelector$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$notifyPsSelected$1((WwanSelectorCallback) obj);
                }
            });
        } else {
            notifyPsSelectedInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyPsSelected$1(WwanSelectorCallback wwanSelectorCallback) {
        this.mWwanSelectorCallback = wwanSelectorCallback;
        notifyPsSelectedInternal();
    }

    private void notifyPsSelectedInternal() {
        if (this.mWwanSelectorCallback != null) {
            logd("notifyPsSelected - onWwanSelected");
            this.mWwanSelectorCallback.onDomainSelected(2, false);
        } else {
            loge("wwanSelectorCallback is null");
            this.mTransportSelectorCallback.onSelectionTerminated(43);
        }
    }

    private void notifyCsSelected(boolean z) {
        if (z && isOutOfService()) {
            loge("Cannot place call in current ServiceState: " + this.mServiceState.getState());
            notifySelectionTerminated(18);
            return;
        }
        logd("notifyCsSelected");
        this.mSelectorState = SelectorState.INACTIVE;
        if (this.mWwanSelectorCallback == null) {
            this.mTransportSelectorCallback.onWwanSelected(new Consumer() { // from class: com.android.services.telephony.domainselection.NormalCallDomainSelector$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$notifyCsSelected$2((WwanSelectorCallback) obj);
                }
            });
        } else {
            notifyCsSelectedInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyCsSelected$2(WwanSelectorCallback wwanSelectorCallback) {
        this.mWwanSelectorCallback = wwanSelectorCallback;
        notifyCsSelectedInternal();
    }

    private void notifyCsSelectedInternal() {
        if (this.mWwanSelectorCallback != null) {
            logd("wwanSelectorCallback -> onDomainSelected(DOMAIN_CS)");
            this.mWwanSelectorCallback.onDomainSelected(1, false);
        } else {
            loge("wwanSelectorCallback is null");
            this.mTransportSelectorCallback.onSelectionTerminated(43);
        }
    }

    private void notifySelectionTerminated(int i) {
        this.mSelectorState = SelectorState.INACTIVE;
        TransportSelectorCallback transportSelectorCallback = this.mTransportSelectorCallback;
        if (transportSelectorCallback != null) {
            transportSelectorCallback.onSelectionTerminated(i);
            finishSelection();
        }
    }

    private boolean isOutOfService() {
        return this.mServiceState.getState() == 1 || this.mServiceState.getState() == 3 || this.mServiceState.getState() == 2;
    }

    private boolean isWpsCallSupportedByIms() {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(this.mSelectionAttributes.getSubscriptionId(), "support_wps_over_ims_bool") : null;
        if (configForSubId != null) {
            return configForSubId.getBoolean("support_wps_over_ims_bool");
        }
        return false;
    }

    private void handleWpsCall() {
        if (isWpsCallSupportedByIms()) {
            logd("WPS call placed over PS");
            notifyPsSelected();
        } else {
            logd("WPS call placed over CS");
            notifyCsSelected(true);
        }
    }

    private void handleReselectDomain(ImsReasonInfo imsReasonInfo) {
        this.mReselectDomain = false;
        if (imsReasonInfo != null) {
            logd("PsDisconnectCause:" + imsReasonInfo.getCode());
            if (imsReasonInfo.getCode() == 146) {
                logd("Redialing over CS");
                notifyCsSelected(false);
                return;
            } else {
                logd("Redialing cancelled.");
                notifySelectionTerminated(-1);
                return;
            }
        }
        int csDisconnectCause = this.mSelectionAttributes.getCsDisconnectCause();
        if (csDisconnectCause == 3001 || csDisconnectCause == 3002) {
            if (this.mImsStateTracker.isImsRegistered()) {
                logd("IMS is registered");
                notifyPsSelected();
                return;
            }
            logd("IMS is NOT registered");
        }
        logd("Redialing cancelled.");
        notifySelectionTerminated(-1);
    }

    private boolean isTtySupportedByIms() {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(this.mSelectionAttributes.getSubscriptionId(), "carrier_volte_tty_supported_bool") : null;
        return configForSubId != null && configForSubId.getBoolean("carrier_volte_tty_supported_bool");
    }

    private boolean isTtyModeEnabled() {
        TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        if (telecomManager != null) {
            return telecomManager.getCurrentTtyMode() != 0;
        }
        loge("isTtyModeEnabled: telecom not available");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: selectDomain, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$selectDomain$0() {
        DomainSelectionService.SelectionAttributes selectionAttributes;
        if (this.mSelectorState == SelectorState.ACTIVE && (selectionAttributes = this.mSelectionAttributes) != null && this.mTransportSelectorCallback != null) {
            if (this.mServiceState == null) {
                logd("Waiting for ServiceState callback.");
                return;
            }
            if (this.mReselectDomain) {
                handleReselectDomain(selectionAttributes.getPsDisconnectCause());
                return;
            }
            if (!this.mImsStateTracker.isMmTelFeatureAvailable()) {
                logd("MmTelFeatureAvailable unavailable");
                notifyCsSelected(true);
                return;
            }
            if (this.mImsRegStateReceived && this.mMmTelCapabilitiesReceived) {
                if (hasMessages(11)) {
                    removeMessages(11);
                }
                if (!this.mImsStateTracker.isImsRegistered()) {
                    logd("IMS is NOT registered");
                    notifyCsSelected(true);
                    return;
                }
                if (isTtyModeEnabled() && !isTtySupportedByIms()) {
                    notifyCsSelected(true);
                    return;
                }
                if (this.mSelectionAttributes.isVideoCall()) {
                    logd("It's a video call");
                    if (this.mImsStateTracker.isImsVideoCapable()) {
                        logd("IMS is video capable");
                        notifyPsSelected();
                    } else {
                        logd("IMS is not video capable. Ending the call");
                        notifySelectionTerminated(43);
                    }
                    return;
                }
                if (this.mImsStateTracker.isImsVoiceCapable()) {
                    logd("IMS is voice capable");
                    if (PhoneNumberUtils.isWpsCallNumber(this.mSelectionAttributes.getAddress().getSchemeSpecificPart())) {
                        handleWpsCall();
                    } else {
                        notifyPsSelected();
                    }
                } else {
                    logd("IMS is not voice capable");
                    notifyCsSelected(true);
                }
                return;
            }
            loge("Waiting for ImsState and MmTelCapabilities callbacks");
            return;
        }
        this.mSelectorState = SelectorState.INACTIVE;
        logd("Domain Selection is stopped.");
    }

    protected SelectorState getSelectorState() {
        return this.mSelectorState;
    }
}

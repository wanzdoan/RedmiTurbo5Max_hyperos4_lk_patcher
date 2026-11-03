package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.telephony.DomainSelectionService;
import android.telephony.TransportSelectorCallback;
import android.telephony.WwanSelectorCallback;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class SmsDomainSelector extends DomainSelectorBase implements ImsStateTracker.ImsStateListener {
    protected boolean mDestroyed;
    private boolean mDomainSelectionRequested;

    public SmsDomainSelector(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener) {
        this(context, i, i2, looper, imsStateTracker, destroyListener, "DomainSelector-SMS");
    }

    protected SmsDomainSelector(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener, String str) {
        super(context, i, i2, looper, imsStateTracker, destroyListener, str);
        this.mDestroyed = false;
        this.mDomainSelectionRequested = false;
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void destroy() {
        if (this.mDestroyed) {
            return;
        }
        logd("destroy");
        this.mDestroyed = true;
        this.mImsStateTracker.removeImsStateListener(this);
        super.destroy();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 101) {
            selectDomain();
        } else {
            super.handleMessage(message);
        }
    }

    public void reselectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        if (isDomainSelectionRequested()) {
            logi("Domain selection is already running.");
            return;
        }
        logi("reselectDomain");
        this.mSelectionAttributes = selectionAttributes;
        setDomainSelectionRequested(true);
        obtainMessage(101).sendToTarget();
    }

    public void finishSelection() {
        logi("finishSelection");
        setDomainSelectionRequested(false);
        this.mSelectionAttributes = null;
        this.mTransportSelectorCallback = null;
        this.mWwanSelectorCallback = null;
        destroy();
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes, TransportSelectorCallback transportSelectorCallback) {
        if (isDomainSelectionRequested()) {
            logi("Domain selection is already running.");
            return;
        }
        this.mSelectionAttributes = selectionAttributes;
        this.mTransportSelectorCallback = transportSelectorCallback;
        setDomainSelectionRequested(true);
        this.mImsStateTracker.addImsStateListener(this);
        obtainMessage(101).sendToTarget();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelFeatureAvailableChanged() {
        sendMessageForDomainSelection();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsRegistrationStateChanged() {
        sendMessageForDomainSelection();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelCapabilitiesChanged() {
        sendMessageForDomainSelection();
    }

    protected boolean isSmsOverImsAvailable() {
        return this.mImsStateTracker.isImsSmsCapable() && this.mImsStateTracker.isImsRegistered() && this.mImsStateTracker.isMmTelFeatureAvailable();
    }

    protected void selectDomain() {
        if (!isDomainSelectionRequested()) {
            logi("Domain selection is not requested!");
            return;
        }
        logi("selectDomain: " + this.mImsStateTracker.imsStateToString());
        if (isSmsOverImsAvailable()) {
            if (this.mImsStateTracker.isImsRegisteredOverWlan()) {
                notifyWlanSelected(false);
                return;
            } else {
                notifyWwanSelected(2, false);
                return;
            }
        }
        notifyWwanSelected(1, false);
    }

    protected void sendMessageForDomainSelection() {
        removeMessages(101);
        obtainMessage(101).sendToTarget();
    }

    protected boolean isDomainSelectionRequested() {
        return this.mDomainSelectionRequested;
    }

    protected void setDomainSelectionRequested(boolean z) {
        if (this.mDomainSelectionRequested != z) {
            logd("DomainSelectionRequested: " + this.mDomainSelectionRequested + " >> " + z);
            this.mDomainSelectionRequested = z;
        }
    }

    protected void notifyWlanSelected(boolean z) {
        logi("DomainSelected: WLAN, E-PDN=" + z);
        this.mTransportSelectorCallback.onWlanSelected(z);
        setDomainSelectionRequested(false);
    }

    protected void notifyWwanSelected(final int i, final boolean z) {
        if (this.mWwanSelectorCallback == null) {
            this.mTransportSelectorCallback.onWwanSelected(new Consumer() { // from class: com.android.services.telephony.domainselection.SmsDomainSelector$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$notifyWwanSelected$0(i, z, (WwanSelectorCallback) obj);
                }
            });
        } else {
            notifyWwanSelectedInternal(i, z);
        }
        setDomainSelectionRequested(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyWwanSelected$0(int i, boolean z, WwanSelectorCallback wwanSelectorCallback) {
        this.mWwanSelectorCallback = wwanSelectorCallback;
        notifyWwanSelectedInternal(i, z);
    }

    protected void notifyWwanSelectedInternal(int i, boolean z) {
        logi("DomainSelected: WWAN/" + DomainSelectionService.getDomainName(i) + ", E-PDN=" + z);
        WwanSelectorCallback wwanSelectorCallback = this.mWwanSelectorCallback;
        if (wwanSelectorCallback != null) {
            wwanSelectorCallback.onDomainSelected(i, z);
        } else {
            this.mTransportSelectorCallback.onSelectionTerminated(3);
        }
    }
}

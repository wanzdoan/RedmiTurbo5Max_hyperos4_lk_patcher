package com.android.internal.telephony.domainselection;

import android.net.Uri;
import android.telephony.DomainSelectionService;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyCallDomainSelectionConnection extends DomainSelectionConnection {
    private DomainSelectionConnection.DomainSelectionConnectionCallback mCallback;
    private EmergencyStateTracker mEmergencyStateTracker;
    private AccessNetworksManager.AccessNetworksManagerCallback mPreferredTransportCallback;
    private int mPreferredTransportType;

    public EmergencyCallDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController) {
        this(phone, domainSelectionController, EmergencyStateTracker.getInstance());
    }

    public EmergencyCallDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController, EmergencyStateTracker emergencyStateTracker) {
        super(phone, 1, true, domainSelectionController);
        this.mEmergencyStateTracker = null;
        this.mPreferredTransportType = -1;
        this.mPreferredTransportCallback = new AccessNetworksManager.AccessNetworksManagerCallback(this, new CarrierPrivilegesTracker$$ExternalSyntheticLambda0()) { // from class: com.android.internal.telephony.domainselection.EmergencyCallDomainSelectionConnection.1
            @Override // com.android.internal.telephony.data.AccessNetworksManager.AccessNetworksManagerCallback
            public void onPreferredTransportChanged(int i, boolean z) {
            }
        };
        this.mTag = "EmergencyCallDomainSelectionConnection";
        this.mEmergencyStateTracker = emergencyStateTracker;
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onWlanSelected(boolean z) {
        this.mEmergencyStateTracker.lambda$onEmergencyTransportChanged$4(1, 2);
        if (z) {
            int preferredTransport = this.mPhone.getAccessNetworksManager().getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12);
            logi("onWlanSelected curTransportType=" + preferredTransport);
            if (preferredTransport != 2) {
                changePreferredTransport(2);
                return;
            }
        }
        CompletableFuture<Integer> completableFuture = getCompletableFuture();
        if (completableFuture != null) {
            completableFuture.complete(4);
        }
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onWwanSelected() {
        this.mEmergencyStateTracker.onEmergencyTransportChangedAndWait(1, 1);
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onSelectionTerminated(int i) {
        DomainSelectionConnection.DomainSelectionConnectionCallback domainSelectionConnectionCallback = this.mCallback;
        if (domainSelectionConnectionCallback != null) {
            domainSelectionConnectionCallback.onSelectionTerminated(i);
        }
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onDomainSelected(int i, boolean z) {
        if (i == 2 && z) {
            int preferredTransport = this.mPhone.getAccessNetworksManager().getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12);
            logi("onDomainSelected curTransportType=" + preferredTransport);
            if (preferredTransport != 1) {
                changePreferredTransport(1);
                return;
            }
        }
        super.onDomainSelected(i, z);
    }

    public CompletableFuture<Integer> createEmergencyConnection(DomainSelectionService.SelectionAttributes selectionAttributes, DomainSelectionConnection.DomainSelectionConnectionCallback domainSelectionConnectionCallback) {
        this.mCallback = domainSelectionConnectionCallback;
        selectDomain(selectionAttributes);
        return getCompletableFuture();
    }

    private void changePreferredTransport(int i) {
        logi("changePreferredTransport " + i);
        initHandler();
        this.mPreferredTransportType = i;
        this.mPhone.getAccessNetworksManager().registerForQualifiedNetworksChanged(this.mHandler, 2);
        this.mPhone.notifyEmergencyDomainSelected(i);
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    protected void onQualifiedNetworksChanged(List<AccessNetworksManager.QualifiedNetworks> list) {
        int preferredTransport = getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12, list);
        logi("onQualifiedNetworksChanged preferred=" + this.mPreferredTransportType + ", current=" + preferredTransport);
        if (preferredTransport == this.mPreferredTransportType) {
            CompletableFuture<Integer> completableFuture = getCompletableFuture();
            if (completableFuture != null) {
                if (preferredTransport == 2) {
                    completableFuture.complete(4);
                } else {
                    completableFuture.complete(2);
                }
            }
            this.mPhone.getAccessNetworksManager().unregisterForQualifiedNetworksChanged(this.mHandler);
        }
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void cancelSelection() {
        logi("cancelSelection");
        this.mPhone.getAccessNetworksManager().unregisterForQualifiedNetworksChanged(this.mHandler);
        super.cancelSelection();
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public CompletableFuture<Integer> reselectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        int disconnectCause = getDisconnectCause();
        int csDisconnectCause = selectionAttributes.getCsDisconnectCause();
        if (disconnectCause == 63) {
            csDisconnectCause = CallFailCause.EMERGENCY_TEMP_FAILURE;
        } else if (disconnectCause == 64) {
            csDisconnectCause = CallFailCause.EMERGENCY_PERM_FAILURE;
        }
        int i = csDisconnectCause;
        if (i != selectionAttributes.getCsDisconnectCause()) {
            selectionAttributes = getSelectionAttributes(selectionAttributes.getSlotIndex(), selectionAttributes.getSubscriptionId(), selectionAttributes.isExitedFromAirplaneMode(), selectionAttributes.getCallId(), selectionAttributes.getAddress() != null ? selectionAttributes.getAddress().getSchemeSpecificPart() : "", selectionAttributes.isTestEmergencyNumber(), i, selectionAttributes.getPsDisconnectCause(), selectionAttributes.getEmergencyRegistrationResult());
        }
        return super.reselectDomain(selectionAttributes);
    }

    public static DomainSelectionService.SelectionAttributes getSelectionAttributes(int i, int i2, boolean z, String str, String str2, boolean z2, int i3, ImsReasonInfo imsReasonInfo, EmergencyRegistrationResult emergencyRegistrationResult) {
        if (i3 == 1342) {
            i3 = CallFailCause.EMERGENCY_TEMP_FAILURE;
        } else if (i3 == 1343) {
            i3 = CallFailCause.EMERGENCY_PERM_FAILURE;
        }
        DomainSelectionService.SelectionAttributes.Builder csDisconnectCause = new DomainSelectionService.SelectionAttributes.Builder(i, i2, 1).setEmergency(true).setTestEmergencyNumber(z2).setExitedFromAirplaneMode(z).setCallId(str).setAddress(Uri.fromParts("tel", str2, null)).setCsDisconnectCause(i3);
        if (imsReasonInfo != null) {
            csDisconnectCause.setPsDisconnectCause(imsReasonInfo);
        }
        if (emergencyRegistrationResult != null) {
            csDisconnectCause.setEmergencyRegistrationResult(emergencyRegistrationResult);
        }
        return csDisconnectCause.build();
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    protected DomainSelectionService.SelectionAttributes getSelectionAttributesToRebindService() {
        DomainSelectionService.SelectionAttributes selectionAttributes = getSelectionAttributes();
        if (selectionAttributes == null) {
            return null;
        }
        return new DomainSelectionService.SelectionAttributes.Builder(selectionAttributes.getSlotIndex(), selectionAttributes.getSubscriptionId(), 1).setCallId(selectionAttributes.getCallId()).setAddress(selectionAttributes.getAddress()).setVideoCall(selectionAttributes.isVideoCall()).setEmergency(true).setTestEmergencyNumber(selectionAttributes.isTestEmergencyNumber()).setExitedFromAirplaneMode(selectionAttributes.isExitedFromAirplaneMode()).setEmergencyRegistrationResult(new EmergencyRegistrationResult(0, 4, 0, false, false, 0, 0, "", "", "")).build();
    }
}

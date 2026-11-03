package com.android.internal.telephony.domainselection;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EmergencySmsDomainSelectionConnection extends SmsDomainSelectionConnection {
    private EmergencyStateTracker mEmergencyStateTracker;
    private final Object mLock;
    private int mPreferredTransportType;

    public EmergencySmsDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController) {
        this(phone, domainSelectionController, EmergencyStateTracker.getInstance());
    }

    public EmergencySmsDomainSelectionConnection(Phone phone, DomainSelectionController domainSelectionController, EmergencyStateTracker emergencyStateTracker) {
        super(phone, domainSelectionController, true);
        this.mLock = new Object();
        this.mPreferredTransportType = -1;
        this.mTag = "DomainSelectionConnection-EmergencySMS";
        this.mEmergencyStateTracker = emergencyStateTracker;
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onWlanSelected(boolean z) {
        synchronized (this.mLock) {
            try {
                if (this.mPreferredTransportType != -1) {
                    logi("Domain selection completion is in progress");
                    return;
                }
                this.mEmergencyStateTracker.lambda$onEmergencyTransportChanged$4(2, 2);
                if (z && this.mPhone.getAccessNetworksManager().getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12) != 2) {
                    changePreferredTransport(2);
                } else {
                    super.onWlanSelected(z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onWwanSelected() {
        this.mEmergencyStateTracker.lambda$onEmergencyTransportChanged$4(2, 1);
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void onDomainSelected(int i, boolean z) {
        synchronized (this.mLock) {
            try {
                if (this.mPreferredTransportType != -1) {
                    logi("Domain selection completion is in progress");
                } else if (z && i == 2 && this.mPhone.getAccessNetworksManager().getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12) != 1) {
                    changePreferredTransport(1);
                } else {
                    super.onDomainSelected(i, z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    public void finishSelection() {
        AccessNetworksManager accessNetworksManager = this.mPhone.getAccessNetworksManager();
        synchronized (this.mLock) {
            try {
                if (this.mPreferredTransportType != -1) {
                    this.mPreferredTransportType = -1;
                    accessNetworksManager.unregisterForQualifiedNetworksChanged(this.mHandler);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        super.finishSelection();
    }

    @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection
    protected void onQualifiedNetworksChanged(List<AccessNetworksManager.QualifiedNetworks> list) {
        int preferredTransport = getPreferredTransport(NtnRILConstants.MI_NTN_OEM_ERROR_12, list);
        synchronized (this.mLock) {
            try {
                if (preferredTransport == this.mPreferredTransportType) {
                    this.mPreferredTransportType = -1;
                    super.onDomainSelected(2, true);
                    this.mPhone.getAccessNetworksManager().unregisterForQualifiedNetworksChanged(this.mHandler);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void changePreferredTransport(int i) {
        logi("Change preferred transport: " + i);
        initHandler();
        this.mPreferredTransportType = i;
        this.mPhone.getAccessNetworksManager().registerForQualifiedNetworksChanged(this.mHandler, 2);
        this.mPhone.notifyEmergencyDomainSelected(i);
    }
}

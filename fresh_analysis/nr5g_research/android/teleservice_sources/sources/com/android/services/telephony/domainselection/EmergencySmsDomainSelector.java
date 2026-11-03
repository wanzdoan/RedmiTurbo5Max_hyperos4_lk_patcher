package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.BarringInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.VopsSupportInfo;
import android.telephony.WwanSelectorCallback;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class EmergencySmsDomainSelector extends SmsDomainSelector implements ImsStateTracker.BarringInfoListener, ImsStateTracker.ServiceStateListener {
    private BarringInfo mBarringInfo;
    private boolean mBarringInfoReceived;
    private boolean mEmergencyNetworkScanInProgress;
    private CancellationSignal mEmergencyNetworkScanSignal;
    private List mEmergencyPdnRequiredRegistrationStatusByConfig;
    private Boolean mEmergencySmsOverImsSupportedByConfig;
    private ServiceState mServiceState;
    private boolean mServiceStateReceived;

    public EmergencySmsDomainSelector(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener) {
        super(context, i, i2, looper, imsStateTracker, destroyListener, "DomainSelector-EmergencySMS");
        this.mImsStateTracker.addServiceStateListener(this);
        this.mImsStateTracker.addBarringInfoListener(this);
    }

    @Override // com.android.services.telephony.domainselection.SmsDomainSelector, com.android.services.telephony.domainselection.DomainSelectorBase
    public void destroy() {
        if (this.mDestroyed) {
            return;
        }
        this.mImsStateTracker.removeServiceStateListener(this);
        this.mImsStateTracker.removeBarringInfoListener(this);
        super.destroy();
    }

    @Override // com.android.services.telephony.domainselection.SmsDomainSelector, android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 201) {
            handleEmergencyNetworkScanResult((EmergencyRegistrationResult) message.obj);
        } else {
            super.handleMessage(message);
        }
    }

    @Override // com.android.services.telephony.domainselection.SmsDomainSelector
    public void finishSelection() {
        super.finishSelection();
        this.mServiceStateReceived = false;
        this.mServiceState = null;
        this.mBarringInfoReceived = false;
        this.mBarringInfo = null;
        this.mEmergencySmsOverImsSupportedByConfig = null;
        this.mEmergencyPdnRequiredRegistrationStatusByConfig = null;
        this.mEmergencyNetworkScanInProgress = false;
        CancellationSignal cancellationSignal = this.mEmergencyNetworkScanSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mEmergencyNetworkScanSignal = null;
        }
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.BarringInfoListener
    public void onBarringInfoUpdated(BarringInfo barringInfo) {
        this.mBarringInfoReceived = true;
        this.mBarringInfo = barringInfo;
        sendMessageForDomainSelection();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ServiceStateListener
    public void onServiceStateUpdated(ServiceState serviceState) {
        this.mServiceStateReceived = true;
        this.mServiceState = serviceState;
        sendMessageForDomainSelection();
    }

    public boolean isDomainSelectionReady() {
        return this.mServiceStateReceived && this.mBarringInfoReceived;
    }

    @Override // com.android.services.telephony.domainselection.SmsDomainSelector
    protected boolean isSmsOverImsAvailable() {
        if (super.isSmsOverImsAvailable()) {
            if (isEmergencyPdnRequiredByRegistrationStatus()) {
                return isNetworkAvailableForImsEmergencySms();
            }
            return true;
        }
        return isImsEmergencySmsAvailable();
    }

    @Override // com.android.services.telephony.domainselection.SmsDomainSelector
    protected void selectDomain() {
        if (!isDomainSelectionRequested()) {
            logi("Domain selection is not requested!");
            return;
        }
        if (!isDomainSelectionReady()) {
            logd("Wait for the readiness of the domain selection!");
            return;
        }
        if (this.mEmergencyNetworkScanInProgress) {
            logi("Emergency network scan is in progress.");
            return;
        }
        logi("selectDomain: " + this.mImsStateTracker.imsStateToString());
        if (isSmsOverImsAvailable()) {
            boolean zIsEmergencyPdnRequiredByRegistrationStatus = isEmergencyPdnRequiredByRegistrationStatus();
            if (this.mImsStateTracker.isImsRegisteredOverWlan()) {
                if (!zIsEmergencyPdnRequiredByRegistrationStatus) {
                    notifyWlanSelected(false);
                    return;
                }
                logi("DomainSelected: WLAN >> WWAN");
            }
            if (zIsEmergencyPdnRequiredByRegistrationStatus && isNrEmergencyServiceFallbackRequired()) {
                requestEmergencyNetworkScan(List.of(3));
                return;
            } else {
                notifyWwanSelected(2, zIsEmergencyPdnRequiredByRegistrationStatus);
                return;
            }
        }
        notifyWwanSelected(1, false);
    }

    private void requestEmergencyNetworkScan(final List list) {
        this.mEmergencyNetworkScanInProgress = true;
        if (this.mWwanSelectorCallback == null) {
            this.mTransportSelectorCallback.onWwanSelected(new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencySmsDomainSelector$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$requestEmergencyNetworkScan$0(list, (WwanSelectorCallback) obj);
                }
            });
        } else {
            requestEmergencyNetworkScanInternal(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestEmergencyNetworkScan$0(List list, WwanSelectorCallback wwanSelectorCallback) {
        this.mWwanSelectorCallback = wwanSelectorCallback;
        requestEmergencyNetworkScanInternal(list);
    }

    private void requestEmergencyNetworkScanInternal(List list) {
        logi("requestEmergencyNetworkScan: preferredNetworks=" + list);
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.mEmergencyNetworkScanSignal = cancellationSignal;
        this.mWwanSelectorCallback.onRequestEmergencyNetworkScan(list, 2, false, cancellationSignal, new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencySmsDomainSelector$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$requestEmergencyNetworkScanInternal$1((EmergencyRegistrationResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestEmergencyNetworkScanInternal$1(EmergencyRegistrationResult emergencyRegistrationResult) {
        logi("requestEmergencyNetworkScan-onComplete");
        obtainMessage(EaseManager.EaseStyleDef.PERLIN, emergencyRegistrationResult).sendToTarget();
    }

    /* JADX WARN: Code duplicated, block: B:4:0x0020  */
    private void handleEmergencyNetworkScanResult(EmergencyRegistrationResult emergencyRegistrationResult) {
        int i;
        logi("handleEmergencyNetworkScanResult: " + emergencyRegistrationResult);
        this.mEmergencyNetworkScanInProgress = false;
        this.mEmergencyNetworkScanSignal = null;
        int accessNetwork = emergencyRegistrationResult.getAccessNetwork();
        if (accessNetwork != 6) {
            if (accessNetwork == 3) {
                if (emergencyRegistrationResult.getDomain() == 1) {
                    logi("PS emergency service is not supported in LTE network.");
                } else {
                    i = 2;
                }
            }
            i = 1;
        } else {
            i = 2;
        }
        notifyWwanSelected(i, i == 2);
    }

    private boolean isImsEmergencySmsAvailable() {
        boolean zIsEmergencyPdnRequiredByRegistrationStatus = isEmergencyPdnRequiredByRegistrationStatus();
        boolean zIsNetworkAvailableForImsEmergencySms = isNetworkAvailableForImsEmergencySms();
        logi("isImsEmergencySmsAvailable: emergencyPdnRequired=" + zIsEmergencyPdnRequiredByRegistrationStatus + ", mmTelFeatureAvailable=" + this.mImsStateTracker.isMmTelFeatureAvailable() + ", networkAvailable=" + zIsNetworkAvailableForImsEmergencySms);
        return zIsEmergencyPdnRequiredByRegistrationStatus && this.mImsStateTracker.isMmTelFeatureAvailable() && zIsNetworkAvailableForImsEmergencySms;
    }

    private boolean isEmergencySmsOverImsSupportedIfNetworkLimitedOrInService() {
        if (this.mEmergencySmsOverImsSupportedByConfig == null) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
            if (carrierConfigManager == null) {
                loge("CarrierConfigManager is null");
                return false;
            }
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(getSubId());
            if (configForSubId == null) {
                loge("PersistableBundle is null");
                return false;
            }
            this.mEmergencySmsOverImsSupportedByConfig = Boolean.valueOf(configForSubId.getBoolean("support_emergency_sms_over_ims_bool"));
        }
        return this.mEmergencySmsOverImsSupportedByConfig.booleanValue();
    }

    private boolean isEmergencyPdnRequiredByRegistrationStatus() {
        List emergencyPdnRequiredRegistrationStatus = getEmergencyPdnRequiredRegistrationStatus();
        if (emergencyPdnRequiredRegistrationStatus.isEmpty()) {
            return isEmergencySmsOverImsSupportedIfNetworkLimitedOrInService();
        }
        NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1);
        return emergencyPdnRequiredRegistrationStatus.contains(Integer.valueOf((networkRegistrationInfo == null || !networkRegistrationInfo.isNetworkRoaming()) ? 0 : 1));
    }

    private List getEmergencyPdnRequiredRegistrationStatus() {
        if (this.mEmergencyPdnRequiredRegistrationStatusByConfig == null) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
            if (carrierConfigManager == null) {
                return Collections.EMPTY_LIST;
            }
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(getSubId());
            if (configForSubId == null) {
                return Collections.EMPTY_LIST;
            }
            int[] intArray = configForSubId.getIntArray("imssms.emergency_sms_over_emergency_pdn_int_array");
            this.mEmergencyPdnRequiredRegistrationStatusByConfig = Collections.EMPTY_LIST;
            if (intArray != null) {
                this.mEmergencyPdnRequiredRegistrationStatusByConfig = (List) Arrays.stream(intArray).boxed().collect(Collectors.toList());
            }
        }
        return this.mEmergencyPdnRequiredRegistrationStatusByConfig;
    }

    private boolean isLteEmergencyAvailableInService() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mServiceState;
        return serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) != null && networkRegistrationInfo.getAccessNetworkTechnology() == 13 && networkRegistrationInfo.isRegistered() && isEmergencyServiceSupported(networkRegistrationInfo) && isEmergencyServiceAllowed();
    }

    private boolean isLteEmergencyAvailableInLimitedService() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mServiceState;
        return serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1)) != null && networkRegistrationInfo.getAccessNetworkTechnology() == 13 && networkRegistrationInfo.isEmergencyEnabled() && isEmergencyServiceSupported(networkRegistrationInfo) && isEmergencyServiceAllowed();
    }

    private boolean isNetworkAvailableForImsEmergencySms() {
        return isLteEmergencyAvailableInService() || isLteEmergencyAvailableInLimitedService() || isNrEmergencyAvailable();
    }

    private boolean isEmergencyServiceSupported(NetworkRegistrationInfo networkRegistrationInfo) {
        VopsSupportInfo vopsSupportInfo;
        DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo();
        return (dataSpecificInfo == null || (vopsSupportInfo = dataSpecificInfo.getVopsSupportInfo()) == null || !vopsSupportInfo.isEmergencyServiceSupported()) ? false : true;
    }

    private boolean isEmergencyServiceFallbackSupported(NetworkRegistrationInfo networkRegistrationInfo) {
        VopsSupportInfo vopsSupportInfo;
        DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo();
        return (dataSpecificInfo == null || (vopsSupportInfo = dataSpecificInfo.getVopsSupportInfo()) == null || !vopsSupportInfo.isEmergencyServiceFallbackSupported()) ? false : true;
    }

    private boolean isEmergencyServiceAllowed() {
        BarringInfo barringInfo = this.mBarringInfo;
        if (barringInfo == null) {
            return true;
        }
        return !barringInfo.getBarringServiceInfo(8).isBarred();
    }

    private boolean isNrEmergencyServiceFallbackRequired() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mServiceState;
        return serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) != null && networkRegistrationInfo.getAccessNetworkTechnology() == 20 && networkRegistrationInfo.isRegistered() && !isEmergencyServiceSupported(networkRegistrationInfo) && isEmergencyServiceFallbackSupported(networkRegistrationInfo);
    }

    private boolean isNrEmergencyAvailable() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mServiceState;
        if (serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) != null && networkRegistrationInfo.getAccessNetworkTechnology() == 20 && networkRegistrationInfo.isRegistered()) {
            return isEmergencyServiceSupported(networkRegistrationInfo) || isEmergencyServiceFallbackSupported(networkRegistrationInfo);
        }
        return false;
    }
}

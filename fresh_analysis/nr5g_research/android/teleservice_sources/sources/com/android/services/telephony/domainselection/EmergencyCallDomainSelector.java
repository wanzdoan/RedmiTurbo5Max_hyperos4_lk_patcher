package com.android.services.telephony.domainselection;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.telecom.TelecomManager;
import android.telephony.BarringInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.DomainSelectionService;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TransportSelectorCallback;
import android.telephony.WwanSelectorCallback;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyCallDomainSelector extends DomainSelectorBase implements ImsStateTracker.BarringInfoListener, ImsStateTracker.ImsStateListener {
    private static final boolean DBG;
    public static final int MSG_MAX_CELLULAR_TIMEOUT = 14;
    public static final int MSG_NETWORK_SCAN_TIMEOUT = 12;
    public static final int MSG_WAIT_DISCONNECTION_TIMEOUT = 15;
    public static final int MSG_WAIT_FOR_IMS_STATE_TIMEOUT = 16;
    private static List sDefaultRetryReasonCodes;
    private static List sDisconnectCauseForTerminatation;
    private static final LocalLog sLocalLog;
    private static List sPreferCsAfterCsfbFailure;
    private static List sPreferGeranWhenSimAbsent;
    private static List sPreferSlotWithNormalServiceList;
    private static List sSimReadyAllowList;
    private boolean mBarringInfoReceived;
    private int mCallSetupTimerOnCurrentRat;
    private CancellationSignal mCancelSignal;
    private List mCdmaPreferredNumbers;
    private final CrossSimRedialingController mCrossSimRedialingController;
    private boolean mCrossStackTimerExpired;
    private int mCsNetworkType;
    private int[] mCsRatsConfig;
    private int[] mCsRoamRatsConfig;
    private boolean mDestroyed;
    private int[] mDomainPreference;
    private int[] mDomainPreferenceRoam;
    private boolean mDomainSelected;
    private boolean mDomainSelectionRequested;
    private final DataConnectionStateHelper mEpdnHelper;
    private final ImsEmergencyRegistrationStateHelper mImsEmergencyRegistrationHelper;
    private int[] mImsRatsConfig;
    private boolean mImsRegStateReceived;
    private boolean mImsRegistered;
    private int[] mImsRoamRatsConfig;
    private boolean mIsEmergencyBarred;
    private boolean mIsMonitoringConnectivity;
    private boolean mIsScanRequested;
    private boolean mIsVoiceCapable;
    private boolean mIsWaitingForDataDisconnection;
    private int mLastNetworkType;
    private List mLastPreferredNetworks;
    private EmergencyRegistrationResult mLastRegResult;
    private int mLastTransportType;
    private boolean mLtePreferredAfterNrFailure;
    private int mMaxCellularTimeout;
    private boolean mMaxCellularTimerExpired;
    private int mMaxNumOfVoWifiTries;
    private boolean mMmTelCapabilitiesReceived;
    private int mModemCount;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private boolean mNetworkScanTimerExpired;
    private boolean mNonTtyOrTtySupported;
    private final PowerManager.WakeLock mPartialWakeLock;
    private boolean mPreferImsWhenCallsOnCs;
    private int mPreferredNetworkScanType;
    private int mPsNetworkType;
    private boolean mRequiresImsRegistration;
    private boolean mRequiresVoLteEnabled;
    private boolean mRequiresVoNrEnabled;
    private List mRetryReasonCodes;
    private boolean mScanLimitedOnlyAfterVolteFailure;
    private int mScanTimeout;
    private int mScanType;
    private boolean mSwitchRatPreferenceWithLocalNotRegistered;
    private boolean mTerminateAfterCsFailure;
    private boolean mTryCsWhenPsFails;
    private boolean mTryEsFallback;
    private boolean mVoWifiOverEmergencyPdn;
    private int mVoWifiRequiresCondition;
    private int mVoWifiTrialCount;
    private boolean mWasCsfbAfterPsFailure;
    private boolean mWiFiAvailable;
    private final List mWiFiNetworksAvailable;

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelFeatureAvailableChanged() {
    }

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
        sDefaultRetryReasonCodes = List.of(146, 103, 132, 1514);
        sDisconnectCauseForTerminatation = List.of(63);
        sLocalLog = new LocalLog(50);
    }

    public EmergencyCallDomainSelector(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener, CrossSimRedialingController crossSimRedialingController, DataConnectionStateHelper dataConnectionStateHelper) {
        super(context, i, i2, looper, imsStateTracker, destroyListener, "DomainSelector-EmergencyCall");
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                EmergencyCallDomainSelector.this.logi("onAvailable: " + network);
                if (network != null && !EmergencyCallDomainSelector.this.mWiFiNetworksAvailable.contains(network)) {
                    EmergencyCallDomainSelector.this.mWiFiNetworksAvailable.add(network);
                }
                EmergencyCallDomainSelector.this.mWiFiAvailable = true;
                EmergencyCallDomainSelector.this.sendEmptyMessage(17);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                EmergencyCallDomainSelector.this.logi("onLost: " + network);
                if (network != null) {
                    EmergencyCallDomainSelector.this.mWiFiNetworksAvailable.remove(network);
                }
                boolean zIsEmpty = EmergencyCallDomainSelector.this.mWiFiNetworksAvailable.isEmpty();
                EmergencyCallDomainSelector emergencyCallDomainSelector = EmergencyCallDomainSelector.this;
                if (!zIsEmpty) {
                    emergencyCallDomainSelector.logi("onLost: available networks=" + EmergencyCallDomainSelector.this.mWiFiNetworksAvailable);
                    return;
                }
                emergencyCallDomainSelector.mWiFiAvailable = false;
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                EmergencyCallDomainSelector.this.logi("onUnavailable");
                EmergencyCallDomainSelector.this.mWiFiNetworksAvailable.clear();
                EmergencyCallDomainSelector.this.mWiFiAvailable = false;
            }
        };
        this.mVoWifiTrialCount = 0;
        this.mCsNetworkType = 0;
        this.mPsNetworkType = 0;
        this.mLastNetworkType = 0;
        this.mLastTransportType = -1;
        this.mDestroyed = false;
        this.mIsScanRequested = false;
        this.mDomainSelected = false;
        this.mCrossStackTimerExpired = false;
        this.mMaxCellularTimerExpired = false;
        this.mNetworkScanTimerExpired = false;
        this.mDomainSelectionRequested = false;
        this.mWiFiNetworksAvailable = new ArrayList();
        this.mImsStateTracker.addBarringInfoListener(this);
        this.mImsStateTracker.addImsStateListener(this);
        this.mPartialWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, "DomainSelector-EmergencyCall");
        this.mCrossSimRedialingController = crossSimRedialingController;
        this.mEpdnHelper = dataConnectionStateHelper;
        dataConnectionStateHelper.setEmergencyCallDomainSelector(this);
        this.mImsEmergencyRegistrationHelper = new ImsEmergencyRegistrationStateHelper(this.mContext, getSlotId(), getSubId(), getLooper());
        acquireWakeLock();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.mDestroyed) {
        }
        switch (message.what) {
            case 11:
                startDomainSelection();
                break;
            case 12:
                handleNetworkScanTimeout();
                break;
            case 13:
                handleScanResult((EmergencyRegistrationResult) message.obj);
                break;
            case 14:
                handleMaxCellularTimeout();
                break;
            case 15:
                requestScanDelayed();
                break;
            case 16:
                handleWaitForImsStateTimeout();
                break;
            case 17:
                handleWifiAvailable();
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }

    private void handleScanResult(EmergencyRegistrationResult emergencyRegistrationResult) {
        int i;
        logi("handleScanResult result=" + emergencyRegistrationResult);
        if (this.mLastTransportType == 2) {
            logi("handleScanResult timer expired, WLAN has been selected, ignore stale result");
            return;
        }
        if (!allowEmergencyCalls(emergencyRegistrationResult)) {
            terminateSelectionPermanentlyForSlot();
            return;
        }
        if (emergencyRegistrationResult.getAccessNetwork() == 0) {
            if (maybeRedialOnTheOtherSlotInNormalService(this.mLastRegResult)) {
                return;
            }
            int i2 = this.mPreferredNetworkScanType;
            if (i2 == 2 && this.mScanType == 2) {
                this.mScanType = 1;
                this.mWwanSelectorCallback.onRequestEmergencyNetworkScan(this.mLastPreferredNetworks, 1, false, this.mCancelSignal, new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleScanResult$0((EmergencyRegistrationResult) obj);
                    }
                });
                return;
            } else if (i2 == 1 && (i = this.mScanType) == 2) {
                this.mWwanSelectorCallback.onRequestEmergencyNetworkScan(this.mLastPreferredNetworks, i, true, this.mCancelSignal, new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleScanResult$1((EmergencyRegistrationResult) obj);
                    }
                });
                return;
            } else {
                requestScan(false);
                return;
            }
        }
        checkAndSetTerminateAfterCsFailure(emergencyRegistrationResult);
        if (emergencyRegistrationResult.getRegState() == 1 || emergencyRegistrationResult.getRegState() == 5 || !maybeRedialOnTheOtherSlotInNormalService(emergencyRegistrationResult)) {
            this.mLastRegResult = emergencyRegistrationResult;
            removeMessages(12);
            onWwanNetworkTypeSelected(getAccessNetworkType(emergencyRegistrationResult));
            this.mCancelSignal = null;
            maybeModifyScanType(this.mLastNetworkType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleScanResult$0(EmergencyRegistrationResult emergencyRegistrationResult) {
        logi("requestScan-onComplete");
        sendMessage(obtainMessage(13, emergencyRegistrationResult));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleScanResult$1(EmergencyRegistrationResult emergencyRegistrationResult) {
        logi("requestScan-onComplete");
        sendMessage(obtainMessage(13, emergencyRegistrationResult));
    }

    private int getAccessNetworkType(EmergencyRegistrationResult emergencyRegistrationResult) {
        int regState;
        int accessNetwork = emergencyRegistrationResult.getAccessNetwork();
        if (accessNetwork != 3 || (((regState = emergencyRegistrationResult.getRegState()) != 1 && regState != 5) || !isCsDomainOnlyAvailable(emergencyRegistrationResult))) {
            return accessNetwork;
        }
        logi("getAccessNetworkType emergency not supported but CSFB is possible");
        return 2;
    }

    private boolean isCsDomainOnlyAvailable(EmergencyRegistrationResult emergencyRegistrationResult) {
        int domain = emergencyRegistrationResult.getDomain();
        if (domain == 1) {
            return true;
        }
        if ((domain & 1) > 0) {
            return !emergencyRegistrationResult.isEmcBearerSupported();
        }
        return false;
    }

    public void reselectDomain(DomainSelectionService.SelectionAttributes selectionAttributes) {
        logi("reselectDomain attr=" + selectionAttributes);
        this.mSelectionAttributes = selectionAttributes;
        post(new Runnable() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reselectDomain$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: reselectDomain, reason: merged with bridge method [inline-methods] */
    public void lambda$reselectDomain$2() {
        int dataConnectionState;
        logi("reselectDomain tryCsWhenPsFails=" + this.mTryCsWhenPsFails);
        int disconnectCause = getDisconnectCause();
        this.mCrossSimRedialingController.notifyCallFailure(disconnectCause);
        if ((disconnectCause == 325 && this.mCrossSimRedialingController.isThereOtherSlot()) || disconnectCause == 326) {
            logi("reselectDomain should redial on the other subscription");
            terminateSelectionForCrossSimRedialing(disconnectCause == 326);
            return;
        }
        if (this.mCrossStackTimerExpired) {
            logi("reselectDomain cross stack timer expired");
            terminateSelectionForCrossSimRedialing(false);
            return;
        }
        if (maybeTerminateSelection(disconnectCause)) {
            logi("reselectDomain terminate selection");
            return;
        }
        this.mTerminateAfterCsFailure = false;
        if (this.mTryCsWhenPsFails) {
            this.mTryCsWhenPsFails = false;
            logi("reselectDomain tryCs=" + accessNetworkTypeToString(this.mCsNetworkType));
            int i = this.mCsNetworkType;
            if (i != 0) {
                this.mWasCsfbAfterPsFailure = true;
                onWwanNetworkTypeSelected(i);
                return;
            }
        }
        if (this.mWasCsfbAfterPsFailure) {
            this.mWasCsfbAfterPsFailure = false;
            if (preferCsAfterCsfbFailure(disconnectCause)) {
                this.mLastNetworkType = 3;
            }
        }
        if (this.mMaxCellularTimerExpired) {
            if (this.mLastTransportType == 1 && maybeDialOverWlan()) {
                return;
            }
            if (this.mLastTransportType == 2) {
                this.mMaxCellularTimerExpired = false;
            }
        }
        if (this.mLastTransportType == 1 && this.mLastNetworkType == 6 && !this.mTryEsFallback && this.mLtePreferredAfterNrFailure && (dataConnectionState = this.mEpdnHelper.getDataConnectionState(getSlotId())) != 0 && dataConnectionState != -1) {
            this.mIsWaitingForDataDisconnection = true;
            sendEmptyMessageDelayed(15, 2000L);
            this.mDomainSelected = false;
            return;
        }
        if (this.mLastTransportType == 2) {
            onWwanSelected(new Runnable() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.reselectDomainInternal();
                }
            });
            return;
        }
        if (this.mLastNetworkType == 3 && this.mLastRegResult != null && this.mSelectionAttributes.getPsDisconnectCause() != null && !this.mScanLimitedOnlyAfterVolteFailure && !this.mSwitchRatPreferenceWithLocalNotRegistered) {
            int regState = this.mLastRegResult.getRegState();
            if (this.mSelectionAttributes.getPsDisconnectCause().getCode() == 132 && regState != 1 && regState != 5 && isSimReady()) {
                this.mLastNetworkType = 0;
                this.mSwitchRatPreferenceWithLocalNotRegistered = true;
            }
        }
        requestScan(true);
        this.mDomainSelected = false;
    }

    private boolean preferCsAfterCsfbFailure(int i) {
        EmergencyRegistrationResult emergencyRegistrationResult;
        if (i != 63) {
            return false;
        }
        if (sPreferCsAfterCsfbFailure == null || (emergencyRegistrationResult = this.mLastRegResult) == null || TextUtils.isEmpty(emergencyRegistrationResult.getCountryIso())) {
            return true;
        }
        return sPreferCsAfterCsfbFailure.contains(this.mLastRegResult.getCountryIso());
    }

    private int getDisconnectCause() {
        int csDisconnectCause = this.mSelectionAttributes.getCsDisconnectCause();
        ImsReasonInfo psDisconnectCause = this.mSelectionAttributes.getPsDisconnectCause();
        if (psDisconnectCause != null) {
            int code = psDisconnectCause.getCode();
            if (code == 363) {
                return 325;
            }
            if (code == 364) {
                return 326;
            }
        }
        return csDisconnectCause;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reselectDomainInternal() {
        post(new Runnable() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reselectDomainInternal$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reselectDomainInternal$3() {
        if (this.mDestroyed) {
            return;
        }
        requestScan(true, false, true);
        this.mDomainSelected = false;
    }

    private void requestScanDelayed() {
        logi("requestScanDelayed waiting=" + this.mIsWaitingForDataDisconnection);
        if (!this.mDestroyed && this.mIsWaitingForDataDisconnection) {
            requestScan(true);
            removeMessages(15);
        }
        this.mIsWaitingForDataDisconnection = false;
    }

    public void finishSelection() {
        logi("finishSelection");
        destroy();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.BarringInfoListener
    public void onBarringInfoUpdated(BarringInfo barringInfo) {
        if (this.mDestroyed) {
            return;
        }
        this.mBarringInfoReceived = true;
        BarringInfo.BarringServiceInfo barringServiceInfo = barringInfo.getBarringServiceInfo(8);
        this.mIsEmergencyBarred = barringServiceInfo.isBarred();
        logi("onBarringInfoUpdated emergencyBarred=" + this.mIsEmergencyBarred + ", serviceInfo=" + barringServiceInfo);
        selectDomain();
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes, TransportSelectorCallback transportSelectorCallback) {
        logi("selectDomain attr=" + selectionAttributes);
        this.mTransportSelectorCallback = transportSelectorCallback;
        this.mSelectionAttributes = selectionAttributes;
        this.mLastRegResult = selectionAttributes.getEmergencyRegistrationResult();
        this.mModemCount = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getActiveModemCount();
        sendEmptyMessage(11);
    }

    private void startDomainSelection() {
        logi("startDomainSelection modemCount=" + this.mModemCount);
        readResourceConfiguration();
        updateCarrierConfiguration();
        this.mDomainSelectionRequested = true;
        startCrossStackTimer();
        if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
            if (this.mCallSetupTimerOnCurrentRat > 0) {
                this.mImsEmergencyRegistrationHelper.start();
            }
            sendEmptyMessageDelayed(16, 3000L);
            selectDomain();
            return;
        }
        logi("startDomainSelection invalid subId");
        onImsRegistrationStateChanged();
        onImsMmTelCapabilitiesChanged();
    }

    private void handleWaitForImsStateTimeout() {
        logi("handleWaitForImsStateTimeout");
        onImsRegistrationStateChanged();
        onImsMmTelCapabilitiesChanged();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsRegistrationStateChanged() {
        this.mImsRegStateReceived = true;
        this.mImsRegistered = this.mImsStateTracker.isImsRegistered();
        logi("onImsRegistrationStateChanged " + this.mImsRegistered);
        selectDomain();
        handleImsStateChange();
    }

    @Override // com.android.services.telephony.domainselection.ImsStateTracker.ImsStateListener
    public void onImsMmTelCapabilitiesChanged() {
        this.mMmTelCapabilitiesReceived = true;
        this.mIsVoiceCapable = this.mImsStateTracker.isImsVoiceCapable();
        logi("onImsMmTelCapabilitiesChanged " + this.mIsVoiceCapable);
        selectDomain();
        handleImsStateChange();
    }

    private void handleImsStateChange() {
        if (this.mVoWifiOverEmergencyPdn || this.mDomainSelected) {
            return;
        }
        if (this.mMaxCellularTimerExpired || this.mNetworkScanTimerExpired) {
            maybeDialOverWlan();
        }
    }

    private boolean isSimReady() {
        return SubscriptionManager.isValidSubscriptionId(getSubId()) && ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getSimState(getSlotId()) == 5;
    }

    private void updateCarrierConfiguration() {
        PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(getSubId(), "imsemergency.emergency_over_ims_supported_3gpp_network_types_int_array", "imsemergency.emergency_over_ims_roaming_supported_3gpp_network_types_int_array", "imsemergency.emergency_over_cs_supported_access_network_types_int_array", "imsemergency.emergency_over_cs_roaming_supported_access_network_types_int_array", "imsemergency.emergency_domain_preference_int_array", "imsemergency.emergency_domain_preference_roaming_int_array", "imsemergency.prefer_ims_emergency_when_voice_calls_on_cs_bool", "imsemergency.emergency_vowifi_requires_condition_int", "imsemergency.emergency_scan_timer_sec_int", "imsemergency.maximum_cellular_search_timer_sec_int", "imsemergency.maximum_number_of_emergency_tries_over_vowifi_int", "imswfc.emergency_call_over_emergency_pdn_bool", "imsemergency.emergency_network_scan_type_int", "imsemergency.emergency_call_setup_timer_on_current_network_sec_int", "imsemergency.emergency_requires_ims_registration_bool", "imsemergency.emergency_requires_volte_enabled_bool", "imsemergency.emergency_requires_vonr_enabled_bool", "imsemergency.emergency_lte_preferred_after_nr_failed_bool", "imsemergency.scan_limited_service_after_volte_failure_bool", "imsemergency.ims_reasoninfo_code_to_retry_emergency_int_array", "carrier_volte_tty_supported_bool", "imsemergency.emergency_cdma_preferred_numbers_string_array");
        if (configForSubId == null) {
            configForSubId = CarrierConfigManager.getDefaultConfig();
        }
        this.mImsRatsConfig = configForSubId.getIntArray("imsemergency.emergency_over_ims_supported_3gpp_network_types_int_array");
        this.mImsRoamRatsConfig = configForSubId.getIntArray("imsemergency.emergency_over_ims_roaming_supported_3gpp_network_types_int_array");
        this.mCsRatsConfig = configForSubId.getIntArray("imsemergency.emergency_over_cs_supported_access_network_types_int_array");
        this.mCsRoamRatsConfig = configForSubId.getIntArray("imsemergency.emergency_over_cs_roaming_supported_access_network_types_int_array");
        this.mDomainPreference = configForSubId.getIntArray("imsemergency.emergency_domain_preference_int_array");
        this.mDomainPreferenceRoam = configForSubId.getIntArray("imsemergency.emergency_domain_preference_roaming_int_array");
        this.mPreferImsWhenCallsOnCs = configForSubId.getBoolean("imsemergency.prefer_ims_emergency_when_voice_calls_on_cs_bool");
        this.mVoWifiRequiresCondition = configForSubId.getInt("imsemergency.emergency_vowifi_requires_condition_int");
        this.mScanTimeout = configForSubId.getInt("imsemergency.emergency_scan_timer_sec_int") * 1000;
        this.mMaxCellularTimeout = configForSubId.getInt("imsemergency.maximum_cellular_search_timer_sec_int") * 1000;
        this.mMaxNumOfVoWifiTries = configForSubId.getInt("imsemergency.maximum_number_of_emergency_tries_over_vowifi_int");
        this.mVoWifiOverEmergencyPdn = configForSubId.getBoolean("imswfc.emergency_call_over_emergency_pdn_bool");
        this.mPreferredNetworkScanType = configForSubId.getInt("imsemergency.emergency_network_scan_type_int");
        this.mCallSetupTimerOnCurrentRat = configForSubId.getInt("imsemergency.emergency_call_setup_timer_on_current_network_sec_int") * 1000;
        this.mRequiresImsRegistration = configForSubId.getBoolean("imsemergency.emergency_requires_ims_registration_bool");
        this.mRequiresVoLteEnabled = configForSubId.getBoolean("imsemergency.emergency_requires_volte_enabled_bool");
        this.mRequiresVoNrEnabled = configForSubId.getBoolean("imsemergency.emergency_requires_vonr_enabled_bool");
        this.mLtePreferredAfterNrFailure = configForSubId.getBoolean("imsemergency.emergency_lte_preferred_after_nr_failed_bool");
        this.mScanLimitedOnlyAfterVolteFailure = configForSubId.getBoolean("imsemergency.scan_limited_service_after_volte_failure_bool");
        String[] stringArray = configForSubId.getStringArray("imsemergency.emergency_cdma_preferred_numbers_string_array");
        int[] intArray = configForSubId.getIntArray("imsemergency.ims_reasoninfo_code_to_retry_emergency_int_array");
        boolean z = configForSubId.getBoolean("carrier_volte_tty_supported_bool");
        this.mNonTtyOrTtySupported = isNonTtyOrTtySupported(z);
        if (this.mImsRatsConfig == null) {
            this.mImsRatsConfig = new int[0];
        }
        if (this.mCsRatsConfig == null) {
            this.mCsRatsConfig = new int[0];
        }
        if (this.mImsRoamRatsConfig == null) {
            this.mImsRoamRatsConfig = new int[0];
        }
        if (this.mCsRoamRatsConfig == null) {
            this.mCsRoamRatsConfig = new int[0];
        }
        if (this.mDomainPreference == null) {
            this.mDomainPreference = new int[0];
        }
        if (this.mDomainPreferenceRoam == null) {
            this.mDomainPreferenceRoam = new int[0];
        }
        if (stringArray == null) {
            stringArray = new String[0];
        }
        if (intArray == null) {
            intArray = new int[0];
        }
        List list = (List) Arrays.stream(intArray).boxed().collect(Collectors.toList());
        this.mRetryReasonCodes = list;
        list.addAll(sDefaultRetryReasonCodes);
        logi("updateCarrierConfiguration imsRats=" + arrayToString(this.mImsRatsConfig, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.accessNetworkTypeToString(i);
            }
        }) + ", csRats=" + arrayToString(this.mCsRatsConfig, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.accessNetworkTypeToString(i);
            }
        }) + ", imsRoamRats=" + arrayToString(this.mImsRoamRatsConfig, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.accessNetworkTypeToString(i);
            }
        }) + ", csRoamRats=" + arrayToString(this.mCsRoamRatsConfig, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.accessNetworkTypeToString(i);
            }
        }) + ", domainPref=" + arrayToString(this.mDomainPreference, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.domainPreferenceToString(i);
            }
        }) + ", domainPrefRoam=" + arrayToString(this.mDomainPreferenceRoam, new IntFunction() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return EmergencyCallDomainSelector.domainPreferenceToString(i);
            }
        }) + ", preferImsOnCs=" + this.mPreferImsWhenCallsOnCs + ", voWifiRequiresCondition=" + this.mVoWifiRequiresCondition + ", scanTimeout=" + this.mScanTimeout + ", maxCellularTimeout=" + this.mMaxCellularTimeout + ", maxNumOfVoWifiTries=" + this.mMaxNumOfVoWifiTries + ", voWifiOverEmergencyPdn=" + this.mVoWifiOverEmergencyPdn + ", preferredScanType=" + carrierConfigNetworkScanTypeToString(this.mPreferredNetworkScanType) + ", callSetupTimer=" + this.mCallSetupTimerOnCurrentRat + ", requiresImsReg=" + this.mRequiresImsRegistration + ", requiresVoLteEnabled=" + this.mRequiresVoLteEnabled + ", requiresVoNrEnabled=" + this.mRequiresVoNrEnabled + ", ltePreferredAfterNr=" + this.mLtePreferredAfterNrFailure + ", scanLimitedOnly=" + this.mScanLimitedOnlyAfterVolteFailure + ", retryReasonCodes=" + this.mRetryReasonCodes + ", ttySupported=" + z + ", cdmaPreferredNumbers=" + arrayToString(stringArray));
        this.mCdmaPreferredNumbers = Arrays.asList(stringArray);
        int i = this.mPreferredNetworkScanType;
        if (i == 1 || i == 2) {
            this.mScanType = 2;
        } else {
            this.mScanType = 0;
        }
    }

    private void readResourceConfiguration() {
        if (sSimReadyAllowList == null) {
            sSimReadyAllowList = readResourceConfiguration(R.array.config_countries_require_sim_for_emergency);
        }
        logi("readResourceConfiguration simReadyCountries=" + sSimReadyAllowList);
        if (sPreferSlotWithNormalServiceList == null) {
            sPreferSlotWithNormalServiceList = readResourceConfiguration(R.array.config_countries_prefer_normal_service_capable_subscription);
        }
        logi("readResourceConfiguration preferNormalServiceCountries=" + sPreferSlotWithNormalServiceList);
        if (sPreferCsAfterCsfbFailure == null) {
            sPreferCsAfterCsfbFailure = readResourceConfiguration(R.array.config_countries_prefer_cs_preferred_scan_after_csfb_failure);
        }
        logi("readResourceConfiguration preferCsAfterCsfbFailure=" + sPreferCsAfterCsfbFailure);
        if (sPreferGeranWhenSimAbsent == null) {
            sPreferGeranWhenSimAbsent = readResourceConfiguration(R.array.config_countries_prefer_geran_when_sim_absent);
        }
        logi("readResourceConfiguration preferGeranWhenSimAbsent=" + sPreferGeranWhenSimAbsent);
    }

    private List readResourceConfiguration(int i) {
        logi("readResourceConfiguration id=" + i);
        try {
            try {
                List listAsList = Arrays.asList(this.mContext.getResources().getStringArray(i));
                return listAsList == null ? new ArrayList() : listAsList;
            } catch (Resources.NotFoundException e) {
                loge("readResourceConfiguration exception=" + e);
                return new ArrayList();
            } catch (NullPointerException e2) {
                loge("readResourceConfiguration exception=" + e2);
                return new ArrayList();
            }
        } catch (Throwable th) {
            new ArrayList();
            throw th;
        }
    }

    public void clearResourceConfiguration() {
        sSimReadyAllowList = null;
        sPreferSlotWithNormalServiceList = null;
        sPreferCsAfterCsfbFailure = null;
        sPreferGeranWhenSimAbsent = null;
    }

    private void selectDomain() {
        if (this.mDomainSelectionRequested) {
            if (!this.mBarringInfoReceived || !this.mImsRegStateReceived || !this.mMmTelCapabilitiesReceived) {
                logi("selectDomain not received BarringInfo, IMS registration state, or MMTEL capabilities");
                return;
            }
            removeMessages(16);
            this.mDomainSelectionRequested = false;
            if (!allowEmergencyCalls(this.mSelectionAttributes.getEmergencyRegistrationResult())) {
                terminateSelectionPermanentlyForSlot();
            } else if (isWifiPreferred() || isInEmergencyCallbackModeOnWlan()) {
                onWlanSelected();
            } else {
                onWwanSelected(new Runnable() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.selectDomainInternal();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectDomainInternal() {
        post(new Runnable() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.selectDomainFromInitialState();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectDomainFromInitialState() {
        if (this.mDestroyed) {
            return;
        }
        if (isInEmergencyCallbackModeOnPsWwan()) {
            logi("selectDomain PS cellular connected in ECBM");
            this.mPsNetworkType = 3;
            onWwanNetworkTypeSelected(3);
            return;
        }
        boolean zIsCsInService = isCsInService();
        boolean zIsPsInService = isPsInService();
        if (!zIsCsInService && !zIsPsInService) {
            if (maybeRedialOnTheOtherSlotInNormalService(this.mLastRegResult)) {
                return;
            }
            this.mCsNetworkType = getSelectableCsNetworkType();
            this.mPsNetworkType = getSelectablePsNetworkType(false);
            logi("selectDomain limited service ps=" + accessNetworkTypeToString(this.mPsNetworkType) + ", cs=" + accessNetworkTypeToString(this.mCsNetworkType));
            if (!isInRoaming() && this.mPreferredNetworkScanType == 1) {
                requestScan(true);
                return;
            }
            int i = this.mPsNetworkType;
            if (i != 0) {
                onWwanNetworkTypeSelected(i);
            } else if (this.mCsNetworkType != 0) {
                checkAndSetTerminateAfterCsFailure(this.mLastRegResult);
                onWwanNetworkTypeSelected(this.mCsNetworkType);
            } else {
                requestScan(true);
            }
            maybeModifyScanType(this.mLastNetworkType);
            return;
        }
        this.mCsNetworkType = 0;
        this.mPsNetworkType = 0;
        if (zIsCsInService) {
            this.mCsNetworkType = getSelectableCsNetworkType();
        }
        if (zIsPsInService) {
            this.mPsNetworkType = getSelectablePsNetworkType(true);
        }
        boolean z = this.mCsNetworkType != 0;
        boolean z2 = this.mPsNetworkType != 0;
        logi("selectDomain CS={" + zIsCsInService + ", " + accessNetworkTypeToString(this.mCsNetworkType) + "}, PS={" + zIsPsInService + ", " + accessNetworkTypeToString(this.mPsNetworkType) + "}");
        if (z && z2) {
            if (this.mSelectionAttributes.isExitedFromAirplaneMode() || this.mPreferImsWhenCallsOnCs || isImsRegisteredWithVoiceCapability()) {
                this.mTryCsWhenPsFails = true;
                onWwanNetworkTypeSelected(this.mPsNetworkType);
            } else if (isDeactivatedSim()) {
                onWwanNetworkTypeSelected(this.mPsNetworkType);
            } else {
                onWwanNetworkTypeSelected(this.mCsNetworkType);
            }
        } else if (z2) {
            this.mTryEsFallback = this.mPsNetworkType == 6 && isEsFallbackAvailable();
            if (this.mSelectionAttributes.isExitedFromAirplaneMode() || !this.mRequiresImsRegistration || isImsRegisteredWithVoiceCapability() || isDeactivatedSim()) {
                onWwanNetworkTypeSelected(this.mPsNetworkType);
            } else {
                this.mTryEsFallback = false;
                requestScan(true, true);
            }
        } else if (z) {
            onWwanNetworkTypeSelected(this.mCsNetworkType);
        } else if (!this.mSelectionAttributes.isExitedFromAirplaneMode() && this.mRequiresImsRegistration && !isImsRegisteredWithVoiceCapability()) {
            requestScan(true, true);
        } else {
            this.mTryEsFallback = isEsFallbackAvailable();
            requestScan(true);
        }
        maybeModifyScanType(this.mLastNetworkType);
    }

    private void requestScan(boolean z) {
        requestScan(z, false);
    }

    private void requestScan(boolean z, boolean z2) {
        requestScan(z, z2, false);
    }

    private void requestScan(boolean z, boolean z2, boolean z3) {
        logi("requestScan timer=" + z + ", csPreferred=" + z2 + ", wifiFailed=" + z3);
        this.mCancelSignal = new CancellationSignal();
        if (!z3 || this.mLastPreferredNetworks == null) {
            this.mLastPreferredNetworks = getNextPreferredNetworks(z2, this.mTryEsFallback);
        }
        this.mTryEsFallback = false;
        if (isInRoaming() && this.mPreferredNetworkScanType == 1) {
            this.mScanType = 0;
        }
        this.mIsScanRequested = true;
        this.mWwanSelectorCallback.onRequestEmergencyNetworkScan(this.mLastPreferredNetworks, this.mScanType, false, this.mCancelSignal, new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$requestScan$4((EmergencyRegistrationResult) obj);
            }
        });
        if (z && isSimReady() && isEmcOverWifiSupported() && this.mScanTimeout > 0 && this.mVoWifiTrialCount < this.mMaxNumOfVoWifiTries) {
            logi("requestScan start scan timer");
            removeMessages(12);
            sendEmptyMessageDelayed(12, this.mScanTimeout);
            registerForConnectivityChanges();
        }
        if (this.mMaxCellularTimerExpired || hasMessages(14)) {
            return;
        }
        startMaxCellularTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestScan$4(EmergencyRegistrationResult emergencyRegistrationResult) {
        logi("requestScan-onComplete");
        sendMessage(obtainMessage(13, emergencyRegistrationResult));
    }

    public List getNextPreferredNetworks(boolean z, boolean z2) {
        List listGeneratePreferredNetworks;
        int i;
        if (this.mRequiresVoLteEnabled && !isAdvancedCallingSettingEnabled()) {
            logi("getNextPreferredNetworks VoLte setting is not enabled.");
            return generatePreferredNetworks(getCsNetworkTypeConfiguration());
        }
        ArrayList arrayList = new ArrayList();
        List domainPreference = getDomainPreference();
        int iIndexOf = domainPreference.indexOf(2);
        int iIndexOf2 = domainPreference.indexOf(1);
        logi("getNextPreferredNetworks psPriority=" + iIndexOf + ", csPriority=" + iIndexOf2 + ", csPreferred=" + z + ", esFallback=" + z2 + ", lastNetworkType=" + accessNetworkTypeToString(this.mLastNetworkType));
        EmergencyRegistrationResult emergencyRegistrationResult = this.mLastRegResult;
        if (emergencyRegistrationResult != null && sPreferGeranWhenSimAbsent.contains(emergencyRegistrationResult.getCountryIso()) && !isSimReady()) {
            logi("getNextPreferredNetworks preferGeran");
            arrayList.add(1);
            arrayList.add(2);
            arrayList.add(3);
            arrayList.add(6);
            return arrayList;
        }
        if (!z && (this.mLastNetworkType == 0 || z2)) {
            if (iIndexOf == -1 && iIndexOf2 == -1) {
                listGeneratePreferredNetworks = generatePreferredNetworks(getCsNetworkTypeConfiguration(), getImsNetworkTypeConfiguration());
            } else if (iIndexOf == -1 && iIndexOf2 > -1) {
                listGeneratePreferredNetworks = generatePreferredNetworks(getCsNetworkTypeConfiguration());
            } else if (iIndexOf > -1 && iIndexOf2 == -1) {
                listGeneratePreferredNetworks = generatePreferredNetworks(getImsNetworkTypeConfiguration());
            } else if (iIndexOf < iIndexOf2) {
                listGeneratePreferredNetworks = generatePreferredNetworks(getImsNetworkTypeConfiguration(), getCsNetworkTypeConfiguration());
            } else {
                listGeneratePreferredNetworks = generatePreferredNetworks(getCsNetworkTypeConfiguration(), getImsNetworkTypeConfiguration());
            }
            if (z2 && listGeneratePreferredNetworks.contains(6)) {
                listGeneratePreferredNetworks.remove((Object) 6);
                listGeneratePreferredNetworks.add(6);
            }
        } else if (z || (i = this.mLastNetworkType) == 3 || i == 6) {
            if (!z && this.mLastNetworkType == 6 && this.mLtePreferredAfterNrFailure) {
                listGeneratePreferredNetworks = generatePreferredNetworks(getImsNetworkTypeConfiguration(), getCsNetworkTypeConfiguration());
                if (listGeneratePreferredNetworks.contains(6)) {
                    listGeneratePreferredNetworks.remove((Object) 6);
                    listGeneratePreferredNetworks.add(6);
                }
            } else {
                listGeneratePreferredNetworks = iIndexOf2 > -1 ? generatePreferredNetworks(getCsNetworkTypeConfiguration(), getImsNetworkTypeConfiguration()) : generatePreferredNetworks(getImsNetworkTypeConfiguration());
            }
        } else {
            listGeneratePreferredNetworks = iIndexOf > -1 ? generatePreferredNetworks(getImsNetworkTypeConfiguration(), getCsNetworkTypeConfiguration()) : generatePreferredNetworks(getCsNetworkTypeConfiguration());
        }
        if (!isSimReady() && !listGeneratePreferredNetworks.contains(6)) {
            listGeneratePreferredNetworks.add(6);
        }
        if (!this.mNonTtyOrTtySupported) {
            logi("getNextPreferredNetworks adjust for TTY");
            listGeneratePreferredNetworks.remove((Object) 6);
            listGeneratePreferredNetworks.remove((Object) 3);
            if (listGeneratePreferredNetworks.isEmpty()) {
                listGeneratePreferredNetworks.add(2);
                listGeneratePreferredNetworks.add(1);
            }
        }
        return listGeneratePreferredNetworks;
    }

    private List generatePreferredNetworks(List... listArr) {
        ArrayList arrayList = new ArrayList();
        for (List list : listArr) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    private void handleWifiAvailable() {
        if (this.mDomainSelected) {
            return;
        }
        if (this.mMaxCellularTimerExpired || this.mNetworkScanTimerExpired) {
            maybeDialOverWlan();
        }
    }

    private void handleMaxCellularTimeout() {
        logi("handleMaxCellularTimeout");
        if (this.mVoWifiTrialCount >= this.mMaxNumOfVoWifiTries) {
            logi("handleMaxCellularTimeout already tried maximum");
            return;
        }
        this.mMaxCellularTimerExpired = true;
        if (this.mDomainSelected) {
            logi("handleMaxCellularTimeout wait for reselectDomain");
        } else {
            if (maybeDialOverWlan()) {
                return;
            }
            logd("handleMaxCellularTimeout VoWi-Fi is not available");
        }
    }

    private void handleNetworkScanTimeout() {
        logi("handleNetworkScanTimeout");
        this.mNetworkScanTimerExpired = true;
        maybeDialOverWlan();
    }

    private boolean maybeDialOverWlan() {
        boolean zIsWifiCallingSettingEnabled = this.mWiFiAvailable;
        logi("maybeDialOverWlan overEmergencyPdn=" + this.mVoWifiOverEmergencyPdn + ", wifiAvailable=" + zIsWifiCallingSettingEnabled);
        if (this.mVoWifiOverEmergencyPdn) {
            if (!zIsWifiCallingSettingEnabled && isImsRegisteredOverCrossSim()) {
                zIsWifiCallingSettingEnabled = true;
            }
            if (zIsWifiCallingSettingEnabled) {
                int i = this.mVoWifiRequiresCondition;
                if (i == 1) {
                    zIsWifiCallingSettingEnabled = isWifiCallingSettingEnabled();
                } else if (i == 2) {
                    zIsWifiCallingSettingEnabled = isWifiCallingActivated();
                }
            }
        } else {
            zIsWifiCallingSettingEnabled = isImsRegisteredWithVoiceCapability() && isImsRegisteredOverWifi();
        }
        logi("maybeDialOverWlan VoWi-Fi available=" + zIsWifiCallingSettingEnabled);
        if (zIsWifiCallingSettingEnabled) {
            CancellationSignal cancellationSignal = this.mCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mCancelSignal = null;
            }
            onWlanSelected();
        }
        return zIsWifiCallingSettingEnabled;
    }

    private boolean isCsInService() {
        EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
        if (emergencyRegistrationResult == null) {
            return false;
        }
        int regState = emergencyRegistrationResult.getRegState();
        return (regState == 1 || regState == 5) && (emergencyRegistrationResult.getDomain() & 1) > 0;
    }

    private int getSelectableCsNetworkType() {
        if (getDomainPreference().indexOf(1) == -1) {
            return 0;
        }
        EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
        logi("getSelectableCsNetworkType regResult=" + emergencyRegistrationResult);
        if (emergencyRegistrationResult == null) {
            return 0;
        }
        int accessNetwork = emergencyRegistrationResult.getAccessNetwork();
        List csNetworkTypeConfiguration = getCsNetworkTypeConfiguration();
        if (csNetworkTypeConfiguration.contains(Integer.valueOf(accessNetwork))) {
            return accessNetwork;
        }
        return (emergencyRegistrationResult.getAccessNetwork() == 3 && (emergencyRegistrationResult.getDomain() & 1) > 0 && csNetworkTypeConfiguration.contains(2)) ? 2 : 0;
    }

    private boolean isPsInService() {
        EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
        if (emergencyRegistrationResult == null) {
            return false;
        }
        int regState = emergencyRegistrationResult.getRegState();
        return (regState == 1 || regState == 5) && (emergencyRegistrationResult.getDomain() & 2) > 0;
    }

    private int getSelectablePsNetworkType(boolean z) {
        if (getDomainPreference().indexOf(2) != -1 && this.mNonTtyOrTtySupported) {
            EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
            logi("getSelectablePsNetworkType regResult=" + emergencyRegistrationResult);
            if (emergencyRegistrationResult == null) {
                return 0;
            }
            if (this.mRequiresVoLteEnabled && !isAdvancedCallingSettingEnabled()) {
                logi("getSelectablePsNetworkType VoLte setting is not enabled.");
                return 0;
            }
            int accessNetwork = emergencyRegistrationResult.getAccessNetwork();
            List imsNetworkTypeConfiguration = getImsNetworkTypeConfiguration();
            if (!z && !imsNetworkTypeConfiguration.contains(6) && !isSimReady() && !TextUtils.isEmpty(emergencyRegistrationResult.getCountryIso())) {
                imsNetworkTypeConfiguration.add(6);
                logi("getSelectablePsNetworkType ratList=" + imsNetworkTypeConfiguration);
            }
            if (imsNetworkTypeConfiguration.contains(Integer.valueOf(accessNetwork))) {
                if (this.mIsEmergencyBarred) {
                    logi("getSelectablePsNetworkType barred");
                    return 0;
                }
                if (accessNetwork == 6) {
                    return ((emergencyRegistrationResult.getNwProvidedEmc() == 1 || emergencyRegistrationResult.getNwProvidedEmc() == 3) && (emergencyRegistrationResult.isVopsSupported() || !z)) ? 6 : 0;
                }
                if (accessNetwork == 3 && emergencyRegistrationResult.isEmcBearerSupported() && (emergencyRegistrationResult.isVopsSupported() || !z)) {
                    return 3;
                }
            }
        }
        return 0;
    }

    private boolean isEsFallbackAvailable() {
        EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
        return emergencyRegistrationResult != null && getImsNetworkTypeConfiguration().contains(3) && emergencyRegistrationResult.getNwProvidedEmf() > 0;
    }

    private boolean isDeactivatedSim() {
        if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
            int dataActivationState = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(getSubId()).getDataActivationState();
            logi("isDeactivatedSim state=" + dataActivationState);
            if (dataActivationState == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmcOverWifiSupported() {
        if (isSimReady() && this.mNonTtyOrTtySupported) {
            boolean zContains = getDomainPreference().contains(3);
            logi("isEmcOverWifiSupported " + zContains);
            return zContains;
        }
        logi("isEmcOverWifiSupported invalid subId or lock state");
        return false;
    }

    private boolean isWifiPreferred() {
        if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
            return false;
        }
        int iIndexOf = getDomainPreference().indexOf(3);
        logi("isWifiPreferred priority=" + iIndexOf);
        if (iIndexOf != 0 || !isImsRegisteredWithVoiceCapability() || !isImsRegisteredOverWifi()) {
            return false;
        }
        logi("isWifiPreferred try emergency call over Wi-Fi");
        return true;
    }

    private boolean isAdvancedCallingSettingEnabled() {
        try {
            if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
                return true;
            }
            boolean zIsAdvancedCallingSettingEnabled = ((ImsManager) this.mContext.getSystemService(ImsManager.class)).getImsMmTelManager(getSubId()).isAdvancedCallingSettingEnabled();
            logi("isAdvancedCallingSettingEnabled " + zIsAdvancedCallingSettingEnabled);
            return zIsAdvancedCallingSettingEnabled;
        } catch (Exception e) {
            logi("isAdvancedCallingSettingEnabled e=" + e);
            return true;
        }
    }

    private boolean isNrAdvancedCallingEnabled() {
        SubscriptionInfoInternal subscriptionInfoInternal;
        try {
            int subId = getSubId();
            if (!SubscriptionManager.isValidSubscriptionId(subId) || (subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(subId)) == null) {
                return true;
            }
            boolean zIsNrAdvancedCallingEnabled = subscriptionInfoInternal.isNrAdvancedCallingEnabled();
            logi("isNrAdvancedCallingEnabled " + zIsNrAdvancedCallingEnabled);
            return zIsNrAdvancedCallingEnabled;
        } catch (Exception e) {
            logi("isNrAdvancedCallingEnabled e=" + e);
            return true;
        }
    }

    private boolean isWifiCallingActivated() {
        try {
            String provisioningStringValue = ((ImsManager) this.mContext.getSystemService(ImsManager.class)).getProvisioningManager(getSubId()).getProvisioningStringValue(67);
            boolean z = (TextUtils.isEmpty(provisioningStringValue) || TextUtils.equals("0", provisioningStringValue)) ? false : true;
            logi("isWifiCallingActivated " + z);
            return z;
        } catch (Exception e) {
            logi("isWifiCallingActivated e=" + e);
            return false;
        }
    }

    private boolean isWifiCallingSettingEnabled() {
        boolean zIsVoWiFiSettingEnabled = false;
        try {
            if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
                ImsMmTelManager imsMmTelManager = ((ImsManager) this.mContext.getSystemService(ImsManager.class)).getImsMmTelManager(getSubId());
                if (isInRoaming()) {
                    zIsVoWiFiSettingEnabled = imsMmTelManager.isVoWiFiRoamingSettingEnabled();
                } else {
                    zIsVoWiFiSettingEnabled = imsMmTelManager.isVoWiFiSettingEnabled();
                }
                logi("isWifiCallingSettingEnabled " + zIsVoWiFiSettingEnabled);
            }
            return zIsVoWiFiSettingEnabled;
        } catch (Exception e) {
            logi("isWifiCallingSettingEnabled e=" + e);
            return zIsVoWiFiSettingEnabled;
        }
    }

    private List getImsNetworkTypeConfiguration() {
        EmergencyRegistrationResult emergencyRegistrationResult;
        int[] iArr = this.mImsRatsConfig;
        if (isInRoaming()) {
            iArr = this.mImsRoamRatsConfig;
        }
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        if (arrayList.contains(6) && this.mRequiresVoNrEnabled && !isNrAdvancedCallingEnabled()) {
            arrayList.remove((Object) 6);
        }
        if (arrayList.contains(6) && (emergencyRegistrationResult = this.mLastRegResult) != null && emergencyRegistrationResult.getAccessNetwork() != 0 && this.mLastRegResult.getAccessNetwork() != 6 && !TextUtils.isEmpty(this.mLastRegResult.getCountryIso())) {
            arrayList.remove((Object) 6);
            arrayList.add(6);
        }
        return arrayList;
    }

    private List getCsNetworkTypeConfiguration() {
        int[] iArr = this.mCsRatsConfig;
        if (isInRoaming()) {
            iArr = this.mCsRoamRatsConfig;
        }
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        if (!this.mCdmaPreferredNumbers.isEmpty()) {
            if (this.mCdmaPreferredNumbers.contains(this.mSelectionAttributes.getAddress().getSchemeSpecificPart())) {
                arrayList.clear();
                arrayList.add(new Integer(4));
                return arrayList;
            }
            arrayList.remove(new Integer(4));
        }
        return arrayList;
    }

    private List getDomainPreference() {
        int[] iArr = this.mDomainPreference;
        if (isInRoaming()) {
            iArr = this.mDomainPreferenceRoam;
        }
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    private boolean isInRoaming() {
        if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
            return false;
        }
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(getSubId());
        String networkCountryIso = telephonyManagerCreateForSubscriptionId.getNetworkCountryIso();
        EmergencyRegistrationResult emergencyRegistrationResult = this.mLastRegResult;
        if (emergencyRegistrationResult != null) {
            if (emergencyRegistrationResult.getRegState() == 1) {
                return false;
            }
            String countryIso = emergencyRegistrationResult.getCountryIso();
            if (!TextUtils.isEmpty(countryIso)) {
                networkCountryIso = countryIso;
            }
        }
        String simCountryIso = telephonyManagerCreateForSubscriptionId.getSimCountryIso();
        logi("isInRoaming simIso=" + simCountryIso + ", netIso=" + networkCountryIso);
        if (TextUtils.isEmpty(simCountryIso) || TextUtils.isEmpty(networkCountryIso)) {
            return false;
        }
        return !TextUtils.equals(simCountryIso, networkCountryIso);
    }

    private boolean isImsRegisteredOverWifi() {
        boolean zIsImsRegisteredOverWlan = SubscriptionManager.isValidSubscriptionId(getSubId()) ? this.mImsStateTracker.isImsRegisteredOverWlan() : false;
        logi("isImsRegisteredOverWifi " + zIsImsRegisteredOverWlan);
        return zIsImsRegisteredOverWlan;
    }

    private boolean isImsRegisteredOverCrossSim() {
        boolean zIsImsRegisteredOverCrossSim = SubscriptionManager.isValidSubscriptionId(getSubId()) ? this.mImsStateTracker.isImsRegisteredOverCrossSim() : false;
        logi("isImsRegisteredOverCrossSim " + zIsImsRegisteredOverCrossSim);
        return zIsImsRegisteredOverCrossSim;
    }

    private boolean isImsRegisteredWithVoiceCapability() {
        boolean z = this.mImsRegistered && this.mIsVoiceCapable;
        logi("isImsRegisteredWithVoiceCapability " + z);
        return z;
    }

    private void onWlanSelected() {
        logi("onWlanSelected");
        if (this.mLastTransportType == 2) {
            logi("onWlanSelected ignore duplicated callback");
            return;
        }
        this.mDomainSelected = true;
        this.mNetworkScanTimerExpired = false;
        this.mIsWaitingForDataDisconnection = false;
        removeMessages(15);
        this.mLastTransportType = 2;
        this.mVoWifiTrialCount++;
        this.mTransportSelectorCallback.onWlanSelected(this.mVoWifiOverEmergencyPdn);
        this.mWwanSelectorCallback = null;
        removeMessages(12);
        removeMessages(14);
    }

    private void onWwanSelected(final Runnable runnable) {
        logi("onWwanSelected");
        if (this.mLastTransportType == 1) {
            logi("onWwanSelected ignore duplicated callback");
        } else {
            this.mLastTransportType = 1;
            this.mTransportSelectorCallback.onWwanSelected(new Consumer() { // from class: com.android.services.telephony.domainselection.EmergencyCallDomainSelector$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onWwanSelected$5(runnable, (WwanSelectorCallback) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWwanSelected$5(Runnable runnable, WwanSelectorCallback wwanSelectorCallback) {
        this.mWwanSelectorCallback = wwanSelectorCallback;
        runnable.run();
    }

    private void onWwanNetworkTypeSelected(int i) {
        logi("onWwanNetworkTypeSelected " + accessNetworkTypeToString(i));
        WwanSelectorCallback wwanSelectorCallback = this.mWwanSelectorCallback;
        if (wwanSelectorCallback == null) {
            logi("onWwanNetworkTypeSelected callback is null");
            return;
        }
        this.mDomainSelected = true;
        this.mNetworkScanTimerExpired = false;
        this.mLastNetworkType = i;
        int i2 = (i == 3 || i == 6) ? 2 : 1;
        wwanSelectorCallback.onDomainSelected(i2, i2 == 2);
    }

    private void registerForConnectivityChanges() {
        if (this.mIsMonitoringConnectivity) {
            return;
        }
        this.mWiFiNetworksAvailable.clear();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
        if (connectivityManager != null) {
            logi("registerForConnectivityChanges");
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(1);
            connectivityManager.registerNetworkCallback(builder.build(), this.mNetworkCallback);
            this.mIsMonitoringConnectivity = true;
        }
    }

    private void unregisterForConnectivityChanges() {
        if (this.mIsMonitoringConnectivity) {
            this.mWiFiNetworksAvailable.clear();
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class);
            if (connectivityManager != null) {
                logi("unregisterForConnectivityChanges");
                connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
                this.mIsMonitoringConnectivity = false;
            }
        }
    }

    private void startMaxCellularTimer() {
        logd("startMaxCellularTimer tried=" + this.mVoWifiTrialCount + ", max=" + this.mMaxNumOfVoWifiTries);
        if (!isEmcOverWifiSupported() || this.mMaxCellularTimeout <= 0 || this.mVoWifiTrialCount >= this.mMaxNumOfVoWifiTries) {
            return;
        }
        logi("startMaxCellularTimer start timer");
        sendEmptyMessageDelayed(14, this.mMaxCellularTimeout);
        registerForConnectivityChanges();
    }

    private boolean allowEmergencyCalls(EmergencyRegistrationResult emergencyRegistrationResult) {
        if (emergencyRegistrationResult == null) {
            loge("allowEmergencyCalls null regResult");
            return true;
        }
        String countryIso = emergencyRegistrationResult.getCountryIso();
        if (sSimReadyAllowList.contains(countryIso)) {
            if (isSimReady()) {
                SubscriptionInfo activeSubscriptionInfo = ((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfo(getSubId());
                if (activeSubscriptionInfo != null && activeSubscriptionInfo.getProfileClass() == 1) {
                    logi("allowEmergencyCalls bootstrap profile, iso=" + countryIso);
                    return false;
                }
            } else {
                logi("allowEmergencyCalls SIM state not ready, iso=" + countryIso);
                return false;
            }
        }
        return true;
    }

    private String getCountryIso(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        String networkCountryIso = telephonyManager.getNetworkCountryIso(getSlotId());
        if (TextUtils.isEmpty(networkCountryIso)) {
            for (int i = 0; i < this.mModemCount; i++) {
                networkCountryIso = telephonyManager.getNetworkCountryIso(i);
                if (!TextUtils.isEmpty(networkCountryIso)) {
                    return networkCountryIso;
                }
            }
        }
        return networkCountryIso;
    }

    private boolean maybeRedialOnTheOtherSlotInNormalService(EmergencyRegistrationResult emergencyRegistrationResult) {
        if (emergencyRegistrationResult == null) {
            return false;
        }
        if (!sPreferSlotWithNormalServiceList.contains(getCountryIso(emergencyRegistrationResult.getCountryIso())) || !this.mCrossSimRedialingController.isThereOtherSlotInService()) {
            return false;
        }
        logi("maybeRedialOnTheOtherSlotInNormalService");
        terminateSelectionForCrossSimRedialing(false);
        return true;
    }

    private void terminateSelectionPermanentlyForSlot() {
        logi("terminateSelectionPermanentlyForSlot");
        this.mCrossSimRedialingController.notifyCallFailure(326);
        if (this.mCrossSimRedialingController.isThereOtherSlot()) {
            terminateSelection(64);
        } else {
            terminateSelection(19);
        }
    }

    private void terminateSelectionForCrossSimRedialing(boolean z) {
        logi("terminateSelectionForCrossSimRedialing perm=" + z);
        terminateSelection(z ? 64 : 63);
    }

    private void terminateSelection(int i) {
        removeMessages(12);
        removeMessages(14);
        this.mTransportSelectorCallback.onSelectionTerminated(i);
    }

    private boolean maybeTerminateSelection(int i) {
        if (i == 249) {
            terminateSelection(-1);
            return true;
        }
        ImsReasonInfo psDisconnectCause = this.mSelectionAttributes.getPsDisconnectCause();
        List list = this.mRetryReasonCodes;
        if (list != null && psDisconnectCause != null) {
            if (list.contains(Integer.valueOf(psDisconnectCause.getCode()))) {
                return false;
            }
            terminateSelection(-1);
            return true;
        }
        if (psDisconnectCause != null || !sDisconnectCauseForTerminatation.contains(Integer.valueOf(i)) || !this.mTerminateAfterCsFailure) {
            return false;
        }
        logi("maybeTerminateSelection terminate after CS failure");
        terminateSelection(-1);
        return true;
    }

    public void startCrossStackTimer() {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (this.mModemCount == 1) {
            return;
        }
        EmergencyRegistrationResult emergencyRegistrationResult = this.mSelectionAttributes.getEmergencyRegistrationResult();
        if (emergencyRegistrationResult != null) {
            int regState = emergencyRegistrationResult.getRegState();
            boolean z4 = emergencyRegistrationResult.getDomain() > 0 && (regState == 1 || regState == 5);
            if (regState != 5 && !isInRoaming()) {
                z3 = false;
            }
            z = z4;
            z2 = z3;
        } else {
            z = false;
            z2 = false;
        }
        this.mCrossSimRedialingController.startTimer(this.mContext, this, this.mSelectionAttributes.getCallId(), this.mSelectionAttributes.getAddress().getSchemeSpecificPart(), z, z2, this.mModemCount);
    }

    public void notifyCrossStackTimerExpired() {
        logi("notifyCrossStackTimerExpired");
        this.mCrossStackTimerExpired = true;
        boolean zHangupOngoingDialing = hangupOngoingDialing();
        if (!this.mDomainSelected || zHangupOngoingDialing) {
            this.mIsWaitingForDataDisconnection = false;
            removeMessages(15);
            terminateSelectionForCrossSimRedialing(zHangupOngoingDialing);
        }
    }

    public void maybeHangupOngoingDialing() {
        logi("maybeHangupOngoingDialing");
        if (this.mDomainSelected && hangupOngoingDialing()) {
            notifyCrossStackTimerExpired();
        }
    }

    private boolean hangupOngoingDialing() {
        return this.mCallSetupTimerOnCurrentRat > 0 && !this.mImsEmergencyRegistrationHelper.isImsEmergencyRegistered();
    }

    public void notifyDataConnectionStateChange(int i, int i2) {
        if (i == getSlotId() && this.mIsWaitingForDataDisconnection) {
            if (i2 == 0 || i2 == -1) {
                requestScanDelayed();
            } else if (i2 == 4) {
                logi("notifyDataConnectionStateChange deactivation starting, restart timer");
                removeMessages(15);
                sendEmptyMessageDelayed(15, 2000L);
            }
        }
    }

    private void maybeModifyScanType(int i) {
        if (this.mPreferredNetworkScanType != 1 && this.mScanLimitedOnlyAfterVolteFailure && i == 3) {
            this.mScanType = 1;
        }
    }

    private static String arrayToString(int[] iArr, IntFunction intFunction) {
        int length = iArr.length;
        StringBuilder sb = new StringBuilder("{");
        if (length > 0) {
            sb.append((String) intFunction.apply(iArr[0]));
            for (int i = 1; i < length; i++) {
                sb.append(", ");
                sb.append((String) intFunction.apply(iArr[i]));
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String arrayToString(String[] strArr) {
        int length = strArr.length;
        StringBuilder sb = new StringBuilder("{");
        if (length > 0) {
            sb.append(strArr[0]);
            for (int i = 1; i < length; i++) {
                sb.append(", ");
                sb.append(strArr[i]);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String domainPreferenceToString(int i) {
        if (i == 1) {
            return "CS";
        }
        if (i == 2) {
            return "PS_3GPP";
        }
        if (i == 3) {
            return "PS_NON_3GPP";
        }
        return "UNKNOWN";
    }

    private static String carrierConfigNetworkScanTypeToString(int i) {
        if (i == 0) {
            return "NO_PREF";
        }
        if (i == 1) {
            return "FULL";
        }
        if (i == 2) {
            return "FULL_N_LIMITED";
        }
        return "UNKNOWN";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String accessNetworkTypeToString(int i) {
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

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    public void destroy() {
        if (DBG) {
            logd("destroy");
        }
        this.mEpdnHelper.setEmergencyCallDomainSelector(null);
        this.mImsEmergencyRegistrationHelper.destroy();
        this.mCrossSimRedialingController.stopTimer();
        releaseWakeLock();
        this.mDestroyed = true;
        this.mImsStateTracker.removeBarringInfoListener(this);
        this.mImsStateTracker.removeImsStateListener(this);
        unregisterForConnectivityChanges();
        super.destroy();
    }

    private void acquireWakeLock() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                logi("acquireWakeLock");
                this.mPartialWakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                try {
                    if (this.mPartialWakeLock.isHeld()) {
                        logi("releaseWakeLock");
                        this.mPartialWakeLock.release();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private boolean isInEmergencyCallbackModeOnWlan() {
        return this.mEpdnHelper.isInEmergencyCallbackMode(getSlotId()) && this.mEpdnHelper.getTransportType(getSlotId()) == 2 && this.mEpdnHelper.getDataConnectionState(getSlotId()) == 2;
    }

    private boolean isInEmergencyCallbackModeOnPsWwan() {
        return this.mEpdnHelper.isInEmergencyCallbackMode(getSlotId()) && this.mEpdnHelper.getTransportType(getSlotId()) == 1 && this.mEpdnHelper.getDataConnectionState(getSlotId()) == 2;
    }

    private boolean isNonTtyOrTtySupported(boolean z) {
        if (z) {
            return true;
        }
        TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        if (telecomManager == null) {
            logi("isNonTtyOrTtySupported telecom not available");
            return true;
        }
        boolean z2 = telecomManager.getCurrentTtyMode() == 0;
        logi("isNonTtyOrTtySupported ret=" + z2);
        return z2;
    }

    private void checkAndSetTerminateAfterCsFailure(EmergencyRegistrationResult emergencyRegistrationResult) {
        if (emergencyRegistrationResult == null) {
            return;
        }
        String mcc = emergencyRegistrationResult.getMcc();
        int accessNetwork = emergencyRegistrationResult.getAccessNetwork();
        if (TextUtils.isEmpty(mcc) || !mcc.startsWith("00")) {
            return;
        }
        if (accessNetwork == 2 || accessNetwork == 1) {
            this.mTerminateAfterCsFailure = true;
        }
    }

    public boolean isWiFiAvailable() {
        return this.mWiFiAvailable;
    }

    public List getWiFiNetworksAvailable() {
        return this.mWiFiNetworksAvailable;
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    protected void logi(String str) {
        super.logi(str);
        sLocalLog.log(str);
    }

    @Override // com.android.services.telephony.domainselection.DomainSelectorBase
    protected void loge(String str) {
        super.loge(str);
        sLocalLog.log(str);
    }
}

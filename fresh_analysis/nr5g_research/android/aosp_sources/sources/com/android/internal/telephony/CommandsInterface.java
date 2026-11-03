package com.android.internal.telephony;

import android.hardware.radio.network.PrioritizedNetworkScanRequest;
import android.hardware.radio.network.SatelliteNetworkInfo;
import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.Message;
import android.os.WorkSource;
import android.telephony.BarringInfo;
import android.telephony.CarrierRestrictionRules;
import android.telephony.ClientRequestStats;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.SignalThresholdInfo;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.imsphone.ImsCallInfo;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.SimPhonebookRecord;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface CommandsInterface {
    public static final String CB_FACILITY_BAIC = "AI";
    public static final String CB_FACILITY_BAICr = "IR";
    public static final String CB_FACILITY_BAOC = "AO";
    public static final String CB_FACILITY_BAOIC = "OI";
    public static final String CB_FACILITY_BAOICxH = "OX";
    public static final String CB_FACILITY_BA_ALL = "AB";
    public static final String CB_FACILITY_BA_FD = "FD";
    public static final String CB_FACILITY_BA_MO = "AG";
    public static final String CB_FACILITY_BA_MT = "AC";
    public static final String CB_FACILITY_BA_SIM = "SC";
    public static final String CB_FACILITY_BIC_ACR = "AR";
    public static final int CDMA_SMS_FAIL_CAUSE_INVALID_TELESERVICE_ID = 4;
    public static final int CDMA_SMS_FAIL_CAUSE_OTHER_TERMINAL_PROBLEM = 39;
    public static final int CDMA_SMS_FAIL_CAUSE_RESOURCE_SHORTAGE = 35;
    public static final int CF_ACTION_DISABLE = 0;
    public static final int CF_ACTION_ENABLE = 1;
    public static final int CF_ACTION_ERASURE = 4;
    public static final int CF_ACTION_REGISTRATION = 3;
    public static final int CF_REASON_ALL = 4;
    public static final int CF_REASON_ALL_CONDITIONAL = 5;
    public static final int CF_REASON_BUSY = 1;
    public static final int CF_REASON_NOT_LOGGED_IN = 6;
    public static final int CF_REASON_NOT_REACHABLE = 3;
    public static final int CF_REASON_NO_REPLY = 2;
    public static final int CF_REASON_UNCONDITIONAL = 0;
    public static final int CLIR_DEFAULT = 0;
    public static final int CLIR_INVOCATION = 1;
    public static final int CLIR_SUPPRESSION = 2;
    public static final int GSM_SMS_FAIL_CAUSE_MEMORY_CAPACITY_EXCEEDED = 211;
    public static final int GSM_SMS_FAIL_CAUSE_UNSPECIFIED_ERROR = 255;
    public static final int GSM_SMS_FAIL_CAUSE_USIM_APP_TOOLKIT_BUSY = 212;
    public static final int GSM_SMS_FAIL_CAUSE_USIM_DATA_DOWNLOAD_ERROR = 213;
    public static final int IMS_MMTEL_CAPABILITY_SMS = 4;
    public static final int IMS_MMTEL_CAPABILITY_VIDEO = 2;
    public static final int IMS_MMTEL_CAPABILITY_VOICE = 1;
    public static final int IMS_RCS_CAPABILITIES = 8;
    public static final int SERVICE_CLASS_DATA = 2;
    public static final int SERVICE_CLASS_DATA_ASYNC = 32;
    public static final int SERVICE_CLASS_DATA_SYNC = 16;
    public static final int SERVICE_CLASS_FAX = 4;
    public static final int SERVICE_CLASS_MAX = 128;
    public static final int SERVICE_CLASS_NONE = 0;
    public static final int SERVICE_CLASS_PACKET = 64;
    public static final int SERVICE_CLASS_PAD = 128;
    public static final int SERVICE_CLASS_SMS = 8;
    public static final int SERVICE_CLASS_VOICE = 1;
    public static final int SS_STATUS_UNKNOWN = 255;
    public static final int USSD_MODE_NOTIFY = 0;
    public static final int USSD_MODE_NW_RELEASE = 2;
    public static final int USSD_MODE_REQUEST = 1;

    void acceptCall(Message message);

    void acknowledgeIncomingGsmSmsWithPdu(boolean z, String str, Message message);

    void acknowledgeLastIncomingCdmaSms(boolean z, int i, Message message);

    void acknowledgeLastIncomingGsmSms(boolean z, int i, Message message);

    default void allocatePduSessionId(Message message) {
    }

    default void areUiccApplicationsEnabled(Message message) {
    }

    default boolean canToggleUiccApplicationsEnablement() {
        return false;
    }

    default void cancelEmergencyNetworkScan(boolean z, Message message) {
    }

    default void cancelHandover(Message message, int i) {
    }

    void cancelPendingUssd(Message message);

    void changeBarringPassword(String str, String str2, String str3, Message message);

    void changeIccPin(String str, String str2, Message message);

    void changeIccPin2ForApp(String str, String str2, String str3, Message message);

    void changeIccPinForApp(String str, String str2, String str3, Message message);

    void conference(Message message);

    void deactivateDataCall(int i, int i2, Message message);

    void deleteSmsOnSim(int i, Message message);

    void dial(String str, boolean z, EmergencyNumber emergencyNumber, boolean z2, int i, Message message);

    void dial(String str, boolean z, EmergencyNumber emergencyNumber, boolean z2, int i, UUSInfo uUSInfo, Message message);

    default void disablePrioritizedNetworkScan(int i, Message message) {
    }

    default void enableModem(boolean z, Message message) {
    }

    default void enablePrioritizedNetworkScan(int i, PrioritizedNetworkScanRequest prioritizedNetworkScanRequest, Message message) {
    }

    default void enableUiccApplications(boolean z, Message message) {
    }

    void exitEmergencyCallbackMode(Message message);

    default void exitEmergencyMode(Message message) {
    }

    void explicitCallTransfer(Message message);

    default void getAllowedCarriers(Message message, WorkSource workSource) {
    }

    void getAllowedNetworkTypesBitmap(Message message);

    void getAvailableNetworks(Message message);

    default void getBarringInfo(Message message) {
    }

    void getBasebandVersion(Message message);

    void getCLIR(Message message);

    void getCdmaBroadcastConfig(Message message);

    default void getCellInfoList(Message message, WorkSource workSource) {
    }

    default List<ClientRequestStats> getClientRequestStats() {
        return null;
    }

    void getCurrentCalls(Message message);

    void getDataCallList(Message message);

    void getDataRegistrationState(Message message);

    void getDeviceIdentity(Message message);

    void getGsmBroadcastConfig(Message message);

    void getHardwareConfig(Message message);

    void getIMSI(Message message);

    void getIMSIForApp(String str, Message message);

    void getIccCardStatus(Message message);

    void getImei(Message message);

    void getImsRegistrationState(Message message);

    default BarringInfo getLastBarringInfo() {
        return null;
    }

    void getLastCallFailCause(Message message);

    default void getModemActivityInfo(Message message, WorkSource workSource) {
    }

    default void getModemStatus(Message message) {
    }

    void getMute(Message message);

    void getNetworkSelectionMode(Message message);

    void getOperator(Message message);

    void getPreferredNetworkType(Message message);

    void getPreferredVoicePrivacy(Message message);

    void getRadioCapability(Message message);

    int getRadioState();

    int getRilVersion();

    void getSignalStrength(Message message);

    void getSimPhonebookCapacity(Message message);

    void getSimPhonebookRecords(Message message);

    default void getSlicingConfig(Message message) {
    }

    void getSmscAddress(Message message);

    default void getSupportedNetworkAlertCategories(Message message) {
    }

    default void getSystemSelectionChannels(Message message) {
    }

    default void getUsageSetting(Message message) {
    }

    void getVoiceRadioTechnology(Message message);

    void getVoiceRegistrationState(Message message);

    void handleCallSetupRequestFromSim(boolean z, Message message);

    void hangupConnection(int i, Message message);

    void hangupForegroundResumeBackground(Message message);

    void hangupWaitingOrBackground(Message message);

    void iccCloseLogicalChannel(int i, boolean z, Message message);

    void iccIO(int i, int i2, String str, int i3, int i4, int i5, String str2, String str3, Message message);

    void iccIOForApp(int i, int i2, String str, int i3, int i4, int i5, String str2, String str3, String str4, Message message);

    void iccOpenLogicalChannel(String str, int i, Message message);

    void iccTransmitApduBasicChannel(int i, int i2, int i3, int i4, int i5, String str, Message message);

    void iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z, Message message);

    default void isCellularIdentifierTransparencyEnabled(Message message) {
    }

    default void isN1ModeEnabled(Message message) {
    }

    default void isNrDualConnectivityEnabled(Message message, WorkSource workSource) {
    }

    default void isNullCipherAndIntegrityEnabled(Message message) {
    }

    default void isSatelliteEnabledForCarrier(int i, Message message) {
    }

    default void isSecurityAlgorithmsUpdatedEnabled(Message message) {
    }

    default void isVoNrEnabled(Message message, WorkSource workSource) {
    }

    default void notifyImsDataNetwork(int i, int i2, int i3, int i4, Message message) {
    }

    void nvResetConfig(int i, Message message);

    default void onSlotActiveStatusChange(boolean z) {
    }

    void queryCLIP(Message message);

    void queryCallForwardStatus(int i, int i2, String str, Message message);

    void queryCallWaiting(int i, Message message);

    void queryFacilityLock(String str, String str2, int i, Message message);

    void queryFacilityLockForApp(String str, String str2, int i, String str3, Message message);

    void queryTTYMode(Message message);

    void registerForApnUnthrottled(Handler handler, int i, Object obj);

    void registerForAvailable(Handler handler, int i, Object obj);

    default void registerForBarringInfoChanged(Handler handler, int i, Object obj) {
    }

    void registerForCallStateChanged(Handler handler, int i, Object obj);

    void registerForCarrierInfoForImsiEncryption(Handler handler, int i, Object obj);

    void registerForCellInfoList(Handler handler, int i, Object obj);

    default void registerForCellularIdentifierDisclosures(Handler handler, int i, Object obj) {
    }

    default void registerForConnectionSetupFailure(Handler handler, int i, Object obj) {
    }

    void registerForDataCallListChanged(Handler handler, int i, Object obj);

    void registerForDataCallListUpdated(Handler handler, int i, Object obj);

    void registerForDisplayInfo(Handler handler, int i, Object obj);

    void registerForDisplayNetworkTypeChanged(Handler handler, int i, Object obj);

    default void registerForEmergencyNetworkScan(Handler handler, int i, Object obj) {
    }

    void registerForEmergencyNumberList(Handler handler, int i, Object obj);

    void registerForExitEmergencyCallbackMode(Handler handler, int i, Object obj);

    void registerForHardwareConfigChanged(Handler handler, int i, Object obj);

    void registerForIccRefresh(Handler handler, int i, Object obj);

    void registerForIccStatusChanged(Handler handler, int i, Object obj);

    void registerForImeiMappingChanged(Handler handler, int i, Object obj);

    void registerForImsNetworkStateChanged(Handler handler, int i, Object obj);

    void registerForInCallVoicePrivacyOff(Handler handler, int i, Object obj);

    void registerForInCallVoicePrivacyOn(Handler handler, int i, Object obj);

    void registerForLceInfo(Handler handler, int i, Object obj);

    void registerForModemReset(Handler handler, int i, Object obj);

    void registerForNattKeepaliveStatus(Handler handler, int i, Object obj);

    void registerForNetworkScanResult(Handler handler, int i, Object obj);

    default void registerForNetworkSecurityEvents(Handler handler, int i, Object obj) {
    }

    void registerForNetworkStateChanged(Handler handler, int i, Object obj);

    void registerForNotAvailable(Handler handler, int i, Object obj);

    default void registerForNotifyAnbr(Handler handler, int i, Object obj) {
    }

    void registerForOffOrNotAvailable(Handler handler, int i, Object obj);

    void registerForOn(Handler handler, int i, Object obj);

    void registerForPcoData(Handler handler, int i, Object obj);

    void registerForPhysicalChannelConfiguration(Handler handler, int i, Object obj);

    void registerForPrioritizedScanModeChanged(Handler handler, int i, Object obj);

    void registerForRadioCapabilityChanged(Handler handler, int i, Object obj);

    void registerForRadioStateChanged(Handler handler, int i, Object obj);

    void registerForResendIncallMute(Handler handler, int i, Object obj);

    void registerForRilConnected(Handler handler, int i, Object obj);

    void registerForRingbackTone(Handler handler, int i, Object obj);

    default void registerForSecurityAlgorithmUpdates(Handler handler, int i, Object obj) {
    }

    void registerForSignalInfo(Handler handler, int i, Object obj);

    void registerForSimPhonebookChanged(Handler handler, int i, Object obj);

    void registerForSimPhonebookRecordsReceived(Handler handler, int i, Object obj);

    void registerForSlicingConfigChanged(Handler handler, int i, Object obj);

    void registerForSrvccStateChanged(Handler handler, int i, Object obj);

    default void registerForTriggerImsDeregistration(Handler handler, int i, Object obj) {
    }

    void registerForVoiceRadioTechChanged(Handler handler, int i, Object obj);

    default void registerUiccApplicationEnablementChanged(Handler handler, int i, Object obj) {
    }

    void rejectCall(Message message);

    default void releasePduSessionId(Message message, int i) {
    }

    void reportSmsMemoryStatus(boolean z, Message message);

    void reportStkServiceIsRunning(Message message);

    void requestIccSimAuthentication(int i, String str, String str2, Message message);

    void requestShutdown(Message message);

    default void sendAnbrQuery(int i, int i2, int i3, Message message) {
    }

    void sendBurstDtmf(String str, int i, int i2, Message message);

    void sendCdmaSMSExpectMore(byte[] bArr, Message message);

    void sendCdmaSms(byte[] bArr, Message message);

    void sendDeviceState(int i, boolean z, Message message);

    void sendDtmf(char c, Message message);

    void sendEnvelope(String str, Message message);

    void sendEnvelopeWithStatus(String str, Message message);

    void sendImsCdmaSms(byte[] bArr, int i, int i2, Message message);

    void sendImsGsmSms(String str, String str2, int i, int i2, Message message);

    void sendSMS(String str, String str2, Message message);

    void sendSMSExpectMore(String str, String str2, Message message);

    void sendTerminalResponse(String str, Message message);

    void sendUSSD(String str, Message message);

    void separateConnection(int i, Message message);

    default void setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules, Message message, WorkSource workSource) {
    }

    void setAllowedNetworkTypesBitmap(int i, Message message);

    void setCLIR(int i, Message message);

    void setCallForward(int i, int i2, int i3, String str, int i4, Message message);

    void setCallWaiting(boolean z, int i, Message message);

    void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, Message message);

    void setCdmaBroadcastActivation(boolean z, Message message);

    void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] cdmaSmsBroadcastConfigInfoArr, Message message);

    default void setCellInfoListRate(int i, Message message, WorkSource workSource) {
    }

    default void setCellularIdentifierTransparencyEnabled(boolean z, Message message) {
    }

    void setDataAllowed(boolean z, Message message);

    void setDataProfile(DataProfile[] dataProfileArr, Message message);

    default void setDataThrottling(Message message, WorkSource workSource, int i, long j) {
    }

    void setEmergencyCallbackMode(Handler handler, int i, Object obj);

    default void setEmergencyMode(int i, Message message) {
    }

    void setFacilityLock(String str, boolean z, String str2, int i, Message message);

    void setFacilityLockForApp(String str, boolean z, String str2, int i, String str3, Message message);

    void setGsmBroadcastActivation(boolean z, Message message);

    void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] smsBroadcastConfigInfoArr, Message message);

    void setInitialAttachApn(DataProfile dataProfile, Message message);

    void setLinkCapacityReportingCriteria(int i, int i2, int i3, int[] iArr, int[] iArr2, int i4, Message message);

    default void setLocationUpdates(boolean z, Message message) {
    }

    default void setLocationUpdates(boolean z, WorkSource workSource, Message message) {
    }

    default boolean setModemService(String str) {
        return true;
    }

    void setMute(boolean z, Message message);

    default void setN1ModeEnabled(boolean z, Message message) {
    }

    void setNetworkSelectionModeAutomatic(Message message);

    void setNetworkSelectionModeManual(String str, int i, Message message);

    default void setNrDualConnectivityState(int i, Message message, WorkSource workSource) {
    }

    default void setNullCipherAndIntegrityEnabled(boolean z, Message message) {
    }

    void setOnCallRing(Handler handler, int i, Object obj);

    void setOnCatCallSetUp(Handler handler, int i, Object obj);

    void setOnCatCcAlphaNotify(Handler handler, int i, Object obj);

    void setOnCatEvent(Handler handler, int i, Object obj);

    void setOnCatProactiveCmd(Handler handler, int i, Object obj);

    void setOnCatSessionEnd(Handler handler, int i, Object obj);

    void setOnIccSmsFull(Handler handler, int i, Object obj);

    void setOnNITZTime(Handler handler, int i, Object obj);

    void setOnNewCdmaSms(Handler handler, int i, Object obj);

    void setOnNewGsmBroadcastSms(Handler handler, int i, Object obj);

    void setOnNewGsmSms(Handler handler, int i, Object obj);

    default void setOnRegistrationFailed(Handler handler, int i, Object obj) {
    }

    void setOnRestrictedStateChanged(Handler handler, int i, Object obj);

    void setOnSignalStrengthUpdate(Handler handler, int i, Object obj);

    void setOnSmsOnSim(Handler handler, int i, Object obj);

    void setOnSmsStatus(Handler handler, int i, Object obj);

    void setOnSs(Handler handler, int i, Object obj);

    void setOnSuppServiceNotification(Handler handler, int i, Object obj);

    void setOnUSSD(Handler handler, int i, Object obj);

    void setPhoneType(int i);

    void setPreferredNetworkType(int i, Message message);

    void setPreferredVoicePrivacy(boolean z, Message message);

    void setRadioCapability(RadioCapability radioCapability, Message message);

    default void setRadioPower(boolean z, boolean z2, boolean z3, Message message) {
    }

    default void setSatelliteEnabledForCarrier(int i, boolean z, Message message) {
    }

    default void setSatelliteNetworkInfo(int i, SatelliteNetworkInfo satelliteNetworkInfo, Message message) {
    }

    default void setSatellitePlmn(int i, List<String> list, List<String> list2, Message message) {
    }

    default void setSecurityAlgorithmsUpdatedEnabled(boolean z, Message message) {
    }

    void setSignalStrengthReportingCriteria(List<SignalThresholdInfo> list, Message message);

    default void setSimCardPower(int i, Message message, WorkSource workSource) {
    }

    void setSmscAddress(String str, Message message);

    default void setSrvccCallInfo(SrvccConnection[] srvccConnectionArr, Message message) {
    }

    default void setSystemSelectionChannels(List<RadioAccessSpecifier> list, Message message) {
    }

    void setTTYMode(int i, Message message);

    void setUiccSubscription(int i, int i2, int i3, int i4, Message message);

    void setUnsolResponseFilter(int i, Message message);

    default void setUsageSetting(Message message, int i) {
    }

    default void setUserDataEnabled(Message message, boolean z) {
    }

    default void setUserDataRoamingEnabled(Message message, boolean z) {
    }

    default void setVoNrEnabled(boolean z, Message message, WorkSource workSource) {
    }

    void setupDataCall(int i, DataProfile dataProfile, boolean z, int i2, LinkProperties linkProperties, int i3, NetworkSliceInfo networkSliceInfo, TrafficDescriptor trafficDescriptor, boolean z2, Message message);

    void startDtmf(char c, Message message);

    default void startHandover(Message message, int i) {
    }

    default void startImsTraffic(int i, int i2, int i3, int i4, Message message) {
    }

    void startNattKeepalive(int i, KeepalivePacketData keepalivePacketData, int i2, Message message);

    void startNetworkScan(NetworkScanRequest networkScanRequest, Message message);

    void stopDtmf(Message message);

    default void stopImsTraffic(int i, Message message) {
    }

    void stopNattKeepalive(int i, Message message);

    void stopNetworkScan(Message message);

    void supplyIccPin(String str, Message message);

    void supplyIccPin2ForApp(String str, String str2, Message message);

    void supplyIccPinForApp(String str, String str2, Message message);

    void supplyIccPuk2ForApp(String str, String str2, String str3, Message message);

    void supplyIccPukForApp(String str, String str2, String str3, Message message);

    void supplyNetworkDepersonalization(String str, Message message);

    void supplySimDepersonalization(IccCardApplicationStatus.PersoSubState persoSubState, String str, Message message);

    void switchWaitingOrHoldingAndActive(Message message);

    void testingEmergencyCall();

    default void triggerEmergencyNetworkScan(int[] iArr, int i, Message message) {
    }

    default void triggerEpsFallback(int i, Message message) {
    }

    void unSetOnCatCallSetUp(Handler handler);

    void unSetOnCatCcAlphaNotify(Handler handler);

    void unSetOnCatEvent(Handler handler);

    void unSetOnCatProactiveCmd(Handler handler);

    void unSetOnCatSessionEnd(Handler handler);

    void unSetOnIccSmsFull(Handler handler);

    void unSetOnNITZTime(Handler handler);

    void unSetOnNewCdmaSms(Handler handler);

    void unSetOnNewGsmBroadcastSms(Handler handler);

    void unSetOnNewGsmSms(Handler handler);

    void unSetOnSignalStrengthUpdate(Handler handler);

    void unSetOnSmsOnSim(Handler handler);

    void unSetOnSmsStatus(Handler handler);

    void unregisterForAvailable(Handler handler);

    default void unregisterForBarringInfoChanged(Handler handler) {
    }

    void unregisterForCellInfoList(Handler handler);

    default void unregisterForConnectionSetupFailure(Handler handler) {
    }

    void unregisterForDataCallListChanged(Handler handler);

    void unregisterForDataCallListUpdated(Handler handler);

    void unregisterForDisplayInfo(Handler handler);

    void unregisterForDisplayNetworkTypeChanged(Handler handler);

    default void unregisterForEmergencyNetworkScan(Handler handler) {
    }

    void unregisterForHardwareConfigChanged(Handler handler);

    void unregisterForIccRefresh(Handler handler);

    void unregisterForImsNetworkStateChanged(Handler handler);

    void unregisterForInCallVoicePrivacyOff(Handler handler);

    void unregisterForInCallVoicePrivacyOn(Handler handler);

    void unregisterForLceInfo(Handler handler);

    void unregisterForModemReset(Handler handler);

    void unregisterForNattKeepaliveStatus(Handler handler);

    void unregisterForNetworkScanResult(Handler handler);

    void unregisterForNetworkStateChanged(Handler handler);

    void unregisterForNotAvailable(Handler handler);

    void unregisterForOffOrNotAvailable(Handler handler);

    void unregisterForOn(Handler handler);

    void unregisterForPcoData(Handler handler);

    void unregisterForPhysicalChannelConfiguration(Handler handler);

    void unregisterForPrioritizedScanModeChanged(Handler handler);

    void unregisterForRadioCapabilityChanged(Handler handler);

    void unregisterForRadioStateChanged(Handler handler);

    void unregisterForResendIncallMute(Handler handler);

    void unregisterForRilConnected(Handler handler);

    void unregisterForRingbackTone(Handler handler);

    void unregisterForSignalInfo(Handler handler);

    void unregisterForSimPhonebookChanged(Handler handler);

    void unregisterForSimPhonebookRecordsReceived(Handler handler);

    default void updateAllowedImsServices(Set<Integer> set, Set<Integer> set2, Message message) {
    }

    default void updateImsCallStatus(List<ImsCallInfo> list, Message message) {
    }

    default void updateImsRegistrationInfo(int i, int i2, int i3, int i4, int i5, Message message) {
    }

    default void updateImsRegistrationInfo(int i, int i2, int i3, int i4, Message message) {
    }

    void updateSimPhonebookRecord(SimPhonebookRecord simPhonebookRecord, Message message);

    void writeSmsToRuim(int i, byte[] bArr, Message message);

    void writeSmsToSim(int i, String str, String str2, Message message);

    default void setRadioPower(boolean z, Message message) {
        setRadioPower(z, false, false, message);
    }

    @Deprecated
    default HalVersion getHalVersion() {
        return HalVersion.UNKNOWN;
    }

    default HalVersion getHalVersion(int i) {
        return HalVersion.UNKNOWN;
    }

    default String getModemService() {
        return AccessNetworksManager.IWLAN_OPERATION_MODE_DEFAULT;
    }
}

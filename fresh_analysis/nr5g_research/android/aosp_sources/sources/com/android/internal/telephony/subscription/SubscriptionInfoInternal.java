package com.android.internal.telephony.subscription;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.UiccAccessRule;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class SubscriptionInfoInternal {
    private int mAlertSoundDuration;
    private final String mAllowedNetworkTypesForReasons;
    private final int mAreUiccApplicationsEnabled;
    private final int mCardId;
    private final String mCardString;
    private final byte[] mCarrierConfigAccessRules;
    private final int mCarrierId;
    private final String mCarrierName;
    private final String mCountryIso;
    private final int mDataRoaming;
    private final String mDeviceToDeviceStatusSharingContacts;
    private final int mDeviceToDeviceStatusSharingPreference;
    private final String mDisplayName;
    private final int mDisplayNameSource;
    private final String mEhplmns;
    private final String mEnabledMobileDataPolicies;
    private final String mGroupOwner;
    private final String mGroupUuid;
    private final String mHplmns;
    private final String mIccId;
    private final int mIconTint;
    private final int mId;
    private final String mImsi;
    private int mIsAlertSpeechEnabled;
    private int mIsAlertVibrationEnabled;
    private int mIsAmberAlertEnabled;
    private int mIsAreaInfoMessageEnabled;
    private final int mIsCrossSimCallingEnabled;
    private final int mIsEmbedded;
    private int mIsEmergencyAlertEnabled;
    private final int mIsEnhanced4GModeEnabled;
    private int mIsEtwsTestAlertEnabled;
    private int mIsExtremeThreatAlertEnabled;
    private final boolean mIsGroupDisabled;
    private final int mIsNrAdvancedCallingEnabled;
    private final int mIsOnlyNonTerrestrialNetwork;
    private final int mIsOpportunistic;
    private int mIsOptOutDialogEnabled;
    private final int mIsPrivateNetwork;
    private final int mIsRcsUceEnabled;
    private final int mIsRemovableEmbedded;
    private final int mIsSatelliteAttachEnabledForCarrier;
    private final int mIsSatelliteESOSSupported;
    private final int mIsSatelliteEnabled;
    private final int mIsSatelliteEntitlementStatus;
    private final int mIsSatelliteProvisionedForNonIpDatagram;
    private int mIsSevereThreatAlertEnabled;
    private int mIsTestAlertEnabled;
    private final int mIsVideoTelephonyEnabled;
    private final int mIsVoImsOptInEnabled;
    private final int mIsWifiCallingEnabled;
    private final int mIsWifiCallingEnabledForRoaming;
    private final int mLastUsedTPMessageReference;
    private final String mMcc;
    private final String mMnc;
    private final byte[] mNativeAccessRules;
    private final String mNumber;
    private final String mNumberFromCarrier;
    private final String mNumberFromIms;
    private final String mNumberFromTs43;
    private final int mPortIndex;
    private final int mProfileClass;
    private final byte[] mRcsConfig;
    private int mReminderInterval;
    private final String mSatelliteEntitlementBarredPlmnsList;
    private final String mSatelliteEntitlementDataPlanForPlmn;
    private final String mSatelliteEntitlementPlmns;
    private final String mSatelliteEntitlementServicesForPlmn;
    private final String mSatellitePlmnsDataServicePolicy;
    private final String mSatellitePlmnsVoiceServicePolicy;
    private final int mServiceCapabilities;
    private final int mSimSlotIndex;
    private final long mStreamingAppMaxDownlinkKbps;
    private final long mStreamingAppMaxUplinkKbps;
    private final int mTransferStatus;
    private final int mType;
    private final int mUsageSetting;
    private final int mUserId;
    private final int mWifiCallingMode;
    private final int mWifiCallingModeForRoaming;

    protected SubscriptionInfoInternal(Builder builder) {
        this.mId = builder.mId;
        this.mIccId = builder.mIccId;
        this.mSimSlotIndex = builder.mSimSlotIndex;
        this.mDisplayName = builder.mDisplayName;
        this.mCarrierName = builder.mCarrierName;
        this.mDisplayNameSource = builder.mDisplayNameSource;
        this.mIconTint = builder.mIconTint;
        this.mNumber = builder.mNumber;
        this.mDataRoaming = builder.mDataRoaming;
        this.mMcc = builder.mMcc;
        this.mMnc = builder.mMnc;
        this.mEhplmns = builder.mEhplmns;
        this.mHplmns = builder.mHplmns;
        this.mIsEmbedded = builder.mIsEmbedded;
        this.mCardString = builder.mCardString;
        this.mNativeAccessRules = builder.mNativeAccessRules;
        this.mCarrierConfigAccessRules = builder.mCarrierConfigAccessRules;
        this.mIsRemovableEmbedded = builder.mIsRemovableEmbedded;
        this.mIsExtremeThreatAlertEnabled = builder.mIsExtremeThreatAlertEnabled;
        this.mIsSevereThreatAlertEnabled = builder.mIsSevereThreatAlertEnabled;
        this.mIsAmberAlertEnabled = builder.mIsAmberAlertEnabled;
        this.mIsEmergencyAlertEnabled = builder.mIsEmergencyAlertEnabled;
        this.mAlertSoundDuration = builder.mAlertSoundDuration;
        this.mReminderInterval = builder.mReminderInterval;
        this.mIsAlertVibrationEnabled = builder.mIsAlertVibrationEnabled;
        this.mIsAlertSpeechEnabled = builder.mIsAlertSpeechEnabled;
        this.mIsEtwsTestAlertEnabled = builder.mIsEtwsTestAlertEnabled;
        this.mIsAreaInfoMessageEnabled = builder.mIsAreaInfoMessageEnabled;
        this.mIsTestAlertEnabled = builder.mIsTestAlertEnabled;
        this.mIsOptOutDialogEnabled = builder.mIsOptOutDialogEnabled;
        this.mIsEnhanced4GModeEnabled = builder.mIsEnhanced4GModeEnabled;
        this.mIsVideoTelephonyEnabled = builder.mIsVideoTelephonyEnabled;
        this.mIsWifiCallingEnabled = builder.mIsWifiCallingEnabled;
        this.mWifiCallingMode = builder.mWifiCallingMode;
        this.mWifiCallingModeForRoaming = builder.mWifiCallingModeForRoaming;
        this.mIsWifiCallingEnabledForRoaming = builder.mIsWifiCallingEnabledForRoaming;
        this.mIsOpportunistic = builder.mIsOpportunistic;
        this.mGroupUuid = builder.mGroupUuid;
        this.mCountryIso = builder.mCountryIso;
        this.mCarrierId = builder.mCarrierId;
        this.mProfileClass = builder.mProfileClass;
        this.mType = builder.mType;
        this.mGroupOwner = builder.mGroupOwner;
        this.mEnabledMobileDataPolicies = builder.mEnabledMobileDataPolicies;
        this.mImsi = builder.mImsi;
        this.mAreUiccApplicationsEnabled = builder.mAreUiccApplicationsEnabled;
        this.mIsRcsUceEnabled = builder.mIsRcsUceEnabled;
        this.mIsCrossSimCallingEnabled = builder.mIsCrossSimCallingEnabled;
        this.mRcsConfig = builder.mRcsConfig;
        this.mAllowedNetworkTypesForReasons = builder.mAllowedNetworkTypesForReasons;
        this.mDeviceToDeviceStatusSharingPreference = builder.mDeviceToDeviceStatusSharingPreference;
        this.mIsVoImsOptInEnabled = builder.mIsVoImsOptInEnabled;
        this.mDeviceToDeviceStatusSharingContacts = builder.mDeviceToDeviceStatusSharingContacts;
        this.mIsNrAdvancedCallingEnabled = builder.mIsNrAdvancedCallingEnabled;
        this.mNumberFromCarrier = builder.mNumberFromCarrier;
        this.mNumberFromIms = builder.mNumberFromIms;
        this.mNumberFromTs43 = builder.mNumberFromTs43;
        this.mPortIndex = builder.mPortIndex;
        this.mUsageSetting = builder.mUsageSetting;
        this.mLastUsedTPMessageReference = builder.mLastUsedTPMessageReference;
        this.mUserId = builder.mUserId;
        this.mIsSatelliteEnabled = builder.mIsSatelliteEnabled;
        this.mIsSatelliteAttachEnabledForCarrier = builder.mIsSatelliteAttachEnabledForCarrier;
        this.mIsOnlyNonTerrestrialNetwork = builder.mIsOnlyNonTerrestrialNetwork;
        this.mCardId = builder.mCardId;
        this.mIsGroupDisabled = builder.mIsGroupDisabled;
        this.mServiceCapabilities = builder.mServiceCapabilities;
        this.mTransferStatus = builder.mTransferStatus;
        this.mIsSatelliteEntitlementStatus = builder.mIsSatelliteEntitlementStatus;
        this.mSatelliteEntitlementPlmns = builder.mSatelliteEntitlementPlmns;
        this.mIsSatelliteESOSSupported = builder.mIsSatelliteESOSSupported;
        this.mIsSatelliteProvisionedForNonIpDatagram = builder.mIsSatelliteProvisionedForNonIpDatagram;
        this.mSatelliteEntitlementBarredPlmnsList = builder.mSatelliteEntitlementBarredPlmnsList;
        this.mSatelliteEntitlementDataPlanForPlmn = builder.mSatelliteEntitlementDataPlanForPlmn;
        this.mSatelliteEntitlementServicesForPlmn = builder.mSatelliteEntitlementServicesForPlmn;
        this.mSatellitePlmnsDataServicePolicy = builder.mSatellitePlmnsDataServicePolicy;
        this.mSatellitePlmnsVoiceServicePolicy = builder.mSatellitePlmnsVoiceServicePolicy;
        this.mIsPrivateNetwork = builder.mIsPrivateNetwork;
        this.mStreamingAppMaxDownlinkKbps = builder.mStreamingAppMaxDownlinkKbps;
        this.mStreamingAppMaxUplinkKbps = builder.mStreamingAppMaxUplinkKbps;
    }

    public int getSubscriptionId() {
        return this.mId;
    }

    public String getIccId() {
        return this.mIccId;
    }

    public int getSimSlotIndex() {
        return this.mSimSlotIndex;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public String getCarrierName() {
        return this.mCarrierName;
    }

    public int getDisplayNameSource() {
        return this.mDisplayNameSource;
    }

    public int getIconTint() {
        return this.mIconTint;
    }

    public String getNumber() {
        return TextUtils.isEmpty(this.mNumberFromCarrier) ? this.mNumber : this.mNumberFromCarrier;
    }

    public int getDataRoaming() {
        return this.mDataRoaming;
    }

    public String getMcc() {
        return this.mMcc;
    }

    public String getMnc() {
        return this.mMnc;
    }

    public String getEhplmns() {
        return this.mEhplmns;
    }

    public String getHplmns() {
        return this.mHplmns;
    }

    public boolean isEmbedded() {
        return this.mIsEmbedded != 0;
    }

    public int getEmbedded() {
        return this.mIsEmbedded;
    }

    public String getCardString() {
        return this.mCardString;
    }

    public byte[] getNativeAccessRules() {
        return this.mNativeAccessRules;
    }

    public byte[] getCarrierConfigAccessRules() {
        return this.mCarrierConfigAccessRules;
    }

    public boolean isRemovableEmbedded() {
        return this.mIsRemovableEmbedded != 0;
    }

    public int getRemovableEmbedded() {
        return this.mIsRemovableEmbedded;
    }

    public int getCellBroadcastExtremeThreatAlertEnabled() {
        return this.mIsExtremeThreatAlertEnabled;
    }

    public int getCellBroadcastSevereThreatAlertEnabled() {
        return this.mIsSevereThreatAlertEnabled;
    }

    public int getCellBroadcastAmberAlertEnabled() {
        return this.mIsAmberAlertEnabled;
    }

    public int getCellBroadcastEmergencyAlertEnabled() {
        return this.mIsEmergencyAlertEnabled;
    }

    public int getCellBroadcastAlertSoundDuration() {
        return this.mAlertSoundDuration;
    }

    public int getCellBroadcastAlertReminderInterval() {
        return this.mReminderInterval;
    }

    public int getCellBroadcastAlertVibrationEnabled() {
        return this.mIsAlertVibrationEnabled;
    }

    public int getCellBroadcastAlertSpeechEnabled() {
        return this.mIsAlertSpeechEnabled;
    }

    public int getCellBroadcastEtwsTestAlertEnabled() {
        return this.mIsEtwsTestAlertEnabled;
    }

    public int getCellBroadcastAreaInfoMessageEnabled() {
        return this.mIsAreaInfoMessageEnabled;
    }

    public int getCellBroadcastTestAlertEnabled() {
        return this.mIsTestAlertEnabled;
    }

    public int getCellBroadcastOptOutDialogEnabled() {
        return this.mIsOptOutDialogEnabled;
    }

    public boolean isEnhanced4GModeEnabled() {
        return this.mIsEnhanced4GModeEnabled == 1;
    }

    public int getEnhanced4GModeEnabled() {
        return this.mIsEnhanced4GModeEnabled;
    }

    public boolean isVideoTelephonyEnabled() {
        return this.mIsVideoTelephonyEnabled != 0;
    }

    public int getVideoTelephonyEnabled() {
        return this.mIsVideoTelephonyEnabled;
    }

    public boolean isWifiCallingEnabled() {
        return this.mIsWifiCallingEnabled == 1;
    }

    public int getWifiCallingEnabled() {
        return this.mIsWifiCallingEnabled;
    }

    public int getWifiCallingMode() {
        return this.mWifiCallingMode;
    }

    public int getWifiCallingModeForRoaming() {
        return this.mWifiCallingModeForRoaming;
    }

    public boolean isWifiCallingEnabledForRoaming() {
        return this.mIsWifiCallingEnabledForRoaming == 1;
    }

    public int getWifiCallingEnabledForRoaming() {
        return this.mIsWifiCallingEnabledForRoaming;
    }

    public boolean isOpportunistic() {
        return this.mIsOpportunistic != 0;
    }

    public int getOpportunistic() {
        return this.mIsOpportunistic;
    }

    public String getGroupUuid() {
        return this.mGroupUuid;
    }

    public String getCountryIso() {
        return this.mCountryIso;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public int getProfileClass() {
        return this.mProfileClass;
    }

    public int getSubscriptionType() {
        return this.mType;
    }

    public String getGroupOwner() {
        return this.mGroupOwner;
    }

    public String getEnabledMobileDataPolicies() {
        return this.mEnabledMobileDataPolicies;
    }

    public String getImsi() {
        return this.mImsi;
    }

    public boolean areUiccApplicationsEnabled() {
        return this.mAreUiccApplicationsEnabled != 0;
    }

    public int getUiccApplicationsEnabled() {
        return this.mAreUiccApplicationsEnabled;
    }

    public boolean isRcsUceEnabled() {
        return this.mIsRcsUceEnabled != 0;
    }

    public int getRcsUceEnabled() {
        return this.mIsRcsUceEnabled;
    }

    public boolean isCrossSimCallingEnabled() {
        return this.mIsCrossSimCallingEnabled != 0;
    }

    public int getCrossSimCallingEnabled() {
        return this.mIsCrossSimCallingEnabled;
    }

    public byte[] getRcsConfig() {
        return this.mRcsConfig;
    }

    public String getAllowedNetworkTypesForReasons() {
        return this.mAllowedNetworkTypesForReasons;
    }

    public int getDeviceToDeviceStatusSharingPreference() {
        return this.mDeviceToDeviceStatusSharingPreference;
    }

    public boolean isVoImsOptInEnabled() {
        return this.mIsVoImsOptInEnabled != 0;
    }

    public int getVoImsOptInEnabled() {
        return this.mIsVoImsOptInEnabled;
    }

    public String getDeviceToDeviceStatusSharingContacts() {
        return this.mDeviceToDeviceStatusSharingContacts;
    }

    public boolean isNrAdvancedCallingEnabled() {
        return this.mIsNrAdvancedCallingEnabled == 1;
    }

    public int getNrAdvancedCallingEnabled() {
        return this.mIsNrAdvancedCallingEnabled;
    }

    public String getNumberFromCarrier() {
        return this.mNumberFromCarrier;
    }

    public String getNumberFromIms() {
        return this.mNumberFromIms;
    }

    public String getNumberFromTs43() {
        return this.mNumberFromTs43;
    }

    public int getPortIndex() {
        return this.mPortIndex;
    }

    public int getUsageSetting() {
        return this.mUsageSetting;
    }

    public int getLastUsedTPMessageReference() {
        return this.mLastUsedTPMessageReference;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getSatelliteEnabled() {
        return this.mIsSatelliteEnabled;
    }

    public int getSatelliteAttachEnabledForCarrier() {
        return this.mIsSatelliteAttachEnabledForCarrier;
    }

    public int getOnlyNonTerrestrialNetwork() {
        return this.mIsOnlyNonTerrestrialNetwork;
    }

    public int getCardId() {
        return this.mCardId;
    }

    public boolean isGroupDisabled() {
        return this.mIsGroupDisabled;
    }

    public boolean isActive() {
        return this.mSimSlotIndex >= 0 || this.mType == 1;
    }

    public boolean isVisible() {
        if (getProfileClass() == 1 || getOnlyNonTerrestrialNetwork() == 1) {
            return false;
        }
        return !isOpportunistic() || TextUtils.isEmpty(this.mGroupUuid);
    }

    public int getServiceCapabilities() {
        return this.mServiceCapabilities;
    }

    public int getTransferStatus() {
        return this.mTransferStatus;
    }

    public int getSatelliteEntitlementStatus() {
        return this.mIsSatelliteEntitlementStatus;
    }

    public String getSatelliteEntitlementPlmns() {
        return this.mSatelliteEntitlementPlmns;
    }

    public int getSatelliteESOSSupported() {
        return this.mIsSatelliteESOSSupported;
    }

    public int getIsSatelliteProvisionedForNonIpDatagram() {
        return this.mIsSatelliteProvisionedForNonIpDatagram;
    }

    public String getSatelliteEntitlementBarredPlmnsList() {
        return this.mSatelliteEntitlementBarredPlmnsList;
    }

    public String getSatelliteEntitlementDataPlanForPlmns() {
        return this.mSatelliteEntitlementDataPlanForPlmn;
    }

    public String getSatelliteEntitlementPlmnsServiceTypes() {
        return this.mSatelliteEntitlementServicesForPlmn;
    }

    public String getSatellitePlmnsDataServicePolicy() {
        return this.mSatellitePlmnsDataServicePolicy;
    }

    public String getSatellitePlmnsVoiceServicePolicy() {
        return this.mSatellitePlmnsVoiceServicePolicy;
    }

    public int getIsPrivateNetwork() {
        return this.mIsPrivateNetwork;
    }

    public long getStreamingAppMaxDownlinkKbps() {
        return this.mStreamingAppMaxDownlinkKbps;
    }

    public long getStreamingAppMaxUplinkKbps() {
        return this.mStreamingAppMaxUplinkKbps;
    }

    public SubscriptionInfo toSubscriptionInfo() {
        SubscriptionInfo.Builder embedded = new SubscriptionInfo.Builder().setId(this.mId).setIccId(this.mIccId).setSimSlotIndex(this.mSimSlotIndex).setDisplayName(this.mDisplayName).setCarrierName(this.mCarrierName).setDisplayNameSource(this.mDisplayNameSource).setIconTint(this.mIconTint).setNumber(getNumber()).setDataRoaming(this.mDataRoaming).setMcc(this.mMcc).setMnc(this.mMnc).setEhplmns(TextUtils.isEmpty(this.mEhplmns) ? null : this.mEhplmns.split(",")).setHplmns(TextUtils.isEmpty(this.mHplmns) ? null : this.mHplmns.split(",")).setCountryIso(this.mCountryIso).setEmbedded(this.mIsEmbedded != 0);
        byte[] bArr = this.mNativeAccessRules;
        SubscriptionInfo.Builder groupOwner = embedded.setNativeAccessRules(bArr.length == 0 ? null : UiccAccessRule.decodeRules(bArr)).setCardString(this.mCardString).setCardId(this.mCardId).setOpportunistic(this.mIsOpportunistic != 0).setGroupUuid(this.mGroupUuid).setGroupDisabled(this.mIsGroupDisabled).setCarrierId(this.mCarrierId).setProfileClass(this.mProfileClass).setType(this.mType).setGroupOwner(this.mGroupOwner);
        byte[] bArr2 = this.mCarrierConfigAccessRules;
        return groupOwner.setCarrierConfigAccessRules(bArr2.length != 0 ? UiccAccessRule.decodeRules(bArr2) : null).setUiccApplicationsEnabled(this.mAreUiccApplicationsEnabled != 0).setPortIndex(this.mPortIndex).setUsageSetting(this.mUsageSetting).setOnlyNonTerrestrialNetwork(this.mIsOnlyNonTerrestrialNetwork == 1).setServiceCapabilities(SubscriptionManager.getServiceCapabilitiesSet(this.mServiceCapabilities)).setTransferStatus(this.mTransferStatus).setSatelliteESOSSupported(this.mIsSatelliteESOSSupported == 1).setIsPrivateNetwork(this.mIsPrivateNetwork == 1).setStreamingAppMaxDownlinkKbps(this.mStreamingAppMaxDownlinkKbps).setStreamingAppMaxUplinkKbps(this.mStreamingAppMaxUplinkKbps).build();
    }

    public String toString() {
        return "[SubscriptionInfoInternal: id=" + this.mId + " iccId=" + SubscriptionInfo.getPrintableId(this.mIccId) + " simSlotIndex=" + this.mSimSlotIndex + " portIndex=" + this.mPortIndex + " isEmbedded=" + this.mIsEmbedded + " isRemovableEmbedded=" + this.mIsRemovableEmbedded + " carrierId=" + this.mCarrierId + " displayName=" + this.mDisplayName + " carrierName=" + this.mCarrierName + " isOpportunistic=" + this.mIsOpportunistic + " groupUuid=" + this.mGroupUuid + " groupOwner=" + this.mGroupOwner + " displayNameSource=" + SubscriptionManager.displayNameSourceToString(this.mDisplayNameSource) + " iconTint=" + this.mIconTint + " number=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, getNumber()) + " dataRoaming=" + this.mDataRoaming + " mcc=" + this.mMcc + " mnc=" + this.mMnc + " ehplmns=" + this.mEhplmns + " hplmns=" + this.mHplmns + " cardString=" + SubscriptionInfo.getPrintableId(this.mCardString) + " cardId=" + this.mCardId + " nativeAccessRules=" + IccUtils.bytesToHexString(this.mNativeAccessRules) + " carrierConfigAccessRules=" + IccUtils.bytesToHexString(this.mCarrierConfigAccessRules) + " countryIso=" + this.mCountryIso + " profileClass=" + this.mProfileClass + " type=" + SubscriptionManager.subscriptionTypeToString(this.mType) + " areUiccApplicationsEnabled=" + this.mAreUiccApplicationsEnabled + " usageSetting=" + SubscriptionManager.usageSettingToString(this.mUsageSetting) + " isEnhanced4GModeEnabled=" + this.mIsEnhanced4GModeEnabled + " isVideoTelephonyEnabled=" + this.mIsVideoTelephonyEnabled + " isWifiCallingEnabled=" + this.mIsWifiCallingEnabled + " isWifiCallingEnabledForRoaming=" + this.mIsWifiCallingEnabledForRoaming + " wifiCallingMode=" + ImsMmTelManager.wifiCallingModeToString(this.mWifiCallingMode) + " wifiCallingModeForRoaming=" + ImsMmTelManager.wifiCallingModeToString(this.mWifiCallingModeForRoaming) + " enabledMobileDataPolicies=" + this.mEnabledMobileDataPolicies + " imsi=" + SubscriptionInfo.getPrintableId(this.mImsi) + " rcsUceEnabled=" + this.mIsRcsUceEnabled + " crossSimCallingEnabled=" + this.mIsCrossSimCallingEnabled + " rcsConfig=" + IccUtils.bytesToHexString(this.mRcsConfig) + " allowedNetworkTypesForReasons=" + this.mAllowedNetworkTypesForReasons + " deviceToDeviceStatusSharingPreference=" + this.mDeviceToDeviceStatusSharingPreference + " isVoImsOptInEnabled=" + this.mIsVoImsOptInEnabled + " deviceToDeviceStatusSharingContacts=" + this.mDeviceToDeviceStatusSharingContacts + " numberFromCarrier=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mNumberFromCarrier) + " numberFromIms=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mNumberFromIms) + " numberFromTs43=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mNumberFromTs43) + " userId=" + this.mUserId + " isSatelliteEnabled=" + this.mIsSatelliteEnabled + " satellite_attach_enabled_for_carrier=" + this.mIsSatelliteAttachEnabledForCarrier + " getOnlyNonTerrestrialNetwork=" + this.mIsOnlyNonTerrestrialNetwork + " isGroupDisabled=" + this.mIsGroupDisabled + " serviceCapabilities=" + this.mServiceCapabilities + " transferStatus=" + this.mTransferStatus + " satelliteEntitlementStatus=" + this.mIsSatelliteEntitlementStatus + " satelliteEntitlementPlmns=" + this.mSatelliteEntitlementPlmns + " isSatelliteESOSSupported=" + this.mIsSatelliteESOSSupported + " isSatelliteProvisionedForNonIpDatagram=" + this.mIsSatelliteProvisionedForNonIpDatagram + " mSatelliteEntitlementBarredPlmnsList=" + this.mSatelliteEntitlementBarredPlmnsList + " mSatelliteEntitlementDataPlanForPlmn=" + this.mSatelliteEntitlementDataPlanForPlmn + " mSatelliteEntitlementServicesForPlmn=" + this.mSatelliteEntitlementServicesForPlmn + " mSatellitePlmnsDataServicePolicy=" + this.mSatellitePlmnsDataServicePolicy + " mSatellitePlmnsVoiceServicePolicy=" + this.mSatellitePlmnsVoiceServicePolicy + " isPrivateNetwork=" + this.mIsPrivateNetwork + " streamingAppMaxDownlinkKbps=" + this.mStreamingAppMaxDownlinkKbps + " streamingAppMaxUplinkKbps=" + this.mStreamingAppMaxUplinkKbps + "]";
    }

    public boolean equalsDbItemsOnly(SubscriptionInfoInternal subscriptionInfoInternal) {
        return this.mId == subscriptionInfoInternal.mId && this.mSimSlotIndex == subscriptionInfoInternal.mSimSlotIndex && this.mDisplayNameSource == subscriptionInfoInternal.mDisplayNameSource && this.mIconTint == subscriptionInfoInternal.mIconTint && this.mDataRoaming == subscriptionInfoInternal.mDataRoaming && this.mIsEmbedded == subscriptionInfoInternal.mIsEmbedded && this.mIsRemovableEmbedded == subscriptionInfoInternal.mIsRemovableEmbedded && this.mIsExtremeThreatAlertEnabled == subscriptionInfoInternal.mIsExtremeThreatAlertEnabled && this.mIsSevereThreatAlertEnabled == subscriptionInfoInternal.mIsSevereThreatAlertEnabled && this.mIsAmberAlertEnabled == subscriptionInfoInternal.mIsAmberAlertEnabled && this.mIsEmergencyAlertEnabled == subscriptionInfoInternal.mIsEmergencyAlertEnabled && this.mAlertSoundDuration == subscriptionInfoInternal.mAlertSoundDuration && this.mReminderInterval == subscriptionInfoInternal.mReminderInterval && this.mIsAlertVibrationEnabled == subscriptionInfoInternal.mIsAlertVibrationEnabled && this.mIsAlertSpeechEnabled == subscriptionInfoInternal.mIsAlertSpeechEnabled && this.mIsEtwsTestAlertEnabled == subscriptionInfoInternal.mIsEtwsTestAlertEnabled && this.mIsAreaInfoMessageEnabled == subscriptionInfoInternal.mIsAreaInfoMessageEnabled && this.mIsEnhanced4GModeEnabled == subscriptionInfoInternal.mIsEnhanced4GModeEnabled && this.mIsVideoTelephonyEnabled == subscriptionInfoInternal.mIsVideoTelephonyEnabled && this.mIsWifiCallingEnabled == subscriptionInfoInternal.mIsWifiCallingEnabled && this.mWifiCallingMode == subscriptionInfoInternal.mWifiCallingMode && this.mWifiCallingModeForRoaming == subscriptionInfoInternal.mWifiCallingModeForRoaming && this.mIsWifiCallingEnabledForRoaming == subscriptionInfoInternal.mIsWifiCallingEnabledForRoaming && this.mIsOpportunistic == subscriptionInfoInternal.mIsOpportunistic && this.mCarrierId == subscriptionInfoInternal.mCarrierId && this.mProfileClass == subscriptionInfoInternal.mProfileClass && this.mType == subscriptionInfoInternal.mType && this.mAreUiccApplicationsEnabled == subscriptionInfoInternal.mAreUiccApplicationsEnabled && this.mIsRcsUceEnabled == subscriptionInfoInternal.mIsRcsUceEnabled && this.mIsCrossSimCallingEnabled == subscriptionInfoInternal.mIsCrossSimCallingEnabled && this.mDeviceToDeviceStatusSharingPreference == subscriptionInfoInternal.mDeviceToDeviceStatusSharingPreference && this.mIsVoImsOptInEnabled == subscriptionInfoInternal.mIsVoImsOptInEnabled && this.mIsNrAdvancedCallingEnabled == subscriptionInfoInternal.mIsNrAdvancedCallingEnabled && this.mPortIndex == subscriptionInfoInternal.mPortIndex && this.mUsageSetting == subscriptionInfoInternal.mUsageSetting && this.mLastUsedTPMessageReference == subscriptionInfoInternal.mLastUsedTPMessageReference && this.mUserId == subscriptionInfoInternal.mUserId && this.mIsSatelliteEnabled == subscriptionInfoInternal.mIsSatelliteEnabled && this.mIccId.equals(subscriptionInfoInternal.mIccId) && this.mDisplayName.equals(subscriptionInfoInternal.mDisplayName) && this.mCarrierName.equals(subscriptionInfoInternal.mCarrierName) && this.mNumber.equals(subscriptionInfoInternal.mNumber) && this.mMcc.equals(subscriptionInfoInternal.mMcc) && this.mMnc.equals(subscriptionInfoInternal.mMnc) && this.mEhplmns.equals(subscriptionInfoInternal.mEhplmns) && this.mHplmns.equals(subscriptionInfoInternal.mHplmns) && this.mCardString.equals(subscriptionInfoInternal.mCardString) && Arrays.equals(this.mNativeAccessRules, subscriptionInfoInternal.mNativeAccessRules) && Arrays.equals(this.mCarrierConfigAccessRules, subscriptionInfoInternal.mCarrierConfigAccessRules) && this.mGroupUuid.equals(subscriptionInfoInternal.mGroupUuid) && this.mCountryIso.equals(subscriptionInfoInternal.mCountryIso) && this.mGroupOwner.equals(subscriptionInfoInternal.mGroupOwner) && this.mEnabledMobileDataPolicies.equals(subscriptionInfoInternal.mEnabledMobileDataPolicies) && this.mImsi.equals(subscriptionInfoInternal.mImsi) && Arrays.equals(this.mRcsConfig, subscriptionInfoInternal.mRcsConfig) && this.mAllowedNetworkTypesForReasons.equals(subscriptionInfoInternal.mAllowedNetworkTypesForReasons) && this.mDeviceToDeviceStatusSharingContacts.equals(subscriptionInfoInternal.mDeviceToDeviceStatusSharingContacts) && this.mNumberFromCarrier.equals(subscriptionInfoInternal.mNumberFromCarrier) && this.mNumberFromIms.equals(subscriptionInfoInternal.mNumberFromIms) && this.mNumberFromTs43.equals(subscriptionInfoInternal.mNumberFromTs43) && this.mIsSatelliteAttachEnabledForCarrier == subscriptionInfoInternal.mIsSatelliteAttachEnabledForCarrier && this.mIsOnlyNonTerrestrialNetwork == subscriptionInfoInternal.mIsOnlyNonTerrestrialNetwork && this.mServiceCapabilities == subscriptionInfoInternal.mServiceCapabilities && this.mTransferStatus == subscriptionInfoInternal.mTransferStatus && this.mIsSatelliteEntitlementStatus == subscriptionInfoInternal.mIsSatelliteEntitlementStatus && this.mSatelliteEntitlementPlmns.equals(subscriptionInfoInternal.mSatelliteEntitlementPlmns) && this.mIsSatelliteESOSSupported == subscriptionInfoInternal.mIsSatelliteESOSSupported && this.mIsSatelliteProvisionedForNonIpDatagram == subscriptionInfoInternal.mIsSatelliteProvisionedForNonIpDatagram && this.mSatelliteEntitlementBarredPlmnsList.equals(subscriptionInfoInternal.mSatelliteEntitlementBarredPlmnsList) && this.mSatelliteEntitlementDataPlanForPlmn.equals(subscriptionInfoInternal.mSatelliteEntitlementDataPlanForPlmn) && this.mSatelliteEntitlementServicesForPlmn.equals(subscriptionInfoInternal.mSatelliteEntitlementServicesForPlmn) && this.mSatellitePlmnsDataServicePolicy.equals(subscriptionInfoInternal.mSatellitePlmnsDataServicePolicy) && this.mSatellitePlmnsVoiceServicePolicy.equals(subscriptionInfoInternal.mSatellitePlmnsVoiceServicePolicy) && this.mIsPrivateNetwork == subscriptionInfoInternal.mIsPrivateNetwork && this.mStreamingAppMaxDownlinkKbps == subscriptionInfoInternal.mStreamingAppMaxDownlinkKbps && this.mStreamingAppMaxUplinkKbps == subscriptionInfoInternal.mStreamingAppMaxUplinkKbps;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SubscriptionInfoInternal subscriptionInfoInternal = (SubscriptionInfoInternal) obj;
            if (equalsDbItemsOnly(subscriptionInfoInternal) && this.mCardId == subscriptionInfoInternal.mCardId && this.mIsGroupDisabled == subscriptionInfoInternal.mIsGroupDisabled) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (((((Objects.hash(Integer.valueOf(this.mId), this.mIccId, Integer.valueOf(this.mSimSlotIndex), this.mDisplayName, this.mCarrierName, Integer.valueOf(this.mDisplayNameSource), Integer.valueOf(this.mIconTint), this.mNumber, Integer.valueOf(this.mDataRoaming), this.mMcc, this.mMnc, this.mEhplmns, this.mHplmns, Integer.valueOf(this.mIsEmbedded), this.mCardString, Integer.valueOf(this.mIsRemovableEmbedded), Integer.valueOf(this.mIsExtremeThreatAlertEnabled), Integer.valueOf(this.mIsSevereThreatAlertEnabled), Integer.valueOf(this.mIsAmberAlertEnabled), Integer.valueOf(this.mIsEmergencyAlertEnabled), Integer.valueOf(this.mAlertSoundDuration), Integer.valueOf(this.mReminderInterval), Integer.valueOf(this.mIsAlertVibrationEnabled), Integer.valueOf(this.mIsAlertSpeechEnabled), Integer.valueOf(this.mIsEtwsTestAlertEnabled), Integer.valueOf(this.mIsAreaInfoMessageEnabled), Integer.valueOf(this.mIsEnhanced4GModeEnabled), Integer.valueOf(this.mIsVideoTelephonyEnabled), Integer.valueOf(this.mIsWifiCallingEnabled), Integer.valueOf(this.mWifiCallingMode), Integer.valueOf(this.mWifiCallingModeForRoaming), Integer.valueOf(this.mIsWifiCallingEnabledForRoaming), Integer.valueOf(this.mIsOpportunistic), this.mGroupUuid, this.mCountryIso, Integer.valueOf(this.mCarrierId), Integer.valueOf(this.mProfileClass), Integer.valueOf(this.mType), this.mGroupOwner, this.mEnabledMobileDataPolicies, this.mImsi, Integer.valueOf(this.mAreUiccApplicationsEnabled), Integer.valueOf(this.mIsRcsUceEnabled), Integer.valueOf(this.mIsCrossSimCallingEnabled), this.mAllowedNetworkTypesForReasons, Integer.valueOf(this.mDeviceToDeviceStatusSharingPreference), Integer.valueOf(this.mIsVoImsOptInEnabled), this.mDeviceToDeviceStatusSharingContacts, Integer.valueOf(this.mIsNrAdvancedCallingEnabled), this.mNumberFromCarrier, this.mNumberFromIms, this.mNumberFromTs43, Integer.valueOf(this.mPortIndex), Integer.valueOf(this.mUsageSetting), Integer.valueOf(this.mLastUsedTPMessageReference), Integer.valueOf(this.mUserId), Integer.valueOf(this.mIsSatelliteEnabled), Integer.valueOf(this.mCardId), Boolean.valueOf(this.mIsGroupDisabled), Integer.valueOf(this.mIsSatelliteAttachEnabledForCarrier), Integer.valueOf(this.mIsOnlyNonTerrestrialNetwork), Integer.valueOf(this.mServiceCapabilities), Integer.valueOf(this.mTransferStatus), Integer.valueOf(this.mIsSatelliteEntitlementStatus), this.mSatelliteEntitlementPlmns, Integer.valueOf(this.mIsSatelliteESOSSupported), Integer.valueOf(this.mIsSatelliteProvisionedForNonIpDatagram), this.mSatelliteEntitlementBarredPlmnsList, this.mSatelliteEntitlementDataPlanForPlmn, this.mSatelliteEntitlementServicesForPlmn, this.mSatellitePlmnsDataServicePolicy, this.mSatellitePlmnsVoiceServicePolicy, Integer.valueOf(this.mIsPrivateNetwork), Long.valueOf(this.mStreamingAppMaxDownlinkKbps), Long.valueOf(this.mStreamingAppMaxUplinkKbps)) * 31) + Arrays.hashCode(this.mNativeAccessRules)) * 31) + Arrays.hashCode(this.mCarrierConfigAccessRules)) * 31) + Arrays.hashCode(this.mRcsConfig);
    }

    public static class Builder {
        private int mAlertSoundDuration;
        private String mAllowedNetworkTypesForReasons;
        private int mAreUiccApplicationsEnabled;
        private int mCardId;
        private String mCardString;
        private byte[] mCarrierConfigAccessRules;
        private int mCarrierId;
        private String mCarrierName;
        private String mCountryIso;
        private int mDataRoaming;
        private String mDeviceToDeviceStatusSharingContacts;
        private int mDeviceToDeviceStatusSharingPreference;
        private String mDisplayName;
        private int mDisplayNameSource;
        private String mEhplmns;
        private String mEnabledMobileDataPolicies;
        private String mGroupOwner;
        private String mGroupUuid;
        private String mHplmns;
        private String mIccId;
        private int mIconTint;
        private int mId;
        private String mImsi;
        private int mIsAlertSpeechEnabled;
        private int mIsAlertVibrationEnabled;
        private int mIsAmberAlertEnabled;
        private int mIsAreaInfoMessageEnabled;
        private int mIsCrossSimCallingEnabled;
        private int mIsEmbedded;
        private int mIsEmergencyAlertEnabled;
        private int mIsEnhanced4GModeEnabled;
        private int mIsEtwsTestAlertEnabled;
        private int mIsExtremeThreatAlertEnabled;
        private boolean mIsGroupDisabled;
        private int mIsNrAdvancedCallingEnabled;
        private int mIsOnlyNonTerrestrialNetwork;
        private int mIsOpportunistic;
        private int mIsOptOutDialogEnabled;
        private int mIsPrivateNetwork;
        private int mIsRcsUceEnabled;
        private int mIsRemovableEmbedded;
        private int mIsSatelliteAttachEnabledForCarrier;
        private int mIsSatelliteESOSSupported;
        private int mIsSatelliteEnabled;
        private int mIsSatelliteEntitlementStatus;
        private int mIsSatelliteProvisionedForNonIpDatagram;
        private int mIsSevereThreatAlertEnabled;
        private int mIsTestAlertEnabled;
        private int mIsVideoTelephonyEnabled;
        private int mIsVoImsOptInEnabled;
        private int mIsWifiCallingEnabled;
        private int mIsWifiCallingEnabledForRoaming;
        private int mLastUsedTPMessageReference;
        private String mMcc;
        private String mMnc;
        private byte[] mNativeAccessRules;
        private String mNumber;
        private String mNumberFromCarrier;
        private String mNumberFromIms;
        private String mNumberFromTs43;
        private int mPortIndex;
        private int mProfileClass;
        private byte[] mRcsConfig;
        private int mReminderInterval;
        private String mSatelliteEntitlementBarredPlmnsList;
        private String mSatelliteEntitlementDataPlanForPlmn;
        private String mSatelliteEntitlementPlmns;
        private String mSatelliteEntitlementServicesForPlmn;
        private String mSatellitePlmnsDataServicePolicy;
        private String mSatellitePlmnsVoiceServicePolicy;
        private int mServiceCapabilities;
        private int mSimSlotIndex;
        private long mStreamingAppMaxDownlinkKbps;
        private long mStreamingAppMaxUplinkKbps;
        private int mTransferStatus;
        private int mType;
        private int mUsageSetting;
        private int mUserId;
        private int mWifiCallingMode;
        private int mWifiCallingModeForRoaming;

        public Builder() {
            this.mId = -1;
            this.mIccId = "";
            this.mSimSlotIndex = -1;
            this.mDisplayName = "";
            this.mCarrierName = "";
            this.mDisplayNameSource = -1;
            this.mIconTint = 0;
            this.mNumber = "";
            this.mDataRoaming = 0;
            this.mMcc = "";
            this.mMnc = "";
            this.mEhplmns = "";
            this.mHplmns = "";
            this.mIsEmbedded = 0;
            this.mCardString = "";
            this.mNativeAccessRules = new byte[0];
            this.mCarrierConfigAccessRules = new byte[0];
            this.mIsRemovableEmbedded = 0;
            this.mIsExtremeThreatAlertEnabled = 1;
            this.mIsSevereThreatAlertEnabled = 1;
            this.mIsAmberAlertEnabled = 1;
            this.mIsEmergencyAlertEnabled = 1;
            this.mAlertSoundDuration = 4;
            this.mReminderInterval = 0;
            this.mIsAlertVibrationEnabled = 1;
            this.mIsAlertSpeechEnabled = 1;
            this.mIsEtwsTestAlertEnabled = 0;
            this.mIsAreaInfoMessageEnabled = 1;
            this.mIsTestAlertEnabled = 0;
            this.mIsOptOutDialogEnabled = 1;
            this.mIsEnhanced4GModeEnabled = -1;
            this.mIsVideoTelephonyEnabled = -1;
            this.mIsWifiCallingEnabled = -1;
            this.mWifiCallingMode = -1;
            this.mWifiCallingModeForRoaming = -1;
            this.mIsWifiCallingEnabledForRoaming = -1;
            this.mIsOpportunistic = 0;
            this.mGroupUuid = "";
            this.mCountryIso = "";
            this.mCarrierId = -1;
            this.mProfileClass = -1;
            this.mType = 0;
            this.mGroupOwner = "";
            this.mEnabledMobileDataPolicies = "";
            this.mImsi = "";
            this.mAreUiccApplicationsEnabled = 1;
            this.mIsRcsUceEnabled = 0;
            this.mIsCrossSimCallingEnabled = 0;
            this.mRcsConfig = new byte[0];
            this.mAllowedNetworkTypesForReasons = "";
            this.mDeviceToDeviceStatusSharingPreference = 0;
            this.mIsVoImsOptInEnabled = 0;
            this.mDeviceToDeviceStatusSharingContacts = "";
            this.mIsNrAdvancedCallingEnabled = -1;
            this.mNumberFromCarrier = "";
            this.mNumberFromIms = "";
            this.mNumberFromTs43 = "";
            this.mPortIndex = -1;
            this.mUsageSetting = -1;
            this.mLastUsedTPMessageReference = -1;
            this.mUserId = -10000;
            this.mIsSatelliteEnabled = 0;
            this.mIsSatelliteAttachEnabledForCarrier = -1;
            this.mIsOnlyNonTerrestrialNetwork = 0;
            this.mCardId = -2;
            this.mIsSatelliteEntitlementStatus = 0;
            this.mSatelliteEntitlementPlmns = "";
            this.mIsSatelliteESOSSupported = 0;
            this.mIsSatelliteProvisionedForNonIpDatagram = 0;
            this.mSatelliteEntitlementBarredPlmnsList = "";
            this.mSatelliteEntitlementDataPlanForPlmn = "";
            this.mSatelliteEntitlementServicesForPlmn = "";
            this.mSatellitePlmnsDataServicePolicy = "";
            this.mSatellitePlmnsVoiceServicePolicy = "";
            this.mIsPrivateNetwork = 0;
            this.mStreamingAppMaxDownlinkKbps = -1L;
            this.mStreamingAppMaxUplinkKbps = -1L;
        }

        public Builder(SubscriptionInfoInternal subscriptionInfoInternal) {
            this.mId = -1;
            this.mIccId = "";
            this.mSimSlotIndex = -1;
            this.mDisplayName = "";
            this.mCarrierName = "";
            this.mDisplayNameSource = -1;
            this.mIconTint = 0;
            this.mNumber = "";
            this.mDataRoaming = 0;
            this.mMcc = "";
            this.mMnc = "";
            this.mEhplmns = "";
            this.mHplmns = "";
            this.mIsEmbedded = 0;
            this.mCardString = "";
            this.mNativeAccessRules = new byte[0];
            this.mCarrierConfigAccessRules = new byte[0];
            this.mIsRemovableEmbedded = 0;
            this.mIsExtremeThreatAlertEnabled = 1;
            this.mIsSevereThreatAlertEnabled = 1;
            this.mIsAmberAlertEnabled = 1;
            this.mIsEmergencyAlertEnabled = 1;
            this.mAlertSoundDuration = 4;
            this.mReminderInterval = 0;
            this.mIsAlertVibrationEnabled = 1;
            this.mIsAlertSpeechEnabled = 1;
            this.mIsEtwsTestAlertEnabled = 0;
            this.mIsAreaInfoMessageEnabled = 1;
            this.mIsTestAlertEnabled = 0;
            this.mIsOptOutDialogEnabled = 1;
            this.mIsEnhanced4GModeEnabled = -1;
            this.mIsVideoTelephonyEnabled = -1;
            this.mIsWifiCallingEnabled = -1;
            this.mWifiCallingMode = -1;
            this.mWifiCallingModeForRoaming = -1;
            this.mIsWifiCallingEnabledForRoaming = -1;
            this.mIsOpportunistic = 0;
            this.mGroupUuid = "";
            this.mCountryIso = "";
            this.mCarrierId = -1;
            this.mProfileClass = -1;
            this.mType = 0;
            this.mGroupOwner = "";
            this.mEnabledMobileDataPolicies = "";
            this.mImsi = "";
            this.mAreUiccApplicationsEnabled = 1;
            this.mIsRcsUceEnabled = 0;
            this.mIsCrossSimCallingEnabled = 0;
            this.mRcsConfig = new byte[0];
            this.mAllowedNetworkTypesForReasons = "";
            this.mDeviceToDeviceStatusSharingPreference = 0;
            this.mIsVoImsOptInEnabled = 0;
            this.mDeviceToDeviceStatusSharingContacts = "";
            this.mIsNrAdvancedCallingEnabled = -1;
            this.mNumberFromCarrier = "";
            this.mNumberFromIms = "";
            this.mNumberFromTs43 = "";
            this.mPortIndex = -1;
            this.mUsageSetting = -1;
            this.mLastUsedTPMessageReference = -1;
            this.mUserId = -10000;
            this.mIsSatelliteEnabled = 0;
            this.mIsSatelliteAttachEnabledForCarrier = -1;
            this.mIsOnlyNonTerrestrialNetwork = 0;
            this.mCardId = -2;
            this.mIsSatelliteEntitlementStatus = 0;
            this.mSatelliteEntitlementPlmns = "";
            this.mIsSatelliteESOSSupported = 0;
            this.mIsSatelliteProvisionedForNonIpDatagram = 0;
            this.mSatelliteEntitlementBarredPlmnsList = "";
            this.mSatelliteEntitlementDataPlanForPlmn = "";
            this.mSatelliteEntitlementServicesForPlmn = "";
            this.mSatellitePlmnsDataServicePolicy = "";
            this.mSatellitePlmnsVoiceServicePolicy = "";
            this.mIsPrivateNetwork = 0;
            this.mStreamingAppMaxDownlinkKbps = -1L;
            this.mStreamingAppMaxUplinkKbps = -1L;
            this.mId = subscriptionInfoInternal.mId;
            this.mIccId = subscriptionInfoInternal.mIccId;
            this.mSimSlotIndex = subscriptionInfoInternal.mSimSlotIndex;
            this.mDisplayName = subscriptionInfoInternal.mDisplayName;
            this.mCarrierName = subscriptionInfoInternal.mCarrierName;
            this.mDisplayNameSource = subscriptionInfoInternal.mDisplayNameSource;
            this.mIconTint = subscriptionInfoInternal.mIconTint;
            this.mNumber = subscriptionInfoInternal.mNumber;
            this.mDataRoaming = subscriptionInfoInternal.mDataRoaming;
            this.mMcc = subscriptionInfoInternal.mMcc;
            this.mMnc = subscriptionInfoInternal.mMnc;
            this.mEhplmns = subscriptionInfoInternal.mEhplmns;
            this.mHplmns = subscriptionInfoInternal.mHplmns;
            this.mIsEmbedded = subscriptionInfoInternal.mIsEmbedded;
            this.mCardString = subscriptionInfoInternal.mCardString;
            this.mNativeAccessRules = subscriptionInfoInternal.mNativeAccessRules;
            this.mCarrierConfigAccessRules = subscriptionInfoInternal.mCarrierConfigAccessRules;
            this.mIsRemovableEmbedded = subscriptionInfoInternal.mIsRemovableEmbedded;
            this.mIsExtremeThreatAlertEnabled = subscriptionInfoInternal.mIsExtremeThreatAlertEnabled;
            this.mIsSevereThreatAlertEnabled = subscriptionInfoInternal.mIsSevereThreatAlertEnabled;
            this.mIsAmberAlertEnabled = subscriptionInfoInternal.mIsAmberAlertEnabled;
            this.mIsEmergencyAlertEnabled = subscriptionInfoInternal.mIsEmergencyAlertEnabled;
            this.mAlertSoundDuration = subscriptionInfoInternal.mAlertSoundDuration;
            this.mReminderInterval = subscriptionInfoInternal.mReminderInterval;
            this.mIsAlertVibrationEnabled = subscriptionInfoInternal.mIsAlertVibrationEnabled;
            this.mIsAlertSpeechEnabled = subscriptionInfoInternal.mIsAlertSpeechEnabled;
            this.mIsEtwsTestAlertEnabled = subscriptionInfoInternal.mIsEtwsTestAlertEnabled;
            this.mIsAreaInfoMessageEnabled = subscriptionInfoInternal.mIsAreaInfoMessageEnabled;
            this.mIsTestAlertEnabled = subscriptionInfoInternal.mIsTestAlertEnabled;
            this.mIsOptOutDialogEnabled = subscriptionInfoInternal.mIsOptOutDialogEnabled;
            this.mIsEnhanced4GModeEnabled = subscriptionInfoInternal.mIsEnhanced4GModeEnabled;
            this.mIsVideoTelephonyEnabled = subscriptionInfoInternal.mIsVideoTelephonyEnabled;
            this.mIsWifiCallingEnabled = subscriptionInfoInternal.mIsWifiCallingEnabled;
            this.mWifiCallingMode = subscriptionInfoInternal.mWifiCallingMode;
            this.mWifiCallingModeForRoaming = subscriptionInfoInternal.mWifiCallingModeForRoaming;
            this.mIsWifiCallingEnabledForRoaming = subscriptionInfoInternal.mIsWifiCallingEnabledForRoaming;
            this.mIsOpportunistic = subscriptionInfoInternal.mIsOpportunistic;
            this.mGroupUuid = subscriptionInfoInternal.mGroupUuid;
            this.mCountryIso = subscriptionInfoInternal.mCountryIso;
            this.mCarrierId = subscriptionInfoInternal.mCarrierId;
            this.mProfileClass = subscriptionInfoInternal.mProfileClass;
            this.mType = subscriptionInfoInternal.mType;
            this.mGroupOwner = subscriptionInfoInternal.mGroupOwner;
            this.mEnabledMobileDataPolicies = subscriptionInfoInternal.mEnabledMobileDataPolicies;
            this.mImsi = subscriptionInfoInternal.mImsi;
            this.mAreUiccApplicationsEnabled = subscriptionInfoInternal.mAreUiccApplicationsEnabled;
            this.mIsRcsUceEnabled = subscriptionInfoInternal.mIsRcsUceEnabled;
            this.mIsCrossSimCallingEnabled = subscriptionInfoInternal.mIsCrossSimCallingEnabled;
            this.mRcsConfig = subscriptionInfoInternal.mRcsConfig;
            this.mAllowedNetworkTypesForReasons = subscriptionInfoInternal.mAllowedNetworkTypesForReasons;
            this.mDeviceToDeviceStatusSharingPreference = subscriptionInfoInternal.mDeviceToDeviceStatusSharingPreference;
            this.mIsVoImsOptInEnabled = subscriptionInfoInternal.mIsVoImsOptInEnabled;
            this.mDeviceToDeviceStatusSharingContacts = subscriptionInfoInternal.mDeviceToDeviceStatusSharingContacts;
            this.mIsNrAdvancedCallingEnabled = subscriptionInfoInternal.mIsNrAdvancedCallingEnabled;
            this.mNumberFromCarrier = subscriptionInfoInternal.mNumberFromCarrier;
            this.mNumberFromIms = subscriptionInfoInternal.mNumberFromIms;
            this.mNumberFromTs43 = subscriptionInfoInternal.mNumberFromTs43;
            this.mPortIndex = subscriptionInfoInternal.mPortIndex;
            this.mUsageSetting = subscriptionInfoInternal.mUsageSetting;
            this.mLastUsedTPMessageReference = subscriptionInfoInternal.getLastUsedTPMessageReference();
            this.mUserId = subscriptionInfoInternal.mUserId;
            this.mIsSatelliteEnabled = subscriptionInfoInternal.mIsSatelliteEnabled;
            this.mIsSatelliteAttachEnabledForCarrier = subscriptionInfoInternal.mIsSatelliteAttachEnabledForCarrier;
            this.mIsOnlyNonTerrestrialNetwork = subscriptionInfoInternal.mIsOnlyNonTerrestrialNetwork;
            this.mCardId = subscriptionInfoInternal.mCardId;
            this.mIsGroupDisabled = subscriptionInfoInternal.mIsGroupDisabled;
            this.mServiceCapabilities = subscriptionInfoInternal.mServiceCapabilities;
            this.mTransferStatus = subscriptionInfoInternal.mTransferStatus;
            this.mIsSatelliteEntitlementStatus = subscriptionInfoInternal.mIsSatelliteEntitlementStatus;
            this.mSatelliteEntitlementPlmns = subscriptionInfoInternal.mSatelliteEntitlementPlmns;
            this.mIsSatelliteESOSSupported = subscriptionInfoInternal.mIsSatelliteESOSSupported;
            this.mIsSatelliteProvisionedForNonIpDatagram = subscriptionInfoInternal.mIsSatelliteProvisionedForNonIpDatagram;
            this.mSatelliteEntitlementBarredPlmnsList = subscriptionInfoInternal.mSatelliteEntitlementBarredPlmnsList;
            this.mSatelliteEntitlementDataPlanForPlmn = subscriptionInfoInternal.mSatelliteEntitlementDataPlanForPlmn;
            this.mSatelliteEntitlementServicesForPlmn = subscriptionInfoInternal.mSatelliteEntitlementServicesForPlmn;
            this.mSatellitePlmnsDataServicePolicy = subscriptionInfoInternal.mSatellitePlmnsDataServicePolicy;
            this.mSatellitePlmnsVoiceServicePolicy = subscriptionInfoInternal.mSatellitePlmnsVoiceServicePolicy;
            this.mIsPrivateNetwork = subscriptionInfoInternal.mIsPrivateNetwork;
            this.mStreamingAppMaxDownlinkKbps = subscriptionInfoInternal.mStreamingAppMaxDownlinkKbps;
            this.mStreamingAppMaxUplinkKbps = subscriptionInfoInternal.mStreamingAppMaxUplinkKbps;
        }

        public Builder setId(int i) {
            this.mId = i;
            return this;
        }

        public Builder setIccId(String str) {
            Objects.requireNonNull(str);
            this.mIccId = str;
            return this;
        }

        public Builder setSimSlotIndex(int i) {
            this.mSimSlotIndex = i;
            return this;
        }

        public Builder setDisplayName(String str) {
            Objects.requireNonNull(str);
            this.mDisplayName = str;
            return this;
        }

        public Builder setCarrierName(String str) {
            Objects.requireNonNull(str);
            this.mCarrierName = str;
            return this;
        }

        public Builder setDisplayNameSource(int i) {
            this.mDisplayNameSource = i;
            return this;
        }

        public Builder setIconTint(int i) {
            this.mIconTint = i;
            return this;
        }

        public Builder setNumber(String str) {
            Objects.requireNonNull(str);
            this.mNumber = str;
            return this;
        }

        public Builder setDataRoaming(int i) {
            this.mDataRoaming = i;
            return this;
        }

        public Builder setMcc(String str) {
            Objects.requireNonNull(str);
            this.mMcc = str;
            return this;
        }

        public Builder setMnc(String str) {
            Objects.requireNonNull(str);
            this.mMnc = str;
            return this;
        }

        public Builder setEhplmns(String str) {
            Objects.requireNonNull(str);
            this.mEhplmns = str;
            return this;
        }

        public Builder setHplmns(String str) {
            Objects.requireNonNull(str);
            this.mHplmns = str;
            return this;
        }

        public Builder setEmbedded(int i) {
            this.mIsEmbedded = i;
            return this;
        }

        public Builder setCardString(String str) {
            Objects.requireNonNull(str);
            this.mCardString = str;
            return this;
        }

        public Builder setNativeAccessRules(byte[] bArr) {
            Objects.requireNonNull(bArr);
            this.mNativeAccessRules = bArr;
            return this;
        }

        public Builder setNativeAccessRules(List<UiccAccessRule> list) {
            Objects.requireNonNull(list);
            if (!list.isEmpty()) {
                this.mNativeAccessRules = UiccAccessRule.encodeRules((UiccAccessRule[]) list.toArray(new UiccAccessRule[0]));
            }
            return this;
        }

        public Builder setCarrierConfigAccessRules(byte[] bArr) {
            Objects.requireNonNull(bArr);
            this.mCarrierConfigAccessRules = bArr;
            return this;
        }

        public Builder setRemovableEmbedded(boolean z) {
            this.mIsRemovableEmbedded = z ? 1 : 0;
            return this;
        }

        public Builder setRemovableEmbedded(int i) {
            this.mIsRemovableEmbedded = i;
            return this;
        }

        public Builder setCellBroadcastExtremeThreatAlertEnabled(int i) {
            this.mIsExtremeThreatAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastSevereThreatAlertEnabled(int i) {
            this.mIsSevereThreatAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastAmberAlertEnabled(int i) {
            this.mIsAmberAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastEmergencyAlertEnabled(int i) {
            this.mIsEmergencyAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastAlertSoundDuration(int i) {
            this.mAlertSoundDuration = i;
            return this;
        }

        public Builder setCellBroadcastAlertReminderInterval(int i) {
            this.mReminderInterval = i;
            return this;
        }

        public Builder setCellBroadcastAlertVibrationEnabled(int i) {
            this.mIsAlertVibrationEnabled = i;
            return this;
        }

        public Builder setCellBroadcastAlertSpeechEnabled(int i) {
            this.mIsAlertSpeechEnabled = i;
            return this;
        }

        public Builder setCellBroadcastEtwsTestAlertEnabled(int i) {
            this.mIsEtwsTestAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastAreaInfoMessageEnabled(int i) {
            this.mIsAreaInfoMessageEnabled = i;
            return this;
        }

        public Builder setCellBroadcastTestAlertEnabled(int i) {
            this.mIsTestAlertEnabled = i;
            return this;
        }

        public Builder setCellBroadcastOptOutDialogEnabled(int i) {
            this.mIsOptOutDialogEnabled = i;
            return this;
        }

        public Builder setEnhanced4GModeEnabled(int i) {
            this.mIsEnhanced4GModeEnabled = i;
            return this;
        }

        public Builder setVideoTelephonyEnabled(int i) {
            this.mIsVideoTelephonyEnabled = i;
            return this;
        }

        public Builder setWifiCallingEnabled(int i) {
            this.mIsWifiCallingEnabled = i;
            return this;
        }

        public Builder setWifiCallingMode(int i) {
            this.mWifiCallingMode = i;
            return this;
        }

        public Builder setWifiCallingModeForRoaming(int i) {
            this.mWifiCallingModeForRoaming = i;
            return this;
        }

        public Builder setWifiCallingEnabledForRoaming(int i) {
            this.mIsWifiCallingEnabledForRoaming = i;
            return this;
        }

        public Builder setOpportunistic(int i) {
            this.mIsOpportunistic = i;
            return this;
        }

        public Builder setGroupUuid(String str) {
            Objects.requireNonNull(str);
            this.mGroupUuid = str;
            return this;
        }

        public Builder setCountryIso(String str) {
            Objects.requireNonNull(str);
            this.mCountryIso = str;
            return this;
        }

        public Builder setCarrierId(int i) {
            this.mCarrierId = i;
            return this;
        }

        public Builder setProfileClass(int i) {
            this.mProfileClass = i;
            return this;
        }

        public Builder setType(int i) {
            this.mType = i;
            return this;
        }

        public Builder setGroupOwner(String str) {
            Objects.requireNonNull(str);
            this.mGroupOwner = str;
            return this;
        }

        public Builder setEnabledMobileDataPolicies(String str) {
            Objects.requireNonNull(str);
            this.mEnabledMobileDataPolicies = str;
            return this;
        }

        public Builder setImsi(String str) {
            Objects.requireNonNull(str);
            this.mImsi = str;
            return this;
        }

        public Builder setUiccApplicationsEnabled(int i) {
            this.mAreUiccApplicationsEnabled = i;
            return this;
        }

        public Builder setRcsUceEnabled(int i) {
            this.mIsRcsUceEnabled = i;
            return this;
        }

        public Builder setCrossSimCallingEnabled(int i) {
            this.mIsCrossSimCallingEnabled = i;
            return this;
        }

        public Builder setRcsConfig(byte[] bArr) {
            Objects.requireNonNull(bArr);
            this.mRcsConfig = bArr;
            return this;
        }

        public Builder setAllowedNetworkTypesForReasons(String str) {
            Objects.requireNonNull(str);
            this.mAllowedNetworkTypesForReasons = str;
            return this;
        }

        public Builder setDeviceToDeviceStatusSharingPreference(int i) {
            this.mDeviceToDeviceStatusSharingPreference = i;
            return this;
        }

        public Builder setVoImsOptInEnabled(int i) {
            this.mIsVoImsOptInEnabled = i;
            return this;
        }

        public Builder setDeviceToDeviceStatusSharingContacts(String str) {
            Objects.requireNonNull(str);
            this.mDeviceToDeviceStatusSharingContacts = str;
            return this;
        }

        public Builder setNrAdvancedCallingEnabled(int i) {
            this.mIsNrAdvancedCallingEnabled = i;
            return this;
        }

        public Builder setNumberFromCarrier(String str) {
            Objects.requireNonNull(str);
            this.mNumberFromCarrier = str;
            return this;
        }

        public Builder setNumberFromIms(String str) {
            Objects.requireNonNull(str);
            this.mNumberFromIms = str;
            return this;
        }

        public Builder setNumberFromTs43(String str) {
            Objects.requireNonNull(str);
            this.mNumberFromTs43 = str;
            return this;
        }

        public Builder setPortIndex(int i) {
            this.mPortIndex = i;
            return this;
        }

        public Builder setUsageSetting(int i) {
            this.mUsageSetting = i;
            return this;
        }

        public Builder setLastUsedTPMessageReference(int i) {
            this.mLastUsedTPMessageReference = i;
            return this;
        }

        public Builder setUserId(int i) {
            this.mUserId = i;
            return this;
        }

        public Builder setSatelliteEnabled(int i) {
            this.mIsSatelliteEnabled = i;
            return this;
        }

        public Builder setSatelliteAttachEnabledForCarrier(int i) {
            this.mIsSatelliteAttachEnabledForCarrier = i;
            return this;
        }

        public Builder setOnlyNonTerrestrialNetwork(int i) {
            this.mIsOnlyNonTerrestrialNetwork = i;
            return this;
        }

        public Builder setCardId(int i) {
            this.mCardId = i;
            return this;
        }

        public Builder setGroupDisabled(boolean z) {
            this.mIsGroupDisabled = z;
            return this;
        }

        public Builder setServiceCapabilities(int i) {
            this.mServiceCapabilities = i;
            return this;
        }

        public Builder setTransferStatus(int i) {
            this.mTransferStatus = i;
            return this;
        }

        public Builder setSatelliteEntitlementStatus(int i) {
            this.mIsSatelliteEntitlementStatus = i;
            return this;
        }

        public Builder setSatelliteEntitlementPlmns(String str) {
            this.mSatelliteEntitlementPlmns = str;
            return this;
        }

        public Builder setSatelliteESOSSupported(int i) {
            this.mIsSatelliteESOSSupported = i;
            return this;
        }

        public Builder setIsSatelliteProvisionedForNonIpDatagram(int i) {
            this.mIsSatelliteProvisionedForNonIpDatagram = i;
            return this;
        }

        public Builder setStreamingAppMaxDownlinkKbps(long j) {
            this.mStreamingAppMaxDownlinkKbps = j;
            return this;
        }

        public Builder setStreamingAppMaxUplinkKbps(long j) {
            this.mStreamingAppMaxUplinkKbps = j;
            return this;
        }

        public SubscriptionInfoInternal build() {
            return new SubscriptionInfoInternal(this);
        }

        public Builder setSatelliteEntitlementBarredPlmnsList(String str) {
            this.mSatelliteEntitlementBarredPlmnsList = str;
            return this;
        }

        public Builder setSatelliteEntitlementDataPlanForPlmns(String str) {
            this.mSatelliteEntitlementDataPlanForPlmn = str;
            return this;
        }

        public Builder setSatelliteEntitlementPlmnServiceTypes(String str) {
            this.mSatelliteEntitlementServicesForPlmn = str;
            return this;
        }

        public Builder setSatellitePlmnsDataServicePolicy(String str) {
            this.mSatellitePlmnsDataServicePolicy = str;
            return this;
        }

        public Builder setSatellitePlmnsVoiceServicePolicy(String str) {
            this.mSatellitePlmnsVoiceServicePolicy = str;
            return this;
        }

        public Builder setIsPrivateNetwork(int i) {
            this.mIsPrivateNetwork = i;
            return this;
        }
    }
}

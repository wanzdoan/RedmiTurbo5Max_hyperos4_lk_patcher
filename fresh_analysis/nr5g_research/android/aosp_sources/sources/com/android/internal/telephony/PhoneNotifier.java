package com.android.internal.telephony;

import android.telephony.BarringInfo;
import android.telephony.CallQuality;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.CellularIdentifierDisclosure;
import android.telephony.LinkCapacityEstimate;
import android.telephony.NetworkSecurityEvent;
import android.telephony.PhoneCapability;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseDataConnectionState;
import android.telephony.SecurityAlgorithmUpdate;
import android.telephony.ServiceState;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.MediaQualityStatus;
import android.telephony.satellite.NtnSignalStrength;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface PhoneNotifier {
    void notifyAllowedNetworkTypesChanged(Phone phone, int i, long j);

    void notifyBarringInfoChanged(Phone phone, BarringInfo barringInfo);

    void notifyCallForwardingChanged(Phone phone);

    void notifyCallQualityChanged(Phone phone, CallQuality callQuality, int i);

    void notifyCallbackModeRestarted(Phone phone, int i, long j);

    void notifyCallbackModeStarted(Phone phone, int i, long j);

    void notifyCallbackModeStopped(Phone phone, int i, int i2);

    void notifyCarrierRoamingNtnAvailableServicesChanged(Phone phone, int[] iArr);

    void notifyCarrierRoamingNtnEligibleStateChanged(Phone phone, boolean z);

    void notifyCarrierRoamingNtnModeChanged(Phone phone, boolean z);

    void notifyCarrierRoamingNtnSignalStrengthChanged(Phone phone, NtnSignalStrength ntnSignalStrength);

    void notifyCellInfo(Phone phone, List<CellInfo> list);

    void notifyCellLocation(Phone phone, CellIdentity cellIdentity);

    void notifyCellularIdentifierDisclosedChanged(Phone phone, CellularIdentifierDisclosure cellularIdentifierDisclosure);

    void notifyDataActivationStateChanged(Phone phone, int i);

    void notifyDataActivity(Phone phone);

    void notifyDataConnection(Phone phone, PreciseDataConnectionState preciseDataConnectionState);

    void notifyDataEnabled(Phone phone, boolean z, int i);

    void notifyDisconnectCause(Phone phone, int i, int i2);

    void notifyDisplayInfoChanged(Phone phone, TelephonyDisplayInfo telephonyDisplayInfo);

    void notifyDomainSelectionEmergencyModeEntered(Phone phone, int i);

    void notifyDomainSelectionEmergencyModeExited(Phone phone, int i);

    void notifyEmergencyNumberList(Phone phone);

    void notifyImsDisconnectCause(Phone phone, ImsReasonInfo imsReasonInfo);

    void notifyLinkCapacityEstimateChanged(Phone phone, List<LinkCapacityEstimate> list);

    void notifyMediaQualityStatusChanged(Phone phone, MediaQualityStatus mediaQualityStatus);

    void notifyMessageWaitingChanged(Phone phone);

    void notifyNetworkSecurityEvents(Phone phone, Set<NetworkSecurityEvent> set);

    void notifyOutgoingEmergencySms(Phone phone, EmergencyNumber emergencyNumber);

    void notifyPhoneCapabilityChanged(PhoneCapability phoneCapability);

    void notifyPhoneState(Phone phone);

    void notifyPhysicalChannelConfig(Phone phone, List<PhysicalChannelConfig> list);

    void notifyPreciseCallState(Phone phone, String[] strArr, int[] iArr, int[] iArr2);

    void notifyRadioPowerStateChanged(Phone phone, int i);

    void notifyRegistrationFailed(Phone phone, CellIdentity cellIdentity, String str, int i, int i2, int i3);

    void notifySatellitePurchaseModeChanged(Phone phone, boolean z, int i);

    void notifySecurityAlgorithmsChanged(Phone phone, SecurityAlgorithmUpdate securityAlgorithmUpdate);

    void notifyServiceState(Phone phone);

    void notifyServiceStateForSubId(Phone phone, ServiceState serviceState, int i);

    void notifySignalStrength(Phone phone);

    void notifySimultaneousCellularCallingSubscriptionsChanged(Set<Integer> set);

    void notifySrvccStateChanged(Phone phone, int i);

    void notifyUserMobileDataStateChanged(Phone phone, boolean z);

    void notifyVoiceActivationStateChanged(Phone phone, int i);
}

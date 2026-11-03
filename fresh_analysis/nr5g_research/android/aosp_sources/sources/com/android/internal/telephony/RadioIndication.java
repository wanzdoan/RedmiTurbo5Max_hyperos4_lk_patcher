package com.android.internal.telephony;

import android.hardware.radio.V1_0.CdmaCallWaiting;
import android.hardware.radio.V1_0.CdmaInformationRecords;
import android.hardware.radio.V1_0.CdmaSignalInfoRecord;
import android.hardware.radio.V1_0.CdmaSmsMessage;
import android.hardware.radio.V1_0.CellInfo;
import android.hardware.radio.V1_0.CfData;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.PcoDataInfo;
import android.hardware.radio.V1_0.RadioState;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.SignalStrength;
import android.hardware.radio.V1_0.SimRefreshResult;
import android.hardware.radio.V1_0.SsInfoData;
import android.hardware.radio.V1_0.StkCcUnsolSsResult;
import android.hardware.radio.V1_0.SuppSvcNotification;
import android.hardware.radio.V1_1.KeepaliveStatus;
import android.hardware.radio.V1_1.NetworkScanResult;
import android.hardware.radio.V1_2.LinkCapacityEstimate;
import android.hardware.radio.V1_4.EmergencyNumber;
import android.hardware.radio.V1_4.PhysicalChannelConfig;
import android.hardware.radio.V1_5.BarringInfo;
import android.hardware.radio.V1_5.CellIdentity;
import android.hardware.radio.V1_6.IRadioIndication;
import android.hardware.radio.V1_6.PhonebookRecordInfo;
import android.hardware.radio.network.NetworkSecurityEvent;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.RemoteException;
import android.telephony.AnomalyReporter;
import android.telephony.PcoData;
import android.telephony.ServiceState;
import android.text.TextUtils;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.ReceivedPhonebookRecords;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public class RadioIndication extends IRadioIndication.Stub {
    public RIL mRil;

    public void cdmaCallWaiting(int i, CdmaCallWaiting cdmaCallWaiting) {
    }

    public void cdmaInfoRec(int i, CdmaInformationRecords cdmaInformationRecords) {
    }

    public void cdmaOtaProvisionStatus(int i, int i2) {
    }

    public void cdmaPrlChanged(int i, int i2) {
    }

    public void cdmaRuimSmsStorageFull(int i) {
    }

    public void cdmaSubscriptionSourceChanged(int i, int i2) {
    }

    public RadioIndication(RIL ril) {
        this.mRil = ril;
    }

    public void radioStateChanged(int i, int i2) {
        this.mRil.processIndication(0, i);
        int iConvertHalRadioState = RILUtils.convertHalRadioState(i2);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogMore(1000, "radioStateChanged: " + RadioState.toString(i2));
        }
        this.mRil.setRadioState(iConvertHalRadioState, false);
    }

    public void callStateChanged(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1001);
        }
        this.mRil.mCallStateRegistrants.notifyRegistrants();
    }

    public void networkStateChanged(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1002);
        }
        this.mRil.mNetworkStateRegistrants.notifyRegistrants();
    }

    public void newSms(int i, ArrayList<Byte> arrayList) {
        this.mRil.processIndication(0, i);
        byte[] bArrArrayListToPrimitiveArray = RILUtils.arrayListToPrimitiveArray(arrayList);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1003);
        }
        SmsMessage smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArrArrayListToPrimitiveArray);
        Registrant registrant = this.mRil.mGsmSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, smsMessageCreateFromPdu == null ? null : new android.telephony.SmsMessage(smsMessageCreateFromPdu), (Throwable) null));
        }
    }

    public void newSmsStatusReport(int i, ArrayList<Byte> arrayList) {
        this.mRil.processIndication(0, i);
        byte[] bArrArrayListToPrimitiveArray = RILUtils.arrayListToPrimitiveArray(arrayList);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1004);
        }
        Registrant registrant = this.mRil.mSmsStatusRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, bArrArrayListToPrimitiveArray, (Throwable) null));
        }
    }

    public void newSmsOnSim(int i, int i2) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1005);
        }
        Registrant registrant = this.mRil.mSmsOnSimRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    public void onUssd(int i, int i2, String str) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogMore(1006, "" + i2);
        }
        String[] strArr = {"" + i2, str};
        Registrant registrant = this.mRil.mUSSDRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, strArr, (Throwable) null));
        }
    }

    public void nitzTimeReceived(int i, String str, long j) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(1008, str);
        }
        Object[] objArr = {str, Long.valueOf(j)};
        boolean zBooleanValue = TelephonyProperties.ignore_nitz().orElse(Boolean.FALSE).booleanValue();
        RIL ril = this.mRil;
        if (zBooleanValue) {
            if (ril.isLogOrTrace()) {
                this.mRil.riljLog("ignoring UNSOL_NITZ_TIME_RECEIVED");
            }
        } else {
            Registrant registrant = ril.mNITZTimeRegistrant;
            if (registrant != null) {
                registrant.notifyRegistrant(new AsyncResult((Object) null, objArr, (Throwable) null));
            }
            this.mRil.mLastNITZTimeInfo = objArr;
        }
    }

    public void currentSignalStrength(int i, SignalStrength signalStrength) {
        this.mRil.unsljLogMore(1009, "unsupported on IRadio < 1.4");
    }

    public void currentLinkCapacityEstimate(int i, LinkCapacityEstimate linkCapacityEstimate) {
        this.mRil.processIndication(0, i);
        List<android.telephony.LinkCapacityEstimate> listConvertHalLinkCapacityEstimate = RILUtils.convertHalLinkCapacityEstimate(linkCapacityEstimate);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_LCEDATA_RECV, listConvertHalLinkCapacityEstimate);
        }
        RegistrantList registrantList = this.mRil.mLceInfoRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, listConvertHalLinkCapacityEstimate, (Throwable) null));
        }
    }

    public void currentLinkCapacityEstimate_1_6(int i, android.hardware.radio.V1_6.LinkCapacityEstimate linkCapacityEstimate) {
        this.mRil.processIndication(0, i);
        List<android.telephony.LinkCapacityEstimate> listConvertHalLinkCapacityEstimate = RILUtils.convertHalLinkCapacityEstimate(linkCapacityEstimate);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_LCEDATA_RECV, listConvertHalLinkCapacityEstimate);
        }
        RegistrantList registrantList = this.mRil.mLceInfoRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, listConvertHalLinkCapacityEstimate, (Throwable) null));
        }
    }

    public void currentSignalStrength_1_2(int i, android.hardware.radio.V1_2.SignalStrength signalStrength) {
        this.mRil.unsljLogMore(1009, "unsupported on IRadio < 1.4");
    }

    public void currentSignalStrength_1_4(int i, android.hardware.radio.V1_4.SignalStrength signalStrength) {
        this.mRil.processIndication(0, i);
        android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = RILUtils.convertHalSignalStrength(signalStrength);
        if (this.mRil.isLogvOrTrace()) {
            this.mRil.unsljLogvRet(1009, signalStrengthConvertHalSignalStrength);
        }
        Registrant registrant = this.mRil.mSignalStrengthRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, signalStrengthConvertHalSignalStrength, (Throwable) null));
        }
    }

    public void currentSignalStrength_1_6(int i, android.hardware.radio.V1_6.SignalStrength signalStrength) {
        this.mRil.processIndication(0, i);
        android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = RILUtils.convertHalSignalStrength(signalStrength);
        if (this.mRil.isLogvOrTrace()) {
            this.mRil.unsljLogvRet(1009, signalStrengthConvertHalSignalStrength);
        }
        Registrant registrant = this.mRil.mSignalStrengthRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, signalStrengthConvertHalSignalStrength, (Throwable) null));
        }
    }

    public void currentPhysicalChannelConfigs_1_4(int i, ArrayList<PhysicalChannelConfig> arrayList) {
        this.mRil.processIndication(0, i);
        physicalChannelConfigsIndication(arrayList);
    }

    public void currentPhysicalChannelConfigs_1_6(int i, ArrayList<android.hardware.radio.V1_6.PhysicalChannelConfig> arrayList) {
        this.mRil.processIndication(0, i);
        physicalChannelConfigsIndication(arrayList);
    }

    public void currentPhysicalChannelConfigs(int i, ArrayList<android.hardware.radio.V1_2.PhysicalChannelConfig> arrayList) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG, "unsupported on IRadio < 1.4");
    }

    public void currentEmergencyNumberList(int i, ArrayList<EmergencyNumber> arrayList) {
        int i2 = 0;
        this.mRil.processIndication(0, i);
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        int size = arrayList.size();
        while (i2 < size) {
            EmergencyNumber emergencyNumber = arrayList.get(i2);
            i2++;
            EmergencyNumber emergencyNumber2 = emergencyNumber;
            arrayList2.add(new android.telephony.emergency.EmergencyNumber(emergencyNumber2.number, MccTable.countryCodeForMcc(emergencyNumber2.mcc), emergencyNumber2.mnc, emergencyNumber2.categories, emergencyNumber2.urns, emergencyNumber2.sources, 0));
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_EMERGENCY_NUMBER_LIST, arrayList2);
        }
        this.mRil.cacheEmergencyNumberListIndication(arrayList2);
        this.mRil.mEmergencyNumberListRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayList2, (Throwable) null));
    }

    public void dataCallListChanged(int i, ArrayList<SetupDataCallResult> arrayList) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_DATA_CALL_LIST_CHANGED, "unsupported on IRadio < 1.4");
    }

    public void dataCallListChanged_1_4(int i, ArrayList<android.hardware.radio.V1_4.SetupDataCallResult> arrayList) {
        responseDataCallListChanged(i, arrayList);
    }

    public void dataCallListChanged_1_5(int i, ArrayList<android.hardware.radio.V1_5.SetupDataCallResult> arrayList) {
        responseDataCallListChanged(i, arrayList);
    }

    public void dataCallListChanged_1_6(int i, ArrayList<android.hardware.radio.V1_6.SetupDataCallResult> arrayList) {
        responseDataCallListChanged(i, arrayList);
    }

    public void unthrottleApn(int i, String str) throws RemoteException {
        responseApnUnthrottled(i, str);
    }

    public void suppSvcNotify(int i, SuppSvcNotification suppSvcNotification) {
        this.mRil.processIndication(0, i);
        SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
        suppServiceNotification.notificationType = suppSvcNotification.isMT ? 1 : 0;
        suppServiceNotification.code = suppSvcNotification.code;
        suppServiceNotification.index = suppSvcNotification.index;
        suppServiceNotification.type = suppSvcNotification.type;
        suppServiceNotification.number = suppSvcNotification.number;
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_SUPP_SVC_NOTIFICATION, suppServiceNotification);
        }
        Registrant registrant = this.mRil.mSsnRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, suppServiceNotification, (Throwable) null));
        }
    }

    public void stkSessionEnd(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_SESSION_END);
        }
        Registrant registrant = this.mRil.mCatSessionEndRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    public void stkProactiveCommand(int i, String str) {
        Registrant registrant;
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_PROACTIVE_COMMAND);
        }
        if (MiuiPhoneStub.get(this.mRil.mPhoneId.intValue()).notifyStkProCmdForEsimBinding(str) || (registrant = this.mRil.mCatProCmdRegistrant) == null) {
            return;
        }
        registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
    }

    public void stkEventNotify(int i, String str) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY);
        }
        Registrant registrant = this.mRil.mCatEventRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
        }
    }

    public void stkCallSetup(int i, long j) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CALL_SETUP, Long.valueOf(j));
        }
        Registrant registrant = this.mRil.mCatCallSetUpRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Long.valueOf(j), (Throwable) null));
        }
    }

    public void simSmsStorageFull(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL);
        }
        Registrant registrant = this.mRil.mIccSmsFullRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    public void simRefresh(int i, SimRefreshResult simRefreshResult) {
        this.mRil.processIndication(0, i);
        IccRefreshResponse iccRefreshResponse = new IccRefreshResponse();
        iccRefreshResponse.refreshResult = simRefreshResult.type;
        iccRefreshResponse.efId = simRefreshResult.efId;
        iccRefreshResponse.aid = simRefreshResult.aid;
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_REFRESH, iccRefreshResponse);
        }
        this.mRil.mIccRefreshRegistrants.notifyRegistrants(new AsyncResult((Object) null, iccRefreshResponse, (Throwable) null));
    }

    public void callRing(int i, boolean z, CdmaSignalInfoRecord cdmaSignalInfoRecord) {
        char[] cArr;
        this.mRil.processIndication(0, i);
        if (z) {
            cArr = null;
        } else {
            cArr = new char[]{cdmaSignalInfoRecord.isPresent ? (char) 1 : (char) 0, (char) cdmaSignalInfoRecord.signalType, (char) cdmaSignalInfoRecord.alertPitch, (char) cdmaSignalInfoRecord.signal};
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CALL_RING, cArr);
        }
        Registrant registrant = this.mRil.mRingRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, cArr, (Throwable) null));
        }
    }

    public void simStatusChanged(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED);
        }
        this.mRil.mIccStatusChangedRegistrants.notifyRegistrants();
    }

    public void cdmaNewSms(int i, CdmaSmsMessage cdmaSmsMessage) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_CDMA_NEW_SMS);
        }
        android.telephony.SmsMessage smsMessage = new android.telephony.SmsMessage(RILUtils.convertHalCdmaSmsMessage(cdmaSmsMessage));
        Registrant registrant = this.mRil.mCdmaSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, smsMessage, (Throwable) null));
        }
    }

    public void newSecureSms(int i, ArrayList<Byte> arrayList, NetworkSecurityEvent networkSecurityEvent) {
        if (this.mRil.isLogOrTrace()) {
            this.mRil.riljLog("newSecureSms: secure sms feature is disabled.");
        }
        newSms(i, arrayList);
    }

    public void newBroadcastSms(int i, ArrayList<Byte> arrayList) {
        this.mRil.processIndication(0, i);
        byte[] bArrArrayListToPrimitiveArray = RILUtils.arrayListToPrimitiveArray(arrayList);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS, IccUtils.bytesToHexString(bArrArrayListToPrimitiveArray));
        }
        Registrant registrant = this.mRil.mGsmBroadcastSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, bArrArrayListToPrimitiveArray, (Throwable) null));
        }
    }

    public void restrictedStateChanged(int i, int i2) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RESTRICTED_STATE_CHANGED, Integer.valueOf(i2));
        }
        Registrant registrant = this.mRil.mRestrictedStateRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    public void enterEmergencyCallbackMode(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE);
        }
        Registrant registrant = this.mRil.mEmergencyCallbackModeRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    public void indicateRingbackTone(int i, boolean z) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RINGBACK_TONE, Boolean.valueOf(z));
        }
        this.mRil.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(z), (Throwable) null));
    }

    public void resendIncallMute(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESEND_INCALL_MUTE);
        }
        this.mRil.mResendIncallMuteRegistrants.notifyRegistrants();
    }

    public void exitEmergencyCallbackMode(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE);
        }
        this.mRil.mExitEmergencyCallbackModeRegistrants.notifyRegistrants();
    }

    public void rilConnected(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RIL_CONNECTED);
        }
        this.mRil.setRadioPower(false, null);
        this.mRil.notifyRegistrantsRilConnectionChanged(15);
    }

    public void voiceRadioTechChanged(int i, int i2) {
        this.mRil.processIndication(0, i);
        int[] iArr = {i2};
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_VOICE_RADIO_TECH_CHANGED, iArr);
        }
        this.mRil.mVoiceRadioTechChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void cellInfoList(int i, ArrayList<CellInfo> arrayList) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_CELL_INFO_LIST, "unsupported on IRadio < 1.4");
    }

    public void cellInfoList_1_2(int i, ArrayList<android.hardware.radio.V1_2.CellInfo> arrayList) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_CELL_INFO_LIST, "unsupported on IRadio < 1.4");
    }

    public void cellInfoList_1_4(int i, ArrayList<android.hardware.radio.V1_4.CellInfo> arrayList) {
        this.mRil.processIndication(0, i);
        responseCellInfoList(arrayList);
    }

    public void cellInfoList_1_5(int i, ArrayList<android.hardware.radio.V1_5.CellInfo> arrayList) {
        this.mRil.processIndication(0, i);
        responseCellInfoList(arrayList);
    }

    public void cellInfoList_1_6(int i, ArrayList<android.hardware.radio.V1_6.CellInfo> arrayList) {
        this.mRil.processIndication(0, i);
        responseCellInfoList(arrayList);
    }

    private void responseCellInfoList(ArrayList arrayList) {
        ArrayList<android.telephony.CellInfo> arrayListConvertHalCellInfoList = RILUtils.convertHalCellInfoList((ArrayList<Object>) arrayList);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CELL_INFO_LIST, arrayListConvertHalCellInfoList);
        }
        this.mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayListConvertHalCellInfoList, (Throwable) null));
    }

    public void uiccApplicationsEnablementChanged(int i, boolean z) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_APPLICATIONS_ENABLEMENT_CHANGED, Boolean.valueOf(z));
        }
        this.mRil.mUiccApplicationsEnablementRegistrants.notifyResult(Boolean.valueOf(z));
    }

    public void networkScanResult(int i, NetworkScanResult networkScanResult) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT, "unsupported on IRadio < 1.4");
    }

    public void networkScanResult_1_2(int i, android.hardware.radio.V1_2.NetworkScanResult networkScanResult) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT, "unsupported on IRadio < 1.4");
    }

    public void networkScanResult_1_4(int i, android.hardware.radio.V1_4.NetworkScanResult networkScanResult) {
        responseNetworkScan(i, networkScanResult);
    }

    public void networkScanResult_1_5(int i, android.hardware.radio.V1_5.NetworkScanResult networkScanResult) {
        responseNetworkScan_1_5(i, networkScanResult);
    }

    public void networkScanResult_1_6(int i, android.hardware.radio.V1_6.NetworkScanResult networkScanResult) {
        responseNetworkScan_1_6(i, networkScanResult);
    }

    public void imsNetworkStateChanged(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED);
        }
        this.mRil.mImsNetworkStateChangedRegistrants.notifyRegistrants();
    }

    public void subscriptionStatusChanged(int i, boolean z) {
        this.mRil.processIndication(0, i);
        int[] iArr = {z ? 1 : 0};
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED, iArr);
        }
        this.mRil.mSubscriptionStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void srvccStateNotify(int i, int i2) {
        this.mRil.processIndication(0, i);
        int[] iArr = {i2};
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_SRVCC_STATE_NOTIFY, iArr);
        }
        this.mRil.writeMetricsSrvcc(i2);
        this.mRil.mSrvccStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void hardwareConfigChanged(int i, ArrayList<android.hardware.radio.V1_0.HardwareConfig> arrayList) {
        this.mRil.processIndication(0, i);
        ArrayList<HardwareConfig> arrayListConvertHalHardwareConfigList = RILUtils.convertHalHardwareConfigList(arrayList);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_HARDWARE_CONFIG_CHANGED, arrayListConvertHalHardwareConfigList);
        }
        this.mRil.mHardwareConfigChangeRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayListConvertHalHardwareConfigList, (Throwable) null));
    }

    public void radioCapabilityIndication(int i, android.hardware.radio.V1_0.RadioCapability radioCapability) {
        this.mRil.processIndication(0, i);
        RadioCapability radioCapabilityConvertHalRadioCapability = RILUtils.convertHalRadioCapability(radioCapability, this.mRil);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RADIO_CAPABILITY, radioCapabilityConvertHalRadioCapability);
        }
        this.mRil.mPhoneRadioCapabilityChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, radioCapabilityConvertHalRadioCapability, (Throwable) null));
    }

    public void onSupplementaryServiceIndication(int i, StkCcUnsolSsResult stkCcUnsolSsResult) {
        int i2 = 0;
        this.mRil.processIndication(0, i);
        SsData ssData = new SsData();
        ssData.serviceType = ssData.ServiceTypeFromRILInt(stkCcUnsolSsResult.serviceType);
        ssData.requestType = ssData.RequestTypeFromRILInt(stkCcUnsolSsResult.requestType);
        ssData.teleserviceType = ssData.TeleserviceTypeFromRILInt(stkCcUnsolSsResult.teleserviceType);
        ssData.serviceClass = stkCcUnsolSsResult.serviceClass;
        ssData.result = stkCcUnsolSsResult.result;
        if (ssData.serviceType.isTypeCF() && ssData.requestType.isTypeInterrogation()) {
            CfData cfData = (CfData) stkCcUnsolSsResult.cfData.get(0);
            int size = cfData.cfInfo.size();
            ssData.cfInfo = new CallForwardInfo[size];
            while (i2 < size) {
                android.hardware.radio.V1_0.CallForwardInfo callForwardInfo = (android.hardware.radio.V1_0.CallForwardInfo) cfData.cfInfo.get(i2);
                ssData.cfInfo[i2] = new CallForwardInfo();
                CallForwardInfo callForwardInfo2 = ssData.cfInfo[i2];
                callForwardInfo2.status = callForwardInfo.status;
                callForwardInfo2.reason = callForwardInfo.reason;
                callForwardInfo2.serviceClass = callForwardInfo.serviceClass;
                callForwardInfo2.toa = callForwardInfo.toa;
                callForwardInfo2.number = callForwardInfo.number;
                callForwardInfo2.timeSeconds = callForwardInfo.timeSeconds;
                this.mRil.riljLog("[SS Data] CF Info " + i2 + " : " + ssData.cfInfo[i2]);
                i2++;
            }
        } else {
            SsInfoData ssInfoData = (SsInfoData) stkCcUnsolSsResult.ssInfo.get(0);
            int size2 = ssInfoData.ssInfo.size();
            ssData.ssInfo = new int[size2];
            while (i2 < size2) {
                ssData.ssInfo[i2] = ((Integer) ssInfoData.ssInfo.get(i2)).intValue();
                this.mRil.riljLog("[SS Data] SS Info " + i2 + " : " + ssData.ssInfo[i2]);
                i2++;
            }
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_ON_SS, ssData);
        }
        Registrant registrant = this.mRil.mSsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ssData, (Throwable) null));
        }
    }

    public void stkCallControlAlphaNotify(int i, String str) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CC_ALPHA_NOTIFY, str);
        }
        Registrant registrant = this.mRil.mCatCcAlphaRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
        }
    }

    public void lceData(int i, LceDataInfo lceDataInfo) {
        this.mRil.unsljLogMore(NtnRILConstants.MI_NTN_RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG, "unsupported on IRadio < 1.4");
    }

    public void pcoData(int i, PcoDataInfo pcoDataInfo) {
        this.mRil.processIndication(0, i);
        PcoData pcoData = new PcoData(pcoDataInfo.cid, pcoDataInfo.bearerProto, pcoDataInfo.pcoId, RILUtils.arrayListToPrimitiveArray(pcoDataInfo.contents));
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_PCO_DATA, pcoData);
        }
        this.mRil.mPcoDataRegistrants.notifyRegistrants(new AsyncResult((Object) null, pcoData, (Throwable) null));
    }

    public void modemReset(int i, String str) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_MODEM_RESTART, str);
        }
        this.mRil.writeMetricsModemRestartEvent(str);
        this.mRil.mModemResetRegistrants.notifyRegistrants(new AsyncResult((Object) null, str, (Throwable) null));
    }

    public void carrierInfoForImsiEncryption(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CARRIER_INFO_IMSI_ENCRYPTION, null);
        }
        this.mRil.mCarrierInfoForImsiEncryptionRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    public void keepaliveStatus(int i, KeepaliveStatus keepaliveStatus) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_KEEPALIVE_STATUS, "handle=" + keepaliveStatus.sessionHandle + " code=" + keepaliveStatus.code);
        }
        this.mRil.mNattKeepaliveStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, new com.android.internal.telephony.data.KeepaliveStatus(keepaliveStatus.sessionHandle, keepaliveStatus.code), (Throwable) null));
    }

    public void simPhonebookChanged(int i) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMSI_QUERY);
        }
        this.mRil.mSimPhonebookChangedRegistrants.notifyRegistrants();
    }

    public void simPhonebookRecordsReceived(int i, byte b, ArrayList<PhonebookRecordInfo> arrayList) {
        int i2 = 0;
        this.mRil.processIndication(0, i);
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        while (i2 < size) {
            PhonebookRecordInfo phonebookRecordInfo = arrayList.get(i2);
            i2++;
            arrayList2.add(RILUtils.convertHalPhonebookRecordInfo(phonebookRecordInfo));
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_AUTHENTICATION, "status = " + ((int) b) + " received " + arrayList.size() + " records");
        }
        this.mRil.mSimPhonebookRecordsReceivedRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ReceivedPhonebookRecords(b, arrayList2), (Throwable) null));
    }

    public void registrationFailed(int i, CellIdentity cellIdentity, String str, int i2, int i3, int i4) {
        this.mRil.processIndication(0, i);
        android.telephony.CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(cellIdentity);
        if (cellIdentityConvertHalCellIdentity == null || TextUtils.isEmpty(str) || (i2 & 3) == 0 || (i2 & (-4)) != 0 || i3 < 0 || i4 < 0 || (i3 == Integer.MAX_VALUE && i4 == Integer.MAX_VALUE)) {
            AnomalyReporter.reportAnomaly(UUID.fromString("f16e5703-6105-4341-9eb3-e68189156eb4"), "Invalid registrationFailed indication");
            this.mRil.riljLoge("Invalid registrationFailed indication");
        } else {
            this.mRil.mRegistrationFailedRegistrant.notifyRegistrant(new AsyncResult((Object) null, new RegistrationFailedEvent(cellIdentityConvertHalCellIdentity, str, i2, i3, i4), (Throwable) null));
        }
    }

    public void barringInfoChanged(int i, CellIdentity cellIdentity, ArrayList<BarringInfo> arrayList) {
        this.mRil.processIndication(0, i);
        if (cellIdentity == null || arrayList == null) {
            AnomalyReporter.reportAnomaly(UUID.fromString("645b16bb-c930-4c1c-9c5d-568696542e05"), "Invalid barringInfoChanged indication");
            this.mRil.riljLoge("Invalid barringInfoChanged indication");
        } else {
            this.mRil.mBarringInfoChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, new android.telephony.BarringInfo(RILUtils.convertHalCellIdentity(cellIdentity), RILUtils.convertHalBarringInfoList(arrayList)), (Throwable) null));
        }
    }

    private void setFrequencyRangeOrChannelNumber(android.telephony.PhysicalChannelConfig.Builder builder, PhysicalChannelConfig physicalChannelConfig) {
        byte discriminator = physicalChannelConfig.rfInfo.getDiscriminator();
        if (discriminator == 0) {
            builder.setFrequencyRange(physicalChannelConfig.rfInfo.range());
            return;
        }
        if (discriminator == 1) {
            builder.setDownlinkChannelNumber(physicalChannelConfig.rfInfo.channelNumber());
            return;
        }
        this.mRil.riljLoge("Unsupported frequency type " + ((int) physicalChannelConfig.rfInfo.getDiscriminator()));
    }

    private void physicalChannelConfigsIndication(List list) {
        int iGeranBand;
        ArrayList arrayList = new ArrayList(list.size());
        try {
            for (Object obj : list) {
                if (obj instanceof PhysicalChannelConfig) {
                    PhysicalChannelConfig physicalChannelConfig = (PhysicalChannelConfig) obj;
                    android.telephony.PhysicalChannelConfig.Builder builder = new android.telephony.PhysicalChannelConfig.Builder();
                    setFrequencyRangeOrChannelNumber(builder, physicalChannelConfig);
                    arrayList.add(builder.setCellConnectionStatus(RILUtils.convertHalCellConnectionStatus(physicalChannelConfig.base.status)).setCellBandwidthDownlinkKhz(physicalChannelConfig.base.cellBandwidthDownlink).setNetworkType(ServiceState.rilRadioTechnologyToNetworkType(physicalChannelConfig.rat)).setPhysicalCellId(physicalChannelConfig.physicalCellId).setContextIds(physicalChannelConfig.contextIds.stream().mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.RadioIndication$$ExternalSyntheticLambda0
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj2) {
                            return ((Integer) obj2).intValue();
                        }
                    }).toArray()).build());
                } else if (obj instanceof android.hardware.radio.V1_6.PhysicalChannelConfig) {
                    android.hardware.radio.V1_6.PhysicalChannelConfig physicalChannelConfig2 = (android.hardware.radio.V1_6.PhysicalChannelConfig) obj;
                    android.telephony.PhysicalChannelConfig.Builder builder2 = new android.telephony.PhysicalChannelConfig.Builder();
                    byte discriminator = physicalChannelConfig2.band.getDiscriminator();
                    if (discriminator == 0) {
                        iGeranBand = physicalChannelConfig2.band.geranBand();
                    } else if (discriminator == 1) {
                        iGeranBand = physicalChannelConfig2.band.utranBand();
                    } else if (discriminator == 2) {
                        iGeranBand = physicalChannelConfig2.band.eutranBand();
                    } else if (discriminator == 3) {
                        iGeranBand = physicalChannelConfig2.band.ngranBand();
                    } else {
                        this.mRil.riljLoge("Unsupported band " + ((int) physicalChannelConfig2.band.getDiscriminator()));
                        iGeranBand = 0;
                    }
                    if (iGeranBand == 0) {
                        this.mRil.riljLoge("Unsupported unknown band.");
                    } else {
                        builder2.setBand(iGeranBand);
                    }
                    arrayList.add(builder2.setCellConnectionStatus(RILUtils.convertHalCellConnectionStatus(physicalChannelConfig2.status)).setDownlinkChannelNumber(physicalChannelConfig2.downlinkChannelNumber).setUplinkChannelNumber(physicalChannelConfig2.uplinkChannelNumber).setCellBandwidthDownlinkKhz(physicalChannelConfig2.cellBandwidthDownlinkKhz).setCellBandwidthUplinkKhz(physicalChannelConfig2.cellBandwidthUplinkKhz).setNetworkType(ServiceState.rilRadioTechnologyToNetworkType(physicalChannelConfig2.rat)).setPhysicalCellId(physicalChannelConfig2.physicalCellId).setContextIds(physicalChannelConfig2.contextIds.stream().mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.RadioIndication$$ExternalSyntheticLambda1
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj2) {
                            return ((Integer) obj2).intValue();
                        }
                    }).toArray()).build());
                } else {
                    this.mRil.riljLoge("Unsupported PhysicalChannelConfig " + obj);
                }
            }
            if (this.mRil.isLogOrTrace()) {
                this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG, arrayList);
            }
            this.mRil.mPhysicalChannelConfigurationRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayList, (Throwable) null));
        } catch (IllegalArgumentException e) {
            AnomalyReporter.reportAnomaly(UUID.fromString("918f0970-9aa9-4bcd-a28e-e49a83fe77d5"), "RIL reported invalid PCC (HIDL)");
            this.mRil.riljLoge("Invalid PhysicalChannelConfig " + e);
        }
    }

    private void responseNetworkScan(int i, android.hardware.radio.V1_4.NetworkScanResult networkScanResult) {
        this.mRil.processIndication(0, i);
        NetworkScanResult networkScanResult2 = new NetworkScanResult(networkScanResult.status, networkScanResult.error, RILUtils.convertHalCellInfoList((ArrayList<Object>) new ArrayList(networkScanResult.networkInfos)));
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT, networkScanResult2);
        }
        this.mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult((Object) null, networkScanResult2, (Throwable) null));
    }

    private void responseNetworkScan_1_5(int i, android.hardware.radio.V1_5.NetworkScanResult networkScanResult) {
        this.mRil.processIndication(0, i);
        NetworkScanResult networkScanResult2 = new NetworkScanResult(networkScanResult.status, networkScanResult.error, RILUtils.convertHalCellInfoList((ArrayList<Object>) new ArrayList(networkScanResult.networkInfos)));
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT, networkScanResult2);
        }
        this.mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult((Object) null, networkScanResult2, (Throwable) null));
    }

    private void responseNetworkScan_1_6(int i, android.hardware.radio.V1_6.NetworkScanResult networkScanResult) {
        this.mRil.processIndication(0, i);
        NetworkScanResult networkScanResult2 = new NetworkScanResult(networkScanResult.status, networkScanResult.error, RILUtils.convertHalCellInfoList((ArrayList<Object>) new ArrayList(networkScanResult.networkInfos)));
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT, networkScanResult2);
        }
        this.mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult((Object) null, networkScanResult2, (Throwable) null));
    }

    private void responseDataCallListChanged(int i, List list) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_DATA_CALL_LIST_CHANGED, list);
        }
        this.mRil.mDataCallListChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, RILUtils.convertHalDataCallResultList((List<? extends Object>) list), (Throwable) null));
    }

    private void responseApnUnthrottled(int i, String str) {
        this.mRil.processIndication(0, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_UNTHROTTLE_APN, str);
        }
        this.mRil.mApnUnthrottledRegistrants.notifyRegistrants(new AsyncResult((Object) null, str, (Throwable) null));
    }
}

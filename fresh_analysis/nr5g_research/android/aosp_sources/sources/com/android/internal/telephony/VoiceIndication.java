package com.android.internal.telephony;

import android.hardware.radio.voice.CdmaCallWaiting;
import android.hardware.radio.voice.CdmaInformationRecord;
import android.hardware.radio.voice.CdmaSignalInfoRecord;
import android.hardware.radio.voice.CfData;
import android.hardware.radio.voice.EmergencyNumber;
import android.hardware.radio.voice.IRadioVoiceIndication;
import android.hardware.radio.voice.SsInfoData;
import android.hardware.radio.voice.StkCcUnsolSsResult;
import android.os.AsyncResult;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class VoiceIndication extends IRadioVoiceIndication.Stub {
    private final RIL mRil;

    public void cdmaCallWaiting(int i, CdmaCallWaiting cdmaCallWaiting) {
    }

    public void cdmaInfoRec(int i, CdmaInformationRecord[] cdmaInformationRecordArr) {
    }

    public void cdmaOtaProvisionStatus(int i, int i2) {
    }

    public int getInterfaceVersion() {
        return 5;
    }

    public VoiceIndication(RIL ril) {
        this.mRil = ril;
    }

    public void callRing(int i, boolean z, CdmaSignalInfoRecord cdmaSignalInfoRecord) {
        char[] cArr;
        this.mRil.processIndication(6, i);
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

    public void callStateChanged(int i) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1001);
        }
        this.mRil.mCallStateRegistrants.notifyRegistrants();
    }

    public void currentEmergencyNumberList(int i, EmergencyNumber[] emergencyNumberArr) {
        this.mRil.processIndication(6, i);
        ArrayList arrayList = new ArrayList(emergencyNumberArr.length);
        for (EmergencyNumber emergencyNumber : emergencyNumberArr) {
            arrayList.add(new android.telephony.emergency.EmergencyNumber(emergencyNumber.number, MccTable.countryCodeForMcc(emergencyNumber.mcc), emergencyNumber.mnc, emergencyNumber.categories, RILUtils.primitiveArrayToArrayList(emergencyNumber.urns), emergencyNumber.sources, 0));
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_EMERGENCY_NUMBER_LIST, arrayList);
        }
        this.mRil.cacheEmergencyNumberListIndication(arrayList);
        this.mRil.mEmergencyNumberListRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayList, (Throwable) null));
    }

    public void enterEmergencyCallbackMode(int i) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE);
        }
        Registrant registrant = this.mRil.mEmergencyCallbackModeRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    public void exitEmergencyCallbackMode(int i) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE);
        }
        this.mRil.mExitEmergencyCallbackModeRegistrants.notifyRegistrants();
    }

    public void indicateRingbackTone(int i, boolean z) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RINGBACK_TONE, Boolean.valueOf(z));
        }
        this.mRil.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(z), (Throwable) null));
    }

    public void onSupplementaryServiceIndication(int i, StkCcUnsolSsResult stkCcUnsolSsResult) {
        this.mRil.processIndication(6, i);
        SsData ssData = new SsData();
        ssData.serviceType = ssData.ServiceTypeFromRILInt(stkCcUnsolSsResult.serviceType);
        ssData.requestType = ssData.RequestTypeFromRILInt(stkCcUnsolSsResult.requestType);
        ssData.teleserviceType = ssData.TeleserviceTypeFromRILInt(stkCcUnsolSsResult.teleserviceType);
        ssData.serviceClass = stkCcUnsolSsResult.serviceClass;
        ssData.result = stkCcUnsolSsResult.result;
        int i2 = 0;
        if (ssData.serviceType.isTypeCF() && ssData.requestType.isTypeInterrogation()) {
            CfData cfData = stkCcUnsolSsResult.cfData[0];
            int length = cfData.cfInfo.length;
            ssData.cfInfo = new CallForwardInfo[length];
            while (i2 < length) {
                android.hardware.radio.voice.CallForwardInfo callForwardInfo = cfData.cfInfo[i2];
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
            SsInfoData ssInfoData = stkCcUnsolSsResult.ssInfo[0];
            int length2 = ssInfoData.ssInfo.length;
            ssData.ssInfo = new int[length2];
            while (i2 < length2) {
                ssData.ssInfo[i2] = ssInfoData.ssInfo[i2];
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

    public void onUssd(int i, int i2, String str) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogMore(1006, "" + i2);
        }
        String[] strArr = {"" + i2, str};
        Registrant registrant = this.mRil.mUSSDRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, strArr, (Throwable) null));
        }
    }

    public void resendIncallMute(int i) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESEND_INCALL_MUTE);
        }
        this.mRil.mResendIncallMuteRegistrants.notifyRegistrants();
    }

    public void srvccStateNotify(int i, int i2) {
        this.mRil.processIndication(6, i);
        int[] iArr = {i2};
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_SRVCC_STATE_NOTIFY, iArr);
        }
        this.mRil.writeMetricsSrvcc(i2);
        this.mRil.mSrvccStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void stkCallControlAlphaNotify(int i, String str) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CC_ALPHA_NOTIFY, str);
        }
        Registrant registrant = this.mRil.mCatCcAlphaRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
        }
    }

    public void stkCallSetup(int i, long j) {
        this.mRil.processIndication(6, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CALL_SETUP, Long.valueOf(j));
        }
        Registrant registrant = this.mRil.mCatCallSetUpRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Long.valueOf(j), (Throwable) null));
        }
    }

    public String getInterfaceHash() {
        return "2c472edb830b4cf0f08316ad454bffdbcd14670f";
    }
}

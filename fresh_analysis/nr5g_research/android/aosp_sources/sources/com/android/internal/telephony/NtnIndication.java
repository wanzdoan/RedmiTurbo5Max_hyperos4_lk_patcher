package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Bundle;
import android.telephony.data.DataCallResponse;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ntnphone.AntIdentitySignalStrength;
import com.android.internal.telephony.ntnphone.HlsErrorInfo;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.ntnphone.NtnRILUtils;
import com.android.internal.telephony.ntnphone.NtnSmsSendResult;
import com.android.internal.telephony.ntnphone.NtnSmsStatusReport;
import java.util.ArrayList;
import vendor.xiaomi.hardware.satellite.AntInfoSignalStrength;
import vendor.xiaomi.hardware.satellite.EPHEM;
import vendor.xiaomi.hardware.satellite.EphemCalcInfo;
import vendor.xiaomi.hardware.satellite.HLS_ERROR_INFO;
import vendor.xiaomi.hardware.satellite.HWA_CPST;
import vendor.xiaomi.hardware.satellite.HWA_Trace;
import vendor.xiaomi.hardware.satellite.INTNRadioIndication;
import vendor.xiaomi.hardware.satellite.JS_TXPWR;
import vendor.xiaomi.hardware.satellite.NtnPosInfo;
import vendor.xiaomi.hardware.satellite.NtnTtlogInfo;
import vendor.xiaomi.hardware.satellite.SetupDataCallResult;
import vendor.xiaomi.hardware.satellite.SignalStrength;
import vendor.xiaomi.hardware.satellite.SmsSendResult;
import vendor.xiaomi.hardware.satellite.SmsStatusReport;

/* JADX INFO: loaded from: classes.dex */
public class NtnIndication extends INTNRadioIndication.Stub {
    NtnRIL mRil;
    ArrayList responseList;

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void BandFrequency(int i, int i2, int i3) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void EPHEMReport(int i, EPHEM ephem) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void ETWSReport(int i, String str) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void ImsRegistrationState(int i, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void ephemCalcRequest(int i, EphemCalcInfo ephemCalcInfo) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public int getInterfaceVersion() {
        return 3;
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void hwaCPSTReport(int i, HWA_CPST hwa_cpst) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void hwaTraceReport(int i, HWA_Trace hWA_Trace) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void minRangeForGPS(int i, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void moDisconnectCause(int i, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void nitzTimeReceived(int i, String str, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void requestStartEPHEMResult(int i, int i2, int i3) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void requestStopEPHEMResult(int i, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void voiceControlClose(int i, int i2) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void voiceVersionQuery(int i, int i2) {
    }

    public NtnIndication(NtnRIL ntnRIL) {
        this.mRil = ntnRIL;
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void radioStateChanged(int i, int i2) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(1000);
        int iConvertHalRadioState = NtnRILUtils.convertHalRadioState(i2);
        this.mRil.riljLog("NtnIndication radioStateChanged state = " + iConvertHalRadioState);
        this.mRil.setRadioState(iConvertHalRadioState, false);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void callStateChanged(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(1001);
        this.mRil.mCallStateRegistrants.notifyRegistrants();
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void networkStateChanged(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(1002);
        this.mRil.mNetworkStateRegistrants.notifyRegistrants();
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void newSms(int i, byte[] bArr) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(1003);
        SmsMessage smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArr);
        Registrant registrant = this.mRil.mNtnSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, smsMessageCreateFromPdu == null ? null : new android.telephony.SmsMessage(smsMessageCreateFromPdu), (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void currentSignalStrength(int i, SignalStrength signalStrength) {
        this.mRil.processIndicationForNtn(8, i);
        android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = NtnRILUtils.convertHalSignalStrength(signalStrength);
        this.mRil.ntnUnsljLogvRet(1009, signalStrengthConvertHalSignalStrength);
        Registrant registrant = this.mRil.mSignalStrengthRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, signalStrengthConvertHalSignalStrength, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void callRing(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CALL_RING, null);
        Registrant registrant = this.mRil.mRingRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void simStatusChanged(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED);
        this.mRil.mIccStatusChangedRegistrants.notifyRegistrants();
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void imsiQuery(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMSI_QUERY);
        Registrant registrant = this.mRil.mImsiQueryRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void authQuery(int i, String str) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_AUTHENTICATION);
        Registrant registrant = this.mRil.mSimAutoRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void newSmsStatusReport(int i, SmsStatusReport smsStatusReport) {
        this.mRil.processIndicationForNtn(8, i);
        NtnSmsStatusReport ntnSmsStatusReportConvertToNtnSmsStatusReport = NtnRILUtils.convertToNtnSmsStatusReport(smsStatusReport);
        this.mRil.ntnUnsljLog(1004);
        Registrant registrant = this.mRil.mNtnStatusReportRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ntnSmsStatusReportConvertToNtnSmsStatusReport, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void newSmsSendResult(int i, SmsSendResult smsSendResult) {
        this.mRil.processIndicationForNtn(8, i);
        NtnSmsSendResult ntnSmsSendResultConvertToNtnSmsSendResult = NtnRILUtils.convertToNtnSmsSendResult(smsSendResult);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_NEW_SMS_SEND_RESULT);
        Registrant registrant = this.mRil.mNtnSmsSendResultRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ntnSmsSendResultConvertToNtnSmsSendResult, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void txPwrReport(int i, JS_TXPWR js_txpwr) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_TXPWR_REPORT);
        Registrant registrant = this.mRil.mTxPwrRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, js_txpwr, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void SnrReport(int i, int i2) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SNR_REPORT);
        Registrant registrant = this.mRil.mSnrRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void gpsQuery(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_GPS_QUERY);
        Registrant registrant = this.mRil.mGpsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void ntnTtlogInfo(int i, NtnTtlogInfo ntnTtlogInfo) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_TTLOG_INFO);
        com.android.internal.telephony.ntnphone.NtnTtlogInfo ntnTtlogInfoConvertToNtnTtlogInfo = NtnRILUtils.convertToNtnTtlogInfo(ntnTtlogInfo);
        Registrant registrant = this.mRil.mTtlogInfoRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ntnTtlogInfoConvertToNtnTtlogInfo, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void ntnPosInfo(int i, NtnPosInfo ntnPosInfo) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_POS_INFO);
        com.android.internal.telephony.ntnphone.NtnPosInfo ntnPosInfoConvertToNtnPosInfo = NtnRILUtils.convertToNtnPosInfo(ntnPosInfo);
        Registrant registrant = this.mRil.mNtnPosInfoRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, ntnPosInfoConvertToNtnPosInfo, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public String getInterfaceHash() {
        return "ffb2075644123aea70558b66153b0c951d994dcc";
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void voiceControlOpen(int i, int i2) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_VOICE_CONTROL_OPEN);
        Registrant registrant = this.mRil.mVoiceControlRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void stackErrorEventNotify(int i, HLS_ERROR_INFO hls_error_info) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STACK_ERROR_EVENT_NOTIFY);
        HlsErrorInfo hlsErrorInfoConvertToHlsErrorInfo = NtnRILUtils.convertToHlsErrorInfo(hls_error_info);
        Registrant registrant = this.mRil.mHlserrorReportRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, hlsErrorInfoConvertToHlsErrorInfo, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void imeiQuery(int i) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMEI_QUERY);
        Registrant registrant = this.mRil.mImeiQueryRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void dataCallListChanged(int i, SetupDataCallResult setupDataCallResult) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_DATA_CALL_LIST_CHANGED);
        DataCallResponse dataCallResponseConvertHalDataCallResult = NtnRILUtils.convertHalDataCallResult(setupDataCallResult);
        ArrayList arrayList = new ArrayList();
        this.responseList = arrayList;
        arrayList.add(dataCallResponseConvertHalDataCallResult);
        Registrant registrant = this.mRil.mDataCallListChangedRegistrants;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, this.responseList, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void onChannelStateChanged(int i, int i2) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_CHANNEL_STATE_INFO);
        this.mRil.riljLog("NtnIndication onChannelStateChanged channelState = " + i2);
        RegistrantList registrantList = this.mRil.mChannelStateRegistrantList;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void currentAntInfoSignalStrength(int i, AntInfoSignalStrength antInfoSignalStrength) {
        this.mRil.processIndicationForNtn(8, i);
        ArrayList<AntIdentitySignalStrength> arrayListConvertHalAntInfoSignalStrength = NtnRILUtils.convertHalAntInfoSignalStrength(antInfoSignalStrength);
        this.mRil.ntnUnsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_ANT_INFO_SIGNAL_STRENGTH, arrayListConvertHalAntInfoSignalStrength);
        Registrant registrant = this.mRil.mAntInfoSignalStrengthRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, arrayListConvertHalAntInfoSignalStrength, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void unsolHighPenetrationStatusChanged(int i, int i2, int i3) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_HIGH_PENETRATION_STATE_CHANGED);
        Registrant registrant = this.mRil.mHighPenetrationRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
        this.mRil.riljLog("NtnIndication unsolHighPenetrationStatusChanged enhancedPgingMode = " + i2 + ", rssi = " + i3);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void otaStateChanged(int i, int i2) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_OTA_STATE_CHANGED, Integer.valueOf(i2));
        RegistrantList registrantList = this.mRil.mOtaStateChangedRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioIndication
    public void unsolNtnCdcMsg(int i, int i2, byte[] bArr) {
        this.mRil.processIndicationForNtn(8, i);
        this.mRil.ntnUnsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CDC_MESSAGE, Integer.valueOf(i2));
        Bundle bundle = new Bundle();
        bundle.putInt(NtnRILUtils.NTN_CDC_MESSAGE_ID, i2);
        bundle.putByteArray(NtnRILUtils.NTN_CDC_MESSAGE_DATA, bArr);
        RegistrantList registrantList = this.mRil.mUnsolNtnCdcMsgRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, bundle, (Throwable) null));
        }
    }
}

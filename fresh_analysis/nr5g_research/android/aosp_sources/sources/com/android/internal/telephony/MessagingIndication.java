package com.android.internal.telephony;

import android.hardware.radio.messaging.CdmaSmsMessage;
import android.hardware.radio.messaging.IRadioMessagingIndication;
import android.hardware.radio.network.NetworkSecurityEvent;
import android.os.AsyncResult;
import android.telephony.SmsMessage;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.uicc.IccUtils;

/* JADX INFO: loaded from: classes.dex */
public class MessagingIndication extends IRadioMessagingIndication.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public MessagingIndication(RIL ril) {
        this.mRil = ril;
    }

    public void cdmaNewSms(int i, CdmaSmsMessage cdmaSmsMessage) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_CDMA_NEW_SMS);
        }
        SmsMessage smsMessage = new SmsMessage(RILUtils.convertHalCdmaSmsMessage(cdmaSmsMessage));
        Registrant registrant = this.mRil.mCdmaSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, smsMessage, (Throwable) null));
        }
    }

    public void cdmaRuimSmsStorageFull(int i) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL);
        }
        Registrant registrant = this.mRil.mIccSmsFullRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    public void newBroadcastSms(int i, byte[] bArr) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogvRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS, IccUtils.bytesToHexString(bArr));
        }
        Registrant registrant = this.mRil.mGsmBroadcastSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, bArr, (Throwable) null));
        }
    }

    public void newSms(int i, byte[] bArr) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1003);
        }
        com.android.internal.telephony.gsm.SmsMessage smsMessageCreateFromPdu = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(bArr);
        Registrant registrant = this.mRil.mGsmSmsRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, smsMessageCreateFromPdu == null ? null : new SmsMessage(smsMessageCreateFromPdu), (Throwable) null));
        }
    }

    public void newSmsOnSim(int i, int i2) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1005);
        }
        Registrant registrant = this.mRil.mSmsOnSimRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null));
        }
    }

    public void newSmsStatusReport(int i, byte[] bArr) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(1004);
        }
        Registrant registrant = this.mRil.mSmsStatusRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, bArr, (Throwable) null));
        }
    }

    public void simSmsStorageFull(int i) {
        this.mRil.processIndication(2, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL);
        }
        Registrant registrant = this.mRil.mIccSmsFullRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
    }

    public void newSecureSms(int i, byte[] bArr, NetworkSecurityEvent networkSecurityEvent) {
        if (this.mRil.isLogOrTrace()) {
            this.mRil.riljLog("newSecureSms: secure sms feature is disabled.");
        }
        newSms(i, bArr);
    }

    public String getInterfaceHash() {
        return "4305c82b7f2dad0f9664f0be75e4b3e3a3b19475";
    }
}

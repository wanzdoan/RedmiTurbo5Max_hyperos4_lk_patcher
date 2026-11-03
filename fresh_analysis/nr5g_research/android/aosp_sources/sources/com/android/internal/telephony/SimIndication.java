package com.android.internal.telephony;

import android.hardware.radio.sim.IRadioSimIndication;
import android.hardware.radio.sim.PhonebookRecordInfo;
import android.hardware.radio.sim.SimRefreshResult;
import android.os.AsyncResult;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.ReceivedPhonebookRecords;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SimIndication extends IRadioSimIndication.Stub {
    private final RIL mRil;

    public void cdmaSubscriptionSourceChanged(int i, int i2) {
    }

    public int getInterfaceVersion() {
        return 5;
    }

    public SimIndication(RIL ril) {
        this.mRil = ril;
    }

    public void carrierInfoForImsiEncryption(int i) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CARRIER_INFO_IMSI_ENCRYPTION, null);
        }
        this.mRil.mCarrierInfoForImsiEncryptionRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    public void simPhonebookChanged(int i) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMSI_QUERY);
        }
        this.mRil.mSimPhonebookChangedRegistrants.notifyRegistrants();
    }

    public void simPhonebookRecordsReceived(int i, byte b, PhonebookRecordInfo[] phonebookRecordInfoArr) {
        this.mRil.processIndication(5, i);
        ArrayList arrayList = new ArrayList();
        for (PhonebookRecordInfo phonebookRecordInfo : phonebookRecordInfoArr) {
            arrayList.add(RILUtils.convertHalPhonebookRecordInfo(phonebookRecordInfo));
        }
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_AUTHENTICATION, "status = " + ((int) b) + " received " + phonebookRecordInfoArr.length + " records");
        }
        this.mRil.mSimPhonebookRecordsReceivedRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ReceivedPhonebookRecords(b, arrayList), (Throwable) null));
    }

    public void simRefresh(int i, SimRefreshResult simRefreshResult) {
        this.mRil.processIndication(5, i);
        IccRefreshResponse iccRefreshResponse = new IccRefreshResponse();
        iccRefreshResponse.refreshResult = simRefreshResult.type;
        iccRefreshResponse.efId = simRefreshResult.efId;
        iccRefreshResponse.aid = simRefreshResult.aid;
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_REFRESH, iccRefreshResponse);
        }
        this.mRil.mIccRefreshRegistrants.notifyRegistrants(new AsyncResult((Object) null, iccRefreshResponse, (Throwable) null));
    }

    public void simStatusChanged(int i) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED);
        }
        this.mRil.mIccStatusChangedRegistrants.notifyRegistrants();
    }

    public void stkEventNotify(int i, String str) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY);
        }
        Registrant registrant = this.mRil.mCatEventRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
        }
    }

    public void stkProactiveCommand(int i, String str) {
        Registrant registrant;
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_PROACTIVE_COMMAND);
        }
        if (MiuiPhoneStub.get(this.mRil.mPhoneId.intValue()).notifyStkProCmdForEsimBinding(str) || (registrant = this.mRil.mCatProCmdRegistrant) == null) {
            return;
        }
        registrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
    }

    public void stkSessionEnd(int i) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_SESSION_END);
        }
        Registrant registrant = this.mRil.mCatSessionEndRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    public void subscriptionStatusChanged(int i, boolean z) {
        this.mRil.processIndication(5, i);
        int[] iArr = {z ? 1 : 0};
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED, iArr);
        }
        this.mRil.mSubscriptionStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void uiccApplicationsEnablementChanged(int i, boolean z) {
        this.mRil.processIndication(5, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_APPLICATIONS_ENABLEMENT_CHANGED, Boolean.valueOf(z));
        }
        this.mRil.mUiccApplicationsEnablementRegistrants.notifyResult(Boolean.valueOf(z));
    }

    public String getInterfaceHash() {
        return "5aff83e927c9aeca4d1a1d5e2114d87b7d069b52";
    }
}

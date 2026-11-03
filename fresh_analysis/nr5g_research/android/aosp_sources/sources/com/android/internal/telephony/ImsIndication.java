package com.android.internal.telephony;

import android.hardware.radio.ims.ConnectionFailureInfo;
import android.hardware.radio.ims.IRadioImsIndication;
import android.os.AsyncResult;
import com.android.internal.telephony.ntnphone.NtnRILConstants;

/* JADX INFO: loaded from: classes.dex */
public class ImsIndication extends IRadioImsIndication.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 4;
    }

    public ImsIndication(RIL ril) {
        this.mRil = ril;
    }

    public String getInterfaceHash() {
        return "2f451a440a404c0eda2001bd542876005bd3a5ac";
    }

    public void onConnectionSetupFailure(int i, int i2, ConnectionFailureInfo connectionFailureInfo) {
        this.mRil.processIndication(7, i);
        Object[] objArr = {Integer.valueOf(i2), new android.telephony.ims.feature.ConnectionFailureInfo(RILUtils.convertHalConnectionFailureReason(connectionFailureInfo.failureReason), connectionFailureInfo.causeCode, connectionFailureInfo.waitTimeMillis)};
        this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_CONNECTION_SETUP_FAILURE, objArr);
        this.mRil.mConnectionSetupFailureRegistrants.notifyRegistrants(new AsyncResult((Object) null, objArr, (Throwable) null));
    }

    public void notifyAnbr(int i, int i2, int i3, int i4) {
        this.mRil.processIndication(7, i);
        int[] iArr = {i2, i3, i4};
        this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_NOTIFY_ANBR, iArr);
        this.mRil.mNotifyAnbrRegistrants.notifyRegistrants(new AsyncResult((Object) null, iArr, (Throwable) null));
    }

    public void triggerImsDeregistration(int i, int i2) {
        this.mRil.processIndication(7, i);
        this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_TRIGGER_IMS_DEREGISTRATION, Integer.valueOf(i2));
        this.mRil.mTriggerImsDeregistrationRegistrants.notifyRegistrants(new AsyncResult((Object) null, new int[]{RILUtils.convertHalDeregistrationReason(i2)}, (Throwable) null));
    }
}

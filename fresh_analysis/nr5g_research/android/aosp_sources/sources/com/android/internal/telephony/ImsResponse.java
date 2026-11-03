package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.ims.ConnectionFailureInfo;
import android.hardware.radio.ims.IRadioImsResponse;

/* JADX INFO: loaded from: classes.dex */
public class ImsResponse extends IRadioImsResponse.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 4;
    }

    public ImsResponse(RIL ril) {
        this.mRil = ril;
    }

    public String getInterfaceHash() {
        return "2f451a440a404c0eda2001bd542876005bd3a5ac";
    }

    public void setSrvccCallInfoResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void updateImsRegistrationInfoResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void startImsTrafficResponse(RadioResponseInfo radioResponseInfo, ConnectionFailureInfo connectionFailureInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(7, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            Object[] objArr = {new Integer(-1), null};
            if (radioResponseInfo.error == 0) {
                if (connectionFailureInfo != null) {
                    objArr[1] = new android.telephony.ims.feature.ConnectionFailureInfo(RILUtils.convertHalConnectionFailureReason(connectionFailureInfo.failureReason), connectionFailureInfo.causeCode, connectionFailureInfo.waitTimeMillis);
                }
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, objArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, objArr);
        }
    }

    public void stopImsTrafficResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void triggerEpsFallbackResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void sendAnbrQueryResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void updateImsCallStatusResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }

    public void updateAllowedServicesResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(7, this.mRil, radioResponseInfo);
    }
}

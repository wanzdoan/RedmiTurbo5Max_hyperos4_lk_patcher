package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.data.IRadioDataResponse;
import android.hardware.radio.data.KeepaliveStatus;
import android.hardware.radio.data.SetupDataCallResult;
import android.hardware.radio.data.SlicingConfig;
import android.os.AsyncResult;
import android.telephony.data.DataCallResponse;
import android.telephony.data.NetworkSlicingConfig;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class DataResponse extends IRadioDataResponse.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public DataResponse(RIL ril) {
        this.mRil = ril;
    }

    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    public void allocatePduSessionIdResponse(RadioResponseInfo radioResponseInfo, int i) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Integer.valueOf(i));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Integer.valueOf(i));
        }
    }

    public void cancelHandoverResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void deactivateDataCallResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void getDataCallListResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult[] setupDataCallResultArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<DataCallResponse> arrayListConvertHalDataCallResultList = RILUtils.convertHalDataCallResultList(setupDataCallResultArr);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalDataCallResultList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayListConvertHalDataCallResultList);
        }
    }

    public void getSlicingConfigResponse(RadioResponseInfo radioResponseInfo, SlicingConfig slicingConfig) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            NetworkSlicingConfig networkSlicingConfigConvertHalSlicingConfig = RILUtils.convertHalSlicingConfig(slicingConfig);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, networkSlicingConfigConvertHalSlicingConfig);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, networkSlicingConfigConvertHalSlicingConfig);
        }
    }

    public void releasePduSessionIdResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setDataAllowedResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setDataProfileResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setDataThrottlingResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setInitialAttachApnResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setupDataCallResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            DataCallResponse dataCallResponseConvertHalDataCallResult = RILUtils.convertHalDataCallResult(setupDataCallResult);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, dataCallResponseConvertHalDataCallResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, dataCallResponseConvertHalDataCallResult);
        }
    }

    public void startHandoverResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void startKeepaliveResponse(RadioResponseInfo radioResponseInfo, KeepaliveStatus keepaliveStatus) {
        com.android.internal.telephony.data.KeepaliveStatus keepaliveStatus2;
        com.android.internal.telephony.data.KeepaliveStatus keepaliveStatus3;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        try {
            int i = radioResponseInfo.error;
            if (i == 0) {
                int iConvertHalKeepaliveStatusCode = RILUtils.convertHalKeepaliveStatusCode(keepaliveStatus.code);
                if (iConvertHalKeepaliveStatusCode < 0) {
                    keepaliveStatus2 = new com.android.internal.telephony.data.KeepaliveStatus(1);
                } else {
                    keepaliveStatus2 = new com.android.internal.telephony.data.KeepaliveStatus(keepaliveStatus.sessionHandle, iConvertHalKeepaliveStatusCode);
                }
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, keepaliveStatus2);
                keepaliveStatus3 = keepaliveStatus2;
            } else if (i == 6) {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(1);
            } else if (i == 42) {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(2);
            } else {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(3);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, keepaliveStatus3);
        } catch (Throwable th) {
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
            throw th;
        }
    }

    public void stopKeepaliveResponse(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(1, radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        try {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, null);
            }
        } finally {
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    public void setUserDataEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void setUserDataRoamingEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public void notifyImsDataNetworkResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(1, this.mRil, radioResponseInfo);
    }

    public String getInterfaceHash() {
        return "6acb69b7e1400ebd9d4e169667e93f778d3ec27c";
    }

    public void dataCallListUpdated(int i, SetupDataCallResult[] setupDataCallResultArr) {
        this.mRil.processIndication(1, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(1112, setupDataCallResultArr);
        }
        ArrayList<DataCallResponse> arrayListConvertHalDataCallResultList = RILUtils.convertHalDataCallResultList(setupDataCallResultArr);
        RegistrantList registrantList = this.mRil.mDataCallListUpdatedRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, arrayListConvertHalDataCallResultList, (Throwable) null));
        }
    }
}

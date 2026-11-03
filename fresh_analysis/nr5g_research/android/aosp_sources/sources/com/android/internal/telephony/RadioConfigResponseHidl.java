package com.android.internal.telephony;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.config.V1_0.SimSlotStatus;
import android.hardware.radio.config.V1_1.ModemsConfig;
import android.hardware.radio.config.V1_1.PhoneCapability;
import android.hardware.radio.config.V1_3.IRadioConfigResponse;
import android.os.RemoteException;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class RadioConfigResponseHidl extends IRadioConfigResponse.Stub {
    private final HalVersion mHalVersion;
    private final RadioConfig mRadioConfig;

    public RadioConfigResponseHidl(RadioConfig radioConfig, HalVersion halVersion) {
        this.mRadioConfig = radioConfig;
        this.mHalVersion = halVersion;
    }

    @Override // android.hardware.radio.config.V1_0.IRadioConfigResponse
    public void getSimSlotsStatusResponse(RadioResponseInfo radioResponseInfo, ArrayList<SimSlotStatus> arrayList) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<IccSlotStatus> arrayListConvertHalSlotStatus = RILUtils.convertHalSlotStatus(arrayList);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalSlotStatus);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " " + arrayListConvertHalSlotStatus.toString());
                return;
            }
            rILRequestProcessResponse.onError(i, arrayListConvertHalSlotStatus);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getSimSlotsStatusResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_0.IRadioConfigResponse
    public void setSimSlotsMappingResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, null);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, null);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("setSimSlotsMappingResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_1.IRadioConfigResponse
    public void getPhoneCapabilityResponse(RadioResponseInfo radioResponseInfo, PhoneCapability phoneCapability) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.PhoneCapability phoneCapabilityConvertHalPhoneCapability = RILUtils.convertHalPhoneCapability(this.mRadioConfig.getDeviceNrCapabilities(), phoneCapability);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, phoneCapabilityConvertHalPhoneCapability);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " " + phoneCapabilityConvertHalPhoneCapability.toString());
                return;
            }
            rILRequestProcessResponse.onError(i, phoneCapabilityConvertHalPhoneCapability);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getPhoneCapabilityResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_1.IRadioConfigResponse
    public void setPreferredDataModemResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, null);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, null);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("setPreferredDataModemResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_1.IRadioConfigResponse
    public void setModemsConfigResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Integer.valueOf(rILRequestProcessResponse.mRequest));
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, null);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("setModemsConfigResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_1.IRadioConfigResponse
    public void getModemsConfigResponse(RadioResponseInfo radioResponseInfo, ModemsConfig modemsConfig) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, modemsConfig);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, modemsConfig);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getModemsConfigResponse: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_2.IRadioConfigResponse
    public void getSimSlotsStatusResponse_1_2(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.config.V1_2.SimSlotStatus> arrayList) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<IccSlotStatus> arrayListConvertHalSlotStatus = RILUtils.convertHalSlotStatus(arrayList);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalSlotStatus);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " " + arrayListConvertHalSlotStatus.toString());
                return;
            }
            rILRequestProcessResponse.onError(i, arrayListConvertHalSlotStatus);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getSimSlotsStatusResponse_1_2: Error " + radioResponseInfo.toString());
    }

    @Override // android.hardware.radio.config.V1_3.IRadioConfigResponse
    public void getHalDeviceCapabilitiesResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, boolean z) throws RemoteException {
        RILRequest rILRequestProcessResponse_1_6 = this.mRadioConfig.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            Set<String> caps = RILUtils.getCaps(this.mHalVersion, z);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse_1_6.mResult, caps);
                logd(rILRequestProcessResponse_1_6, RILUtils.requestToString(rILRequestProcessResponse_1_6.mRequest));
                return;
            }
            rILRequestProcessResponse_1_6.onError(i, caps);
            loge(rILRequestProcessResponse_1_6, RILUtils.requestToString(rILRequestProcessResponse_1_6.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getHalDeviceCapabilities: Error " + radioResponseInfo.toString());
    }

    private static void logd(String str) {
        Rlog.d("RadioConfigResponse", str);
    }

    private static void loge(String str) {
        Rlog.e("RadioConfigResponse", str);
    }

    private static void logd(RILRequest rILRequest, String str) {
        logd(rILRequest.serialString() + "< " + str);
    }

    private static void loge(RILRequest rILRequest, String str) {
        loge(rILRequest.serialString() + "< " + str);
    }
}

package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.config.IRadioConfigResponse;
import android.hardware.radio.config.PhoneCapability;
import android.hardware.radio.config.SimSlotStatus;
import android.hardware.radio.config.SimTypeInfo;
import android.os.RemoteException;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class RadioConfigResponseAidl extends IRadioConfigResponse.Stub {
    private final HalVersion mHalVersion;
    private final RadioConfig mRadioConfig;

    public int getInterfaceVersion() {
        return 5;
    }

    public RadioConfigResponseAidl(RadioConfig radioConfig, HalVersion halVersion) {
        this.mRadioConfig = radioConfig;
        this.mHalVersion = halVersion;
    }

    public void getHalDeviceCapabilitiesResponse(RadioResponseInfo radioResponseInfo, boolean z) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            Set<String> caps = RILUtils.getCaps(this.mHalVersion, z);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, caps);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, caps);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getHalDeviceCapabilities: Error " + radioResponseInfo.toString());
    }

    public void getNumOfLiveModemsResponse(RadioResponseInfo radioResponseInfo, byte b) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Byte.valueOf(b));
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest));
                return;
            }
            rILRequestProcessResponse.onError(i, Byte.valueOf(b));
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getNumOfLiveModemsResponse: Error " + radioResponseInfo.toString());
    }

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

    public void getSimultaneousCallingSupportResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<Integer> arrayListPrimitiveArrayToArrayList = RILUtils.primitiveArrayToArrayList(iArr);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListPrimitiveArrayToArrayList);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " " + arrayListPrimitiveArrayToArrayList);
                return;
            }
            rILRequestProcessResponse.onError(i, iArr);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getSimultaneousCallingSupportResponse: Error " + radioResponseInfo.toString());
    }

    public void getSimSlotsStatusResponse(RadioResponseInfo radioResponseInfo, SimSlotStatus[] simSlotStatusArr) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<IccSlotStatus> arrayListConvertHalSlotStatus = RILUtils.convertHalSlotStatus(simSlotStatusArr);
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

    public void setNumOfLiveModemsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
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

    public void getSimTypeInfoResponse(RadioResponseInfo radioResponseInfo, SimTypeInfo[] simTypeInfoArr) throws RemoteException {
        RILRequest rILRequestProcessResponse = this.mRadioConfig.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<com.android.internal.telephony.uicc.SimTypeInfo> arrayListConvertAidlSimTypeInfo = RILUtils.convertAidlSimTypeInfo(simTypeInfoArr);
            int i = radioResponseInfo.error;
            if (i == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertAidlSimTypeInfo);
                logd(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " " + arrayListConvertAidlSimTypeInfo.toString());
                return;
            }
            rILRequestProcessResponse.onError(i, null);
            loge(rILRequestProcessResponse, RILUtils.requestToString(rILRequestProcessResponse.mRequest) + " error " + radioResponseInfo.error);
            return;
        }
        loge("getSimTypeInfoResponse: Error " + radioResponseInfo.toString());
    }

    public void setSimTypeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
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
        loge("setSimTypeResponse: Error " + radioResponseInfo.toString());
    }

    public void rebootModemResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
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
        loge("rebootModemResponse: Error " + radioResponseInfo.toString());
    }

    private static void logd(String str) {
        Rlog.d("RadioConfigResponseAidl", str);
    }

    private static void loge(String str) {
        Rlog.e("RadioConfigResponseAidl", str);
    }

    private static void logd(RILRequest rILRequest, String str) {
        logd(rILRequest.serialString() + "< " + str);
    }

    private static void loge(RILRequest rILRequest, String str) {
        loge(rILRequest.serialString() + "< " + str);
    }

    public String getInterfaceHash() {
        return "b9f08521b4288bc71112cdeb47220af1ff758383";
    }
}

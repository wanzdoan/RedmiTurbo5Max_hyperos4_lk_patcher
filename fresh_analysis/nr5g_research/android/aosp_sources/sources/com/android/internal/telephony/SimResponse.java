package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.sim.CardStatus;
import android.hardware.radio.sim.CarrierRestrictions;
import android.hardware.radio.sim.IRadioSimResponse;
import android.hardware.radio.sim.IccIoResult;
import android.hardware.radio.sim.PhonebookCapacity;
import android.telephony.CarrierRestrictionRules;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.AdnCapacity;
import com.android.internal.telephony.uicc.IccCardStatus;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SimResponse extends IRadioSimResponse.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public SimResponse(RIL ril) {
        this.mRil = ril;
    }

    private void responseIccIo(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            com.android.internal.telephony.uicc.IccIoResult iccIoResult2 = new com.android.internal.telephony.uicc.IccIoResult(iccIoResult.sw1, iccIoResult.sw2, iccIoResult.simResponse);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, iccIoResult2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iccIoResult2);
        }
    }

    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    public void areUiccApplicationsEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void changeIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void changeIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void enableUiccApplicationsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void getAllowedCarriersResponse(RadioResponseInfo radioResponseInfo, CarrierRestrictions carrierRestrictions, int i) {
        CarrierRestrictionRules carrierRestrictionRulesBuild;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        boolean zGreaterOrEqual = this.mRil.getHalVersion(5).greaterOrEqual(RIL.RADIO_HAL_VERSION_2_2);
        if (rILRequestProcessResponse == null) {
            return;
        }
        int iConvertAidlSimLockMultiSimPolicy = RILUtils.convertAidlSimLockMultiSimPolicy(i);
        int i2 = !carrierRestrictions.allowedCarriersPrioritized ? 1 : 0;
        if (zGreaterOrEqual) {
            carrierRestrictionRulesBuild = CarrierRestrictionRules.newBuilder().setAllowedCarriers(RILUtils.convertAidlCarrierInfoListToHalCarrierList(carrierRestrictions.allowedCarrierInfoList)).setExcludedCarriers(RILUtils.convertAidlCarrierInfoListToHalCarrierList(carrierRestrictions.excludedCarrierInfoList)).setDefaultCarrierRestriction(i2).setMultiSimPolicy(iConvertAidlSimLockMultiSimPolicy).setCarrierRestrictionStatus(carrierRestrictions.status).setAllowedCarrierInfo(RILUtils.convertAidlCarrierInfoList(carrierRestrictions.allowedCarrierInfoList)).setExcludedCarrierInfo(RILUtils.convertAidlCarrierInfoList(carrierRestrictions.excludedCarrierInfoList)).setCarrierLockInfoFeature(true).build();
        } else if (this.mRil.getHalVersion(5).equals(RIL.RADIO_HAL_VERSION_2_1)) {
            carrierRestrictionRulesBuild = CarrierRestrictionRules.newBuilder().setAllowedCarriers(RILUtils.convertHalCarrierList(carrierRestrictions.allowedCarriers)).setExcludedCarriers(RILUtils.convertHalCarrierList(carrierRestrictions.excludedCarriers)).setDefaultCarrierRestriction(i2).setMultiSimPolicy(iConvertAidlSimLockMultiSimPolicy).setCarrierRestrictionStatus(carrierRestrictions.status).build();
        } else {
            carrierRestrictionRulesBuild = CarrierRestrictionRules.newBuilder().setAllowedCarriers(RILUtils.convertHalCarrierList(carrierRestrictions.allowedCarriers)).setExcludedCarriers(RILUtils.convertHalCarrierList(carrierRestrictions.excludedCarriers)).setDefaultCarrierRestriction(i2).setMultiSimPolicy(iConvertAidlSimLockMultiSimPolicy).build();
        }
        if (radioResponseInfo.error == 0) {
            RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, carrierRestrictionRulesBuild);
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, carrierRestrictionRulesBuild);
    }

    public void getCdmaSubscriptionResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3, String str4, String str5) {
        RadioResponse.responseStrings(5, this.mRil, radioResponseInfo, str, str2, str3, str4, str5);
    }

    public void getCdmaSubscriptionSourceResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void getFacilityLockForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void getIccCardStatusResponse(RadioResponseInfo radioResponseInfo, CardStatus cardStatus) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            IccCardStatus iccCardStatusConvertHalCardStatus = RILUtils.convertHalCardStatus(cardStatus);
            this.mRil.riljLog("responseIccCardStatus: from AIDL: " + iccCardStatusConvertHalCardStatus);
            if (radioResponseInfo.error == 0) {
                QtiSimUtils.updatePersoTempUnlockStatusIfRequired(iccCardStatusConvertHalCardStatus);
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, iccCardStatusConvertHalCardStatus);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iccCardStatusConvertHalCardStatus);
        }
    }

    public void getImsiForAppResponse(RadioResponseInfo radioResponseInfo, String str) {
        RadioResponse.responseString(5, this.mRil, radioResponseInfo, str);
    }

    public void getSimPhonebookCapacityResponse(RadioResponseInfo radioResponseInfo, PhonebookCapacity phonebookCapacity) {
        AdnCapacity adnCapacityConvertHalPhonebookCapacity = RILUtils.convertHalPhonebookCapacity(phonebookCapacity);
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, adnCapacityConvertHalPhonebookCapacity);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, adnCapacityConvertHalPhonebookCapacity);
        }
    }

    public void getSimPhonebookRecordsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void iccCloseLogicalChannelResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void iccCloseLogicalChannelWithSessionInfoResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void iccIoForAppResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void iccOpenLogicalChannelResponse(RadioResponseInfo radioResponseInfo, int i, byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i));
        for (byte b : bArr) {
            arrayList.add(Integer.valueOf(b));
        }
        RadioResponse.responseIntArrayList(5, this.mRil, radioResponseInfo, arrayList);
    }

    public void iccTransmitApduBasicChannelResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void iccTransmitApduLogicalChannelResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void reportStkServiceIsRunningResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void requestIccSimAuthenticationResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            com.android.internal.telephony.uicc.IccIoResult iccIoResult2 = new com.android.internal.telephony.uicc.IccIoResult(iccIoResult.sw1, iccIoResult.sw2, TextUtils.isEmpty(iccIoResult.simResponse) ? null : iccIoResult.simResponse.getBytes());
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, iccIoResult2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iccIoResult2);
        }
    }

    public void requestIsimAuthenticationResponse(RadioResponseInfo radioResponseInfo, String str) {
        throw new RuntimeException("Inexplicable response received for requestIsimAuthentication");
    }

    public void sendEnvelopeResponse(RadioResponseInfo radioResponseInfo, String str) {
        RadioResponse.responseString(5, this.mRil, radioResponseInfo, str);
    }

    public void sendEnvelopeWithStatusResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void sendTerminalResponseToSimResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void setAllowedCarriersResponse(RadioResponseInfo radioResponseInfo) {
        int i;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(5, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            this.mRil.riljLog("setAllowedCarriersResponse - error = " + radioResponseInfo.error);
            if (radioResponseInfo.error == 0) {
                i = 0;
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, 0);
            } else {
                i = 2;
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Integer.valueOf(i));
        }
    }

    public void setCarrierInfoForImsiEncryptionResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void setCdmaSubscriptionSourceResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void setFacilityLockForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void setSimCardPowerResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void setUiccSubscriptionResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(5, this.mRil, radioResponseInfo);
    }

    public void supplyIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void supplyIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void supplyIccPuk2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void supplyIccPukForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public void supplySimDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i, int i2) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i, i2);
    }

    public void updateSimPhonebookRecordsResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(5, this.mRil, radioResponseInfo, i);
    }

    public String getInterfaceHash() {
        return "5aff83e927c9aeca4d1a1d5e2114d87b7d069b52";
    }
}

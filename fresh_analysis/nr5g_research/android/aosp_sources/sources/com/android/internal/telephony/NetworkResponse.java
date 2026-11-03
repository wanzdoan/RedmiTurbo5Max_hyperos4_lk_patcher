package com.android.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.network.BarringInfo;
import android.hardware.radio.network.CellIdentity;
import android.hardware.radio.network.CellInfo;
import android.hardware.radio.network.EmergencyRegResult;
import android.hardware.radio.network.IRadioNetworkResponse;
import android.hardware.radio.network.OperatorInfo;
import android.hardware.radio.network.RadioAccessSpecifier;
import android.hardware.radio.network.RegStateResult;
import android.hardware.radio.network.SignalStrength;
import android.os.AsyncResult;
import android.os.RemoteException;
import android.telephony.EmergencyRegistrationResult;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NetworkResponse extends IRadioNetworkResponse.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public NetworkResponse(RIL ril) {
        this.mRil = ril;
    }

    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    public void getAllowedNetworkTypesBitmapResponse(RadioResponseInfo radioResponseInfo, int i) {
        int iConvertHalNetworkTypeBitMask = RILUtils.convertHalNetworkTypeBitMask(i);
        RIL ril = this.mRil;
        ril.mAllowedNetworkTypesBitmask = iConvertHalNetworkTypeBitMask;
        RadioResponse.responseInts(4, ril, radioResponseInfo, iConvertHalNetworkTypeBitMask);
    }

    public void getAvailableBandModesResponse(RadioResponseInfo radioResponseInfo, int[] iArr) {
        RadioResponse.responseIntArrayList(4, this.mRil, radioResponseInfo, RILUtils.primitiveArrayToArrayList(iArr));
    }

    public void getAvailableNetworksResponse(RadioResponseInfo radioResponseInfo, OperatorInfo[] operatorInfoArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList = new ArrayList();
            for (OperatorInfo operatorInfo : operatorInfoArr) {
                arrayList.add(new OperatorInfo(operatorInfo.alphaLong, operatorInfo.alphaShort, operatorInfo.operatorNumeric, RILUtils.convertHalOperatorStatus(operatorInfo.status)));
            }
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayList);
        }
    }

    public void getBarringInfoResponse(RadioResponseInfo radioResponseInfo, CellIdentity cellIdentity, BarringInfo[] barringInfoArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.BarringInfo barringInfo = new android.telephony.BarringInfo(RILUtils.convertHalCellIdentity(cellIdentity), RILUtils.convertHalBarringInfoList(barringInfoArr));
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, barringInfo);
                this.mRil.mBarringInfoChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, barringInfo, (Throwable) null));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, barringInfo);
        }
    }

    public void getCdmaRoamingPreferenceResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, i);
    }

    public void getCellInfoListResponse(RadioResponseInfo radioResponseInfo, CellInfo[] cellInfoArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<android.telephony.CellInfo> arrayListConvertHalCellInfoList = RILUtils.convertHalCellInfoList(cellInfoArr);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalCellInfoList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayListConvertHalCellInfoList);
        }
    }

    public void getDataRegistrationStateResponse(RadioResponseInfo radioResponseInfo, RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, regStateResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, regStateResult);
        }
    }

    public void getImsRegistrationStateResponse(RadioResponseInfo radioResponseInfo, boolean z, int i) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, z ? 1 : 0, i == 0 ? 1 : 2);
    }

    public void getNetworkSelectionModeResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, z ? 1 : 0);
    }

    public void getOperatorResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3) {
        RadioResponse.responseStrings(4, this.mRil, radioResponseInfo, str, str2, str3);
    }

    public void getSignalStrengthResponse(RadioResponseInfo radioResponseInfo, SignalStrength signalStrength) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = RILUtils.convertHalSignalStrength(signalStrength);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, signalStrengthConvertHalSignalStrength);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, signalStrengthConvertHalSignalStrength);
        }
    }

    public void getSystemSelectionChannelsResponse(RadioResponseInfo radioResponseInfo, RadioAccessSpecifier[] radioAccessSpecifierArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList = new ArrayList();
            for (RadioAccessSpecifier radioAccessSpecifier : radioAccessSpecifierArr) {
                arrayList.add(RILUtils.convertHalRadioAccessSpecifier(radioAccessSpecifier));
            }
            this.mRil.riljLog("getSystemSelectionChannelsResponse: from AIDL: " + arrayList);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, arrayList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayList);
        }
    }

    public void getVoiceRadioTechnologyResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, i);
    }

    public void getVoiceRegistrationStateResponse(RadioResponseInfo radioResponseInfo, RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, regStateResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, regStateResult);
        }
    }

    public void isNrDualConnectivityEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setAllowedNetworkTypesBitmapResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setBandModeResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setBarringPasswordResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setCdmaRoamingPreferenceResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setCellInfoListRateResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setIndicationFilterResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setLinkCapacityReportingCriteriaResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setLocationUpdatesResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setNetworkSelectionModeAutomaticResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setNetworkSelectionModeManualResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setNrDualConnectivityStateResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setSignalStrengthReportingCriteriaResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setSuppServiceNotificationsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setSystemSelectionChannelsResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void startNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            NetworkScanResult networkScanResult = null;
            if (radioResponseInfo.error == 0) {
                NetworkScanResult networkScanResult2 = new NetworkScanResult(1, 0, (List) null);
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, networkScanResult2);
                networkScanResult = networkScanResult2;
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, networkScanResult);
        }
    }

    public void stopNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            NetworkScanResult networkScanResult = null;
            if (radioResponseInfo.error == 0) {
                NetworkScanResult networkScanResult2 = new NetworkScanResult(1, 0, (List) null);
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, networkScanResult2);
                networkScanResult = networkScanResult2;
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, networkScanResult);
        }
    }

    public void supplyNetworkDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, i);
    }

    public void setUsageSettingResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void getUsageSettingResponse(RadioResponseInfo radioResponseInfo, int i) {
        RadioResponse.responseInts(4, this.mRil, radioResponseInfo, i);
    }

    public void setEmergencyModeResponse(RadioResponseInfo radioResponseInfo, EmergencyRegResult emergencyRegResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            EmergencyRegistrationResult emergencyRegistrationResultConvertHalEmergencyRegResult = RILUtils.convertHalEmergencyRegResult(emergencyRegResult);
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, emergencyRegistrationResultConvertHalEmergencyRegResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, emergencyRegistrationResultConvertHalEmergencyRegResult);
        }
    }

    public void triggerEmergencyNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void exitEmergencyModeResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void cancelEmergencyNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setNullCipherAndIntegrityEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void isNullCipherAndIntegrityEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void isN1ModeEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setN1ModeEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setCellularIdentifierTransparencyEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void isCellularIdentifierTransparencyEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setSecurityAlgorithmsUpdatedEnabledResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void isSecurityAlgorithmsUpdatedEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setSatellitePlmnResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void setSatelliteEnabledForCarrierResponse(RadioResponseInfo radioResponseInfo) {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void isSatelliteEnabledForCarrierResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(4, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setSatelliteNetworkInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void enablePrioritizedNetworkScanResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void disablePrioritizedNetworkScanResponse(RadioResponseInfo radioResponseInfo) throws RemoteException {
        RadioResponse.responseVoid(4, this.mRil, radioResponseInfo);
    }

    public void getSupportedNetworkAlertCategoriesResponse(RadioResponseInfo radioResponseInfo, int[] iArr) {
        RadioResponse.responseIntArrayList(4, this.mRil, radioResponseInfo, RILUtils.primitiveArrayToArrayList(iArr));
    }

    public String getInterfaceHash() {
        return "d30e321bff565b6a44f81ce4113979c4844a61a3";
    }
}

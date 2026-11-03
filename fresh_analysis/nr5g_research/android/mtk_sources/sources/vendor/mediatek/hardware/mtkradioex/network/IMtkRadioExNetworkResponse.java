package vendor.mediatek.hardware.mtkradioex.network;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExNetworkResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$network$IMtkRadioExNetworkResponse".replace('$', '.');
    public static final String HASH = "583aefd4764eb70d99325928242c9ced29a9c0ee";
    public static final int VERSION = 1;

    void abortFemtocellListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cancelAvailableNetworksResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cfgA2offsetResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cfgB1offsetResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void clearLteAvailableFileResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void deactivateNrScgCommunicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableCAPlusBandWidthFilterResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableSCGfailureResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void get4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getAllBandModeResponse(RadioResponseInfo radioResponseInfo, BandModeInfo bandModeInfo) throws RemoteException;

    void getApcInfoResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getAvailableNetworksWithActResponse(RadioResponseInfo radioResponseInfo, OperatorInfoWithAct[] operatorInfoWithActArr) throws RemoteException;

    void getBandModeResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getBandPriorityListResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getCALinkCapabilityListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo, boolean z) throws RemoteException;

    void getCaBandModeResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getCampedFemtoCellInfoResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getCurrentPOLListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getDeactivateNrScgCommunicationResponse(RadioResponseInfo radioResponseInfo, int i, int i2) throws RemoteException;

    void getDisable2GResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getFemtocellListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getIWlanRegistrationStateResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getLte1xRttCellListResponse(RadioResponseInfo radioResponseInfo, Lte1xRttCellInfo[] lte1xRttCellInfoArr) throws RemoteException;

    void getLteBsrTimerResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteDataResponse(RadioResponseInfo radioResponseInfo, LteData lteData) throws RemoteException;

    void getLteRRCStateResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteReleaseVersionResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteScanDurationResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getPOLCapabilityResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getPlmnNameFromSE13TableResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void getQamEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    void getRoamingEnableResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo radioResponseInfo, SignalStrengthWithWcdmaEcio signalStrengthWithWcdmaEcio) throws RemoteException;

    void getSuggestedPlmnListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getTOEInfoResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3) throws RemoteException;

    void getTm9EnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void selectFemtocellResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void set4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setApcModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBandPriorityListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCarrierAggregationModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setDisable2GResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteBandEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteBsrTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteReleaseVersionResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteScanDurationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNROptionResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNrBandModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setPOLEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setQamEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setRoamingEnableResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSearchRatResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSearchStoredFreqInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setServiceStateToModemResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTm9EnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExNetworkResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void abortFemtocellListResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cancelAvailableNetworksResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cfgA2offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cfgB1offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void clearLteAvailableFileResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setCarrierAggregationModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void enableSCGfailureResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void get4x4MimoEnabledResponse(RadioResponseInfo info, int enabled_bitmask) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getAllBandModeResponse(RadioResponseInfo info, BandModeInfo data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getApcInfoResponse(RadioResponseInfo info, int[] cellInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getAvailableNetworksWithActResponse(RadioResponseInfo info, OperatorInfoWithAct[] networkInfosWithAct) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getBandPriorityListResponse(RadioResponseInfo info, int[] bandPriList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCALinkCapabilityListResponse(RadioResponseInfo info, String[] linkCapabilityList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCALinkEnableStatusResponse(RadioResponseInfo info, boolean status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCaBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCampedFemtoCellInfoResponse(RadioResponseInfo info, String[] femto) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, String[] polList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getFemtocellListResponse(RadioResponseInfo responseInfo, String[] femtoList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLte1xRttCellListResponse(RadioResponseInfo info, Lte1xRttCellInfo[] list) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteBsrTimerResponse(RadioResponseInfo info, int timer) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteDataResponse(RadioResponseInfo info, LteData data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteRRCStateResponse(RadioResponseInfo info, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteReleaseVersionResponse(RadioResponseInfo info, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteScanDurationResponse(RadioResponseInfo info, int duration) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, int[] polCapability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getRoamingEnableResponse(RadioResponseInfo responseInfo, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo info, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, String[] plmnList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getTOEInfoResponse(RadioResponseInfo info, String longName, String shortName, String numeric) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void selectFemtocellResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void set4x4MimoEnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setApcModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setBandPriorityListResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setCALinkEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setDisable2GResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteBandEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteBsrTimerResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteReleaseVersionResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteScanDurationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNROptionResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNrBandModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setPOLEntryResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setQamEnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setSearchRatResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setTm9EnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setRoamingEnableResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExNetworkResponse {
        static final int TRANSACTION_abortFemtocellListResponse = 1;
        static final int TRANSACTION_cancelAvailableNetworksResponse = 2;
        static final int TRANSACTION_cfgA2offsetResponse = 3;
        static final int TRANSACTION_cfgB1offsetResponse = 4;
        static final int TRANSACTION_clearLteAvailableFileResponse = 5;
        static final int TRANSACTION_deactivateNrScgCommunicationResponse = 6;
        static final int TRANSACTION_enableCAPlusBandWidthFilterResponse = 8;
        static final int TRANSACTION_enableSCGfailureResponse = 9;
        static final int TRANSACTION_get4x4MimoEnabledResponse = 10;
        static final int TRANSACTION_getAllBandModeResponse = 11;
        static final int TRANSACTION_getApcInfoResponse = 12;
        static final int TRANSACTION_getAvailableNetworksWithActResponse = 13;
        static final int TRANSACTION_getBandModeResponse = 14;
        static final int TRANSACTION_getBandPriorityListResponse = 15;
        static final int TRANSACTION_getCALinkCapabilityListResponse = 16;
        static final int TRANSACTION_getCALinkEnableStatusResponse = 17;
        static final int TRANSACTION_getCaBandModeResponse = 18;
        static final int TRANSACTION_getCampedFemtoCellInfoResponse = 19;
        static final int TRANSACTION_getCurrentPOLListResponse = 20;
        static final int TRANSACTION_getDeactivateNrScgCommunicationResponse = 21;
        static final int TRANSACTION_getDisable2GResponse = 22;
        static final int TRANSACTION_getFemtocellListResponse = 23;
        static final int TRANSACTION_getIWlanRegistrationStateResponse = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLte1xRttCellListResponse = 25;
        static final int TRANSACTION_getLteBsrTimerResponse = 26;
        static final int TRANSACTION_getLteDataResponse = 27;
        static final int TRANSACTION_getLteRRCStateResponse = 28;
        static final int TRANSACTION_getLteReleaseVersionResponse = 29;
        static final int TRANSACTION_getLteScanDurationResponse = 30;
        static final int TRANSACTION_getPOLCapabilityResponse = 31;
        static final int TRANSACTION_getPlmnNameFromSE13TableResponse = 32;
        static final int TRANSACTION_getQamEnabledResponse = 33;
        static final int TRANSACTION_getRoamingEnableResponse = 34;
        static final int TRANSACTION_getSignalStrengthWithWcdmaEcioResponse = 35;
        static final int TRANSACTION_getSuggestedPlmnListResponse = 36;
        static final int TRANSACTION_getTOEInfoResponse = 37;
        static final int TRANSACTION_getTm9EnabledResponse = 38;
        static final int TRANSACTION_queryFemtoCellSystemSelectionModeResponse = 39;
        static final int TRANSACTION_selectFemtocellResponse = 40;
        static final int TRANSACTION_set4x4MimoEnabledResponse = 41;
        static final int TRANSACTION_setApcModeResponse = 42;
        static final int TRANSACTION_setBandPriorityListResponse = 43;
        static final int TRANSACTION_setBgsrchDeltaSleepTimerResponse = 44;
        static final int TRANSACTION_setCALinkEnableStatusResponse = 45;
        static final int TRANSACTION_setCarrierAggregationModeResponse = 7;
        static final int TRANSACTION_setDisable2GResponse = 46;
        static final int TRANSACTION_setFemtoCellSystemSelectionModeResponse = 47;
        static final int TRANSACTION_setLteBandEnableStatusResponse = 48;
        static final int TRANSACTION_setLteBsrTimerResponse = 49;
        static final int TRANSACTION_setLteReleaseVersionResponse = 50;
        static final int TRANSACTION_setLteScanDurationResponse = 51;
        static final int TRANSACTION_setNROptionResponse = 52;
        static final int TRANSACTION_setNetworkSelectionModeManualWithActResponse = 53;
        static final int TRANSACTION_setNrBandModeResponse = 54;
        static final int TRANSACTION_setPOLEntryResponse = 55;
        static final int TRANSACTION_setQamEnabledResponse = 56;
        static final int TRANSACTION_setRoamingEnableResponse = 61;
        static final int TRANSACTION_setSearchRatResponse = 57;
        static final int TRANSACTION_setSearchStoredFreqInfoResponse = 58;
        static final int TRANSACTION_setServiceStateToModemResponse = 59;
        static final int TRANSACTION_setTm9EnabledResponse = 60;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExNetworkResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExNetworkResponse)) {
                return (IMtkRadioExNetworkResponse) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= TRANSACTION_getInterfaceVersion) {
                data.enforceInterface(DESCRIPTOR);
            }
            if (code == TRANSACTION_getInterfaceVersion) {
                reply.writeNoException();
                reply.writeInt(getInterfaceVersion());
                return true;
            }
            if (code == TRANSACTION_getInterfaceHash) {
                reply.writeNoException();
                reply.writeString(getInterfaceHash());
                return true;
            }
            switch (code) {
                case 1:
                    RadioResponseInfo _arg0 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    abortFemtocellListResponse(_arg0);
                    return true;
                case 2:
                    RadioResponseInfo _arg1 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    cancelAvailableNetworksResponse(_arg1);
                    return true;
                case 3:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    cfgA2offsetResponse(_arg2);
                    return true;
                case 4:
                    RadioResponseInfo _arg3 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    cfgB1offsetResponse(_arg3);
                    return true;
                case 5:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    clearLteAvailableFileResponse(_arg4);
                    return true;
                case 6:
                    RadioResponseInfo _arg5 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    deactivateNrScgCommunicationResponse(_arg5);
                    return true;
                case 7:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCarrierAggregationModeResponse(_arg6);
                    return true;
                case 8:
                    RadioResponseInfo _arg7 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    enableCAPlusBandWidthFilterResponse(_arg7);
                    return true;
                case 9:
                    RadioResponseInfo _arg8 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    enableSCGfailureResponse(_arg8);
                    return true;
                case 10:
                    RadioResponseInfo _arg9 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg10 = data.readInt();
                    data.enforceNoDataAvail();
                    get4x4MimoEnabledResponse(_arg9, _arg10);
                    return true;
                case 11:
                    RadioResponseInfo _arg11 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    BandModeInfo _arg12 = (BandModeInfo) data.readTypedObject(BandModeInfo.CREATOR);
                    data.enforceNoDataAvail();
                    getAllBandModeResponse(_arg11, _arg12);
                    return true;
                case 12:
                    RadioResponseInfo _arg13 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg14 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getApcInfoResponse(_arg13, _arg14);
                    return true;
                case 13:
                    RadioResponseInfo _arg15 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    OperatorInfoWithAct[] _arg16 = (OperatorInfoWithAct[]) data.createTypedArray(OperatorInfoWithAct.CREATOR);
                    data.enforceNoDataAvail();
                    getAvailableNetworksWithActResponse(_arg15, _arg16);
                    return true;
                case 14:
                    RadioResponseInfo _arg17 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg18 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getBandModeResponse(_arg17, _arg18);
                    return true;
                case 15:
                    RadioResponseInfo _arg19 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg20 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getBandPriorityListResponse(_arg19, _arg20);
                    return true;
                case 16:
                    RadioResponseInfo _arg21 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg22 = data.createStringArray();
                    data.enforceNoDataAvail();
                    getCALinkCapabilityListResponse(_arg21, _arg22);
                    return true;
                case 17:
                    RadioResponseInfo _arg23 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    boolean _arg24 = data.readBoolean();
                    data.enforceNoDataAvail();
                    getCALinkEnableStatusResponse(_arg23, _arg24);
                    return true;
                case 18:
                    RadioResponseInfo _arg25 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg26 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getCaBandModeResponse(_arg25, _arg26);
                    return true;
                case TRANSACTION_getCampedFemtoCellInfoResponse /* 19 */:
                    RadioResponseInfo _arg27 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg28 = data.createStringArray();
                    data.enforceNoDataAvail();
                    getCampedFemtoCellInfoResponse(_arg27, _arg28);
                    return true;
                case 20:
                    RadioResponseInfo _arg29 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg30 = data.createStringArray();
                    data.enforceNoDataAvail();
                    getCurrentPOLListResponse(_arg29, _arg30);
                    return true;
                case TRANSACTION_getDeactivateNrScgCommunicationResponse /* 21 */:
                    RadioResponseInfo _arg31 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    getDeactivateNrScgCommunicationResponse(_arg31, _arg32, _arg33);
                    return true;
                case TRANSACTION_getDisable2GResponse /* 22 */:
                    RadioResponseInfo _arg34 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg35 = data.readInt();
                    data.enforceNoDataAvail();
                    getDisable2GResponse(_arg34, _arg35);
                    return true;
                case TRANSACTION_getFemtocellListResponse /* 23 */:
                    RadioResponseInfo _arg36 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg37 = data.createStringArray();
                    data.enforceNoDataAvail();
                    getFemtocellListResponse(_arg36, _arg37);
                    return true;
                case 24:
                    RadioResponseInfo _arg38 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg39 = data.readInt();
                    data.enforceNoDataAvail();
                    getIWlanRegistrationStateResponse(_arg38, _arg39);
                    return true;
                case TRANSACTION_getLte1xRttCellListResponse /* 25 */:
                    RadioResponseInfo _arg40 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    Lte1xRttCellInfo[] _arg41 = (Lte1xRttCellInfo[]) data.createTypedArray(Lte1xRttCellInfo.CREATOR);
                    data.enforceNoDataAvail();
                    getLte1xRttCellListResponse(_arg40, _arg41);
                    return true;
                case TRANSACTION_getLteBsrTimerResponse /* 26 */:
                    RadioResponseInfo _arg42 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteBsrTimerResponse(_arg42, _arg43);
                    return true;
                case TRANSACTION_getLteDataResponse /* 27 */:
                    RadioResponseInfo _arg44 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    LteData _arg45 = (LteData) data.readTypedObject(LteData.CREATOR);
                    data.enforceNoDataAvail();
                    getLteDataResponse(_arg44, _arg45);
                    return true;
                case TRANSACTION_getLteRRCStateResponse /* 28 */:
                    RadioResponseInfo _arg46 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg47 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteRRCStateResponse(_arg46, _arg47);
                    return true;
                case TRANSACTION_getLteReleaseVersionResponse /* 29 */:
                    RadioResponseInfo _arg48 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg49 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteReleaseVersionResponse(_arg48, _arg49);
                    return true;
                case TRANSACTION_getLteScanDurationResponse /* 30 */:
                    RadioResponseInfo _arg50 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg51 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteScanDurationResponse(_arg50, _arg51);
                    return true;
                case TRANSACTION_getPOLCapabilityResponse /* 31 */:
                    RadioResponseInfo _arg52 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg53 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getPOLCapabilityResponse(_arg52, _arg53);
                    return true;
                case 32:
                    RadioResponseInfo _arg54 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg55 = data.readString();
                    data.enforceNoDataAvail();
                    getPlmnNameFromSE13TableResponse(_arg54, _arg55);
                    return true;
                case 33:
                    RadioResponseInfo _arg56 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    boolean _arg57 = data.readBoolean();
                    boolean _arg58 = data.readBoolean();
                    data.enforceNoDataAvail();
                    getQamEnabledResponse(_arg56, _arg57, _arg58);
                    return true;
                case TRANSACTION_getRoamingEnableResponse /* 34 */:
                    RadioResponseInfo _arg59 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg60 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getRoamingEnableResponse(_arg59, _arg60);
                    return true;
                case TRANSACTION_getSignalStrengthWithWcdmaEcioResponse /* 35 */:
                    RadioResponseInfo _arg61 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SignalStrengthWithWcdmaEcio _arg62 = (SignalStrengthWithWcdmaEcio) data.readTypedObject(SignalStrengthWithWcdmaEcio.CREATOR);
                    data.enforceNoDataAvail();
                    getSignalStrengthWithWcdmaEcioResponse(_arg61, _arg62);
                    return true;
                case 36:
                    RadioResponseInfo _arg63 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg64 = data.createStringArray();
                    data.enforceNoDataAvail();
                    getSuggestedPlmnListResponse(_arg63, _arg64);
                    return true;
                case TRANSACTION_getTOEInfoResponse /* 37 */:
                    RadioResponseInfo _arg65 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg66 = data.readString();
                    String _arg67 = data.readString();
                    String _arg68 = data.readString();
                    data.enforceNoDataAvail();
                    getTOEInfoResponse(_arg65, _arg66, _arg67, _arg68);
                    return true;
                case 38:
                    RadioResponseInfo _arg69 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    boolean _arg70 = data.readBoolean();
                    boolean _arg71 = data.readBoolean();
                    data.enforceNoDataAvail();
                    getTm9EnabledResponse(_arg69, _arg70, _arg71);
                    return true;
                case TRANSACTION_queryFemtoCellSystemSelectionModeResponse /* 39 */:
                    RadioResponseInfo _arg72 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg73 = data.readInt();
                    data.enforceNoDataAvail();
                    queryFemtoCellSystemSelectionModeResponse(_arg72, _arg73);
                    return true;
                case 40:
                    RadioResponseInfo _arg74 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    selectFemtocellResponse(_arg74);
                    return true;
                case TRANSACTION_set4x4MimoEnabledResponse /* 41 */:
                    RadioResponseInfo _arg75 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    set4x4MimoEnabledResponse(_arg75);
                    return true;
                case TRANSACTION_setApcModeResponse /* 42 */:
                    RadioResponseInfo _arg76 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setApcModeResponse(_arg76);
                    return true;
                case TRANSACTION_setBandPriorityListResponse /* 43 */:
                    RadioResponseInfo _arg77 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setBandPriorityListResponse(_arg77);
                    return true;
                case TRANSACTION_setBgsrchDeltaSleepTimerResponse /* 44 */:
                    RadioResponseInfo _arg78 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setBgsrchDeltaSleepTimerResponse(_arg78);
                    return true;
                case TRANSACTION_setCALinkEnableStatusResponse /* 45 */:
                    RadioResponseInfo _arg79 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCALinkEnableStatusResponse(_arg79);
                    return true;
                case 46:
                    RadioResponseInfo _arg80 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setDisable2GResponse(_arg80);
                    return true;
                case 47:
                    RadioResponseInfo _arg81 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setFemtoCellSystemSelectionModeResponse(_arg81);
                    return true;
                case 48:
                    RadioResponseInfo _arg82 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setLteBandEnableStatusResponse(_arg82);
                    return true;
                case TRANSACTION_setLteBsrTimerResponse /* 49 */:
                    RadioResponseInfo _arg83 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setLteBsrTimerResponse(_arg83);
                    return true;
                case 50:
                    RadioResponseInfo _arg84 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setLteReleaseVersionResponse(_arg84);
                    return true;
                case 51:
                    RadioResponseInfo _arg85 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setLteScanDurationResponse(_arg85);
                    return true;
                case 52:
                    RadioResponseInfo _arg86 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setNROptionResponse(_arg86);
                    return true;
                case TRANSACTION_setNetworkSelectionModeManualWithActResponse /* 53 */:
                    RadioResponseInfo _arg87 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setNetworkSelectionModeManualWithActResponse(_arg87);
                    return true;
                case 54:
                    RadioResponseInfo _arg88 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setNrBandModeResponse(_arg88);
                    return true;
                case TRANSACTION_setPOLEntryResponse /* 55 */:
                    RadioResponseInfo _arg89 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setPOLEntryResponse(_arg89);
                    return true;
                case 56:
                    RadioResponseInfo _arg90 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setQamEnabledResponse(_arg90);
                    return true;
                case TRANSACTION_setSearchRatResponse /* 57 */:
                    RadioResponseInfo _arg91 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSearchRatResponse(_arg91);
                    return true;
                case TRANSACTION_setSearchStoredFreqInfoResponse /* 58 */:
                    RadioResponseInfo _arg92 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSearchStoredFreqInfoResponse(_arg92);
                    return true;
                case TRANSACTION_setServiceStateToModemResponse /* 59 */:
                    RadioResponseInfo _arg93 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setServiceStateToModemResponse(_arg93);
                    return true;
                case 60:
                    RadioResponseInfo _arg94 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setTm9EnabledResponse(_arg94);
                    return true;
                case TRANSACTION_setRoamingEnableResponse /* 61 */:
                    RadioResponseInfo _arg95 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setRoamingEnableResponse(_arg95);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExNetworkResponse {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void abortFemtocellListResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cancelAvailableNetworksResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cfgA2offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cfgB1offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void clearLteAvailableFileResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setCarrierAggregationModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void enableSCGfailureResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void get4x4MimoEnabledResponse(RadioResponseInfo info, int enabled_bitmask) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(enabled_bitmask);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getAllBandModeResponse(RadioResponseInfo info, BandModeInfo data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getApcInfoResponse(RadioResponseInfo info, int[] cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(cellInfo);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getAvailableNetworksWithActResponse(RadioResponseInfo info, OperatorInfoWithAct[] networkInfosWithAct) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(networkInfosWithAct, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getBandPriorityListResponse(RadioResponseInfo info, int[] bandPriList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(bandPriList);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCALinkCapabilityListResponse(RadioResponseInfo info, String[] linkCapabilityList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(linkCapabilityList);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCALinkEnableStatusResponse(RadioResponseInfo info, boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(status);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCaBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCampedFemtoCellInfoResponse(RadioResponseInfo info, String[] femto) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(femto);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCampedFemtoCellInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, String[] polList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(polList);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(deactivate);
                    _data.writeInt(allowSCGAdd);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDeactivateNrScgCommunicationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDisable2GResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getFemtocellListResponse(RadioResponseInfo responseInfo, String[] femtoList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(femtoList);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getFemtocellListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(24, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLte1xRttCellListResponse(RadioResponseInfo info, Lte1xRttCellInfo[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(list, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLte1xRttCellListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteBsrTimerResponse(RadioResponseInfo info, int timer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(timer);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteBsrTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteDataResponse(RadioResponseInfo info, LteData data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteDataResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteRRCStateResponse(RadioResponseInfo info, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteRRCStateResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteReleaseVersionResponse(RadioResponseInfo info, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteReleaseVersionResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteScanDurationResponse(RadioResponseInfo info, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(duration);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteScanDurationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, int[] polCapability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeIntArray(polCapability);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getPOLCapabilityResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(ulOrDl);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getRoamingEnableResponse(RadioResponseInfo responseInfo, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getRoamingEnableResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo info, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(signalStrength, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSignalStrengthWithWcdmaEcioResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, String[] plmnList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(plmnList);
                    boolean _status = this.mRemote.transact(36, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getTOEInfoResponse(RadioResponseInfo info, String longName, String shortName, String numeric) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(longName);
                    _data.writeString(shortName);
                    _data.writeString(numeric);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTOEInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(38, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_queryFemtoCellSystemSelectionModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void selectFemtocellResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void set4x4MimoEnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_set4x4MimoEnabledResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setApcModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setApcModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setBandPriorityListResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBandPriorityListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBgsrchDeltaSleepTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setCALinkEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setCALinkEnableStatusResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setDisable2GResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteBandEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(48, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteBsrTimerResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteBsrTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteReleaseVersionResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteScanDurationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(51, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNROptionResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(52, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNetworkSelectionModeManualWithActResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNrBandModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(54, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setPOLEntryResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPOLEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setQamEnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(56, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setSearchRatResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchRatResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchStoredFreqInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setServiceStateToModemResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setTm9EnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(60, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setRoamingEnableResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setRoamingEnableResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}

package vendor.mediatek.hardware.mtkradioex.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication;
import vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExNetwork extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$network$IMtkRadioExNetwork".replace('$', '.');
    public static final String HASH = "583aefd4764eb70d99325928242c9ced29a9c0ee";
    public static final int VERSION = 1;

    void abortFemtocellList(int i, int i2) throws RemoteException;

    void cancelAvailableNetworks(int i, int i2) throws RemoteException;

    void cfgA2offset(int i, int i2, int i3, int i4) throws RemoteException;

    void cfgB1offset(int i, int i2, int i3, int i4) throws RemoteException;

    void clearLteAvailableFile(int i, int i2) throws RemoteException;

    void deactivateNrScgCommunication(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void enableCAPlusBandWidthFilter(int i, boolean z, int i2) throws RemoteException;

    void enableSCGfailure(int i, boolean z, int i2, int i3, int i4, int i5) throws RemoteException;

    void get4x4MimoEnabled(int i, int i2) throws RemoteException;

    void getAllBandMode(int i, int i2) throws RemoteException;

    void getApcInfo(int i, int i2) throws RemoteException;

    void getAvailableNetworksWithAct(int i, int i2) throws RemoteException;

    void getBandMode(int i, int i2) throws RemoteException;

    void getBandPriorityList(int i, int i2) throws RemoteException;

    void getCALinkCapabilityList(int i, int i2, int i3) throws RemoteException;

    void getCALinkEnableStatus(int i, String str, int i2, int i3) throws RemoteException;

    void getCaBandMode(int i, int i2, int i3) throws RemoteException;

    void getCampedFemtoCellInfo(int i, int i2) throws RemoteException;

    void getCurrentPOLList(int i, int i2) throws RemoteException;

    void getDeactivateNrScgCommunication(int i, int i2) throws RemoteException;

    void getDisable2G(int i, int i2) throws RemoteException;

    void getFemtocellList(int i, int i2) throws RemoteException;

    void getIWlanRegistrationState(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getLte1xRttCellList(int i, boolean z, int i2) throws RemoteException;

    void getLteBsrTimer(int i, int i2) throws RemoteException;

    void getLteData(int i, int i2) throws RemoteException;

    void getLteRRCState(int i, int i2) throws RemoteException;

    void getLteReleaseVersion(int i, int i2) throws RemoteException;

    void getLteScanDuration(int i, int i2) throws RemoteException;

    void getPOLCapability(int i, int i2) throws RemoteException;

    void getPlmnNameFromSE13Table(int i, int i2, int i3, int i4) throws RemoteException;

    void getQamEnabled(int i, boolean z, int i2) throws RemoteException;

    void getRoamingEnable(int i, int i2, int i3) throws RemoteException;

    void getSignalStrengthWithWcdmaEcio(int i, int i2) throws RemoteException;

    void getSmartRatSwitch(int i, int i2, int i3) throws RemoteException;

    void getSuggestedPlmnList(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void getTOEInfo(int i, int i2) throws RemoteException;

    void getTm9Enabled(int i, boolean z, int i2) throws RemoteException;

    void queryFemtoCellSystemSelectionMode(int i, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void selectFemtocell(int i, String str, String str2, String str3, int i2) throws RemoteException;

    void set4x4MimoEnabled(int i, int i2, int i3) throws RemoteException;

    void setApcMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setBandPriorityList(int i, int[] iArr, int i2) throws RemoteException;

    void setBgsrchDeltaSleepTimer(int i, int i2, int i3) throws RemoteException;

    void setCALinkEnableStatus(int i, boolean z, String str, int i2, int i3) throws RemoteException;

    void setCarrierAggregationMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setDisable2G(int i, boolean z, int i2) throws RemoteException;

    void setFemtoCellSystemSelectionMode(int i, int i2, int i3) throws RemoteException;

    void setLteBandEnableStatus(int i, int i2, boolean z, int i3) throws RemoteException;

    void setLteBsrTimer(int i, int i2, int i3) throws RemoteException;

    void setLteReleaseVersion(int i, int i2, int i3) throws RemoteException;

    void setLteScanDuration(int i, int i2, int i3) throws RemoteException;

    void setNROption(int i, int i2, int i3) throws RemoteException;

    void setNetworkSelectionModeManualWithAct(int i, String str, String str2, String str3, int i2) throws RemoteException;

    void setNrBandMode(int i, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i2) throws RemoteException;

    void setPOLEntry(int i, int i2, String str, int i3, int i4) throws RemoteException;

    void setQamEnabled(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExNetworkResponse iMtkRadioExNetworkResponse, IMtkRadioExNetworkIndication iMtkRadioExNetworkIndication) throws RemoteException;

    void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse iSmartRatSwitchRadioResponse, ISmartRatSwitchRadioIndication iSmartRatSwitchRadioIndication) throws RemoteException;

    void setRoamingEnable(int i, int[] iArr, int i2) throws RemoteException;

    void setSearchRat(int i, int[] iArr, int i2) throws RemoteException;

    void setSearchStoredFreqInfo(int i, int i2, int i3, int i4, int[] iArr, int i5) throws RemoteException;

    void setServiceStateToModem(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws RemoteException;

    void setTm9Enabled(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void smartRatSwitch(int i, int i2, int i3, int i4) throws RemoteException;

    public static class Default implements IMtkRadioExNetwork {
        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void abortFemtocellList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cancelAvailableNetworks(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cfgA2offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cfgB1offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void clearLteAvailableFile(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void deactivateNrScgCommunication(int serial, boolean deactivate, boolean allowSCGAdd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setCarrierAggregationMode(int serial, int mode, int option, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void enableCAPlusBandWidthFilter(int serial, boolean enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void enableSCGfailure(int serial, boolean enable, int T1, int P1, int T2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void get4x4MimoEnabled(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getAllBandMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getApcInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getAvailableNetworksWithAct(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getBandMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getBandPriorityList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCALinkCapabilityList(int serial, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCALinkEnableStatus(int serial, String bandsCombo, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCaBandMode(int serial, int primaryBandId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCampedFemtoCellInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCurrentPOLList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getDeactivateNrScgCommunication(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getDisable2G(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getFemtocellList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getIWlanRegistrationState(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLte1xRttCellList(int serial, boolean available, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteBsrTimer(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteData(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteRRCState(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteReleaseVersion(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteScanDuration(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getPOLCapability(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getPlmnNameFromSE13Table(int serial, int mcc, int mnc, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getQamEnabled(int serial, boolean ulOrDl, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getRoamingEnable(int serial, int phoneId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSignalStrengthWithWcdmaEcio(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSmartRatSwitch(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSuggestedPlmnList(int serial, int rat, int num, int timer, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getTOEInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getTm9Enabled(int serial, boolean fddOrTdd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void queryFemtoCellSystemSelectionMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void selectFemtocell(int serial, String operatorNumeric, String act, String csgId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void set4x4MimoEnabled(int serial, int enabled_bitmask, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setApcMode(int serial, int mode, int reportMode, int interval, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setBandPriorityList(int serial, int[] bandPriList, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setBgsrchDeltaSleepTimer(int serial, int sleepDuration, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setCALinkEnableStatus(int serial, boolean status, String bandsCombo, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setDisable2G(int serial, boolean mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setFemtoCellSystemSelectionMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteBandEnableStatus(int serial, int bandId, boolean status, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteBsrTimer(int serial, int timer, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteReleaseVersion(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteScanDuration(int serial, int duration, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNROption(int serial, int option, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNetworkSelectionModeManualWithAct(int serial, String operatorNumeric, String act, String mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNrBandMode(int serial, int[] saEnable, int[] saDisable, int[] nsaEnable, int[] nsaDisable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setPOLEntry(int serial, int index, String numeric, int nAct, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setQamEnabled(int serial, boolean ulOrDl, boolean enabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setSearchRat(int serial, int[] rat, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setSearchStoredFreqInfo(int serial, int operation, int plmn_id, int rat, int[] freq, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setServiceStateToModem(int serial, int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setTm9Enabled(int serial, boolean fddOrTdd, boolean enabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void smartRatSwitch(int serial, int mode, int rat, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setRoamingEnable(int serial, int[] config, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setResponseFunctionsMtk(IMtkRadioExNetworkResponse radioResponse, IMtkRadioExNetworkIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse radioResponse, ISmartRatSwitchRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExNetwork {
        static final int TRANSACTION_abortFemtocellList = 1;
        static final int TRANSACTION_cancelAvailableNetworks = 2;
        static final int TRANSACTION_cfgA2offset = 3;
        static final int TRANSACTION_cfgB1offset = 4;
        static final int TRANSACTION_clearLteAvailableFile = 5;
        static final int TRANSACTION_deactivateNrScgCommunication = 6;
        static final int TRANSACTION_enableCAPlusBandWidthFilter = 8;
        static final int TRANSACTION_enableSCGfailure = 9;
        static final int TRANSACTION_get4x4MimoEnabled = 10;
        static final int TRANSACTION_getAllBandMode = 11;
        static final int TRANSACTION_getApcInfo = 12;
        static final int TRANSACTION_getAvailableNetworksWithAct = 13;
        static final int TRANSACTION_getBandMode = 14;
        static final int TRANSACTION_getBandPriorityList = 15;
        static final int TRANSACTION_getCALinkCapabilityList = 16;
        static final int TRANSACTION_getCALinkEnableStatus = 17;
        static final int TRANSACTION_getCaBandMode = 18;
        static final int TRANSACTION_getCampedFemtoCellInfo = 19;
        static final int TRANSACTION_getCurrentPOLList = 20;
        static final int TRANSACTION_getDeactivateNrScgCommunication = 21;
        static final int TRANSACTION_getDisable2G = 22;
        static final int TRANSACTION_getFemtocellList = 23;
        static final int TRANSACTION_getIWlanRegistrationState = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLte1xRttCellList = 25;
        static final int TRANSACTION_getLteBsrTimer = 26;
        static final int TRANSACTION_getLteData = 27;
        static final int TRANSACTION_getLteRRCState = 28;
        static final int TRANSACTION_getLteReleaseVersion = 29;
        static final int TRANSACTION_getLteScanDuration = 30;
        static final int TRANSACTION_getPOLCapability = 31;
        static final int TRANSACTION_getPlmnNameFromSE13Table = 32;
        static final int TRANSACTION_getQamEnabled = 33;
        static final int TRANSACTION_getRoamingEnable = 34;
        static final int TRANSACTION_getSignalStrengthWithWcdmaEcio = 35;
        static final int TRANSACTION_getSmartRatSwitch = 36;
        static final int TRANSACTION_getSuggestedPlmnList = 37;
        static final int TRANSACTION_getTOEInfo = 38;
        static final int TRANSACTION_getTm9Enabled = 39;
        static final int TRANSACTION_queryFemtoCellSystemSelectionMode = 40;
        static final int TRANSACTION_responseAcknowledgementMtk = 64;
        static final int TRANSACTION_selectFemtocell = 41;
        static final int TRANSACTION_set4x4MimoEnabled = 42;
        static final int TRANSACTION_setApcMode = 43;
        static final int TRANSACTION_setBandPriorityList = 44;
        static final int TRANSACTION_setBgsrchDeltaSleepTimer = 45;
        static final int TRANSACTION_setCALinkEnableStatus = 46;
        static final int TRANSACTION_setCarrierAggregationMode = 7;
        static final int TRANSACTION_setDisable2G = 47;
        static final int TRANSACTION_setFemtoCellSystemSelectionMode = 48;
        static final int TRANSACTION_setLteBandEnableStatus = 49;
        static final int TRANSACTION_setLteBsrTimer = 50;
        static final int TRANSACTION_setLteReleaseVersion = 51;
        static final int TRANSACTION_setLteScanDuration = 52;
        static final int TRANSACTION_setNROption = 53;
        static final int TRANSACTION_setNetworkSelectionModeManualWithAct = 54;
        static final int TRANSACTION_setNrBandMode = 55;
        static final int TRANSACTION_setPOLEntry = 56;
        static final int TRANSACTION_setQamEnabled = 57;
        static final int TRANSACTION_setResponseFunctionsMtk = 65;
        static final int TRANSACTION_setResponseFunctionsSmartRatSwitch = 66;
        static final int TRANSACTION_setRoamingEnable = 63;
        static final int TRANSACTION_setSearchRat = 58;
        static final int TRANSACTION_setSearchStoredFreqInfo = 59;
        static final int TRANSACTION_setServiceStateToModem = 60;
        static final int TRANSACTION_setTm9Enabled = 61;
        static final int TRANSACTION_smartRatSwitch = 62;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExNetwork asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExNetwork)) {
                return (IMtkRadioExNetwork) iin;
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
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    abortFemtocellList(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    cancelAvailableNetworks(_arg2, _arg3);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    cfgA2offset(_arg4, _arg5, _arg6, _arg7);
                    return true;
                case 4:
                    int _arg8 = data.readInt();
                    int _arg9 = data.readInt();
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    data.enforceNoDataAvail();
                    cfgB1offset(_arg8, _arg9, _arg10, _arg11);
                    return true;
                case 5:
                    int _arg12 = data.readInt();
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    clearLteAvailableFile(_arg12, _arg13);
                    return true;
                case 6:
                    int _arg14 = data.readInt();
                    boolean _arg15 = data.readBoolean();
                    boolean _arg16 = data.readBoolean();
                    int _arg17 = data.readInt();
                    data.enforceNoDataAvail();
                    deactivateNrScgCommunication(_arg14, _arg15, _arg16, _arg17);
                    return true;
                case 7:
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    int _arg21 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    setCarrierAggregationMode(_arg18, _arg19, _arg20, _arg21, _arg22);
                    return true;
                case 8:
                    int _arg23 = data.readInt();
                    boolean _arg24 = data.readBoolean();
                    int _arg25 = data.readInt();
                    data.enforceNoDataAvail();
                    enableCAPlusBandWidthFilter(_arg23, _arg24, _arg25);
                    return true;
                case 9:
                    int _arg26 = data.readInt();
                    boolean _arg27 = data.readBoolean();
                    int _arg28 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg30 = data.readInt();
                    int _arg31 = data.readInt();
                    data.enforceNoDataAvail();
                    enableSCGfailure(_arg26, _arg27, _arg28, _arg29, _arg30, _arg31);
                    return true;
                case 10:
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    get4x4MimoEnabled(_arg32, _arg33);
                    return true;
                case 11:
                    int _arg34 = data.readInt();
                    int _arg35 = data.readInt();
                    data.enforceNoDataAvail();
                    getAllBandMode(_arg34, _arg35);
                    return true;
                case 12:
                    int _arg36 = data.readInt();
                    int _arg37 = data.readInt();
                    data.enforceNoDataAvail();
                    getApcInfo(_arg36, _arg37);
                    return true;
                case 13:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    data.enforceNoDataAvail();
                    getAvailableNetworksWithAct(_arg38, _arg39);
                    return true;
                case 14:
                    int _arg40 = data.readInt();
                    int _arg41 = data.readInt();
                    data.enforceNoDataAvail();
                    getBandMode(_arg40, _arg41);
                    return true;
                case 15:
                    int _arg42 = data.readInt();
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    getBandPriorityList(_arg42, _arg43);
                    return true;
                case 16:
                    int _arg44 = data.readInt();
                    int _arg45 = data.readInt();
                    int _arg46 = data.readInt();
                    data.enforceNoDataAvail();
                    getCALinkCapabilityList(_arg44, _arg45, _arg46);
                    return true;
                case 17:
                    int _arg47 = data.readInt();
                    String _arg48 = data.readString();
                    int _arg49 = data.readInt();
                    int _arg50 = data.readInt();
                    data.enforceNoDataAvail();
                    getCALinkEnableStatus(_arg47, _arg48, _arg49, _arg50);
                    return true;
                case 18:
                    int _arg51 = data.readInt();
                    int _arg52 = data.readInt();
                    int _arg53 = data.readInt();
                    data.enforceNoDataAvail();
                    getCaBandMode(_arg51, _arg52, _arg53);
                    return true;
                case TRANSACTION_getCampedFemtoCellInfo /* 19 */:
                    int _arg54 = data.readInt();
                    int _arg55 = data.readInt();
                    data.enforceNoDataAvail();
                    getCampedFemtoCellInfo(_arg54, _arg55);
                    return true;
                case 20:
                    int _arg56 = data.readInt();
                    int _arg57 = data.readInt();
                    data.enforceNoDataAvail();
                    getCurrentPOLList(_arg56, _arg57);
                    return true;
                case TRANSACTION_getDeactivateNrScgCommunication /* 21 */:
                    int _arg58 = data.readInt();
                    int _arg59 = data.readInt();
                    data.enforceNoDataAvail();
                    getDeactivateNrScgCommunication(_arg58, _arg59);
                    return true;
                case TRANSACTION_getDisable2G /* 22 */:
                    int _arg60 = data.readInt();
                    int _arg61 = data.readInt();
                    data.enforceNoDataAvail();
                    getDisable2G(_arg60, _arg61);
                    return true;
                case TRANSACTION_getFemtocellList /* 23 */:
                    int _arg62 = data.readInt();
                    int _arg63 = data.readInt();
                    data.enforceNoDataAvail();
                    getFemtocellList(_arg62, _arg63);
                    return true;
                case 24:
                    int _arg64 = data.readInt();
                    int _arg65 = data.readInt();
                    data.enforceNoDataAvail();
                    getIWlanRegistrationState(_arg64, _arg65);
                    return true;
                case TRANSACTION_getLte1xRttCellList /* 25 */:
                    int _arg66 = data.readInt();
                    boolean _arg67 = data.readBoolean();
                    int _arg68 = data.readInt();
                    data.enforceNoDataAvail();
                    getLte1xRttCellList(_arg66, _arg67, _arg68);
                    return true;
                case TRANSACTION_getLteBsrTimer /* 26 */:
                    int _arg69 = data.readInt();
                    int _arg70 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteBsrTimer(_arg69, _arg70);
                    return true;
                case TRANSACTION_getLteData /* 27 */:
                    int _arg71 = data.readInt();
                    int _arg72 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteData(_arg71, _arg72);
                    return true;
                case TRANSACTION_getLteRRCState /* 28 */:
                    int _arg73 = data.readInt();
                    int _arg74 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteRRCState(_arg73, _arg74);
                    return true;
                case TRANSACTION_getLteReleaseVersion /* 29 */:
                    int _arg75 = data.readInt();
                    int _arg76 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteReleaseVersion(_arg75, _arg76);
                    return true;
                case TRANSACTION_getLteScanDuration /* 30 */:
                    int _arg77 = data.readInt();
                    int _arg78 = data.readInt();
                    data.enforceNoDataAvail();
                    getLteScanDuration(_arg77, _arg78);
                    return true;
                case TRANSACTION_getPOLCapability /* 31 */:
                    int _arg79 = data.readInt();
                    int _arg80 = data.readInt();
                    data.enforceNoDataAvail();
                    getPOLCapability(_arg79, _arg80);
                    return true;
                case 32:
                    int _arg81 = data.readInt();
                    int _arg82 = data.readInt();
                    int _arg83 = data.readInt();
                    int _arg84 = data.readInt();
                    data.enforceNoDataAvail();
                    getPlmnNameFromSE13Table(_arg81, _arg82, _arg83, _arg84);
                    return true;
                case 33:
                    int _arg85 = data.readInt();
                    boolean _arg86 = data.readBoolean();
                    int _arg87 = data.readInt();
                    data.enforceNoDataAvail();
                    getQamEnabled(_arg85, _arg86, _arg87);
                    return true;
                case TRANSACTION_getRoamingEnable /* 34 */:
                    int _arg88 = data.readInt();
                    int _arg89 = data.readInt();
                    int _arg90 = data.readInt();
                    data.enforceNoDataAvail();
                    getRoamingEnable(_arg88, _arg89, _arg90);
                    return true;
                case TRANSACTION_getSignalStrengthWithWcdmaEcio /* 35 */:
                    int _arg91 = data.readInt();
                    int _arg92 = data.readInt();
                    data.enforceNoDataAvail();
                    getSignalStrengthWithWcdmaEcio(_arg91, _arg92);
                    return true;
                case 36:
                    int _arg93 = data.readInt();
                    int _arg94 = data.readInt();
                    int _arg95 = data.readInt();
                    data.enforceNoDataAvail();
                    getSmartRatSwitch(_arg93, _arg94, _arg95);
                    return true;
                case TRANSACTION_getSuggestedPlmnList /* 37 */:
                    int _arg96 = data.readInt();
                    int _arg97 = data.readInt();
                    int _arg98 = data.readInt();
                    int _arg99 = data.readInt();
                    int _arg100 = data.readInt();
                    data.enforceNoDataAvail();
                    getSuggestedPlmnList(_arg96, _arg97, _arg98, _arg99, _arg100);
                    return true;
                case 38:
                    int _arg101 = data.readInt();
                    int _arg102 = data.readInt();
                    data.enforceNoDataAvail();
                    getTOEInfo(_arg101, _arg102);
                    return true;
                case TRANSACTION_getTm9Enabled /* 39 */:
                    int _arg103 = data.readInt();
                    boolean _arg104 = data.readBoolean();
                    int _arg105 = data.readInt();
                    data.enforceNoDataAvail();
                    getTm9Enabled(_arg103, _arg104, _arg105);
                    return true;
                case 40:
                    int _arg106 = data.readInt();
                    int _arg107 = data.readInt();
                    data.enforceNoDataAvail();
                    queryFemtoCellSystemSelectionMode(_arg106, _arg107);
                    return true;
                case TRANSACTION_selectFemtocell /* 41 */:
                    int _arg108 = data.readInt();
                    String _arg109 = data.readString();
                    String _arg110 = data.readString();
                    String _arg111 = data.readString();
                    int _arg112 = data.readInt();
                    data.enforceNoDataAvail();
                    selectFemtocell(_arg108, _arg109, _arg110, _arg111, _arg112);
                    return true;
                case TRANSACTION_set4x4MimoEnabled /* 42 */:
                    int _arg113 = data.readInt();
                    int _arg114 = data.readInt();
                    int _arg115 = data.readInt();
                    data.enforceNoDataAvail();
                    set4x4MimoEnabled(_arg113, _arg114, _arg115);
                    return true;
                case TRANSACTION_setApcMode /* 43 */:
                    int _arg116 = data.readInt();
                    int _arg117 = data.readInt();
                    int _arg118 = data.readInt();
                    int _arg119 = data.readInt();
                    int _arg120 = data.readInt();
                    data.enforceNoDataAvail();
                    setApcMode(_arg116, _arg117, _arg118, _arg119, _arg120);
                    return true;
                case TRANSACTION_setBandPriorityList /* 44 */:
                    int _arg121 = data.readInt();
                    int[] _arg122 = data.createIntArray();
                    int _arg123 = data.readInt();
                    data.enforceNoDataAvail();
                    setBandPriorityList(_arg121, _arg122, _arg123);
                    return true;
                case TRANSACTION_setBgsrchDeltaSleepTimer /* 45 */:
                    int _arg124 = data.readInt();
                    int _arg125 = data.readInt();
                    int _arg126 = data.readInt();
                    data.enforceNoDataAvail();
                    setBgsrchDeltaSleepTimer(_arg124, _arg125, _arg126);
                    return true;
                case 46:
                    int _arg127 = data.readInt();
                    boolean _arg128 = data.readBoolean();
                    String _arg129 = data.readString();
                    int _arg130 = data.readInt();
                    int _arg131 = data.readInt();
                    data.enforceNoDataAvail();
                    setCALinkEnableStatus(_arg127, _arg128, _arg129, _arg130, _arg131);
                    return true;
                case 47:
                    int _arg132 = data.readInt();
                    boolean _arg133 = data.readBoolean();
                    int _arg134 = data.readInt();
                    data.enforceNoDataAvail();
                    setDisable2G(_arg132, _arg133, _arg134);
                    return true;
                case 48:
                    int _arg135 = data.readInt();
                    int _arg136 = data.readInt();
                    int _arg137 = data.readInt();
                    data.enforceNoDataAvail();
                    setFemtoCellSystemSelectionMode(_arg135, _arg136, _arg137);
                    return true;
                case TRANSACTION_setLteBandEnableStatus /* 49 */:
                    int _arg138 = data.readInt();
                    int _arg139 = data.readInt();
                    boolean _arg140 = data.readBoolean();
                    int _arg141 = data.readInt();
                    data.enforceNoDataAvail();
                    setLteBandEnableStatus(_arg138, _arg139, _arg140, _arg141);
                    return true;
                case 50:
                    int _arg142 = data.readInt();
                    int _arg143 = data.readInt();
                    int _arg144 = data.readInt();
                    data.enforceNoDataAvail();
                    setLteBsrTimer(_arg142, _arg143, _arg144);
                    return true;
                case 51:
                    int _arg145 = data.readInt();
                    int _arg146 = data.readInt();
                    int _arg147 = data.readInt();
                    data.enforceNoDataAvail();
                    setLteReleaseVersion(_arg145, _arg146, _arg147);
                    return true;
                case 52:
                    int _arg148 = data.readInt();
                    int _arg149 = data.readInt();
                    int _arg150 = data.readInt();
                    data.enforceNoDataAvail();
                    setLteScanDuration(_arg148, _arg149, _arg150);
                    return true;
                case TRANSACTION_setNROption /* 53 */:
                    int _arg151 = data.readInt();
                    int _arg152 = data.readInt();
                    int _arg153 = data.readInt();
                    data.enforceNoDataAvail();
                    setNROption(_arg151, _arg152, _arg153);
                    return true;
                case 54:
                    int _arg154 = data.readInt();
                    String _arg155 = data.readString();
                    String _arg156 = data.readString();
                    String _arg157 = data.readString();
                    int _arg158 = data.readInt();
                    data.enforceNoDataAvail();
                    setNetworkSelectionModeManualWithAct(_arg154, _arg155, _arg156, _arg157, _arg158);
                    return true;
                case TRANSACTION_setNrBandMode /* 55 */:
                    int _arg159 = data.readInt();
                    int[] _arg160 = data.createIntArray();
                    int[] _arg161 = data.createIntArray();
                    int[] _arg162 = data.createIntArray();
                    int[] _arg163 = data.createIntArray();
                    int _arg164 = data.readInt();
                    data.enforceNoDataAvail();
                    setNrBandMode(_arg159, _arg160, _arg161, _arg162, _arg163, _arg164);
                    return true;
                case 56:
                    int _arg165 = data.readInt();
                    int _arg166 = data.readInt();
                    String _arg167 = data.readString();
                    int _arg168 = data.readInt();
                    int _arg169 = data.readInt();
                    data.enforceNoDataAvail();
                    setPOLEntry(_arg165, _arg166, _arg167, _arg168, _arg169);
                    return true;
                case TRANSACTION_setQamEnabled /* 57 */:
                    int _arg170 = data.readInt();
                    boolean _arg171 = data.readBoolean();
                    boolean _arg172 = data.readBoolean();
                    int _arg173 = data.readInt();
                    data.enforceNoDataAvail();
                    setQamEnabled(_arg170, _arg171, _arg172, _arg173);
                    return true;
                case TRANSACTION_setSearchRat /* 58 */:
                    int _arg174 = data.readInt();
                    int[] _arg175 = data.createIntArray();
                    int _arg176 = data.readInt();
                    data.enforceNoDataAvail();
                    setSearchRat(_arg174, _arg175, _arg176);
                    return true;
                case TRANSACTION_setSearchStoredFreqInfo /* 59 */:
                    int _arg177 = data.readInt();
                    int _arg178 = data.readInt();
                    int _arg179 = data.readInt();
                    int _arg180 = data.readInt();
                    int[] _arg181 = data.createIntArray();
                    int _arg182 = data.readInt();
                    data.enforceNoDataAvail();
                    setSearchStoredFreqInfo(_arg177, _arg178, _arg179, _arg180, _arg181, _arg182);
                    return true;
                case 60:
                    int _arg183 = data.readInt();
                    int _arg184 = data.readInt();
                    int _arg185 = data.readInt();
                    int _arg186 = data.readInt();
                    int _arg187 = data.readInt();
                    int _arg188 = data.readInt();
                    int _arg189 = data.readInt();
                    int _arg190 = data.readInt();
                    data.enforceNoDataAvail();
                    setServiceStateToModem(_arg183, _arg184, _arg185, _arg186, _arg187, _arg188, _arg189, _arg190);
                    return true;
                case TRANSACTION_setTm9Enabled /* 61 */:
                    int _arg191 = data.readInt();
                    boolean _arg192 = data.readBoolean();
                    boolean _arg193 = data.readBoolean();
                    int _arg194 = data.readInt();
                    data.enforceNoDataAvail();
                    setTm9Enabled(_arg191, _arg192, _arg193, _arg194);
                    return true;
                case TRANSACTION_smartRatSwitch /* 62 */:
                    int _arg195 = data.readInt();
                    int _arg196 = data.readInt();
                    int _arg197 = data.readInt();
                    int _arg198 = data.readInt();
                    data.enforceNoDataAvail();
                    smartRatSwitch(_arg195, _arg196, _arg197, _arg198);
                    return true;
                case TRANSACTION_setRoamingEnable /* 63 */:
                    int _arg199 = data.readInt();
                    int[] _arg200 = data.createIntArray();
                    int _arg201 = data.readInt();
                    data.enforceNoDataAvail();
                    setRoamingEnable(_arg199, _arg200, _arg201);
                    return true;
                case 64:
                    responseAcknowledgementMtk();
                    return true;
                case TRANSACTION_setResponseFunctionsMtk /* 65 */:
                    IMtkRadioExNetworkResponse _arg202 = IMtkRadioExNetworkResponse.Stub.asInterface(data.readStrongBinder());
                    IMtkRadioExNetworkIndication _arg203 = IMtkRadioExNetworkIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsMtk(_arg202, _arg203);
                    return true;
                case TRANSACTION_setResponseFunctionsSmartRatSwitch /* 66 */:
                    ISmartRatSwitchRadioResponse _arg204 = ISmartRatSwitchRadioResponse.Stub.asInterface(data.readStrongBinder());
                    ISmartRatSwitchRadioIndication _arg205 = ISmartRatSwitchRadioIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsSmartRatSwitch(_arg204, _arg205);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExNetwork {
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void abortFemtocellList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cancelAvailableNetworks(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cfgA2offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(offset);
                    _data.writeInt(threshBound);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cfgB1offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(offset);
                    _data.writeInt(threshBound);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void clearLteAvailableFile(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void deactivateNrScgCommunication(int serial, boolean deactivate, boolean allowSCGAdd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(deactivate);
                    _data.writeBoolean(allowSCGAdd);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setCarrierAggregationMode(int serial, int mode, int option, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(option);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void enableCAPlusBandWidthFilter(int serial, boolean enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void enableSCGfailure(int serial, boolean enable, int T1, int P1, int T2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(T1);
                    _data.writeInt(P1);
                    _data.writeInt(T2);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void get4x4MimoEnabled(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getAllBandMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getApcInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getAvailableNetworksWithAct(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getBandMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getBandPriorityList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCALinkCapabilityList(int serial, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCALinkEnableStatus(int serial, String bandsCombo, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(bandsCombo);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCaBandMode(int serial, int primaryBandId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(primaryBandId);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCampedFemtoCellInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getCampedFemtoCellInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCurrentPOLList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getDeactivateNrScgCommunication(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDeactivateNrScgCommunication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getDisable2G(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDisable2G, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getFemtocellList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getFemtocellList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getIWlanRegistrationState(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLte1xRttCellList(int serial, boolean available, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(available);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLte1xRttCellList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteBsrTimer(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteBsrTimer, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteData(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteData, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteRRCState(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteRRCState, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteReleaseVersion(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteReleaseVersion, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteScanDuration(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteScanDuration, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getPOLCapability(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getPOLCapability, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getPlmnNameFromSE13Table(int serial, int mcc, int mnc, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mcc);
                    _data.writeInt(mnc);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getQamEnabled(int serial, boolean ulOrDl, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(ulOrDl);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getRoamingEnable(int serial, int phoneId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(phoneId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getRoamingEnable, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSignalStrengthWithWcdmaEcio(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSignalStrengthWithWcdmaEcio, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSmartRatSwitch(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSuggestedPlmnList(int serial, int rat, int num, int timer, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rat);
                    _data.writeInt(num);
                    _data.writeInt(timer);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSuggestedPlmnList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getTOEInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getTm9Enabled(int serial, boolean fddOrTdd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTm9Enabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void queryFemtoCellSystemSelectionMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void selectFemtocell(int serial, String operatorNumeric, String act, String csgId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(operatorNumeric);
                    _data.writeString(act);
                    _data.writeString(csgId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_selectFemtocell, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void set4x4MimoEnabled(int serial, int enabled_bitmask, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(enabled_bitmask);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_set4x4MimoEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setApcMode(int serial, int mode, int reportMode, int interval, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(reportMode);
                    _data.writeInt(interval);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setApcMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setBandPriorityList(int serial, int[] bandPriList, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(bandPriList);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBandPriorityList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setBgsrchDeltaSleepTimer(int serial, int sleepDuration, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(sleepDuration);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBgsrchDeltaSleepTimer, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setCALinkEnableStatus(int serial, boolean status, String bandsCombo, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(status);
                    _data.writeString(bandsCombo);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setDisable2G(int serial, boolean mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setFemtoCellSystemSelectionMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteBandEnableStatus(int serial, int bandId, boolean status, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(bandId);
                    _data.writeBoolean(status);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteBandEnableStatus, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteBsrTimer(int serial, int timer, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(timer);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteReleaseVersion(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteScanDuration(int serial, int duration, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(duration);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNROption(int serial, int option, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(option);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNROption, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNetworkSelectionModeManualWithAct(int serial, String operatorNumeric, String act, String mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(operatorNumeric);
                    _data.writeString(act);
                    _data.writeString(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNrBandMode(int serial, int[] saEnable, int[] saDisable, int[] nsaEnable, int[] nsaDisable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(saEnable);
                    _data.writeIntArray(saDisable);
                    _data.writeIntArray(nsaEnable);
                    _data.writeIntArray(nsaDisable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNrBandMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setPOLEntry(int serial, int index, String numeric, int nAct, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index);
                    _data.writeString(numeric);
                    _data.writeInt(nAct);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setQamEnabled(int serial, boolean ulOrDl, boolean enabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(ulOrDl);
                    _data.writeBoolean(enabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setQamEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setSearchRat(int serial, int[] rat, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(rat);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchRat, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setSearchStoredFreqInfo(int serial, int operation, int plmn_id, int rat, int[] freq, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(operation);
                    _data.writeInt(plmn_id);
                    _data.writeInt(rat);
                    _data.writeIntArray(freq);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchStoredFreqInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setServiceStateToModem(int serial, int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(voiceRegState);
                    _data.writeInt(dataRegState);
                    _data.writeInt(voiceRoamingType);
                    _data.writeInt(dataRoamingType);
                    _data.writeInt(rilVoiceRegState);
                    _data.writeInt(rilDataRegState);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setTm9Enabled(int serial, boolean fddOrTdd, boolean enabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeBoolean(enabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setTm9Enabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void smartRatSwitch(int serial, int mode, int rat, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(rat);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_smartRatSwitch, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setRoamingEnable(int serial, int[] config, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(config);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setRoamingEnable, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(64, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setResponseFunctionsMtk(IMtkRadioExNetworkResponse radioResponse, IMtkRadioExNetworkIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMtk, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse radioResponse, ISmartRatSwitchRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsSmartRatSwitch, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
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

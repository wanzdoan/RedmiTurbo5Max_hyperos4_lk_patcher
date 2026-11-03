package com.mediatek.internal.telephony;

import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import com.mediatek.telephony.IOemHookCallback;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkTelephonyEx extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.IMtkTelephonyEx";

    boolean abortFemtoCellList(int i) throws RemoteException;

    boolean canSwitchSimType(int i) throws RemoteException;

    boolean cancelAvailableNetworks(int i) throws RemoteException;

    boolean exitEmergencyCallbackMode(int i) throws RemoteException;

    int[] getAdnStorageInfo(int i) throws RemoteException;

    PseudoCellInfo getApcInfoUsingSlotId(int i) throws RemoteException;

    boolean getCallSubAddressEnabled(int i) throws RemoteException;

    int getCdmaSubscriptionActStatus(int i) throws RemoteException;

    int getCurrentSimType(int i) throws RemoteException;

    int getDisable2G(int i) throws RemoteException;

    int getDsdaState() throws RemoteException;

    List<FemtoCellInfo> getFemtoCellList(int i) throws RemoteException;

    int getIccAppFamily(int i) throws RemoteException;

    String getIccAtr(int i) throws RemoteException;

    String getIccCardType(int i) throws RemoteException;

    boolean getIsLastEccIms() throws RemoteException;

    String getLocatedPlmn(int i) throws RemoteException;

    int getMainCapabilityPhoneId() throws RemoteException;

    String getMvnoMatchType(int i) throws RemoteException;

    String getMvnoPattern(int i, String str) throws RemoteException;

    int getPCO520State(int i) throws RemoteException;

    List<PreferredOperatorInfo> getPreferredOperatorList(int i) throws RemoteException;

    int[] getRoamingEnable(int i) throws RemoteException;

    int getSelfActivateState(int i) throws RemoteException;

    ServiceState getServiceStateByPhoneId(int i, String str, String str2) throws RemoteException;

    int getSimLockStateForRSU(int i) throws RemoteException;

    int getSimOnOffExecutingState(int i) throws RemoteException;

    int getSimOnOffState(int i) throws RemoteException;

    String[] getSimOperatorNumericForPhoneEx(int i) throws RemoteException;

    int getSimTypeExecutingStatus(int i) throws RemoteException;

    int getSimTypeExecutingStatusAll() throws RemoteException;

    String[] getSuggestedPlmnList(int i, int i2, int i3, int i4, String str) throws RemoteException;

    String getUimSubscriberId(String str, int i) throws RemoteException;

    @Deprecated
    int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    int invokeOemRilRequestRawBySlot(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean isAppTypeSupported(int i, int i2) throws RemoteException;

    boolean isCapabilitySwitching() throws RemoteException;

    boolean isEmergencyNumber(int i, String str) throws RemoteException;

    boolean isFdnEnabled(int i) throws RemoteException;

    boolean isImsRegistered(int i) throws RemoteException;

    boolean isInCsCall(int i) throws RemoteException;

    boolean isInEmergencyCall() throws RemoteException;

    boolean isInHomeNetwork(int i) throws RemoteException;

    boolean isPhbReady(int i) throws RemoteException;

    boolean isRadioOffBySimManagement(int i) throws RemoteException;

    boolean isTestIccCard(int i) throws RemoteException;

    boolean isVolteEnabled(int i) throws RemoteException;

    boolean isWifiCallingEnabled(int i) throws RemoteException;

    List<String> loadEFLinearFixedAll(int i, int i2, int i3, String str) throws RemoteException;

    byte[] loadEFTransparent(int i, int i2, int i3, String str) throws RemoteException;

    int queryFemtoCellSystemSelectionMode(int i) throws RemoteException;

    Bundle queryNetworkLock(int i, int i2) throws RemoteException;

    void registerAtUrcInd(int i, IOemHookCallback iOemHookCallback) throws RemoteException;

    void repollIccStateForNetworkLock(int i, boolean z) throws RemoteException;

    boolean selectFemtoCell(int i, FemtoCellInfo femtoCellInfo) throws RemoteException;

    int selfActivationAction(int i, Bundle bundle, int i2) throws RemoteException;

    void sendAtCmd(int i, long j, String str, IOemHookCallback iOemHookCallback) throws RemoteException;

    void setApcModeUsingSlotId(int i, int i2, boolean z, int i3) throws RemoteException;

    void setCallSubAddressEnabled(int i, boolean z) throws RemoteException;

    boolean setDisable2G(int i, boolean z) throws RemoteException;

    boolean setFemtoCellSystemSelectionMode(int i, int i2) throws RemoteException;

    void setIsLastEccIms(boolean z) throws RemoteException;

    boolean setPreferredOperator(int i, PreferredOperatorInfo preferredOperatorInfo) throws RemoteException;

    boolean setPreferredPhysicalSim(int i) throws RemoteException;

    boolean setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) throws RemoteException;

    boolean setRoamingEnable(int i, int[] iArr) throws RemoteException;

    int setSimPower(int i, int i2) throws RemoteException;

    int[] supplyDeviceNetworkDepersonalization(String str) throws RemoteException;

    int supplyNetworkDepersonalization(int i, String str) throws RemoteException;

    boolean supportSimTypeSwitch() throws RemoteException;

    int switchSimType(int i, int i2) throws RemoteException;

    public static class Default implements IMtkTelephonyEx {
        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isInHomeNetwork(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getIccAppFamily(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getIccCardType(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isAppTypeSupported(int slotId, int appType) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isTestIccCard(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getIccAtr(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public byte[] loadEFTransparent(int slotId, int family, int fileID, String filePath) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public List<String> loadEFLinearFixedAll(int slotId, int family, int fileID, String filePath) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int setSimPower(int slotId, int state) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSimOnOffState(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSimOnOffExecutingState(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public Bundle queryNetworkLock(int subId, int category) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int supplyNetworkDepersonalization(int subId, String strPasswd) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void repollIccStateForNetworkLock(int subId, boolean needIntent) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getMvnoMatchType(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getMvnoPattern(int subId, String type) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isRadioOffBySimManagement(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isFdnEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getUimSubscriberId(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String[] getSimOperatorNumericForPhoneEx(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isCapabilitySwitching() throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getMainCapabilityPhoneId() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isImsRegistered(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isVolteEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isWifiCallingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int[] getAdnStorageInfo(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isPhbReady(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int selfActivationAction(int action, Bundle param, int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSelfActivateState(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getPCO520State(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean exitEmergencyCallbackMode(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void setApcModeUsingSlotId(int slotId, int mode, boolean reportOn, int reportInterval) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public PseudoCellInfo getApcInfoUsingSlotId(int slotId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getCdmaSubscriptionActStatus(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void setIsLastEccIms(boolean val) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean getIsLastEccIms() throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void registerAtUrcInd(int slotId, IOemHookCallback cb) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void sendAtCmd(int slotId, long token, String atCmd, IOemHookCallback cb) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        @Deprecated
        public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int invokeOemRilRequestRawBySlot(int slotId, byte[] oemReq, byte[] oemResp) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isInCsCall(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String getLocatedPlmn(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setDisable2G(int phoneId, boolean mode) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getDisable2G(int phoneId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public List<FemtoCellInfo> getFemtoCellList(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean abortFemtoCellList(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean selectFemtoCell(int phoneId, FemtoCellInfo femtocell) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int queryFemtoCellSystemSelectionMode(int phoneId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setFemtoCellSystemSelectionMode(int phoneId, int mode) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean cancelAvailableNetworks(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int[] supplyDeviceNetworkDepersonalization(String pwd) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSimLockStateForRSU(int phoneId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public ServiceState getServiceStateByPhoneId(int phoneId, String callingPackage, String callingFeatureId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setRoamingEnable(int phoneId, int[] config) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int[] getRoamingEnable(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public String[] getSuggestedPlmnList(int phoneId, int rat, int num, int timer, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isEmergencyNumber(int phoneId, String number) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getDsdaState() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean getCallSubAddressEnabled(int phoneId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public void setCallSubAddressEnabled(int phoneId, boolean enable) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean isInEmergencyCall() throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public List<PreferredOperatorInfo> getPreferredOperatorList(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setPreferredOperator(int phoneId, PreferredOperatorInfo info) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean supportSimTypeSwitch() throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean canSwitchSimType(int slotId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getCurrentSimType(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int switchSimType(int slotId, int mode) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSimTypeExecutingStatus(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public int getSimTypeExecutingStatusAll() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
        public boolean setPreferredPhysicalSim(int slotId) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkTelephonyEx {
        static final int TRANSACTION_abortFemtoCellList = 47;
        static final int TRANSACTION_canSwitchSimType = 66;
        static final int TRANSACTION_cancelAvailableNetworks = 51;
        static final int TRANSACTION_exitEmergencyCallbackMode = 32;
        static final int TRANSACTION_getAdnStorageInfo = 27;
        static final int TRANSACTION_getApcInfoUsingSlotId = 34;
        static final int TRANSACTION_getCallSubAddressEnabled = 60;
        static final int TRANSACTION_getCdmaSubscriptionActStatus = 35;
        static final int TRANSACTION_getCurrentSimType = 67;
        static final int TRANSACTION_getDisable2G = 45;
        static final int TRANSACTION_getDsdaState = 59;
        static final int TRANSACTION_getFemtoCellList = 46;
        static final int TRANSACTION_getIccAppFamily = 2;
        static final int TRANSACTION_getIccAtr = 6;
        static final int TRANSACTION_getIccCardType = 3;
        static final int TRANSACTION_getIsLastEccIms = 37;
        static final int TRANSACTION_getLocatedPlmn = 43;
        static final int TRANSACTION_getMainCapabilityPhoneId = 23;
        static final int TRANSACTION_getMvnoMatchType = 15;
        static final int TRANSACTION_getMvnoPattern = 16;
        static final int TRANSACTION_getPCO520State = 31;
        static final int TRANSACTION_getPreferredOperatorList = 63;
        static final int TRANSACTION_getRoamingEnable = 56;
        static final int TRANSACTION_getSelfActivateState = 30;
        static final int TRANSACTION_getServiceStateByPhoneId = 54;
        static final int TRANSACTION_getSimLockStateForRSU = 53;
        static final int TRANSACTION_getSimOnOffExecutingState = 11;
        static final int TRANSACTION_getSimOnOffState = 10;
        static final int TRANSACTION_getSimOperatorNumericForPhoneEx = 20;
        static final int TRANSACTION_getSimTypeExecutingStatus = 69;
        static final int TRANSACTION_getSimTypeExecutingStatusAll = 70;
        static final int TRANSACTION_getSuggestedPlmnList = 57;
        static final int TRANSACTION_getUimSubscriberId = 19;
        static final int TRANSACTION_invokeOemRilRequestRaw = 40;
        static final int TRANSACTION_invokeOemRilRequestRawBySlot = 41;
        static final int TRANSACTION_isAppTypeSupported = 4;
        static final int TRANSACTION_isCapabilitySwitching = 22;
        static final int TRANSACTION_isEmergencyNumber = 58;
        static final int TRANSACTION_isFdnEnabled = 18;
        static final int TRANSACTION_isImsRegistered = 24;
        static final int TRANSACTION_isInCsCall = 42;
        static final int TRANSACTION_isInEmergencyCall = 62;
        static final int TRANSACTION_isInHomeNetwork = 1;
        static final int TRANSACTION_isPhbReady = 28;
        static final int TRANSACTION_isRadioOffBySimManagement = 17;
        static final int TRANSACTION_isTestIccCard = 5;
        static final int TRANSACTION_isVolteEnabled = 25;
        static final int TRANSACTION_isWifiCallingEnabled = 26;
        static final int TRANSACTION_loadEFLinearFixedAll = 8;
        static final int TRANSACTION_loadEFTransparent = 7;
        static final int TRANSACTION_queryFemtoCellSystemSelectionMode = 49;
        static final int TRANSACTION_queryNetworkLock = 12;
        static final int TRANSACTION_registerAtUrcInd = 38;
        static final int TRANSACTION_repollIccStateForNetworkLock = 14;
        static final int TRANSACTION_selectFemtoCell = 48;
        static final int TRANSACTION_selfActivationAction = 29;
        static final int TRANSACTION_sendAtCmd = 39;
        static final int TRANSACTION_setApcModeUsingSlotId = 33;
        static final int TRANSACTION_setCallSubAddressEnabled = 61;
        static final int TRANSACTION_setDisable2G = 44;
        static final int TRANSACTION_setFemtoCellSystemSelectionMode = 50;
        static final int TRANSACTION_setIsLastEccIms = 36;
        static final int TRANSACTION_setPreferredOperator = 64;
        static final int TRANSACTION_setPreferredPhysicalSim = 71;
        static final int TRANSACTION_setRadioCapability = 21;
        static final int TRANSACTION_setRoamingEnable = 55;
        static final int TRANSACTION_setSimPower = 9;
        static final int TRANSACTION_supplyDeviceNetworkDepersonalization = 52;
        static final int TRANSACTION_supplyNetworkDepersonalization = 13;
        static final int TRANSACTION_supportSimTypeSwitch = 65;
        static final int TRANSACTION_switchSimType = 68;

        public Stub() {
            attachInterface(this, IMtkTelephonyEx.DESCRIPTOR);
        }

        public static IMtkTelephonyEx asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkTelephonyEx.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkTelephonyEx)) {
                return (IMtkTelephonyEx) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            byte[] _arg1;
            byte[] _arg2;
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IMtkTelephonyEx.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result = isInHomeNetwork(_arg0);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result2 = getIccAppFamily(_arg3);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result3 = getIccCardType(_arg4);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result4 = isAppTypeSupported(_arg5, _arg6);
                    reply.writeNoException();
                    reply.writeBoolean(_result4);
                    return true;
                case 5:
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result5 = isTestIccCard(_arg7);
                    reply.writeNoException();
                    reply.writeBoolean(_result5);
                    return true;
                case 6:
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result6 = getIccAtr(_arg8);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 7:
                    int _arg9 = data.readInt();
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    String _arg12 = data.readString();
                    data.enforceNoDataAvail();
                    byte[] _result7 = loadEFTransparent(_arg9, _arg10, _arg11, _arg12);
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 8:
                    int _arg13 = data.readInt();
                    int _arg14 = data.readInt();
                    int _arg15 = data.readInt();
                    String _arg16 = data.readString();
                    data.enforceNoDataAvail();
                    List<String> _result8 = loadEFLinearFixedAll(_arg13, _arg14, _arg15, _arg16);
                    reply.writeNoException();
                    reply.writeStringList(_result8);
                    return true;
                case 9:
                    int _arg17 = data.readInt();
                    int _arg18 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result9 = setSimPower(_arg17, _arg18);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 10:
                    int _arg19 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result10 = getSimOnOffState(_arg19);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 11:
                    int _arg20 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result11 = getSimOnOffExecutingState(_arg20);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 12:
                    int _arg21 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    Bundle _result12 = queryNetworkLock(_arg21, _arg22);
                    reply.writeNoException();
                    reply.writeTypedObject(_result12, 1);
                    return true;
                case 13:
                    int _arg23 = data.readInt();
                    String _arg24 = data.readString();
                    data.enforceNoDataAvail();
                    int _result13 = supplyNetworkDepersonalization(_arg23, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 14:
                    int _arg25 = data.readInt();
                    boolean _arg26 = data.readBoolean();
                    data.enforceNoDataAvail();
                    repollIccStateForNetworkLock(_arg25, _arg26);
                    reply.writeNoException();
                    return true;
                case 15:
                    int _arg27 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result14 = getMvnoMatchType(_arg27);
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 16:
                    int _arg28 = data.readInt();
                    String _arg29 = data.readString();
                    data.enforceNoDataAvail();
                    String _result15 = getMvnoPattern(_arg28, _arg29);
                    reply.writeNoException();
                    reply.writeString(_result15);
                    return true;
                case 17:
                    int _arg30 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result16 = isRadioOffBySimManagement(_arg30);
                    reply.writeNoException();
                    reply.writeBoolean(_result16);
                    return true;
                case 18:
                    int _arg31 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result17 = isFdnEnabled(_arg31);
                    reply.writeNoException();
                    reply.writeBoolean(_result17);
                    return true;
                case TRANSACTION_getUimSubscriberId /* 19 */:
                    String _arg32 = data.readString();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result18 = getUimSubscriberId(_arg32, _arg33);
                    reply.writeNoException();
                    reply.writeString(_result18);
                    return true;
                case 20:
                    int _arg34 = data.readInt();
                    data.enforceNoDataAvail();
                    String[] _result19 = getSimOperatorNumericForPhoneEx(_arg34);
                    reply.writeNoException();
                    reply.writeStringArray(_result19);
                    return true;
                case TRANSACTION_setRadioCapability /* 21 */:
                    RadioAccessFamily[] _arg35 = (RadioAccessFamily[]) data.createTypedArray(RadioAccessFamily.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result20 = setRadioCapability(_arg35);
                    reply.writeNoException();
                    reply.writeBoolean(_result20);
                    return true;
                case TRANSACTION_isCapabilitySwitching /* 22 */:
                    boolean _result21 = isCapabilitySwitching();
                    reply.writeNoException();
                    reply.writeBoolean(_result21);
                    return true;
                case TRANSACTION_getMainCapabilityPhoneId /* 23 */:
                    int _result22 = getMainCapabilityPhoneId();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 24:
                    int _arg36 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result23 = isImsRegistered(_arg36);
                    reply.writeNoException();
                    reply.writeBoolean(_result23);
                    return true;
                case TRANSACTION_isVolteEnabled /* 25 */:
                    int _arg37 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result24 = isVolteEnabled(_arg37);
                    reply.writeNoException();
                    reply.writeBoolean(_result24);
                    return true;
                case TRANSACTION_isWifiCallingEnabled /* 26 */:
                    int _arg38 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result25 = isWifiCallingEnabled(_arg38);
                    reply.writeNoException();
                    reply.writeBoolean(_result25);
                    return true;
                case TRANSACTION_getAdnStorageInfo /* 27 */:
                    int _arg39 = data.readInt();
                    data.enforceNoDataAvail();
                    int[] _result26 = getAdnStorageInfo(_arg39);
                    reply.writeNoException();
                    reply.writeIntArray(_result26);
                    return true;
                case TRANSACTION_isPhbReady /* 28 */:
                    int _arg40 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result27 = isPhbReady(_arg40);
                    reply.writeNoException();
                    reply.writeBoolean(_result27);
                    return true;
                case TRANSACTION_selfActivationAction /* 29 */:
                    int _arg41 = data.readInt();
                    Bundle _arg42 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result28 = selfActivationAction(_arg41, _arg42, _arg43);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case TRANSACTION_getSelfActivateState /* 30 */:
                    int _arg44 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result29 = getSelfActivateState(_arg44);
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case TRANSACTION_getPCO520State /* 31 */:
                    int _arg45 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result30 = getPCO520State(_arg45);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 32:
                    int _arg46 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result31 = exitEmergencyCallbackMode(_arg46);
                    reply.writeNoException();
                    reply.writeBoolean(_result31);
                    return true;
                case 33:
                    int _arg47 = data.readInt();
                    int _arg48 = data.readInt();
                    boolean _arg49 = data.readBoolean();
                    int _arg50 = data.readInt();
                    data.enforceNoDataAvail();
                    setApcModeUsingSlotId(_arg47, _arg48, _arg49, _arg50);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getApcInfoUsingSlotId /* 34 */:
                    int _arg51 = data.readInt();
                    data.enforceNoDataAvail();
                    PseudoCellInfo _result32 = getApcInfoUsingSlotId(_arg51);
                    reply.writeNoException();
                    reply.writeTypedObject(_result32, 1);
                    return true;
                case TRANSACTION_getCdmaSubscriptionActStatus /* 35 */:
                    int _arg52 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result33 = getCdmaSubscriptionActStatus(_arg52);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 36:
                    boolean _arg53 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setIsLastEccIms(_arg53);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getIsLastEccIms /* 37 */:
                    boolean _result34 = getIsLastEccIms();
                    reply.writeNoException();
                    reply.writeBoolean(_result34);
                    return true;
                case 38:
                    int _arg54 = data.readInt();
                    IOemHookCallback _arg55 = IOemHookCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerAtUrcInd(_arg54, _arg55);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_sendAtCmd /* 39 */:
                    int _arg56 = data.readInt();
                    long _arg57 = data.readLong();
                    String _arg58 = data.readString();
                    IOemHookCallback _arg59 = IOemHookCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    sendAtCmd(_arg56, _arg57, _arg58, _arg59);
                    reply.writeNoException();
                    return true;
                case 40:
                    byte[] _arg60 = data.createByteArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length > 1000000) {
                        throw new BadParcelableException("Array too large: " + _arg1_length);
                    }
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    data.enforceNoDataAvail();
                    int _result35 = invokeOemRilRequestRaw(_arg60, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    reply.writeByteArray(_arg1);
                    return true;
                case TRANSACTION_invokeOemRilRequestRawBySlot /* 41 */:
                    int _arg61 = data.readInt();
                    byte[] _arg62 = data.createByteArray();
                    int _arg2_length = data.readInt();
                    if (_arg2_length > 1000000) {
                        throw new BadParcelableException("Array too large: " + _arg2_length);
                    }
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new byte[_arg2_length];
                    }
                    data.enforceNoDataAvail();
                    int _result36 = invokeOemRilRequestRawBySlot(_arg61, _arg62, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    reply.writeByteArray(_arg2);
                    return true;
                case TRANSACTION_isInCsCall /* 42 */:
                    int _arg63 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result37 = isInCsCall(_arg63);
                    reply.writeNoException();
                    reply.writeBoolean(_result37);
                    return true;
                case TRANSACTION_getLocatedPlmn /* 43 */:
                    int _arg64 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result38 = getLocatedPlmn(_arg64);
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case TRANSACTION_setDisable2G /* 44 */:
                    int _arg65 = data.readInt();
                    boolean _arg66 = data.readBoolean();
                    data.enforceNoDataAvail();
                    boolean _result39 = setDisable2G(_arg65, _arg66);
                    reply.writeNoException();
                    reply.writeBoolean(_result39);
                    return true;
                case TRANSACTION_getDisable2G /* 45 */:
                    int _arg67 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result40 = getDisable2G(_arg67);
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                case 46:
                    int _arg68 = data.readInt();
                    data.enforceNoDataAvail();
                    List<FemtoCellInfo> _result41 = getFemtoCellList(_arg68);
                    reply.writeNoException();
                    reply.writeTypedList(_result41, 1);
                    return true;
                case 47:
                    int _arg69 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result42 = abortFemtoCellList(_arg69);
                    reply.writeNoException();
                    reply.writeBoolean(_result42);
                    return true;
                case 48:
                    int _arg70 = data.readInt();
                    FemtoCellInfo _arg71 = (FemtoCellInfo) data.readTypedObject(FemtoCellInfo.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result43 = selectFemtoCell(_arg70, _arg71);
                    reply.writeNoException();
                    reply.writeBoolean(_result43);
                    return true;
                case TRANSACTION_queryFemtoCellSystemSelectionMode /* 49 */:
                    int _arg72 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result44 = queryFemtoCellSystemSelectionMode(_arg72);
                    reply.writeNoException();
                    reply.writeInt(_result44);
                    return true;
                case 50:
                    int _arg73 = data.readInt();
                    int _arg74 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result45 = setFemtoCellSystemSelectionMode(_arg73, _arg74);
                    reply.writeNoException();
                    reply.writeBoolean(_result45);
                    return true;
                case 51:
                    int _arg75 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result46 = cancelAvailableNetworks(_arg75);
                    reply.writeNoException();
                    reply.writeBoolean(_result46);
                    return true;
                case 52:
                    String _arg76 = data.readString();
                    data.enforceNoDataAvail();
                    int[] _result47 = supplyDeviceNetworkDepersonalization(_arg76);
                    reply.writeNoException();
                    reply.writeIntArray(_result47);
                    return true;
                case TRANSACTION_getSimLockStateForRSU /* 53 */:
                    int _arg77 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result48 = getSimLockStateForRSU(_arg77);
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    return true;
                case 54:
                    int _arg78 = data.readInt();
                    String _arg79 = data.readString();
                    String _arg80 = data.readString();
                    data.enforceNoDataAvail();
                    ServiceState _result49 = getServiceStateByPhoneId(_arg78, _arg79, _arg80);
                    reply.writeNoException();
                    reply.writeTypedObject(_result49, 1);
                    return true;
                case TRANSACTION_setRoamingEnable /* 55 */:
                    int _arg81 = data.readInt();
                    int[] _arg82 = data.createIntArray();
                    data.enforceNoDataAvail();
                    boolean _result50 = setRoamingEnable(_arg81, _arg82);
                    reply.writeNoException();
                    reply.writeBoolean(_result50);
                    return true;
                case 56:
                    int _arg83 = data.readInt();
                    data.enforceNoDataAvail();
                    int[] _result51 = getRoamingEnable(_arg83);
                    reply.writeNoException();
                    reply.writeIntArray(_result51);
                    return true;
                case TRANSACTION_getSuggestedPlmnList /* 57 */:
                    int _arg84 = data.readInt();
                    int _arg85 = data.readInt();
                    int _arg86 = data.readInt();
                    int _arg87 = data.readInt();
                    String _arg88 = data.readString();
                    data.enforceNoDataAvail();
                    String[] _result52 = getSuggestedPlmnList(_arg84, _arg85, _arg86, _arg87, _arg88);
                    reply.writeNoException();
                    reply.writeStringArray(_result52);
                    return true;
                case TRANSACTION_isEmergencyNumber /* 58 */:
                    int _arg89 = data.readInt();
                    String _arg90 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result53 = isEmergencyNumber(_arg89, _arg90);
                    reply.writeNoException();
                    reply.writeBoolean(_result53);
                    return true;
                case TRANSACTION_getDsdaState /* 59 */:
                    int _result54 = getDsdaState();
                    reply.writeNoException();
                    reply.writeInt(_result54);
                    return true;
                case 60:
                    int _arg91 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result55 = getCallSubAddressEnabled(_arg91);
                    reply.writeNoException();
                    reply.writeBoolean(_result55);
                    return true;
                case TRANSACTION_setCallSubAddressEnabled /* 61 */:
                    int _arg92 = data.readInt();
                    boolean _arg93 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setCallSubAddressEnabled(_arg92, _arg93);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isInEmergencyCall /* 62 */:
                    boolean _result56 = isInEmergencyCall();
                    reply.writeNoException();
                    reply.writeBoolean(_result56);
                    return true;
                case TRANSACTION_getPreferredOperatorList /* 63 */:
                    int _arg94 = data.readInt();
                    data.enforceNoDataAvail();
                    List<PreferredOperatorInfo> _result57 = getPreferredOperatorList(_arg94);
                    reply.writeNoException();
                    reply.writeTypedList(_result57, 1);
                    return true;
                case 64:
                    int _arg95 = data.readInt();
                    PreferredOperatorInfo _arg96 = (PreferredOperatorInfo) data.readTypedObject(PreferredOperatorInfo.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result58 = setPreferredOperator(_arg95, _arg96);
                    reply.writeNoException();
                    reply.writeBoolean(_result58);
                    return true;
                case TRANSACTION_supportSimTypeSwitch /* 65 */:
                    boolean _result59 = supportSimTypeSwitch();
                    reply.writeNoException();
                    reply.writeBoolean(_result59);
                    return true;
                case TRANSACTION_canSwitchSimType /* 66 */:
                    int _arg97 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result60 = canSwitchSimType(_arg97);
                    reply.writeNoException();
                    reply.writeBoolean(_result60);
                    return true;
                case TRANSACTION_getCurrentSimType /* 67 */:
                    int _arg98 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result61 = getCurrentSimType(_arg98);
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case TRANSACTION_switchSimType /* 68 */:
                    int _arg99 = data.readInt();
                    int _arg100 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result62 = switchSimType(_arg99, _arg100);
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case TRANSACTION_getSimTypeExecutingStatus /* 69 */:
                    int _arg101 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result63 = getSimTypeExecutingStatus(_arg101);
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case TRANSACTION_getSimTypeExecutingStatusAll /* 70 */:
                    int _result64 = getSimTypeExecutingStatusAll();
                    reply.writeNoException();
                    reply.writeInt(_result64);
                    return true;
                case 71:
                    int _arg102 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result65 = setPreferredPhysicalSim(_arg102);
                    reply.writeNoException();
                    reply.writeBoolean(_result65);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkTelephonyEx {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IMtkTelephonyEx.DESCRIPTOR;
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isInHomeNetwork(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getIccAppFamily(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getIccCardType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isAppTypeSupported(int slotId, int appType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(appType);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isTestIccCard(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getIccAtr(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public byte[] loadEFTransparent(int slotId, int family, int fileID, String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(family);
                    _data.writeInt(fileID);
                    _data.writeString(filePath);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public List<String> loadEFLinearFixedAll(int slotId, int family, int fileID, String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(family);
                    _data.writeInt(fileID);
                    _data.writeString(filePath);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int setSimPower(int slotId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(state);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSimOnOffState(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSimOnOffExecutingState(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public Bundle queryNetworkLock(int subId, int category) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(category);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int supplyNetworkDepersonalization(int subId, String strPasswd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(strPasswd);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void repollIccStateForNetworkLock(int subId, boolean needIntent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeBoolean(needIntent);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getMvnoMatchType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getMvnoPattern(int subId, String type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(type);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isRadioOffBySimManagement(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isFdnEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getUimSubscriberId(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUimSubscriberId, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String[] getSimOperatorNumericForPhoneEx(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeTypedArray(rafs, 0);
                    this.mRemote.transact(Stub.TRANSACTION_setRadioCapability, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isCapabilitySwitching() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isCapabilitySwitching, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getMainCapabilityPhoneId() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getMainCapabilityPhoneId, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isImsRegistered(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isVolteEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_isVolteEnabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isWifiCallingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_isWifiCallingEnabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int[] getAdnStorageInfo(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getAdnStorageInfo, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isPhbReady(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_isPhbReady, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int selfActivationAction(int action, Bundle param, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeTypedObject(param, 0);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_selfActivationAction, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSelfActivateState(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getSelfActivateState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getPCO520State(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getPCO520State, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean exitEmergencyCallbackMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void setApcModeUsingSlotId(int slotId, int mode, boolean reportOn, int reportInterval) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(mode);
                    _data.writeBoolean(reportOn);
                    _data.writeInt(reportInterval);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public PseudoCellInfo getApcInfoUsingSlotId(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(Stub.TRANSACTION_getApcInfoUsingSlotId, _data, _reply, 0);
                    _reply.readException();
                    PseudoCellInfo _result = (PseudoCellInfo) _reply.readTypedObject(PseudoCellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getCdmaSubscriptionActStatus(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getCdmaSubscriptionActStatus, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void setIsLastEccIms(boolean val) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeBoolean(val);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean getIsLastEccIms() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getIsLastEccIms, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void registerAtUrcInd(int slotId, IOemHookCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void sendAtCmd(int slotId, long token, String atCmd, IOemHookCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeLong(token);
                    _data.writeString(atCmd);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(Stub.TRANSACTION_sendAtCmd, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            @Deprecated
            public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeByteArray(oemReq);
                    _data.writeInt(oemResp.length);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int invokeOemRilRequestRawBySlot(int slotId, byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeByteArray(oemReq);
                    _data.writeInt(oemResp.length);
                    this.mRemote.transact(Stub.TRANSACTION_invokeOemRilRequestRawBySlot, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isInCsCall(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_isInCsCall, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String getLocatedPlmn(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_getLocatedPlmn, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setDisable2G(int phoneId, boolean mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(mode);
                    this.mRemote.transact(Stub.TRANSACTION_setDisable2G, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getDisable2G(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_getDisable2G, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public List<FemtoCellInfo> getFemtoCellList(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    List<FemtoCellInfo> _result = _reply.createTypedArrayList(FemtoCellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean abortFemtoCellList(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean selectFemtoCell(int phoneId, FemtoCellInfo femtocell) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeTypedObject(femtocell, 0);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int queryFemtoCellSystemSelectionMode(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_queryFemtoCellSystemSelectionMode, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setFemtoCellSystemSelectionMode(int phoneId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(mode);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean cancelAvailableNetworks(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int[] supplyDeviceNetworkDepersonalization(String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeString(pwd);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSimLockStateForRSU(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_getSimLockStateForRSU, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public ServiceState getServiceStateByPhoneId(int phoneId, String callingPackage, String callingFeatureId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    _data.writeString(callingFeatureId);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    ServiceState _result = (ServiceState) _reply.readTypedObject(ServiceState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setRoamingEnable(int phoneId, int[] config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeIntArray(config);
                    this.mRemote.transact(Stub.TRANSACTION_setRoamingEnable, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int[] getRoamingEnable(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public String[] getSuggestedPlmnList(int phoneId, int rat, int num, int timer, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(rat);
                    _data.writeInt(num);
                    _data.writeInt(timer);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(Stub.TRANSACTION_getSuggestedPlmnList, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isEmergencyNumber(int phoneId, String number) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(number);
                    this.mRemote.transact(Stub.TRANSACTION_isEmergencyNumber, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getDsdaState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getDsdaState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean getCallSubAddressEnabled(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public void setCallSubAddressEnabled(int phoneId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(Stub.TRANSACTION_setCallSubAddressEnabled, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean isInEmergencyCall() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isInEmergencyCall, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public List<PreferredOperatorInfo> getPreferredOperatorList(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(Stub.TRANSACTION_getPreferredOperatorList, _data, _reply, 0);
                    _reply.readException();
                    List<PreferredOperatorInfo> _result = _reply.createTypedArrayList(PreferredOperatorInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setPreferredOperator(int phoneId, PreferredOperatorInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeTypedObject(info, 0);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean supportSimTypeSwitch() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_supportSimTypeSwitch, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean canSwitchSimType(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(Stub.TRANSACTION_canSwitchSimType, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getCurrentSimType(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(Stub.TRANSACTION_getCurrentSimType, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int switchSimType(int slotId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(mode);
                    this.mRemote.transact(Stub.TRANSACTION_switchSimType, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSimTypeExecutingStatus(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(Stub.TRANSACTION_getSimTypeExecutingStatus, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public int getSimTypeExecutingStatusAll() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSimTypeExecutingStatusAll, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkTelephonyEx
            public boolean setPreferredPhysicalSim(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkTelephonyEx.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

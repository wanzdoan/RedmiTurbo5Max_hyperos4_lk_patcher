package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IXmTelephony extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.IXmTelephony";

    boolean enableEndc(int i, boolean z, ResultReceiver resultReceiver) throws RemoteException;

    boolean getDdsAssistantInfo(ResultReceiver resultReceiver) throws RemoteException;

    int getHalVersion(int i) throws RemoteException;

    boolean getModemReason(ResultReceiver resultReceiver) throws RemoteException;

    boolean getModemScene(ResultReceiver resultReceiver) throws RemoteException;

    boolean getSarDsi(ResultReceiver resultReceiver) throws RemoteException;

    boolean getSarDsiGroup(ResultReceiver resultReceiver) throws RemoteException;

    boolean promoteBSR(int i, int i2, ResultReceiver resultReceiver) throws RemoteException;

    boolean registerCommMsgCallback(int i, int i2, ICommMsgCallback iCommMsgCallback) throws RemoteException;

    boolean registerHstStateCallback(int i, IHstStateCallback iHstStateCallback) throws RemoteException;

    boolean registerModemExceptionCallback(IModemExceptionCallback iModemExceptionCallback) throws RemoteException;

    boolean registerModemSceneCallback(IModemSceneCallback iModemSceneCallback) throws RemoteException;

    boolean registerQosStateCallback(int i, IQosStateCallback iQosStateCallback) throws RemoteException;

    boolean restartRILD(int i, ResultReceiver resultReceiver) throws RemoteException;

    boolean sendCommMsg(int i, int i2, byte[] bArr, ResultReceiver resultReceiver) throws RemoteException;

    byte[] sendCommMsgSync(int i, int i2, byte[] bArr) throws RemoteException;

    boolean setDataStallMode(int i, int i2, int i3, int i4, int i5, ResultReceiver resultReceiver) throws RemoteException;

    boolean setDeprioritizeSA(int i, boolean z, ResultReceiver resultReceiver) throws RemoteException;

    boolean setFiveTupleBlackList(String str, List<String> list, int i, ResultReceiver resultReceiver) throws RemoteException;

    boolean setMobilityOptParams(int i, boolean z, int i2, int i3, ResultReceiver resultReceiver) throws RemoteException;

    boolean setModemCallbackSwitch(boolean z, ResultReceiver resultReceiver) throws RemoteException;

    boolean setModemEventIndSwitch(int i, boolean z, ResultReceiver resultReceiver) throws RemoteException;

    boolean setModemModelCfg(int[] iArr, ResultReceiver resultReceiver) throws RemoteException;

    boolean setNrMode(int i, int i2, ResultReceiver resultReceiver) throws RemoteException;

    boolean setPreferBandParams(int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int i3, ResultReceiver resultReceiver) throws RemoteException;

    boolean setSarDsi(int i, ResultReceiver resultReceiver) throws RemoteException;

    boolean setSarDsiGroup(int[] iArr, ResultReceiver resultReceiver) throws RemoteException;

    boolean setSensorState(int i, int i2, int i3, ResultReceiver resultReceiver) throws RemoteException;

    boolean unRegisterCommMsgCallback(int i, int i2, ICommMsgCallback iCommMsgCallback) throws RemoteException;

    boolean unRegisterHstStateCallback(int i, IHstStateCallback iHstStateCallback) throws RemoteException;

    boolean unRegisterQosStateCallback(int i, IQosStateCallback iQosStateCallback) throws RemoteException;

    boolean unregisterModemExceptionCallback(IModemExceptionCallback iModemExceptionCallback) throws RemoteException;

    boolean unregisterModemSceneCallback(IModemSceneCallback iModemSceneCallback) throws RemoteException;

    public static class Default implements IXmTelephony {
        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean sendCommMsg(int phoneId, int msgId, byte[] data, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean registerCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean unRegisterCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean getSarDsi(ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean getSarDsiGroup(ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public byte[] sendCommMsgSync(int phoneId, int msgId, byte[] data) throws RemoteException {
            return null;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setDeprioritizeSA(int phoneId, boolean enable, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setNrMode(int phoneId, int nrStatus, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean enableEndc(int phoneId, boolean enable, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean registerHstStateCallback(int phoneId, IHstStateCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean unRegisterHstStateCallback(int phoneId, IHstStateCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean registerQosStateCallback(int slotId, IQosStateCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean unRegisterQosStateCallback(int slotId, IQosStateCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean restartRILD(int phoneId, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setDataStallMode(int phoneId, int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setMobilityOptParams(int phoneId, boolean enable, int rat, int featureId, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setPreferBandParams(int phoneId, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean promoteBSR(int phoneId, int level, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setSensorState(int phoneId, int sensorType, int sensorState, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean getModemScene(ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean getModemReason(ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean registerModemExceptionCallback(IModemExceptionCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean unregisterModemExceptionCallback(IModemExceptionCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean registerModemSceneCallback(IModemSceneCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean unregisterModemSceneCallback(IModemSceneCallback callback) throws RemoteException {
            return false;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public int getHalVersion(int phoneId) throws RemoteException {
            return 0;
        }

        @Override // com.xiaomi.internal.telephony.IXmTelephony
        public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IXmTelephony {
        static final int TRANSACTION_enableEndc = 11;
        static final int TRANSACTION_getDdsAssistantInfo = 22;
        static final int TRANSACTION_getHalVersion = 32;
        static final int TRANSACTION_getModemReason = 24;
        static final int TRANSACTION_getModemScene = 23;
        static final int TRANSACTION_getSarDsi = 6;
        static final int TRANSACTION_getSarDsiGroup = 7;
        static final int TRANSACTION_promoteBSR = 20;
        static final int TRANSACTION_registerCommMsgCallback = 2;
        static final int TRANSACTION_registerHstStateCallback = 12;
        static final int TRANSACTION_registerModemExceptionCallback = 28;
        static final int TRANSACTION_registerModemSceneCallback = 30;
        static final int TRANSACTION_registerQosStateCallback = 14;
        static final int TRANSACTION_restartRILD = 16;
        static final int TRANSACTION_sendCommMsg = 1;
        static final int TRANSACTION_sendCommMsgSync = 8;
        static final int TRANSACTION_setDataStallMode = 17;
        static final int TRANSACTION_setDeprioritizeSA = 9;
        static final int TRANSACTION_setFiveTupleBlackList = 33;
        static final int TRANSACTION_setMobilityOptParams = 18;
        static final int TRANSACTION_setModemCallbackSwitch = 26;
        static final int TRANSACTION_setModemEventIndSwitch = 27;
        static final int TRANSACTION_setModemModelCfg = 25;
        static final int TRANSACTION_setNrMode = 10;
        static final int TRANSACTION_setPreferBandParams = 19;
        static final int TRANSACTION_setSarDsi = 4;
        static final int TRANSACTION_setSarDsiGroup = 5;
        static final int TRANSACTION_setSensorState = 21;
        static final int TRANSACTION_unRegisterCommMsgCallback = 3;
        static final int TRANSACTION_unRegisterHstStateCallback = 13;
        static final int TRANSACTION_unRegisterQosStateCallback = 15;
        static final int TRANSACTION_unregisterModemExceptionCallback = 29;
        static final int TRANSACTION_unregisterModemSceneCallback = 31;

        public Stub() {
            attachInterface(this, IXmTelephony.DESCRIPTOR);
        }

        public static IXmTelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IXmTelephony.DESCRIPTOR);
            if (iin != null && (iin instanceof IXmTelephony)) {
                return (IXmTelephony) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IXmTelephony.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    byte[] _arg2 = data.createByteArray();
                    ResultReceiver _arg3 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result = sendCommMsg(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    ICommMsgCallback _arg6 = ICommMsgCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result2 = registerCommMsgCallback(_arg4, _arg5, _arg6);
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    int _arg7 = data.readInt();
                    int _arg8 = data.readInt();
                    ICommMsgCallback _arg9 = ICommMsgCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result3 = unRegisterCommMsgCallback(_arg7, _arg8, _arg9);
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                case 4:
                    int _arg10 = data.readInt();
                    ResultReceiver _arg11 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result4 = setSarDsi(_arg10, _arg11);
                    reply.writeNoException();
                    reply.writeBoolean(_result4);
                    return true;
                case 5:
                    int[] _arg12 = data.createIntArray();
                    ResultReceiver _arg13 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result5 = setSarDsiGroup(_arg12, _arg13);
                    reply.writeNoException();
                    reply.writeBoolean(_result5);
                    return true;
                case 6:
                    ResultReceiver _arg14 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result6 = getSarDsi(_arg14);
                    reply.writeNoException();
                    reply.writeBoolean(_result6);
                    return true;
                case 7:
                    ResultReceiver _arg15 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result7 = getSarDsiGroup(_arg15);
                    reply.writeNoException();
                    reply.writeBoolean(_result7);
                    return true;
                case 8:
                    int _arg16 = data.readInt();
                    int _arg17 = data.readInt();
                    byte[] _arg18 = data.createByteArray();
                    data.enforceNoDataAvail();
                    byte[] _result8 = sendCommMsgSync(_arg16, _arg17, _arg18);
                    reply.writeNoException();
                    reply.writeByteArray(_result8);
                    return true;
                case 9:
                    int _arg19 = data.readInt();
                    boolean _arg20 = data.readBoolean();
                    ResultReceiver _arg21 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result9 = setDeprioritizeSA(_arg19, _arg20, _arg21);
                    reply.writeNoException();
                    reply.writeBoolean(_result9);
                    return true;
                case 10:
                    int _arg22 = data.readInt();
                    int _arg23 = data.readInt();
                    ResultReceiver _arg24 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result10 = setNrMode(_arg22, _arg23, _arg24);
                    reply.writeNoException();
                    reply.writeBoolean(_result10);
                    return true;
                case 11:
                    int _arg25 = data.readInt();
                    boolean _arg26 = data.readBoolean();
                    ResultReceiver _arg27 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result11 = enableEndc(_arg25, _arg26, _arg27);
                    reply.writeNoException();
                    reply.writeBoolean(_result11);
                    return true;
                case 12:
                    int _arg28 = data.readInt();
                    IHstStateCallback _arg29 = IHstStateCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result12 = registerHstStateCallback(_arg28, _arg29);
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case 13:
                    int _arg30 = data.readInt();
                    IHstStateCallback _arg31 = IHstStateCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result13 = unRegisterHstStateCallback(_arg30, _arg31);
                    reply.writeNoException();
                    reply.writeBoolean(_result13);
                    return true;
                case 14:
                    int _arg32 = data.readInt();
                    IQosStateCallback _arg33 = IQosStateCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result14 = registerQosStateCallback(_arg32, _arg33);
                    reply.writeNoException();
                    reply.writeBoolean(_result14);
                    return true;
                case 15:
                    int _arg34 = data.readInt();
                    IQosStateCallback _arg35 = IQosStateCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result15 = unRegisterQosStateCallback(_arg34, _arg35);
                    reply.writeNoException();
                    reply.writeBoolean(_result15);
                    return true;
                case 16:
                    int _arg36 = data.readInt();
                    ResultReceiver _arg37 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result16 = restartRILD(_arg36, _arg37);
                    reply.writeNoException();
                    reply.writeBoolean(_result16);
                    return true;
                case 17:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    int _arg40 = data.readInt();
                    int _arg41 = data.readInt();
                    int _arg42 = data.readInt();
                    ResultReceiver _arg43 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result17 = setDataStallMode(_arg38, _arg39, _arg40, _arg41, _arg42, _arg43);
                    reply.writeNoException();
                    reply.writeBoolean(_result17);
                    return true;
                case 18:
                    int _arg44 = data.readInt();
                    boolean _arg45 = data.readBoolean();
                    int _arg46 = data.readInt();
                    int _arg47 = data.readInt();
                    ResultReceiver _arg48 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result18 = setMobilityOptParams(_arg44, _arg45, _arg46, _arg47, _arg48);
                    reply.writeNoException();
                    reply.writeBoolean(_result18);
                    return true;
                case 19:
                    int _arg49 = data.readInt();
                    int _arg50 = data.readInt();
                    int[] _arg51 = data.createIntArray();
                    int[] _arg52 = data.createIntArray();
                    int[] _arg53 = data.createIntArray();
                    int _arg54 = data.readInt();
                    ResultReceiver _arg55 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result19 = setPreferBandParams(_arg49, _arg50, _arg51, _arg52, _arg53, _arg54, _arg55);
                    reply.writeNoException();
                    reply.writeBoolean(_result19);
                    return true;
                case 20:
                    int _arg56 = data.readInt();
                    int _arg57 = data.readInt();
                    ResultReceiver _arg58 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result20 = promoteBSR(_arg56, _arg57, _arg58);
                    reply.writeNoException();
                    reply.writeBoolean(_result20);
                    return true;
                case 21:
                    int _arg59 = data.readInt();
                    int _arg60 = data.readInt();
                    int _arg61 = data.readInt();
                    ResultReceiver _arg62 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result21 = setSensorState(_arg59, _arg60, _arg61, _arg62);
                    reply.writeNoException();
                    reply.writeBoolean(_result21);
                    return true;
                case 22:
                    ResultReceiver _arg63 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result22 = getDdsAssistantInfo(_arg63);
                    reply.writeNoException();
                    reply.writeBoolean(_result22);
                    return true;
                case 23:
                    ResultReceiver _arg64 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result23 = getModemScene(_arg64);
                    reply.writeNoException();
                    reply.writeBoolean(_result23);
                    return true;
                case 24:
                    ResultReceiver _arg65 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result24 = getModemReason(_arg65);
                    reply.writeNoException();
                    reply.writeBoolean(_result24);
                    return true;
                case 25:
                    int[] _arg66 = data.createIntArray();
                    ResultReceiver _arg67 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result25 = setModemModelCfg(_arg66, _arg67);
                    reply.writeNoException();
                    reply.writeBoolean(_result25);
                    return true;
                case 26:
                    boolean _arg68 = data.readBoolean();
                    ResultReceiver _arg69 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result26 = setModemCallbackSwitch(_arg68, _arg69);
                    reply.writeNoException();
                    reply.writeBoolean(_result26);
                    return true;
                case 27:
                    int _arg70 = data.readInt();
                    boolean _arg71 = data.readBoolean();
                    ResultReceiver _arg72 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result27 = setModemEventIndSwitch(_arg70, _arg71, _arg72);
                    reply.writeNoException();
                    reply.writeBoolean(_result27);
                    return true;
                case 28:
                    IModemExceptionCallback _arg73 = IModemExceptionCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result28 = registerModemExceptionCallback(_arg73);
                    reply.writeNoException();
                    reply.writeBoolean(_result28);
                    return true;
                case 29:
                    IModemExceptionCallback _arg74 = IModemExceptionCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result29 = unregisterModemExceptionCallback(_arg74);
                    reply.writeNoException();
                    reply.writeBoolean(_result29);
                    return true;
                case 30:
                    IModemSceneCallback _arg75 = IModemSceneCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result30 = registerModemSceneCallback(_arg75);
                    reply.writeNoException();
                    reply.writeBoolean(_result30);
                    return true;
                case 31:
                    IModemSceneCallback _arg76 = IModemSceneCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    boolean _result31 = unregisterModemSceneCallback(_arg76);
                    reply.writeNoException();
                    reply.writeBoolean(_result31);
                    return true;
                case 32:
                    int _arg77 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result32 = getHalVersion(_arg77);
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case TRANSACTION_setFiveTupleBlackList /* 33 */:
                    String _arg78 = data.readString();
                    List<String> _arg79 = data.createStringArrayList();
                    int _arg80 = data.readInt();
                    ResultReceiver _arg81 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result33 = setFiveTupleBlackList(_arg78, _arg79, _arg80, _arg81);
                    reply.writeNoException();
                    reply.writeBoolean(_result33);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IXmTelephony {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IXmTelephony.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean sendCommMsg(int phoneId, int msgId, byte[] data, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(msgId);
                    _data.writeByteArray(data);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean registerCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(msgId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean unRegisterCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(msgId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(dsi);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeIntArray(dsi);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean getSarDsi(ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean getSarDsiGroup(ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public byte[] sendCommMsgSync(int phoneId, int msgId, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(msgId);
                    _data.writeByteArray(data);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setDeprioritizeSA(int phoneId, boolean enable, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(enable);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setNrMode(int phoneId, int nrStatus, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(nrStatus);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean enableEndc(int phoneId, boolean enable, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(enable);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean registerHstStateCallback(int phoneId, IHstStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean unRegisterHstStateCallback(int phoneId, IHstStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean registerQosStateCallback(int slotId, IQosStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean unRegisterQosStateCallback(int slotId, IQosStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean restartRILD(int phoneId, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setDataStallMode(int phoneId, int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(mode);
                    _data.writeInt(operation);
                    _data.writeInt(bearerType);
                    _data.writeInt(scene);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setMobilityOptParams(int phoneId, boolean enable, int rat, int featureId, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(enable);
                    _data.writeInt(rat);
                    _data.writeInt(featureId);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setPreferBandParams(int phoneId, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(rat);
                    _data.writeIntArray(bands);
                    _data.writeIntArray(arfcns);
                    _data.writeIntArray(rsrpOffsets);
                    _data.writeInt(preferType);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean promoteBSR(int phoneId, int level, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(level);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setSensorState(int phoneId, int sensorType, int sensorState, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(sensorType);
                    _data.writeInt(sensorState);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean getModemScene(ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean getModemReason(ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeIntArray(modelCfg);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeBoolean(isRegister);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean registerModemExceptionCallback(IModemExceptionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean unregisterModemExceptionCallback(IModemExceptionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean registerModemSceneCallback(IModemSceneCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean unregisterModemSceneCallback(IModemSceneCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public int getHalVersion(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaomi.internal.telephony.IXmTelephony
            public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IXmTelephony.DESCRIPTOR);
                    _data.writeString(rmnetData);
                    _data.writeStringList(ipList);
                    _data.writeInt(mode);
                    _data.writeTypedObject(resultReceiver, 0);
                    this.mRemote.transact(Stub.TRANSACTION_setFiveTupleBlackList, _data, _reply, 0);
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

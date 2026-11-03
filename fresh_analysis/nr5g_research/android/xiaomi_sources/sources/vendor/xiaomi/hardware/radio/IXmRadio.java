package vendor.xiaomi.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IXmRadio extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$radio$IXmRadio".replace('$', '.');
    public static final String HASH = "0a6fcf9e7119f7f2b3db206a650c509fcd647679";
    public static final int VERSION = 2;

    void enableEndc(int i, boolean z) throws RemoteException;

    void getDdsAssistantInfo(int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getModemReason(int i) throws RemoteException;

    void getModemScene(int i) throws RemoteException;

    void getSarDsi(int i) throws RemoteException;

    void getSarDsiGroup(int i) throws RemoteException;

    void promoteBSR(int i, int i2) throws RemoteException;

    void sendCommMsg(int i, int i2, byte[] bArr) throws RemoteException;

    void setDataStallMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setDeprioritizeSA(int i, boolean z) throws RemoteException;

    void setFiveTupleBlackList(int i, String str, List<String> list, int i2) throws RemoteException;

    void setMobilityOptParams(int i, boolean z, int i2, int i3) throws RemoteException;

    void setModemCallbackSwitch(int i, boolean z) throws RemoteException;

    void setModemEventIndSwitch(int i, int i2, boolean z) throws RemoteException;

    void setModemModelCfg(int i, int[] iArr) throws RemoteException;

    void setNrMode(int i, int i2) throws RemoteException;

    void setPreferBandParams(int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int i3) throws RemoteException;

    void setResponseFunctions(IXmRadioResponse iXmRadioResponse, IXmRadioIndication iXmRadioIndication) throws RemoteException;

    void setSarDsi(int i, int i2) throws RemoteException;

    void setSarDsiGroup(int i, int[] iArr) throws RemoteException;

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
    public static class Default implements IXmRadio {
        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setResponseFunctions(IXmRadioResponse XmResponse, IXmRadioIndication XmIndication) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void sendCommMsg(int serial, int msgId, byte[] data) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setSarDsi(int serial, int dsi) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setSarDsiGroup(int serial, int[] dsi) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void getSarDsi(int serial) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void getSarDsiGroup(int serial) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setDeprioritizeSA(int serial, boolean enable) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setNrMode(int serial, int nrStatus) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void enableEndc(int serial, boolean enable) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setDataStallMode(int serial, int mode, int operation, int bearerType, int scene) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setMobilityOptParams(int serial, boolean enable, int rat, int featureId) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setPreferBandParams(int serial, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void promoteBSR(int serial, int level) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void getDdsAssistantInfo(int serial) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void getModemScene(int serial) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void getModemReason(int serial) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setModemModelCfg(int serial, int[] cfg) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setModemCallbackSwitch(int serial, boolean enable) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setModemEventIndSwitch(int serial, int event, boolean isRegister) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public void setFiveTupleBlackList(int serial, String rmnetData, List<String> ipList, int mode) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadio
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
    public static abstract class Stub extends Binder implements IXmRadio {
        static final int TRANSACTION_enableEndc = 9;
        static final int TRANSACTION_getDdsAssistantInfo = 14;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getModemReason = 16;
        static final int TRANSACTION_getModemScene = 15;
        static final int TRANSACTION_getSarDsi = 5;
        static final int TRANSACTION_getSarDsiGroup = 6;
        static final int TRANSACTION_promoteBSR = 13;
        static final int TRANSACTION_sendCommMsg = 2;
        static final int TRANSACTION_setDataStallMode = 10;
        static final int TRANSACTION_setDeprioritizeSA = 7;
        static final int TRANSACTION_setFiveTupleBlackList = 20;
        static final int TRANSACTION_setMobilityOptParams = 11;
        static final int TRANSACTION_setModemCallbackSwitch = 18;
        static final int TRANSACTION_setModemEventIndSwitch = 19;
        static final int TRANSACTION_setModemModelCfg = 17;
        static final int TRANSACTION_setNrMode = 8;
        static final int TRANSACTION_setPreferBandParams = 12;
        static final int TRANSACTION_setResponseFunctions = 1;
        static final int TRANSACTION_setSarDsi = 3;
        static final int TRANSACTION_setSarDsiGroup = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IXmRadio asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IXmRadio)) {
                return (IXmRadio) iin;
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
                    IXmRadioResponse _arg0 = IXmRadioResponse.Stub.asInterface(data.readStrongBinder());
                    IXmRadioIndication _arg1 = IXmRadioIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctions(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    byte[] _arg4 = data.createByteArray();
                    data.enforceNoDataAvail();
                    sendCommMsg(_arg2, _arg3, _arg4);
                    return true;
                case 3:
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    setSarDsi(_arg5, _arg6);
                    return true;
                case 4:
                    int _arg7 = data.readInt();
                    int[] _arg8 = data.createIntArray();
                    data.enforceNoDataAvail();
                    setSarDsiGroup(_arg7, _arg8);
                    return true;
                case 5:
                    int _arg9 = data.readInt();
                    data.enforceNoDataAvail();
                    getSarDsi(_arg9);
                    return true;
                case 6:
                    int _arg10 = data.readInt();
                    data.enforceNoDataAvail();
                    getSarDsiGroup(_arg10);
                    return true;
                case 7:
                    int _arg11 = data.readInt();
                    boolean _arg12 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setDeprioritizeSA(_arg11, _arg12);
                    return true;
                case 8:
                    int _arg13 = data.readInt();
                    int _arg14 = data.readInt();
                    data.enforceNoDataAvail();
                    setNrMode(_arg13, _arg14);
                    return true;
                case 9:
                    int _arg15 = data.readInt();
                    boolean _arg16 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableEndc(_arg15, _arg16);
                    return true;
                case 10:
                    int _arg17 = data.readInt();
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    int _arg21 = data.readInt();
                    data.enforceNoDataAvail();
                    setDataStallMode(_arg17, _arg18, _arg19, _arg20, _arg21);
                    return true;
                case 11:
                    int _arg22 = data.readInt();
                    boolean _arg23 = data.readBoolean();
                    int _arg24 = data.readInt();
                    int _arg25 = data.readInt();
                    data.enforceNoDataAvail();
                    setMobilityOptParams(_arg22, _arg23, _arg24, _arg25);
                    return true;
                case 12:
                    int _arg26 = data.readInt();
                    int _arg27 = data.readInt();
                    int[] _arg28 = data.createIntArray();
                    int[] _arg29 = data.createIntArray();
                    int[] _arg30 = data.createIntArray();
                    int _arg31 = data.readInt();
                    data.enforceNoDataAvail();
                    setPreferBandParams(_arg26, _arg27, _arg28, _arg29, _arg30, _arg31);
                    return true;
                case 13:
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    promoteBSR(_arg32, _arg33);
                    return true;
                case 14:
                    int _arg34 = data.readInt();
                    data.enforceNoDataAvail();
                    getDdsAssistantInfo(_arg34);
                    return true;
                case 15:
                    int _arg35 = data.readInt();
                    data.enforceNoDataAvail();
                    getModemScene(_arg35);
                    return true;
                case 16:
                    int _arg36 = data.readInt();
                    data.enforceNoDataAvail();
                    getModemReason(_arg36);
                    return true;
                case 17:
                    int _arg37 = data.readInt();
                    int[] _arg38 = data.createIntArray();
                    data.enforceNoDataAvail();
                    setModemModelCfg(_arg37, _arg38);
                    return true;
                case 18:
                    int _arg39 = data.readInt();
                    boolean _arg40 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setModemCallbackSwitch(_arg39, _arg40);
                    return true;
                case 19:
                    int _arg41 = data.readInt();
                    int _arg42 = data.readInt();
                    boolean _arg43 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setModemEventIndSwitch(_arg41, _arg42, _arg43);
                    return true;
                case 20:
                    int _arg44 = data.readInt();
                    String _arg45 = data.readString();
                    List<String> _arg46 = data.createStringArrayList();
                    int _arg47 = data.readInt();
                    data.enforceNoDataAvail();
                    setFiveTupleBlackList(_arg44, _arg45, _arg46, _arg47);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
        private static final class Proxy implements IXmRadio {
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setResponseFunctions(IXmRadioResponse XmResponse, IXmRadioIndication XmIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(XmResponse);
                    _data.writeStrongInterface(XmIndication);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void sendCommMsg(int serial, int msgId, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(msgId);
                    _data.writeByteArray(data);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setSarDsi(int serial, int dsi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(dsi);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setSarDsiGroup(int serial, int[] dsi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(dsi);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void getSarDsi(int serial) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void getSarDsiGroup(int serial) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setDeprioritizeSA(int serial, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setNrMode(int serial, int nrStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(nrStatus);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void enableEndc(int serial, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setDataStallMode(int serial, int mode, int operation, int bearerType, int scene) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(operation);
                    _data.writeInt(bearerType);
                    _data.writeInt(scene);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setMobilityOptParams(int serial, boolean enable, int rat, int featureId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(rat);
                    _data.writeInt(featureId);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setPreferBandParams(int serial, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rat);
                    _data.writeIntArray(bands);
                    _data.writeIntArray(arfcns);
                    _data.writeIntArray(rsrpOffsets);
                    _data.writeInt(preferType);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void promoteBSR(int serial, int level) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(level);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void getDdsAssistantInfo(int serial) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void getModemScene(int serial) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void getModemReason(int serial) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setModemModelCfg(int serial, int[] cfg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(cfg);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setModemCallbackSwitch(int serial, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setModemEventIndSwitch(int serial, int event, boolean isRegister) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(event);
                    _data.writeBoolean(isRegister);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
            public void setFiveTupleBlackList(int serial, String rmnetData, List<String> ipList, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(rmnetData);
                    _data.writeStringList(ipList);
                    _data.writeInt(mode);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadio
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

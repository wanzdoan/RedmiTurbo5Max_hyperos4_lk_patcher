package vendor.mediatek.hardware.mtkradioex.modem;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExModemIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$modem$IMtkRadioExModemIndication".replace('$', '.');
    public static final String HASH = "343450221c89bcee3b0e260bb10c69390c3cb226";
    public static final int VERSION = 5;

    void dsbpStateChanged(int i, int i2) throws RemoteException;

    void eMBMSAtInfoIndication(int i, String str) throws RemoteException;

    void eMBMSSessionStatusIndication(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void oemHookRaw(int i, byte[] bArr) throws RemoteException;

    void onCellularQualityChangedInd(int i, int[] iArr) throws RemoteException;

    void onTxPowerIndication(int i, int[] iArr) throws RemoteException;

    void onTxPowerStatusIndication(int i, int[] iArr) throws RemoteException;

    void worldModeChangedIndication(int i, int[] iArr) throws RemoteException;

    public static class Default implements IMtkRadioExModemIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void dsbpStateChanged(int indicationType, int dsbpState) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void eMBMSAtInfoIndication(int type, String info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void eMBMSSessionStatusIndication(int type, int status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void oemHookRaw(int type, byte[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onTxPowerStatusIndication(int type, int[] indPower) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void worldModeChangedIndication(int type, int[] modes) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onCellularQualityChangedInd(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExModemIndication {
        static final int TRANSACTION_dsbpStateChanged = 1;
        static final int TRANSACTION_eMBMSAtInfoIndication = 2;
        static final int TRANSACTION_eMBMSSessionStatusIndication = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_oemHookRaw = 4;
        static final int TRANSACTION_onCellularQualityChangedInd = 8;
        static final int TRANSACTION_onTxPowerIndication = 5;
        static final int TRANSACTION_onTxPowerStatusIndication = 6;
        static final int TRANSACTION_worldModeChangedIndication = 7;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExModemIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExModemIndication)) {
                return (IMtkRadioExModemIndication) iin;
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
                    dsbpStateChanged(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    String _arg3 = data.readString();
                    data.enforceNoDataAvail();
                    eMBMSAtInfoIndication(_arg2, _arg3);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    eMBMSSessionStatusIndication(_arg4, _arg5);
                    return true;
                case 4:
                    int _arg6 = data.readInt();
                    byte[] _arg7 = data.createByteArray();
                    data.enforceNoDataAvail();
                    oemHookRaw(_arg6, _arg7);
                    return true;
                case 5:
                    int _arg8 = data.readInt();
                    int[] _arg9 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onTxPowerIndication(_arg8, _arg9);
                    return true;
                case 6:
                    int _arg10 = data.readInt();
                    int[] _arg11 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onTxPowerStatusIndication(_arg10, _arg11);
                    return true;
                case 7:
                    int _arg12 = data.readInt();
                    int[] _arg13 = data.createIntArray();
                    data.enforceNoDataAvail();
                    worldModeChangedIndication(_arg12, _arg13);
                    return true;
                case 8:
                    int _arg14 = data.readInt();
                    int[] _arg15 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onCellularQualityChangedInd(_arg14, _arg15);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExModemIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void dsbpStateChanged(int indicationType, int dsbpState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(indicationType);
                    _data.writeInt(dsbpState);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void eMBMSAtInfoIndication(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void eMBMSSessionStatusIndication(int type, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(status);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void oemHookRaw(int type, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indPower);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onTxPowerStatusIndication(int type, int[] indPower) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indPower);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void worldModeChangedIndication(int type, int[] modes) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(modes);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onCellularQualityChangedInd(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
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

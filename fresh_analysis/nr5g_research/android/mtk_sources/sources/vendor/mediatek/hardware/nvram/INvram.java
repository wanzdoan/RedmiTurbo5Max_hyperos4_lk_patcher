package vendor.mediatek.hardware.nvram;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface INvram extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$nvram$INvram".replace('$', '.');
    public static final String HASH = "18369ad8cca8ba6194b627a0947518c877ae7078";
    public static final int VERSION = 1;

    boolean AddBackupFileNum(int i) throws RemoteException;

    boolean BackupAll_NvRam() throws RemoteException;

    boolean BackupData_Special(byte[] bArr, int i, int i2) throws RemoteException;

    boolean BackupToBinRegion_All() throws RemoteException;

    boolean BackupToBinRegion_All_Ex(int i) throws RemoteException;

    boolean BackupToBinRegion_All_Exx(byte[] bArr) throws RemoteException;

    ParcelableReadFileVerInfoRet ReadFileVerInfo(int i) throws RemoteException;

    boolean ResetFileToDefault(int i) throws RemoteException;

    boolean RestoreAll_NvRam() throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    String readFileByName(String str, int i) throws RemoteException;

    ParcelableReadFileBylidRet readFileBylid(int i, char c) throws RemoteException;

    byte writeFileByNamevec(String str, int i, byte[] bArr) throws RemoteException;

    ParcelableWriteFileBylidRet writeFileBylid(int i, char c, byte[] bArr) throws RemoteException;

    public static class Default implements INvram {
        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean AddBackupFileNum(int lid) throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean BackupAll_NvRam() throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean BackupData_Special(byte[] data, int count, int mode) throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean BackupToBinRegion_All() throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean BackupToBinRegion_All_Ex(int value) throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean BackupToBinRegion_All_Exx(byte[] data) throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public ParcelableReadFileVerInfoRet ReadFileVerInfo(int lid) throws RemoteException {
            return null;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean ResetFileToDefault(int lid) throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public boolean RestoreAll_NvRam() throws RemoteException {
            return false;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public String readFileByName(String filename, int size) throws RemoteException {
            return null;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public ParcelableReadFileBylidRet readFileBylid(int lid, char para) throws RemoteException {
            return null;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public byte writeFileByNamevec(String filename, int size, byte[] data) throws RemoteException {
            return (byte) 0;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public ParcelableWriteFileBylidRet writeFileBylid(int lid, char para, byte[] data) throws RemoteException {
            return null;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.nvram.INvram
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INvram {
        static final int TRANSACTION_AddBackupFileNum = 1;
        static final int TRANSACTION_BackupAll_NvRam = 2;
        static final int TRANSACTION_BackupData_Special = 3;
        static final int TRANSACTION_BackupToBinRegion_All = 4;
        static final int TRANSACTION_BackupToBinRegion_All_Ex = 5;
        static final int TRANSACTION_BackupToBinRegion_All_Exx = 6;
        static final int TRANSACTION_ReadFileVerInfo = 7;
        static final int TRANSACTION_ResetFileToDefault = 8;
        static final int TRANSACTION_RestoreAll_NvRam = 9;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_readFileByName = 10;
        static final int TRANSACTION_readFileBylid = 11;
        static final int TRANSACTION_writeFileByNamevec = 12;
        static final int TRANSACTION_writeFileBylid = 13;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static INvram asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INvram)) {
                return (INvram) iin;
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
                    data.enforceNoDataAvail();
                    boolean _result = AddBackupFileNum(_arg0);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    boolean _result2 = BackupAll_NvRam();
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    byte[] _arg1 = data.createByteArray();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result3 = BackupData_Special(_arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                case 4:
                    boolean _result4 = BackupToBinRegion_All();
                    reply.writeNoException();
                    reply.writeBoolean(_result4);
                    return true;
                case 5:
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result5 = BackupToBinRegion_All_Ex(_arg4);
                    reply.writeNoException();
                    reply.writeBoolean(_result5);
                    return true;
                case 6:
                    byte[] _arg5 = data.createByteArray();
                    data.enforceNoDataAvail();
                    boolean _result6 = BackupToBinRegion_All_Exx(_arg5);
                    reply.writeNoException();
                    reply.writeBoolean(_result6);
                    return true;
                case 7:
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    ParcelableReadFileVerInfoRet _result7 = ReadFileVerInfo(_arg6);
                    reply.writeNoException();
                    reply.writeTypedObject(_result7, 1);
                    return true;
                case 8:
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result8 = ResetFileToDefault(_arg7);
                    reply.writeNoException();
                    reply.writeBoolean(_result8);
                    return true;
                case 9:
                    boolean _result9 = RestoreAll_NvRam();
                    reply.writeNoException();
                    reply.writeBoolean(_result9);
                    return true;
                case 10:
                    String _arg8 = data.readString();
                    int _arg9 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result10 = readFileByName(_arg8, _arg9);
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 11:
                    int _arg10 = data.readInt();
                    char _arg11 = (char) data.readInt();
                    data.enforceNoDataAvail();
                    ParcelableReadFileBylidRet _result11 = readFileBylid(_arg10, _arg11);
                    reply.writeNoException();
                    reply.writeTypedObject(_result11, 1);
                    return true;
                case 12:
                    String _arg12 = data.readString();
                    int _arg13 = data.readInt();
                    byte[] _arg14 = data.createByteArray();
                    data.enforceNoDataAvail();
                    byte _result12 = writeFileByNamevec(_arg12, _arg13, _arg14);
                    reply.writeNoException();
                    reply.writeByte(_result12);
                    return true;
                case 13:
                    int _arg15 = data.readInt();
                    char _arg16 = (char) data.readInt();
                    byte[] _arg17 = data.createByteArray();
                    data.enforceNoDataAvail();
                    ParcelableWriteFileBylidRet _result13 = writeFileBylid(_arg15, _arg16, _arg17);
                    reply.writeNoException();
                    reply.writeTypedObject(_result13, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements INvram {
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

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean AddBackupFileNum(int lid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(lid);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean BackupAll_NvRam() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean BackupData_Special(byte[] data, int count, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(data);
                    _data.writeInt(count);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean BackupToBinRegion_All() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean BackupToBinRegion_All_Ex(int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean BackupToBinRegion_All_Exx(byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public ParcelableReadFileVerInfoRet ReadFileVerInfo(int lid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(lid);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    ParcelableReadFileVerInfoRet _result = (ParcelableReadFileVerInfoRet) _reply.readTypedObject(ParcelableReadFileVerInfoRet.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean ResetFileToDefault(int lid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(lid);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public boolean RestoreAll_NvRam() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public String readFileByName(String filename, int size) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(size);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public ParcelableReadFileBylidRet readFileBylid(int lid, char para) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(lid);
                    _data.writeInt(para);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    ParcelableReadFileBylidRet _result = (ParcelableReadFileBylidRet) _reply.readTypedObject(ParcelableReadFileBylidRet.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public byte writeFileByNamevec(String filename, int size, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(size);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    byte _result = _reply.readByte();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
            public ParcelableWriteFileBylidRet writeFileBylid(int lid, char para, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(lid);
                    _data.writeInt(para);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    ParcelableWriteFileBylidRet _result = (ParcelableWriteFileBylidRet) _reply.readTypedObject(ParcelableWriteFileBylidRet.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.nvram.INvram
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

            @Override // vendor.mediatek.hardware.nvram.INvram
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

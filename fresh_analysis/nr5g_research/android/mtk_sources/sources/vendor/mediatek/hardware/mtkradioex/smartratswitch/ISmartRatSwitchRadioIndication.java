package vendor.mediatek.hardware.mtkradioex.smartratswitch;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface ISmartRatSwitchRadioIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$smartratswitch$ISmartRatSwitchRadioIndication".replace('$', '.');
    public static final String HASH = "b9bfcf6a1d07eb6465af9664f5d5c5f3404c4e3c";
    public static final int VERSION = 1;

    void codecActiveInd(int i, int i2, boolean z) throws RemoteException;

    void codecEmptyInd(int i, int i2, boolean z) throws RemoteException;

    void codecFpsInd(int i, int i2, int i3) throws RemoteException;

    void codecResolutionInd(int i, int i2, int i3, int i4) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void smartRatSwitchInd(int i, int i2) throws RemoteException;

    public static class Default implements ISmartRatSwitchRadioIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public void codecActiveInd(int type, int instanceId, boolean use) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public void codecEmptyInd(int type, int instanceId, boolean empty) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public void codecFpsInd(int type, int instanceId, int fps) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public void codecResolutionInd(int type, int instanceId, int w, int h) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public void smartRatSwitchInd(int type, int info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISmartRatSwitchRadioIndication {
        static final int TRANSACTION_codecActiveInd = 1;
        static final int TRANSACTION_codecEmptyInd = 2;
        static final int TRANSACTION_codecFpsInd = 3;
        static final int TRANSACTION_codecResolutionInd = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_smartRatSwitchInd = 5;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static ISmartRatSwitchRadioIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISmartRatSwitchRadioIndication)) {
                return (ISmartRatSwitchRadioIndication) iin;
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
                    boolean _arg2 = data.readBoolean();
                    data.enforceNoDataAvail();
                    codecActiveInd(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    boolean _arg5 = data.readBoolean();
                    data.enforceNoDataAvail();
                    codecEmptyInd(_arg3, _arg4, _arg5);
                    return true;
                case 3:
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    codecFpsInd(_arg6, _arg7, _arg8);
                    return true;
                case 4:
                    int _arg9 = data.readInt();
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    int _arg12 = data.readInt();
                    data.enforceNoDataAvail();
                    codecResolutionInd(_arg9, _arg10, _arg11, _arg12);
                    return true;
                case 5:
                    int _arg13 = data.readInt();
                    int _arg14 = data.readInt();
                    data.enforceNoDataAvail();
                    smartRatSwitchInd(_arg13, _arg14);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements ISmartRatSwitchRadioIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
            public void codecActiveInd(int type, int instanceId, boolean use) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(instanceId);
                    _data.writeBoolean(use);
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
            public void codecEmptyInd(int type, int instanceId, boolean empty) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(instanceId);
                    _data.writeBoolean(empty);
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
            public void codecFpsInd(int type, int instanceId, int fps) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(instanceId);
                    _data.writeInt(fps);
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
            public void codecResolutionInd(int type, int instanceId, int w, int h) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(instanceId);
                    _data.writeInt(w);
                    _data.writeInt(h);
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
            public void smartRatSwitchInd(int type, int info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication
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

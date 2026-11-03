package com.mediatek.smartratswitch;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface ISmartRatSwitch extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.smartratswitch.ISmartRatSwitch";

    void configWokingMode(int i) throws RemoteException;

    void forceDisable5G(boolean z) throws RemoteException;

    void forceEnable5G(boolean z) throws RemoteException;

    int getLastOperation() throws RemoteException;

    void resetKeepRatDuration() throws RemoteException;

    void resetNextTput(String str) throws RemoteException;

    void setCustAppProperty(String str, int i) throws RemoteException;

    void setKeepRatDuration(int i) throws RemoteException;

    void setNextTput(String str, int i, int i2) throws RemoteException;

    public static class Default implements ISmartRatSwitch {
        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void setCustAppProperty(String package_name, int param) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public int getLastOperation() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void forceEnable5G(boolean on) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void forceDisable5G(boolean on) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void configWokingMode(int mode) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void setKeepRatDuration(int KeepRatDuration) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void resetKeepRatDuration() throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void setNextTput(String package_name, int Tput_tx, int Tput_rx) throws RemoteException {
        }

        @Override // com.mediatek.smartratswitch.ISmartRatSwitch
        public void resetNextTput(String package_name) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISmartRatSwitch {
        static final int TRANSACTION_configWokingMode = 5;
        static final int TRANSACTION_forceDisable5G = 4;
        static final int TRANSACTION_forceEnable5G = 3;
        static final int TRANSACTION_getLastOperation = 2;
        static final int TRANSACTION_resetKeepRatDuration = 7;
        static final int TRANSACTION_resetNextTput = 9;
        static final int TRANSACTION_setCustAppProperty = 1;
        static final int TRANSACTION_setKeepRatDuration = 6;
        static final int TRANSACTION_setNextTput = 8;

        public Stub() {
            attachInterface(this, ISmartRatSwitch.DESCRIPTOR);
        }

        public static ISmartRatSwitch asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISmartRatSwitch.DESCRIPTOR);
            if (iin != null && (iin instanceof ISmartRatSwitch)) {
                return (ISmartRatSwitch) iin;
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
                data.enforceInterface(ISmartRatSwitch.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    setCustAppProperty(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    int _result = getLastOperation();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    boolean _arg2 = data.readBoolean();
                    data.enforceNoDataAvail();
                    forceEnable5G(_arg2);
                    reply.writeNoException();
                    return true;
                case 4:
                    boolean _arg3 = data.readBoolean();
                    data.enforceNoDataAvail();
                    forceDisable5G(_arg3);
                    reply.writeNoException();
                    return true;
                case 5:
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    configWokingMode(_arg4);
                    reply.writeNoException();
                    return true;
                case 6:
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    setKeepRatDuration(_arg5);
                    reply.writeNoException();
                    return true;
                case 7:
                    resetKeepRatDuration();
                    reply.writeNoException();
                    return true;
                case 8:
                    String _arg6 = data.readString();
                    int _arg7 = data.readInt();
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    setNextTput(_arg6, _arg7, _arg8);
                    reply.writeNoException();
                    return true;
                case 9:
                    String _arg9 = data.readString();
                    data.enforceNoDataAvail();
                    resetNextTput(_arg9);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements ISmartRatSwitch {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return ISmartRatSwitch.DESCRIPTOR;
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void setCustAppProperty(String package_name, int param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeString(package_name);
                    _data.writeInt(param);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public int getLastOperation() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void forceEnable5G(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeBoolean(on);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void forceDisable5G(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeBoolean(on);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void configWokingMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void setKeepRatDuration(int KeepRatDuration) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeInt(KeepRatDuration);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void resetKeepRatDuration() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void setNextTput(String package_name, int Tput_tx, int Tput_rx) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeString(package_name);
                    _data.writeInt(Tput_tx);
                    _data.writeInt(Tput_rx);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.smartratswitch.ISmartRatSwitch
            public void resetNextTput(String package_name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmartRatSwitch.DESCRIPTOR);
                    _data.writeString(package_name);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

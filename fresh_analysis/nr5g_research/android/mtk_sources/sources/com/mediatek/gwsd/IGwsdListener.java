package com.mediatek.gwsd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IGwsdListener extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.gwsd.IGwsdListener";

    void onAddListenered(int i, String str) throws RemoteException;

    void onAutoRejectModeChanged(int i, String str) throws RemoteException;

    void onCallValidTimerChanged(int i, String str) throws RemoteException;

    void onIgnoreSameNumberIntervalChanged(int i, String str) throws RemoteException;

    void onSyncGwsdInfoFinished(int i, String str) throws RemoteException;

    void onSystemStateChanged(int i) throws RemoteException;

    void onUserSelectionModeChanged(int i, String str) throws RemoteException;

    public static class Default implements IGwsdListener {
        @Override // com.mediatek.gwsd.IGwsdListener
        public void onAddListenered(int status, String reason) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onUserSelectionModeChanged(int status, String reason) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onAutoRejectModeChanged(int status, String reason) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onSyncGwsdInfoFinished(int status, String reason) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onSystemStateChanged(int state) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onCallValidTimerChanged(int status, String reason) throws RemoteException {
        }

        @Override // com.mediatek.gwsd.IGwsdListener
        public void onIgnoreSameNumberIntervalChanged(int status, String reason) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGwsdListener {
        static final int TRANSACTION_onAddListenered = 1;
        static final int TRANSACTION_onAutoRejectModeChanged = 3;
        static final int TRANSACTION_onCallValidTimerChanged = 6;
        static final int TRANSACTION_onIgnoreSameNumberIntervalChanged = 7;
        static final int TRANSACTION_onSyncGwsdInfoFinished = 4;
        static final int TRANSACTION_onSystemStateChanged = 5;
        static final int TRANSACTION_onUserSelectionModeChanged = 2;

        public Stub() {
            attachInterface(this, IGwsdListener.DESCRIPTOR);
        }

        public static IGwsdListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IGwsdListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IGwsdListener)) {
                return (IGwsdListener) iin;
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
                data.enforceInterface(IGwsdListener.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    data.enforceNoDataAvail();
                    onAddListenered(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    String _arg3 = data.readString();
                    data.enforceNoDataAvail();
                    onUserSelectionModeChanged(_arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    String _arg5 = data.readString();
                    data.enforceNoDataAvail();
                    onAutoRejectModeChanged(_arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _arg6 = data.readInt();
                    String _arg7 = data.readString();
                    data.enforceNoDataAvail();
                    onSyncGwsdInfoFinished(_arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 5:
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    onSystemStateChanged(_arg8);
                    reply.writeNoException();
                    return true;
                case 6:
                    int _arg9 = data.readInt();
                    String _arg10 = data.readString();
                    data.enforceNoDataAvail();
                    onCallValidTimerChanged(_arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 7:
                    int _arg11 = data.readInt();
                    String _arg12 = data.readString();
                    data.enforceNoDataAvail();
                    onIgnoreSameNumberIntervalChanged(_arg11, _arg12);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IGwsdListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IGwsdListener.DESCRIPTOR;
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onAddListenered(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onUserSelectionModeChanged(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onAutoRejectModeChanged(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onSyncGwsdInfoFinished(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onSystemStateChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onCallValidTimerChanged(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.gwsd.IGwsdListener
            public void onIgnoreSameNumberIntervalChanged(int status, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdListener.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeString(reason);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

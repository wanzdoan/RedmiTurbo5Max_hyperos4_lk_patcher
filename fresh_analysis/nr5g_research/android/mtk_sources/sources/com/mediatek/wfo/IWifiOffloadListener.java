package com.mediatek.wfo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IWifiOffloadListener extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.wfo.IWifiOffloadListener";

    void onHandover(int i, int i2, int i3) throws RemoteException;

    void onRoveOut(int i, boolean z, int i2) throws RemoteException;

    void onWfcStateChanged(int i, int i2) throws RemoteException;

    void onWifiPdnOOSStateChanged(int i, int i2) throws RemoteException;

    public static class Default implements IWifiOffloadListener {
        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onHandover(int simIdx, int stage, int ratType) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onRoveOut(int simIdx, boolean roveOut, int rssi) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onWifiPdnOOSStateChanged(int simIdx, int oosState) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onWfcStateChanged(int simIdx, int state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiOffloadListener {
        static final int TRANSACTION_onHandover = 1;
        static final int TRANSACTION_onRoveOut = 2;
        static final int TRANSACTION_onWfcStateChanged = 4;
        static final int TRANSACTION_onWifiPdnOOSStateChanged = 3;

        public Stub() {
            attachInterface(this, IWifiOffloadListener.DESCRIPTOR);
        }

        public static IWifiOffloadListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWifiOffloadListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiOffloadListener)) {
                return (IWifiOffloadListener) iin;
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
                data.enforceInterface(IWifiOffloadListener.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    onHandover(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    int _arg3 = data.readInt();
                    boolean _arg4 = data.readBoolean();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    onRoveOut(_arg3, _arg4, _arg5);
                    return true;
                case 3:
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    onWifiPdnOOSStateChanged(_arg6, _arg7);
                    return true;
                case 4:
                    int _arg8 = data.readInt();
                    int _arg9 = data.readInt();
                    data.enforceNoDataAvail();
                    onWfcStateChanged(_arg8, _arg9);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IWifiOffloadListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IWifiOffloadListener.DESCRIPTOR;
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onHandover(int simIdx, int stage, int ratType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(stage);
                    _data.writeInt(ratType);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onRoveOut(int simIdx, boolean roveOut, int rssi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeBoolean(roveOut);
                    _data.writeInt(rssi);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onWifiPdnOOSStateChanged(int simIdx, int oosState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(oosState);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onWfcStateChanged(int simIdx, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(state);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}

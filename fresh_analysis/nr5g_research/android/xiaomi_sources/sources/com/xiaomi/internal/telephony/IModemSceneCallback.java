package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IModemSceneCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.IModemSceneCallback";

    void onModemSceneChanged(int i, int[] iArr) throws RemoteException;

    public static class Default implements IModemSceneCallback {
        @Override // com.xiaomi.internal.telephony.IModemSceneCallback
        public void onModemSceneChanged(int slotId, int[] sceneInfo) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IModemSceneCallback {
        static final int TRANSACTION_onModemSceneChanged = 1;

        public Stub() {
            attachInterface(this, IModemSceneCallback.DESCRIPTOR);
        }

        public static IModemSceneCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IModemSceneCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IModemSceneCallback)) {
                return (IModemSceneCallback) iin;
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
                data.enforceInterface(IModemSceneCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onModemSceneChanged(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IModemSceneCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IModemSceneCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.IModemSceneCallback
            public void onModemSceneChanged(int slotId, int[] sceneInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IModemSceneCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeIntArray(sceneInfo);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

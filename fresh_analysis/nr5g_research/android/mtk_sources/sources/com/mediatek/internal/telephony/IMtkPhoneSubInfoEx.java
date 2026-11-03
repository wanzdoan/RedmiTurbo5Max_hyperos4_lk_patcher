package com.mediatek.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkPhoneSubInfoEx extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.IMtkPhoneSubInfoEx";

    String getIsimDomainForSubscriber(int i) throws RemoteException;

    String getIsimImpiForSubscriber(int i) throws RemoteException;

    String[] getIsimImpuForSubscriber(int i) throws RemoteException;

    String getIsimIstForSubscriber(int i) throws RemoteException;

    String[] getIsimPcscfForSubscriber(int i) throws RemoteException;

    String getLine1PhoneNumberForSubscriber(int i, String str, String str2) throws RemoteException;

    int getMncLength() throws RemoteException;

    int getMncLengthForSubscriber(int i) throws RemoteException;

    boolean getUsimService(int i, String str) throws RemoteException;

    boolean getUsimServiceForSubscriber(int i, int i2, String str) throws RemoteException;

    public static class Default implements IMtkPhoneSubInfoEx {
        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public boolean getUsimService(int service, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public boolean getUsimServiceForSubscriber(int subId, int service, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public int getMncLength() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public int getMncLengthForSubscriber(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String getIsimImpiForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String getIsimDomainForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String[] getIsimImpuForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String getIsimIstForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String[] getIsimPcscfForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
        public String getLine1PhoneNumberForSubscriber(int subId, String callingPackage, String callingFeatureId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkPhoneSubInfoEx {
        static final int TRANSACTION_getIsimDomainForSubscriber = 6;
        static final int TRANSACTION_getIsimImpiForSubscriber = 5;
        static final int TRANSACTION_getIsimImpuForSubscriber = 7;
        static final int TRANSACTION_getIsimIstForSubscriber = 8;
        static final int TRANSACTION_getIsimPcscfForSubscriber = 9;
        static final int TRANSACTION_getLine1PhoneNumberForSubscriber = 10;
        static final int TRANSACTION_getMncLength = 3;
        static final int TRANSACTION_getMncLengthForSubscriber = 4;
        static final int TRANSACTION_getUsimService = 1;
        static final int TRANSACTION_getUsimServiceForSubscriber = 2;

        public Stub() {
            attachInterface(this, IMtkPhoneSubInfoEx.DESCRIPTOR);
        }

        public static IMtkPhoneSubInfoEx asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkPhoneSubInfoEx.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkPhoneSubInfoEx)) {
                return (IMtkPhoneSubInfoEx) iin;
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
                data.enforceInterface(IMtkPhoneSubInfoEx.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result = getUsimService(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result2 = getUsimServiceForSubscriber(_arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    int _result3 = getMncLength();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result4 = getMncLengthForSubscriber(_arg5);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result5 = getIsimImpiForSubscriber(_arg6);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 6:
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result6 = getIsimDomainForSubscriber(_arg7);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 7:
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    String[] _result7 = getIsimImpuForSubscriber(_arg8);
                    reply.writeNoException();
                    reply.writeStringArray(_result7);
                    return true;
                case 8:
                    int _arg9 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result8 = getIsimIstForSubscriber(_arg9);
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 9:
                    int _arg10 = data.readInt();
                    data.enforceNoDataAvail();
                    String[] _result9 = getIsimPcscfForSubscriber(_arg10);
                    reply.writeNoException();
                    reply.writeStringArray(_result9);
                    return true;
                case 10:
                    int _arg11 = data.readInt();
                    String _arg12 = data.readString();
                    String _arg13 = data.readString();
                    data.enforceNoDataAvail();
                    String _result10 = getLine1PhoneNumberForSubscriber(_arg11, _arg12, _arg13);
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkPhoneSubInfoEx {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IMtkPhoneSubInfoEx.DESCRIPTOR;
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public boolean getUsimService(int service, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(service);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public boolean getUsimServiceForSubscriber(int subId, int service, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(service);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public int getMncLength() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public int getMncLengthForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String getIsimImpiForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String getIsimDomainForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
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

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String[] getIsimImpuForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String getIsimIstForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String[] getIsimPcscfForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkPhoneSubInfoEx
            public String getLine1PhoneNumberForSubscriber(int subId, String callingPackage, String callingFeatureId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkPhoneSubInfoEx.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeString(callingFeatureId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

package vendor.xiaomi.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.xiaomi.hardware.radio.data.QosInfo;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IXmRadioIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$radio$IXmRadioIndication".replace('$', '.');
    public static final String HASH = "0a6fcf9e7119f7f2b3db206a650c509fcd647679";
    public static final int VERSION = 2;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void hstInfoInd(int i, boolean z, int i2) throws RemoteException;

    void modemExceptionInd(int i, int[] iArr) throws RemoteException;

    void modemSceneInd(int i, int[] iArr) throws RemoteException;

    void qosInfoInd(int i, QosInfo qosInfo) throws RemoteException;

    void unsolCommMsgInd(int i, int i2, byte[] bArr) throws RemoteException;

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
    public static class Default implements IXmRadioIndication {
        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public void unsolCommMsgInd(int type, int msgId, byte[] data) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public void hstInfoInd(int type, boolean hstState, int hstRat) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public void qosInfoInd(int type, QosInfo qosInfo) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public void modemExceptionInd(int type, int[] modemException) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public void modemSceneInd(int type, int[] sceneInfo) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
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
    public static abstract class Stub extends Binder implements IXmRadioIndication {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_hstInfoInd = 2;
        static final int TRANSACTION_modemExceptionInd = 4;
        static final int TRANSACTION_modemSceneInd = 5;
        static final int TRANSACTION_qosInfoInd = 3;
        static final int TRANSACTION_unsolCommMsgInd = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IXmRadioIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IXmRadioIndication)) {
                return (IXmRadioIndication) iin;
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
                    byte[] _arg2 = data.createByteArray();
                    data.enforceNoDataAvail();
                    unsolCommMsgInd(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    int _arg3 = data.readInt();
                    boolean _arg4 = data.readBoolean();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    hstInfoInd(_arg3, _arg4, _arg5);
                    return true;
                case 3:
                    int _arg6 = data.readInt();
                    QosInfo _arg7 = (QosInfo) data.readTypedObject(QosInfo.CREATOR);
                    data.enforceNoDataAvail();
                    qosInfoInd(_arg6, _arg7);
                    return true;
                case 4:
                    int _arg8 = data.readInt();
                    int[] _arg9 = data.createIntArray();
                    data.enforceNoDataAvail();
                    modemExceptionInd(_arg8, _arg9);
                    return true;
                case 5:
                    int _arg10 = data.readInt();
                    int[] _arg11 = data.createIntArray();
                    data.enforceNoDataAvail();
                    modemSceneInd(_arg10, _arg11);
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
        private static final class Proxy implements IXmRadioIndication {
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
            public void unsolCommMsgInd(int type, int msgId, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(msgId);
                    _data.writeByteArray(data);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
            public void hstInfoInd(int type, boolean hstState, int hstRat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeBoolean(hstState);
                    _data.writeInt(hstRat);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
            public void qosInfoInd(int type, QosInfo qosInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(qosInfo, 0);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
            public void modemExceptionInd(int type, int[] modemException) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(modemException);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
            public void modemSceneInd(int type, int[] sceneInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(sceneInfo);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
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

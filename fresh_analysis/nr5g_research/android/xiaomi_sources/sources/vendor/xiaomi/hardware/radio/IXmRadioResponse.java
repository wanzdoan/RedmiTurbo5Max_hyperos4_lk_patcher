package vendor.xiaomi.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IXmRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$radio$IXmRadioResponse".replace('$', '.');
    public static final String HASH = "0a6fcf9e7119f7f2b3db206a650c509fcd647679";
    public static final int VERSION = 2;

    void enableEndcResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getDdsAssistantInfoResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getModemReasonResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getModemSceneResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getSarDsiGroupResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getSarDsiResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void promoteBSRResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendCommMsgResponse(RadioResponseInfo radioResponseInfo, byte[] bArr) throws RemoteException;

    void setDataStallModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setDeprioritizeSAResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setFiveTupleBlackListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setMobilityOptParamsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemCallbackSwitchResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemEventIndSwitchResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemModelCfgResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNrModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setPreferBandParamsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSarDsiGroupResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSarDsiResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
  classes.dex
 */
    public static class Default implements IXmRadioResponse {
        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void sendCommMsgResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setSarDsiResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setSarDsiGroupResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void getSarDsiResponse(RadioResponseInfo info, int dsi) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void getSarDsiGroupResponse(RadioResponseInfo info, int[] dsiGroup) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setDeprioritizeSAResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setNrModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void enableEndcResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setDataStallModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setMobilityOptParamsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setPreferBandParamsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void promoteBSRResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void getDdsAssistantInfoResponse(RadioResponseInfo info, int[] assistantInfo) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void getModemSceneResponse(RadioResponseInfo info, int[] scene) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void getModemReasonResponse(RadioResponseInfo info, int[] reason) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setModemModelCfgResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setModemCallbackSwitchResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setModemEventIndSwitchResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public void setFiveTupleBlackListResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
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
    public static abstract class Stub extends Binder implements IXmRadioResponse {
        static final int TRANSACTION_enableEndcResponse = 8;
        static final int TRANSACTION_getDdsAssistantInfoResponse = 13;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getModemReasonResponse = 15;
        static final int TRANSACTION_getModemSceneResponse = 14;
        static final int TRANSACTION_getSarDsiGroupResponse = 5;
        static final int TRANSACTION_getSarDsiResponse = 4;
        static final int TRANSACTION_promoteBSRResponse = 12;
        static final int TRANSACTION_sendCommMsgResponse = 1;
        static final int TRANSACTION_setDataStallModeResponse = 9;
        static final int TRANSACTION_setDeprioritizeSAResponse = 6;
        static final int TRANSACTION_setFiveTupleBlackListResponse = 19;
        static final int TRANSACTION_setMobilityOptParamsResponse = 10;
        static final int TRANSACTION_setModemCallbackSwitchResponse = 17;
        static final int TRANSACTION_setModemEventIndSwitchResponse = 18;
        static final int TRANSACTION_setModemModelCfgResponse = 16;
        static final int TRANSACTION_setNrModeResponse = 7;
        static final int TRANSACTION_setPreferBandParamsResponse = 11;
        static final int TRANSACTION_setSarDsiGroupResponse = 3;
        static final int TRANSACTION_setSarDsiResponse = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IXmRadioResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IXmRadioResponse)) {
                return (IXmRadioResponse) iin;
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
                    RadioResponseInfo _arg0 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    byte[] _arg1 = data.createByteArray();
                    data.enforceNoDataAvail();
                    sendCommMsgResponse(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSarDsiResponse(_arg2);
                    reply.writeNoException();
                    return true;
                case 3:
                    RadioResponseInfo _arg3 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSarDsiGroupResponse(_arg3);
                    reply.writeNoException();
                    return true;
                case 4:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    getSarDsiResponse(_arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 5:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg7 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getSarDsiGroupResponse(_arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 6:
                    RadioResponseInfo _arg8 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setDeprioritizeSAResponse(_arg8);
                    reply.writeNoException();
                    return true;
                case 7:
                    RadioResponseInfo _arg9 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setNrModeResponse(_arg9);
                    reply.writeNoException();
                    return true;
                case 8:
                    RadioResponseInfo _arg10 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    enableEndcResponse(_arg10);
                    reply.writeNoException();
                    return true;
                case 9:
                    RadioResponseInfo _arg11 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setDataStallModeResponse(_arg11);
                    reply.writeNoException();
                    return true;
                case 10:
                    RadioResponseInfo _arg12 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setMobilityOptParamsResponse(_arg12);
                    reply.writeNoException();
                    return true;
                case 11:
                    RadioResponseInfo _arg13 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setPreferBandParamsResponse(_arg13);
                    reply.writeNoException();
                    return true;
                case 12:
                    RadioResponseInfo _arg14 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    promoteBSRResponse(_arg14);
                    reply.writeNoException();
                    return true;
                case 13:
                    RadioResponseInfo _arg15 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg16 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getDdsAssistantInfoResponse(_arg15, _arg16);
                    return true;
                case 14:
                    RadioResponseInfo _arg17 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg18 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getModemSceneResponse(_arg17, _arg18);
                    return true;
                case 15:
                    RadioResponseInfo _arg19 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg20 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getModemReasonResponse(_arg19, _arg20);
                    return true;
                case 16:
                    RadioResponseInfo _arg21 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setModemModelCfgResponse(_arg21);
                    return true;
                case 17:
                    RadioResponseInfo _arg22 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setModemCallbackSwitchResponse(_arg22);
                    return true;
                case 18:
                    RadioResponseInfo _arg23 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setModemEventIndSwitchResponse(_arg23);
                    return true;
                case 19:
                    RadioResponseInfo _arg24 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setFiveTupleBlackListResponse(_arg24);
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
        private static final class Proxy implements IXmRadioResponse {
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void sendCommMsgResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setSarDsiResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setSarDsiGroupResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void getSarDsiResponse(RadioResponseInfo info, int dsi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(dsi);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void getSarDsiGroupResponse(RadioResponseInfo info, int[] dsiGroup) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(dsiGroup);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setDeprioritizeSAResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setNrModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void enableEndcResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setDataStallModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setMobilityOptParamsResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setPreferBandParamsResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void promoteBSRResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void getDdsAssistantInfoResponse(RadioResponseInfo info, int[] assistantInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(assistantInfo);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void getModemSceneResponse(RadioResponseInfo info, int[] scene) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(scene);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void getModemReasonResponse(RadioResponseInfo info, int[] reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(reason);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setModemModelCfgResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setModemCallbackSwitchResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setModemEventIndSwitchResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
            public void setFiveTupleBlackListResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
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

            @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
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

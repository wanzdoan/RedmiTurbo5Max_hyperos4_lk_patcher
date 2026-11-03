package vendor.mediatek.hardware.mtkradioex.voice;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExVoiceResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$voice$IMtkRadioExVoiceResponse".replace('$', '.');
    public static final String HASH = "6fb707faf11116647ab6b8daa3ee47c2662abaa2";
    public static final int VERSION = 1;

    void getCallSubAddressResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getColpResponse(RadioResponseInfo radioResponseInfo, int i, int i2) throws RemoteException;

    void getColrResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getEccNumResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void hangupAllResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void hangupWithReasonResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo radioResponseInfo, CallForwardInfoEx[] callForwardInfoExArr) throws RemoteException;

    void resetSuppServResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendCnapResponse(RadioResponseInfo radioResponseInfo, int i, int i2) throws RemoteException;

    void setCallForwardInTimeSlotResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCallIndicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCallSubAddressResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCallValidTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setClipResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setColpResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setColrResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setEccModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setEccNumResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setGwsdModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setIgnoreSameNumberIntervalResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setKeepAliveByIpDataResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSuppServPropertyResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExVoiceResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void hangupAllResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void hangupWithReasonResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void getCallSubAddressResponse(RadioResponseInfo info, int enable) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void getColpResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void getColrResponse(RadioResponseInfo info, int n) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void getEccNumResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo info, CallForwardInfoEx[] callForwardInfoExs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void resetSuppServResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void sendCnapResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setCallForwardInTimeSlotResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setCallIndicationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setCallSubAddressResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setCallValidTimerResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setClipResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setColpResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setColrResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setEccModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setEccNumResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setGwsdModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setKeepAliveByIpDataResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public void setSuppServPropertyResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExVoiceResponse {
        static final int TRANSACTION_getCallSubAddressResponse = 3;
        static final int TRANSACTION_getColpResponse = 4;
        static final int TRANSACTION_getColrResponse = 5;
        static final int TRANSACTION_getEccNumResponse = 6;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_hangupAllResponse = 1;
        static final int TRANSACTION_hangupWithReasonResponse = 2;
        static final int TRANSACTION_queryCallForwardInTimeSlotStatusResponse = 7;
        static final int TRANSACTION_resetSuppServResponse = 8;
        static final int TRANSACTION_sendCnapResponse = 9;
        static final int TRANSACTION_setCallForwardInTimeSlotResponse = 10;
        static final int TRANSACTION_setCallIndicationResponse = 11;
        static final int TRANSACTION_setCallSubAddressResponse = 12;
        static final int TRANSACTION_setCallValidTimerResponse = 13;
        static final int TRANSACTION_setClipResponse = 14;
        static final int TRANSACTION_setColpResponse = 15;
        static final int TRANSACTION_setColrResponse = 16;
        static final int TRANSACTION_setEccModeResponse = 17;
        static final int TRANSACTION_setEccNumResponse = 18;
        static final int TRANSACTION_setGwsdModeResponse = 19;
        static final int TRANSACTION_setIgnoreSameNumberIntervalResponse = 20;
        static final int TRANSACTION_setKeepAliveByIpDataResponse = 21;
        static final int TRANSACTION_setKeepAliveByPDCPCtrlPDUResponse = 22;
        static final int TRANSACTION_setSuppServPropertyResponse = 23;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExVoiceResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExVoiceResponse)) {
                return (IMtkRadioExVoiceResponse) iin;
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
                    data.enforceNoDataAvail();
                    hangupAllResponse(_arg0);
                    return true;
                case 2:
                    RadioResponseInfo _arg1 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    hangupWithReasonResponse(_arg1);
                    return true;
                case 3:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    getCallSubAddressResponse(_arg2, _arg3);
                    return true;
                case 4:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    getColpResponse(_arg4, _arg5, _arg6);
                    return true;
                case 5:
                    RadioResponseInfo _arg7 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg8 = data.readInt();
                    data.enforceNoDataAvail();
                    getColrResponse(_arg7, _arg8);
                    return true;
                case 6:
                    RadioResponseInfo _arg9 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    getEccNumResponse(_arg9);
                    return true;
                case 7:
                    RadioResponseInfo _arg10 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    CallForwardInfoEx[] _arg11 = (CallForwardInfoEx[]) data.createTypedArray(CallForwardInfoEx.CREATOR);
                    data.enforceNoDataAvail();
                    queryCallForwardInTimeSlotStatusResponse(_arg10, _arg11);
                    return true;
                case 8:
                    RadioResponseInfo _arg12 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    resetSuppServResponse(_arg12);
                    return true;
                case 9:
                    RadioResponseInfo _arg13 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg14 = data.readInt();
                    int _arg15 = data.readInt();
                    data.enforceNoDataAvail();
                    sendCnapResponse(_arg13, _arg14, _arg15);
                    return true;
                case 10:
                    RadioResponseInfo _arg16 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCallForwardInTimeSlotResponse(_arg16);
                    return true;
                case 11:
                    RadioResponseInfo _arg17 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCallIndicationResponse(_arg17);
                    return true;
                case 12:
                    RadioResponseInfo _arg18 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCallSubAddressResponse(_arg18);
                    return true;
                case 13:
                    RadioResponseInfo _arg19 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCallValidTimerResponse(_arg19);
                    return true;
                case 14:
                    RadioResponseInfo _arg20 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setClipResponse(_arg20);
                    return true;
                case 15:
                    RadioResponseInfo _arg21 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setColpResponse(_arg21);
                    return true;
                case 16:
                    RadioResponseInfo _arg22 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setColrResponse(_arg22);
                    return true;
                case 17:
                    RadioResponseInfo _arg23 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setEccModeResponse(_arg23);
                    return true;
                case 18:
                    RadioResponseInfo _arg24 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setEccNumResponse(_arg24);
                    return true;
                case TRANSACTION_setGwsdModeResponse /* 19 */:
                    RadioResponseInfo _arg25 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setGwsdModeResponse(_arg25);
                    return true;
                case 20:
                    RadioResponseInfo _arg26 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setIgnoreSameNumberIntervalResponse(_arg26);
                    return true;
                case TRANSACTION_setKeepAliveByIpDataResponse /* 21 */:
                    RadioResponseInfo _arg27 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setKeepAliveByIpDataResponse(_arg27);
                    return true;
                case TRANSACTION_setKeepAliveByPDCPCtrlPDUResponse /* 22 */:
                    RadioResponseInfo _arg28 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setKeepAliveByPDCPCtrlPDUResponse(_arg28);
                    return true;
                case TRANSACTION_setSuppServPropertyResponse /* 23 */:
                    RadioResponseInfo _arg29 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSuppServPropertyResponse(_arg29);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExVoiceResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void hangupAllResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void hangupWithReasonResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void getCallSubAddressResponse(RadioResponseInfo info, int enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(enable);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void getColpResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(n);
                    _data.writeInt(m);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void getColrResponse(RadioResponseInfo info, int n) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(n);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void getEccNumResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo info, CallForwardInfoEx[] callForwardInfoExs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(callForwardInfoExs, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void resetSuppServResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void sendCnapResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(n);
                    _data.writeInt(m);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setCallForwardInTimeSlotResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setCallIndicationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setCallSubAddressResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setCallValidTimerResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setClipResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setColpResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setColrResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setEccModeResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setEccNumResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setGwsdModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setGwsdModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setKeepAliveByIpDataResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setKeepAliveByIpDataResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setKeepAliveByPDCPCtrlPDUResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
            public void setSuppServPropertyResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSuppServPropertyResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
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

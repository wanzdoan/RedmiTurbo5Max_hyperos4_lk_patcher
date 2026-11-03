package vendor.mediatek.hardware.mtkradioex.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExNetworkIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$network$IMtkRadioExNetworkIndication".replace('$', '.');
    public static final String HASH = "583aefd4764eb70d99325928242c9ced29a9c0ee";
    public static final int VERSION = 1;

    void currentSignalStrengthWithWcdmaEcioInd(int i, SignalStrengthWithWcdmaEcio signalStrengthWithWcdmaEcio) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void iwlanRegistrationStateInd(int i, int i2) throws RemoteException;

    void networkBandInfoInd(int i, int[] iArr) throws RemoteException;

    void networkInfoInd(int i, String[] strArr) throws RemoteException;

    void nrCaBandChangeInd(int i, int[] iArr) throws RemoteException;

    void nrSysInfoInd(int i, int[] iArr) throws RemoteException;

    void on5GUWInfoInd(int i, int[] iArr) throws RemoteException;

    void onMccMncChanged(int i, String str) throws RemoteException;

    void onNwCfgInfoInd(int i, boolean z, boolean z2, boolean z3) throws RemoteException;

    void onNwRrcStateInd(int i, int i2, int i3) throws RemoteException;

    void onPseudoCellInfoInd(int i, int[] iArr) throws RemoteException;

    void responseCsNetworkStateChangeInd(int i, String[] strArr) throws RemoteException;

    void responseFemtocellInfo(int i, String[] strArr) throws RemoteException;

    void responseLteNetworkInfo(int i, int i2) throws RemoteException;

    void responseModulationInfoInd(int i, int[] iArr) throws RemoteException;

    void responseNetworkEventInd(int i, int[] iArr) throws RemoteException;

    void responsePsNetworkStateChangeInd(int i, int[] iArr) throws RemoteException;

    void sib16TimeInfoInd(int i, String str, long j) throws RemoteException;

    void toeInfoInd(int i, String str, String str2, String str3) throws RemoteException;

    public static class Default implements IMtkRadioExNetworkIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void currentSignalStrengthWithWcdmaEcioInd(int type, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void networkBandInfoInd(int type, int[] state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void networkInfoInd(int type, String[] networkinfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void nrCaBandChangeInd(int type, int[] bands) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void nrSysInfoInd(int type, int[] nrSysInfos) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void on5GUWInfoInd(int type, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void onMccMncChanged(int type, String mccmnc) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void onNwCfgInfoInd(int type, boolean mimo, boolean qam_256, boolean qam_ul64) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void onNwRrcStateInd(int type, int rat, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void onPseudoCellInfoInd(int type, int[] cellInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responseCsNetworkStateChangeInd(int type, String[] state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responseFemtocellInfo(int type, String[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responseLteNetworkInfo(int type, int lteBand) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responseModulationInfoInd(int type, int[] modulation) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responseNetworkEventInd(int type, int[] event) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void responsePsNetworkStateChangeInd(int type, int[] state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void sib16TimeInfoInd(int type, String sib16Time, long receivedTime) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void toeInfoInd(int type, String longName, String shortName, String numeric) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public void iwlanRegistrationStateInd(int type, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExNetworkIndication {
        static final int TRANSACTION_currentSignalStrengthWithWcdmaEcioInd = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_iwlanRegistrationStateInd = 19;
        static final int TRANSACTION_networkBandInfoInd = 2;
        static final int TRANSACTION_networkInfoInd = 3;
        static final int TRANSACTION_nrCaBandChangeInd = 4;
        static final int TRANSACTION_nrSysInfoInd = 5;
        static final int TRANSACTION_on5GUWInfoInd = 6;
        static final int TRANSACTION_onMccMncChanged = 7;
        static final int TRANSACTION_onNwCfgInfoInd = 8;
        static final int TRANSACTION_onNwRrcStateInd = 9;
        static final int TRANSACTION_onPseudoCellInfoInd = 10;
        static final int TRANSACTION_responseCsNetworkStateChangeInd = 11;
        static final int TRANSACTION_responseFemtocellInfo = 12;
        static final int TRANSACTION_responseLteNetworkInfo = 13;
        static final int TRANSACTION_responseModulationInfoInd = 14;
        static final int TRANSACTION_responseNetworkEventInd = 15;
        static final int TRANSACTION_responsePsNetworkStateChangeInd = 16;
        static final int TRANSACTION_sib16TimeInfoInd = 17;
        static final int TRANSACTION_toeInfoInd = 18;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExNetworkIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExNetworkIndication)) {
                return (IMtkRadioExNetworkIndication) iin;
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
                    SignalStrengthWithWcdmaEcio _arg1 = (SignalStrengthWithWcdmaEcio) data.readTypedObject(SignalStrengthWithWcdmaEcio.CREATOR);
                    data.enforceNoDataAvail();
                    currentSignalStrengthWithWcdmaEcioInd(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int[] _arg3 = data.createIntArray();
                    data.enforceNoDataAvail();
                    networkBandInfoInd(_arg2, _arg3);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    String[] _arg5 = data.createStringArray();
                    data.enforceNoDataAvail();
                    networkInfoInd(_arg4, _arg5);
                    return true;
                case 4:
                    int _arg6 = data.readInt();
                    int[] _arg7 = data.createIntArray();
                    data.enforceNoDataAvail();
                    nrCaBandChangeInd(_arg6, _arg7);
                    return true;
                case 5:
                    int _arg8 = data.readInt();
                    int[] _arg9 = data.createIntArray();
                    data.enforceNoDataAvail();
                    nrSysInfoInd(_arg8, _arg9);
                    return true;
                case 6:
                    int _arg10 = data.readInt();
                    int[] _arg11 = data.createIntArray();
                    data.enforceNoDataAvail();
                    on5GUWInfoInd(_arg10, _arg11);
                    return true;
                case 7:
                    int _arg12 = data.readInt();
                    String _arg13 = data.readString();
                    data.enforceNoDataAvail();
                    onMccMncChanged(_arg12, _arg13);
                    return true;
                case 8:
                    int _arg14 = data.readInt();
                    boolean _arg15 = data.readBoolean();
                    boolean _arg16 = data.readBoolean();
                    boolean _arg17 = data.readBoolean();
                    data.enforceNoDataAvail();
                    onNwCfgInfoInd(_arg14, _arg15, _arg16, _arg17);
                    return true;
                case 9:
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    data.enforceNoDataAvail();
                    onNwRrcStateInd(_arg18, _arg19, _arg20);
                    return true;
                case 10:
                    int _arg21 = data.readInt();
                    int[] _arg22 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onPseudoCellInfoInd(_arg21, _arg22);
                    return true;
                case 11:
                    int _arg23 = data.readInt();
                    String[] _arg24 = data.createStringArray();
                    data.enforceNoDataAvail();
                    responseCsNetworkStateChangeInd(_arg23, _arg24);
                    return true;
                case 12:
                    int _arg25 = data.readInt();
                    String[] _arg26 = data.createStringArray();
                    data.enforceNoDataAvail();
                    responseFemtocellInfo(_arg25, _arg26);
                    return true;
                case 13:
                    int _arg27 = data.readInt();
                    int _arg28 = data.readInt();
                    data.enforceNoDataAvail();
                    responseLteNetworkInfo(_arg27, _arg28);
                    return true;
                case 14:
                    int _arg29 = data.readInt();
                    int[] _arg30 = data.createIntArray();
                    data.enforceNoDataAvail();
                    responseModulationInfoInd(_arg29, _arg30);
                    return true;
                case 15:
                    int _arg31 = data.readInt();
                    int[] _arg32 = data.createIntArray();
                    data.enforceNoDataAvail();
                    responseNetworkEventInd(_arg31, _arg32);
                    return true;
                case 16:
                    int _arg33 = data.readInt();
                    int[] _arg34 = data.createIntArray();
                    data.enforceNoDataAvail();
                    responsePsNetworkStateChangeInd(_arg33, _arg34);
                    return true;
                case 17:
                    int _arg35 = data.readInt();
                    String _arg36 = data.readString();
                    long _arg37 = data.readLong();
                    data.enforceNoDataAvail();
                    sib16TimeInfoInd(_arg35, _arg36, _arg37);
                    return true;
                case 18:
                    int _arg38 = data.readInt();
                    String _arg39 = data.readString();
                    String _arg40 = data.readString();
                    String _arg41 = data.readString();
                    data.enforceNoDataAvail();
                    toeInfoInd(_arg38, _arg39, _arg40, _arg41);
                    return true;
                case TRANSACTION_iwlanRegistrationStateInd /* 19 */:
                    int _arg42 = data.readInt();
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    iwlanRegistrationStateInd(_arg42, _arg43);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExNetworkIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void currentSignalStrengthWithWcdmaEcioInd(int type, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(signalStrength, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void networkBandInfoInd(int type, int[] state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(state);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void networkInfoInd(int type, String[] networkinfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(networkinfo);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void nrCaBandChangeInd(int type, int[] bands) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(bands);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void nrSysInfoInd(int type, int[] nrSysInfos) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(nrSysInfos);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void on5GUWInfoInd(int type, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void onMccMncChanged(int type, String mccmnc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(mccmnc);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void onNwCfgInfoInd(int type, boolean mimo, boolean qam_256, boolean qam_ul64) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeBoolean(mimo);
                    _data.writeBoolean(qam_256);
                    _data.writeBoolean(qam_ul64);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void onNwRrcStateInd(int type, int rat, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(rat);
                    _data.writeInt(state);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void onPseudoCellInfoInd(int type, int[] cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(cellInfo);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responseCsNetworkStateChangeInd(int type, String[] state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(state);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responseFemtocellInfo(int type, String[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responseLteNetworkInfo(int type, int lteBand) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(lteBand);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responseModulationInfoInd(int type, int[] modulation) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(modulation);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responseNetworkEventInd(int type, int[] event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(event);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void responsePsNetworkStateChangeInd(int type, int[] state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(state);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void sib16TimeInfoInd(int type, String sib16Time, long receivedTime) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(sib16Time);
                    _data.writeLong(receivedTime);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void toeInfoInd(int type, String longName, String shortName, String numeric) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(longName);
                    _data.writeString(shortName);
                    _data.writeString(numeric);
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
            public void iwlanRegistrationStateInd(int type, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_iwlanRegistrationStateInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
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

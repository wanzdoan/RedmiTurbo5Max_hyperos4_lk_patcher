package vendor.xiaomi.modem.qms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiQms extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$modem$qms$IMiQms".replace('$', '.');

    int enableCellBarringFeature(int i, boolean z);

    int getDppdCapability();

    int getDppdRecommendation();

    int getHstState();

    int getMultiSimSubmodeInfo();

    int getNfList(int i);

    void registerDppdIndication();

    void registerHstStateReport();

    void registerMultiSimSubmodeIndication();

    void registerQmsClient();

    void registerSubwayStateReport();

    void setCallback(IMiQmsResponse iMiQmsResponse);

    int setCellBarringInfo(int i, CellBarringInfoS cellBarringInfoS);

    long setCellListInfo(CellListS cellListS);

    long setCellListInfoV2(SetCellListInfoV2S setCellListInfoV2S);

    int setCoverageState(int i, int i2);

    int setDataStallParameters(int i, DataStallParamS dataStallParamS);

    int setHstState(int i);

    int setL2NrSelection(int i);

    int setLteDubiousCellArray(int i, char c, LteDubiousCellInfoS[] lteDubiousCellInfoSArr);

    int setNfList(int i, char c, Nr5gNflEntryTypeS[] nr5gNflEntryTypeSArr);

    int setNrDubiousCellArray(int i, char c, NrDubiousCellInfoS[] nrDubiousCellInfoSArr);

    int setOutOfServiceState(int i, int i2);

    int setPreferredCellList(int i, PreferredCellListS preferredCellListS);

    long setPreferredFrequencyList(SetPreferredFrequencyListS setPreferredFrequencyListS);

    void setResponseFunctions(IMiQmsResponse iMiQmsResponse, IMiQmsIndication iMiQmsIndication);

    int setSubwayState(int i);

    int updateDppdUiOption(boolean z);

    public abstract class Stub extends Binder implements IMiQms {
        public static IMiQms asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IMiQms.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IMiQms)) {
                return (IMiQms) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        final class Proxy implements IMiQms {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int getDppdCapability() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int getDppdRecommendation() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int getHstState() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int getMultiSimSubmodeInfo() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int getNfList(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void registerDppdIndication() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void registerHstStateReport() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void registerMultiSimSubmodeIndication() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(8, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void registerQmsClient() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(9, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void setCallback(IMiQmsResponse iMiQmsResponse) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iMiQmsResponse);
                    if (!this.mRemote.transact(10, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setCoverageState(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(11, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i3 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i3;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setDataStallParameters(int i, DataStallParamS dataStallParamS) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(dataStallParamS, 0);
                    if (!this.mRemote.transact(12, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setHstState(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(13, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setL2NrSelection(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(14, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setLteDubiousCellArray(int i, char c, LteDubiousCellInfoS[] lteDubiousCellInfoSArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(c);
                    parcelObtain.writeTypedArray(lteDubiousCellInfoSArr, 0);
                    if (!this.mRemote.transact(15, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setNfList(int i, char c, Nr5gNflEntryTypeS[] nr5gNflEntryTypeSArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(c);
                    parcelObtain.writeTypedArray(nr5gNflEntryTypeSArr, 0);
                    if (!this.mRemote.transact(16, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setNrDubiousCellArray(int i, char c, NrDubiousCellInfoS[] nrDubiousCellInfoSArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(c);
                    parcelObtain.writeTypedArray(nrDubiousCellInfoSArr, 0);
                    if (!this.mRemote.transact(17, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setOutOfServiceState(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(18, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i3 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i3;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setPreferredCellList(int i, PreferredCellListS preferredCellListS) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(preferredCellListS, 0);
                    if (!this.mRemote.transact(19, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int updateDppdUiOption(boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(20, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int enableCellBarringFeature(int i, boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(21, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setCellBarringInfo(int i, CellBarringInfoS cellBarringInfoS) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(cellBarringInfoS, 0);
                    if (!this.mRemote.transact(22, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void registerSubwayStateReport() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    if (!this.mRemote.transact(23, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public int setSubwayState(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(24, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    int i2 = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public void setResponseFunctions(IMiQmsResponse iMiQmsResponse, IMiQmsIndication iMiQmsIndication) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iMiQmsResponse);
                    parcelObtain.writeStrongInterface(iMiQmsIndication);
                    if (!this.mRemote.transact(25, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public long setCellListInfo(CellListS cellListS) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeTypedObject(cellListS, 0);
                    if (!this.mRemote.transact(26, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public long setCellListInfoV2(SetCellListInfoV2S setCellListInfoV2S) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeTypedObject(setCellListInfoV2S, 0);
                    if (!this.mRemote.transact(27, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.modem.qms.IMiQms
            public long setPreferredFrequencyList(SetPreferredFrequencyListS setPreferredFrequencyListS) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMiQms.DESCRIPTOR);
                    parcelObtain.writeTypedObject(setPreferredFrequencyListS, 0);
                    if (!this.mRemote.transact(29, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }
        }
    }
}

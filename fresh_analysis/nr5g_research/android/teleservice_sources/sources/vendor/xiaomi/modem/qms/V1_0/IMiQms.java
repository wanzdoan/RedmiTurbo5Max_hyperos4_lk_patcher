package vendor.xiaomi.modem.qms.V1_0;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiQms extends IHwInterface {
    int getDppdCapability();

    int getDppdRecommendation();

    int getHstState();

    int getMultiSimSubmodeInfo();

    int getNfList(int i);

    ArrayList interfaceChain();

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j);

    void registerDppdIndication();

    void registerHstStateReport();

    void registerMultiSimSubmodeIndication();

    void registerQmsClient();

    void setCallback(IMiQmsResponse iMiQmsResponse);

    int setCoverageState(int i, int i2);

    int setDataStallParameters(int i, DataStallParamS dataStallParamS);

    int setHstState(int i);

    int setL2NrSelection(int i);

    int setLteDubiousCellArray(int i, short s, ArrayList arrayList);

    int setNfList(int i, short s, ArrayList arrayList);

    int setNrDubiousCellArray(int i, short s, ArrayList arrayList);

    int setOutOfServiceState(int i, int i2);

    int setPreferredCellList(int i, PreferredCellListS preferredCellListS);

    int updateDppdUiOption(boolean z);

    static IMiQms asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface iHwInterfaceQueryLocalInterface = iHwBinder.queryLocalInterface("vendor.xiaomi.modem.qms@1.0::IMiQms");
        if (iHwInterfaceQueryLocalInterface != null && (iHwInterfaceQueryLocalInterface instanceof IMiQms)) {
            return (IMiQms) iHwInterfaceQueryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            ArrayList arrayListInterfaceChain = proxy.interfaceChain();
            int size = arrayListInterfaceChain.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayListInterfaceChain.get(i);
                i++;
                if (((String) obj).equals("vendor.xiaomi.modem.qms@1.0::IMiQms")) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static IMiQms getService(String str) {
        return asInterface(HwBinder.getService("vendor.xiaomi.modem.qms@1.0::IMiQms", str));
    }

    static IMiQms getService() {
        return getService("default");
    }

    public final class Proxy implements IMiQms {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.xiaomi.modem.qms@1.0::IMiQms]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public void setCallback(IMiQmsResponse iMiQmsResponse) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeStrongBinder(iMiQmsResponse == null ? null : iMiQmsResponse.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public void registerQmsClient() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setDataStallParameters(int i, DataStallParamS dataStallParamS) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            dataStallParamS.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setNfList(int i, short s, ArrayList arrayList) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            hwParcel.writeInt16(s);
            Nr5gNflEntryTypeS.writeVectorToParcel(hwParcel, arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int getNfList(int i) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setL2NrSelection(int i) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setLteDubiousCellArray(int i, short s, ArrayList arrayList) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            hwParcel.writeInt16(s);
            LteDubiousCellInfoS.writeVectorToParcel(hwParcel, arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(7, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setNrDubiousCellArray(int i, short s, ArrayList arrayList) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            hwParcel.writeInt16(s);
            NrDubiousCellInfoS.writeVectorToParcel(hwParcel, arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(8, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int updateDppdUiOption(boolean z) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(9, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int getDppdCapability() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(10, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int getDppdRecommendation() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(11, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public void registerDppdIndication() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(12, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setOutOfServiceState(int i, int i2) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(13, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public void registerHstStateReport() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(14, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setHstState(int i) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(15, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int getHstState() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(16, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public void registerMultiSimSubmodeIndication() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(17, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int getMultiSimSubmodeInfo() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(18, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setCoverageState(int i, int i2) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            hwParcel.writeInt32(i2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(19, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public int setPreferredCellList(int i, PreferredCellListS preferredCellListS) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.modem.qms@1.0::IMiQms");
            hwParcel.writeInt32(i);
            preferredCellListS.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(20, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public ArrayList interfaceChain() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readStringVector();
            } finally {
                hwParcel2.release();
            }
        }

        public String interfaceDescriptor() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQms
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }
    }
}

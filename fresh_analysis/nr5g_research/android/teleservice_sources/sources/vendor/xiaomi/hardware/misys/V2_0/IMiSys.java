package vendor.xiaomi.hardware.misys.V2_0;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiSys extends IHwInterface {
    boolean IsExists(String str, String str2);

    int MiSysWriteBuffer(String str, String str2, ArrayList arrayList, long j);

    ArrayList interfaceChain();

    static IMiSys asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface iHwInterfaceQueryLocalInterface = iHwBinder.queryLocalInterface("vendor.xiaomi.hardware.misys@2.0::IMiSys");
        if (iHwInterfaceQueryLocalInterface != null && (iHwInterfaceQueryLocalInterface instanceof IMiSys)) {
            return (IMiSys) iHwInterfaceQueryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            ArrayList arrayListInterfaceChain = proxy.interfaceChain();
            int size = arrayListInterfaceChain.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayListInterfaceChain.get(i);
                i++;
                if (((String) obj).equals("vendor.xiaomi.hardware.misys@2.0::IMiSys")) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static IMiSys getService(String str, boolean z) {
        return asInterface(HwBinder.getService("vendor.xiaomi.hardware.misys@2.0::IMiSys", str, z));
    }

    static IMiSys getService(boolean z) {
        return getService("default", z);
    }

    public final class Proxy implements IMiSys {
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
                return "[class or subclass of vendor.xiaomi.hardware.misys@2.0::IMiSys]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.xiaomi.hardware.misys.V2_0.IMiSys
        public int MiSysWriteBuffer(String str, String str2, ArrayList arrayList, long j) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.hardware.misys@2.0::IMiSys");
            hwParcel.writeString(str);
            hwParcel.writeString(str2);
            hwParcel.writeInt8Vector(arrayList);
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.hardware.misys.V2_0.IMiSys
        public boolean IsExists(String str, String str2) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.xiaomi.hardware.misys@2.0::IMiSys");
            hwParcel.writeString(str);
            hwParcel.writeString(str2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.xiaomi.hardware.misys.V2_0.IMiSys
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
    }
}

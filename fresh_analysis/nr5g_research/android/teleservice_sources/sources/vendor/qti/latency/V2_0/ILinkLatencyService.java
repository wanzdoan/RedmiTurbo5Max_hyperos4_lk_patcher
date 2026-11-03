package vendor.qti.latency.V2_0;

import android.os.HidlSupport;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public interface ILinkLatencyService extends IHwInterface {

    public interface setLevelCallback {
        void onValues(long j, long j2, long j3);
    }

    ArrayList interfaceChain();

    long prioritizeDefaultDataSubscription(boolean z);

    void setLevel(Config config, setLevelCallback setlevelcallback);

    static ILinkLatencyService asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface iHwInterfaceQueryLocalInterface = iHwBinder.queryLocalInterface("vendor.qti.latency@2.0::ILinkLatencyService");
        if (iHwInterfaceQueryLocalInterface != null && (iHwInterfaceQueryLocalInterface instanceof ILinkLatencyService)) {
            return (ILinkLatencyService) iHwInterfaceQueryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            ArrayList arrayListInterfaceChain = proxy.interfaceChain();
            int size = arrayListInterfaceChain.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayListInterfaceChain.get(i);
                i++;
                if (((String) obj).equals("vendor.qti.latency@2.0::ILinkLatencyService")) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    public final class Proxy implements ILinkLatencyService {
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
                return "[class or subclass of vendor.qti.latency@2.0::ILinkLatencyService]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.qti.latency.V2_0.ILinkLatencyService
        public void setLevel(Config config, setLevelCallback setlevelcallback) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.qti.latency@2.0::ILinkLatencyService");
            config.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                setlevelcallback.onValues(hwParcel2.readInt64(), hwParcel2.readInt64(), hwParcel2.readInt64());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.qti.latency.V2_0.ILinkLatencyService
        public long prioritizeDefaultDataSubscription(boolean z) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.qti.latency@2.0::ILinkLatencyService");
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt64();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.qti.latency.V2_0.ILinkLatencyService
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

package vendor.qti.data.factory.V2_0;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.latency.V2_0.IClientToken;
import vendor.qti.latency.V2_0.ILinkLatencyService;

/* JADX INFO: loaded from: classes2.dex */
public interface IFactory extends IHwInterface {

    public interface createILinkLatencyServiceCallback {
        void onValues(int i, ILinkLatencyService iLinkLatencyService);
    }

    void createILinkLatencyService(IClientToken iClientToken, createILinkLatencyServiceCallback createilinklatencyservicecallback);

    ArrayList interfaceChain();

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j);

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient);

    static IFactory asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IHwInterface iHwInterfaceQueryLocalInterface = iHwBinder.queryLocalInterface("vendor.qti.data.factory@2.0::IFactory");
        if (iHwInterfaceQueryLocalInterface != null && (iHwInterfaceQueryLocalInterface instanceof IFactory)) {
            return (IFactory) iHwInterfaceQueryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            ArrayList arrayListInterfaceChain = proxy.interfaceChain();
            int size = arrayListInterfaceChain.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayListInterfaceChain.get(i);
                i++;
                if (((String) obj).equals("vendor.qti.data.factory@2.0::IFactory")) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static IFactory getService(String str) {
        return asInterface(HwBinder.getService("vendor.qti.data.factory@2.0::IFactory", str));
    }

    static IFactory getService() {
        return getService("default");
    }

    public final class Proxy implements IFactory {
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
                return "[class or subclass of vendor.qti.data.factory@2.0::IFactory]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.qti.data.factory.V2_0.IFactory
        public void createILinkLatencyService(IClientToken iClientToken, createILinkLatencyServiceCallback createilinklatencyservicecallback) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.qti.data.factory@2.0::IFactory");
            hwParcel.writeStrongBinder(iClientToken == null ? null : iClientToken.asBinder());
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(6, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                createilinklatencyservicecallback.onValues(hwParcel2.readInt32(), ILinkLatencyService.asInterface(hwParcel2.readStrongBinder()));
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.qti.data.factory.V2_0.IFactory
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

        @Override // vendor.qti.data.factory.V2_0.IFactory
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        @Override // vendor.qti.data.factory.V2_0.IFactory
        public boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }
}

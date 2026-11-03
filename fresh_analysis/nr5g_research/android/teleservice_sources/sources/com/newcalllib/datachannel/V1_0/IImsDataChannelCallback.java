package com.newcalllib.datachannel.V1_0;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

/* JADX INFO: loaded from: classes.dex */
public interface IImsDataChannelCallback extends IInterface {

    public abstract class Stub extends Binder implements IImsDataChannelCallback {
        public static IImsDataChannelCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.newcalllib.datachannel.V1_0.IImsDataChannelCallback");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IImsDataChannelCallback)) {
                return (IImsDataChannelCallback) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        final class Proxy implements IImsDataChannelCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }
        }
    }
}

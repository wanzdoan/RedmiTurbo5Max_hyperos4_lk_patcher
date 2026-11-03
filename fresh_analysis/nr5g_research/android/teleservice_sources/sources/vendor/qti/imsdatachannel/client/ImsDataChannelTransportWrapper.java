package vendor.qti.imsdatachannel.client;

import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.Executor;
import vendor.qti.imsdatachannel.aidl.IImsDataChannelEventListener;
import vendor.qti.imsdatachannel.aidl.IImsDataChannelTransport;

/* JADX INFO: loaded from: classes2.dex */
public class ImsDataChannelTransportWrapper implements ImsDataChannelTransport {
    private final String mCallId;
    private final Executor mClientAppExecutor;
    private IImsDataChannelTransport mDCBinderTransportInstance;
    private final ImsDataChannelEventListener mEventListener;
    private final IImsDataChannelEventListener mEventListenerStub = new IImsDataChannelEventListener.Stub() { // from class: vendor.qti.imsdatachannel.client.ImsDataChannelTransportWrapper.1
    };
    private final Executor mExecutor;
    private final int mSlotId;

    public ImsDataChannelTransportWrapper(Executor executor, Executor executor2, int i, String str, ImsDataChannelEventListener imsDataChannelEventListener) {
        this.mExecutor = executor;
        this.mClientAppExecutor = executor2;
        this.mSlotId = i;
        this.mCallId = str;
        this.mEventListener = imsDataChannelEventListener;
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelTransport
    public void createDataChannel(final String[] strArr, final String str) {
        this.mExecutor.execute(new Runnable() { // from class: vendor.qti.imsdatachannel.client.ImsDataChannelTransportWrapper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createDataChannel$0(strArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDataChannel$0(String[] strArr, String str) {
        try {
            this.mDCBinderTransportInstance.createDataChannel(strArr, str);
        } catch (RemoteException unused) {
            Log.e("datachannellib:ImsDataChannelTransportWrapper", "createDataChannel: RemoteException occured");
        }
    }

    public IImsDataChannelEventListener getDataChannelEventListener() {
        return this.mEventListenerStub;
    }

    public boolean equals(int i, String str) {
        return i == this.mSlotId && str.equals(this.mCallId);
    }

    public void setDataChannelTransportBinderInstance(IImsDataChannelTransport iImsDataChannelTransport) {
        this.mDCBinderTransportInstance = iImsDataChannelTransport;
    }

    public IImsDataChannelTransport getDataChannelTransportBinderInstance() {
        return this.mDCBinderTransportInstance;
    }
}

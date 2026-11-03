package vendor.qti.imsdatachannel.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import vendor.qti.imsdatachannel.aidl.IImsDataChannelService;
import vendor.qti.imsdatachannel.aidl.IImsDataChannelServiceAvailabilityCallback;
import vendor.qti.imsdatachannel.aidl.IImsDataChannelTransport;

/* JADX INFO: loaded from: classes2.dex */
public class ImsDataChannelServiceManagerImpl implements ImsDataChannelServiceManager {
    private Executor mCbExecutor;
    private Context mContext;
    private ImsDataChannelServiceConnectionCallback mDataChannelServiceConnectionCallback;
    private ImsDataChannelManagerServiceConnection mServiceConnection;
    private IImsDataChannelService mDataChannelService = null;
    private boolean mIsBound = false;
    private List dataChannelTransportList = new ArrayList();
    Executor mDataChannelMgrExecutor = new ScheduledThreadPoolExecutor(1);
    IImsDataChannelTransport transportBinderObj = null;
    ImsDataChannelTransportWrapper transportObj = null;
    ImsDcServiceAvailabilityCallback[] mServiceAvailabilityCb = new ImsDcServiceAvailabilityCallback[2];

    class ImsDcServiceAvailabilityCallback extends IImsDataChannelServiceAvailabilityCallback.Stub {
        private ImsDataChannelServiceAvailabilityCallback mDataChannelServiceAvailabilityCallback;
        int mSlotId;

        ImsDcServiceAvailabilityCallback(int i, ImsDataChannelServiceAvailabilityCallback imsDataChannelServiceAvailabilityCallback) {
            this.mSlotId = i;
            this.mDataChannelServiceAvailabilityCallback = imsDataChannelServiceAvailabilityCallback;
        }
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager
    public void initialize(Context context, Executor executor) {
        this.mContext = context;
        this.mCbExecutor = executor;
    }

    public class ImsDataChannelManagerServiceConnection implements ServiceConnection {
        public ImsDataChannelManagerServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "On service connected, get the binder object");
            ImsDataChannelServiceManagerImpl.this.mDataChannelService = IImsDataChannelService.Stub.asInterface(iBinder);
            ImsDataChannelServiceManagerImpl.this.mIsBound = true;
            if (ImsDataChannelServiceManagerImpl.this.mDataChannelServiceConnectionCallback != null) {
                ImsDataChannelServiceManagerImpl.this.mDataChannelServiceConnectionCallback.onServiceConnected();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "On Service Disconnected ");
            ImsDataChannelServiceManagerImpl.this.mDataChannelService = null;
            ImsDataChannelServiceManagerImpl.this.mIsBound = false;
            if (ImsDataChannelServiceManagerImpl.this.mDataChannelServiceConnectionCallback != null) {
                ImsDataChannelServiceManagerImpl.this.mDataChannelServiceConnectionCallback.onServiceDisconnected();
            }
        }
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager
    public void connectImsDataChannelService(ImsDataChannelServiceConnectionCallback imsDataChannelServiceConnectionCallback) {
        Log.d("datachannellib:ImsDataChannelServiceManagerImpl", "Creating IMS Data Channel Service, if not started start");
        if (this.mContext == null) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "mContext is null, please invoke initialise method first ");
            return;
        }
        if (!this.mIsBound) {
            this.mDataChannelServiceConnectionCallback = imsDataChannelServiceConnectionCallback;
            Intent intent = new Intent(this.mContext, (Class<?>) IImsDataChannelService.class);
            intent.setComponent(new ComponentName("vendor.qti.imsdatachannel", "vendor.qti.imsdatachannel.ImsDataChannelService"));
            ImsDataChannelManagerServiceConnection imsDataChannelManagerServiceConnection = new ImsDataChannelManagerServiceConnection();
            this.mServiceConnection = imsDataChannelManagerServiceConnection;
            this.mIsBound = this.mContext.bindService(intent, imsDataChannelManagerServiceConnection, 1);
            return;
        }
        Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "Service is already bound");
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager
    public void getAvailability(final int i, ImsDataChannelServiceAvailabilityCallback imsDataChannelServiceAvailabilityCallback) {
        Log.d("datachannellib:ImsDataChannelServiceManagerImpl", "getAvailability slotId =" + i);
        if (!this.mIsBound || this.mServiceConnection == null) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "Either service bind failed or service is not created");
            return;
        }
        if (i < 0 || i > 1) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", " slotId is incorrect");
            return;
        }
        ImsDcServiceAvailabilityCallback[] imsDcServiceAvailabilityCallbackArr = this.mServiceAvailabilityCb;
        if (imsDcServiceAvailabilityCallbackArr[i] == null) {
            imsDcServiceAvailabilityCallbackArr[i] = new ImsDcServiceAvailabilityCallback(i, imsDataChannelServiceAvailabilityCallback);
        } else {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", " mServiceAvailability for this slot is already created.. This should not happen");
        }
        this.mDataChannelMgrExecutor.execute(new Runnable() { // from class: vendor.qti.imsdatachannel.client.ImsDataChannelServiceManagerImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getAvailability$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAvailability$0(int i) {
        try {
            this.mDataChannelService.getAvailability(i, this.mServiceAvailabilityCb[i]);
        } catch (RemoteException e) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "getAvailability remote exception");
            e.printStackTrace();
        }
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager
    public ImsDataChannelTransport createDataChannelTransport(final int i, final String str, ImsDataChannelEventListener imsDataChannelEventListener) {
        Log.d("datachannellib:ImsDataChannelServiceManagerImpl", "createDataChannelTransport slotId =" + i + "callId" + str);
        if (!this.mIsBound || this.mServiceConnection == null) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "Either service bind failed or service is not created");
            return null;
        }
        for (ImsDataChannelTransportWrapper imsDataChannelTransportWrapper : this.dataChannelTransportList) {
            if (imsDataChannelTransportWrapper.equals(i, str)) {
                Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "createDataChannelTransport: Object already exists ");
                return imsDataChannelTransportWrapper;
            }
        }
        this.transportObj = new ImsDataChannelTransportWrapper(this.mDataChannelMgrExecutor, this.mCbExecutor, i, str, imsDataChannelEventListener);
        this.mDataChannelMgrExecutor.execute(new Runnable() { // from class: vendor.qti.imsdatachannel.client.ImsDataChannelServiceManagerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createDataChannelTransport$1(i, str);
            }
        });
        return this.transportObj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDataChannelTransport$1(int i, String str) {
        try {
            IImsDataChannelTransport iImsDataChannelTransportCreateDataChannelTransport = this.mDataChannelService.createDataChannelTransport(i, str, this.transportObj.getDataChannelEventListener());
            this.transportBinderObj = iImsDataChannelTransportCreateDataChannelTransport;
            if (iImsDataChannelTransportCreateDataChannelTransport != null) {
                this.transportObj.setDataChannelTransportBinderInstance(iImsDataChannelTransportCreateDataChannelTransport);
                this.dataChannelTransportList.add(this.transportObj);
            }
        } catch (RemoteException e) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "createDataChannelTransport remote exception");
            this.transportBinderObj = null;
            this.transportObj = null;
            e.printStackTrace();
        }
    }

    @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager
    public void closeDataChannelTransport(ImsDataChannelTransport imsDataChannelTransport) {
        Log.d("datachannellib:ImsDataChannelServiceManagerImpl", "closeDataChannelTransport");
        if (!this.mIsBound || this.mServiceConnection == null) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "Either service bind failed or service is not created");
            return;
        }
        final ImsDataChannelTransportWrapper imsDataChannelTransportWrapper = (ImsDataChannelTransportWrapper) imsDataChannelTransport;
        this.mDataChannelMgrExecutor.execute(new Runnable() { // from class: vendor.qti.imsdatachannel.client.ImsDataChannelServiceManagerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$closeDataChannelTransport$2(imsDataChannelTransportWrapper);
            }
        });
        if (this.dataChannelTransportList.contains(imsDataChannelTransportWrapper)) {
            List list = this.dataChannelTransportList;
            list.remove(list.indexOf(imsDataChannelTransportWrapper));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeDataChannelTransport$2(ImsDataChannelTransportWrapper imsDataChannelTransportWrapper) {
        try {
            this.mDataChannelService.closeDataChannelTransport(imsDataChannelTransportWrapper.getDataChannelTransportBinderInstance());
        } catch (RemoteException e) {
            Log.e("datachannellib:ImsDataChannelServiceManagerImpl", "closeDataChannelTransport remote exception");
            e.printStackTrace();
        }
    }
}

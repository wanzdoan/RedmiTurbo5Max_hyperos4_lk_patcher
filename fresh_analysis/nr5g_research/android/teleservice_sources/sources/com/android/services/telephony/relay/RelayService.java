package com.android.services.telephony.relay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RelayService extends Service implements RelayDeviceChangeCallback {
    private static final String TAG = "RelayService";
    private final RemoteCallbackList mDeviceChangeCallbacks = new RemoteCallbackList();
    private final Binder mBinder = new IRelayService.Stub() { // from class: com.android.services.telephony.relay.RelayService.1
        @Override // com.android.services.telephony.relay.IRelayService
        public List getRelayDeviceIds() throws RemoteException {
            if (RelayUtils.Relay.isRelaySide()) {
                RelayLog.logi(RelayService.TAG, "getRelayDeviceIds, pad");
                return new ArrayList();
            }
            List connectedServiceDevices = PhoneContinuityController.getInstance().getConnectedServiceDevices();
            RelayLog.logi(RelayService.TAG, "getRelayDeviceIds, " + connectedServiceDevices.size());
            return connectedServiceDevices;
        }

        @Override // com.android.services.telephony.relay.IRelayService
        public void registerRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
            RelayLog.logi(RelayService.TAG, "registerRelayDeviceChangeCallback");
            if (iDeviceChangeCallback != null) {
                RelayService.this.mDeviceChangeCallbacks.register(iDeviceChangeCallback);
            }
        }

        @Override // com.android.services.telephony.relay.IRelayService
        public void unregisterRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
            RelayLog.logi(RelayService.TAG, "unregisterRelayDeviceChangeCallback");
            if (iDeviceChangeCallback != null) {
                RelayService.this.mDeviceChangeCallbacks.unregister(iDeviceChangeCallback);
            }
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (!RelayUtils.Relay.isRelaySide()) {
            PhoneContinuityController.getInstance().setRelayDeviceChangeCallback(this);
        }
        RelayLog.logi(TAG, "onCreate");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        RelayLog.logi(TAG, "onDestroy");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        RelayLog.logi(TAG, "onBind");
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        RelayLog.logi(TAG, "onUnbind");
        return true;
    }

    @Override // com.android.services.telephony.relay.RelayDeviceChangeCallback
    public void onDeviceChange() {
        synchronized (this.mDeviceChangeCallbacks) {
            int iBeginBroadcast = this.mDeviceChangeCallbacks.beginBroadcast();
            int i = 0;
            while (true) {
                RemoteCallbackList remoteCallbackList = this.mDeviceChangeCallbacks;
                if (i < iBeginBroadcast) {
                    try {
                        ((IDeviceChangeCallback) remoteCallbackList.getBroadcastItem(i)).onDeviceChange();
                        RelayLog.logi(TAG, "onDeviceChange");
                    } catch (RemoteException unused) {
                        RelayLog.loge(TAG, "Callback failed");
                    }
                    i++;
                } else {
                    remoteCallbackList.finishBroadcast();
                }
                throw th;
            }
        }
    }
}

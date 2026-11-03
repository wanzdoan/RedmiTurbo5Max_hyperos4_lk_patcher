package com.android.services.telephony.relay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.services.telephony.relay.phonecall.MiPhoneContinuityController;
import com.android.services.telephony.relay.phonecall.PadContinuityController;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RelayStateService extends Service implements DeviceInRelayChangeCallback {
    private static final String TAG = "RelayStateService";
    private boolean mSupportDualPhoneRelayCall;
    private final RemoteCallbackList mDeviceChangeCallbacks = new RemoteCallbackList();
    private final Binder mBinder = new ISynergy.Stub() { // from class: com.android.services.telephony.relay.RelayStateService.1
        @Override // com.android.services.telephony.relay.ISynergy
        public List getSynergyDeviceList() throws RemoteException {
            String deviceInRelay;
            RelayLog.logi(RelayStateService.TAG, "getSynergyDeviceList");
            ArrayList arrayList = new ArrayList();
            String deviceInRelay2 = "";
            if (RelayUtils.Relay.isRelaySide()) {
                deviceInRelay = PadContinuityController.getInstance().getDeviceInRelay();
            } else {
                String deviceInRelay3 = RelayStateService.this.mSupportDualPhoneRelayCall ? MiPhoneContinuityController.getInstance().getDeviceInRelay() : "";
                deviceInRelay2 = PhoneContinuityController.getInstance().getDeviceInRelay();
                deviceInRelay = deviceInRelay3;
            }
            if (!TextUtils.isEmpty(deviceInRelay)) {
                arrayList.add(deviceInRelay);
                RelayLog.logi(RelayStateService.TAG, "getSynergyDeviceList, deviceId:" + deviceInRelay);
            }
            if (!TextUtils.isEmpty(deviceInRelay2)) {
                arrayList.add(deviceInRelay2);
                RelayLog.logi(RelayStateService.TAG, "getSynergyDeviceList, deviceId:" + deviceInRelay2);
            }
            return arrayList;
        }

        @Override // com.android.services.telephony.relay.ISynergy
        public void registerSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
            RelayLog.logi(RelayStateService.TAG, "registerSynergyDeviceChangeCallback");
            if (iDeviceChangeCallback != null) {
                RelayStateService.this.mDeviceChangeCallbacks.register(iDeviceChangeCallback);
            }
        }

        @Override // com.android.services.telephony.relay.ISynergy
        public void unregisterSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
            RelayLog.logi(RelayStateService.TAG, "unregisterSynergyDeviceChangeCallback");
            if (iDeviceChangeCallback != null) {
                RelayStateService.this.mDeviceChangeCallbacks.unregister(iDeviceChangeCallback);
            }
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        RelayUtils.System.setContext(this);
        this.mSupportDualPhoneRelayCall = RelayUtils.Relay.supportDualPhoneRelayCallFeature(getApplicationContext());
        if (RelayUtils.Relay.isRelaySide()) {
            PadContinuityController.getInstance().setDeviceInRelayChangeCallback(this);
        } else {
            PhoneContinuityController.getInstance().setDeviceInRelayChangeCallback(this);
            if (this.mSupportDualPhoneRelayCall) {
                MiPhoneContinuityController.getInstance().setDeviceInRelayChangeCallback(this);
            }
        }
        RelayLog.logi(TAG, "onCreate");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        RelayLog.logi(TAG, "onDestroy");
        this.mDeviceChangeCallbacks.kill();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        RelayLog.logi(TAG, "onBind");
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        RelayLog.logi(TAG, "onUnbind");
        if (RelayUtils.Relay.isRelaySide()) {
            PadContinuityController.getInstance().setDeviceInRelayChangeCallback(null);
            return true;
        }
        PhoneContinuityController.getInstance().setDeviceInRelayChangeCallback(null);
        if (!this.mSupportDualPhoneRelayCall) {
            return true;
        }
        MiPhoneContinuityController.getInstance().setDeviceInRelayChangeCallback(null);
        return true;
    }

    @Override // com.android.services.telephony.relay.DeviceInRelayChangeCallback
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

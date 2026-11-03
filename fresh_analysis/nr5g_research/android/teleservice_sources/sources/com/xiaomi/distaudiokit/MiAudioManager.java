package com.xiaomi.distaudiokit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.miui.audiomonitor.distaudio.IDistAudioCallback;
import com.miui.audiomonitor.distaudio.IDistAudioService;
import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;
import com.miui.audiomonitor.distaudio.data.DistAudioRoute;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class MiAudioManager {
    private final Context mContext;
    private CountDownLatch mCountDownLatch;
    private DistAudioCallbackImpl mDistAudioCallbackImpl;
    protected IDistAudioService mDistAudioService;
    private boolean isBound = false;
    private final ServiceConnection connection = new ServiceConnection() { // from class: com.xiaomi.distaudiokit.MiAudioManager.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MiAudioManager", "onServiceConnected: service have connected");
            MiAudioManager.this.mDistAudioService = IDistAudioService.Stub.asInterface(iBinder);
            MiAudioManager.this.mCountDownLatch.countDown();
            MiAudioManager.this.isBound = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("MiAudioManager", "onServiceDisconnected");
            MiAudioManager miAudioManager = MiAudioManager.this;
            miAudioManager.mDistAudioService = null;
            miAudioManager.isBound = false;
        }
    };

    public MiAudioManager(Context context) {
        Log.d("MiAudioManager", "MiAudioManager: 23.07.11");
        this.mContext = context;
        this.mDistAudioCallbackImpl = new DistAudioCallbackImpl();
        this.mDistAudioService = bindAndWaitForConnectedService();
    }

    private IDistAudioService bindAndWaitForConnectedService() {
        String str;
        Log.d("MiAudioManager", "bindAndWaitForConnectedService: ");
        Intent intent = new Intent(RelayUtils.Relay.AUDIO_SERVICE_ACTION);
        intent.setPackage(RelayUtils.Relay.AUDIO_PACKAGE_NAME);
        this.mCountDownLatch = new CountDownLatch(1);
        Log.d("MiAudioManager", "bind service ...");
        this.mContext.getApplicationContext().bindService(intent, 1, new ProfileInstallReceiver$$ExternalSyntheticLambda0(), this.connection);
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            Log.d("MiAudioManager", "bindAndWaitForConnectedService: wait bind service ...");
            boolean zAwait = this.mCountDownLatch.await(4000L, TimeUnit.MILLISECONDS);
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            if (zAwait) {
                str = "bind service time: " + jCurrentTimeMillis2;
            } else {
                str = "bind service timeout....";
            }
            Log.d("MiAudioManager", "bindAndWaitForConnectedService: ".concat(str));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.mDistAudioService;
    }

    public void connectDistAudioDevice(DistAudioRoute distAudioRoute) {
        IDistAudioService iDistAudioService = this.mDistAudioService;
        if (iDistAudioService == null) {
            Log.d("MiAudioManager", "connectDistAudioDevice: no service");
            return;
        }
        try {
            iDistAudioService.connectDistAudioDevice(distAudioRoute);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void disconnectDistAudioDevice(DistAudioRoute distAudioRoute) {
        IDistAudioService iDistAudioService = this.mDistAudioService;
        if (iDistAudioService == null) {
            Log.d("MiAudioManager", "disconnectDistAudioDevice: no service");
            return;
        }
        try {
            iDistAudioService.disconnectDistAudioDevice(distAudioRoute);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List getDistAudioDevices() {
        IDistAudioService iDistAudioService = this.mDistAudioService;
        if (iDistAudioService != null) {
            try {
                return iDistAudioService.getDistAudioDevices();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Log.d("MiAudioManager", "getDistAudioDevices: no service");
        return null;
    }

    public DistAudioRoute getCurDistAudioRoute() {
        IDistAudioService iDistAudioService = this.mDistAudioService;
        if (iDistAudioService != null) {
            try {
                return iDistAudioService.getCurDistAudioRoute();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Log.d("MiAudioManager", "getCurDistAudioRoute: no service");
        return null;
    }

    public void registerDistAudioCallback(String str, DistAudioCallback distAudioCallback) {
        Log.d("MiAudioManager", "registerDistAudioCallback: ");
        if (this.mDistAudioService == null) {
            Log.d("MiAudioManager", "registerDistAudioCallback: no service");
            return;
        }
        try {
            this.mDistAudioCallbackImpl.setDistAudioCallback(distAudioCallback);
            this.mDistAudioService.registerDistAudioCallback(str, this.mDistAudioCallbackImpl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterDistAudioCallback(String str) {
        Log.d("MiAudioManager", "unregisterDistAudioCallback: ");
        try {
            if (this.mDistAudioService == null) {
                Log.d("MiAudioManager", "unregisterDistAudioCallback: no service");
            } else {
                this.mDistAudioCallbackImpl.setDistAudioCallback(null);
                this.mDistAudioService.unregisterDistAudioCallback(str);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        Log.d("MiAudioManager", "release: ");
        if (this.isBound) {
            Log.d("MiAudioManager", "release: unbindService");
            this.mContext.getApplicationContext().unbindService(this.connection);
            this.isBound = false;
        }
    }

    class DistAudioCallbackImpl extends IDistAudioCallback.Stub {
        private DistAudioCallback mDistAudioCallback = null;

        public DistAudioCallbackImpl() {
            Log.d("MiAudioManager", "DistAudioCallbackImpl: constructor");
        }

        public void setDistAudioCallback(DistAudioCallback distAudioCallback) {
            Log.d("MiAudioManager", "setDistAudioCallback: ");
            if (distAudioCallback == null) {
                Log.d("MiAudioManager", "setDistAudioCallback: null callback");
            }
            this.mDistAudioCallback = distAudioCallback;
        }

        @Override // com.miui.audiomonitor.distaudio.IDistAudioCallback
        public void onDistAudioDeviceAdded(DistAudioDeviceInfo distAudioDeviceInfo) {
            Log.i("MiAudioManager", "onDistAudioDeviceAdded, " + distAudioDeviceInfo.getDeviceName());
            DistAudioCallback distAudioCallback = this.mDistAudioCallback;
            if (distAudioCallback == null) {
                Log.e("MiAudioManager", "onDistAudioDeviceAdded error, null callback");
            } else {
                distAudioCallback.onDistAudioDeviceAdded(distAudioDeviceInfo);
            }
        }

        @Override // com.miui.audiomonitor.distaudio.IDistAudioCallback
        public void onDistAudioDeviceRemoved(DistAudioDeviceInfo distAudioDeviceInfo) {
            Log.d("MiAudioManager", "onDistAudioDeviceRemoved, " + distAudioDeviceInfo.getDeviceName());
            DistAudioCallback distAudioCallback = this.mDistAudioCallback;
            if (distAudioCallback == null) {
                Log.e("MiAudioManager", "onDistAudioDeviceRemoved error, null callback");
            } else {
                distAudioCallback.onDistAudioDeviceRemoved(distAudioDeviceInfo);
            }
        }

        @Override // com.miui.audiomonitor.distaudio.IDistAudioCallback
        public void onConnectionStatus(DistAudioDeviceInfo distAudioDeviceInfo, int i, String str) {
            Log.d("MiAudioManager", "onConnectionStatus, " + distAudioDeviceInfo.getDeviceName() + " code " + i + " msg " + str);
            DistAudioCallback distAudioCallback = this.mDistAudioCallback;
            if (distAudioCallback == null) {
                Log.e("MiAudioManager", "onConnectionStatus error, null callback");
            } else {
                distAudioCallback.onConnectionStatus(distAudioDeviceInfo, i, str);
            }
        }
    }
}

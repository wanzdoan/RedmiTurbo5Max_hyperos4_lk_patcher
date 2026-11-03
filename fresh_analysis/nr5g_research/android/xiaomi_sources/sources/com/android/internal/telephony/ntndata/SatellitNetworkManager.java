package com.android.internal.telephony.ntndata;

import android.content.Context;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class SatellitNetworkManager {
    protected static final boolean DBG = true;
    protected static final int EVENT_SATELLITE_SWITCH = 1;
    private static final int REQUEST_LOG_SIZE = 256;
    private static final int SATELLITE_OFF = 0;
    private static final int SATELLITE_ON = 1;
    private static final String SATELLITE_STATE = "satellite_state";
    private Context mContext;
    protected Handler mInternalHandler;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    protected final Phone mPhone;
    ContentObserver mSatelliteObserver;
    protected String LOG_TAG = "SatelliteNetworkManager";
    private final LocalLog mLocalLog = new LocalLog(256);
    protected ConnectivityManager mConnectivityManager = getConnectivityManager();

    public SatellitNetworkManager(Looper looper, Phone phone) {
        this.mSatelliteObserver = new ContentObserver(this.mInternalHandler) { // from class: com.android.internal.telephony.ntndata.SatellitNetworkManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                SatellitNetworkManager.this.logl("SatelliteChange");
                SatellitNetworkManager.this.mInternalHandler.sendEmptyMessage(1);
            }
        };
        this.mPhone = phone;
        this.mContext = phone.getContext();
        this.mInternalHandler = new InternalHandler(looper);
        registerListener();
    }

    public void registerListener() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(SATELLITE_STATE), false, this.mSatelliteObserver);
    }

    protected class InternalHandler extends Handler {
        protected InternalHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    boolean zIsSatelliteEnabled = SatellitNetworkManager.this.isSatelliteEnabled();
                    SatellitNetworkManager satellitNetworkManager = SatellitNetworkManager.this;
                    if (zIsSatelliteEnabled) {
                        satellitNetworkManager.logl("Satellite is enabled");
                        SatellitNetworkManager.this.startSatelliteNewNetworkRequest();
                    } else {
                        satellitNetworkManager.logl("Satellite is disabled");
                        SatellitNetworkManager.this.releaseSatelliteNetworkRequest();
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSatelliteNewNetworkRequest() {
        this.mNetworkCallback = new NetworkRequestCallback();
        NetworkRequest networkRequest = new NetworkRequest.Builder().addTransportType(10).addCapability(12).addCapability(28).removeCapability(13).removeCapability(37).build();
        this.mConnectivityManager.requestNetwork(networkRequest, this.mNetworkCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseSatelliteNetworkRequest() {
        if (this.mNetworkCallback != null) {
            try {
                this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            } catch (IllegalArgumentException e) {
                log("Unregister network callback exception" + e);
            }
        }
        resetStatus();
    }

    private void resetStatus() {
        this.mNetworkCallback = null;
    }

    private class NetworkRequestCallback extends ConnectivityManager.NetworkCallback {
        private NetworkRequestCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            SatellitNetworkManager.this.log("NetworkCallbackListener.onAvailable: network=" + network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            super.onLost(network);
            SatellitNetworkManager.this.log("NetworkCallbackListener.onLost: network=" + network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            super.onUnavailable();
            SatellitNetworkManager.this.log("NetworkCallbackListener.onUnavailable");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSatelliteEnabled() {
        int isSatelliteEnabled = Settings.System.getInt(this.mContext.getContentResolver(), SATELLITE_STATE, 0);
        if (isSatelliteEnabled == 1) {
            return DBG;
        }
        return false;
    }

    private ConnectivityManager getConnectivityManager() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        }
        return this.mConnectivityManager;
    }

    protected void log(String s) {
        Rlog.d(this.LOG_TAG, s);
    }

    protected void logl(String s) {
        log(s);
        this.mLocalLog.log(s);
    }
}

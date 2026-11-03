package com.android.phone.dsda;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.PreciseDataConnectionState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.telephony.MiuiTelephony;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDualDataTracker {
    private static String LOG_TAG = "MiuiDualDataTracker";
    private ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private Network mNetwork;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private final Runnable mNetworkReleaseTask;
    private NetworkRequest mNetworkRequest;
    private int mPhoneId;
    private final Handler mReleaseHandler;
    private int mRequestCount;
    private boolean mWaitNetwork = false;
    private int mNetworkRequestFailCount = 0;

    public MiuiDualDataTracker(Context context, Phone phone) {
        this.mPhoneId = -1;
        log("MiuiDualDataTracker enter");
        this.mContext = context;
        this.mPhoneId = phone.getPhoneId();
        resetStatus();
        this.mConnectivityManager = getConnectivityManager();
        this.mReleaseHandler = new Handler(Looper.getMainLooper());
        this.mNetworkReleaseTask = new Runnable() { // from class: com.android.phone.dsda.MiuiDualDataTracker.1
            @Override // java.lang.Runnable
            public void run() {
                if (MiuiDualDataTracker.this.mWaitNetwork) {
                    MiuiDualDataTracker.this.log("No network response, timer expired");
                }
            }
        };
    }

    class NetworkRequestCallback extends ConnectivityManager.NetworkCallback {
        private NetworkRequestCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            MiuiDualDataTracker.this.log("NetworkCallbackListener.onAvailable: network=" + network);
            MiuiDualDataTracker.this.handleNetworkNotification(true, network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            super.onLost(network);
            MiuiDualDataTracker.this.log("NetworkCallbackListener.onLost: network=" + network);
            MiuiDualDataTracker.this.handleNetworkNotification(false, network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            super.onUnavailable();
            MiuiDualDataTracker.this.log("NetworkCallbackListener.onUnavailable");
            MiuiDualDataTracker.this.handleNetworkNotification(false, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkNotification(boolean z, Network network) {
        log("handleNetworkNotification, result = " + z + " ,for network " + network);
        if (!z) {
            this.mNetworkRequestFailCount++;
            releaseNetworkRequest();
            int viceSlotId = MiuiDualDataController.getViceSlotId();
            if (this.mNetworkRequestFailCount <= 3 && this.mPhoneId == viceSlotId) {
                requestNetworkRequest();
            }
        }
        if (!z || network == null) {
            return;
        }
        this.mNetworkRequestFailCount = 0;
        this.mNetwork = network;
        this.mWaitNetwork = false;
    }

    public void requestNetworkRequest() {
        this.mReleaseHandler.removeCallbacks(this.mNetworkReleaseTask);
        log("mNetwork: " + this.mNetwork + ", mNetworkCallback: " + this.mNetworkCallback);
        SystemProperties.set("ril.multisim.dualdata_status", "1");
        updateDppdSlotId(this.mPhoneId);
        notifyNddsDataConnection();
        if (this.mNetwork != null) {
            return;
        }
        if (this.mNetworkCallback == null) {
            this.mRequestCount++;
            startNewNetworkRequest();
        }
        this.mReleaseHandler.postDelayed(this.mNetworkReleaseTask, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void startNewNetworkRequest() {
        log("startNewNetworkRequest, mRequestCount: " + this.mRequestCount);
        this.mWaitNetwork = true;
        this.mNetworkCallback = new NetworkRequestCallback();
        NetworkRequest networkRequestBuild = new NetworkRequest.Builder().addTransportType(0).addCapability(12).setNetworkSpecifier(String.valueOf(SubscriptionManager.getDefault().getSubscriptionIdForSlot(this.mPhoneId))).build();
        this.mNetworkRequest = networkRequestBuild;
        this.mConnectivityManager.requestNetwork(networkRequestBuild, this.mNetworkCallback);
    }

    public void releaseNetworkRequest() {
        int i = SystemProperties.getInt("ril.multisim.dualdata_phoneid", -1);
        log("releaseNetworkRequest: mRequestCount = " + this.mRequestCount + ", dppdSlotId = " + i + ", mPhoneId = " + this.mPhoneId);
        int i2 = this.mRequestCount;
        if (i2 == 0 && i != -1 && i != this.mPhoneId) {
            SystemProperties.set("ril.multisim.dualdata_status", "2");
            updateDppdSlotId(-1);
        } else if (i2 > 0) {
            SystemProperties.set("ril.multisim.dualdata_status", "2");
            notifyNddsDataConnection();
            this.mRequestCount--;
            releaseRequest(this.mNetworkCallback);
            updateDppdSlotId(-1);
        }
    }

    private void releaseRequest(ConnectivityManager.NetworkCallback networkCallback) {
        log("releaseRequest");
        if (networkCallback != null) {
            try {
                this.mConnectivityManager.unregisterNetworkCallback(networkCallback);
            } catch (IllegalArgumentException e) {
                log("Unregister network callback exception" + e);
            }
        }
        resetStatus();
    }

    private void resetStatus() {
        this.mNetworkCallback = null;
        this.mNetwork = null;
        this.mRequestCount = 0;
    }

    private ConnectivityManager getConnectivityManager() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        }
        return this.mConnectivityManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d(LOG_TAG + "[" + this.mPhoneId + "] ", str);
    }

    private void notifyNddsDataConnection() {
        DataNetwork dataNetworkFindActiveInternetDataNetwork;
        if (MiuiDualDataController.getViceSlotId() != this.mPhoneId) {
            return;
        }
        log("notifyNddsDataConnection");
        Phone phone = PhoneFactory.getPhone(this.mPhoneId);
        if (phone == null || (dataNetworkFindActiveInternetDataNetwork = ((MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, this.mPhoneId)).findActiveInternetDataNetwork()) == null || phone.getServiceState() == null) {
            return;
        }
        phone.notifyDataConnection(new PreciseDataConnectionState.Builder().setTransportType(dataNetworkFindActiveInternetDataNetwork.getTransport()).setState(-1).setApnSetting(dataNetworkFindActiveInternetDataNetwork.getDataProfile().getApnSetting()).setNetworkType(phone.getServiceState().getDataNetworkType()).setLinkProperties(dataNetworkFindActiveInternetDataNetwork.getLinkProperties()).build());
    }

    private void updateDppdSlotId(int i) {
        if (SystemProperties.getInt("ril.multisim.dualdata_phoneid", -1) != i) {
            SystemProperties.set("ril.multisim.dualdata_phoneid", String.valueOf(i));
        }
    }
}

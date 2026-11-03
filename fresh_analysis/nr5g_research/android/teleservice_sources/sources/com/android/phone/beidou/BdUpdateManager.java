package com.android.phone.beidou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.ot.pubsub.util.v;

/* JADX INFO: loaded from: classes.dex */
public class BdUpdateManager {
    public static String TAG = "BdUpdateManager";
    private static BdUpdateManager mInstance;
    private BdStatusListener mBdStatusListener;
    protected BdUpdateManagerReceiver mBdUpdateManagerReceiver;
    protected ConnectivityManager mConnectivityManager;
    private Context mContext;
    private Handler mHandler;
    public static final long UPDATE_KEY_INTERVAL = v.b;
    public static final long UPDATE_EPH_INTERVAL = v.a;
    public static final long DELAY_RETRY_TIMER = 1800000;
    ConnectivityManager.NetworkCallback mUpdateKeyAndSyncMailNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.beidou.BdUpdateManager.1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            BdUpdateManager.logi("mUpdateKeyAndSyncMailNetworkCallback.onAvailable: network=" + network);
            BdUpdateManager.this.unregisterNetworkCallback(this);
            BdUpdateManager.this.updateKeyAndSyncMail();
        }
    };
    ConnectivityManager.NetworkCallback mUpdateKeyNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.beidou.BdUpdateManager.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            BdUpdateManager.logi("mUpdateKeyNetworkCallback.onAvailable: network=" + network);
            BdUpdateManager.this.unregisterNetworkCallback(this);
            BdUpdateManager.this.updateAuthKey();
        }
    };
    ConnectivityManager.NetworkCallback mUpdateEphNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.beidou.BdUpdateManager.3
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            BdUpdateManager.logi("mUpdateEphNetworkCallback.onAvailable: network=" + network);
            BdUpdateManager.this.unregisterNetworkCallback(this);
            BdUpdateManager.this.updateEphData();
        }
    };

    private BdUpdateManager() {
        this.mBdStatusListener = null;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mConnectivityManager = (ConnectivityManager) phoneGlobals.getSystemService("connectivity");
        this.mHandler = new BdUpdateManagerHandler(Looper.getMainLooper());
        this.mBdUpdateManagerReceiver = new BdUpdateManagerReceiver();
        this.mBdStatusListener = new BdStatusListener();
        registerBroadcast();
        BdSmsController.registerBdStatusListener(this.mBdStatusListener);
    }

    public static void init() {
        if (mInstance == null) {
            mInstance = new BdUpdateManager();
        }
    }

    public static BdUpdateManager getInstance() {
        if (mInstance == null) {
            init();
        }
        return mInstance;
    }

    public void cancelAuthKeyMonitor() {
        try {
            this.mHandler.removeMessages(0);
            unregisterNetworkCallback(this.mUpdateEphNetworkCallback);
        } catch (Exception e) {
            loge("disableBdMonitor: " + e.getMessage());
        }
    }

    public void cancelEphMonitor() {
        try {
            this.mHandler.removeMessages(1);
            unregisterNetworkCallback(this.mUpdateEphNetworkCallback);
        } catch (Exception e) {
            loge("disableBdMonitor: " + e.getMessage());
        }
    }

    public void cancelMonitor() {
        cancelAuthKeyMonitor();
        cancelEphMonitor();
        try {
            this.mHandler.removeMessages(2);
            unregisterNetworkCallback(this.mUpdateKeyNetworkCallback);
        } catch (Exception e) {
            loge("disableBdMonitor: " + e.getMessage());
        }
    }

    public void updateKeyAndSyncMail() {
        if (MiuiPhoneUtils.isConnectedToInternet(this.mContext)) {
            BdSmsController.getInstance().updateKeyAndSyncMail();
        } else {
            loge("updateKeyAndSyncMail: No network");
            registerNetworkCallback(this.mUpdateKeyAndSyncMailNetworkCallback);
        }
    }

    public void updateAuthKey() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long lastUpdateAuthKey = getLastUpdateAuthKey();
        if (lastUpdateAuthKey == -1 || jCurrentTimeMillis - lastUpdateAuthKey > UPDATE_KEY_INTERVAL) {
            if (MiuiPhoneUtils.isConnectedToInternet(this.mContext)) {
                BdSmsController.getInstance().updateAuthKey();
                return;
            } else {
                loge("updateAuthKey: No network");
                registerNetworkCallback(this.mUpdateKeyNetworkCallback);
                return;
            }
        }
        logi("updateAuthKey : Suspend updates");
    }

    public void updateEphData() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long lastUpdateEph = getLastUpdateEph();
        if (lastUpdateEph == -1 || jCurrentTimeMillis - lastUpdateEph > UPDATE_EPH_INTERVAL || !BdSmsController.getInstance().checkEphData()) {
            if (MiuiPhoneUtils.isConnectedToInternet(this.mContext)) {
                BdSmsController.getInstance().updateEphData();
                return;
            } else {
                loge("updateEphData: No network");
                registerNetworkCallback(this.mUpdateEphNetworkCallback);
                return;
            }
        }
        logi("updateEphData : Suspend updates");
    }

    private void registerNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        if (this.mConnectivityManager != null) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(1);
            builder.addTransportType(0);
            builder.addCapability(12);
            builder.addCapability(16);
            this.mConnectivityManager.registerNetworkCallback(builder.build(), networkCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void registerBroadcast() {
        if (this.mBdUpdateManagerReceiver == null) {
            this.mBdUpdateManagerReceiver = new BdUpdateManagerReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_BD_SWITCH_STATE_CHANGE");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiver(this.mBdUpdateManagerReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLastUpdateAuthKey(long j) {
        logi("setLastUpdateAuthKey : lastUpdateAuthKey = " + j);
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putLong("pref_set_last_update_auth_key", j).apply();
    }

    private long getLastUpdateAuthKey() {
        long j = PreferenceManager.getDefaultSharedPreferences(this.mContext).getLong("pref_set_last_update_auth_key", -1L);
        logi("getLastUpdateAuthKey : lastUpdateAuthKey = " + j);
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLastUpdateEph(long j) {
        logi("setLastUpdateEph : lastUpdateEph = " + j);
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putLong("pref_set_last_update_eph", j).apply();
    }

    private long getLastUpdateEph() {
        long j = PreferenceManager.getDefaultSharedPreferences(this.mContext).getLong("pref_set_last_update_eph", -1L);
        logi("getLastUpdateEph : lastUpdateEph = " + j);
        return j;
    }

    protected static void logi(String str) {
        Log.i(TAG, str);
    }

    protected static void loge(String str) {
        Log.e(TAG, str);
    }

    protected static void logd(String str) {
        Log.d(TAG, str);
    }

    final class BdUpdateManagerReceiver extends BroadcastReceiver {
        private BdUpdateManagerReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BdUpdateManager.logi("onReceive action " + action);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                if (BdSmsUtils.getBdSwitchState() && "LOADED".equals(stringExtra)) {
                    BdUpdateManager.this.updateKeyAndSyncMail();
                    BdUpdateManager.this.updateEphData();
                    return;
                }
                return;
            }
            if ("miui.intent.action.ACTION_BD_SWITCH_STATE_CHANGE".equals(action)) {
                if (BdSmsUtils.getBdSwitchState()) {
                    return;
                }
                BdUpdateManager.this.cancelMonitor();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                BdUpdateManager.logd("bluetooth state = " + intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE));
            }
        }
    }

    class BdStatusListener implements BdSmsController.IBdStatusListener {
        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSysStatus(int i, int i2) {
        }

        BdStatusListener() {
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdServiceConnected() {
            Log.d(BdUpdateManager.TAG, "notifyBdServiceConnected");
            BdUpdateManager.this.updateKeyAndSyncMail();
            BdUpdateManager.this.updateEphData();
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdEphStatus(boolean z, int i, int i2) {
            Log.d(BdUpdateManager.TAG, "notifyBdEphStatus");
            if (z) {
                return;
            }
            BdUpdateManager.this.updateEphData();
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSatelliteStatus(int i, int i2, int i3, int i4, int i5) {
            Log.d(BdUpdateManager.TAG, "notifyBdSatelliteStatus");
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdSendSmsStatus(int i, int i2, int i3, int i4) {
            Log.d(BdUpdateManager.TAG, "notifyBdSendSmsStatus");
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdReceiveSmsStatus(boolean z, int i, String str, String str2) {
            Log.d(BdUpdateManager.TAG, "notifyBdReceiveSmsStatus");
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyBdServiceStatus(boolean z, int i) {
            Log.d(BdUpdateManager.TAG, "notifyBdServiceStatus = " + z);
            if (z) {
                return;
            }
            BdUpdateManager.getInstance().updateKeyAndSyncMail();
            BdUpdateManager.loge("getBdServiceStatus fail, updateKeyAndSyncMail");
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateAuthKey(boolean z, int i) {
            Log.d(BdUpdateManager.TAG, "notifyUpdateAuthKey: result = " + z + ", cause = " + i);
            BdUpdateManager bdUpdateManager = BdUpdateManager.this;
            if (!z) {
                if (bdUpdateManager.mHandler.hasMessages(0)) {
                    return;
                }
                BdUpdateManager.this.mHandler.sendMessageDelayed(Message.obtain(BdUpdateManager.this.mHandler, 0), BdUpdateManager.DELAY_RETRY_TIMER);
                return;
            }
            bdUpdateManager.setLastUpdateAuthKey(System.currentTimeMillis());
            BdUpdateManager.this.cancelAuthKeyMonitor();
        }

        @Override // com.android.phone.beidou.BdSmsController.IBdStatusListener
        public void notifyUpdateEphData(boolean z, int i) {
            Log.d(BdUpdateManager.TAG, "notifyUpdateEphData: result = " + z + ", cause = " + i);
            BdUpdateManager bdUpdateManager = BdUpdateManager.this;
            if (!z) {
                if (bdUpdateManager.mHandler.hasMessages(1)) {
                    return;
                }
                BdUpdateManager.this.mHandler.sendMessageDelayed(Message.obtain(BdUpdateManager.this.mHandler, 1), BdUpdateManager.DELAY_RETRY_TIMER);
                return;
            }
            bdUpdateManager.setLastUpdateEph(System.currentTimeMillis());
            BdUpdateManager.this.cancelEphMonitor();
        }
    }

    class BdUpdateManagerHandler extends Handler {
        public BdUpdateManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BdUpdateManager.logi("BdUpdateManagerHandler handleMessage what = " + message.what);
            int i = message.what;
            if (i == 0) {
                BdUpdateManager.this.updateAuthKey();
            } else if (i == 1) {
                BdUpdateManager.this.updateEphData();
            } else {
                if (i != 2) {
                    return;
                }
                BdUpdateManager.this.updateKeyAndSyncMail();
            }
        }
    }
}

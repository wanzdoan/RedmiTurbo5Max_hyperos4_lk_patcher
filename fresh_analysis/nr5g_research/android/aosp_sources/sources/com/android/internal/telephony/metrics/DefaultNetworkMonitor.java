package com.android.internal.telephony.metrics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class DefaultNetworkMonitor extends Handler {
    private static final String TAG = "DefaultNetworkMonitor";
    private ConnectivityManager.NetworkCallback mDefaultNetworkCallback;
    private Network mNetwork;
    private NetworkCapabilities mNetworkCapabilities;

    private final class DefaultNetworkCallback extends ConnectivityManager.NetworkCallback {
        private DefaultNetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            DefaultNetworkMonitor.this.mNetwork = network;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (network == DefaultNetworkMonitor.this.mNetwork) {
                DefaultNetworkMonitor.this.mNetworkCapabilities = networkCapabilities;
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            DefaultNetworkMonitor.this.mNetwork = null;
            DefaultNetworkMonitor.this.mNetworkCapabilities = null;
        }
    }

    DefaultNetworkMonitor(Context context, FeatureFlags featureFlags) {
        super(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
        registerSystemDefaultNetworkCallback(context);
    }

    private void registerSystemDefaultNetworkCallback(Context context) {
        if (this.mDefaultNetworkCallback != null) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        if (connectivityManager != null) {
            DefaultNetworkCallback defaultNetworkCallback = new DefaultNetworkCallback();
            this.mDefaultNetworkCallback = defaultNetworkCallback;
            connectivityManager.registerSystemDefaultNetworkCallback(defaultNetworkCallback, this);
            return;
        }
        Rlog.e(TAG, "registerSystemDefaultNetworkCallback: ConnectivityManager is null!");
    }

    public NetworkCapabilities getNetworkCapabilities() {
        return this.mNetworkCapabilities;
    }
}

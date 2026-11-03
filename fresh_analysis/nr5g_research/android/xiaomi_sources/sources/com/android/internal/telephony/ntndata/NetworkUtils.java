package com.android.internal.telephony.ntndata;

import android.net.InterfaceConfiguration;
import android.os.INetworkManagementService;
import android.os.ServiceManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    private static INetworkManagementService mNMService;

    private NetworkUtils() {
    }

    public static InterfaceConfiguration getInterfaceConfig(String iface) {
        Log.d(TAG, "getInterfaceConfig: " + iface);
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
            }
            return mNMService.getInterfaceConfig(iface);
        } catch (Exception e) {
            Log.e(TAG, "getInterfaceConfig exception!", e);
            return null;
        }
    }

    public static void setInterfaceConfig(String iface, InterfaceConfiguration cfg) {
        Log.d(TAG, "setInterfaceConfig: " + iface);
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
            }
            mNMService.setInterfaceConfig(iface, cfg);
        } catch (Exception e) {
            Log.e(TAG, "setInterfaceConfig exception!", e);
        }
    }

    public static void setInterfaceUp(String iface) {
        Log.d(TAG, "setInterfaceUp: " + iface);
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
            }
            mNMService.setInterfaceUp(iface);
            Log.d(TAG, "setInterfaceUp: " + iface + " done");
        } catch (Exception e) {
            Log.e(TAG, "setInterfaceUp exception!", e);
        }
    }

    public static void setInterfaceDown(String iface) {
        Log.d(TAG, "setInterfaceDown: " + iface);
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
            }
            mNMService.setInterfaceDown(iface);
        } catch (Exception e) {
            Log.e(TAG, "setInterfaceDown exception!", e);
        }
    }

    public static void clearInterfaceAddresses(String iface) {
        Log.d(TAG, "clearInterfaceAddresses: " + iface);
        try {
            if (mNMService == null) {
                mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
            }
            mNMService.clearInterfaceAddresses(iface);
        } catch (Exception e) {
            Log.e(TAG, "clearInterfaceAddresses exception!", e);
        }
    }
}

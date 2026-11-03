package com.mediatek.internal.telephony;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothAdapterCompatible {
    private static final String TAG = "miui_BluetoothAdapterCompatible";

    public static void setSARLimit(int set) {
        Log.d(TAG, "MIUI setSARLimit: " + set);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.setSarLimit(set);
        }
    }

    public static boolean isNeeded() {
        return Build.IS_GLOBAL_BUILD;
    }
}

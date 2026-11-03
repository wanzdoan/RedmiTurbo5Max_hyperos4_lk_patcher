package com.mediatek.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSignalT1 {
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String ACTION_GPS_STATE_CHANGE = "com.android.phone.intent.action.gps_state_changed";
    private static final String ACTION_RECEIVER_CALL_STATE = "com.android.phone.intent.action.receive_call_state";
    private static final String ACTION_T1_STATE_CHANGED = "com.android.phone.intent.action.t1_state_changed";
    private static final String KERNEL_T1_GPIO_CTRL_FILE_PATH = "/proc/modem_t1_gpio_ctrl";
    private static final String KEY_GPS_STATUS = "KEY_GPS_STATUS";
    private static final int OPT_ENABLED = 1;
    private static final int T1_OPT = 1;
    private static final String TAG = "MEModuleSignalT1";
    private static final int VOLTE_CALL = 0;
    private static final int VONR_CALL = 1;
    private static final int WEAK_SIGNAL = 4;
    private static MEModuleSignalT1 mInstance = null;
    private static final int status_low_rsrp_call = 2;
    private static final int status_off = 0;
    private static final int status_on = 1;
    private Context mContext;
    private int gps_state = 0;
    private int wwan_weak = 0;
    private int data_wifi = 0;
    private int weak_call = 0;
    private int mIsFeatureEnable = 1;
    private String GPIO_GPS_PRIORITY = "001";
    private String GPIO_WIFI_PRIORITY = "010";
    private String GPIO_WWAN_PRIORITY = "000";
    private String last_state = "000";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.dataconnection.MEModuleSignalT1.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.d(MEModuleSignalT1.TAG, "mBroadcastReceiver, intent is null");
                return;
            }
            String action = intent.getAction();
            Log.d(MEModuleSignalT1.TAG, "BroadcastReceiver action: " + action);
            if (MEModuleSignalT1.ACTION_CONNECTIVITY_CHANGE.equals(action)) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) MEModuleSignalT1.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                    Log.d(MEModuleSignalT1.TAG, "Network is not available!");
                    MEModuleSignalT1.this.data_wifi = 0;
                } else {
                    Log.d(MEModuleSignalT1.TAG, "networkInfo.getTypeName(): " + activeNetworkInfo.getTypeName());
                    MEModuleSignalT1.this.data_wifi = activeNetworkInfo.getType() == 1 ? 1 : 0;
                }
            } else if (MEModuleSignalT1.ACTION_RECEIVER_CALL_STATE.equals(action)) {
                int intExtra = intent.getIntExtra("call_type", -1);
                int intExtra2 = intent.getIntExtra("opt_type", -1);
                int intExtra3 = intent.getIntExtra("opt_enabled", -1);
                Log.d(MEModuleSignalT1.TAG, "ACTION_CALL_STATE_CHANGED: " + intExtra);
                Log.d(MEModuleSignalT1.TAG, "option type: " + intExtra2);
                Log.d(MEModuleSignalT1.TAG, "option status: " + intExtra3);
                if ((intExtra == 0 || intExtra == 1) && intExtra2 == 1) {
                    MEModuleSignalT1.this.weak_call = intExtra3 == 1 ? 1 : 0;
                }
                if (intExtra == 4 && intExtra2 == 1) {
                    MEModuleSignalT1.this.wwan_weak = intExtra3 == 1 ? 1 : 0;
                }
            } else if (MEModuleSignalT1.ACTION_GPS_STATE_CHANGE.equals(action)) {
                MEModuleSignalT1.this.gps_state = "true".equals(intent.getStringExtra(MEModuleSignalT1.KEY_GPS_STATUS)) ? 1 : 0;
            } else if (MEModuleSignalT1.ACTION_T1_STATE_CHANGED.equals(action)) {
                MEModuleSignalT1.this.mIsFeatureEnable = intent.getIntExtra("state", -1) == 1 ? 1 : 0;
                if (MEModuleSignalT1.this.mIsFeatureEnable == 0) {
                    MEModuleSignalT1.this.setGPIO(MEModuleSignalT1.this.GPIO_WWAN_PRIORITY);
                }
            }
            MEModuleSignalT1.this.scene_judge();
        }
    };

    private MEModuleSignalT1(Context context) {
        Log.d(TAG, "MEModuleSignalT1 init...");
        if (1 == SystemProperties.getInt("ro.boot.factorybuild", 0)) {
            Log.d(TAG, "This device is factory build!!!, disable MEModuleSignalT1.");
            return;
        }
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECTIVITY_CHANGE);
        filter.addAction(ACTION_RECEIVER_CALL_STATE);
        filter.addAction(ACTION_GPS_STATE_CHANGE);
        filter.addAction(ACTION_T1_STATE_CHANGED);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        Log.d(TAG, "MEModuleSignalT1 init done");
    }

    public static MEModuleSignalT1 getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MEModuleSignalT1(context);
        }
        return mInstance;
    }

    public void scene_judge() {
        if (this.mIsFeatureEnable == 0) {
            return;
        }
        Log.d(TAG, "gps_state: " + this.gps_state + ", wwan_weak: " + this.wwan_weak + ", data_wifi: " + this.data_wifi + ", weak_call: " + this.weak_call);
        if (this.gps_state == 1 && this.wwan_weak == 0) {
            setGPIO(this.GPIO_GPS_PRIORITY);
            return;
        }
        if (this.gps_state == 0 && this.data_wifi == 1 && (this.wwan_weak == 0 || (this.wwan_weak == 1 && this.weak_call == 0))) {
            setGPIO(this.GPIO_WIFI_PRIORITY);
        } else {
            setGPIO(this.GPIO_WWAN_PRIORITY);
        }
    }

    public void setGPIO(String temp_str) {
        if (this.last_state.equals(temp_str)) {
            Log.d(TAG, "setGPIO skipped, since no changed!");
            return;
        }
        this.last_state = temp_str;
        File gpiof = new File(KERNEL_T1_GPIO_CTRL_FILE_PATH);
        if (!gpiof.exists()) {
            Log.d(TAG, "setGPIO failed as proc node does not exist!");
            return;
        }
        FileOutputStream fos = null;
        try {
            try {
                try {
                    fos = new FileOutputStream(gpiof);
                    fos.write(this.last_state.getBytes());
                } catch (Exception e) {
                    Log.e(TAG, "setGPIO errors");
                    e.printStackTrace();
                    if (fos != null) {
                    }
                    Log.d(TAG, "SetGPIO gpio string = " + this.last_state);
                }
                fos.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            Log.d(TAG, "SetGPIO gpio string = " + this.last_state);
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }
}

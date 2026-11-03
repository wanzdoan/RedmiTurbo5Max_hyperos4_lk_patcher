package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class MTKRFCloudManager {
    private static MTKRFCloudManager sInstance;
    private final Context mAppContext;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MTKRFCloudManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder("Broadcast received: ");
            sb.append(intent != null ? intent.getAction() : "null");
            Log.d("MTKRFCloudManager", sb.toString());
            if (intent != null && "com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED".equals(intent.getAction())) {
                Log.d("MTKRFCloudManager", "Processing TelephonyRFCloud broadcast");
                MTKRFCloudManager.this.processRFCloudData();
            }
        }
    };
    private boolean mRegistered;

    private MTKRFCloudManager(Context context) {
        this.mAppContext = context.getApplicationContext();
    }

    public static synchronized void init(Context context) {
        try {
            Log.d("MTKRFCloudManager", "MTKRFCloudManager.init() called");
            if (sInstance == null && context != null) {
                MTKRFCloudManager mTKRFCloudManager = new MTKRFCloudManager(context);
                sInstance = mTKRFCloudManager;
                mTKRFCloudManager.register();
                Log.d("MTKRFCloudManager", "MTKRFCloudManager initialized successfully");
            } else {
                Log.d("MTKRFCloudManager", "MTKRFCloudManager already initialized or context is null");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public static synchronized void initCloudDataOnBoot(Context context) {
        try {
            Log.d("MTKRFCloudManager", "MTKRFCloudManager.initCloudDataOnBoot() called");
            if (sInstance == null && context != null) {
                MTKRFCloudManager mTKRFCloudManager = new MTKRFCloudManager(context);
                sInstance = mTKRFCloudManager;
                mTKRFCloudManager.register();
                Log.d("MTKRFCloudManager", "MTKRFCloudManager instance created for boot initialization");
            }
            initCloudDataRetrieval();
        } catch (Throwable th) {
            throw th;
        }
    }

    private static void initCloudDataRetrieval() {
        try {
            Log.d("MTKRFCloudManager", "initCloudDataRetrieval: Starting RF config restoration from local storage");
            RFConfig rFConfigLoadRFConfigFromStorage = loadRFConfigFromStorage();
            if (rFConfigLoadRFConfigFromStorage != null && rFConfigLoadRFConfigFromStorage.isValid()) {
                Log.d("MTKRFCloudManager", "initCloudDataRetrieval: Successfully loaded RF config from storage -> status=" + rFConfigLoadRFConfigFromStorage.status + ", params1=" + rFConfigLoadRFConfigFromStorage.params1 + ", params2=" + rFConfigLoadRFConfigFromStorage.params2);
                if (applyRFActionStatus(rFConfigLoadRFConfigFromStorage.params1, rFConfigLoadRFConfigFromStorage.params2, String.valueOf(rFConfigLoadRFConfigFromStorage.status))) {
                    Log.i("MTKRFCloudManager", "initCloudDataRetrieval: Successfully restored RF config on boot");
                    return;
                } else {
                    Log.w("MTKRFCloudManager", "initCloudDataRetrieval: Failed to restore RF config on boot");
                    return;
                }
            }
            Log.w("MTKRFCloudManager", "initCloudDataRetrieval: No valid RF config found in local storage");
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "initCloudDataRetrieval: Exception occurred during RF config restoration: " + e);
        }
    }

    private void register() {
        if (this.mRegistered) {
            return;
        }
        registerBroadcastReceiver(createIntentFilter());
        this.mRegistered = true;
        Log.d("MTKRFCloudManager", "MTKRFCloudManager registered, waiting for broadcast");
    }

    private IntentFilter createIntentFilter() {
        return createFilter("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED");
    }

    private void registerBroadcastReceiver(IntentFilter intentFilter) {
        registerReceiver(this.mAppContext, this.mReceiver, intentFilter);
    }

    private static IntentFilter createFilter(String str) {
        return new IntentFilter(str);
    }

    private static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        context.registerReceiver(broadcastReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRFCloudData() {
        FiveGCloudController.FeatureCloudInfo featureCloudInfo = getFeatureCloudInfo();
        if (featureCloudInfo == null) {
            return;
        }
        logFeatureInfo(featureCloudInfo);
        if (isCloudDataSameAsLocal(featureCloudInfo)) {
            Log.d("MTKRFCloudManager", "processRFCloudData: Cloud data matches local storage, skipping duplicate processing");
        } else {
            applyRFCloudData(featureCloudInfo);
        }
    }

    private boolean isCloudDataSameAsLocal(FiveGCloudController.FeatureCloudInfo featureCloudInfo) {
        try {
            RFConfig rFConfigLoadRFConfigFromStorage = loadRFConfigFromStorage();
            if (rFConfigLoadRFConfigFromStorage != null && rFConfigLoadRFConfigFromStorage.isValid()) {
                int status = featureCloudInfo.getStatus();
                String params1 = featureCloudInfo.getParams1();
                String params2 = featureCloudInfo.getParams2();
                if (params1 == null) {
                    params1 = "";
                }
                if (params2 == null) {
                    params2 = "";
                }
                if (rFConfigLoadRFConfigFromStorage.params1 == null) {
                    rFConfigLoadRFConfigFromStorage.params1 = "";
                }
                if (rFConfigLoadRFConfigFromStorage.params2 == null) {
                    rFConfigLoadRFConfigFromStorage.params2 = "";
                }
                boolean z = status == rFConfigLoadRFConfigFromStorage.status && params1.equals(rFConfigLoadRFConfigFromStorage.params1) && params2.equals(rFConfigLoadRFConfigFromStorage.params2);
                Log.d("MTKRFCloudManager", "isCloudDataSameAsLocal: Comparison result=" + z);
                return z;
            }
            Log.d("MTKRFCloudManager", "isCloudDataSameAsLocal: No valid local config, cloud data will be processed");
            return false;
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "isCloudDataSameAsLocal: Exception occurred during comparison: " + e);
            return false;
        }
    }

    private FiveGCloudController.FeatureCloudInfo getFeatureCloudInfo() {
        return FiveGCloudController.getFeatureInfoByCloud("TelephonyRFCloud");
    }

    private void logFeatureInfo(FiveGCloudController.FeatureCloudInfo featureCloudInfo) {
        Log.d("MTKRFCloudManager", "TelephonyRFCloud changed -> featureType=" + featureCloudInfo.getFeatureType() + ", status=" + featureCloudInfo.getStatus() + ", params1=" + featureCloudInfo.getParams1() + ", params2=" + featureCloudInfo.getParams2());
    }

    private void applyRFCloudData(FiveGCloudController.FeatureCloudInfo featureCloudInfo) {
        try {
            Log.d("MTKRFCloudManager", "applyRFCloudData: Applying TelephonyRFCloud data to TelephonyRFAction");
            int status = featureCloudInfo.getStatus();
            if (status == 0) {
                if (applyRFDisableAction()) {
                    saveRFConfigToStorage(status, "", "");
                    Log.i("MTKRFCloudManager", "applyRFCloudData: RF disabled and config saved to storage");
                    return;
                } else {
                    Log.w("MTKRFCloudManager", "applyRFCloudData: Failed to disable TelephonyRFCloud");
                    return;
                }
            }
            if (status == 1) {
                Log.d("MTKRFCloudManager", "applyRFCloudData: TelephonyRFCloud status=" + status);
                String params1 = featureCloudInfo.getParams1();
                String params2 = featureCloudInfo.getParams2();
                if (params1 == null || params2 == null) {
                    Log.e("MTKRFCloudManager", "applyRFCloudData: Invalid parameters for enabled status - params1=" + params1 + ", params2=" + params2);
                    return;
                }
                if (applyRFActionStatus(params1, params2, String.valueOf(status))) {
                    saveRFConfigToStorage(status, params1, params2);
                    Log.i("MTKRFCloudManager", "applyRFCloudData: RF config applied and saved to storage - status=" + status + ", params1=" + params1 + ", params2=" + params2);
                    return;
                }
                Log.w("MTKRFCloudManager", "applyRFCloudData: Failed to apply TelephonyRFCloud data");
                return;
            }
            Log.e("MTKRFCloudManager", "applyRFCloudData: Invalid status value: " + status + ", expected 0 or 1");
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "applyRFCloudData: Exception occurred while applying RF cloud data: " + e);
        }
    }

    private static boolean applyRFDisableAction() {
        try {
            return new TelephonyRFAction().disableRFCloud();
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "applyRFDisableAction: Exception occurred: " + e);
            return false;
        }
    }

    private static Context getPhoneGlobalsInstance() {
        return PhoneGlobals.getInstance();
    }

    private static boolean applyRFActionStatus(String str, String str2, String str3) {
        return RFActionHelper.applyRFAction(str, str2, str3);
    }

    abstract class RFActionHelper {
        static boolean applyRFAction(String str, String str2, String str3) {
            try {
                return new TelephonyRFAction().applyRFCloudStatus(str, str2, str3);
            } catch (Exception e) {
                Log.e("MTKRFCloudManager", "RFActionHelper: Exception occurred: " + e);
                return false;
            }
        }
    }

    class RFConfig {
        boolean hasValidConfig;
        String params1;
        String params2;
        int status;

        RFConfig(int i, String str, String str2, boolean z) {
            this.status = i;
            this.params1 = str;
            this.params2 = str2;
            this.hasValidConfig = z;
        }

        boolean isValid() {
            if (!this.hasValidConfig) {
                return false;
            }
            int i = this.status;
            return i == 0 || i == 1;
        }
    }

    private static void saveRFConfigToStorage(int i, String str, String str2) {
        try {
            Context phoneGlobalsInstance = getPhoneGlobalsInstance();
            if (phoneGlobalsInstance == null) {
                Log.e("MTKRFCloudManager", "saveRFConfigToStorage: Context is null");
                return;
            }
            SharedPreferences.Editor editorEdit = phoneGlobalsInstance.getSharedPreferences("MTKRFCloudConfig", 0).edit();
            editorEdit.putInt("rf_cloud_status", i);
            editorEdit.putString("rf_cloud_params1", str != null ? str : "");
            editorEdit.putString("rf_cloud_params2", str2 != null ? str2 : "");
            editorEdit.putBoolean("rf_cloud_has_valid_config", true);
            if (!editorEdit.commit()) {
                Log.e("MTKRFCloudManager", "saveRFConfigToStorage: Failed to save RF config");
                return;
            }
            Log.d("MTKRFCloudManager", "saveRFConfigToStorage: RF config saved successfully - status=" + i + ", params1=" + str + ", params2=" + str2);
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "saveRFConfigToStorage: Exception occurred: " + e);
        }
    }

    private static RFConfig loadRFConfigFromStorage() {
        try {
            Context phoneGlobalsInstance = getPhoneGlobalsInstance();
            if (phoneGlobalsInstance == null) {
                Log.e("MTKRFCloudManager", "loadRFConfigFromStorage: Context is null");
                return null;
            }
            SharedPreferences sharedPreferences = phoneGlobalsInstance.getSharedPreferences("MTKRFCloudConfig", 0);
            boolean z = sharedPreferences.getBoolean("rf_cloud_has_valid_config", false);
            if (!z) {
                Log.d("MTKRFCloudManager", "loadRFConfigFromStorage: No valid config found in storage");
                return null;
            }
            int i = sharedPreferences.getInt("rf_cloud_status", -1);
            String string = sharedPreferences.getString("rf_cloud_params1", "");
            String string2 = sharedPreferences.getString("rf_cloud_params2", "");
            RFConfig rFConfig = new RFConfig(i, string, string2, z);
            Log.d("MTKRFCloudManager", "loadRFConfigFromStorage: RF config loaded from storage - status=" + i + ", params1=" + string + ", params2=" + string2);
            return rFConfig;
        } catch (Exception e) {
            Log.e("MTKRFCloudManager", "loadRFConfigFromStorage: Exception occurred: " + e);
            return null;
        }
    }
}

package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCloudCarrierConfigManager {
    private static String LOG_TAG = "MiuiCloudCarrierConfigManager";
    private static volatile MiuiCloudCarrierConfigManager sInstance;
    private CloudDataHandler mBaseHandler;
    private HashMap mCloudCarrierConfig = new HashMap();
    private Context mContext;

    public static void init() {
        if (sInstance == null) {
            synchronized (MiuiCloudCarrierConfigManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiCloudCarrierConfigManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static MiuiCloudCarrierConfigManager getInstance() {
        return sInstance;
    }

    private MiuiCloudCarrierConfigManager() {
        this.mContext = null;
        this.mBaseHandler = null;
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG);
        handlerThread.start();
        this.mBaseHandler = new CloudDataHandler(handlerThread.getLooper());
        Log.d(LOG_TAG, "init done");
    }

    class CloudDataHandler extends Handler {
        public CloudDataHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d(MiuiCloudCarrierConfigManager.LOG_TAG, "handle message what: " + message.what);
            if (message.what != 1) {
                return;
            }
            MiuiCloudCarrierConfigManager.this.cloudChangedInternal((String) message.obj, message.arg1);
        }
    }

    public void cloudChanged() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            String simOperator = PhoneProxy.getSimOperator(i);
            if (simOperator != null && this.mCloudCarrierConfig.get(simOperator) != null) {
                CloudDataHandler cloudDataHandler = this.mBaseHandler;
                cloudDataHandler.sendMessage(cloudDataHandler.obtainMessage(1, i, -1, simOperator));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cloudChangedInternal(String str, int i) {
        boolean zUpdateCloudConfig = updateCloudConfig(str, ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId(i)));
        Log.d(LOG_TAG, "cloudConfigChanged for operator: " + str + " needReloadConfig" + zUpdateCloudConfig);
        if (zUpdateCloudConfig) {
            PhoneProxy.updateConfigForPhoneId(this.mContext, i, "LOADED");
        }
    }

    private static int getSubId(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length < 1) {
            return -1;
        }
        return subId[0];
    }

    private synchronized boolean updateCloudConfig(String str, PersistableBundle persistableBundle) {
        HashMap featureCloudInfoMap = FiveGCloudController.getFeatureCloudInfoMap();
        if (featureCloudInfoMap == null) {
            return false;
        }
        boolean zPutValue = false;
        for (String str2 : featureCloudInfoMap.keySet()) {
            if (str2 != null && str2.contains(str)) {
                FiveGCloudController.FeatureCloudInfo featureCloudInfo = (FiveGCloudController.FeatureCloudInfo) featureCloudInfoMap.get(str2);
                String str3 = str2.split("-")[0];
                String params1 = featureCloudInfo.getParams1();
                String params2 = featureCloudInfo.getParams2();
                if (params1 != null && params2 != null && persistableBundle.containsKey(str3) && !params2.equals(persistableBundle.get(str3).toString())) {
                    zPutValue = putValue(str3, params1, params2, (PersistableBundle) this.mCloudCarrierConfig.get(str));
                }
            }
        }
        return zPutValue;
    }

    private boolean putValue(String str, String str2, String str3, PersistableBundle persistableBundle) {
        try {
            int iHashCode = str2.hashCode();
            if (iHashCode == -891985903) {
                if (str2.equals(KeyStringSettingItem.TYPE)) {
                    persistableBundle.putString(str, str3);
                    Log.d(LOG_TAG, "putValue cloudConfig[" + str + "," + str3 + "]");
                    return true;
                }
                Log.d(LOG_TAG, "putValue fail paramType: " + str2 + " is incorrect");
                return false;
            }
            if (iHashCode == 104431) {
                if (str2.equals("int")) {
                    persistableBundle.putInt(str, Integer.valueOf(str3).intValue());
                    Log.d(LOG_TAG, "putValue cloudConfig[" + str + "," + str3 + "]");
                    return true;
                }
                Log.d(LOG_TAG, "putValue fail paramType: " + str2 + " is incorrect");
                return false;
            }
            if (iHashCode == 64711720 && str2.equals("boolean")) {
                persistableBundle.putBoolean(str, Boolean.valueOf(str3).booleanValue());
                Log.d(LOG_TAG, "putValue cloudConfig[" + str + "," + str3 + "]");
                return true;
            }
            Log.d(LOG_TAG, "putValue fail paramType: " + str2 + " is incorrect");
            return false;
        } catch (Exception unused) {
            Log.d(LOG_TAG, "putValue fail paramValue: " + str3 + " is not " + str2);
            return false;
        }
    }

    public void syncCloudCarrierConfig(String str, PersistableBundle persistableBundle) {
        PersistableBundle persistableBundle2 = (PersistableBundle) this.mCloudCarrierConfig.get(str);
        if (persistableBundle2 == null) {
            persistableBundle2 = new PersistableBundle();
            this.mCloudCarrierConfig.put(str, persistableBundle2);
            updateCloudConfig(str, persistableBundle);
        }
        persistableBundle.putAll(persistableBundle2);
        Log.d(LOG_TAG, "syncCloudCarrierConfig operator + " + str + " configs: " + this.mCloudCarrierConfig.get(str));
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MiuiCloudCarrierConfigManager dump start------");
        synchronized (this.mCloudCarrierConfig) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                try {
                    String simOperator = PhoneProxy.getSimOperator(i);
                    printWriter.println("cloud config for " + simOperator + ":\n" + this.mCloudCarrierConfig.get(simOperator));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        printWriter.println("MiuiCloudCarrierConfigManager dump end------");
        printWriter.flush();
    }

    public PersistableBundle getCloudCarrierConfig(String str) {
        return (PersistableBundle) this.mCloudCarrierConfig.get(str);
    }
}

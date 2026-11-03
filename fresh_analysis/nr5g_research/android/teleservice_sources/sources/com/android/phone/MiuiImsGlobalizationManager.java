package com.android.phone;

import android.os.PersistableBundle;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsGlobalizationManager {
    private static String LOG_TAG = "MiuiImsGlobalizationManager";
    private static volatile MiuiImsGlobalizationManager sInstance;
    private HashMap mGlobalizationCarrierConfig = new HashMap();
    private MiuiCloudCarrierConfigManager mMiuiCloudCarrierConfigManager;

    public static void init() {
        if (sInstance == null) {
            synchronized (MiuiImsGlobalizationManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiImsGlobalizationManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static MiuiImsGlobalizationManager getInstance() {
        return sInstance;
    }

    public void updateConfigForVolteGlobalization(String str, PersistableBundle persistableBundle) {
        if (persistableBundle.getBoolean("carrier_volte_globalization_enable")) {
            MiuiCloudCarrierConfigManager miuiCloudCarrierConfigManager = MiuiCloudCarrierConfigManager.getInstance();
            this.mMiuiCloudCarrierConfigManager = miuiCloudCarrierConfigManager;
            PersistableBundle cloudCarrierConfig = miuiCloudCarrierConfigManager.getCloudCarrierConfig(str);
            if (cloudCarrierConfig != null && cloudCarrierConfig.containsKey("carrier_volte_available_bool") && !cloudCarrierConfig.getBoolean("carrier_volte_available_bool")) {
                Log.d(LOG_TAG, "carrier_volte_available_bool is false by cloudConfig");
                return;
            }
            if (!persistableBundle.getBoolean("carrier_volte_unavailable_bool") && !persistableBundle.getBoolean("carrier_volte_available_bool")) {
                try {
                    persistableBundle.putBoolean("carrier_volte_available_bool", true);
                    persistableBundle.putBoolean("enhanced_4g_lte_on_by_default_bool", false);
                    Log.d(LOG_TAG, "updateConfigForVolteGlobalization operator = " + str + "[carrier_volte_available_bool , true] and [enhanced_4g_lte_on_by_default_bool , false]");
                    updateGlobalizationDump(str, persistableBundle);
                } catch (Exception unused) {
                    Log.d(LOG_TAG, "updateConfigForVolteGlobalization fail");
                }
            }
            Log.d(LOG_TAG, "updateConfigForVolteGlobalization operator = " + str);
        }
    }

    public void updateGlobalizationDump(String str, PersistableBundle persistableBundle) {
        PersistableBundle persistableBundle2 = (PersistableBundle) this.mGlobalizationCarrierConfig.get(str);
        if (persistableBundle2 == null) {
            persistableBundle2 = new PersistableBundle();
        }
        persistableBundle2.putBoolean("carrier_volte_available_bool", persistableBundle.getBoolean("carrier_volte_available_bool"));
        persistableBundle2.putBoolean("enhanced_4g_lte_on_by_default_bool", persistableBundle.getBoolean("enhanced_4g_lte_on_by_default_bool"));
        this.mGlobalizationCarrierConfig.put(str, persistableBundle2);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MiuiImsGlobalizationManager dump start------");
        synchronized (this.mGlobalizationCarrierConfig) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                try {
                    String simOperator = PhoneProxy.getSimOperator(i);
                    printWriter.println("Globalization config for " + simOperator + ":\n" + this.mGlobalizationCarrierConfig.get(simOperator));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        printWriter.println("MiuiImsGlobalizationManager dump end------");
        printWriter.flush();
    }

    public HashMap getGlobalizationCarrierConfigMap() {
        return this.mGlobalizationCarrierConfig;
    }
}

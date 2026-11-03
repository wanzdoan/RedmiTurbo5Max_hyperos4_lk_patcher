package com.android.phone.satellite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.phone.ModuleDelegate;
import com.android.phone.SatelliteManager;
import com.android.phone.module.IUtilityModule;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteOTAManager {
    private static Context mContext;
    private static SatelliteOTAManager sInstance;
    private int mFlashState = 6;
    private final BroadcastReceiver mMiuiSatelliteOtaReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.SatelliteOTAManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                SatelliteOTAManager.loge("mMiuiSatelliteOtaReceiver received null intent or action");
                return;
            }
            SatelliteOTAManager.logd("mMiuiSatelliteOtaReceiver onReceive action = " + intent.getAction());
            if ("xiaomi.intent.action.ACTION_NTN_OTA_STATUS".equals(intent.getAction())) {
                byte[] byteArrayExtra = intent.getByteArrayExtra("BROADCAST_CONTEXT");
                if (byteArrayExtra == null || byteArrayExtra.length == 0) {
                    SatelliteOTAManager.logd("data is null.");
                    return;
                }
                int unused = SatelliteOTAManager.this.mFlashState;
                try {
                    int i = Integer.parseInt(String.valueOf((char) byteArrayExtra[0]));
                    if (i == 0 || i == 2 || i == 3 || i == 4) {
                        SatelliteOTAManager.this.mFlashState = 2;
                    } else {
                        SatelliteOTAManager.this.mFlashState = i;
                    }
                    int i2 = SatelliteOTAManager.this.mFlashState;
                    SatelliteOTAManager satelliteOTAManager = SatelliteOTAManager.this;
                    if (i2 == 6) {
                        satelliteOTAManager.unRegisterSatelliteOtaReceiver();
                    } else if (satelliteOTAManager.mFlashState == 5) {
                        SatelliteOTAManager.this.mSatelliteManager.getUIController().showSatelliteOtaNotification();
                    }
                    SatelliteOTAManager.logd("mMiuiSatelliteOtaReceiver onReceive action = " + SatelliteOTAManager.this.mFlashState);
                    return;
                } catch (NumberFormatException e) {
                    SatelliteOTAManager.loge("NumberFormatException: " + e);
                    return;
                }
            }
            if ("xiaomi.intent.action.ACTION_NTN_OTA_BIGDATA".equals(intent.getAction())) {
                SatelliteOTAManager.this.unRegisterSatelliteOtaReceiver();
                byte[] byteArrayExtra2 = intent.getByteArrayExtra("BROADCAST_CONTEXT");
                if (byteArrayExtra2 == null || byteArrayExtra2.length == 0) {
                    SatelliteOTAManager.logd("data is null.");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (byte b : byteArrayExtra2) {
                    if (b == 0) {
                        break;
                    }
                    sb.append((char) b);
                }
                String string = sb.toString();
                HashMap map = new HashMap();
                map.put("flashData", string);
                SatelliteOTAManager.logd("sendSatelliteOtaDotBroadcast." + map);
                ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordSatelliteOta(map);
            }
        }
    };
    private SatelliteManager mSatelliteManager;

    public static SatelliteOTAManager make(SatelliteManager satelliteManager, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteOTAManager(satelliteManager, context);
        }
        return sInstance;
    }

    public SatelliteOTAManager(SatelliteManager satelliteManager, Context context) {
        this.mSatelliteManager = satelliteManager;
        mContext = context;
        registerSatelliteOtaReceiver();
    }

    private void registerSatelliteOtaReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("xiaomi.intent.action.ACTION_NTN_OTA_STATUS");
        intentFilter.addAction("xiaomi.intent.action.ACTION_NTN_OTA_BIGDATA");
        mContext.registerReceiver(this.mMiuiSatelliteOtaReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterSatelliteOtaReceiver() {
        BroadcastReceiver broadcastReceiver = this.mMiuiSatelliteOtaReceiver;
        if (broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public int getFlashState() {
        return this.mFlashState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteOTAManager", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("SatelliteOTAManager", str);
    }
}

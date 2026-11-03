package com.android.phone.labtest;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.android.phone.PhoneGlobals;

/* JADX INFO: loaded from: classes.dex */
public class LabTestManager {
    private static String LOG_TAG = "LabTestManager";
    private static LabTestManager sInstance;
    private Context mContext;
    private LabTestHandler mHandler;
    private HandlerThread mWorkThread;

    public static void init() {
        if (sInstance == null) {
            sInstance = new LabTestManager();
        }
    }

    private LabTestManager() {
        log("LabTestManager: init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG, 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        LabTestHandler labTestHandler = new LabTestHandler(this.mWorkThread.getLooper());
        this.mHandler = labTestHandler;
        labTestHandler.obtainMessage(0).sendToTarget();
    }

    class LabTestHandler extends Handler {
        public LabTestHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            LabTestManager.this.initAction();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        log("initAction");
        setCaptivePortalEnabled(this.mContext, false);
        setFiveGHostedModeEnabled(this.mContext, false);
        setFiveGPowerSaverEnabled(this.mContext, false);
        setSpecialNetworkModeEnabled(this.mContext, false);
        setSnsOptimizationEnabled(this.mContext, false);
    }

    private void setCaptivePortalEnabled(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), "captive_portal_mode", z ? 1 : 0);
    }

    private void setFiveGHostedModeEnabled(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), "fiveg_hosted_mode_user_enable", z ? 1 : 0);
    }

    private void setFiveGPowerSaverEnabled(Context context, boolean z) {
        Settings.System.putInt(context.getContentResolver(), "power_center_5g_save_mode", z ? 1 : 0);
    }

    private void setSpecialNetworkModeEnabled(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), "special_network_optimization", z ? 1 : 0);
    }

    private void setSnsOptimizationEnabled(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), "qms_switch_status", z ? 1 : 0);
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}

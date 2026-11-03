package com.android.services.telephony.relay.phonecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.mi.mibridge.DeviceLevel;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class RelayStarter {
    private static final String TAG = "RelayStarter";
    private static RelayStarter sRelayStarter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver(this) { // from class: com.android.services.telephony.relay.phonecall.RelayStarter.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            RelayLog.logd(RelayStarter.TAG, "Action:" + action);
            if ("android.intent.action.USER_PRESENT".equals(action)) {
                RelayUtils.WorkThread.getWorkHandler().post(new Runnable(this) { // from class: com.android.services.telephony.relay.phonecall.RelayStarter.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (RelayUtils.Relay.isRelaySide()) {
                                PadContinuityController.getInstance().init();
                                PadContinuityController.getInstance().registerConnectivityReceiver();
                                return;
                            }
                            PhoneContinuityController.getInstance().init();
                            PhoneContinuityController.getInstance().registerConnectivityReceiver();
                            if (RelayUtils.Relay.supportDualPhoneRelayCallFeature(context)) {
                                MiPhoneContinuityController.getInstance().init();
                                MiPhoneContinuityController.getInstance().registerReceiver();
                            }
                        } catch (Throwable th) {
                            RelayLog.loge(RelayStarter.TAG, "Init continuity failed!");
                            th.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    public static synchronized RelayStarter getInstance() {
        try {
            if (sRelayStarter == null) {
                sRelayStarter = new RelayStarter();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sRelayStarter;
    }

    public void start(Context context) {
        RelayLog.logi(TAG, "Lite devices: " + DeviceLevel.IS_MIUI_LITE_VERSION + ", Middle devices: " + DeviceLevel.IS_MIUI_MIDDLE_VERSION);
        if (!RelayUtils.Relay.supportRelayCallFeature(context) || isDeviceUnsupportRelay()) {
            RelayLog.logi(TAG, "Old lite devices not support call relay.");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    public boolean isDeviceUnsupportRelay() {
        boolean z = HyperOSCustFeatureResolve.getBoolean("unsupport_relay_call_device", false);
        RelayLog.logi(TAG, "isDeviceUnsupportRelay = " + z);
        return z;
    }
}

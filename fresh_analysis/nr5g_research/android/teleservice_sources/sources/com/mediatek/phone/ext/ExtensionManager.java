package com.mediatek.phone.ext;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class ExtensionManager {
    private static final String LOG_TAG = "ExtensionManager";
    private static Context sContext;
    private static IIncomingCallExt sIncomingCallExt;
    private static IMobileNetworkSettingsExt sMobileNetworkSettingsExt;

    private ExtensionManager() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    public static void registerApplicationContext(Context context) {
        if (sContext == null) {
            sContext = context;
            log("registerApplicationContext");
            sContext.registerReceiver(new BroadcastReceiver() { // from class: com.mediatek.phone.ext.ExtensionManager.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    ExtensionManager.log("[onReceive] Clear plugin due to " + intent.getAction());
                    OpPhoneCustomizationUtils.resetOpFactory(ExtensionManager.sContext);
                    synchronized (IIncomingCallExt.class) {
                        ExtensionManager.sIncomingCallExt = null;
                    }
                    synchronized (IMobileNetworkSettingsExt.class) {
                        ExtensionManager.sMobileNetworkSettingsExt = null;
                    }
                }
            }, new IntentFilter("com.mediatek.common.carrierexpress.operator_config_changed"));
        }
    }

    public static IIncomingCallExt getIncomingCallExt() {
        if (sIncomingCallExt == null) {
            synchronized (IIncomingCallExt.class) {
                try {
                    if (sIncomingCallExt == null) {
                        sIncomingCallExt = OpPhoneCustomizationUtils.getOpFactory(sContext).makeIncomingCallExt();
                        log("[sIncomingCallExt] create ext instance: " + sIncomingCallExt);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sIncomingCallExt;
    }

    public static IMobileNetworkSettingsExt getMobileNetworkSettingsExt() {
        IMobileNetworkSettingsExt iMobileNetworkSettingsExt;
        synchronized (IMobileNetworkSettingsExt.class) {
            try {
                if (sMobileNetworkSettingsExt == null) {
                    sMobileNetworkSettingsExt = OpPhoneCustomizationUtils.getOpFactory(sContext).makeMobileNetworkSettingsExt();
                    log("[sMobileNetworkSettingsExt] create ext instance: " + sMobileNetworkSettingsExt);
                }
                iMobileNetworkSettingsExt = sMobileNetworkSettingsExt;
            } catch (Throwable th) {
                throw th;
            }
        }
        return iMobileNetworkSettingsExt;
    }
}

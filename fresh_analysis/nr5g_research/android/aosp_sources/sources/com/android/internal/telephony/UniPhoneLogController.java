package com.android.internal.telephony;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class UniPhoneLogController {
    public static boolean CALL_DEBUG = false;
    public static boolean DATA_DEBUG = false;
    public static boolean IMS_DEBUG = false;
    public static boolean MOBILE_NETWORK_SETTINGS_DEBUG = false;
    public static boolean OMACP_DEBUG = false;
    public static boolean SIM_DEBUG = false;
    public static boolean SMS_CONTACTS_DEBUG = false;
    public static boolean SUPPLEMENTARY_SERVICE_DEBUG = false;
    public static boolean SYSTEMUI_DEBUG = false;
    private static UniPhoneLogController sInstance;

    public static UniPhoneLogController getInstance(Context context) {
        UniPhoneLogController uniPhoneLogController;
        synchronized (UniPhoneLogController.class) {
            try {
                if (sInstance == null) {
                    sInstance = UniTelephonyInjectionFactory.getInstance().getUniPhoneLogController(context);
                }
                uniPhoneLogController = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniPhoneLogController;
    }
}

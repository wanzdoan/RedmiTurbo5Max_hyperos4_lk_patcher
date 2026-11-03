package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.ISub;

/* JADX INFO: loaded from: classes.dex */
public class MtkSubscriptionManager {
    private static final boolean DBG = false;
    public static final String ENHANCED_VONR_SHOW_UI_ENABLED = "vonr_ui_enabled";
    public static final int EXTRA_VALUE_NEW_SIM = 1;
    public static final int EXTRA_VALUE_NOCHANGE = 4;
    public static final int EXTRA_VALUE_REMOVE_SIM = 2;
    public static final int EXTRA_VALUE_REPOSITION_SIM = 3;
    public static final String INTENT_KEY_DETECT_STATUS = "simDetectStatus";
    public static final String INTENT_KEY_PROP_KEY = "simPropKey";
    public static final String INTENT_KEY_SIM_COUNT = "simCount";
    private static final String LOG_TAG = "MtkSubscriptionManager";
    private static final boolean VDBG = false;

    public static int getSubIdUsingPhoneId(int phoneId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getSubId(phoneId);
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }
}

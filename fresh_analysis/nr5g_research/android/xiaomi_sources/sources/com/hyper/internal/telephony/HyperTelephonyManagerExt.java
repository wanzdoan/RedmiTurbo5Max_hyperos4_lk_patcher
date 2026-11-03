package com.hyper.internal.telephony;

import android.app.ActivityThread;
import android.content.Context;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import com.hyper.internal.telephony.ext.IHyperCallManagerExt;
import com.hyper.internal.telephony.ext.IHyperDataManagerExt;
import com.hyper.internal.telephony.ext.IHyperNetworkManagerExt;
import com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt;
import com.hyper.internal.telephony.ext.IHyperUiccManagerExt;

/* JADX INFO: loaded from: classes.dex */
public class HyperTelephonyManagerExt extends HyperTelephonyInternalManager {
    private static final String LOG_TAG = "HyperTelephonyManagerExt";
    private static Context sContext = null;

    public HyperTelephonyManagerExt() {
        logd("HyperTelephonyManagerExt enter!");
    }

    public static Context getContext() {
        if (sContext == null) {
            sContext = ActivityThread.currentApplication().getApplicationContext();
        }
        return sContext;
    }

    @Override // com.hyper.internal.telephony.HyperTelephonyInternalManager
    public IHyperUiccManagerExt getHyperUiccManager() {
        return HyperUiccManagerImplExt.getInstance();
    }

    @Override // com.hyper.internal.telephony.HyperTelephonyInternalManager
    public IHyperNetworkManagerExt getHyperNetworkManager() {
        return HyperNetworkManagerImplExt.getInstance();
    }

    @Override // com.hyper.internal.telephony.HyperTelephonyInternalManager
    public IHyperCallManagerExt getHyperCallManager() {
        return HyperCallManagerImplExt.getInstance();
    }

    @Override // com.hyper.internal.telephony.HyperTelephonyInternalManager
    public IHyperDataManagerExt getHyperDataManager() {
        return HyperDataManagerImplExt.getInstance();
    }

    @Override // com.hyper.internal.telephony.HyperTelephonyInternalManager
    public IHyperNetworkTypeManagerExt getHyperNetworkTypeManager(Phone phone, NetworkTypeController networkTypeController) {
        return new HyperNetworkTypeManagerImplExt(phone, networkTypeController);
    }

    public static void logd(String string) {
        Rlog.d(LOG_TAG, string);
    }
}

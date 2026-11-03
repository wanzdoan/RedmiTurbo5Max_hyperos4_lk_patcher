package com.hyper.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import com.hyper.internal.telephony.ext.IHyperCallManagerExt;
import com.hyper.internal.telephony.ext.IHyperCatManagerExt;
import com.hyper.internal.telephony.ext.IHyperDataManagerExt;
import com.hyper.internal.telephony.ext.IHyperFrameworkTelephonyManagerExt;
import com.hyper.internal.telephony.ext.IHyperNetworkManagerExt;
import com.hyper.internal.telephony.ext.IHyperNetworkTypeManagerExt;
import com.hyper.internal.telephony.ext.IHyperSsManagerExt;
import com.hyper.internal.telephony.ext.IHyperTeleImsManagerExt;
import com.hyper.internal.telephony.ext.IHyperTeleSmsManagerExt;
import com.hyper.internal.telephony.ext.IHyperUiccManagerExt;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class HyperTelephonyInternalManager {
    private static final String TAG = "HyperTelephonyInternalManager";
    private static HyperTelephonyInternalManager sInstance = null;
    private static Context sContext = null;
    private static final Object mLock = new Object();

    public static HyperTelephonyInternalManager getInstance() {
        HyperTelephonyInternalManager hyperTelephonyInternalManager;
        synchronized (mLock) {
            if (sInstance == null) {
                try {
                    PathClassLoader classLoader = new PathClassLoader("/system_ext/framework/hyper-telephony-common-ext.jar", HyperTelephonyInternalManager.class.getClassLoader());
                    Rlog.d(TAG, "classLoader = " + classLoader.toString());
                    Class<?> cls = Class.forName("com.hyper.internal.telephony.HyperTelephonyManagerExt", true, classLoader);
                    Rlog.d(TAG, "cls = " + cls);
                    Constructor<?> constructor = cls.getConstructor(new Class[0]);
                    Rlog.d(TAG, "constructor = " + constructor);
                    HyperTelephonyInternalManager hyperTelephonyInternalManager2 = (HyperTelephonyInternalManager) constructor.newInstance(new Object[0]);
                    sInstance = hyperTelephonyInternalManager2;
                } catch (Exception e) {
                    e.printStackTrace();
                    Rlog.e(TAG, "Reflect exception getInstance: " + e.toString());
                    sInstance = new HyperTelephonyInternalManager();
                }
                hyperTelephonyInternalManager = sInstance;
            } else {
                hyperTelephonyInternalManager = sInstance;
            }
            throw th;
        }
        return hyperTelephonyInternalManager;
    }

    public IHyperUiccManagerExt getHyperUiccManager() {
        return IHyperUiccManagerExt.DEFAULT;
    }

    public IHyperCallManagerExt getHyperCallManager() {
        return IHyperCallManagerExt.DEFAULT;
    }

    public IHyperDataManagerExt getHyperDataManager() {
        return IHyperDataManagerExt.DEFAULT;
    }

    public IHyperNetworkManagerExt getHyperNetworkManager() {
        return IHyperNetworkManagerExt.DEFAULT;
    }

    public IHyperCatManagerExt getHyperCatManager() {
        return IHyperCatManagerExt.DEFAULT;
    }

    public IHyperFrameworkTelephonyManagerExt getHyperFrameworkTelephonyManager() {
        return IHyperFrameworkTelephonyManagerExt.DEFAULT;
    }

    public IHyperSsManagerExt getHyperSsManager() {
        return IHyperSsManagerExt.DEFAULT;
    }

    public IHyperTeleSmsManagerExt getHyperTeleSmsManager() {
        return IHyperTeleSmsManagerExt.DEFAULT;
    }

    public IHyperNetworkTypeManagerExt getHyperNetworkTypeManager(Phone phone, NetworkTypeController networkTypeController) {
        return IHyperNetworkTypeManagerExt.DEFAULT;
    }

    public IHyperTeleImsManagerExt getHyperTeleImsManager() {
        return IHyperTeleImsManagerExt.DEFAULT;
    }
}

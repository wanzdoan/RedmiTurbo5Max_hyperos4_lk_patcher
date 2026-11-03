package com.android.internal.telephony;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import com.android.ims.ImsCall;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.call.UniCallManager;
import com.android.internal.telephony.cat.UniCatManager;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataRetryManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.DataStallRecoveryManager;
import com.android.internal.telephony.data.UniDataManager;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.satellite.UniSatelliteController;
import com.android.internal.telephony.simlock.SimLockController;
import com.android.internal.telephony.subsidylock.SubsidyLockController;
import com.android.internal.telephony.uicc.PinStorage;
import com.android.internal.telephony.uicc.UniUiccManager;

/* JADX INFO: loaded from: classes.dex */
public class UniTelephonyInjectionFactory {
    private static final String TAG = "UniTelephonyInjectionFactory";
    private static UniTelephonyInjectionFactory sInstance;
    private SimLockController mSimLockController;
    private SubsidyLockController mSubsidyLockController;

    public UniTelephonyInjectionFactory() {
        log("UniTelephonyInjectionFactory created");
    }

    public static UniTelephonyInjectionFactory getInstance() {
        UniTelephonyInjectionFactory uniTelephonyInjectionFactory;
        synchronized (UniTelephonyInjectionFactory.class) {
            try {
                if (sInstance == null) {
                    sInstance = TelephonyComponentFactory.getInstance().inject(UniTelephonyInjectionFactory.class.getName()).makeUniTelephonyInjectionFactory();
                }
                uniTelephonyInjectionFactory = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniTelephonyInjectionFactory;
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, ImsCall imsCall, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        return new ImsPhoneConnection(phone, imsCall, imsPhoneCallTracker, imsPhoneCall, z);
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, String str, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z, boolean z2, ImsPhone.ImsDialArgs imsDialArgs) {
        return new ImsPhoneConnection(phone, str, imsPhoneCallTracker, imsPhoneCall, z, z2, imsDialArgs);
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, String[] strArr, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        return new ImsPhoneConnection(phone, strArr, imsPhoneCallTracker, imsPhoneCall, z);
    }

    public UniCallManager makeUniCallManager() {
        return new UniCallManager();
    }

    public UniTeleImsManager makeUniTeleImsManager() {
        return new UniTeleImsManager();
    }

    public UniSsManager getUniSsManager() {
        return new UniSsManager();
    }

    public DataStallRecoveryManager makeDataStallRecoveryManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, FeatureFlags featureFlags, Looper looper, DataStallRecoveryManager.DataStallRecoveryManagerCallback dataStallRecoveryManagerCallback) {
        return new DataStallRecoveryManager(phone, dataNetworkController, dataServiceManager, featureFlags, looper, dataStallRecoveryManagerCallback);
    }

    public DataRetryManager makeDataRetryManager(Phone phone, DataNetworkController dataNetworkController, SparseArray<DataServiceManager> sparseArray, Looper looper, FeatureFlags featureFlags, DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback) {
        return new DataRetryManager(phone, dataNetworkController, sparseArray, looper, featureFlags, dataRetryManagerCallback);
    }

    public DataConfigManager makeDataConfigManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new DataConfigManager(phone, looper, featureFlags);
    }

    public PinStorage makePinStorage(Context context, Looper looper, FeatureFlags featureFlags) {
        return new PinStorage(context, looper, featureFlags);
    }

    public UniDataManager getUniDataManager() {
        return new UniDataManager();
    }

    public UniCatManager getUniCatManager() {
        return new UniCatManager();
    }

    public UniTeleSmsManager getUniTeleSmsManager() {
        return new UniTeleSmsManager();
    }

    public UniUiccManager getUniUiccManager() {
        return new UniUiccManager();
    }

    public UniPhoneLogController getUniPhoneLogController(Context context) {
        return new UniPhoneLogController();
    }

    public UniSignalStrengthManager makeUniSignalStrengthManager() {
        return new UniSignalStrengthManager();
    }

    public SimLockController makeSimLockController(Context context) {
        SimLockController simLockController = new SimLockController(context);
        this.mSimLockController = simLockController;
        return simLockController;
    }

    public SimLockController getSimLockController() {
        return this.mSimLockController;
    }

    public SubsidyLockController makeSubsidyLockController(Context context) {
        SubsidyLockController subsidyLockController = new SubsidyLockController(context);
        this.mSubsidyLockController = subsidyLockController;
        return subsidyLockController;
    }

    public SubsidyLockController getSubsidyLockController() {
        return this.mSubsidyLockController;
    }

    public UniSatelliteController makeUniSatelliteController(Context context, Looper looper) {
        return new UniSatelliteController(context, looper);
    }

    private static void log(String str) {
        Log.d(TAG, str);
    }
}

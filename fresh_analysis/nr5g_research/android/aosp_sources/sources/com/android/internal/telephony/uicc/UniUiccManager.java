package com.android.internal.telephony.uicc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.NetworkScanRequestTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UniTelephonyInjectionFactory;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;

/* JADX INFO: loaded from: classes.dex */
public class UniUiccManager {
    public static final int ACTION_INITIALIZE_TRACKER_FOR_PHONE = 100;
    public static final int ADN_CAPACITY_FULL = 1;
    public static final int OVER_NAME_MAX_LENGTH = 2;
    public static final String TAG = "UniUiccManager";
    public static final int TIMING_START_NEW_SCAN = 0;
    public static final int TIMING_START_SCAN_DOWN = 1;
    public static final int UNKNOWN_FAILED = 0;
    private static UniUiccManager sInstance;

    private boolean shouldEnableHighPowerConsumptionIndications(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return (z || z2 || z3 || z4) && z5;
    }

    public int getAdnErrorCode(int i) {
        return 0;
    }

    public Handler getSimThreadHandler() {
        return null;
    }

    public boolean isCarrierSupport5gSaOrNsaNetworkTypes(int i, Context context) {
        return false;
    }

    public boolean isNetTransSatisfyOpti(int i, int i2) {
        return false;
    }

    public boolean isSupportRadioBitmaskNrLte(int i, Context context) {
        return false;
    }

    public boolean isVendorOptimizedNetworkTypeSupport() {
        return false;
    }

    public void makeUiccPhoneBookController() {
    }

    public void notifyFdnStatusChanged(Context context, int i, boolean z) {
    }

    public void notifySimOperatorChanged(Context context, SubscriptionInfoInternal subscriptionInfoInternal, int i) {
    }

    public void setAdnErrorCode(int i, int i2) {
    }

    public void setEnh4glteMode(int i, int i2, Context context) {
    }

    public void setInternalDataEnabled(Phone[] phoneArr) {
    }

    public void setOptimizedNetworkTypesBitmap(int i, int i2, Context context, Message message) {
    }

    public void setRadioCapabilityforDataCard(Context context, int i) {
    }

    public void setSaOrNsaNetworkTypesBitmap(int i, int i2, Context context, Message message) {
    }

    public boolean shouldInitCarrierPriviledgeByHandlerThread(Phone phone) {
        return false;
    }

    public void unregisterForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo networkScanRequestInfo, Handler handler) {
    }

    public UniUiccManager() {
        log("UniUiccManager created");
    }

    public static UniUiccManager getInstance() {
        UniUiccManager uniUiccManager;
        synchronized (UniUiccManager.class) {
            try {
                if (sInstance == null) {
                    sInstance = UniTelephonyInjectionFactory.getInstance().getUniUiccManager();
                }
                uniUiccManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniUiccManager;
    }

    public String getValueUseForWcdma() {
        return "rssi";
    }

    public boolean shouldEnableSignalStrengthReports(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        if (shouldEnableHighPowerConsumptionIndications(z, z2, z3, z4, z5)) {
            return true;
        }
        return z6 && z5;
    }

    public SIMRecords makeSIMRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new SIMRecords(uiccCardApplication, context, commandsInterface);
    }

    public RuimRecords makeRuimRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new RuimRecords(uiccCardApplication, context, commandsInterface);
    }

    public IccFileHandler makeSimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new SIMFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public IccFileHandler makeUsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new UsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public IccFileHandler makeCsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new CsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public AdnRecordCache makeAdnRecordCache(IccFileHandler iccFileHandler) {
        return new AdnRecordCache(iccFileHandler);
    }

    public void registerForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo networkScanRequestInfo, Handler handler, int i, int i2) {
        if (i2 == 1) {
            networkScanRequestInfo.mPhone.mCi.registerForNetworkScanResult(handler, i, networkScanRequestInfo);
        }
    }

    public static boolean isEngTest() {
        return "true".equals(SystemProperties.get("persist.vendor.radio.engtest.enable", "false"));
    }

    private static void log(String str) {
        Log.d(TAG, str);
    }
}

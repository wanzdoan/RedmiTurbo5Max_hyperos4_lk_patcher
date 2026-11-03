package com.android.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhysicalChannelConfig;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.CsimFileHandler;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMFileHandler;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UsimFileHandler;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperUiccManager extends IHyperCommonModule {
    public static final int ADN_CAPACITY_FULL = 1;
    public static final IHyperUiccManager DEFAULT = new IHyperUiccManager() { // from class: com.android.internal.telephony.IHyperUiccManager.1
    };
    public static final int OVER_NAME_MAX_LENGTH = 2;
    public static final String TAG = "IHyperUiccManager";
    public static final int TIMING_START_NEW_SCAN = 0;
    public static final int TIMING_START_SCAN_DOWN = 1;
    public static final int UNKNOWN_FAILED = 0;

    private default boolean shouldEnableHighPowerConsumptionIndications(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return (z || z2 || z3 || z4) && z5;
    }

    default void activateUiccCard(int i) {
    }

    default void deactivateUiccCard(int i) {
    }

    default boolean hasNrSecondaryServingCell(List<PhysicalChannelConfig> list, int i) {
        return i == 2;
    }

    default boolean isNetTransSatisfyOpti(int i, int i2) {
        return false;
    }

    default boolean isSupportRadioBitmaskNrLte(int i, Context context) {
        return false;
    }

    default boolean isVendorOptimizedNetworkTypeSupport() {
        return false;
    }

    default boolean isVendorOverride() {
        return false;
    }

    default void makeUiccPhoneBookController() {
    }

    default void notifyFdnStatusChanged(Context context, int i, boolean z) {
    }

    default void notifySimOperatorChanged(Context context, SubscriptionInfoInternal subscriptionInfoInternal, int i) {
    }

    default void setAdnErrorCode(int i, int i2) {
    }

    default void setEnh4glteMode(int i, int i2, Context context) {
    }

    default void setInternalDataEnabled(Phone[] phoneArr) {
    }

    default void setOptimizedNetworkTypesBitmap(int i, int i2, Context context, Message message) {
    }

    default void unregisterForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo networkScanRequestInfo, Handler handler) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperUiccManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default void registerForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo networkScanRequestInfo, Handler handler, int i, int i2) {
        if (i2 == 1) {
            networkScanRequestInfo.mPhone.mCi.registerForNetworkScanResult(handler, i, networkScanRequestInfo);
        }
    }

    default AdnRecordCache makeAdnRecordCache(IccFileHandler iccFileHandler) {
        return new AdnRecordCache(iccFileHandler);
    }

    default SIMRecords makeSIMRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new SIMRecords(uiccCardApplication, context, commandsInterface);
    }

    default RuimRecords makeRuimRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new RuimRecords(uiccCardApplication, context, commandsInterface);
    }

    default IccFileHandler makeSimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new SIMFileHandler(uiccCardApplication, str, commandsInterface);
    }

    default IccFileHandler makeUsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new UsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    default IccFileHandler makeCsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new CsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    default boolean shouldEnableSignalStrengthReports(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        if (shouldEnableHighPowerConsumptionIndications(z, z2, z3, z4, z5)) {
            return true;
        }
        return z6 && z5;
    }
}

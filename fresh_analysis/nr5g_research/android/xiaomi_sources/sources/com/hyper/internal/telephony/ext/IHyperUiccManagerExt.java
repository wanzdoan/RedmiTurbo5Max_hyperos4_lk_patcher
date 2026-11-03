package com.hyper.internal.telephony.ext;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhysicalChannelConfig;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.NetworkScanRequestTracker;
import com.android.internal.telephony.Phone;
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

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperUiccManagerExt extends IHyperCommonModule {
    public static final IHyperUiccManagerExt DEFAULT = new IHyperUiccManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperUiccManagerExt.1
    };
    public static final String TAG = "IHyperUiccManager";
    public static final int TIMING_START_SCAN_DOWN = 1;

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default void activateUiccCard(int phoneId) {
    }

    default void deactivateUiccCard(int phoneId) {
    }

    default boolean isVendorOptimizedNetworkTypeSupport() {
        return false;
    }

    default boolean isSupportRadioBitmaskNrLte(int phoneId, Context context) {
        return false;
    }

    default boolean isNetTransSatisfyOpti(int oldFilteredTypeInput, int newFilteredRaf) {
        return false;
    }

    default void setOptimizedNetworkTypesBitmap(int filteredRaf, int phoneId, Context context, Message response) {
    }

    default void setEnh4glteMode(int networkType, int phoneId, Context context) {
    }

    default void notifySimOperatorChanged(Context context, SubscriptionInfoInternal subInfo, int phoneId) {
    }

    default void notifyFdnStatusChanged(Context context, int phoneId, boolean fdnEnabled) {
    }

    default void setInternalDataEnabled(Phone[] phones) {
    }

    default void registerForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo nsri, Handler handler, int event, int timing) {
        if (timing == 1) {
            nsri.mPhone.mCi.registerForNetworkScanResult(handler, event, nsri);
        }
    }

    default boolean isVendorOverride() {
        return false;
    }

    default void unregisterForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo nsri, Handler handler) {
    }

    default boolean hasNrSecondaryServingCell(List<PhysicalChannelConfig> configs, int physicalLinkStatus) {
        return physicalLinkStatus == 2;
    }

    default void makeUiccPhoneBookController() {
    }

    default AdnRecordCache makeAdnRecordCache(IccFileHandler fh) {
        return new AdnRecordCache(fh);
    }

    default SIMRecords makeSIMRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        return new SIMRecords(app, c, ci);
    }

    default RuimRecords makeRuimRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        return new RuimRecords(app, c, ci);
    }

    default IccFileHandler makeSimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci, Context c) {
        return new SIMFileHandler(app, aid, ci);
    }

    default IccFileHandler makeUsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci, Context c) {
        return new UsimFileHandler(app, aid, ci);
    }

    default IccFileHandler makeCsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        return new CsimFileHandler(app, aid, ci);
    }

    default boolean shouldEnableSignalStrengthReports(boolean isCharging, boolean isScreenOn, boolean isTetheringOn, boolean isAutomotiveProjectionActive, boolean isRadioOn, boolean isAlwaysSignalStrengthReportingEnabled) {
        return shouldEnableHighPowerConsumptionIndications(isCharging, isScreenOn, isTetheringOn, isAutomotiveProjectionActive, isRadioOn) || (isAlwaysSignalStrengthReportingEnabled && isRadioOn);
    }

    private default boolean shouldEnableHighPowerConsumptionIndications(boolean isCharging, boolean isScreenOn, boolean isTetheringOn, boolean isAutomotiveProjectionActive, boolean isRadioOn) {
        return (isCharging || isScreenOn || isTetheringOn || isAutomotiveProjectionActive) && isRadioOn;
    }

    default void setAdnErrorCode(int efId, int errorCode) {
    }
}

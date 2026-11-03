package com.hyper.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhysicalChannelConfig;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IHyperUiccManager;
import com.android.internal.telephony.NetworkScanRequestTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class HyperUiccManagerImpl implements IHyperUiccManager {
    private static HyperUiccManagerImpl sInstance;

    private HyperUiccManagerImpl() {
    }

    public static HyperUiccManagerImpl getInstance() {
        HyperUiccManagerImpl hyperUiccManagerImpl;
        synchronized (HyperUiccManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperUiccManagerImpl();
            }
            hyperUiccManagerImpl = sInstance;
        }
        return hyperUiccManagerImpl;
    }

    public void activateUiccCard(int phoneId) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().activateUiccCard(phoneId);
    }

    public void deactivateUiccCard(int phoneId) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().deactivateUiccCard(phoneId);
    }

    public boolean isVendorOptimizedNetworkTypeSupport() {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().isVendorOptimizedNetworkTypeSupport();
    }

    public boolean isSupportRadioBitmaskNrLte(int phoneId, Context context) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().isSupportRadioBitmaskNrLte(phoneId, context);
    }

    public boolean isNetTransSatisfyOpti(int oldFilteredTypeInput, int newFilteredRaf) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().isNetTransSatisfyOpti(oldFilteredTypeInput, newFilteredRaf);
    }

    public void setOptimizedNetworkTypesBitmap(int filteredRaf, int phoneId, Context context, Message response) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().setOptimizedNetworkTypesBitmap(filteredRaf, phoneId, context, response);
    }

    public void setEnh4glteMode(int networkType, int phoneId, Context context) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().setEnh4glteMode(networkType, phoneId, context);
    }

    public void notifySimOperatorChanged(Context context, SubscriptionInfoInternal subInfo, int phoneId) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().notifySimOperatorChanged(context, subInfo, phoneId);
    }

    public void notifyFdnStatusChanged(Context context, int phoneId, boolean fdnEnabled) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().notifyFdnStatusChanged(context, phoneId, fdnEnabled);
    }

    public void setInternalDataEnabled(Phone[] phones) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().setInternalDataEnabled(phones);
    }

    public void registerForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo nsri, Handler handler, int event, int timing) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().registerForNetworkScanResult(nsri, handler, event, timing);
    }

    public void unregisterForNetworkScanResult(NetworkScanRequestTracker.NetworkScanRequestInfo nsri, Handler handler) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().unregisterForNetworkScanResult(nsri, handler);
    }

    public boolean isVendorOverride() {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().isVendorOverride();
    }

    public boolean hasNrSecondaryServingCell(List<PhysicalChannelConfig> configs, int physicalLinkStatus) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().hasNrSecondaryServingCell(configs, physicalLinkStatus);
    }

    public void makeUiccPhoneBookController() {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeUiccPhoneBookController();
    }

    public AdnRecordCache makeAdnRecordCache(IccFileHandler fh) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeAdnRecordCache(fh);
    }

    public SIMRecords makeSIMRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeSIMRecords(app, c, ci);
    }

    public RuimRecords makeRuimRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeRuimRecords(app, c, ci);
    }

    public IccFileHandler makeSimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci, Context c) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeSimFileHandler(app, aid, ci, c);
    }

    public IccFileHandler makeUsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci, Context c) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeUsimFileHandler(app, aid, ci, c);
    }

    public IccFileHandler makeCsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().makeCsimFileHandler(app, aid, ci);
    }

    public void setAdnErrorCode(int efId, int errorCode) {
        HyperTelephonyInternalManager.getInstance().getHyperUiccManager().setAdnErrorCode(efId, errorCode);
    }

    public boolean shouldEnableSignalStrengthReports(boolean isCharging, boolean isScreenOn, boolean isTetheringOn, boolean isAutomotiveProjectionActive, boolean isRadioOn, boolean isAlwaysSignalStrengthReportingEnabled) {
        return HyperTelephonyInternalManager.getInstance().getHyperUiccManager().shouldEnableSignalStrengthReports(isCharging, isScreenOn, isTetheringOn, isAutomotiveProjectionActive, isRadioOn, isAlwaysSignalStrengthReportingEnabled);
    }
}

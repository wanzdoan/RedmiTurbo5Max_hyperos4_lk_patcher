package com.android.services.telephony;

import android.content.Context;
import android.content.Intent;
import com.android.phone.CarrierConfigRepairManager;
import com.android.phone.CellInfoLogger;
import com.android.phone.DefaultSlotSelectorImpl;
import com.android.phone.Dual4GManager;
import com.android.phone.Enable2GManager;
import com.android.phone.FakeCellController;
import com.android.phone.FdnQueryManager;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGManager;
import com.android.phone.FiveGManagerBase;
import com.android.phone.FiveGPowerController;
import com.android.phone.FiveGServiceInfoTracker;
import com.android.phone.GutiUtils;
import com.android.phone.GwsdManager;
import com.android.phone.IllegalMeDialogHelper;
import com.android.phone.MiSimRepairManager;
import com.android.phone.MiuiDeviceEuiccBindingMgrForSt;
import com.android.phone.MiuiEsimManager;
import com.android.phone.MiuiImsGlobalizationManager;
import com.android.phone.MiuiLog;
import com.android.phone.MiuiNotificationMgr;
import com.android.phone.MiuiOsuManager;
import com.android.phone.MiuiPhoneInterfaceManager;
import com.android.phone.MiuiPhoneReceiver;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.MiuiSimHotSwapManager;
import com.android.phone.ModemProvider;
import com.android.phone.MtnSimLockController;
import com.android.phone.NetworkModeManager;
import com.android.phone.OtaLteUpdateManager;
import com.android.phone.PhoneAdapter;
import com.android.phone.PhoneGlobals;
import com.android.phone.QcPhoneConnector;
import com.android.phone.R;
import com.android.phone.ResetRadioReceiver;
import com.android.phone.TelephonyAction;
import com.android.phone.TelephonyDelegateImpl;
import com.android.phone.VoiceLinkController;
import com.android.phone.VoiceServiceMgr;
import com.android.phone.VolteEnableManager;
import com.android.phone.dcservice.MiDcServiceManager;
import com.android.phone.dsda.MiuiDsdaManager;
import com.android.phone.dsda.vtcallfeature.TxsDsdaNonDdsVtOptimize;
import com.android.phone.ims.ImsSignalQualityHelper;
import com.android.phone.labtest.LabTestManager;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.roaming.RoamingCFManager;
import com.android.phone.roaming.RoamingManager;
import com.android.phone.roaming.recovery.NetworkRecovery;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.phone.utils.DcStatMonitor;
import com.android.phone.utils.ImsConferenceHandler;
import com.android.phone.utils.PhoneStateBroadcaster;
import com.android.services.telephony.ims.ImsAdapter;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.EsimManager;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCoreModule implements ICoreModule {
    private Context mContext;
    private DcStatMonitor mDcStatMonitor;
    private TelephonyDelegateImpl mDelegate;
    MiuiNotificationMgr mMiuiNotificationMgr;
    VoiceServiceMgr mVoiceServiceMgr;

    @Override // com.android.services.telephony.ICoreModule
    public int getPriority() {
        return 80;
    }

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("TelephonyCoreModule", "onCreate");
        this.mContext = context;
        MiuiPhoneInterfaceManager.getInstance().init(this.mContext);
        if (MiuiPhoneUtils.isEsimCapable()) {
            MiuiEsimManager.make();
            if (MiuiPhoneUtils.isSupportEuiccBinding()) {
                MiuiDeviceEuiccBindingMgrForSt.init();
            }
            if (MiuiPhoneUtils.isSupportOsuFeature()) {
                MiuiOsuManager.internalInit();
            }
        }
        if (!MiuiPhoneUtils.isProjectSupportEsim()) {
            EsimManager.init(this.mContext);
        }
        MiuiLog.register();
        MiuiPhoneReceiver.register(this.mContext);
        this.mMiuiNotificationMgr = new MiuiNotificationMgr(PhoneGlobals.getInstance());
        if (MiuiPhoneUtils.isSupportFakeCellUsingRilHookNV()) {
            FakeCellController.init(this.mContext);
        }
        ResetRadioReceiver.register(this.mContext);
        if (MiuiPhoneUtils.isLabTestOn()) {
            LabTestManager.init();
        }
        this.mDelegate = TelephonyDelegateImpl.init();
        try {
            Intent intent = new Intent();
            intent.setAction("com.xiaomi.phone.service.MiTelephonyService");
            intent.setPackage("com.xiaomi.phone");
            this.mContext.startService(intent);
        } catch (Exception e) {
            android.util.Log.e("TelephonyCoreModule", e.toString());
        }
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("TelephonyCoreModule", "onSimPresent");
        NetworkModeManager.make();
        this.mDelegate.setNetworkMode(NetworkMode.getInstance());
        this.mDelegate.setTelephonyAction(TelephonyAction.getInstance());
        DefaultSlotSelectorImpl.make();
        this.mDelegate.setPhoneAdapter(PhoneAdapter.make());
        MiuiSimHotSwapManager.init();
        ImsAdapter.init();
        RoamingManager.init(this.mContext);
        this.mDelegate.setRoaming(RoamingManager.getInstance());
        CellInfoLogger.init();
        this.mDelegate.setCellInfoLogger(CellInfoLogger.getInstance());
        FdnQueryManager fdnQueryManager = FdnQueryManager.getInstance();
        fdnQueryManager.init(this.mContext);
        this.mDelegate.setFdnQuery(fdnQueryManager);
        Enable2GManager.init(this.mContext);
        this.mDelegate.setEnable2G(Enable2GManager.getInstance());
        if (ModemProvider.IS_QCOM) {
            QcPhoneConnector.get().init(this.mContext, null);
        }
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            Dual4GManager.init();
            this.mDelegate.setDual4G(Dual4GManager.getInstance());
        }
        if (MiuiPhoneUtils.isSupportMtnLocked()) {
            MtnSimLockController.make();
        }
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManager.make().init();
            this.mDelegate.setFiveGPower(FiveGPowerController.getInstance());
            FiveGServiceInfoTracker.make();
        } else if (MiuiPhoneUtils.isTrafficControlSupported()) {
            MiuiPhoneUtils.initTrafficControlDefaultData();
        }
        if (MiuiPhoneUtils.isGwsdSupport()) {
            GwsdManager.make();
        }
        boolean z = Build.IS_INTERNATIONAL_BUILD;
        if (z && MiuiPhoneUtils.isSupportVoiceLinkFeature() && !FiveGCloudController.isFeatureDisabledByCloud("voice_link_feature")) {
            VoiceLinkController.init();
        }
        VolteEnableManager.init();
        this.mDelegate.setVolteEnable(VolteEnableManager.getInstance());
        if (MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
            this.mVoiceServiceMgr = new VoiceServiceMgr(PhoneGlobals.getInstance());
        }
        if (!z || MiuiPhoneUtils.isDcStatMonitorSupported()) {
            this.mDcStatMonitor = new DcStatMonitor();
        }
        if (MiSimRepairManager.isSupportMultiSimConfigSelfRecovery() || MiSimRepairManager.isSupportSimAbsentSelfRecovery()) {
            MiSimRepairManager.getInstance(this.mContext);
        }
        PhoneStateBroadcaster.init();
        if (MiuiPhoneUtils.isSupportHangUpoptimize3_0()) {
            MiuiCallStateMonitor.getIntance();
        }
        if (MiuiPhoneUtils.isGUTISupported()) {
            GutiUtils.getInstance();
        }
        if (MiuiPhoneUtils.isImsSignalOptimizationSupported(this.mContext)) {
            ImsSignalQualityHelper.init();
            this.mDelegate.setImsSignal(ImsSignalQualityHelper.getInstance());
        }
        if (RoamingUtils.isRoamingOptimizationSupported(this.mContext)) {
            NetworkRecovery.init();
        }
        if (MiuiPhoneUtils.isSupportTxsDsdaNonDdsVtOptimizeFeature()) {
            TxsDsdaNonDdsVtOptimize.init(this.mContext);
        }
        if (MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
            this.mDelegate.setDsda(MiuiDsdaManager.getInstance());
        }
        if (MiuiPhoneUtils.isSupportNewCall()) {
            MiDcServiceManager.init(PhoneGlobals.getInstance());
        }
        if (MiuiPhoneUtils.isSupportOtaLteUpdate()) {
            OtaLteUpdateManager.init(this.mContext);
        }
        IllegalMeDialogHelper.init();
        CarrierConfigRepairManager.init(this.mContext);
        this.mDelegate.setCarrierConfigRepair(CarrierConfigRepairManager.getInstance());
        MiuiImsGlobalizationManager.init();
        this.mDelegate.setImsGlobalization(MiuiImsGlobalizationManager.getInstance());
        this.mDelegate.setImsConferenceHandler(ImsConferenceHandler.getInstance());
        this.mDelegate.setRoamingCF(RoamingCFManager.getInstance());
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return HyperOSCustFeatureResolve.getBoolean("config_support_phone_module", context.getResources().getBoolean(R.bool.phone_module_enabled));
    }
}

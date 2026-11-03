package com.android.services.telephony;

import android.content.Context;
import com.android.phone.BatteryStateManager;
import com.android.phone.CloudController.CloudModemControllerBase;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.CloudEccListController;
import com.android.phone.FiveGCloudController;
import com.android.phone.FlashManager;
import com.android.phone.MiCarrierConfigLoaderImpl;
import com.android.phone.MiuiCloudCarrierConfigManager;
import com.android.phone.MiuiCloudDataManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.SensorStateManager;
import com.android.phone.TelephonySensorManager;
import com.android.phone.TelephonyXiaoAiManager;
import com.android.phone.UtilityDelegateImpl;
import com.android.phone.badcells.BadCellStats;
import com.android.phone.statistics.EarlyInitStatistic;
import com.android.phone.statistics.FiveGAStatistics;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.android.phone.statistics.MiuiDsdaStatistic;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.rf.SixRxStatistic;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.SimCardStatMonitor;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class UtilityCoreModule implements ICoreModule {
    private Context mContext;
    private UtilityDelegateImpl mDelegate;

    @Override // com.android.services.telephony.ICoreModule
    public int getPriority() {
        return 100;
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("UtilityCoreModule", "onCreate");
        this.mContext = context;
        ATCommandUtils.make();
        BatteryStateManager.getInstance().init(this.mContext);
        FlashManager.getInstance().init(this.mContext);
        FiveGCloudController.init();
        FiveGCloudController.getInstance().fetchAll5GCloudStatus(false);
        MiStatInterfaceUtil.init(this.mContext);
        EarlyInitStatistic.init(this.mContext);
        CloudEccListController.make();
        this.mDelegate = UtilityDelegateImpl.init();
        MiCarrierConfigLoaderImpl miCarrierConfigLoaderImpl = MiCarrierConfigLoaderImpl.getInstance();
        if (miCarrierConfigLoaderImpl != null) {
            MiuiCloudDataManager.init(miCarrierConfigLoaderImpl.getConfigFromDefaultApp());
        } else {
            android.util.Log.w("UtilityCoreModule", "onCreate: MiCarrierConfigLoaderImpl not initialized yet, MiuiCloudDataManager init skipped");
        }
        HighSpeedTrainStats.init();
    }

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return context.getResources().getBoolean(R.bool.utility_module_enabled);
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("UtilityCoreModule", "onSimPresent");
        TelephonyXiaoAiManager.make();
        ModemUtils.init(this.mContext);
        ModemUtils.getMiRilHook(this.mContext);
        this.mDelegate.setSensorManager(TelephonySensorManager.init(this.mContext));
        TelephonyCloudController.getInstance().init();
        MiuiDsdaStatistic.init();
        SensorStateManager.getInstance(this.mContext);
        if (MiuiPhoneUtils.getPhones().length > 1 && !Build.IS_INTERNATIONAL_BUILD) {
            new SimCardStatMonitor();
        }
        if (MiuiPhoneUtils.isSupportSixRX()) {
            SixRxStatistic.make();
        }
        if (TelephonyManagerEx.getDefault().isFiveGASupported()) {
            FiveGAStatistics.make();
        }
        this.mDelegate.setStatistic(MiuiTelephonyStatistic.make());
        MiuiCloudCarrierConfigManager.init();
        this.mDelegate.setCloudCarrierConfig(MiuiCloudCarrierConfigManager.getInstance());
        if (MiuiPhoneUtils.isSupportBadCellOptimization()) {
            BadCellStats.init(this.mContext);
        }
        if (HyperOSCustFeatureResolve.getBoolean("support_modem_cloud_controller", false)) {
            CloudModemControllerBase.init();
        }
    }
}

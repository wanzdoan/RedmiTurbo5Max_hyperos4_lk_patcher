package com.android.services.telephony;

import android.content.Context;
import com.android.phone.FastDormancy;
import com.android.phone.FiveGModemExpress;
import com.android.phone.FiveGModemStatusTracker;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemAiController;
import com.android.phone.ModemDelegateImpl;
import com.android.phone.ModemDump;
import com.android.phone.ModemPowerEstimate;
import com.android.phone.ModemProvider;
import com.android.phone.ModemSmartPowerSavingController;
import com.android.phone.ModemTcpKaFactory;
import com.android.phone.NoSimPowerController;
import com.android.phone.R;
import com.android.phone.dfs.MiuiTelephonyDFS;
import com.android.phone.statistics.AftersaleController;
import com.android.phone.statistics.ModemLogUploadTools;
import com.android.phone.statistics.ModemLogUploadToolsBase;
import com.android.phone.statistics.utils.ModemHydraStat;
import com.android.phone.utils.ModemDFS;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class ModemCoreModule implements ICoreModule {
    private Context mContext;
    private ModemDelegateImpl mDelegate;

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) throws Throwable {
        android.util.Log.d("ModemCoreModule", "onCreate");
        this.mContext = context;
        ModemLogUploadTools.make();
        if (ModemLogUploadToolsBase.getInstance() != null) {
            ModemLogUploadToolsBase.getInstance().init();
        }
        AftersaleController.make();
        ModemProvider.getInstance().getMainHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.ModemCoreModule$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ModemCoreModule.$r8$lambda$YeheaplDF20MsiQH0OI_db1V9eE();
            }
        }, this.mContext.getResources().getInteger(R.integer.config_hydra_init_delay));
        this.mDelegate = ModemDelegateImpl.init();
        if (MiuiPhoneUtils.isSupportNoSimPowerOptimization()) {
            NoSimPowerController.init(this.mContext);
        }
    }

    public static /* synthetic */ void $r8$lambda$YeheaplDF20MsiQH0OI_db1V9eE() {
        android.util.Log.d("ModemCoreModule", "Init ModemDump");
        ModemDump.init();
        android.util.Log.d("ModemCoreModule", "Init ModemHydraStat");
        ModemHydraStat.make();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("ModemCoreModule", "onSimPresent");
        if (MiuiPhoneUtils.isSupportedDFS()) {
            MiuiTelephonyDFS.getInstance();
            this.mDelegate.setModemDFS(ModemDFS.make());
        }
        this.mDelegate.setFastDormancy(FastDormancy.make());
        FiveGModemExpress.make();
        FiveGModemStatusTracker.init();
        ModemAiController.make();
        if (MiuiPhoneUtils.isModemPowerEstimateSupported()) {
            ModemPowerEstimate.getInstance();
        }
        if (MiuiPhoneUtils.isSupportTieredPowerSaving()) {
            ModemSmartPowerSavingController.init();
        }
        if (MiuiPhoneUtils.isDeviceModemTcpKaSupported()) {
            this.mDelegate.setTcpKa(ModemTcpKaFactory.make());
        }
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return HyperOSCustFeatureResolve.getBoolean("config_support_modem_module", context.getResources().getBoolean(R.bool.modem_module_enabled));
    }
}

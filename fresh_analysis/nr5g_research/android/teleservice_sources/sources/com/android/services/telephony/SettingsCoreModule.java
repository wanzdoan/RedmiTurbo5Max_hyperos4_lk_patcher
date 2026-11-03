package com.android.services.telephony;

import android.content.Context;
import com.android.phone.FiveGManagerBase;
import com.android.phone.FiveGNetworkScan;
import com.android.phone.MiuiAisSimLockManager;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneSettingsMigrateManager;
import com.android.phone.R;
import com.android.phone.SettingsDelegateImpl;
import com.android.phone.SingleFiveGController;
import com.android.phone.settings.VendorUtils;
import com.android.phone.utils.FiveGUtils;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class SettingsCoreModule implements ICoreModule {
    private Context mContext;
    private SettingsDelegateImpl mDelegate;

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("SettingsCoreModule", "onCreate");
        this.mContext = context;
        VendorUtils.make();
        MiuiAisSimLockManager.init(this.mContext);
        PhoneSettingsMigrateManager.init();
        this.mDelegate = SettingsDelegateImpl.init();
    }

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return HyperOSCustFeatureResolve.getBoolean("config_support_settings_module", context.getResources().getBoolean(R.bool.settings_module_enabled));
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("SettingsCoreModule", "onSimPresent");
        if (FiveGUtils.isSupportSingleFiveG()) {
            SingleFiveGController.init();
        }
        if (ModemProvider.IS_QCOM && FiveGManagerBase.isFiveGCapable()) {
            FiveGNetworkScan.init();
        }
    }
}

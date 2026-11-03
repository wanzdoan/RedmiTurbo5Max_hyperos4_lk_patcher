package com.android.services.telephony;

import android.content.Context;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.services.telephony.relay.network.NetworkRelayManager;
import com.android.services.telephony.relay.network.NetworkRelayStats;
import com.android.services.telephony.relay.offlinecall.OfflineCallsManager;
import com.android.services.telephony.relay.offlinecall.OfflineUtils;
import com.android.services.telephony.relay.phonecall.MiPhoneContinuityController;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayStarter;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.timezone.TimeZoneRelayManager;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.FeatureConfiguration;
import miui.telephony.TelephonyManagerEx;
import miui.util.ComputilityLevel;

/* JADX INFO: loaded from: classes.dex */
public class MiConnectCoreModule implements ICoreModule {
    private Context mContext;
    private MiConnectDelegateImpl mDelegate;
    private boolean mIsRelayDepsInitialized = false;

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("MiConnectCoreModule", "onCreate");
        this.mContext = context;
        RelayUtils.System.setContext(context);
        OfflineUtils.System.setContext(this.mContext);
        this.mDelegate = MiConnectDelegateImpl.init();
        if (RelayUtils.Relay.isRelaySide() || RelayUtils.Relay.supportDualPhoneRelayCallFeature(context)) {
            initRelayDependencies();
            this.mIsRelayDepsInitialized = true;
        }
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return HyperOSCustFeatureResolve.getBoolean("config_support_miconnect_module", context.getResources().getBoolean(R.bool.miconnect_module_enabled));
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("MiConnectCoreModule", "onSimPresent");
        if (this.mIsRelayDepsInitialized) {
            return;
        }
        initRelayDependencies();
        this.mIsRelayDepsInitialized = true;
    }

    private void initRelayDependencies() {
        initRelayService();
        initOfflineService();
        this.mDelegate.setPhoneContinuity(PhoneContinuityController.getInstance());
        this.mDelegate.setMiPhoneContinuity(MiPhoneContinuityController.getInstance());
        this.mDelegate.setOfflineCalls(OfflineCallsManager.getInstance());
        this.mDelegate.setNetworkRelayStats(NetworkRelayStats.getInstance());
    }

    private void initRelayService() {
        handleLyraServiceInit();
        if (TelephonyManagerEx.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_RELAY_NETWORK)) {
            NetworkRelayManager.getInstance(this.mContext).init();
        }
    }

    private void initOfflineService() {
        if (TelephonyManagerEx.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_OFFLINE_CALL)) {
            OfflineCallsManager.getInstance().init(this.mContext);
        }
    }

    public void handleLyraServiceInit() {
        try {
            RelayStarter.getInstance().start(this.mContext);
            android.util.Log.d("MiConnectCoreModule", "RelayStarter initialized.");
            if (!MiuiPhoneUtils.isPad() || !isPrimaryLocationTimeZoneProviderValid()) {
                if (MiuiPhoneUtils.isPad() || ComputilityLevel.getComputilityLevel() < 3) {
                    return;
                }
            }
            TimeZoneRelayManager.getInstance(this.mContext).init();
            android.util.Log.d("MiConnectCoreModule", "TimeZoneRelayManager initialized.");
        } catch (Exception e) {
            android.util.Log.e("MiConnectCoreModule", "Failed to init relay service", e);
            e.printStackTrace();
        }
    }

    private boolean isPrimaryLocationTimeZoneProviderValid() {
        try {
            return RelayUtils.Relay.PRIMARY_LOCATION_TIMEZONE_PROVIDER.equals(this.mContext.getResources().getString(android.R.string.device_storage_monitor_notification_channel));
        } catch (Exception unused) {
            return false;
        }
    }
}

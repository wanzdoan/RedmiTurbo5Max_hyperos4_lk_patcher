package com.android.services.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.phone.SatelliteDelegateImpl;
import com.android.phone.SatelliteManager;
import com.android.phone.beidou.BdSmsController;
import com.android.phone.ntn.NtnBoxServiceManager;
import com.android.phone.ntn.NtnLocationManager;
import com.android.phone.ntn.NtnOptimizeController;
import com.android.phone.ntn.NtnPhoneInfoMonitor;
import com.android.phone.ntn.NtnPhoneProxy;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteCoreModule implements ICoreModule {
    private Context mContext;
    private SatelliteDelegateImpl mDelegate;
    private BroadcastReceiver mReceiver;

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("SatelliteCoreModule", "onCreate");
        this.mContext = context;
        this.mDelegate = SatelliteDelegateImpl.init();
        SatelliteManager.make();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("SatelliteCoreModule", "onSimPresent");
        if (TelephonyManagerEx.getDefault().isSatelliteBox()) {
            NtnBoxServiceManager.init();
            this.mDelegate.setNtnBoxService(NtnBoxServiceManager.getInstance());
        }
        if (TelephonyManagerEx.getDefault().isSupportBeiDouByDevice()) {
            BdSmsController.init();
            this.mDelegate.setBdSmsController(BdSmsController.getInstance());
        }
        this.mReceiver = new NtnReceiver();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE), 2);
    }

    public static void wireHeavyDelegates(SatelliteDelegateImpl satelliteDelegateImpl) {
        satelliteDelegateImpl.setNtnPhoneProxy(NtnPhoneProxy.make());
        satelliteDelegateImpl.setNtnOptimize(NtnOptimizeController.make());
        satelliteDelegateImpl.setNtnLocationManager(NtnLocationManager.getInstance());
    }

    final class NtnReceiver extends BroadcastReceiver {
        private NtnReceiver(SatelliteCoreModule satelliteCoreModule) {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            android.util.Log.d("SatelliteCoreModule", "action = " + action);
            if (SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
                android.util.Log.d("SatelliteCoreModule", "receive ACTION_SATELLITE_STATE_CHANGE,isNtnEnabled: " + booleanExtra);
                if (booleanExtra) {
                    NtnPhoneInfoMonitor.getInstance().init();
                } else {
                    NtnPhoneInfoMonitor.getInstance().finish();
                }
            }
        }
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return TelephonyManagerEx.getDefault().isSupportSatelliteByDevice();
    }
}

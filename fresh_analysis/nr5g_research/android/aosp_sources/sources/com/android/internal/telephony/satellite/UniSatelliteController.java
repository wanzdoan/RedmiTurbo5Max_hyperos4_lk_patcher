package com.android.internal.telephony.satellite;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.IIntegerConsumer;
import android.telephony.UniCarrierConfigManager;
import android.telephony.satellite.ICombinedPositionUpdatesCallback;
import android.telephony.satellite.ISatelliteStateUpdateCallback;
import com.android.internal.telephony.UniTelephonyInjectionFactory;
import com.android.internal.telephony.util.WorkerThread;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class UniSatelliteController extends Handler {
    protected static final String KEY_5G_SATELLITE_MODE_ENABLED = "5g_satellite_mode_enabled";
    private static UniSatelliteController sInstance;
    public final CarrierConfigManager mCarrierConfigManager;
    public final Context mContext;

    public UniSatelliteController(Context context, Looper looper) {
        super(looper);
        this.mContext = context;
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public static UniSatelliteController getOrCreateInstance(Context context) {
        if (sInstance == null) {
            sInstance = UniTelephonyInjectionFactory.getInstance().makeUniSatelliteController(context, WorkerThread.get().getLooper());
        }
        return sInstance;
    }

    public PersistableBundle getConfigForSubId(int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            try {
                configForSubId = carrierConfigManager.getConfigForSubId(i, "carrier_default_satellite_network_type_int", "terrestrial_network_scan_while_satellite_mode_int", "enable_t2t_calling_indicator_bool");
            } catch (Exception e) {
                Rlog.e("UniSatelliteController", "getConfigForSubId: " + e);
                configForSubId = null;
            }
        } else {
            configForSubId = null;
        }
        return (configForSubId == null || configForSubId.isEmpty()) ? UniCarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    public void requestSatelliteNetworkType(int i, int i2, int i3, IIntegerConsumer iIntegerConsumer) {
        Rlog.d("UniSatelliteController", "requestSatelliteNetworkType subId: " + i + ", ntRadioTechnology: " + i2 + ", enableTerrestrialScan: " + i3);
    }

    public int getSatelliteNetworkType(int i) {
        Rlog.d("UniSatelliteController", "getSatelliteNetworkType subId: " + i);
        return getConfigForSubId(i).getInt("carrier_default_satellite_network_type_int", 0);
    }

    public int registerForSatelliteStateUpdates(int i, ISatelliteStateUpdateCallback iSatelliteStateUpdateCallback) {
        Rlog.d("UniSatelliteController", "registerForSatelliteStateUpdates subId: " + i);
        return 11;
    }

    public void unregisterForSatelliteStateUpdates(int i, ISatelliteStateUpdateCallback iSatelliteStateUpdateCallback) {
        Rlog.d("UniSatelliteController", "unregisterForSatelliteStateUpdates subId: " + i);
    }

    public void registerForCombinedPositionUpdates(int i, IIntegerConsumer iIntegerConsumer, ICombinedPositionUpdatesCallback iCombinedPositionUpdatesCallback) {
        Rlog.d("UniSatelliteController", "registerForCombinedPositionUpdates subId: " + i);
    }

    public void unregisterForCombinedPositionUpdates(int i, IIntegerConsumer iIntegerConsumer, ICombinedPositionUpdatesCallback iCombinedPositionUpdatesCallback) {
        Rlog.d("UniSatelliteController", "unregisterForCombinedPositionUpdates subId: " + i);
    }

    public boolean isT2TVoiceEnabled(int i) {
        Rlog.d("UniSatelliteController", "isT2TVoiceEnabled subId: " + i);
        return getConfigForSubId(i).getBoolean("enable_t2t_calling_indicator_bool", false);
    }

    public void setT2TVoiceEnabled(int i, boolean z) {
        Rlog.d("UniSatelliteController", "setT2TVoiceEnabled subId: " + i + ", isEnabled: " + z);
    }

    public void updateCatchLocationInfo(Location location, int i) {
        Rlog.d("UniSatelliteController", "updateCatchLocationInfo phoneId: " + i);
    }
}

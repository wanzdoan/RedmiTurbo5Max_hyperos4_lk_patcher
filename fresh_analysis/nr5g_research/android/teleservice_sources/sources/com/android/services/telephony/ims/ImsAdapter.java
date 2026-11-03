package com.android.services.telephony.ims;

import android.R;
import android.content.Context;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneGlobals;
import com.android.phone.VolteEnableManager;
import com.android.phone.ims.MiuiImsStatistics;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class ImsAdapter extends ImsAdapterBase {
    public static ImsAdapterBase init() {
        ImsAdapterBase.sInstance = new ImsAdapter();
        return ImsAdapterBase.sInstance;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setEnhanced4gLteModeSetting(Context context, boolean z) {
        int i = 0;
        if (!MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                if (i == defaultDataSlotId) {
                    setEnhanced4gLteModeSetting(context, z, i);
                } else {
                    int subscriptionId = SubscriptionManager.getSubscriptionId(i);
                    if (SubscriptionManager.from(context).isActiveSubId(subscriptionId) && (!MiuiPhoneUtils.isCTVoLTESupport(MiuiPhoneUtils.getPhone(i)) || VolteEnableManager.getInstance().getUserSelectedVolteState(subscriptionId) != -1)) {
                        setEnhanced4gLteModeSettingDB(context, subscriptionId, booleanToPropertyInt(z));
                    }
                }
                i++;
            }
            setEnhanced4gLteModeSettingDB(context, booleanToPropertyInt(z));
            return;
        }
        while (i < MiuiPhoneUtils.PHONE_COUNT) {
            setEnhanced4gLteModeSetting(context, z, i);
            i++;
        }
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setEnhanced4gLteModeSetting(Context context, boolean z, int i) {
        Log.d("ImsAdapter", "setEnhanced4gLteModeSetting slotId=" + i);
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            imsManager.setEnhanced4gLteModeSetting(z);
        }
        MiuiPhoneUtils.sendEnhanced4GLteModeChangeBroadcast(i, booleanToPropertyInt(z));
        MiuiPhoneUtils.queryVoNrStateAndSendModeChangeBroadcast(i);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setEnhanced4gLteModeSettingDB(Context context, int i, int i2) {
        SubscriptionManager.setSubscriptionProperty(i, "volte_vt_enabled", i2 + "");
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setEnhanced4gLteModeSettingDB(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "volte_vt_enabled", i);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isVolteSupportedByDevice(Context context) {
        if (SystemProperties.getInt("persist.dbg.volte_avail_ovr", 0) == 1 || ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            return true;
        }
        return context.getResources().getBoolean(R.bool.config_disable_all_cb_messages);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isVideoEnabled(Phone phone) {
        return phone.isVideoEnabled();
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean getEnhanced4gLteModeSetting(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        }
        return false;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setWfcSetting(Context context, int i, boolean z) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            imsManager.setWfcSetting(z);
            if (z) {
                MiuiImsStatistics.recordOpenVoWiFiEvent(i);
            }
        }
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isWfcEnabledByUser(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.isWfcEnabledByUser();
        }
        return false;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isWfcSupportWifiOnly(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("carrier_wfc_supports_wifi_only_bool", false);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isWfcEnabledByPlatform(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.isWfcEnabledByPlatform();
        }
        return false;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean hideEnhanded4GLte(int i) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneGlobals.getInstance(), i, "hide_enhanced_4g_lte_bool", false);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isWfcModeEditable(Context context, int i) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(context, i, "editable_wfc_mode_bool", true);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isUseWfcHomeNetworkinRoaming(Context context, int i) {
        Log.d("ImsAdapter", "isUseWfcHomeNetworkinRoaming ");
        return CarrierConfigManagerEx.getBooleanConfigForSubId(context, i, "use_wfc_home_network_mode_in_roaming_network_bool", false);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setWfcMode(Context context, int i, int i2, boolean z) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            imsManager.setWfcMode(i2, z);
        }
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public int getWfcMode(Context context, int i, boolean z) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.getWfcMode(z);
        }
        return -1;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isWfcRoamingModeEditable(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("editable_wfc_roaming_mode_bool", false);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean hideWfc(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("carrier_hide_wfc_bool", false);
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isImsPreferredSupported(int i) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneGlobals.getInstance(), i, "carrier_wfc_supports_ims_preferred_bool");
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isCrossSimCallingEnabled(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.isCrossSimCallingEnabled();
        }
        return false;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public boolean isCrossSimCallingEnabledByUser(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            return imsManager.isCrossSimCallingEnabledByUser();
        }
        return false;
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setCrossSimCallingEnabled(Context context, int i, boolean z) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        if (imsManager != null) {
            imsManager.setCrossSimCallingEnabled(z);
        }
    }

    @Override // com.android.services.telephony.ims.ImsAdapterBase
    public void setCrossSimCallingDisabledByCloud(Context context) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            ImsManager imsManager = ImsManager.getInstance(context, i);
            if (imsManager != null && isCrossSimCallingEnabled(context, i)) {
                Log.d("ImsAdapter", "setCrossSimCallingDisabledByCloud phoneId: " + i);
                imsManager.setCrossSimCallingEnabled(false);
            }
        }
    }
}

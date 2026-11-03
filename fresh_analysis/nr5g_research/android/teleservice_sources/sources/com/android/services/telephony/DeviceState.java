package com.android.services.telephony;

import android.content.Context;
import android.provider.Settings;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class DeviceState {
    public boolean shouldCheckSimStateBeforeOutgoingCall(Context context) {
        return context.getResources().getBoolean(R.bool.config_checkSimStateBeforeOutgoingCall);
    }

    public boolean isSuplDdsSwitchRequiredForEmergencyCall(Context context) {
        return context.getResources().getBoolean(R.bool.config_gnss_supl_requires_default_data_for_emergency);
    }

    public boolean isRadioPowerDownAllowedOnBluetooth(Context context) {
        return context.getResources().getBoolean(R.bool.config_allowRadioPowerDownOnBluetooth);
    }

    public boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) > 0;
    }

    public int getCellOnStatus(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "cell_on", 1);
    }

    public boolean isTtyModeEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "preferred_tty_mode", 0) != 0;
    }
}

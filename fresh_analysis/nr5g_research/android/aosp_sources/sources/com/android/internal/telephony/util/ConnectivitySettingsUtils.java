package com.android.internal.telephony.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class ConnectivitySettingsUtils {
    public static final String L4S_DEVELOPER_OPTION = "l4s_developer_option";
    public static final String NETWORK_AVOID_BAD_WIFI = "network_avoid_bad_wifi";
    public static final String NETWORK_CARRIER_AWARE_AVOID_BAD_WIFI = "network_carrier_aware_avoid_bad_wifi";
    public static final String PRIVATE_DNS_DEFAULT_MODE = "private_dns_default_mode";
    public static final String PRIVATE_DNS_MODE = "private_dns_mode";
    public static final int PRIVATE_DNS_MODE_OFF = 1;
    public static final String PRIVATE_DNS_MODE_OFF_STRING = "off";
    public static final int PRIVATE_DNS_MODE_OPPORTUNISTIC = 2;
    public static final String PRIVATE_DNS_MODE_OPPORTUNISTIC_STRING = "opportunistic";
    public static final int PRIVATE_DNS_MODE_PROVIDER_HOSTNAME = 3;
    public static final String PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING = "hostname";
    public static final String PRIVATE_DNS_SPECIFIER = "private_dns_specifier";
    public static final String TAG = "ConnectivitySettingsUtils";

    public static String getPrivateDnsModeAsString(int i) {
        if (i == 1) {
            return PRIVATE_DNS_MODE_OFF_STRING;
        }
        if (i == 2) {
            return PRIVATE_DNS_MODE_OPPORTUNISTIC_STRING;
        }
        if (i == 3) {
            return PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING;
        }
        RILUtils$$ExternalSyntheticBUOutline0.m("Invalid private dns mode: ", i);
        return null;
    }

    private static int getPrivateDnsModeAsInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 2;
        }
        str.hashCode();
        if (str.equals(PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING)) {
            return 3;
        }
        return !str.equals(PRIVATE_DNS_MODE_OFF_STRING) ? 2 : 1;
    }

    public static String getAvoidBadWifiSettingKey(int i) {
        return "network_carrier_aware_avoid_bad_wifi/" + i;
    }

    public static int getPrivateDnsMode(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String string = Settings.Global.getString(contentResolver, PRIVATE_DNS_MODE);
        if (TextUtils.isEmpty(string)) {
            string = Settings.Global.getString(contentResolver, PRIVATE_DNS_DEFAULT_MODE);
        }
        return getPrivateDnsModeAsInt(string);
    }

    public static void setPrivateDnsMode(Context context, int i) {
        if (i != 1 && i != 2 && i != 3) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid private dns mode: ", i);
        } else {
            Settings.Global.putString(context.getContentResolver(), PRIVATE_DNS_MODE, getPrivateDnsModeAsString(i));
        }
    }

    public static String getPrivateDnsHostname(Context context) {
        return Settings.Global.getString(context.getContentResolver(), PRIVATE_DNS_SPECIFIER);
    }

    public static void setPrivateDnsHostname(Context context, String str) {
        Settings.Global.putString(context.getContentResolver(), PRIVATE_DNS_SPECIFIER, str);
    }

    @Deprecated
    public static void setNetworkLegacyGlobalAvoidBadWifiSetting(Context context, String str) {
        Settings.Global.putString(context.getContentResolver(), NETWORK_AVOID_BAD_WIFI, str);
    }

    @Deprecated
    public static String getNetworkLegacyGlobalAvoidBadWifiSetting(Context context) {
        return Settings.Global.getString(context.getContentResolver(), NETWORK_AVOID_BAD_WIFI);
    }

    public static void setNetworkAvoidBadWifiSetting(Context context, int i, String str) {
        Settings.Global.putString(context.getContentResolver(), getAvoidBadWifiSettingKey(i), str);
    }

    public static String getNetworkAvoidBadWifiSetting(Context context, int i) {
        return Settings.Global.getString(context.getContentResolver(), getAvoidBadWifiSettingKey(i));
    }

    public static int getNetworkAvoidBadWifiIntegerSetting(Context context, int i) {
        String networkAvoidBadWifiSetting = getNetworkAvoidBadWifiSetting(context, i);
        if (networkAvoidBadWifiSetting == null) {
            return 1;
        }
        return "1".equals(networkAvoidBadWifiSetting) ? 2 : 0;
    }

    public static int getL4sDeveloperOptionSetting(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), L4S_DEVELOPER_OPTION);
        if (string == null) {
            return 2;
        }
        return "1".equals(string) ? 1 : 0;
    }

    public static void setL4sDeveloperOptionSetting(Context context, int i) {
        String str;
        if (i == 0) {
            str = "0";
        } else if (i == 1) {
            str = "1";
        } else {
            if (i != 2) {
                RILUtils$$ExternalSyntheticBUOutline0.m("Invalid L4S status: ", i);
                return;
            }
            str = null;
        }
        Settings.Global.putString(context.getContentResolver(), L4S_DEVELOPER_OPTION, str);
    }
}

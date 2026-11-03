package com.xiaomi.modem.sar;

import android.os.SystemProperties;
import android.telephony.Rlog;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceStateListenerBase {
    private static final String LOG_TAG = "DeviceStateListenerBase";
    private static final String PROPERTY_DYNAMIC_SAR_LOG = "persist.vendor.radio.dynamic_sar_log";
    public static final int TYPE_AUDIO_RECEIVER_STATE = 5;
    public static final int TYPE_BATTERY_LEVEL_STATE = 26;
    public static final int TYPE_BLUETOOTH_PAN_STATE = 7;
    public static final int TYPE_BLUETOOTH_STATE = 8;
    public static final int TYPE_CABLE_STATE = 10;
    public static final int TYPE_DSI_VALUE_STATE = 29;
    public static final int TYPE_GPD_SENSOR_STATE = 17;
    public static final int TYPE_GRAVITY_Y_AXES_STATE = 18;
    public static final int TYPE_HALL_SENSOR2_STATE = 20;
    public static final int TYPE_HALL_SENSOR_STATE = 4;
    public static final int TYPE_HORIZONTAL_GAME_LIST_STATE = 28;
    public static final int TYPE_HOTSPOT_STATE = 1;
    public static final int TYPE_HST_ENHANCEMENT_STATE = 30;
    public static final int TYPE_LANDSCAPE_GAME_STATE = 25;
    public static final int TYPE_MCC_STATE = 6;
    public static final int TYPE_MODEM_STATE = 2;
    public static final int TYPE_NTN_RADIO_POWER_STATE = 19;
    public static final int TYPE_RADIOPOWER_STATE = 13;
    public static final int TYPE_SAR_SENSOR2_STATE = 9;
    public static final int TYPE_SAR_SENSOR2_USE_SENSOR1_STATE = 21;
    public static final int TYPE_SAR_SENSOR3_STATE = 22;
    public static final int TYPE_SAR_SENSOR_STATE = 3;
    public static final int TYPE_SATELLITE_SWITCH_STATE = 23;
    public static final int TYPE_SCREEN_STATE = 24;
    public static final int TYPE_SIMCARD_STATE = 14;
    public static final int TYPE_SIM_MCC_STATE = 27;
    public static final int TYPE_SMART_TX_STATE = 15;
    public static final int TYPE_USB_STATE = 11;
    public static final int TYPE_USE_CASE_TEST = 12;
    public static final int TYPE_WHITE_LIST_STATE = 16;
    public static final int TYPE_WIFI_STATE = 0;

    public static String getDeviceEventTypeStringEx(int i) {
        String deviceEventTypeString = getDeviceEventTypeString(i);
        if (deviceEventTypeString != null) {
            deviceEventTypeString = "[" + i + "]" + deviceEventTypeString;
        }
        logi(LOG_TAG, "getDeviceEventTypeStringEx, eventType = " + i + ", string = " + deviceEventTypeString);
        return deviceEventTypeString;
    }

    public static String getDeviceEventTypeString(int i) {
        if (i == 0) {
            return "WIFI_STATE";
        }
        if (1 == i) {
            return "HOTSPOT_STATE";
        }
        if (2 == i) {
            return "MODEM_STATE";
        }
        if (3 == i) {
            return "SAR_SENSOR_STATE";
        }
        if (4 == i) {
            return "HALL_SENSOR_STATE";
        }
        if (5 == i) {
            return "AUDIO_RECEIVER_STATE";
        }
        if (6 == i) {
            return "MCC_STATE";
        }
        if (7 == i) {
            return "BLUETOOTH_PAN_STATE";
        }
        if (8 == i) {
            return "BLUETOOTH_STATE";
        }
        if (9 == i) {
            return "SAR_SENSOR2_STATE";
        }
        if (10 == i) {
            return "CABLE_STATE";
        }
        if (11 == i) {
            return "USB_STATE";
        }
        if (12 == i) {
            return "USE_CASE_TEST";
        }
        if (13 == i) {
            return "RADIOPOWER_STATE";
        }
        if (14 == i) {
            return "SIMCARD_STATE";
        }
        if (15 == i) {
            return "SMART_TX_STATE";
        }
        if (16 == i) {
            return "WHITE_LIST_STATE";
        }
        if (17 == i) {
            return "GPD_SENSOR_STATE";
        }
        if (18 == i) {
            return "GRAVITY_Y_AXES_STATE";
        }
        if (19 == i) {
            return "NTN_RADIO_POWER_STATE";
        }
        if (20 == i) {
            return "HALL_SENSOR2_STATE";
        }
        if (21 == i) {
            return "SAR_SENSOR2_ONE_TYPE";
        }
        if (22 == i) {
            return "SAR_SENSOR3_STATE";
        }
        if (23 == i) {
            return "SATELLITE_SWITCH_STATE";
        }
        if (24 == i) {
            return "TYPE_SCREEN_STATE";
        }
        if (25 == i) {
            return "LANDSCAPE_GAME_STATE";
        }
        if (26 == i) {
            return "BATTERY_LEVEL_STATE";
        }
        if (27 == i) {
            return "SIM_MCC_STATE";
        }
        if (28 == i) {
            return "HORIZONTAL_GAME_LIST_STATE";
        }
        if (29 == i) {
            return "DSI_VALUE_STATE";
        }
        if (30 == i) {
            return "HST_ENHANCEMENT_STATE";
        }
        return null;
    }

    private static void logi(String str, String str2) {
        if (SystemProperties.getInt(PROPERTY_DYNAMIC_SAR_LOG, 0) > 2) {
            Rlog.i(str, str2);
        }
    }
}

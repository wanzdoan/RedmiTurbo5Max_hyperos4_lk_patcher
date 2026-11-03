package com.xiaomi.modem.sar;

import android.os.SystemProperties;
import android.telephony.Rlog;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class DeviceStateListenerBase {
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

    public static String getDeviceEventTypeStringEx(int eventType) {
        String strType = getDeviceEventTypeString(eventType);
        if (strType != null) {
            strType = "[" + eventType + "]" + strType;
        }
        logi(LOG_TAG, "getDeviceEventTypeStringEx, eventType = " + eventType + ", string = " + strType);
        return strType;
    }

    public static String getDeviceEventTypeString(int eventType) {
        if (eventType == 0) {
            String strType = "WIFI_STATE";
            return strType;
        }
        if (1 == eventType) {
            String strType2 = "HOTSPOT_STATE";
            return strType2;
        }
        if (2 == eventType) {
            String strType3 = "MODEM_STATE";
            return strType3;
        }
        if (3 == eventType) {
            String strType4 = "SAR_SENSOR_STATE";
            return strType4;
        }
        if (4 == eventType) {
            String strType5 = "HALL_SENSOR_STATE";
            return strType5;
        }
        if (5 == eventType) {
            String strType6 = "AUDIO_RECEIVER_STATE";
            return strType6;
        }
        if (6 == eventType) {
            String strType7 = "MCC_STATE";
            return strType7;
        }
        if (7 == eventType) {
            String strType8 = "BLUETOOTH_PAN_STATE";
            return strType8;
        }
        if (8 == eventType) {
            String strType9 = "BLUETOOTH_STATE";
            return strType9;
        }
        if (9 == eventType) {
            String strType10 = "SAR_SENSOR2_STATE";
            return strType10;
        }
        if (10 == eventType) {
            String strType11 = "CABLE_STATE";
            return strType11;
        }
        if (11 == eventType) {
            String strType12 = "USB_STATE";
            return strType12;
        }
        if (12 == eventType) {
            String strType13 = "USE_CASE_TEST";
            return strType13;
        }
        if (13 == eventType) {
            String strType14 = "RADIOPOWER_STATE";
            return strType14;
        }
        if (14 == eventType) {
            String strType15 = "SIMCARD_STATE";
            return strType15;
        }
        if (15 == eventType) {
            String strType16 = "SMART_TX_STATE";
            return strType16;
        }
        if (16 == eventType) {
            String strType17 = "WHITE_LIST_STATE";
            return strType17;
        }
        if (17 == eventType) {
            String strType18 = "GPD_SENSOR_STATE";
            return strType18;
        }
        if (18 == eventType) {
            String strType19 = "GRAVITY_Y_AXES_STATE";
            return strType19;
        }
        if (19 == eventType) {
            String strType20 = "NTN_RADIO_POWER_STATE";
            return strType20;
        }
        if (20 == eventType) {
            String strType21 = "HALL_SENSOR2_STATE";
            return strType21;
        }
        if (21 == eventType) {
            String strType22 = "SAR_SENSOR2_ONE_TYPE";
            return strType22;
        }
        if (22 == eventType) {
            String strType23 = "SAR_SENSOR3_STATE";
            return strType23;
        }
        if (23 == eventType) {
            String strType24 = "SATELLITE_SWITCH_STATE";
            return strType24;
        }
        if (24 == eventType) {
            String strType25 = "TYPE_SCREEN_STATE";
            return strType25;
        }
        if (25 == eventType) {
            String strType26 = "LANDSCAPE_GAME_STATE";
            return strType26;
        }
        if (26 == eventType) {
            String strType27 = "BATTERY_LEVEL_STATE";
            return strType27;
        }
        if (27 == eventType) {
            String strType28 = "SIM_MCC_STATE";
            return strType28;
        }
        if (28 == eventType) {
            String strType29 = "HORIZONTAL_GAME_LIST_STATE";
            return strType29;
        }
        if (29 == eventType) {
            String strType30 = "DSI_VALUE_STATE";
            return strType30;
        }
        if (30 == eventType) {
            String strType31 = "HST_ENHANCEMENT_STATE";
            return strType31;
        }
        return null;
    }

    private static void logi(String logTag, String msg) {
        if (SystemProperties.getInt("persist.vendor.radio.dynamic_sar_log", 0) > 2) {
            Rlog.i(logTag, msg);
        }
    }
}

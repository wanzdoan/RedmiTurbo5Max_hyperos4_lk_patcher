package com.android.phone.settings;

import android.content.ContentResolver;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonySettings {
    public static boolean isSensorAnswerEnabled(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "sensor_answer", false);
    }

    public static void setSensorAnswerEnabled(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "sensor_answer", z);
    }

    public static boolean isFoldAnswerEnabled(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "fold_answer", false);
    }

    public static void setFoldAnswerEnabled(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "fold_answer", z);
    }

    public static boolean isUnFoldTurnOnSpeakerEnabled(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "unfold_turn_on_speaker", false);
    }

    public static void setUnFoldTurnOnSpeakerEnabled(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "unfold_turn_on_speaker", z);
    }

    public static boolean isUnFoldCloseCameraEnabled(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "unfold_close_camera", false);
    }

    public static void setUnFoldCloseCameraEnabled(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "unfold_close_camera", z);
    }

    public static boolean iseUnFoldShowInCallBubble(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "unfold_show_incall_bubble", false);
    }

    public static void setUnFoldShowInCallBubbelEnabled(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "unfold_show_incall_bubble", z);
    }

    public static boolean isRelayCallEnable(ContentResolver contentResolver) {
        return MiuiSettings.Secure.getBoolean(contentResolver, "pref_key_connectivity_relay_call_state", true);
    }

    public static boolean isRelayDialEnable(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "relay_dial_call", false);
    }

    public static boolean isConnectivityServiceEnable(ContentResolver contentResolver) {
        return MiuiSettings.Secure.getBoolean(contentResolver, RelayUtils.Relay.CONNECTION_SERVICE, false);
    }

    public static boolean isFlipAutoAnswerCall(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "flip_auto_answer_call", true);
    }

    public static void setFlipAutoAnswerCall(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "flip_auto_answer_call", z);
    }

    public static boolean isFlipAutoHangupCall(ContentResolver contentResolver) {
        return MiuiSettings.System.getBoolean(contentResolver, "flip_auto_hangup_call", true);
    }

    public static void setFlipAutoHangupCall(ContentResolver contentResolver, boolean z) {
        MiuiSettings.System.putBoolean(contentResolver, "flip_auto_hangup_call", z);
    }

    public static int getFlipAutoAnswerCallCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "flip_auto_answer_call_count", 0);
    }

    public static void setFlipAutoAnswerCallCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "flip_auto_answer_call_count", i);
    }

    public static int getFlipAutoHangupCallCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "flip_auto_hangup_call_count", 0);
    }

    public static void setFlipAutoHangupCallCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "flip_auto_hangup_call_count", i);
    }

    public static int getFlipMistakeHangupCallCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "flip_mistake_hangup_call_count", 0);
    }

    public static void setFlipMistakeHangupCallCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "flip_mistake_hangup_call_count", i);
    }

    public static boolean isLocationTzidDetcetion(ContentResolver contentResolver) {
        try {
            return Settings.Secure.getInt(contentResolver, "location_time_zone_detection_enabled") != 0;
        } catch (Exception unused) {
        }
    }

    public static int getTzidChangedByLocationCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "tzid_changed_count_by_location", 0);
    }

    public static void setTzidChangedByLocationCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "tzid_changed_count_by_location", i);
    }

    public static int getTzidChangedByRelayCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "tzid_changed_count_by_relay", 0);
    }

    public static void setTzidChangedByRelayCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "tzid_changed_count_by_relay", i);
    }

    public static int getTzidWIFIProviderNullCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "tzid_wifi_provider_null", 0);
    }

    public static void setTzidWIFIProviderNullCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "tzid_wifi_provider_null", i);
    }

    public static int getTzidWIFICoordInvalidCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "tzid_wifi_coord_invalid", 0);
    }

    public static void setTzidWIFICoordInvalidCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "tzid_wifi_coord_invalid", i);
    }

    public static int getTzidTimeShapeResultNullCount(ContentResolver contentResolver) {
        return Settings.Global.getInt(contentResolver, "tzid_timeshape_result_null", 0);
    }

    public static void setTzidTimeShapeResultNullCount(ContentResolver contentResolver, int i) {
        Settings.Global.putInt(contentResolver, "tzid_timeshape_result_null", i);
    }

    public static boolean isRttOn(ContentResolver contentResolver) {
        boolean z = MiuiSettings.System.getBoolean(contentResolver, "rtt_call", false);
        Log.i("TelephonySettings", "isRttOn: " + z);
        return z;
    }

    public static void setRttOn(ContentResolver contentResolver, boolean z) {
        Log.i("TelephonySettings", "setRttOn: " + z);
        MiuiSettings.System.putBoolean(contentResolver, "rtt_call", z);
    }
}

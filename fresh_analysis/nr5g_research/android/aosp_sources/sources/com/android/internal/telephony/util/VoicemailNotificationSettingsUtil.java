package com.android.internal.telephony.util;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class VoicemailNotificationSettingsUtil {
    public static void setRingtoneUri(Context context, Uri uri) {
    }

    public static void setVibrationEnabled(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean(getVoicemailVibrationSharedPrefsKey(), z);
        editorEdit.commit();
    }

    public static boolean isVibrationEnabled(Context context) {
        NotificationChannel channel = NotificationChannelController.getChannel(NotificationChannelController.CHANNEL_ID_VOICE_MAIL, context);
        return channel != null ? channel.shouldVibrate() : getVibrationPreference(context);
    }

    public static boolean getVibrationPreference(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        migrateVoicemailVibrationSettingsIfNeeded(context, defaultSharedPreferences);
        return defaultSharedPreferences.getBoolean(getVoicemailVibrationSharedPrefsKey(), false);
    }

    public static Uri getRingtoneUri(Context context) {
        NotificationChannel channel = NotificationChannelController.getChannel(NotificationChannelController.CHANNEL_ID_VOICE_MAIL, context);
        return channel != null ? channel.getSound() : Settings.System.DEFAULT_NOTIFICATION_URI;
    }

    private static void migrateVoicemailVibrationSettingsIfNeeded(Context context, SharedPreferences sharedPreferences) {
        String voicemailVibrationSharedPrefsKey = getVoicemailVibrationSharedPrefsKey();
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(context);
        if (sharedPreferences.contains(voicemailVibrationSharedPrefsKey) || telephonyManagerFrom.getPhoneCount() != 1) {
            return;
        }
        if (sharedPreferences.contains("button_voicemail_notification_vibrate_key")) {
            sharedPreferences.edit().putBoolean(voicemailVibrationSharedPrefsKey, sharedPreferences.getBoolean("button_voicemail_notification_vibrate_key", false)).remove("button_voicemail_notification_vibrate_when_key").commit();
        }
        if (sharedPreferences.contains("button_voicemail_notification_vibrate_when_key")) {
            sharedPreferences.edit().putBoolean(voicemailVibrationSharedPrefsKey, sharedPreferences.getString("button_voicemail_notification_vibrate_when_key", "never").equals("always")).remove("button_voicemail_notification_vibrate_key").commit();
        }
    }

    private static String getVoicemailVibrationSharedPrefsKey() {
        return "voicemail_notification_vibrate_" + SubscriptionManager.getDefaultSubscriptionId();
    }
}

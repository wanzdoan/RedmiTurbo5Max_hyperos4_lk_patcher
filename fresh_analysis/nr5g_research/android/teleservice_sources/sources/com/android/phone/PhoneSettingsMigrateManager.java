package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import android.text.TextUtils;
import androidx.preference.PreferenceManager;
import com.android.internal.os.BackgroundThread;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSettingsMigrateManager {
    private static String LOG_TAG = "PhoneSettingsMigrateManager";
    private static PhoneSettingsMigrateManager sInstance;
    private Context mContext = PhoneGlobals.getInstance();
    private MigrateHandler mHandler;

    public static void init() {
        if (!PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getBoolean("preference_migrate_to_settings_complete", false) && sInstance == null) {
            sInstance = new PhoneSettingsMigrateManager();
        }
    }

    private PhoneSettingsMigrateManager() {
        MigrateHandler migrateHandler = new MigrateHandler(BackgroundThread.get().getLooper());
        this.mHandler = migrateHandler;
        migrateHandler.sendEmptyMessage(0);
    }

    public class MigrateHandler extends Handler {
        public MigrateHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            try {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneSettingsMigrateManager.this.mContext);
                SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                int i = 0;
                if (defaultSharedPreferences.getBoolean("button_auto_answer", false)) {
                    MiuiSettings.Telephony.setAutoAnswerCall(PhoneSettingsMigrateManager.this.mContext.getContentResolver(), true);
                    editorEdit.remove("button_auto_answer");
                    Rlog.d(PhoneSettingsMigrateManager.LOG_TAG, "migrate auto answer switcher 'true' to MiuiSettings");
                }
                int i2 = Integer.parseInt(defaultSharedPreferences.getString("button_auto_answer_delay", "-1"));
                if (i2 > -1) {
                    MiuiSettings.Telephony.setAutoAnswerCallDelayTime(PhoneSettingsMigrateManager.this.mContext.getContentResolver(), i2);
                    editorEdit.remove("button_auto_answer_delay");
                    Rlog.d(PhoneSettingsMigrateManager.LOG_TAG, "migrate auto answer delay time: " + i2 + " to MiuiSettings");
                }
                int i3 = defaultSharedPreferences.getInt("button_auto_answer_scenario", -1);
                if (i3 > -1) {
                    MiuiSettings.Telephony.setAutoAnswerCallScenario(PhoneSettingsMigrateManager.this.mContext.getContentResolver(), i3);
                    editorEdit.remove("button_auto_answer_scenario");
                    Rlog.d(PhoneSettingsMigrateManager.LOG_TAG, "migrate auto answer scenario: " + i3 + " to MiuiSettings");
                }
                SharedPreferences sharedPreferences = PhoneSettingsMigrateManager.this.mContext.getSharedPreferences("respond_via_sms_prefs", 0);
                boolean z = false;
                while (i < 4) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("canned_response_pref_");
                    i++;
                    sb.append(i);
                    String string = sharedPreferences.getString(sb.toString(), "");
                    if (!TextUtils.isEmpty(string)) {
                        MiuiSettings.Telephony.setRejectViaSmsRespone(PhoneSettingsMigrateManager.this.mContext.getContentResolver(), "reject_via_sms_response_" + i, string);
                        Rlog.d(PhoneSettingsMigrateManager.LOG_TAG, "migrate response via sms " + i + ": " + string + " to MiuiSettings");
                        z = true;
                    }
                }
                if (z) {
                    sharedPreferences.edit().clear().commit();
                }
                editorEdit.putBoolean("preference_migrate_to_settings_complete", true);
                editorEdit.commit();
            } catch (Exception e) {
                Rlog.e(PhoneSettingsMigrateManager.LOG_TAG, "handle EVENT_MIGRATE_SETTINGS e: " + e);
            }
        }
    }
}

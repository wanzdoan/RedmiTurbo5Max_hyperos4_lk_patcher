package com.android.phone.vvm;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.util.ArraySet;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class VisualVoicemailSmsFilterConfig {
    public static void enableVisualVoicemailSmsFilter(Context context, String str, int i, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings) {
        new Editor(context, str, i).setBoolean("_enabled", true).setString("_prefix", visualVoicemailSmsFilterSettings.clientPrefix).setStringList("_originating_numbers", visualVoicemailSmsFilterSettings.originatingNumbers).setInt("_destination_port", visualVoicemailSmsFilterSettings.destinationPort).apply();
    }

    public static void disableVisualVoicemailSmsFilter(Context context, String str, int i) {
        new Editor(context, str, i).setBoolean("_enabled", false).apply();
    }

    public static VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(Context context, int i) {
        String packageName;
        ComponentName remotePackage = RemoteVvmTaskManager.getRemotePackage(context, i);
        if (remotePackage == null) {
            packageName = RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME;
        } else {
            packageName = remotePackage.getPackageName();
        }
        return getVisualVoicemailSmsFilterSettings(context, packageName, i);
    }

    public static VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(Context context, String str, int i) {
        Reader reader = new Reader(context, str, i);
        if (reader.getBoolean("_enabled", false)) {
            return new VisualVoicemailSmsFilterSettings.Builder().setClientPrefix(reader.getString("_prefix", "//VVM")).setOriginatingNumbers(reader.getStringSet("_originating_numbers", VisualVoicemailSmsFilterSettings.DEFAULT_ORIGINATING_NUMBERS)).setDestinationPort(reader.getInt("_destination_port", -1)).setPackageName(str).build();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.createDeviceProtectedStorageContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String makePerPhoneAccountKeyPrefix(String str, int i) {
        return "vvm_sms_filter_config_" + str + "_" + i;
    }

    class Editor {
        private final String mKeyPrefix;
        private final SharedPreferences.Editor mPrefsEditor;

        public Editor(Context context, String str, int i) {
            this.mPrefsEditor = VisualVoicemailSmsFilterConfig.getSharedPreferences(context).edit();
            this.mKeyPrefix = VisualVoicemailSmsFilterConfig.makePerPhoneAccountKeyPrefix(str, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Editor setInt(String str, int i) {
            this.mPrefsEditor.putInt(makeKey(str), i);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Editor setString(String str, String str2) {
            this.mPrefsEditor.putString(makeKey(str), str2);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Editor setBoolean(String str, boolean z) {
            this.mPrefsEditor.putBoolean(makeKey(str), z);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Editor setStringList(String str, List list) {
            this.mPrefsEditor.putStringSet(makeKey(str), new ArraySet(list));
            return this;
        }

        public void apply() {
            this.mPrefsEditor.apply();
        }

        private String makeKey(String str) {
            return this.mKeyPrefix + str;
        }
    }

    class Reader {
        private final String mKeyPrefix;
        private final SharedPreferences mPrefs;

        public Reader(Context context, String str, int i) {
            this.mPrefs = VisualVoicemailSmsFilterConfig.getSharedPreferences(context);
            this.mKeyPrefix = VisualVoicemailSmsFilterConfig.makePerPhoneAccountKeyPrefix(str, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getInt(String str, int i) {
            return this.mPrefs.getInt(makeKey(str), i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getString(String str, String str2) {
            return this.mPrefs.getString(makeKey(str), str2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getBoolean(String str, boolean z) {
            return this.mPrefs.getBoolean(makeKey(str), z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List getStringSet(String str, List list) {
            Set<String> stringSet = this.mPrefs.getStringSet(makeKey(str), null);
            return stringSet == null ? list : new ArrayList(stringSet);
        }

        private String makeKey(String str) {
            return this.mKeyPrefix + str;
        }
    }
}

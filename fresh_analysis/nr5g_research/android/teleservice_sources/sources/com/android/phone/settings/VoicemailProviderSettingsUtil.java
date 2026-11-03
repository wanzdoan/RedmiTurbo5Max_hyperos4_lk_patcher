package com.android.phone.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CallForwardInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class VoicemailProviderSettingsUtil {
    private static final String LOG_TAG = "VoicemailProviderSettingsUtil";

    public static VoicemailProviderSettings load(Context context, String str) {
        SharedPreferences prefs = getPrefs(context);
        String string = prefs.getString(str + "#VMNumber", null);
        if (string == null) {
            Log.w(LOG_TAG, "VoiceMailProvider settings for the key \"" + str + "\" were not found. Returning null.");
            return null;
        }
        CallForwardInfo[] callForwardInfoArr = VoicemailProviderSettings.NO_FORWARDING;
        String str2 = str + "#FWDSettings";
        int i = prefs.getInt(str2.concat("#Length"), 0);
        if (i > 0) {
            callForwardInfoArr = new CallForwardInfo[i];
            for (int i2 = 0; i2 < i; i2++) {
                String str3 = str2 + "#Setting" + String.valueOf(i2);
                CallForwardInfo callForwardInfo = new CallForwardInfo();
                callForwardInfoArr[i2] = callForwardInfo;
                callForwardInfo.status = prefs.getInt(str3.concat("#Status"), 0);
                callForwardInfoArr[i2].reason = prefs.getInt(str3.concat("#Reason"), 5);
                CallForwardInfo callForwardInfo2 = callForwardInfoArr[i2];
                callForwardInfo2.serviceClass = 1;
                callForwardInfo2.toa = 145;
                callForwardInfo2.number = prefs.getString(str3.concat("#Number"), "");
                callForwardInfoArr[i2].timeSeconds = prefs.getInt(str3.concat("#Time"), 20);
            }
        }
        return new VoicemailProviderSettings(string, callForwardInfoArr);
    }

    public static void save(Context context, String str, VoicemailProviderSettings voicemailProviderSettings) {
        if (voicemailProviderSettings.equals(load(context, str))) {
            return;
        }
        SharedPreferences.Editor editorEdit = getPrefs(context).edit();
        editorEdit.putString(str + "#VMNumber", voicemailProviderSettings.getVoicemailNumber());
        String str2 = str + "#FWDSettings";
        CallForwardInfo[] forwardingSettings = voicemailProviderSettings.getForwardingSettings();
        if (forwardingSettings != VoicemailProviderSettings.NO_FORWARDING) {
            editorEdit.putInt(str2.concat("#Length"), forwardingSettings.length);
            for (int i = 0; i < forwardingSettings.length; i++) {
                String str3 = str2 + "#Setting" + String.valueOf(i);
                CallForwardInfo callForwardInfo = forwardingSettings[i];
                editorEdit.putInt(str3.concat("#Status"), callForwardInfo.status);
                editorEdit.putInt(str3.concat("#Reason"), callForwardInfo.reason);
                editorEdit.putString(str3.concat("#Number"), callForwardInfo.number);
                editorEdit.putInt(str3.concat("#Time"), callForwardInfo.timeSeconds);
            }
        } else {
            editorEdit.putInt(str2.concat("#Length"), 0);
        }
        editorEdit.apply();
    }

    public static void delete(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        getPrefs(context).edit().putString(str + "#VMNumber", null).putInt(str + "#FWDSettings#Length", 0).apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("vm_numbers", 0);
    }
}

package com.android.phone.settings.cloudbackup;

import android.content.Context;
import android.provider.MiuiSettings;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AutoAnswerBackupHelper {
    private static final String TAG = "AutoAnswerBackupHelper";

    public static void backupAutoAnswer(Context context, DataPackage dataPackage) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("CKEnableAutoAnswer", MiuiSettings.Telephony.isAutoAnswerCall(context.getContentResolver()));
            jSONObject.put("CKAutoAnswerDelay", String.valueOf(MiuiSettings.Telephony.getAutoAnswerCallDelayTime(context.getContentResolver())));
            jSONObject.put("CKAutoAnswerScenario", MiuiSettings.Telephony.getAutoAnswerCallScenario(context.getContentResolver()));
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("AutoAnswer", jSONObject);
    }

    public static void restoreAutoAnswer(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("AutoAnswer") == null || (jSONObject = (JSONObject) dataPackage.get("AutoAnswer").getValue()) == null) {
            return;
        }
        if (jSONObject.has("CKEnableAutoAnswer")) {
            MiuiSettings.Telephony.setAutoAnswerCall(context.getContentResolver(), jSONObject.optBoolean("CKEnableAutoAnswer"));
        }
        if (jSONObject.has("CKAutoAnswerDelay")) {
            try {
                MiuiSettings.Telephony.setAutoAnswerCallDelayTime(context.getContentResolver(), Integer.valueOf(jSONObject.optString("CKAutoAnswerDelay")).intValue());
            } catch (NumberFormatException unused) {
            }
        }
        if (jSONObject.has("CKAutoAnswerScenario")) {
            MiuiSettings.Telephony.setAutoAnswerCallScenario(context.getContentResolver(), jSONObject.optInt("CKAutoAnswerScenario"));
        }
    }
}

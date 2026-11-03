package com.android.phone.settings.cloudbackup;

import android.content.Context;
import android.util.Log;
import com.android.phone.settings.TelephonySettings;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallFeaturesBackupHelper {
    private static final String TAG = "CallFeaturesBackupHelper";

    public static void backupCallFeatures(Context context, DataPackage dataPackage) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("CKEnableSensorAnswer", TelephonySettings.isSensorAnswerEnabled(context.getContentResolver()));
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("CallFeatures", jSONObject);
    }

    public static void restoreCallFeatures(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("CallFeatures") == null || (jSONObject = (JSONObject) dataPackage.get("CallFeatures").getValue()) == null || !jSONObject.has("CKEnableSensorAnswer")) {
            return;
        }
        TelephonySettings.setSensorAnswerEnabled(context.getContentResolver(), jSONObject.optBoolean("CKEnableSensorAnswer"));
    }
}

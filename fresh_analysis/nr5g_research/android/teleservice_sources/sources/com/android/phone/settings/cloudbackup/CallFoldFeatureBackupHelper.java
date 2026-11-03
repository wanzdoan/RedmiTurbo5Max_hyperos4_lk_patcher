package com.android.phone.settings.cloudbackup;

import android.content.Context;
import android.util.Log;
import com.android.phone.settings.TelephonySettings;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallFoldFeatureBackupHelper {
    private static final String TAG = "CallFoldFeatureBackupHelper";

    public static void backupCallFoldFeatures(Context context, DataPackage dataPackage) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("CKEnableFoldAnswer", TelephonySettings.isFoldAnswerEnabled(context.getContentResolver()));
            jSONObject.put("CKEnableUnFoldTurnonSpeaker", TelephonySettings.isUnFoldTurnOnSpeakerEnabled(context.getContentResolver()));
            jSONObject.put("CKEnableUnFoldColseCamera", TelephonySettings.isUnFoldCloseCameraEnabled(context.getContentResolver()));
            jSONObject.put("CKEnableUnFoldShowInCallBubble", TelephonySettings.iseUnFoldShowInCallBubble(context.getContentResolver()));
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("CallFoldFeatures", jSONObject);
    }

    public static void restoreCallFoldFeatures(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("CallFoldFeatures") == null || (jSONObject = (JSONObject) dataPackage.get("CallFoldFeatures").getValue()) == null) {
            return;
        }
        if (jSONObject.has("CKEnableFoldAnswer")) {
            TelephonySettings.setFoldAnswerEnabled(context.getContentResolver(), jSONObject.optBoolean("CKEnableFoldAnswer"));
        }
        if (jSONObject.has("CKEnableUnFoldTurnonSpeaker")) {
            TelephonySettings.setUnFoldTurnOnSpeakerEnabled(context.getContentResolver(), jSONObject.optBoolean("CKEnableUnFoldTurnonSpeaker"));
        }
        if (jSONObject.has("CKEnableUnFoldColseCamera")) {
            TelephonySettings.setUnFoldCloseCameraEnabled(context.getContentResolver(), jSONObject.optBoolean("CKEnableUnFoldColseCamera"));
        }
        if (jSONObject.has("CKEnableUnFoldShowInCallBubble")) {
            TelephonySettings.setUnFoldShowInCallBubbelEnabled(context.getContentResolver(), jSONObject.optBoolean("CKEnableUnFoldShowInCallBubble"));
        }
    }
}

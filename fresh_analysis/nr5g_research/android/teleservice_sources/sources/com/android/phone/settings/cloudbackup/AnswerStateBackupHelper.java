package com.android.phone.settings.cloudbackup;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MiuiSettings;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AnswerStateBackupHelper {
    private static final String TAG = "AnswerStateBackupHelper";

    public static void backupAnswerState(Context context, DataPackage dataPackage) {
        ContentResolver contentResolver = context.getContentResolver();
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
                jSONObject.put("CKEnableAntiStrange", MiuiSettings.Telephony.isAntispamStangerEnabled(contentResolver));
                jSONObject.put("CKEnableProximitySensor", MiuiSettings.Telephony.isProximitySensorEnable(contentResolver));
            }
            jSONObject.put("CKEnableFlashWhenRing", MiuiSettings.System.getBoolean(contentResolver, "flash_when_ring_enabled", false));
            jSONObject.put("CKEnableTurnOverMuteMode", MiuiSettings.Telephony.isTurnOverMuteEnabled(contentResolver));
            jSONObject.put("CKEnableHandonMode", MiuiSettings.Telephony.isHandonRingerEnabled(contentResolver));
            jSONObject.put("CKEnableCrescendoMode", MiuiSettings.Telephony.isCrescendoRingerEnable(contentResolver));
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("AnswerState", jSONObject);
    }

    public static void restoreAnswerState(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("AnswerState") == null || (jSONObject = (JSONObject) dataPackage.get("AnswerState").getValue()) == null) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
            if (jSONObject.has("CKEnableAntiStrange")) {
                MiuiSettings.Telephony.setAntispamStangerEnabled(contentResolver, jSONObject.optBoolean("CKEnableAntiStrange"));
            }
            if (jSONObject.has("CKEnableProximitySensor")) {
                MiuiSettings.Telephony.setProximitySensorEnable(contentResolver, jSONObject.optBoolean("CKEnableProximitySensor"));
            }
        }
        if (jSONObject.has("CKEnableFlashWhenRing")) {
            MiuiSettings.System.putBoolean(contentResolver, "flash_when_ring_enabled", jSONObject.optBoolean("CKEnableFlashWhenRing"));
        }
        if (jSONObject.has("CKEnableTurnOverMuteMode")) {
            MiuiSettings.Telephony.setTurnOverMuteEnabled(contentResolver, jSONObject.optBoolean("CKEnableTurnOverMuteMode"));
        }
        if (jSONObject.has("CKEnableHandonMode")) {
            MiuiSettings.Telephony.setHandonRingerEnabled(contentResolver, jSONObject.optBoolean("CKEnableHandonMode"));
        }
        if (jSONObject.has("CKEnableCrescendoMode")) {
            MiuiSettings.Telephony.setCrescendoRingerEnable(contentResolver, jSONObject.optBoolean("CKEnableCrescendoMode"));
        }
    }
}

package com.android.phone.settings.cloudbackup;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MiuiSettings;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import miui.enterprise.PhoneHelperStub;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallRecordBackupHelper {
    private static final String TAG = "CallRecordBackupHelper";

    public static void backupCallRecord(Context context, DataPackage dataPackage) {
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
            ContentResolver contentResolver = context.getContentResolver();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("CKEnableRecordingNotification", MiuiSettings.Telephony.isRecordNotificationEnabled(contentResolver));
                jSONObject.put("CKEnableAutoRecord", MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver));
                jSONObject.put("CKAutoRecordScenarios", MiuiSettings.Telephony.getRecordScenario(contentResolver));
                jSONObject.put("CKEnableUnknownNumberRecord", MiuiSettings.Telephony.isUnknownNumberRecordEnabled(contentResolver));
                jSONObject.put("CKEnableYellowPageNumberRecord", MiuiSettings.Telephony.isYellowpageRecordEnabled(contentResolver));
                jSONObject.put("CKAutoRecordWhiteList", MiuiSettings.Telephony.getRecordWhiteList(contentResolver));
            } catch (JSONException e) {
                Log.e(TAG, "Build JSON failed", e);
            }
            dataPackage.addKeyJson("CallRecord", jSONObject);
        }
    }

    public static void restoreFromCloud(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall()) {
            return;
        }
        if ((TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) || dataPackage.get("CallRecord") == null || (jSONObject = (JSONObject) dataPackage.get("CallRecord").getValue()) == null) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (jSONObject.has("CKEnableRecordingNotification")) {
            MiuiSettings.Telephony.setRecordNotificationEnabled(contentResolver, jSONObject.optBoolean("CKEnableRecordingNotification"));
        }
        if (jSONObject.has("CKEnableAutoRecord")) {
            MiuiSettings.Telephony.setAutoRecordEnabled(contentResolver, jSONObject.optBoolean("CKEnableAutoRecord"));
        }
        if (jSONObject.has("CKAutoRecordScenarios")) {
            MiuiSettings.Telephony.setRecordScenario(contentResolver, jSONObject.optInt("CKAutoRecordScenarios"));
        }
        if (jSONObject.has("CKEnableUnknownNumberRecord")) {
            MiuiSettings.Telephony.setUnknownNumberRecordEnabled(contentResolver, jSONObject.optBoolean("CKEnableUnknownNumberRecord"));
        }
        if (jSONObject.has("CKEnableYellowPageNumberRecord")) {
            MiuiSettings.Telephony.setYellowpageRecordEnabled(contentResolver, jSONObject.optBoolean("CKEnableYellowPageNumberRecord"));
        }
        if (jSONObject.has("CKAutoRecordWhiteList")) {
            MiuiSettings.Telephony.setRecordWhiteList(contentResolver, jSONObject.optString("CKAutoRecordWhiteList"));
        }
    }
}

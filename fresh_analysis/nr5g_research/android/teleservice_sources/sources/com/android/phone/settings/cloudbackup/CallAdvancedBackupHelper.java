package com.android.phone.settings.cloudbackup;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import com.android.phone.R;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import miui.enterprise.PhoneHelperStub;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallAdvancedBackupHelper {
    private static final String TAG = "CallAdvancedBackupHelper";

    public static void backupCallAdvanced(Context context, DataPackage dataPackage) {
        ContentResolver contentResolver = context.getContentResolver();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("CKEnableAutoRedial", MiuiSettings.Telephony.getEnabledAutoRedial(contentResolver));
            jSONObject.put("CKEnableConnectDisconnectVibrate", MiuiSettings.Telephony.getVibrateKey(contentResolver));
            jSONObject.put("CKCallWaitingTone", MiuiSettings.Telephony.getCallWaitingTone(context));
            if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
                jSONObject.put("CKT9IndexMethod", Settings.System.getInt(contentResolver, "t9_indexing_key", MiuiSettings.System.getT9IndexingKeyDefault()));
                jSONObject.put("CKMissedCallNotifyTime", MiuiSettings.Telephony.getMissedCallNotifyTimes(contentResolver));
                jSONObject.put("CKDTMFToneType", Settings.System.getInt(contentResolver, "dtmf_tone_type", 0));
                jSONObject.put("CKDTMFTone", Settings.System.getInt(contentResolver, "dtmf_tone", 0));
                jSONObject.put("CKDialPadTouchTone", Settings.System.getInt(contentResolver, "dial_pad_touch_tone", -1));
                jSONObject.put("CKInComingCallReport", Settings.Global.getInt(contentResolver, "voice_report_method_phone", 2));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("CallAdvanced", jSONObject);
    }

    public static void restoreCallAdvanced(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        int iOptInt;
        if (dataPackage.get("CallAdvanced") == null || (jSONObject = (JSONObject) dataPackage.get("CallAdvanced").getValue()) == null) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (jSONObject.has("CKEnableAutoRedial") && !PhoneHelperStub.getInstance().isDisableAutoRedial()) {
            MiuiSettings.Telephony.setAutoRedialEnabled(contentResolver, jSONObject.optBoolean("CKEnableAutoRedial"));
        }
        if (jSONObject.has("CKEnableConnectDisconnectVibrate")) {
            MiuiSettings.Telephony.setVibrateKey(contentResolver, jSONObject.optString("CKEnableConnectDisconnectVibrate"));
        }
        if (jSONObject.has("CKCallWaitingTone") && (iOptInt = jSONObject.optInt("CKCallWaitingTone")) < context.getResources().getStringArray(R.array.call_waiting_tone_entries).length) {
            MiuiSettings.Telephony.setCallWaitingTone(context, iOptInt);
        }
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
            if (jSONObject.has("CKT9IndexMethod")) {
                Settings.System.putInt(contentResolver, "t9_indexing_key", jSONObject.optInt("CKT9IndexMethod"));
            }
            if (jSONObject.has("CKMissedCallNotifyTime")) {
                MiuiSettings.Telephony.setMissedCallNotifyTimes(contentResolver, jSONObject.optInt("CKMissedCallNotifyTime"));
            }
            if (jSONObject.has("CKDTMFToneType")) {
                Settings.System.putInt(contentResolver, "dtmf_tone_type", jSONObject.optInt("CKDTMFToneType"));
            }
            if (jSONObject.has("CKDTMFTone")) {
                Settings.System.putInt(contentResolver, "dtmf_tone", jSONObject.optInt("CKDTMFTone"));
            }
            if (jSONObject.has("CKDialPadTouchTone")) {
                Settings.System.putInt(contentResolver, "dial_pad_touch_tone", jSONObject.optInt("CKDialPadTouchTone"));
            }
            if (jSONObject.has("CKInComingCallReport")) {
                Settings.Global.putInt(contentResolver, "voice_report_method_phone", jSONObject.optInt("CKInComingCallReport"));
            }
        }
    }
}

package com.android.phone.settings.cloudbackup;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MiuiSettings;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import java.util.Map;
import miui.telephony.TelephonyManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AutoIpBackupHelper {
    private static final String TAG = "AutoIpBackupHelper";

    public static void backupAutoIp(Context context, DataPackage dataPackage) {
        ContentResolver contentResolver = context.getContentResolver();
        JSONObject jSONObject = new JSONObject();
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                jSONObject.put("CKEnableAutoIp" + i, MiuiSettings.Telephony.isAutoIpEnable(contentResolver, i));
                jSONObject.put("CKCurrentAreaCode" + i, MiuiSettings.Telephony.getCurrentAeraCode(contentResolver, i));
                jSONObject.put("CKSupportLocalNumber" + i, MiuiSettings.Telephony.isAutoIpSupportLocalNum(contentResolver, i));
                jSONObject.put("CKEnableAddZeroPrefix" + i, MiuiSettings.Telephony.isAutoAddZeroPrefix(contentResolver, i));
            } catch (JSONException e) {
                Log.e(TAG, "Build JSON failed", e);
            }
        }
        dataPackage.addKeyJson("AutoIp", jSONObject);
        backupAutoIpExceptionNumbers(context, dataPackage);
    }

    private static void backupAutoIpExceptionNumbers(Context context, DataPackage dataPackage) {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
                Map<String, ?> all = getSharedPreferences(context, i).getAll();
                JSONArray jSONArray = new JSONArray();
                for (Map.Entry<String, ?> entry : all.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("number", entry.getKey());
                        jSONObject3.put("name", entry.getValue());
                        jSONArray.put(jSONObject3);
                    }
                }
                jSONObject2.put("autoIpSlot" + i, jSONArray);
            }
            jSONObject.put("autoIpNumbers", jSONObject2);
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
        dataPackage.addKeyJson("AutoIpExceptionNumbers", jSONObject);
    }

    public static void restoreAutoIp(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("AutoIp") == null || (jSONObject = (JSONObject) dataPackage.get("AutoIp").getValue()) == null) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (jSONObject.has("CKEnableAutoIp" + i)) {
                MiuiSettings.Telephony.setAutoIpEnable(contentResolver, jSONObject.optBoolean("CKEnableAutoIp" + i), i);
            }
            if (jSONObject.has("CKCurrentAreaCode" + i)) {
                MiuiSettings.Telephony.setCurrentAeraCode(contentResolver, jSONObject.optString("CKCurrentAreaCode" + i), i);
            }
            if (jSONObject.has("CKSupportLocalNumber" + i)) {
                MiuiSettings.Telephony.setAutoIpSupportLocalNumEnable(contentResolver, jSONObject.optBoolean("CKSupportLocalNumber" + i), i);
            }
            if (jSONObject.has("CKEnableAddZeroPrefix" + i)) {
                MiuiSettings.Telephony.setAutoAddZeroPrefixEnable(contentResolver, jSONObject.optBoolean("CKEnableAddZeroPrefix" + i), i);
            }
        }
        restoreAutoIpExceptionNumbers(context, dataPackage);
    }

    private static void restoreAutoIpExceptionNumbers(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        JSONObject jSONObjectOptJSONObject;
        if (dataPackage.get("AutoIpExceptionNumbers") == null || (jSONObject = (JSONObject) dataPackage.get("AutoIpExceptionNumbers").getValue()) == null || (jSONObjectOptJSONObject = jSONObject.optJSONObject("autoIpNumbers")) == null) {
            return;
        }
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (jSONObjectOptJSONObject.has("autoIpSlot" + i)) {
                SharedPreferences.Editor editorEdit = getSharedPreferences(context, i).edit();
                editorEdit.clear();
                JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("autoIpSlot" + i);
                for (int i2 = 0; i2 < jSONArrayOptJSONArray.length(); i2++) {
                    JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(i2);
                    editorEdit.putString(jSONObjectOptJSONObject2.optString("number"), jSONObjectOptJSONObject2.optString("name"));
                }
                editorEdit.apply();
            }
        }
    }

    private static SharedPreferences getSharedPreferences(Context context, int i) {
        String str = "_preference_autoip_exceptions";
        if (i >= 0) {
            str = "_preference_autoip_exceptions" + i;
        }
        return context.getSharedPreferences(context.getPackageName() + str, 0);
    }
}

package com.android.phone.settings.cloudbackup;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MiuiSettings;
import android.util.Log;
import com.android.phone.R;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class RespondViaSmsBackupHelper {
    private static final String TAG = "RespondViaSmsBackupHelper";
    private static final String[] SP_KEYS = {"canned_response_pref_1", "canned_response_pref_2", "canned_response_pref_3", "canned_response_pref_4"};
    private static final String[] SETTINGS_KEYS = {"reject_via_sms_response_1", "reject_via_sms_response_2", "reject_via_sms_response_3", "reject_via_sms_response_4"};
    private static final int[] DEFAULT_VALUES_ID = {R.string.respond_via_sms_canned_response_1, R.string.respond_via_sms_canned_response_2, R.string.respond_via_sms_canned_response_3, R.string.respond_via_sms_canned_response_4};

    public static void backupRespondViaSms(Context context, DataPackage dataPackage) {
        getSharedPreferences(context);
        try {
            JSONArray jSONArray = new JSONArray();
            int i = 0;
            while (true) {
                String[] strArr = SP_KEYS;
                if (i < strArr.length) {
                    int i2 = DEFAULT_VALUES_ID[i];
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("index", strArr[i]);
                    jSONObject.put("content", MiuiSettings.Telephony.getRejectViaSmsResponse(context.getContentResolver(), SETTINGS_KEYS[i], context.getString(i2)));
                    jSONArray.put(jSONObject);
                    i++;
                } else {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("RespondViaSmsEnable", MiuiSettings.Telephony.isRejectViaSmsEnable(context.getContentResolver()));
                    jSONObject2.put("array", jSONArray);
                    dataPackage.addKeyJson("RespondViaSms", jSONObject2);
                    return;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Build JSON failed", e);
        }
    }

    public static void restoreRespondViaSms(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("RespondViaSms") == null || (jSONObject = (JSONObject) dataPackage.get("RespondViaSms").getValue()) == null) {
            return;
        }
        if (jSONObject.has("RespondViaSmsEnable")) {
            MiuiSettings.Telephony.setRejectViaSmsEnable(context.getContentResolver(), jSONObject.optBoolean("RespondViaSmsEnable"));
        }
        if (jSONObject.has("array")) {
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("array");
            List listAsList = Arrays.asList(SP_KEYS);
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                int iIndexOf = listAsList.indexOf(jSONObjectOptJSONObject.optString("index"));
                if (iIndexOf < 0) {
                    return;
                }
                String[] strArr = SETTINGS_KEYS;
                if (iIndexOf >= strArr.length) {
                    return;
                }
                MiuiSettings.Telephony.setRejectViaSmsRespone(context.getContentResolver(), strArr[iIndexOf], jSONObjectOptJSONObject.optString("content"));
            }
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("respond_via_sms_prefs", 0);
    }
}

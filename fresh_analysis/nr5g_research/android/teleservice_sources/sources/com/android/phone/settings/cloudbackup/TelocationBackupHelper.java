package com.android.phone.settings.cloudbackup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.MiuiSettings;
import android.util.Log;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import miui.provider.Telocation;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelocationBackupHelper {
    private static final String TAG = "TelocationBackupHelper";

    interface LocationQuery {
        public static final String[] COLUMNS = {"_id", "number", "location", "type"};
    }

    public static void backupTelocation(Context context, DataPackage dataPackage) {
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
            ContentResolver contentResolver = context.getContentResolver();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("CKEnableShowLocation", MiuiSettings.Telephony.isTelocationEnable(contentResolver));
                jSONObject.put("CKEnableAutoCountryCode", MiuiSettings.Telephony.isAutoCountryCodeEnable(contentResolver));
                jSONObject.put("CKCountryCode", MiuiSettings.Telephony.getContactCountrycode(contentResolver));
            } catch (JSONException e) {
                Log.e(TAG, "Build JSON failed", e);
            }
            dataPackage.addKeyJson("Telocation", jSONObject);
            backupCustomTeloction(contentResolver, dataPackage);
        }
    }

    private static void backupCustomTeloction(ContentResolver contentResolver, DataPackage dataPackage) {
        Cursor cursorQuery = contentResolver.query(Telocation.CONTENT_CUSTOM_LOCATION_URI, LocationQuery.COLUMNS, null, null, null);
        if (cursorQuery == null) {
            return;
        }
        JSONArray jSONArray = new JSONArray();
        while (cursorQuery.moveToNext()) {
            try {
                try {
                    String string = cursorQuery.getString(2);
                    String string2 = cursorQuery.getString(1);
                    int i = cursorQuery.getInt(3);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("location", string);
                    jSONObject.put("number", string2);
                    jSONObject.put("type", i);
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    Log.e(TAG, "Build JSON failed", e);
                    cursorQuery.close();
                    return;
                }
            } catch (Throwable th) {
                cursorQuery.close();
                throw th;
            }
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("array", jSONArray);
        dataPackage.addKeyJson("CustomLocation", jSONObject2);
        cursorQuery.close();
    }

    public static void restoreTelocation(Context context, DataPackage dataPackage) {
        JSONObject jSONObject;
        if ((TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) || dataPackage.get("Telocation") == null || (jSONObject = (JSONObject) dataPackage.get("Telocation").getValue()) == null) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (jSONObject.has("CKEnableShowLocation")) {
            MiuiSettings.Telephony.setTelocationEnable(contentResolver, jSONObject.optBoolean("CKEnableShowLocation"));
        }
        if (jSONObject.has("CKEnableAutoCountryCode")) {
            MiuiSettings.Telephony.setAutoCountryCodeEnable(contentResolver, jSONObject.optBoolean("CKEnableAutoCountryCode"));
        }
        if (jSONObject.has("CKCountryCode")) {
            MiuiSettings.Telephony.setContactCountrycode(contentResolver, jSONObject.optString("CKCountryCode"));
        }
        restoreCustomTeloction(contentResolver, dataPackage);
    }

    private static void restoreCustomTeloction(ContentResolver contentResolver, DataPackage dataPackage) {
        JSONObject jSONObject;
        if (dataPackage.get("CustomLocation") == null || (jSONObject = (JSONObject) dataPackage.get("CustomLocation").getValue()) == null || !jSONObject.has("array")) {
            return;
        }
        try {
            contentResolver.delete(Telocation.CONTENT_CUSTOM_LOCATION_URI, null, null);
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("array");
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    String strOptString = jSONObjectOptJSONObject.optString("number");
                    String strOptString2 = jSONObjectOptJSONObject.optString("location");
                    int iOptInt = jSONObjectOptJSONObject.optInt("type");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("number", strOptString.trim());
                    contentValues.put("location", strOptString2.trim());
                    contentValues.put("type", Integer.valueOf(iOptInt));
                    try {
                        contentResolver.insert(Telocation.CONTENT_CUSTOM_LOCATION_URI, contentValues);
                    } catch (Exception e) {
                        Log.e(TAG, "restoreCustomTeloction insert error", e);
                    }
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "restoreCustomTeloction delete error", e2);
        }
    }
}

package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.HandlerThread;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudEccController {
    protected static String CATEGORY = "category";
    protected static String CONDITION = "condition";
    protected static String DEVICE = "device";
    protected static String ECC = "ecc";
    protected static String LOG_TAG = "CloudEccController";
    protected static String MIUI_VERSION = "miui_version";
    protected static String MODE = "mode";
    protected static String PLMN = "plmn";
    protected static String TYPE_FLAG = "type_flag";
    protected static volatile CloudEccController mInstance;
    protected final int EVENT_ECC_LIST_DEBUG = 1;
    protected final int EVENT_ECC_LIST_GET = 2;

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public void init(Context context, HandlerThread handlerThread) {
    }

    public static CloudEccController getInstance() {
        if (mInstance == null) {
            synchronized (CloudEccController.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new CloudEccController();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    protected static List getCloudEccListInfos() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "ecc_list_update");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                ArrayList arrayList = new ArrayList();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    if (cloudData != null) {
                        JSONArray jSONArray = cloudData.json().getJSONArray("ecc_list_update");
                        if (jSONArray != null) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                arrayList.add(jSONArray.getJSONObject(i));
                            }
                        } else {
                            arrayList.add(cloudData.json());
                        }
                    }
                }
                logd("getCloudEccListInfos: count=" + arrayList.size());
                return arrayList;
            }
            logd("getCloudEccListInfos: no cloud dataList");
            return null;
        } catch (Exception e) {
            loge("getCloudEccListInfos: error", e);
            return null;
        }
    }

    protected String getLastCloudEccListInfo() {
        return PhoneGlobals.getInstance().getSharedPreferences("preference_cloud_ecclist_info", 0).getString("cloud_ecclist_info", "");
    }

    protected String getLastCloudEccdataInfo() {
        return PhoneGlobals.getInstance().getSharedPreferences("preference_cloud_eccdata_info", 0).getString("cloud_eccdata_info", "");
    }

    protected void updateLastCloudEccListVersion(List list) {
        SharedPreferences.Editor editorEdit = PhoneGlobals.getInstance().getSharedPreferences("preference_cloud_ecclist_info", 0).edit();
        if (list == null) {
            logd("updateLastCloudEccListVersion: clearing SP");
            editorEdit.putString("cloud_ecclist_info", "");
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                sb.append(((JSONObject) it.next()).toString());
            }
            String string = sb.toString();
            logd("updateLastCloudEccListVersion=".concat(string));
            editorEdit.putString("cloud_ecclist_info", string);
        }
        editorEdit.commit();
    }

    protected void updateLatestCloudEccdata(List list) {
        if (isCloudEccdataChanged(list)) {
            SharedPreferences.Editor editorEdit = PhoneGlobals.getInstance().getSharedPreferences("preference_cloud_eccdata_info", 0).edit();
            editorEdit.putString("cloud_eccdata_info", getCloudEccData());
            editorEdit.commit();
            for (Phone phone : PhoneFactory.getPhones()) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (emergencyNumberTracker != null) {
                    emergencyNumberTracker.updateEmergencyNumberDatabaseCountryChange(emergencyNumberTracker.getLastKnownEmergencyCountryIso());
                }
            }
        }
    }

    public static String getCloudEccData() {
        List<JSONObject> cloudEccListInfos = getCloudEccListInfos();
        if (cloudEccListInfos == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (JSONObject jSONObject : cloudEccListInfos) {
            if (isValidCloudEccList(jSONObject)) {
                if (String.valueOf(4).equals(jSONObject.optString(MODE))) {
                    if (sb.length() > 0) {
                        sb.append(";");
                    }
                    sb.append(jSONObject);
                }
            }
        }
        return sb.toString();
    }

    protected boolean isCloudEccListChanged(List list) {
        String lastCloudEccListInfo = getLastCloudEccListInfo();
        logd("isCloudEccListChanged lastCloudEccListInfo =" + lastCloudEccListInfo);
        if (TextUtils.isEmpty(lastCloudEccListInfo)) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            sb.append(((JSONObject) it.next()).toString());
        }
        boolean z = !TextUtils.equals(lastCloudEccListInfo, sb.toString());
        logd("cloud ecc list changed =" + z);
        return z;
    }

    protected boolean isCloudEccdataChanged(List list) {
        String lastCloudEccdataInfo = getLastCloudEccdataInfo();
        logd("isCloudEccdataChanged lastCloudEccdataInfo =" + lastCloudEccdataInfo);
        if (TextUtils.isEmpty(lastCloudEccdataInfo)) {
            return true;
        }
        List listAsList = Arrays.asList(lastCloudEccdataInfo.split(";"));
        Iterator it = list.iterator();
        boolean z = false;
        int i = 0;
        while (it.hasNext()) {
            JSONObject jSONObject = (JSONObject) it.next();
            if (String.valueOf(4).equals(jSONObject.optString(MODE))) {
                if (!listAsList.contains(jSONObject.toString())) {
                    z = true;
                    break;
                }
                i++;
            }
        }
        if (i != listAsList.size()) {
            return true;
        }
        logd("cloud eccdata changed = " + z);
        return z;
    }

    protected static boolean isValidCloudEccList(JSONObject jSONObject) {
        logd("isValidCloudEccList cloudEccList = " + jSONObject);
        if (jSONObject == null) {
            return false;
        }
        String strOptString = jSONObject.optString(TYPE_FLAG);
        String strOptString2 = jSONObject.optString(ECC);
        String strOptString3 = jSONObject.optString(CATEGORY);
        String strOptString4 = jSONObject.optString(CONDITION);
        String strOptString5 = jSONObject.optString(PLMN);
        String strOptString6 = jSONObject.optString(MODE);
        if (TextUtils.isEmpty(strOptString) || TextUtils.isEmpty(strOptString2) || TextUtils.isEmpty(strOptString3) || TextUtils.isEmpty(strOptString4) || TextUtils.isEmpty(strOptString5) || TextUtils.isEmpty(strOptString6)) {
            logd("isValidCloudEccList config empty");
            return false;
        }
        if (!isNumeric(strOptString2) || !isNumeric(strOptString3) || !isNumeric(strOptString4) || !isNumeric(strOptString6)) {
            logd("isValidCloudEccList config invalid");
            return false;
        }
        String str = Build.DEVICE;
        String str2 = Build.VERSION.INCREMENTAL;
        String strOptString7 = jSONObject.optString(DEVICE);
        String strOptString8 = jSONObject.optString(MIUI_VERSION);
        logd("updateCloudEccList deviceModel = " + str + ", miuiVersion = " + str2 + ", cloudDeviceName" + strOptString7 + ", cloudMiuiVersion" + strOptString8);
        if (strOptString7 == null || TextUtils.isEmpty(strOptString7) || strOptString7.equals(str)) {
            return strOptString8 == null || TextUtils.isEmpty(strOptString8) || strOptString8.equals(str2);
        }
        return false;
    }

    protected boolean isRemoveEccDataSupported(Context context) {
        return context.getResources().getBoolean(285540503);
    }

    protected static void logd(String str) {
        Log.d(LOG_TAG, str);
    }

    protected static void loge(String str) {
        Log.e(LOG_TAG, str);
    }

    protected static void loge(String str, Exception exc) {
        Log.e(LOG_TAG, str, exc);
    }

    public void updateCloudEccList() {
        logd("updateCloudEccList not implemented");
    }

    protected static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }
}

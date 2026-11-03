package com.android.phone.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MiuiSettings;
import com.aios.apptoolsdk.AppResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallRecordSettingsTools {
    public static AppResult recordSettings(Context context, String str, String str2, String str3) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "get_record_list":
                return getRecordList(context);
            case "add_record_list":
                return addRecordList(context, str2, str3);
            case "set_scenario":
                return setScenario(context, str2);
            case "remove_record_list":
                return removeRecordList(context, str2);
            case "set_notification":
                return setNotification(context, str2);
            case "set_unknown":
                return setUnknown(context, str2);
            case "get":
                return getAll(context);
            case "set_auto_record":
                return setAutoRecord(context, str2);
            case "set_yellowpage":
                return setYellowpage(context, str2);
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get / set_notification / set_auto_record / set_scenario / set_unknown / set_yellowpage / get_record_list / add_record_list / remove_record_list");
        }
    }

    private static AppResult getAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        boolean zIsRecordNotificationEnabled = MiuiSettings.Telephony.isRecordNotificationEnabled(contentResolver);
        boolean zIsAutoRecordEnabled = MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver);
        int recordScenario = MiuiSettings.Telephony.getRecordScenario(contentResolver);
        boolean zIsUnknownNumberRecordEnabled = MiuiSettings.Telephony.isUnknownNumberRecordEnabled(contentResolver);
        boolean zIsYellowpageRecordEnabled = MiuiSettings.Telephony.isYellowpageRecordEnabled(contentResolver);
        return AppResult.success("{\"notification\":" + zIsRecordNotificationEnabled + ",\"auto_record\":" + zIsAutoRecordEnabled + ",\"scenario\":\"" + (recordScenario == 0 ? "all" : "custom") + "\",\"unknown\":" + zIsUnknownNumberRecordEnabled + ",\"yellowpage\":" + zIsYellowpageRecordEnabled + "}");
    }

    private static AppResult setNotification(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_notification", str);
        }
        MiuiSettings.Telephony.setRecordNotificationEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"notification\":" + bool + "}");
    }

    private static AppResult setAutoRecord(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_auto_record", str);
        }
        MiuiSettings.Telephony.setAutoRecordEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"auto_record\":" + bool + "}");
    }

    private static AppResult setScenario(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        if (!MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver)) {
            return AppResult.error("自动录音总开关未开启，请先开启自动录音后再设置录音场景");
        }
        if ("all".equals(str)) {
            MiuiSettings.Telephony.setRecordScenario(contentResolver, 0);
            return AppResult.success("{\"scenario\":\"all\"}");
        }
        if ("custom".equals(str)) {
            MiuiSettings.Telephony.setRecordScenario(contentResolver, 1);
            return AppResult.success("{\"scenario\":\"custom\"}");
        }
        return AppResult.error("set_scenario 的 value 无效：" + str + "，支持：all / custom");
    }

    private static AppResult setUnknown(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_unknown", str);
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver)) {
            return AppResult.error("自动录音总开关未开启，请先开启自动录音后再设置陌生号码录音");
        }
        if (MiuiSettings.Telephony.getRecordScenario(contentResolver) != 1) {
            return AppResult.error("录音场景为「全部」，无法单独设置陌生号码录音；请先将录音对象切换为「自定义」");
        }
        MiuiSettings.Telephony.setUnknownNumberRecordEnabled(contentResolver, bool.booleanValue());
        return AppResult.success("{\"unknown\":" + bool + "}");
    }

    private static AppResult setYellowpage(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_yellowpage", str);
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver)) {
            return AppResult.error("自动录音总开关未开启，请先开启自动录音后再设置黄页录音");
        }
        if (MiuiSettings.Telephony.getRecordScenario(contentResolver) != 1) {
            return AppResult.error("录音场景为「全部」，无法单独设置黄页录音；请先将录音对象切换为「自定义」");
        }
        MiuiSettings.Telephony.setYellowpageRecordEnabled(contentResolver, bool.booleanValue());
        return AppResult.success("{\"yellowpage\":" + bool + "}");
    }

    private static AppResult getRecordList(Context context) {
        String recordWhiteList = MiuiSettings.Telephony.getRecordWhiteList(context.getContentResolver());
        if (recordWhiteList == null || recordWhiteList.isEmpty()) {
            return AppResult.success("{\"numbers\":[]}");
        }
        return AppResult.success("{\"numbers\":" + recordWhiteList + "}");
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0045 A[Catch: JSONException -> 0x00ca, TryCatch #0 {JSONException -> 0x00ca, blocks: (B:16:0x0039, B:18:0x003f, B:21:0x004b, B:24:0x0054, B:26:0x0062, B:28:0x0079, B:29:0x007c, B:31:0x0089, B:33:0x0090, B:19:0x0045), top: B:40:0x0039 }] */
    private static AppResult addRecordList(Context context, String str, String str2) {
        JSONArray jSONArray;
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("add_record_list 需要 value 传入电话号码");
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver)) {
            return AppResult.error("自动录音总开关未开启，请先开启自动录音后再添加录音名单");
        }
        if (MiuiSettings.Telephony.getRecordScenario(contentResolver) != 1) {
            return AppResult.error("录音场景为「全部」，无法修改指定号码录音名单；请先将录音对象切换为「自定义」");
        }
        String strTrim = str.trim();
        String recordWhiteList = MiuiSettings.Telephony.getRecordWhiteList(contentResolver);
        if (recordWhiteList != null) {
            try {
                if (recordWhiteList.isEmpty()) {
                    jSONArray = new JSONArray();
                } else {
                    jSONArray = new JSONArray(recordWhiteList);
                }
            } catch (JSONException e) {
                return AppResult.error("录音名单数据解析失败：" + e.getMessage());
            }
        } else {
            jSONArray = new JSONArray();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            if (strTrim.equals(jSONArray.getJSONObject(i).optString("num"))) {
                return AppResult.error("该号码已在录音名单中：" + strTrim);
            }
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("num", strTrim);
        jSONObject.put("name", str2 != null ? str2.trim() : "");
        jSONArray.put(jSONObject);
        MiuiSettings.Telephony.setRecordWhiteList(contentResolver, jSONArray.toString());
        return AppResult.success("{\"added\":\"" + ToolUtils.escapeJson(strTrim) + "\",\"total\":" + jSONArray.length() + "}");
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0045 A[Catch: JSONException -> 0x00bd, TryCatch #0 {JSONException -> 0x00bd, blocks: (B:16:0x0039, B:18:0x003f, B:20:0x004a, B:21:0x0051, B:23:0x0057, B:27:0x006d, B:26:0x006a, B:29:0x0072, B:31:0x0089, B:19:0x0045), top: B:38:0x0039 }] */
    private static AppResult removeRecordList(Context context, String str) {
        JSONArray jSONArray;
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("remove_record_list 需要 value 传入电话号码");
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (!MiuiSettings.Telephony.isAutoRecordEnabled(contentResolver)) {
            return AppResult.error("自动录音总开关未开启，请先开启自动录音后再修改录音名单");
        }
        if (MiuiSettings.Telephony.getRecordScenario(contentResolver) != 1) {
            return AppResult.error("录音场景为「全部」，无法修改指定号码录音名单；请先将录音对象切换为「自定义」");
        }
        String strTrim = str.trim();
        String recordWhiteList = MiuiSettings.Telephony.getRecordWhiteList(contentResolver);
        if (recordWhiteList != null) {
            try {
                if (recordWhiteList.isEmpty()) {
                    jSONArray = new JSONArray();
                } else {
                    jSONArray = new JSONArray(recordWhiteList);
                }
            } catch (JSONException e) {
                return AppResult.error("录音名单数据解析失败：" + e.getMessage());
            }
        } else {
            jSONArray = new JSONArray();
        }
        JSONArray jSONArray2 = new JSONArray();
        boolean z = false;
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            if (strTrim.equals(jSONObject.optString("num"))) {
                z = true;
            } else {
                jSONArray2.put(jSONObject);
            }
        }
        if (!z) {
            return AppResult.error("录音名单中未找到该号码：" + strTrim);
        }
        MiuiSettings.Telephony.setRecordWhiteList(contentResolver, jSONArray2.toString());
        return AppResult.success("{\"removed\":\"" + ToolUtils.escapeJson(strTrim) + "\",\"total\":" + jSONArray2.length() + "}");
    }
}

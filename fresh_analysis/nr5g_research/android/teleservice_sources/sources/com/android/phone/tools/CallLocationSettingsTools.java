package com.android.phone.tools;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import com.aios.apptoolsdk.AppResult;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallLocationSettingsTools {
    public static AppResult locationSettings(Context context, String str, String str2) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "get_auto_country":
                return getAutoCountry(context);
            case "set_telocation":
                return setTelocation(context, str2);
            case "set_country_code":
                return setCountryCode(context, str2);
            case "get_telocation":
                return getTelocation(context);
            case "set_auto_country":
                return setAutoCountry(context, str2);
            case "get_country_code":
                return getCountryCode(context);
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get_telocation / set_telocation / get_auto_country / set_auto_country / get_country_code / set_country_code");
        }
    }

    private static AppResult getTelocation(Context context) {
        try {
            boolean z = true;
            if (Settings.System.getInt(context.getContentResolver(), "enable_telocation", 1) == 0) {
                z = false;
            }
            return AppResult.success("{\"telocation_enabled\":" + z + "}");
        } catch (Exception e) {
            return AppResult.error("查询归属地显示开关失败：" + e.getMessage());
        }
    }

    private static AppResult setTelocation(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("value 无效，请传 true 或 false");
        }
        try {
            Settings.System.putInt(context.getContentResolver(), "enable_telocation", bool.booleanValue() ? 1 : 0);
            return AppResult.success("归属地显示已".concat(bool.booleanValue() ? "开启" : "关闭"));
        } catch (Exception e) {
            return AppResult.error("设置归属地显示开关失败：" + e.getMessage());
        }
    }

    private static AppResult getAutoCountry(Context context) {
        try {
            return AppResult.success("{\"auto_country_enabled\":" + MiuiSettings.Telephony.isAutoCountryCodeEnable(context.getContentResolver()) + "}");
        } catch (Exception e) {
            return AppResult.error("查询自动添加国家码开关失败：" + e.getMessage());
        }
    }

    private static AppResult setAutoCountry(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("value 无效，请传 true 或 false");
        }
        try {
            MiuiSettings.Telephony.setAutoCountryCodeEnable(context.getContentResolver(), bool.booleanValue());
            return AppResult.success("漫游自动添加国家码已".concat(bool.booleanValue() ? "开启" : "关闭"));
        } catch (Exception e) {
            return AppResult.error("设置自动添加国家码开关失败：" + e.getMessage());
        }
    }

    private static AppResult getCountryCode(Context context) {
        try {
            String contactCountrycode = MiuiSettings.Telephony.getContactCountrycode(context.getContentResolver());
            if (contactCountrycode == null) {
                contactCountrycode = "";
            }
            return AppResult.success("{\"country_code\":\"" + ToolUtils.escapeJson(contactCountrycode) + "\"}");
        } catch (Exception e) {
            return AppResult.error("查询联系人默认国家码失败：" + e.getMessage());
        }
    }

    private static AppResult setCountryCode(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            try {
                MiuiSettings.Telephony.setContactCountrycode(context.getContentResolver(), "");
                SystemProperties.set("persist.radio.countrycode", "");
                return AppResult.success("联系人默认国家码已清空");
            } catch (Exception e) {
                return AppResult.error("清空联系人默认国家码失败：" + e.getMessage());
            }
        }
        String strTrim = str.trim();
        if (strTrim.startsWith("+")) {
            strTrim = strTrim.substring(1);
        } else if (strTrim.startsWith("00")) {
            strTrim = strTrim.substring(2);
        }
        if (!strTrim.matches("\\d+")) {
            return AppResult.error("国家码格式错误，请传入纯数字（如 86 或 +86），或传空字符串清空");
        }
        try {
            MiuiSettings.Telephony.setContactCountrycode(context.getContentResolver(), strTrim);
            SystemProperties.set("persist.radio.countrycode", strTrim);
            return AppResult.success("联系人默认国家码已设置为 +".concat(strTrim));
        } catch (Exception e2) {
            return AppResult.error("设置联系人默认国家码失败：" + e2.getMessage());
        }
    }
}

package com.android.phone.tools;

import android.content.Context;
import android.provider.MiuiSettings;
import com.aios.apptoolsdk.AppResult;
import com.android.phone.settings.TelephonySettings;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallAnswerSettingsTools {
    public static AppResult answerSettings(Context context, String str, String str2) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "set_sensor_answer":
                return setSensorAnswer(context, str2);
            case "get_sensor_answer":
                return getSensorAnswer(context);
            case "set_auto_answer_delay":
                return setAutoAnswerDelay(context, str2);
            case "get_auto_answer":
                return getAutoAnswer(context);
            case "set_auto_answer_scenario":
                return setAutoAnswerScenario(context, str2);
            case "set_auto_answer":
                return setAutoAnswer(context, str2);
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get_auto_answer / set_auto_answer / set_auto_answer_delay / set_auto_answer_scenario / get_sensor_answer / set_sensor_answer");
        }
    }

    private static AppResult getAutoAnswer(Context context) {
        return AppResult.success("{\"auto_answer\":" + MiuiSettings.Telephony.isAutoAnswerCall(context.getContentResolver()) + ",\"delay\":" + MiuiSettings.Telephony.getAutoAnswerCallDelayTime(context.getContentResolver()) + ",\"scenario\":\"" + scenarioToString(MiuiSettings.Telephony.getAutoAnswerCallScenario(context.getContentResolver())) + "\"}");
    }

    private static AppResult setAutoAnswer(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_auto_answer 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setAutoAnswerCall(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"auto_answer\":" + bool + "}");
    }

    private static AppResult setAutoAnswerDelay(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_auto_answer_delay 的 value 不能为空，支持：0 / 3 / 5 / 10");
        }
        try {
            int i = Integer.parseInt(str.trim());
            if (i != 0 && i != 3 && i != 5 && i != 10) {
                return AppResult.error("set_auto_answer_delay 的 value 无效：" + i + "，支持：0 / 3 / 5 / 10");
            }
            MiuiSettings.Telephony.setAutoAnswerCallDelayTime(context.getContentResolver(), i);
            return AppResult.success("{\"delay\":" + i + "}");
        } catch (NumberFormatException unused) {
            return AppResult.error("set_auto_answer_delay 的 value 无效：" + str + "，支持：0 / 3 / 5 / 10");
        }
    }

    private static AppResult setAutoAnswerScenario(Context context, String str) {
        String strTrim = str != null ? str.trim() : "";
        strTrim.hashCode();
        int i = 3;
        switch (strTrim) {
            case "always":
                i = 0;
                break;
            case "headset_only":
                i = 1;
                break;
            case "headset_bluetooth":
                break;
            case "bluetooth_only":
                i = 2;
                break;
            default:
                return AppResult.error("set_auto_answer_scenario 的 value 无效：" + str + "，支持：always（始终）/ headset_only（仅有线耳机时）/ bluetooth_only（仅蓝牙耳机时）/ headset_bluetooth（有线或蓝牙耳机时）");
        }
        MiuiSettings.Telephony.setAutoAnswerCallScenario(context.getContentResolver(), i);
        return AppResult.success("{\"scenario\":\"" + str.trim() + "\"}");
    }

    private static AppResult getSensorAnswer(Context context) {
        return AppResult.success("{\"sensor_answer\":" + TelephonySettings.isSensorAnswerEnabled(context.getContentResolver()) + "}");
    }

    private static AppResult setSensorAnswer(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_sensor_answer 的 value 无效：" + str + "，支持：true / false");
        }
        TelephonySettings.setSensorAnswerEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"sensor_answer\":" + bool + "}");
    }

    private static String scenarioToString(int i) {
        if (i == 1) {
            return "headset_only";
        }
        if (i == 2) {
            return "bluetooth_only";
        }
        if (i == 3) {
            return "headset_bluetooth";
        }
        return "always";
    }
}

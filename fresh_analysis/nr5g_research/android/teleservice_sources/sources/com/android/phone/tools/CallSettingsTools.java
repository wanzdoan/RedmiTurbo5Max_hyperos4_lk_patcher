package com.android.phone.tools;

import android.content.Context;
import com.aios.apptoolsdk.AppResult;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallSettingsTools {
    public static AppResult phoneSettings(Context context, String str, String str2, String str3, String str4, String str5, String str6, Integer num) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("category 不能为空，支持：call / record / advanced / location / voicemail / answer");
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "answer":
                return CallAnswerSettingsTools.answerSettings(context, str2, str3);
            case "voicemail":
                return CallVoicemailTools.voicemailSettings(context, str2, str4, str6, num);
            case "record":
                return CallRecordSettingsTools.recordSettings(context, str2, str3, str5);
            case "advanced":
                return CallAdvancedSettingsTools.advancedSettings(context, str2, str3, num);
            case "call":
                return CallIncomingSettingsTools.callSettings(context, str2, str3, str4, num);
            case "location":
                return CallLocationSettingsTools.locationSettings(context, str2, str3);
            default:
                return AppResult.error("不支持的 category：" + str + "，支持：call / record / advanced / location / voicemail / answer");
        }
    }
}

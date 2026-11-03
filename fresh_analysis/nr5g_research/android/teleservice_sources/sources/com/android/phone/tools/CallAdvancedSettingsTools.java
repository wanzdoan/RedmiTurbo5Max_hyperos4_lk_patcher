package com.android.phone.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import com.aios.apptoolsdk.AppResult;
import com.android.phone.R;
import com.android.phone.settings.AutoIpExceptionSetting;
import com.android.phone.settings.RecyclerActivity;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.HashMap;
import miui.telephony.PhoneNumberUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallAdvancedSettingsTools {
    public static AppResult advancedSettings(Context context, String str, String str2, Integer num) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        Integer numResolveSlotId = ToolUtils.resolveSlotId(num);
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "set_dtmf_type":
                return setDtmfType(context, str2);
            case "set_auto_ip_enable":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setAutoIpEnable(context, str2, numResolveSlotId.intValue());
            case "get_local_numbers_ip":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getLocalNumbersIp(context, numResolveSlotId.intValue());
            case "set_reject_via_sms":
                return setRejectViaSms(context, str2);
            case "add_ip_exception":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return addIpException(context, str2, numResolveSlotId.intValue());
            case "remove_ip_exception":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return removeIpException(context, str2, numResolveSlotId.intValue());
            case "set_auto_ip_prefix":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setAutoIpPrefix(context, str2, numResolveSlotId.intValue());
            case "get_add_zero_prefix":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getAddZeroPrefix(context, numResolveSlotId.intValue());
            case "set_connect_vibrate":
                return setConnectVibrate(context, str2);
            case "set_reject_sms_response":
                return setRejectSmsResponse(context, str2);
            case "set_dial_pad_tone":
                return setDialPadTone(context, str2);
            case "get_auto_ip":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getAutoIp(context, numResolveSlotId.intValue());
            case "get_missed_call_notify":
                return getMissedCallNotify(context);
            case "get_auto_redial":
                return getAutoRedial(context);
            case "set_auto_ip_areacode":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setAutoIpAreacode(context, str2, numResolveSlotId.intValue());
            case "set_call_waiting_tone":
                return setCallWaitingTone(context, str2);
            case "get_dial_pad_tone":
                return getDialPadTone(context);
            case "get":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getAll(context, numResolveSlotId.intValue());
            case "get_t9_index":
                return getT9Index(context);
            case "get_reject_sms_responses":
                return getRejectSmsResponses(context);
            case "get_connect_vibrate":
                return getConnectVibrate(context);
            case "set_auto_redial":
                return setAutoRedial(context, str2);
            case "get_ip_exceptions":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getIpExceptions(context, numResolveSlotId.intValue());
            case "set_missed_call_notify":
                return setMissedCallNotify(context, str2);
            case "set_add_zero_prefix":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setAddZeroPrefix(context, str2, numResolveSlotId.intValue());
            case "get_dtmf_type":
                return getDtmfType(context);
            case "get_call_waiting_tone":
                return getCallWaitingTone(context);
            case "get_reject_via_sms":
                return getRejectViaSms(context);
            case "set_t9_index":
                return setT9Index(context, str2);
            case "set_local_numbers_ip":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setLocalNumbersIp(context, str2, numResolveSlotId.intValue());
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get / get_dial_pad_tone / set_dial_pad_tone / get_t9_index / set_t9_index / get_auto_redial / set_auto_redial / get_auto_ip / set_auto_ip_enable / set_auto_ip_prefix / set_auto_ip_areacode / get_local_numbers_ip / set_local_numbers_ip / get_add_zero_prefix / set_add_zero_prefix / get_ip_exceptions / add_ip_exception / remove_ip_exception / get_missed_call_notify / set_missed_call_notify / get_connect_vibrate / set_connect_vibrate / get_call_waiting_tone / set_call_waiting_tone / get_reject_via_sms / set_reject_via_sms / get_reject_sms_responses / set_reject_sms_response / get_dtmf_type / set_dtmf_type");
        }
    }

    private static AppResult getAll(Context context, int i) {
        ContentResolver contentResolver = context.getContentResolver();
        String dialPadToneValue = Settings.System.getInt(contentResolver, "dtmf_tone", 1) == 0 ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF : getDialPadToneValue(contentResolver);
        String str = Settings.System.getInt(contentResolver, "t9_indexing_key", MiuiSettings.System.getT9IndexingKeyDefault()) == 0 ? "pinyin" : "zhuyin";
        boolean enabledAutoRedial = MiuiSettings.Telephony.getEnabledAutoRedial(contentResolver);
        boolean zIsAutoIpEnable = MiuiSettings.Telephony.isAutoIpEnable(contentResolver, i);
        String autoIpPrefix = MiuiSettings.Telephony.getAutoIpPrefix(contentResolver, PhoneNumberUtils.getDefaultIpBySim(context, i), i);
        String currentAeraCode = MiuiSettings.Telephony.getCurrentAeraCode(contentResolver, i);
        boolean zIsAutoIpSupportLocalNum = MiuiSettings.Telephony.isAutoIpSupportLocalNum(contentResolver, i);
        boolean zIsAutoAddZeroPrefix = MiuiSettings.Telephony.isAutoAddZeroPrefix(contentResolver, i);
        int missedCallNotifyTimes = MiuiSettings.Telephony.getMissedCallNotifyTimes(contentResolver);
        String strVibrateKeyToLabel = vibrateKeyToLabel(MiuiSettings.Telephony.getVibrateKey(contentResolver));
        String strCallWaitingToneToString = callWaitingToneToString(MiuiSettings.Telephony.getCallWaitingTone(context));
        boolean zIsRejectViaSmsEnable = MiuiSettings.Telephony.isRejectViaSmsEnable(contentResolver);
        int i2 = Settings.System.getInt(contentResolver, "dtmf_tone_type", 0);
        StringBuilder sb = new StringBuilder("{\"dial_pad_tone\":\"");
        sb.append(dialPadToneValue);
        sb.append("\",\"t9_index\":\"");
        sb.append(str);
        sb.append("\",\"auto_redial\":");
        sb.append(enabledAutoRedial);
        sb.append(",\"auto_ip_enabled\":");
        sb.append(zIsAutoIpEnable);
        sb.append(",\"ip_prefix\":\"");
        if (autoIpPrefix == null) {
            autoIpPrefix = "";
        }
        sb.append(ToolUtils.escapeJson(autoIpPrefix));
        sb.append("\",\"area_code\":\"");
        if (currentAeraCode == null) {
            currentAeraCode = "";
        }
        sb.append(ToolUtils.escapeJson(currentAeraCode));
        sb.append("\",\"local_numbers_ip\":");
        sb.append(zIsAutoIpSupportLocalNum);
        sb.append(",\"add_zero_prefix\":");
        sb.append(zIsAutoAddZeroPrefix);
        sb.append(",\"missed_call_notify\":");
        sb.append(missedCallNotifyTimes);
        sb.append(",\"connect_vibrate\":\"");
        sb.append(strVibrateKeyToLabel);
        sb.append("\",\"call_waiting_tone\":\"");
        sb.append(strCallWaitingToneToString);
        sb.append("\",\"reject_via_sms\":");
        sb.append(zIsRejectViaSmsEnable);
        sb.append(",\"dtmf_type\":\"");
        sb.append(i2 == 0 ? "normal" : "long");
        sb.append("\"}");
        return AppResult.success(sb.toString());
    }

    private static AppResult getDialPadTone(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        if (Settings.System.getInt(contentResolver, "dtmf_tone", 1) == 0) {
            return AppResult.success("{\"dial_pad_tone\":\"off\"}");
        }
        return AppResult.success("{\"dial_pad_tone\":\"" + getDialPadToneValue(contentResolver) + "\"}");
    }

    private static AppResult setDialPadTone(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        if (SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON.equals(str)) {
            Settings.System.putInt(contentResolver, "dtmf_tone", 1);
        } else {
            if (SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF.equals(str)) {
                Settings.System.putInt(contentResolver, "dtmf_tone", 0);
            } else if ("default".equals(str) || "piano".equals(str)) {
                if (Settings.System.getInt(contentResolver, "dtmf_tone", 1) == 0) {
                    return AppResult.error("拨号键盘按键音当前已关闭，无法修改音效。请先将 dial_pad_tone 设置为 on 开启拨号音后再修改音效");
                }
                Settings.System.putInt(contentResolver, "dial_pad_touch_tone", "piano".equals(str) ? 0 : -1);
            } else {
                return AppResult.error("set_dial_pad_tone 的 value 无效：" + str + "，支持：on / off / default / piano");
            }
        }
        return AppResult.success("拨号键盘按键音已设置为 " + str);
    }

    private static AppResult getT9Index(Context context) {
        int i = Settings.System.getInt(context.getContentResolver(), "t9_indexing_key", MiuiSettings.System.getT9IndexingKeyDefault());
        StringBuilder sb = new StringBuilder("{\"t9_index\":\"");
        sb.append(i == 0 ? "pinyin" : "zhuyin");
        sb.append("\"}");
        return AppResult.success(sb.toString());
    }

    private static AppResult setT9Index(Context context, String str) {
        int i;
        if ("pinyin".equalsIgnoreCase(str)) {
            i = 0;
        } else {
            if (!"zhuyin".equalsIgnoreCase(str)) {
                return AppResult.error("set_t9_index 的 value 无效：" + str + "，支持：pinyin（拼音）/ zhuyin（注音）");
            }
            i = 1;
        }
        Settings.System.putInt(context.getContentResolver(), "t9_indexing_key", i);
        return AppResult.success("T9 索引方式已设置为 " + str);
    }

    private static AppResult getAutoRedial(Context context) {
        return AppResult.success("{\"auto_redial\":" + MiuiSettings.Telephony.getEnabledAutoRedial(context.getContentResolver()) + "}");
    }

    private static AppResult setAutoRedial(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_auto_redial", str);
        }
        MiuiSettings.Telephony.setAutoRedialEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("自动重拨已".concat(bool.booleanValue() ? "开启" : "关闭"));
    }

    private static AppResult getAutoIp(Context context, int i) {
        ContentResolver contentResolver = context.getContentResolver();
        boolean zIsAutoIpEnable = MiuiSettings.Telephony.isAutoIpEnable(contentResolver, i);
        String autoIpPrefix = MiuiSettings.Telephony.getAutoIpPrefix(contentResolver, PhoneNumberUtils.getDefaultIpBySim(context, i), i);
        String currentAeraCode = MiuiSettings.Telephony.getCurrentAeraCode(contentResolver, i);
        boolean zIsAutoIpSupportLocalNum = MiuiSettings.Telephony.isAutoIpSupportLocalNum(contentResolver, i);
        boolean zIsAutoAddZeroPrefix = MiuiSettings.Telephony.isAutoAddZeroPrefix(contentResolver, i);
        StringBuilder sb = new StringBuilder("{\"auto_ip_enabled\":");
        sb.append(zIsAutoIpEnable);
        sb.append(",\"ip_prefix\":\"");
        if (autoIpPrefix == null) {
            autoIpPrefix = "";
        }
        sb.append(ToolUtils.escapeJson(autoIpPrefix));
        sb.append("\",\"area_code\":\"");
        if (currentAeraCode == null) {
            currentAeraCode = "";
        }
        sb.append(ToolUtils.escapeJson(currentAeraCode));
        sb.append("\",\"local_numbers_ip\":");
        sb.append(zIsAutoIpSupportLocalNum);
        sb.append(",\"add_zero_prefix\":");
        sb.append(zIsAutoAddZeroPrefix);
        sb.append("}");
        return AppResult.success(sb.toString());
    }

    private static AppResult setAutoIpEnable(Context context, String str, int i) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_auto_ip_enable", str);
        }
        if (bool.booleanValue() && TextUtils.isEmpty(MiuiSettings.Telephony.getCurrentAeraCode(context.getContentResolver(), i))) {
            return AppResult.error("开启IP拨号需要先设置当前所在地区号，请先用 set_auto_ip_areacode 设置区号后再开启");
        }
        MiuiSettings.Telephony.setAutoIpEnable(context.getContentResolver(), bool.booleanValue(), i);
        return AppResult.success("{\"auto_ip_enabled\":" + bool + "}");
    }

    private static AppResult setAutoIpPrefix(Context context, String str, int i) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_auto_ip_prefix 需要 value 传入IP前缀号码（如 17951）");
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改IP前缀");
        }
        MiuiSettings.Telephony.setAutoIpPrefix(context.getContentResolver(), str.trim(), i);
        return AppResult.success("{\"ip_prefix\":\"" + ToolUtils.escapeJson(str.trim()) + "\"}");
    }

    private static AppResult setAutoIpAreacode(Context context, String str, int i) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_auto_ip_areacode 需要 value 传入区号（如 010 或 10）");
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改所在地区号");
        }
        MiuiSettings.Telephony.setCurrentAeraCode(context.getContentResolver(), str.trim(), i);
        return AppResult.success("{\"area_code\":\"" + ToolUtils.escapeJson(str.trim()) + "\"}");
    }

    private static AppResult getLocalNumbersIp(Context context, int i) {
        return AppResult.success("{\"local_numbers_ip\":" + MiuiSettings.Telephony.isAutoIpSupportLocalNum(context.getContentResolver(), i) + "}");
    }

    private static AppResult setLocalNumbersIp(Context context, String str, int i) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_local_numbers_ip", str);
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改所在地号码使用IP拨号设置");
        }
        MiuiSettings.Telephony.setAutoIpSupportLocalNumEnable(context.getContentResolver(), bool.booleanValue(), i);
        return AppResult.success("{\"local_numbers_ip\":" + bool + "}");
    }

    private static AppResult getAddZeroPrefix(Context context, int i) {
        return AppResult.success("{\"add_zero_prefix\":" + MiuiSettings.Telephony.isAutoAddZeroPrefix(context.getContentResolver(), i) + "}");
    }

    private static AppResult setAddZeroPrefix(Context context, String str, int i) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_add_zero_prefix", str);
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改手机号码前加拨0设置");
        }
        MiuiSettings.Telephony.setAutoAddZeroPrefixEnable(context.getContentResolver(), bool.booleanValue(), i);
        return AppResult.success("{\"add_zero_prefix\":" + bool + "}");
    }

    private static AppResult getIpExceptions(Context context, int i) {
        HashMap mapLoadNumbers = AutoIpExceptionSetting.loadNumbers(context, i);
        StringBuilder sb = new StringBuilder("{\"exceptions\":[");
        boolean z = true;
        for (RecyclerActivity.Pair pair : mapLoadNumbers.values()) {
            if (!z) {
                sb.append(",");
            }
            sb.append("{\"number\":\"");
            sb.append(ToolUtils.escapeJson((String) pair.first));
            sb.append("\",\"name\":\"");
            sb.append(ToolUtils.escapeJson((String) pair.second));
            sb.append("\"}");
            z = false;
        }
        sb.append("]}");
        return AppResult.success(sb.toString());
    }

    private static AppResult addIpException(Context context, String str, int i) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("add_ip_exception 需要 value 传入电话号码");
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改排除号码列表");
        }
        String strTrim = str.trim();
        HashMap mapLoadNumbers = AutoIpExceptionSetting.loadNumbers(context, i);
        PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse(strTrim);
        String normalizedNumber = phoneNumber.getNormalizedNumber(false, true);
        phoneNumber.recycle();
        if (mapLoadNumbers.containsKey(normalizedNumber)) {
            return AppResult.error("该号码已在排除列表中：" + strTrim);
        }
        mapLoadNumbers.put(normalizedNumber, new RecyclerActivity.Pair(strTrim, strTrim));
        AutoIpExceptionSetting.saveNumbers(context, i, mapLoadNumbers);
        return AppResult.success("{\"added\":\"" + ToolUtils.escapeJson(strTrim) + "\",\"total\":" + mapLoadNumbers.size() + "}");
    }

    private static AppResult removeIpException(Context context, String str, int i) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("remove_ip_exception 需要 value 传入电话号码");
        }
        if (!MiuiSettings.Telephony.isAutoIpEnable(context.getContentResolver(), i)) {
            return AppResult.error("智能IP拨号当前已关闭，请先开启后再修改排除号码列表");
        }
        String strTrim = str.trim();
        HashMap mapLoadNumbers = AutoIpExceptionSetting.loadNumbers(context, i);
        PhoneNumberUtils.PhoneNumber phoneNumber = PhoneNumberUtils.PhoneNumber.parse(strTrim);
        String normalizedNumber = phoneNumber.getNormalizedNumber(false, true);
        phoneNumber.recycle();
        if (!mapLoadNumbers.containsKey(normalizedNumber)) {
            return AppResult.error("排除列表中未找到该号码：" + strTrim);
        }
        mapLoadNumbers.remove(normalizedNumber);
        AutoIpExceptionSetting.saveNumbers(context, i, mapLoadNumbers);
        return AppResult.success("{\"removed\":\"" + ToolUtils.escapeJson(strTrim) + "\",\"total\":" + mapLoadNumbers.size() + "}");
    }

    private static AppResult getMissedCallNotify(Context context) {
        return AppResult.success("{\"missed_call_notify\":" + MiuiSettings.Telephony.getMissedCallNotifyTimes(context.getContentResolver()) + "}");
    }

    private static AppResult setMissedCallNotify(Context context, String str) {
        try {
            int i = Integer.parseInt(str.trim());
            if (i != 0 && i != 1 && i != 2 && i != 3 && i != 5 && i != 10) {
                return AppResult.error("set_missed_call_notify 的 value 无效：" + str + "，支持：0 / 1 / 2 / 3 / 5 / 10");
            }
            MiuiSettings.Telephony.setMissedCallNotifyTimes(context.getContentResolver(), i);
            return AppResult.success("未接来电通知次数已设置为 " + i + " 次");
        } catch (NumberFormatException unused) {
            return AppResult.error("set_missed_call_notify 的 value 无效：" + str + "，支持：0 / 1 / 2 / 3 / 5 / 10");
        }
    }

    private static AppResult getConnectVibrate(Context context) {
        return AppResult.success("{\"connect_vibrate\":\"" + vibrateKeyToLabel(MiuiSettings.Telephony.getVibrateKey(context.getContentResolver())) + "\"}");
    }

    private static AppResult setConnectVibrate(Context context, String str) {
        String str2;
        if (SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF.equals(str)) {
            str2 = "0";
        } else if ("normal".equals(str)) {
            str2 = "50";
        } else if ("strong".equals(str)) {
            str2 = "100";
        } else {
            return AppResult.error("set_connect_vibrate 的 value 无效：" + str + "，支持：off / normal / strong");
        }
        MiuiSettings.Telephony.setVibrateKey(context.getContentResolver(), str2);
        return AppResult.success("接通/挂断振动已设置为 " + str);
    }

    private static AppResult getCallWaitingTone(Context context) {
        return AppResult.success("{\"call_waiting_tone\":\"" + callWaitingToneToString(MiuiSettings.Telephony.getCallWaitingTone(context)) + "\"}");
    }

    private static AppResult setCallWaitingTone(Context context, String str) {
        int i;
        if ("continuous".equals(str)) {
            i = 0;
        } else if ("twice".equals(str)) {
            i = 1;
        } else {
            if (!"twice_vibrate".equals(str)) {
                return AppResult.error("set_call_waiting_tone 的 value 无效：" + str + "，支持：continuous（一直响）/ twice（响两次）/ twice_vibrate（响两次同时振动）");
            }
            i = 2;
        }
        MiuiSettings.Telephony.setCallWaitingTone(context, i);
        return AppResult.success("呼叫等待提示音已设置为 " + str);
    }

    private static String callWaitingToneToString(int i) {
        if (i == 1) {
            return "twice";
        }
        if (i == 2) {
            return "twice_vibrate";
        }
        return "continuous";
    }

    private static AppResult getRejectViaSms(Context context) {
        return AppResult.success("{\"reject_via_sms\":" + MiuiSettings.Telephony.isRejectViaSmsEnable(context.getContentResolver()) + "}");
    }

    private static AppResult setRejectViaSms(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return ToolUtils.boolParamError("set_reject_via_sms", str);
        }
        MiuiSettings.Telephony.setRejectViaSmsEnable(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("短信拒接已".concat(bool.booleanValue() ? "开启" : "关闭"));
    }

    private static AppResult getRejectSmsResponses(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int[] iArr = {R.string.respond_via_sms_canned_response_1, R.string.respond_via_sms_canned_response_2, R.string.respond_via_sms_canned_response_3, R.string.respond_via_sms_canned_response_4};
        StringBuilder sb = new StringBuilder("{\"responses\":[");
        for (int i = 1; i <= 4; i++) {
            String rejectViaSmsResponse = MiuiSettings.Telephony.getRejectViaSmsResponse(contentResolver, "reject_via_sms_response_" + i, context.getString(iArr[i - 1]));
            if (i > 1) {
                sb.append(",");
            }
            sb.append("{\"index\":");
            sb.append(i);
            sb.append(",\"text\":\"");
            sb.append(ToolUtils.escapeJson(rejectViaSmsResponse));
            sb.append("\"}");
        }
        sb.append("]}");
        return AppResult.success(sb.toString());
    }

    private static AppResult setRejectSmsResponse(Context context, String str) {
        if (str == null || !str.contains(":")) {
            return AppResult.error("set_reject_sms_response 的 value 格式错误，应为 \"序号:内容\"，如 \"1:我在开会\"");
        }
        int iIndexOf = str.indexOf(58);
        String strTrim = str.substring(0, iIndexOf).trim();
        String strSubstring = str.substring(iIndexOf + 1);
        try {
            int i = Integer.parseInt(strTrim);
            if (i < 1 || i > 4) {
                return AppResult.error("序号超出范围：" + i + "，支持 1~4");
            }
            if (TextUtils.isEmpty(strSubstring.replaceAll("\\s*", ""))) {
                return AppResult.error("拒接短信内容不能为空");
            }
            MiuiSettings.Telephony.setRejectViaSmsRespone(context.getContentResolver(), "reject_via_sms_response_" + i, strSubstring);
            return AppResult.success("{\"index\":" + i + ",\"text\":\"" + ToolUtils.escapeJson(strSubstring) + "\"}");
        } catch (NumberFormatException unused) {
            return AppResult.error("序号无效：" + strTrim + "，支持 1~4");
        }
    }

    private static AppResult getDtmfType(Context context) {
        int i = Settings.System.getInt(context.getContentResolver(), "dtmf_tone_type", 0);
        StringBuilder sb = new StringBuilder("{\"dtmf_type\":\"");
        sb.append(i == 0 ? "normal" : "long");
        sb.append("\"}");
        return AppResult.success(sb.toString());
    }

    private static AppResult setDtmfType(Context context, String str) {
        int i;
        if ("normal".equals(str)) {
            i = 0;
        } else {
            if (!"long".equals(str)) {
                return AppResult.error("set_dtmf_type 的 value 无效：" + str + "，支持：normal / long");
            }
            i = 1;
        }
        Settings.System.putInt(context.getContentResolver(), "dtmf_tone_type", i);
        return AppResult.success("DTMF 按键音时长已设置为 " + str);
    }

    private static String getDialPadToneValue(ContentResolver contentResolver) {
        return Settings.System.getInt(contentResolver, "dial_pad_touch_tone", -1) == 0 ? "piano" : "default";
    }

    private static String vibrateKeyToLabel(String str) {
        if ("0".equals(str)) {
            return SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF;
        }
        return "100".equals(str) ? "strong" : "normal";
    }
}

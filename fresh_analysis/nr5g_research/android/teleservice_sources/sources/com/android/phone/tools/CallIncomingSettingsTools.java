package com.android.phone.tools;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import com.aios.apptoolsdk.AppResult;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.Phone;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallIncomingSettingsTools {
    public static AppResult callSettings(Context context, String str, String str2, String str3, Integer num) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        Integer numResolveSlotId = ToolUtils.resolveSlotId(num);
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "get_handon_ringer":
                return getHandonRinger(context);
            case "set_voice_report":
                return setVoiceReport(context, str2);
            case "get_turn_over_mute":
                return getTurnOverMute(context);
            case "set_crescendo_ringer":
                return setCrescendoRinger(context, str2);
            case "get_antispam_stranger":
                return getAntispamStranger(context);
            case "get_call_waiting":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getCallWaiting(context, numResolveSlotId.intValue());
            case "get_caller_id":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getCallerId(context, numResolveSlotId.intValue());
            case "get_crescendo_ringer":
                return getCrescendoRinger(context);
            case "set_turn_over_mute":
                return setTurnOverMute(context, str2);
            case "set_flash_when_ring":
                return setFlashWhenRing(context, str2);
            case "set_call_forward":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setCallForward(context, str2, str3, numResolveSlotId.intValue());
            case "set_caller_id":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setCallerId(context, str2, numResolveSlotId.intValue());
            case "set_proximity_sensor":
                return setProximitySensor(context, str2);
            case "get_voice_report":
                return getVoiceReport(context);
            case "get_proximity_sensor":
                return getProximitySensor(context);
            case "set_handon_ringer":
                return setHandonRinger(context, str2);
            case "get_flash_when_ring":
                return getFlashWhenRing(context);
            case "set_antispam_stranger":
                return setAntispamStranger(context, str2);
            case "set_call_waiting":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return setCallWaiting(context, str2, numResolveSlotId.intValue());
            case "get_call_forward":
                if (numResolveSlotId == null) {
                    return ToolUtils.slotRequiredError();
                }
                return getCallForward(context, str2, numResolveSlotId.intValue());
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get_voice_report / set_voice_report / get_call_waiting / set_call_waiting / get_call_forward / set_call_forward / get_caller_id / set_caller_id / get_turn_over_mute / set_turn_over_mute / get_handon_ringer / set_handon_ringer / get_crescendo_ringer / set_crescendo_ringer / get_flash_when_ring / set_flash_when_ring / get_proximity_sensor / set_proximity_sensor / get_antispam_stranger / set_antispam_stranger（陌生来电第一声响铃静音）");
        }
    }

    private static AppResult getVoiceReport(Context context) {
        return AppResult.success("{\"voice_report\":\"" + voiceReportToString(Settings.Global.getInt(context.getContentResolver(), "voice_report_method_phone", 2)) + "\"}");
    }

    private static AppResult setVoiceReport(Context context, String str) {
        String strTrim = str != null ? str.trim() : "";
        strTrim.hashCode();
        int i = 2;
        switch (strTrim) {
            case "always":
                i = 1;
                break;
            case "headset_only":
                i = 0;
                break;
            case "off":
                break;
            default:
                return AppResult.error("set_voice_report 的 value 无效：" + str + "，支持：off（关闭）/ always（任何时候）/ headset_only（仅耳机时）");
        }
        Settings.Global.putInt(context.getContentResolver(), "voice_report_method_phone", i);
        return AppResult.success("{\"voice_report\":\"" + str.trim() + "\"}");
    }

    private static String voiceReportToString(int i) {
        if (i == 0) {
            return "headset_only";
        }
        if (i == 1) {
            return "always";
        }
        return SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF;
    }

    private static AppResult getCallWaiting(Context context, int i) {
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        boolean z = false;
        AsyncResult asyncResultDoGetCWRequest = doGetCWRequest(phone, 0, 3500L);
        if (asyncResultDoGetCWRequest == null || asyncResultDoGetCWRequest.exception != null) {
            asyncResultDoGetCWRequest = doGetCWRequest(phone, 1, 3500L);
        }
        if (asyncResultDoGetCWRequest == null) {
            return AppResult.error("查询来电等待超时，请检查网络信号");
        }
        if (asyncResultDoGetCWRequest.exception != null) {
            return AppResult.error("查询来电等待失败：" + asyncResultDoGetCWRequest.exception.getMessage());
        }
        int[] iArr = (int[]) asyncResultDoGetCWRequest.result;
        if (iArr != null && iArr.length >= 2 && iArr[0] == 1 && (iArr[1] & 1) == 1) {
            z = true;
        }
        return AppResult.success("{\"call_waiting\":" + z + "}");
    }

    private static AppResult setCallWaiting(Context context, String str, int i) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_call_waiting 的 value 无效：" + str + "，支持：true / false");
        }
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoSetCWRequest = doSetCWRequest(phone, bool.booleanValue(), 0, 3500L);
        if (asyncResultDoSetCWRequest == null || asyncResultDoSetCWRequest.exception != null) {
            asyncResultDoSetCWRequest = doSetCWRequest(phone, bool.booleanValue(), 1, 3500L);
        }
        if (asyncResultDoSetCWRequest == null) {
            return AppResult.error("设置来电等待超时，请检查网络信号");
        }
        if (asyncResultDoSetCWRequest.exception != null) {
            return AppResult.error("设置来电等待失败：" + asyncResultDoSetCWRequest.exception.getMessage());
        }
        return AppResult.success("{\"call_waiting\":" + bool + "}");
    }

    private static AppResult getCallForward(Context context, String str, int i) {
        int cfReason = parseCfReason(str);
        if (cfReason < 0) {
            return cfReasonError("get_call_forward", str);
        }
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoGetCFRequest = doGetCFRequest(phone, cfReason, 1, 0, 3500L);
        if (asyncResultDoGetCFRequest == null || asyncResultDoGetCFRequest.exception != null) {
            asyncResultDoGetCFRequest = doGetCFRequest(phone, cfReason, 1, 1, 3500L);
        }
        if (asyncResultDoGetCFRequest == null) {
            return AppResult.error("查询来电转接超时，请检查网络信号");
        }
        if (asyncResultDoGetCFRequest.exception != null) {
            return AppResult.error("查询来电转接失败：" + asyncResultDoGetCFRequest.exception.getMessage());
        }
        CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResultDoGetCFRequest.result;
        if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
            return AppResult.success("{\"enabled\":false,\"number\":\"\",\"reason\":\"" + str.trim() + "\"}");
        }
        CallForwardInfo callForwardInfo = callForwardInfoArr[0];
        boolean z = callForwardInfo.status == 1;
        String str2 = callForwardInfo.number;
        if (str2 == null) {
            str2 = "";
        }
        return AppResult.success("{\"enabled\":" + z + ",\"number\":\"" + ToolUtils.escapeJson(str2) + "\",\"reason\":\"" + str.trim() + "\"}");
    }

    private static AppResult setCallForward(Context context, String str, String str2, int i) {
        int cfReason = parseCfReason(str);
        if (cfReason < 0) {
            return cfReasonError("set_call_forward", str);
        }
        if (cfReason != 0) {
            Phone phone = ToolUtils.getPhone(i);
            if (phone == null) {
                return AppResult.error("无法获取 Phone 对象");
            }
            Boolean boolIsUnconditionalForwardEnabled = isUnconditionalForwardEnabled(phone);
            if (boolIsUnconditionalForwardEnabled == null) {
                return AppResult.error("无法在超时内确认始终转接状态，操作已阻止。请检查网络信号后重试");
            }
            if (boolIsUnconditionalForwardEnabled.booleanValue()) {
                return AppResult.error("始终转接（无条件转接）当前已开启，忙时/无应答/无法接通转接无法修改。请先关闭始终转接后再进行设置");
            }
        }
        int i2 = 0;
        boolean z = str2 == null || str2.trim().isEmpty();
        int i3 = z ? 0 : 3;
        String strTrim = z ? "" : str2.trim();
        if (!z && cfReason == 2) {
            i2 = 20;
        }
        int i4 = i2;
        Phone phone2 = ToolUtils.getPhone(i);
        if (phone2 == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoSetCFRequest = doSetCFRequest(phone2, i3, cfReason, strTrim, 1, i4, 0, 3500L);
        if (asyncResultDoSetCFRequest == null || asyncResultDoSetCFRequest.exception != null) {
            asyncResultDoSetCFRequest = doSetCFRequest(phone2, i3, cfReason, strTrim, 1, i4, 1, 3500L);
        }
        if (asyncResultDoSetCFRequest == null) {
            return AppResult.error("设置来电转接超时，请检查网络信号");
        }
        if (asyncResultDoSetCFRequest.exception != null) {
            return AppResult.error("设置来电转接失败：" + asyncResultDoSetCFRequest.exception.getMessage());
        }
        if (z) {
            return AppResult.success("{\"enabled\":false,\"reason\":\"" + str.trim() + "\"}");
        }
        return AppResult.success("{\"enabled\":true,\"number\":\"" + ToolUtils.escapeJson(strTrim) + "\",\"reason\":\"" + str.trim() + "\"}");
    }

    private static AppResult getCallerId(Context context, int i) {
        String str;
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoGetCLIRRequest = doGetCLIRRequest(phone, 0, 3500L);
        if (asyncResultDoGetCLIRRequest == null || asyncResultDoGetCLIRRequest.exception != null) {
            asyncResultDoGetCLIRRequest = doGetCLIRRequest(phone, 1, 3500L);
        }
        if (asyncResultDoGetCLIRRequest == null) {
            return AppResult.error("查询发送本机号码超时，请检查网络信号");
        }
        if (asyncResultDoGetCLIRRequest.exception != null) {
            return AppResult.error("查询发送本机号码失败：" + asyncResultDoGetCLIRRequest.exception.getMessage());
        }
        int[] iArr = (int[]) asyncResultDoGetCLIRRequest.result;
        if (iArr == null || iArr.length < 2) {
            return AppResult.error("查询发送本机号码返回数据异常");
        }
        int i2 = iArr[0];
        if (i2 == 1) {
            str = "hide";
        } else if (i2 == 2) {
            str = "show";
        } else {
            str = "default";
        }
        int i3 = iArr[1];
        return AppResult.success("{\"caller_id\":\"" + str + "\",\"settable\":" + (i3 == 1 || i3 == 3 || i3 == 4) + "}");
    }

    private static AppResult setCallerId(Context context, String str, int i) {
        String strTrim = str != null ? str.trim() : "";
        strTrim.hashCode();
        int i2 = 2;
        switch (strTrim) {
            case "hide":
                i2 = 1;
                break;
            case "show":
                break;
            case "default":
                i2 = 0;
                break;
            default:
                return AppResult.error("set_caller_id 的 value 无效：" + str + "，支持：default（网络默认）/ hide（隐藏号码）/ show（显示号码）");
        }
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoSetCLIRRequest = doSetCLIRRequest(phone, i2, 0, 3500L);
        if (asyncResultDoSetCLIRRequest == null || asyncResultDoSetCLIRRequest.exception != null) {
            asyncResultDoSetCLIRRequest = doSetCLIRRequest(phone, i2, 1, 3500L);
        }
        if (asyncResultDoSetCLIRRequest == null) {
            return AppResult.error("设置发送本机号码超时，请检查网络信号");
        }
        if (asyncResultDoSetCLIRRequest.exception != null) {
            return AppResult.error("设置发送本机号码失败：" + asyncResultDoSetCLIRRequest.exception.getMessage());
        }
        return AppResult.success("{\"caller_id\":\"" + str.trim() + "\"}");
    }

    private static AppResult getTurnOverMute(Context context) {
        return AppResult.success("{\"turn_over_mute\":" + MiuiSettings.Telephony.isTurnOverMuteEnabled(context.getContentResolver()) + "}");
    }

    private static AppResult setTurnOverMute(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_turn_over_mute 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setTurnOverMuteEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"turn_over_mute\":" + bool + "}");
    }

    private static AppResult getHandonRinger(Context context) {
        return AppResult.success("{\"handon_ringer\":" + MiuiSettings.Telephony.isHandonRingerEnabled(context.getContentResolver()) + "}");
    }

    private static AppResult setHandonRinger(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_handon_ringer 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setHandonRingerEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"handon_ringer\":" + bool + "}");
    }

    private static AppResult getCrescendoRinger(Context context) {
        return AppResult.success("{\"crescendo_ringer\":" + MiuiSettings.Telephony.isCrescendoRingerEnable(context.getContentResolver()) + "}");
    }

    private static AppResult setCrescendoRinger(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_crescendo_ringer 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setCrescendoRingerEnable(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"crescendo_ringer\":" + bool + "}");
    }

    private static AppResult getFlashWhenRing(Context context) {
        return AppResult.success("{\"flash_when_ring\":" + MiuiSettings.System.getBoolean(context.getContentResolver(), "flash_when_ring_enabled", false) + "}");
    }

    private static AppResult setFlashWhenRing(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_flash_when_ring 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.System.putBoolean(context.getContentResolver(), "flash_when_ring_enabled", bool.booleanValue());
        return AppResult.success("{\"flash_when_ring\":" + bool + "}");
    }

    private static AppResult getProximitySensor(Context context) {
        return AppResult.success("{\"proximity_sensor\":" + MiuiSettings.Telephony.isProximitySensorEnable(context.getContentResolver()) + "}");
    }

    private static AppResult setProximitySensor(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_proximity_sensor 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setProximitySensorEnable(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"proximity_sensor\":" + bool + "}");
    }

    private static AppResult getAntispamStranger(Context context) {
        return AppResult.success("{\"antispam_stranger\":" + MiuiSettings.Telephony.isAntispamStangerEnabled(context.getContentResolver()) + "}");
    }

    private static AppResult setAntispamStranger(Context context, String str) {
        Boolean bool = ToolUtils.parseBool(str);
        if (bool == null) {
            return AppResult.error("set_antispam_stranger 的 value 无效：" + str + "，支持：true / false");
        }
        MiuiSettings.Telephony.setAntispamStangerEnabled(context.getContentResolver(), bool.booleanValue());
        return AppResult.success("{\"antispam_stranger\":" + bool + "}");
    }

    private static Boolean isUnconditionalForwardEnabled(Phone phone) {
        AsyncResult asyncResultDoGetCFRequest = doGetCFRequest(phone, 0, 1, 0, 2000L);
        if (asyncResultDoGetCFRequest == null || asyncResultDoGetCFRequest.exception != null) {
            return null;
        }
        CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResultDoGetCFRequest.result;
        if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(callForwardInfoArr[0].status == 1);
    }

    private static AsyncResult doGetCWRequest(Phone phone, int i, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-cw-get-" + i);
        handlerThread.start();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallWaitingOption(phone, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static AsyncResult doSetCWRequest(Phone phone, boolean z, int i, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-cw-set-" + i);
        handlerThread.start();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallWaitingOption(phone, z, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static AsyncResult doGetCFRequest(Phone phone, int i, int i2, int i3, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-cf-get-" + i3);
        handlerThread.start();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallForwardingOption(phone, i, i2, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i3));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static AsyncResult doGetCLIRRequest(Phone phone, int i, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-clir-get-" + i);
        handlerThread.start();
        phone.getOutgoingCallerIdDisplay(new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.4
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static AsyncResult doSetCLIRRequest(Phone phone, int i, int i2, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-clir-set-" + i2);
        handlerThread.start();
        phone.setOutgoingCallerIdDisplay(i, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.5
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i2));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static AsyncResult doSetCFRequest(Phone phone, int i, int i2, String str, int i3, int i4, int i5, long j) {
        boolean zAwait;
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-cf-set-" + i5);
        handlerThread.start();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallForwardingOption(phone, i, i2, str, i3, i4, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallIncomingSettingsTools.6
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i5));
        try {
            zAwait = countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            zAwait = false;
        }
        handlerThread.quit();
        if (!zAwait || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static int parseCfReason(String str) {
        if (str == null) {
            return -1;
        }
        String strTrim = str.trim();
        strTrim.hashCode();
        switch (strTrim) {
            case "unconditional":
                return 0;
            case "unreachable":
                return 3;
            case "busy":
                return 1;
            case "no_reply":
                return 2;
            default:
                return -1;
        }
    }

    private static AppResult cfReasonError(String str, String str2) {
        return AppResult.error(str + " 的 value 无效：" + str2 + "，支持：unconditional（无条件）/ busy（忙时）/ no_reply（无应答）/ unreachable（无法接通）");
    }
}

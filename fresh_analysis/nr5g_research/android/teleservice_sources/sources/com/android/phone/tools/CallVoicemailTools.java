package com.android.phone.tools;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.aios.apptoolsdk.AppResult;
import com.android.internal.telephony.Phone;
import java.util.concurrent.CountDownLatch;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallVoicemailTools {
    public static AppResult voicemailSettings(Context context, String str, String str2, String str3, Integer num) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空");
        }
        Integer numResolveSlotId = ToolUtils.resolveSlotId(num);
        if (numResolveSlotId == null) {
            return ToolUtils.slotRequiredError();
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        if (lowerCase.equals("get_voicemail")) {
            return getVoicemail(numResolveSlotId.intValue());
        }
        if (lowerCase.equals("set_voicemail")) {
            return setVoicemail(str2, str3, numResolveSlotId.intValue());
        }
        return AppResult.error("无效的 action：" + str + "，支持：get_voicemail / set_voicemail");
    }

    private static AppResult getVoicemail(int i) {
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        String voiceMailNumber = phone.getVoiceMailNumber();
        String voiceMailAlphaTag = phone.getVoiceMailAlphaTag();
        String string = voiceMailAlphaTag != null ? voiceMailAlphaTag.toString() : "";
        if (voiceMailNumber == null) {
            voiceMailNumber = "";
        }
        return AppResult.success("{\"number\":\"" + ToolUtils.escapeJson(voiceMailNumber) + "\",\"label\":\"" + ToolUtils.escapeJson(string) + "\"}");
    }

    private static AppResult setVoicemail(String str, String str2, int i) {
        String string;
        Object obj;
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_voicemail 需要提供 number 参数（语音信箱号码）");
        }
        Phone phone = ToolUtils.getPhone(i);
        if (phone == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        if (str2 != null && !str2.trim().isEmpty()) {
            string = str2.trim();
        } else {
            String voiceMailAlphaTag = phone.getVoiceMailAlphaTag();
            string = voiceMailAlphaTag != null ? voiceMailAlphaTag.toString() : "";
        }
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-vm-set");
        handlerThread.start();
        phone.setVoiceMailNumber(string, str.trim(), new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.CallVoicemailTools.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(0));
        boolean zAwaitLatch = ToolUtils.awaitLatch(countDownLatch, 1000L);
        handlerThread.quit();
        if (zAwaitLatch && (obj = objArr[0]) != null) {
            AsyncResult asyncResult = (AsyncResult) obj;
            if (asyncResult.exception != null) {
                return AppResult.error("设置语音信箱失败：" + asyncResult.exception.getMessage());
            }
        }
        return AppResult.success("{\"number\":\"" + ToolUtils.escapeJson(str.trim()) + "\",\"label\":\"" + ToolUtils.escapeJson(string) + "\"}");
    }
}

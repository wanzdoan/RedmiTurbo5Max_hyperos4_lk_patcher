package com.android.phone.tools;

import com.aios.apptoolsdk.AppResult;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class ToolUtils {
    public static Integer resolveSlotId(Integer num) {
        if (num != null) {
            return num;
        }
        try {
            int defaultVoiceSlotId = DefaultSimManager.getDefaultVoiceSlotId();
            if (defaultVoiceSlotId >= 0) {
                return Integer.valueOf(defaultVoiceSlotId);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Phone getPhone(int i) {
        try {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones != null && phones.length != 0) {
                return (i < 0 || i >= phones.length) ? phones[0] : phones[i];
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static boolean awaitLatch(CountDownLatch countDownLatch, long j) {
        try {
            return countDownLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public static Boolean parseBool(String str) {
        if ("true".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        return null;
    }

    public static AppResult boolParamError(String str, String str2) {
        return AppResult.error(str + " 的 value 无效：" + str2 + "，支持：true / false");
    }

    public static AppResult slotRequiredError() {
        return AppResult.error("当前未设置默认拨号卡，请通过 sim_slot 参数指定 SIM 卡（0=SIM1，1=SIM2）");
    }

    public static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

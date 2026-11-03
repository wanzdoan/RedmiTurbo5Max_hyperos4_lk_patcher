package com.android.phone.statistics.telmqs;

import android.text.TextUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import miui.mqsas.sdk.MQSEventManagerDelegate;

/* JADX INFO: loaded from: classes.dex */
public abstract class MqsasTool {
    public static void reportTelephonyException(String str) {
        if (TextUtils.isEmpty(str) || MiStatInterfaceUtil.isForbiddenRecordMqsEvent()) {
            return;
        }
        try {
            MQSEventManagerDelegate.getInstance().reportTelephonyEvent(22, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

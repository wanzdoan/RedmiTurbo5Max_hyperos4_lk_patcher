package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.Build;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.WapPushOverSms;

/* JADX INFO: loaded from: classes.dex */
public class MtkWapPushOverSms extends WapPushOverSms {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final String TAG = "Mtk_WAP_PUSH";

    public MtkWapPushOverSms(Context context, FeatureFlags featureFlags) {
        super(context, featureFlags);
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x00ec */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int dispatchWapPdu(byte[] r24, com.android.internal.telephony.InboundSmsHandler.SmsBroadcastReceiver r25, com.android.internal.telephony.InboundSmsHandler r26, java.lang.String r27, java.lang.String r28, int r29, long r30) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 516
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.MtkWapPushOverSms.dispatchWapPdu(byte[], com.android.internal.telephony.InboundSmsHandler$SmsBroadcastReceiver, com.android.internal.telephony.InboundSmsHandler, java.lang.String, java.lang.String, int, long):int");
    }
}

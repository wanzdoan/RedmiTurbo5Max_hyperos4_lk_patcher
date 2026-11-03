package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import com.xiaomi.modem.ModemLog;

/* JADX INFO: loaded from: classes.dex */
public class ModemAiLog extends ModemLog {
    private static final Object mLock = new Object();
    protected static volatile ModemAiLog mLoger;

    private ModemAiLog(Context context) {
        super(context, "persist.vendor.radio.print_modem_ai", "2");
    }

    public static ModemAiLog getInstance(Context context) {
        try {
            if (mLoger == null) {
                synchronized (mLock) {
                    try {
                        if (mLoger == null) {
                            mLoger = new ModemAiLog(context);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLoger;
    }
}

package com.xiaomi.modem.modemipc;

import android.content.Context;
import com.xiaomi.modem.ModemLog;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class ModemIpcLog extends ModemLog {
    private static final Object mLock = new Object();
    protected static ModemIpcLog mLoger = null;

    public ModemIpcLog(Context context) {
        super(context, "persist.vendor.radio.print_modem_ipc");
    }

    public static ModemIpcLog getInstance(Context cnt) {
        if (mLoger != null) {
            return mLoger;
        }
        synchronized (mLock) {
            if (mLoger == null) {
                mLoger = new ModemIpcLog(cnt);
            }
        }
        return mLoger;
    }
}

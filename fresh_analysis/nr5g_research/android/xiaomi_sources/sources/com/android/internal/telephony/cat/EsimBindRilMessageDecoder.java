package com.android.internal.telephony.cat;

import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.uicc.IccFileHandler;

/* JADX INFO: loaded from: classes.dex */
public class EsimBindRilMessageDecoder extends RilMessageDecoder {
    private static int mSimCount = 0;
    private static EsimBindRilMessageDecoder[] mInstance = null;
    private static String TAG = EsimBindRilMessageDecoder.class.getSimpleName();

    public static synchronized EsimBindRilMessageDecoder getInstance(Handler caller, IccFileHandler fh, Context context, int slotId) {
        if (mInstance == null) {
            mSimCount = TelephonyManager.getDefault().getSupportedModemCount();
            mInstance = new EsimBindRilMessageDecoder[mSimCount];
            for (int i = 0; i < mSimCount; i++) {
                mInstance[i] = null;
            }
        }
        if (slotId != -1 && slotId < mSimCount) {
            if (mInstance[slotId] == null) {
                CatLog.d(TAG, "Create EsimBindRilMessageDecoder for : " + slotId);
                mInstance[slotId] = new EsimBindRilMessageDecoder(caller, fh, context);
            }
            return mInstance[slotId];
        }
        CatLog.d(TAG, "invaild slot id: " + slotId);
        return null;
    }

    private EsimBindRilMessageDecoder(Handler caller, IccFileHandler fh, Context context) {
        super(caller, fh, context);
    }
}

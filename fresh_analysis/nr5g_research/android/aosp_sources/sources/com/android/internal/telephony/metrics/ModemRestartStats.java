package com.android.internal.telephony.metrics;

import android.os.Build;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class ModemRestartStats {
    private static final String TAG = "ModemRestartStats";

    public static void onModemRestart(String str) {
        String strTruncateString = truncateString(str, 100);
        String strTruncateString2 = truncateString(Build.getRadioVersion(), 100);
        int carrierId = getCarrierId();
        Rlog.d(TAG, "Modem restart (carrier=" + carrierId + "): " + strTruncateString);
        TelephonyStatsLog.write(312, strTruncateString2, strTruncateString, carrierId);
    }

    private static String truncateString(String str, int i) {
        String strNullToEmpty = nullToEmpty(str);
        return strNullToEmpty.length() > i ? strNullToEmpty.substring(0, i) : strNullToEmpty;
    }

    private static String nullToEmpty(String str) {
        return str != null ? str : "";
    }

    private static int getCarrierId() {
        try {
            int carrierId = -1;
            for (Phone phone : PhoneFactory.getPhones()) {
                try {
                    carrierId = phone.getCarrierId();
                    if (carrierId != -1) {
                        return carrierId;
                    }
                } catch (IllegalStateException unused) {
                    return carrierId;
                }
            }
            return carrierId;
        } catch (IllegalStateException unused2) {
            return -1;
        }
    }
}

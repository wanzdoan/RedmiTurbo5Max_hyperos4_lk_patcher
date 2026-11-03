package miui.telephony;

import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiAisSimLockUtils {
    public static final String AIS_MCC_MNC = "52003";
    public static final boolean IS_AIS_SIMLOCK_BUILD = "100".equals(SystemProperties.get("persist.radio.ais_sim_lock", ""));
    private static final String TAG = "MiuiAisSimLockUtils";

    public static boolean isNonAisSimRestricted() {
        boolean isRestricted = false;
        if (!IS_AIS_SIMLOCK_BUILD) {
            return false;
        }
        int nonAisSimCount = 0;
        int insertSimCount = 0;
        TelephonyManager tm = TelephonyManager.getDefault();
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            if (tm.hasIccCard(i)) {
                insertSimCount++;
                String mccmnc = tm.getSimOperatorNumericForPhone(i);
                Rlog.d(TAG, "mccmnc =" + mccmnc);
                if (TextUtils.isEmpty(mccmnc) || (!TextUtils.isEmpty(mccmnc) && !mccmnc.equals(AIS_MCC_MNC))) {
                    nonAisSimCount++;
                }
            }
        }
        if ((insertSimCount == 1 && nonAisSimCount == 1) || (insertSimCount == 2 && nonAisSimCount == 2)) {
            isRestricted = true;
        }
        Rlog.d(TAG, "isRestricted =" + isRestricted + ",insertSimCount =" + insertSimCount + ",nonAisSimCount=" + nonAisSimCount);
        return isRestricted;
    }
}

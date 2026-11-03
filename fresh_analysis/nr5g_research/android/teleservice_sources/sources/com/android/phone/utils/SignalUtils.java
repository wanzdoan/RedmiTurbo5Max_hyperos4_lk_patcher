package com.android.phone.utils;

import android.telephony.SignalStrength;
import android.util.Log;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class SignalUtils {
    private static String LOG_TAG = "SignalUtils";
    public static final int[] sLteRsrpThresholds = {-115, -105, -95, -85};
    public static final int[] sLteRsrqThresholds = {-19, -17, -14, -12};
    private static final int[] sLteRssnrThresholds = {-3, 1, 5, 13};

    public static int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public static int updateLevelWithMeasure(int i, int[] iArr) {
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        if (i >= iArr[3]) {
            return 4;
        }
        if (i >= iArr[2]) {
            return 3;
        }
        if (i >= iArr[1]) {
            return 2;
        }
        return i >= iArr[0] ? 1 : 0;
    }

    private static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    public static boolean isLteSignalMoreModerate(int i) {
        SignalStrength signalStrength;
        if (isValidatePhoneId(i) && (signalStrength = PhoneFactory.getPhone(i).getSignalStrength()) != null) {
            int lteRsrp = signalStrength.getLteRsrp();
            int lteRsrq = signalStrength.getLteRsrq();
            int iInRangeOrUnavailable = inRangeOrUnavailable(lteRsrp, -140, -44);
            int iInRangeOrUnavailable2 = inRangeOrUnavailable(lteRsrq, -35, -3);
            int iUpdateLevelWithMeasure = updateLevelWithMeasure(iInRangeOrUnavailable, sLteRsrpThresholds);
            int iUpdateLevelWithMeasure2 = updateLevelWithMeasure(iInRangeOrUnavailable2, sLteRsrqThresholds);
            Log.d(LOG_TAG, "isLteSignalModerate rsrpLevel=" + iUpdateLevelWithMeasure + ",rsrqLevel=" + iUpdateLevelWithMeasure2);
            if (iUpdateLevelWithMeasure >= 2 && iUpdateLevelWithMeasure2 >= 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLteSignalSuitForRestoreFiveG(int i) {
        SignalStrength signalStrength;
        boolean z = false;
        if (isValidatePhoneId(i) && (signalStrength = PhoneFactory.getPhone(i).getSignalStrength()) != null) {
            int lteRsrp = signalStrength.getLteRsrp();
            int lteRsrq = signalStrength.getLteRsrq();
            int lteRssnr = signalStrength.getLteRssnr();
            int iInRangeOrUnavailable = inRangeOrUnavailable(lteRsrp, -140, -44);
            int iInRangeOrUnavailable2 = inRangeOrUnavailable(lteRsrq, -35, -3);
            int iInRangeOrUnavailable3 = inRangeOrUnavailable(lteRssnr, -20, 30);
            if (iInRangeOrUnavailable != Integer.MAX_VALUE && iInRangeOrUnavailable2 != Integer.MAX_VALUE && iInRangeOrUnavailable3 != Integer.MAX_VALUE) {
                int iUpdateLevelWithMeasure = updateLevelWithMeasure(iInRangeOrUnavailable, sLteRsrpThresholds);
                int iUpdateLevelWithMeasure2 = updateLevelWithMeasure(iInRangeOrUnavailable2, sLteRsrqThresholds);
                int iUpdateLevelWithMeasure3 = updateLevelWithMeasure(iInRangeOrUnavailable3, sLteRssnrThresholds);
                z = iUpdateLevelWithMeasure <= 2 || iUpdateLevelWithMeasure2 <= 3 || iUpdateLevelWithMeasure3 <= 3;
                Log.d(LOG_TAG, "isLteSignalSuitForRestoreFiveG rp=" + iUpdateLevelWithMeasure + ",rq=" + iUpdateLevelWithMeasure2 + ",snr=" + iUpdateLevelWithMeasure3 + ",rst=" + z);
            }
        }
        return z;
    }

    public static boolean isLteSignalSuitForReattach(int i) {
        SignalStrength signalStrength;
        boolean z = false;
        if (isValidatePhoneId(i) && (signalStrength = PhoneFactory.getPhone(i).getSignalStrength()) != null) {
            int lteRsrp = signalStrength.getLteRsrp();
            int lteRsrq = signalStrength.getLteRsrq();
            int lteRssnr = signalStrength.getLteRssnr();
            int iInRangeOrUnavailable = inRangeOrUnavailable(lteRsrp, -140, -44);
            int iInRangeOrUnavailable2 = inRangeOrUnavailable(lteRsrq, -35, -3);
            int iInRangeOrUnavailable3 = inRangeOrUnavailable(lteRssnr, -20, 30);
            if (iInRangeOrUnavailable != Integer.MAX_VALUE && iInRangeOrUnavailable2 != Integer.MAX_VALUE && iInRangeOrUnavailable3 != Integer.MAX_VALUE) {
                int iUpdateLevelWithMeasure = updateLevelWithMeasure(iInRangeOrUnavailable, sLteRsrpThresholds);
                int iUpdateLevelWithMeasure2 = updateLevelWithMeasure(iInRangeOrUnavailable2, sLteRsrqThresholds);
                int iUpdateLevelWithMeasure3 = updateLevelWithMeasure(iInRangeOrUnavailable3, sLteRssnrThresholds);
                z = iUpdateLevelWithMeasure >= 2 || iUpdateLevelWithMeasure2 <= 2 || iUpdateLevelWithMeasure3 <= 2;
                Log.d(LOG_TAG, "isLteSignalSuitForReattach rp=" + iUpdateLevelWithMeasure + ",rq=" + iUpdateLevelWithMeasure2 + ",snr=" + iUpdateLevelWithMeasure3 + ",rst=" + z);
            }
        }
        return z;
    }
}

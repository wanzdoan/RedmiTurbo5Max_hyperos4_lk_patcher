package com.android.phone.network;

import android.util.Log;
import com.android.phone.ModemDataCollectorBase;

/* JADX INFO: loaded from: classes.dex */
public class NrcaScheduleEvaluator {
    private static volatile NrcaScheduleEvaluator sInstance;

    private NrcaScheduleEvaluator() {
    }

    public static NrcaScheduleEvaluator getInstance() {
        if (sInstance == null) {
            synchronized (NrcaScheduleEvaluator.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new NrcaScheduleEvaluator();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public boolean getNrcaScheduleStatus() {
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase == null) {
            Log.d("NrcaScheduleEvaluator", "getNrcaScheduleStatus: collector is null");
            return true;
        }
        ModemFiveGAScheduleInfo[] modemFiveGAScheduleInfoArr = modemDataCollectorBase.get5GAScheduleInfo();
        if (!isValidScheduleInfos(modemFiveGAScheduleInfoArr)) {
            Log.d("NrcaScheduleEvaluator", "getNrcaScheduleStatus: scheduleInfos is invalid");
            return true;
        }
        byte b = modemFiveGAScheduleInfoArr[modemFiveGAScheduleInfoArr.length - 1].nrCaNum;
        Log.d("NrcaScheduleEvaluator", "getNrcaScheduleStatus: lastNrCaNum=" + ((int) b));
        if (b > 1) {
            return checkFiveGAStatus(modemFiveGAScheduleInfoArr);
        }
        return true;
    }

    private boolean checkFiveGAStatus(ModemFiveGAScheduleInfo[] modemFiveGAScheduleInfoArr) {
        for (ModemFiveGAScheduleInfo modemFiveGAScheduleInfo : modemFiveGAScheduleInfoArr) {
            if (modemFiveGAScheduleInfo.nrCaNum <= 1) {
                Log.d("NrcaScheduleEvaluator", "checkFiveGAStatus: not all records are 5GA, return true");
                return true;
            }
        }
        long j = 0;
        for (ModemFiveGAScheduleInfo modemFiveGAScheduleInfo2 : modemFiveGAScheduleInfoArr) {
            long[] jArr = modemFiveGAScheduleInfo2.tbBytes;
            if (jArr == null || jArr.length == 0) {
                Log.d("NrcaScheduleEvaluator", "checkFiveGAStatus: tbBytes is empty, return true");
                return true;
            }
            j += jArr[0];
        }
        if (j <= 62500 || j >= 5000000) {
            Log.d("NrcaScheduleEvaluator", "checkFiveGAStatus: PCC total out of range, return true");
            return true;
        }
        byte b = modemFiveGAScheduleInfoArr[0].nrCaNum;
        for (int i = 1; i < b; i++) {
            if (isSccAbnormal(modemFiveGAScheduleInfoArr, i)) {
                Log.d("NrcaScheduleEvaluator", "checkFiveGAStatus: SCC[" + i + "] abnormal >= Threshold, return false");
                return false;
            }
        }
        Log.d("NrcaScheduleEvaluator", "checkFiveGAStatus: all SCC normal, return true");
        return true;
    }

    private boolean isSccAbnormal(ModemFiveGAScheduleInfo[] modemFiveGAScheduleInfoArr, int i) {
        int[] iArr;
        int[] iArr2;
        int i2;
        int i3 = 0;
        for (ModemFiveGAScheduleInfo modemFiveGAScheduleInfo : modemFiveGAScheduleInfoArr) {
            if (modemFiveGAScheduleInfo != null && (iArr = modemFiveGAScheduleInfo.pdcpDecode) != null && (iArr2 = modemFiveGAScheduleInfo.crcFail) != null && i < iArr.length && i < iArr2.length && ((i2 = iArr[i]) == 0 || iArr2[i] / i2 > 0.4f)) {
                i3++;
            }
        }
        return i3 >= 3;
    }

    private boolean isValidScheduleInfos(ModemFiveGAScheduleInfo[] modemFiveGAScheduleInfoArr) {
        if (modemFiveGAScheduleInfoArr == null || modemFiveGAScheduleInfoArr.length == 0) {
            return false;
        }
        for (ModemFiveGAScheduleInfo modemFiveGAScheduleInfo : modemFiveGAScheduleInfoArr) {
            if (modemFiveGAScheduleInfo == null) {
                return false;
            }
        }
        return true;
    }
}

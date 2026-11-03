package com.android.internal.telephony;

import android.content.Context;
import android.os.Bundle;
import android.provider.BlockedNumberContract;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class BlockChecker {
    @Deprecated
    public static boolean isBlocked(Context context, String str) {
        return isBlocked(context, str, null);
    }

    public static boolean isBlocked(Context context, String str, Bundle bundle) {
        return getBlockStatus(context, str, bundle) != 0;
    }

    public static int getBlockStatus(Context context, String str, Bundle bundle) {
        int iShouldSystemBlockNumber;
        long jNanoTime = System.nanoTime();
        try {
            iShouldSystemBlockNumber = BlockedNumberContract.SystemContract.shouldSystemBlockNumber(context, str, bundle);
            if (iShouldSystemBlockNumber != 0) {
                try {
                    Rlog.d("BlockChecker", str + " is blocked.");
                } catch (Exception e) {
                    e = e;
                    Rlog.e("BlockChecker", "Exception checking for blocked number: " + e);
                }
            }
        } catch (Exception e2) {
            e = e2;
            iShouldSystemBlockNumber = 0;
        }
        int iNanoTime = (int) ((System.nanoTime() - jNanoTime) / TimeUtils.NANOS_PER_MS);
        if (iNanoTime > 500) {
            Rlog.d("BlockChecker", "Blocked number lookup took: " + iNanoTime + " ms.");
        }
        return iShouldSystemBlockNumber;
    }
}

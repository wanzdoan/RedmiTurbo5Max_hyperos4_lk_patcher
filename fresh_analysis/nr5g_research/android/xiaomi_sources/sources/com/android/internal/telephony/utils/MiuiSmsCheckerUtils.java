package com.android.internal.telephony.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.telephony.BlockChecker;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsCheckerUtils {
    public static final long DEFAULT_TIMEOUT_MILLIS = 1000;
    private static final String TAG = "MiuiSmsCheckerUtils";

    private MiuiSmsCheckerUtils() {
    }

    public static boolean isBlocked(final Context context, final String phoneNumber, final Bundle extras, long timeout) {
        if (timeout < 0) {
            Log.v(TAG, "isBlocked timeout = " + timeout);
            return BlockChecker.isBlocked(context, phoneNumber, extras);
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Boolean> futureTask = new FutureTask<>(new Callable<Boolean>() { // from class: com.android.internal.telephony.utils.MiuiSmsCheckerUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() {
                boolean res = BlockChecker.isBlocked(context, phoneNumber, extras);
                return new Boolean(res);
            }
        });
        executor.execute(futureTask);
        try {
            try {
                Boolean result = futureTask.get(timeout, TimeUnit.MILLISECONDS);
                if (result != null && result.booleanValue()) {
                    Log.v(TAG, "isBlocked return true");
                    try {
                        executor.shutdown();
                    } catch (Exception e) {
                        Log.e(TAG, "isBlocked shutdown exp ", e);
                    }
                    return true;
                }
            } catch (Exception e2) {
                Log.e(TAG, "isBlocked exp is ", e2);
                try {
                    futureTask.cancel(true);
                } catch (Exception e1) {
                    Log.e(TAG, "isBlocked futureTask.cancel exp ", e1);
                }
            }
            try {
                executor.shutdown();
            } catch (Exception e3) {
                Log.e(TAG, "isBlocked shutdown exp ", e3);
            }
            Log.v(TAG, "isBlocked return false");
            return false;
        } catch (Throwable th) {
            try {
                executor.shutdown();
            } catch (Exception e4) {
                Log.e(TAG, "isBlocked shutdown exp ", e4);
            }
            throw th;
        }
    }

    public static boolean isBlocked(Context context, String phoneNumber) {
        return isBlocked(context, phoneNumber, null, -1L);
    }
}

package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import android.os.SystemProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ModemAiDataCollector {
    private static final String DATA_FILE_PATH = "sdcard/ModemAiDataCollector/";
    private static final String LOG_TAG = "ModemAiDataCollector";
    private static final String MODEM_AI_COLLECTOR_PROP_STRINT = "persist.vendor.radio.modem_ai_signal_collector";
    private static final String PROP_DATA_COLLECTOR_OFF = "0";
    private static final String PROP_DATA_COLLECTOR_ON = "1";
    private static String mDataCollectorFlag = "0";
    private static volatile ModemAiDataCollector mInstance;
    private static final Object mLock = new Object();
    private final Context mContext;

    private ModemAiDataCollector(Context context, String str) {
        this.mContext = context;
        mDataCollectorFlag = SystemProperties.get(str, "0");
    }

    public static ModemAiDataCollector getInstance(Context context) {
        if (mInstance == null) {
            synchronized (mLock) {
                try {
                    if (mInstance == null) {
                        mInstance = new ModemAiDataCollector(context, MODEM_AI_COLLECTOR_PROP_STRINT);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public void appendDebugStringToFile(String str, String str2) {
        if ("1".equals(mDataCollectorFlag)) {
            log("Try appendDebugStringToFile");
            checkFilePath();
            File file = new File(DATA_FILE_PATH + str);
            if (!file.exists()) {
                try {
                    log("create file:" + file.getAbsolutePath());
                    file.createNewFile();
                    log("create file success!");
                } catch (IOException e) {
                    log("create file failed!");
                    printException(e);
                    return;
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                try {
                    bufferedWriter.write(str2);
                    bufferedWriter.newLine();
                    log("Data appended to file: " + file.getAbsolutePath());
                    bufferedWriter.close();
                } catch (Throwable th) {
                    try {
                        bufferedWriter.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e2) {
                log("Failed to append data to file: " + e2.getMessage());
                printException(e2);
            }
        }
    }

    private boolean checkFilePath() {
        File file = new File(DATA_FILE_PATH);
        try {
            if (file.exists()) {
                return true;
            }
            if (!file.mkdirs()) {
                log("Failed to create directory: " + file.getAbsolutePath());
                return false;
            }
            log("Directory created: " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            log("Exception while checking directory: " + file.getAbsolutePath());
            printException(e);
            return false;
        }
    }

    private void printException(Exception exc) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : exc.getStackTrace()) {
            sb.append(stackTraceElement.toString());
            sb.append("\n");
        }
        log("Error:" + exc.getMessage() + "\n" + sb.toString());
    }

    private void log(String str) {
        ModemAiLog.getInstance(this.mContext).log(LOG_TAG, str);
    }
}

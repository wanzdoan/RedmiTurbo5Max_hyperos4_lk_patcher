package com.android.internal.telephony;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class DebugService {
    private static String TAG = "DebugService";
    private final Context mContext;

    public DebugService(Context context) {
        log("DebugService:");
        this.mContext = context;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            String str = strArr[0];
            str.hashCode();
            switch (str) {
                case "--saveatoms":
                    if (Build.IS_DEBUGGABLE) {
                        log("Saving atoms..");
                        PhoneFactory.getMetricsCollector().flushAtomsStorage();
                        break;
                    }
                    break;
                case "--clearatoms":
                    if (Build.IS_DEBUGGABLE || this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
                        log("Clearing atoms..");
                        PhoneFactory.getMetricsCollector().clearAtomsStorage();
                        if (strArr.length > 1 && "--saveFileImmediately".equals(strArr[1])) {
                            log("Setting save-immediately mode to true after clearing.");
                            PhoneFactory.getMetricsCollector().setSaveFileImmediately(true);
                        } else {
                            log("Restoring default save-delay mode after clearing.");
                            PhoneFactory.getMetricsCollector().setSaveFileImmediately(false);
                        }
                        break;
                    } else {
                        printWriter.println("ERROR: Failed to clear atom, does not have permission.");
                        logw("Clearing atoms.. failed, does not have permission");
                        break;
                    }
                    break;
                case "--pullAtomsBase64":
                    if (Build.IS_DEBUGGABLE || this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
                        log("Pulling atoms..");
                        try {
                            byte[] atomsProtoBytes = PhoneFactory.getMetricsCollector().getAtomsProtoBytes();
                            if (atomsProtoBytes != null) {
                                printWriter.println(Base64.encodeToString(atomsProtoBytes, 2));
                            }
                            if (strArr.length > 1 && "--clearAtoms".equals(strArr[1])) {
                                log("clear atoms after pulling.");
                                PhoneFactory.getMetricsCollector().clearAtomsStorage();
                                PhoneFactory.getMetricsCollector().setSaveFileImmediately(true);
                                break;
                            }
                        } catch (Exception e) {
                            Rlog.e(TAG, "Failed to get/encode atom data", e);
                            return;
                        }
                    } else {
                        logw("Pulling atoms.. failed does not have permission");
                        break;
                    }
                    break;
            }
            return;
        }
        log("Dump telephony.");
        PhoneFactory.dump(fileDescriptor, printWriter, strArr);
    }

    private static void log(String str) {
        Rlog.d(TAG, "DebugService " + str);
    }

    private static void logw(String str) {
        Rlog.w(TAG, "DebugService " + str);
    }
}

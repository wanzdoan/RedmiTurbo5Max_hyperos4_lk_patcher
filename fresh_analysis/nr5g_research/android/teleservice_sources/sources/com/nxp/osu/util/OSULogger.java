package com.nxp.osu.util;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public abstract class OSULogger {
    private static boolean sIsNfcDisableSupport = true;
    private static boolean sIsNfcRestartSupport = false;
    private static String sLoggerfilePath;
    private static PrintWriter sWriter;

    public static boolean isDebug() {
        return true;
    }

    public static boolean isNFCDisableRequired() {
        return sIsNfcDisableSupport;
    }

    public static boolean isNfcRestartRequired() {
        return sIsNfcRestartSupport;
    }

    public static void setNFCDisableRequired(boolean z) {
        sIsNfcDisableSupport = z;
    }

    public static void setNFCRestartSupported(boolean z) {
        sIsNfcRestartSupport = z;
    }

    public static String getSharedPreferenceKey() {
        return "OSUJar";
    }

    public static String getLoggerPath() {
        return sLoggerfilePath;
    }

    public static boolean isOpened() {
        return sWriter != null;
    }

    public static void open(Context context) {
        if (context != null) {
            try {
                String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
                if (str == null) {
                    Log.e("OSU-Logger", "Not able to determine PKG data directory");
                    return;
                }
                sLoggerfilePath = str + "/OSU_" + new SimpleDateFormat("dd-MM-YY_hh_mm_ss_SSS").format(new Date()) + ".txt";
                sWriter = new PrintWriter(new File(sLoggerfilePath));
            } catch (Exception e) {
                sLoggerfilePath = null;
                e.printStackTrace();
            }
        }
    }

    public static void write(String str, boolean z) {
        if (sWriter == null || str == null) {
            return;
        }
        if (z) {
            str = new Date() + " : " + str;
        }
        sWriter.println(str);
    }

    public static void write(byte[] bArr, boolean z) {
        if (bArr != null) {
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            for (byte b : bArr) {
                sb.append(String.format("%02x", Byte.valueOf(b)));
            }
            write(sb.toString(), z);
        }
    }

    public static void close() {
        PrintWriter printWriter = sWriter;
        if (printWriter != null) {
            printWriter.close();
            sWriter = null;
            sLoggerfilePath = null;
        }
    }
}

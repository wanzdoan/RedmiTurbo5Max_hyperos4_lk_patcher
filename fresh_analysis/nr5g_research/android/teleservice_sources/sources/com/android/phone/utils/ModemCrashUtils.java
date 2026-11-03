package com.android.phone.utils;

import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemCrashUtils {
    /* JADX WARN: Code duplicated, block: B:36:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static String getModemCrashReason() throws Throwable {
        Throwable th;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                File modemCrashFileIfExists = getModemCrashFileIfExists();
                if (modemCrashFileIfExists != null) {
                    fileInputStream = new FileInputStream(modemCrashFileIfExists);
                    try {
                        String strTrim = new String(readInputStream(fileInputStream)).trim();
                        try {
                            fileInputStream.close();
                            return strTrim;
                        } catch (IOException e) {
                            log("can not get modem crash reason " + e);
                            return strTrim;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        log("getModemCrashByFeatureValue is error");
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                                log("can not get modem crash reason " + e3);
                            }
                        }
                        return null;
                    }
                }
                log("getModemCrashByFeatureValue file is null");
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e4) {
                        log("can not get modem crash reason " + e4);
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                fileInputStream2.close();
            }
            throw th;
        }
    }

    public static File getModemCrashFileIfExists() {
        File file = new File("/sdcard/ramdump");
        try {
            if (file.exists()) {
                new ArrayList();
                File[] fileArrListFiles = file.listFiles(new FileFilter() { // from class: com.android.phone.utils.ModemCrashUtils.1
                    @Override // java.io.FileFilter
                    public boolean accept(File file2) {
                        return file2.getName().contains("_dmesg.log");
                    }
                });
                if (fileArrListFiles != null && fileArrListFiles.length != 0) {
                    Arrays.sort(fileArrListFiles, new Comparator() { // from class: com.android.phone.utils.ModemCrashUtils.2
                        @Override // java.util.Comparator
                        public int compare(File file2, File file3) {
                            long jLastModified = file2.lastModified() - file3.lastModified();
                            if (jLastModified > 0) {
                                return 1;
                            }
                            return jLastModified == 0 ? 0 : -1;
                        }
                    });
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    long j = jCurrentTimeMillis - RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
                    long jLastModified = fileArrListFiles[fileArrListFiles.length - 1].lastModified();
                    log("fileName = " + fileArrListFiles[fileArrListFiles.length - 1].getName() + ", creatime = " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS").format(new Date(jLastModified)) + ", currentTime = " + jCurrentTimeMillis + ", beforeMinuteTime = " + j + ", fileUpdateTime = " + jLastModified);
                    if (jLastModified <= jCurrentTimeMillis && jLastModified >= j) {
                        return fileArrListFiles[fileArrListFiles.length - 1];
                    }
                    return null;
                }
                log("no documents meet");
                return null;
            }
        } catch (Exception e) {
            log("getModemCrashFileIfExists e = " + e);
        }
        return null;
    }

    public static byte[] readInputStream(FileInputStream fileInputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[512];
        int i = 0;
        while (true) {
            try {
                try {
                    int i2 = fileInputStream.read(bArr, 0, 512);
                    if (i2 <= 0) {
                        break;
                    }
                    i += i2;
                    if (i > 10485760) {
                        log("readInputStream exceeds max size limit: 10485760");
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        log("readInputStream " + e);
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                            throw th;
                        } catch (IOException e2) {
                            log("readInputStream close FileInputStream " + e2);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                log("readInputStream " + e3);
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e4) {
                    log("readInputStream " + e4);
                }
                if (fileInputStream == null) {
                    return null;
                }
                try {
                    fileInputStream.close();
                    return null;
                } catch (IOException e5) {
                    log("readInputStream close FileInputStream " + e5);
                    return null;
                }
            }
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e6) {
            log("readInputStream " + e6);
        }
        try {
            fileInputStream.close();
        } catch (IOException e7) {
            log("readInputStream close FileInputStream " + e7);
        }
        return byteArray;
    }

    private static void log(String str) {
        Log.d("ModemCrashUtils", str);
    }
}

package com.xiaomi.internal.telephony;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.xiaomi.modem.ModemUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class ModemDebugService extends Service {
    private static final String LOG_TAG = "MtkModemDebug_Service";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        log("onCreate");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        updateModemStatus(printWriter);
    }

    private void updateModemStatus(PrintWriter printWriter) {
        try {
            ModemUtils.dump(printWriter, "modem crash info");
            readFile(printWriter, "/data/ramdump");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(PrintWriter printWriter, String str) throws IOException {
        String[] list;
        File file = new File(str);
        if (!file.exists() || (list = file.list()) == null) {
            return;
        }
        for (String str2 : list) {
            File file2 = new File(str + "/" + str2);
            if (file2.getName().contains(".log")) {
                ModemUtils.dump(printWriter, file2.getName());
                ModemUtils.dump(printWriter, parseCrashInfo(file2));
            }
        }
    }

    private String parseCrashInfo(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    return sb.toString();
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
                e.printStackTrace();
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void log(String str) {
        ModemUtils.logd(LOG_TAG, str);
    }
}

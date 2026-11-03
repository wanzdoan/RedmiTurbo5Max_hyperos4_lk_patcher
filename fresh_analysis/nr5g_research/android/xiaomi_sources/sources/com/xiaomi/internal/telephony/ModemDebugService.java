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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        updateModemStatus(pw);
    }

    private void updateModemStatus(PrintWriter pw) {
        try {
            ModemUtils.dump(pw, "modem crash info");
            readFile(pw, "/data/ramdump");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(PrintWriter pw, String filepath) throws IOException {
        String[] filelist;
        File file = new File(filepath);
        if (file.exists() && (filelist = file.list()) != null) {
            for (String str : filelist) {
                File readfile = new File(filepath + "/" + str);
                if (readfile.getName().contains(".log")) {
                    ModemUtils.dump(pw, readfile.getName());
                    ModemUtils.dump(pw, parseCrashInfo(readfile));
                }
            }
        }
    }

    private String parseCrashInfo(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String s = br.readLine();
                    if (s == null) {
                        break;
                    }
                    result.append(s);
                    return result.toString();
                } catch (Throwable th) {
                    try {
                        br.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
                e.printStackTrace();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }
}

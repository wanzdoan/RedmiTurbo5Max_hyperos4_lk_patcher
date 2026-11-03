package com.xiaomi.onetrack.api;

import android.os.Process;
import android.util.Log;
import com.xiaomi.onetrack.CrashAnalysis;
import com.xiaomi.onetrack.f.a;
import com.xiaomi.onetrack.util.b;
import com.xiaomi.onetrack.util.j;
import com.xiaomi.onetrack.util.t;
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ap implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler b;
    private c i;
    private final Date g = new Date();
    private int j = 50;
    private int k = 50;
    private int l = 200;
    private boolean m = true;
    private boolean n = true;

    public ap(c cVar) {
        this.i = cVar;
    }

    public void a() {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof ap) {
            return;
        }
        this.b = defaultUncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        Log.d(com.xiaomi.onetrack.util.r.a("OneTrackExceptionHandler"), "crash happened->stacktrace: " + th.getStackTrace());
        FutureTask futureTask = new FutureTask(new aq(this, thread, th), null);
        j.a(futureTask);
        try {
            futureTask.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(com.xiaomi.onetrack.util.r.a("OneTrackExceptionHandler"), "handleException error :" + e.getMessage());
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.b;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:30:0x00c4 A[Catch: all -> 0x00cc, Exception -> 0x00d1, TryCatch #11 {Exception -> 0x00d1, all -> 0x00cc, blocks: (B:28:0x008e, B:30:0x00c4, B:35:0x00d5, B:37:0x00d9, B:39:0x00dd, B:42:0x00f0, B:44:0x00f4, B:45:0x00ff, B:47:0x0103, B:48:0x010e, B:50:0x012e, B:52:0x0134, B:41:0x00e1), top: B:90:0x008e }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00e1 A[Catch: all -> 0x00cc, Exception -> 0x00d1, TryCatch #11 {Exception -> 0x00d1, all -> 0x00cc, blocks: (B:28:0x008e, B:30:0x00c4, B:35:0x00d5, B:37:0x00d9, B:39:0x00dd, B:42:0x00f0, B:44:0x00f4, B:45:0x00ff, B:47:0x0103, B:48:0x010e, B:50:0x012e, B:52:0x0134, B:41:0x00e1), top: B:90:0x008e }] */
    /* JADX WARN: Code duplicated, block: B:44:0x00f4 A[Catch: all -> 0x00cc, Exception -> 0x00d1, TryCatch #11 {Exception -> 0x00d1, all -> 0x00cc, blocks: (B:28:0x008e, B:30:0x00c4, B:35:0x00d5, B:37:0x00d9, B:39:0x00dd, B:42:0x00f0, B:44:0x00f4, B:45:0x00ff, B:47:0x0103, B:48:0x010e, B:50:0x012e, B:52:0x0134, B:41:0x00e1), top: B:90:0x008e }] */
    /* JADX WARN: Code duplicated, block: B:47:0x0103 A[Catch: all -> 0x00cc, Exception -> 0x00d1, TryCatch #11 {Exception -> 0x00d1, all -> 0x00cc, blocks: (B:28:0x008e, B:30:0x00c4, B:35:0x00d5, B:37:0x00d9, B:39:0x00dd, B:42:0x00f0, B:44:0x00f4, B:45:0x00ff, B:47:0x0103, B:48:0x010e, B:50:0x012e, B:52:0x0134, B:41:0x00e1), top: B:90:0x008e }] */
    /* JADX WARN: Code duplicated, block: B:82:0x0087 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:94:? A[RETURN, SYNTHETIC] */
    public void a(Thread thread, Throwable th) throws Throwable {
        File file;
        Throwable th2;
        String string;
        String strA;
        Throwable th3;
        RandomAccessFile randomAccessFile;
        int i;
        JSONObject jSONObjectA;
        Date date = new Date();
        RandomAccessFile randomAccessFile2 = null;
        try {
            file = new File(String.format(Locale.US, "%s/%s_%020d_%s__%s%s", CrashAnalysis.getCrashPath(), "tombstone", Long.valueOf(this.g.getTime() * 1000), b.a(a.b()), a.e(), ".java.xcrash"));
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                e = e;
                com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler createLogFile failed", e);
            }
        } catch (Exception e2) {
            e = e2;
            file = null;
        }
        try {
            try {
                try {
                    StringWriter stringWriter = new StringWriter();
                    th2 = th;
                    try {
                        th2.printStackTrace(new PrintWriter(stringWriter));
                        string = stringWriter.toString();
                        try {
                            strA = a(date, thread, string);
                        } catch (Exception e3) {
                            e = e3;
                            com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler getEmergency failed", e);
                            strA = null;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        string = null;
                        com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler getEmergency failed", e);
                        strA = null;
                        if (file != null) {
                            return;
                        }
                        try {
                            randomAccessFile = new RandomAccessFile(file, "rws");
                            try {
                                randomAccessFile.write(("backtrace feature id:\n\t" + CrashAnalysis.calculateJavaDigest(string) + "\n\n").getBytes("UTF-8"));
                                randomAccessFile.write(("error reason:\n\t" + th2.toString() + "\n\n").getBytes("UTF-8"));
                                if (strA != null) {
                                    randomAccessFile.write(strA.getBytes("UTF-8"));
                                }
                                i = this.l;
                                if (i <= 0) {
                                    randomAccessFile.write(b.a(i, this.j, this.k).getBytes("UTF-8"));
                                } else {
                                    randomAccessFile.write(b.a(i, this.j, this.k).getBytes("UTF-8"));
                                }
                                if (this.m) {
                                    randomAccessFile.write(b.f().getBytes("UTF-8"));
                                }
                                if (this.n) {
                                    randomAccessFile.write(b.e().getBytes("UTF-8"));
                                }
                                randomAccessFile.write(b.d().getBytes("UTF-8"));
                                randomAccessFile.write("foreground:\nyes\n\n".getBytes("UTF-8"));
                                this.i.d();
                                jSONObjectA = t.a((Map) null, false);
                                if (jSONObjectA != null) {
                                    randomAccessFile.write(("dynamic common property:\n\t" + jSONObjectA + "\n\n").getBytes("UTF-8"));
                                }
                                randomAccessFile.close();
                            } catch (Exception e5) {
                                e = e5;
                                randomAccessFile2 = randomAccessFile;
                                com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler write log file failed", e);
                                if (randomAccessFile2 != null) {
                                    randomAccessFile2.close();
                                }
                            } catch (Throwable th4) {
                                th3 = th4;
                                randomAccessFile2 = randomAccessFile;
                                if (randomAccessFile2 != null) {
                                    try {
                                        randomAccessFile2.close();
                                        throw th3;
                                    } catch (Exception e6) {
                                        com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler close RandomAccessFile failed", e6);
                                        throw th3;
                                    }
                                }
                                throw th3;
                            }
                        } catch (Exception e7) {
                            e = e7;
                        }
                    }
                } catch (Exception e8) {
                    e = e8;
                    th2 = th;
                }
                if (file != null) {
                    return;
                }
                randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.write(("backtrace feature id:\n\t" + CrashAnalysis.calculateJavaDigest(string) + "\n\n").getBytes("UTF-8"));
                randomAccessFile.write(("error reason:\n\t" + th2.toString() + "\n\n").getBytes("UTF-8"));
                if (strA != null) {
                    randomAccessFile.write(strA.getBytes("UTF-8"));
                }
                i = this.l;
                if (i <= 0 || this.j > 0 || this.k > 0) {
                    randomAccessFile.write(b.a(i, this.j, this.k).getBytes("UTF-8"));
                }
                if (this.m) {
                    randomAccessFile.write(b.f().getBytes("UTF-8"));
                }
                if (this.n) {
                    randomAccessFile.write(b.e().getBytes("UTF-8"));
                }
                randomAccessFile.write(b.d().getBytes("UTF-8"));
                randomAccessFile.write("foreground:\nyes\n\n".getBytes("UTF-8"));
                this.i.d();
                jSONObjectA = t.a((Map) null, false);
                if (jSONObjectA != null && jSONObjectA.length() != 0) {
                    randomAccessFile.write(("dynamic common property:\n\t" + jSONObjectA + "\n\n").getBytes("UTF-8"));
                }
                randomAccessFile.close();
            } catch (Exception e9) {
                com.xiaomi.onetrack.util.r.b("OneTrackExceptionHandler", "JavaCrashHandler close RandomAccessFile failed", e9);
            }
        } catch (Throwable th5) {
            th3 = th5;
        }
    }

    private String a(Date date, Thread thread, String str) {
        return b.a(this.g, date, "java", a.e(), b.a(a.b())) + "pid: " + Process.myPid() + ", tid: " + Process.myTid() + ", name: " + thread.getName() + "  >>> " + a.e() + " <<<\n\njava stacktrace:\n" + str + "\n";
    }
}

package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import android.util.LruCache;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;

/* JADX INFO: loaded from: classes2.dex */
public abstract class m {
    private static LruCache d = new n(1048576);

    class a {
        String a;

        private a() {
        }

        /* synthetic */ a(n nVar) {
            this();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.xiaomi.onetrack.util.n] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    public static String a(String str) throws Throwable {
        ?? r0;
        BufferedReader bufferedReader;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        a aVar = (a) d.get(str);
        if (aVar != null) {
            return aVar.a;
        }
        ?? r1 = 0;
        r1 = 0;
        try {
            try {
                File file = new File(b(), str);
                StringBuilder sb = new StringBuilder();
                if (file.exists()) {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    while (true) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        } catch (Exception e) {
                            e = e;
                            r1 = bufferedReader;
                            r.c("FileUtil", "get error:" + e.toString());
                            o.a((Closeable) r1);
                            return "";
                        } catch (Throwable th) {
                            th = th;
                            r0 = bufferedReader;
                            o.a((Closeable) r0);
                            throw th;
                        }
                    }
                } else {
                    bufferedReader = null;
                }
                String string = sb.toString();
                a aVar2 = new a(r1);
                aVar2.a = string;
                d.put(str, aVar2);
                o.a(bufferedReader);
                return string;
            } catch (Throwable th2) {
                th = th2;
                r0 = r1;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private static String b() {
        return c("onetrack");
    }

    public static String a() {
        return c("tombstone");
    }

    private static String c(String str) {
        String str2 = com.xiaomi.onetrack.f.a.a().getFilesDir().getAbsolutePath() + File.separator + str;
        File file = new File(str2);
        if (!file.exists()) {
            try {
                file.mkdirs();
                return str2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str2;
    }

    public static String[] a(String str, int i) throws Throwable {
        String[] strArr = new String[2];
        BufferedReader bufferedReader = null;
        try {
            try {
                File file = new File(str);
                StringBuilder sb = new StringBuilder();
                if (file.exists()) {
                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                    boolean z = false;
                    while (true) {
                        try {
                            String line = bufferedReader2.readLine();
                            if (line == null) {
                                break;
                            }
                            if (!z) {
                                if ("dynamic common property:".equals(line)) {
                                    z = true;
                                }
                            } else {
                                strArr[0] = line;
                                z = false;
                            }
                            if (sb.length() <= i) {
                                sb.append(line);
                                sb.append("\n");
                            } else if (!TextUtils.isEmpty(strArr[0])) {
                                break;
                            }
                        } catch (Exception e) {
                            e = e;
                            bufferedReader = bufferedReader2;
                            r.c("FileUtil", "readCrashFile error:", e);
                            o.a(bufferedReader);
                            return strArr;
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            o.a(bufferedReader);
                            throw th;
                        }
                    }
                    bufferedReader = bufferedReader2;
                }
                if (sb.length() > i) {
                    strArr[1] = sb.substring(0, i - 1);
                } else {
                    strArr[1] = sb.toString();
                }
                o.a(bufferedReader);
                return strArr;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static void a(File file) {
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            r.c("FileUtil", "failed to remove file " + file.getName() + ", e :" + e.getMessage());
        }
    }
}

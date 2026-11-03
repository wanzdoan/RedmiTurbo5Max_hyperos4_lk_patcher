package com.ot.pubsub.util;

import android.text.TextUtils;
import android.util.LruCache;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/* JADX INFO: loaded from: classes.dex */
public class g {
    private static final String a = "FileUtil";
    private static final String b = "onetrack";
    private static final String c = "tombstone";
    private static LruCache d = new h(1048576);

    class a {
        String a;

        private a() {
        }

        /* synthetic */ a(h hVar) {
            this();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.ot.pubsub.util.h] */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v6 */
    public static void a(String str, String str2) throws Throwable {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        ?? r1 = 0;
        BufferedWriter bufferedWriter = null;
        try {
            try {
                a aVar = new a(r1);
                aVar.a = str2;
                d.put(str, aVar);
                String strB = b();
                File file = new File(strB);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(strB, str);
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file2), SmartNetworkRelayScore.TRAFFIC_UNIT);
                try {
                    bufferedWriter2.write(str2);
                    bufferedWriter2.flush();
                    i.a(bufferedWriter2);
                } catch (Exception e) {
                    r1 = bufferedWriter2;
                    e = e;
                    j.c(a, "put error:" + e.toString());
                    i.a((Closeable) r1);
                } catch (Throwable th) {
                    bufferedWriter = bufferedWriter2;
                    th = th;
                    i.a(bufferedWriter);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.ot.pubsub.util.h] */
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
                            j.c(a, "get error:" + e.toString());
                            i.a((Closeable) r1);
                            return "";
                        } catch (Throwable th) {
                            th = th;
                            r0 = bufferedReader;
                            i.a((Closeable) r0);
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
                i.a(bufferedReader);
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
        return c(b);
    }

    public static String a() {
        return c(c);
    }

    private static String c(String str) {
        String str2 = b.a().getFilesDir().getAbsolutePath() + File.separator + str;
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

    public static void b(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            d.remove(str);
            File file = new File(b(), str);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            j.c(a, "clear error:" + e.toString());
        }
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0031: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:12:0x0031 */
    public static String a(String str, int i) throws Throwable {
        BufferedReader bufferedReader;
        Closeable closeable;
        Closeable closeable2 = null;
        try {
            try {
                File file = new File(str);
                StringBuilder sb = new StringBuilder();
                if (file.exists()) {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    do {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                            sb.append("\n");
                        } catch (Exception e) {
                            e = e;
                            j.c(a, "get error:" + e.toString());
                            i.a(bufferedReader);
                            return null;
                        }
                    } while (sb.length() <= i);
                } else {
                    bufferedReader = null;
                }
                if (sb.length() > i) {
                    String strSubstring = sb.substring(0, i - 1);
                    i.a(bufferedReader);
                    return strSubstring;
                }
                String string = sb.toString();
                i.a(bufferedReader);
                return string;
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                i.a(closeable2);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
            i.a(closeable2);
            throw th;
        }
    }

    public static void a(File file) {
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            j.c(a, "failed to remove file: " + file.getName() + "," + e.toString());
        }
    }
}

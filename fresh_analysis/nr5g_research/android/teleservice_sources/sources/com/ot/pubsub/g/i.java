package com.ot.pubsub.g;

import com.ot.pubsub.util.v;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class i {
    public static final long a = 0;
    public static final long b = 1;
    public static final long c = 2;
    public static final String d = "projectid";
    public static final String e = "topic";
    public static final String f = "eventName";
    public static final String g = "data";
    public static final String h = "attributes";
    public static final String i = "needGzipAndEncrypt";
    private static final String j = "NetworkAccessManager";
    private static final String k = "ot_pubsub";
    private static String l = "ot_pubsub_netaccess_%s";
    private static SimpleDateFormat m = new SimpleDateFormat(v.g);
    private static volatile boolean n = true;
    private static volatile boolean o = false;
    private static boolean p = true;

    public static boolean a() {
        return p;
    }

    public static void a(boolean z) {
        p = z;
    }

    private static String d() {
        return com.ot.pubsub.util.b.a().getFilesDir().getAbsolutePath() + File.separator + k;
    }

    public static void a(String str, String str2, String str3, String str4, Map map, boolean z) {
        com.ot.pubsub.util.e.a(new j(str, str2, str3, str4, map, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void c(String str, String str2, String str3, String str4, Map map, boolean z) {
        FileWriter fileWriter;
        File file = new File(d(), String.format(l, m.format(new Date())));
        BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists()) {
                if (file.getParentFile().exists()) {
                    file.createNewFile();
                } else {
                    new File(file.getParentFile().getAbsolutePath()).mkdirs();
                    file.createNewFile();
                }
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("projectid", str);
            jSONObject.put("topic", str2);
            jSONObject.put(f, str3);
            jSONObject.put("data", str4);
            jSONObject.put(h, com.ot.pubsub.util.c.a(map));
            jSONObject.put(i, z);
            byte[] bArrA = c.a(jSONObject.toString());
            fileWriter = new FileWriter(file, true);
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter);
                    try {
                        bufferedWriter2.write(com.ot.pubsub.c.c.a(bArrA));
                        bufferedWriter2.newLine();
                        n = true;
                        com.ot.pubsub.util.i.a(bufferedWriter2);
                    } catch (Exception e2) {
                        e = e2;
                        bufferedWriter = bufferedWriter2;
                        com.ot.pubsub.util.j.b(j, "cta doSaveData error: " + e.toString());
                        e.printStackTrace();
                        com.ot.pubsub.util.i.a(bufferedWriter);
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        com.ot.pubsub.util.i.a(bufferedWriter);
                        com.ot.pubsub.util.i.a(fileWriter);
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e4) {
            e = e4;
            fileWriter = null;
        } catch (Throwable th3) {
            th = th3;
            fileWriter = null;
        }
        com.ot.pubsub.util.i.a(fileWriter);
    }

    public static synchronized void b(boolean z) {
        try {
            try {
                File file = new File(d());
                if (file.exists() && file.isDirectory()) {
                    String str = String.format(l, m.format(new Date()));
                    File[] fileArrListFiles = file.listFiles();
                    for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                        if (fileArrListFiles[i2].isFile() && (z || !fileArrListFiles[i2].getName().equalsIgnoreCase(str))) {
                            fileArrListFiles[i2].delete();
                        }
                    }
                    if (file.listFiles().length == 0) {
                        n = false;
                    }
                    return;
                }
                n = false;
            } catch (Exception e2) {
                com.ot.pubsub.util.j.b(j, "cta removeObsoleteEvent error: " + e2.toString());
                e2.printStackTrace();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.Closeable] */
    public static synchronized List b() {
        FileReader fileReader;
        Exception e2;
        BufferedReader bufferedReader;
        File file = new File(d(), String.format(l, m.format(new Date())));
        ?? r3 = 0;
        r3 = 0;
        if (!file.exists()) {
            return null;
        }
        try {
            List arrayList = new ArrayList();
            try {
                fileReader = new FileReader(file);
                try {
                    bufferedReader = new BufferedReader(fileReader);
                    while (true) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            arrayList.add(new JSONObject(c.a(com.ot.pubsub.c.c.a(line))));
                        } catch (Exception e3) {
                            e2 = e3;
                            com.ot.pubsub.util.j.b(j, "cta getCacheData error: " + e2.toString());
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e4) {
                    e2 = e4;
                    bufferedReader = null;
                } catch (Throwable th) {
                    th = th;
                    com.ot.pubsub.util.i.a((Closeable) r3);
                    com.ot.pubsub.util.i.a(fileReader);
                    throw th;
                }
            } catch (Exception e5) {
                fileReader = null;
                e2 = e5;
                bufferedReader = null;
            } catch (Throwable th2) {
                th = th2;
                fileReader = null;
            }
            com.ot.pubsub.util.i.a(bufferedReader);
            com.ot.pubsub.util.i.a(fileReader);
            if (arrayList.size() > 100) {
                arrayList = arrayList.subList(arrayList.size() - 100, arrayList.size());
            }
            if (arrayList.size() > 0) {
                n = true;
            }
            return arrayList;
        } catch (Throwable th3) {
            th = th3;
            r3 = file;
        }
    }

    public static boolean c() {
        return !o && n;
    }

    public static synchronized void a(com.ot.pubsub.a.c cVar) {
        if (!c()) {
            com.ot.pubsub.util.j.a(j, "analytics is publishing or no cache can be found，skip it!");
        } else if (!a()) {
            com.ot.pubsub.util.j.a(j, "network is unaccessable, cta");
        } else {
            o = true;
            com.ot.pubsub.util.e.a(new k(cVar));
        }
    }
}

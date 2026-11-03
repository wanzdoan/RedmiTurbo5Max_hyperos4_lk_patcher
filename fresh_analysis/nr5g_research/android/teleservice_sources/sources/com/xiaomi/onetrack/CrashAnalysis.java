package com.xiaomi.onetrack;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.onetrack.api.c;
import com.xiaomi.onetrack.d.d;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.b;
import com.xiaomi.onetrack.util.m;
import com.xiaomi.onetrack.util.r;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class CrashAnalysis {
    private static volatile CrashAnalysis s;
    private final FileProcessor[] q;
    private final c r;
    private boolean t;

    public static CrashAnalysis getInstance(Context context, c cVar) {
        if (s == null) {
            synchronized (CrashAnalysis.class) {
                try {
                    if (s == null) {
                        s = new CrashAnalysis(context, cVar);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return s;
    }

    private CrashAnalysis(Context context, c cVar) {
        try {
            Class[] clsArr = new Class[0];
            Object objNewInstance = Class.forName("xcrash.XCrash$InitParameters").getConstructor(null).newInstance(null);
            Boolean bool = Boolean.FALSE;
            a(objNewInstance, "setNativeDumpAllThreads", bool);
            a(objNewInstance, "setLogDir", getCrashPath());
            a(objNewInstance, "setNativeDumpMap", bool);
            a(objNewInstance, "setNativeDumpFds", bool);
            a(objNewInstance, "setJavaDumpAllThreads", bool);
            if (DeviceUtil.g()) {
                r.a("CrashAnalysis", "isMiTv:true");
                a(objNewInstance, "setAnrCheckProcessState", bool);
            } else {
                r.a("CrashAnalysis", "isMiTv:false");
            }
            Class.forName("xcrash.XCrash").getDeclaredMethod("init", Context.class, objNewInstance.getClass()).invoke(null, context.getApplicationContext(), objNewInstance);
            r.a("CrashAnalysis", "XCrash init success");
            this.t = true;
        } catch (Throwable th) {
            r.c("CrashAnalysis", "XCrash init failed: ", th);
        }
        this.r = cVar;
        this.q = new FileProcessor[]{new FileProcessor("java"), new FileProcessor("anr"), new FileProcessor("native")};
        a();
    }

    public boolean isSupport() {
        return this.t;
    }

    private void a() {
        try {
            if (!d()) {
                r.a("CrashAnalysis", "no crash file found");
            } else {
                e();
            }
        } catch (Throwable th) {
            r.b("CrashAnalysis", "processCrash error: ", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(String str, String str2) {
        int iIndexOf;
        int iIndexOf2;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str2.equals("anr")) {
                    int iIndexOf3 = str.indexOf(" tid=1 ");
                    if (iIndexOf3 != -1 && (iIndexOf = str.indexOf("\n  at ", iIndexOf3)) != -1 && (iIndexOf2 = str.indexOf(10, iIndexOf + 6)) != -1) {
                        return str.substring(iIndexOf + 2, iIndexOf2);
                    }
                    return "uncategoried";
                }
                int iIndexOf4 = str.indexOf("error reason:\n\t");
                if (iIndexOf4 != -1) {
                    int i = iIndexOf4 + 15;
                    int iIndexOf5 = str.indexOf("\n\n", i);
                    if (iIndexOf5 != -1) {
                        return str.substring(i, iIndexOf5);
                    }
                    return "uncategoried";
                }
                if (str2.equals("native")) {
                    String strB = b.b(str);
                    return !TextUtils.isEmpty(strB) ? strB : "uncategoried";
                }
                return "uncategoried";
            } catch (Exception e) {
                r.b("CrashAnalysis", "getErrorReasonString error: " + e.toString());
                return "uncategoried";
            }
        }
        return "uncategoried";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String d(String str, String str2) {
        int i;
        int iIndexOf;
        int iIndexOf2;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str2.equals("anr")) {
                    int iIndexOf3 = str.indexOf(" tid=1 ");
                    if (iIndexOf3 != -1 && (iIndexOf2 = str.indexOf("\n\n", iIndexOf3)) != -1) {
                        return calculateJavaDigest(str.substring(iIndexOf3, iIndexOf2));
                    }
                    return "";
                }
                int iIndexOf4 = str.indexOf("backtrace feature id:\n\t");
                if (iIndexOf4 != -1 && (iIndexOf = str.indexOf("\n\n", (i = iIndexOf4 + 23))) != -1) {
                    return str.substring(i, iIndexOf);
                }
                return "";
            } catch (Exception e) {
                r.b("CrashAnalysis", "calculateFeatureId error: " + e.toString());
                return "";
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map c(String str) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str.substring(1));
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, jSONObject.opt(next));
                }
            } catch (Exception e) {
                r.b("CrashAnalysis", "getDynamicCommonProperty error：", e);
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long d(String str) {
        int i;
        int iIndexOf;
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        try {
            int iIndexOf2 = str.indexOf("Crash time: '");
            if (iIndexOf2 == -1 || (iIndexOf = str.indexOf("'\n", (i = iIndexOf2 + 13))) == -1) {
                return 0L;
            }
            return b.a(str.substring(i, iIndexOf));
        } catch (Exception e) {
            r.b("CrashAnalysis", "getCrashTimeStamp error: " + e.toString());
            return 0L;
        }
    }

    private void a(Object obj, String str, Object obj2) throws IllegalAccessException, InvocationTargetException {
        obj.getClass().getDeclaredMethod(str, obj2.getClass() == Boolean.class ? Boolean.TYPE : obj2.getClass()).invoke(obj, obj2);
    }

    public static String getCrashPath() {
        return m.a();
    }

    private long b() {
        long jC = ac.c();
        if (jC == 0) {
            r.a("CrashAnalysis", "no ticket data found, return max count");
            return 10L;
        }
        long jB = ae.b();
        if (jC / 100 != jB) {
            r.a("CrashAnalysis", "no today's ticket, return max count");
            return 10L;
        }
        long j = jC - (jB * 100);
        r.a("CrashAnalysis", "today's remain ticket is " + j);
        return j;
    }

    private void a(long j) {
        ac.c((ae.b() * 100) + j);
    }

    private List c() {
        File[] fileArrListFiles = new File(getCrashPath()).listFiles();
        if (fileArrListFiles == null) {
            r.a("CrashAnalysis", "this path does not denote a directory, or if an I/O error occurs.");
            return null;
        }
        List listAsList = Arrays.asList(fileArrListFiles);
        Collections.sort(listAsList, new Comparator() { // from class: com.xiaomi.onetrack.CrashAnalysis.1
            @Override // java.util.Comparator
            public int compare(File file, File file2) {
                return (int) (file.lastModified() - file2.lastModified());
            }
        });
        int size = listAsList.size();
        if (size <= 20) {
            return listAsList;
        }
        int i = size - 20;
        for (int i2 = 0; i2 < i; i2++) {
            m.a((File) listAsList.get(i2));
        }
        return listAsList.subList(i, size);
    }

    private boolean d() {
        boolean z;
        Iterator it;
        List listC = c();
        long jB = b();
        if (listC == null || listC.size() <= 0) {
            z = false;
        } else {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long jB2 = ac.b();
            long j = v.a;
            if (jB2 > jCurrentTimeMillis) {
                jB2 = jCurrentTimeMillis - v.a;
            }
            Iterator it2 = listC.iterator();
            long j2 = 0;
            long j3 = 0;
            boolean z2 = false;
            while (it2.hasNext()) {
                File file = (File) it2.next();
                long jLastModified = file.lastModified();
                if (jLastModified < jCurrentTimeMillis - j || jLastModified > jCurrentTimeMillis) {
                    it = it2;
                    r.a("CrashAnalysis", "remove obsolete crash files: " + file.getName());
                    m.a(file);
                } else if (jLastModified <= jB2) {
                    r.a("CrashAnalysis", "found already reported crash file, ignore");
                    it = it2;
                } else if (jB > j2) {
                    FileProcessor[] fileProcessorArr = this.q;
                    int length = fileProcessorArr.length;
                    int i = 0;
                    while (true) {
                        it = it2;
                        if (i < length) {
                            if (fileProcessorArr[i].a(file)) {
                                r.a("CrashAnalysis", "find crash file:" + file.getName());
                                jB--;
                                z2 = true;
                                if (j3 < jLastModified) {
                                    j3 = jLastModified;
                                }
                            }
                            i++;
                            it2 = it;
                        }
                    }
                } else {
                    it = it2;
                }
                it2 = it;
                j2 = j2;
                j = v.a;
            }
            if (j3 > j2) {
                ac.b(j3);
            }
            z = z2;
        }
        if (z) {
            a(jB);
        }
        return z;
    }

    private void e() throws Throwable {
        for (FileProcessor fileProcessor : this.q) {
            fileProcessor.a();
        }
    }

    class FileProcessor {
        final List a = new ArrayList();
        final String b;
        final String c;

        FileProcessor(String str) {
            this.c = str;
            this.b = str + ".xcrash";
        }

        boolean a(File file) {
            if (!file.getName().contains(this.b)) {
                return false;
            }
            this.a.add(file);
            return true;
        }

        private String a(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            String[] strArrSplit = str.split("__");
            if (strArrSplit.length < 2) {
                return null;
            }
            String[] strArrSplit2 = strArrSplit[0].split("_");
            if (strArrSplit2.length == 3) {
                return strArrSplit2[2];
            }
            return null;
        }

        void a() throws Throwable {
            for (int i = 0; i < this.a.size(); i++) {
                File file = (File) this.a.get(i);
                String absolutePath = file.getAbsoluteFile().getAbsolutePath();
                String strA = a(file.getName());
                String[] strArrA = m.a(absolutePath, 358400);
                r.a("CrashAnalysis", "crash content size: " + strArrA[1].length());
                if (!TextUtils.isEmpty(strArrA[1]) && CrashAnalysis.this.r != null) {
                    String strD = CrashAnalysis.d(strArrA[1], this.c);
                    String strC = CrashAnalysis.c(strArrA[1], this.c);
                    long jD = CrashAnalysis.d(strArrA[1]);
                    Map mapC = CrashAnalysis.c(strArrA[0]);
                    r.a("CrashAnalysis", "fileName: " + absolutePath);
                    r.a("CrashAnalysis", "feature id: " + strD);
                    r.a("CrashAnalysis", "error: " + strC);
                    r.a("CrashAnalysis", "crashTimeStamp: " + jD);
                    StringBuilder sb = new StringBuilder("dynamicCommonProperty: ");
                    sb.append(mapC == null ? "" : mapC.toString());
                    r.a("CrashAnalysis", sb.toString());
                    CrashAnalysis.this.r.a(strArrA[1], strC, this.c, strA, strD, jD, mapC, false);
                    m.a(new File(absolutePath));
                    r.a("CrashAnalysis", "remove reported crash file");
                }
            }
        }
    }

    public static String calculateJavaDigest(String str) {
        String[] strArrSplit = str.replaceAll("\\t", "").split("\\n");
        StringBuilder sb = new StringBuilder();
        int iMin = Math.min(strArrSplit.length, 20);
        for (int i = 0; i < iMin; i++) {
            strArrSplit[i] = strArrSplit[i].replaceAll("((java:)|(length=)|(index=)|(Index:)|(Size:))\\d+", "$1XX").replaceAll("\\$[0-9a-fA-F]{1,10}@[0-9a-fA-F]{1,10}|@[0-9a-fA-F]{1,10}|0x[0-9a-fA-F]{1,10}", "XX").replaceAll("\\d+[B,KB,MB]*", "");
        }
        for (int i2 = 0; i2 < iMin && (!strArrSplit[i2].contains("...") || !strArrSplit[i2].contains("more")); i2++) {
            sb.append(strArrSplit[i2]);
            sb.append('\n');
        }
        return d.h(sb.toString());
    }
}

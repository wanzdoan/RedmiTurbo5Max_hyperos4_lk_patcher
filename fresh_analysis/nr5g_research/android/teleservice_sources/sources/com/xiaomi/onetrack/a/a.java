package com.xiaomi.onetrack.a;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private static String g = "AdMExceptionControl";
    private static volatile a h;
    public ConcurrentSkipListSet a;
    public ConcurrentSkipListSet b;
    public ConcurrentSkipListSet c;
    public ConcurrentSkipListSet d;
    public ConcurrentSkipListSet e;
    private long k;
    private long i = -1;
    private int j = 20;
    public volatile boolean f = false;

    public static a a() {
        if (h == null) {
            e();
        }
        return h;
    }

    private static void e() {
        if (h == null) {
            synchronized (a.class) {
                try {
                    if (h == null) {
                        h = new a();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private a() {
        this.k = 0L;
        String strE = ac.E();
        if (ab.b(strE)) {
            a(strE);
            this.k = ac.F();
        }
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject == null) {
            b();
        } else {
            a(jSONObject.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean f() {
        if (this.i != -1 && System.currentTimeMillis() < this.i) {
            return false;
        }
        r.a(g, "admPolicy is expiredTime :" + this.i);
        b();
        return true;
    }

    public void a(String str) {
        try {
            r.a(g, "parseAdmPolicy: " + str);
            if (ab.b(str)) {
                JSONObject jSONObject = new JSONObject(str);
                this.i = ae.a(jSONObject.optString("dl"));
                if (f()) {
                    return;
                }
                this.f = true;
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("p");
                JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("e");
                JSONArray jSONArrayOptJSONArray3 = jSONObject.optJSONArray("u");
                JSONArray jSONArrayOptJSONArray4 = jSONObject.optJSONArray("r");
                JSONArray jSONArrayOptJSONArray5 = jSONObject.optJSONArray("x");
                int iOptInt = jSONObject.optInt("c", 20);
                this.j = iOptInt;
                if (iOptInt > 100) {
                    this.j = 100;
                }
                this.a = a(this.a, jSONArrayOptJSONArray);
                this.b = a(this.b, jSONArrayOptJSONArray2);
                this.c = a(this.c, jSONArrayOptJSONArray3);
                this.d = a(this.d, jSONArrayOptJSONArray4);
                this.e = a(this.e, jSONArrayOptJSONArray5);
                ac.s(str);
            }
        } catch (Exception e) {
            r.b(g, "parseAdmPolicy exception: " + e);
        }
    }

    private ConcurrentSkipListSet a(ConcurrentSkipListSet concurrentSkipListSet, JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.length() != 0) {
            if (concurrentSkipListSet == null) {
                try {
                    concurrentSkipListSet = new ConcurrentSkipListSet();
                } catch (Exception e) {
                    r.b(g, "refreshMemorySet: ", e);
                }
            }
            concurrentSkipListSet.clear();
            for (int i = 0; i < jSONArray.length(); i++) {
                concurrentSkipListSet.add(jSONArray.getString(i));
            }
            return concurrentSkipListSet;
        }
        return null;
    }

    public void b() {
        ac.s("");
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = false;
    }

    public void a(com.xiaomi.onetrack.a.b.a aVar, int i, String str, Throwable th) {
        if (!this.f) {
            r.a(g, "OpenCaughtAdMException is false");
            return;
        }
        if (s.k()) {
            r.a(g, "trackAdMCaughtExceptionEvent isInternational return");
        } else if (aVar == null || !ab.b(aVar.c()) || !ab.b(aVar.j())) {
            r.a(g, "caught exception return , monitor or url is null");
        } else {
            com.xiaomi.onetrack.c.c.a(new b(this, aVar, i, th, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(com.xiaomi.onetrack.a.b.a aVar, int i, Throwable th) {
        ConcurrentSkipListSet concurrentSkipListSet = this.c;
        if (concurrentSkipListSet != null && !concurrentSkipListSet.isEmpty()) {
            boolean z = false;
            for (String str : this.c) {
                if (aVar.c().contains(str)) {
                    z = true;
                }
                r.a(g, "AdMUrl:" + aVar.c() + "，cloudUrl:" + str + "，isMatchInUrl:" + z);
            }
            if (!z) {
                r.a(g, "is filter ,caught exception url no match");
                return true;
            }
        }
        ConcurrentSkipListSet concurrentSkipListSet2 = this.d;
        if (concurrentSkipListSet2 != null && !concurrentSkipListSet2.isEmpty()) {
            boolean z2 = false;
            for (String str2 : this.d) {
                if (str2.contains("xx")) {
                    if (Pattern.matches(str2.replace("xx", "\\d{2}"), String.valueOf(i))) {
                        z2 = true;
                    }
                } else if (str2.equals(String.valueOf(i))) {
                    z2 = true;
                }
            }
            if (!z2) {
                r.a(g, "is filter ,caught exception code no match,code is " + i);
                return true;
            }
        }
        ConcurrentSkipListSet concurrentSkipListSet3 = this.e;
        if (concurrentSkipListSet3 != null && !concurrentSkipListSet3.isEmpty()) {
            if (th == null || TextUtils.isEmpty(th.getClass().getSimpleName())) {
                r.a(g, "is filter ,caught exception type is null");
                return true;
            }
            boolean z3 = false;
            for (String str3 : this.e) {
                if (str3.equals(th.getClass().getSimpleName())) {
                    z3 = true;
                }
                r.a(g, "AdMException:" + th.getClass().getName() + "，cloudException:" + str3 + "，isMatchExceptionType:" + z3);
            }
            if (!z3) {
                r.a(g, "is filter ,caught exception type no match");
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        if (this.k == 0) {
            r.a(g, "no caught data found, allowTodayCaught true");
            return true;
        }
        long jB = ae.b();
        long j = this.k;
        if (j / 100 != jB) {
            r.a(g, "today no caught , allowTodayCaught true");
            this.k = 0L;
            ac.l(0L);
            return true;
        }
        long j2 = j - (jB * 100);
        long j3 = this.j;
        int i = this.j;
        if (j2 < j3) {
            r.a(g, "today allow caught ,mDaysMaxCount :" + i + ", todayCount:" + j2);
            return true;
        }
        r.a(g, "today not allow caught ,mDaysMaxCount :" + i + ", todayCount:" + j2);
        return false;
    }

    public void c() {
        long j = this.k;
        if (j == 0) {
            this.k = (ae.b() * 100) + 1;
        } else {
            this.k = j + 1;
        }
        ac.l(this.k);
    }
}

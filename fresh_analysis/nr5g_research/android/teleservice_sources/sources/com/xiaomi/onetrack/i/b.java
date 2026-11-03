package com.xiaomi.onetrack.i;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.r;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b {
    private static long h;
    private static int i;
    private static int j;
    private static long k;
    private static int l;
    private static int m;
    private static volatile b n;
    private JSONObject o;

    private b() {
        this.o = null;
        try {
            String strC = ac.C();
            if (TextUtils.isEmpty(strC)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(strC);
            this.o = jSONObject;
            long jOptLong = jSONObject.optLong("nextRequestTime", 0L);
            k = jOptLong;
            h = jOptLong;
            int iOptInt = this.o.optInt("failedCount", 0);
            l = iOptInt;
            i = iOptInt;
            int iOptInt2 = this.o.optInt("invalidCount", 0);
            m = iOptInt2;
            j = iOptInt2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static b a() {
        if (n == null) {
            synchronized (b.class) {
                try {
                    if (n == null) {
                        n = new b();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return n;
    }

    public boolean b() {
        boolean z = true;
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j2 = h;
            if (jCurrentTimeMillis < j2 && j2 - jCurrentTimeMillis <= 1800000) {
                z = false;
                r.b("TokenChecker", "not allow update,  nextRequestTime：" + h);
                return false;
            }
            return true;
        } catch (Exception e) {
            r.b("TokenChecker", "checkAllowUpdate check error：", e);
            return z;
        }
    }

    public void c() {
        i++;
        h = System.currentTimeMillis() + (((long) a(i)) * 1000);
        r.a("TokenChecker", "addFiledCount nextRequestTime：" + h);
        g();
    }

    public void d() {
        if (j >= 2) {
            return;
        }
        i = 0;
        h = 0L;
        g();
    }

    public void e() {
        int i2 = j + 1;
        j = i2;
        if (i2 >= 2) {
            c();
        }
        g();
    }

    public void f() {
        i = 0;
        j = 0;
        h = 0L;
        g();
    }

    private void g() {
        try {
            if (this.o == null) {
                this.o = new JSONObject();
            }
            long j2 = h;
            if (j2 == k && i == l && j == m) {
                return;
            }
            this.o.put("nextRequestTime", j2);
            this.o.put("failedCount", i);
            this.o.put("invalidCount", j);
            ac.r(this.o.toString());
            k = h;
            l = i;
            m = j;
        } catch (Exception unused) {
        }
    }

    public static int a(int i2) {
        int i3;
        int iMin;
        int iNextInt = 0;
        if (i2 <= 0) {
            return 0;
        }
        int iMin2 = 1800;
        if (i2 >= 5) {
            iMin = 1200;
        } else {
            try {
                iMin = Math.min(1800, ((int) Math.pow(4.0d, i2 - 1)) * 10);
                try {
                    iMin2 = Math.min(1800, ((int) Math.pow(4.0d, i2)) * 10);
                } catch (Exception e) {
                    e = e;
                    i3 = 0;
                    r.a("TokenChecker", "getRetryInterval exception maxIntervalSec：" + iNextInt + "_minIntervalSec:" + iMin + "_randomIntervalSec:" + i3);
                    StringBuilder sb = new StringBuilder("getRetryInterval exception：");
                    sb.append(e);
                    r.a("TokenChecker", sb.toString());
                    return i3;
                }
            } catch (Exception e2) {
                e = e2;
                i3 = 0;
                iMin = 0;
            }
        }
        try {
            iNextInt = new Random().nextInt((iMin2 - iMin) + 1) + iMin;
            r.a("TokenChecker", "getRetryInterval failedCount：" + i2 + " ,randomIntervalSec:" + iNextInt);
            return iNextInt;
        } catch (Exception e3) {
            e = e3;
            i3 = iNextInt;
            iNextInt = iMin2;
            r.a("TokenChecker", "getRetryInterval exception maxIntervalSec：" + iNextInt + "_minIntervalSec:" + iMin + "_randomIntervalSec:" + i3);
            StringBuilder sb2 = new StringBuilder("getRetryInterval exception：");
            sb2.append(e);
            r.a("TokenChecker", sb2.toString());
            return i3;
        }
    }
}

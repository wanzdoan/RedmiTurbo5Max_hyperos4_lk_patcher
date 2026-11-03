package com.ot.pubsub.i.b;

import android.text.TextUtils;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.b.o;
import com.ot.pubsub.j.d;
import com.ot.pubsub.util.j;
import com.ot.pubsub.util.l;
import com.ot.pubsub.util.t;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static final String a = "";
    private static final String b = "b";
    private static volatile b c;
    private volatile boolean d = false;
    private ConcurrentHashMap e = new ConcurrentHashMap();

    public static b a() {
        if (c == null) {
            synchronized (b.class) {
                try {
                    if (c == null) {
                        c = new b();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return c;
    }

    private b() {
    }

    public String a(String str) {
        if (a((a) this.e.get(str))) {
            return ((a) this.e.get(str)).d;
        }
        a aVarC = c(str);
        if (a(aVarC)) {
            this.e.put(str, aVarC);
            return aVarC.d;
        }
        a aVarD = d(str);
        if (a(aVarD)) {
            this.e.put(str, aVarD);
            a(str, aVarD.g);
            b(aVarD);
            return aVarD.d;
        }
        return "";
    }

    private a c(String str) {
        try {
            String strB = t.b();
            if (TextUtils.isEmpty(strB)) {
                return null;
            }
            return a.a(new JSONObject(com.ot.pubsub.c.a.c(strB, com.ot.pubsub.c.a.a)).optString(str));
        } catch (Exception unused) {
            return null;
        }
    }

    private void a(String str, String str2) {
        try {
            String strB = t.b();
            if (!TextUtils.isEmpty(strB)) {
                String str3 = com.ot.pubsub.c.a.a;
                String strC = com.ot.pubsub.c.a.c(strB, str3);
                if (!TextUtils.isEmpty(strC)) {
                    JSONObject jSONObject = new JSONObject(strC);
                    jSONObject.put(str, str2);
                    t.a(com.ot.pubsub.c.a.a(jSONObject.toString(), str3));
                    return;
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(str, str2);
            t.a(com.ot.pubsub.c.a.a(jSONObject2.toString(), com.ot.pubsub.c.a.a));
        } catch (Exception e) {
            j.a(b, "putProjectIdTokenBySP Exception:" + e.getMessage());
        }
    }

    private a d(String str) {
        a aVarA = null;
        try {
            if (!this.d && !l.c(b)) {
                this.d = true;
                Configuration configurationA = o.a().a(str);
                if (configurationA != null) {
                    String privateKeyId = configurationA.getPrivateKeyId();
                    HashMap map = new HashMap();
                    map.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    aVarA = a.a(com.ot.pubsub.i.a.b.a(privateKeyId, com.ot.pubsub.g.l.a().b(), map));
                }
                return aVarA;
            }
            return null;
        } catch (Exception unused) {
            return null;
        } finally {
            this.d = false;
        }
    }

    public void b(String str) {
        try {
            this.e.put(str, new a());
            t.a("");
            a aVarD = d(str);
            if (a(aVarD)) {
                this.e.put(str, aVarD);
                a(str, aVarD.g);
                b(aVarD);
            }
        } catch (Exception e) {
            j.b(b, "forceRefreshToken Exception：" + e.getMessage());
        }
    }

    public void b() {
        try {
            this.e.clear();
            t.a("");
        } catch (Exception unused) {
        }
    }

    public boolean a(a aVar) {
        if (aVar == null) {
            return false;
        }
        try {
            if (TextUtils.isEmpty(aVar.d)) {
                return false;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j = aVar.f;
            if (jCurrentTimeMillis < j) {
                return false;
            }
            long j2 = ((jCurrentTimeMillis - j) / 1000) + 180;
            if (j2 < aVar.e) {
                return true;
            }
            j.a(b, "interval:" + j2 + "_expires:" + aVar.e + "_isValid:false");
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    private void b(a aVar) {
        d.a().a(false);
        d.a().a(0, false);
        d.a().a(1, false);
    }
}

package com.ot.pubsub.g;

import android.text.TextUtils;
import com.ot.pubsub.util.t;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class l {
    public static final String a = "CN";
    public static final String b = "RU";
    private static final String c = "RegionDomainManager";
    private static final String d = "INTL";
    private static final String e = "IN";
    private static final String f = "http://";
    private static final String g = "https://";
    private static String h = "";
    private static String i = "";
    private static String j = "";
    private static String k = "";
    private static String l = "";
    private static String m = "";
    private static String n = "";
    private static final String o = "/api/v1/token";
    private static final String p = "/track/key_get";
    private static final String q = "/api/v4/detail/config_common";
    private static final String r = "/api/v4/detail/config_p";
    private static final byte[] s = {116, 114, 97, 99, 107, 105, 110, 103, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] t = {116, 114, 97, 99, 107, 105, 110, 103, 46, 105, 110, 116, 108, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] u = {116, 114, 97, 99, 107, 105, 110, 103, 46, 114, 117, 115, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] v = {116, 114, 97, 99, 107, 105, 110, 103, 46, 105, 110, 100, 105, 97, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] w = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 97, 100, 46, 105, 110, 116, 108, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] x = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 97, 100, 46, 105, 110, 100, 105, 97, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] y = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 97, 100, 46, 114, 117, 115, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static ConcurrentHashMap z = new ConcurrentHashMap();

    private l() {
        f();
        z.put(e, n);
        z.put(b, m);
    }

    class a {
        private static final l a = new l();

        private a() {
        }
    }

    public static l a() {
        return a.a;
    }

    private void f() {
        k = a(s);
        l = a(t);
        m = a(u);
        n = a(v);
        h = a(w);
        i = a(x);
        j = a(y);
    }

    private String a(byte[] bArr) {
        try {
            return new String(bArr, "UTF-8");
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(c, e2.getMessage());
            return "";
        }
    }

    public String b() {
        return a(g(), h(), o);
    }

    public String a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public String c() {
        return a(g(), i(), p);
    }

    private String g() {
        return g;
    }

    private String h() {
        boolean zH = com.ot.pubsub.util.l.h();
        String strI = com.ot.pubsub.util.l.i();
        if (!zH) {
            return h;
        }
        if (TextUtils.equals(strI, e)) {
            return i;
        }
        if (TextUtils.equals(strI, b)) {
            return j;
        }
        return h;
    }

    private String i() {
        return a(com.ot.pubsub.util.l.h(), com.ot.pubsub.util.l.i());
    }

    private String a(boolean z2, String str) {
        if (!z2) {
            return k;
        }
        String str2 = (String) z.get(str);
        return TextUtils.isEmpty(str2) ? l : str2;
    }

    public String d() {
        return a(g(), h(), q);
    }

    public synchronized void a(JSONObject jSONObject) {
        try {
            com.ot.pubsub.util.j.a(c, "updateHostMap:" + jSONObject.toString());
            try {
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    String strOptString = jSONObject.optString(next);
                    if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(strOptString)) {
                        z.put(next, strOptString);
                    }
                }
                t.g(new JSONObject(z).toString());
            } catch (Exception e2) {
                com.ot.pubsub.util.j.a(c, "updateHostMap: " + e2.toString());
            }
            com.ot.pubsub.util.j.a(c, "merge config:" + new JSONObject(z).toString());
        } catch (Throwable th) {
            throw th;
        }
    }

    public String e() {
        return a(g(), h(), r);
    }
}

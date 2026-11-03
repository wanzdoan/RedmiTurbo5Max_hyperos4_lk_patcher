package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class z {
    private static String h = "";
    private static String i = "";
    private static String j = "";
    private static String k = "";
    private static String l = "";
    private static String m = "";
    private static String n = "";
    private static String o = "";
    private static String p = "";
    private static String q = "";
    private static final byte[] r = {116, 114, 97, 99, 107, 105, 110, 103, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] s = {116, 114, 97, 99, 107, 105, 110, 103, 46, 105, 110, 116, 108, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] t = {116, 114, 97, 99, 107, 105, 110, 103, 46, 114, 117, 115, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] u = {116, 114, 97, 99, 107, 105, 110, 103, 46, 105, 110, 100, 105, 97, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] v = {116, 114, 97, 99, 107, 105, 110, 103, 46, 101, 117, 46, 109, 105, 117, 105, 46, 99, 111, 109};
    private static final byte[] w = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] x = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 105, 110, 116, 108, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] y = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 105, 110, 100, 105, 97, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] z = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 114, 117, 115, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static final byte[] A = {115, 100, 107, 99, 111, 110, 102, 105, 103, 46, 101, 117, 46, 120, 105, 97, 111, 109, 105, 46, 99, 111, 109};
    private static ConcurrentHashMap I = new ConcurrentHashMap();

    private z() {
        h();
        I.put("IN", k);
        I.put(com.ot.pubsub.g.l.b, j);
        i();
    }

    private void h() {
        h = a(r);
        i = a(s);
        j = a(t);
        k = a(u);
        l = a(v);
        m = a(w);
        n = a(x);
        o = a(y);
        p = a(z);
        q = a(A);
    }

    public String a(byte[] bArr) {
        String str = "";
        try {
            String str2 = new String(bArr, "UTF-8");
            try {
                r.a("RegionDomainManager", "transmitToString host:".concat(str2));
                return str2;
            } catch (Exception e) {
                e = e;
                str = str2;
                r.b("RegionDomainManager", e.getMessage());
                return str;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    abstract class a {
        private static final z a = new z();
    }

    public static z a() {
        return a.a;
    }

    private void i() {
        try {
            String strE = ac.e();
            if (TextUtils.isEmpty(strE)) {
                return;
            }
            a(new JSONObject(strE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void a(JSONObject jSONObject) {
        try {
            r.a("RegionDomainManager", "updateHostMap:" + jSONObject.toString());
            try {
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    String strOptString = jSONObject.optString(next);
                    if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(strOptString)) {
                        I.put(next, strOptString);
                    }
                }
                ac.b(new JSONObject(I).toString());
            } catch (Exception e) {
                r.a("RegionDomainManager", "updateHostMap: " + e.toString());
            }
            r.a("RegionDomainManager", "merge config:" + new JSONObject(I).toString());
        } catch (Throwable th) {
            throw th;
        }
    }

    public String b() {
        try {
            if (TextUtils.isEmpty(ac.i())) {
                com.xiaomi.onetrack.b.g.b();
            }
        } catch (Exception e) {
            r.a("RegionDomainManager", "getTrackingUrl: " + e.toString());
        }
        return a(j(), k(), "/track/v4");
    }

    public String c() {
        return a(j(), l(), "/api/v4/detail/config");
    }

    public String d() {
        return a(j(), l(), "/api/v4/detail/config_common");
    }

    public String e() {
        return a(j(), l(), "/api/ex/lastUseful");
    }

    public String f() {
        return a(j(), k(), "/track/key_get");
    }

    public String a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    private String j() {
        return "https://";
    }

    private String k() {
        String strL = s.l();
        if (TextUtils.equals(strL, com.ot.pubsub.g.l.a)) {
            return h;
        }
        String str = (String) I.get(strL);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            if (com.xiaomi.onetrack.util.a.n.contains(strL.toUpperCase())) {
                return l;
            }
            return i;
        } catch (Exception e) {
            r.b("RegionDomainManager", "getTrackingHostname error: ", e);
            return i;
        }
    }

    public String g() {
        try {
            if (TextUtils.isEmpty(ac.i())) {
                com.xiaomi.onetrack.b.g.b();
            }
        } catch (Exception e) {
            r.a("RegionDomainManager", "getTrackingUrl: " + e.toString());
        }
        return a(j(), l(), "/api/v1/token");
    }

    private String l() {
        String strL = s.l();
        if (TextUtils.equals(strL, com.ot.pubsub.g.l.a)) {
            return m;
        }
        if (TextUtils.equals(strL, "IN")) {
            return o;
        }
        if (TextUtils.equals(strL, com.ot.pubsub.g.l.b)) {
            return p;
        }
        try {
            if (com.xiaomi.onetrack.util.a.n.contains(strL.toUpperCase())) {
                return q;
            }
            return n;
        } catch (Exception e) {
            r.b("RegionDomainManager", "getSdkConfigHost error: ", e);
            return n;
        }
    }
}

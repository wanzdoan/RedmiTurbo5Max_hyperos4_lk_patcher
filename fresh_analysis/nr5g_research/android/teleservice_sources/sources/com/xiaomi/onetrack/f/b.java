package com.xiaomi.onetrack.f;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.g.c;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.x;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b {
    private long b;
    private String c;
    private String d;
    private String e;
    private int f;
    private JSONObject g;
    private long h;

    /* JADX INFO: renamed from: com.xiaomi.onetrack.f.b$b, reason: collision with other inner class name */
    public abstract class AbstractC0020b {
        public static String A = "ot_test_env";
        public static String B = "ot_privacy_policy";
        public static String C = "market_name";
        public static String D = "ot_ad";
        public static String E = "ot_basic_mode";
        public static String F = "ot_ad_monitor";
        public static String H = "ot_mi_os";
        public static String I = "ot_device_type";
        public static String J = "ot_event_id";
        public static String K = "ot_url_count";
        public static String a = "event";
        public static String b = "oaid";
        public static String c = "gaid";
        public static String d = "android_id";
        public static String e = "instance_id";
        public static String f = "mfrs";
        public static String g = "model";
        public static String h = "platform";
        public static String i = "miui";
        public static String j = "build";
        public static String k = "os_ver";
        public static String l = "app_id";
        public static String m = "app_ver";
        public static String n = "pkg";
        public static String o = "channel";
        public static String p = "e_ts";
        public static String q = "tz";
        public static String r = "net";
        public static String s = "region";
        public static String t = "plugin_id";
        public static String u = "sdk_ver";
        public static String v = "uid";
        public static String w = "uid_type";
        public static String x = "sid";
        public static String y = "sdk_mode";
        public static String z = "ot_first_day";
    }

    public b() {
    }

    public b(b bVar) {
        this.b = bVar.b();
        this.c = bVar.c();
        this.d = bVar.d();
        this.e = bVar.e();
        this.f = bVar.f();
        this.g = new JSONObject(bVar.g().toString());
        this.h = bVar.h();
    }

    public long b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public void b(String str) {
        this.c = str;
    }

    public String d() {
        return this.d;
    }

    public void c(String str) {
        this.d = str;
    }

    public String e() {
        return this.e;
    }

    public void d(String str) {
        this.e = str;
    }

    public int f() {
        return this.f;
    }

    public void a(int i) {
        this.f = i;
    }

    public JSONObject g() {
        return this.g;
    }

    public void b(JSONObject jSONObject) {
        this.g = jSONObject;
    }

    public long h() {
        return this.h;
    }

    public void b(long j) {
        this.h = j;
    }

    public boolean i() {
        try {
            JSONObject jSONObject = this.g;
            return (jSONObject == null || !jSONObject.has(com.ot.pubsub.a.b.b) || !this.g.has(com.ot.pubsub.a.b.a) || TextUtils.isEmpty(this.c) || TextUtils.isEmpty(this.d)) ? false : true;
        } catch (Exception e) {
            r.b("Event", "check event isValid error, ", e);
            return false;
        }
    }

    public static JSONObject a(String str, Configuration configuration, x xVar, boolean z) {
        return a(str, configuration, "", xVar, z);
    }

    public static JSONObject a(String str, Configuration configuration, String str2, x xVar, boolean z) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Context contextB = a.b();
        jSONObject.put(AbstractC0020b.a, str);
        boolean zA = a(xVar.b());
        if (!zA) {
            if (s.b() ? s.k() : configuration.isInternational()) {
                if (xVar.b(str)) {
                    String strB = DeviceUtil.b(contextB);
                    if (!TextUtils.isEmpty(strB)) {
                        jSONObject.put(AbstractC0020b.c, strB);
                    }
                }
            } else {
                String strA = com.xiaomi.onetrack.util.oaid.a.a().a(contextB);
                jSONObject.put(AbstractC0020b.b, strA);
                if (!ab.b(strA)) {
                    jSONObject.put(AbstractC0020b.d, DeviceUtil.f(contextB));
                }
            }
            jSONObject.put(AbstractC0020b.e, q.a().b());
            a(jSONObject, configuration, str2);
            a(jSONObject, contextB);
            jSONObject.put(AbstractC0020b.x, s.h());
        }
        jSONObject.put(AbstractC0020b.f, DeviceUtil.d());
        jSONObject.put(AbstractC0020b.g, DeviceUtil.b());
        jSONObject.put(AbstractC0020b.h, "Android");
        jSONObject.put(AbstractC0020b.I, DeviceUtil.h());
        jSONObject.put(AbstractC0020b.i, s.e());
        jSONObject.put(AbstractC0020b.H, s.f());
        jSONObject.put(AbstractC0020b.j, s.d());
        jSONObject.put(AbstractC0020b.k, s.g());
        jSONObject.put(AbstractC0020b.m, a.c());
        jSONObject.put(AbstractC0020b.p, System.currentTimeMillis());
        jSONObject.put(AbstractC0020b.q, s.c());
        jSONObject.put(AbstractC0020b.r, c.a(contextB).toString());
        String strL = s.l();
        com.xiaomi.onetrack.b.a.a().d(strL);
        jSONObject.put(AbstractC0020b.s, strL);
        jSONObject.put(AbstractC0020b.u, "3.1.3");
        if (z) {
            jSONObject.put(AbstractC0020b.l, configuration.getAdEventAppId());
        } else {
            jSONObject.put(AbstractC0020b.l, configuration.getAppId());
        }
        jSONObject.put(AbstractC0020b.D, z);
        jSONObject.put(AbstractC0020b.n, a.e());
        jSONObject.put(AbstractC0020b.o, !TextUtils.isEmpty(configuration.getChannel()) ? configuration.getChannel() : "default");
        jSONObject.put(AbstractC0020b.y, (configuration.getMode() != null ? configuration.getMode() : OneTrack.Mode.APP).getType());
        jSONObject.put(AbstractC0020b.z, ae.b(ac.w()));
        if (r.c) {
            jSONObject.put(AbstractC0020b.A, true);
        }
        jSONObject.put(AbstractC0020b.B, xVar.a());
        jSONObject.put(AbstractC0020b.C, DeviceUtil.c());
        jSONObject.put(AbstractC0020b.E, zA);
        return jSONObject;
    }

    private static void a(JSONObject jSONObject, Context context) throws JSONException {
        String strP = ac.p();
        String strR = ac.r();
        if (TextUtils.isEmpty(strP) || TextUtils.isEmpty(strR)) {
            return;
        }
        jSONObject.put(AbstractC0020b.v, strP);
        jSONObject.put(AbstractC0020b.w, strR);
    }

    private static void a(JSONObject jSONObject, Configuration configuration, String str) throws JSONException {
        if (!TextUtils.isEmpty(str)) {
            jSONObject.put(AbstractC0020b.t, str);
        } else {
            jSONObject.put(AbstractC0020b.t, configuration.getPluginId());
        }
    }

    public static boolean a(boolean z) {
        if (OneTrack.getGlobalBasicModeEnable() == 0) {
            return z;
        }
        return OneTrack.getGlobalBasicModeEnable() != 1 && OneTrack.getGlobalBasicModeEnable() == 2;
    }
}

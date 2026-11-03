package com.ot.pubsub.h;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.BuildConfig;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubTrack;
import com.ot.pubsub.util.DeviceUtil;
import com.ot.pubsub.util.j;
import com.ot.pubsub.util.k;
import com.ot.pubsub.util.l;
import com.ot.pubsub.util.q;
import com.ot.pubsub.util.t;
import com.ot.pubsub.util.v;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {
    protected static final int a = 1;
    public static final int b = 0;
    public static final int c = 1;
    public static final int d = 2;
    private static final String e = "BaseMessage";
    private String f;
    private long g;
    private String h;
    private String i;
    private String j;
    private int k;
    private JSONObject l;
    private long m;

    public a() {
    }

    public long a() {
        return this.g;
    }

    public void a(long j) {
        this.g = j;
    }

    public String b() {
        return this.h;
    }

    public void a(String str) {
        this.h = str;
    }

    public String c() {
        return this.i;
    }

    public void b(String str) {
        this.i = str;
    }

    public String d() {
        return this.j;
    }

    public void c(String str) {
        this.j = str;
    }

    public int e() {
        return this.k;
    }

    public void a(int i) {
        this.k = i;
    }

    public JSONObject f() {
        return this.l;
    }

    public void a(JSONObject jSONObject) {
        this.l = jSONObject;
    }

    public long g() {
        return this.m;
    }

    public void b(long j) {
        this.m = j;
    }

    public String h() {
        return this.f;
    }

    public void d(String str) {
        this.f = str;
    }

    public boolean i() {
        try {
            JSONObject jSONObject = this.l;
            return (jSONObject == null || !jSONObject.has(com.ot.pubsub.a.b.b) || !this.l.has(com.ot.pubsub.a.b.a) || TextUtils.isEmpty(this.h) || TextUtils.isEmpty(this.i)) ? false : true;
        } catch (Exception e2) {
            j.b(e, "check event isValid error, ", e2);
            return false;
        }
    }

    private a(C0017a c0017a) {
        this.g = c0017a.a;
        this.h = c0017a.b;
        this.i = c0017a.c;
        this.j = c0017a.d;
        this.k = c0017a.e;
        this.l = c0017a.f;
        this.m = c0017a.g;
    }

    /* JADX INFO: renamed from: com.ot.pubsub.h.a$a, reason: collision with other inner class name */
    public class C0017a {
        private long a;
        private String b;
        private String c;
        private String d;
        private int e;
        private JSONObject f;
        private long g;

        public C0017a a(long j) {
            this.a = this.a;
            return this;
        }

        public C0017a a(String str) {
            this.b = str;
            return this;
        }

        public C0017a b(String str) {
            this.c = str;
            return this;
        }

        public C0017a c(String str) {
            this.d = str;
            return this;
        }

        public C0017a a(int i) {
            this.e = i;
            return this;
        }

        public C0017a a(JSONObject jSONObject) {
            this.f = jSONObject;
            return this;
        }

        public C0017a b(long j) {
            this.g = j;
            return this;
        }

        public a a() {
            return new a(this);
        }
    }

    class b {
        static final String a = "protocol_ver";

        b() {
        }
    }

    class c {
        public static String A = "ot_privacy_policy";
        public static String B = "market_name";
        public static String C = "ot_hybrid_type";
        public static String a = "event";
        public static String b = "oaid";
        public static String c = "gaid";
        public static String d = "instance_id";
        public static String e = "mfrs";
        public static String f = "model";
        public static String g = "platform";
        public static String h = "miui";
        public static String i = "build";
        public static String j = "os_ver";
        public static String k = "app_id";
        public static String l = "app_ver";
        public static String m = "pkg";
        public static String n = "channel";
        public static String o = "e_ts";
        public static String p = "tz";
        public static String q = "net";
        public static String r = "region";
        public static String s = "plugin_id";
        public static String t = "sdk_ver";
        public static String u = "uid";
        public static String v = "uid_type";
        public static String w = "sid";
        public static String x = "sdk_mode";
        public static String y = "ot_first_day";
        public static String z = "ot_test_env";

        c() {
        }
    }

    public static JSONObject a(String str, Configuration configuration, PubSubTrack.IEventHook iEventHook, q qVar) throws JSONException {
        return a(str, configuration, iEventHook, "", qVar);
    }

    public static JSONObject a(String str, Configuration configuration, PubSubTrack.IEventHook iEventHook, String str2, q qVar) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Context contextB = com.ot.pubsub.util.b.b();
        jSONObject.put(c.a, str);
        if (iEventHook != null && iEventHook.isRecommendEvent(str)) {
            String strA = DeviceUtil.a(contextB);
            if (!TextUtils.isEmpty(strA)) {
                jSONObject.put(c.c, strA);
            }
        }
        jSONObject.put(c.d, com.ot.pubsub.g.b.a().b());
        jSONObject.put(c.e, DeviceUtil.c());
        jSONObject.put(c.f, DeviceUtil.a());
        jSONObject.put(c.g, "Android");
        jSONObject.put(c.h, l.d());
        jSONObject.put(c.i, l.c());
        jSONObject.put(c.j, l.e());
        jSONObject.put(c.l, com.ot.pubsub.util.b.c());
        jSONObject.put(c.o, System.currentTimeMillis());
        jSONObject.put(c.p, l.b());
        jSONObject.put(c.q, k.b(contextB).toString());
        String strI = l.i();
        com.ot.pubsub.b.a.a().d(strI);
        jSONObject.put(c.r, strI);
        jSONObject.put(c.t, BuildConfig.SDK_VERSION);
        jSONObject.put(c.k, configuration.getAppId());
        jSONObject.put(c.m, com.ot.pubsub.util.b.e());
        jSONObject.put(c.n, !TextUtils.isEmpty(configuration.getChannel()) ? configuration.getChannel() : "default");
        jSONObject.put(c.w, l.f());
        jSONObject.put(c.x, "sdk");
        jSONObject.put(c.y, v.d(t.g()));
        if (j.c) {
            jSONObject.put(c.z, true);
        }
        jSONObject.put(c.A, qVar.a());
        jSONObject.put(c.B, DeviceUtil.b());
        return jSONObject;
    }
}

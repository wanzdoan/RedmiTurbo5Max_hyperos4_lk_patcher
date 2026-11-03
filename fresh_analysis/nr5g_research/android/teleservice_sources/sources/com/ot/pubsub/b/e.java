package com.ot.pubsub.b;

import android.text.TextUtils;
import com.ot.pubsub.BuildConfig;
import com.ot.pubsub.util.DeviceUtil;
import com.ot.pubsub.util.t;
import com.ot.pubsub.util.v;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class e {
    public static final String a = "l";
    private static final String b = "CommonConfigUpdater";
    private static final String c = "t";
    private static final String d = "levels";
    private static final String e = "Android";
    private static ConcurrentHashMap f = new ConcurrentHashMap();
    private static final long g = 172800000;

    public static void a() {
        com.ot.pubsub.util.e.a(new f());
    }

    public static void b() {
        if (d()) {
            e();
        } else {
            com.ot.pubsub.util.j.a(b, "CommonConfigUpdater Does not meet prerequisites for request");
        }
    }

    private static boolean d() {
        if (!com.ot.pubsub.util.k.a()) {
            com.ot.pubsub.util.j.b(b, "net is not connected!");
            return false;
        }
        if (TextUtils.isEmpty(t.h())) {
            return true;
        }
        long jI = t.i();
        return jI < System.currentTimeMillis() || jI - System.currentTimeMillis() > g;
    }

    private static void e() {
        if (com.ot.pubsub.util.l.c(b)) {
            return;
        }
        HashMap map = new HashMap();
        try {
            map.put(m.b, com.ot.pubsub.util.l.d());
            map.put(m.c, com.ot.pubsub.util.l.c());
            map.put(m.d, com.ot.pubsub.util.l.h() ? "1" : "0");
            map.put(m.e, BuildConfig.SDK_VERSION);
            map.put(m.m, com.ot.pubsub.util.b.c());
            map.put(m.f, com.ot.pubsub.util.l.e());
            map.put(m.g, DeviceUtil.a());
            map.put(m.h, com.ot.pubsub.util.l.i());
            map.put(m.l, e);
            String strD = com.ot.pubsub.g.l.a().d();
            String strA = com.ot.pubsub.i.a.b.a(strD, (Map) map, true);
            com.ot.pubsub.util.j.a(b, "url:" + strD + " response:" + strA);
            a(strA);
        } catch (IOException e2) {
            com.ot.pubsub.util.j.a(b, "requestCloudData: " + e2.toString());
        }
    }

    private static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt(com.ot.pubsub.i.a.a.d) == 0) {
                String strOptString = jSONObject.optString(com.ot.pubsub.i.a.a.e);
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
                if (jSONObjectOptJSONObject != null) {
                    JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("regionUrl");
                    if (jSONObjectOptJSONObject2 != null) {
                        com.ot.pubsub.g.l.a().a(jSONObjectOptJSONObject2);
                    }
                    t.f(jSONObjectOptJSONObject.toString());
                    t.h(strOptString);
                }
                t.d(System.currentTimeMillis() + v.b + ((long) new Random().nextInt(86400000)));
            }
        } catch (JSONException e2) {
            com.ot.pubsub.util.j.a(b, "saveCommonCloudData: " + e2.toString());
        }
    }

    public static Map c() {
        try {
            if (!f.isEmpty()) {
                return f;
            }
            String strH = t.h();
            if (!TextUtils.isEmpty(strH)) {
                JSONArray jSONArrayOptJSONArray = new JSONObject(strH).optJSONArray(d);
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArrayOptJSONArray.getJSONObject(i);
                    int iOptInt = jSONObject.optInt(a);
                    int iOptInt2 = jSONObject.optInt(c);
                    if (iOptInt > 0 && iOptInt2 > 0) {
                        f.put(Integer.valueOf(iOptInt), Integer.valueOf(iOptInt2));
                    }
                }
            }
            return f.isEmpty() ? f() : f;
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(b, "getLevelIntervalConfig: " + e2.toString());
        }
    }

    private static HashMap f() {
        HashMap map = new HashMap();
        map.put(1, 1000);
        map.put(2, 15000);
        map.put(3, 900000);
        return map;
    }
}

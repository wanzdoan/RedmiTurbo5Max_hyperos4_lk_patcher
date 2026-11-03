package com.xiaomi.onetrack.b;

import android.text.TextUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.z;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class g {
    private static ConcurrentHashMap f = new ConcurrentHashMap();

    public static void b() {
        if (e()) {
            f();
        } else {
            r.a("CommonConfigUpdater", "CommonConfigUpdater Does not meet prerequisites for request");
        }
    }

    private static boolean e() {
        if (!com.xiaomi.onetrack.g.c.a()) {
            r.b("CommonConfigUpdater", "net is not connected!");
            return false;
        }
        if (TextUtils.isEmpty(ac.i())) {
            return true;
        }
        long jG = ac.g();
        return jG < System.currentTimeMillis() || jG - System.currentTimeMillis() > 172800000;
    }

    private static void f() {
        if (s.a("CommonConfigUpdater")) {
            return;
        }
        HashMap map = new HashMap();
        try {
            if (OneTrack.getGlobalBasicModeEnable() != 2) {
                String strA = com.xiaomi.onetrack.util.oaid.a.a().a(com.xiaomi.onetrack.f.a.b());
                if (ab.b(strA)) {
                    strA = com.xiaomi.onetrack.d.d.c(strA);
                }
                map.put(com.ot.pubsub.b.m.a, strA);
            }
            map.put(com.ot.pubsub.b.m.b, s.e());
            map.put(com.ot.pubsub.b.m.c, s.d());
            map.put(com.ot.pubsub.b.m.d, s.k() ? "1" : "0");
            map.put(com.ot.pubsub.b.m.e, "3.1.3");
            map.put(com.ot.pubsub.b.m.m, com.xiaomi.onetrack.f.a.c());
            map.put(com.ot.pubsub.b.m.f, s.g());
            map.put(com.ot.pubsub.b.m.g, DeviceUtil.b());
            map.put(com.ot.pubsub.b.m.h, s.l());
            map.put(com.ot.pubsub.b.m.l, "Android");
            String strD = z.a().d();
            String strB = com.xiaomi.onetrack.g.b.b(strD, map, true);
            r.a("CommonConfigUpdater", "url:" + strD + " response:" + strB);
            a(strB);
        } catch (IOException e) {
            r.a("CommonConfigUpdater", "requestCloudData: " + e.toString());
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
                        z.a().a(jSONObjectOptJSONObject2);
                    }
                    ac.d(jSONObjectOptJSONObject.toString());
                    ac.c(strOptString);
                }
                ac.e(System.currentTimeMillis() + v.b + ((long) new Random().nextInt(86400000)));
            }
        } catch (JSONException e) {
            r.a("CommonConfigUpdater", "saveCommonCloudData: " + e.toString());
        }
    }

    public static Map c() {
        try {
            if (!f.isEmpty()) {
                return f;
            }
            String strI = ac.i();
            if (!TextUtils.isEmpty(strI)) {
                JSONArray jSONArrayOptJSONArray = new JSONObject(strI).optJSONArray("levels");
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArrayOptJSONArray.getJSONObject(i);
                    int iOptInt = jSONObject.optInt(com.ot.pubsub.b.e.a);
                    int iOptInt2 = jSONObject.optInt("t");
                    if (iOptInt > 0 && iOptInt2 > 0) {
                        f.put(Integer.valueOf(iOptInt), Integer.valueOf(iOptInt2));
                    }
                }
            }
            return f.isEmpty() ? g() : f;
        } catch (Exception e) {
            r.a("CommonConfigUpdater", "getLevelIntervalConfig: " + e.toString());
        }
    }

    private static HashMap g() {
        HashMap map = new HashMap();
        map.put(1, 1000);
        map.put(2, 15000);
        map.put(3, 900000);
        return map;
    }
}

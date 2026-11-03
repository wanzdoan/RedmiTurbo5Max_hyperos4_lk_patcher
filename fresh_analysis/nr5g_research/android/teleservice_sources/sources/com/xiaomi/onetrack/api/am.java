package com.xiaomi.onetrack.api;

import android.content.Context;
import com.ot.pubsub.a.a;
import com.ot.pubsub.a.b;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.t;
import com.xiaomi.onetrack.util.x;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class am {
    public static String a(String str, JSONObject jSONObject, Configuration configuration, JSONObject jSONObject2, x xVar) throws JSONException {
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put(b.b, com.xiaomi.onetrack.f.b.a(str, configuration, xVar, false));
        jSONObject3.put(b.a, t.a(jSONObject, jSONObject2));
        return jSONObject3.toString();
    }

    public static String a(String str, String str2, Configuration configuration, JSONObject jSONObject, boolean z, x xVar) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(b.b, com.xiaomi.onetrack.f.b.a(str2, configuration, xVar, false));
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put(a.r, str);
        jSONObject3.put("type", 1);
        jSONObject3.put(a.t, z);
        jSONObject2.put(b.a, t.a(jSONObject3, jSONObject));
        return jSONObject2.toString();
    }

    public static String a(String str, String str2, long j, Configuration configuration, JSONObject jSONObject, x xVar) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(b.b, com.xiaomi.onetrack.f.b.a(str2, configuration, xVar, false));
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put(a.r, str);
        jSONObject3.put("type", 2);
        jSONObject3.put("duration", j);
        jSONObject2.put(b.a, t.a(jSONObject3, jSONObject));
        return jSONObject2.toString();
    }

    public static String a(String str, String str2, String str3, String str4, String str5, long j, Configuration configuration, JSONObject jSONObject, x xVar, boolean z) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObjectA = com.xiaomi.onetrack.f.b.a(a.f, configuration, xVar, false);
        if (str5 != null) {
            jSONObjectA.put(com.xiaomi.onetrack.f.b.AbstractC0020b.m, str5);
        }
        jSONObject2.put(b.b, jSONObjectA);
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("exception", str);
        jSONObject3.put("type", str3);
        jSONObject3.put(a.m, str2);
        jSONObject3.put(a.n, str4);
        jSONObject3.put(a.o, j);
        if (z) {
            jSONObject3.put("custom", 1);
        }
        jSONObject2.put(b.a, t.a(jSONObject3, jSONObject));
        return jSONObject2.toString();
    }

    public static String a(Configuration configuration, JSONObject jSONObject, x xVar) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(b.b, com.xiaomi.onetrack.f.b.a("onetrack_dau", configuration, xVar, false));
        JSONObject jSONObject3 = new JSONObject();
        Context contextB = com.xiaomi.onetrack.f.a.b();
        boolean zN = com.xiaomi.onetrack.util.ac.n();
        if (zN) {
            com.xiaomi.onetrack.util.ac.c(false);
        }
        jSONObject3.put(a.x, zN);
        if (!(s.b() ? s.k() : configuration.isInternational())) {
            if (configuration.isIMEIEnable()) {
                jSONObject3.put(a.y, DeviceUtil.c(contextB));
            }
            if (configuration.isIMSIEnable()) {
                jSONObject3.put(a.z, DeviceUtil.d(contextB));
            }
        }
        jSONObject3.put(a.C, ak.a(configuration));
        jSONObject2.put(b.a, t.a(jSONObject3, jSONObject));
        return jSONObject2.toString();
    }

    public static String a(long j, String str, long j2, long j3, Configuration configuration, x xVar) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(b.b, com.xiaomi.onetrack.f.b.a(a.j, configuration, xVar, false));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(a.X, j);
        jSONObject2.put(a.Y, str);
        jSONObject2.put(a.Z, j2);
        jSONObject2.put(a.aa, j3);
        jSONObject.put(b.a, jSONObject2);
        return jSONObject.toString();
    }

    public static String a(com.xiaomi.onetrack.h.c.a aVar, Configuration configuration, x xVar) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(b.b, com.xiaomi.onetrack.f.b.a("ot_monitor", configuration, xVar, false));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("ot_date", aVar.b());
        jSONObject2.put("ot_type", "sdk");
        JSONObject jSONObject3 = new JSONObject();
        HashMap mapC = aVar.c();
        if (mapC != null) {
            jSONObject3 = a(mapC);
        }
        jSONObject2.put("ot_all", jSONObject3.toString());
        JSONArray jSONArray = new JSONArray();
        HashMap mapD = aVar.d();
        if (mapD != null && mapD.size() > 0) {
            for (Map.Entry entry : mapD.entrySet()) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("en", entry.getKey());
                jSONObject4.put("cnt", entry.getValue());
                jSONArray.put(jSONObject4);
            }
        }
        jSONObject2.put("ot_events", jSONArray);
        jSONObject.put(b.a, jSONObject2);
        return jSONObject.toString();
    }

    private static JSONObject a(HashMap map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        HashMap map2 = (HashMap) map.get("tr");
        if (map2 != null) {
            jSONObject.put("tr", map2.get(""));
        } else {
            jSONObject.put("tr", 0);
        }
        HashMap map3 = (HashMap) map.get("ch");
        JSONObject jSONObject2 = new JSONObject();
        if (map3 == null) {
            jSONObject2.put("r1", 0);
            jSONObject2.put("r2", 0);
            jSONObject2.put("r3", 0);
            jSONObject2.put("r4", 0);
        } else {
            jSONObject2.put("r1", map3.get("r1") == null ? 0 : ((Integer) map3.get("r1")).intValue());
            jSONObject2.put("r2", map3.get("r2") == null ? 0 : ((Integer) map3.get("r2")).intValue());
            jSONObject2.put("r3", map3.get("r3") == null ? 0 : ((Integer) map3.get("r3")).intValue());
            jSONObject2.put("r4", map3.get("r4") == null ? 0 : ((Integer) map3.get("r4")).intValue());
        }
        jSONObject.put("ch", jSONObject2);
        HashMap map4 = (HashMap) map.get("bu");
        if (map4 != null) {
            jSONObject.put("bu", map4.get(""));
        } else {
            jSONObject.put("bu", 0);
        }
        HashMap map5 = (HashMap) map.get("se");
        JSONObject jSONObject3 = new JSONObject();
        if (map5 == null) {
            jSONObject3.put("r1", 0);
            jSONObject3.put("r2", 0);
        } else {
            jSONObject3.put("r1", map5.get("r1") == null ? 0 : ((Integer) map5.get("r1")).intValue());
            jSONObject3.put("r2", map5.get("r2") == null ? 0 : ((Integer) map5.get("r2")).intValue());
        }
        jSONObject.put("se", jSONObject3);
        if (map.get("sa") != null) {
            HashMap map6 = (HashMap) map.get("sa");
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("su", map6.get("su") == null ? 0 : ((Integer) map6.get("su")).intValue());
            jSONObject4.put("r1", map6.get("r1") == null ? 0 : ((Integer) map6.get("r1")).intValue());
            jSONObject4.put("r2", map6.get("r2") == null ? 0 : ((Integer) map6.get("r2")).intValue());
            jSONObject4.put("r3", map6.get("r3") == null ? 0 : ((Integer) map6.get("r3")).intValue());
            jSONObject4.put("r4", map6.get("r4") == null ? 0 : ((Integer) map6.get("r4")).intValue());
            jSONObject4.put("r5", map6.get("r5") != null ? ((Integer) map6.get("r5")).intValue() : 0);
            jSONObject.put("sa", jSONObject4);
        }
        if (map.get("up") != null) {
            jSONObject.put("up", ((HashMap) map.get("up")).get(""));
        }
        if (map.get("do") != null) {
            jSONObject.put("do", ((HashMap) map.get("do")).get(""));
        }
        return jSONObject;
    }

    public static String a(String str, String str2, String str3, Configuration configuration, x xVar) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(b.b, com.xiaomi.onetrack.f.b.a("ot_failed", configuration, xVar, false));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("ot_f_e", str);
        jSONObject2.put("ot_f_t", "sdk");
        jSONObject2.put("ot_f_s", str2);
        jSONObject2.put("ot_f_r", str3);
        jSONObject.put(b.a, jSONObject2);
        return jSONObject.toString();
    }

    public static com.xiaomi.onetrack.f.b a(com.xiaomi.onetrack.f.b bVar, String str, String str2) throws JSONException {
        com.xiaomi.onetrack.f.b bVar2 = new com.xiaomi.onetrack.f.b(bVar);
        bVar2.d("ot_failed");
        JSONObject jSONObjectG = bVar2.g();
        jSONObjectG.getJSONObject(b.b).put(com.xiaomi.onetrack.f.b.AbstractC0020b.a, "ot_failed");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("ot_f_e", bVar.e());
        jSONObject.put("ot_f_t", "sdk");
        jSONObject.put("ot_f_s", str);
        jSONObject.put("ot_f_r", str2);
        jSONObjectG.put(b.a, jSONObject);
        return bVar2;
    }
}

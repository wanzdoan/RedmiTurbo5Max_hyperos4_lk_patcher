package com.xiaomi.onetrack.b;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.z;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class e {
    private static ConcurrentHashMap b = new ConcurrentHashMap();
    private static final AtomicBoolean r = new AtomicBoolean(false);
    private static final AtomicLong s = new AtomicLong(-1);

    public static void a() {
        AtomicBoolean atomicBoolean = r;
        try {
            if (atomicBoolean.compareAndSet(false, true)) {
                try {
                    if (c()) {
                        d();
                    } else {
                        r.a("CloudControlClawConfigUpdater", "CloudControlClawConfigUpdater Does not meet prerequisites for request");
                    }
                    atomicBoolean.set(false);
                    return;
                } catch (Exception e) {
                    r.a("CloudControlClawConfigUpdater", "CloudControlClawConfigUpdater pullDataSync err :" + e.getMessage());
                    r.set(false);
                    return;
                }
            }
            r.a("CloudControlClawConfigUpdater", "Another thread is already requesting data.");
        } catch (Throwable th) {
            r.set(false);
            throw th;
        }
    }

    private static boolean c() {
        if (s.k()) {
            r.a("CloudControlClawConfigUpdater", "isInternationalBuild , return false");
            return false;
        }
        if (!com.xiaomi.onetrack.g.c.a()) {
            r.b("CloudControlClawConfigUpdater", "net is not connected!");
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        AtomicLong atomicLong = s;
        if (atomicLong.get() == -1) {
            atomicLong.set(ac.D());
        }
        long j = atomicLong.get();
        r.a("CloudControlClawConfigUpdater", "canRequest nextUpdateTime=" + j);
        return j < jCurrentTimeMillis || j - jCurrentTimeMillis > 345600000;
    }

    private static void d() {
        StringBuilder sb;
        if (s.a("CloudControlClawConfigUpdater")) {
            return;
        }
        HashMap map = new HashMap();
        try {
            Context contextA = com.xiaomi.onetrack.f.a.a();
            map.put("cv", com.xiaomi.onetrack.f.a.c());
            map.put("p", com.xiaomi.onetrack.f.a.e());
            if (OneTrack.getGlobalBasicModeEnable() != 2) {
                String strA = com.xiaomi.onetrack.util.oaid.a.a().a(contextA);
                if (ab.b(strA)) {
                    map.put("i", com.xiaomi.onetrack.d.d.c(strA));
                }
            }
            String strL = s.l();
            map.put("r", strL);
            map.put("m", DeviceUtil.b());
            map.put("d", s.g());
            map.put("v", s.e());
            map.put("cr", s.c(strL));
            String strE = z.a().e();
            String strA2 = com.xiaomi.onetrack.g.b.a(strE, (Map) map, true);
            r.a("CloudControlClawConfigUpdater", "url:" + strE + " response:" + strA2);
            a(strA2);
            try {
                long jCurrentTimeMillis = System.currentTimeMillis() + v.b + ((long) (Math.random() * 2.592E8d));
                s.set(jCurrentTimeMillis);
                ac.k(jCurrentTimeMillis);
            } catch (Exception e) {
                e = e;
                sb = new StringBuilder("nextUpdateTime error: ");
                sb.append(e.toString());
                String string = sb.toString();
            }
        } catch (Exception e2) {
            r.c("CloudControlClawConfigUpdater", "requestCloudData error: " + e2.toString());
            try {
                long jCurrentTimeMillis2 = System.currentTimeMillis() + v.b + ((long) (Math.random() * 2.592E8d));
                s.set(jCurrentTimeMillis2);
                ac.k(jCurrentTimeMillis2);
            } catch (Exception e3) {
                e = e3;
                sb = new StringBuilder("nextUpdateTime error: ");
                sb.append(e.toString());
                String string2 = sb.toString();
            }
        } finally {
            try {
                long jCurrentTimeMillis3 = System.currentTimeMillis() + v.b + ((long) (Math.random() * 2.592E8d));
                s.set(jCurrentTimeMillis3);
                ac.k(jCurrentTimeMillis3);
            } catch (Exception e4) {
                r.c("CloudControlClawConfigUpdater", "nextUpdateTime error: " + e4.toString());
            }
        }
    }

    private static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt(com.ot.pubsub.i.a.a.d) == 0) {
                String strOptString = jSONObject.optString(com.ot.pubsub.a.a.L);
                if (TextUtils.isEmpty(strOptString) || TextUtils.equals("{}", strOptString)) {
                    com.xiaomi.onetrack.a.a.a().b();
                }
                try {
                    if (!ab.b(strOptString) || s.k()) {
                        return;
                    }
                    JSONObject jSONObject2 = new JSONObject(strOptString);
                    String strOptString2 = jSONObject2.optString("dl");
                    JSONObject jSONObjectOptJSONObject = jSONObject2.optJSONObject("adm");
                    if (jSONObjectOptJSONObject != null) {
                        jSONObjectOptJSONObject.put("dl", strOptString2);
                    }
                    com.xiaomi.onetrack.a.a.a().a(jSONObjectOptJSONObject);
                    return;
                } catch (Exception e) {
                    r.c("CloudControlClawConfigUpdater", "opt net cloud e: " + e.getMessage());
                    return;
                }
                r.c("CloudControlClawConfigUpdater", "saveCloudData error: " + e.toString());
            }
        } catch (JSONException e2) {
            r.c("CloudControlClawConfigUpdater", "saveCloudData error: " + e2.toString());
        }
    }
}

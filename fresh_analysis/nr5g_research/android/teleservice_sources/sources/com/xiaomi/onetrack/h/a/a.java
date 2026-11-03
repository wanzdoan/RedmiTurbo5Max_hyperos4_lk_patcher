package com.xiaomi.onetrack.h.a;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.ot.pubsub.j.d;
import com.xiaomi.onetrack.b.j;
import com.xiaomi.onetrack.util.ac;
import com.xiaomi.onetrack.util.ae;
import com.xiaomi.onetrack.util.o;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.t;
import com.xiaomi.onetrack.util.x;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private static a r;
    private HashMap n = new HashMap();
    private HashMap o = new HashMap();
    private HashMap p = new HashMap();
    private HashMap q = new HashMap();

    public static a a() {
        if (r == null) {
            synchronized (a.class) {
                try {
                    if (r == null) {
                        r = new a();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return r;
    }

    public void a(String str, x xVar, boolean z) {
        JSONObject jSONObjectG;
        this.n.put(str, Boolean.valueOf(z));
        this.o.put(str, xVar);
        try {
            if (((Boolean) this.n.get(str)).booleanValue()) {
                String strO = ac.o(str);
                jSONObjectG = !TextUtils.isEmpty(strO) ? new JSONObject(strO) : null;
            } else {
                jSONObjectG = j.a().g(str);
            }
            if (jSONObjectG == null || jSONObjectG.length() <= 0) {
                return;
            }
            a(str, jSONObjectG);
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "loadLocalMonitorConfig error: " + e);
        }
    }

    public void b(String str, x xVar, boolean z) {
        this.n.put(str, Boolean.valueOf(z));
        this.o.put(str, xVar);
        a(str);
    }

    public void a(String str) {
        JSONObject jSONObjectG;
        try {
            if (((Boolean) this.n.get(str)).booleanValue()) {
                String strO = ac.o(str);
                jSONObjectG = !TextUtils.isEmpty(strO) ? new JSONObject(strO) : null;
                if (b(str) && a(jSONObjectG)) {
                    String strC = c(str);
                    r.a("OTMonitorConfigManager", "checkUpdateMonitorConfig, remoteConfig: " + strC + ", cachedConfig: " + strO);
                    if (!TextUtils.isEmpty(strC) && !strC.equals(strO)) {
                        jSONObjectG = new JSONObject(strC);
                        jSONObjectG.put("last_update_time", System.currentTimeMillis());
                        ac.a(str, jSONObjectG.toString());
                    }
                    d(str);
                }
            } else {
                jSONObjectG = j.a().g(str);
            }
            if (jSONObjectG == null || jSONObjectG.length() <= 0) {
                return;
            }
            a(str, jSONObjectG);
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "checkUpdateMonitorConfig error: " + e);
        }
    }

    private boolean a(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.length() != 0) {
            try {
                if (System.currentTimeMillis() - jSONObject.getLong("last_update_time") < 1800000) {
                    return false;
                }
            } catch (Exception e) {
                r.b("OTMonitorConfigManager", "shouldGetRemoteMonitorConfig error: " + e);
            }
        }
        return true;
    }

    private boolean b(String str) {
        long jN = ac.n(str);
        if (ae.b() == jN / 100) {
            return ((int) (jN % 100)) <= 10;
        }
        ac.a(str, ae.b() * 100);
        return true;
    }

    private synchronized String c(String str) {
        Cursor cursorQuery;
        String string;
        Cursor cursor = null;
        string = null;
        string = null;
        string = null;
        try {
            try {
                Uri.Builder builderBuildUpon = Uri.parse("content://com.miui.analytics.OneTrackProvider/monitorConfig").buildUpon();
                builderBuildUpon.appendQueryParameter(d.b, str);
                builderBuildUpon.appendQueryParameter("pkg", com.xiaomi.onetrack.f.a.e());
                builderBuildUpon.appendQueryParameter("sign", com.xiaomi.onetrack.d.a.a("monitorConfig" + com.xiaomi.onetrack.f.a.e() + str));
                cursorQuery = com.xiaomi.onetrack.f.a.a().getContentResolver().query(builderBuildUpon.build(), null, null, null, null);
                if (cursorQuery != null) {
                    try {
                        try {
                            if (cursorQuery.moveToFirst()) {
                                string = cursorQuery.getString(0);
                            }
                        } catch (Exception e) {
                            e = e;
                            r.b("OTMonitorConfigManager", "getRemoteMonitorConfig error", e);
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = cursorQuery;
                        o.b(cursor);
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                throw th2;
            }
        } catch (Exception e2) {
            e = e2;
            cursorQuery = null;
        } catch (Throwable th3) {
            th = th3;
            o.b(cursor);
            throw th;
        }
        o.b(cursorQuery);
        return string;
    }

    private void d(String str) {
        long jN = ac.n(str);
        if (ae.b() == jN / 100) {
            ac.a(str, jN + 1);
        } else {
            ac.a(str, ae.b() * 100);
        }
    }

    public void a(String str, JSONObject jSONObject) {
        boolean z;
        try {
            long jA = ae.a(jSONObject.optString("dl"));
            boolean z2 = true;
            if (jA == -1) {
                z = jSONObject.optInt("monitor", 0) == 1;
                if (jSONObject.optInt("failed", 0) != 1) {
                    z2 = false;
                }
            } else {
                boolean z3 = jA >= System.currentTimeMillis() && jSONObject.optInt("monitor", 0) == 1;
                if (jA < System.currentTimeMillis() || jSONObject.optInt("failed", 0) != 1) {
                    z2 = false;
                }
                z = z3;
            }
            this.p.put(str, Boolean.valueOf(z));
            this.q.put(str, Boolean.valueOf(z2));
            r.a("OTMonitorConfigManager", "updateCachedMonitorConfig, shouldRecordAppStats: " + z + ", shouldRecordEventFailed: " + z2);
            if (z2) {
                return;
            }
            ac.q(str);
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "checkUpdateMonitorConfig error" + e.getMessage());
        }
    }

    public boolean a(String str, String str2) {
        if (t.b(str2)) {
            r.a("OTMonitorConfigManager", "shouldRecordStats, not record ot_quality event");
            return false;
        }
        if (this.o.get(str) != null && ((x) this.o.get(str)).a("ot_monitor")) {
            return this.p.get(str) != null && ((Boolean) this.p.get(str)).booleanValue();
        }
        r.a("OTMonitorConfigManager", "shouldRecordStats, privacy not permitted");
        return false;
    }

    public boolean b(String str, String str2) {
        if (t.b(str2)) {
            r.a("OTMonitorConfigManager", "shouldRecordEventFailed, not record ot_quality event");
            return false;
        }
        if (this.o.get(str) == null || !((x) this.o.get(str)).a("ot_failed")) {
            r.a("OTMonitorConfigManager", "shouldRecordEventFailed, privacy not permitted");
            return false;
        }
        if (this.q.get(str) == null || !((Boolean) this.q.get(str)).booleanValue()) {
            r.a("OTMonitorConfigManager", "shouldRecordEventFailed, quality config not permitted");
            return false;
        }
        if (d(str, str2)) {
            return true;
        }
        r.a("OTMonitorConfigManager", "shouldRecordEventFailed, no ticket");
        return false;
    }

    private boolean d(String str, String str2) {
        String strP = ac.p(str);
        if (TextUtils.isEmpty(strP)) {
            e(str);
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(strP);
            if (ae.b() == jSONObject.optLong("reportTime")) {
                return jSONObject.optInt("total") > 0 && jSONObject.optJSONObject(com.ot.pubsub.b.a.c).optInt(str2, 100) > 0;
            }
            e(str);
            return true;
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "hasReportOTFailedTicket error: ", e);
            return false;
        }
    }

    private void e(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("reportTime", ae.b());
            jSONObject.put("total", 1000);
            jSONObject.put(com.ot.pubsub.b.a.c, new JSONObject());
            ac.b(str, jSONObject.toString());
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "setDefaultReportOTFailedTicket error: ", e);
        }
    }

    public void c(String str, String str2) {
        try {
            String strP = ac.p(str);
            if (TextUtils.isEmpty(strP)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(strP);
            if (jSONObject.optLong("reportTime") != ae.b()) {
                return;
            }
            int iOptInt = jSONObject.optInt("total");
            jSONObject.put("total", iOptInt > 0 ? iOptInt - 1 : 0);
            int iOptInt2 = jSONObject.optJSONObject(com.ot.pubsub.b.a.c).optInt(str2, -1);
            if (iOptInt2 == -1) {
                jSONObject.getJSONObject(com.ot.pubsub.b.a.c).put(str2, 99);
            } else {
                jSONObject.getJSONObject(com.ot.pubsub.b.a.c).put(str2, iOptInt2 > 0 ? iOptInt2 - 1 : 0);
            }
            ac.b(str, jSONObject.toString());
        } catch (Exception e) {
            r.b("OTMonitorConfigManager", "updateReportOTFailedTicket error: ", e);
        }
    }
}

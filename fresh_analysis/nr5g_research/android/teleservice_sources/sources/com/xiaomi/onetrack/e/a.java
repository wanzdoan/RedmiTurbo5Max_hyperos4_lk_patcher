package com.xiaomi.onetrack.e;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a extends com.xiaomi.onetrack.f.b {
    public a(com.xiaomi.onetrack.a.b.a aVar, int i, String str, Throwable th) {
        b("001");
        c(com.xiaomi.onetrack.f.a.e());
        a(2);
        d("onetrack_exception");
        b(b(aVar, i, str, th));
        b(System.currentTimeMillis());
    }

    private JSONObject b(com.xiaomi.onetrack.a.b.a aVar, int i, String str, Throwable th) {
        try {
            JSONObject jSONObjectA = a(aVar);
            JSONObject jSONObjectA2 = a(aVar, i, str, th);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(com.ot.pubsub.a.b.b, jSONObjectA);
            jSONObject.put(com.ot.pubsub.a.b.a, jSONObjectA2);
            return jSONObject;
        } catch (JSONException e) {
            r.b("CaughtAdMExceptionEvent", "create caught exception event error", e);
            return null;
        }
    }

    protected JSONObject a(com.xiaomi.onetrack.a.b.a aVar) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Context contextA = com.xiaomi.onetrack.f.a.a();
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.a, e());
        if (r.a) {
            if (s.k()) {
                jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.c, DeviceUtil.b(contextA));
            } else {
                jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.b, p.a(contextA));
            }
        }
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.e, q.a().b());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.f, DeviceUtil.d());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.g, DeviceUtil.b());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.h, "Android");
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.i, s.e());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.H, s.f());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.j, s.d());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.k, s.g());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.m, com.xiaomi.onetrack.f.a.c());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.p, System.currentTimeMillis());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.q, s.c());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.r, com.xiaomi.onetrack.g.c.a(contextA).toString());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.s, s.l());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.l, "001");
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.n, com.xiaomi.onetrack.f.a.e());
        jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.C, DeviceUtil.c());
        if (aVar != null) {
            jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.J, aVar.j());
            jSONObject.put(com.xiaomi.onetrack.f.b.AbstractC0020b.K, aVar.h());
        }
        return jSONObject;
    }

    public JSONObject a(com.xiaomi.onetrack.a.b.a aVar, int i, String str, Throwable th) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "adm");
            if (aVar != null) {
                jSONObject.put("ad_event", aVar.a());
                jSONObject.put("ad_pkg", aVar.f());
                jSONObject.put("e_host", a(aVar.c()));
                jSONObject.put("url_index", aVar.i());
            }
            jSONObject.put(com.ot.pubsub.a.a.M, 0);
            jSONObject.put("err_code", i);
            jSONObject.put("err_body", str);
            if (th != null) {
                try {
                    StringWriter stringWriter = new StringWriter();
                    th.printStackTrace(new PrintWriter(stringWriter));
                    jSONObject.put("exception", stringWriter.toString());
                } catch (Throwable unused) {
                    r.b("CaughtAdMExceptionEvent", "printStackTrace error");
                }
                jSONObject.put(com.ot.pubsub.a.a.m, th.getMessage());
            }
            return jSONObject;
        } catch (JSONException e) {
            r.b("CaughtAdMExceptionEvent", "createBusinessData error", e);
            return null;
        }
    }

    public String a(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            return new URL(str).getHost();
        } catch (Exception e) {
            r.b("CaughtAdMExceptionEvent", "getHost error", e);
            return "";
        }
    }
}

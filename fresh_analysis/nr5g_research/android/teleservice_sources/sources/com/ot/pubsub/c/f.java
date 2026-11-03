package com.ot.pubsub.c;

import android.content.Context;
import android.text.TextUtils;
import com.ot.pubsub.util.j;
import com.ot.pubsub.util.l;
import com.ot.pubsub.util.t;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class f {
    public static final JSONObject a = new JSONObject();
    private static final String b = "SecretKeyManager";
    private static final String c = "secretKey";
    private static final String d = "sid";
    private static final String e = "key";
    private Context f;
    private JSONObject g;
    private String[] h;

    private f() {
        this.g = null;
        this.h = new String[2];
        this.f = com.ot.pubsub.util.b.a();
    }

    final class a {
        private static final f a = new f();

        private a() {
        }
    }

    public static f a() {
        return a.a;
    }

    public synchronized String[] b() {
        try {
            JSONObject jSONObjectE = e();
            this.h[0] = jSONObjectE != null ? jSONObjectE.optString(e) : "";
            this.h[1] = jSONObjectE != null ? jSONObjectE.optString(d) : "";
            d();
        } catch (Throwable th) {
            throw th;
        }
        return this.h;
    }

    private void d() {
        if (j.a) {
            if (!TextUtils.isEmpty(this.h[0]) && !TextUtils.isEmpty(this.h[1])) {
                j.a(b, "key  and sid is valid! ");
            } else {
                j.a(b, "key or sid is invalid!");
            }
        }
    }

    public JSONObject c() {
        try {
            if (l.c(b)) {
                return a;
            }
            byte[] bArrA = com.ot.pubsub.c.a.a();
            String strA = c.a(e.a(bArrA));
            HashMap map = new HashMap();
            map.put(c, strA);
            String strA2 = com.ot.pubsub.i.a.b.a(com.ot.pubsub.g.l.a().c(), (Map) map, true);
            if (!TextUtils.isEmpty(strA2)) {
                JSONObject jSONObject = new JSONObject(strA2);
                int iOptInt = jSONObject.optInt(com.ot.pubsub.i.a.a.d);
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
                if (iOptInt == 0 && jSONObjectOptJSONObject != null) {
                    String strOptString = jSONObjectOptJSONObject.optString(e);
                    String strOptString2 = jSONObjectOptJSONObject.optString(d);
                    if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                        String strA3 = c.a(com.ot.pubsub.c.a.b(c.a(strOptString), bArrA));
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put(e, strA3);
                        jSONObject2.put(d, strOptString2);
                        this.g = jSONObject2;
                        t.c(b.a(this.f, jSONObject2.toString()));
                        t.b(System.currentTimeMillis());
                    }
                }
            }
            return this.g;
        } catch (Exception e2) {
            j.b(b, "requestSecretData: " + e2.toString());
        }
    }

    private JSONObject e() {
        JSONObject jSONObjectF = this.g;
        if (jSONObjectF == null && (jSONObjectF = f()) != null) {
            this.g = jSONObjectF;
        }
        return jSONObjectF == null ? c() : jSONObjectF;
    }

    private JSONObject f() {
        try {
            String strD = t.d();
            if (TextUtils.isEmpty(strD)) {
                return null;
            }
            return new JSONObject(b.b(this.f, strD));
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }
}

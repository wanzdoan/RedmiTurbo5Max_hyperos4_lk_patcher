package com.ot.pubsub.b;

import android.text.TextUtils;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.util.t;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class p implements Runnable {
    final /* synthetic */ Configuration a;
    final /* synthetic */ String b;
    final /* synthetic */ o c;

    p(o oVar, Configuration configuration, String str) {
        this.c = oVar;
        this.a = configuration;
        this.b = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            String strC = t.c();
            if (!TextUtils.isEmpty(strC)) {
                String str = com.ot.pubsub.c.a.a;
                String strC2 = com.ot.pubsub.c.a.c(strC, str);
                if (!TextUtils.isEmpty(strC2)) {
                    JSONObject jSONObject = new JSONObject(strC2);
                    jSONObject.put(this.b, this.c.b(this.a));
                    t.b(com.ot.pubsub.c.a.a(jSONObject.toString(), str));
                    return;
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(this.b, this.c.b(this.a));
            t.b(com.ot.pubsub.c.a.a(jSONObject2.toString(), com.ot.pubsub.c.a.a));
        } catch (Throwable th) {
            com.ot.pubsub.util.j.a("ConfigEntityManager", "putProjectIdConfigBySP Throwable:" + th.getMessage());
        }
    }
}

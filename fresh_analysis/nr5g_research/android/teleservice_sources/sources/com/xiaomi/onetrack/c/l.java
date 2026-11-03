package com.xiaomi.onetrack.c;

import com.xiaomi.onetrack.api.ao;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class l implements Runnable {
    final /* synthetic */ ao a;

    l(ao aoVar) {
        this.a = aoVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            List<JSONObject> listC = j.c();
            if (listC != null && listC.size() > 0) {
                com.xiaomi.onetrack.util.r.a("NetworkAccessManager", "cache file has " + listC.size() + " events, start to upload");
                for (JSONObject jSONObject : listC) {
                    this.a.a(jSONObject.optString(com.ot.pubsub.g.i.f), jSONObject.optString("data"));
                }
            }
            j.c(true);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("NetworkAccessManager", "cta event error: " + e.getMessage());
        }
        boolean unused = j.k = false;
    }
}

package com.xiaomi.onetrack.api;

import com.ot.pubsub.a.a;
import com.xiaomi.onetrack.h.d.b;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class q implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ long b;
    final /* synthetic */ c c;

    q(c cVar, String str, long j) {
        this.c = cVar;
        this.a = str;
        this.b = j;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            if (this.c.i()) {
                return;
            }
            if (this.c.e.isAutoTrackActivityAction()) {
                b.a(this.c.e.getAppId(), a.g, "tr", "", 1);
                JSONObject jSONObjectG = this.c.g(a.g);
                String str = this.a;
                long j = this.b;
                c cVar = this.c;
                com.xiaomi.onetrack.util.ac.h(am.a(str, a.g, j, cVar.e, jSONObjectG, cVar.g));
                return;
            }
            com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "config.autoTrackActivityAction is false, ignore onetrack_pa pause event");
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "savePageEndData error:" + e.toString());
            if (e instanceof JSONException) {
                b.a(this.c.e.getAppId(), a.g, "bu", "", 1);
                ao aoVarJ = this.c.j();
                c cVar2 = this.c;
                c.a(aoVarJ, cVar2.e, cVar2.g, a.g, "bu", "");
            }
        }
    }
}

package com.xiaomi.onetrack.api;

import com.ot.pubsub.a.a;
import com.xiaomi.onetrack.h.d.b;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class p implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ boolean b;
    final /* synthetic */ c c;

    p(c cVar, String str, boolean z) {
        this.c = cVar;
        this.a = str;
        this.b = z;
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
                c cVar = this.c;
                this.c.j().a(a.g, am.a(str, a.g, cVar.e, jSONObjectG, this.b, cVar.g));
                if (com.xiaomi.onetrack.util.r.a) {
                    com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "trackPageStartAuto");
                    return;
                }
                return;
            }
            com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "config.autoTrackActivityAction is false, ignore onetrack_pa resume event");
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "auto trackPageStartAuto error: " + e.toString());
            if (e instanceof JSONException) {
                b.a(this.c.e.getAppId(), a.g, "bu", "", 1);
                ao aoVarJ = this.c.j();
                c cVar2 = this.c;
                c.a(aoVarJ, cVar2.e, cVar2.g, a.g, "bu", "");
            }
        }
    }
}

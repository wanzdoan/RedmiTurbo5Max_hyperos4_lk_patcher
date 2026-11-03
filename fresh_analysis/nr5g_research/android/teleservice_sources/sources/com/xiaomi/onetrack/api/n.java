package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.h.d.b;
import com.xiaomi.onetrack.util.ae;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class n implements Runnable {
    final /* synthetic */ c a;

    n(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            if (this.a.i() || ae.b(com.xiaomi.onetrack.util.ac.o())) {
                return;
            }
            b.a(this.a.e.getAppId(), "onetrack_dau", "tr", "", 1);
            com.xiaomi.onetrack.util.ac.g(System.currentTimeMillis());
            JSONObject jSONObjectG = this.a.g("onetrack_dau");
            c cVar = this.a;
            this.a.j().a("onetrack_dau", am.a(cVar.e, jSONObjectG, cVar.g));
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackDau error  e:" + e.toString());
            if (e instanceof JSONException) {
                b.a(this.a.e.getAppId(), "onetrack_dau", "bu", "", 1);
                ao aoVarJ = this.a.j();
                c cVar2 = this.a;
                c.a(aoVarJ, cVar2.e, cVar2.g, "onetrack_dau", "bu", "");
            }
        }
    }
}

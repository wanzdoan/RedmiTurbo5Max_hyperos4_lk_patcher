package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.h.d.b;
import com.xiaomi.onetrack.util.t;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class af implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ Map b;
    final /* synthetic */ c c;

    af(c cVar, String str, Map map) {
        this.c = cVar;
        this.a = str;
        this.b = map;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            if (this.c.i()) {
                return;
            }
            b.a(this.c.e.getAppId(), this.a, "tr", "", 1);
            if (this.c.e(this.a)) {
                b.a(this.c.e.getAppId(), this.a, "ch", "r1", 1);
                ao aoVarJ = this.c.j();
                c cVar = this.c;
                c.a(aoVarJ, cVar.e, cVar.g, this.a, "ch", "r1");
                return;
            }
            JSONObject jSONObjectA = t.a(this.b, true);
            Map map = this.b;
            if (map != null && jSONObjectA != null && map.size() != jSONObjectA.length()) {
                b.a(this.c.e.getAppId(), this.a, "ch", "r2", 1);
                ao aoVarJ2 = this.c.j();
                c cVar2 = this.c;
                c.a(aoVarJ2, cVar2.e, cVar2.g, this.a, "ch", "r2");
            }
            JSONObject jSONObjectG = this.c.g(this.a);
            String str = this.a;
            c cVar3 = this.c;
            this.c.j().a(this.a, am.a(str, jSONObjectA, cVar3.e, jSONObjectG, cVar3.g));
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "track map error: " + e.toString());
            if (e instanceof JSONException) {
                b.a(this.c.e.getAppId(), this.a, "bu", "", 1);
                ao aoVarJ3 = this.c.j();
                c cVar4 = this.c;
                c.a(aoVarJ3, cVar4.e, cVar4.g, this.a, "bu", "");
            }
        }
    }
}

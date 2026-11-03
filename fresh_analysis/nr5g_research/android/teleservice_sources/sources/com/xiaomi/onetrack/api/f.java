package com.xiaomi.onetrack.api;

import com.ot.pubsub.a.a;
import com.xiaomi.onetrack.h.d.b;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class f implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ String b;
    final /* synthetic */ Map c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ String g;
    final /* synthetic */ long h;
    final /* synthetic */ c i;

    f(c cVar, boolean z, String str, Map map, String str2, String str3, String str4, String str5, long j) {
        this.i = cVar;
        this.a = z;
        this.b = str;
        this.c = map;
        this.d = str2;
        this.e = str3;
        this.f = str4;
        this.g = str5;
        this.h = j;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            if (this.i.i()) {
                return;
            }
            b.a(this.i.e.getAppId(), a.f, "tr", "", 1);
            if (!this.a || Arrays.asList("java", "anr", "native").contains(this.b)) {
                JSONObject jSONObjectD = this.i.d(this.c);
                ao aoVarJ = this.i.j();
                String str = this.d;
                String str2 = this.e;
                String str3 = this.b;
                String str4 = this.f;
                String str5 = this.g;
                long j = this.h;
                c cVar = this.i;
                aoVarJ.a(a.f, am.a(str, str2, str3, str4, str5, j, cVar.e, jSONObjectD, cVar.g, this.a));
                return;
            }
            com.xiaomi.onetrack.util.r.c("BaseOneTrackImp", "track custom exception failed, crashType invalid!!!");
            b.a(this.i.e.getAppId(), a.f, "ch", "r2", 1);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackException error: " + e.toString());
            if (e instanceof JSONException) {
                b.a(this.i.e.getAppId(), a.f, "bu", "", 1);
                ao aoVarJ2 = this.i.j();
                c cVar2 = this.i;
                c.a(aoVarJ2, cVar2.e, cVar2.g, a.f, "bu", "");
            }
        }
    }
}

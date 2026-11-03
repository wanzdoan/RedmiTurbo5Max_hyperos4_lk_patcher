package com.xiaomi.onetrack.api;

import android.text.TextUtils;
import com.ot.pubsub.a.a;
import com.ot.pubsub.a.b;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
class r implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ c b;

    r(c cVar, boolean z) {
        this.b = cVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.b.i()) {
                return;
            }
            if (!this.b.e.isAutoTrackActivityAction()) {
                com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "config.autoTrackActivityAction is false, ignore onetrack_pa pause event");
                return;
            }
            String strU = com.xiaomi.onetrack.util.ac.u();
            if (TextUtils.isEmpty(strU)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(strU);
            this.b.j().a(a.g, jSONObject.put(b.a, jSONObject.optJSONObject(b.a).put(a.u, this.a)).toString());
            if (com.xiaomi.onetrack.util.r.a) {
                com.xiaomi.onetrack.util.r.a("BaseOneTrackImp", "trackPageEndAuto");
            }
            com.xiaomi.onetrack.util.ac.h("");
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("BaseOneTrackImp", "trackPageEndAuto error:" + e.toString());
        }
    }
}

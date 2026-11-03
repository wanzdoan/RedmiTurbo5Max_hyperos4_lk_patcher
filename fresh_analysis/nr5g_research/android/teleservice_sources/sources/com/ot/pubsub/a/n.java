package com.ot.pubsub.a;

import android.text.TextUtils;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class n implements Runnable {
    final /* synthetic */ Map a;
    final /* synthetic */ d b;

    n(d dVar, Map map) {
        this.b = dVar;
        this.a = map;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            JSONObject jSONObjectA = com.ot.pubsub.util.m.a(this.a, true);
            String strA = com.ot.pubsub.util.g.a(com.ot.pubsub.util.m.a(this.b.h));
            com.ot.pubsub.util.g.a(com.ot.pubsub.util.m.a(this.b.h), com.ot.pubsub.util.m.a(jSONObjectA, !TextUtils.isEmpty(strA) ? new JSONObject(strA) : null).toString());
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("PubSubTrackImp", " " + e.toString());
        }
    }
}

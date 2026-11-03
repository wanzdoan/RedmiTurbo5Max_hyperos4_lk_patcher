package com.ot.pubsub.a;

import android.text.TextUtils;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class p implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ d b;

    p(d dVar, String str) {
        this.b = dVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            String strA = com.ot.pubsub.util.g.a(com.ot.pubsub.util.m.a(this.b.h));
            if (TextUtils.isEmpty(strA)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(strA);
            jSONObject.remove(this.a);
            com.ot.pubsub.util.g.a(com.ot.pubsub.util.m.a(this.b.h), jSONObject.toString());
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("PubSubTrackImp", "removeCommonProperty error:" + e.toString());
        }
    }
}

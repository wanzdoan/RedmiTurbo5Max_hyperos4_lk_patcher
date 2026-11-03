package com.ot.pubsub.g;

import android.text.TextUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class g implements Runnable {
    final /* synthetic */ Map a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ com.ot.pubsub.h.a d;
    final /* synthetic */ f e;

    g(f fVar, Map map, String str, String str2, com.ot.pubsub.h.a aVar) {
        this.e = fVar;
        this.a = map;
        this.b = str;
        this.c = str2;
        this.d = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.a != null) {
                String strI = com.ot.pubsub.util.l.i();
                if (this.a.get(com.ot.pubsub.util.a.d) == null) {
                    this.a.put(com.ot.pubsub.util.a.d, strI);
                }
                if (this.a.get(com.ot.pubsub.util.a.e) == null && !TextUtils.isEmpty(strI) && com.ot.pubsub.util.a.h.contains(strI.toUpperCase())) {
                    this.a.put(com.ot.pubsub.util.a.e, com.ot.pubsub.util.a.f);
                }
            }
            this.e.b(this.b, this.c, this.d, this.a);
            com.ot.pubsub.j.d.a().a(this.d.e(), false);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("MessageOTManager", "EventManager.addTrackMessage exception: ", e);
        }
    }
}

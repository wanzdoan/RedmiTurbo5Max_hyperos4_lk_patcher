package com.ot.pubsub.g;

import android.text.TextUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class d implements Runnable {
    final /* synthetic */ Map a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ boolean e;
    final /* synthetic */ c f;

    d(c cVar, Map map, String str, String str2, String str3, boolean z) {
        this.f = cVar;
        this.a = map;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = z;
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
            this.f.b(this.b, this.c, this.d, this.a, this.e);
            com.ot.pubsub.j.d.a().a(false);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("MessageManager", "EventManager.addTrackMessage exception: ", e);
        }
    }
}

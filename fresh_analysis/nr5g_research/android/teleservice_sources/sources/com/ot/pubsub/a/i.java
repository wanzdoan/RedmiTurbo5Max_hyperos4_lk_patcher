package com.ot.pubsub.a;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class i implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ Map b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ Map e;
    final /* synthetic */ d f;

    i(d dVar, String str, Map map, String str2, String str3, Map map2) {
        this.f = dVar;
        this.a = str;
        this.b = map;
        this.c = str2;
        this.d = str3;
        this.e = map2;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        try {
            if (this.f.c(this.a)) {
                return;
            }
            this.f.d.a(this.c, this.d, this.a, b.a(this.a, com.ot.pubsub.util.m.a(this.b, true), this.f.h, this.f.j, this.f.d(this.a), this.f.i), this.e);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("PubSubTrackImp", "track map error: " + e.toString());
        }
    }
}

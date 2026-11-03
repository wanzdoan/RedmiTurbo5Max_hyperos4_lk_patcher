package com.ot.pubsub.a;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class h implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ Map d;
    final /* synthetic */ boolean e;
    final /* synthetic */ d f;

    h(d dVar, String str, String str2, String str3, Map map, boolean z) {
        this.f = dVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = map;
        this.e = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f.d.a(this.a, this.b, this.c, this.d, this.e);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("PubSubTrackImp", "publish pb data error: " + e.toString());
        }
    }
}

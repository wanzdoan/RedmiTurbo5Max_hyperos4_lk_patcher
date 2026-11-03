package com.ot.pubsub.a;

import com.ot.pubsub.util.t;

/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ d b;

    f(d dVar, boolean z) {
        this.b = dVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.b.i.a(this.a);
        t.a(com.ot.pubsub.util.m.a(this.b.h), this.a);
    }
}

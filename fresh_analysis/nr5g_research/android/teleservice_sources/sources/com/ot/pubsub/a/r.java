package com.ot.pubsub.a;

/* JADX INFO: loaded from: classes.dex */
class r implements Runnable {
    final /* synthetic */ q a;

    r(q qVar) {
        this.a = qVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (com.ot.pubsub.g.i.a()) {
            com.ot.pubsub.j.d.a().a(true);
            com.ot.pubsub.j.d.a().a(0, true);
            com.ot.pubsub.j.d.a().a(1, true);
        }
    }
}

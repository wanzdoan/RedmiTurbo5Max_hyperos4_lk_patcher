package com.ot.pubsub.a;

/* JADX INFO: loaded from: classes.dex */
class g implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ d b;

    g(d dVar, String str) {
        this.b = dVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            com.ot.pubsub.g.b.a().a(this.a);
        } catch (Exception e) {
            com.ot.pubsub.util.j.b("PubSubTrackImp", "setInstanceId error: " + e.toString());
        }
    }
}

package com.ot.pubsub.j;

/* JADX INFO: loaded from: classes.dex */
class i implements Runnable {
    final /* synthetic */ d a;

    i(d dVar) {
        this.a = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            com.ot.pubsub.b.e.b();
        } catch (Exception e) {
            com.ot.pubsub.util.j.a("UploaderEngine", "handleNetChange: " + e);
        }
    }
}

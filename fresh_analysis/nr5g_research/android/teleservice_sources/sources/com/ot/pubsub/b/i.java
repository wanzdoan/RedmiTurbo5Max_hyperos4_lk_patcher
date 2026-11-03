package com.ot.pubsub.b;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
class i implements Runnable {
    final /* synthetic */ ArrayList a;
    final /* synthetic */ h b;

    i(h hVar, ArrayList arrayList) {
        this.b = hVar;
        this.a = arrayList;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        if (com.ot.pubsub.util.j.a) {
            com.ot.pubsub.util.j.a("ConfigDbManager", "update: " + this.a);
        }
        this.b.b(this.a);
    }
}

package com.ot.pubsub.j;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
class j implements Runnable {
    final /* synthetic */ d.a a;

    j(d.a aVar) {
        this.a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        d.a().a(false);
        com.ot.pubsub.util.j.a("UploaderEngine", "Scheduled try track events cost: " + (SystemClock.elapsedRealtime() - SystemClock.elapsedRealtime()));
        if (com.ot.pubsub.g.c.a().e()) {
            return;
        }
        d.this.g.sendEmptyMessageDelayed(123, d.i);
    }
}

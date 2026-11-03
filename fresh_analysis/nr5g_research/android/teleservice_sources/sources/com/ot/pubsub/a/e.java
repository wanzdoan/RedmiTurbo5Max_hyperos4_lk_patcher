package com.ot.pubsub.a;

import android.text.TextUtils;
import com.ot.pubsub.util.t;

/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.ot.pubsub.util.j.a();
        com.ot.pubsub.e.a.a();
        com.ot.pubsub.b.a.a().a(this.a.h.getAppId());
        com.ot.pubsub.g.a.a();
        if (t.g() == 0) {
            t.c(System.currentTimeMillis());
        }
        if (!TextUtils.isEmpty(this.a.h.getInstanceId())) {
            com.ot.pubsub.g.b.a().a(this.a.h.getInstanceId());
        }
        com.ot.pubsub.j.d.a().c();
        com.ot.pubsub.g.i.b(false);
    }
}

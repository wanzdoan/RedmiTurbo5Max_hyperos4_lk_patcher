package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.c.a;

/* JADX INFO: loaded from: classes2.dex */
class ba implements Runnable {
    final /* synthetic */ Configuration a;
    final /* synthetic */ az b;

    ba(az azVar, Configuration configuration) {
        this.b = azVar;
        this.a = configuration;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.a();
        ah.a().d();
        ah.a().f();
        com.xiaomi.onetrack.b.q.a().a(this.a.getProjectId(), com.xiaomi.onetrack.util.a.a(this.a.getAppId()), this.a.getPrivateKeyId(), this.a.getAppId());
        com.xiaomi.onetrack.util.q.a().a(Boolean.FALSE);
        com.xiaomi.onetrack.c.n.b();
    }
}

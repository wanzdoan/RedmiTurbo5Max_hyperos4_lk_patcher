package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.a;

/* JADX INFO: loaded from: classes2.dex */
class ay implements Runnable {
    final /* synthetic */ Configuration a;
    final /* synthetic */ ax b;

    ay(ax axVar, Configuration configuration) {
        this.b = axVar;
        this.a = configuration;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.xiaomi.onetrack.b.q.a().a(this.a.getProjectId(), a.a(this.a.getAppId()), this.a.getPrivateKeyId(), this.a.getAppId());
        com.xiaomi.onetrack.util.q.a().a(Boolean.TRUE);
        if (OneTrack.isRestrictGetNetworkInfo()) {
            ah.a().f();
        }
    }
}

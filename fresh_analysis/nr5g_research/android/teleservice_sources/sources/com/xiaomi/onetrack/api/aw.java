package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.OneTrack;

/* JADX INFO: loaded from: classes2.dex */
class aw implements Runnable {
    final /* synthetic */ av a;

    aw(av avVar) {
        this.a = avVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.xiaomi.onetrack.util.q.a().a(Boolean.TRUE);
        if (OneTrack.isRestrictGetNetworkInfo()) {
            ah.a().f();
        }
    }
}

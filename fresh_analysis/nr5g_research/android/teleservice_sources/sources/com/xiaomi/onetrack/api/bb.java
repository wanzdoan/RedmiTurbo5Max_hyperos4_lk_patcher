package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.b.a;

/* JADX INFO: loaded from: classes2.dex */
class bb implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ az b;

    bb(az azVar, boolean z) {
        this.b = azVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.a) {
            a.a().c(this.b.e.getAdEventAppId());
        }
        a.a().c(this.b.e.getAppId());
    }
}

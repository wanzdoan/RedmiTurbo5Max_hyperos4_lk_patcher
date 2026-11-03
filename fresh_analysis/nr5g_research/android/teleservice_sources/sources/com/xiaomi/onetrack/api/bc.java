package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.c.j;

/* JADX INFO: loaded from: classes2.dex */
class bc implements Runnable {
    final /* synthetic */ az a;

    bc(az azVar) {
        this.a = azVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (j.b()) {
            com.xiaomi.onetrack.c.ah.a().a(0, true);
            com.xiaomi.onetrack.c.ah.a().a(1, true);
        }
    }
}

package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.a.c.b;
import com.xiaomi.onetrack.c.j;

/* JADX INFO: loaded from: classes2.dex */
class au implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ as b;

    au(as asVar, int i) {
        this.b = asVar;
        this.a = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (j.b() && this.a == 2) {
            com.xiaomi.onetrack.c.ah.a().a(0, true);
            com.xiaomi.onetrack.c.ah.a().a(1, true);
            b.a().b();
        }
    }
}

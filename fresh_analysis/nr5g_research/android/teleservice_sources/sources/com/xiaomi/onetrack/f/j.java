package com.xiaomi.onetrack.f;

import android.content.Context;
import com.xiaomi.onetrack.api.ah;
import com.xiaomi.onetrack.util.q;

/* JADX INFO: loaded from: classes2.dex */
class j implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ i b;

    j(i iVar, Context context) {
        this.b = iVar;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        q.a().a(Boolean.FALSE);
        com.xiaomi.onetrack.c.a.a();
        ah.a().d();
        ah.a().f();
        com.xiaomi.onetrack.c.d.a(this.a);
    }
}

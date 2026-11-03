package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.b.a;
import com.xiaomi.onetrack.b.e;

/* JADX INFO: loaded from: classes2.dex */
class at implements Runnable {
    final /* synthetic */ boolean a;
    final /* synthetic */ as b;

    at(as asVar, boolean z) {
        this.b = asVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.a) {
            e.a();
            a.a().c(this.b.e.getAdEventAppId());
        }
        a.a().c(this.b.e.getAppId());
    }
}

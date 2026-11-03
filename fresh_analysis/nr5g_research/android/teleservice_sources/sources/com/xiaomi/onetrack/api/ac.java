package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.h.a.a;

/* JADX INFO: loaded from: classes2.dex */
class ac implements Runnable {
    final /* synthetic */ c a;

    ac(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        a aVarA = a.a();
        String appId = this.a.e.getAppId();
        c cVar = this.a;
        aVarA.b(appId, cVar.g, cVar.a());
        this.a.r();
    }
}

package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.c.j;

/* JADX INFO: loaded from: classes2.dex */
class y implements Runnable {
    final /* synthetic */ c a;

    y(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        j.a(this.a.j());
    }
}

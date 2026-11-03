package com.xiaomi.onetrack.b;

import com.xiaomi.onetrack.util.r;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
class k implements Runnable {
    final /* synthetic */ ArrayList a;
    final /* synthetic */ j b;

    k(j jVar, ArrayList arrayList) {
        this.b = jVar;
        this.a = arrayList;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        if (r.a) {
            r.a("ConfigDbManager", "update: " + this.a);
        }
        this.b.b(this.a);
        this.b.c(this.a);
    }
}

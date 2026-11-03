package com.xiaomi.onetrack.a;

import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class d implements Runnable {
    final /* synthetic */ com.xiaomi.onetrack.f.b a;
    final /* synthetic */ c b;

    d(c cVar, com.xiaomi.onetrack.f.b bVar) {
        this.b = cVar;
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        List listA;
        com.xiaomi.onetrack.f.b bVar = this.a;
        if (!(bVar instanceof com.xiaomi.onetrack.a.b.b) || (listA = ((com.xiaomi.onetrack.a.b.b) bVar).a()) == null || listA.size() <= 0) {
            return;
        }
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            this.b.a((com.xiaomi.onetrack.a.b.a) it.next());
        }
    }
}

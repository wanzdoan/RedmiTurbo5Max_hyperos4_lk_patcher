package com.xiaomi.onetrack.b;

import java.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
class d implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ a.b b;

    d(a.b bVar, String str) {
        this.b = bVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.b(Arrays.asList(this.a));
        a.G.put(this.a, Boolean.FALSE);
    }
}

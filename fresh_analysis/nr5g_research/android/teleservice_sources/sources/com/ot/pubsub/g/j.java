package com.ot.pubsub.g;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class j implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ Map e;
    final /* synthetic */ boolean f;

    j(String str, String str2, String str3, String str4, Map map, boolean z) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = map;
        this.f = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        i.c(this.a, this.b, this.c, this.d, this.e, this.f);
        com.ot.pubsub.util.j.a("NetworkAccessManager", "save data successfully");
    }
}

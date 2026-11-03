package com.ot.pubsub.f;

import com.ot.pubsub.g.f;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ Map g;

    c(String str, String str2, String str3, String str4, String str5, String str6, Map map) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
        this.g = map;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        f.a().a(this.e, this.f, a.a(this.a, this.b, this.c, this.d), this.g);
    }
}

package com.xiaomi.onetrack.f;

import com.xiaomi.onetrack.c.n;
import com.xiaomi.onetrack.e.c;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class h implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ Map g;

    h(String str, String str2, String str3, String str4, String str5, String str6, Map map) {
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
        n.a().a(this.e, this.f, c.b(this.a, this.b, this.c, this.d), this.g, true);
    }
}

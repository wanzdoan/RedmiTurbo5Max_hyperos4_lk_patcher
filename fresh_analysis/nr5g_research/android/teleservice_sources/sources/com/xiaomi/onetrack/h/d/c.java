package com.xiaomi.onetrack.h.d;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.ae;

/* JADX INFO: loaded from: classes2.dex */
class c implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ int d;
    final /* synthetic */ String e;

    c(String str, String str2, String str3, int i, String str4) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = i;
        this.e = str4;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.xiaomi.onetrack.h.b.b.a().a(this.a, ae.a(0), this.b, this.c, this.d);
        if ("bu".equals(this.b) || !(TextUtils.isEmpty(this.c) || "su".equals(this.c))) {
            com.xiaomi.onetrack.h.b.b.a().a(this.a, ae.a(0), this.e, this.d);
        }
    }
}

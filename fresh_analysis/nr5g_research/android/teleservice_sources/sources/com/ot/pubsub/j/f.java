package com.ot.pubsub.j;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ e b;

    f(e eVar, String str) {
        this.b = eVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (TextUtils.equals(this.a, "android.intent.action.SCREEN_OFF")) {
            this.b.a.l();
            d.a().a(false);
            d.a().a(2, false);
        } else if (TextUtils.equals(this.a, "android.intent.action.SCREEN_ON")) {
            this.b.a.k();
            d.a().a(false);
            d.a().a(0, false);
        }
    }
}

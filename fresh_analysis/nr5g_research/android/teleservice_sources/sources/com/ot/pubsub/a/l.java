package com.ot.pubsub.a;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
class l implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ k b;

    l(k kVar, String str) {
        this.b = kVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (TextUtils.equals(this.a, "android.intent.action.SCREEN_OFF") || TextUtils.equals(this.a, "android.intent.action.SCREEN_ON")) {
            this.b.a.e();
        }
    }
}

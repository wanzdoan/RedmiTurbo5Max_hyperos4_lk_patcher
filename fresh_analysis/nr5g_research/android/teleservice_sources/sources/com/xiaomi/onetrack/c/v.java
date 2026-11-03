package com.xiaomi.onetrack.c;

/* JADX INFO: loaded from: classes2.dex */
class v implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ s b;

    v(s sVar, String str) {
        this.b = sVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (j.b()) {
            this.b.a((String) null, (String) null, (String) null, this.a, false);
        }
    }
}

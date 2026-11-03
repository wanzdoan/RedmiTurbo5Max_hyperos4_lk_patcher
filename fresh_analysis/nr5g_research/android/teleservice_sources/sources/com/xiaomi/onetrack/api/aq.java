package com.xiaomi.onetrack.api;

/* JADX INFO: loaded from: classes2.dex */
class aq implements Runnable {
    final /* synthetic */ Thread a;
    final /* synthetic */ Throwable b;
    final /* synthetic */ ap c;

    aq(ap apVar, Thread thread, Throwable th) {
        this.c = apVar;
        this.a = thread;
        this.b = th;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        this.c.a(this.a, this.b);
    }
}
